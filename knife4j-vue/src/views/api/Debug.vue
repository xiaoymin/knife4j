<template>
  <div class="knife4j-debug">
    <a-spin tip="Loading..." :spinning="debugLoading">
      <div class="spin-content">
        <a-row>
          <a-col :class="'knife4j-debug-api-' + api.methodType.toLowerCase()" :span="24">
            <a-input-group compact>
              <span class="knife4j-api-summary-method">
                <a-icon v-if="api.securityFlag" style="font-size:16px;" type="unlock" /> {{ api.methodType }}
              </span>
              <a-input :style="debugUrlStyle" :value="debugUrl" @change="debugUrlChange" />
              <a-button v-html="$t('debug.send')" class="knife4j-api-send" type="primary" @click="sendRestfulApi">发 送
              </a-button>
              <a-button v-if="enableReloadCacheParameter" @click="reloadCacheParameter">刷新变量</a-button>
            </a-input-group>
          </a-col>
        </a-row>
        <a-row class="knife4j-debug-tabs">
          <a-tabs defaultActiveKey="2">
            <a-tab-pane key="1">
              <template slot="tab">
                <span>
                  <a-tag v-if="headerCountFlag" class="knife4j-debug-param-count">{{
                      headerCount
                  }}</a-tag><span v-html="$t('debug.headers')">请求头部</span>
                </span>
              </template>
              <a-table v-if="headerTableFlag" bordered size="small" :rowSelection="rowSelection" :columns="headerColumn"
                :pagination="pagination" :dataSource="headerData" rowKey="id">
                <!--请求头下拉框-->
                <template slot="headerName" slot-scope="text, record">
                  <!-- <a-select showSearch :options="headerOptions" placeholder="输入请求头" optionFilterProp="children" style="width: 100%">
                  </a-select> -->
                  <a-auto-complete @select="headerSelect" :data-id="record.id" @search="headerSearch"
                    @change="headerNameChange(record)" :value="text" :filterOption="headerNameFilterOption"
                    :allowClear="allowClear" :dataSource="headerAutoOptions" style="width: 100%"
                    :placeholder="$t('debug.tableHeader.holderName')" />
                </template>
                <template slot="headerValue" slot-scope="text, record">
                  <!--判断枚举类型-->
                  <a-row v-if="record.enums != null">
                    <!--不为空-->
                    <a-select :mode="record.enumsMode" :defaultValue="text" :data-key="record.id"
                      :options="record.enums" style="width: 100%" @change="headerContentEnumChnage">
                    </a-select>
                  </a-row>
                  <a-row v-else>
                    <a-input :placeholder="$t('debug.tableHeader.holderValue')"
                      :class="'knife4j-debug-param-require' + record.require" :data-key="record.id" :defaultValue="text"
                      @change="headerContentChnage" />
                  </a-row>
                </template>
                <a-row slot="operation" slot-scope="text, record">
                  <a-button v-html="$t('debug.tableHeader.holderDel')" type="link" v-if="!record.new"
                    @click="headerDelete(record)">删除</a-button>
                </a-row>
              </a-table>
            </a-tab-pane>
            <a-tab-pane :tab="$t('debug.params')" key="2" forceRender>
              <a-row class="knife4j-debug-request-type">
                <div class="knife4j-debug-request-content-type-float">
                  <a-radio-group @change="requestContentTypeChange" class="knife4j-debug-request-content-type"
                    v-model="requestContentType">
                    <a-radio value="x-www-form-urlencoded">x-www-form-urlencoded</a-radio>
                    <a-radio value="form-data">form-data</a-radio>
                    <a-radio value="raw">raw</a-radio>
                  </a-radio-group>
                </div>
                <div class="knife4j-debug-request-content-type-float">
                  <div class="knife4j-debug-request-content-type-raw">
                    <a-dropdown v-if="rawTypeFlag" :trigger="['click']">
                      <span class="knife4j-debug-raw-span">
                        <span>{{ rawDefaultText }}</span>
                        <a-icon type="down" />
                      </span>
                      <a-menu slot="overlay" @click="rawMenuClick">
                        <a-menu-item data-mode-type="application/json" data-mode="text" key="Auto">Auto</a-menu-item>
                        <a-menu-item data-mode-type="text/plain" data-mode="text" key="Text(text/plain)">
                          Text(text/plain)</a-menu-item>
                        <a-menu-item data-mode-type="application/json" data-mode="json" key="JSON(application/json)">
                          JSON(application/json)</a-menu-item>
                        <a-menu-item data-mode-type="application/javascript" data-mode="javascript"
                          key="Javascript(application/Javascript)">Javascript(application/Javascript)</a-menu-item>
                        <a-menu-item data-mode-type="application/xml" data-mode="xml" key="XML(application/xml)">
                          XML(application/xml)</a-menu-item>
                        <a-menu-item data-mode-type="text/xml" data-mode="xml" key="XML(text/xml)">XML(text/xml)
                        </a-menu-item>
                        <a-menu-item data-mode-type="text/html" data-mode="html" key="HTML(text/html)">HTML(text/html)
                        </a-menu-item>
                      </a-menu>
                    </a-dropdown>
                  </div>
                </div>
                <div v-if="formatFlag" class="knife4j-debug-request-content-type-beautify">
                  <a @click="beautifyJson">Beautify</a>
                </div>
              </a-row>
              <a-row v-if="formFlag">
                <a-table v-if="formTableFlag" bordered size="small" :rowSelection="rowFormSelection"
                  :columns="formColumn" :pagination="pagination" :dataSource="formData" rowKey="id">
                  <!--参数名称-->
                  <template slot="formName" slot-scope="text, record">
                    <a-input :placeholder="record.description" :data-key="record.id" :defaultValue="text"
                      @change="formNameChange" />
                  </template>
                  <!--参数下拉框-->
                  <template slot="formType" slot-scope="text, record">
                    <a-select :defaultValue="text + '-' + record.id" @change="formTypeChange" style="width: 100%;">
                      <a-select-option :value="'text-' + record.id"><span v-html="$t('debug.form.itemText')">文本</span>
                      </a-select-option>
                      <a-select-option :value="'file-' + record.id"><span v-html="$t('debug.form.itemFile')">文件</span>
                      </a-select-option>
                    </a-select>
                  </template>
                  <!--参数名称-->
                  <template slot="formValue" slot-scope="text, record">
                    <div v-if="record.type == 'text'">
                      <!--判断枚举类型-->
                      <a-row v-if="record.enums != null">
                        <!--不为空-->
                        <a-select :mode="record.enumsMode" :defaultValue="text" :data-key="record.id"
                          :options="record.enums" style="width: 100%" @change="formContentEnumChange">
                        </a-select>
                      </a-row>
                      <a-row v-else>
                        <a-input :placeholder="record.description"
                          :class="'knife4j-debug-param-require' + record.require" :data-key="record.id"
                          :defaultValue="text" @change="formContentChange" />
                      </a-row>
                    </div>
                    <div v-else>
                      <!-- <input type="file" :data-key="record.id" @change="formFileChange" /> -->
                      <div>
                        <div style="display:none;" v-if="record.multipart">
                          <input :id="'file' + record.id" multiple style="display:none;" type="file"
                            :data-key="record.id" @change="formFileChange" />
                        </div>
                        <div style="display:none;" v-else>
                          <input :id="'file' + record.id" style="display:none;" type="file" :data-key="record.id"
                            @change="formFileChange" />
                        </div>
                        <a-input-group compact>
                          <a-input style="width: 80%" :class="'knife4j-debug-param-require' + record.require"
                            :value="record.content" disabled />
                          <a-button v-html="$t('debug.form.upload')" @click="formFileUploadClick(record)"
                            class="knife4j-api-send" style="width:80px;" type="primary">选择文件</a-button>
                        </a-input-group>
                      </div>
                    </div>
                  </template>
                  <a-row slot="operation" slot-scope="text, record">
                    <a-button v-html="$t('debug.tableHeader.holderDel')" type="link" v-if="!record.new"
                      @click="formDelete(record)">删除</a-button>
                  </a-row>
                </a-table>
              </a-row>
              <a-row v-if="urlFormFlag">
                <a-table v-if="urlFormTableFlag" bordered size="small" :rowSelection="rowUrlFormSelection"
                  :columns="urlFormColumn" :pagination="pagination" :dataSource="urlFormData" rowKey="id">
                  <!--参数名称-->
                  <template slot="urlFormName" slot-scope="text, record">
                    <a-input :placeholder="record.description" :data-key="record.id" :defaultValue="text"
                      @change="urlFormNameChange" />
                  </template>

                  <!--参数名称-->
                  <template slot="urlFormValue" slot-scope="text, record">
                    <!--判断枚举类型-->
                    <a-row v-if="record.enums != null">
                      <!--不为空-->
                      <a-select :mode="record.enumsMode" :defaultValue="text" :data-key="record.id"
                        :options="record.enums" style="width: 100%" @change="urlFormContentEnumChange">
                      </a-select>
                    </a-row>
                    <a-row v-else>
                      <a-input :placeholder="record.description" :class="'knife4j-debug-param-require' + record.require"
                        :data-key="record.id" :defaultValue="text" @change="urlFormContentChange" />
                    </a-row>
                  </template>
                  <a-row slot="operation" slot-scope="text, record">
                    <a-button v-html="$t('debug.tableHeader.holderDel')" type="link" v-if="!record.new"
                      @click="urlFormDelete(record)">删除</a-button>
                  </a-row>
                </a-table>
              </a-row>
              <a-row v-if="rawFlag">
                <a-row v-if="rawFormFlag">
                  <!--如果存在raw类型的参数则显示该表格-->
                  <a-table v-if="rawFormTableFlag" bordered size="small" :rowSelection="rowRawFormSelection"
                    :columns="urlFormColumn" :pagination="pagination" :dataSource="rawFormData" rowKey="id">
                    <!--参数名称-->
                    <template slot="urlFormName" slot-scope="text, record">
                      <a-input :placeholder="record.description" :data-key="record.id" :defaultValue="text"
                        @change="rawFormNameChange" />
                    </template>

                    <!--参数名称-->
                    <template slot="urlFormValue" slot-scope="text, record">
                      <!--判断枚举类型-->
                      <a-row v-if="record.enums != null">
                        <!--不为空-->
                        <a-select :mode="record.enumsMode" :defaultValue="text" :data-key="record.id"
                          :options="record.enums" style="width: 100%" @change="rawFormContentEnumChange">
                        </a-select>
                      </a-row>
                      <a-row v-else>
                        <a-input :placeholder="record.description"
                          :class="'knife4j-debug-param-require' + record.require" :data-key="record.id"
                          :defaultValue="text" @change="rawFormContentChange" />
                      </a-row>
                    </template>
                    <a-row slot="operation" slot-scope="text, record">
                      <a-button v-html="$t('debug.tableHeader.holderDel')" type="link" v-if="!record.new"
                        @click="rawFormDelete(record)">删除</a-button>
                    </a-row>
                  </a-table>
                </a-row>
                <editor-debug-show style="margin-top:5px;" :value="rawText" :mode="rawMode" @change="rawChange">
                </editor-debug-show>
              </a-row>
            </a-tab-pane>
            <a-tab-pane v-if="enableAfterScript" key="3" tab="AfterScript">
              <a-row style="height:25px;line-height:25px;">
                关于AfterScript更详细的使用方法及介绍,请<a href="https://gitee.com/xiaoym/knife4j/wikis/AfterScript"
                  target="_blank">参考文档</a>
              </a-row>
              <a-row>
                <editor-script style="margin-top:5px;" :value="rawScript" @change="rawScriptChange"></editor-script>
              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-row>
        <a-row>
          <DebugResponse ref="childDebugResponse" :responseFieldDescriptionChecked="responseFieldDescriptionChecked"
            :swaggerInstance="swaggerInstance" :api="api"
            @debugShowFieldDescriptionChange="debugShowFieldDescriptionChange" @debugEditorChange="debugEditorChange"
            :debugSend="debugSend" :responseContent="responseContent" :responseCurlText="responseCurlText"
            :responseStatus="responseStatus" :responseRawText="responseRawText" :responseHeaders="responseHeaders" />
        </a-row>
      </div>
    </a-spin>
  </div>
</template>
<script>
import md5 from "js-md5";
import qs from "qs"
import KUtils from "@/core/utils";
import KEnvironment from "@/core/Environment"
import constant from "@/store/constants";
/* import EditorDebugShow from "./EditorDebugShow";
import DebugResponse from "./DebugResponse"; */
import DebugAxios from "axios";
import vkbeautify from "@/components/utils/vkbeautify";

export default {
  name: "Debug",
  components: {
    "EditorScript": () => import('./EditorScript'),
    "EditorDebugShow": () => import('./EditorDebugShow'),
    "DebugResponse": () => import('./DebugResponse')
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
      i18n: null,
      // 当前回调数据是否太大
      bigFlag: false,
      // 数据很大,raw显示会导致内存溢出
      bigBlobFlag: false,
      // 是否开启缓存
      debugUrlStyle: "width: 80%",
      enableRequestCache: false,
      // 是否动态参数
      enableDynamicParameter: false,
      enableHost: false,
      enableHostText: '',
      authorizeQueryParameters: [],
      headerColumn: [],
      formColumn: [],
      urlFormColumn: [],
      allowClear: true,
      pagination: false,
      headerAutoOptions: constant.debugRequestHeaders,
      headerOptions: constant.debugRequestHeaderOptions,
      headerCount: 0,
      headerCountFlag: false,
      headerSelectName: "",
      selectedRowKeys: [],
      // 是否允许有请求参数,一般get情况下直接屏蔽
      requestParameterAllow: true,
      // 请求头的选中框
      rowSelection: {
        selectedRowKeys: [],
        onChange: (selectrowkey, selectrows) => {
          this.rowSelection.selectedRowKeys = selectrowkey;
        }
      },
      rowFormSelection: {
        selectedRowKeys: [],
        onChange: (selectrowkey, selectrows) => {
          this.rowFormSelection.selectedRowKeys = selectrowkey;
        }
      },
      rowRawFormSelection: {
        selectedRowKeys: [],
        onChange: (selectrowkey, selectrows) => {
          this.rowRawFormSelection.selectedRowKeys = selectrowkey;
        }
      },
      rowUrlFormSelection: {
        selectedRowKeys: [],
        onChange: (selectrowkey, selectrows) => {
          this.rowUrlFormSelection.selectedRowKeys = selectrowkey;
        }
      },
      headerData: [],
      headerTableFlag: true,
      // 本地缓存全局参数
      globalParameters: [],
      // 调试接口
      debugUrl: "",
      // 当前请求接口地址是否为path类型,如果是,在发送请求时需要对地址栏进行替换
      debugPathFlag: false,
      // 需要替换的参数值key
      debugPathParams: [],
      // loading效果
      debugLoading: false,
      oAuthApi: false,
      debugSend: false,
      // form参数值对象
      formData: [],
      formFlag: false,
      formTableFlag: true,
      urlFormData: [],
      urlFormFlag: false,
      urlFormTableFlag: true,
      // raw类型请求存在query类型的参数
      rawFormData: [],
      rawFormFlag: false,
      rawFormTableFlag: true,
      rawDefaultText: "Auto",
      rawFlag: false,
      rawTypeFlag: false,
      // 格式化按钮显示标志
      formatFlag: false,
      rawText: "",
      rawScript: "",
      rawScriptMode: "javascript",
      rawMode: "text",
      rawRequestType: "application/json",
      requestContentType: "x-www-form-urlencoded",
      responseHeaders: [],
      responseRawText: "",
      responseCurlText: "",
      responseStatus: null,
      responseContent: null,
      responseFieldDescriptionChecked: true,
      // 网关转发请求Header标志
      routeHeader: null,
      oas2: true
    };
  },
  created() {
    this.routeHeader = this.swaggerInstance.header;
    this.oas2 = this.swaggerInstance.oas2();
    this.initI18n();
    // 初始化读取本地缓存全局参数
    this.initLocalGlobalParameters();
    this.initDebugUrl();
    // 显示表单参数
    // this.initShowFormTable();
    if (this.enableReloadCacheParameter) {
      this.debugUrlStyle = "width: 70%;"
    } else {
      this.debugUrlStyle = "width: 80%;"
    }
  },
  computed: {
    language() {
      return this.$store.state.globals.language;
    },
    enableAfterScript() {
      return this.$store.state.globals.enableAfterScript;
    },
    enableReloadCacheParameter() {
      return this.$store.state.globals.enableReloadCacheParameter;
    }
  },
  watch: {
    language: function (val, oldval) {
      this.initI18n();
    }
  },
  methods: {
    reloadCacheParameter() {
      // console.log("刷新变量,从缓存中重新读取变量值")
      // 刷新变量,从缓存中重新读取变量值
      // 初始化读取本地缓存全局参数
      // this.initLocalGlobalParameters();
      // 只更新变量,不做增加等任何处理
      var tempglobalParameters = [];
      const key = this.api.instanceId;
      // console.log(this.headerData);
      // 初始化读取本地缓存全局参数
      this.$localStore.getItem(constant.globalParameter).then(val => {
        if (val != null) {
          if (val[key] != undefined && val[key] != null) {
            tempglobalParameters = val[key];
          }
        }
        if (KUtils.arrNotEmpty(tempglobalParameters)) {
          // 更新header
          this.reloadUpdateHeader(tempglobalParameters);
          // 根据不同的请求类型,更新不同的请求参数
          if (this.rawFlag) {
            // 更新rawForm
            this.reloadUpdateRawForm(tempglobalParameters);
          } else if (this.formFlag) {
            // 更新form
            this.reloadUpdateForm(tempglobalParameters);
          } else if (this.urlFormFlag) {
            // 更新url-form
            this.reloadUpdateUrlForm(tempglobalParameters);
          }
        }
      });
    },
    /**
     * 根据原始数据得到最终要更新的数据和更新标志
     * @originalDatas 原始数据
     * @type 类型，主要 ：header\query
     */
    reloadUpdateCommons(tempglobalParameters, originalDatas, type) {
      var tempArrays = [];
      var tempUpdateFlag = false;
      var add = false;
      // 1、判断原始数据中是否存在要更新的值
      if (KUtils.arrNotEmpty(originalDatas)) {
        originalDatas.forEach(tempData => {
          // 查找是否存在
          var tempId = tempData.name + type;
          var tempFilterArr = tempglobalParameters.filter(t => t.pkid == tempId);
          if (KUtils.arrNotEmpty(tempFilterArr)) {
            var tempCache = tempFilterArr[0];
            var teampValue = KUtils.getValue(tempCache, "value", "", true);
            // 更新
            tempData.content = teampValue;
            tempUpdateFlag = true;
          }
          tempArrays.push(tempData);
        })
      }
      // 第2种情况，判断是否有新增的值
      var tempFilterArr = tempglobalParameters.filter(tp => tp.in == type);
      if (KUtils.arrNotEmpty(tempFilterArr)) {
        // 查找是否有新增的值
        tempFilterArr.forEach(tpdata => {
          // 判断是否存在,如果不存在，代表新增
          var addDateArr = tempArrays.filter(th => th.name == tpdata.name);
          if (!KUtils.arrNotEmpty(addDateArr)) {
            // 存在新增的值
            var newData = {
              id: KUtils.randomMd5(),
              name: tpdata.name,
              content: tpdata.value,
              require: true,
              description: "",
              enums: null, // 枚举下拉框
              // 枚举是否支持多选('default' | 'multiple' )
              enumsMode: "default",
              new: false
            };
            tempArrays.push(newData);
            tempUpdateFlag = true;
            add = true;
          }
        })
      }
      console.log(tempArrays)
      return {
        update: tempUpdateFlag,
        data: tempArrays,
        add: add
      }
    },
    reloadUpdateHeader(tempglobalParameters) {
      var newDataObject = this.reloadUpdateCommons(tempglobalParameters, this.headerData, "header");
      // 如果两种情况只需要1种情况存在更新,那么重新更新当前Header
      if (newDataObject.update) {
        this.headerData = [];
        setTimeout(() => {
          this.headerData = newDataObject.data;
          if (newDataObject.add) {
            // 如果有新增，刷新选中
            this.initSelectionHeaders();
            // 计算heaer数量
            this.headerResetCalc();
          }
        }, 10)
      }
    },
    reloadUpdateUrlForm(tempglobalParameters) {
      var newDataObject = this.reloadUpdateCommons(tempglobalParameters, this.urlFormData, "query");
      // 判断是否需要更新
      if (newDataObject.update) {
        this.urlFormData = [];
        setTimeout(() => {
          this.urlFormData = newDataObject.data;
          if (newDataObject.add) {
            this.initUrlFormSelections();
          }
        }, 10)
      }
    },
    reloadUpdateForm(tempglobalParameters) {
      var newDataObject = this.reloadUpdateCommons(tempglobalParameters, this.formData, "query");
      if (newDataObject.update) {
        this.formData = [];
        setTimeout(() => {
          this.formData = newDataObject.data;
          if (newDataObject.add) {
            this.initFormSelections();
          }
        }, 10)
      }
    },
    reloadUpdateRawForm(tempglobalParameters) {
      var newDataObject = this.reloadUpdateCommons(tempglobalParameters, this.rawFormData, "query");
      if (newDataObject.update) {
        this.rawFormData = [];
        setTimeout(() => {
          this.rawFormData = newDataObject.data;
          if (newDataObject.add) {
            this.rawFormFlag = true;
            this.rawFormTableFlag = true;
            this.initRawFormSelections();
          }
        }, 10)
      }
    },
    getCurrentI18nInstance() {
      return this.$i18n.messages[this.language];
    },
    initI18n() {
      // 根据i18n初始化部分参数
      var inst = this.getCurrentI18nInstance();
      this.i18n = inst;
      this.headerColumn = inst.table.debugRequestHeaderColumns;
      this.formColumn = inst.table.debugFormDataRequestColumns;
      this.urlFormColumn = inst.table.debugUrlFormRequestColumns;
    },
    debugUrlChange(e) {
      this.debugUrl = e.target.value;
    },
    initDebugUrl() {
      this.debugUrl = this.api.url;
      // 判断是否为paht类型
      var reg = new RegExp("{(.*?)}", "ig");
      // console("地址是否为path");
      if (reg.test(this.debugUrl)) {
        this.debugPathFlag = true;
        var ma = null;
        var mreg = new RegExp("{(.*?)}", "ig");
        while ((ma = mreg.exec(this.debugUrl))) {
          this.debugPathParams.push(ma[1]);
        }
      }
    },
    initLocalGlobalParameters() {
      const key = this.api.instanceId;
      // console.log(this.api)
      // 读取是否开启请求缓存标志
      this.$localStore.getItem(constant.globalSettingsKey).then(settings => {
        if (KUtils.checkUndefined(settings)) {
          this.enableRequestCache = settings.enableRequestCache;
          // 判断settings是否包含动态参数的配置
          if (KUtils.checkUndefined(settings["enableDynamicParameter"])) {
            // 如果存在,赋值
            this.enableDynamicParameter = settings.enableDynamicParameter;
          }
          // 读取Host配置
          if (KUtils.checkUndefined(settings["enableHost"])) {
            this.enableHost = settings.enableHost;
            // 判断Host的值
            var tmpHostValue = settings.enableHostText;
            if (KUtils.checkUndefined(tmpHostValue)) {
              if (!tmpHostValue.startWith("http")) {
                tmpHostValue = "http://" + tmpHostValue;
              }
              this.enableHostText = tmpHostValue;
            } else {
              // hostvalue为空,默认取消
              this.enableHost = false;
            }
          }
        }
        // 初始化读取本地缓存全局参数
        this.$localStore.getItem(constant.globalParameter).then(val => {
          if (val != null) {
            if (val[key] != undefined && val[key] != null) {
              this.globalParameters = val[key];
            }
          }
          // 当前接口的id作为缓存key值
          var cacheApiKey = constant.debugCacheApiId + this.api.id;
          this.$localStore.getItem(cacheApiKey).then(cacheApi => {
            // 开始同步执行其他方法-初始化请求头参数
            this.initHeaderParameter(cacheApi);
            // 判断是否authorize中包含query
            // 不读api的默认请求头,根据用户选择的表单请求类型做自动请求头适配
            // 读取Author的参数情况
            var securitykey = constant.globalSecurityParamPrefix + this.api.instanceId;
            this.$localStore.getItem(securitykey).then(val => {
              // console.log(val);
              // console.log(this.api)
              // console("读取本都Auth请");
              if (KUtils.arrNotEmpty(val)) {
                // 不为空
                val.forEach(security => {
                  if (security.in == 'query') {
                    // console.log(security)
                    var newquery = {
                      id: KUtils.randomMd5(),
                      name: security.name,
                      content: security.value,
                      value: security.value,
                      require: true,
                      description: "",
                      enums: null, // 枚举下拉框
                      // 枚举是否支持多选('default' | 'multiple' )
                      enumsMode: "default",
                      new: false
                    };
                    // 判断该接口是否security-Authorize
                    if (this.api.securityFlag) {
                      if (this.api.securityKeys.includes(security.key)) {
                        this.authorizeQueryParameters.push(newquery);
                      }
                    }
                  }
                });
              }
              // 请求体参数初始化
              this.initBodyParameter(cacheApi);
            });
          });
        });
      });
    },
    initHeaderParameter(cacheApi) {
      var oauth = this.syncFromOAuth2();
      if (KUtils.checkUndefined(oauth)) {
        this.oAuthApi = true;
        var oAuthHeader = {
          id: KUtils.randomMd5(),
          name: oauth.name,
          content: oauth.accessToken,
          require: true,
          description: "",
          enums: null, // 枚举下拉框
          // 枚举是否支持多选('default' | 'multiple' )
          enumsMode: "default",
          new: false
        };
        this.addDebugHeader(oAuthHeader);
      }
      // console.log("initHeaderParameter")
      // console.log(this.globalParameters)
      // 本都缓存读取到参数，初始化header参数
      this.globalParameters.forEach(param => {
        console.log(param)
        if (param.in == "header") {
          var newHeader = {
            id: KUtils.randomMd5(),
            name: param.name,
            content: param.value,
            require: false,
            description: "",
            enums: null, // 枚举下拉框
            // 枚举是否支持多选('default' | 'multiple' )
            enumsMode: "default",
            new: false
          };
          // this.headerData.push(newHeader);
          this.addDebugHeader(newHeader);
        }
      });
      // 不读api的默认请求头,根据用户选择的表单请求类型做自动请求头适配
      // 读取Author的参数情况
      var key = constant.globalSecurityParamPrefix + this.api.instanceId;
      this.$localStore.getItem(key).then(val => {
        // console("读取本都Auth请");
        if (KUtils.arrNotEmpty(val)) {
          // 不为空
          val.forEach(security => {
            // 追加一个schema
            // https://gitee.com/xiaoym/knife4j/issues/I4WDQ4
            let securityHeaderTmpValue = KUtils.getOAuth2BearerValue(security.schema, security.value);
            // console.log(security)
            var newHeader = {
              id: KUtils.randomMd5(),
              name: security.name,
              content: securityHeaderTmpValue,
              require: true,
              description: "",
              enums: null, // 枚举下拉框
              // 枚举是否支持多选('default' | 'multiple' )
              enumsMode: "default",
              new: false
            };
            if (security.in == 'header') {
              // console.log("addHeader.", security)
              // this.headerData.push(newHeader);
              // 判断该接口是否security-Authorize
              if (this.api.securityFlag) {
                if (this.api.securityKeys.includes(security.key)) {
                  this.addDebugHeader(newHeader);
                }
              }
            }
          });
        }
        this.updateHeaderFromCacheApi(cacheApi);
        // 判断是否开启了接口请求参数
        this.addNewLineHeader();
        this.initSelectionHeaders();
        // 计算heaer数量
        this.headerResetCalc();
      });
    },
    updateHeaderFromCacheApi(cacheApi) {
      // 从缓存中更新header参数
      if (this.enableRequestCache) {
        if (KUtils.checkUndefined(cacheApi)) {
          var cacheHeaderData = cacheApi.headerData;
          this.headerData.forEach(header => {
            // 判断当前header参数在缓存中是否存在，如果当前header存在值,则不更新
            if (!KUtils.strNotBlank(header.content)) {
              var cacheHeaderArr = cacheHeaderData.filter(
                ch => ch.name == header.name
              );
              if (cacheHeaderArr.length > 0) {
                if (!this.oAuthApi) {
                  // 非auth请求
                  // update
                  header.content = cacheHeaderArr[0].content;
                } else {
                  if (header.name != "Authorization") {
                    header.content = cacheHeaderArr[0].content;
                  }
                }
              }
            }
          });
        }
      }
    },
    updateUrlFormCacheApi(cacheApi) {
      // console("从缓存中更新UrlForm参数");
      // 从缓存中更新header参数
      if (this.enableRequestCache) {
        if (KUtils.checkUndefined(cacheApi)) {
          var cacheUrlFormData = cacheApi.urlFormData;
          this.urlFormData.forEach(form => {
            if (!KUtils.strNotBlank(form.content)) {
              var cacheUrlFormArr = cacheUrlFormData.filter(
                f => f.name == form.name
              );
              if (cacheUrlFormArr.length > 0) {
                form.content = cacheUrlFormArr[0].content;
              }
            }
          });
        }
      }
    },
    updateRawFormCacheApi(cacheApi) {
      // 从缓存中更新header参数
      if (this.enableRequestCache) {
        if (KUtils.checkUndefined(cacheApi)) {
          var cacheFormData = cacheApi.rawFormData;
          this.rawFormData.forEach(form => {
            if (!KUtils.strNotBlank(form.content)) {
              // console("缓存-raw:" + form.id);
              // console(cacheFormData);
              var cacheFormArr = cacheFormData.filter(f => f.name == form.name);
              // console(cacheFormArr);
              if (cacheFormArr.length > 0) {
                form.content = cacheFormArr[0].content;
              }
            }
          });
          // 更新Txt
          this.rawText = cacheApi.rawText;
        }
      }
    },
    syncFromOAuth2() {
      var instanceId = this.swaggerInstance.id;
      var key = "SELFOAuth" + instanceId;
      // console.log("syncFromOAuth2")
      if (window.localStorage) {
        var value = window.localStorage.getItem(key);
        // console.log(value)
        if (KUtils.strNotBlank(value)) {
          // 包含OAuth2参数
          var oauth = KUtils.json5parse(value);
          return oauth;
        }
      }
      return null;
    },
    updateFormCacheApi(cacheApi) {
      // console("从缓存中更新Form参数");
      // 从缓存中更新header参数
      if (this.enableRequestCache) {
        if (KUtils.checkUndefined(cacheApi)) {
          var cacheFormData = cacheApi.formData;
          this.formData.forEach(form => {
            if (!KUtils.strNotBlank(form.content)) {
              var cacheFormArr = cacheFormData.filter(f => f.name == form.name);
              if (cacheFormArr.length > 0) {
                form.content = cacheFormArr[0].content;
              }
            }
          });
        }
      }
    },
    initBodyParameter(cacheApi) {
      // this.initBodyType();
      // 初始化请求体参数
      // 得到body类型的请求参数
      var bodyParameters = this.globalParameters.filter(
        param => param.in != "header"
      );
      var bodyData = [];
      // 接口本身的参数对象
      var tmpApiParameters = this.api.parameters;
      // 本身全局参数显示集合
      var showGlobalParameters = [];
      // 本身接口api参数显示集合
      var showApiParameters = [];
      // 是否存在全局参数
      if (bodyParameters.length > 0) {
        // 存在，判断全局参数中和parameter对比，是否存在相同参数，如果存在，判断是否parameters参数有值，如果后端有值,则globalParams中的参数值不显示
        bodyParameters.forEach(global => {
          if (KUtils.arrNotEmpty(tmpApiParameters)) {
            var show = true;
            tmpApiParameters.forEach(param => {
              if (global.name == param.name && global.in == param.in) {
                // 在全局参数中存在相同的参数
                // 判断txtValue是否有值
                if (KUtils.strNotBlank(param.txtValue)) {
                  show = false;
                }
              }
            });
            // 如果show=true，则显示该参数
            if (show) {
              showGlobalParameters.push(global);
            }
          } else {
            // 当前接口不存在参数,但是开发者在界面添加了全局参数
            showGlobalParameters.push(global);
          }
        });
      }
      if (KUtils.arrNotEmpty(tmpApiParameters)) {
        tmpApiParameters.forEach(param => {
          if (KUtils.arrNotEmpty(bodyParameters)) {
            var show = true;
            bodyParameters.forEach(global => {
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

      if (KUtils.arrNotEmpty(this.authorizeQueryParameters)) {
        this.authorizeQueryParameters.forEach(aqp => {
          showGlobalParameters.push(aqp);
        })
      }
      // console.log(showGlobalParameters)
      // 根据参数列表、参数类型,开始自动判断接口的请求类型
      // 如果是单个@RequestBody类型,则参数只有一个,且只有一个，类型必须是body类型
      var paramSize = showGlobalParameters.length + showApiParameters.length;
      // console("参数大小:" + paramSize);
      if (KUtils.arrNotEmpty(showApiParameters)) {
        // 判断参数是否为body类型
        var bodySize = showApiParameters.filter(param => param.in == "body")
          .length;
        if (bodySize == 1) {
          // console(showApiParameters);
          // 判断raw类型是否还存在query类型的参数,如果存在,加入rawFormdata集合中
          var rawQueryParams = showApiParameters.filter(
            param => param.in != "body" && param.in != "header"
          );
          this.addGlobalParameterToRawForm(showGlobalParameters);
          if (rawQueryParams.length > 0) {
            // 存在
            this.rawFormFlag = true;
            // 添加参数
            this.addApiParameterToRawForm(rawQueryParams);
          }
          if (KUtils.arrNotEmpty(this.rawFormData)) {
            // 存在
            this.rawFormFlag = true;
          }
          // raw类型
          // raw类型之中可能有表格参数-待写
          this.showTabRaw();
          this.addApiParameterToRaw(showApiParameters);
          // 从缓存更新
          this.updateRawFormCacheApi(cacheApi);
          if (this.rawFormFlag) {
            // raw-form-data表单
            this.initFirstRawFormValue();
            // console(this.rawFormData);
          }
        } else {
          // 判断是否包含文件
          var fileSize = showApiParameters.filter(
            param =>
              param.schemaValue == "MultipartFile" ||
              param.schemaValue == "file" ||
              param.type == "file" ||
              param.in == "formData" ||
              param.in == "formdata"
          ).length;
          // console("文件大小参数---" + fileSize);

          if (fileSize > 0) {
            // form-data
            this.showTabForm();
            this.addGlobalParameterToForm(showGlobalParameters);
            this.addApiParameterToForm(showApiParameters);
            this.updateFormCacheApi(cacheApi);
            // form-data表单
            this.initFirstFormValue();
          } else {
            // url-form
            this.showTabUrlForm();
            this.addGlobalParameterToUrlForm(showGlobalParameters);
            this.addApiParameterToUrlForm(showApiParameters);
            this.updateUrlFormCacheApi(cacheApi);
            // url-form-data表单
            this.initUrlFormValue();
          }
        }
      } else {
        // 判断类型
        if (this.api.contentValue == "raw") {
          this.showTabRaw();
          this.initFirstRawFormValue();
        } else {
          // url-form类型
          this.showTabUrlForm();
          this.addGlobalParameterToUrlForm(showGlobalParameters);
          this.addApiParameterToUrlForm(showApiParameters);
          this.updateUrlFormCacheApi(cacheApi);
          // url-form-data表单
          this.initUrlFormValue();
        }

      }
      this.updateScriptFromCache(cacheApi);
      this.updateHeaderFromCacheApi(cacheApi);
      this.hideDynamicParameterTable();
      // console.log(this.urlFormData);
    },
    updateScriptFromCache(cacheApi) {
      // 更新script脚本功能,add by xiaoyumin 2020年10月21日
      if (KUtils.checkUndefined(cacheApi) && KUtils.strNotBlank(cacheApi.rawScript)) {
        this.rawScript = cacheApi.rawScript;
      }
    },
    hideDynamicParameterTable() {
      // 如果当前确定未开启动态参数调试,且参数为0的情况下,关闭table 的参数显示
      if (!this.enableDynamicParameter) {
        // 关闭header
        if (this.headerData.length == 0) {
          this.headerTableFlag = false;
        } else {
          this.headerTableFlag = true;
        }
        // 关闭urlform
        if (this.urlFormData.length == 0) {
          this.urlFormTableFlag = false;
        } else {
          this.urlFormTableFlag = true;
        }
        // 关闭form
        if (this.formData.length == 0) {
          this.formTableFlag = false;
        } else {
          this.formTableFlag = true;
        }
        // 关闭rawtable
        if (this.rawFormData.length == 0) {
          this.rawFormTableFlag = false;
        } else {
          this.rawFormTableFlag = true;
        }
      }
      this.initSelectionHeaders();
      // 计算heaer数量
      this.headerResetCalc();
    },
    addNewLineHeader() {
      if (this.enableDynamicParameter) {
        var newHeader = {
          id: KUtils.randomMd5(),
          name: "",
          content: "",
          require: false,
          description: "",
          enums: null, // 枚举下拉框
          // 枚举是否支持多选('default' | 'multiple' )
          enumsMode: "default",
          new: true
        };
        // 延时处理，保证新增的空行在最后一行
        setTimeout(() => this.addDebugHeader(newHeader), 100);
        // this.addDebugHeader(newHeader);
      }
      this.hideDynamicParameterTable();
    },
    addDebugHeader(newHeader) {
      if (KUtils.strNotBlank(newHeader.name)) {
        // 判断新的header的内容是否为空
        // 判断是否当前的header数据中是否已经存在
        var filterHeaders = this.headerData.filter(header => header.name == newHeader.name);
        if (KUtils.strBlank(newHeader.content)) {
          // 如果当前newHeader的数据为空,则判断当前的header数据中是否已经存在
          if (filterHeaders.length == 0) {
            // 不存在,插入新行
            this.headerData.push(newHeader);
          }
        } else {
          this.headerData.push(newHeader);
        }
      } else {
        // 动态调试,新行
        this.headerData.push(newHeader);
      }
    },
    initFirstFormValue() {
      // 添加一行初始form的值
      this.addNewLineFormValue();
      this.initFormSelections();
    },
    initFormSelections(selectedKey) {
      // 表单
      if (KUtils.strNotBlank(selectedKey)) {
        // 判断是否添加过
        var len = this.rowFormSelection.selectedRowKeys.filter(
          id => id == selectedKey
        ).length;
        if (len == 0) {
          this.rowFormSelection.selectedRowKeys.push(selectedKey);
        }
      } else {
        this.formData.forEach(form => {
          if (form.require) {
            this.rowFormSelection.selectedRowKeys.push(form.id);
          }
        });
      }
    },
    initRawFormSelections(selectedKey) {
      if (KUtils.strNotBlank(selectedKey)) {
        // 判断是否添加过
        var len = this.rowRawFormSelection.selectedRowKeys.filter(
          id => id == selectedKey
        ).length;
        if (len == 0) {
          this.rowRawFormSelection.selectedRowKeys.push(selectedKey);
        }
      } else {
        this.rawFormData.forEach(form => {
          if (form.require) {
            this.rowRawFormSelection.selectedRowKeys.push(form.id);
          }
        });
      }
    },
    initUrlFormSelections(selectedKey) {
      if (KUtils.strNotBlank(selectedKey)) {
        // 判断是否添加过
        var len = this.rowUrlFormSelection.selectedRowKeys.filter(
          id => id == selectedKey
        ).length;
        if (len == 0) {
          this.rowUrlFormSelection.selectedRowKeys.push(selectedKey);
        }
      } else {
        // 全选
        this.urlFormData.forEach(form => {
          if (form.require) {
            this.rowUrlFormSelection.selectedRowKeys.push(form.id);
          }
        });
      }
    },
    showTabForm() {
      this.formFlag = true;
      this.rawFlag = false;
      this.rawTypeFlag = false;
      this.formatFlag = false;
      this.urlFormFlag = false;
      this.requestContentType = "form-data";
      this.toggleBeautifyButtonStatus();
    },
    showTabUrlForm() {
      this.urlFormFlag = true;
      this.rawFlag = false;
      this.rawTypeFlag = false;
      this.formFlag = false;
      this.requestContentType = "x-www-form-urlencoded";
      this.toggleBeautifyButtonStatus();
    },
    showTabRaw() {
      this.rawFlag = true;
      this.rawMode = this.api.contentMode;
      this.rawDefaultText = this.api.contentShowValue;
      this.rawTypeFlag = true;
      this.formFlag = false;
      this.urlFormFlag = false;
      // 如果是raw类型，则赋值
      this.rawText = KUtils.toString(this.api.requestValue, "");
      if (this.api.xmlRequest) {
        this.rawRequestType = "application/xml";
      }
      this.requestContentType = "raw";
      this.toggleBeautifyButtonStatus();
    },
    getEnumOptions(param) {
      var tmpenum = KUtils.propValue("enum", param, null);
      var enumValue = null;
      if (KUtils.checkUndefined(tmpenum)) {
        var tmArr = [];
        tmpenum.forEach(em => {
          tmArr.push({ value: em, label: em });
        });
        enumValue = tmArr;
      }
      return enumValue;
    },
    addNewLineFormValue() {
      if (this.enableDynamicParameter) {
        // 添加新行form表单值
        var newFormHeader = {
          id: KUtils.randomMd5(),
          name: "",
          type: "text",
          require: false,
          // 文件表单域的target
          target: null,
          multipart: false,
          content: "",
          description: "",
          enums: null, // 枚举下拉框
          // 枚举是否支持多选('default' | 'multiple' )
          enumsMode: "default",
          new: true
        };
        this.formData.push(newFormHeader);
      } else {
        this.hideDynamicParameterTable();
      }
    },
    addGlobalParameterToRawForm(globalParameters) {
      // raw-form-data类型添加参数
      if (KUtils.arrNotEmpty(globalParameters)) {
        globalParameters.forEach(global => {
          var newFormHeader = {
            id: KUtils.randomMd5(),
            name: global.name,
            type: "text",
            require: false,
            // 文件表单域的target
            target: null,
            multipart: false,
            content: global.value,
            description: "",
            enums: null, // 枚举下拉框
            // 枚举是否支持多选('default' | 'multiple' )
            enumsMode: "default",
            new: false
          };
          this.rawFormData.push(newFormHeader);
        });
      }
    },
    addGlobalParameterToForm(globalParameters) {
      // form-data类型添加参数
      if (KUtils.arrNotEmpty(globalParameters)) {
        globalParameters.forEach(global => {
          var newFormHeader = {
            id: KUtils.randomMd5(),
            name: global.name,
            type: "text",
            require: false,
            // 文件表单域的target
            target: null,
            multipart: false,
            content: global.value,
            description: "",
            enums: null, // 枚举下拉框
            // 枚举是否支持多选('default' | 'multiple' )
            enumsMode: "default",
            new: false
          };
          this.formData.push(newFormHeader);
        });
      }
    },
    addApiParameterToRaw(apiParameters) {
      // raw类型添加header
      if (KUtils.arrNotEmpty(apiParameters)) {
        var headers = apiParameters.filter(param => param.in == "header");
        if (headers.length > 0) {
          headers.forEach(param => {
            // console(param);
            var newHeader = {
              id: KUtils.randomMd5(),
              name: param.name,
              require: param.require,
              content: param.txtValue,
              description: KUtils.propValue("description", param, ""),
              enums: this.getEnumOptions(param), // 枚举下拉框
              // 枚举是否支持多选('default' | 'multiple' )
              enumsMode: "default",
              new: false
            };
            // 判断枚举类型是否为空
            if (newHeader.enums != null) {
              // 判断content是否为空
              if (!KUtils.strNotBlank(newHeader.content)) {
                // 默认取第一个枚举值
                newHeader.content = newHeader.enums[0].value;
              }
            }
            // this.headerData.push(newHeader);
            this.addDebugHeader(newHeader);
          });
        }
      }
    },
    addApiParameterToForm(apiParameters) {
      // form-data类型
      if (KUtils.arrNotEmpty(apiParameters)) {
        apiParameters.forEach(param => {
          if (param.in == "header") {
            var newHeader = {
              id: KUtils.randomMd5(),
              name: param.name,
              require: param.require,
              content: param.txtValue,
              description: KUtils.propValue("description", param, ""),
              enums: this.getEnumOptions(param), // 枚举下拉框
              // 枚举是否支持多选('default' | 'multiple' )
              enumsMode: "default",
              new: false
            };
            // 判断枚举类型是否为空
            if (newHeader.enums != null) {
              // 判断content是否为空
              if (!KUtils.strNotBlank(newHeader.content)) {
                // 默认取第一个枚举值
                newHeader.content = newHeader.enums[0].value;
              }
            }
            // this.headerData.push(newHeader);
            this.addDebugHeader(newHeader);
          } else {
            var ptype = "text";
            var multipart = false;
            if (
              param.schemaValue == "MultipartFile" ||
              param.schemaValue == "file" ||
              param.type == "file"
            ) {
              ptype = "file";
              // 文件类型,判断是否是arrar
              if (param.type == "array") {
                multipart = true;
              }
            }
            // form-data的参数多一个文件是否允许多个上传的属性
            var newFormHeader = {
              id: KUtils.randomMd5(),
              name: param.name,
              type: ptype,
              // 是否必须
              require: param.require,
              // 文件表单域的target
              target: null,
              // 文件是否允许多个上传
              multipart: multipart,
              content: param.txtValue,
              description: KUtils.propValue("description", param, ""),
              enums: this.getEnumOptions(param), // 枚举下拉框
              // 枚举是否支持多选('default' | 'multiple' )
              enumsMode: "default",
              new: false
            };
            // 判断枚举类型是否为空
            if (newFormHeader.enums != null) {
              // 判断content是否为空
              if (!KUtils.strNotBlank(newFormHeader.content)) {
                // 默认取第一个枚举值
                newFormHeader.content = newFormHeader.enums[0].value;
              }
            }
            this.formData.push(newFormHeader);
          }
        });
      }
    },
    addGlobalParameterToUrlForm(globalParameters) {
      if (KUtils.arrNotEmpty(globalParameters)) {
        globalParameters.forEach(global => {
          var newFormHeader = {
            id: KUtils.randomMd5(),
            name: global.name,
            type: "text",
            require: false,
            // 文件表单域的target
            target: null,
            content: global.value,
            description: "",
            enums: null, // 枚举下拉框
            // 枚举是否支持多选('default' | 'multiple' )
            enumsMode: "default",
            new: false
          };
          this.urlFormData.push(newFormHeader);
        });
      }
    },
    addApiParameterToRawForm(apiParameters) {
      if (KUtils.arrNotEmpty(apiParameters)) {
        apiParameters.forEach(param => {
          if (param.in == "header") {
            var newHeader = {
              id: KUtils.randomMd5(),
              name: param.name,
              require: param.require,
              content: param.txtValue,
              description: KUtils.propValue("description", param, ""),
              enums: this.getEnumOptions(param), // 枚举下拉框
              // 枚举是否支持多选('default' | 'multiple' )
              enumsMode: "default",
              new: false
            };
            // 判断枚举类型是否为空
            if (newHeader.enums != null) {
              // 判断content是否为空
              if (!KUtils.strNotBlank(newHeader.content)) {
                // 默认取第一个枚举值
                newHeader.content = newHeader.enums[0].value;
              }
            }
            // this.headerData.push(newHeader);
            this.addDebugHeader(newHeader);
          } else {
            var newFormHeader = {
              id: KUtils.randomMd5(),
              name: param.name,
              type: "text",
              // 是否必须
              require: param.require,
              // 文件表单域的target
              target: null,
              content: param.txtValue,
              description: KUtils.propValue("description", param, ""),
              enums: this.getEnumOptions(param), // 枚举下拉框
              // 枚举是否支持多选('default' | 'multiple' )
              enumsMode: "default",
              new: false
            };
            // 判断枚举类型是否为空
            if (newFormHeader.enums != null) {
              // 判断content是否为空
              if (!KUtils.strNotBlank(newFormHeader.content)) {
                // 默认取第一个枚举值
                newFormHeader.content = newFormHeader.enums[0].value;
              }
            }
            this.rawFormData.push(newFormHeader);
          }
        });
      }
    },
    addApiParameterToUrlForm(apiParameters) {
      if (KUtils.arrNotEmpty(apiParameters)) {
        apiParameters.forEach(param => {
          if (param.in == "header") {
            var newHeader = {
              id: KUtils.randomMd5(),
              name: param.name,
              require: param.require,
              content: param.txtValue,
              description: KUtils.propValue("description", param, ""),
              enums: this.getEnumOptions(param), // 枚举下拉框
              // 枚举是否支持多选('default' | 'multiple' )
              enumsMode: "default",
              new: false
            };
            // 判断枚举类型是否为空
            if (newHeader.enums != null) {
              // 判断content是否为空
              if (!KUtils.strNotBlank(newHeader.content)) {
                // 默认取第一个枚举值
                newHeader.content = newHeader.enums[0].value;
              }
            }
            // this.headerData.push(newHeader);
            this.addDebugHeader(newHeader);
          } else {
            // console.log(param)
            // 判断该参数是否是枚举
            var enumsMode = "default";
            if (KUtils.arrNotEmpty(param.enum)) {
              // 枚举类型，判断是否是数组
              if (param.type == "array") {
                enumsMode = "multiple";
              }
            }
            var newFormHeader = {
              id: KUtils.randomMd5(),
              name: param.name,
              type: "text",
              // 是否必须
              require: param.require,
              // 文件表单域的target
              target: null,
              content: param.txtValue,
              description: KUtils.propValue("description", param, ""),
              enums: this.getEnumOptions(param), // 枚举下拉框
              // 枚举是否支持多选('default' | 'multiple' )
              enumsMode: enumsMode,
              new: false
            };
            // 判断枚举类型是否为空
            if (newFormHeader.enums != null) {
              // 判断content是否为空
              if (!KUtils.strNotBlank(newFormHeader.content)) {
                // 默认取第一个枚举值
                newFormHeader.content = newFormHeader.enums[0].value;
              }
            }
            this.urlFormData.push(newFormHeader);
          }
        });
      }
    },
    addNewLineUrlFormValue() {
      if (this.enableDynamicParameter) {
        var newFormHeader = {
          id: KUtils.randomMd5(),
          name: "",
          type: "text",
          // 是否必须
          require: false,
          // 文件表单域的target
          target: null,
          content: "",
          description: "",
          enums: null, // 枚举下拉框
          // 枚举是否支持多选('default' | 'multiple' )
          enumsMode: "default",
          new: true
        };
        this.urlFormData.push(newFormHeader);
      } else {
        this.hideDynamicParameterTable();
      }
    },
    addNewLineRawFormValue() {
      if (this.enableDynamicParameter) {
        var newFormHeader = {
          id: KUtils.randomMd5(),
          name: "",
          type: "text",
          // 是否必须
          require: false,
          // 文件表单域的target
          target: null,
          content: "",
          description: "",
          enums: null, // 枚举下拉框
          // 枚举是否支持多选('default' | 'multiple' )
          enumsMode: "default",
          new: true
        };
        this.rawFormData.push(newFormHeader);
      } else {
        this.hideDynamicParameterTable();
      }
    },
    initFirstRawFormValue() {
      this.addNewLineRawFormValue();
      this.initRawFormSelections();
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
      this.toggleBeautifyButtonStatus();
    },
    initSelectionHeaders(selectedKey) {
      if (KUtils.strNotBlank(selectedKey)) {
        // 判断是否添加过
        var len = this.rowSelection.selectedRowKeys.filter(
          id => id == selectedKey
        ).length;
        if (len == 0) {
          this.rowSelection.selectedRowKeys.push(selectedKey);
        }
      } else {
        this.headerData.forEach(header => {
          if (header.require) {
            this.rowSelection.selectedRowKeys.push(header.id);
          }
        });
      }
    },
    headerContentEnumChnage(value, option) {
      var headerId = option.context.$attrs["data-key"];
      this.headerContentChnageUpdate(value, headerId);

    },
    headerCookieValue(header) {
      // https://gitee.com/xiaoym/knife4j/issues/I439PO
      if (header.name.toLowerCase() == "cookie") {
        document.cookie = header.content;
      }
    },
    headerContentChnage(e) {
      var headerValue = e.target.value;
      var headerId = e.target.getAttribute("data-key");
      this.headerContentChnageUpdate(headerValue, headerId);

    },

    headerContentChnageUpdate(headerValue, headerId) {
      var record = this.headerData.filter(header => header.id == headerId)[0];
      if (record.new) {
        this.headerData.forEach(header => {
          if (header.id == record.id) {
            header.content = headerValue;
            header.new = false;
            this.headerCookieValue(header);

          }
        });
        // 插入一行
        this.addNewLineHeader();
      } else {
        this.headerData.forEach(header => {
          if (header.id == record.id) {
            header.content = headerValue;
            header.new = false;
            this.headerCookieValue(header);
          }
        });
      }
      this.initSelectionHeaders(record.id);
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
      let dataId = option.context["$attrs"]["data-id"];
      this.headerData.forEach(header => {
        if (header.id == dataId) {
          header.name = value;
          header.new = false;
        }
      });
    },
    headerSearch(value) {
      this.headerSelectName = value;
    },
    headerNameChange(record) {
      // 判断是否是new标志位,如果是new标志位,当前标志位置为false，重新生成一个new标志位的行
      if (record.new) {
        this.headerData.forEach(header => {
          if (header.id == record.id) {
            header.name = this.headerSelectName;
            header.new = false;
          }
        });
        // 插入一行
        this.addNewLineHeader();
      } else {
        this.headerData.forEach(header => {
          if (header.id == record.id) {
            header.name = this.headerSelectName;
            header.new = false;
          }
        });
      }
      this.initSelectionHeaders(record.id);
      this.headerResetCalc();
    },
    headerDelete(record) {
      var nheader = [];
      this.headerData.forEach(header => {
        if (header.id != record.id) {
          nheader.push(header);
        }
      });
      this.headerData = nheader;
      this.headerResetCalc();
    },
    headerResetCalc() {
      // 重新计算header请求头数量
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
      // console("radio checked", e.target.value);
      this.requestContentType = e.target.value;
      this.initShowFormTable();
    },
    formDelete(record) {
      var nforms = [];
      this.formData.forEach(form => {
        if (form.id != record.id) {
          nforms.push(form);
        }
      });
      this.formData = nforms;
    },
    formFileUploadClick(record) {
      // 触发file隐藏表单域的click事件
      document.getElementById("file" + record.id).click();
    },
    formNameChange(e) {
      var formValue = e.target.value;
      var formId = e.target.getAttribute("data-key");
      var record = this.formData.filter(form => form.id == formId)[0];
      if (record.new) {
        this.formData.forEach(form => {
          if (form.id == record.id) {
            form.name = formValue;
            form.new = false;
          }
        });
        this.addNewLineFormValue();
      } else {
        this.formData.forEach(form => {
          if (form.id == record.id) {
            form.name = formValue;
            form.new = false;
          }
        });
      }
      this.initFormSelections(record.id);
    },
    formTypeChange(value, option) {
      var arr = value.split("-");
      var formType = arr[0];
      var formId = arr[1];
      this.formData.forEach(form => {
        if (form.id == formId) {
          // 选择表单类型后,表单值置空
          form.content = "";
          form.type = formType;
          // 判断是否是文件类型，如果是文件类型，给定一个目标input-file域的target属性
        }
      });
    },
    formFileChange(e) {
      // console("文件发生变化了");
      // console(e);
      // console(e.target.files);
      var files = e.target.files;
      var fileStr = [];
      for (var i = 0; i < files.length; i++) {
        fileStr.push(files[i].name);
      }
      var fileStrValue = fileStr.join(",");
      var target = e.target;
      var formId = target.getAttribute("data-key");
      var record = this.formData.filter(form => form.id == formId)[0];
      if (record.new) {
        this.formData.forEach(form => {
          if (form.id == record.id) {
            form.content = fileStrValue;
            form.target = target;
            form.new = false;
          }
        });
        // console(this.formData);
        this.addNewLineFormValue();
      } else {
        this.formData.forEach(form => {
          if (form.id == record.id) {
            form.content = fileStrValue;
            form.target = target;
            form.new = false;
          }
        });
      }
      this.initFormSelections(record.id);
    },
    formContentUpdate(formValue, formId) {
      var record = this.formData.filter(form => form.id == formId)[0];
      if (record.new) {
        this.formData.forEach(form => {
          if (form.id == record.id) {
            form.content = formValue;
            form.new = false;
          }
        });
        this.addNewLineFormValue();
      } else {
        this.formData.forEach(form => {
          if (form.id == record.id) {
            form.content = formValue;
            form.new = false;
          }
        });
      }
      this.initFormSelections(record.id);
    },
    formContentEnumChange(formValue, option) {
      // console.log(option);
      var formId = option.context.$attrs["data-key"];
      // console.log("value:" + formValue + ",formId:" + formId);
      this.formContentUpdate(formValue, formId);
    },
    formContentChange(e) {
      var formValue = e.target.value;
      // //console("formcontent-value:" + formValue);
      var formId = e.target.getAttribute("data-key");
      this.formContentUpdate(formValue, formId);
    },
    rawFormDelete(record) {
      var nforms = [];
      this.rawFormData.forEach(form => {
        if (form.id != record.id) {
          nforms.push(form);
        }
      });
      this.rawFormData = nforms;
    },
    urlFormDelete(record) {
      var nforms = [];
      this.urlFormData.forEach(form => {
        if (form.id != record.id) {
          nforms.push(form);
        }
      });
      this.urlFormData = nforms;
    },
    rawFormNameChange(e) {
      var formValue = e.target.value;
      var formId = e.target.getAttribute("data-key");
      var record = this.rawFormData.filter(form => form.id == formId)[0];
      if (record.new) {
        this.rawFormData.forEach(form => {
          if (form.id == record.id) {
            form.name = formValue;
            form.new = false;
          }
        });
        this.addNewLineRawFormValue();
      } else {
        this.rawFormData.forEach(form => {
          if (form.id == record.id) {
            form.name = formValue;
            form.new = false;
          }
        });
      }
      this.initRawFormSelections(record.id);
    },
    urlFormNameChange(e) {
      var formValue = e.target.value;
      var formId = e.target.getAttribute("data-key");
      var record = this.urlFormData.filter(form => form.id == formId)[0];
      if (record.new) {
        this.urlFormData.forEach(form => {
          if (form.id == record.id) {
            form.name = formValue;
            form.new = false;
          }
        });
        this.addNewLineUrlFormValue();
      } else {
        this.urlFormData.forEach(form => {
          if (form.id == record.id) {
            form.name = formValue;
            form.new = false;
          }
        });
      }
      this.initUrlFormSelections(record.id);
    },
    rawFormContentUpdate(formValue, formId) {
      var record = this.rawFormData.filter(form => form.id == formId)[0];
      if (record.new) {
        this.rawFormData.forEach(form => {
          if (form.id == record.id) {
            form.content = formValue;
            form.new = false;
          }
        });
        this.addNewLineRawFormValue();
      } else {
        this.rawFormData.forEach(form => {
          if (form.id == record.id) {
            form.content = formValue;
            form.new = false;
          }
        });
      }
      this.initRawFormSelections(record.id);
    },
    rawFormContentEnumChange(formValue, option) {
      var formId = option.context.$attrs["data-key"];
      this.rawFormContentUpdate(formValue, formId);
    },
    rawFormContentChange(e) {
      var formValue = e.target.value;
      var formId = e.target.getAttribute("data-key");
      this.rawFormContentUpdate(formValue, formId);
    },
    urlFormContentUpdate(formValue, formId) {
      // 更新urlForm的表单内容
      var record = this.urlFormData.filter(form => form.id == formId)[0];
      if (record.new) {
        this.urlFormData.forEach(form => {
          if (form.id == record.id) {
            form.content = formValue;
            form.new = false;
          }
        });
        this.addNewLineUrlFormValue();
      } else {
        this.urlFormData.forEach(form => {
          if (form.id == record.id) {
            form.content = formValue;
            form.new = false;
          }
        });
      }
      this.initUrlFormSelections(record.id);
    },
    urlFormContentEnumChange(formValue, option) {
      if (KUtils.checkUndefined(option)) {
        // 支持枚举下拉多选
        var formId = "";
        // 判断formValue是否是数组,如果是数组代表多选
        // console.log(typeof(option))
        if (Array.isArray(option)) {
          // 任取1个
          formId = option[0].context.$attrs["data-key"];
        } else {
          formId = option.context.$attrs["data-key"];
        }
        // console.log("枚举选择")
        // console.log(formValue);
        // console.log(option)
        // console.log(formId)
        // var formId = option.context.$attrs["data-key"];
        this.urlFormContentUpdate(formValue, formId);
      }
    },
    urlFormContentChange(e) {
      var formValue = e.target.value;
      var formId = e.target.getAttribute("data-key");
      this.urlFormContentUpdate(formValue, formId);
    },
    rawMenuClick({ item, key, keyPath }) {
      this.rawMode = item.$el.getAttribute("data-mode");
      this.rawRequestType = item.$el.getAttribute("data-mode-type");
      this.rawDefaultText = key;
      this.toggleBeautifyButtonStatus();
    },
    beautifyJson() {
      let jsontext = this.rawText;
      if (KUtils.strNotBlank(jsontext)) {
        // 格式化操作
        try {
          let j = KUtils.json5stringify(KUtils.json5parse(jsontext));
          this.rawText = j;
        } catch (err) {
          console.error(err);
        }
      }
    },
    toggleBeautifyButtonStatus() {
      // 格式化JSON按钮标志
      let flag = false;
      if (this.rawFlag) {
        if (this.rawMode == 'json') {
          flag = true;
        }
      }
      this.formatFlag = flag;
    },
    rawScriptChange(value) {
      this.rawScript = value;
    },
    rawChange(value) {
      this.rawText = value;
    },
    sendRestfulApi(e) {
      e.preventDefault();
      // 验证公共请求头
      var validateHeader = this.validateCommonHeaders();
      // console("公共请求头验证");
      // console(validateHeader);
      // console.log(this)
      if (validateHeader.validate) {
        // 根据不同的请求类型,发送不同的请求
        if (this.rawFlag) {
          this.debugSendRawRequest();
        } else if (this.formFlag) {
          this.debugSendFormRequest();
        } else if (this.urlFormFlag) {
          // console.log("urlForm")
          this.debugSendUrlFormRequest();
        }
      } else {
        this.$message.info(validateHeader.message);
      }
    },
    callChildEditorShow() {
      // console("调用子类方法---");
      if (!this.bigFlag) {
        this.$refs.childDebugResponse.showEditorFieldDescription();
      }
    },
    debugHeaders() {
      // 获取发送请求的自定义等等请求头参数
      var headers = {};
      // 判断accept
      var apiInfo = this.api;
      if (
        apiInfo.produces != undefined &&
        apiInfo.produces != null &&
        apiInfo.produces.length > 0
      ) {
        var first = apiInfo.produces[0];
        headers["Accept"] = first;
      }
      this.headerData.forEach(header => {
        if (!header.new) {
          // 不是新行
          // 判断header是否选中
          var tmphArrs = this.rowSelection.selectedRowKeys.filter(
            rs => rs == header.id
          );
          if (tmphArrs.length > 0) {
            // 获取选中的headers才能发送
            if (KUtils.strNotBlank(header.name)) {
              // 此处去除header名称为cookie的值，因为已经更新了，浏览器发送时会自动带上
              if (header.name.toLowerCase() != "cookie") {
                // 需要判断请求头是否为中文,如果是中文,对其进行encode处理
                if (KUtils.isChinese(header.content)) {
                  headers[header.name] = encodeURIComponent(header.content);
                } else {
                  // header名称不等于空
                  headers[header.name] = KUtils.toString(header.content, "");
                }
              }
            }
          }
        }
      });

      // 追加1个Knife4j的默认请求头参数
      headers["Request-Origion"] = "Knife4j";
      // 判断是否包含请求Content-Type类型，如果不包含，则添加
      if (!KUtils.checkUndefined(headers["Content-Type"])) {
        if (this.rawFlag) {
          headers["Content-Type"] = this.rawRequestType;
        } else if (this.urlFormFlag) {
          headers["Content-Type"] = "application/x-www-form-urlencoded";
        } else if (this.formFlag) {
          // 此处需要验证是否是文件上传的表单类型
          if (this.validateFormDataContaintsFile()) {
            // 包含文件
            headers["Content-Type"] = "multipart/form-data";
          } else {
            headers["Content-Type"] = "application/x-www-form-urlencoded";
          }
        }
      }
      // 判断是否routeProxy请求，基于Knife4j自研aggre聚合组件请求header
      // add by xiaoymin 2020年11月13日 21:33:18
      if (KUtils.checkUndefined(this.routeHeader)) {
        headers["knfie4j-gateway-request"] = this.routeHeader;
      }
      // Knife4jAggregationDesktop组件header
      if (this.swaggerInstance.desktop) {
        headers["knife4j-gateway-code"] = this.swaggerInstance.desktopCode;
      }

      return headers;
    },
    debugRawFormParams() {
      // 获取url-form类型的参数
      var params = {};
      this.rawFormData.forEach(form => {
        if (!form.new) {
          // 判断header是否选中
          var tmphArrs = this.rowRawFormSelection.selectedRowKeys.filter(
            rs => rs == form.id
          );
          if (tmphArrs.length > 0) {
            // 必须选中
            if (KUtils.strNotBlank(form.name)) {
              params[form.name] = form.content;
            }
          }
        }
      });
      return params;
    },
    debugUrlFormParams() {
      // 获取url-form类型的参数
      var params = {};
      // 对于GET 类型请求编码处理
      this.urlFormData.forEach(form => {
        if (!form.new) {
          // 判断header是否选中
          var tmphArrs = this.rowUrlFormSelection.selectedRowKeys.filter(
            rs => rs == form.id
          );
          if (tmphArrs.length > 0) {
            // 必须选中
            if (KUtils.strNotBlank(form.name)) {
              params[form.name] = form.content;
            }
          }
        }
      });
      return params;
    },
    debugFormDataParams(fileFlag) {
      // form-data类型的请求参数
      var validateForm = { url: "", params: {} };
      var url = this.debugUrl;
      // console("表单验证url:" + url);
      if (fileFlag) {
        // 文件
        var formData = new FormData();
        this.formData.forEach(form => {
          if (!form.new) {
            // 判断header是否选中
            var tmphArrs = this.rowFormSelection.selectedRowKeys.filter(
              rs => rs == form.id
            );
            if (tmphArrs.length > 0) {
              // 必须选中
              if (KUtils.strNotBlank(form.name)) {
                // 判断类型
                if (form.type == "text") {
                  // 判断是否是urlPath参数
                  if (this.debugPathFlag) {
                    if (this.debugPathParams.indexOf(form.name) == -1) {
                      // 非空判断
                      // https://gitee.com/xiaoym/knife4j/issues/I3AHDQ
                      if (KUtils.strNotBlank(form.content)) {
                        formData.append(form.name, form.content);
                      }
                    } else {
                      var replaceRege = "{" + form.name + "}";
                      url = url.replace(replaceRege, form.content);
                    }
                  } else {
                    // 非空判断
                    // https://gitee.com/xiaoym/knife4j/issues/I3AHDQ
                    if (KUtils.strNotBlank(form.content)) {
                      formData.append(form.name, form.content);
                    }
                  }
                } else {
                  // 文件
                  if (KUtils.checkUndefined(form.target)) {
                    var files = form.target.files;
                    // 判断是否是运行多个上传
                    if (files.length > 0) {
                      for (var i = 0; i < files.length; i++) {
                        formData.append(form.name, files[i]);
                      }
                    }
                  }
                }
              }
            }
          }
        });
        validateForm.params = formData;
      } else {
        var params = {};
        this.formData.forEach(form => {
          if (!form.new) {
            // 判断header是否选中
            var tmphArrs = this.rowFormSelection.selectedRowKeys.filter(
              rs => rs == form.id
            );
            if (tmphArrs.length > 0) {
              // 必须选中
              if (KUtils.strNotBlank(form.name)) {
                // 判断是否是urlPath参数
                if (this.debugPathFlag) {
                  if (this.debugPathParams.indexOf(form.name) == -1) {
                    params[form.name] = form.content;
                  } else {
                    var replaceRege = "{" + form.name + "}";
                    url = url.replace(replaceRege, form.content);
                  }
                } else {
                  params[form.name] = form.content;
                }
              }
            }
          }
        });
        validateForm.params = params;
        // return params;
      }
      validateForm.url = url;
      return validateForm;
    },
    debugStreamFlag() {
      var streamFlag = false;
      var apiInfo = this.api;
      // console.log(apiInfo);
      if (
        apiInfo.produces != undefined &&
        apiInfo.produces != null &&
        apiInfo.produces.length > 0
      ) {
        var first = apiInfo.produces[0];
        var binaryObject = KUtils.binaryContentType(apiInfo.produces, null);
        streamFlag = binaryObject.binary;
      }
      return streamFlag;
    },
    validateCommonHeaders() {
      // 验证公共请求头
      var validate = true;
      var message = "";
      for (var i = 0; i < this.headerData.length; i++) {
        var header = this.headerData[i];
        if (!header.new) {
          // 判断header是否选中
          var tmphArrs = this.rowSelection.selectedRowKeys.filter(
            rs => rs == header.id
          );
          if (tmphArrs.length > 0) {
            // 必须选中
            if (KUtils.strNotBlank(header.name)) {
              if (header.require) {
                if (!KUtils.strNotBlank(header.content)) {
                  validate = false;
                  // message = "请求头" + header.name + "不能为空";
                  message = this.i18n.validate.header + header.name + this.i18n.validate.notEmpty;
                  break;
                }
              }
            }
          }
        }
      }
      return { validate: validate, message: message };
    },
    validateFormData() {
      // 验证form-data的参数
      var validate = true;
      var message = "";
      for (var i = 0; i < this.formData.length; i++) {
        var form = this.formData[i];
        if (!form.new) {
          // 判断header是否选中
          var tmphArrs = this.rowFormSelection.selectedRowKeys.filter(
            rs => rs == form.id
          );
          if (tmphArrs.length > 0) {
            // 必须选中
            if (KUtils.strNotBlank(form.name)) {
              if (form.require) {
                // 判断参数类型,如果是text，则验证content，否则验证target
                if (form.type == "text") {
                  if (!KUtils.strNotBlank(form.content)) {
                    validate = false;
                    // message = form.name + "不能为空";
                    message = form.name + this.i18n.validate.notEmpty;
                    break;
                  }
                } else {
                  // 文件
                  if (form.target == null) {
                    validate = false;
                    // message = form.name + "文件不能为空";
                    message = form.name + this.i18n.validate.fileNotEmpty;
                    break;
                  }
                }
              }
            }
          }
        }
      }
      return { validate: validate, message: message };
    },
    validateRawForm() {
      // 验证raw-form的参数
      var validate = true;
      var message = "";
      for (var i = 0; i < this.rawFormData.length; i++) {
        var form = this.rawFormData[i];
        if (!form.new) {
          // 判断header是否选中
          var tmphArrs = this.rowRawFormSelection.selectedRowKeys.filter(
            rs => rs == form.id
          );
          if (tmphArrs.length > 0) {
            // 必须选中
            if (KUtils.strNotBlank(form.name)) {
              if (form.require) {
                if (!KUtils.strNotBlank(form.content)) {
                  validate = false;
                  // message = form.name + "不能为空";
                  message = form.name + this.i18n.validate.notEmpty;
                  break;
                }
              }
            }
          }
        }
      }
      return { validate: validate, message: message };
    },
    validateUrlForm() {
      // 验证url-form的参数
      var validate = true;
      var message = "";
      for (var i = 0; i < this.urlFormData.length; i++) {
        var form = this.urlFormData[i];
        if (!form.new) {
          // 判断header是否选中
          var tmphArrs = this.rowUrlFormSelection.selectedRowKeys.filter(
            rs => rs == form.id
          );
          if (tmphArrs.length > 0) {
            // 必须选中
            if (KUtils.strNotBlank(form.name)) {
              if (form.require) {
                if (!KUtils.strNotBlank(form.content)) {
                  validate = false;
                  // message = form.name + "不能为空";
                  message = form.name + this.i18n.validate.notEmpty;
                  break;
                }
              }
            }
          }
        }
      }
      return { validate: validate, message: message };
    },
    validateFormDataContaintsFile() {
      // 验证form-data中是否包含file文件
      var flag = false;
      this.formData.forEach(form => {
        if (!form.new) {
          // 判断header是否选中
          var tmphArrs = this.rowFormSelection.selectedRowKeys.filter(
            rs => rs == form.id
          );
          if (tmphArrs.length > 0) {
            if (form.type == "file") {
              flag = true;
            }
          }
        }
      });
      return flag;
    },
    checkUrlParams(url) {
      // 如果开发者自己在url栏添加了后缀参数,则解析动态添加
      // https://gitee.com/xiaoym/knife4j/issues/I1C5OQ
      // 校验url请求参数是否携带参数
      // 如果开发者自己在url栏添加了后缀参数,则解析动态添加
      // https://gitee.com/xiaoym/knife4j/issues/I1C5OQ
      var paramIndex = url.indexOf("?");
      var checkResult = {
        result: false, // 不包含参数
        params: {},
        url: url
      };
      if (paramIndex > -1) {
        // 包含参数
        var subParam = url.substring(paramIndex + 1);
        // 存在
        // url重新赋值
        checkResult.url = url.substring(0, paramIndex);
        checkResult.result = true;
        // url重新赋值
        // split
        if (KUtils.strNotBlank(subParam)) {
          var subParamArrs = subParam.split("&");
          subParamArrs.forEach(suba => {
            if (KUtils.strNotBlank(suba)) {
              var realpa = suba.split("=");
              if (realpa.length == 2) {
                // 追加参数
                checkResult.params[realpa[0]] = realpa[1];
              }
            }
          });
        }
      }
      return checkResult;
    },
    debugSendHasCookie(headers) {
      // 判断请求头中是否包含Cookie
      var flag = false;
      // console.log("校验请求头是否存在Cookie");
      // console.log(headers);
      if (KUtils.checkUndefined(headers)) {
        var headerNameArrs = Object.keys(headers);
        if (KUtils.arrNotEmpty(headerNameArrs)) {
          var cookieArr = headerNameArrs.filter(headerName => headerName.toLocaleLowerCase() === "cookie").length;
          if (cookieArr > 0) {
            // 存在cookie
            var cookieValue = headers["Cookie"];
            if (KUtils.strNotBlank(cookieValue)) {
              // 前端JS写入
              document.cookie = cookieValue;
              flag = true;
            }
          }
        }
      }
      // console.log("校验结果："+flag);
      return flag;
    },
    applyRequestParams(formParams, methodType) {
      var requestData = null;
      var requestParams = null;
      // console.log(formParams)
      if (["post", "put", "patch"].includes(methodType.toLowerCase())) {
        if (KUtils.checkUndefined(formParams)) {
          requestData = qs.stringify(formParams);
          //requestParams = qs.stringify(formParams);
          //requestParams = formParams;
        }
      } else {
        //requestData = formParams;
        requestParams = formParams;
      }
      return {
        data: requestData, params: requestParams
      }
    },
    debugCheckUrl(url) {
      // 如果服务端开启了enableUrlTemplating，正常参数会出现在url中
      // 比如：/api/nxew205/reqEnumArr{?errorCodes,name}
      // https://gitee.com/xiaoym/knife4j/issues/I22J5Q
      // 针对这种形式，直接去除地址栏后面的{?errorCodes,name}模板参数，因为axios在发送的时候会直接带上参数地址,这样也避免参数丢失和访问接口出现404
      var checkUrl = url;
      try {
        var reg = new RegExp('.*?(\{.*?\})$', "ig");
        if (reg.test(url)) {
          var rr = RegExp.$1;
          checkUrl = url.replace(rr, "");
        }
      } catch (e) {
        // ignore
        if (window.console) {
          console.error(e);
        }
      }
      return checkUrl;
    },
    debugSendUrlFormRequest() {
      // 发送url-form类型的请求
      // console("发送url-form接口");
      var validateForm = this.validateUrlForm();
      // console(validateForm);
      if (validateForm.validate) {
        this.debugLoading = true;
        // 发送状态置为已发送请求
        this.debugSend = true;
        // raw类型的请求需要判断是何种类型
        var headers = this.debugHeaders();
        var url = this.debugUrl;
        var methodType = this.api.methodType.toLowerCase();
        var formParams = this.debugUrlFormParams();
        // 得到key-value的参数值,对请求类型进行判断，判断是否为path
        if (this.debugPathFlag) {
          const realFormParams = {};
          // 是path类型的接口,需要对地址、参数进行replace处理
          this.debugPathParams.forEach(pathKey => {
            var replaceRege = "{" + pathKey + "}";
            // var value = formParams[pathKey];
            var value = KUtils.getValue(formParams, pathKey, "", true);
            url = url.replace(replaceRege, value);
          });
          for (var key in formParams) {
            // 判断key在debugPath中是否存在
            if (this.debugPathParams.indexOf(key) == -1) {
              // 不存在
              realFormParams[key] = formParams[key];
            }
          }
          // 重新赋值
          formParams = realFormParams;
        }
        var checkResult = this.checkUrlParams(url);
        if (checkResult.result) {
          url = checkResult.url;
          formParams = Object.assign(formParams, checkResult.params);
        }
        var baseUrl = '';
        // 是否启用Host
        if (this.enableHost) {
          baseUrl = this.enableHostText;
        }
        // console.log(headers)
        var applyReuqest = this.applyRequestParams(formParams, methodType);
        // console.log(applyReuqest)
        var requestConfig = {
          baseURL: baseUrl,
          url: this.debugCheckUrl(url),
          method: methodType,
          headers: headers,
          params: applyReuqest.params,
          timeout: 0,
          // Cookie标志
          withCredentials: this.debugSendHasCookie(headers),
          // 此data必传,不然默认是data:undefined,https://github.com/axios/axios/issues/86
          // 否则axios会忽略请求头Content-Type
          data: applyReuqest.data
        };
        // 判断当前接口规范是OAS3还是Swagger2
        if (this.oas2) {
          // OAS2规范制定了produces的定义,需要判断请求头
          // 需要判断是否是下载请求
          if (this.debugStreamFlag()) {
            // 流请求
            requestConfig = { ...requestConfig, responseType: "blob" };
          }
        } else {
          // 统一追加一个blob类型的响应,在OpenAPI3.0的规范中,没有关于produces的设定，因此无法判断当前请求是否是流的请求
          // https://gitee.com/xiaoym/knife4j/issues/I374SP
          requestConfig = { ...requestConfig, responseType: "blob" };
        }
        // console.log(requestConfig);
        const debugInstance = DebugAxios.create();
        // get请求编码问题
        // https://gitee.com/xiaoym/knife4j/issues/I19C8Y
        debugInstance.interceptors.request.use(config => {
          let url = config.url;
          if (config.method === "get" && config.params) {
            url += "?";
            let keys = Object.keys(config.params);
            for (let key of keys) {
              if (KUtils.strNotBlank(config.params[key])) {
                url += `${encodeURIComponent(key)}=${encodeURIComponent(
                  config.params[key]
                )}&`;
              }
            }
            url = url.substring(0, url.length - 1);
            config.params = {};
          }
          //  get参数编码
          config.url = url;
          return config;
        });
        // console(headers);
        // console(requestConfig);
        // 开始时间定义在发送之前
        var startTime = new Date();
        debugInstance
          .request(requestConfig)
          .then(res => {
            // console("url-form-success");
            // console.log(res);
            //  console.log("响应成功")
            // console.log(res);
            this.debugLoading = false;
            this.handleDebugSuccess(startTime, new Date(), res);
          })
          .catch(err => {
            // console("触发url-form-error");
            // console.info(err);
            this.debugLoading = false;
            // 虽然是错误,但依然有返回值
            if (err.response) {
              this.handleDebugError(startTime, new Date(), err.response);
            } else {
              this.$message.error(err.message);
              // //console(err.message);
            }
          });
      } else {
        this.$message.info(validateForm.message);
      }
    },
    debugSendFormRequest() {
      // 发送form类型的请求
      var validateForm = this.validateFormData();
      // console(validateForm);
      if (validateForm.validate) {
        // console("验证通过---");
        this.debugLoading = true;
        // 发送状态置为已发送请求
        this.debugSend = true;
        // raw类型的请求需要判断是何种类型
        var headers = this.debugHeaders();
        var url = this.debugUrl;

        var methodType = this.api.methodType.toLowerCase();
        var fileFlag = this.validateFormDataContaintsFile();
        var validateFormd = this.debugFormDataParams(fileFlag);
        // console(validateFormd);
        url = validateFormd.url;
        // var formParams = this.debugFormDataParams(fileFlag);
        var formParams = validateFormd.params;
        var baseUrl = '';
        // 是否启用Host
        if (this.enableHost) {
          baseUrl = this.enableHostText;
        }
        var requestConfig = {
          baseURL: baseUrl,
          url: this.debugCheckUrl(url),
          method: methodType,
          headers: headers,
          timeout: 0,
          // Cookie标志
          withCredentials: this.debugSendHasCookie(headers),
          // 此data必传,不然默认是data:undefined,https://github.com/axios/axios/issues/86
          // 否则axios会忽略请求头Content-Type
          data: null
        };
        if (fileFlag) {
          requestConfig = { ...requestConfig, data: formParams };
        } else {
          var checkResult = this.checkUrlParams(url);
          if (checkResult.result) {
            url = checkResult.url;
            formParams = Object.assign(formParams, checkResult.params);
          }
          requestConfig = { ...requestConfig, params: formParams };
        }
        // 需要判断是否是下载请求
        if (this.debugStreamFlag()) {
          // 流请求
          requestConfig = { ...requestConfig, responseType: "blob" };
        }
        let debugInstance = DebugAxios.create();
        // console(headers);
        // console(requestConfig);
        var startTime = new Date();
        debugInstance
          .request(requestConfig)
          .then(res => {
            // console("url-form-success");
            // console(res);
            this.debugLoading = false;
            this.handleDebugSuccess(startTime, new Date(), res);
          })
          .catch(err => {
            this.debugLoading = false;
            // console("触发url-form-error");
            if (err.response) {
              this.handleDebugError(startTime, new Date(), err.response);
            } else {
              this.$message.error(err.message);
              // //console(err.message);
            }
          });
      } else {
        this.$message.info(validateForm.message);
      }
    },
    debugSendRawRequest() {
      // 发送raw类型的请求
      // console("发送raw接口");
      var validateForm = this.validateRawForm();
      if (validateForm.validate) {
        this.debugLoading = true;
        // 发送状态置为已发送请求
        this.debugSend = true;
        // raw类型的请求需要判断是何种类型
        var headers = this.debugHeaders();
        var url = this.debugUrl;
        var methodType = this.api.methodType.toLowerCase();
        var data = this.rawText;
        var formParams = this.debugRawFormParams();
        // 得到key-value的参数值,对请求类型进行判断，判断是否为path
        if (this.debugPathFlag) {
          const realFormParams = {};
          // 是path类型的接口,需要对地址、参数进行replace处理
          this.debugPathParams.forEach(pathKey => {
            var replaceRege = "{" + pathKey + "}";
            // var value = formParams[pathKey];
            var value = KUtils.getValue(formParams, pathKey, "", true);
            url = url.replace(replaceRege, value);
          });
          for (var key in formParams) {
            // 判断key在debugPath中是否存在
            if (this.debugPathParams.indexOf(key) == -1) {
              // 不存在
              realFormParams[key] = formParams[key];
            }
          }
          // 重新赋值
          formParams = realFormParams;
        }
        var checkResult = this.checkUrlParams(url);
        if (checkResult.result) {
          url = checkResult.url;
          formParams = Object.assign(formParams, checkResult.params);
        }
        var baseUrl = '';
        // 是否启用Host
        if (this.enableHost) {
          baseUrl = this.enableHostText;
        }
        var requestConfig = {
          baseURL: baseUrl,
          url: this.debugCheckUrl(url),
          method: methodType,
          headers: headers,
          params: formParams,
          data: data,
          // Cookie标志
          withCredentials: this.debugSendHasCookie(headers),
          timeout: 0
        }
        // 需要判断是否是下载请求
        // https://gitee.com/xiaoym/knife4j/issues/I1U4LA
        if (this.debugStreamFlag()) {
          // 流请求
          requestConfig = { ...requestConfig, responseType: "blob" };
        }
        // console(headers);
        // console(this.rawText);
        var startTime = new Date();
        DebugAxios.create()
          .request(requestConfig)
          .then(res => {
            this.debugLoading = false;
            this.handleDebugSuccess(startTime, new Date(), res);
          })
          .catch(err => {
            this.debugLoading = false;
            if (err.response) {
              this.handleDebugError(startTime, new Date(), err.response);
            } else {
              this.$message.error(err.message);
            }
          });
      } else {
        this.$message.info(validateForm.message);
      }
    },
    executeAfterScript(res) {
      // console.log("executeAfterScript");
      // console.log(res);
      if (KUtils.strNotBlank(this.rawScript)) {
        var groupid = this.swaggerInstance.id;
        var allgroupid = this.swaggerInstance.allGroupIds;
        // console.log("groupid:"+groupid);
        // script不为空
        var settings = {
          allgroupids: allgroupid,
          groupid: groupid,
          response: {
            data: res.data,
            headers: res.headers
          }
        }
        var ke = new KEnvironment(settings);
        try {
          var func = new Function('ke', this.rawScript);
          // 执行
          func(ke);
          setTimeout(() => {
            // console.log("开始执行...")
            ke.global.action();
          }, 1000);
        } catch (e) {
          console.error(e);
        }
      }
    },
    handleDebugSuccess(startTime, endTime, res) {
      this.bigFlag = false;
      this.bigBlobFlag = false;
      // 成功的情况
      this.setResponseBody(res);
      this.setResponseHeaders(res.headers);
      this.setResponseRaw(res);
      // console("开始执行status--");
      this.setResponseStatus(startTime, endTime, res);
      this.setResponseCurl(res.request);
      this.callChildEditorShow();
      // 执行成功后,执行afterScript中的脚本
      this.executeAfterScript(res);
      this.storeApiParams();
    },
    handleDebugError(startTime, endTime, resp) {
      this.bigFlag = false;
      this.bigBlobFlag = false;
      // console.log("失败情况---");
      // console.log(resp);
      // 失败的情况
      this.setResponseBody(resp);
      this.setResponseHeaders(resp.headers);
      this.setResponseRaw(resp);
      this.setResponseStatus(startTime, endTime, resp);
      this.setResponseCurl(resp.request);
      this.callChildEditorShow();
      this.storeApiParams();
    },
    storeApiParams() {
      // 对于开启请求参数缓存的配置,在接口发送后,缓存配置
      if (this.enableRequestCache) {
        // console.log("开启缓存--")
        var cacheApi = {
          headerData: [],
          formData: [],
          urlFormData: [],
          rawFormData: [],
          rawText: ""
        };
        var cacheApiKey = constant.debugCacheApiId + this.api.id;
        // 得到headercans
        cacheApi.headerData = this.headerData.filter(
          header => header.new == false
        );
        // 得到form
        cacheApi.formData = this.formData.filter(form => form.new == false);
        // url-form
        cacheApi.urlFormData = this.urlFormData.filter(
          form => form.new == false
        );
        // raw-form
        cacheApi.rawFormData = this.rawFormData.filter(
          form => form.new == false
        );
        cacheApi.rawText = this.rawText;
        // 增加script功能 2020年10月21日
        cacheApi.rawScript = this.rawScript;
        // console("缓存请求参数");
        // console(cacheApi);
        this.$localStore.setItem(cacheApiKey, cacheApi);
      }
    },
    setResponseHeaders(respHeaders) {
      // 给相应请求头表格赋值
      var tmpRespHeaderArrs = [];
      if (KUtils.checkUndefined(respHeaders)) {
        for (var k in respHeaders) {
          var tmpH = {
            id: KUtils.randomMd5(),
            name: k,
            value: respHeaders[k]
          };
          tmpRespHeaderArrs.push(tmpH);
        }
      }
      this.responseHeaders = tmpRespHeaderArrs;
    },
    setResponseRaw(res) {
      if (KUtils.checkUndefined(res)) {
        var resp = res.request;
        var headers = res.headers;
        if (KUtils.checkUndefined(resp)) {
          // 判断是否是blob类型
          if (resp.responseType != "blob") {
            var _tmpRawText = KUtils.toString(resp.responseText, "");
            // 123
            // console.log(_tmpRawText)
            this.responseRawText = _tmpRawText;
          }
        }
      }
    },
    setResponseStatus(startTime, endTime, res) {
      // console("响应状态------------");
      if (KUtils.checkUndefined(res)) {
        var resp = res.request;
        // 响应状态
        if (KUtils.checkUndefined(resp)) {
          // var endTime = new Date();
          var costStr = "";
          var cost = endTime.getTime() - startTime.getTime();
          var code = resp.status;
          if (cost > 1000) {
            // 超过1秒钟
            var sec = Math.floor(cost / 1000).toFixed(1);
            costStr = sec + "s";
          } else {
            // 接口响应毫秒级别
            costStr = cost + "ms";
          }
          // 判断是否包含text
          var size = 0;
          // 判断是否是blob类型
          if (resp.responseType == "blob") {
            // blob类型
            size = resp.response.size;
          } else {
            if (KUtils.checkUndefined(resp.responseText)) {
              size = resp.responseText.gblen();
            }
          }
          // 赋值
          this.responseStatus = {
            code: code,
            cost: costStr,
            size: size
          };
          // console(this.responseStatus);
        }
      }
    },
    setResponseCurl(resp) {
      var that = this;
      var url = this.debugCheckUrl(this.debugUrl);
      // console.log("setResponseCurl:"+url)
      // 构建请求响应CURL
      var curlified = new Array();
      var protocol = "http";
      // 获取location
      var href = window.location.href;
      // 判断是否是https
      var proRegex = new RegExp("^https.*", "ig");
      if (proRegex.test(href)) {
        protocol = "https";
      }
      var httpReg = new RegExp("^(http|https):.*", "ig");
      var fullurl = "";
      if (httpReg.test(this.api.host)) {
        // 如果包含,则不追究
        fullurl = this.api.host;
      } else {
        fullurl = protocol + ":// " + this.api.host;
      }
      // 判断是否开启了Host的配置,如果开启则直接使用Host中的地址
      if (this.enableHost) {
        fullurl = this.enableHostText;
      }
      // 判断url是否是以/开头
      if (!url.startWith("/")) {
        fullurl += "/";
      }
      fullurl += url;
      curlified.push("curl");
      curlified.push("-X", this.api.methodType.toUpperCase());
      // 设置请求头
      var headers = this.debugHeaders();
      var ignoreHeaders = [];
      ignoreHeaders.push("knfie4j-gateway-request");
      ignoreHeaders.push("knife4j-gateway-code");
      ignoreHeaders.push("Request-Origion");
      if (KUtils.checkUndefined(headers)) {
        for (var h in headers) {
          if (!ignoreHeaders.includes(h)) {
            curlified.push("-H ");
            curlified.push('"' + h + ":" + headers[h] + '"');
          }
        }
      }

      if (this.rawFlag) {
        // headers["Content-Type"] = this.rawRequestType;
        // console("raw------------------curl");
        var formParams = this.debugRawFormParams();
        var tmpUrls = [];
        if (KUtils.checkUndefined(formParams)) {
          for (var p in formParams) {
            if (that.debugPathFlag) {
              // 确实是，判断该参数是否出现
              if (that.debugPathParams.indexOf(p) == -1) {
                tmpUrls.push(p + "=" + KUtils.toString(formParams[p], ""));
              } else {
                var replaceRege = "{" + p + "}";
                var value = KUtils.toString(formParams[p], "");
                fullurl = fullurl.replace(replaceRege, value);
              }
            } else {
              tmpUrls.push(p + "=" + KUtils.toString(formParams[p], ""));
            }
          }
        }
        var tmpUrlStr = tmpUrls.join("&");
        if (KUtils.strNotBlank(tmpUrlStr)) {
          // 地址栏追加参数
          if (fullurl.indexOf("?") == -1) {
            fullurl = fullurl + "?" + tmpUrlStr;
          } else {
            fullurl = fullurl + "&" + tmpUrlStr;
          }
        }

        if (KUtils.strNotBlank(this.rawText)) {
          try {
            var jobj = JSON.parse(this.rawText);
            var objstr = JSON.stringify(jobj)
              .replace(/\\n/g, "")
              .replace(/"/g, '\\"');
            // console(objstr);
            curlified.push("-d");
            curlified.push('"' + objstr + '"');
          } catch (error) {
            var objstr = this.rawText.replace(/\\n/g, "").replace(/"/g, '\\"');
            curlified.push("-d");
            curlified.push('"' + objstr + '"');
          }
        }
      } else if (this.urlFormFlag) {
        // 判断请求类型是否为get或者delete
        var urlFormParams = this.debugUrlFormParams();
        if (KUtils.checkUndefined(urlFormParams)) {
          var tmpUrls = [];
          // 此处需要判断url是否是path类型
          for (var p in urlFormParams) {
            if (that.debugPathFlag) {
              // 确实是，判断该参数是否出现
              if (that.debugPathParams.indexOf(p) == -1) {
                tmpUrls.push(p + "=" + KUtils.toString(urlFormParams[p], ''));
              } else {
                var replaceRege = "{" + p + "}";
                var value = KUtils.toString(urlFormParams[p], '');
                fullurl = fullurl.replace(replaceRege, value);
              }
            } else {
              tmpUrls.push(p + "=" + KUtils.toString(urlFormParams[p], ''));
            }
          }
          var tmpUrlStr = tmpUrls.join("&");
          if (KUtils.strNotBlank(tmpUrlStr)) {
            if (
              this.api.methodType.toLowerCase() == "get" ||
              this.api.methodType.toLowerCase() == "delete"
            ) {
              // 地址栏追加参数
              if (fullurl.indexOf("?") == -1) {
                fullurl = fullurl + "?" + tmpUrlStr;
              } else {
                fullurl = fullurl + "&" + tmpUrlStr;
              }
            } else {
              // -d 追加参数
              curlified.push("--data-urlencode ");
              curlified.push('"' + tmpUrlStr + '"');
            }
          }
        }
      } else if (this.formFlag) {
        // 此处需要验证是否是文件上传的表单类型
        var params = this.debugFormCurlParams();
        if (KUtils.checkUndefined(params)) {
          if (this.validateFormDataContaintsFile()) {
            // 包含文件
            // headers["Content-Type"] = "multipart/form-data";
            this.formData.forEach(form => {
              if (!form.new) {
                // 判断header是否选中
                var tmphArrs = this.rowFormSelection.selectedRowKeys.filter(
                  rs => rs == form.id
                );
                if (tmphArrs.length > 0) {
                  // 必须选中
                  if (KUtils.strNotBlank(form.name)) {
                    curlified.push("-F ");
                    // 判断类型
                    if (form.type == "text") {
                      curlified.push(
                        '"' +
                        form.name +
                        "=" +
                        KUtils.toString(form.content, "") +
                        '"'
                      );
                    } else {
                      curlified.push(
                        '"' + form.name + "=@" + form.content + '"'
                      );
                    }
                  }
                }
              }
            });
          } else {
            var tmpUrls = [];
            // 此处需要判断url是否是path类型
            for (var p in params) {
              if (that.debugPathFlag) {
                // 确实是，判断该参数是否出现
                if (that.debugPathParams.indexOf(p) == -1) {
                  tmpUrls.push(p + "=" + KUtils.toString(params[p], ''));
                } else {
                  var replaceRege = "{" + p + "}";
                  var value = KUtils.toString(params[p], '');
                  fullurl = fullurl.replace(replaceRege, value);
                }
              } else {
                tmpUrls.push(p + "=" + KUtils.toString(params[p], ''));
              }
            }
            /* for (var p in params) {
              tmpUrls.push(p + "=" + params[p]);
            } */
            var tmpUrlStr = tmpUrls.join("&");
            // console("tmpUrlStr:" + tmpUrlStr);
            if (KUtils.strNotBlank(tmpUrlStr)) {
              if (
                this.api.methodType.toLowerCase() == "get" ||
                this.api.methodType.toLowerCase() == "delete"
              ) {
                // 地址栏追加参数
                if (fullurl.indexOf("?") == -1) {
                  fullurl = fullurl + "?" + tmpUrlStr;
                } else {
                  fullurl = fullurl + "&" + tmpUrlStr;
                }
              } else {
                // -d 追加参数
                curlified.push("--data-urlencode ");
                curlified.push('"' + tmpUrlStr + '"');
              }
            }
          }
        }
      }
      // 此处url需要encoding
      curlified.push('"' + encodeURI(fullurl) + '"');
      this.responseCurlText = curlified.join(" ");
    },
    debugFormCurlParams() {
      var params = {};
      this.formData.forEach(form => {
        if (!form.new) {
          // 判断header是否选中
          var tmphArrs = this.rowFormSelection.selectedRowKeys.filter(
            rs => rs == form.id
          );
          if (tmphArrs.length > 0) {
            // 必须选中
            if (KUtils.strNotBlank(form.name)) {
              params[form.name] = form.content;
            }
          }
        }
      });
      return params;
    },
    setResponseBody(res) {
      // console.log("成功");
      // console.log(res);
      let that = this;
      if (KUtils.checkUndefined(res)) {
        var resp = res.request;
        // console.log(res);
        var headers = res.headers;
        if (KUtils.checkUndefined(resp)) {
          var ctype = KUtils.propValue("content-type", headers, "");
          // 判断是否是blob类型
          var contentDisposition = KUtils.propValue("content-disposition", headers, "");
          if (resp.responseType == "blob" || KUtils.strNotBlank(contentDisposition)) {
            // 针对OpenAPI3的规范,由于统一添加了blob类型，此处需要判断
            if (res.data.type == "application/json" ||
              res.data.type == "application/xml" ||
              res.data.type == "text/html" ||
              res.data.type == "text/plain") {
              // 服务端返回JSON数据,Blob对象转为JSON
              const reader = new FileReader();
              reader.onload = e => {
                // let message=KUtils.json5parse(e.target.result);
                // console.log(e.target.result);
                // 重新赋值
                let readerResponse = {
                  responseText: e.target.result,
                  response: e.target.result,
                  responseType: "",
                  status: resp.status,
                  statusText: resp.statusText,
                  readyState: resp.readyState,
                  timeout: resp.timeout,
                  withCredentials: resp.withCredentials
                }
                that.setResponseJsonBody(readerResponse, headers)
              };
              reader.readAsText(res.data);
            } else if (ctype == "text/html" || ctype == "text/plain" || ctype == "application/xml") {
              this.setResponseJsonBody(resp, headers);
            } else {
              let inlineFlag = false;
              // 从响应头中得到文件名称
              var fileName = "Knife4j.txt";
              if (!KUtils.strNotBlank(contentDisposition)) {
                // 如果是空,获取小写的请求头
                contentDisposition = KUtils.propValue(
                  "content-disposition",
                  headers,
                  ""
                );
              }
              if (KUtils.strNotBlank(contentDisposition)) {
                var respcds = contentDisposition.split(";");
                for (var i = 0; i < respcds.length; i++) {
                  var header = respcds[i];
                  if (header != null && header != "") {
                    if (header.toLowerCase().indexOf("inline") > -1) {
                      inlineFlag = true;
                    }
                    var headerValu = header.split("=");
                    if (headerValu != null && headerValu.length > 0) {
                      var _hdvalue = headerValu[0];
                      if (
                        _hdvalue != null &&
                        _hdvalue != undefined &&
                        _hdvalue != ""
                      ) {
                        if (
                          _hdvalue.toLowerCase() == "filename*" ||
                          _hdvalue.toLowerCase() == "filename"
                        ) {
                          // 对filename进行decode处理,防止出现中文的情况,去除双引号
                          let tmpHeader = headerValu[1].replace(/\"/g, "");
                          fileName = decodeURIComponent(tmpHeader);
                        }
                      }
                    }
                  }
                }
              }
              // 双重验证,判断是否为图片
              var imageFlag = false;
              if (ctype.indexOf("image") != -1) {
                imageFlag = true;
              } else {
                // 如果contentType非image,判断文件名称
                // png,jpg,jpeg,gif
                var imageArrs = [
                  "bmp",
                  "jpg",
                  "png",
                  "tif",
                  "gif",
                  "pcx",
                  "tga",
                  "exif",
                  "fpx",
                  "svg",
                  "psd",
                  "cdr",
                  "pcd",
                  "dxf",
                  "ufo",
                  "eps",
                  "ai",
                  "raw",
                  "WMF",
                  "webp"
                ];
                imageArrs.forEach(fmt => {
                  if (fileName.endsWith(fmt)) {
                    imageFlag = true;
                  }
                });
              }
              // 最后再判断produces
              var produces = this.api.produces;
              // 判断是否是image
              var imgProduceFlag = false;
              if (KUtils.arrNotEmpty(produces)) {
                produces.forEach(prd => {
                  if (prd.indexOf("image") != -1) {
                    imgProduceFlag = true;
                  }
                })
              }
              if (!imageFlag) {
                imageFlag = imgProduceFlag;
              }
              // console.log(imgProduceFlag);
              // application/octet-stream
              // 判断url是否存在
              if (inlineFlag) {
                //判断content-disposition	inline;filename=f.txt的情况
                //直接解析
                this.setResponseJsonBody(resp, headers);
              } else {
                let downloadurl = "";
                try {
                  downloadurl = window.URL ? window.URL.createObjectURL(res.data) : window.webkitURL.createObjectURL(res.data);
                } catch (e) {
                  // https://gitee.com/xiaoym/knife4j/issues/I5DKE8
                  // 捕获异常
                  if (window.console) {
                    window.console.error(e);
                  }
                  let binaryData = [].concat(res.data);
                  let blobFile = new Blob(binaryData);
                  downloadurl = window.URL ? window.URL.createObjectURL(blobFile) : window.webkitURL.createObjectURL(blobFile);
                }
                // let blobTarget=new Blob([res.data])
                // var downloadurl = window.URL.createObjectURL(blobTarget);
                this.responseContent = {
                  text: "",
                  mode: "blob",
                  blobFlag: true,
                  imageFlag: imageFlag,
                  blobFileName: fileName,
                  blobUrl: downloadurl,
                  base64: ""
                };
              }

            }
          } else {
            this.setResponseJsonBody(resp, headers);
          }
        }
      }
    },
    setResponseJsonBody(resp, headers) {
      // 判断响应的类型
      // var _text = resp.responseText;
      // console.log("setResponseJsonBody")
      // console.log(resp);
      var _text = "";
      var _base64 = "";
      var mode = this.getContentTypeByHeaders(headers);
      // console.log("动态mode-- ---" + mode);
      // console(res);
      if (mode == "json") {
        // _text = KUtils.json5stringify(KUtils.json5parse(_text));
        // 不能使用res.data对象,必须使用stringfy重新转换1次,否则会出现精度丢失的情况
        // _text = KUtils.json5stringify(res.data);
        var responseSize = resp.responseText.gblen();
        var mbSize = (responseSize / 1024).toFixed(1);
        var maxSize = 150;
        // 数据大小
        this.bigBlobFlag = mbSize > 300;
        // console.log(mbSize)
        if (mbSize > maxSize) {
          this.bigFlag = true;
          // _text = resp.responseText;
          // var messageInfo='接口响应数据量超过限制,不在响应内容中显示,请在raw中进行查看';
          var messageInfo = this.i18n.message.debug.contentToBig;
          this.$message.info(messageInfo);
          mode = "text";
          // _text=resp.responseText;
          // console.log("1")
        } else {
          // console.log("2")
          // 此处存在空指针异常
          if (KUtils.strNotBlank(resp.responseText)) {
            try {
              _text = KUtils.json5stringify(KUtils.json5parse(resp.responseText));
            } catch (e) {
              // json处理失败,捕获异常,作为text文本处理
              _text = resp.responseText;
              mode = "text";
            }
          }
          // console.log("2-1")
        }
        if (KUtils.strNotBlank(resp.responseText)) {
          // console.log("3")
          if (!this.bigFlag) {
            // 数据太大,查找缓慢
            if (resp.responseText.indexOf("data:image") > -1) {
              // console.log("3-1.5")
              var base64ImageRegex = new RegExp(".*?\"(data:image.*?base64.*?)\".*", "ig");
              if (base64ImageRegex.test(resp.responseText)) {
                var s = RegExp.$1;
                _base64 = s;
              }

            }
          }
          // console.log("3-1")
        }
        /* if(_text.indexOf("data:image/jpg;base64") > -1) {
            let newStr = _text.substring(_text.indexOf("data:image/jpg;base64"));
            _base64 = newStr.substring(0, newStr.indexOf("\","))
        } */
      } else if (mode == "xml") {
        var tmpXmlText = resp.responseText;
        if (KUtils.strNotBlank(tmpXmlText)) {
          _text = new vkbeautify().xml(tmpXmlText);
        } else {
          _text = tmpXmlText;
        }
        // console.log("4")
      } else {
        // console.log("5")
        _text = resp.responseText;
      }
      // console.log('set value before')
      this.responseContent = {
        text: _text,
        mode: mode,
        blobFlag: false,
        imageFlag: false,
        blobFileName: "",
        blobUrl: "",
        base64: _base64
      };
      // console.log(this.responseContent)
    },
    debugEditorChange(value) {
      // 针对Debug调试框inputchange事件做的处理
      if (KUtils.checkUndefined(this.responseContent)) {
        this.responseContent.text = value;
      }
    },
    getContentTypeByHeaders(headers) {
      // 根据响应请求头判断响应的数据类型,默认JSON
      var mode = "json";
      var contentType = KUtils.propValue("Content-Type", headers, "");
      if (!KUtils.strNotBlank(contentType)) {
        contentType = KUtils.propValue("content-type", headers, "");
      }
      // console("contentType:" + contentType);
      if (KUtils.strNotBlank(contentType)) {
        // 不为空
        if (contentType.indexOf("json") >= 0) {
          mode = "json";
        } else if (contentType.indexOf("xml") >= 0) {
          mode = "xml";
        } else if (contentType.indexOf("text/html") >= 0) {
          mode = "html";
        } else {
          mode = "text";
        }
      }
      return mode;
    },
    debugShowFieldDescriptionChange(flag) {
      this.responseFieldDescriptionChecked = flag;
    }
  }
};
</script>
