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

<style scoped>
</style>
