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