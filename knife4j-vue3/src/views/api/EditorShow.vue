<template>
  <div>
    <editor :value="value" @init="editorInit" :lang="lang" theme="eclipse" width="100%" :style="{height: editorHeight + 'px'}"
      @input="change"></editor>
  </div>
</template>

<script>
import { VAceEditor } from 'vue3-ace-editor'
import ace from "ace-builds";
import modeJson from "ace-builds/src-noconflict/mode-json?url";
import modeXml from "ace-builds/src-noconflict/mode-xml?url";
import themeEclipse from "ace-builds/src-noconflict/theme-eclipse?url";
import extLanguageTools from "ace-builds/src-noconflict/ext-language_tools?url";

ace.config.setModuleUrl('ace/mode/json', modeJson)
ace.config.setModuleUrl('ace/mode/xml', modeXml)
ace.config.setModuleUrl('ace/theme/eclipse', themeEclipse)
ace.config.setModuleUrl('ace/ext-language/tools', extLanguageTools)

export default {
  name: "EditorShow",
  components: { editor: VAceEditor },
  props: {
    value: {
      type: [String, Object],
      required: true,
      default: ""
    },
    xmlMode: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  emits: ['showDescription', 'change'],
  data() {
    return {
      lang: "json",
      editor: null,
      editorHeight: 200
    };
  },
  methods: {
    change(value) {
      this.$emit("change", value);
    },
    resetEditorHeight() {
      const that = this
      // 重设高度
      setTimeout(() => {
        let length_editor = that.editor.session.getLength()
        if (length_editor == 1) {
          length_editor = 10;
        }
        that.editorHeight = length_editor * 16;
      }, 300);
    },
    editorInit(editor) {
      const that = this
      this.editor = editor;
      // require("brace/ext/language_tools"); //language extension prerequsite...
      // require("brace/mode/json");
      // require("brace/mode/xml");
      if (this.xmlMode) {
        this.lang = "xml";
      }
      // require("brace/theme/eclipse");
      // 重设高度
      this.resetEditorHeight();
      this.editor.renderer.on("afterRender", function () {
        that.$emit("showDescription", "123")
      });
    }
  }
};
</script>
