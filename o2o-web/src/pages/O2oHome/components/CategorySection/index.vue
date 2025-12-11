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
      <!-- 分类卡片1 -->
      <div class="category-card">
        <div class="category-image-wrapper">
          <img
            src="https://images.unsplash.com/photo-1546069901-ba9599a7e63c"
            alt="餐饮美食"
            class="category-image"
            @error="handleImageError"
          />
        </div>
        <div class="category-content">
          <h3 class="category-name">餐饮美食</h3>
          <p class="category-subcount">3个子类 ></p>
        </div>
      </div>

      <!-- 分类卡片2 -->
      <div class="category-card">
        <div class="category-image-wrapper">
          <img
            src="https://images.unsplash.com/photo-1445205170230-053b83016050"
            alt="时尚服饰"
            class="category-image"
            @error="handleImageError"
          />
        </div>
        <div class="category-content">
          <h3 class="category-name">时尚服饰</h3>
          <p class="category-subcount">3个子类 ></p>
        </div>
      </div>

      <!-- 分类卡片3 -->
      <div class="category-card">
        <div class="category-image-wrapper">
          <img
            src="https://images.unsplash.com/photo-1542744095-fcf48d80b5f4"
            alt="数码电子"
            class="category-image"
            @error="handleImageError"
          />
        </div>
        <div class="category-content">
          <h3 class="category-name">数码电子</h3>
          <p class="category-subcount">2个子类 ></p>
        </div>
      </div>

      <!-- 分类卡片4 -->
      <div class="category-card">
        <div class="category-image-wrapper">
          <img
            src="https://images.unsplash.com/photo-1557843652-4b4d59bd8d7b"
            alt="超市便利"
            class="category-image"
            @error="handleImageError"
          />
        </div>
        <div class="category-content">
          <h3 class="category-name">超市便利</h3>
          <p class="category-subcount">2个子类 ></p>
        </div>
      </div>

      <!-- 分类卡片5 -->
      <div class="category-card">
        <div class="category-image-wrapper">
          <img
            src="https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c"
            alt="图书文化"
            class="category-image"
            @error="handleImageError"
          />
        </div>
        <div class="category-content">
          <h3 class="category-name">图书文化</h3>
          <p class="category-subcount">2个子类 ></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Icon as VanIcon } from 'vant';

// 处理图片加载错误的函数
const handleImageError = (event: Event) => {
  const target = event.target as HTMLImageElement;
  target.onerror = null;
  target.src = 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c';
};
</script>

<style scoped>
@import './style.css';
</style>