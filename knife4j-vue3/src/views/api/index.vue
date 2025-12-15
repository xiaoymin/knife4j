<template>
  <a-layout-content class="knife4j-body-content">
    <a-row v-if="debugSupport" style="display: block">
      <a-tabs defaultActiveKey="doc" tabPosition="left" class="api-tab">
        <a-tab-pane key="doc">
          <template #tab>
            <my-icon type="icon-wendang" /><span>{{ $t('doc.title') }}</span>
          </template>
          <Document :api="api" :swaggerInstance="swaggerInstance" />
        </a-tab-pane>
        <a-tab-pane key="debug">
          <template #tab>
            <my-icon type="icon-debug" /><span>{{ $t('debug.title') }}</span>
          </template>
          <Debug :api="api" :swaggerInstance="swaggerInstance" />
        </a-tab-pane>
        <a-tab-pane v-if="settings.enableOpenApi" key="openapi">
          <template #tab>
            <file-text-outlined/><span>Open</span>
          </template>
          <OpenApi :api="api" :swaggerInstance="swaggerInstance" />
        </a-tab-pane>
        <a-tab-pane v-if="settings.enableOpenApi" key="script" tab="Script">-->
          <ScriptView :api="api" :swaggerInstance="swaggerInstance" />-->
        </a-tab-pane>-->

      </a-tabs>
    </a-row>
    <a-row class="knife4j-api-readonly" v-else style="display: block">
      <Document :api="api" :swaggerInstance="swaggerInstance" />
    </a-row>
  </a-layout-content>
</template>
<script>
/* import Document from "./Document";
import Debug from "./Debug"; */
import Constants from "@/store/constants";
import KUtils from "@/core/utils";
import { useGlobalsStore } from '@/store/modules/global.js'
import { computed, defineAsyncComponent } from 'vue'
import localStore from '@/store/local.js'
import { FileTextOutlined } from '@ant-design/icons-vue'

export default {
  name: "APIDoc",
  components: {
    "Document": defineAsyncComponent(() => import("./Document.vue")),
    "Debug": defineAsyncComponent(() => import("./Debug.vue")),
    "OpenApi": defineAsyncComponent(() => import("./OpenApi.vue")),
    "ScriptView": defineAsyncComponent(() => import("./ScriptView.vue")),
    FileTextOutlined,
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
  setup() {
    const globalsStore = useGlobalsStore()
    const swagger = computed(() => {
      return globalsStore.swagger
    })
    const settings = computed(() => {
      return globalsStore.settings
    })
    return {
      swagger,
      settings
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
        localStore
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
                      s.updateApis = {};
                      s.updateApis[apiInfo.id].url = apiInfo.url;
                      s.updateApis[apiInfo.id].versionId = apiInfo.versionId;
                      s.updateApis[apiInfo.id].lastTime = new Date();
                    }
                  }
                }
              });
            }
            localStore.setItem(
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
