<template>
  <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" size="large">
    <el-form-item prop="identifier">
      <el-input v-model="loginForm.identifier" placeholder="请输入用户名">
        <template #prefix>
          <el-icon class="el-input__icon">
            <user />
          </el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="credential">
      <el-input v-model="loginForm.credential" type="password" placeholder="请输入密码" show-password autocomplete="new-password">
        <template #prefix>
          <el-icon class="el-input__icon">
            <lock />
          </el-icon>
        </template>
      </el-input>
    </el-form-item>
  </el-form>
  <div class="login-btn">
    <el-button type="primary" size="large" :loading="loading" @click="login(loginFormRef)">
      登 录
    </el-button>
  </div>
</template>

<script setup lang="ts">

import type { ElForm } from "element-plus";
import { ElNotification } from "element-plus";
import { onBeforeUnmount, onMounted, reactive, ref } from "vue";
import { useRouter } from "vue-router";

import { Login } from "@/api/interface";
import { loginApi } from "@/api/modules/login";
import { HOME_URL } from "@/config";
import { initDynamicRouter } from "@/routers/modules/dynamicRouter";
import { useKeepAliveStore } from "@/stores/modules/keepAlive";
import { useTabsStore } from "@/stores/modules/tabs";
import { useUserStore } from "@/stores/modules/user";

const router = useRouter();
const userStore = useUserStore();
const tabsStore = useTabsStore();
const keepAliveStore = useKeepAliveStore();

type FormInstance = InstanceType<typeof ElForm>;
const loginFormRef = ref<FormInstance>();
const loginRules = reactive({
  identifier: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  credential: [{ required: true, message: "请输入密码", trigger: "blur" }]
});

const loading = ref(false);
const loginForm = reactive<Login.ReqLoginForm>({
  identifier: "",
  credential: "",
  platform: "manager"
});

// login
const login = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.validate(async valid => {
    if (!valid) return;
    loading.value = true;
    try {
      // 1.执行登录接口
      const { data } = await loginApi(loginForm);
      userStore.setToken(data);

      // 2.添加动态路由
      await initDynamicRouter();

      // 3.清空 tabs、keepAlive 数据
      tabsStore.setTabs([]);
      keepAliveStore.setKeepAliveName([]);

      // 4.跳转到首页
      router.push(HOME_URL);
      ElNotification({
        title: "登录成功",
        message: "欢迎使用 O2O 管理后台",
        type: "success",
        duration: 3000
      });
    } catch {
      // 登录失败的错误提示已在拦截器中统一处理
    } finally {
      loading.value = false;
    }
  });
};

onMounted(() => {
  // 监听 enter 事件（调用登录）
  document.onkeydown = (e: KeyboardEvent) => {
    if (e.code === "Enter" || e.code === "enter" || e.code === "NumpadEnter") {
      if (loading.value) return;
      login(loginFormRef.value);
    }
  };
});

onBeforeUnmount(() => {
  document.onkeydown = null;
});
</script>

<style scoped lang="scss">
@use "../index";
</style>
