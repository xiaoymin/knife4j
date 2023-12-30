import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
//import './registerServiceWorker'
//import 'ant-design-vue/dist/antd.css'
import '@/assets/iconfonts/iconfont.css'
/* import Antd from 'ant-design-vue' */
import axios from 'axios'
import {
  Icon
} from 'ant-design-vue'

import {
  Menu, Input, Select, Table, Tabs,
  Radio, Layout, Button,
  Tag, Divider, Tree, Dropdown, Form, Modal,
  Col, Row, AutoComplete, Tooltip, Spin, Badge, Card,
  Collapse, Checkbox, message
} from 'ant-design-vue'
Vue.prototype.$message = message;
//Vue.use(message)
Vue.use(Card)
Vue.use(Badge);
Vue.use(Modal)
Vue.use(Form)
Vue.use(Collapse)
Vue.use(Checkbox)
Vue.use(Tooltip)
Vue.use(Spin)
Vue.use(AutoComplete)
Vue.use(Col)
Vue.use(Row)
Vue.use(Icon)
Vue.use(Menu)
Vue.use(Input)
Vue.use(Select)
Vue.use(Table)
Vue.use(Tabs)
Vue.use(Radio)
Vue.use(Layout)
Vue.use(Button)
Vue.use(Tag)
Vue.use(Divider)
Vue.use(Tree)
Vue.use(Dropdown)


import kloading from '@/components/loading'
import VueI18n from 'vue-i18n'

Vue.use(VueI18n)
Vue.use(kloading)

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
import OtherMarkdown from '@/views/othermarkdown/index'

Vue.component('ApiInfo', ApiInfo);
Vue.component('Authorize', Authorize);
Vue.component('SwaggerModels', SwaggerModels);
Vue.component('GlobalParameters', GlobalParameters);
Vue.component('Settings', Settings);
Vue.component('OfficelineDocument', OfficelineDocument);
Vue.component('OtherMarkdown', OtherMarkdown);

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
//本地缓存models
import knife4jModel from './store/knife4jModels'

Vue.prototype.$Knife4jModels = knife4jModel;

/**
 * 日志组件
 */
import logger from '@/core/logger'
Vue.prototype.$logger = logger;

String.prototype.gblen = function () {
  var len = 0;
  for (var i = 0; i < this.length; i++) {
    if (this.charCodeAt(i) > 127 || this.charCodeAt(i) == 94) {
      len += 2;
    } else {
      len++;
    }
  }
  return len;
}

String.prototype.startWith = function (str) {
  var reg = new RegExp("^" + str);
  return reg.test(this);
}

import i18nZH from '@/assets/common/lang/zh'
import i18nEN from '@/assets/common/lang/en'
import i18nJP from '@/assets/common/lang/jp'

//i18n
const i18n = new VueI18n({
  locale: 'zh-CN',
  messages: {
    'zh-CN': i18nZH,
    'en-US': i18nEN,
    'ja-JP': i18nJP
  }
})

//Vue.use(localStore)
//Vue.use(Antd)
new Vue({
  router,
  store,
  i18n,
  render: h => h(App)
}).$mount('#app')
