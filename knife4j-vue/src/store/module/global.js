const globals = {
  namespaced: true,
  state: {
    menuData: [],
    language:'zh-CN',
    swaggerCurrentInstance:null,
    currentMenuData:[],
    serviceOptions:[],
    defaultServiceOption: ''
  },
  mutations: {
    setMenuData: (state, menudatas) => {
      let newMenuArrs=state.menuData.concat(menudatas);
      state.menuData =  newMenuArrs;
      state.currentMenuData=menudatas;
    },setCurrentMenuData:(state,menudatas)=>{
      state.currentMenuData=menudatas;
    },setLang:(state,lang)=>{
      state.language=lang;
    },
    setSwaggerInstance:(state,instance)=>{
      state.swaggerCurrentInstance=instance;
    },
    setServiceOptions:(state,services)=>{
      state.serviceOptions=services;
    },
    setDefaultService:(state,defaultOption)=>{
      state.defaultServiceOption=defaultOption;
    }
  },
  actions: {
    setMenuData({
      commit
    }, menudatas) {
      commit('setMenuData', menudatas);
    },
    setCurrentMenuData({commit},menudatas){
      commit('setCurrentMenuData',menudatas);
    },
    setLang({commit},lang){
      commit('setLang',lang);
    },
    setSwaggerInstance({commit},instance){
      commit('setSwaggerInstance',instance);
    },
    setServiceOptions({commit},services){
      commit('setServiceOptions',services);
    },
    setDefaultService({commit},defaultOption){
      commit('setDefaultService',defaultOption);
    }
  }

}

export default globals;
