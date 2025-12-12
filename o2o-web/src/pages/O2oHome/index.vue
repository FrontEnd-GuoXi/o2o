<template>
  <div class="home-container">
    <!-- 顶部导航栏 -->
    <HomeHeader />

    <!-- 主要内容区 -->
    <div class="home-content">
      <!-- 热门头条区域 -->
      <HotSection />

      <!-- 商铺分类区域 -->
      <CategorySection />
    </div>
  </div>
</template>

<script setup lang="ts">
import HomeHeader from './components/HomeHeader/index.vue'
import HotSection from './components/HotSection/index.vue'
import CategorySection from './components/CategorySection/index.vue'
</script>

<style scoped>
@import './style.css';
</style>
