<template>
  <div class="page-container">
    <h2 class="page-title">床位示意图</h2>
    <el-row :gutter="20">
      <el-col :span="6"><el-card class="stat-card" shadow="hover"><div class="stat-item"><div class="stat-label">总床位数</div><div class="stat-value total">{{ stats.total }}</div></div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card" shadow="hover"><div class="stat-item"><div class="stat-label">空闲床位</div><div class="stat-value free">{{ stats.free }}</div></div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card" shadow="hover"><div class="stat-item"><div class="stat-label">有人占用</div><div class="stat-value occupied">{{ stats.occupied }}</div></div></el-card></el-col>
    </el-row>
    <el-card class="floor-card" shadow="never" style="margin-top: 20px;">
      <template #header><div class="floor-header"><span>房间列表</span></div></template>
      <div v-for="room in rooms" :key="room.id" class="room-section">
        <div class="room-title">{{ room.roomNumber }}（{{ room.beds ? room.beds.length : 0 }}个床位）· {{ room.roomType }}</div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'

const stats = reactive({ total: 0, free: 0, occupied: 0 })
const rooms = ref([])

onMounted(async () => {
  const [statsRes, roomRes] = await Promise.all([
    request.get('/bed/diagram'),
    request.get('/bed/rooms')
  ])
  Object.assign(stats, statsRes.data || {})
  rooms.value = roomRes.data || []
})
</script>

<style scoped>
.page-container { padding: 0; }
.page-title { margin-bottom: 20px; font-size: 20px; color: #303133; }
.stat-card { text-align: center; }
.stat-item { padding: 10px; }
.stat-label { font-size: 14px; color: #909399; margin-bottom: 8px; }
.stat-value { font-size: 36px; font-weight: bold; }
.stat-value.total { color: #409EFF; }
.stat-value.free { color: #67C23A; }
.stat-value.occupied { color: #F56C6C; }
.floor-header { display: flex; justify-content: space-between; align-items: center; }
.room-section { margin-bottom: 20px; }
.room-title { font-weight: bold; margin-bottom: 10px; padding: 5px 10px; background: #f5f7fa; border-radius: 4px; }
.bed-item { border: 1px solid #dcdfe6; border-radius: 8px; padding: 15px; text-align: center; margin-bottom: 10px; }
.bed-item.free { background: #f0f9eb; border-color: #67C23A; }
.bed-item.occupied { background: #fef0f0; border-color: #F56C6C; }
.bed-no { font-size: 16px; font-weight: bold; margin-bottom: 5px; }
.bed-status { font-size: 12px; color: #606266; }
.bed-customer { font-size: 12px; color: #409EFF; margin-top: 5px; }
</style>
