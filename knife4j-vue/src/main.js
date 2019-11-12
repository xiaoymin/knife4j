import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './registerServiceWorker'
//import 'ant-design-vue/dist/antd.css'
import '@/assets/iconfonts/iconfont.css'
import Antd from 'ant-design-vue'
import axios from 'axios'
import {
  Icon
} from 'ant-design-vue'

/***
 * 注册全局组件
 */
import Main from '@/views/index/Main'
import Hello2 from '@/views/Hello2'
Vue.component('Main', Main)
Vue.component('Hello2', Hello2)
/***
 * 自定义图标
 */
import iconFront from './assets/iconfonts/iconfont.js'
const myicon = Icon.createFromIconfontCN({
  scriptUrl: iconFront
})
Vue.component('my-icon', myicon)
/***
 * 请求方法
 */
import MethodType from '@/components/common/MethodApi'
Vue.component('MethodType', MethodType)
/***
 * api详情展示组件
 */
import ApiInfo from '@/views/api/index'
Vue.component('ApiInfo', ApiInfo);


Vue.config.productionTip = false
// 响应数据拦截器
axios.interceptors.response.use(function (response) {
  var data = response.data;
  return data
}, function (error) {
  return Promise.reject(error)
})

Vue.prototype.$axios = axios


Vue.use(Antd)
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
