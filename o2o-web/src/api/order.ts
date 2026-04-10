import request from '@/utils/request'
import type { ResponseResult } from '@/types/api'

export interface ProductItem {
  productId: number
  quantity: number
}

export interface ShopItem {
  shopId: number
  productList: ProductItem[]
}

export interface OrderVO {
  shopList: ShopItem[]
  token: string
}

/**
 * 创建订单
 */
export const createOrder = (orderVO: OrderVO) => {
  return request.post<string, ResponseResult<string>>('/api/o2o/order/create', orderVO)
}

/**
 * 获取订单token
 */
export const getOrderToken = () => {
  return request.get<string, ResponseResult<string>>('/api/o2o/order/getOrderToken')
}
