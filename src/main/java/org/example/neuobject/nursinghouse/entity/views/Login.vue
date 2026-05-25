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
        <p>管理员账号：admin / admin</p>
        <p>管理员账号：admin1 / admin1</p>
        <p>管理员账号：admin2 / admin2</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loginFormRef = ref()

const loginForm = reactive({ username: '', password: '' })

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = () => {
  loginFormRef.value.validate((valid) => {
    if (valid) {
      // ===== 模拟登录（前后端联调前使用）=====
      // 后端完成后替换为 axios.post('/api/login', ...)
      if (!loginForm.username || !loginForm.password) {
        ElMessage.warning('请输入账号和密码')
        return
      }

      const mockUsers = [
        { username: 'admin', password: 'admin', role: 'admin' },
        { username: 'admin1', password: 'admin1', role: 'admin' },
        { username: 'admin2', password: 'admin2', role: 'admin' },
        { username: 'health', password: '123456', role: 'health_manager' }
      ]

      const user = mockUsers.find(
        u => u.username === loginForm.username && u.password === loginForm.password
      )

      if (user) {
        localStorage.setItem('token', 'mock-token-' + Date.now())
        localStorage.setItem('role', user.role)
        localStorage.setItem('username', user.username)
        ElMessage.success('登录成功！欢迎 ' + user.username)
        router.push('/dashboard')
      } else {
        ElMessage.error('账号或密码错误')
      }
      // ===== 模拟登录结束 =====
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
