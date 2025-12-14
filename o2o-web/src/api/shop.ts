import request from '@/utils/request'

// 类别数据接口
export interface CategoryOption {
  text: string;
  value: string;
}

/**
 * 获取店铺类别数据
 * @param parentId - 父类别ID，null或空表示获取主要类别
 * @returns 类别选项数组
 */
export const getShopCategoryByParentId = async (parentId: string | null = null) => {
  const params = { parentId: parentId || '' } // 当parentId为null时，传入空字符串
  return request.get<CategoryOption[]>('/api/o2o/shopInfo/getShopCategoryByParentId', { params })
}

/**
 * 注册店铺信息
 * @param data - 店铺信息数据
 * @returns 注册结果
 */
export const registerShop = async (data: any) => {
  return request.post('/api/o2o/shopadmin/registeredshop', data)
}
