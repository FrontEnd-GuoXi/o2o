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

    <!-- 悬浮购物车按钮 -->
    <div class="floating-cart" @click="router.push('/cart')">
      <van-icon name="shopping-cart-o" size="24" :badge="cartStore.totalCount || ''" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { Icon as VanIcon } from 'vant'
import { useCartStore } from '@/stores/cart'
import HomeHeader from './components/HomeHeader/index.vue'
import HotSection from './components/HotSection/index.vue'
import CategorySection from './components/CategorySection/index.vue'

const router = useRouter()
const cartStore = useCartStore()
</script>

<style scoped>
@import './style.css';
</style>
