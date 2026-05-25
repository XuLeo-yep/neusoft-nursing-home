<template>
  <div class="page-container">
    <h2 class="page-title">护理记录</h2>
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="客户姓名"><el-input v-model="searchForm.name" placeholder="请输入客户姓名" clearable style="width: 200px" /></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch"><el-icon><Search /></el-icon> 查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="never"><template #header><span>客户列表</span></template>
          <el-table :data="customerList" size="small" highlight-current-row @row-click="handleSelectCustomer" style="cursor: pointer;">
            <el-table-column prop="username" label="姓名" />
            <el-table-column prop="id" label="ID" width="60" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="never">
          <template #header><span>{{ currentCustomer ? currentCustomer.username + ' 的护理记录' : '请选择客户' }}</span></template>
          <el-table :data="recordList" size="small" v-loading="recordLoading">
            <el-table-column type="index" label="序号" width="50" align="center" />
            <el-table-column prop="projectId" label="项目ID" width="80" align="center" />
            <el-table-column prop="nursingDate" label="护理时间" width="160" />
            <el-table-column prop="nursingQuanity" label="护理数量" width="90" align="center" />
            <el-table-column label="操作" width="80">
              <template #default="{ row }"><el-button type="danger" size="small" text @click="handleDelete(row)">移除</el-button></template>
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
import request from '@/utils/request'

const loading = ref(false)
const recordLoading = ref(false)
const searchForm = reactive({ name: '' })
const currentCustomer = ref(null)
const customerList = ref([])
const recordList = ref([])

const loadCustomers = async () => {
  loading.value = true
  try {
    const res = await request.get('/customer/list', { params: { name: searchForm.name || undefined } })
    customerList.value = res.data || []
  } catch (e) {}
  loading.value = false
}

const handleSearch = () => loadCustomers()
const handleReset = () => { searchForm.name = ''; loadCustomers() }

const handleSelectCustomer = async (row) => {
  currentCustomer.value = row
  recordLoading.value = true
  try {
    const res = await request.get('/nursing/record/list/' + row.id)
    recordList.value = res.data || []
  } catch (e) {}
  recordLoading.value = false
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认移除该护理记录吗？', '提示', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { await request.delete('/nursing/record/' + row.id); ElMessage.success('移除成功'); handleSelectCustomer(currentCustomer.value) }).catch(() => {})
}

onMounted(() => { loadCustomers() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.search-card { margin-bottom: 15px; }
</style>
