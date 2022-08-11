const globals = {
  namespaced: true,
  state: {
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
    defaultServiceOption: ''
  },
  mutations: {
    setMenuData: (state, menudatas) => {
      let newMenuArrs = state.menuData.concat(menudatas);
      state.menuData = newMenuArrs;
      state.currentMenuData = menudatas;
    }, setCurrentMenuData: (state, menudatas) => {
      state.currentMenuData = menudatas;
    }, setLang: (state, lang) => {
      state.language = lang;
    },
    setSwagger: (state, swagger) => {
      state.swagger = swagger;
    },
    setSwaggerInstance: (state, instance) => {
      state.swaggerCurrentInstance = instance;
    },
    setServiceOptions: (state, services) => {
      state.serviceOptions = services;
    },
    setDefaultService: (state, defaultOption) => {
      state.defaultServiceOption = defaultOption;
    },
    setGitVersion: (state, gitVersion) => {
      state.enableVersion = gitVersion;
    },
    setAfterScript: (state, afterScript) => {
      state.enableAfterScript = afterScript;
    },
    setResponseCode: (state, enableResponseCode) => {
      state.enableResponseCode = enableResponseCode;
    },
    setReloadCacheParameter: (state, reloadCacheParameter) => {
      state.enableReloadCacheParameter = reloadCacheParameter;
    },
    setSettings: (state, settings) => {
      state.settings = settings;
    }
  },
  actions: {
    setSettings({ commit }, settings) {
      commit('setSettings', settings);
    },
    setReloadCacheParameter({ commit }, reloadCacheParameter) {
      commit('setReloadCacheParameter', reloadCacheParameter);
    },
    setAfterScript({ commit }, afterScript) {
      commit('setAfterScript', afterScript);
    },
    setResponseCode({ commit }, enableResponseCode) {
      commit('setResponseCode', enableResponseCode);
    },
    setGitVersion({ commit }, gitVersion) {
      commit('setGitVersion', gitVersion);
    },
    setMenuData({
      commit
    }, menudatas) {
      commit('setMenuData', menudatas);
    },
    setCurrentMenuData({ commit }, menudatas) {
      commit('setCurrentMenuData', menudatas);
    },
    setLang({ commit }, lang) {
      commit('setLang', lang);
    },
    setSwagger({ commit }, swagger) {
      commit('setSwagger', swagger);
    },
    setSwaggerInstance({ commit }, instance) {
      commit('setSwaggerInstance', instance);
    },
    setServiceOptions({ commit }, services) {
      commit('setServiceOptions', services);
    },
    setDefaultService({ commit }, defaultOption) {
      commit('setDefaultService', defaultOption);
    }
  }

}

export default globals;
