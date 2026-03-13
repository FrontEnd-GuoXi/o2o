<template>
  <div class="cart-container">
    <O2oHeader title="购物车" />

    <div v-if="cartStore.items.length > 0" class="cart-content">
      <!-- 按店铺分组展示商品 (简化版：直接列表) -->
      <div class="cart-list">
        <div v-for="item in cartStore.items" :key="item.productId" class="cart-item">
          <div class="item-img-wrapper">
            <img :src="getImageUrl(item.imgAddr)" :alt="item.productName" @error="handleImageError" />
          </div>
          <div class="item-info">
            <div class="item-header">
              <h3 class="item-name">{{ item.productName }}</h3>
              <van-icon name="delete-o" size="18" color="#969799" @click="removeItem(item)" />
            </div>
            <p class="item-shop">{{ item.shopName }}</p>
            <div class="item-footer">
              <span class="item-price">¥{{ item.price }}</span>
              <van-stepper
                v-model="item.quantity"
                @change="(val) => cartStore.updateQuantity(item.productId, val)"
                theme="round"
                button-size="22"
                disable-input
              />
            </div>
          </div>
        </div>
      </div>

      <!-- 底部结算栏 -->
      <van-submit-bar
        :price="parseFloat(cartStore.totalPrice) * 100"
        button-text="提交订单"
        @submit="onSubmit"
        class="submit-bar"
      >
        <template #default>
          <span class="total-count">共 {{ cartStore.totalCount }} 件</span>
        </template>
      </van-submit-bar>
    </div>

    <!-- 空状态 -->
    <div v-else class="empty-cart">
      <van-empty description="购物车还是空的" image="search">
        <van-button round type="primary" class="go-home-btn" @click="router.push('/home')">
          去逛逛
        </van-button>
      </van-empty>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import {
  Icon as VanIcon,
  Stepper as VanStepper,
  SubmitBar as VanSubmitBar,
  Empty as VanEmpty,
  Button as VanButton,
  showToast,
  showConfirmDialog
} from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import { useCartStore } from '@/stores/cart'
import { getImageUrl, handleImageError } from '@/utils/image'

const router = useRouter()
const cartStore = useCartStore()

const removeItem = (item: any) => {
  showConfirmDialog({
    title: '提示',
    message: `确定要从购物车中移除 ${item.productName} 吗？`
  }).then(() => {
    cartStore.removeFromCart(item.productId)
    showToast('已移除')
  }).catch(() => {})
}

const onSubmit = () => {
  showToast('订单提交功能开发中...')
}
</script>

<style scoped>
@import './style.css';
</style>
