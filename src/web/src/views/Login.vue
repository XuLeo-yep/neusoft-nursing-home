<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2>东软颐养中心管理系统</h2>
      </template>

      <el-form :model="loginForm" :rules="rules" ref="loginFormRef">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入账号" prefix-icon="User" />
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%">登录</el-button>
        </el-form-item>
      </el-form>

      <div class="tips">
        <p>管理员账号：admin1 / admin1</p>
        <p>管理员账号：admin2 / admin2</p>
        <p>管理员账号：admin3 / admin3</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const loginFormRef = ref()
const userStore = useUserStore()

const loginForm = reactive({ username: '', password: '' })

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = () => {
  loginFormRef.value.validate((valid) => {
    if (valid) {
      request.post('/user/login', {
        username: loginForm.username,
        password: loginForm.password
      }).then(res => {
        const { token, role, username, userId } = res.data
        // 后端 role 是 health_worker，前端路由用 health_manager，统一映射
        const mappedRole = role === 'health_worker' ? 'health_manager' : role
        userStore.setUserInfo({ token, role: mappedRole, username, userId })
        ElMessage.success('登录成功！欢迎 ' + username)
        router.push('/dashboard')
      }).catch(err => {
        ElMessage.error(err.response?.data?.msg || '登录失败')
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
}

.login-card h2 {
  text-align: center;
  color: #409eff;
}

.tips {
  margin-top: 20px;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
  font-size: 12px;
  color: #909399;
}

.tips p {
  margin: 5px 0;
}
</style>
