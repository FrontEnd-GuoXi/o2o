import http from "@/api";
import { ShopVO } from "@/api/interface";

/**
 * @description 获取店铺列表
 * @returns Promise<ResultData<ShopVO[]>>
 */
export const getShopListApi = () => {
  return http.get<ShopVO[]>("/shopadmin/getShopList");
};

/**
 * @description 新增/更新店铺
 * @param formData 包含店铺信息和图片的 FormData
 * @returns Promise<ResultData<string>>
 */
export const registerShopApi = (formData: FormData) => {
  return http.post<string>("/shopadmin/registeredOrUpdateShop", formData, {
    headers: { "Content-Type": "multipart/form-data" }
  });
};