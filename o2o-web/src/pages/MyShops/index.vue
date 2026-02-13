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
        <div v-for="shop in shopList" :key="shop.shopId" class="shop-card-wrapper">
          <ShopCard :shop="shop" @edit="handleEdit" @delete="handleDelete" />
        </div>
        <!-- 无数据展示 -->
        <div v-if="shopList.length === 0" class="no-data">
          暂无店铺数据
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Icon as VanIcon, Button as VanButton, showToast, showConfirmDialog } from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import ShopCard from './components/ShopCard/index.vue'
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import { getShopList, deleteShop, type Shop } from '@/api/shop'

const router = useRouter()

// 跳转到新建店铺页面
const goToAddShop = () => {
  router.push('/addShop')
}

// 店铺数据
const shopList = ref<Shop[]>([])

// 获取店铺列表数据
const fetchShopList = async () => {
  try {
    const res = await getShopList()
    shopList.value = res.data || []
  } catch (error) {
    console.error('获取店铺列表失败:', error)
    showToast('获取店铺列表失败')
  }
}

onMounted(() => {
  fetchShopList()
})

// 处理编辑事件
const handleEdit = (shop: Shop) => {
  router.push({
    path: '/AddShop',
    query: { shopId: shop.shopId }
  })
}

// 处理删除事件
const handleDelete = async (shop: Shop) => {
  try {
    await showConfirmDialog({
      title: '删除确认',
      message: `确定要删除店铺 "${shop.shopName}" 吗？此操作不可撤销。`,
      confirmButtonColor: '#f56c6c',
    })

    const res = await deleteShop(shop.shopId)
    if (res.data) {
      showToast('删除成功')
      fetchShopList() // 刷新列表
    }
  } catch (error) {
    if (error === 'cancel') return
    console.error('删除店铺失败:', error)
  }
}
</script>

<style scoped>
@import './style.css';
</style>
