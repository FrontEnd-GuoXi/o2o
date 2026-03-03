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

    <!-- 店铺产品列表 -->
    <div class="product-section">
      <div class="section-title-wrapper">
        <h2 class="section-title">店铺商品</h2>
      </div>

      <div class="product-list">
        <div v-for="product in products" :key="product.productId" class="product-item">
          <div class="product-img-wrapper">
            <img :src="getImageUrl(product.imgAddr)" :alt="product.productName" @error="handleImageError" />
          </div>
          <div class="product-info">
            <h3 class="product-name">{{ product.productName }}</h3>
            <p class="product-desc">{{ product.productDesc }}</p>
            <div class="product-bottom">
              <div class="product-price">
                <span v-if="product.promotionPrice" class="promo-price">¥{{ product.promotionPrice }}</span>
                <span :class="{ 'original-price': product.promotionPrice, 'normal-price': !product.promotionPrice }">
                  ¥{{ product.normalPrice }}
                </span>
              </div>
              <van-button size="mini" type="danger" plain round @click.stop="addToCart(product)">
                加入购物车
              </van-button>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <van-empty v-if="products.length === 0" description="该店铺暂无商品" />
      </div>
    </div>

    <!-- 底部操作栏 -->
    <div class="bottom-action">
      <van-button type="primary" block round @click="handleContact">
        立即咨询
      </van-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import {
  Divider as VanDivider,
  Cell as VanCell,
  CellGroup as VanCellGroup,
  Tag as VanTag,
  Button as VanButton,
  Empty as VanEmpty,
  showToast
} from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import { getImageUrl, handleImageError } from '@/utils/image'

const route = useRoute()
const shopId = route.query.shopId as string

// 获取初始数据
const getInitialShopData = () => {
  // 优先从 window.history.state 中获取传递过来的 shopData
  console.log('正在尝试获取传递的 shopData...')
  const state = window.history.state
  console.log('当前 window.history.state:', state)
  if (state && state.shopData) {
    console.log('成功获取到传递的 shopData:', state.shopData)
    return { ...state.shopData }
  }

  // 兜底默认数据
  return {
    shopId: shopId || '',
    shopName: '正在加载...',
    shopDesc: '',
    shopAddr: '',
    phone: '',
    shopImg: '',
    priority: 1,
    enableStatus: 1,
    shopCategoryName: '',
    areaName: ''
  }
}

const shop = ref(getInitialShopData())

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

// 产品列表
const products = ref<any[]>([])

// 模拟产品数据 (当没有真实接口时展示)
const mockProducts = [
  {
    productId: 1,
    productName: '旗舰手机 Pro',
    productDesc: '高性能处理器，专业级摄像系统，全天候长效续航。',
    imgAddr: '',
    normalPrice: '5999',
    promotionPrice: '5499',
    enableStatus: 1
  },
  {
    productId: 2,
    productName: '无线蓝牙耳机',
    productDesc: '主动降噪，高保真音质，佩戴舒适。',
    imgAddr: '',
    normalPrice: '1299',
    promotionPrice: null,
    enableStatus: 1
  }
]



const callPhone = () => {
  if (shop.value.phone) {
    window.location.href = `tel:${shop.value.phone}`
  }
}

const handleContact = () => {
  showToast('联系功能开发中...')
}

const addToCart = (product: any) => {
  showToast(`已将 ${product.productName} 加入购物车`)
}

onMounted(() => {
  console.log('加载店铺详情, ID:', shopId)

  // 再次检查历史状态，确保数据正确应用
  checkHistoryState()

  // 暂时使用模拟商品数据
  products.value = mockProducts
})
</script>

<style scoped>
@import './style.css';
</style>
