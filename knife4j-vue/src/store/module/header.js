const headers = {
  namespaced: true,
  state: {
    userCurrent: {}
  },
  mutations: {
    SetCurrentUser: (state) => {
      state.userCurrent = {
        name: '八一菜刀',
        avatar: ''
      }
    }
  },
  actions: {
    getCurrentUser({
      commit
    }) {
      commit("SetCurrentUser")
    }
  }

};

export default headers;
