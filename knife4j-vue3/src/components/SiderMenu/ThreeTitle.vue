<template>
  <span>
    <my-icon v-if="item.icon" :type="item.icon"></my-icon>
    <a-badge v-if="enableVersion&&item.hasNew" status="processing" title="新接口" style="margin-bottom:3px;" />
    <span>{{item.name}}</span>
    <span
      v-if="item.num&&!collapsed"
      :class="item.menuClass"
      class="knife4j-menu-badge-num"
    >{{item.num}}</span>
  </span>
</template>

<script>
import { useGlobalsStore } from '@/store/modules/global.js'
import { computed } from 'vue'

export default {
  props: {
    item: Object,
    collapsed: {
      type: Boolean,
      default: false
    }
  },
  setup() {
    const appStore = useGlobalsStore()
    return {
      enableVersion: computed(() => {
        return appStore.enableVersion;
      })
    }
  }
};
</script>

<style lang="less" scoped></style>