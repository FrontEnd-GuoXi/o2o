import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { Button, Field, Form, Uploader, Switch, Toast, CellGroup, Cell} from 'vant';
 

import App from './App.vue'
import router from './router'
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

app.use(createPinia())
app.use(router)

app.mount('#app')
