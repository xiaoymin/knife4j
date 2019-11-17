const globalParameters = {
  namespaced: true,
  state: {
    parameter: {}
  },
  mutations: {
    addParameter: (state, groupId, param) => {
      console.log("addparameter")
      if (!state.parameter.hasOwnProperty(groupId)) {
        state.parameter[groupId] = [];
      }
      state.parameter[groupId].push(param);
    },
    updateParameter: (state, groupId, param) => {
      const params = state.parameter[groupId];
      params.forEach(function (pm) {
        if (pm.name == param.name) {
          pm.value = param.value;
          pm.in = param.in;
        }
      })
      state.parameter[groupId] = params;
    },
    removeParameter: (state, groupId, param) => {
      var params = [];
      state.parameter[groupId].forEach(function (pm) {
        if (pm.name != param.name) {
          params.push(pm);
        }
      })
      state.parameter[groupId] = params;
    }
  },
  actions: {

  }
}

export default globalParameters;
