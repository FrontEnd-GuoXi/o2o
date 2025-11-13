<template>
    <div class="shop-form-page">
      <div class="header">新建商铺</div>
  
      <van-form @submit="onSubmit">
        <!-- 商铺名称 -->
        <van-field v-model="form.name" label="商铺名称" placeholder="请输入商铺名称" required />
  
        <!-- 商铺描述 -->
        <van-field v-model="form.desc" label="商铺描述" placeholder="请输入商铺描述" type="textarea" rows="3" />
  
        <!-- 商铺地址 -->
        <van-field v-model="form.address" label="商铺地址" placeholder="请输入商铺地址" />
  
        <!-- 手机号码 -->
        <van-field v-model="form.phone" label="手机号码" placeholder="请输入手机号码" type="tel" />
  
        <!-- 权重 -->
        <van-field v-model.number="form.weight" label="权重" placeholder="请输入权重" type="number" />
  
        <!-- 启用/禁用 -->
        <van-field label="状态">
          <template #input>
            <van-switch v-model="form.enabled" />
          </template>
        </van-field>
  
        <!-- 上传图片 -->
        <div class="uploader-box">
          <div class="uploader-label">上传图片</div>
          <van-uploader v-model="form.images" multiple />
        </div>
  
        <!-- 验证码 -->
        <van-field v-model="form.code" label="验证码" placeholder="请输入验证码">
          <template #button>
            <van-button size="small" type="primary" :disabled="countdown > 0" @click="sendCode">
              {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
            </van-button>
          </template>
        </van-field>
  
        <!-- 提交按钮 -->
        <div class="submit-box">
          <van-button round block type="primary" native-type="submit">提交</van-button>
        </div>
      </van-form>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue';
  import { showToast } from 'vant';
  
  interface ShopForm {
    name: string;
    desc: string;
    address: string;
    phone: string;
    weight: number;
    enabled: boolean;
    images: File[];
    code: string;
  }
  
  const form = ref<ShopForm>({
    name: '',
    desc: '',
    address: '',
    phone: '',
    weight: 0,
    enabled: true,
    images: [],
    code: ''
  });
  
  const countdown = ref(0);
  let timer: number | null = null;
  
  const sendCode = () => {
    if (countdown.value > 0) return;
    countdown.value = 60;
    timer = window.setInterval(() => {
      countdown.value--;
      if (countdown.value <= 0 && timer) {
        clearInterval(timer);
        timer = null;
      }
    }, 1000);
    showToast('验证码已发送');
  };
  
  const onSubmit = () => {
    showToast('提交成功');
    console.log('提交参数：', form.value);
  };
  </script>
  
  <style scoped>
  .shop-form-page {
    min-height: 100vh;
    background: #f5f6f8;
    padding: 16px 12px 60px;
  }
  .header {
    font-size: 20px;
    font-weight: 600;
    text-align: center;
    padding: 12px 0 20px;
  }
  .uploader-box {
    margin: 12px 0;
  }
  .uploader-label {
    font-size: 14px;
    margin-bottom: 8px;
    color: #666;
  }
  .submit-box {
    margin-top: 24px;
  }
  </style>