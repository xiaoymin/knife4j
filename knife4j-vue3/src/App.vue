<template>
  <div id="app">
    <a-config-provider >
      <a-spin :spinning="showLoading" :tip="loadingTip">
        <router-view />
      </a-spin>
    </a-config-provider>
  </div>
</template>
<script setup>

import { useGlobalsStore } from '@/store/modules/global.js'
import { computed } from 'vue'

const store = useGlobalsStore()

const showLoading = computed(() => {
  return store.loading.show
})
const loadingTip = computed(() => {
  return store.loading.text
})
</script>

<style>
/* 字体优化 */
* {
  font-family: 'PingFang SC', 'Source Han Sans CN', 'Helvetica Neue', Helvetica, 'Hiragino Sans GB', 'Microsoft YaHei',
    '微软雅黑', Arial, sans-serif;
}

/* 滚动条优化 */
body {
  overflow-y: scroll;
}

html {
  overflow-y: overlay;
}

::-webkit-scrollbar {
  width: 6px;
  background-color: transparent;
}

::-webkit-scrollbar:horizontal {
  height: 6px;
}

::-webkit-scrollbar-track {
  border-radius: 10px;
}

::-webkit-scrollbar-thumb {
  background-color: #0003;
  border-radius: 10px;
  transition: all 0.2s ease-in-out;
}

::-webkit-scrollbar-thumb:hover {
  cursor: pointer;
  background-color: #0000004d;
}
</style>
