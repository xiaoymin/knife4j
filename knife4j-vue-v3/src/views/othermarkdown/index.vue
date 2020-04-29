<template>
  <a-layout-content class="knife4j-body-content">
    <a-row class="markdown-body editormd-preview-container">
      <vue-markdown :source="markdown.content"></vue-markdown>
    </a-row>
  </a-layout-content>
</template>
<script>
import "@/assets/css/editormd.css";
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
    this.data.instance.markdownFiles.forEach(function(md) {
      if (md.id == id) {
        that.markdown = md;
      }
    });
  }
};
</script>