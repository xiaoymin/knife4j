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
Vue.component('Main', Main)
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
import Authorize from '@/views/settings/Authorize'
import SwaggerModels from '@/views/settings/SwaggerModels';
import GlobalParameters from '@/views/settings/GlobalParameters';
import Settings from '@/views/settings/Settings';
import OfficelineDocument from '@/views/settings/OfficelineDocument';

Vue.component('ApiInfo', ApiInfo);
Vue.component('Authorize', Authorize);
Vue.component('SwaggerModels', SwaggerModels);
Vue.component('GlobalParameters', GlobalParameters);
Vue.component('Settings', Settings);
Vue.component('OfficelineDocument', OfficelineDocument);


Vue.config.productionTip = false
// 响应数据拦截器
axios.interceptors.response.use(function (response) {
  var data = response.data;
  return data
}, function (error) {
  return Promise.reject(error)
})

Vue.prototype.$axios = axios
/***
 * 本地存储解决方案
 */
import localStore from './store/local'
Vue.prototype.$localStore = localStore;

//Vue.use(localStore)
Vue.use(Antd)
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
