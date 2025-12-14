<template>
  <div class="add-shop-container">
    <!-- 顶部公共头部 -->
    <O2oHeader title="新建店铺" />

    <!-- 主要内容区 -->
    <div class="add-shop-content">
      <van-form @submit="onSubmit">
        <!-- 店铺名称 -->
        <div class="form-section">
          <van-field
            v-model="form.name"
            label="店铺名称"
            placeholder="请输入店铺名称"
            required
            :rules="[{ required: true, message: '请输入店铺名称' }]"
          />
        </div>

        <!-- 主要类别和店铺类别 - 横向排列 -->
        <div class="form-section">
          <van-dropdown-menu>
            <van-dropdown-item
              v-model="form.categoryMain"
              :options="mainCategoryOptions"
            />
            <van-dropdown-item
              v-model="form.categorySub"
              :options="subCategoryOptions"
            />
          </van-dropdown-menu>
        </div>

        <!-- 店铺描述 -->
        <div class="form-section">
          <van-field
            v-model="form.desc"
            label="店铺描述"
            placeholder="请输入店铺描述"
            type="textarea"
            rows="3"
          />
        </div>

        <!-- 店铺地址 -->
        <div class="form-section">
          <van-field
            v-model="form.address"
            label="店铺地址"
            placeholder="请输入店铺地址"
            required
            :rules="[{ required: true, message: '请输入店铺地址' }]"
          />
        </div>

        <!-- 联系电话和权重 - 横向排列 -->
        <div class="form-section">
          <div class="field-row">
            <div class="field-half">
              <van-field
                v-model="form.phone"
                label="联系电话"
                placeholder="请输入联系电话"
                type="tel"
                required
                :rules="[{ required: true, message: '请输入联系电话' }]"
              />
            </div>
            <div class="field-half">
              <van-field v-model.number="form.weight" label="权重" placeholder="0" type="number" />
            </div>
          </div>
        </div>

        <!-- 启用状态 -->
        <div class="form-section">
          <van-cell-group>
            <van-cell title="启用状态" center>
              <template #right-icon>
                <van-switch v-model="form.enabled" size="24" />
              </template>
            </van-cell>
          </van-cell-group>
        </div>

        <!-- 店铺图片 -->
        <div class="form-section">
          <van-field
            name="images"
            label="店铺图片"
            placeholder="选择文件: 未选择任何文件"
            readonly
            is-link
          >
            <template #button>
              <van-uploader v-model="form.images" :max-count="1" :after-read="afterRead" />
            </template>
          </van-field>
        </div>

        <!-- 操作按钮 -->
        <div class="form-section">
          <div class="button-box">
            <van-button round block type="default" @click="handleCancel">取消</van-button>
            <van-button round block type="primary" native-type="submit">创建店铺</van-button>
          </div>
        </div>
      </van-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import {
  Form as VanForm,
  Field as VanField,
  DropdownMenu as VanDropdownMenu,
  DropdownItem as VanDropdownItem,
  CellGroup as VanCellGroup,
  Cell as VanCell,
  Switch as VanSwitch,
  Uploader as VanUploader,
  Button as VanButton,
  showToast,
  showLoadingToast,
  closeToast,
} from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import { getShopCategoryByParentId, registerShop, type CategoryOption } from '@/api/shop'

interface ShopForm {
  name: string;
  desc: string;
  address: string;
  phone: string;
  weight: number;
  enabled: boolean;
  images: any[];
  categoryMain: string; // 主要类别
  categorySub: string;  // 店铺类别
}

const form = ref<ShopForm>({
  name: '',
  desc: '',
  address: '',
  phone: '',
  weight: 0,
  enabled: true,
  images: [],
  categoryMain: '', // 初始为空，动态加载后设置默认值
  categorySub: '',  // 初始为空
})

// 创建router实例
const router = useRouter()

// 主要类别选项 - 动态加载
const mainCategoryOptions = ref<CategoryOption[]>([])

// 店铺类别选项 - 动态加载
const subCategoryOptions = ref<CategoryOption[]>([])

// 获取类别数据的函数
const fetchShopCategories = async (parentId: string | null = null) => {
  try {
    showLoadingToast({
      message: '加载中...',
      forbidClick: true,
    })

    const response = await getShopCategoryByParentId(parentId)

    closeToast()
    // 将API返回的数据转换为组件期望的CategoryOption格式
    return response.data.map((category: any) => ({
      text: category.shopCategoryName,
      value: category.shopCategoryId.toString()
    }))
  } catch (error) {
    closeToast()
    showToast('加载类别失败')
    return []
  }
}

// 初始化加载主要类别
const initMainCategories = async () => {
  const categories = await fetchShopCategories(null)
  mainCategoryOptions.value = categories

  // 设置默认选中第一个主要类别
  if (categories.length > 0 && categories[0]) {
    form.value.categoryMain = categories[0].value
    // 加载第一个主要类别的子类别
    await fetchSubCategories(categories[0].value)
  }
}

// 根据主要类别加载店铺类别
const fetchSubCategories = async (parentId: string) => {
  const categories = await fetchShopCategories(parentId)
  subCategoryOptions.value = categories

  // 设置默认选中第一个店铺类别
  if (categories.length > 0 && categories[0]) {
    form.value.categorySub = categories[0].value
  } else {
    form.value.categorySub = ''
  }
}

// 监听主要类别变化，动态加载店铺类别
watch(
  () => form.value.categoryMain,
  (newValue) => {
    if (newValue) {
      fetchSubCategories(newValue)
    }
  },
  { immediate: false }
)

// 页面加载时初始化数据
onMounted(() => {
  initMainCategories()
})

// 图片上传处理
const afterRead = (file: any) => {
  form.value.images = [file.file]
  console.log('上传文件:', file)
}

// 提交表单
const onSubmit = async () => {
  try {
    showLoadingToast({
      message: '创建中...',
      forbidClick: true,
    })
    
    // 准备提交数据
    const submitData = {
      ...form.value,
      shopCategoryId: form.value.categorySub, // 使用子类别ID作为店铺类别
    }
    
    // 调用注册店铺接口
    await registerShop(submitData)
    
    closeToast()
    showToast('创建店铺成功')
    
    // 跳转到MyShops页面
    router.push('/MyShops')
  } catch (error) {
    closeToast()
    showToast('创建店铺失败，请重试')
    console.error('创建店铺失败：', error)
  }
}

// 取消按钮处理
const handleCancel = () => {
  // 直接跳转到MyShops页面
  router.push('/MyShops')
}
</script>

<style scoped>
@import './style.css';
</style>
