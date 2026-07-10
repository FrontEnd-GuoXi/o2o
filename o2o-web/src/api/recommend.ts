import request from '@/utils/request'

export type RecommendRankingType = 'avg_score'

export interface RecommendShop {
  shopId: number
  shopName: string
  shopDesc: string
  shopAddr: string
  shopImg: string
  shopCategoryId: string | null
  shopCategoryName: string | null
  avgScore: string | number | null
  evaluationCount: number | null
}

export const getRecommendShopList = async (rankingType: RecommendRankingType = 'avg_score') => {
  return request.get<RecommendShop[]>('/api/o2o/recommend/shops', { params: { rankingType } })
}
