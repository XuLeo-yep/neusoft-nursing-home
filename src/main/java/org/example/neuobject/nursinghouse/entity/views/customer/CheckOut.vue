<template>
  <div class="page-container">
    <h2 class="page-title">退住登记</h2>

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
      <el-table :data="checkOutList" stripe style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="customerName" label="客户姓名" min-width="100" />
        <el-table-column prop="type" label="退住类型" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="row.type === '死亡退住' ? 'danger' : row.type === '正常退住' ? 'success' : 'info'">{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="退住原因" min-width="150" show-overflow-tooltip />
        <el-table-column prop="checkOutDate" label="退住时间" width="120" />
        <el-table-column prop="status" label="审批状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '通过' ? 'success' : row.status === '不通过' ? 'danger' : 'warning'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="approver" label="审批人" width="100" />
        <el-table-column prop="approveTime" label="审批时间" width="160" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === '已提交'" type="primary" size="small" text @click="handleAudit(row)">审核</el-button>
            <span v-else style="color: #909399; font-size: 12px;">已审核</span>
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

    <el-dialog v-model="auditVisible" title="退住审核" width="500px">
      <el-form :model="auditForm" label-width="100px">
        <el-form-item label="客户姓名">
          <span>{{ auditForm.customerName }}</span>
        </el-form-item>
        <el-form-item label="退住类型">
          <el-tag :type="auditForm.type === '死亡退住' ? 'danger' : auditForm.type === '正常退住' ? 'success' : 'info'">{{ auditForm.type }}</el-tag>
        </el-form-item>
        <el-form-item label="退住原因">
          <span>{{ auditForm.reason }}</span>
        </el-form-item>
        <el-form-item label="审核结果" prop="result">
          <el-radio-group v-model="auditForm.result">
            <el-radio label="通过">通过</el-radio>
            <el-radio label="不通过">不通过</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAuditSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const auditVisible = ref(false)
const auditForm = reactive({
  id: null,
  customerName: '',
  type: '',
  reason: '',
  result: '通过'
})

const searchForm = reactive({ name: '' })

const checkOutList = ref([])
const allRecords = ref([
  { id: 1, customerName: '张三', type: '正常退住', reason: '家属要求接回', checkOutDate: '2026-05-20', status: '已提交', approver: '', approveTime: '' },
  { id: 2, customerName: '李四', type: '正常退住', reason: '合同到期', checkOutDate: '2026-05-18', status: '通过', approver: 'admin', approveTime: '2026-05-18 10:30:00' },
  { id: 3, customerName: '王五', type: '保留床位', reason: '短期住院', checkOutDate: '2026-05-15', status: '通过', approver: 'admin', approveTime: '2026-05-15 09:00:00' },
  { id: 4, customerName: '赵六', type: '正常退住', reason: '转院治疗', checkOutDate: '2026-05-22', status: '已提交', approver: '', approveTime: '' },
  { id: 5, customerName: '孙七', type: '死亡退住', reason: '自然死亡', checkOutDate: '2026-05-10', status: '通过', approver: 'admin1', approveTime: '2026-05-10 14:20:00' }
])

const loadData = () => {
  loading.value = true
  setTimeout(() => {
    let list = [...allRecords.value]
    if (searchForm.name) {
      list = list.filter(r => r.customerName.includes(searchForm.name))
    }
    total.value = list.length
    const start = (pageNum.value - 1) * pageSize.value
    checkOutList.value = list.slice(start, start + pageSize.value)
    loading.value = false
  }, 300)
}

const handleSearch = () => { pageNum.value = 1; loadData() }
const handleReset = () => { searchForm.name = ''; pageNum.value = 1; loadData() }
const handleSizeChange = (val) => { pageSize.value = val; loadData() }
const handleCurrentChange = (val) => { pageNum.value = val; loadData() }

const handleAudit = (row) => {
  auditForm.id = row.id
  auditForm.customerName = row.customerName
  auditForm.type = row.type
  auditForm.reason = row.reason
  auditForm.result = '通过'
  auditVisible.value = true
}

const handleAuditSubmit = () => {
  const index = allRecords.value.findIndex(r => r.id === auditForm.id)
  if (index !== -1) {
    allRecords.value[index].status = auditForm.result
    allRecords.value[index].approver = localStorage.getItem('username') || 'admin'
    allRecords.value[index].approveTime = new Date().toLocaleString()
    ElMessage.success(`审核${auditForm.result === '通过' ? '通过' : '不通过'}`)
  }
  auditVisible.value = false
  loadData()
}

onMounted(() => { loadData() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.search-card { margin-bottom: 15px; }
.table-card { min-height: 500px; }
</style>
