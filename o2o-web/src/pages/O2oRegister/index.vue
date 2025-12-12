<template>
  <div class="auth-container">
    <div class="auth-box">
      <h2>注册账号</h2>
      <form @submit.prevent="handleRegister">
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
          />
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
          />
        </div>
        <div class="form-item">
          <label for="confirmPassword">确认密码</label>
          <input
            type="password"
            id="confirmPassword"
            v-model="form.confirmPassword"
            placeholder="请再次输入密码"
            required
            minlength="6"
            maxlength="20"
          />
        </div>
        <div class="form-item">
          <label for="name">名称</label>
          <input
            type="text"
            id="name"
            v-model="form.name"
            placeholder="请输入名称（1-50字符）"
            required
            minlength="1"
            maxlength="50"
          />
        </div>
        <div class="form-item">
          <label>性别</label>
          <div class="gender-selector">
            <label class="gender-option">
              <input type="radio" v-model="form.gender" value="男" required />
              <span>男</span>
            </label>
            <label class="gender-option">
              <input type="radio" v-model="form.gender" value="女" required />
              <span>女</span>
            </label>
          </div>
        </div>
        <div class="form-item">
          <label>用户类型</label>
          <div class="user-type-selector">
            <label class="user-type-option">
              <input type="radio" v-model="form.userType" :value="1" required />
              <span>顾客</span>
            </label>
            <label class="user-type-option">
              <input type="radio" v-model="form.userType" :value="2" required />
              <span>店家</span>
            </label>
          </div>
        </div>
        <div class="form-item">
          <label for="profileImg">头像（可选）</label>
          <input type="url" id="profileImg" v-model="form.profileImg" placeholder="请输入头像URL" />
        </div>
        <button type="submit" class="auth-btn register-btn" :disabled="isLoading">
          {{ isLoading ? '注册中...' : '注册' }}
        </button>
      </form>
      <div class="auth-link">
        <span>已有账号？</span>
        <router-link to="/login">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register, type RegisterRequest } from '@/api/auth'
import { showToast } from 'vant'

const router = useRouter()
const form = ref({
  identifier: '',
  credential: '',
  confirmPassword: '',
  name: '',
  gender: '',
  userType: 1,
  profileImg: '',
} as RegisterRequest & { confirmPassword: string })

const isLoading = ref(false)

const handleRegister = async () => {
  // 表单验证
  if (form.value.credential !== form.value.confirmPassword) {
    showToast({
      message: '两次输入的密码不一致，请重新输入',
      position: 'top',
      duration: 2000,
    })
    return
  }

  if (form.value.identifier.length < 6 || form.value.identifier.length > 20) {
    showToast({
      message: '账号长度必须在6-20字符之间',
      position: 'top',
      duration: 2000,
    })
    return
  }

  if (form.value.credential.length < 6 || form.value.credential.length > 20) {
    showToast({
      message: '密码长度必须在6-20字符之间',
      position: 'top',
      duration: 2000,
    })
    return
  }

  if (form.value.name.length < 1 || form.value.name.length > 50) {
    showToast({
      message: '名称长度必须在1-50字符之间',
      position: 'top',
      duration: 2000,
    })
    return
  }

  if (!form.value.gender) {
    showToast({
      message: '请选择性别',
      position: 'top',
      duration: 2000,
    })
    return
  }

  try {
    isLoading.value = true

    // 调用注册接口
    const registerData: RegisterRequest = {
      identifier: form.value.identifier,
      credential: form.value.credential,
      name: form.value.name,
      gender: form.value.gender,
      userType: form.value.userType,
      profileImg: form.value.profileImg,
    }

    const response = await register(registerData)

    console.log('注册成功:', response)

    // 注册成功后跳转到登录页面
    showToast({
      message: '注册成功，请登录',
      position: 'top',
      duration: 2000,
    })
    setTimeout(() => {
      router.push('/login')
    }, 1500)
  } catch (error) {
    console.error('注册失败:', error)
    // 错误提示已在拦截器中处理，这里不需要重复提示
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
/* 引入公共样式文件 */
@import '@/styles/common.css';
/* 引入注册页面特定样式 */
@import './register.css';
</style>
