<template>
  <div class="document">
    <a-row style="margin-top:10px;">
      <a-button type="primary" :id="'btnCopyOpenApi' + api.id">
        <a-icon type="copy" /><span v-html="$t('open.copy')"> 复 制 </span>
      </a-button>

      <a-button style="margin-left:10px;" @click="triggerDownloadOpen">
        <a-icon type="download" /> <span v-html="$t('open.download')"> 下 载 </span>
      </a-button>
    </a-row>
    <a-row style="margin-top:10px;" :id="'knife4jDocumentOpenApiShowEditor'">
      <editor-show @change="change" :value="openApiRaw"></editor-show>
    </a-row>
  </div>
</template>
<script>
import KUtils from "@/core/utils";
import ClipboardJS from "clipboard";
export default {
  name: "Document",
  components: {
    editor: require("vue2-ace-editor"),
    "EditorShow": () => import('./EditorShow')
  },
  props: {
    api: {
      type: Object,
      required: true
    },
    swaggerInstance: {
      type: Object,
      required: true
    }
  },
  computed: {
    language() {
      return this.$store.state.globals.language;
    }
  },
  data() {
    return {
      openApiRaw: "",
      name: "OpenAPI.json"
    }
  },
  created() {
    this.openApiRaw = KUtils.json5stringify(this.api.openApiRaw);
    this.name = this.api.summary + "_OpenAPI.json";
    // console.log(this.api);
    setTimeout(() => {
      this.copyOpenApi();
    }, 500);
  },
  methods: {
    change(value) {
      this.openApiRaw = value;
    },
    getCurrentI18nInstance() {
      return this.$i18n.messages[this.language];
    },
    triggerDownloadOpen() {
      var content = this.openApiRaw;
      var a = document.createElement("a");
      // var content = this.getHtmlContent(this.data.instance.title);
      var option = {};
      var fileName = this.name;
      var url = window.URL.createObjectURL(
        new Blob([content], {
          type:
            (option.type || "text/plain") +
            ";charset=" +
            (option.encoding || "utf-8")
        })
      );
      a.href = url;
      a.download = fileName || "file";
      a.click();
      window.URL.revokeObjectURL(url);
    },
    copyOpenApi() {
      var that = this;
      var btnId = "btnCopyOpenApi" + this.api.id;
      var clipboard = new ClipboardJS("#" + btnId, {
        text() {
          return that.openApiRaw;
        }
      });
      clipboard.on("success", () => {
        var inst = that.getCurrentI18nInstance();
        // "复制地址成功"
        var successMessage = inst.message.copy.open.success;
        that.$message.info(successMessage);
      })
      clipboard.on("error", function (e) {
        var inst = that.getCurrentI18nInstance();
        console.log(inst)
        // "复制地址失败"
        var failMessage = inst.message.copy.open.fail;
        that.$message.info(failMessage);
      });
    }
  }
}
</script>