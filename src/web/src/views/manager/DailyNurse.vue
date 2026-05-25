<template>
  <div class="page-container">
    <h2 class="page-title">日常护理</h2>
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
        <el-card shadow="never" v-if="currentCustomer">
          <template #header><span>{{ currentCustomer.username }} 的护理项目</span></template>
          <el-table :data="projectList" size="small">
            <el-table-column prop="projectId" label="项目ID" width="80" align="center" />
            <el-table-column prop="quanity" label="总数量" width="90" align="center" />
            <el-table-column prop="remainingQuanity" label="剩余" width="90" align="center">
              <template #default="{ row }"><el-tag :type="row.remainingQuanity > 0 ? 'success' : 'danger'" size="small">{{ row.remainingQuanity }}</el-tag></template>
            </el-table-column>
            <el-table-column prop="expireDate" label="到期日期" width="120" />
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button v-if="row.remainingQuanity > 0" type="primary" size="small" text @click="handleNurse(row)">护理</el-button>
                <span v-else style="color: #909399; font-size: 12px;">已用完</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        <el-card v-else shadow="never"><el-empty description="请选择左侧客户" /></el-card>
      </el-col>
    </el-row>
    <el-dialog v-model="nurseVisible" title="添加护理记录" width="500px">
      <el-form :model="nurseForm" label-width="100px">
        <el-form-item label="客户"><span>{{ nurseForm.customerName }}</span></el-form-item>
        <el-form-item label="护理项目"><span>{{ nurseForm.projectName }}</span></el-form-item>
        <el-form-item label="护理数量"><el-input-number v-model="nurseForm.quantity" :min="1" :max="nurseForm.maxQuantity" style="width: 100%" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="nurseVisible = false">取消</el-button><el-button type="primary" @click="handleNurseSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const searchForm = reactive({ name: '' })
const currentCustomer = ref(null)
const nurseVisible = ref(false)
const nurseForm = reactive({ customerName: '', projectId: null, quantity: 1, maxQuantity: 1, customerId: null })
const customerList = ref([])
const projectList = ref([])

const loadCustomers = async () => {
  try {
    const res = await request.get('/customer/list')
    let list = res.data || []
    if (searchForm.name) list = list.filter(c => c.username?.includes(searchForm.name))
    customerList.value = list
  } catch (e) {}
}

const handleSearch = () => loadCustomers()
const handleReset = () => { searchForm.name = ''; loadCustomers() }

const handleSelectCustomer = async (row) => {
  currentCustomer.value = row
  try {
    const res = await request.get('/nursing/record/list/' + row.id)
    projectList.value = (await request.get('/nursing/customer/' + row.id + '/project').catch(() => ({ data: [] }))).data || []
  } catch (e) { projectList.value = [] }
}

const handleNurse = (row) => {
  nurseForm.customerName = currentCustomer.value.username
  nurseForm.projectId = row.projectId
  nurseForm.quantity = 1
  nurseForm.maxQuantity = row.remainingQuanity
  nurseForm.customerId = currentCustomer.value.id
  nurseVisible.value = true
}

const handleNurseSubmit = async () => {
  try {
    await request.post('/nursing/record/add', { customerId: nurseForm.customerId, projectId: nurseForm.projectId, quantity: nurseForm.quantity })
    ElMessage.success('护理记录添加成功'); nurseVisible.value = false; handleSelectCustomer(currentCustomer.value)
  } catch (e) {}
}

onMounted(() => { loadCustomers() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.search-card { margin-bottom: 15px; }
</style>
