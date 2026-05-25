<template>
  <div class="page-container">
    <h2 class="page-title">床位管理</h2>
    <el-card class="table-card" shadow="never">
      <el-table :data="historyList" stripe style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="customerId" label="客户ID" min-width="100" />
        <el-table-column prop="bedId" label="床位号" width="120" align="center" />
        <el-table-column prop="startDate" label="入住日期" width="160" />
        <el-table-column prop="endDate" label="结束日期" width="160">
          <template #default="{ row }"><span v-if="row.endDate">{{ row.endDate }}</span><span v-else style="color: #909399;">—</span></template>
        </el-table-column>
        <el-table-column prop="status" label="使用状态" width="100" align="center">
          <template #default="{ row }"><el-tag :type="row.status === '在住' ? 'success' : 'info'">{{ row.status }}</el-tag></template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[10, 20, 50]" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
    </el-card>
    <el-card style="margin-top: 20px;">
      <template #header><span>床位调换</span></template>
      <el-form :model="transferForm" inline>
        <el-form-item label="客户ID"><el-input-number v-model="transferForm.customerId" style="width: 150px" /></el-form-item>
        <el-form-item label="房间号"><el-input v-model="transferForm.roomNumber" placeholder="如 101" style="width: 120px" /></el-form-item>
        <el-form-item label="目标床位ID"><el-input-number v-model="transferForm.bedId" style="width: 150px" /></el-form-item>
        <el-form-item><el-button type="primary" @click="handleTransfer">调换</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const historyList = ref([])
const transferForm = reactive({ customerId: null, roomNumber: '', bedId: null })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/bed/history')
    let list = res.data || []
    total.value = list.length
    const start = (pageNum.value - 1) * pageSize.value
    historyList.value = list.slice(start, start + pageSize.value)
  } catch (e) {}
  loading.value = false
}

const handleSizeChange = (val) => { pageSize.value = val; loadData() }
const handleCurrentChange = (val) => { pageNum.value = val; loadData() }

const handleTransfer = async () => {
  if (!transferForm.customerId || !transferForm.roomNumber || !transferForm.bedId) {
    ElMessage.warning('请填写完整信息'); return
  }
  try {
    await request.post('/bed/transfer', { customerId: transferForm.customerId, roomNumber: transferForm.roomNumber, bedId: transferForm.bedId })
    ElMessage.success('床位调换成功'); loadData()
  } catch (e) {}
}

onMounted(() => { loadData() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.table-card { min-height: 400px; }
</style>
