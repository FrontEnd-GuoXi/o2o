<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-logo">
        <img class="login-icon" src="@/assets/images/logo.svg" alt="" />
        <h2 class="logo-text">O2O 管理后台</h2>
        <p class="logo-subtitle">欢迎回来，请登录您的账号</p>
      </div>
      <LoginForm />
    </div>
  </div>
</template>

<script setup lang="ts" name="login">
import LoginForm from "./components/LoginForm.vue";
</script>

<style scoped lang="scss">
@use "./index";
</style>
