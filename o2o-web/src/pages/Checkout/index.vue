<template>
  <div class="checkout-container">
    <O2oHeader title="确认订单" />

    <div class="checkout-content">
      <!-- 收货地址 (模拟) -->
      <div class="address-section" @click="onSelectAddress">
        <div class="address-icon">
          <van-icon name="location-o" size="24" />
        </div>
        <div class="address-info">
          <div class="user-info">
            <span class="name">张三</span>
            <span class="phone">138****8888</span>
          </div>
          <div class="address-detail">
            广东省深圳市南山区某某街道某某大厦
          </div>
        </div>
        <van-icon name="arrow" color="#969799" />
      </div>

      <!-- 商品列表 (按店铺分组) -->
      <div v-for="shop in groupedCheckoutItems" :key="shop.shopId" class="shop-group">
        <div class="shop-header">
          <van-icon name="shop-o" size="18" />
          <span class="shop-name">{{ shop.shopName }}</span>
        </div>
        <div class="product-list">
          <div v-for="item in shop.items" :key="item.productId" class="product-item">
            <img :src="getImageUrl(item.imgAddr)" :alt="item.productName" class="product-img" />
            <div class="product-info">
              <h4 class="product-name">{{ item.productName }}</h4>
              <div class="product-price-row">
                <span class="price">¥{{ item.price }}</span>
                <span class="count">x{{ item.quantity }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 费用明细 -->
      <van-cell-group inset class="fee-group">
        <van-cell title="商品总额" :value="'¥' + totalAmount.toFixed(2)" />
        <van-cell title="运费" value="¥0.00" />
        <van-cell title="优惠券" is-link value="暂无可用" />
      </van-cell-group>
    </div>

    <!-- 底部支付栏 -->
    <van-submit-bar
      :price="totalAmount * 100"
      button-text="立即支付"
      @submit="onPay"
      class="submit-bar"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import {
  Icon as VanIcon,
  Cell as VanCell,
  CellGroup as VanCellGroup,
  SubmitBar as VanSubmitBar,
  showToast,
  showLoadingToast
} from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import { getImageUrl } from '@/utils/image'

const router = useRouter()
const checkoutItems = ref<any[]>([])

onMounted(() => {
  // 从路由 state 中获取结算商品数据
  const state = window.history.state
  if (state && state.checkoutItems) {
    checkoutItems.value = state.checkoutItems
  } else {
    showToast('订单信息失效，请重新结算')
    router.back()
  }
})

// 按店铺分组
const groupedCheckoutItems = computed(() => {
  const groups: { [key: string]: { shopId: string, shopName: string, items: any[] } } = {}
  checkoutItems.value.forEach(item => {
    const shopId = item.shopId || 'unknown'
    if (!groups[shopId]) {
      groups[shopId] = {
        shopId,
        shopName: item.shopName || '其他店铺',
        items: []
      }
    }
    groups[shopId].items.push(item)
  })
  return Object.values(groups)
})

// 总金额
const totalAmount = computed(() => {
  return checkoutItems.value.reduce((sum, item) => sum + parseFloat(item.price) * item.quantity, 0)
})

const onSelectAddress = () => {
  showToast('地址选择功能开发中...')
}

const onPay = () => {
  showLoadingToast({
    message: '支付跳转中...',
    forbidClick: true,
  })
  
  setTimeout(() => {
    showToast({
      type: 'success',
      message: '支付成功(模拟)',
      onClose: () => {
        router.replace('/home')
      }
    })
  }, 1500)
}
</script>

<style scoped>
@import './style.css';
</style>
