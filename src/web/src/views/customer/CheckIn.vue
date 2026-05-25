<template>
  <div class="page-container">
    <h2 class="page-title">入住登记</h2>
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
      <div class="table-toolbar"><el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon> 新增入住</el-button></div>
      <el-table :data="customerList" stripe style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="username" label="客户姓名" min-width="100" />
        <el-table-column prop="age" label="年龄" width="80" align="center" />
        <el-table-column prop="gender" label="性别" width="80" align="center" />
        <el-table-column prop="idNumber" label="身份证号" min-width="160" />
        <el-table-column prop="phone" label="联系电话" min-width="130" />
        <el-table-column prop="checkInDate" label="入住时间" width="120" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }"><el-tag :type="row.status === '在住' ? 'success' : 'info'">{{ row.status }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" text @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" text @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[10, 20, 50]" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
    </el-card>
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" destroy-on-close>
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="客户姓名" prop="username"><el-input v-model="formData.username" placeholder="请输入客户姓名" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="身份证号" prop="idNumber"><el-input v-model="formData.idNumber" placeholder="请输入身份证号" maxlength="18" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="性别" prop="gender"><el-radio-group v-model="formData.gender"><el-radio label="男">男</el-radio><el-radio label="女">女</el-radio></el-radio-group></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="年龄" prop="age"><el-input-number v-model="formData.age" :min="0" :max="150" style="width: 100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="联系电话" prop="phone"><el-input v-model="formData.phone" placeholder="请输入联系电话" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="入住时间" prop="checkInDate"><el-date-picker v-model="formData.checkInDate" type="date" placeholder="选择日期" style="width: 100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="健康信息"><el-input v-model="formData.healthInfo" type="textarea" :rows="2" placeholder="健康信息" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
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
const dialogVisible = ref(false)
const dialogTitle = ref('新增入住')
const formRef = ref()
const isEdit = ref(false)
const editId = ref(null)
const searchForm = reactive({ name: '' })
const customerList = ref([])

const formData = reactive({ username: '', idNumber: '', gender: '男', age: null, phone: '', checkInDate: '', healthInfo: '' })
const formRules = {
  username: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }],
  idNumber: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  checkInDate: [{ required: true, message: '请选择入住时间', trigger: 'change' }]
}

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

const handleSearch = () => { pageNum.value = 1; loadData() }
const handleReset = () => { searchForm.name = ''; pageNum.value = 1; loadData() }
const handleSizeChange = (val) => { pageSize.value = val; loadData() }
const handleCurrentChange = (val) => { pageNum.value = val; loadData() }

const handleAdd = () => {
  isEdit.value = false; editId.value = null
  formData.username = ''; formData.idNumber = ''; formData.gender = '男'; formData.age = null; formData.phone = ''; formData.checkInDate = ''; formData.healthInfo = ''
  dialogTitle.value = '新增入住'; dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true; editId.value = row.id
  Object.assign(formData, row)
  dialogTitle.value = '修改客户信息'; dialogVisible.value = true
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    if (isEdit.value) {
      await request.put('/customer/update', { id: editId.value, ...formData })
      ElMessage.success('修改成功')
    } else {
      await request.post('/customer/add', { ...formData, status: '在住' })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false; loadData()
  } catch (e) {}
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除客户【${row.username}】吗？`, '提示', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { await request.delete('/customer/' + row.id); ElMessage.success('删除成功'); loadData() }).catch(() => {})
}

onMounted(() => { loadData() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.search-card { margin-bottom: 15px; }
.table-toolbar { margin-bottom: 15px; }
.table-card { min-height: 500px; }
</style>
