<template>
  <a-layout-content class="knife4j-body-content">
    <a-row v-if="debugSupport">
      <a-tabs defaultActiveKey="2" tabPosition="left" class="api-tab">
        <a-tab-pane key="1">
          <span slot="tab">
            <my-icon type="icon-wendang" />文档</span>
          <Document :api="api" />
        </a-tab-pane>
        <a-tab-pane key="2">
          <span slot="tab">
            <my-icon type="icon-debug" />调试</span>
          <Debug :api="api" :swaggerInstance="swaggerInstance" />
        </a-tab-pane>
      </a-tabs>
    </a-row>
    <a-row class="knife4j-api-readonly" v-else>
      <Document :api="api" />
    </a-row>
  </a-layout-content>
</template>
<script>
import Document from "./Document";
import Debug from "./Debug";
export default {
  name: "APIDoc",
  components: { Document, Debug },
  props: {
    data: {
      type: Object
    }
  },
  data() {
    return {
      api: null,
      swaggerInstance: null,
      debugSupport: false
    };
  },
  mounted() {},
  created() {
    //根据地址栏得到api详情
    let params = this.$route.params;
    console.log(params);
    console.log(this.data);
    //根据当前的分组id、接口id找出apiInfo信息
    let instance = this.data.instance;
    let apiInfo = null;
    instance.paths.forEach(function(path) {
      if (path.operationId == params.summary) {
        apiInfo = path;
      }
    });
    this.swaggerInstance = instance;
    this.api = apiInfo;
    this.debugSupport = this.api.configurationDebugSupport;
  },
  methods: {
    onTabChange(key, type) {
      console.log(key, type);
      this[type] = key;
    }
  }
};
</script>
<style lang="less" scoped></style>
