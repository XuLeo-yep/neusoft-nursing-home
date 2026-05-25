<template>
  <div class="page-container">
    <h2 class="page-title">设置服务对象</h2>

    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="管家姓名">
          <el-input v-model="searchForm.name" placeholder="请输入管家姓名" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon> 查询
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="20">
      <el-col :span="10">
        <el-card shadow="never">
          <template #header><span>健康管家列表</span></template>
          <el-table :data="managerList" size="small" highlight-current-row @row-click="handleSelectManager" style="cursor: pointer;">
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="phone" label="电话" min-width="120" />
            <el-table-column prop="serviceCount" label="服务客户" width="90" align="center" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>{{ currentManager ? currentManager.name + ' 的服务对象' : '请选择管家' }}</span>
              <el-button v-if="currentManager" type="primary" size="small" @click="addVisible = true">
                <el-icon><Plus /></el-icon> 添加客户
              </el-button>
            </div>
          </template>
          <el-table :data="serviceList" size="small" v-loading="serviceLoading">
            <el-table-column type="index" label="序号" width="50" align="center" />
            <el-table-column prop="customerName" label="客户姓名" />
            <el-table-column prop="roomNo" label="房间号" width="100" />
            <el-table-column prop="bedNo" label="床位号" width="100" />
            <el-table-column label="操作" width="80">
              <template #default="{ row }">
                <el-button type="danger" size="small" text @click="handleRemove(row)">移除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="addVisible" title="添加服务对象" width="500px">
      <el-form :model="addForm" inline>
        <el-form-item label="客户姓名">
          <el-input v-model="addForm.searchName" placeholder="搜索客户" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchNoManager">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="noManagerList" size="small">
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="roomNo" label="房间" />
        <el-table-column label="操作" width="80">
          <template #default="{ row }">
            <el-button type="primary" size="small" text @click="handleAddCustomer(row)">添加</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const searchForm = reactive({ name: '' })
const addForm = reactive({ searchName: '' })
const currentManager = ref(null)
const serviceLoading = ref(false)
const addVisible = ref(false)

const managerList = ref([])
const allManagers = ref([
  { id: 1, name: '护工A', phone: '13800138011', serviceCount: 2 },
  { id: 2, name: '护工B', phone: '13800138012', serviceCount: 1 },
  { id: 3, name: '护工C', phone: '13800138013', serviceCount: 0 },
  { id: 4, name: '护工D', phone: '13800138014', serviceCount: 3 }
])

const serviceList = ref([])
const allServices = ref([
  { id: 1, managerId: 1, customerId: 1, customerName: '张三', roomNo: 'A101', bedNo: '01' },
  { id: 2, managerId: 1, customerId: 2, customerName: '李四', roomNo: 'A101', bedNo: '02' },
  { id: 3, managerId: 2, customerId: 3, customerName: '王五', roomNo: 'B202', bedNo: '01' },
  { id: 4, managerId: 4, customerId: 4, customerName: '赵六', roomNo: 'B203', bedNo: '02' },
  { id: 5, managerId: 4, customerId: 5, customerName: '孙七', roomNo: 'C301', bedNo: '03' }
])

const noManagerList = ref([])
const allCustomers = ref([
  { id: 1, name: '张三', roomNo: 'A101', bedNo: '01', hasManager: true },
  { id: 2, name: '李四', roomNo: 'A101', bedNo: '02', hasManager: true },
  { id: 3, name: '王五', roomNo: 'B202', bedNo: '01', hasManager: true },
  { id: 4, name: '赵六', roomNo: 'B203', bedNo: '02', hasManager: true },
  { id: 5, name: '孙七', roomNo: 'C301', bedNo: '03', hasManager: true },
  { id: 6, name: '周八', roomNo: 'A103', bedNo: '01', hasManager: false },
  { id: 7, name: '吴九', roomNo: 'B204', bedNo: '03', hasManager: false },
  { id: 8, name: '郑十', roomNo: 'C302', bedNo: '02', hasManager: false }
])

const loadManagers = () => {
  let list = [...allManagers.value]
  if (searchForm.name) list = list.filter(m => m.name.includes(searchForm.name))
  list = list.map(m => ({ ...m, serviceCount: allServices.value.filter(s => s.managerId === m.id).length }))
  managerList.value = list
}

const handleSearch = () => { loadManagers() }
const handleReset = () => { searchForm.name = ''; loadManagers() }

const handleSelectManager = (row) => {
  currentManager.value = row
  serviceLoading.value = true
  setTimeout(() => {
    serviceList.value = allServices.value.filter(s => s.managerId === row.id)
    serviceLoading.value = false
  }, 200)
}

const searchNoManager = () => {
  let list = allCustomers.value.filter(c => !c.hasManager)
  if (addForm.searchName) list = list.filter(c => c.name.includes(addForm.searchName))
  noManagerList.value = list
}

const handleAddCustomer = (row) => {
  allServices.value.push({
    id: Date.now(),
    managerId: currentManager.value.id,
    customerId: row.id,
    customerName: row.name,
    roomNo: row.roomNo,
    bedNo: row.bedNo
  })
  const idx = allCustomers.value.findIndex(c => c.id === row.id)
  if (idx !== -1) allCustomers.value[idx].hasManager = true
  ElMessage.success('添加成功')
  addVisible.value = false
  handleSelectManager(currentManager.value)
  loadManagers()
}

const handleRemove = (row) => {
  const idx = allServices.value.findIndex(s => s.id === row.id)
  if (idx !== -1) {
    allServices.value.splice(idx, 1)
    const cIdx = allCustomers.value.findIndex(c => c.id === row.customerId)
    if (cIdx !== -1) allCustomers.value[cIdx].hasManager = false
    ElMessage.success('移除成功')
  }
  handleSelectManager(currentManager.value)
  loadManagers()
}

onMounted(() => { loadManagers() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.search-card { margin-bottom: 15px; }
</style>
