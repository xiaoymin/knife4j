<template>
  <a-row class="knife4j-debug-response">
    <a-row v-if="debugSend">
      <a-tabs defaultActiveKey="debugResponse">
        <template slot="tabBarExtraContent">
          <a-row v-if="responseStatus" class="knife4j-debug-status">
            <span class="key">响应码:</span>
            <span class="value">{{responseStatus.code}}</span>
            <span class="key">耗时:</span>
            <span class="value">{{responseStatus.cost}}</span>
            <span class="key">大小:</span>
            <span class="value">{{responseStatus.size}} b</span>
          </a-row>
        </template>
        <a-tab-pane tab="响应内容" key="debugResponse">
          <a-row v-if="responseContent">
            <a-row v-if="responseContent.blobFlag">
              <a-button type="link" :href="responseContent.blobUrl" :download="responseContent.blobFileName">下载文件</a-button>
            </a-row>
            <a-row v-else>
              <editor-debug-show :value="responseContent.text" :mode="responseContent.mode"></editor-debug-show>
            </a-row>
          </a-row>
        </a-tab-pane>
        <a-tab-pane tab="Raw" key="debugRaw" forceRender>
          <a-row class="knife4j-debug-response-mt">
            <a-button :id="'btnDebugCopyRaw'+api.id" type="primary">
              <a-icon type="copy" /> 复制</a-button>
          </a-row>
          <a-row class="knife4j-debug-response-mt">
            <a-textarea :rows="10" :value="responseRawText" />
          </a-row>
        </a-tab-pane>
        <a-tab-pane tab="Headers" key="debugHeaders">
          <a-row class="knife4j-debug-response-mt">
            <a-table bordered size="small" :columns="responseHeaderColumn" :pagination="pagination" :dataSource="responseHeaders" rowKey="id">
            </a-table>
          </a-row>
        </a-tab-pane>
        <a-tab-pane tab="Curl" key="debugCurl">
          <a-row class="knife4j-debug-response-mt">
            <a-button :id="'btnDebugCopyCurl'+api.id" type="primary">
              <a-icon type="copy" /> 复制</a-button>
          </a-row>
          <a-row class="knife4j-debug-response-mt">
            <pre class="knife4j-debug-response-curl">{{responseCurlText}}</pre>
          </a-row>
        </a-tab-pane>
      </a-tabs>
    </a-row>
    <a-row v-else>

    </a-row>
  </a-row>
</template>
<script>
import KUtils from "@/core/utils";
import ClipboardJS from "clipboard";
import EditorDebugShow from "./EditorDebugShow";

export default {
  props: {
    api: {
      type: Object,
      required: true
    },
    debugSend: {
      type: Boolean,
      default: false
    },
    responseHeaders: {
      type: Array
    },
    responseRawText: {
      type: String
    },
    responseCurlText: {
      type: String,
      default: ""
    },
    responseStatus: {
      type: Object
    },
    responseContent: {
      type: Object
    }
  },
  components: { EditorDebugShow },
  data() {
    return {
      pagination: false,
      responseHeaderColumn: [
        {
          title: "响应头",
          dataIndex: "name",
          width: "20%"
        },
        {
          title: "值",
          dataIndex: "value"
        }
      ]
    };
  },
  created() {
    //this.resetResponseContent();
    this.copyRawText();
    this.copyCurlText();
  },
  methods: {
    copyRawText() {
      //复制raw的文本信息
      var that = this;
      var btnId = "btnDebugCopyRaw" + this.api.id;
      var clipboard = new ClipboardJS("#" + btnId, {
        text() {
          return that.responseRawText;
        }
      });
      clipboard.on("success", function(e) {
        that.$message.info("复制Raw成功");
      });
      clipboard.on("error", function(e) {
        that.$message.info("复制Raw失败");
      });
    },
    copyCurlText() {
      //复制curl
      var that = this;
      var btnId = "btnDebugCopyCurl" + this.api.id;
      var clipboard = new ClipboardJS("#" + btnId, {
        text() {
          return that.responseCurlText;
        }
      });
      clipboard.on("success", function(e) {
        that.$message.info("复制Curl成功");
      });
      clipboard.on("error", function(e) {
        that.$message.info("复制Curl失败");
      });
    },
    resetResponseContent() {
      if (this.responseContent != null) {
        if (this.responseContent.mode == "json") {
          //json格式特别处理
          const tmpJson = this.responseContent.text;
          console.log("特殊处理json");
          console.log(tmpJson);
          this.responseContent.text = KUtils.json5stringify(
            KUtils.json5parse(tmpJson)
          );
        }
      }
    }
  }
};
</script>