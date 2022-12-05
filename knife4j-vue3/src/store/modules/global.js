import { defineStore } from 'pinia'

export const useGlobalsStore = defineStore('Globals',{
  state() {
    return {
      menuData: [],
      language: 'zh-CN',
      swagger: null,
      swaggerCurrentInstance: null,
      enableVersion: false,
      enableAfterScript: true,
      enableResponseCode: true,
      enableReloadCacheParameter: false,
      currentMenuData: [],
      serviceOptions: [],
      settings: {},
      defaultServiceOption: '',
      loading: {
        show: false,
        text: '加载中...'
      }
    }
  },
  actions: {
    setSettings(settings) {
      this.settings = settings;
    },
    setReloadCacheParameter(reloadCacheParameter) {
      this.enableReloadCacheParameter = reloadCacheParameter;
    },
    setAfterScript(afterScript) {
      this.enableAfterScript = afterScript;
    },
    setResponseCode(enableResponseCode) {
      this.enableResponseCode = enableResponseCode;
    },
    setGitVersion(gitVersion) {
      this.enableVersion = gitVersion;
    },
    setMenuData(menudatas) {
      this.menuData = this.menuData.concat(menudatas);
      this.currentMenuData = menudatas;
    },
    setCurrentMenuData(menudatas) {
      this.currentMenuData = menudatas;
    },
    setLang(lang) {
      this.language = lang;
    },
    setSwagger(swagger) {
      this.swagger = swagger;
    },
    setSwaggerInstance(instance) {
      this.swaggerCurrentInstance = instance;
    },
    setServiceOptions(services) {
      this.serviceOptions = services;
    },
    setDefaultService(defaultOption) {
      this.defaultServiceOption = defaultOption;
    },
    showLoading(options) {
      this.loading.show = true
      if (options) {
        this.loading.text = options.text
      }
    },
    destroyLoading() {
      this.loading.show = false
      this.loading.text = '加载中...'
    }
  }
})
