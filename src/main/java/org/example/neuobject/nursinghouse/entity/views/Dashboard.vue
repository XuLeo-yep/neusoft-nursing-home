<template>
  <div class="dashboard">
    <h2>欢迎使用东软颐养中心管理系统</h2>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><User /></el-icon>
              <span>客户总数</span>
            </div>
          </template>
          <div class="card-body">
            <span class="number">128</span>
            <span class="unit">人</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><HomeFilled /></el-icon>
              <span>床位使用</span>
            </div>
          </template>
          <div class="card-body">
            <span class="number">85</span>
            <span class="unit">/ 120</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><FirstAidKit /></el-icon>
              <span>护理项目</span>
            </div>
          </template>
          <div class="card-body">
            <span class="number">24</span>
            <span class="unit">项</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><Service /></el-icon>
              <span>健康管家</span>
            </div>
          </template>
          <div class="card-body">
            <span class="number">12</span>
            <span class="unit">人</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header><span>最近入住</span></template>
          <el-table :data="recentCheckins" size="small">
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="room" label="房间号" />
            <el-table-column prop="bed" label="床位号" />
            <el-table-column prop="date" label="入住日期" />
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header><span>待办事项</span></template>
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
import { ref } from 'vue'

const recentCheckins = ref([
  { name: '张三', room: 'A101', bed: '01', date: '2026-05-20' },
  { name: '李四', room: 'A102', bed: '02', date: '2026-05-18' },
  { name: '王五', room: 'B201', bed: '01', date: '2026-05-15' }
])

const todos = ref([
  { content: '审批张三的外出申请', time: '10:30' },
  { content: '处理李四的退住申请', time: '09:00' },
  { content: '完成王五的日常护理记录', time: '昨天' }
])
</script>

<style scoped>
.dashboard h2 { color: #303133; }
.stat-card .card-header { display: flex; align-items: center; gap: 8px; color: #606266; }
.stat-card .card-body { text-align: center; }
.stat-card .number { font-size: 36px; font-weight: bold; color: #409EFF; }
.stat-card .unit { font-size: 14px; color: #909399; margin-left: 5px; }
</style>
