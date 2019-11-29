<template>
  <a-layout-content class="knife4j-body-content">
    <a-row class="markdown-row">
      <a-row class="markdown-body editormd-preview-container">
        <vue-markdown :source="description"></vue-markdown>
      </a-row>

      <a-row>
        <a-button type="primary" @click="triggerDownload">
          下载Html</a-button>
        <a-button style="margin-left:10px;" type="primary" @click="downloadHtml">下载PDF</a-button>
      </a-row>
      <!--  <a-modal v-model="downloadHtmlFlag" :footer="null" :maskClosable="false" :keyboard="false" :closable="false">
        <p>正在下载中...</p>
      </a-modal> -->
      <div class="htmledit_views" id="content_views">
        <!--基础信息-->
        <a-row>
          <a-col :span="24">
            <div class="title">
              <h2>{{data.instance.title}}</h2>
            </div>
            <div class="description">
              <a-row class="content-line">
                <a-col :span="5">
                  <h3>简介</h3>
                </a-col>
                <a-col :span="19"><span v-html="data.instance.description" /></a-col>
              </a-row>
              <a-divider class="divider" />
              <a-row class="content-line">
                <a-col :span="5">
                  <h3>作者</h3>
                </a-col>
                <a-col :span="19"><span v-html="data.instance.contact" /></a-col>
              </a-row>
              <a-divider class="divider" />
              <a-row class="content-line">
                <a-col :span="5">
                  <h3>版本</h3>
                </a-col>
                <a-col :span="19"><span v-html="data.instance.version" /></a-col>
              </a-row>
              <a-divider class="divider" />
              <a-row class="content-line">
                <a-col :span="5">
                  <h3>host</h3>
                </a-col>
                <a-col :span="19"><span v-html="data.instance.host" /></a-col>
              </a-row>
              <a-divider class="divider" />
              <a-row class="content-line">
                <a-col :span="5">
                  <h3>basePath</h3>
                </a-col>
                <a-col :span="19"><span v-html="data.instance.basePath" /></a-col>
              </a-row>
              <a-divider class="divider" />
              <a-row class="content-line">
                <a-col :span="5">
                  <h3>服务Url</h3>
                </a-col>
                <a-col :span="19"><span v-html="data.instance.termsOfService" /></a-col>
              </a-row>
              <a-divider class="divider" />
              <a-row class="content-line">
                <a-col :span="5">
                  <h3>分组名称</h3>
                </a-col>
                <a-col :span="19"><span v-html="data.instance.name" /></a-col>
              </a-row>
              <a-divider class="divider" />
              <a-row class="content-line">
                <a-col :span="5">
                  <h3>分组url</h3>
                </a-col>
                <a-col :span="19"><span v-html="data.instance.url" /></a-col>
              </a-row>
              <a-divider class="divider" />
              <a-row class="content-line">
                <a-col :span="5">
                  <h3>分组location</h3>
                </a-col>
                <a-col :span="19"><span v-html="data.instance.location" /></a-col>
              </a-row>
              <a-divider class="divider" />
              <a-row class="content-line">
                <a-col :span="5">
                  <h3>接口统计信息</h3>
                </a-col>
                <a-col :span="19">
                  <a-row class="content-line-count" v-for="param in data.instance.pathArrs" :key="param.method">
                    <a-col :span="3">
                      {{param.method}}
                    </a-col>
                    <a-col :span="2">
                      <a-tag color="#108ee9">{{param.count}}</a-tag>
                    </a-col>
                    <a-divider class="divider-count" />
                  </a-row>
                </a-col>
              </a-row>

            </div>
          </a-col>
        </a-row>
        <a-row v-for="api in apis" :key="api.id">
          <Document :api="api" />
        </a-row>
      </div>
    </a-row>
  </a-layout-content>
</template>
<script>
import "@/assets/css/editormd.css";
import VueMarkdown from "vue-markdown";
import html2canvas from "html2canvas";
import { resumecss } from "./antd";
import getDocumentTemplates from "@/components/officeDocument/officeDocTemplate";
import Document from "@/views/api/Document";
import { Modal } from "ant-design-vue";

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
    Document
  },
  data() {
    return {
      columns: columns,
      apis: [],
      expanRows: true,
      downloadHtmlFlag: false,
      downloadPDF: false,
      modal: null,
      page: false,
      description:
        "> `Knife4j`提供markdwon格式类型的离线文档,开发者可拷贝该内容通过其他markdown转换工具进行转换为html或pdf."
    };
  },
  updated() {
    console.log("dom重新被渲染");
    if (this.downloadHtmlFlag) {
      //下载html
      this.downloadHtml();
      //关闭
      this.$kloading.destroy();
    }
  },
  created() {},
  methods: {
    triggerDownload() {
      console.log("trigger---");
      let that = this;
      console.log(this.$refs);
      that.$kloading.show({
        text: "正在下载中...",
        el: this.$refs.box
      });

      if (!this.downloadHtmlFlag) {
        this.downloadHtmlFlag = true;
        //赋值Html重新渲染dom
        setTimeout(() => {
          that.apis = that.data.instance.paths;
          //that.$kloading.destroy();
        }, 600);
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
