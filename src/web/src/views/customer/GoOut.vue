<template>
  <div class="page-container">
    <h2 class="page-title">外出登记</h2>
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="客户姓名"><el-input v-model="searchForm.name" placeholder="请输入客户姓名" clearable style="width: 200px" /></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch"><el-icon><Search /></el-icon> 查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="table-card" shadow="never">
      <el-table :data="goOutList" stripe style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="customerId" label="客户ID" width="80" />
        <el-table-column prop="reason" label="外出事由" min-width="150" show-overflow-tooltip />
        <el-table-column prop="outingTime" label="外出时间" width="160" />
        <el-table-column prop="plannedReturnTime" label="预计回院" width="160" />
        <el-table-column prop="actualReturnTime" label="实际回院" width="160">
          <template #default="{ row }"><span v-if="row.actualReturnTime">{{ row.actualReturnTime }}</span><el-tag v-else type="warning" size="small">未回院</el-tag></template>
        </el-table-column>
        <el-table-column prop="approvalStatus" label="审批状态" width="100" align="center">
          <template #default="{ row }"><el-tag :type="row.approvalStatus === '已批准' ? 'success' : row.approvalStatus === '已拒绝' ? 'danger' : 'warning'">{{ row.approvalStatus }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="approver" label="审批人" width="100" />
        <el-table-column prop="approvalTime" label="审批时间" width="160" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.approvalStatus === '待审批'" type="primary" size="small" text @click="handleAudit(row)">审核</el-button>
            <el-button v-if="row.approvalStatus === '已批准' && !row.actualReturnTime" type="success" size="small" text @click="handleBack(row)">登记回院</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[10, 20, 50]" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
    </el-card>
    <el-dialog v-model="auditVisible" title="外出审核" width="500px">
      <el-form :model="auditForm" label-width="100px">
        <el-form-item label="客户ID"><span>{{ auditForm.customerId }}</span></el-form-item>
        <el-form-item label="外出事由"><span>{{ auditForm.reason }}</span></el-form-item>
        <el-form-item label="审核结果"><el-radio-group v-model="auditForm.result"><el-radio label="通过">通过</el-radio><el-radio label="不通过">不通过</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="auditVisible = false">取消</el-button><el-button type="primary" @click="handleAuditSubmit">确定</el-button></template>
    </el-dialog>
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
const auditVisible = ref(false)
const goOutList = ref([])
const auditForm = reactive({ id: null, customerId: null, reason: '', result: '通过' })
const searchForm = reactive({ name: '' })

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/application/outing/list')
    let list = res.data || []
    if (searchForm.name) list = list.filter(r => String(r.customerId).includes(searchForm.name))
    total.value = list.length
    const start = (pageNum.value - 1) * pageSize.value
    goOutList.value = list.slice(start, start + pageSize.value)
  } catch (e) {}
  loading.value = false
}

const handleSearch = () => { pageNum.value = 1; loadData() }
const handleReset = () => { searchForm.name = ''; pageNum.value = 1; loadData() }
const handleSizeChange = (val) => { pageSize.value = val; loadData() }
const handleCurrentChange = (val) => { pageNum.value = val; loadData() }

const handleAudit = (row) => {
  auditForm.id = row.id; auditForm.customerId = row.customerId; auditForm.reason = row.reason; auditForm.result = '通过'; auditVisible.value = true
}

const handleAuditSubmit = async () => {
  try {
    await request.put('/application/outing/' + auditForm.id + '/approve', { result: auditForm.result, approver: localStorage.getItem('username') || 'admin' })
    ElMessage.success('审核完成'); auditVisible.value = false; loadData()
  } catch (e) {}
}

const handleBack = async (row) => {
  try {
    await request.put('/application/outing/' + row.id + '/return', {})
    ElMessage.success('回院登记成功'); loadData()
  } catch (e) {}
}

onMounted(() => { loadData() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.search-card { margin-bottom: 15px; }
.table-card { min-height: 500px; }
</style>
