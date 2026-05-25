<template>
  <div class="page-container">
    <h2 class="page-title">床位管理</h2>

    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.name" placeholder="请输入客户姓名" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="入住日期">
          <el-date-picker v-model="searchForm.checkInDate" type="date" placeholder="选择日期" style="width: 180px" />
        </el-form-item>
        <el-form-item label="使用状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 150px">
            <el-option label="正在使用" value="正在使用" />
            <el-option label="使用历史" value="使用历史" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon> 查询
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card" shadow="never">
      <el-table :data="bedList" stripe style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="customerName" label="客户姓名" min-width="100" />
        <el-table-column prop="building" label="楼栋" width="80" align="center" />
        <el-table-column prop="roomNo" label="房间号" width="100" align="center" />
        <el-table-column prop="bedNo" label="床位号" width="100" align="center" />
        <el-table-column prop="checkInDate" label="入住日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120">
          <template #default="{ row }">
            <span v-if="row.endDate">{{ row.endDate }}</span>
            <span v-else style="color: #909399;">—</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="使用状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '正在使用' ? 'success' : 'info'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === '正在使用'" type="primary" size="small" text @click="handleEdit(row)">修改</el-button>
            <el-button v-if="row.status === '正在使用'" type="warning" size="small" text @click="handleTransfer(row)">调换</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog v-model="editVisible" title="修改床位详情" width="400px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="客户姓名"><span>{{ editForm.customerName }}</span></el-form-item>
        <el-form-item label="当前床位"><span>{{ editForm.building }} {{ editForm.roomNo }} {{ editForm.bedNo }}</span></el-form-item>
        <el-form-item label="入住日期"><span>{{ editForm.checkInDate }}</span></el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="editForm.endDate" type="date" placeholder="选择结束日期" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEditSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="transferVisible" title="床位调换" width="500px">
      <el-form :model="transferForm" label-width="100px">
        <el-form-item label="客户姓名"><span>{{ transferForm.customerName }}</span></el-form-item>
        <el-form-item label="当前床位"><span>{{ transferForm.building }} {{ transferForm.roomNo }} {{ transferForm.bedNo }}</span></el-form-item>
        <el-form-item label="新楼栋"><el-input v-model="transferForm.newBuilding" disabled /></el-form-item>
        <el-form-item label="新房间号">
          <el-select v-model="transferForm.newRoomNo" placeholder="选择房间" style="width: 100%" @change="handleNewRoomChange">
            <el-option-group v-for="floor in floorList" :key="floor.label" :label="floor.label">
              <el-option v-for="room in floor.rooms" :key="room" :label="room" :value="room" />
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="新床位号">
          <el-select v-model="transferForm.newBedNo" placeholder="选择床位" style="width: 100%" :disabled="!transferForm.newRoomNo">
            <el-option v-for="bed in newAvailableBeds" :key="bed" :label="bed" :value="bed" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="transferVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTransferSubmit">确定调换</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const editVisible = ref(false)
const transferVisible = ref(false)

const searchForm = reactive({ name: '', checkInDate: '', status: '正在使用' })
const editForm = reactive({ id: null, customerName: '', building: '', roomNo: '', bedNo: '', checkInDate: '', endDate: '' })
const transferForm = reactive({ id: null, customerName: '', building: '', roomNo: '', bedNo: '', newBuilding: '606', newRoomNo: '', newBedNo: '' })

const floorList = [
  { label: '一层', rooms: ['A101', 'A102', 'A103', 'A104'] },
  { label: '二层', rooms: ['B201', 'B202', 'B203', 'B204'] },
  { label: '三层', rooms: ['C301', 'C302', 'C303', 'C304'] }
]

const bedList = ref([])
const allRecords = ref([
  { id: 1, customerName: '张三', building: '606', roomNo: 'A101', bedNo: '01', checkInDate: '2025-03-15', endDate: '', status: '正在使用' },
  { id: 2, customerName: '李四', building: '606', roomNo: 'A101', bedNo: '02', checkInDate: '2025-01-20', endDate: '', status: '正在使用' },
  { id: 3, customerName: '王五', building: '606', roomNo: 'B202', bedNo: '01', checkInDate: '2025-06-01', endDate: '', status: '正在使用' },
  { id: 4, customerName: '赵六', building: '606', roomNo: 'B203', bedNo: '02', checkInDate: '2025-02-10', endDate: '', status: '正在使用' },
  { id: 5, customerName: '张三', building: '606', roomNo: 'B201', bedNo: '03', checkInDate: '2024-01-01', endDate: '2025-03-14', status: '使用历史' }
])

const newAvailableBeds = computed(() => {
  if (!transferForm.newRoomNo) return []
  const used = allRecords.value.filter(r => r.roomNo === transferForm.newRoomNo && r.status === '正在使用').map(r => r.bedNo)
  const map = { 'A101': ['01','02','03'], 'A102': ['01','02'], 'A103': ['01','02','03','04'], 'A104': ['01','02'], 'B201': ['01','02'], 'B202': ['01','02','03'], 'B203': ['01','02'], 'B204': ['01','02','03','04'], 'C301': ['01','02','03'], 'C302': ['01','02'], 'C303': ['01','02','03','04'], 'C304': ['01','02'] }
  return (map[transferForm.newRoomNo] || []).filter(b => !used.includes(b))
})

const loadData = () => {
  loading.value = true
  setTimeout(() => {
    let list = [...allRecords.value]
    if (searchForm.name) list = list.filter(r => r.customerName.includes(searchForm.name))
    if (searchForm.checkInDate) list = list.filter(r => r.checkInDate === searchForm.checkInDate)
    if (searchForm.status) list = list.filter(r => r.status === searchForm.status)
    total.value = list.length
    const start = (pageNum.value - 1) * pageSize.value
    bedList.value = list.slice(start, start + pageSize.value)
    loading.value = false
  }, 300)
}

const handleSearch = () => { pageNum.value = 1; loadData() }
const handleReset = () => { searchForm.name = ''; searchForm.checkInDate = ''; searchForm.status = '正在使用'; pageNum.value = 1; loadData() }
const handleSizeChange = (val) => { pageSize.value = val; loadData() }
const handleCurrentChange = (val) => { pageNum.value = val; loadData() }

const handleEdit = (row) => {
  Object.assign(editForm, row)
  editVisible.value = true
}

const handleEditSubmit = () => {
  const index = allRecords.value.findIndex(r => r.id === editForm.id)
  if (index !== -1) {
    allRecords.value[index].endDate = editForm.endDate
    ElMessage.success('修改成功')
  }
  editVisible.value = false; loadData()
}

const handleTransfer = (row) => {
  Object.assign(transferForm, { id: row.id, customerName: row.customerName, building: row.building, roomNo: row.roomNo, bedNo: row.bedNo, newBuilding: '606', newRoomNo: '', newBedNo: '' })
  transferVisible.value = true
}

const handleNewRoomChange = () => { transferForm.newBedNo = '' }

const handleTransferSubmit = () => {
  if (!transferForm.newRoomNo || !transferForm.newBedNo) {
    ElMessage.warning('请选择新房间和新床位')
    return
  }
  const today = new Date().toISOString().split('T')[0]
  const idx = allRecords.value.findIndex(r => r.id === transferForm.id)
  if (idx !== -1) {
    allRecords.value[idx].status = '使用历史'
    allRecords.value[idx].endDate = today
    allRecords.value.push({
      id: Date.now(),
      customerName: transferForm.customerName,
      building: '606',
      roomNo: transferForm.newRoomNo,
      bedNo: transferForm.newBedNo,
      checkInDate: today,
      endDate: '',
      status: '正在使用'
    })
    ElMessage.success('床位调换成功')
  }
  transferVisible.value = false; loadData()
}

onMounted(() => { loadData() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.search-card { margin-bottom: 15px; }
.table-card { min-height: 500px; }
</style>
