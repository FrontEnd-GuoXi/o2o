<template>
  <div class="personal-container">
    <!-- 顶部公共头部 -->
    <O2oHeader title="个人信息" />

    <div class="personal-content">
      <!-- 个人信息卡片 -->
      <div class="profile-card">
        <div class="profile-header">
          <img
            src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d"
            alt="用户头像"
            class="avatar"
            @error="handleImageError"
          />
          <div class="profile-info">
            <div class="name-role">
              <h2 class="username">张三</h2>
              <span class="role">管理员</span>
            </div>
            <div class="contact-info">
              <div class="contact-item">
                <VanIcon class="contact-icon" name="phone-o" />
                <span>138****8888</span>
              </div>
              <div class="contact-item">
                <VanIcon class="contact-icon" name="email-o" />
                <span>zhang**@example.com</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 数据统计 -->
      <div class="stats-card">
        <div class="stat-item">
          <div class="stat-number">1580</div>
          <div class="stat-label">总浏览量</div>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-item">
          <div class="stat-number">5</div>
          <div class="stat-label">已收藏</div>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-item">
          <div class="stat-number">12</div>
          <div class="stat-label">已关注</div>
        </div>
      </div>

      <!-- 功能菜单 -->
      <div class="menu-card">
        <div class="menu-item" @click="goToMyShops">
          <VanIcon class="menu-icon shop-icon" name="shop-o" />
          <span>我的店铺</span>
        </div>
        <div class="menu-item">
          <VanIcon class="menu-icon like-o" name="like-o" />
          <span>我的收藏</span>
        </div>
        <div class="menu-item">
          <VanIcon class="menu-icon eye-icon" name="eye-o" />
          <span>我的关注</span>
        </div>
        <div class="menu-item">
          <VanIcon class="menu-icon star-icon" name="star-o" />
          <span>我的评价</span>
        </div>
        <div class="menu-item">
          <VanIcon class="menu-icon setting-icon" name="setting-o" />
          <span>设置</span>
        </div>
      </div>

      <!-- 退出登录按钮 -->
      <div class="logout-section">
        <van-button type="default" block color="#f56c6c" text-color="#fff" @click="handleLogout"
          >退出登录</van-button
        >
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Icon as VanIcon, Button as VanButton } from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import { handleImageError } from '@/utils/image'
import { useRouter } from 'vue-router'

const router = useRouter()

// 跳转到我的店铺页面
const goToMyShops = () => {
  router.push('/myShops')
}

// 退出登录处理
const handleLogout = () => {
  // 清除localStorage中的token
  localStorage.removeItem('token')
  // 刷新页面
  location.reload()
}
</script>

<style scoped>
@import './style.css';
</style>
