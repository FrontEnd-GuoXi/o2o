<template>
  <div class="shop-detail-container">
    <!-- 顶部导航 -->
    <O2oHeader title="店铺详情" />

    <!-- 店铺主图 -->
    <div class="shop-banner">
      <img :src="getImageUrl(shop.shopImg)" :alt="shop.shopName" @error="handleImageError" />
    </div>

    <!-- 店铺基本信息 -->
    <div class="shop-info-section">
      <div class="shop-header-info">
        <h1 class="shop-title">{{ shop.shopName }}</h1>
        <van-tag type="primary" size="medium">{{ shop.shopCategoryName }}</van-tag>
      </div>

      <div class="shop-desc">
        {{ shop.shopDesc || '暂无店铺描述' }}
      </div>

      <div class="shop-rating-summary">
        <div class="rating-summary-item">
          <span class="rating-summary-label">店铺评分</span>
          <span class="rating-summary-value score">{{ formatAvgScore(shop.avgScore) }}</span>
        </div>
        <div class="rating-summary-item">
          <span class="rating-summary-label">评价人数</span>
          <span class="rating-summary-value">{{ shop.evaluationCount ?? 0 }}人</span>
        </div>
      </div>

      <van-divider />

      <!-- 详细列表 -->
      <van-cell-group inset>
        <van-cell title="详细地址" :label="shop.shopAddr" icon="location-o" />
        <van-cell title="联系电话" :value="shop.phone" icon="phone-o" is-link @click="callPhone" />
        <van-cell title="所在区域" :value="shop.areaName" icon="map-marked" />
        <van-cell title="营业状态" icon="clock-o">
          <template #value>
            <van-tag :type="shop.enableStatus === 1 ? 'success' : 'warning'">
              {{ shop.enableStatus === 1 ? '正在营业' : '暂停营业' }}
            </van-tag>
          </template>
        </van-cell>
      </van-cell-group>
    </div>


    <div class="product-entry-section">
      <div class="section-title-wrapper">
        <h2 class="section-title">店铺商品</h2>
      </div>

      <div class="product-entry-card" @click="goToProducts">
        <div class="product-entry-info">
          <div class="product-entry-title">查看全部商品</div>
          <div class="product-entry-desc">商品列表已拆分到独立页面，浏览和加购更清晰。</div>
        </div>
        <van-icon name="arrow" color="#969799" />
      </div>
    </div>

    <ShopReviewList :reviews="reviews" />

    <!-- 底部操作栏 -->
    <div class="bottom-action">
      <div class="cart-badge-wrapper" @click="router.push('/cart')">
        <van-icon name="shopping-cart-o" size="24" :badge="cartStore.totalCount || ''" />
        <span class="cart-text">购物车</span>
      </div>
      <van-button type="primary" block round @click="handleContact">
        立即咨询
      </van-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Divider as VanDivider,
  Cell as VanCell,
  CellGroup as VanCellGroup,
  Tag as VanTag,
  Button as VanButton,
  Icon as VanIcon,
  showToast
} from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import ShopReviewList from '@/components/ShopReviewList.vue'
import { getImageUrl, handleImageError } from '@/utils/image'
import { getShopDetailById, type Shop } from '@/api/shop'
import { queryEvaluationListByShopId, type StoreReviewItem } from '@/api/evaluation'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const router = useRouter()
const shopId = route.query.shopId as string
const cartStore = useCartStore()

const formatAvgScore = (score?: string | number | null) => {
  if (score === null || score === undefined || score === '') {
    return '暂无'
  }

  const numericScore = Number(score)
  return Number.isNaN(numericScore) ? '暂无' : numericScore.toFixed(1)
}

// 获取初始数据
const getInitialShopData = (): Shop => {
  // 优先从 window.history.state 中获取传递过来的 shopData
  console.log('正在尝试获取传递的 shopData...')
  const state = window.history.state
  console.log('当前 window.history.state:', state)
  if (state && state.shopData) {
    console.log('成功获取到传递的 shopData:', state.shopData)
    return { ...state.shopData } as Shop
  }

  // 兜底默认数据
  return {
    shopId: Number(shopId || 0),
    shopName: '正在加载...',
    shopDesc: '',
    shopAddr: '',
    phone: '',
    shopImg: '',
    priority: 1,
    enableStatus: 1,
    shopCategoryName: '',
    areaName: '',
    avgScore: null,
    evaluationCount: 0,
    createTime: '',
    lastEditTime: '',
    advice: ''
  }
}

const shop = ref(getInitialShopData())

// 获取店铺详情 (兜底方案)
const fetchShopDetail = async () => {
  if (!shopId) return

  try {
    const res = await getShopDetailById(shopId)
    if (res.data) {
      shop.value = res.data
    }
  } catch (error) {
    console.error('获取店铺详情失败:', error)
    showToast('获取店铺详情失败')
  }
}

// 如果进入页面时没拿到数据，或者数据不对，我们在 onMounted 再次检查
// 有时候路由跳转后的历史状态更新会有微小延迟
const checkHistoryState = () => {
  if (shop.value.shopName === '正在加载...' || shop.value.shopId.toString() !== shopId) {
    const state = window.history.state
    if (state && state.shopData) {
      console.log('在 checkHistoryState 中成功获取到 shopData:', state.shopData)
      shop.value = { ...state.shopData }
    } else {
      console.log('在 checkHistoryState 中仍未获取到 shopData，触发 fetchShopDetail')
      fetchShopDetail()
    }
  }
}

const reviews = ref<StoreReviewItem[]>([])

const fetchReviews = async () => {
  if (!shopId) return

  try {
    const res = await queryEvaluationListByShopId(shopId)
    if (res.data) {
      reviews.value = res.data
    }
  } catch (error) {
    console.error('获取店铺评价失败:', error)
    showToast('获取店铺评价失败')
  }
}

const callPhone = () => {
  if (shop.value.phone) {
    window.location.href = `tel:${shop.value.phone}`
  }
}

const handleContact = () => {
  showToast('联系功能开发中...')
}

const goToProducts = () => {
  const shopData = JSON.parse(JSON.stringify(shop.value))
  router.push({
    path: '/shopProducts',
    query: {
      shopId: String(shop.value.shopId || shopId),
      shopName: shop.value.shopName || ''
    },
    state: {
      shopData
    }
  })
}

onMounted(() => {
  console.log('加载店铺详情, ID:', shopId)

  // 同步购物车数据
  cartStore.fetchCartFromBackend()

  // 再次检查历史状态，确保数据正确应用
  checkHistoryState()

  fetchReviews()
})
</script>

<style scoped>
@import './style.css';
</style>
