import request from '@/utils/request'
import type { ResponseResult } from '@/types/api'

export interface ProductItem {
  productId: number | string
  quantity: number
}

export interface ShopItem {
  shopId: number | string
  productList: ProductItem[]
}

export interface OrderVO {
  shopList: ShopItem[]
  token: string
}

export interface PayOrderDTO {
  orderList: (number | string)[]
  token: string
}

/**
 * 创建订单
 */
export const createOrder = (orderVO: OrderVO) => {
  return request.post<string, ResponseResult<any>>('/api/o2o/order/create', orderVO)
}

/**
 * 扣减库存
 */
export const inventoryDeduction = (payOrderDTO: PayOrderDTO) => {
  return request.post<boolean, ResponseResult<boolean>>('/api/o2o/order/inventoryDeduction', payOrderDTO)
}

/**
 * 获取订单token
 */
export const getOrderToken = () => {
  return request.get<string, ResponseResult<string>>('/api/o2o/order/getOrderToken')
}
