// 路由权限控制
import router from './index';

// 全局路由拦截器：检查token是否存在
router.beforeEach((to, from, next) => {
  // 白名单：不需要token的页面
  const whiteList = ['/login', '/register'];
  
  // 如果是白名单中的页面，直接放行
  if (whiteList.includes(to.path)) {
    next();
    return;
  }
  
  // 检查token是否存在
  const token = localStorage.getItem('token');
  if (token) {
    // token存在，放行
    next();
  } else {
    // token不存在，跳转到登录页
    next('/login');
  }
});
