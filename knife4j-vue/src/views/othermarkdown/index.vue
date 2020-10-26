<template>
  <a-layout-content class="knife4j-body-content">
    <a-row class="markdown-body editormd-preview-container">
      <vue-markdown :source="markdown.content"></vue-markdown>
    </a-row>
  </a-layout-content>
</template>
<script>
import "@/assets/css/editormd.css";
import KUtils from "@/core/utils";
import VueMarkdown from "vue-markdown";
export default {
  props: {
    data: {
      type: Object
    }
  },
  components: {
    VueMarkdown
  },
  data() {
    return {
      markdown: {}
    };
  },
  created() {
    //获取当前地址的id
    var that = this;
    var id = this.$route.params.id;
    var key= this.data.instance.id+'markdownFiles';
    this.$localStore.getItem(key).then(mdfileMap=>{
      if(KUtils.checkUndefined(mdfileMap)){
        var content=mdfileMap[id];
        if(KUtils.strNotBlank(content)){
          that.markdown={
            content:content
          };
        }
      }
    })
  }
};
</script>