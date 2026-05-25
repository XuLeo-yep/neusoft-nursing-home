<template>
  <div class="page-container">
    <h2 class="page-title">床位示意图</h2>

    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-item">
            <div class="stat-label">总床位数</div>
            <div class="stat-value total">{{ totalBeds }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-item">
            <div class="stat-label">空闲床位</div>
            <div class="stat-value free">{{ freeBeds }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-item">
            <div class="stat-label">有人占用</div>
            <div class="stat-value occupied">{{ occupiedBeds }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-item">
            <div class="stat-label">外出状态</div>
            <div class="stat-value out">{{ outBeds }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="floor-card" shadow="never" style="margin-top: 20px;">
      <template #header>
        <div class="floor-header">
          <span>楼层选择</span>
          <el-radio-group v-model="currentFloor" size="small">
            <el-radio-button v-for="floor in floors" :key="floor" :label="floor">{{ floor }}</el-radio-button>
          </el-radio-group>
        </div>
      </template>

      <div v-for="room in currentRooms" :key="room.roomNo" class="room-section">
        <div class="room-title">{{ room.roomNo }}（{{ room.beds.length }}个床位）</div>
        <el-row :gutter="10">
          <el-col v-for="bed in room.beds" :key="bed.bedNo" :span="4">
            <div class="bed-item" :class="bed.status">
              <div class="bed-no">{{ bed.bedNo }}号床</div>
              <div class="bed-status">{{ statusText[bed.status] }}</div>
              <div v-if="bed.customerName" class="bed-customer">{{ bed.customerName }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <div class="legend" style="margin-top: 15px;">
      <span class="legend-item"><span class="legend-dot free"></span> 空闲</span>
      <span class="legend-item"><span class="legend-dot occupied"></span> 有人</span>
      <span class="legend-item"><span class="legend-dot out"></span> 外出</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const floors = ['一层', '二层', '三层']
const currentFloor = ref('一层')
const statusText = { free: '空闲', occupied: '有人', out: '外出' }

const roomData = {
  '一层': [
    { roomNo: 'A101', beds: [
      { bedNo: '01', status: 'occupied', customerName: '张三' },
      { bedNo: '02', status: 'occupied', customerName: '李四' },
      { bedNo: '03', status: 'free', customerName: '' }
    ]},
    { roomNo: 'A102', beds: [
      { bedNo: '01', status: 'free', customerName: '' },
      { bedNo: '02', status: 'occupied', customerName: '王五' }
    ]},
    { roomNo: 'A103', beds: [
      { bedNo: '01', status: 'occupied', customerName: '赵六' },
      { bedNo: '02', status: 'free', customerName: '' },
      { bedNo: '03', status: 'free', customerName: '' },
      { bedNo: '04', status: 'occupied', customerName: '钱七' }
    ]},
    { roomNo: 'A104', beds: [
      { bedNo: '01', status: 'out', customerName: '孙八' },
      { bedNo: '02', status: 'free', customerName: '' }
    ]}
  ],
  '二层': [
    { roomNo: 'B201', beds: [
      { bedNo: '01', status: 'occupied', customerName: '周九' },
      { bedNo: '02', status: 'occupied', customerName: '吴十' }
    ]},
    { roomNo: 'B202', beds: [
      { bedNo: '01', status: 'free', customerName: '' },
      { bedNo: '02', status: 'free', customerName: '' },
      { bedNo: '03', status: 'occupied', customerName: '郑十一' }
    ]},
    { roomNo: 'B203', beds: [
      { bedNo: '01', status: 'occupied', customerName: '冯十二' },
      { bedNo: '02', status: 'out', customerName: '陈十三' }
    ]},
    { roomNo: 'B204', beds: [
      { bedNo: '01', status: 'free', customerName: '' },
      { bedNo: '02', status: 'occupied', customerName: '褚十四' },
      { bedNo: '03', status: 'free', customerName: '' },
      { bedNo: '04', status: 'occupied', customerName: '卫十五' }
    ]}
  ],
  '三层': [
    { roomNo: 'C301', beds: [
      { bedNo: '01', status: 'free', customerName: '' },
      { bedNo: '02', status: 'free', customerName: '' },
      { bedNo: '03', status: 'occupied', customerName: '蒋十六' }
    ]},
    { roomNo: 'C302', beds: [
      { bedNo: '01', status: 'occupied', customerName: '沈十七' },
      { bedNo: '02', status: 'free', customerName: '' }
    ]},
    { roomNo: 'C303', beds: [
      { bedNo: '01', status: 'free', customerName: '' },
      { bedNo: '02', status: 'occupied', customerName: '韩十八' },
      { bedNo: '03', status: 'free', customerName: '' },
      { bedNo: '04', status: 'free', customerName: '' }
    ]},
    { roomNo: 'C304', beds: [
      { bedNo: '01', status: 'occupied', customerName: '杨十九' },
      { bedNo: '02', status: 'occupied', customerName: '朱二十' }
    ]}
  ]
}

const currentRooms = computed(() => roomData[currentFloor.value] || [])

const allBeds = computed(() => {
  let beds = []
  Object.values(roomData).forEach(floor => {
    floor.forEach(room => {
      beds.push(...room.beds)
    })
  })
  return beds
})

const totalBeds = computed(() => allBeds.value.length)
const freeBeds = computed(() => allBeds.value.filter(b => b.status === 'free').length)
const occupiedBeds = computed(() => allBeds.value.filter(b => b.status === 'occupied').length)
const outBeds = computed(() => allBeds.value.filter(b => b.status === 'out').length)
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
.stat-value.out { color: #E6A23C; }

.floor-header { display: flex; justify-content: space-between; align-items: center; }

.room-section { margin-bottom: 20px; }
.room-title { font-weight: bold; margin-bottom: 10px; padding: 5px 10px; background: #f5f7fa; border-radius: 4px; }

.bed-item {
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  padding: 15px;
  text-align: center;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.3s;
}
.bed-item:hover { transform: translateY(-2px); box-shadow: 0 2px 12px rgba(0,0,0,0.1); }
.bed-item.free { background: #f0f9eb; border-color: #67C23A; }
.bed-item.occupied { background: #fef0f0; border-color: #F56C6C; }
.bed-item.out { background: #fdf6ec; border-color: #E6A23C; }

.bed-no { font-size: 16px; font-weight: bold; margin-bottom: 5px; }
.bed-status { font-size: 12px; color: #606266; }
.bed-customer { font-size: 12px; color: #409EFF; margin-top: 5px; }

.legend { display: flex; gap: 20px; }
.legend-item { display: flex; align-items: center; gap: 5px; font-size: 14px; }
.legend-dot { width: 12px; height: 12px; border-radius: 50%; display: inline-block; }
.legend-dot.free { background: #67C23A; }
.legend-dot.occupied { background: #F56C6C; }
.legend-dot.out { background: #E6A23C; }
</style>
