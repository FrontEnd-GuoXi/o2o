<template>
  <div class="add-shop-container">
    <!-- 顶部公共头部 -->
    <O2oHeader :title="isEdit ? '编辑店铺' : '新建店铺'" />

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

        <!-- 所在区域 -->
        <div class="form-section">
          <van-field
            v-model="selectedAreaName"
            label="所在区域"
            placeholder="请选择所在区域"
            required
            readonly
            is-link
            @click="showAreaPicker = true"
            :rules="[{ required: true, message: '请选择所在区域' }]"
          />
          <van-popup v-model:show="showAreaPicker" position="bottom">
            <van-picker
              :columns="areaOptions"
              @confirm="onAreaConfirm"
              @cancel="showAreaPicker = false"
            />
          </van-popup>
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
import { useRouter, useRoute } from 'vue-router'
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
  Popup as VanPopup,
  Picker as VanPicker,
  showToast,
  showLoadingToast,
} from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import { getShopCategoryByParentId, registerShop, getShopById, type CategoryOption } from '@/api/shop'
import { getAreaList } from '@/api/area'
import { getImageUrl, handleImageError } from '@/utils/image'

interface ShopForm {
  name: string;
  desc: string;
  address: string;
  area: string;
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
  area: '',
  phone: '',
  weight: 0,
  enabled: true,
  images: [],
  categoryMain: '', // 初始为空，动态加载后设置默认值
  categorySub: '',  // 初始为空
})

// 创建router实例
const router = useRouter()
const route = useRoute()

// 是否为编辑模式
const isEdit = ref(false)
const shopId = ref<number | null>(null)

// 主要类别选项 - 动态加载
const mainCategoryOptions = ref<CategoryOption[]>([])

// 店铺类别选项 - 动态加载
const subCategoryOptions = ref<CategoryOption[]>([])

// 区域选项
const areaOptions = ref<{ text: string; value: string }[]>([])
const showAreaPicker = ref(false)
const selectedAreaName = ref('')

// 获取类别数据的函数
const fetchShopCategories = async (parentId: string | null = null) => {
  const toast = showLoadingToast({
    message: '加载中...',
    forbidClick: true,
  })
  try {
    const response = await getShopCategoryByParentId(parentId)

    toast.close()
    // 将API返回的数据转换为组件期望的CategoryOption格式
    return response.data.map((category: any) => ({
      text: category.shopCategoryName,
      value: category.shopCategoryId.toString()
    }))
  } catch (e) {
    toast.close()
    console.error('加载类别失败：', e)
    return []
  }
}

// 初始化加载主要类别
const initMainCategories = async () => {
  const categories = await fetchShopCategories(null)
  mainCategoryOptions.value = categories
}

// 初始化加载区域列表
const initAreaList = async () => {
  try {
    const response = await getAreaList()
    areaOptions.value = response.data.map(area => ({
      text: area.areaName,
      value: area.areaId.toString()
    }))

    // 默认选中第一个区域
    if (areaOptions.value.length > 0) {
      form.value.area = areaOptions.value[0].value
      selectedAreaName.value = areaOptions.value[0].text
    }
  } catch (e) {
    console.error('加载区域列表失败：', e)
  }
}

// 区域选择确认
const onAreaConfirm = ({ selectedOptions }: any) => {
  const option = selectedOptions[0]
  form.value.area = option.value
  selectedAreaName.value = option.text
  showAreaPicker.value = false
}

// 根据主要类别加载店铺类别
const fetchSubCategories = async (parentId: string) => {
  const categories = await fetchShopCategories(parentId)
  subCategoryOptions.value = categories
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
onMounted(async () => {


  // 检查是否有 shopId 参数，如果有则进入编辑模式
  const id = route.query.shopId
  if (id) {
    isEdit.value = true
    shopId.value = Number(id)
    fetchShopDetail(shopId.value)
  }

    await initMainCategories()
    await initAreaList()
})

// 获取店铺详情用于回显
const fetchShopDetail = async (id: number) => {
  const toast = showLoadingToast({
    message: '获取详情中...',
    forbidClick: true,
  })
  try {
    const res = await getShopById(id)
    const shop = res.data
    if (shop) {
      const imgUrl =  location.origin + getImageUrl(shop.shopImg)
      console.log(imgUrl)
      form.value = {
        name: shop.shopName,
        desc: shop.shopDesc,
        address: shop.shopAddr,
        area: shop.area?.areaId?.toString() || '',
        phone: shop.phone,
        weight: shop.priority,
        enabled: shop.enableStatus === 1,
        images: [ { url: imgUrl } ], // 编辑时图片通常需要重新上传，这里先清空
        categoryMain: shop.shopCategoryParentId || '',
        categorySub: shop.shopCategoryId,
      }

      // 更新显示的区域名称
      const area = areaOptions.value.find(opt => opt.value === form.value.area)
      if (area) {
        selectedAreaName.value = area.text
      }
    }
    toast.close()
  } catch (error) {
    toast.close()
    console.error('获取店铺详情失败:', error)
    showToast('获取店铺详情失败')
  }
}

// 图片上传处理
const afterRead = (file: any) => {
  // form.value.images = [file.file]
  console.log('上传文件:', file)
}

// 提交表单
const onSubmit = async () => {
  const toast = showLoadingToast({
    message: '创建中...',
    forbidClick: true,
  })
  try {
    // 准备提交数据
    const formData = new FormData()
    if (isEdit.value && shopId.value) {
      formData.append('shopId', String(shopId.value))
    }
    formData.append('shopName', form.value.name)
    formData.append('shopDesc', form.value.desc)
    formData.append('shopAddr', form.value.address)
    formData.append('phone', form.value.phone)
    formData.append('priority', String(form.value.weight))
    formData.append('enableStatus', form.value.enabled ? '1' : '0')
    formData.append('area', form.value.area)
    formData.append('categorySup', form.value.categoryMain)
    formData.append('categorySub', form.value.categorySub)
    console.log('添加图片:', form.value.images[0])
    // 添加图片
    if (form.value.images && form.value.images.length > 0) {
      const fileItem = form.value.images[0]
      // 只有当存在 file 对象时才添加（即用户重新选择了图片）
      if (fileItem.file) {
        formData.append('shopImg', fileItem.file)
      }
    }

    // 调用注册店铺接口
    await registerShop(formData)

    toast.close()
    showToast(isEdit.value ? '修改店铺成功' : '创建店铺成功')

    // 跳转到MyShops页面
    router.push('/MyShops')
  } catch (error) {
    toast.close()
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
