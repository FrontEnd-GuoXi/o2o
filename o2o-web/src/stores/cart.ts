import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { addOrUpdateProduct, getCartProductList, removeProduct } from '@/api/cart'

export interface CartItem {
  cartId?: number
  productId: number
  productName: string
  imgAddr: string
  price: string
  quantity: number
  shopId?: string
  shopName?: string
}

export const useCartStore = defineStore('cart', () => {
  // 从 localStorage 加载初始数据作为备选
  const savedItems = localStorage.getItem('o2o-cart')
  const items = ref<CartItem[]>(savedItems ? JSON.parse(savedItems) : [])

  // 保存到 localStorage
  const saveToLocal = () => {
    localStorage.setItem('o2o-cart', JSON.stringify(items.value))
  }

  // 从后端同步购物车数据
  const fetchCartFromBackend = async () => {
    try {
      const res = await getCartProductList()
      if (res.data) {
        items.value = res.data.map(item => ({
          cartId: item.cartId,
          productId: Number(item.productId),
          productName: item.productName,
          imgAddr: item.imgAddr,
          price: item.promotionPrice || item.normalPrice,
          quantity: item.count,
          shopId: item.shopId.toString(),
          shopName: item.shopName
        }))
        saveToLocal()
      }
    } catch (error) {
      console.error('同步购物车失败:', error)
    }
  }

  // 添加商品到购物车
  const addToCart = async (product: any, shop: any) => {
    const existingItem = items.value.find(item => item.productId === product.productId)
    const count = existingItem ? existingItem.quantity + 1 : 1

    try {
      // 这里的 addOrUpdateProduct 接口：
      // 1. 如果购物车中没有该产品，则为新增。
      // 2. 如果购物车中已经存在该产品，后端 ON DUPLICATE KEY UPDATE 会更新数量。
      const res = await addOrUpdateProduct({
        cartId: existingItem ? existingItem.cartId : undefined,
        productId: product.productId,
        count: count
      })

      if (res.code === 0) {
        if (existingItem) {
          existingItem.quantity = count
          // 关键点：即使商品已存在，也同步更新店铺信息，防止之前同步后端时丢失了店铺名
          if (shop) {
            existingItem.shopId = shop.shopId.toString()
            existingItem.shopName = shop.shopName
          }
        } else {
          items.value.push({
            cartId: res.data, // 后端返回生成的 cartId
            productId: product.productId,
            productName: product.productName,
            imgAddr: product.imgAddr,
            price: product.promotionPrice || product.normalPrice,
            quantity: 1,
            shopId: shop?.shopId?.toString(),
            shopName: shop?.shopName
          })
        }
        saveToLocal()
      }
    } catch (error) {
      console.error('添加购物车失败:', error)
      throw error
    }
  }

  // 从购物车移除商品
  const removeFromCart = async (productId: number) => {
    try {
      const res = await removeProduct(productId)
      if (res.code === 0) {
        const index = items.value.findIndex(item => item.productId === productId)
        if (index !== -1) {
          items.value.splice(index, 1)
          saveToLocal()
        }
      }
    } catch (error) {
      console.error('移除购物车商品失败:', error)
    }
  }

  // 更新数量
  const updateQuantity = async (productId: number, quantity: number) => {
    const item = items.value.find(item => item.productId === productId)
    if (!item) return

    if (quantity <= 0) {
      return removeFromCart(productId)
    }

    try {
      const res = await addOrUpdateProduct({
        cartId: item.cartId,
        productId: productId,
        count: quantity
      })
      if (res.code === 0) {
        item.quantity = quantity
        saveToLocal()
      }
    } catch (error) {
      console.error('更新购物车数量失败:', error)
    }
  }

  // 清空购物车
  const clearCart = () => {
    items.value = []
    saveToLocal()
  }

  // 总数量
  const totalCount = computed(() => {
    return items.value.reduce((total, item) => total + item.quantity, 0)
  })

  // 总金额
  const totalPrice = computed(() => {
    return items.value.reduce((total, item) => {
      const price = parseFloat(item.price) || 0
      return total + price * item.quantity
    }, 0).toFixed(2)
  })

  return {
    items,
    fetchCartFromBackend,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
    totalCount,
    totalPrice
  }
})
