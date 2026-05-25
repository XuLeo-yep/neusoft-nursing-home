<template>
  <div class="page-container">
    <h2 class="page-title">护理项目管理</h2>

    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="项目名称">
          <el-input v-model="searchForm.name" placeholder="请输入项目名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="项目状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 150px">
            <el-option label="启用" value="启用" />
            <el-option label="停用" value="停用" />
          </el-select>
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
      <div class="table-toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon> 新增项目
        </el-button>
      </div>

      <el-table :data="projectList" stripe style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="code" label="编号" width="100" />
        <el-table-column prop="name" label="名称" min-width="120" />
        <el-table-column prop="price" label="价格（元）" width="110" align="right">
          <template #default="{ row }">
            <span style="color: #F56C6C; font-weight: bold;">{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '启用' ? 'success' : 'info'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="cycle" label="执行周期" width="100" />
        <el-table-column prop="times" label="执行次数" width="90" align="center" />
        <el-table-column prop="description" label="描述" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" text @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" text @click="handleDelete(row)">删除</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" destroy-on-close>
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="编号" prop="code">
              <el-input v-model="formData.code" placeholder="请输入编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input v-model="formData.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="价格" prop="price">
              <el-input-number v-model="formData.price" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="formData.status">
                <el-radio label="启用">启用</el-radio>
                <el-radio label="停用">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="执行周期" prop="cycle">
              <el-select v-model="formData.cycle" placeholder="选择周期" style="width: 100%">
                <el-option label="每日" value="每日" />
                <el-option label="每周" value="每周" />
                <el-option label="每月" value="每月" />
                <el-option label="按需" value="按需" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行次数" prop="times">
              <el-input-number v-model="formData.times" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入项目描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('新增项目')
const formRef = ref()
const isEdit = ref(false)
const editId = ref(null)

const searchForm = reactive({ name: '', status: '启用' })

const formData = reactive({
  code: '', name: '', price: 0, status: '启用', cycle: '每日', times: 1, description: ''
})

const formRules = {
  code: [{ required: true, message: '请输入编号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  cycle: [{ required: true, message: '请选择执行周期', trigger: 'change' }],
  times: [{ required: true, message: '请输入执行次数', trigger: 'blur' }]
}

const projectList = ref([])
const allProjects = ref([
  { id: 1, code: 'HL001', name: '日常护理', price: 100, status: '启用', cycle: '每日', times: 3, description: '每日三次基础生活护理' },
  { id: 2, code: 'HL002', name: '康复训练', price: 200, status: '启用', cycle: '每日', times: 2, description: '肢体康复与运动训练' },
  { id: 3, code: 'HL003', name: '心理疏导', price: 150, status: '启用', cycle: '每周', times: 2, description: '心理健康评估与疏导' },
  { id: 4, code: 'HL004', name: '饮食指导', price: 80, status: '启用', cycle: '每日', times: 3, description: '营养膳食搭配指导' },
  { id: 5, code: 'HL005', name: '用药提醒', price: 50, status: '停用', cycle: '每日', times: 3, description: '按时用药提醒服务' },
  { id: 6, code: 'HL006', name: '睡眠监测', price: 120, status: '启用', cycle: '每日', times: 1, description: '夜间睡眠质量监测' },
  { id: 7, code: 'HL007', name: '运动指导', price: 100, status: '启用', cycle: '每日', times: 1, description: '适度运动方案指导' },
  { id: 8, code: 'HL008', name: '健康评估', price: 300, status: '启用', cycle: '每月', times: 1, description: '月度全面健康评估' }
])

const loadData = () => {
  loading.value = true
  setTimeout(() => {
    let list = [...allProjects.value]
    if (searchForm.name) list = list.filter(p => p.name.includes(searchForm.name))
    if (searchForm.status) list = list.filter(p => p.status === searchForm.status)
    total.value = list.length
    const start = (pageNum.value - 1) * pageSize.value
    projectList.value = list.slice(start, start + pageSize.value)
    loading.value = false
  }, 300)
}

const handleSearch = () => { pageNum.value = 1; loadData() }
const handleReset = () => { searchForm.name = ''; searchForm.status = '启用'; pageNum.value = 1; loadData() }
const handleSizeChange = (val) => { pageSize.value = val; loadData() }
const handleCurrentChange = (val) => { pageNum.value = val; loadData() }

const handleAdd = () => {
  isEdit.value = false; editId.value = null
  formData.code = ''; formData.name = ''; formData.price = 0; formData.status = '启用'
  formData.cycle = '每日'; formData.times = 1; formData.description = ''
  dialogTitle.value = '新增项目'; dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true; editId.value = row.id
  Object.assign(formData, row)
  dialogTitle.value = '编辑项目'; dialogVisible.value = true
}

const handleSubmit = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        const idx = allProjects.value.findIndex(p => p.id === editId.value)
        if (idx !== -1) { Object.assign(allProjects.value[idx], formData); ElMessage.success('修改成功') }
      } else {
        allProjects.value.push({ id: Date.now(), ...formData })
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false; loadData()
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除项目【${row.name}】吗？`, '提示', {
    confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'
  }).then(() => {
    const idx = allProjects.value.findIndex(p => p.id === row.id)
    if (idx !== -1) { allProjects.value[idx].deleted = true }
    ElMessage.success('删除成功'); loadData()
  }).catch(() => {})
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
