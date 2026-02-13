import request from '@/utils/request'

export interface Area {
  areaId: number
  areaName: string
  priority?: number
  createTime?: string
  lastEditTime?: string
}

/**
 * 获取区域列表
 * @returns 区域列表
 */
export const getAreaList = async () => {
  return request.get<Area[]>('/api/area/list')
}
