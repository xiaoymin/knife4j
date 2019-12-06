<template>
  <div>
    <div v-if="debugResponse">
      <editor class="knife4j-debug-ace-editor" :value="value" @init="editorInit" @input="change" :lang="mode" theme="eclipse" width="100%" :height="editorHeight"></editor>
    </div>
    <div v-else>
      <editor :value="value" @init="editorInit" @input="change" :lang="mode" theme="eclipse" width="100%" :height="editorHeight"></editor>
    </div>

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
  data() {
    return {
      editor: null,
      editorHeight: 200
    };
  },
  methods: {
    resetEditorHeight() {
      var that = this;
      //重设高度
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
        that.editorHeight = rows_editor;
      }, 10);
    },
    change(value) {
      //重设高度
      this.resetEditorHeight();
      this.$emit("change", value);
    },
    editorInit(editor) {
      var that = this;
      this.editor = editor;
      require("brace/ext/language_tools"); //language extension prerequsite...
      require("brace/theme/eclipse");
      require("brace/mode/json");
      require("brace/mode/text");
      require("brace/mode/html");
      require("brace/mode/xml");
      require("brace/mode/javascript");
      /*  if (this.mode == "json") {
      } else if (this.mode == "html") {
      } else if (this.mode == "text") {
      } else if (this.mode == "xml") {
      } else if (this.mode == "javascript") {
      } */
      this.editor.gotoLine(1);
      //重设高度
      this.resetEditorHeight();
    }
  }
};
</script>