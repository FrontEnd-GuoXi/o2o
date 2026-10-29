import request from '@/utils/request'

export interface ProductItem {
  productId: number | string
  quantity: number
}

export interface ShopItem {
  shopId: number | string
  userId?: number | string
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

export interface CreateOrderResponse {
  totalPrice: string
  shopIdMapOrderId: Record<string, string>
}

/**
 * 创建订单
 */
export const createOrder = (orderVO: OrderVO) => {
  return request.post<CreateOrderResponse>('/api/o2o/order/create', orderVO)
}

/**
 * 扣减库存
 */
export const inventoryDeduction = (payOrderDTO: PayOrderDTO) => {
  return request.post<boolean>('/api/o2o/order/inventoryDeduction', payOrderDTO)
}

/**
 * 获取订单token
 */
export const getOrderToken = () => {
  return request.get<string>('/api/o2o/order/getOrderToken')
}
