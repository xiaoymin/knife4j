<template>
  <a-layout-content class="knife4j-body-content">
    <a-row class="markdown-body editormd-preview-container">
      <Markdown :source="content" />
    </a-row>
  </a-layout-content>
</template>
<script>
import "@/assets/css/editormd.css";
import KUtils from "@/core/utils";
export default {
  props: {
    data: {
      type: Object
    }
  },
  components: {
    "Markdown": () => import('@/components/Markdown')
  },
  data() {
    return {
      content: ""
    };
  },
  created() {
    // 获取当前地址的id
    var that = this;
    var id = this.$route.params.id;
    var key = this.data.instance.id + 'markdownFiles';
    this.$localStore.getItem(key).then(mdfileMap => {
      // console.log(mdfileMap)
      if (KUtils.checkUndefined(mdfileMap)) {
        var content = mdfileMap[id];
        if (KUtils.strNotBlank(content)) {
          that.content = content;
        }
      }
    })
  }
};
</script>