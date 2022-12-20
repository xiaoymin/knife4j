<template>
  <div class="knife4j-document">
    <!-- <h2 :id="api.operationId">{{api.summary}}</h2> -->
    <a-row>
      <a-row :id="api.operationId" class="knife4j-api-title">
        {{ api.summary }}
      </a-row>
      <a-row :class="'knife4j-api-' + api.methodType.toLowerCase()">
        <div class="knife4j-api-summary">
          <span class="knife4j-api-summary-method">{{ api.methodType }}</span>
          <span class="knife4j-api-summary-path">{{ api.showUrl }}</span>
        </div>
      </a-row>
      <a-row class="knife4j-api-row">
        <a-col :span="12">
          <a-row>
            <a-col class="api-basic-title" :span="6">请求数据类型</a-col>
            {{ api.consumes }}
          </a-row>
        </a-col>
        <a-col :span="12">
          <a-row>
            <a-col class="api-basic-title" :span="6">响应数据类型</a-col>
            {{ api.produces }}
          </a-row>
        </a-col>
      </a-row>
    </a-row>
    <!-- <a-divider class="divider" /> -->
    <!--接口描述-->
    <div v-if="api.description">
      <div class="api-title">
        接口描述
      </div>
      <div v-if="api.description" v-html="api.description" class="api-body-desc"></div>
    </div>
    <!--请求示例-->
    <div v-if="api.requestValue">
      <div class="api-title">
        请求示例
      </div>
      <pre class="api-editor-show" v-html="formaterJson(api.requestValue)"></pre>
      <!-- <editor-show :value="api.requestValue"></editor-show> -->
    </div>
    <div class="api-title">
      请求参数
    </div>
    <a-table defaultExpandAllRows :columns="columns" :dataSource="reqParameters" :rowKey="genUnionTableKey" size="small"
      :pagination="page">
      <template slot="requireTemplate" slot-scope="text">
        <span v-if="text" style="color:red">{{ text.toLocaleString() }}</span>
        <span v-else>{{ text.toLocaleString() }}</span>
      </template>

      <template slot="typeTemplate" slot-scope="text">
        <span :class="'knife4j-request-' + text">{{ text }}</span>
      </template>

      <template slot="datatypeTemplate" slot-scope="text, record">
        <data-type :text="text" :record="record"></data-type>
      </template>
    </a-table>
    <div class="api-title">
      响应状态
    </div>
    <a-table :defaultExpandAllRows="expanRows" :columns="responseStatuscolumns" :dataSource="api.responseCodes"
      rowKey="code" size="small" :pagination="page">
      <template slot="descriptionTemplate" slot-scope="text">
        <div v-html="text"></div>
      </template>
    </a-table>
    <!--响应参数需要判断是否存在多个code-schema的情况-->
    <div v-if="api.multipartResponseSchema">
      <!--多个响应编码code的情况在离线文档中需要单独遍历分开-->
      <a-tabs v-for="resp in multipCodeDatas" :key="resp.code">
        <a-tab-pane :tab="resp.code">
          <!--判断响应头-->
          <div v-if="resp.responseHeaderParameters">
            <div class="api-title">
              响应Header
            </div>
            <a-table :defaultExpandAllRows="expanRows" :columns="responseHeaderColumns"
              :dataSource="resp.responseHeaderParameters" rowKey="id" size="small" :pagination="page">
            </a-table>
          </div>
          <!--响应参数-->
          <div class="api-title">
            响应参数
          </div>
          <a-table :defaultExpandAllRows="expanRows" :columns="responseParametersColumns" :dataSource="resp.data"
            rowKey="id" size="small" :pagination="page">
          </a-table>
          <div class="api-title">
            响应示例
          </div>
          <div class="api-editor-show" v-if="resp.responseBasicType">
            {{ resp.responseText }}
          </div>
          <pre class="api-editor-show" v-else v-html="formaterJson(resp.responseValue)"></pre>
          <!-- <editor-show :value="resp.responseBasicType ? resp.responseText : resp.responseValue"></editor-show> -->
          <!-- <editor :value="resp.responseBasicType ? resp.responseText : resp.responseValue" @init="multiResponseSampleEditorInit" lang="json" theme="eclipse" width="100%" :height="editorMultiHeight"></editor> -->
        </a-tab-pane>
      </a-tabs>
    </div>
    <div v-else>
      <!--判断响应头-->
      <div v-if="api.responseHeaderParameters">
        <div class="api-title">
          响应Header
        </div>
        <a-table :defaultExpandAllRows="expanRows" :columns="responseHeaderColumns"
          :dataSource="api.responseHeaderParameters" rowKey="id" size="small" :pagination="page">
        </a-table>
      </div>
      <!--响应参数-->
      <div class="api-title">
        响应参数
      </div>
      <a-table :defaultExpandAllRows="expanRows" :columns="responseParametersColumns" :dataSource="multipData.data"
        rowKey="id" size="small" :pagination="page">
      </a-table>
      <div class="api-title">
        响应示例
      </div>
      <div class="api-editor-show" v-if="multipData.responseBasicType">
        {{ multipData.responseText }}
      </div>
      <pre class="api-editor-show" v-else v-html="formaterJson(multipData.responseValue)"></pre>
      <!--  <editor-show :value="multipData.responseBasicType ? multipData.responseText : multipData.responseValue"></editor-show> -->
      <!-- <editor :value="multipData.responseBasicType ? multipData.responseText : multipData.responseValue" @init="singleResponseSampleEditorInit" lang="json" theme="eclipse" width="100%" :height="editorSingleHeight"></editor> -->
    </div>
  </div>
</template>
<script>
import KUtils from "@/core/utils";
import Constants from "@/store/constants";
/* import DataType from "./DataType";
import EditorShow from "./EditorShow"; */
// 请求参数table-header
const requestcolumns = [
  {
    title: "参数名称",
    dataIndex: "name",
    width: "30%"
  },
  {
    title: "参数说明",
    dataIndex: "description",
    width: "25%"
  },
  {
    title: "请求类型",
    dataIndex: "in",
    scopedSlots: { customRender: "typeTemplate" }
  },
  {
    title: "是否必须",
    dataIndex: "require",
    scopedSlots: { customRender: "requireTemplate" }
  },
  {
    title: "数据类型",
    dataIndex: "type",
    scopedSlots: { customRender: "datatypeTemplate" }
  },
  {
    title: "schema",
    dataIndex: "schemaValue",
    width: "15%"
  }
];
// 响应状态table-header
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
// 响应头-header
const responseHeaderColumns = [
  {
    title: "参数名称",
    dataIndex: "name",
    width: "30%"
  },
  {
    title: "参数说明",
    dataIndex: "description",
    width: "55%"
  },
  {
    title: "数据类型",
    dataIndex: "type"
  }
];
const responseParametersColumns = [
  {
    title: "参数名称",
    dataIndex: "name",
    width: "35%"
  },
  {
    title: "参数说明",
    dataIndex: "description",
    width: "40%"
  },
  {
    title: "类型",
    dataIndex: "type"
  },
  {
    title: "schema",
    dataIndex: "schemaValue",
    width: "15%"
  }
];
var instance = null;
export default {
  name: "Document",
  components: {
    editor: require("vue2-ace-editor"),
    "DataType": () => import('./DataType'),
    "EditorShow": () => import('./EditorShow')
  },
  props: {
    api: {
      type: Object,
      required: true
    },
    swaggerInstance: {
      type: Object,
      required: true
    }
  },
  beforeCreate() {
    instance = this;
  },
  data() {
    return {
      content: "<span>Hello</span>",
      columns: requestcolumns,
      responseHeaderColumns: responseHeaderColumns,
      responseStatuscolumns: responseStatuscolumns,
      responseParametersColumns: responseParametersColumns,
      expanRows: true,
      // 接收一个响应信息对象,遍历得到树形结构的值
      multipCode: false,
      multipCodeDatas: [],
      multipData: {},
      page: false,
      reqParameters: []
    };
  },
  created() {
    // var key = Constants.globalTreeTableModelParams + this.api.instanceId;
    // var treeTableModel = this.swaggerInstance.refTreeTableModels;
    // instance.$Knife4jModels.setValue(key, treeTableModel);
    // instance.initRequestParams();
    // console("该请求参数----------：" + this.api.showUrl);
    // console(this.api);
    // console(this.api.reqParameters);
    // 赋值
    this.reqParameters = this.api.reqParameters;
    instance.initResponseCodeParams();
  },
  methods: {
    genUnionTableKey() {
      return KUtils.randomMd5();
    },
    initRequestParams() {
      var data = [];
      var that = this;
      var key = Constants.globalTreeTableModelParams + this.api.instanceId;

      var treeTableModel = this.swaggerInstance.refTreeTableModels;
      var apiInfo = this.api;
      if (apiInfo.parameters != null && apiInfo.parameters.length > 0) {
        data = data.concat(apiInfo.parameters);
      }
      if (
        apiInfo.refTreetableparameters != null &&
        apiInfo.refTreetableparameters.length > 0
      ) {
        apiInfo.refTreetableparameters.forEach(function (ref) {
          data = data.concat(ref.params);
        });
      }
      if (data != null) {
        data.sort(function (a, b) {
          return b.require - a.require;
        });
      }
      let reqParameters = [];
      if (data != null && data.length > 0) {
        data.forEach(function (md) {
          if (md.pid == "-1") {
            md.children = [];
            if (md.schema) {
              // 判断当前缓存是否存在
              var schemaName = md.schemaValue;
              if (KUtils.checkUndefined(schemaName)) {
                if (that.$Knife4jModels.exists(key, schemaName)) {
                  // 存在
                  // console("存在-不用查找---" + schemaName);
                  // //console(that.$Knife4jModels.instance);
                  var model = that.$Knife4jModels.getByModelName(
                    key,
                    schemaName
                  );
                  if (KUtils.checkUndefined(model)) {
                    var children = model.params;
                    // 更改pid
                    if (KUtils.arrNotEmpty(children)) {
                      children.forEach(function (chd) {
                        var target = that.copyNewParameter(chd);
                        target.pid = md.id;
                        md.children.push(target);
                      });
                    }
                  }

                  // md.children = children;
                } else {
                  // 不存在
                  // console("不存在--开始查找---" + schemaName);
                }
              }
            }
            //  that.findModelChildren(md, data);
            // 查找后如果没有,则将children置空
            if (!KUtils.arrNotEmpty(md.children)) {
              md.children = null;
            }
            /* if (md.children.length == 0) {
              md.children = null;
            } */
            reqParameters.push(md);
          }
        });
      }
      that.reqParameters = reqParameters;
      // that.storeCacheModels(cacheModelChildrens);
      // //console("遍历完成");
      // //console(reqParameters);
    },
    storeCacheModels(val) {
      var key = Constants.globalTreeTableModelParams + this.api.instanceId;
      this.$localStore.setItem(key, val);
    },
    deepTreeTableSchemaModel(param, treeTableModel, rootParam) {
      var that = this;
      var key = Constants.globalTreeTableModelParams + this.api.instanceId;
      // //console(model.name)
      if (KUtils.checkUndefined(param.schemaValue)) {
        var schema = treeTableModel[param.schemaValue];
        if (KUtils.checkUndefined(schema)) {
          rootParam.parentTypes.push(param.schemaValue);
          if (KUtils.arrNotEmpty(schema.params)) {
            schema.params.forEach(function (nmd) {
              // childrenparam需要深拷贝一个对象
              var childrenParam = that.copyNewParameter(nmd);
              childrenParam.pid = param.id;
              param.children.push(childrenParam);
              // children.push(childrenParam)
              if (childrenParam.schema) {
                // 存在schema,判断是否出现过
                if (
                  rootParam.parentTypes.indexOf(childrenParam.schemaValue) == -1
                ) {
                  var schemaName = childrenParam.schemaValue;
                  if (KUtils.checkUndefined(schemaName)) {
                    childrenParam.children = [];
                    // 减少递归次数
                    if (that.$Knife4jModels.exists(key, schemaName)) {
                      // console("递归中存在--不用找了");
                      var children = that.$Knife4jModels.getByModelName(
                        key,
                        schemaName
                      );
                      // 更改pid
                      if (KUtils.arrNotEmpty(children)) {
                        children.forEach(function (chd) {
                          var target = that.copyNewParameter(chd);
                          target.pid = childrenParam.id;
                          childrenParam.children.push(target);
                        });
                      }
                      // childrenParam.children = children;
                    } else {
                      // 不存在
                      // console("不存在--开始查找-递归中---" + schemaName);
                      // 根据schema查找当前的子级参数
                      that.deepTreeTableSchemaModel(
                        childrenParam,
                        treeTableModel,
                        rootParam
                      );
                      if (childrenParam.children.length == 0) {
                        childrenParam.children = null;
                      }
                      that.$Knife4jModels.addModels(
                        key,
                        schemaName,
                        childrenParam.children
                      );
                      // 更新
                      // cacheModelChildrens[schemaName] = md.children;
                    }
                  }
                  // 找chlidrenParam的子类
                }
              }
            });
          }
        }
      }
    },
    copyNewParameter(source) {
      var target = {
        children: source.children,
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
    findModelChildren(md, modelData) {
      var that = this;
      if (modelData != null && modelData != undefined && modelData.length > 0) {
        modelData.forEach(function (nmd) {
          if (nmd.pid == md.id) {
            nmd.children = [];
            that.findModelChildren(nmd, modelData);
            // 查找后如果没有,则将children置空
            if (nmd.children.length == 0) {
              nmd.children = null;
            }
            md.children.push(nmd);
          }
        });
      }
    },
    initResponseCodeParams() {
      // 响应体参数
      var that = this;
      that.multipCode = that.api.multipartResponseSchema;
      let rcodes = that.api.responseCodes;
      if (rcodes != null && rcodes != undefined) {
        rcodes.forEach(function (rc) {
          // 遍历
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
              rc.responseTreetableRefParameters.forEach(function (ref) {
                respdata = respdata.concat(ref.params);
              });
            }
            let nrecodedatas = [];
            // 遍历得到新的符合antd的树形结构
            if (respdata != null && respdata.length > 0) {
              respdata.forEach(function (md) {
                if (md.pid == "-1") {
                  md.children = [];
                  // that.findModelChildren(md, respdata);
                  // 查找后如果没有,则将children置空
                  if (md.children.length == 0) {
                    md.children = null;
                  }
                  nrecodedatas.push(md);
                }
              });
            }
            var nresobj = { ...rc, data: nrecodedatas };
            if (!that.multipCode) {
              that.multipData = nresobj;
            }
            that.multipCodeDatas.push(nresobj);
          }
        });
      }
      // //console("响应头");
      // //console(that.multipCodeDatas);
      // //console(that.multipData);
    },
    formaterJson(json) {
      try {
        if (typeof json != "string") {
          json = JSON.stringify(json, undefined, 2);
        }
        json = json
          .replace(/&/g, "&")
          .replace(/</g, "<")
          .replace(/>/g, ">");
        return json.replace(
          /("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g,
          function (match) {
            var cls = "number";
            if (/^"/.test(match)) {
              if (/:$/.test(match)) {
                cls = "key";
              } else {
                cls = "string";
              }
            } else if (/true|false/.test(match)) {
              cls = "boolean";
            } else if (/null/.test(match)) {
              cls = "null";
            }
            return '<span class="' + cls + '">' + match + "</span>";
          }
        );
      } catch (error) {
        return json;
      }
    }
  }
};
</script>
<style lang="less" scoped>
.knife4j-document {
  margin-top: 30px;
}

.api-tab {
  margin-top: 15px;

  .ant-tag {
    height: 32px;
    line-height: 32px;
  }
}

.api-editor-show {
  margin: 15px 0;
  font: 100 12px/18px monaco, andale mono, courier new;
  padding: 10px 12px;
  border: #ccc 1px solid;
  border-left-width: 4px;
  background-color: #fefefe;
  box-shadow: 0 0 4px #eee;
  word-break: break-all;
  word-wrap: break-word;
  color: #444;
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
  height: 30px;
  line-height: 30px;
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
