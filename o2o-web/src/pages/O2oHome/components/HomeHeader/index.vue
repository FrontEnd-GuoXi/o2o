<template>
  <div class="home-header">
    <div class="header-left">
      <van-icon name="shop-o" color="#1989fa" size="28" />
      <span class="header-title">商铺导航</span>
    </div>
    <div class="header-right">
      <img
        src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d"
        alt="用户头像"
        class="user-avatar"
        @error="handleImageError"
        @click="goToPersonalInfo"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { Icon as VanIcon } from 'vant'
import { useRouter } from 'vue-router'
import { handleImageError } from '@/utils/image'

const router = useRouter()

// 跳转到个人信息页面
const goToPersonalInfo = () => {
  router.push('/personalInfo')
}
</script>

<style scoped>
@import './style.css';
</style>
