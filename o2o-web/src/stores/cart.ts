import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export interface CartItem {
  productId: number
  productName: string
  imgAddr: string
  price: string
  quantity: number
  shopId: string
  shopName: string
}

export const useCartStore = defineStore('cart', () => {
  // 从 localStorage 加载初始数据
  const savedItems = localStorage.getItem('o2o-cart')
  const items = ref<CartItem[]>(savedItems ? JSON.parse(savedItems) : [])

  // 监听 items 变化并保存到 localStorage
  const saveCart = () => {
    localStorage.setItem('o2o-cart', JSON.stringify(items.value))
  }

  // 添加商品到购物车
  const addToCart = (product: any, shop: any) => {
    const existingItem = items.value.find(item => item.productId === product.productId)
    if (existingItem) {
      existingItem.quantity++
    } else {
      items.value.push({
        productId: product.productId,
        productName: product.productName,
        imgAddr: product.imgAddr,
        price: product.promotionPrice || product.normalPrice,
        quantity: 1,
        shopId: shop.shopId.toString(),
        shopName: shop.shopName
      })
    }
    saveCart()
  }

  // 从购物车移除商品
  const removeFromCart = (productId: number) => {
    const index = items.value.findIndex(item => item.productId === productId)
    if (index !== -1) {
      items.value.splice(index, 1)
      saveCart()
    }
  }

  // 更新数量
  const updateQuantity = (productId: number, quantity: number) => {
    const item = items.value.find(item => item.productId === productId)
    if (item) {
      if (quantity <= 0) {
        removeFromCart(productId)
      } else {
        item.quantity = quantity
        saveCart()
      }
    }
  }

  // 清空购物车
  const clearCart = () => {
    items.value = []
    saveCart()
  }

  // 总数量
  const totalCount = computed(() => {
    return items.value.reduce((total, item) => total + item.quantity, 0)
  })

  // 总金额
  const totalPrice = computed(() => {
    return items.value.reduce((total, item) => {
      return total + parseFloat(item.price) * item.quantity
    }, 0).toFixed(2)
  })

  return {
    items,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
    totalCount,
    totalPrice
  }
})
