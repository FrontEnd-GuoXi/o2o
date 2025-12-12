// 路由权限控制
import router from './index'

// 全局路由拦截器：检查token是否存在
router.beforeEach((to, from, next) => {
  // 白名单：不需要token的页面
  const whiteList = ['/login', '/register']

  // 检查token是否存在
  const token = localStorage.getItem('token')

  // 如果有token
  if (token) {
    if (whiteList.includes(to.path)) {
      // 已登录用户访问白名单页面，跳转到首页
      next('/home')
    } else {
      // 已登录用户访问非白名单页面，直接通过
      next()
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
