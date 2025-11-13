import { createRouter, createWebHistory } from 'vue-router'



const routes = [
  {
    name: 'AddShop',
    path: '/addShop', 
    component: () => import('@/pages/AddShop/index.vue')
  }
]


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
