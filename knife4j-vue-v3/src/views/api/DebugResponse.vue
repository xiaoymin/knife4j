<template>
  <a-row class="knife4j-debug-response">
    <a-row v-if="debugSend">
      <a-tabs defaultActiveKey="debugResponse">
        <template slot="tabBarExtraContent">
          <a-row v-if="responseStatus" class="knife4j-debug-status">
            <span>
              <a-checkbox :defaultChecked="responseFieldDescriptionChecked" @change="showFieldDesChange"><span style="color: #919191;">显示说明</span></a-checkbox>
            </span>
            <span class="key">响应码:</span>
            <span class="value">{{ responseStatus.code }}</span>
            <span class="key">耗时:</span>
            <span class="value">{{ responseStatus.cost }}</span>
            <span class="key">大小:</span>
            <span class="value">{{ responseSizeText }} </span>
          </a-row>
        </template>
        <a-tab-pane tab="响应内容" key="debugResponse">
          <a-row v-if="responseContent">
            <a-row v-if="responseContent.blobFlag">
              <div v-if="responseContent.imageFlag">
                <img :src="responseContent.blobUrl" />
              </div>
              <div v-else>
                <a-button type="link" :href="responseContent.blobUrl" :download="responseContent.blobFileName">下载文件</a-button>
              </div>
            </a-row>
            <a-row :id="'responseEditorContent' + api.id" v-else>
              <editor-debug-show @debugEditorChange="debugEditorChange" :debugResponse="debugResponse" :value="responseContent.text" :mode="responseContent.mode"></editor-debug-show>
            </a-row>
          </a-row>
        </a-tab-pane>
        <a-tab-pane tab="Raw" key="debugRaw" forceRender>
          <a-row class="knife4j-debug-response-mt">
            <a-button :id="'btnDebugCopyRaw' + api.id" type="primary">
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
            <a-button :id="'btnDebugCopyCurl' + api.id" type="primary">
              <a-icon type="copy" /> 复制</a-button>
          </a-row>
          <a-row class="knife4j-debug-response-mt">
            <pre class="knife4j-debug-response-curl">{{
              responseCurlText
            }}</pre>
          </a-row>
        </a-tab-pane>
      </a-tabs>
    </a-row>
    <a-row v-else> </a-row>
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
    swaggerInstance: {
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
    },
    responseFieldDescriptionChecked: {
      type: Boolean,
      default: true
    }
  },
  components: { EditorDebugShow },
  data() {
    return {
      pagination: false,
      debugResponse: true,
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
  computed: {
    responseSizeText() {
      var str = "0 b";
      var responseStatus = this.responseStatus;
      if (responseStatus != null && responseStatus != undefined) {
        var responseSize = responseStatus.size;
        var kbSize = (responseSize / 1024).toFixed(2);
        var mbSize = (responseSize / 1024 / 1024).toFixed(2);
        if (kbSize > 1) {
          str = kbSize + " kb";
        } else if (mbSize > 1) {
          str = mbSize + " mb";
        } else {
          str = responseSize + " b";
        }
      }
      return str;
    }
  },
  created() {
    //this.resetResponseContent();
    this.copyRawText();
    this.copyCurlText();
    this.showEditorFieldDescription();
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
          //console("特殊处理json");
          //console(tmpJson);
          this.responseContent.text = KUtils.json5stringify(
            KUtils.json5parse(tmpJson)
          );
        }
      }
    },
    showFieldDesChange(e) {
      var flag = e.target.checked;
      this.$emit("debugShowFieldDescriptionChange", flag);
      this.toggleFieldDescription(flag);
    },
    debugEditorChange(value) {
      this.$emit("debugEditorChange", value);
    },
    toggleFieldDescription(flag) {
      var cid = "responseEditorContent" + this.api.id;
      var editorContainer = document.getElementById(cid);
      var fields = editorContainer.getElementsByClassName(
        "knife4j-debug-editor-field-description"
      );
      if (KUtils.arrNotEmpty(fields)) {
        fields.forEach(function(item) {
          if (flag) {
            //显示
            item.style.display = "block";
          } else {
            //隐藏
            item.style.display = "none";
          }
        });
      } else {
        this.showEditorFieldAnyWay();
      }
    },
    showEditorFieldDescription() {
      var that = this;
      //需要延时1s处理
      setTimeout(() => {
        that.showEditorFieldWait();
      }, 1000);
    },
    showEditorFieldWait() {
      //显示editor字段说明
      if (this.debugSend) {
        if (this.responseFieldDescriptionChecked) {
          if (this.responseContent.mode == "json") {
            this.showEditorFieldAnyWay();
          }
        }
      }
    },
    showEditorFieldAnyWay() {
      var swaggerInstance = this.swaggerInstance;
      //console("我被调用了--------");
      var responseCode = this.api.getHttpSuccessCodeObject();
      var cid = "responseEditorContent" + this.api.id;
      var editorContainer = document.getElementById(cid);
      //console(cid);
      var paths = [];
      //var aceJsonText = $aceJsonContent.find(".ace_text-layer");
      var aceJsonText = editorContainer.getElementsByClassName(
        "ace_text-layer"
      );
      var acePrintMarginLeft = editorContainer.querySelector(
        ".ace_print-margin"
      ).style.left;
      if (aceJsonText.length > 0) {
        var aceLineDoms = aceJsonText[0].getElementsByClassName("ace_line");
        for (var i = 0; i < aceLineDoms.length; i++) {
          var item = aceLineDoms[i];
          var $variable = item.getElementsByClassName("ace_variable");
          var key = null;
          if (KUtils.arrNotEmpty($variable)) {
            key = KUtils.toString($variable[0].innerHTML, "").replace(
              /^"(.*)"$/g,
              "$1"
            );
            //判断是否存在
            var sfd = item.getElementsByClassName(
              "knife4j-debug-editor-field-description"
            );
            if (!KUtils.arrNotEmpty(sfd)) {
              var fieldSpan = document.createElement("span");
              fieldSpan.className = "knife4j-debug-editor-field-description";
              fieldSpan.innerHTML = responseCode.responseDescriptionFind(
                paths,
                key,
                swaggerInstance
              );
              fieldSpan.style.left = acePrintMarginLeft;
              item.appendChild(fieldSpan);
            }
          }
          var itemParen = item.getElementsByClassName("ace_paren");
          if (KUtils.arrNotEmpty(itemParen)) {
            //如果元素大于0,拼装多个
            var parentArrs = [];
            for (var e = 0; e < itemParen.length; e++) {
              parentArrs.push(itemParen[e].innerHTML);
            }
            //var parentText = itemParen[0].innerHTML;
            var parentText = parentArrs.join("");
            switch (parentText) {
              case "[":
              case "{":
                paths.push(key ? key : 0);
                break;
              case "}":
              case "]":
                paths.pop();
                break;
            }
          }
        }
      }
    }
  }
};
</script>
