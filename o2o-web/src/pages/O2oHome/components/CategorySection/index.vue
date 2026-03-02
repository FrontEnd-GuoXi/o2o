<template>
  <!-- 商铺分类区域 -->
  <div class="category-section">
    <div class="section-header">
      <div class="section-title">
        <van-icon name="apps-o" color="#67c23a" size="20" />
        <span>商铺分类</span>
      </div>
    </div>
    <p class="section-subtitle">选择类别查看更多商铺</p>

    <!-- 分类卡片网格 -->
    <div class="category-grid">
      <!-- 分类卡片 -->
      <div
        v-for="category in categories"
        :key="category.shopCategoryId"
        class="category-card"
        @click="handleCategoryClick(category)"
      >
        <div class="category-image-wrapper">
          <img
            :src="getImageUrl(category.shopCategoryImg)"
            :alt="category.shopCategoryName"
            class="category-image"
            @error="handleImageError"
          />
        </div>
        <div class="category-content">
          <h3 class="category-name">{{ category.shopCategoryName }}</h3>
          <p class="category-subcount">{{ category.shopCategoryDesc || '查看更多' }} ></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Icon as VanIcon } from 'vant'
import { getImageUrl, handleImageError } from '@/utils/image'
import { getShopCategoryByParentId, type ShopCategory } from '@/api/shop'

const router = useRouter()
const categories = ref<ShopCategory[]>([])

// 获取一级分类数据
const fetchCategories = async () => {
  try {
    const res = await getShopCategoryByParentId(null)
    if (res.data) {
      categories.value = res.data
    }
  } catch (error) {
    console.error('获取商铺分类失败:', error)
  }
}

// 处理分类点击
const handleCategoryClick = (category: ShopCategory) => {
  console.log('点击了分类:', category.shopCategoryName)
  // 跳转到商铺列表页，并带上父类别ID和名称
  router.push({
    path: '/shopList',
    query: {
      parentId: category.shopCategoryId.toString(),
      categoryName: category.shopCategoryName
    }
  })
}

onMounted(() => {
  fetchCategories()
})
</script>

<style scoped>
@import './style.css';
</style>
