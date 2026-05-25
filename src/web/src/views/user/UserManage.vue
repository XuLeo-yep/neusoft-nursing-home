<template>
  <div class="page-container">
    <h2 class="page-title">用户管理</h2>
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="用户姓名"><el-input v-model="searchForm.name" placeholder="请输入用户姓名" clearable style="width: 180px" /></el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.role" placeholder="全部" clearable style="width: 150px">
            <el-option label="系统管理员" value="admin" />
            <el-option label="健康管家" value="health_worker" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch"><el-icon><Search /></el-icon> 查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="table-card" shadow="never">
      <div class="table-toolbar"><el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon> 新增用户</el-button></div>
      <el-table :data="userList" stripe style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="username" label="登录账号" min-width="120" />
        <el-table-column prop="username" label="真实姓名" min-width="100" />
        <el-table-column prop="phone" label="手机号" min-width="130" />
        <el-table-column prop="role" label="角色" width="120" align="center">
          <template #default="{ row }"><el-tag :type="row.role === 'admin' ? 'danger' : 'success'">{{ row.role === 'admin' ? '系统管理员' : '健康管家' }}</el-tag></template>
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" destroy-on-close>
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="登录账号" prop="loginCode"><el-input v-model="formData.loginCode" placeholder="请输入登录账号" /></el-form-item>
        <el-form-item label="用户名" prop="username"><el-input v-model="formData.username" placeholder="请输入用户名" /></el-form-item>
        <el-form-item label="手机号" prop="phone"><el-input v-model="formData.phone" placeholder="请输入手机号" maxlength="11" /></el-form-item>
        <el-form-item label="密码" prop="password" v-if="!isEdit"><el-input v-model="formData.password" type="password" placeholder="请输入密码" show-password /></el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="formData.role" placeholder="选择角色" style="width: 100%">
            <el-option label="系统管理员" value="admin" />
            <el-option label="健康管家" value="health_worker" />
          </el-select>
        </el-form-item>
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
const dialogTitle = ref('新增用户')
const formRef = ref()
const isEdit = ref(false)
const editId = ref(null)
const searchForm = reactive({ name: '', role: '' })
const userList = ref([])

const formData = reactive({ loginCode: '', username: '', phone: '', password: '', role: 'health_worker' })
const formRules = {
  loginCode: [{ required: true, message: '请输入登录账号', trigger: 'blur' }],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/user/list')
    let list = res.data || []
    if (searchForm.name) list = list.filter(u => u.username?.includes(searchForm.name))
    if (searchForm.role) list = list.filter(u => u.role === searchForm.role)
    total.value = list.length
    const start = (pageNum.value - 1) * pageSize.value
    userList.value = list.slice(start, start + pageSize.value)
  } catch (e) {}
  loading.value = false
}

const handleSearch = () => { pageNum.value = 1; loadData() }
const handleReset = () => { searchForm.name = ''; searchForm.role = ''; pageNum.value = 1; loadData() }
const handleSizeChange = (val) => { pageSize.value = val; loadData() }
const handleCurrentChange = (val) => { pageNum.value = val; loadData() }

const handleAdd = () => {
  isEdit.value = false; editId.value = null
  formData.loginCode = ''; formData.username = ''; formData.phone = ''; formData.password = ''; formData.role = 'health_worker'
  dialogTitle.value = '新增用户'; dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true; editId.value = row.id
  formData.loginCode = row.loginCode; formData.username = row.username; formData.phone = row.phone || ''; formData.role = row.role
  dialogTitle.value = '编辑用户'; dialogVisible.value = true
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    if (isEdit.value) {
      await request.put('/user/update', { id: editId.value, loginCode: formData.loginCode, username: formData.username, phone: formData.phone, password: formData.password, role: formData.role })
      ElMessage.success('修改成功')
    } else {
      await request.post('/user/add', { loginCode: formData.loginCode, username: formData.username, phone: formData.phone, password: formData.password || '123456', role: formData.role })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false; loadData()
  } catch (e) {}
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除用户【${row.username}】吗？`, '提示', { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { await request.delete('/user/' + row.id); ElMessage.success('删除成功'); loadData() }).catch(() => {})
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
