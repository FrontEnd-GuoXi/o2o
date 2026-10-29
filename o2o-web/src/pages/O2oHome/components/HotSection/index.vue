<template>
  <!-- 热门头条区域 -->
  <div class="hot-section">
    <div class="section-header">
      <div class="section-title">
        <van-icon name="fire-o" color="#f56c6c" size="20" />
        <span>热门头条</span>
      </div>
      <div class="section-tag">精选推荐</div>
    </div>

    <van-swipe
      v-if="recommendShopList.length"
      class="hot-swipe"
      :autoplay="3000"
      :loop="true"
      :show-indicators="recommendShopList.length > 1"
      indicator-color="#f56c6c"
    >
      <van-swipe-item
        v-for="shop in recommendShopList"
        :key="shop.shopId"
        class="hot-swipe-item"
      >
        <div class="shop-card" @click="goToShopDetail(shop)">
          <div class="shop-image-wrapper">
            <img
              :src="getImageUrl(shop.shopImg)"
              :alt="shop.shopName"
              class="shop-image"
              @error="handleImageError"
            />
            <div class="headline-tag">头条</div>
          </div>
          <div class="shop-info">
            <div class="shop-name-rating">
              <h3 class="shop-name">{{ shop.shopName }}</h3>
              <div class="shop-rating">
                <van-icon name="star" color="#fadb14" size="16" />
                <span class="rating-score">{{ formatScore(shop.avgScore) }}</span>
              </div>
            </div>
            <p class="shop-description">{{ shop.shopDesc || '暂无简介' }}</p>
            <div class="shop-category">
              <span>{{ shop.shopCategoryName || '暂无分类' }}</span>
              <van-icon name="arrow-right" color="#909399" size="14" />
            </div>
          </div>
        </div>
      </van-swipe-item>
    </van-swipe>

    <div v-else class="shop-card-list">
      <div class="shop-card">
        <div class="shop-info">
          <p class="shop-description">{{ loading ? '加载中...' : '暂无热门头条' }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { showToast, Icon as VanIcon } from 'vant'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getRecommendShopList, type RecommendShop } from '@/api/recommend'
import { getShopDetailById } from '@/api/shop'
import { getImageUrl, handleImageError } from '@/utils/image'

const router = useRouter()

const loading = ref(false)
const recommendShopList = ref<RecommendShop[]>([])

const formatScore = (score: RecommendShop['avgScore']) => {
  if (score === null || score === undefined || score === '') return '--'
  const num = Number(score)
  if (Number.isNaN(num)) return '--'
  return num.toFixed(1)
}

const fetchRecommendShops = async () => {
  loading.value = true
  try {
    const res = await getRecommendShopList('avg_score')
    recommendShopList.value = res.data || []
  } catch (error) {
    showToast({
      message: error instanceof Error ? error.message : '获取热门头条失败',
      position: 'top',
      duration: 3000,
    })
  } finally {
    loading.value = false
  }
}

const goToShopDetail = async (shop: RecommendShop) => {
  try {
    const res = await getShopDetailById(shop.shopId)
    const shopData = res.data ? JSON.parse(JSON.stringify(res.data)) : undefined

    await router.push({
      path: '/shopDetail',
      query: { shopId: shop.shopId.toString() },
      state: shopData ? { shopData } : undefined,
    })
  } catch (error) {
    console.error('获取店铺详情失败:', error)
    showToast({
      message: error instanceof Error ? error.message : '获取店铺详情失败',
      position: 'top',
      duration: 3000,
    })
  }
}

onMounted(() => {
  fetchRecommendShops()
})
</script>

<style scoped>
@import './style.css';
</style>
