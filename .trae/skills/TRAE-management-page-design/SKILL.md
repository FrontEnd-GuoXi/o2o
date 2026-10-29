---
name: "TRAE-management-page-design"
description: "专用于 o2o-management-web（Vue 3 + Vue CLI + Element Plus PC 管理端）的页面 UI 设计。当用户需要设计管理端页面、后台界面、优化 PC 端样式时调用。"
---

# o2o-management-web 页面设计规范

## 适用项目

`e:\study\java\o2o\o2o-management-web` — Vue 3.2 + TypeScript 4.5 + Vue CLI 5 + Vuex 4 的 PC 管理后台项目。

---

## 技术栈约束（重要）

| 项 | 技术 | 注意 |
|---|---|---|
| 框架 | Vue 3.2 | 非 Vue 3.5，注意 API 兼容 |
| 组件语法 | **vue-class-component** (`@Options` 装饰器 + class) | **禁止使用 `<script setup>` 和 Composition API** |
| 状态管理 | Vuex 4 | **禁止使用 Pinia** |
| 样式 | **Sass/SCSS** (`<style scoped lang="scss">`) | **禁止使用纯 CSS** |
| 构建 | Vue CLI 5 (webpack) | **禁止使用 Vite** |
| TypeScript | ~4.5.5 | `experimentalDecorators: true` |
| UI 库 | **Element Plus** | PC 端管理后台首选 |

### 组件写法规范

```vue
<template>
  <div class="shop-management">
    <!-- 模板内容 -->
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component'

@Options({
  components: { /* 子组件 */ }
})
export default class ShopManagement extends Vue {
  // data
  loading = false
  shopList: Shop[] = []

  // computed
  get totalCount(): number {
    return this.shopList.length
  }

  // methods
  async fetchShopList(): Promise<void> {
    this.loading = true
    // ...
    this.loading = false
  }

  // lifecycle
  mounted(): void {
    this.fetchShopList()
  }
}
</script>

<style scoped lang="scss">
.shop-management {
  padding: 20px;
}
</style>
```

---

## 一、目录结构

```
src/
├── api/              # 接口请求（按模块拆分）
├── assets/           # 静态资源（图片、图标）
├── components/       # 公共组件
│   ├── Layout/       # 布局组件（侧边栏 + 顶栏 + 内容区）
│   └── common/       # 通用组件（表格、表单等）
├── router/           # 路由配置
├── store/            # Vuex Store（modules 分模块）
│   └── modules/
├── styles/           # 全局 SCSS
│   ├── variables.scss   # 变量（颜色、间距、字体）
│   ├── mixins.scss      # Mixin
│   └── global.scss      # 全局样式重置
├── utils/            # 工具函数
│   └── request.ts    # Axios 封装
└── views/            # 页面组件
    ├── shop/         # 店铺管理
    ├── product/      # 商品管理
    ├── order/        # 订单管理
    └── user/         # 用户管理
```

---

## 二、配色方案（PC 管理后台风格）

### 主题色
| 用途 | 颜色值 | 说明 |
|---|---|---|
| 主色（Element Plus 默认蓝） | `#409eff` | 按钮、链接、选中态 |
| 主色深 | `#337ecc` | hover / active 态 |
| 成功绿 | `#67c23a` | 操作成功、状态-已上架 |
| 警告橙 | `#e6a23c` | 警告提示、状态-待审核 |
| 危险红 | `#f56c6c` | 删除、状态-已下架 |
| 信息灰 | `#909399` | 次要信息、状态-草稿 |

### 背景色
| 用途 | 颜色值 |
|---|---|
| 页面底色 | `#f0f2f5` |
| 卡片/表格背景 | `#fff` |
| 侧边栏背景 | `#304156`（深色）或 `#fff`（浅色） |
| 侧边栏 hover | `#263445` |
| 顶栏背景 | `#fff` |

### 文字色
| 用途 | 颜色值 |
|---|---|
| 主文字 | `#303133` |
| 常规文字 | `#606266` |
| 次要文字 | `#909399` |
| 占位文字 | `#c0c4cc` |

### 边框
| 用途 | 颜色值 |
|---|---|
| 表格/卡片边框 | `#ebeef5` |
| 输入框边框 | `#dcdfe6` |
| 分割线 | `#e4e7ed` |

---

## 三、布局规范

### 经典管理后台布局
```
┌──────────────────────────────────────────┐
│  Top Header（顶栏）                        │  height: 60px, bg: #fff
│  [Logo]    [面包屑]    [用户头像/退出]       │  box-shadow
├────────┬─────────────────────────────────┤
│        │                                 │
│  Side  │  Main Content（内容区）           │
│  Bar   │  ┌──────────────────────────┐  │
│  侧边栏 │  │  搜索/筛选区域             │  │
│  w:220 │  │  ┌──────────────────────┐│  │
│        │  │  │  Table / 表格         ││  │
│  bg:   │  │  │  ...                 ││  │
│  #304  │  │  └──────────────────────┘│  │
│  156   │  │  Pagination / 分页        │  │
│        │  └──────────────────────────┘  │
│        │                                 │
└────────┴─────────────────────────────────┘
```

### 布局 CSS 关键值
```scss
// 侧边栏
$sidebar-width: 220px;
$sidebar-bg: #304156;
$sidebar-text: #bfcbd9;
$sidebar-active-text: #409eff;

// 顶栏
$header-height: 60px;
$header-bg: #fff;

// 内容区
$content-bg: #f0f2f5;
$content-padding: 20px;
```

---

## 四、Element Plus 组件使用规范

| 组件 | 用法 & 样式约定 |
|---|---|
| **el-container** | 整体布局容器，配合 `el-aside` + `el-header` + `el-main` |
| **el-menu** | 侧边栏导航，`router` 模式，`background-color: #304156`，`text-color: #bfcbd9` |
| **el-table** | 数据表格，`border` + `stripe`，空状态用 `empty` 插槽 |
| **el-pagination** | 分页，`background` 样式，放在表格下方右对齐 |
| **el-form** | 表单，`label-width="100px"`，搜索区域用 `inline` 模式 |
| **el-input** | 输入框，搜索框配合 `el-button` 使用 |
| **el-select** | 下拉选择，筛选条件常用 |
| **el-date-picker** | 日期选择，订单/数据筛选用 |
| **el-dialog** | 弹窗，用于新增/编辑表单，`width="500px"` ~ `"700px"` |
| **el-button** | 按钮，主操作 `type="primary"`，删除 `type="danger"` |
| **el-tag** | 状态标签，`success` / `warning` / `danger` / `info` |
| **el-switch** | 开关，用于上下架、启用/禁用 |
| **el-card** | 卡片容器，统计面板、详情展示 |
| **el-breadcrumb** | 面包屑导航，顶栏下方 |
| **el-dropdown** | 下拉菜单，用户头像操作 |
| **el-message** | 消息提示，操作成功/失败反馈 |
| **el-message-box** | 确认弹窗，删除前确认 |
| **el-upload** | 文件上传，店铺/商品图片上传 |
| **el-image** | 图片预览，表格中缩略图点击放大 |
| **el-empty** | 空状态，无数据时展示 |
| **el-skeleton** | 骨架屏，首屏加载 |

---

## 五、典型页面模板

### 1. 列表管理页（最常用）
```
┌──────────────────────────────────────────┐
│  面包屑: 首页 > 店铺管理                    │
├──────────────────────────────────────────┤
│  ┌──────────────────────────────────────┐│
│  │ 搜索区域（el-card 或 el-form inline）  ││
│  │ [店铺名称____] [状态▾] [搜索] [重置]    ││
│  └──────────────────────────────────────┘│
│  ┌──────────────────────────────────────┐│
│  │ 操作栏                                ││
│  │ [+ 新增店铺]  [批量删除]  [导出]       ││
│  └──────────────────────────────────────┘│
│  ┌──────────────────────────────────────┐│
│  │ 表格（el-table）                      ││
│  │ │ ID │ 名称 │ 分类 │ 状态 │ 评分 │ 操作││
│  │ │ 1  │ XX店 │ 餐饮 │ 上架 │ 4.5 │ 编辑││
│  │ │ ...                               ││
│  └──────────────────────────────────────┘│
│  分页: [< 1 2 3 ... 10 >]  共 200 条     │
└──────────────────────────────────────────┘
```

### 2. 表单页（新增/编辑）
```
┌──────────────────────────────────────────┐
│  面包屑: 首页 > 店铺管理 > 新增店铺         │
├──────────────────────────────────────────┤
│  ┌──────────────────────────────────────┐│
│  │  el-card                             ││
│  │  ┌────────────────────────────────┐  ││
│  │  │  el-form (label-width: 100px)  │  ││
│  │  │  店铺名称: [____________]       │  ││
│  │  │  店铺分类: [____▾]             │  ││
│  │  │  店铺图片: [上传] [预览]        │  ││
│  │  │  店铺描述: [____________]       │  ││
│  │  │  ...                          │  ││
│  │  │  [保存] [取消]                 │  ││
│  │  └────────────────────────────────┘  ││
│  └──────────────────────────────────────┘│
└──────────────────────────────────────────┘
```

### 3. 详情页
```
┌──────────────────────────────────────────┐
│  面包屑: 首页 > 店铺管理 > 店铺详情         │
├──────────────────────────────────────────┤
│  ┌────────────────┐ ┌──────────────────┐ │
│  │ 基本信息卡片     │ │ 统计卡片          │ │
│  │ 名称: XX店      │ │ 总订单: 1,234    │ │
│  │ 分类: 餐饮       │ │ 总评分: 4.5      │ │
│  │ 状态: 已上架     │ │ 总评价: 567      │ │
│  └────────────────┘ └──────────────────┘ │
│  ┌──────────────────────────────────────┐│
│  │  Tab 切换: [商品列表] [评价列表] [订单] ││
│  │  ┌────────────────────────────────┐  ││
│  │  │ 子表格数据...                    │  ││
│  │  └────────────────────────────────┘  ││
│  └──────────────────────────────────────┘│
└──────────────────────────────────────────┘
```

### 4. 登录页
```
┌──────────────────────────────────────────┐
│                                          │
│                                          │
│           ┌──────────────────┐           │
│           │   O2O 管理后台    │           │
│           │                  │           │
│           │  [用户名____]    │           │
│           │  [密码______]    │           │
│           │                  │           │
│           │  [    登录    ]  │           │
│           │                  │           │
│           └──────────────────┘           │
│                                          │
│           bg: 渐变或浅色背景              │
└──────────────────────────────────────────┘
```

---

## 六、间距 & 尺寸规范

| 间距 | 用途 |
|---|---|
| `8px` | 表单内小间距 |
| `12px` | 按钮间距、标签间距 |
| `16px` | 卡片内边距 |
| `20px` | 内容区 padding、表格与分页间距 |
| `24px` | 搜索区与表格间距 |

| 尺寸 | 用途 |
|---|---|
| 表格行高 | 默认（Element Plus 默认 `48px`） |
| 按钮 | `small` 或默认 `medium` |
| 弹窗宽度 | `500px`（简单表单）~ `700px`（复杂表单）~ `900px`（大表格） |
| 侧边栏宽度 | `220px` |

---

## 七、字体规范

| 层级 | 字号 | 字重 | 用途 |
|---|---|---|---|
| 页面标题 | `20px` | `600` | 面包屑下方标题 |
| 卡片标题 | `18px` | `600` | 卡片 header |
| 表格/正文 | `14px` | `400` | Element Plus 默认 |
| 辅助文字 | `12px` | `400` | 说明文字、时间戳 |

---

## 八、交互规范

### 表格操作
- 行内操作：编辑 / 删除 / 查看 - 不超过 3 个按钮
- 批量操作：勾选后顶部出现批量操作栏
- 删除前必须 `el-message-box` 二次确认

### 表单提交
- 成功后 `el-message.success('保存成功')` + 关闭弹窗/返回列表
- 失败后 `el-message.error(response.msg)` 在表单内显示错误
- 提交按钮 loading 状态防重复提交

### 加载状态
- 表格加载：`v-loading="loading"` 指令
- 按钮加载：`:loading="submitLoading"`
- 首屏：`el-skeleton` 骨架屏

### 空状态
- 无数据：`el-empty description="暂无数据"`
- 搜索无结果：`el-empty description="未找到匹配结果"`
- 网络错误：`el-empty description="加载失败，请重试"` + 重试按钮

---

## 九、SCSS 规范

### 变量定义（variables.scss）
```scss
// 颜色
$primary: #409eff;
$success: #67c23a;
$warning: #e6a23c;
$danger: #f56c6c;
$info: #909399;

// 背景
$bg-page: #f0f2f5;
$bg-card: #fff;
$bg-sidebar: #304156;

// 文字
$text-primary: #303133;
$text-regular: #606266;
$text-secondary: #909399;

// 布局
$sidebar-width: 220px;
$header-height: 60px;
```

### 使用规范
- 组件样式使用 `<style scoped lang="scss">`
- 全局变量放在 `src/styles/variables.scss`，通过 `vue.config.js` 的 `css.loaderOptions` 全局注入
- 嵌套层级不超过 3 层
- 使用 Element Plus 的 CSS 变量覆盖主题色（`:root { --el-color-primary: #409eff; }`）

---

## 十、输出格式

当用户要求设计管理端页面时，按以下顺序输出：

1. **页面布局结构图**（ASCII 示意图 + 标注尺寸/颜色）
2. **Element Plus 组件选型**（列出用到的组件及用途）
3. **Vuex Store 设计**（如涉及新状态）
4. **完整代码**（按项目规范：`.vue` 单文件，class 组件 + SCSS）