<template>
    <div class="shop-form-page">
      <O2oHeader title="新建商铺" />

      <div class="form-content">
      <van-form @submit="onSubmit">
        <!-- 商铺名称 -->
        <van-field v-model="form.name" label="商铺名称" placeholder="请输入商铺名称" required />

              <van-field
        v-model="form.categoryMain"
        is-link
        readonly
        name="categoryMain"
        label="商铺大类"
        placeholder="请选择商铺大类"
        :rules="[{ required: true, message: '请选择商铺大类' }]"
        @click="showMainCategoryPopup = true"
      />

            <van-field
        v-model="form.categorySub"
        is-link
        readonly
        name="categorySub"
        label="商铺类别"
        placeholder="请选择商铺类别"
        :rules="[{ required: true, message: '请选择商铺类别' }]"
        @click="showSubCategoryPopup = true"
        :disabled="!form.categoryMain"
      />

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

          <!-- 商铺大类弹出层 -->
    <van-popup v-model:show="showMainCategoryPopup" position="bottom">
      <van-picker
        :columns="mainCategories"
        @confirm="onMainCategoryConfirm"
        @cancel="showMainCategoryPopup = false"
        :default-index="getMainCategoryIndex()"
      />
    </van-popup>

    <!-- 商铺类别弹出层 -->
    <van-popup v-model:show="showSubCategoryPopup" position="bottom">
      <van-picker
        :columns="currentSubCategories"
        @confirm="onSubCategoryConfirm"
        @cancel="showSubCategoryPopup = false"
        :default-index="getSubCategoryIndex()"
      />
    </van-popup>
    </div>
  </template>

  <script setup lang="ts">
import { ref } from 'vue';
import { showToast } from 'vant';
import O2oHeader from '@/components/O2oHeader.vue';

  interface ShopForm {
    name: string;
    desc: string;
    address: string;
    phone: string;
    weight: number;
    enabled: boolean;
    images: File[];
    code: string;
    categoryMain: string; // 商铺大类
    categorySub: string;  // 商铺类别
  }

  const form = ref<ShopForm>({
    name: '',
    desc: '',
     categoryMain: '', // 商铺大类
  categorySub: '',  // 商铺类别
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


  // 商铺大类选项
const mainCategories = [
  { text: '餐饮美食', value: 'food' },
  { text: '购物零售', value: 'retail' },
  { text: '生活服务', value: 'service' },
  { text: '休闲娱乐', value: 'entertainment' },
  { text: '教育培训', value: 'education' }
]

// 商铺类别选项（根据大类动态变化）
const subCategories = {
  food: [
    { text: '中餐厅', value: 'chinese_restaurant' },
    { text: '西餐厅', value: 'western_restaurant' },
    { text: '快餐', value: 'fast_food' },
    { text: '饮品店', value: 'beverage' }
  ],
  retail: [
    { text: '服装鞋帽', value: 'clothing' },
    { text: '超市', value: 'supermarket' },
    { text: '便利店', value: 'convenience_store' },
    { text: '电子产品', value: 'electronics' }
  ],
  service: [
    { text: '美容美发', value: 'beauty' },
    { text: '洗衣店', value: 'laundry' },
    { text: '维修服务', value: 'repair' },
    { text: '快递服务', value: 'express' }
  ],
  entertainment: [
    { text: '电影院', value: 'cinema' },
    { text: 'KTV', value: 'ktv' },
    { text: '健身房', value: 'gym' },
    { text: '网吧', value: 'internet_cafe' }
  ],
  education: [
    { text: '培训机构', value: 'training' },
    { text: '幼儿园', value: 'kindergarten' },
    { text: '学校', value: 'school' },
    { text: '图书馆', value: 'library' }
  ]
}

// 子类别选项，根据大类选择动态更新
const currentSubCategories = ref([])

// 当大类改变时更新子类别选项
const onMainCategoryChange = (value) => {
  form.value.categorySub = '' // 重置子类别选择
  currentSubCategories.value = subCategories[value] || []
}

  // 弹出层显示控制
const showMainCategoryPopup = ref(false)
const showSubCategoryPopup = ref(false)

// 获取当前大类索引
const getMainCategoryIndex = () => {
  return mainCategories.findIndex(item => item.value === form.value.categoryMain)
}

// 获取当前子类索引
const getSubCategoryIndex = () => {
  return currentSubCategories.value.findIndex(item => item.value === form.value.categorySub)
}

// 确认选择大类
const onMainCategoryConfirm = ({ selectedOptions }) => {
  form.value.categoryMain = selectedOptions[0].value
  showMainCategoryPopup.value = false
  onMainCategoryChange(selectedOptions[0].value)
}

// 确认选择子类
const onSubCategoryConfirm = ({ selectedOptions }) => {
  form.value.categorySub = selectedOptions[0].value
  showSubCategoryPopup.value = false
}
  </script>

  <style scoped>
  @import './style.css';
  </style>
