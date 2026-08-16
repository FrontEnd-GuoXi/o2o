import http from "@/api";
import { Area } from "@/api/interface";

/**
 * @description 获取区域列表
 * @returns Promise<ResultData<Area[]>>
 */
export const getAreaListApi = () => {
  return http.get<Area[]>("/area/list");
};