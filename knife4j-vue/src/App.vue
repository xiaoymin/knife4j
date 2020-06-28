<template>
  <div id="app">
    <config-provider >
      <router-view />
    </config-provider>
  </div>
</template>

<script>
import { ConfigProvider } from "ant-design-vue";
import zhCN from "ant-design-vue/lib/locale-provider/zh_CN";

export default {
  name: "app",
  components: { ConfigProvider  },
  data() {
    return {
      locale: zhCN
    };
  }
};
</script>
<style lang="less">
@import url("style/knife4j.less");
</style>

<style lang="less">
body {
  overflow: hidden;
}
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
