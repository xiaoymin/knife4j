<template>
  <a-layout-content class="knife4j-body-content">
    <a-row v-if="debugSupport">
      <a-tabs defaultActiveKey="doc" tabPosition="left" class="api-tab">
        <a-tab-pane key="doc">
          <span slot="tab">
            <my-icon type="icon-wendang" /><span v-html="$t('doc.title')">调试</span>
          </span>
          <Document :api="api" :swaggerInstance="swaggerInstance" />
        </a-tab-pane>
        <a-tab-pane key="debug">
          <span slot="tab">
            <my-icon type="icon-debug" /><span v-html="$t('debug.title')">调试</span>
          </span>
          <Debug :api="api" :swaggerInstance="swaggerInstance" />
        </a-tab-pane>
        <a-tab-pane v-if="settings.enableOpenApi" key="openapi">
          <span slot="tab">
            <a-icon type="file-text" /><span>Open</span>
          </span>
          <OpenApi :api="api" :swaggerInstance="swaggerInstance" />
        </a-tab-pane>
        <a-tab-pane v-if="settings.enableOpenApi" key="script">
          <span slot="tab">
            <a-icon type="codepen" /><span>Script</span>
          </span>
          <ScriptView :api="api" :swaggerInstance="swaggerInstance" />
        </a-tab-pane>

      </a-tabs>
    </a-row>
    <a-row class="knife4j-api-readonly" v-else>
      <Document :api="api" :swaggerInstance="swaggerInstance" />
    </a-row>
  </a-layout-content>
</template>
<script>
/* import Document from "./Document";
import Debug from "./Debug"; */
import Constants from "@/store/constants";
import KUtils from "@/core/utils";

export default {
  name: "APIDoc",
  components: {
    "Document": () => import("./Document"),
    "Debug": () => import("./Debug"),
    "OpenApi": () => import("./OpenApi"),
    "ScriptView": () => import("./ScriptView.vue"),
  },
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
  computed: {
    swagger() {
      return this.$store.state.globals.swagger;
    },
    settings() {
      return this.$store.state.globals.settings;
    }
  },
  mounted() { },
  beforeCreate() {
  },
  created() {
    //  根据地址栏得到api详情
    let params = this.$route.params;
    // console(params);
    // console(this.data);
    // 根据当前的分组id、接口id找出apiInfo信息
    let instance = this.data.instance;
    let apiInfo = null;
    instance.paths.forEach(function (path) {
      if (path.operationId == params.summary) {
        apiInfo = path;
      }
    });
    if (!apiInfo.init) {
      this.swagger.initApiInfoAsync(apiInfo);
    }
    // console.log(apiInfo)
    this.storeCacheApiAddApiInfo(apiInfo, instance.groupId);
    this.swaggerInstance = instance;
    this.api = apiInfo;
    // this.debugSupport = this.api.configurationDebugSupport;
    this.debugSupport = this.settings.enableDebug;

  },
  methods: {
    onTabChange(key, type) {
      // console(key, type);
      this[type] = key;
    },
    /**
     * 将接口id加入缓存，再页面点击后
     */
    storeCacheApiAddApiInfo(apiInfo, groupId) {
      // 只有在当前接口是new的情况下才加入缓存
      if (apiInfo.hasNew || apiInfo.hasChanged) {
        this.$localStore
          .getItem(Constants.globalGitApiVersionCaches)
          .then(gitVal => {
            if (KUtils.strNotBlank(gitVal)) {
              gitVal.forEach(s => {
                if (s.id == groupId) {
                  // 判断是新增还是修改
                  if (apiInfo.hasNew) {
                    s.cacheApis.push(apiInfo.id);
                  } else if (apiInfo.hasChanged) {
                    var _upt = s.updateApis;
                    if (_upt != undefined && _upt != null) {
                      // 判断是否有值
                      if (_upt.hasOwnProperty(apiInfo.id)) {
                        s.updateApis[apiInfo.id].versionId = apiInfo.versionId;
                        s.updateApis[apiInfo.id].lastTime = new Date();
                      }
                    } else {
                      s.updateApis = new Object();
                      s.updateApis[apiInfo.id].url = apiInfo.url;
                      s.updateApis[apiInfo.id].versionId = apiInfo.versionId;
                      s.updateApis[apiInfo.id].lastTime = new Date();
                    }
                  }
                }
              });
            }
            this.$localStore.setItem(
              Constants.globalGitApiVersionCaches,
              gitVal
            );
          });
      }
    }
  }
};
</script>
<style lang="less" scoped>
</style>
