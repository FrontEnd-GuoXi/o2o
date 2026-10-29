export interface EvaluationOrderMeta {
  shopId: number | string
  orderId: number | string
  shopName: string
}

export const buildEvaluationOrders = (
  shopIdMapOrderId: Record<string, string>,
  shopGroups: Array<{ shopId: number | string; shopName: string }>
) => {
  const shopNameMap = new Map(
    shopGroups.map(group => [String(group.shopId), group.shopName || `店铺${group.shopId}`])
  )

  return Object.entries(shopIdMapOrderId).map(([shopId, orderId]) => ({
    shopId,
    orderId,
    shopName: shopNameMap.get(String(shopId)) || `店铺${shopId}`
  }))
}

export const serializeEvaluationOrders = (orders: EvaluationOrderMeta[]) => {
  return encodeURIComponent(JSON.stringify(orders))
}

export const deserializeEvaluationOrders = (value?: string | null) => {
  if (!value) {
    return [] as EvaluationOrderMeta[]
  }

  try {
    const parsed = JSON.parse(decodeURIComponent(value))
    if (!Array.isArray(parsed)) {
      return [] as EvaluationOrderMeta[]
    }

    return parsed
      .filter(item => item && item.shopId && item.orderId)
      .map(item => ({
        shopId: item.shopId,
        orderId: item.orderId,
        shopName: item.shopName || `店铺${item.shopId}`
      })) as EvaluationOrderMeta[]
  } catch (error) {
    console.error('解析评价订单信息失败:', error)
    return [] as EvaluationOrderMeta[]
  }
}
