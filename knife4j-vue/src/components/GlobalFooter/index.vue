<template>
  <div class="globalFooter">
    <div v-if="$slots.copyright" class="copyright">
      <slot name="copyright" />
    </div>
    <div v-else class="copyright">Apache License 2.0 | Copyright
      <a-icon type="copyright" /> 2019-<a target="_blank" href="https://gitee.com/xiaoym/knife4j">Knife4j</a></div>
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
  data() {
    return {};
  }
};
</script>

<style lang="less" scoped>
@import "./index.less";
</style>

