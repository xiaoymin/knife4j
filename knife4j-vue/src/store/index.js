import Vue from 'vue'
import Vuex from 'vuex'

import header from './module/header'
import globals from './module/global'
Vue.use(Vuex)
const store = new Vuex.Store({
  modules: {
    header,
    globals
  }
});


export default store;
