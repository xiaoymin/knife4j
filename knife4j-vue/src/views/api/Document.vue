<template>
  <div class="document">
    <a-row>
      <div class="api-title">
        基本信息
      </div>
      <a-row class="api-basic">
        <a-col class="api-basic-title" :span="3">接口名称</a-col>
        <a-col class="api-basic-body" :span="21">{{api.summary}}</a-col>
      </a-row>
      <a-row class="api-basic">
        <a-col class="api-basic-title" :span="3">接口地址</a-col>
        <a-col class="api-basic-body" :span="21">{{api.showUrl}}</a-col>
      </a-row>
      <a-row class="api-basic">
        <a-col class="api-basic-title" :span="3">请求方式</a-col>
        <a-col class="api-basic-body" :span="21">{{api.methodType}}</a-col>
      </a-row>
      <a-row class="api-basic">
        <a-col class="api-basic-title" :span="3">consumes</a-col>
        <a-col class="api-basic-body" :span="21">{{api.consumes}}</a-col>
      </a-row>
      <a-row class="api-basic">
        <a-col class="api-basic-title" :span="3">produces</a-col>
        <a-col class="api-basic-body" :span="21">{{api.produces}}</a-col>
      </a-row>
    </a-row>
    <!-- <a-divider class="divider" /> -->
    <!--接口描述-->
    <div v-if="api.description">
      <div class="api-title">
        接口描述
      </div>
      <div v-if="api.description" v-html="api.description" class="api-body-desc">
      </div>
    </div>
    <!--请求示例-->
    <div v-if="api.requestValue">
      <div class="api-title">
        请求示例
      </div>
      <editor v-model="api.requestValue" @init="editorInit" lang="json" theme="chmoe" width="100%" height="200"></editor>
    </div>
    <div class="api-title">
      请求参数
    </div>
    <a-table :defaultExpandAllRows="expanRows" :columns="columns" :dataSource="reqParameters" rowKey="id" size="small" :pagination="page">
      <template slot="requireTemplate" slot-scope="text">
        <span v-if="text" style="color:red">{{text.toLocaleString()}}</span>
        <span v-else>{{text.toLocaleString()}}</span>
      </template>
    </a-table>
    <div class="api-title">
      响应状态
    </div>
    <a-table :defaultExpandAllRows="expanRows" :columns="responseStatuscolumns" :dataSource="api.responseCodes" rowKey="id" size="small" :pagination="page">
      <template slot="descriptionTemplate" slot-scope="text">
        <div v-html="text"></div>
      </template>
    </a-table>

    <div class="api-title">
      响应参数
    </div>
    <div class="api-body-desc">
      我是说明少时诵诗书所所所所所所所所,knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案,knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案,knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案,knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案,knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案
    </div>
    <div class="api-title">
      响应示例
    </div>
    <editor v-model="content" @init="editorInit" lang="html" theme="chrome" width="100%" height="200"></editor>
  </div>
</template>
<script>
const requestcolumns = [
  {
    title: "参数名称",
    dataIndex: "name",
    width: "30%"
  },
  {
    title: "参数说明",
    dataIndex: "description",
    width: "15%"
  },
  {
    title: "请求类型",
    dataIndex: "in"
  },
  {
    title: "是否必须",
    dataIndex: "require",
    scopedSlots: { customRender: "requireTemplate" }
  },
  {
    title: "数据类型",
    dataIndex: "type"
  },
  {
    title: "schema",
    dataIndex: "schemaValue",
    width: "15%"
  }
];

const responseStatuscolumns = [
  {
    title: "状态码",
    dataIndex: "code",
    width: "20%"
  },
  {
    title: "说明",
    dataIndex: "description",
    width: "55%",
    scopedSlots: { customRender: "descriptionTemplate" }
  },
  {
    title: "schema",
    dataIndex: "schema"
  }
];
export default {
  name: "Document",
  components: { editor: require("vue2-ace-editor") },
  props: {
    api: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      content: "<span>Hello</span>",
      columns: requestcolumns,
      responseStatuscolumns: responseStatuscolumns,
      expanRows: true,
      page: false,
      reqParameters: []
    };
  },
  created() {
    this.initRequestParams();
  },
  methods: {
    initRequestParams() {
      var data = [];
      var that = this;
      var apiInfo = this.api;
      if (apiInfo.parameters != null && apiInfo.parameters.length > 0) {
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
      let reqParameters = [];
      if (data != null && data.length > 0) {
        data.forEach(function(md) {
          if (md.pid == "-1") {
            md.children = [];
            that.findModelChildren(md, data);
            //查找后如果没有,则将children置空
            if (md.children.length == 0) {
              md.children = null;
            }
            reqParameters.push(md);
          }
        });
      }
      that.reqParameters = reqParameters;
      console.log("遍历完成");
      console.log(reqParameters);
    },
    findModelChildren(md, modelData) {
      var that = this;
      if (modelData != null && modelData != undefined && modelData.length > 0) {
        modelData.forEach(function(nmd) {
          if (nmd.pid == md.id) {
            nmd.children = [];
            that.findModelChildren(nmd, modelData);
            //查找后如果没有,则将children置空
            if (nmd.children.length == 0) {
              nmd.children = null;
            }
            md.children.push(nmd);
          }
        });
      }
    },
    editorInit: function() {
      require("brace/ext/language_tools"); //language extension prerequsite...
      require("brace/mode/json");
      /*  require("brace/mode/javascript"); //language
      require("brace/mode/less");
      require("brace/theme/chrome");
      require("brace/snippets/javascript"); //snippet */
    }
  }
};
</script>
<style lang="less" scoped>
.api-tab {
  margin-top: 15px;

  .ant-tag {
    height: 32px;
    line-height: 32px;
  }
}
.api-basic {
  padding: 11px;
}
.api-basic-title {
  font-size: 14px;
  font-weight: 700;
}
.api-basic-body {
  font-size: 14px;
  font-family: -webkit-body;
}

.api-description {
  border-left: 4px solid #ddd;
  line-height: 30px;
}
.api-body-desc {
  padding: 10px;
  min-height: 35px;
  box-sizing: border-box;
  border: 1px solid #e8e8e8;
}
.ant-card-body {
  padding: 5px;
}
.api-title {
  margin-top: 10px;
  margin-bottom: 5px;
  font-size: 16px;
  font-weight: 600;
  height: 32px;
  line-height: 32px;
  border-left: 4px solid #00ab6d;
  text-indent: 8px;
}
.content-line {
  height: 25px;
  line-height: 25px;
}
.content-line-count {
  height: 35px;
  line-height: 35px;
}
.divider {
  margin: 4px 0;
}
</style>