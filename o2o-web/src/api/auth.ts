// 认证相关接口调用
import request from '@/utils/request'

// 登录请求参数接口
export interface LoginRequest {
  identifier: string
  credential: string
}

// 注册请求参数接口
export interface RegisterRequest {
  identifier: string
  credential: string
  name: string
  gender: string
  userType: number
  profileImg?: string
}

// 登录响应数据接口
export interface LoginResponse {
  data: string
}

// 登录接口
export const login = async (data: LoginRequest) => {
  return request.post<LoginResponse>('/api/o2o/auth/login', data)
}

// 注册接口
export const register = async (data: RegisterRequest) => {
  return request.post('/api/o2o/auth/register', data)
}
