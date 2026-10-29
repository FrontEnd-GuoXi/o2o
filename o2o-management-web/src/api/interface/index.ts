// 请求响应参数（不包含data）
export interface Result {
  code: string;
  msg: string;
}

// 请求响应参数（包含data）
export interface ResultData<T = any> extends Result {
  data: T;
}

// 分页响应参数
export interface ResPage<T> {
  list: T[];
  pageNum: number;
  pageSize: number;
  total: number;
}

// 分页请求参数
export interface ReqPage {
  pageNum: number;
  pageSize: number;
}

// 登录模块
export namespace Login {
  export interface ReqLoginForm {
    identifier: string;
    credential: string;
    platform: string;
  }
  export interface ResAuthButtons {
    [key: string]: string[];
  }
}

// 用户信息
export interface UserInfo {
  userId: string;
  name: string;
  gender: string;
  userType: number;
  profileImg: string;
}

// 店铺
export interface ShopVO {
  shopId: string;
  shopName: string;
  shopDesc: string;
  shopAddr: string;
  phone: string;
  shopImg: string;
  priority: number;
  createTime: string;
  lastEditTime: string;
  enableStatus: number;
  advice: string;
  areaId: string;
  areaName: string;
  ownerId: string;
  shopCategoryId: string;
  shopCategoryName: string;
  shopCategoryParentId: string;
  avgScore: number;
  evaluationCount: number;
}

// 新增/更新店铺
export interface AddShop {
  shopId?: string;
  shopName: string;
  shopDesc?: string;
  shopAddr: string;
  phone: string;
  priority: number;
  enableStatus: number;
  area: number;
  categorySub: number;
}

// 区域
export interface Area {
  areaId: number;
  areaName: string;
  priority: number;
}

// 店铺分类
export interface ShopCategory {
  shopCategoryId: number;
  shopCategoryName: string;
  shopCategoryDesc: string;
  priority: number;
  parent: { shopCategoryId: number } | null;
}