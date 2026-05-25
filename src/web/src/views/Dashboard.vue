<template>
  <div class="dashboard">
    <h2>欢迎使用东软颐养中心管理系统</h2>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <template #header><div class="card-header"><el-icon><User /></el-icon><span>客户总数</span></div></template>
          <div class="card-body"><span class="number">{{ stats.customerCount }}</span><span class="unit">人</span></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <template #header><div class="card-header"><el-icon><HomeFilled /></el-icon><span>床位使用</span></div></template>
          <div class="card-body"><span class="number">{{ stats.occupied }}</span><span class="unit">/ {{ stats.total }}</span></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <template #header><div class="card-header"><el-icon><FirstAidKit /></el-icon><span>护理项目</span></div></template>
          <div class="card-body"><span class="number">{{ stats.projectCount }}</span><span class="unit">项</span></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <template #header><div class="card-header"><el-icon><Service /></el-icon><span>健康管家</span></div></template>
          <div class="card-body"><span class="number">{{ stats.workerCount }}</span><span class="unit">人</span></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card><template #header><span>最近入住</span></template>
          <el-table :data="recentCheckins" size="small">
            <el-table-column prop="username" label="姓名" />
            <el-table-column prop="room" label="房间号" />
            <el-table-column prop="bed" label="床位号" />
            <el-table-column prop="checkInDate" label="入住日期" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card><template #header><span>待办审批</span></template>
          <el-timeline>
            <el-timeline-item v-for="(item, index) in todos" :key="index" :timestamp="item.time">
              {{ item.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'

const stats = reactive({ customerCount: 0, total: 0, free: 0, occupied: 0, projectCount: 0, workerCount: 0 })
const recentCheckins = ref([])
const todos = ref([])

onMounted(async () => {
  try {
    const [bedRes, customerRes, projectRes, userRes, checkoutRes, outingRes] = await Promise.all([
      request.get('/bed/diagram'),
      request.get('/customer/list'),
      request.get('/nursing/project/list'),
      request.get('/user/list'),
      request.get('/application/checkout/list'),
      request.get('/application/outing/list')
    ])
    Object.assign(stats, {
      total: bedRes.data?.total || 0,
      free: bedRes.data?.free || 0,
      occupied: bedRes.data?.occupied || 0,
      customerCount: (customerRes.data || []).length,
      projectCount: (projectRes.data || []).length,
      workerCount: (userRes.data || []).filter(u => u.role === 'health_worker').length
    })
    recentCheckins.value = (customerRes.data || []).slice(0, 3).map(c => ({
      username: c.username, room: c.roomNumber || c.roomNo || '-', bed: c.bedNumber || c.bedNo || '-', checkInDate: c.checkInDate || '-'
    }))
    const pending = [
      ...(checkoutRes.data || []).filter(a => a.approvalStatus === '待审批').map(a => ({ content: `退住申请待审批: ${a.reason}`, time: a.applyTime?.split(' ')[0] || '-' })),
      ...(outingRes.data || []).filter(a => a.approvalStatus === '待审批').map(a => ({ content: `外出申请待审批: ${a.reason}`, time: a.outingTime?.split(' ')[0] || '-' }))
    ]
    todos.value = pending.length > 0 ? pending : [{ content: '暂无待审批事项', time: '' }]
  } catch (e) {}
})
</script>

<style scoped>
.dashboard h2 { color: #303133; }
.stat-card .card-header { display: flex; align-items: center; gap: 8px; color: #606266; }
.stat-card .card-body { text-align: center; }
.stat-card .number { font-size: 36px; font-weight: bold; color: #409EFF; }
.stat-card .unit { font-size: 14px; color: #909399; margin-left: 5px; }
</style>
