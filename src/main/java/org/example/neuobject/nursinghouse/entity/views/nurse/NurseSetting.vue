<template>
  <div class="page-container">
    <h2 class="page-title">客户护理设置</h2>

    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.name" placeholder="请输入客户姓名" clearable style="width: 200px" />
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
      <el-table :data="customerList" stripe style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="name" label="客户姓名" min-width="100" />
        <el-table-column prop="age" label="年龄" width="80" align="center" />
        <el-table-column prop="roomNo" label="房间号" width="100" align="center" />
        <el-table-column prop="bedNo" label="床位号" width="100" align="center" />
        <el-table-column prop="nurseLevel" label="护理级别" width="120" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.nurseLevel" type="success">{{ row.nurseLevel }}</el-tag>
            <span v-else style="color: #909399;">未设置</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button v-if="!row.nurseLevel" type="primary" size="small" text @click="handleSet(row)">设置级别</el-button>
            <el-button v-else type="danger" size="small" text @click="handleRemove(row)">移除级别</el-button>
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

    <el-dialog v-model="setVisible" title="设置护理级别" width="600px">
      <el-form :model="setForm" label-width="100px">
        <el-form-item label="客户姓名"><span>{{ setForm.customerName }}</span></el-form-item>
        <el-form-item label="护理级别" prop="level">
          <el-select v-model="setForm.level" placeholder="选择护理级别" style="width: 100%" @change="handleLevelChange">
            <el-option v-for="level in levelOptions" :key="level.id" :label="level.name" :value="level.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="包含项目" v-if="setForm.level">
          <el-table :data="levelProjects" size="small" border>
            <el-table-column prop="name" label="项目名称" />
            <el-table-column prop="price" label="价格" width="100" align="right" />
            <el-table-column prop="cycle" label="周期" width="100" />
          </el-table>
        </el-form-item>
        <el-form-item label="服务到期">
          <el-date-picker v-model="setForm.expireDate" type="date" placeholder="选择到期日期" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="setVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSetSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const setVisible = ref(false)
const searchForm = reactive({ name: '' })
const setForm = reactive({ customerId: null, customerName: '', level: '', expireDate: '' })

const levelOptions = ref([
  { id: 1, name: '一级护理', projects: [1, 2, 3] },
  { id: 2, name: '二级护理', projects: [2, 4, 6] },
  { id: 3, name: '三级护理', projects: [1, 3, 4, 7] }
])

const allProjects = ref([
  { id: 1, name: '日常护理', price: 100, cycle: '每日' },
  { id: 2, name: '康复训练', price: 200, cycle: '每日' },
  { id: 3, name: '心理疏导', price: 150, cycle: '每周' },
  { id: 4, name: '饮食指导', price: 80, cycle: '每日' },
  { id: 6, name: '睡眠监测', price: 120, cycle: '每日' },
  { id: 7, name: '运动指导', price: 100, cycle: '每日' }
])

const levelProjects = computed(() => {
  const level = levelOptions.value.find(l => l.name === setForm.level)
  if (!level) return []
  return allProjects.value.filter(p => level.projects.includes(p.id))
})

const customerList = ref([])
const allCustomers = ref([
  { id: 1, name: '张三', age: 78, roomNo: 'A101', bedNo: '01', nurseLevel: '一级护理' },
  { id: 2, name: '李四', age: 82, roomNo: 'A101', bedNo: '02', nurseLevel: '二级护理' },
  { id: 3, name: '王五', age: 75, roomNo: 'B202', bedNo: '01', nurseLevel: '' },
  { id: 4, name: '赵六', age: 80, roomNo: 'B203', bedNo: '02', nurseLevel: '三级护理' },
  { id: 5, name: '孙七', age: 73, roomNo: 'C301', bedNo: '03', nurseLevel: '' }
])

const loadData = () => {
  loading.value = true
  setTimeout(() => {
    let list = [...allCustomers.value]
    if (searchForm.name) list = list.filter(c => c.name.includes(searchForm.name))
    total.value = list.length
    const start = (pageNum.value - 1) * pageSize.value
    customerList.value = list.slice(start, start + pageSize.value)
    loading.value = false
  }, 300)
}

const handleSearch = () => { pageNum.value = 1; loadData() }
const handleReset = () => { searchForm.name = ''; pageNum.value = 1; loadData() }
const handleSizeChange = (val) => { pageSize.value = val; loadData() }
const handleCurrentChange = (val) => { pageNum.value = val; loadData() }

const handleSet = (row) => {
  setForm.customerId = row.id; setForm.customerName = row.name
  setForm.level = ''; setForm.expireDate = ''
  setVisible.value = true
}

const handleLevelChange = () => {
  const d = new Date(); d.setMonth(d.getMonth() + 3)
  setForm.expireDate = d.toISOString().split('T')[0]
}

const handleSetSubmit = () => {
  if (!setForm.level) { ElMessage.warning('请选择护理级别'); return }
  const idx = allCustomers.value.findIndex(c => c.id === setForm.customerId)
  if (idx !== -1) {
    allCustomers.value[idx].nurseLevel = setForm.level
    ElMessage.success('设置成功')
  }
  setVisible.value = false; loadData()
}

const handleRemove = (row) => {
  ElMessageBox.confirm(`确认移除客户【${row.name}】的护理级别吗？`, '提示', {
    confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'
  }).then(() => {
    const idx = allCustomers.value.findIndex(c => c.id === row.id)
    if (idx !== -1) { allCustomers.value[idx].nurseLevel = ''; ElMessage.success('移除成功') }
    loadData()
  }).catch(() => {})
}

onMounted(() => { loadData() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.search-card { margin-bottom: 15px; }
.table-card { min-height: 500px; }
</style>
