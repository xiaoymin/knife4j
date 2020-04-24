const globals = {
  namespaced: true,
  state: {
    menuData: []
  },
  mutations: {
    setMenuData: (state, menudatas) => {
      state.menuData = menudatas;
    }
  },
  actions: {
    setMenuData({
      commit
    }, menudatas) {
      commit("setMenuData", menudatas);
    }
  }

}

export default globals;
