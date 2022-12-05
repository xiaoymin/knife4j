<template>
  <div>
    <div v-if="debugResponse">
      <editor class="knife4j-debug-ace-editor" @input="change" :options="debugOptions" v-model:value="valueText" @init="editorInit"
        :lang="mode" theme="eclipse" width="100%" :style="{height: editorHeight + 'px'}"></editor>
    </div>
    <div v-else>
      <editor v-model:value="valueText" @init="editorInit" @input="change" :lang="mode" theme="eclipse" width="100%"
        :style="{height: editorHeight + 'px'}"></editor>
    </div>

  </div>
</template>

<script>
import { VAceEditor } from 'vue3-ace-editor'
import ace from "ace-builds";
import "ace-builds/src-noconflict/mode-json.js";
import "ace-builds/src-noconflict/mode-xml.js";
import "ace-builds/src-noconflict/mode-text.js";
import "ace-builds/src-noconflict/mode-javascript.js";
import "ace-builds/src-noconflict/theme-eclipse.js";
import "ace-builds/src-noconflict/ext-language_tools";
import { ref, watch } from 'vue'
export default {
  name: "EditorShow",
  components: { editor: VAceEditor },
  props: {
    value: {
      type: String,
      required: true,
      default: ""
    },
    mode: {
      type: String,
      required: true,
      default: "json"
    },
    debugResponse: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:value', 'debugEditorChange', 'showDescription'],
  setup(props) {
    const valueText = ref(props.value)
    watch(() => props.value, () => {
      valueText.value = props.value
    })
    return {
      valueText
    }
  },
  data() {
    return {
      editor: null,
      editorHeight: 200,
      debugOptions: {
        readOnly: false,
        autoScrollEditorIntoView: true,
        displayIndentGuides: false,
        fixedWidthGutter: true
      },
      commonOptions: {
        readOnly: false
      }
    };
  },
  methods: {
    resetEditorHeight() {
      var that = this;
      //  重设高度
      setTimeout(() => {
        var length_editor = that.editor.session.getLength();
        if (length_editor == 1) {
          length_editor = 15;
        }
        if (length_editor < 15) {
          if (that.debugResponse) {
            length_editor = 30;
          } else {
            length_editor = 15;
          }
        }
        if (length_editor > 20) {
          if (!that.debugResponse) {
            length_editor = 20;
          }
        }
        var rows_editor = length_editor * 16;
        if (rows_editor > 2000) {
          rows_editor = 2000;
        }
        // console.log(rows_editor)
        that.editorHeight = rows_editor;
      }, 10);
    },
    change() {
      // this.value = value;
      // 重设高度
      this.$emit("update:value", this.valueText);
      if (!this.debugResponse) {
        this.resetEditorHeight();
      }
    },
    editorInit(editor) {
      var that = this;
      // console("aaa");
      this.editor = editor;
      // require("brace/ext/language_tools"); // language extension prerequsite...
      // require("brace/theme/eclipse");
      // require("brace/mode/json");
      // require("brace/mode/text");
      // require("brace/mode/html");
      // require("brace/mode/xml");
      // require("brace/mode/javascript");
      /*  if (this.mode == "json") {
      } else if (this.mode == "html") {
      } else if (this.mode == "text") {
      } else if (this.mode == "xml") {
      } else if (this.mode == "javascript") {
      } */
      // this.editor.gotoLine(1);
      if (this.debugResponse) {
        // 启动换行
        this.editor.getSession().setUseWrapMode(true);
        this.editor.setOptions(this.debugOptions);
        if (this.mode == "text") {
          this.editor.getSession().setUseWrapMode(true);
        }
      } else {
        this.editor.setOptions(this.commonOptions);
      }
      // 重设高度
      this.resetEditorHeight();
      this.editor.renderer.on("afterRender", function () {
        var length_editor = that.editor.session.getLength();
        that.$emit("showDescription", length_editor)
      });
    }
  }
};
</script>