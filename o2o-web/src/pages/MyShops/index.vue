<template>
  <div class="my-shops-container">
    <!-- 顶部公共头部 -->
    <O2oHeader title="我的店铺" />

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 新建店铺按钮 -->
      <div class="new-shop-btn-container">
        <van-button type="primary" block color="#1989fa" class="new-shop-btn" @click="goToAddShop">
          <van-icon name="plus" /> 新建店铺
        </van-button>
      </div>

      <!-- 店铺列表标题 -->
      <div class="shop-list-title">店铺列表</div>

      <!-- 店铺列表 -->
      <div class="shop-list">
        <div v-for="shop in shopList" :key="shop.name" class="shop-card-wrapper">
          <ShopCard :shop="shop" @edit="handleEdit" @delete="handleDelete" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Icon as VanIcon, Button as VanButton } from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import ShopCard from './components/ShopCard/index.vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 跳转到新建店铺页面
const goToAddShop = () => {
  router.push('/addShop')
}

// 店铺数据类型定义
interface ShopInfo {
  name: string
  type: string
  address: string
  phone: string
  createTime: string
}

// 模拟店铺数据
const shopList: ShopInfo[] = [
  {
    name: '瑞星咖啡',
    type: '咖啡',
    address: '番禺区',
    phone: '18312673102',
    createTime: '2025/12/12 11:09:48',
  },
  {
    name: '星巴克',
    type: '咖啡',
    address: '番禺区',
    phone: '123456',
    createTime: '2025/12/12 11:11:07',
  },
]

// 处理编辑事件
const handleEdit = (shop: ShopInfo) => {
  console.log('编辑店铺:', shop)
}

// 处理删除事件
const handleDelete = (shop: ShopInfo) => {
  console.log('删除店铺:', shop)
}
</script>

<style scoped>
@import './style.css';
</style>
