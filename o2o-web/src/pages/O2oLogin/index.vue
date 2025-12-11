<template>
  <div class="auth-container">
    <div class="auth-box">
      <h2>登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-item">
          <label for="username">用户名</label>
          <input
            type="text"
            id="username"
            v-model="form.identifier"
            placeholder="请输入用户名（6-20字符）"
            required
            minlength="6"
            maxlength="20"
          >
        </div>
        <div class="form-item">
          <label for="password">密码</label>
          <input
            type="password"
            id="password"
            v-model="form.credential"
            placeholder="请输入密码（6-20字符）"
            required
            minlength="6"
            maxlength="20"
          >
        </div>
        <button type="submit" class="auth-btn login-btn" :disabled="isLoading">
          {{ isLoading ? '登录中...' : '登录' }}
        </button>
      </form>
      <div class="auth-link">
        <span>还没有账号？</span>
        <router-link to="/register">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login, type LoginRequest } from '@/api/auth'
import { showToast } from 'vant'

const router = useRouter()
const form = ref<LoginRequest>({
  identifier: '',
  credential: ''
})

const isLoading = ref(false)

const handleLogin = async () => {
  // 表单验证
  if (form.value.identifier.length < 6 || form.value.identifier.length > 20) {
    showToast({
      message: '账号长度必须在6-20字符之间',
      position: 'top',
      duration: 2000
    })
    return
  }

  if (form.value.credential.length < 6 || form.value.credential.length > 20) {
    showToast({
      message: '密码长度必须在6-20字符之间',
      position: 'top',
      duration: 2000
    })
    return
  }

  try {
    isLoading.value = true

    // 调用登录接口
    const response = await login(form.value)
    localStorage.setItem('token', response.data);

    // 登录成功后跳转到首页或其他页面
    showToast({
      message: '登录成功',
      position: 'top',
      duration: 2000
    })
    // 这里可以根据实际需求修改跳转路径
    setTimeout(() => {
      router.push('/addShop')
    }, 1500)
  } catch (error) {
    console.error('登录失败:', error)
    // 错误提示已在拦截器中处理，这里不需要重复提示
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
/* 引入公共样式文件 */
@import '@/styles/common.css';
/* 引入登录页面特定样式 */
@import './login.css';
</style>
