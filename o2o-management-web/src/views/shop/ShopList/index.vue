<template>
  <div class="shop-list card">
    <el-table :data="shopList" border stripe v-loading="loading" style="width: 100%">
      <el-table-column prop="shopId" label="店铺ID" width="100" />
      <el-table-column prop="shopName" label="店铺名称" min-width="150" />
      <el-table-column prop="shopCategoryName" label="店铺分类" width="120" />
      <el-table-column prop="areaName" label="所属区域" width="120" />
      <el-table-column prop="shopAddr" label="店铺地址" min-width="200" show-overflow-tooltip />
      <el-table-column prop="phone" label="联系电话" width="130" />
      <el-table-column prop="priority" label="优先级" width="80" align="center" />
      <el-table-column prop="enableStatus" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.enableStatus === 1 ? 'success' : 'danger'">
            {{ row.enableStatus === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="avgScore" label="评分" width="80" align="center">
        <template #default="{ row }">
          {{ row.avgScore != null ? row.avgScore : '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="evaluationCount" label="评价数" width="80" align="center" />
      <el-table-column prop="createTime" label="创建时间" width="170" />
    </el-table>
  </div>
</template>

<script setup lang="ts" name="shopList">
import { ref, onMounted } from "vue";
import { getShopListApi } from "@/api/modules/shop";
import { ShopVO } from "@/api/interface";

const shopList = ref<ShopVO[]>([]);
const loading = ref(false);

const fetchShopList = async () => {
  loading.value = true;
  try {
    const { data } = await getShopListApi();
    shopList.value = data ?? [];
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchShopList();
});
</script>

<style scoped lang="scss">
@use "./index";
</style>