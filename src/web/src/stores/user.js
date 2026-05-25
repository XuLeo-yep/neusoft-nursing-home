import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const username = ref(localStorage.getItem('username') || '')
  const role = ref(localStorage.getItem('role') || '')
  const userId = ref(localStorage.getItem('userId') || '')
  
  const setUserInfo = (userInfo) => {
    token.value = userInfo.token
    username.value = userInfo.username
    role.value = userInfo.role
    userId.value = userInfo.userId
    localStorage.setItem('token', userInfo.token)
    localStorage.setItem('username', userInfo.username)
    localStorage.setItem('role', userInfo.role)
    localStorage.setItem('userId', userInfo.userId)
  }
  
  const logout = () => {
    token.value = ''
    username.value = ''
    role.value = ''
    userId.value = ''
    localStorage.clear()
  }
  
  return {
    token, username, role, userId,
    setUserInfo, logout
  }
})
