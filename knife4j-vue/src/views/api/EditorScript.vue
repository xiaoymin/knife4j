<template>
  <div>
    <editor :value="value" @init="editorInit" :lang="lang" @input="change" theme="eclipse" width="100%"
      :height="editorHeight"></editor>
  </div>
</template>

<script>
export default {
  name: "EditorShow",
  components: { editor: require("vue2-ace-editor") },
  props: {
    value: {
      type: String,
      required: true,
      default: ""
    },
    tsMode: {
      type: Boolean,
      required: false,
      default: false,
    }
  },
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
      require("brace/ext/language_tools"); // language extension prerequsite...
      require('brace/mode/javascript');
      require('brace/mode/typescript');
      require("brace/theme/eclipse");
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
