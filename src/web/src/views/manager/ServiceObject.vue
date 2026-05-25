<template>
  <div class="page-container">
    <h2 class="page-title">设置服务对象</h2>
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="管家姓名"><el-input v-model="searchForm.name" placeholder="请输入管家姓名" clearable style="width: 200px" /></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch"><el-icon><Search /></el-icon> 查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-row :gutter="20">
      <el-col :span="10">
        <el-card shadow="never"><template #header><span>健康管家列表</span></template>
          <el-table :data="managerList" size="small" highlight-current-row @row-click="handleSelectManager" style="cursor: pointer;">
            <el-table-column prop="username" label="姓名" />
            <el-table-column prop="phone" label="电话" min-width="120" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <template #header><span>{{ currentManager ? currentManager.username + ' 的服务对象' : '请选择管家' }}</span></template>
          <el-table :data="serviceList" size="small" v-loading="serviceLoading">
            <el-table-column type="index" label="序号" width="50" align="center" />
            <el-table-column prop="username" label="客户姓名" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'

const searchForm = reactive({ name: '' })
const currentManager = ref(null)
const serviceLoading = ref(false)
const managerList = ref([])
const serviceList = ref([])

const loadManagers = async () => {
  try {
    const res = await request.get('/user/list')
    let list = (res.data || []).filter(u => u.role === 'health_worker')
    if (searchForm.name) list = list.filter(m => m.username?.includes(searchForm.name))
    managerList.value = list
  } catch (e) {}
}

const handleSearch = () => loadManagers()
const handleReset = () => { searchForm.name = ''; loadManagers() }

const handleSelectManager = async (row) => {
  currentManager.value = row
  serviceLoading.value = true
  try {
    const res = await request.get('/customer/list')
    const workerIds = row.servedCustomerIds ? JSON.parse(row.servedCustomerIds || '[]') : []
    serviceList.value = (res.data || []).filter(c => workerIds.includes(String(c.id)))
  } catch (e) {}
  serviceLoading.value = false
}

onMounted(() => { loadManagers() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.search-card { margin-bottom: 15px; }
</style>
