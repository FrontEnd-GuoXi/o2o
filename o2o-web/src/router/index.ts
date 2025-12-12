import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/home',
  },
  {
    name: 'Login',
    path: '/login',
    component: () => import('@/pages/O2oLogin/index.vue'),
  },
  {
    name: 'Register',
    path: '/register',
    component: () => import('@/pages/O2oRegister/index.vue'),
  },
  {
    name: 'Home',
    path: '/home',
    component: () => import('@/pages/O2oHome/index.vue'),
  },
  {
    name: 'PersonalInfo',
    path: '/personalInfo',
    component: () => import('@/pages/PersonalInfo/index.vue'),
  },
  {
    name: 'MyShops',
    path: '/myShops',
    component: () => import('@/pages/MyShops/index.vue'),
  },
  {
    name: 'AddShop',
    path: '/addShop',
    component: () => import('@/pages/AddShop/index.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
