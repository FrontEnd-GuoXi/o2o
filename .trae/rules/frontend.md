# 前端开发规范 (Frontend Rules)

## 项目概览

本项目包含两个前端子项目：

### o2o-web（用户端 - 移动端）
| 项 | 技术 |
|---|---|
| 框架 | Vue 3.5 + TypeScript 5.9 |
| 构建 | Vite (rolldown-vite) |
| 状态管理 | Pinia 3.0 |
| 路由 | Vue Router 4.6 |
| UI 组件库 | Vant 4.9（移动端） |
| 样式 | CSS + postcss-px-to-viewport（vw 适配） |
| 代码检查 | ESLint 9 + Oxlint + Prettier |
| 测试 | Vitest |

### o2o-management-web（管理端 - PC 端）
| 项 | 技术 |
|---|---|
| 框架 | Vue 3.2 + TypeScript 4.5 |
| 构建 | Vue CLI 5 |
| 状态管理 | Vuex 4 |
| 路由 | Vue Router 4 |
| 样式 | Sass/SCSS |
| 代码检查 | ESLint 7 |

---

## 通用规范

### 1. 目录结构
```
src/
├── api/            # 接口请求层（按模块拆分）
├── components/     # 公共组件
├── pages/          # 页面组件（o2o-web）
├── views/          # 页面组件（o2o-management-web）
├── router/         # 路由配置
├── stores/         # Pinia Store（o2o-web）
├── store/          # Vuex Store（o2o-management-web）
├── utils/          # 工具函数
└── styles/         # 全局样式
```

### 2. 命名规范
- 组件目录: `PascalCase`（如 `HomeWork/`, `ShopCard/`，内部包含 `index.vue` + `style.css` + `useXxx.ts`）
- 页面目录: `PascalCase`（如 `O2oHome/`, `ShopDetail/`）
- 工具/API 文件: `camelCase.ts`（如 `request.ts`, `shop.ts`）
- Store 文件: `camelCase.ts`（如 `user.ts`, `cart.ts`）
- Composable 文件: `useXxx.ts`（如 `useHomeWork.ts`, `useShopCard.ts`）
- CSS 文件: `style.css`（组件内）或 `kebab-case.css`（页面级，如 `login.css`）
- 变量/函数: `camelCase`
- 类型/接口: `PascalCase`
- 常量: `UPPER_SNAKE_CASE`

### 3. TypeScript 规范
- 所有新代码必须使用 TypeScript
- API 接口定义必须声明请求参数和响应类型
- 避免使用 `any`，优先使用 `unknown` 或具体类型
- Store 中的 state/getters/actions 需有类型推断

### 4. Vue 组件规范

#### 组件文件结构
每个组件以文件夹形式组织，内部文件拆分为模板、样式、逻辑三部分：

```
HomeWork/
├── index.vue          # 组件模板 + 简单逻辑（<script setup>）
├── style.css          # 组件样式（scoped 或独立 CSS）
└── useHomeWork.ts     # 组件核心逻辑（Composable，状态、方法、生命周期等）
```

**规则：**
- 组件目录名: `PascalCase`（如 `HomeWork/`, `ShopCard/`）
- 模板文件: 固定为 `index.vue`
- 样式文件: `style.css`（o2o-web）或 `style.scss`（o2o-management-web）
- 逻辑文件: `useXxx.ts`（如 `useHomeWork.ts`、`useShopCard.ts`），导出 composable 函数
- `index.vue` 中只保留模板绑定和简单调用，核心逻辑全部抽到 `useXxx.ts`
- 若组件足够简单（无复杂逻辑），可省略 `useXxx.ts`，样式也可内联在 `index.vue` 的 `<style scoped>` 中

#### 编码规范
- 使用 `<script setup lang="ts">` 语法（o2o-web）
- 使用 Composition API，避免 Options API
- 组件 props 使用 `defineProps<T>()` 泛型声明
- 组件 emits 使用 `defineEmits<T>()` 泛型声明
- 模板中 v-for 必须绑定 key
- 避免在模板中写复杂逻辑，用 computed 或 methods

### 5. API 请求规范
- 统一使用 `utils/request.ts` 封装的请求实例
- API 按模块拆分到 `api/` 目录下的独立文件
- 请求拦截器统一添加 token
- 响应拦截器统一处理错误
- 每个 API 函数返回 Promise，调用方用 async/await

### 6. 样式规范
- o2o-web: 使用 Vant 组件样式 + 自定义 CSS，适配移动端
- o2o-management-web: 使用 Sass/SCSS
- 移动端使用 vw 单位做响应式适配（postcss-px-to-viewport 会自动转换 px）
- 组件样式使用 `<style scoped>` 避免污染
- 避免使用 `!important`，优先通过选择器优先级解决

### 7. 状态管理规范
- o2o-web: Pinia store，使用 `defineStore` + Composition API 风格
- o2o-management-web: Vuex store，使用 modules 分模块
- Store 只放跨组件共享的状态，局部状态放组件内

### 8. 路由规范
- 路由懒加载: `() => import('@/pages/Xxx/index.vue')`
- 路由守卫在 `router/permission.ts` 中统一管理
- 需要登录的页面添加 `meta: { requiresAuth: true }`

### 9. 代码格式化
- 提交前运行 `npm run lint` 和 `npm run format`
- 遵循 Prettier 默认配置
- 单引号、无分号、尾逗号（按项目 ESLint 配置）

### 10. 禁止事项
- **禁止混用 Options API 和 Composition API**
- **禁止在 o2o-web 中引入 Vant 以外的 UI 库**
- **禁止在 o2o-management-web 中引入 Vant**
- **禁止直接在组件中操作 DOM**（除非必要，用 ref）
- **禁止使用 `var`**，只用 `const` 和 `let`
- **禁止在 `v-for` 中使用 `v-if`**
- **禁止提交 `console.log` 到生产代码**