<template>
  <div class="cart-container">
    <O2oHeader title="购物车" />

    <div v-if="cartStore.items.length > 0" class="cart-content">
      <!-- 按店铺分组展示商品 -->
      <div v-for="shop in groupedCartItems" :key="shop.shopId" class="shop-group">
        <div class="shop-header">
          <van-checkbox
            :model-value="isShopSelected(shop.shopId)"
            @update:model-value="(val) => onShopSelectChange(shop.shopId, val)"
            class="shop-checkbox"
          />
          <van-icon name="shop-o" size="20" class="shop-icon" />
          <span class="shop-name">{{ shop.shopName }}</span>
        </div>

        <div class="cart-list">
          <div v-for="item in shop.items" :key="item.productId" class="cart-item">
            <van-checkbox
              v-model="item.selected"
              class="item-checkbox"
            />
            <div class="item-img-wrapper">
              <img :src="getImageUrl(item.imgAddr)" :alt="item.productName" @error="handleImageError" />
            </div>
            <div class="item-info">
              <div class="item-header">
                <h3 class="item-name">{{ item.productName }}</h3>
                <van-icon name="delete-o" size="18" color="#969799" class="delete-icon" @click="removeItem(item)" />
              </div>
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
      </div>

      <!-- 底部结算栏 -->
      <van-submit-bar
        :price="totalPrice"
        button-text="提交订单"
        @submit="onSubmit"
        class="submit-bar"
      >
        <template #default>
          <van-checkbox v-model="isAllSelected" class="all-checkbox">全选</van-checkbox>
          <span class="total-count">已选 {{ selectedCount }} 件</span>
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
import { onMounted, computed, ref, watch } from 'vue'
import {
  Icon as VanIcon,
  Stepper as VanStepper,
  SubmitBar as VanSubmitBar,
  Empty as VanEmpty,
  Button as VanButton,
  Checkbox as VanCheckbox,
  showToast,
  showConfirmDialog
} from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import { useCartStore, type CartItem } from '@/stores/cart'
import { getImageUrl, handleImageError } from '@/utils/image'
import { getOrderToken } from '@/api/order'

const router = useRouter()
const cartStore = useCartStore()

// 扩展 CartItem 类型以包含 selected 状态
interface ExtendedCartItem extends CartItem {
  selected?: boolean
}

// 本地管理的商品列表，带上勾选状态
const localItems = ref<ExtendedCartItem[]>([])

onMounted(async () => {
  await cartStore.fetchCartFromBackend()
  // 同步 store 中的数据到本地 localItems，并默认初始化 selected 为 false
  localItems.value = cartStore.items.map(item => ({
    ...item,
    selected: false
  }))
})

// 监听 store 数据变化，同步到 localItems
watch(() => cartStore.items, (newItems) => {
  const selectedMap = new Map(localItems.value.map(i => [i.productId, i.selected]))
  localItems.value = newItems.map(item => ({
    ...item,
    selected: selectedMap.get(item.productId) || false
  }))
}, { deep: true })

// 按店铺分组
const groupedCartItems = computed(() => {
  const groups: { [key: string]: { shopId: string, shopName: string, items: ExtendedCartItem[] } } = {}
  localItems.value.forEach(item => {
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

// 店铺是否被全选
const isShopSelected = (shopId: string) => {
  const shop = groupedCartItems.value.find(g => g.shopId === shopId)
  return shop ? shop.items.every(i => i.selected) : false
}

// 切换店铺勾选状态
const onShopSelectChange = (shopId: string, checked: boolean) => {
  localItems.value.forEach(item => {
    if (item.shopId === shopId) {
      item.selected = checked
    }
  })
}

// 全选状态
const isAllSelected = computed({
  get: () => localItems.value.length > 0 && localItems.value.every(item => item.selected),
  set: (val: boolean) => {
    localItems.value.forEach(item => item.selected = val)
  }
})

// 已选商品总价
const totalPrice = computed(() => {
  const total = localItems.value
    .filter(item => item.selected)
    .reduce((sum, item) => sum + parseFloat(item.price) * item.quantity, 0)
  return total * 100 // 转换为分
})

// 已选商品数量
const selectedCount = computed(() => {
  return localItems.value.filter(item => item.selected).reduce((sum, item) => sum + item.quantity, 0)
})

const removeItem = (item: any) => {
  showConfirmDialog({
    title: '提示',
    message: `确定要从购物车中移除 ${item.productName} 吗？`
  }).then(async () => {
    await cartStore.removeFromCart(item.productId)
    // 删除后重新从后端拉取，确保列表刷新
    await cartStore.fetchCartFromBackend()
    showToast('已移除')
  }).catch(() => {})
}

const onSubmit = async () => {
  const selectedItems = localItems.value.filter(item => item.selected)
  if (selectedItems.length === 0) {
    showToast('请至少选择一件商品')
    return
  }

  try {
    // 1. 获取订单 token
    const res = await getOrderToken()
    if (res.code === "200") {
      const token = res.data
      // 2. 跳转到结算页面，并将 token 作为路由参数
      router.push({
        name: 'Checkout',
        params: { token },
        state: {
          checkoutItems: JSON.parse(JSON.stringify(selectedItems))
        }
      })
    } else {
      showToast(res.message || '获取订单凭证失败')
    }
  } catch (error) {
    console.error('Get order token error:', error)
    showToast('网络异常，请稍后再试')
  }
}

</script>

<style scoped>
@import './style.css';
</style>
