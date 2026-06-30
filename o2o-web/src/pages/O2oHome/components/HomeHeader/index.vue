<template>
  <div class="home-header">
    <div class="header-left">
      <van-icon name="shop-o" color="#1989fa" size="28" />
      <span class="header-title">商铺导航</span>
    </div>
    <div class="header-right">
      <img
        :src="avatarUrl"
        alt="用户头像"
        class="user-avatar"
        @error="handleAvatarError"
        @click="goToPersonalInfo"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Icon as VanIcon } from 'vant'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getImageUrl, handleImageError } from '@/utils/image'

const router = useRouter()
const userStore = useUserStore()
const DEFAULT_AVATAR_URL = 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d'

const avatarUrl = computed(() => {
  const profileImg = userStore.userInfo?.profileImg
  console.log(userStore.userInfo)
  return profileImg ? getImageUrl(profileImg) : DEFAULT_AVATAR_URL
})

const handleAvatarError = (event: Event) => {
  handleImageError(event, DEFAULT_AVATAR_URL)
}

// 跳转到个人信息页面
const goToPersonalInfo = () => {
  router.push('/personalInfo')
}
</script>

<style scoped>
@import './style.css';
</style>
