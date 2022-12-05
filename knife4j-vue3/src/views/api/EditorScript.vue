<template>
  <div>
    <editor :value="value" @init="editorInit" :lang="lang" @input="change" theme="eclipse" width="100%"
      :style="{height: editorHeight + 'px'}"></editor>
  </div>
</template>

<script>
import { VAceEditor } from 'vue3-ace-editor'
import ace from "ace-builds";
import "ace-builds/src-noconflict/mode-typescript.js";
import "ace-builds/src-noconflict/mode-javascript.js";
import "ace-builds/src-noconflict/theme-eclipse.js";
import "ace-builds/src-noconflict/ext-language_tools";
export default {
  name: "EditorShow",
  components: { editor: VAceEditor },
  props: {
    value: {
      type: [String, Object],
      required: true,
      default: ""
    },
    tsMode: {
      type: Boolean,
      required: false,
      default: false,
    }
  },
  emits: ['showDescription'],
  data() {
    return {
      lang: "javascript",
      editor: null,
      editorHeight: 200
    };
  },
  methods: {
    resetEditorHeight() {
      var that = this;
      //  重设高度
      setTimeout(() => {
        var length_editor = that.editor.session.getLength();
        if (length_editor == 1) {
          length_editor = 10;
        }
        var rows_editor = length_editor * 16;
        that.editorHeight = rows_editor;
      }, 300);
    },
    change(value) {
      // this.value = value;
      // 重设高度
      // this.resetEditorHeight();
      this.$emit("change", value);
    },
    editorInit(editor) {
      var that = this;
      this.editor = editor;
      // require("brace/ext/language_tools"); // language extension prerequsite...
      // require('brace/mode/javascript');
      // require('brace/mode/typescript');
      // require("brace/theme/eclipse");
      if (this.tsMode) {
        this.lang = "typescript";
      }
      // 重设高度
      this.resetEditorHeight();
      this.editor.renderer.on("afterRender", function () {
        that.$emit("showDescription", "123")
      });
    }
  }
};
</script>
