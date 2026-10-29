<template>
  <div class="review-section">
    <div class="section-title-wrapper">
      <h2 class="section-title">店铺评价</h2>
      <span class="review-count">共{{ reviews.length }}条</span>
    </div>

    <div v-if="reviews.length" class="review-list">
      <div v-for="review in reviews" :key="review.evaluationId" class="review-item">
        <div class="review-header">
          <div class="review-user">
            <img
              class="review-avatar"
              :src="getImageUrl(review.profileImg)"
              :alt="review.name"
              @error="handleImageError"
            />
            <div class="review-user-meta">
              <div class="review-user-name">{{ review.name || '匿名用户' }}</div>
              <div class="review-time">{{ formatDate(review.createTime) }}</div>
            </div>
          </div>
          <van-rate
            :model-value="review.totalScore"
            readonly
            color="#ffb400"
            void-color="#eee"
            size="14"
          />
        </div>

        <div class="review-sub-scores">
          <span>综合 {{ review.totalScore }}</span>
          <span>服务 {{ review.serviceScore }}</span>
          <span>环境 {{ review.environmentScore }}</span>
        </div>

        <p class="review-content">{{ review.content || '用户未填写评价内容' }}</p>
      </div>
    </div>

    <van-empty v-else description="暂无评价" />
  </div>
</template>

<script setup lang="ts">
import { Empty as VanEmpty, Rate as VanRate } from 'vant'
import type { StoreReviewItem } from '@/api/evaluation'
import { getImageUrl, handleImageError } from '@/utils/image'

defineProps<{
  reviews: StoreReviewItem[]
}>()

const formatDate = (value?: string) => {
  if (!value) {
    return '时间未知'
  }

  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }

  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hour = String(date.getHours()).padStart(2, '0')
  const minute = String(date.getMinutes()).padStart(2, '0')

  return `${year}-${month}-${day} ${hour}:${minute}`
}
</script>

<style scoped>
.review-section {
  background-color: #fff;
  padding: 16px;
  margin-top: 12px;
}

.section-title-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  border-left: 4px solid #1989fa;
  padding-left: 10px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #323233;
  margin: 0;
}

.review-count {
  font-size: 12px;
  color: #969799;
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.review-item {
  padding-bottom: 16px;
  border-bottom: 1px solid #ebedf0;
}

.review-item:last-child {
  padding-bottom: 0;
  border-bottom: none;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
}

.review-user {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.review-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  background: #f2f3f5;
  flex-shrink: 0;
}

.review-user-meta {
  min-width: 0;
}

.review-user-name {
  font-size: 14px;
  font-weight: 600;
  color: #323233;
}

.review-time {
  margin-top: 4px;
  font-size: 12px;
  color: #969799;
}

.review-sub-scores {
  display: flex;
  gap: 12px;
  margin: 10px 0 8px;
  font-size: 12px;
  color: #646566;
}

.review-content {
  margin: 0;
  font-size: 14px;
  line-height: 1.6;
  color: #323233;
  word-break: break-word;
}
</style>
