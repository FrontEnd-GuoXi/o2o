<template>
  <!-- 热门头条区域 -->
  <div class="hot-section">
    <div class="section-header">
      <div class="section-title">
        <van-icon name="fire-o" color="#f56c6c" size="20" />
        <span>热门头条</span>
      </div>
      <div class="section-tag">精选推荐</div>
    </div>

    <!-- 商铺卡片列表 -->
    <div class="shop-card-list">
      <!-- 商铺卡片1 -->
      <div class="shop-card">
        <div class="shop-image-wrapper">
          <img
            src="https://images.unsplash.com/photo-1517248135467-4c7edcad34c4"
            alt="米其林餐厅"
            class="shop-image"
          />
          <div class="headline-tag">头条</div>
        </div>
        <div class="shop-info">
          <div class="shop-name-rating">
            <h3 class="shop-name">米其林餐厅 Le Gourmet</h3>
            <div class="shop-rating">
              <van-icon name="star" color="#fadb14" size="16" />
              <span class="rating-score">4.9</span>
            </div>
          </div>
          <p class="shop-description">正宗法式料理,米其林三星认证</p>
          <div class="shop-category">
            <span>餐饮美食</span>
            <van-icon name="arrow-right" color="#909399" size="14" />
          </div>
        </div>
      </div>

      <!-- 商铺卡片2 -->
      <div class="shop-card">
        <div class="shop-image-wrapper">
          <img
            src="https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb"
            alt="星巴克旗舰店"
            class="shop-image"
            @error="handleImageError"
          />
          <div class="headline-tag">头条</div>
        </div>
        <div class="shop-info">
          <div class="shop-name-rating">
            <h3 class="shop-name">星巴克旗舰店</h3>
            <div class="shop-rating">
              <van-icon name="star" color="#fadb14" size="16" />
              <span class="rating-score">4.7</span>
            </div>
          </div>
          <p class="shop-description">精选咖啡豆,舒适的第三空间</p>
          <div class="shop-category">
            <span>餐饮美食</span>
            <van-icon name="arrow-right" color="#909399" size="14" />
          </div>
        </div>
      </div>

      <!-- 商铺卡片3 -->
      <div class="shop-card">
        <div class="shop-image-wrapper">
          <img
            src="https://images.unsplash.com/photo-1551650975-87deedd944c3"
            alt="ZARA时尚旗舰店"
            class="shop-image"
            @error="handleImageError"
          />
        </div>
        <div class="shop-info">
          <div class="shop-name-rating">
            <h3 class="shop-name">ZARA时尚旗舰店</h3>
            <div class="shop-rating">
              <van-icon name="star" color="#fadb14" size="16" />
              <span class="rating-score">4.6</span>
            </div>
          </div>
          <p class="shop-description">最新时尚单品,本季新款上市</p>
          <div class="shop-category">
            <span>时尚服饰</span>
            <van-icon name="arrow-right" color="#909399" size="14" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Icon as VanIcon } from 'vant'
import { handleImageError } from '@/utils/image'
</script>

<style scoped>
@import './style.css';
</style>
