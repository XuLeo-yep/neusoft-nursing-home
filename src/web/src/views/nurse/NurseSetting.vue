<template>
  <div class="page-container">
    <h2 class="page-title">客户护理设置</h2>
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
      <el-table :data="customerList" stripe style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="username" label="客户姓名" min-width="100" />
        <el-table-column prop="age" label="年龄" width="80" align="center" />
        <el-table-column prop="nursingLevelId" label="护理级别ID" width="120" align="center">
          <template #default="{ row }"><el-tag v-if="row.nursingLevelId" type="success">{{ row.nursingLevelId }}</el-tag><span v-else style="color: #909399;">未设置</span></template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button v-if="!row.nursingLevelId" type="primary" size="small" text @click="handleSet(row)">设置级别</el-button>
            <el-button v-else type="danger" size="small" text @click="handleRemove(row)">移除级别</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[10, 20, 50]" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
    </el-card>
    <el-dialog v-model="setVisible" title="设置护理级别" width="400px">
      <el-form :model="setForm" label-width="100px">
        <el-form-item label="客户"><span>{{ setForm.customerName }}</span></el-form-item>
        <el-form-item label="护理级别" prop="levelId">
          <el-select v-model="setForm.levelId" placeholder="选择护理级别" style="width: 100%">
            <el-option v-for="level in levelOptions" :key="level.levelId" :label="level.name" :value="level.levelId" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="setVisible = false">取消</el-button><el-button type="primary" @click="handleSetSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const setVisible = ref(false)
const searchForm = reactive({ name: '' })
const setForm = reactive({ customerId: null, customerName: '', levelId: null })
const customerList = ref([])
const levelOptions = ref([])

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/customer/list', { params: { name: searchForm.name || undefined } })
    let list = res.data || []
    total.value = list.length
    const start = (pageNum.value - 1) * pageSize.value
    customerList.value = list.slice(start, start + pageSize.value)
  } catch (e) {}
  loading.value = false
}

const loadLevels = async () => {
  try { const res = await request.get('/nursing/level/list'); levelOptions.value = (res.data || []).filter(l => l.status === '启用') } catch (e) {}
}

const handleSearch = () => { pageNum.value = 1; loadData() }
const handleReset = () => { searchForm.name = ''; pageNum.value = 1; loadData() }
const handleSizeChange = (val) => { pageSize.value = val; loadData() }
const handleCurrentChange = (val) => { pageNum.value = val; loadData() }

const handleSet = (row) => {
  setForm.customerId = row.id; setForm.customerName = row.username; setForm.levelId = null
  loadLevels(); setVisible.value = true
}

const handleSetSubmit = async () => {
  if (!setForm.levelId) { ElMessage.warning('请选择护理级别'); return }
  try {
    await request.post('/nursing/customer/' + setForm.customerId + '/level/' + setForm.levelId)
    ElMessage.success('设置成功'); setVisible.value = false; loadData()
  } catch (e) {}
}

const handleRemove = (row) => {
  ElMessageBox.confirm(`确认移除客户【${row.username}】的护理级别吗？`, '提示', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { await request.delete('/nursing/customer/' + row.id + '/level'); ElMessage.success('移除成功'); loadData() }).catch(() => {})
}

onMounted(() => { loadData() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.search-card { margin-bottom: 15px; }
.table-card { min-height: 500px; }
</style>
