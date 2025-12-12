<template>
  <div class="shop-card">
    <div class="shop-header">
      <div class="shop-info">
        <div class="shop-name-type">
          <van-icon name="shop-o" class="shop-icon" />
          <h3 class="shop-name">{{ shop.name }}</h3>
          <span class="shop-type">{{ shop.type }}</span>
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
        <span>{{ shop.address }}</span>
      </div>
      <div class="detail-item">
        <van-icon name="phone-o" class="detail-icon" />
        <span>{{ shop.phone }}</span>
      </div>
      <div class="detail-item">
        <van-icon name="clock-o" class="detail-icon" />
        <span>创建时间: {{ shop.createTime }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Icon as VanIcon } from 'vant'

// 定义组件props
interface ShopInfo {
  name: string
  type: string
  address: string
  phone: string
  createTime: string
}

const props = defineProps<{
  shop: ShopInfo
}>()

// 定义事件
const emit = defineEmits<{
  (e: 'edit', shop: ShopInfo): void
  (e: 'delete', shop: ShopInfo): void
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
