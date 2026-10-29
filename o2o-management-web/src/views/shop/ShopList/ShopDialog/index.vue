<template>
  <el-dialog
    v-model="visible"
    title="新建店铺"
    width="600px"
    top="5vh"
    :close-on-click-modal="false"
    destroy-on-close
  >
    <el-scrollbar max-height="65vh">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="店铺名称" prop="shopName">
          <el-input v-model="form.shopName" placeholder="请输入店铺名称" />
        </el-form-item>
        <el-form-item label="店铺描述" prop="shopDesc">
          <el-input v-model="form.shopDesc" type="textarea" placeholder="请输入店铺描述" />
        </el-form-item>
        <el-form-item label="店铺地址" prop="shopAddr">
          <el-input v-model="form.shopAddr" placeholder="请输入店铺地址" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-input-number v-model="form.priority" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="enableStatus">
          <el-radio-group v-model="form.enableStatus">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所属区域" prop="area">
          <el-select v-model="form.area" placeholder="请选择区域" style="width: 100%">
            <el-option
              v-for="item in areaList"
              :key="item.areaId"
              :label="item.areaName"
              :value="item.areaId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="一级分类" prop="parentCategoryId">
          <el-select v-model="form.parentCategoryId" placeholder="请选择一级分类" style="width: 100%">
            <el-option
              v-for="item in parentCategoryList"
              :key="item.shopCategoryId"
              :label="item.shopCategoryName"
              :value="item.shopCategoryId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="二级分类" prop="categorySub">
          <el-select v-model="form.categorySub" placeholder="请选择二级分类" style="width: 100%">
            <el-option
              v-for="item in subCategoryList"
              :key="item.shopCategoryId"
              :label="item.shopCategoryName"
              :value="item.shopCategoryId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="店铺图片" required>
          <el-upload
            :auto-upload="false"
            :limit="1"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
            :file-list="shopImgFileName ? [{ name: shopImgFileName }] : []"
            accept="image/*"
          >
            <el-button type="primary">选择图片</el-button>
            <template #tip>
              <div class="el-upload__tip">支持 jpg/png 格式，大小不超过 2MB</div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
    </el-scrollbar>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="submit">确认创建</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts" name="shopDialog">
import { useShopDialog } from "./useShopDialog";

const emit = defineEmits<{
  (e: "success"): void;
}>();

const {
  visible,
  formRef,
  form,
  areaList,
  parentCategoryList,
  subCategoryList,
  shopImgFileName,
  rules,
  submitting,
  open,
  handleFileChange,
  handleFileRemove,
  submit
} = useShopDialog(emit);

defineExpose({ open });
</script>

<style scoped lang="scss">
@use "./index";
</style>