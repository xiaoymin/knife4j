import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './registerServiceWorker'
//import 'ant-design-vue/dist/antd.css'
import Antd from 'ant-design-vue'
import axios from 'axios'
/***
 * 注册全局组件
 */
import Main from '@/views/index/Main'
import Hello2 from '@/views/Hello2'

Vue.component('Main', Main)
Vue.component('Hello2', Hello2)

Vue.config.productionTip = false
Vue.prototype.$axios = axios

Vue.use(Antd)
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
