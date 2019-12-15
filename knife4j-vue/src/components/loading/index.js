import Loading from "./loading.vue"
import Vue from "vue"

const LoadingConstrc = Vue.extend(Loading);

let instance = new LoadingConstrc({
  el: document.createElement("div")
});

instance.show = false;
const loadOptions = {
  show(options = {}) {
    instance.show = true;
    if (options) {
      //console(instance)
      //console(options)
      document.body.append(instance.$el);
      //options.el.appendChild(instance.$el);
      instance.text = options.text;
    }
  },
  destroy() {
    instance.show = false;
  }
}

export default {
  install() {
    if (!Vue.$kloading) {
      Vue.$kloading = loadOptions;
    }
    Vue.mixin({
      created() {
        this.$kloading = Vue.$kloading;
      }
    })
  }
}
