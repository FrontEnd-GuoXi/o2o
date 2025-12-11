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
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { Icon as VanIcon } from 'vant';

// 处理图片加载错误的函数
const handleImageError = (event: Event) => {
  const target = event.target as HTMLImageElement;
  target.onerror = null;
  target.src = 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c';
};
</script>

<style scoped>
@import './style.css';
</style>