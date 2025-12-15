<template>
  <div class="document">
    <a-row>
      <a-row class="knife4j-api-title">
        <a-col :span="18">
          <span v-if="api.deprecated" class="knife4j-menu-api-deprecated">
            {{ api.summary }}
          </span>
          <span v-else>
            {{ api.summary }}
          </span>
        </a-col>
        <a-col :span="2" :id="'btnCopyMethod' + api.id" class="knife4j-api-copy-address" v-html="$t('doc.copyMethod')">
          复制接口</a-col>
        <a-col :span="2" :id="'btnCopyMarkdown' + api.id" class="knife4j-api-copy-address" v-html="$t('doc.copy')">复制文档
        </a-col>
        <a-col :span="2" :id="'btnCopyAddress' + api.id" class="knife4j-api-copy-address" v-html="$t('doc.copyHash')">
          复制地址</a-col>
      </a-row>
      <a-row :class="'knife4j-api-' + api.methodType.toLowerCase()">
        <div class="knife4j-api-summary">
          <span class="knife4j-api-summary-method">
            <a-icon v-if="api.securityFlag" style="font-size:16px;" type="unlock" /> {{ api.methodType }}
          </span>
          <span class="knife4j-api-summary-path">{{ api.showUrl }}</span>
        </div>
      </a-row>
      <a-row class="knife4j-api-row">
        <a-col :span="12">
          <a-row>
            <a-col class="api-basic-title" :span="6" v-html="$t('doc.produces')">请求数据类型</a-col>
            {{ contentType }}
          </a-row>
        </a-col>
        <a-col :span="12">
          <a-row>
            <a-col class="api-basic-title" :span="6" v-html="$t('doc.consumes')">响应数据类型</a-col>
            {{ api.produces }}
          </a-row>
        </a-col>
      </a-row>
    </a-row>
    <!-- <a-divider class="divider" /> -->
    <div v-if="api.author">
      <div class="api-title" v-html="$t('doc.author')">
        开发者
      </div>
      <div v-if="api.author" v-html="api.author" class="api-body-desc"></div>
    </div>
    <!--接口描述-->
    <div v-if="api.description">
      <div class="api-title" v-html="$t('doc.des')">
        接口描述
      </div>
      <div v-if="api.description" v-html="api.description" class="api-body-desc"></div>
    </div>
    <!--请求示例-->
    <div v-if="api.requestValue">
      <div class="api-title" v-html="$t('doc.requestExample')">
        请求示例
      </div>
      <editor-show :value="api.requestValue" :xmlMode="api.xmlRequest"></editor-show>
    </div>
    <div class="api-title" v-html="$t('doc.params')">
      请求参数
    </div>
    <a-table :defaultExpandAllRows="expanRows" :columns="columns" :dataSource="reqParameters" rowKey="id" size="small"
      :pagination="page">
      <template slot="descriptionValueTemplate" slot-scope="text,record">
        <span v-html="text"></span>
        <span v-if="record.example">,<span v-html="$t('doc.example')"></span>({{ record.example }})</span>
      </template>
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
    <div v-if="responseCodeDisplayStatus">
      <div class="api-title" v-html="$t('doc.response')">
        响应状态
      </div>
      <a-table :defaultExpandAllRows="expanRows" :columns="responseStatuscolumns" :dataSource="api.responseCodes"
        rowKey="code" size="small" :pagination="page">
        <template slot="descriptionTemplate" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="schemaTemplate" slot-scope="text,record">
          <span v-if="text != null" v-html="text"></span>
          <span v-else-if="record.schemaTitle != null" v-html="record.schemaTitle"></span>
        </template>
      </a-table>
    </div>
    <!--响应参数需要判断是否存在多个code-schema的情况-->
    <div v-if="api.multipartResponseSchema">
      <a-tabs @change="multipartTabCodeChanges">
        <a-tab-pane v-for="resp in multipCodeDatas" :key="resp.code" :tab="resp.code">
          <!--判断响应头-->
          <div v-if="resp.responseHeaderParameters">
            <div class="api-title" v-html="$t('doc.responseHeaderParams')">
              响应Header
            </div>
            <a-table :defaultExpandAllRows="expanRows" :columns="responseHeaderColumns"
              :dataSource="resp.responseHeaderParameters" rowKey="id" size="small" :pagination="page">
            </a-table>
          </div>
          <!--响应参数-->
          <div class="api-title" v-html="$t('doc.responseParams')">
            响应参数
          </div>
          <a-table :defaultExpandAllRows="expanRows" :columns="responseParametersColumns" :dataSource="resp.data"
            rowKey="id" size="small" :pagination="page">
            <template slot="descriptionTemplate" slot-scope="text">
              <span v-html="text"></span>
            </template>
          </a-table>
          <div class="api-title" v-html="$t('doc.responseExample')">
            响应示例
          </div>
          <a-row :id="'knife4jDocumentShowEditor' + api.id + resp.code">
            <editor-show @showDescription="showResponseEditFieldDescription" :value="resp.responseBasicType ? resp.responseText : resp.responseValue
              "></editor-show>
          </a-row>

          <!-- <editor :value="resp.responseBasicType ? resp.responseText : resp.responseValue" @init="multiResponseSampleEditorInit" lang="json" theme="eclipse" width="100%" :height="editorMultiHeight"></editor> -->
        </a-tab-pane>
      </a-tabs>
    </div>
    <div v-else>
      <!--判断响应头-->
      <div v-if="api.responseHeaderParameters">
        <div class="api-title" v-html="$t('doc.responseHeaderParams')">
          响应Header
        </div>
        <a-table :defaultExpandAllRows="expanRows" :columns="responseHeaderColumns"
          :dataSource="api.responseHeaderParameters" rowKey="id" size="small" :pagination="page">
        </a-table>
      </div>
      <!--响应参数-->
      <div class="api-title" v-html="$t('doc.responseParams')">
        响应参数
      </div>
      <a-table :defaultExpandAllRows="expanRows" :columns="responseParametersColumns" :dataSource="multipData.data"
        rowKey="id" size="small" :pagination="page">
        <template slot="descriptionTemplate" slot-scope="text">
          <span v-html="text"></span>
        </template>
      </a-table>
      <div class="api-title" v-html="$t('doc.responseExample')">
        响应示例
      </div>
      <a-row :id="'knife4jDocumentShowEditor' + api.id">
        <editor-show @showDescription="showResponseEditFieldDescription" :value="multipData.responseBasicType
          ? multipData.responseText
          : multipData.responseValue
          "></editor-show>

      </a-row>
    </div>
  </div>
</template>
<script>
import KUtils from "@/core/utils";
import Constants from "@/store/constants";
import markdownSingleText from "@/components/officeDocument/markdownSingleTransform";
import markdownSingleTextUs from "@/components/officeDocument/markdownSingleTransformUS";
/* import DataType from "./DataType";
import EditorShow from "./EditorShow"; */
import ClipboardJS from "clipboard";
import uniqueId from "lodash/uniqueId";
import isObject from 'lodash/isObject'
import has from 'lodash/has'
import keys from 'lodash/keys'
import cloneDeep from 'lodash/cloneDeep'

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
  data() {
    return {
      content: "<span>Hello</span>",
      contentType: "*/*",//  请求数据类型
      columns: [],
      responseHeaderColumns: [],
      responseStatuscolumns: [],
      responseParametersColumns: [],
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
    var that = this;
    // console.log("Document")
    // console.log(this.api);
    var key = Constants.globalTreeTableModelParams + this.swaggerInstance.id;
    var treeTableModel = this.swaggerInstance.swaggerTreeTableModels;
    this.$Knife4jModels.setValue(key, treeTableModel);
    this.initI18n();
    this.initRequestParams();
    this.initResponseCodeParams();
    setTimeout(() => {
      that.copyApiAddress();
      that.copyApiMarkdown();
      that.copyApiUrl();
      // console.log("status", this.responseCodeDisplayStatus)
    }, 1500);
  },
  computed: {
    language() {
      return this.$store.state.globals.language;
    },
    swagger() {
      return this.$store.state.globals.swagger;
    },
    responseCodeDisplayStatus() {
      return this.$store.state.globals.enableResponseCode;
    }
  },
  watch: {
    language: function (val, oldval) {
      this.initI18n();
    }
  },
  methods: {
    getCurrentI18nInstance() {
      return this.$i18n.messages[this.language];
    },
    initI18n() {
      // 根据i18n初始化部分参数
      var inst = this.getCurrentI18nInstance();
      this.columns = inst.table.documentRequestColumns;
      this.responseStatuscolumns = inst.table.documentResponseStatusColumns;
      this.responseHeaderColumns = inst.table.documentResponseHeaderColumns;
      this.responseParametersColumns = inst.table.documentResponseColumns;
    },
    copyApiUrl() {
      var that = this;
      var btnId = "btnCopyMethod" + this.api.id;
      var copyMethodText = this.api.showUrl;
      var clipboard = new ClipboardJS("#" + btnId, {
        text() {
          return copyMethodText;
        }
      });

      clipboard.on("success", () => {
        var inst = that.getCurrentI18nInstance();
        // "复制地址成功"
        var successMessage = inst.message.copy.method.success;
        that.$message.info(successMessage);
      })
      clipboard.on("error", function (e) {
        var inst = that.getCurrentI18nInstance();
        console.log(inst)
        // "复制地址失败"
        var failMessage = inst.message.copy.method.fail;
        that.$message.info(failMessage);
      });
    },
    copyApiAddress() {
      var that = this;
      var btnId = "btnCopyAddress" + this.api.id;
      var clipboard = new ClipboardJS("#" + btnId, {
        text() {
          return window.location.href;
        }
      });


      clipboard.on("success", function (e) {
        var inst = that.getCurrentI18nInstance();
        // "复制地址成功"
        var successMessage = inst.message.copy.url.success;
        that.$message.info(successMessage);
      });
      clipboard.on("error", function (e) {
        var inst = that.getCurrentI18nInstance();
        // "复制地址失败"
        var failMessage = inst.message.copy.url.fail;
        that.$message.info(failMessage);
      });
    },
    copyApiMarkdown() {
      var that = this;
      var btnId = "btnCopyMarkdown" + this.api.id;
      var api = {
        ...that.api,
        reqParameters: that.reqParameters,
        multipCodeDatas: that.multipCodeDatas,
        multipData: that.multipData
      };
      // console.log(api);
      var clipboard = new ClipboardJS("#" + btnId, {
        text() {
          var inst = that.getCurrentI18nInstance();
          if (inst.lang === 'zh') {
            return markdownSingleText(api);
          } else if (inst.lang === 'us') {
            return markdownSingleTextUs(api);
          }
        }
      });
      clipboard.on("success", function (e) {
        var inst = that.getCurrentI18nInstance();
        // "复制文档成功"
        var successMessage = inst.message.copy.document.success;
        that.$message.info(successMessage);
      });
      clipboard.on("error", function (e) {
        var inst = that.getCurrentI18nInstance();
        // "复制文档失败"
        var failMessage = inst.message.copy.document.fail;
        that.$message.info(failMessage);
      });
    },
    /**
     * 递归剔除请求参数表格忽略字段
     * @param keys ['a.b.c', 'a']
     * @param childrens []
     * @param parent ''
     */
    filterChildrens(keys = [], childrens = [], parent) {
      if (keys.length === 0) return childrens;
      const that = this;
      const arrs = parent
        ? childrens.filter(child => !keys.includes(`${parent}.${child.name}`))
        : childrens.filter(child => !keys.includes(child.name));
      return arrs.map(child => {
        child.id = uniqueId("param"); //  这里顺带重置一下 id , 避免与相应参数对象服用时组件 id 相同报错
        if (child.children)
          child.children = that.filterChildrens(
            keys,
            child.children,
            child.name
          );
        return child;
      });
    },
    initRequestParams() {
      var key = Constants.globalTreeTableModelParams + this.swaggerInstance.id;
      var data = [];
      var that = this;
      var apiInfo = this.api;
      if (KUtils.strNotBlank(apiInfo.contentType)) {
        this.contentType = apiInfo.contentType;
      }
      if (apiInfo.contentType == "application/x-www-form-urlencoded;charset=UTF-8") {
        this.contentType = "application/x-www-form-urlencoded";
      }
      // console.log(apiInfo);
      // 针对数组类型的ignore写法,在这里不需要,table树里面是对象点属性
      // 忽略数组的写法 name[0]
      var tmpKeys = Object.keys(apiInfo.ignoreParameters || {});
      var ignoreParameterAllKeys = [];
      var reg = new RegExp("\\[0\\]", "gm");
      if (tmpKeys != null && tmpKeys.length > 0) {
        tmpKeys.forEach(tk => {
          ignoreParameterAllKeys.push(tk);
          if (tk.indexOf("[0]") > -1) {
            ignoreParameterAllKeys.push(tk.replace(reg, ""));
          }
        });
      }
      /*  const ignoreParameterAllKeys = Object.keys(
        apiInfo.ignoreParameters || {}
      ); */
      if (apiInfo.parameters != null && apiInfo.parameters.length > 0) {
        var dx = apiInfo.parameters.filter(function (pm) {
          if (pm.name.indexOf("[0]") > -1) {
            // 存在数组的情况
            if (ignoreParameterAllKeys.length > 0) {
              return (
                ignoreParameterAllKeys.filter(name => !pm.name.startsWith(name))
                  .length > 0
              );
            } else {
              return true;
            }
          } else {
            return !ignoreParameterAllKeys.includes(pm.name);
          }
        });
        data = data.concat(dx);
        /*  data = data.concat(
          apiInfo.parameters
            //  过滤掉忽略参数
            .filter(
              ({ name }) =>
                !ignoreParameterAllKeys.includes(name) 
            )
        ); */
        // console.log(data);
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
        // console.log("初始化请求参数:", JSON.parse(JSON.stringify(data)));
        data.forEach(function (param) {
          if (param.pid == "-1") {
            param.children = null;
            // 判断该参数是否存在schema参数
            if (param.schema) {
              // 判断当前缓存是否存在
              var schemaName = param.schemaValue;
              // console.log("param:", param);
              if (KUtils.checkUndefined(schemaName)) {
                // console.log("key:", key);
                // console.log("schemaName:", schemaName);
                // console.log("that.$Knife4jModels.exists:", that.$Knife4jModels.exists(key, schemaName));
                if (that.$Knife4jModels.exists(key, schemaName)) {
                  var model = that.$Knife4jModels.getByModelName(key, schemaName);
                  // console.log("当前model:", JSON.parse(JSON.stringify(model)));
                  model = that.swagger.analysisDefinitionRefTableModel(that.swaggerInstance.id, model);
                  // console.log("当前model:", JSON.parse(JSON.stringify(model)));
                  if (model && model.params) {
                    const childrens = model.params
                      .filter(({ name }) => {
                        // 过滤第一层忽略的参数
                        return !(
                          (
                            ignoreParameterAllKeys.includes(name) || //  处理 form 表单提交
                            ignoreParameterAllKeys.includes(name + "[0]") ||
                            ignoreParameterAllKeys.includes(
                              `${param.name}.${name}`
                            ) ||
                            ignoreParameterAllKeys.some(key =>
                              new RegExp(`^(${key}$|${key}[.[])`).test(name) || eval('/' + key + '/g').test(name)
                            )
                          ) //  处理 json 提交
                        );
                      })
                      .map(swaggerBootstrapUiParameter => {
                        const newObj = that.copyNewParameter(
                          swaggerBootstrapUiParameter
                        );
                        newObj.pid = param.id;
                        if (newObj.children) {
                          // 递归过滤更深层次忽略的属性
                          const childrens = JSON.parse(
                            JSON.stringify(newObj.children)
                          ); // 深拷贝原始集合
                          const currentIgnores = ignoreParameterAllKeys
                            .map(key => {
                              if (
                                key.startsWith(`${param.name}.${newObj.name}.`)
                              ) {
                                return key.replace(
                                  `${param.name}.${newObj.name}.`,
                                  ""
                                );
                              } else if (key.startsWith(`${newObj.name}.`)) {
                                return key.replace(`${newObj.name}.`, "");
                              }
                              return null;
                            })
                            .filter(Boolean);
                          newObj.children = that.filterChildrens(
                            currentIgnores,
                            childrens
                          );
                        }
                        return newObj;
                      });
                    param.children = childrens.length > 0 ? childrens : null;
                  }
                }
              }
            }
            reqParameters.push(param);
          }
        });
        // console.log("当前reqParameters:", JSON.parse(JSON.stringify(reqParameters)));
      }
      // 此处需要递归去除include之外的parameters
      if (apiInfo.includeParameters != null) {
        var tmpIncludeKeys = Object.keys(apiInfo.includeParameters || {});
        var bodyParam = reqParameters.filter(req => req.in == "body").length;
        if (tmpIncludeKeys.length > 0 && bodyParam > 0) {
          var includeParameters = [];
          // rootkey代表的JSON的父级path,父级path必须保留
          var rootKeys = [];
          this.deepRootKeys(tmpIncludeKeys, rootKeys);
          // console.log(rootKeys)
          // console.log(tmpIncludeKeys)
          reqParameters.forEach(param => {
            // 判断是否有childrens
            if (rootKeys.includes(param.name)) {
              var copyParam = cloneDeep(param);
              copyParam.children = null;
              if (param.children != null && param.children.length > 0) {
                copyParam.children = new Array();
                this.deepIncludeParam(copyParam.name, copyParam, param.children, tmpIncludeKeys, rootKeys);
              }
              includeParameters.push(copyParam);
            } else {
              if (tmpIncludeKeys.includes(param.name)) {
                var copyParam = cloneDeep(param);
                copyParam.children = null;
                if (param.children != null && param.children.length > 0) {
                  copyParam.children = new Array();
                  this.deepIncludeParam(copyParam.name, copyParam, param.children, tmpIncludeKeys, rootKeys);
                }
                includeParameters.push(copyParam);
              }
            }
          })
          that.reqParameters = includeParameters;
        } else {
          that.reqParameters = reqParameters;
        }
      } else {
        that.reqParameters = reqParameters;
      }
      // console.log("当前reqParameters:", JSON.parse(JSON.stringify(reqParameters)));
    },
    deepRootKeys(tmpIncludeKeys, rootKeys) {
      var tmpRooks = [];
      tmpIncludeKeys.forEach(key => {
        var rootKey = key.substring(0, key.lastIndexOf("."));
        if (rootKey.indexOf(".") > -1) {
          tmpRooks.push(rootKey);
        }
        if (!rootKeys.includes(rootKey)) {
          rootKeys.push(rootKey);
        }
      })
      if (tmpRooks.length > 0) {
        this.deepRootKeys(tmpRooks, rootKeys);
      }
    },
    deepIncludeParam(parentName, deepParams, children, tmpIncludeKeys, rootKeys) {
      if (children != null && children.length > 0) {
        children.forEach(childrenParam => {
          var jsonPath = parentName + "." + childrenParam.name;
          // 判断root
          if (rootKeys.includes(jsonPath)) {
            var copyParam = cloneDeep(childrenParam);
            // 初始化children需要判断当前的param.name是否在includes中
            copyParam.children = null;
            deepParams.children.push(copyParam)
            if (KUtils.arrNotEmpty(childrenParam.children)) {
              copyParam.children = new Array();
              this.deepIncludeParam(jsonPath, copyParam, childrenParam.children, tmpIncludeKeys, rootKeys);
            }
          } else {
            if (tmpIncludeKeys.includes(jsonPath)) {
              deepParams.children.push(childrenParam)
            }
          }
        })
      }
    },
    copyNewParameter(source) {
      const renewId = arrs => {
        if (!arrs) {
          return null;
        }
        return arrs.map(row => {
          row.id = uniqueId("param");
          renewId(row.children);
        });
      };
      //  拷贝原始对象
      const target = Object.assign({}, source);
      //  这里需要重新生成新的 id, 否则对象被重复引用时参数 id 是相同的,造成组件出现重复 key 引起页面报错
      target.id = uniqueId("param");
      renewId(target.children);

      return target;
    },
    deepTreeTableSchemaModel(param, treeTableModel, rootParam) {
      var that = this;
      ////console(model.name)
      if (KUtils.checkUndefined(param.schemaValue)) {
        var schema = treeTableModel[param.schemaValue];
        if (KUtils.checkUndefined(schema)) {
          rootParam.parentTypes.push(param.schemaValue);
          if (KUtils.arrNotEmpty(schema.params)) {
            schema.params.forEach(function (nmd) {
              // childrenparam需要深拷贝一个对象
              var childrenParam = {
                childrenTypes: nmd.childrenTypes,
                def: nmd.def,
                description: nmd.description,
                enum: nmd.enum,
                example: nmd.example,
                id: nmd.id,
                ignoreFilterName: nmd.ignoreFilterName,
                in: nmd.in,
                level: nmd.level,
                name: nmd.name,
                parentTypes: nmd.parentTypes,
                pid: nmd.pid,
                readOnly: nmd.readOnly,
                require: nmd.require,
                schema: nmd.schema,
                schemaValue: nmd.schemaValue,
                show: nmd.show,
                txtValue: nmd.txtValue,
                type: nmd.type,
                validateInstance: nmd.validateInstance,
                validateStatus: nmd.validateStatus,
                value: nmd.value
              };
              childrenParam.pid = param.id;
              param.children.push(childrenParam);
              if (childrenParam.schema) {
                // 存在schema,判断是否出现过
                if (
                  rootParam.parentTypes.indexOf(childrenParam.schemaValue) == -1
                ) {
                  childrenParam.children = [];
                  that.deepTreeTableSchemaModel(
                    childrenParam,
                    treeTableModel,
                    rootParam
                  );
                  if (childrenParam.children.length == 0) {
                    childrenParam.children = null;
                  }
                }
              }
            });
          }
        }
      }
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
      // 遍历响应参数
      var that = this;
      var key = Constants.globalTreeTableModelParams + this.swaggerInstance.id;
      // 添加自定义属性
      that.multipCode = this.api.multipartResponseSchema;
      that.multipCodeDatas = [];
      // 这里不
      that.multipData = {};
      let rcodes = this.api.responseCodes;
      //console.log("rcodes")
      //console.log(rcodes)
      if (rcodes != null && rcodes != undefined) {
        for (let i = 0; i < rcodes.length; i++) {
          let rc = rcodes[i];

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
              respdata.forEach(function (param) {
                if (param.pid == "-1") {
                  param.children = [];
                  // 判断该参数是否存在schema参数
                  if (param.schema) {
                    // 判断当前缓存是否存在
                    var schemaName = param.schemaValue;
                    //  console.log("schemaName:"+schemaName)
                    if (KUtils.checkUndefined(schemaName)) {
                      //  //console("schemaValue--checkUndefined");
                      if (that.$Knife4jModels.exists(key, schemaName)) {
                        // //console("存在-不用查找---" + schemaName);
                        // //console(that.$Knife4jModels.instance);
                        var model = that.$Knife4jModels.getByModelName(
                          key,
                          schemaName
                        );
                        model = that.swagger.analysisDefinitionRefTableModel(that.swaggerInstance.id, model);
                        if (!KUtils.checkUndefined(param.description)) {
                          //如果参数已经有description，那么就不赋值，否则，取model的description
                          if (KUtils.checkUndefined(model.description)) {
                            param.description = model.description;
                          }
                        }

                        //console.log('params-model', model)
                        if (model && model.params) {
                          param.children = model.params.map(child => {
                            const newObj = that.copyNewParameter(child);
                            newObj.pid = param.id;
                            return newObj;
                          });
                        }
                      } else {
                        //console.log("schemavalue--Not Existis,",schemaName)
                        //非ref类型，在当前自己的params在过滤一边
                        let childrenParamArray = respdata.filter(childrenParam => childrenParam.pid == param.id);
                        //console.log(childrenParamArray)
                        if (KUtils.checkUndefined(childrenParamArray)) {
                          param.children = childrenParamArray.map(cd => {
                            const newObj = that.copyNewParameter(cd);
                            newObj.pid = param.id;
                            return newObj;
                          })
                        }
                        // //console("schemavalue--Not Existis");
                      }
                    }
                  }

                  // that.findModelChildren(md, respdata);
                  // 查找后如果没有,则将children置空
                  if (param.children.length == 0) {
                    param.children = null;
                  }
                  nrecodedatas.push(param);
                }
              });
            }
            var nresobj = { ...rc, data: nrecodedatas };
            if (!that.multipCode) {
              that.multipData = nresobj;
            }
            that.multipCodeDatas.push(nresobj);
          } else {
            // 只获取第一个
            // https://gitee.com/xiaoym/knife4j/issues/I5W145
            if (i == 0) {
              //不存在schema，直接赋值
              that.multipData = rc;
            }

          }
        }
        var multipKeys = Object.keys(that.multipData);
        if (KUtils.arrNotEmpty(rcodes) && !KUtils.arrNotEmpty(multipKeys)) {
          var rc = rcodes[0];
          if (KUtils.strNotBlank(rc.schemaTitle)) {
            var nresobj = { ...rc, data: [] };
            that.multipData = nresobj;
          }
        }
      }
      //console.log(that.multipData);
    },
    showResponseEditFieldDescription(p) {
      // 显示说明
      // console.log("接收emit事件,数据："+p);
      var that = this;
      if (this.api.multipartResponseSchema) {
        // 多个
        // 默认只显示第1个
        var resp = this.multipCodeDatas[0];
        var id = "knife4jDocumentShowEditor" + that.api.id + resp.code;
        // console("editorShowID:" + id);
        that.showEditorFieldAnyWay(id);
        /* this.multipCodeDatas.forEach(function(resp) {
          var id = "knife4jDocumentShowEditor" + that.api.id + resp.code;
          // console("editorShowID:" + id);
          that.showEditorFieldAnyWay(id);
        }); */
      } else {
        // 单个
        var id = "knife4jDocumentShowEditor" + this.api.id;
        this.showEditorFieldAnyWay(id);
      }
    },
    multipartTabCodeChanges(activeKey) {
      var that = this;
      setTimeout(() => {
        var id = "knife4jDocumentShowEditor" + that.api.id + activeKey;
        that.showEditorFieldAnyWay(id);
      }, 1000);
    },
    showEditorFieldAnyWay(containerId) {
      var swaggerInstance = this.swaggerInstance;
      var responseCode = this.api.getHttpSuccessCodeObject();
      var editorContainer = document.getElementById(containerId);
      var paths = [];
      // var aceJsonText = $aceJsonContent.find(".ace_text-layer");
      var aceJsonText = editorContainer.getElementsByClassName(
        "ace_text-layer"
      );
      var acePrintMarginLeft = editorContainer.querySelector(
        ".ace_print-margin"
      ).style.left;
      if (aceJsonText.length > 0) {
        var aceLineDoms = aceJsonText[0].getElementsByClassName("ace_line");
        for (var i = 0; i < aceLineDoms.length; i++) {
          var item = aceLineDoms[i];
          var $variable = item.getElementsByClassName("ace_variable");
          var key = null;
          if (KUtils.arrNotEmpty($variable)) {
            key = KUtils.toString($variable[0].innerHTML, "").replace(
              /^"(.*)"$/g,
              "$1"
            );
            // 判断是否存在
            var sfd = item.getElementsByClassName(
              "knife4j-debug-editor-field-description"
            );
            if (!KUtils.arrNotEmpty(sfd) && responseCode != null) {
              var fieldSpan = document.createElement("span");
              fieldSpan.className = "knife4j-debug-editor-field-description";
              fieldSpan.innerHTML = responseCode.responseDescriptionFind(
                paths,
                key,
                swaggerInstance
              );
              fieldSpan.style.left = acePrintMarginLeft;
              item.appendChild(fieldSpan);
            }
          }
          var itemParen = item.getElementsByClassName("ace_paren");
          if (KUtils.arrNotEmpty(itemParen)) {
            // 如果元素大于0,拼装多个
            var parentArrs = [];
            for (var e = 0; e < itemParen.length; e++) {
              parentArrs.push(itemParen[e].innerHTML);
            }
            // var parentText = itemParen[0].innerHTML;
            var parentText = parentArrs.join("");
            switch (parentText) {
              case "[":
              case "{":
                paths.push(key ? key : 0);
                break;
              case "}":
              case "]":
                paths.pop();
                break;
            }
          }
        }
      }
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
