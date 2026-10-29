import http from "@/api";
import { Login, UserInfo } from "@/api/interface/index";
import authButtonList from "@/assets/json/authButtonList.json";
import authMenuList from "@/assets/json/authMenuList.json";

/**
 * @description 用户登录
 * @param params Login.ReqLoginForm
 * @returns Promise<ResultData<string>>
 */
export const loginApi = (params: Login.ReqLoginForm) => {
  return http.post<string>("/auth/login", params, { loading: false });
};

/**
 * @description 获取菜单列表
 * @returns Promise<Menu.MenuOptions[]>
 */
export const getAuthMenuListApi = () => {
  // return http.get<Menu.MenuOptions[]>(PORT1 + `/menu/list`, {}, { loading: false });
  // 使用本地菜单数据
  return authMenuList;
};

/**
 * @description 获取按钮权限
 * @returns Promise<Login.ResAuthButtons>
 */
export const getAuthButtonListApi = () => {
  // return http.get<Login.ResAuthButtons>(PORT1 + `/auth/buttons`, {}, { loading: false });
  // 使用本地按钮权限数据
  return authButtonList;
};

/**
 * @description 用户退出登录
 */
export const logoutApi = () => {
  return http.post(PORT1 + `/logout`);
};

/**
 * @description 获取用户信息
 * @returns Promise<ResultData<UserInfo>>
 */
export const getUserInfoApi = () => {
  return http.get<UserInfo>("/auth/getUserInfo");
};
