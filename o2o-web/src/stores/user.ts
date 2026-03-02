import { ref } from 'vue'
import { defineStore } from 'pinia'
import { getUserInfo, type UserInfo } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<UserInfo | null>(null)

  const fetchUserInfo = async () => {
    try {
      const res = await getUserInfo()
      if (res.data) {
        userInfo.value = res.data
      }
      return res.data
    } catch (error) {
      console.error('获取用户信息失败:', error)
      return null
    }
  }

  const clearUserInfo = () => {
    userInfo.value = null
  }

  return {
    userInfo,
    fetchUserInfo,
    clearUserInfo
  }
})
