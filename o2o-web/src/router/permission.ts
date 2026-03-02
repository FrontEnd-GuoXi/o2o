// 路由权限控制
import router from './index'
import { useUserStore } from '@/stores/user'

// 全局路由拦截器：检查token是否存在
router.beforeEach(async (to, from, next) => {
  // 白名单：不需要token的页面
  const whiteList = ['/login', '/register']

  // 检查token是否存在
  const token = localStorage.getItem('token')

  const userStore = useUserStore()

  // 如果有token
  if (token) {
    if (whiteList.includes(to.path)) {
      // 已登录用户访问白名单页面，跳转到首页
      next('/home')
    } else {
      // 已登录用户访问非白名单页面，检查是否有用户信息
      if (!userStore.userInfo) {
        // 如果没有用户信息（首次登录或刷新页面），发起请求
        const userInfo = await userStore.fetchUserInfo()
        if (userInfo) {
          next()
        } else {
          // 获取用户信息失败（可能token过期），清空token并跳转登录
          localStorage.removeItem('token')
          next('/login')
        }
      } else {
        // 已有用户信息，直接通过
        next()
      }
    }
  } else {
    // 如果没有token
    if (whiteList.includes(to.path)) {
      // 未登录用户访问白名单页面，直接通过
      next()
    } else {
      // 未登录用户访问非白名单页面，跳转到登录页
      next('/login')
    }
  }
})
