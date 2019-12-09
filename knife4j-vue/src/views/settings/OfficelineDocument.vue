<template>
  <a-layout-content class="knife4j-body-content">
    <a-row class="markdown-row">
      <a-row class="globalparameters">
        <a-row class="gptips">
          Knife4j提供导出4种格式的离线文档(Html\Markdown\Word\Pdf)
        </a-row>
      </a-row>
      <a-row class="knife4j-download-button">
        <a-button @click="triggerDownloadMarkdown">
          <a-icon type="file-markdown" />下载Markdown</a-button>
        <a-button type="default" @click="triggerDownload">
          <a-icon type="file-text" />下载Html</a-button>
        <a-button type="default" @click="triggerDownloadWord">
          <a-icon type="file-word" />下载Word</a-button>
        <a-button type="default" @click="triggerDownloadPDF">
          <a-icon type="file-pdf" />下载PDF</a-button>
      </a-row>
      <!--  <a-modal v-model="downloadHtmlFlag" :footer="null" :maskClosable="false" :keyboard="false" :closable="false">
        <p>正在下载中...</p>
      </a-modal> -->
      <div class="htmledit_views" :id="'content_views' + data.instance.id">
        <component :is="downloadType" :instance="data.instance" :tags="tags" />
      </div>
    </a-row>
  </a-layout-content>
</template>
<script>
import VueMarkdown from "vue-markdown";
import html2canvas from "html2canvas";
import { resumecss } from "./OfficelineCss";
import getDocumentTemplates from "@/components/officeDocument/officeDocTemplate";
import markdownText from "@/components/officeDocument/markdownTransform";
import OnlineDocument from "@/views/api/OnlineDocument";
import { Modal } from "ant-design-vue";
import DownloadHtml from "./DownloadHtml";

const columns = [
  {
    title: "名称",
    dataIndex: "name",
    width: "30%"
  },
  {
    title: "类型",
    dataIndex: "type",
    width: "15%"
  },
  {
    title: "说明",
    dataIndex: "description"
  },
  {
    title: "schema",
    dataIndex: "schemaValue",
    width: "15%"
  }
];
export default {
  props: {
    data: {
      type: Object
    }
  },
  components: {
    VueMarkdown,
    OnlineDocument,
    DownloadHtml
  },
  data() {
    return {
      columns: columns,
      tags: [],
      downloadType: "DownloadHtml",
      markdownText: "",
      expanRows: true,
      downloadHtmlFlag: false,
      downloadPDF: false,
      modal: null,
      page: false
    };
  },
  updated() {
    console.log("dom重新被渲染");
    var that = this;
    if (that.downloadType == "DownloadHtml") {
      //html
      if (this.downloadHtmlFlag) {
        //等待ace-editor渲染,给与充足时间
        setTimeout(() => {
          //下载html
          that.downloadHtml();
          //关闭
          that.$kloading.destroy();
        }, 1000);
      }
    }
  },
  created() {},
  methods: {
    triggerDownloadPDF() {
      this.$message.info("该功能尚未实现...");
    },
    triggerDownloadWord() {
      this.$message.info("该功能尚未实现...");
    },
    triggerDownloadMarkdown() {
      //下载markdown
      var that = this;
      that.$kloading.show({
        text: "正在下载Markdown文件中,请稍后..."
      });
      //遍历得到markdown语法
      if (this.markdownText == null || this.markdownText == "") {
        //遍历得到markdown文本
        this.markdownText = markdownText(this.data.instance);
      }
      //等待ace-editor渲染,给与充足时间
      setTimeout(() => {
        //下载html
        that.downloadMarkdown(that.markdownText);
        //关闭
        that.$kloading.destroy();
      }, 1000);
    },
    triggerDownload() {
      let that = this;
      //html
      that.downloadType = "DownloadHtml";
      that.$kloading.show({
        text: "正在下载Html中,请稍后..."
      });

      if (!this.downloadHtmlFlag) {
        this.downloadHtmlFlag = true;
        //赋值Html重新渲染dom
        setTimeout(() => {
          that.tags = that.data.instance.tags;
          //that.$kloading.destroy();
        }, 300);
      } else {
        setTimeout(() => {
          //that.apis = that.data.instance.paths;
          that.$kloading.destroy();
          that.downloadHtml();
        }, 1000);
      }

      //
    },
    downloadMarkdown(content) {
      console.log("downloadMarkdown");
      var a = document.createElement("a");
      //var content = this.getHtmlContent(this.data.instance.title);
      var option = {};
      var fileName = this.data.instance.name + ".md";
      var url = window.URL.createObjectURL(
        new Blob([content], {
          type:
            (option.type || "text/plain") +
            ";charset=" +
            (option.encoding || "utf-8")
        })
      );
      a.href = url;
      a.download = fileName || "file";
      a.click();
      window.URL.revokeObjectURL(url);
    },
    downloadHtml() {
      console.log("downloadHtml");
      var a = document.createElement("a");
      var content = this.getHtmlContent(this.data.instance.title);
      var option = {};
      var fileName = this.data.instance.name + ".html";
      var url = window.URL.createObjectURL(
        new Blob([content], {
          type:
            (option.type || "text/plain") +
            ";charset=" +
            (option.encoding || "utf-8")
        })
      );
      a.href = url;
      a.download = fileName || "file";
      a.click();
      window.URL.revokeObjectURL(url);
      /* html2canvas(document.querySelector('#content_views')).then(canvas => {
        let saveUrl = canvas.toDataURL('image/png')

        let a = document.createElement('a')

        document.body.appendChild(a)

        a.href = saveUrl

        a.download = '这是图片标题'

        a.click()
      }) */
    },
    getHtmlContent(title) {
      //获取html另外一种方式：this.$el.outerHTML
      var domId = "content_views" + this.data.instance.id;
      if (title == undefined || title == null || title == "") {
        title = "Knife4j-API接口文档";
      }
      const template = document.getElementById(domId).innerHTML;
      //const template = document.getElementById("content_views").innerHTML;
      return getDocumentTemplates(title, resumecss, template);
    }
  }
};
</script>

<style lang="less" scoped>
.knife4j-download-button {
  margin: 40px auto;
  text-align: center;
  button {
    width: 150px;
    margin: 20px;
  }
}
.globalparameters {
  width: 73%;
  margin: 40px auto;
}
.gptips {
  color: #31708f;
  background-color: #d9edf7;
  border-color: #bce8f1;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid transparent;
  border-radius: 4px;
}
.download-loading {
  color: white;
  i {
    background-color: #e6f7ff;
  }
}
.spin-content {
  border: 1px solid #91d5ff;
  background-color: #e6f7ff;
  padding: 30px;
}
.htmledit_views {
  display: none;
}
.markdown-row {
  width: 95%;
  margin: 10px auto;
}

.content-line {
  height: 25px;
  line-height: 25px;
}
.content-line-count {
  height: 35px;
  line-height: 35px;
}
.title {
  text-align: center;
  width: 80%;
  margin: 5px auto;
}
.description {
  width: 90%;
  margin: 15px auto;
}
.divider {
  margin: 4px 0;
}
.divider-count {
  margin: 8px 0;
}
</style>
