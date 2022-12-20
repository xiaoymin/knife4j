<template>
  <div class="globalFooter">
    <a-row v-if="settings.enableFooterCustom">
      <Markdown v-if="settings.enableFooterCustom" :source="settings.footerCustomContent" />
    </a-row>
    <a-row v-else-if="settings.enableFooter">
      <div class="copyright">Apache License 2.0 | Copyright
        <a-icon type="copyright" /> 2019-<a target="_blank" href="https://gitee.com/xiaoym/knife4j">Knife4j</a>
      </div>
    </a-row>
  </div>
</template>
<script>
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
    "Markdown": () => import('@/components/Markdown')
  },
  computed: {
    settings() {
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

