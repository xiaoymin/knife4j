<template>
  <a-layout-content class="knife4j-body-content">
    <a-row class="markdown-row">
      <a-row class="globalparameters">
        <a-row class="gptips" v-html="$t('offline.des')">
          Knife4j提供导出4种格式的离线文档(Html\Markdown\Word\Pdf)
        </a-row>
      </a-row>
      <a-row class="knife4j-download-button">
        <a-button @click="triggerDownloadMarkdown">
          <a-icon type="file-markdown" /><span v-html="$t('offline.download.markdown')">下载Markdown</span></a-button
        >
        <a-button type="default" @click="triggerDownload">
          <a-icon type="file-text" /><span v-html="$t('offline.download.html')">下载Html</span></a-button
        >
        <a-button type="default" @click="triggerDownloadWord">
          <a-icon type="file-word" /><span v-html="$t('offline.download.word')">下载Word</span></a-button
        >
        <a-button type="default" @click="triggerDownloadPDF">
          <a-icon type="file-pdf" /><span v-html="$t('offline.download.pdf')">下载PDF</span></a-button
        >
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
import { resumecss } from "./OfficelineCss";
import {getDocumentVueTemplates} from "@/components/officeDocument/officeDocTemplate";
import {getDocumentVueTemplatesUS} from "@/components/officeDocument/officeDocTemplateUS";
import markdownText from "@/components/officeDocument/markdownTransform";
import markdownTextUS from "@/components/officeDocument/markdownTransformUS";
import OnlineDocument from "@/views/api/OnlineDocument";
import { Modal } from "ant-design-vue";
import DownloadHtml from "./DownloadHtml";
import KUtils from "@/core/utils";
import Constants from "@/store/constants";
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
      //是否递归遍历过tags
      deepTagFlag: false,
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
    //console("dom重新被渲染");
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
        }, 1500);
      }
    }
  },
  created() {
    this.initModels();
    //this.deepTags();
  },
  watch:{
    language:function(val,oldval){
      this.markdownText=null;
    }
  },
  computed:{
    language(){
       return this.$store.state.globals.language;
    }
  },
  methods: {
    getCurrentI18nInstance(){
      return this.$i18n.messages[this.language];
    },
    initModels() {
      var key = Constants.globalTreeTableModelParams + this.data.instance.id;
      //根据instance的实例初始化model名称
      var treeTableModel = this.data.instance.refTreeTableModels;
      this.$Knife4jModels.setValue(key, treeTableModel);
      //console("初始化Models");
      //this.$Knife4jModels.setTags(key, this.data.instance.tags);
    },
    deepTags() {
      var that = this;
      var key = Constants.globalTreeTableModelParams + this.data.instance.id;
      if (!this.deepTagFlag) {
        //console("deepTags");
        var tags = this.data.instance.tags;
        ////console(tags);
        //console("开始遍历tags时间：" + new Date().toGMTString());
        if (KUtils.arrNotEmpty(tags)) {
          tags.forEach(function(tag) {
            ////console(tag);
            //判断是否存在参数
            if (KUtils.arrNotEmpty(tag.childrens)) {
              //存在接口,遍历接口的参数
              tag.childrens.forEach(function(apiInfo) {
                ////console("接口地址:" + apiInfo.showUrl);
                //获取接口的参数
                var data = [];
                if (
                  apiInfo.parameters != null &&
                  apiInfo.parameters.length > 0
                ) {
                  data = data.concat(apiInfo.parameters);
                }
                if (
                  apiInfo.refTreetableparameters != null &&
                  apiInfo.refTreetableparameters.length > 0
                ) {
                  apiInfo.refTreetableparameters.forEach(function(ref) {
                    data = data.concat(ref.params);
                  });
                }
                if (data != null) {
                  data.sort(function(a, b) {
                    return b.require - a.require;
                  });
                }
                var reqParameters = [];
                //判断当前data参数接口是否依然还存在参数
                if (KUtils.arrNotEmpty(data)) {
                  //存在请求参数,遍历data参数
                  data.forEach(function(param) {
                    ////console(param);
                    //只查找第一级的参数，即pid=-1的参数
                    if (param.pid == "-1") {
                      param.children = [];
                      //判断该参数是否存在schema参数
                      if (param.schema) {
                        //判断当前缓存是否存在
                        var schemaName = param.schemaValue;

                        if (KUtils.checkUndefined(schemaName)) {
                          // //console("schemaValue--checkUndefined");
                          if (that.$Knife4jModels.exists(key, schemaName)) {
                            ////console("存在-不用查找---" + schemaName);
                            ////console(that.$Knife4jModels.instance);
                            var model = that.$Knife4jModels.getByModelName(
                              key,
                              schemaName
                            );
                            if (KUtils.checkUndefined(model)) {
                              var children = model.params;
                              if (KUtils.arrNotEmpty(children)) {
                                children.forEach(function(chd) {
                                  var target = that.copyNewParameter(chd);
                                  target.pid = param.id;
                                  param.children.push(target);
                                });
                              }
                            }
                          } else {
                            ////console("schemavalue--Not Existis");
                          }
                        }
                      }
                      //针对非空的参数,设置children属性为空
                      if (!KUtils.arrNotEmpty(param.children)) {
                        param.children = null;
                        //从knife4jModels中查询参数
                      }
                      reqParameters.push(param);
                    }
                  });
                }
                //给请求参数重新赋值
                apiInfo.reqParameters = reqParameters;
                //遍历响应参数的值
                that.deepResponseParameters(apiInfo);
              });
            }
          });
        }
        //console("结束遍历tags时间：" + new Date().toGMTString());
        //console(tags);
        /* var tgdata = [];
        tgdata.push(tags[0]); */
        this.tags = tags;
        this.deepTagFlag = true;
        //that.$kloading.destroy();
      }
    },
    deepResponseParameters(apiInfo) {
      //遍历响应参数
      var that = this;
      var key = Constants.globalTreeTableModelParams + this.data.instance.id;
      //添加自定义属性
      apiInfo.multipCode = apiInfo.multipartResponseSchema;
      apiInfo.multipCodeDatas = [];
      //这里不
      apiInfo.multipData = {};
      let rcodes = apiInfo.responseCodes;
      if (rcodes != null && rcodes != undefined) {
        rcodes.forEach(function(rc) {
          //遍历
          if (rc.schema != undefined && rc.schema != null) {
            var respdata = [];
            if (
              rc.responseParameters != null &&
              rc.responseParameters.length > 0
            ) {
              respdata = respdata.concat(rc.responseParameters);
            }
            if (
              rc.responseTreetableRefParameters != null &&
              rc.responseTreetableRefParameters.length > 0
            ) {
              rc.responseTreetableRefParameters.forEach(function(ref) {
                respdata = respdata.concat(ref.params);
              });
            }
            let nrecodedatas = [];
            //遍历得到新的符合antd的树形结构
            if (respdata != null && respdata.length > 0) {
              respdata.forEach(function(param) {
                if (param.pid == "-1") {
                  param.children = [];
                  //判断该参数是否存在schema参数
                  if (param.schema) {
                    //判断当前缓存是否存在
                    var schemaName = param.schemaValue;
                    if (KUtils.checkUndefined(schemaName)) {
                      // //console("schemaValue--checkUndefined");
                      if (that.$Knife4jModels.exists(key, schemaName)) {
                        ////console("存在-不用查找---" + schemaName);
                        ////console(that.$Knife4jModels.instance);
                        var model = that.$Knife4jModels.getByModelName(
                          key,
                          schemaName
                        );
                        if (KUtils.checkUndefined(model)) {
                          var children = model.params;
                          if (KUtils.arrNotEmpty(children)) {
                            children.forEach(function(chd) {
                              var target = that.copyNewParameter(chd);
                              target.pid = param.id;
                              param.children.push(target);
                            });
                          }
                        }
                      } else {
                        ////console("schemavalue--Not Existis");
                      }
                    }
                  }

                  //that.findModelChildren(md, respdata);
                  //查找后如果没有,则将children置空
                  if (param.children.length == 0) {
                    param.children = null;
                  }
                  nrecodedatas.push(param);
                }
              });
            }
            var nresobj = { ...rc, data: nrecodedatas };
            if (!apiInfo.multipCode) {
              apiInfo.multipData = nresobj;
            }
            apiInfo.multipCodeDatas.push(nresobj);
          }
        });
      }
    },
    copyNewParameter(source) {
      var tmpc = source.children;
      if (!KUtils.checkUndefined(tmpc)) {
        tmpc = null;
      }
      var target = {
        children: tmpc,
        childrenTypes: source.childrenTypes,
        def: source.def,
        description: source.description,
        enum: source.enum,
        example: source.example,
        id: source.id,
        ignoreFilterName: source.ignoreFilterName,
        in: source.in,
        level: source.level,
        name: source.name,
        parentTypes: source.parentTypes,
        pid: source.pid,
        readOnly: source.readOnly,
        require: source.require,
        schema: source.schema,
        schemaValue: source.schemaValue,
        show: source.show,
        txtValue: source.txtValue,
        type: source.type,
        validateInstance: source.validateInstance,
        validateStatus: source.validateStatus,
        value: source.value
      };
      return target;
    },
    triggerDownloadPDF() {
      //var message='该功能尚未实现...'
      var message=this.getCurrentI18nInstance().message.offline.imple;
      this.$message.info(message);
    },
    triggerDownloadWord() {
      var message=this.getCurrentI18nInstance().message.offline.imple;
      this.$message.info(message);
    },
    triggerDownloadMarkdown() {
      //下载markdown
      var that = this;
      //正在下载Markdown文件中,请稍后...
      var downloadMessage=this.getCurrentI18nInstance().message.offline.markdown;
      that.$kloading.show({
        text: downloadMessage
      });
      this.deepTags();
      var instance = {
        title: that.data.instance.title,
        description: that.data.instance.title,
        contact: that.data.instance.contact,
        version: that.data.instance.version,
        host: that.data.instance.host,
        basePath: that.data.instance.basePath,
        termsOfService: that.data.instance.termsOfService,
        name: that.data.instance.name,
        url: that.data.instance.url,
        location: that.data.instance.location,
        pathArrs: that.data.instance.pathArrs,
        tags: that.tags,
        markdownFiles: that.data.instance.markdownFiles
      };

      //遍历得到markdown语法
      if (this.markdownText == null || this.markdownText == "") {
        //遍历得到markdown文本
        //this.markdownText = markdownText(this.data.instance);
        if(this.getCurrentI18nInstance().lang==='zh'){
          this.markdownText = markdownText(instance);
        }else{
          this.markdownText = markdownTextUS(instance);
        }
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
      //正在下载Html中,请稍后...
      var message=this.getCurrentI18nInstance().message.offline.html;
      that.$kloading.show({
        text: message
      });
      that.deepTags();
      setTimeout(() => {
        that.$kloading.destroy();
        that.downloadHtml();
      }, 1000);
    },
    downloadMarkdown(content) {
      //console("downloadMarkdown");
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
      ////console("downloadHtml");
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
    getHtmlData() {
      //获取导出网页的Html数据结构,用于在单页面渲染
      var that = this;
      var htmlData = {
        //接口基本信息
        instance: {
          title: that.data.instance.title,
          description: that.data.instance.title,
          contact: that.data.instance.contact,
          version: that.data.instance.version,
          host: that.data.instance.host,
          basePath: that.data.instance.basePath,
          termsOfService: that.data.instance.termsOfService,
          name: that.data.instance.name,
          url: that.data.instance.url,
          location: that.data.instance.location,
          pathArrs: that.data.instance.pathArrs
        },
        hideShow: true,
        tags: that.tags
      };
      return htmlData;
    },
    getHtmlContent(title) {
      //获取html另外一种方式：this.$el.outerHTML
      var domId = "content_views" + this.data.instance.id;
      if (title == undefined || title == null || title == "") {
        title = "Knife4j-API Documenation";
      }
      //抛弃template
      // const template = document.getElementById(domId).innerHTML;
      var dstr = JSON.stringify(this.getHtmlData());
      //const template = document.getElementById("content_views").innerHTML;
      //return getDocumentTemplates(title, resumecss, template);
      if(this.getCurrentI18nInstance().lang==='zh'){
          return getDocumentVueTemplates(title, resumecss, dstr);
      }else{
        return getDocumentVueTemplatesUS(title, resumecss, dstr);
      }
      return getDocumentVueTemplates(title, resumecss, dstr);
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
