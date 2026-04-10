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
import { useRouter, useRoute } from 'vue-router'
import {
  Icon as VanIcon,
  Cell as VanCell,
  CellGroup as VanCellGroup,
  SubmitBar as VanSubmitBar,
  showToast,
  showLoadingToast,
  showDialog
} from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import { getImageUrl } from '@/utils/image'
import { createOrder, type OrderVO } from '@/api/order'
import { useCartStore } from '@/stores/cart'

const router = useRouter()
const route = useRoute()
const cartStore = useCartStore()
const checkoutItems = ref<any[]>([])

const orderToken = computed(() => route.params.token as string)

onMounted(() => {
  // 从路由 state 中获取结算商品数据
  const state = window.history.state
  if (state && state.checkoutItems && orderToken.value) {
    checkoutItems.value = state.checkoutItems
  } else {
    showToast('订单信息失效，请重新结算')
    router.back()
  }
})

// 按店铺分组
const groupedCheckoutItems = computed(() => {
  const groups: { [key: string]: { shopId: number, shopName: string, items: any[] } } = {}
  checkoutItems.value.forEach(item => {
    const shopId = item.shopId || 'unknown'
    if (!groups[shopId]) {
      groups[shopId] = {
        shopId: Number(shopId),
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

const onPay = async () => {
  const loading = showLoadingToast({
    message: '创建订单中...',
    forbidClick: true,
  })

  try {
    // 1. 准备订单数据
    const orderVO: OrderVO = {
      shopList: groupedCheckoutItems.value.map(group => ({
        shopId: group.shopId,
        productList: group.items.map(item => ({
          productId: Number(item.productId),
          quantity: item.quantity
        }))
      })),
      token: orderToken.value
    }

    // 2. 调用后端接口创建订单
    const res = await createOrder(orderVO)
    loading.close()

    if (res.code === "200") {
      const serverTotalPrice = res.data

      // 3. 根据返回值渲染总价，并提示支付
      await showDialog({
        title: '订单创建成功',
        message: `确认支付总计：¥${serverTotalPrice}`,
        theme: 'round-button',
        confirmButtonText: '立即支付',
        cancelButtonText: '稍后支付',
        showCancelButton: true
      })

      // 4. 模拟支付逻辑
      showLoadingToast({
        message: '支付中...',
        forbidClick: true,
      })

      setTimeout(() => {
        showToast({
          type: 'success',
          message: '支付成功(模拟)',
          onClose: () => {
            // 清空购物车
            cartStore.clearCart()
            router.replace('/home')
          }
        })
      }, 1500)
    } else {
      showToast(res.message || '订单创建失败')
    }
  } catch (error) {
    loading.close()
    console.error('Create order error:', error)
    showToast('网络异常，请稍后再试')
  }
}
</script>

<style scoped>
@import './style.css';
</style>
