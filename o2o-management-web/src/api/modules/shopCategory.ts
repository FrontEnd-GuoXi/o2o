import http from "@/api";
import { ShopCategory } from "@/api/interface";

/**
 * @description 根据父级ID获取店铺分类列表
 * @param parentId 父级分类ID，0 表示获取一级分类
 * @returns Promise<ResultData<ShopCategory[]>>
 */
export const getShopCategoryByParentIdApi = (parentId: number) => {
  return http.get<ShopCategory[]>("/shopInfo/getShopCategoryByParentId", { parentId });
};