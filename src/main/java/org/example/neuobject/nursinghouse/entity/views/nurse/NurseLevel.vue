<template>
  <div class="page-container">
    <h2 class="page-title">护理级别</h2>

    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="级别状态">
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
          <el-icon><Plus /></el-icon> 新增级别
        </el-button>
      </div>

      <el-table :data="levelList" stripe style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="name" label="级别名称" min-width="150" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '启用' ? 'success' : 'info'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="projectCount" label="护理项目数" width="110" align="center" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" text @click="handleEdit(row)">修改</el-button>
            <el-button type="warning" size="small" text @click="handleConfig(row)">配置项目</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="400px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="级别名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入级别名称" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio label="启用">启用</el-radio>
            <el-radio label="停用">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="configVisible" title="护理项目配置" width="600px">
      <el-row :gutter="20">
        <el-col :span="12">
          <h4>已配置项目</h4>
          <el-table :data="currentProjects" size="small" height="300">
            <el-table-column prop="name" label="项目名称" />
            <el-table-column label="操作" width="80">
              <template #default="{ row }">
                <el-button type="danger" size="small" text @click="removeProject(row)">移除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="12">
          <h4>可选项目</h4>
          <el-table :data="availableProjects" size="small" height="300">
            <el-table-column prop="name" label="项目名称" />
            <el-table-column label="操作" width="80">
              <template #default="{ row }">
                <el-button type="primary" size="small" text @click="addProject(row)">添加</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const configVisible = ref(false)
const dialogTitle = ref('新增级别')
const formRef = ref()
const isEdit = ref(false)
const editId = ref(null)
const configLevelId = ref(null)

const searchForm = reactive({ status: '启用' })
const formData = reactive({ name: '', status: '启用' })
const formRules = {
  name: [{ required: true, message: '请输入级别名称', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const allProjects = ref([
  { id: 1, name: '日常护理', price: 100, status: '启用' },
  { id: 2, name: '康复训练', price: 200, status: '启用' },
  { id: 3, name: '心理疏导', price: 150, status: '启用' },
  { id: 4, name: '饮食指导', price: 80, status: '启用' },
  { id: 5, name: '用药提醒', price: 50, status: '停用' },
  { id: 6, name: '睡眠监测', price: 120, status: '启用' },
  { id: 7, name: '运动指导', price: 100, status: '启用' },
  { id: 8, name: '健康评估', price: 300, status: '启用' }
])

const levelProjects = ref({
  1: [1, 2, 3],
  2: [2, 4, 6],
  3: [1, 3, 4, 7]
})

const levelList = ref([])
const allLevels = ref([
  { id: 1, name: '一级护理', status: '启用' },
  { id: 2, name: '二级护理', status: '启用' },
  { id: 3, name: '三级护理', status: '启用' },
  { id: 4, name: '特级护理', status: '停用' }
])

const currentProjects = computed(() => {
  const ids = levelProjects.value[configLevelId.value] || []
  return allProjects.value.filter(p => ids.includes(p.id) && p.status === '启用')
})

const availableProjects = computed(() => {
  const ids = levelProjects.value[configLevelId.value] || []
  return allProjects.value.filter(p => !ids.includes(p.id) && p.status === '启用')
})

const loadData = () => {
  loading.value = true
  setTimeout(() => {
    let list = allLevels.value.map(l => ({
      ...l,
      projectCount: (levelProjects.value[l.id] || []).length
    }))
    if (searchForm.status) list = list.filter(l => l.status === searchForm.status)
    levelList.value = list
    loading.value = false
  }, 300)
}

const handleSearch = () => { loadData() }
const handleReset = () => { searchForm.status = '启用'; loadData() }

const handleAdd = () => {
  isEdit.value = false; editId.value = null
  formData.name = ''; formData.status = '启用'
  dialogTitle.value = '新增级别'; dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true; editId.value = row.id
  formData.name = row.name; formData.status = row.status
  dialogTitle.value = '修改级别'; dialogVisible.value = true
}

const handleSubmit = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        const idx = allLevels.value.findIndex(l => l.id === editId.value)
        if (idx !== -1) { allLevels.value[idx].name = formData.name; allLevels.value[idx].status = formData.status; ElMessage.success('修改成功') }
      } else {
        allLevels.value.push({ id: Date.now(), name: formData.name, status: formData.status })
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false; loadData()
    }
  })
}

const handleConfig = (row) => {
  configLevelId.value = row.id; configVisible.value = true
}

const addProject = (row) => {
  if (!levelProjects.value[configLevelId.value]) levelProjects.value[configLevelId.value] = []
  levelProjects.value[configLevelId.value].push(row.id)
  ElMessage.success('添加成功')
  loadData()
}

const removeProject = (row) => {
  const ids = levelProjects.value[configLevelId.value] || []
  const idx = ids.indexOf(row.id)
  if (idx > -1) { ids.splice(idx, 1); ElMessage.success('移除成功'); loadData() }
}

onMounted(() => { loadData() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.search-card { margin-bottom: 15px; }
.table-toolbar { margin-bottom: 15px; }
.table-card { min-height: 500px; }
h4 { margin: 0 0 10px 0; font-size: 14px; color: #606266; }
</style>
