import http from "@/api";
import { ShopVO } from "@/api/interface";

/**
 * @description 获取店铺列表
 * @returns Promise<ResultData<ShopVO[]>>
 */
export const getShopListApi = () => {
  return http.get<ShopVO[]>("/shopadmin/getShopList");
};