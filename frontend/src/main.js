import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios'

// 检查本地存储中的token
const token = localStorage.getItem('token')
if (token) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
}

// 设置axios基础配置
axios.defaults.baseURL = '/api'

const app = createApp(App)
app.config.globalProperties.$axios = axios
app.use(router)
app.use(ElementPlus)
app.mount('#app')