<template>
  <div class="knife4j-debug">
    <a-row>
      <a-col :class="'knife4j-debug-api-' + api.methodType.toLowerCase()" :span="24">
        <a-input-group compact>
          <span class="knife4j-api-summary-method">{{ api.methodType }}</span>
          <a-input style="width: 80%" defaultValue="/api/tes/showApi" />
          <a-button class="knife4j-api-send" type="primary" @click="sendRestfulApi">发 送</a-button>
        </a-input-group>
      </a-col>
    </a-row>
    <a-row class="knife4j-debug-tabs">
      <a-tabs defaultActiveKey="2">
        <a-tab-pane tab="请求头部" key="1">
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
      headerSelectName: "",
      selectedRowKeys: [],
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
    this.readApiHeader();
    this.initFirstHeader();
    //form-data表单
    this.initFirstFormValue();
    this.initSelectionHeaders();
    //url-form-data表单
    this.initUrlFormValue();
    //显示表单参数
    this.initShowFormTable();
  },
  methods: {
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
    },
    headerDelete(record) {
      var nheader = [];
      this.headerData.forEach(function(header) {
        if (header.id != record.id) {
          nheader.push(header);
        }
      });
      this.headerData = nheader;
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
 