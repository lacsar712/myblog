import { createApp } from 'vue'
import Antd from 'ant-design-vue'
import { message, notification } from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import './style.css'
import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(router)
app.use(Antd)

message.config({
  duration: 2,
  maxCount: 3,
})

notification.config({
  duration: 3,
})

app.config.errorHandler = () => {
  notification.error({ message: '页面发生错误', description: '请刷新页面或稍后重试' })
}

app.mount('#app')
