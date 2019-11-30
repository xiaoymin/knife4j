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
        <a-button type="default" @click="downloadHtml">
          <a-icon type="file-pdf" />下载PDF</a-button>
      </a-row>
      <!--  <a-modal v-model="downloadHtmlFlag" :footer="null" :maskClosable="false" :keyboard="false" :closable="false">
        <p>正在下载中...</p>
      </a-modal> -->
      <div class="htmledit_views" id="content_views">
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
    if (this.downloadHtmlFlag) {
      //等待ace-editor渲染,给与充足时间
      setTimeout(() => {
        //下载html
        that.downloadHtml();
        //关闭
        that.$kloading.destroy();
      }, 1000);
    }
  },
  created() {},
  methods: {
    triggerDownloadWord() {},
    triggerDownloadMarkdown() {},
    triggerDownload() {
      console.log("trigger---");
      let that = this;
      //html
      that.downloadType = "DownloadHtml";
      that.$kloading.show({
        text: "正在下载中..."
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
    downloadHtml() {
      console.log("downloadHtml");
      var a = document.createElement("a");
      var content = this.getHtmlContent();
      var option = {};
      var fileName = "test.html";
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
    getHtmlContent() {
      //获取html另外一种方式：this.$el.outerHTML
      const template = document.getElementById("content_views").innerHTML;
      let html = `<!DOCTYPE html>
                <html>
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width,initial-scale=1.0">
                    <title>X-Find迅聘选才</title>
                    <link rel="stylesheet" href="https://unpkg.com/ant-design-vue@1.4.4/dist/antd.min.css" />
                    <style>
                    ${resumecss}
                    </style>
                </head>
                <body>
                    <div class="resume_preview_page" style="margin:0 auto;width:1200px">
                    ${template}
                    </div>
                </body>
                </html>`;
      return getDocumentTemplates(resumecss, template);
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
