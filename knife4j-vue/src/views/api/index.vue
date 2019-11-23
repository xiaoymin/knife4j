<template>
  <a-layout-content class="knife4j-body-content">
    <a-tabs defaultActiveKey="1" tabPosition="left" class="api-tab">
      <a-tab-pane key="1">
        <span slot="tab">
          <a-icon type="pay-circle" />
          文档
        </span>
        <Document />
      </a-tab-pane>
      <a-tab-pane key="2">
        <span slot="tab">
          <a-icon type="bell" />
          调试
        </span>
        Content of Tab Pane 2</a-tab-pane>
    </a-tabs>
  </a-layout-content>

</template>
<script>
import Document from "./Document";
export default {
  name: "APIDoc",
  components: { Document },
  props: {
    data: {
      type: Object
    }
  },
  mounted() {
    console.log(":api----------");
  },
  data() {
    return {};
  },
  methods: {
    onTabChange(key, type) {
      console.log(key, type);
      this[type] = key;
    }
  }
};
</script>
<style lang="less" scoped>
</style>