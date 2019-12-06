<template>
  <a-row class="knife4j-debug-response">
    <a-row v-if="debugSend">
      <a-tabs defaultActiveKey="debugResponse">
        <template slot="tabBarExtraContent">
          <a-row class="knife4j-debug-status">
            右上角状态情况
          </a-row>
        </template>
        <a-tab-pane tab="响应内容" key="debugResponse">Content of Tab Pane 1</a-tab-pane>
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
      发送消息查看接口信息
    </a-row>
  </a-row>
</template>
<script>
import ClipboardJS from "clipboard";

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
    }
  },
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
    }
  }
};
</script>