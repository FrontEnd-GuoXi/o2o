<template>
  <div class="shop-list-container">
    <!-- 返回头 -->
    <O2oHeader :title="categoryName" backUrl="/" />

    <!-- 类别选择按钮区域 (子类别筛选) -->
    <div class="category-selection">
      <div class="category-scroll-wrapper">
        <div
          class="category-btn"
          :class="{ active: selectedSubCategoryId === '' }"
          @click="onSubCategoryChange('')"
        >
          全部
        </div>
        <div
          v-for="cat in subCategories"
          :key="cat.shopCategoryId"
          class="category-btn"
          :class="{ active: selectedSubCategoryId === cat.shopCategoryId.toString() }"
          @click="onSubCategoryChange(cat.shopCategoryId.toString())"
        >
          {{ cat.shopCategoryName }}
        </div>
      </div>
    </div>

    <!-- 店铺列表 -->
    <div class="shop-list-content">
      <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
        <van-list
          v-model:loading="loading"
          :finished="finished"
          finished-text="没有更多了"
          @load="onLoad"
        >
          <div
            v-for="shop in filteredShopList"
            :key="shop.shopId"
            class="shop-item"
            @click="goToShopDetail(shop)"
          >
            <div class="shop-img-wrapper">
              <img :src="getImageUrl(shop.shopImg)" :alt="shop.shopName" @error="handleImageError" />
            </div>
            <div class="shop-info">
              <h3 class="shop-title">{{ shop.shopName }}</h3>
              <p class="shop-desc">{{ shop.shopDesc }}</p>
              <div class="shop-meta">
                <span class="shop-area">
                  <van-icon name="location-o" /> {{ shop.areaName || '默认区域' }}
                </span>
                <span class="shop-category">
                  <van-icon name="apps-o" /> {{ shop.shopCategoryName || '默认类别' }}
                </span>
              </div>
            </div>
          </div>

          <!-- 如果列表为空展示空状态 -->
          <van-empty v-if="filteredShopList.length === 0 && !loading" description="暂无商铺" />
        </van-list>
      </van-pull-refresh>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  List as VanList,
  PullRefresh as VanPullRefresh,
  Icon as VanIcon,
  Empty as VanEmpty,
  showToast
} from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import { getShopCategoryByParentId, queryShopListByCategoryId, type ShopCategory } from '@/api/shop'
import { getImageUrl, handleImageError } from '@/utils/image'

const route = useRoute()
const router = useRouter()

// 路由参数
const parentId = ref(route.query.parentId as string || '')
const categoryName = ref(route.query.categoryName as string || '全部商铺')

// 筛选状态
const selectedSubCategoryId = ref('')
const subCategories = ref<ShopCategory[]>([])

// 列表状态
const shopList = ref<any[]>([])
const loading = ref(false)
const finished = ref(false)
const refreshing = ref(false)

// 获取子分类按钮数据
const fetchSubCategories = async () => {
  try {
    const res = await getShopCategoryByParentId(parentId.value)
    if (res.data) {
      subCategories.value = res.data
    }
  } catch (error) {
    console.error('获取子分类失败:', error)
    showToast('获取分类失败')
  }
}

// 获取店铺列表
const fetchShops = async (isRefresh = false) => {
  if (isRefresh) {
    shopList.value = []
  }

  loading.value = true
  try {
    // 如果没有选择子类 ID，则传父类 ID，否则传选中的子类 ID
    const categoryId = selectedSubCategoryId.value || parentId.value
    const res = await queryShopListByCategoryId(categoryId)
    if (res.data) {
      shopList.value = res.data
    }
    // queryShopListByCategoryId 接口不返回 count 和 分页，直接标记完成
    finished.value = true
  } catch (error) {
    console.error('获取店铺列表失败:', error)
    showToast('获取店铺列表失败')
    finished.value = true
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

// 筛选逻辑 (改回使用接口获取，不再使用 Mock 过滤)
const filteredShopList = computed(() => shopList.value)

const onRefresh = () => {
  fetchShops(true)
}

const onLoad = () => {
  if (!finished.value) {
    fetchShops()
  }
}

const onSubCategoryChange = (id: string) => {
  selectedSubCategoryId.value = id
  fetchShops(true)
}

const goToShopDetail = (shop: any) => {
  console.log('跳转到店铺详情:', shop.shopId)
}

onMounted(() => {
  fetchSubCategories()
  fetchShops()
})
</script>

<style scoped>
.shop-list-container {
  min-height: 100vh;
  background-color: #f7f8fa;
}

/* 类别选择按钮样式 */
.category-selection {
  background-color: #fff;
  padding: 10px 0;
  border-bottom: 1px solid #ebedf0;
  position: sticky;
  top: 46px;
  z-index: 10;
}

.category-scroll-wrapper {
  display: flex;
  overflow-x: auto;
  padding: 0 12px;
  -webkit-overflow-scrolling: touch;
}

.category-scroll-wrapper::-webkit-scrollbar {
  display: none;
}

.category-btn {
  flex-shrink: 0;
  padding: 6px 16px;
  margin-right: 10px;
  background-color: #f2f3f5;
  border-radius: 16px;
  font-size: 14px;
  color: #646566;
  transition: all 0.2s;
}

.category-btn.active {
  background-color: #e8f3ff;
  color: #1989fa;
  font-weight: bold;
}

/* 店铺列表样式 */
.shop-list-content {
  padding: 12px;
}

.shop-item {
  display: flex;
  padding: 12px;
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.shop-img-wrapper {
  width: 90px;
  height: 90px;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 12px;
  flex-shrink: 0;
  background-color: #f2f3f5;
}

.shop-img-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.shop-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.shop-title {
  margin: 0 0 4px;
  font-size: 16px;
  font-weight: bold;
  color: #323233;
}

.shop-desc {
  margin: 0 0 8px;
  font-size: 13px;
  color: #969799;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
}

.shop-meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #646566;
}

.shop-meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
