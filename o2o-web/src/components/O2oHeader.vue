<template>
  <div class="o2o-header">
    <div class="header-content">
      <van-icon
        name="arrow-left"
        class="back-icon"
        @click="handleBack"
      />
      <h1 class="header-title">{{ title }}</h1>
      <div class="header-right"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Icon as VanIcon } from 'vant';
import { useRouter } from 'vue-router';

defineProps<{
  title: string;
}>();

const router = useRouter();

const handleBack = () => {
  router.back();
};
</script>

<style scoped>
.o2o-header {
  width: 100%;
  background-color: white;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  left: 0;
  z-index: 100;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 48px;
  padding: 0 16px;
  position: relative;
}

.back-icon {
  font-size: 20px;
  color: #333;
  cursor: pointer;
}

.header-title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.header-right {
  width: 20px;
}
</style>
