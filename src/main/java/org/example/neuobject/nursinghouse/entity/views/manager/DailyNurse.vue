<template>
  <div class="page-container">
    <h2 class="page-title">日常护理</h2>

    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.name" placeholder="请输入客户姓名" clearable style="width: 200px" />
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
      <el-col :span="8">
        <el-card shadow="never">
          <template #header><span>我的服务对象</span></template>
          <el-table :data="customerList" size="small" highlight-current-row @row-click="handleSelectCustomer" style="cursor: pointer;">
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="roomNo" label="房间" width="80" />
            <el-table-column prop="bedNo" label="床位" width="80" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="never" v-if="currentCustomer">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>{{ currentCustomer.name }} 的护理项目</span>
            </div>
          </template>
          <el-table :data="projectList" size="small">
            <el-table-column prop="projectName" label="项目名称" min-width="120" />
            <el-table-column prop="totalQuantity" label="总数量" width="90" align="center" />
            <el-table-column prop="usedQuantity" label="已使用" width="90" align="center" />
            <el-table-column prop="remaining" label="剩余" width="90" align="center">
              <template #default="{ row }">
                <el-tag :type="row.remaining > 0 ? 'success' : 'danger'" size="small">{{ row.remaining }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="expireDate" label="到期日期" width="120" />
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button v-if="row.remaining > 0" type="primary" size="small" text @click="handleNurse(row)">护理</el-button>
                <span v-else style="color: #909399; font-size: 12px;">已用完</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        <el-card v-else shadow="never">
          <el-empty description="请选择左侧客户" />
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="nurseVisible" title="添加护理记录" width="500px">
      <el-form :model="nurseForm" label-width="100px">
        <el-form-item label="客户"><span>{{ nurseForm.customerName }}</span></el-form-item>
        <el-form-item label="护理项目"><span>{{ nurseForm.projectName }}</span></el-form-item>
        <el-form-item label="护理时间">
          <el-date-picker v-model="nurseForm.nurseTime" type="datetime" placeholder="选择时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="护理数量">
          <el-input-number v-model="nurseForm.quantity" :min="1" :max="nurseForm.maxQuantity" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="nurseForm.remark" type="textarea" :rows="2" placeholder="备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="nurseVisible = false">取消</el-button>
        <el-button type="primary" @click="handleNurseSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const searchForm = reactive({ name: '' })
const currentCustomer = ref(null)
const nurseVisible = ref(false)
const nurseForm = reactive({ customerName: '', projectName: '', projectId: null, nurseTime: '', quantity: 1, maxQuantity: 1, remark: '' })

const customerList = ref([])
const allCustomers = ref([
  { id: 1, name: '张三', roomNo: 'A101', bedNo: '01' },
  { id: 2, name: '李四', roomNo: 'A101', bedNo: '02' },
  { id: 3, name: '王五', roomNo: 'B202', bedNo: '01' }
])

const projectList = ref([])
const allProjects = ref([
  { id: 1, customerId: 1, projectName: '日常护理', totalQuantity: 90, usedQuantity: 30, remaining: 60, expireDate: '2026-08-15' },
  { id: 2, customerId: 1, projectName: '康复训练', totalQuantity: 60, usedQuantity: 20, remaining: 40, expireDate: '2026-08-15' },
  { id: 3, customerId: 1, projectName: '心理疏导', totalQuantity: 24, usedQuantity: 8, remaining: 16, expireDate: '2026-08-15' },
  { id: 4, customerId: 2, projectName: '日常护理', totalQuantity: 90, usedQuantity: 45, remaining: 45, expireDate: '2026-08-20' },
  { id: 5, customerId: 2, projectName: '饮食指导', totalQuantity: 90, usedQuantity: 30, remaining: 60, expireDate: '2026-08-20' },
  { id: 6, customerId: 3, projectName: '日常护理', totalQuantity: 90, usedQuantity: 10, remaining: 80, expireDate: '2026-09-01' }
])

const loadCustomers = () => {
  let list = [...allCustomers.value]
  if (searchForm.name) list = list.filter(c => c.name.includes(searchForm.name))
  customerList.value = list
}

const handleSearch = () => { loadCustomers() }
const handleReset = () => { searchForm.name = ''; loadCustomers() }

const handleSelectCustomer = (row) => {
  currentCustomer.value = row
  projectList.value = allProjects.value.filter(p => p.customerId === row.id)
}

const handleNurse = (row) => {
  nurseForm.customerName = currentCustomer.value.name
  nurseForm.projectName = row.projectName
  nurseForm.projectId = row.id
  nurseForm.nurseTime = new Date()
  nurseForm.quantity = 1
  nurseForm.maxQuantity = row.remaining
  nurseForm.remark = ''
  nurseVisible.value = true
}

const handleNurseSubmit = () => {
  const idx = allProjects.value.findIndex(p => p.id === nurseForm.projectId)
  if (idx !== -1) {
    allProjects.value[idx].usedQuantity += nurseForm.quantity
    allProjects.value[idx].remaining -= nurseForm.quantity
    ElMessage.success('护理记录添加成功')
  }
  nurseVisible.value = false
  if (currentCustomer.value) { handleSelectCustomer(currentCustomer.value) }
}

onMounted(() => { loadCustomers() })
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.search-card { margin-bottom: 15px; }
</style>
