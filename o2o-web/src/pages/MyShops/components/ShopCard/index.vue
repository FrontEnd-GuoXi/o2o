<template>
  <div class="shop-card">
    <div class="shop-header">
      <div class="shop-info-wrapper">
        <div class="shop-image-container">
          <img :src="getImageUrl(shop.shopImg)" :alt="shop.shopName" class="shop-image" @error="handleImageError" />
        </div>
        <div class="shop-info">
          <div class="shop-name-type">
            <h3 class="shop-name">{{ shop.shopName }}</h3>
            <span class="shop-type">{{ shop.shopCategory?.shopCategoryName }}</span>
          </div>
        </div>
      </div>
      <div class="shop-actions">
        <van-icon name="edit" class="action-icon edit-icon" @click="handleEdit" />
        <van-icon name="delete-o" class="action-icon delete-icon" @click="handleDelete" />
      </div>
    </div>
    <div class="shop-details">
      <div class="detail-item">
        <van-icon name="location-o" class="detail-icon" />
        <span>{{ shop.shopAddr }}</span>
      </div>
      <div class="detail-item">
        <van-icon name="phone-o" class="detail-icon" />
        <span>{{ shop.phone }}</span>
      </div>
      <div class="detail-item">
        <van-icon name="clock-o" class="detail-icon" />
        <span>创建时间: {{ formatDate(shop.createTime) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Icon as VanIcon } from 'vant'
import type { Shop } from '@/api/shop'
import { formatDate } from '@/utils/date'
import { getImageUrl, handleImageError } from '@/utils/image'

const props = defineProps<{
  shop: Shop
}>()

// 定义事件
const emit = defineEmits<{
  (e: 'edit', shop: Shop): void
  (e: 'delete', shop: Shop): void
}>()

// 处理编辑事件
const handleEdit = () => {
  emit('edit', props.shop)
}

// 处理删除事件
const handleDelete = () => {
  emit('delete', props.shop)
}
</script>

<style scoped>
@import './style.css';
</style>
