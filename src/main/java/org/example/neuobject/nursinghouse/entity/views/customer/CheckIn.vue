<template>
  <div class="page-container">
    <h2 class="page-title">入住登记</h2>

    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.name" placeholder="请输入客户姓名" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="老人类型">
          <el-select v-model="searchForm.type" placeholder="全部类型" clearable style="width: 150px">
            <el-option label="自理老人" value="自理老人" />
            <el-option label="护理老人" value="护理老人" />
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
          <el-icon><Plus /></el-icon> 新增入住
        </el-button>
      </div>

      <el-table :data="customerList" stripe style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="name" label="客户姓名" min-width="100" />
        <el-table-column prop="age" label="年龄" width="80" align="center" />
        <el-table-column prop="gender" label="性别" width="80" align="center" />
        <el-table-column prop="idCard" label="身份证号" min-width="160" />
        <el-table-column prop="bloodType" label="血型" width="80" align="center" />
        <el-table-column prop="phone" label="联系电话" min-width="130" />
        <el-table-column prop="building" label="楼栋" width="80" align="center" />
        <el-table-column prop="roomNo" label="房间号" width="100" align="center" />
        <el-table-column prop="bedNo" label="床位号" width="100" align="center" />
        <el-table-column prop="checkInDate" label="入住时间" width="120" />
        <el-table-column prop="contractEndDate" label="合同到期" width="120" />
        <el-table-column prop="type" label="老人类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.type === '护理老人' ? 'danger' : 'success'">{{ row.type }}</el-tag>
          </template>
        </el-table-column>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" destroy-on-close>
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户姓名" prop="name">
              <el-input v-model="formData.name" placeholder="请输入客户姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="formData.idCard" placeholder="请输入身份证号" maxlength="18" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="formData.gender">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker v-model="formData.birthDate" type="date" placeholder="选择日期" style="width: 100%" @change="calcAge" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年龄">
              <el-input v-model="formData.age" disabled placeholder="自动计算" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="血型" prop="bloodType">
              <el-select v-model="formData.bloodType" placeholder="选择血型" style="width: 100%">
                <el-option label="A型" value="A型" />
                <el-option label="B型" value="B型" />
                <el-option label="AB型" value="AB型" />
                <el-option label="O型" value="O型" />
                <el-option label="Rh+" value="Rh+" />
                <el-option label="Rh-" value="Rh-" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="家属姓名" prop="familyName">
              <el-input v-model="formData.familyName" placeholder="请输入家属姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="楼栋" prop="building">
              <el-input v-model="formData.building" disabled placeholder="606" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房间号" prop="roomNo">
              <el-select v-model="formData.roomNo" placeholder="选择房间" style="width: 100%" @change="handleRoomChange">
                <el-option-group v-for="floor in floorList" :key="floor.label" :label="floor.label">
                  <el-option v-for="room in floor.rooms" :key="room" :label="room" :value="room" />
                </el-option-group>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="床位号" prop="bedNo">
              <el-select v-model="formData.bedNo" placeholder="选择床位" style="width: 100%" :disabled="!formData.roomNo">
                <el-option v-for="bed in availableBeds" :key="bed" :label="bed" :value="bed" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入住时间" prop="checkInDate">
              <el-date-picker v-model="formData.checkInDate" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合同到期" prop="contractEndDate">
              <el-date-picker v-model="formData.contractEndDate" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('新增入住')
const formRef = ref()
const isEdit = ref(false)
const editId = ref(null)

const searchForm = reactive({
  name: '',
  type: ''
})

const formData = reactive({
  name: '',
  idCard: '',
  gender: '男',
  birthDate: '',
  age: null,
  bloodType: '',
  familyName: '',
  phone: '',
  building: '606',
  roomNo: '',
  bedNo: '',
  checkInDate: '',
  contractEndDate: ''
})

const formRules = {
  name: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }],
  idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  birthDate: [{ required: true, message: '请选择出生日期', trigger: 'change' }],
  bloodType: [{ required: true, message: '请选择血型', trigger: 'change' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  roomNo: [{ required: true, message: '请选择房间号', trigger: 'change' }],
  bedNo: [{ required: true, message: '请选择床位号', trigger: 'change' }],
  checkInDate: [{ required: true, message: '请选择入住时间', trigger: 'change' }],
  contractEndDate: [
    { required: true, message: '请选择合同到期时间', trigger: 'change' },
    {
      validator: (rule, value, callback) => {
        if (formData.checkInDate && value && new Date(value) <= new Date(formData.checkInDate)) {
          callback(new Error('合同到期时间必须晚于入住时间'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ]
}

const floorList = [
  { label: '一层', rooms: ['A101', 'A102', 'A103', 'A104'] },
  { label: '二层', rooms: ['B201', 'B202', 'B203', 'B204'] },
  { label: '三层', rooms: ['C301', 'C302', 'C303', 'C304'] }
]

const roomBedsMap = {
  'A101': ['01', '02', '03'], 'A102': ['01', '02'], 'A103': ['01', '02', '03', '04'], 'A104': ['01', '02'],
  'B201': ['01', '02'], 'B202': ['01', '02', '03'], 'B203': ['01', '02'], 'B204': ['01', '02', '03', '04'],
  'C301': ['01', '02', '03'], 'C302': ['01', '02'], 'C303': ['01', '02', '03', '04'], 'C304': ['01', '02']
}

const availableBeds = computed(() => {
  if (!formData.roomNo) return []
  const usedBeds = customerList.value
    .filter(c => c.roomNo === formData.roomNo && !c.deleted)
    .map(c => c.bedNo)
  const allBeds = roomBedsMap[formData.roomNo] || []
  return allBeds.filter(b => !usedBeds.includes(b))
})

const customerList = ref([])
const allCustomers = ref([
  { id: 1, name: '张三', age: 78, gender: '男', idCard: '110101194801011234', bloodType: 'A型', familyName: '张小明', phone: '13800138001', building: '606', roomNo: 'A101', bedNo: '01', checkInDate: '2025-03-15', contractEndDate: '2026-03-15', type: '自理老人', deleted: false },
  { id: 2, name: '李四', age: 82, gender: '女', idCard: '110101194301021567', bloodType: 'B型', familyName: '李小红', phone: '13800138002', building: '606', roomNo: 'A101', bedNo: '02', checkInDate: '2025-01-20', contractEndDate: '2026-01-20', type: '护理老人', deleted: false },
  { id: 3, name: '王五', age: 75, gender: '男', idCard: '110101195006033456', bloodType: 'O型', familyName: '王小强', phone: '13800138003', building: '606', roomNo: 'B202', bedNo: '01', checkInDate: '2025-06-01', contractEndDate: '2026-06-01', type: '自理老人', deleted: false },
  { id: 4, name: '赵六', age: 80, gender: '女', idCard: '110101194510048901', bloodType: 'AB型', familyName: '赵小芳', phone: '13800138004', building: '606', roomNo: 'B203', bedNo: '02', checkInDate: '2025-02-10', contractEndDate: '2026-02-10', type: '护理老人', deleted: false },
  { id: 5, name: '孙七', age: 73, gender: '男', idCard: '110101195212055678', bloodType: 'A型', familyName: '孙小亮', phone: '13800138005', building: '606', roomNo: 'C301', bedNo: '03', checkInDate: '2025-08-20', contractEndDate: '2026-08-20', type: '自理老人', deleted: false }
])

const loadData = () => {
  loading.value = true
  setTimeout(() => {
    let list = allCustomers.value.filter(c => !c.deleted)
    if (searchForm.name) {
      list = list.filter(c => c.name.includes(searchForm.name))
    }
    if (searchForm.type) {
      list = list.filter(c => c.type === searchForm.type)
    }
    total.value = list.length
    const start = (pageNum.value - 1) * pageSize.value
    customerList.value = list.slice(start, start + pageSize.value)
    loading.value = false
  }, 300)
}

const handleSearch = () => {
  pageNum.value = 1
  loadData()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.type = ''
  pageNum.value = 1
  loadData()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadData()
}

const handleCurrentChange = (val) => {
  pageNum.value = val
  loadData()
}

const resetForm = () => {
  formData.name = ''
  formData.idCard = ''
  formData.gender = '男'
  formData.birthDate = ''
  formData.age = null
  formData.bloodType = ''
  formData.familyName = ''
  formData.phone = ''
  formData.building = '606'
  formData.roomNo = ''
  formData.bedNo = ''
  formData.checkInDate = ''
  formData.contractEndDate = ''
}

const handleAdd = () => {
  isEdit.value = false
  editId.value = null
  resetForm()
  dialogTitle.value = '新增入住'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  editId.value = row.id
  Object.assign(formData, row)
  dialogTitle.value = '修改客户信息'
  dialogVisible.value = true
}

const calcAge = () => {
  if (formData.birthDate) {
    const birth = new Date(formData.birthDate)
    const now = new Date()
    let age = now.getFullYear() - birth.getFullYear()
    const monthDiff = now.getMonth() - birth.getMonth()
    if (monthDiff < 0 || (monthDiff === 0 && now.getDate() < birth.getDate())) {
      age--
    }
    formData.age = age
  }
}

const handleRoomChange = () => {
  formData.bedNo = ''
}

const handleSubmit = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        const index = allCustomers.value.findIndex(c => c.id === editId.value)
        if (index !== -1) {
          allCustomers.value[index] = { ...allCustomers.value[index], ...formData }
          ElMessage.success('修改成功')
        }
      } else {
        const newCustomer = {
          id: Date.now(),
          ...formData,
          type: '自理老人',
          deleted: false
        }
        allCustomers.value.push(newCustomer)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      loadData()
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除客户【${row.name}】的信息吗？`, '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = allCustomers.value.findIndex(c => c.id === row.id)
    if (index !== -1) {
      allCustomers.value[index].deleted = true
    }
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-container {
  padding: 0;
}

.page-title {
  margin-bottom: 20px;
  font-size: 20px;
  color: #303133;
}

.search-card {
  margin-bottom: 15px;
}

.table-toolbar {
  margin-bottom: 15px;
}

.table-card {
  min-height: 500px;
}
</style>
