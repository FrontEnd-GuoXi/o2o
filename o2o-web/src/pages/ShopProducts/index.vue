<template>
  <div class="shop-products-container">
    <O2oHeader :title="shop.shopName || '店铺商品'" />

    <div class="shop-summary">
      <div class="shop-summary-name">{{ shop.shopName || '店铺商品' }}</div>
      <div class="shop-summary-desc">{{ shop.shopDesc || '挑选你喜欢的商品吧' }}</div>
    </div>

    <div class="product-section">
      <div class="section-title-wrapper">
        <h2 class="section-title">全部商品</h2>
      </div>

      <div class="product-list">
        <div v-for="product in products" :key="product.productId" class="product-item">
          <div class="product-img-wrapper">
            <img :src="getImageUrl(product.imgAddr)" :alt="product.productName" @error="handleImageError" />
          </div>
          <div class="product-info">
            <h3 class="product-name">{{ product.productName }}</h3>
            <p class="product-desc">{{ product.productDesc }}</p>
            <div class="product-inventory">
              库存: <span :class="{ 'low-stock': product.productNumber < 10 }">{{ product.productNumber }}</span>
            </div>
            <div class="product-bottom">
              <div class="product-price">
                <span v-if="product.promotionPrice" class="promo-price">¥{{ product.promotionPrice }}</span>
                <span :class="{ 'original-price': product.promotionPrice, 'normal-price': !product.promotionPrice }">
                  ¥{{ product.normalPrice }}
                </span>
              </div>
              <van-button
                size="mini"
                type="danger"
                plain
                round
                :disabled="product.productNumber <= 0"
                @click.stop="addToCart(product)"
              >
                {{ product.productNumber <= 0 ? '无货' : '加入购物车' }}
              </van-button>
            </div>
          </div>
        </div>

        <van-empty v-if="products.length === 0" description="该店铺暂无商品" />
      </div>
    </div>

    <div class="bottom-action">
      <div class="cart-badge-wrapper" @click="router.push('/cart')">
        <van-icon name="shopping-cart-o" size="24" :badge="cartStore.totalCount || ''" />
        <span class="cart-text">购物车</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Button as VanButton,
  Empty as VanEmpty,
  Icon as VanIcon,
  showToast
} from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import { getImageUrl, handleImageError } from '@/utils/image'
import { getProductBriefListByShopId, getShopByShopId, type ProductBrief, type Shop } from '@/api/shop'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const router = useRouter()
const shopId = route.query.shopId as string
const cartStore = useCartStore()

const getInitialShopData = (): Shop => {
  const state = window.history.state
  if (state && state.shopData) {
    return { ...state.shopData } as Shop
  }

  return {
    shopId: Number(shopId || 0),
    shopName: route.query.shopName as string || '店铺商品',
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

const shop = ref<Shop>(getInitialShopData())
const products = ref<ProductBrief[]>([])

const fetchShopDetail = async () => {
  if (!shopId) return

  try {
    const res = await getShopByShopId(shopId)
    if (res.data) {
      shop.value = res.data
    }
  } catch (error) {
    console.error('获取店铺详情失败:', error)
    showToast('获取店铺详情失败')
  }
}

const fetchProducts = async () => {
  if (!shopId) return

  try {
    const res = await getProductBriefListByShopId(shopId)
    if (res.data) {
      products.value = res.data
    }
  } catch (error) {
    console.error('获取商品列表失败:', error)
    showToast('获取商品列表失败')
  }
}

const addToCart = (product: ProductBrief) => {
  if (product.productNumber <= 0) {
    showToast('该商品暂时无货')
    return
  }

  cartStore.addToCart(product, shop.value)
  showToast({
    message: `已将 ${product.productName} 加入购物车`,
    type: 'success',
    duration: 1000
  })
}

onMounted(() => {
  cartStore.fetchCartFromBackend()
  fetchShopDetail()
  fetchProducts()
})
</script>

<style scoped>
.shop-products-container {
  min-height: 100vh;
  background-color: #f7f8fa;
  padding-bottom: 80px;
}

.shop-summary {
  margin: 12px;
  padding: 16px;
  background: #fff;
  border-radius: 12px;
}

.shop-summary-name {
  font-size: 18px;
  font-weight: 600;
  color: #323233;
}

.shop-summary-desc {
  margin-top: 8px;
  font-size: 13px;
  line-height: 1.6;
  color: #646566;
}

.product-section {
  background-color: #fff;
  padding: 16px;
}

.section-title-wrapper {
  margin-bottom: 16px;
  border-left: 4px solid #1989fa;
  padding-left: 10px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #323233;
  margin: 0;
}

.product-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.product-item {
  display: flex;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebedf0;
}

.product-item:last-child {
  border-bottom: none;
}

.product-img-wrapper {
  width: 100px;
  height: 100px;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 12px;
  flex-shrink: 0;
  background-color: #f7f8fa;
}

.product-img-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-name {
  font-size: 16px;
  font-weight: bold;
  color: #323233;
  margin: 0 0 4px 0;
}

.product-desc {
  font-size: 12px;
  color: #969799;
  line-height: 1.4;
  margin: 0 0 8px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-inventory {
  font-size: 12px;
  color: #969799;
  margin-bottom: 4px;
}

.low-stock {
  color: #ee0a24;
  font-weight: bold;
}

.product-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-price {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.promo-price {
  font-size: 16px;
  color: #ee0a24;
  font-weight: bold;
}

.normal-price {
  font-size: 16px;
  color: #ee0a24;
  font-weight: bold;
}

.original-price {
  font-size: 12px;
  color: #969799;
  text-decoration: line-through;
}

.bottom-action {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 10px 16px;
  background-color: #fff;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: flex-start;
  z-index: 100;
}

.cart-badge-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-width: 60px;
  cursor: pointer;
  color: #646566;
}

.cart-text {
  font-size: 10px;
  margin-top: 2px;
}
</style>
