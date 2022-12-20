<template>
  <a-row class="knife4j-debug-response">
    <a-row v-if="debugSend">
      <a-tabs defaultActiveKey="debugResponse">
        <template slot="tabBarExtraContent">
          <a-row v-if="responseStatus" class="knife4j-debug-status">
            <span>
              <a-checkbox :defaultChecked="responseFieldDescriptionChecked" @change="showFieldDesChange"><span
                  style="color: #919191;" v-html="$t('debug.response.showDes')">显示说明</span></a-checkbox>
            </span>
            <span class="key" v-html="$t('debug.response.code')">响应码:</span>
            <span class="value">{{ responseStatus.code }}</span>
            <span class="key" v-html="$t('debug.response.cost')">耗时:</span>
            <span class="value">{{ responseStatus.cost }}</span>
            <span class="key" v-html="$t('debug.response.size')">大小:</span>
            <span class="value">{{ responseSizeText }} </span>
          </a-row>
        </template>
        <a-tab-pane :tab="i18n.debug.response.content" key="debugResponse">
          <a-row v-if="responseContent">
            <a-row v-if="responseContent.blobFlag">
              <div v-if="responseContent.imageFlag">
                <img :src="responseContent.blobUrl" />
              </div>
              <div v-else>
                <a-button type="link" :href="responseContent.blobUrl" :download="responseContent.blobFileName"
                  v-html="$t('debug.response.download')">下载文件</a-button>
              </div>
            </a-row>
            <a-row :id="'responseEditorContent' + api.id" v-else>
              <editor-debug-show @showDescription="showEditorFieldDescription" @debugEditorChange="debugEditorChange"
                :debugResponse="debugResponse" :value="responseContent.text" :mode="responseContent.mode">
              </editor-debug-show>
            </a-row>
          </a-row>
        </a-tab-pane>
        <a-tab-pane tab="Raw" key="debugRaw" forceRender>
          <a-row class="knife4j-debug-response-mt">
            <a-button :id="'btnDebugCopyRaw' + api.id" type="primary">
              <a-icon type="copy" /> <span v-html="$t('debug.response.copy')">复制</span>
            </a-button>
          </a-row>
          <a-row class="knife4j-debug-response-mt">
            <a-textarea :rows="10" :value="responseRawText" />
          </a-row>
        </a-tab-pane>
        <a-tab-pane tab="Headers" key="debugHeaders">
          <a-row class="knife4j-debug-response-mt">
            <a-table bordered size="small" :columns="responseHeaderColumn" :pagination="pagination"
              :dataSource="responseHeaders" rowKey="id">
            </a-table>
          </a-row>
        </a-tab-pane>
        <a-tab-pane tab="Curl" key="debugCurl">
          <a-row class="knife4j-debug-response-mt">
            <a-button :id="'btnDebugCopyCurl' + api.id" type="primary">
              <a-icon type="copy" /> <span v-html="$t('debug.response.copy')">复制</span>
            </a-button>
          </a-row>
          <a-row class="knife4j-debug-response-mt">
            <pre class="knife4j-debug-response-curl">{{
                responseCurlText
            }}</pre>
          </a-row>
        </a-tab-pane>
        <a-tab-pane v-if="responseContent != null && responseContent.base64 != null && responseContent.base64 != ''"
          tab="Base64Img" key="debugBase64Img">
          <a-row class="knife4j-debug-response-mt">
            <img :src="responseContent.base64" />
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
/* import EditorDebugShow from "./EditorDebugShow"; */

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
      type: String,
      default: ""
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
  components: { 'EditorDebugShow': () => import('./EditorDebugShow') },
  data() {
    return {
      pagination: false,
      i18n: null,
      base64Image: false,
      debugResponse: true,
      responseHeaderColumn: [

      ]
    };
  },
  watch: {
    language: function (val, oldval) {
      this.initI18n();
    }
  },
  computed: {
    language() {
      return this.$store.state.globals.language;
    },
    responseSizeText() {
      var str = "0 B";
      var responseStatus = this.responseStatus;
      if (responseStatus != null && responseStatus != undefined) {
        var responseSize = responseStatus.size;
        var kbSize = (responseSize / 1024).toFixed(2);
        var mbSize = (responseSize / 1024 / 1024).toFixed(2);
        if (kbSize > 1) {
          str = kbSize + " KB";
        } else if (mbSize > 1) {
          str = mbSize + " MB";
        } else {
          str = responseSize + " B";
        }
      }
      return str;
    }
  },
  created() {
    //  this.resetResponseContent();
    //  this.base64Init();
    this.initI18n();
    this.copyRawText();
    this.copyCurlText();
    //  this.showEditorFieldDescription();
  },
  methods: {
    getCurrentI18nInstance() {
      return this.$i18n.messages[this.language];
    },
    base64Init() {
      var bimg = KUtils.getValue(this.responseContent, "base64", "", true);
      // console.log(this.responseContent)
      if (KUtils.strNotBlank(bimg)) {
        this.base64Image = true;
      }
    },
    initI18n() {
      // 根据i18n初始化部分参数
      this.i18n = this.getCurrentI18nInstance();
      this.responseHeaderColumn = this.i18n.table.debugResponseHeaderColumns;
    },
    copyRawText() {
      // 复制raw的文本信息
      var that = this;
      var btnId = "btnDebugCopyRaw" + this.api.id;
      var clipboard = new ClipboardJS("#" + btnId, {
        text() {
          return that.responseRawText;
        }
      });
      // 复制Raw成功
      var successMessage = this.i18n.message.copy.raw.success;
      // 复制Raw失败
      var failMessage = this.i18n.message.copy.raw.fail;
      clipboard.on("success", function (e) {
        that.$message.info(successMessage);
      });
      clipboard.on("error", function (e) {
        that.$message.info(failMessage);
      });
    },
    copyCurlText() {
      // 复制curl
      var that = this;
      var btnId = "btnDebugCopyCurl" + this.api.id;
      var clipboard = new ClipboardJS("#" + btnId, {
        text() {
          return that.responseCurlText;
        }
      });
      // 复制Raw成功
      var successMessage = this.i18n.message.copy.curl.success;
      // 复制Raw失败
      var failMessage = this.i18n.message.copy.curl.fail;
      clipboard.on("success", function (e) {
        that.$message.info(successMessage);
      });
      clipboard.on("error", function (e) {
        that.$message.info(failMessage);
      });
    },
    resetResponseContent() {
      if (this.responseContent != null) {
        if (this.responseContent.mode == "json") {
          // json格式特别处理
          const tmpJson = this.responseContent.text;
          // console("特殊处理json");
          // console(tmpJson);
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
        fields.forEach(function (item) {
          if (flag) {
            // 显示
            item.style.display = "block";
          } else {
            // 隐藏
            item.style.display = "none";
          }
        });
      } else {
        this.showEditorFieldAnyWay();
      }
    },
    showEditorFieldDescription(p) {
      // console.log("emit事件-"+p)
      var that = this;
      if (KUtils.checkUndefined(p)) {
        if (parseInt(p) <= 200) {
          // 如果超过200行,不显示属性的字段说明
          // 需要延时1s处理
          setTimeout(() => {
            that.showEditorFieldWait();
          }, 100);
        }
      }

    },
    showEditorFieldWait() {
      // 显示editor字段说明
      if (this.debugSend) {
        if (this.responseFieldDescriptionChecked) {
          if (this.responseContent.mode == "json") {
            // console.log("数据大小："+this.responseStatus.size)
            this.showEditorFieldAnyWay();
          }
        }
      }
    },
    showEditorFieldAnyWay() {
      var swaggerInstance = this.swaggerInstance;
      // console.log("我被调用了--------");
      var responseCode = this.api.getHttpSuccessCodeObject();
      var cid = "responseEditorContent" + this.api.id;
      var editorContainer = document.getElementById(cid);
      // console(cid);
      var paths = [];
      // var aceJsonText = $aceJsonContent.find(".ace_text-layer");
      var aceJsonText = editorContainer.getElementsByClassName(
        "ace_text-layer"
      );
      var acePrintMarginLeft = 0;
      var acePrintMarginObject = editorContainer.querySelector(".ace_print-margin")
      if (KUtils.checkUndefined(acePrintMarginObject) && KUtils.checkUndefined(acePrintMarginObject.style)) {
        acePrintMarginLeft = acePrintMarginObject.style.left;
      }
      // editorContainer.querySelector(".ace_print-margin").style.left;
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
            // 判断是否存在
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
            // 如果元素大于0,拼装多个
            var parentArrs = [];
            for (var e = 0; e < itemParen.length; e++) {
              parentArrs.push(itemParen[e].innerHTML);
            }
            // var parentText = itemParen[0].innerHTML;
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
