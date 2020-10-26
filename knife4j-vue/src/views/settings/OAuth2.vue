<template>
  <a-row>
    我是OAuth2回调页面
  </a-row>
</template>
<script>
/* import Document from "./Document";
import Debug from "./Debug"; */
import Constants from "@/store/constants";
import KUtils from "@/core/utils";

export default {
  name: "OAuth2",
  components: { 
  },
  data() {
    return {
      api: null,
      swaggerInstance: null,
      debugSupport: false
    };
  },
  computed:{
    swagger(){
       return this.$store.state.globals.swagger;
    }
  },
  mounted() {},
  beforeCreate(){
  },
  created() {
     
  },
  methods: {
  }
};
</script>
<style lang="less" scoped></style>
