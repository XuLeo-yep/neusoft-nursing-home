<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="logo">
        <h3>颐养中心</h3>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataLine /></el-icon>
          <span>首页</span>
        </el-menu-item>
        
        <el-sub-menu index="customer">
          <template #title>
            <el-icon><User /></el-icon>
            <span>客户管理</span>
          </template>
          <el-menu-item index="/customer/checkin">入住登记</el-menu-item>
          <el-menu-item index="/customer/checkout">退住登记</el-menu-item>
          <el-menu-item index="/customer/goout">外出登记</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="bed" v-if="role === 'admin'">
          <template #title>
            <el-icon><HomeFilled /></el-icon>
            <span>床位管理</span>
          </template>
          <el-menu-item index="/bed/diagram">床位示意图</el-menu-item>
          <el-menu-item index="/bed/manage">床位管理</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="nurse" v-if="role === 'admin'">
          <template #title>
            <el-icon><FirstAidKit /></el-icon>
            <span>护理管理</span>
          </template>
          <el-menu-item index="/nurse/level">护理级别</el-menu-item>
          <el-menu-item index="/nurse/project">护理项目</el-menu-item>
          <el-menu-item index="/nurse/setting">客户护理设置</el-menu-item>
          <el-menu-item index="/nurse/record">护理记录</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="manager">
          <template #title>
            <el-icon><Service /></el-icon>
            <span>健康管家</span>
          </template>
          <el-menu-item index="/manager/service" v-if="role === 'admin'">设置服务对象</el-menu-item>
          <el-menu-item index="/manager/daily">日常护理</el-menu-item>
          <el-menu-item index="/nurse/record">护理记录</el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="/user/manage" v-if="role === 'admin'">
          <el-icon><Setting /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header>
        <div class="header-left">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-icon><User /></el-icon>
              {{ username }}
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const username = ref(localStorage.getItem('username') || '用户')
const role = ref(localStorage.getItem('role') || 'admin')
const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  if (command === 'logout') {
    localStorage.clear()
    ElMessage.success('已退出登录')
    router.push('/login')
  }
}
</script>

<style scoped>
.layout-container { height: 100vh; }
.el-aside { background-color: #304156; color: #bfcbd9; }
.logo {
  height: 60px; display: flex; align-items: center; justify-content: center;
  color: #409EFF; border-bottom: 1px solid #1f2d3d;
}
.el-menu { border-right: none; }
.el-header {
  background: white; display: flex; align-items: center; justify-content: flex-end;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}
.user-info {
  display: flex; align-items: center; gap: 5px; cursor: pointer; color: #606266;
}
.el-main { background: #f0f2f5; padding: 20px; }
</style>
