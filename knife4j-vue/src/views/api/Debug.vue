<template>
  <div class="knife4j-debug">
    <a-row>
      <a-col :class="'knife4j-debug-api-' + api.methodType.toLowerCase()" :span="24">
        <a-input-group compact>
          <span class="knife4j-api-summary-method">{{ api.methodType }}</span>
          <a-input style="width: 80%" :value="debugUrl" />
          <a-button class="knife4j-api-send" type="primary" @click="sendRestfulApi">发 送</a-button>
        </a-input-group>
      </a-col>
    </a-row>
    <a-row class="knife4j-debug-tabs">
      <a-tabs defaultActiveKey="2">
        <a-tab-pane key="1">
          <template slot="tab">
            <span>
              <a-tag v-if="headerCountFlag" class="knife4j-debug-param-count">{{headerCount}}</a-tag>请求头部
            </span>
          </template>
          <a-table bordered size="small" :rowSelection="rowSelection" :columns="headerColumn" :pagination="pagination" :dataSource="headerData" rowKey="id">
            <!--请求头下拉框-->
            <template slot="headerName" slot-scope="text,record">
              <!-- <a-select showSearch :options="headerOptions" placeholder="输入请求头" optionFilterProp="children" style="width: 100%">
              </a-select> -->
              <a-auto-complete @select="headerSelect" @search="headerSearch" @change="headerNameChange(record)" :value="text" :filterOption="headerNameFilterOption" :allowClear="allowClear" :dataSource="headerAutoOptions" style="width: 100%" placeholder="请求头名称" />
            </template>
            <template slot="headerValue" slot-scope="text,record">
              <a-input placeholder="请求头内容" :data-key="record.id" :defaultValue="text" @change="headerContentChnage" />
            </template>
            <a-row slot="operation" slot-scope="text,record">
              <a-button type="link" v-if="!record.new" @click="headerDelete(record)">删除</a-button>
            </a-row>
          </a-table>
        </a-tab-pane>
        <a-tab-pane tab="请求参数" key="2" forceRender>
          <a-row class="knife4j-debug-request-type">
            <div class="knife4j-debug-request-content-type-float">
              <a-radio-group @change="requestContentTypeChange" class="knife4j-debug-request-content-type" v-model="requestContentType">
                <a-radio value="x-www-form-urlencoded">x-www-form-urlencoded</a-radio>
                <a-radio value="form-data">form-data</a-radio>
                <a-radio value="raw">raw</a-radio>
              </a-radio-group>
            </div>
            <div class="knife4j-debug-request-content-type-float">
              <div class="knife4j-debug-request-content-type-raw">
                <a-dropdown v-if="rawTypeFlag" :trigger="['click']">
                  <span class="knife4j-debug-raw-span"> <span>{{rawDefaultText}}</span>
                    <a-icon type="down" /> </span>
                  <a-menu slot="overlay" @click="rawMenuClick">
                    <a-menu-item data-mode="text" key="Auto">Auto</a-menu-item>
                    <a-menu-item data-mode="text" key="Text(text/plain)">Text(text/plain)</a-menu-item>
                    <a-menu-item data-mode="json" key="JSON(application/json)">JSON(application/json)</a-menu-item>
                    <a-menu-item data-mode="javascript" key="Javascript(application/Javascript)">Javascript(application/Javascript)</a-menu-item>
                    <a-menu-item data-mode="xml" key="XML(application/xml)">XML(application/xml)</a-menu-item>
                    <a-menu-item data-mode="xml" key="XML(text/xml)">XML(text/xml)</a-menu-item>
                    <a-menu-item data-mode="html" key="HTML(text/html)">HTML(text/html)</a-menu-item>
                  </a-menu>
                </a-dropdown>
              </div>
            </div>
          </a-row>
          <a-row v-if="formFlag">
            <a-table bordered size="small" :rowSelection="rowFormSelection" :columns="formColumn" :pagination="pagination" :dataSource="formData" rowKey="id">
              <!--参数名称-->
              <template slot="formName" slot-scope="text,record">
                <a-input placeholder="参数名称" :data-key="record.id" :defaultValue="text" @change="formNameChange" />
              </template>
              <!--参数下拉框-->
              <template slot="formType" slot-scope="text,record">
                <a-select :defaultValue="text+'-'+record.id" @change="formTypeChange" style="width: 100%;">
                  <a-select-option :value="'text-'+record.id">文本</a-select-option>
                  <a-select-option :value="'file-'+record.id">文件</a-select-option>
                </a-select>
              </template>
              <!--参数名称-->
              <template slot="formValue" slot-scope="text,record">
                <div v-if="record.type=='text'">
                  <a-input placeholder="参数值" :data-key="record.id" :defaultValue="text" @change="formContentChange" />
                </div>
                <div v-else>
                  <!-- <input type="file" :data-key="record.id" @change="formFileChange" /> -->
                  <div>
                    <input :id="'file'+record.id" style="display:none;" type="file" :data-key="record.id" @change="formFileChange" />
                    <a-input-group compact>
                      <a-input style="width: 82%" :value="record.content" disabled />
                      <a-button @click="formFileUploadClick(record)" class="knife4j-api-send" style="width:80px;" type="primary">选择文件</a-button>
                    </a-input-group>
                  </div>
                </div>
              </template>
              <a-row slot="operation" slot-scope="text,record">
                <a-button type="link" v-if="!record.new" @click="formDelete(record)">删除</a-button>
              </a-row>
            </a-table>
          </a-row>
          <a-row v-if="urlFormFlag">
            <a-table bordered size="small" :rowSelection="rowUrlFormSelection" :columns="urlFormColumn" :pagination="pagination" :dataSource="urlFormData" rowKey="id">
              <!--参数名称-->
              <template slot="urlFormName" slot-scope="text,record">
                <a-input placeholder="参数名称" :data-key="record.id" :defaultValue="text" @change="urlFormNameChange" />
              </template>

              <!--参数名称-->
              <template slot="urlFormValue" slot-scope="text,record">
                <a-input placeholder="参数值" :data-key="record.id" :defaultValue="text" @change="urlFormContentChange" />
              </template>
              <a-row slot="operation" slot-scope="text,record">
                <a-button type="link" v-if="!record.new" @click="urlFormDelete(record)">删除</a-button>
              </a-row>
            </a-table>
          </a-row>
          <a-row v-if="rawFlag">
            <editor-debug-show :value="rawText" :mode="rawMode" @change="rawChange"></editor-debug-show>
          </a-row>
        </a-tab-pane>
      </a-tabs>
    </a-row>
  </div>
</template>
<script>
import md5 from "js-md5";
import KUtils from "@/core/utils";
import constant from "@/store/constants";
import EditorDebugShow from "./EditorDebugShow";
var instance;
export default {
  name: "Debug",
  components: { EditorDebugShow },
  props: {
    api: {
      type: Object,
      required: true
    }
  },
  beforeCreate() {
    instance = this;
  },
  data() {
    return {
      headerColumn: constant.debugRequestHeaderColumn,
      formColumn: constant.debugFormRequestHeader,
      urlFormColumn: constant.debugUrlFormRequestHeader,
      allowClear: true,
      pagination: false,
      headerAutoOptions: constant.debugRequestHeaders,
      headerOptions: constant.debugRequestHeaderOptions,
      headerCount: 0,
      headerCountFlag: false,
      headerSelectName: "",
      selectedRowKeys: [],
      //是否允许有请求参数,一般get情况下直接屏蔽
      requestParameterAllow: true,
      rowSelection: {
        selectedRowKeys: [],
        onChange(selectrowkey, selectrows) {
          instance.rowSelection.selectedRowKeys = selectrowkey;
        }
      },
      rowFormSelection: {
        selectedRowKeys: [],
        onChange(selectrowkey, selectrows) {
          instance.rowFormSelection.selectedRowKeys = selectrowkey;
        }
      },
      rowUrlFormSelection: {
        selectedRowKeys: [],
        onChange(selectrowkey, selectrows) {
          instance.rowFormSelection.selectedRowKeys = selectrowkey;
        }
      },
      headerData: [],
      //本地缓存全局参数
      globalParameters: [],
      //调试接口
      debugUrl: "",
      //form参数值对象
      formData: [],
      formFlag: false,
      urlFormData: [],
      urlFormFlag: false,
      rawDefaultText: "Auto",
      rawFlag: false,
      rawTypeFlag: false,
      rawText: "",
      rawMode: "text",
      requestContentType: "x-www-form-urlencoded"
    };
  },
  created() {
    //初始化读取本地缓存全局参数
    this.initLocalGlobalParameters();
    this.initDebugUrl();
    //form-data表单
    this.initFirstFormValue();
    this.initSelectionHeaders();
    //显示表单参数
    //this.initShowFormTable();
  },
  methods: {
    initDebugUrl() {
      this.debugUrl = this.api.url;
    },
    initLocalGlobalParameters() {
      const key = this.api.instanceId;
      //初始化读取本地缓存全局参数
      this.$localStore.getItem(constant.globalParameter).then(function(val) {
        if (val != null) {
          if (val[key] != undefined && val[key] != null) {
            instance.globalParameters = val[key];
          }
        }
        //开始同步执行其他方法-初始化请求头参数
        instance.initHeaderParameter();
        //请求体参数初始化
        instance.initBodyParameter();
      });
    },
    initHeaderParameter() {
      //本都缓存读取到参数，初始化header参数
      instance.globalParameters.forEach(function(param) {
        if (param.in == "header") {
          var newHeader = {
            id: md5(
              new Date().getTime().toString() +
                Math.floor(Math.random() * 10000).toString()
            ),
            name: param.name,
            content: param.value,
            new: false
          };
          instance.headerData.push(newHeader);
        }
      });
      this.readApiHeader();
      this.initFirstHeader();
      //计算heaer数量
      this.headerResetCalc();
    },
    initBodyParameter() {
      //this.initBodyType();
      //初始化请求体参数
      //得到body类型的请求参数
      var bodyParameters = instance.globalParameters.filter(
        param => param.in != "header"
      );
      var bodyData = [];
      //接口本身的参数对象
      var tmpApiParameters = this.api.parameters;
      //本身全局参数显示集合
      var showGlobalParameters = [];
      //本身接口api参数显示集合
      var showApiParameters = [];
      //是否存在全局参数
      if (bodyParameters.length > 0) {
        //存在，判断全局参数中和parameter对比，是否存在相同参数，如果存在，判断是否parameters参数有值，如果后端有值,则globalParams中的参数值不显示
        bodyParameters.forEach(function(global) {
          if (KUtils.arrNotEmpty(tmpApiParameters)) {
            var show = true;
            tmpApiParameters.forEach(function(param) {
              if (global.name == param.name && global.in == param.in) {
                //在全局参数中存在相同的参数
                //判断txtValue是否有值
                if (KUtils.strNotBlank(param.txtValue)) {
                  show = false;
                }
              }
            });
            //如果show=true，则显示该参数
            if (show) {
              showGlobalParameters.push(global);
            }
          }
        });
      }
      if (KUtils.arrNotEmpty(tmpApiParameters)) {
        tmpApiParameters.forEach(function(param) {
          if (KUtils.arrNotEmpty(bodyParameters)) {
            var show = true;
            bodyParameters.forEach(function(global) {
              if (global.name == param.name && global.in == param.in) {
                if (!KUtils.strNotBlank(param.txtValue)) {
                  show = false;
                }
              }
            });
            if (show) {
              showApiParameters.push(param);
            }
          } else {
            showApiParameters.push(param);
          }
        });
      }
      //根据参数列表、参数类型,开始自动判断接口的请求类型
      //如果是单个@RequestBody类型,则参数只有一个,且只有一个，类型必须是body类型
      var paramSize = showGlobalParameters.length + showApiParameters.length;
      console.log("参数大小:" + paramSize);
      if (KUtils.arrNotEmpty(showApiParameters)) {
        //判断参数是否为body类型
        var bodySize = showApiParameters.filter(param => param.in == "body")
          .length;
        if (bodySize == 1) {
          console.log("显示raw类型");
          //raw类型
          this.showTabRaw();
        } else {
          //判断是否包含文件
          var fileSize = showApiParameters.filter(
            param =>
              param.schemaValue == "MultipartFile" ||
              param.schemaValue == "file" ||
              param.type == "file"
          ).length;
          if (fileSize > 0) {
            //form-data
            this.showTabForm();
          } else {
            //url-form
            this.showTabUrlForm();
            instance.addApiParameterToUrlForm(showApiParameters);
            //url-form-data表单
            instance.initUrlFormValue();
          }
        }
      } else {
        //url-form类型
        this.showTabUrlForm();
        instance.addGlobalParameterToUrlForm(showGlobalParameters);
        instance.addApiParameterToUrlForm(showApiParameters);
        //url-form-data表单
        instance.initUrlFormValue();
      }
    },
    initBodyType() {
      //请求参数类型
      var contentValue = this.api.contentValue;
      this.requestContentType = contentValue;
      if (contentValue == "form-data") {
        this.formFlag = true;
        this.rawFlag = false;
        this.rawTypeFlag = false;
        this.urlFormFlag = false;
      } else if (contentValue == "x-www-form-urlencoded") {
        this.urlFormFlag = true;
        this.rawFlag = false;
        this.rawTypeFlag = false;
        this.formFlag = false;
      } else if (contentValue == "raw") {
        this.rawFlag = true;
        this.rawMode = this.api.contentMode;
        this.rawDefaultText = this.api.contentShowValue;
        this.rawTypeFlag = true;
        this.formFlag = false;
        this.urlFormFlag = false;
        //如果是raw类型，则赋值
        this.rawText = this.api.requestValue;
      }
    },
    readApiHeader() {
      //读取接口的请求头参数
      console.log("readheader--");
      console.log(this.api);
      //请求-请求头
      this.readConsumesHeader();

      //响应请求头
      var produces = this.api.produces;
      var produceHeader = "*/*";
      if (produces != undefined && produces != null && produces.length > 0) {
        produceHeader = produces[0];
      }
      var newHeader = {
        id: md5(
          new Date().getTime().toString() +
            Math.floor(Math.random() * 10000).toString()
        ),
        name: "Accept",
        content: produceHeader,
        new: false
      };
      this.headerData.push(newHeader);
    },
    readConsumesHeader() {
      //根据参数的请求方式自动确定请求头
      var consumes = this.api.consumes;
      var consumeHeader = "";
      if (consumes != undefined && consumes != null && consumes.length > 0) {
        consumeHeader = consumes[0];
      }
      var newHeader = {
        id: md5(
          new Date().getTime().toString() +
            Math.floor(Math.random() * 10000).toString()
        ),
        name: "Content-Type",
        content: consumeHeader,
        new: false
      };
      this.headerData.push(newHeader);
    },
    initFirstHeader() {
      var newHeader = {
        id: md5(
          new Date().getTime().toString() +
            Math.floor(Math.random() * 10000).toString()
        ),
        name: "",
        content: "",
        new: true
      };
      this.headerData.push(newHeader);
    },
    initFirstFormValue() {
      //添加一行初始form的值
      this.addNewLineFormValue();
      this.initFormSelections();
    },
    initFormSelections() {
      this.formData.forEach(function(form) {
        instance.rowFormSelection.selectedRowKeys.push(form.id);
      });
    },
    initUrlFormSelections() {
      this.urlFormData.forEach(function(form) {
        instance.rowUrlFormSelection.selectedRowKeys.push(form.id);
      });
    },
    showTabForm() {
      this.formFlag = true;
      this.rawFlag = false;
      this.rawTypeFlag = false;
      this.urlFormFlag = false;
    },
    showTabUrlForm() {
      this.urlFormFlag = true;
      this.rawFlag = false;
      this.rawTypeFlag = false;
      this.formFlag = false;
    },
    showTabRaw() {
      this.rawFlag = true;
      this.rawMode = this.api.contentMode;
      this.rawDefaultText = this.api.contentShowValue;
      this.rawTypeFlag = true;
      this.formFlag = false;
      this.urlFormFlag = false;
      //如果是raw类型，则赋值
      this.rawText = this.api.requestValue;
      this.requestContentType = "raw";
    },
    addNewLineFormValue() {
      //添加新行form表单值
      var newFormHeader = {
        id: md5(
          new Date().getTime().toString() +
            Math.floor(Math.random() * 10000).toString()
        ),
        name: "",
        type: "text",
        //文件表单域的target
        target: null,
        content: "",
        new: true
      };
      this.formData.push(newFormHeader);
    },
    addGlobalParameterToUrlForm(globalParameters) {
      if (KUtils.arrNotEmpty(globalParameters)) {
        globalParameters.forEach(function(global) {
          var newFormHeader = {
            id: md5(
              new Date().getTime().toString() +
                Math.floor(Math.random() * 10000).toString()
            ),
            name: global.name,
            type: "text",
            //文件表单域的target
            target: null,
            content: global.value,
            new: false
          };
          instance.urlFormData.push(newFormHeader);
        });
      }
    },
    addApiParameterToUrlForm(apiParameters) {
      if (KUtils.arrNotEmpty(apiParameters)) {
        apiParameters.forEach(function(param) {
          var newFormHeader = {
            id: md5(
              new Date().getTime().toString() +
                Math.floor(Math.random() * 10000).toString()
            ),
            name: param.name,
            type: "text",
            //文件表单域的target
            target: null,
            content: param.txtValue,
            new: false
          };
          instance.urlFormData.push(newFormHeader);
        });
      }
    },
    addNewLineUrlFormValue() {
      var newFormHeader = {
        id: md5(
          new Date().getTime().toString() +
            Math.floor(Math.random() * 10000).toString()
        ),
        name: "",
        type: "text",
        //文件表单域的target
        target: null,
        content: "",
        new: true
      };
      this.urlFormData.push(newFormHeader);
    },
    initUrlFormValue() {
      this.addNewLineUrlFormValue();
      this.initUrlFormSelections();
    },
    initShowFormTable() {
      if (this.requestContentType == "x-www-form-urlencoded") {
        this.urlFormFlag = true;
        this.formFlag = false;
        this.rawFlag = false;
        this.rawTypeFlag = false;
      } else if (this.requestContentType == "form-data") {
        this.formFlag = true;
        this.urlFormFlag = false;
        this.rawFlag = false;
        this.rawTypeFlag = false;
      } else if (this.requestContentType == "raw") {
        this.rawFlag = true;
        this.rawTypeFlag = true;
        this.urlFormFlag = false;
        this.formFlag = false;
      }
    },
    initSelectionHeaders() {
      this.headerData.forEach(function(header) {
        instance.rowSelection.selectedRowKeys.push(header.id);
      });
    },
    headerContentChnage(e) {
      var headerValue = e.target.value;
      var headerId = e.target.getAttribute("data-key");
      var record = this.headerData.filter(header => header.id == headerId)[0];
      if (record.new) {
        this.headerData.forEach(function(header) {
          if (header.id == record.id) {
            header.content = headerValue;
            header.new = false;
          }
        });
        //插入一行
        this.headerData.push({
          id: md5(new Date().getTime().toString()),
          name: "",
          content: "",
          new: true
        });
      } else {
        this.headerData.forEach(function(header) {
          if (header.id == record.id) {
            header.content = headerValue;
            header.new = false;
          }
        });
      }
      this.initSelectionHeaders();
      this.headerResetCalc();
    },
    /**
     * 请求头筛选事件
     */
    headerNameFilterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toUpperCase()
          .indexOf(input.toUpperCase()) >= 0
      );
    },
    headerSelect(value, option) {
      this.headerSelectName = value;
    },
    headerSearch(value) {
      this.headerSelectName = value;
    },
    headerNameChange(record) {
      //判断是否是new标志位,如果是new标志位,当前标志位置为false，重新生成一个new标志位的行
      if (record.new) {
        this.headerData.forEach(function(header) {
          if (header.id == record.id) {
            header.name = instance.headerSelectName;
            header.new = false;
          }
        });
        //插入一行
        this.headerData.push({
          id: md5(new Date().getTime().toString()),
          name: "",
          content: "",
          new: true
        });
      } else {
        this.headerData.forEach(function(header) {
          if (header.id == record.id) {
            header.name = instance.headerSelectName;
            header.new = false;
          }
        });
      }
      this.initSelectionHeaders();
      this.headerResetCalc();
    },
    headerDelete(record) {
      var nheader = [];
      this.headerData.forEach(function(header) {
        if (header.id != record.id) {
          nheader.push(header);
        }
      });
      this.headerData = nheader;
      this.headerResetCalc();
    },
    headerResetCalc() {
      //重新计算header请求头数量
      var noNewHeaderArrs = this.headerData.filter(
        header => header.new == false
      );
      if (noNewHeaderArrs.length > 0) {
        this.headerCountFlag = true;
        this.headerCount = noNewHeaderArrs.length;
      } else {
        this.headerCountFlag = false;
        this.headerCount = 0;
      }
    },
    requestContentTypeChange(e) {
      console.log("radio checked", e.target.value);
      this.requestContentType = e.target.value;
      this.initShowFormTable();
    },
    formDelete(record) {
      var nforms = [];
      this.formData.forEach(function(form) {
        if (form.id != record.id) {
          nforms.push(form);
        }
      });
      this.formData = nforms;
    },
    formFileUploadClick(record) {
      //触发file隐藏表单域的click事件
      document.getElementById("file" + record.id).click();
    },
    formNameChange(e) {
      var formValue = e.target.value;
      var formId = e.target.getAttribute("data-key");
      var record = this.formData.filter(form => form.id == formId)[0];
      if (record.new) {
        this.formData.forEach(function(form) {
          if (form.id == record.id) {
            form.name = formValue;
            form.new = false;
          }
        });
        this.addNewLineFormValue();
      } else {
        this.formData.forEach(function(form) {
          if (form.id == record.id) {
            form.name = formValue;
            form.new = false;
          }
        });
      }
      this.initFormSelections();
    },
    formTypeChange(value, option) {
      var arr = value.split("-");
      var formType = arr[0];
      var formId = arr[1];
      this.formData.forEach(function(form) {
        if (form.id == formId) {
          //选择表单类型后,表单值置空
          form.content = "";
          form.type = formType;
          //判断是否是文件类型，如果是文件类型，给定一个目标input-file域的target属性
        }
      });
    },
    formFileChange(e) {
      console.log("文件发生变化了");
      console.log(e);
      console.log(e.target.files);
      var target = e.target;
      var formId = target.getAttribute("data-key");
      var record = this.formData.filter(form => form.id == formId)[0];
      if (record.new) {
        this.formData.forEach(function(form) {
          if (form.id == record.id) {
            form.content = target.value;
            form.target = target;
            form.new = false;
          }
        });
        console.log(this.formData);
        this.addNewLineFormValue();
      } else {
        this.formData.forEach(function(form) {
          if (form.id == record.id) {
            form.content = target.value;
            form.target = target;
            form.new = false;
          }
        });
      }
      this.initFormSelections();
    },
    formContentChange(e) {
      var formValue = e.target.value;
      console.log("formcontent-value:" + formValue);
      var formId = e.target.getAttribute("data-key");
      var record = this.formData.filter(form => form.id == formId)[0];
      if (record.new) {
        this.formData.forEach(function(form) {
          if (form.id == record.id) {
            form.content = formValue;
            form.new = false;
          }
        });
        this.addNewLineFormValue();
      } else {
        this.formData.forEach(function(form) {
          if (form.id == record.id) {
            form.content = formValue;
            form.new = false;
          }
        });
      }
      this.initFormSelections();
    },
    urlFormDelete(record) {
      var nforms = [];
      this.urlFormData.forEach(function(form) {
        if (form.id != record.id) {
          nforms.push(form);
        }
      });
      this.urlFormData = nforms;
    },
    urlFormNameChange(e) {
      var formValue = e.target.value;
      var formId = e.target.getAttribute("data-key");
      var record = this.urlFormData.filter(form => form.id == formId)[0];
      if (record.new) {
        this.urlFormData.forEach(function(form) {
          if (form.id == record.id) {
            form.name = formValue;
            form.new = false;
          }
        });
        this.addNewLineUrlFormValue();
      } else {
        this.urlFormData.forEach(function(form) {
          if (form.id == record.id) {
            form.name = formValue;
            form.new = false;
          }
        });
      }
      this.initUrlFormSelections();
    },
    urlFormContentChange(e) {
      var formValue = e.target.value;
      var formId = e.target.getAttribute("data-key");
      var record = this.urlFormData.filter(form => form.id == formId)[0];
      if (record.new) {
        this.urlFormData.forEach(function(form) {
          if (form.id == record.id) {
            form.content = formValue;
            form.new = false;
          }
        });
        this.addNewLineUrlFormValue();
      } else {
        this.urlFormData.forEach(function(form) {
          if (form.id == record.id) {
            form.content = formValue;
            form.new = false;
          }
        });
      }
      this.initUrlFormSelections();
    },
    rawMenuClick({ item, key, keyPath }) {
      this.rawMode = item.$el.getAttribute("data-mode");
      this.rawDefaultText = key;
    },
    rawChange(value) {
      this.rawText = value;
    },
    sendRestfulApi(e) {
      e.preventDefault();
      console.log("发送接口");
      this.formData.forEach(function(form) {
        if (!form.new) {
          console.log(form);
          if (form.type == "file") {
            console.log(form.target.files);
          }
        }
      });
    }
  }
};
</script>
 