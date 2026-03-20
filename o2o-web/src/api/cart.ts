import request from '@/utils/request'

export interface CartProduct {
  cartId?: number
  productId: number
  productName: string
  imgAddr: string
  normalPrice: string
  promotionPrice: string | null
  count: number
}

/**
 * 添加或更新购物车产品
 * @param cartInfo { cartId?: number, productId: number, count: number }
 */
export const addOrUpdateProduct = (cartInfo: { cartId?: number; productId: number; count: number }) => {
  return request.post<number>('/api/o2o/cart/addOrUpdateProduct', cartInfo)
}

/**
 * 获取购物车列表
 */
export const getCartProductList = () => {
  return request.get<CartProduct[]>('/api/o2o/cart/productList')
}

/**
 * 移除购物车产品
 * @param productId
 */
export const removeProduct = (productId: number) => {
  return request.get<boolean>('/api/o2o/cart/removeProduct', {
    params: { productId }
  })
}
