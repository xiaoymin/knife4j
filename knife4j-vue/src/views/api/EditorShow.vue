<template>
  <div>
    <editor :value="value" @init="editorInit" :lang="lang" theme="eclipse" width="100%" :height="editorHeight"
      @input="change"></editor>
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
    xmlMode: {
      type: Boolean,
      default: false,
      required: false
    }
  },
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
      var that = this;
      // 重设高度
      setTimeout(() => {
        var length_editor = that.editor.session.getLength();
        if (length_editor == 1) {
          length_editor = 10;
        }
        var rows_editor = length_editor * 16;
        that.editorHeight = rows_editor;
      }, 300);
    },
    editorInit(editor) {
      var that = this;
      this.editor = editor;
      require("brace/ext/language_tools"); //language extension prerequsite...
      require("brace/mode/json");
      require("brace/mode/xml");
      if (this.xmlMode) {
        this.lang = "xml";
      }
      require("brace/theme/eclipse");
      // 重设高度
      this.resetEditorHeight();
      this.editor.renderer.on("afterRender", function () {
        that.$emit("showDescription", "123")
      });
    }
  }
};
</script>
