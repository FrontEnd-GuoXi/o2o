import request from '@/utils/request'

export interface AddEvaluationDTO {
  shopId: number | string
  userId: number | string
  orderId: number | string
  totalScore: number
  serviceScore: number
  environmentScore: number
  content: string
}

export const addEvaluation = (data: AddEvaluationDTO) => {
  return request.post<boolean>('/api/o2o/evaluation/add', data)
}

export interface StoreReviewItem {
  evaluationId: number
  userId: number
  name: string
  profileImg: string
  totalScore: number
  serviceScore: number
  environmentScore: number
  content: string
  createTime: string
  lastEditTime: string
}

export const queryEvaluationListByShopId = (shopId: number | string) => {
  return request.get<StoreReviewItem[]>('/api/o2o/evaluation/queryList', {
    params: { shopId }
  })
}
