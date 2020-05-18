const globals = {
  namespaced: true,
  state: {
    menuData: [],
    language:'zh-CN',
    currentMenuData:[]
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
    },setLang({commit},lang){
      commit('setLang',lang)
    }
  }

}

export default globals;
