import { ref } from "vue";
import { getShopListApi } from "@/api/modules/shop";
import type { ShopVO } from "@/api/interface";

export function useShopList() {
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

  return {
    shopList,
    loading,
    fetchShopList
  };
}