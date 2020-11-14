<template>
  <div class="globalFooter">
    <a-row v-if="settings.enableFooter">
<div  class="copyright">Apache License 2.0 | Copyright
      <a-icon type="copyright" /> 2019-<a target="_blank" href="https://gitee.com/xiaoym/knife4j">Knife4j</a></div>
    </a-row>
    <a-row v-else>
      <vue-markdown v-if="settings.enableFooterCustom" :source="settings.footerCustomContent"></vue-markdown>
    </a-row>
  </div>
</template>
<script>
import VueMarkdown from "vue-markdown";
export default {
  name: "GlobalFooter",
  props: {
    links: {
      type: Array,
      default: () => {
        [];
      }
    }
  },
  components: {
    VueMarkdown
  },
  computed:{
    settings(){
      return this.$store.state.globals.settings;
    }
  },
  data() {
    return {};
  }
};
</script>

<style lang="less" scoped>
@import "./index.less";
</style>

