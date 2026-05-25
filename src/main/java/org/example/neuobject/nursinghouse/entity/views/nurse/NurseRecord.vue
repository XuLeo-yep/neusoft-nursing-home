<template>
  <div class="page-container">
    <h2 class="page-title">护理记录</h2>

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

    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="never">
          <template #header><span>客户列表</span></template>
          <el-table :data="customerList" size="small" highlight-current-row @row-click="handleSelectCustomer" style="cursor: pointer;">
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="roomNo" label="房间" width="80" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="never">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>{{ currentCustomer ? currentCustomer.name + ' 的护理记录' : '请选择客户' }}</span>
            </div>
          </template>
          <el-table :data="recordList" size="small" v-loading="recordLoading">
            <el-table-column type="index" label="序号" width="50" align="center" />
            <el-table-column prop="projectName" label="护理项目" min-width="120" />
            <el-table-column prop="nurseTime" label="护理时间" width="160" />
            <el-table-column prop="quantity" label="护理数量" width="90" align="center" />
            <el-table-column prop="nurseName" label="护理人" width="100" />
            <el-table-column prop="remark" label="备注" min-width="120" show-overflow-tooltip />
            <el-table-column label="操作" width="80">
              <template #default="{ row }">
                <el-button type="danger" size="small" text @click="handleDelete(row)">移除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const recordLoading = ref(false)
const searchForm = reactive({ name: '' })
const currentCustomer = ref(null)

const customerList = ref([])
const allCustomers = ref([
  { id: 1, name: '张三', roomNo: 'A101' },
  { id: 2, name: '李四', roomNo: 'A101' },
  { id: 3, name: '王五', roomNo: 'B202' },
  { id: 4, name: '赵六', roomNo: 'B203' },
  { id: 5, name: '孙七', roomNo: 'C301' }
])

const recordList = ref([])
const allRecords = ref([
  { id: 1, customerId: 1, projectName: '日常护理', nurseTime: '2026-05-24 08:30', quantity: 1, nurseName: '护工A', remark: '晨间护理' },
  { id: 2, customerId: 1, projectName: '康复训练', nurseTime: '2026-05-24 10:00', quantity: 1, nurseName: '护工A', remark: '腿部康复' },
  { id: 3, customerId: 1, projectName: '日常护理', nurseTime: '2026-05-24 14:00', quantity: 1, nurseName: '护工B', remark: '午间护理' },
  { id: 4, customerId: 2, projectName: '心理疏导', nurseTime: '2026-05-24 09:00', quantity: 1, nurseName: '护工C', remark: '定期心理疏导' },
  { id: 5, customerId: 3, projectName: '饮食指导', nurseTime: '2026-05-24 07:30', quantity: 1, nurseName: '护工A', remark: '早餐指导' }
])

const loadCustomers = () => {
  loading.value = true
  setTimeout(() => {
    let list = [...allCustomers.value]
    if (searchForm.name) list = list.filter(c => c.name.includes(searchForm.name))
    customerList.value = list
    loading.value = false
  }, 200)
}

const handleSearch = () => { loadCustomers() }
const handleReset = () => { searchForm.name = ''; loadCustomers() }

const handleSelectCustomer = (row) => {
  currentCustomer.value = row
  recordLoading.value = true
  setTimeout(() => {
    recordList.value = allRecords.value.filter(r => r.customerId === row.id)
    recordLoading.value = false
  }, 200)
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认移除该护理记录吗？', '提示', {
    confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'
  }).then(() => {
    const idx = allRecords.value.findIndex(r => r.id === row.id)
    if (idx !== -1) { allRecords.value.splice(idx, 1); ElMessage.success('移除成功') }
    if (currentCustomer.value) { handleSelectCustomer(currentCustomer.value) }
  }).catch(() => {})
}

onMounted(() => { loadCustomers() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.search-card { margin-bottom: 15px; }
</style>
