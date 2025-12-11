import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { Button, Field, Form, Uploader, Switch, Toast, CellGroup, Cell, Icon} from 'vant';


import App from './App.vue'
import router from './router'
import './router/permission' // 引入路由权限控制
import 'vant/lib/index.css';

const app = createApp(App)

app.use(Button)
app.use(Field)
app.use(Form)
app.use(Uploader)
app.use(CellGroup)
app.use(Switch)
app.use(Toast)
app.use(Cell)
app.use(Icon)

app.use(createPinia())
app.use(router)

app.mount('#app')
