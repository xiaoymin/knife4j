<template>
  <div class="globalFooter">
    <a-row v-if="settings.enableFooterCustom">
      <markdown v-if="settings.enableFooterCustom" :source="settings.footerCustomContent" />
    </a-row>
    <div style="text-align: center" v-else-if="settings.enableFooter">
      <div class="copyright">Apache License 2.0 | Copyright
        <copyright-outlined /> 2019-<a target="_blank" href="https://gitee.com/xiaoym/knife4j">Knife4j</a>
      </div>
    </div>
  </div>
</template>
<script>
import { computed } from 'vue'
import { useGlobalsStore } from '@/store/modules/global.js'
import Markdown from '../Markdown/index.vue'
import { CopyrightOutlined } from '@ant-design/icons-vue'

export default {
  name: "GlobalFooter",
  components: { Markdown, CopyrightOutlined },
  props: {
    links: {
      type: Array,
      default: () => {
        return [];
      }
    }
  },
  setup() {
    const globalsStore = useGlobalsStore()
    return {
      settings: computed(() => {
        return globalsStore.settings;
      })
    }
  }
};
</script>

<style lang="less" scoped>
@import "./index.less";
</style>

