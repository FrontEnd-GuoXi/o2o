import { ref, reactive, watch } from "vue";
import { ElMessage, type FormInstance, type FormRules, type UploadFile } from "element-plus";
import { registerShopApi } from "@/api/modules/shop";
import { getAreaListApi } from "@/api/modules/area";
import { getShopCategoryByParentIdApi } from "@/api/modules/shopCategory";
import type { Area, ShopCategory } from "@/api/interface";

export function useShopDialog(emit: { (e: "success"): void }) {
  const visible = ref(false);
  const formRef = ref<FormInstance>();
  const submitting = ref(false);
  const areaList = ref<Area[]>([]);
  const parentCategoryList = ref<ShopCategory[]>([]);
  const subCategoryList = ref<ShopCategory[]>([]);

  const form = reactive({
    shopName: "",
    shopDesc: "",
    shopAddr: "",
    phone: "",
    priority: 1,
    enableStatus: 1,
    area: undefined as number | undefined,
    parentCategoryId: undefined as number | undefined,
    categorySub: undefined as number | undefined
  });

  const shopImgFile = ref<File | null>(null);
  const shopImgFileName = ref("");

  const rules: FormRules = {
    shopName: [{ required: true, message: "请输入店铺名称", trigger: "blur" }],
    shopAddr: [{ required: true, message: "请输入店铺地址", trigger: "blur" }],
    phone: [{ required: true, message: "请输入联系电话", trigger: "blur" }],
    priority: [{ required: true, message: "请输入优先级", trigger: "blur" }],
    enableStatus: [{ required: true, message: "请选择状态", trigger: "change" }],
    area: [{ required: true, message: "请选择区域", trigger: "change" }],
    parentCategoryId: [{ required: true, message: "请选择一级分类", trigger: "change" }],
    categorySub: [{ required: true, message: "请选择二级分类", trigger: "change" }]
  };

  const fetchAreaList = async () => {
    try {
      const { data } = await getAreaListApi();
      areaList.value = data ?? [];
    } catch {
      areaList.value = [];
    }
  };

  const fetchParentCategories = async () => {
    try {
      const { data } = await getShopCategoryByParentIdApi(0);
      parentCategoryList.value = data ?? [];
    } catch {
      parentCategoryList.value = [];
    }
  };

  const fetchSubCategories = async (parentId: number) => {
    try {
      const { data } = await getShopCategoryByParentIdApi(parentId);
      subCategoryList.value = data ?? [];
    } catch {
      subCategoryList.value = [];
    }
  };

  watch(
    () => form.parentCategoryId,
    (newVal) => {
      form.categorySub = undefined;
      subCategoryList.value = [];
      if (newVal != null) {
        fetchSubCategories(newVal);
      }
    }
  );

  const open = () => {
    resetForm();
    visible.value = true;
    fetchAreaList();
    fetchParentCategories();
  };

  const resetForm = () => {
    form.shopName = "";
    form.shopDesc = "";
    form.shopAddr = "";
    form.phone = "";
    form.priority = 1;
    form.enableStatus = 1;
    form.area = undefined;
    form.parentCategoryId = undefined;
    form.categorySub = undefined;
    subCategoryList.value = [];
    shopImgFile.value = null;
    shopImgFileName.value = "";
    formRef.value?.resetFields();
  };

  const handleFileChange = (file: UploadFile) => {
    if (file.raw) {
      shopImgFile.value = file.raw;
      shopImgFileName.value = file.name;
    }
  };

  const handleFileRemove = () => {
    shopImgFile.value = null;
    shopImgFileName.value = "";
  };

  const submit = async () => {
    if (!formRef.value) return;
    const valid = await formRef.value.validate().catch(() => false);
    if (!valid) return;

    if (!shopImgFile.value) {
      ElMessage.warning("请上传店铺图片");
      return;
    }

    submitting.value = true;
    try {
      const formData = new FormData();
      formData.append("shopName", form.shopName);
      if (form.shopDesc) formData.append("shopDesc", form.shopDesc);
      formData.append("shopAddr", form.shopAddr);
      formData.append("phone", form.phone);
      formData.append("priority", String(form.priority));
      formData.append("enableStatus", String(form.enableStatus));
      formData.append("area", String(form.area));
      formData.append("categorySub", String(form.categorySub));
      formData.append("shopImg", shopImgFile.value);

      await registerShopApi(formData);
      ElMessage.success("店铺创建成功");
      visible.value = false;
      emit("success");
    } finally {
      submitting.value = false;
    }
  };

  return {
    visible,
    formRef,
    form,
    areaList,
    parentCategoryList,
    subCategoryList,
    shopImgFile,
    shopImgFileName,
    rules,
    submitting,
    open,
    handleFileChange,
    handleFileRemove,
    submit
  };
}