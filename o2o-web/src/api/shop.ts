import request from '@/utils/request'

// 类别数据接口
export interface CategoryOption {
  text: string;
  value: string;
}

// 原始店铺分类接口
export interface ShopCategory {
  shopCategoryId: number;
  shopCategoryName: string;
  shopCategoryDesc: string;
  shopCategoryImg: string;
  priority: number;
  parent?: ShopCategory;
}

/**
 * 获取店铺类别数据
 * @param parentId - 父类别ID，null或空表示获取主要类别
 * @returns 类别选项数组
 */
export const getShopCategoryByParentId = async (parentId: string | null = null) => {
  const params = { parentId: parentId || '' } // 当parentId为null时，传入空字符串
  return request.get<ShopCategory[]>('/api/o2o/shopInfo/getShopCategoryByParentId', { params })
}

/**
 * 注册店铺信息
 * @param data - 店铺信息数据
 * @returns 注册结果
 */
export const registerShop = async (data: any) => {
  return request.post('/api/o2o/shopadmin/registeredOrUpdateShop', data)
}

// 店铺信息接口
export interface Shop {
  shopId: number
  shopName: string
  shopDesc: string
  shopAddr: string
  phone: string
  shopImg: string
  priority: number
  createTime: string
  lastEditTime: string
  enableStatus: number
  advice: string
  shopCategoryParentId?: string
  areaId?: string
  areaName?: string
  ownerId?: string
  shopCategoryId?: string
  shopCategoryName?: string
}

/**
 * 根据类别ID获取商铺列表
 * @param categoryId 类别ID（可以是父类或子类ID）
 * @returns 商铺列表
 */
export const queryShopListByCategoryId = async (categoryId: string) => {
  return request.get<Shop[]>('/api/o2o/shopInfo/queryShopListByCategoryId', { params: { categoryId } })
}

/**
 * 获取当前用户的店铺列表
 * @returns 店铺列表
 */
export const getShopList = async () => {
  return request.get<Shop[]>('/api/o2o/shopadmin/getShopList')
}

/**
 * 删除店铺
 * @param shopId 店铺ID
 * @returns 删除结果
 */
export const deleteShop = async (shopId: number) => {
  return request.get<boolean>('/api/o2o/shopadmin/deleteShop', { params: { shopId } })
}

/**
 * 根据店铺ID查询店铺信息
 * @param shopId 店铺ID
 * @returns 店铺信息
 */
export const getShopById = async (shopId: number) => {
  return request.get<Shop>('/api/o2o/shopadmin/queryShopById', { params: { shopId } })
}

/**
 * 根据店铺ID获取店铺详情
 * @param shopId 店铺ID
 */
export const getShopByShopId = async (shopId: string) => {
  return request.get<Shop>('/api/o2o/shopInfo/getShopByShopId', { params: { shopId } })
}

/**
 * 商品简要信息接口
 */
export interface ProductBrief {
  productId: number
  productName: string
  imgAddr: string
  normalPrice: string
  promotionPrice: string | null
  priority: number
  createTime: string
}

/**
 * 根据店铺ID获取商品简要列表
 * @param shopId 店铺ID
 */
export const getProductBriefListByShopId = async (shopId: string) => {
  return request.get<ProductBrief[]>('/api/o2o/productInfo/getProductBriefListByShopId', {
    params: { shopId }
  })
}
