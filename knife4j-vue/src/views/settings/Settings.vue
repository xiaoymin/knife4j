<template>
  <a-layout-content class="knife4j-body-content">
    <a-row class="settingConfig">
      <a-row class="gptips" v-html="$t('message.settingTip')">
        启用个性化配置后,接口Tab标签需关闭后重新打开或者刷新当前页面
      </a-row>
    </a-row>
    <a-row class="settingConfig">
      <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox @change="checkboxChange('enableRequestCache')" :checked="settings.enableRequestCache"><span
              v-html="$t('settings.openCache')"></span></a-checkbox>
        </a-col>
      </a-row>
      <a-divider class="divider" />
      <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox @change="checkboxChange('enableDynamicParameter')" :checked="settings.enableDynamicParameter">
            <span v-html="$t('settings.dynamicParameter')"></span></a-checkbox>
        </a-col>
      </a-row>
      <a-divider class="divider" />
      <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox @change="checkboxChange('enableFilterMultipartApis')"
            :checked="settings.enableFilterMultipartApis"><span v-html="$t('settings.apiFilter')"></span></a-checkbox>
          <a-select style="width:140px;" @change="filterOptionsChange"
            :value="settings.enableFilterMultipartApiMethodType">
            <a-select-option value="GET">GET</a-select-option>
            <a-select-option value="POST">POST</a-select-option>
            <a-select-option value="PUT">PUT</a-select-option>
            <a-select-option value="DELETE">DELETE</a-select-option>
            <a-select-option value="PATCH">PATCH</a-select-option>
            <a-select-option value="OPTIONS">OPTIONS</a-select-option>
            <a-select-option value="HEAD">HEAD</a-select-option>
          </a-select>
        </a-col>
      </a-row>
      <a-divider class="divider" />
      <!--  <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox @change="checkboxChange('enableCacheOpenApiTable')" :checked="settings.enableCacheOpenApiTable">开启缓存已打开的api文档</a-checkbox>
        </a-col>
      </a-row>
      <a-divider class="divider" /> -->
      <!-- <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox
            @change="checkboxChange('enableSwaggerBootstrapUi')"
            :checked="settings.enableSwaggerBootstrapUi"
            ><span v-html="$t('settings.plus')"></span
          ></a-checkbox>
        </a-col>
      </a-row> -->
      <a-divider class="divider" />
      <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox @change="checkboxChange('enableHost')" :checked="settings.enableHost"><span>Host:
              <a-input @change="hostChange" style="width:300px;" :value="settings.enableHostText" />
            </span></a-checkbox>
        </a-col>
      </a-row>
      <a-divider class="divider" />
      <!-- <a-row class="content-line">
        <a-col :span="24" style="text-align:center;">
          <a-button type="primary" @click="saveSettings">保存配置</a-button>
        </a-col>
      </a-row>
      <a-divider class="divider" /> -->
    </a-row>
  </a-layout-content>
</template>
<script>
import Constants from "@/store/constants";
let localStore = null;
let instance = null;

export default {
  props: {
    data: {
      type: Object
    }
  },
  computed: {
    swaggerCurrentInstance() {
      return this.$store.state.globals.swaggerCurrentInstance;
    }, language() {
      return this.$store.state.globals.language;
    }
  },
  data() {
    return {
      settings: Constants.defaultSettings,
      hostValue: '',
      labelCol: {
        xs: { span: 21 },
        sm: { span: 8 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 }
      }
    };
  },
  beforeCreate() {
    instance = this;
    localStore = this.$localStore;
    instance.$message.config({
      top: "100px"
    });
  },
  created() {
    // 读取本地缓存信息,判断是否存在,如果存在即初始化赋值
    var sysHost = this.swaggerCurrentInstance.host;
    localStore.getItem(Constants.globalSettingsKey).then(function (val) {
      if (val != null) {
        // 向下兼容,判断是否包含动态参数的配置
        if (
          val["enableDynamicParameter"] == undefined ||
          val["enableDynamicParameter"] == null
        ) {
          val["enableDynamicParameter"] = false;
        }
        instance.settings = val;
        // 判断释放包含host
        if (!val.hasOwnProperty("enableHost")) {
          // 包含
          instance.settings.enableHost = false;
          instance.settings.enableHostText = sysHost;
        } else {
          if (instance.settings.enableHostText == null || instance.settings.enableHostText == '' || instance.settings.enableHostText == undefined) {
            instance.settings.enableHostText = sysHost;
          }
        }
      } else {
        // 赋予系统默认值
        instance.settings.enableHostText = sysHost;

        localStore.setItem(Constants.globalSettingsKey, instance.settings);
      }
    });
  },
  methods: {
    getCurrentI18nInstance() {
      return this.$i18n.messages[this.language];
    },
    hostChange(e) {
      var value = e.target.value;
      this.settings.enableHostText = value;
      this.saveSettingForLocal();
    },
    checkboxChange(field) {
      const ckStatus = this.settings[field];
      // 需要判断是否存在,向下兼容
      if (ckStatus != undefined && ckStatus != null) {
        this.settings[field] = !ckStatus;
      } else {
        this.settings[field] = true;
      }
      // 判断是否开启增强
      if (field == "enableSwaggerBootstrapUi") {
        if (this.settings.enableSwaggerBootstrapUi) {
          this.validateKnife4j();
        } else {
          this.saveSettingForLocal();
        }
      } else if (field == "enableHost") {
        if (this.settings.enableHost) {
          this.validateHost();
        } else {
          this.saveSettingForLocal();
        }
      } else {
        this.saveSettingForLocal();
      }
    },
    filterOptionsChange(value) {
      this.settings.enableFilterMultipartApiMethodType = value;
      this.saveSettingForLocal();
    },
    saveSettingForLocal() {
      localStore.setItem(Constants.globalSettingsKey, instance.settings);
    },
    validateHost() {
      // 校验Host,校验规则如下：
      // 1.Host不能为空
      // 1.如果是http开头,不能为空
      var hostValue = this.settings.enableHostText;
      if (hostValue == null || hostValue == '' || hostValue == undefined) {
        this.settings.enableHost = false;
        this.$message.error('Please Check Host Value');
        return false;
      }
      var i18n = this.getCurrentI18nInstance();
      this.$message.success(i18n.message.settingHost, 2);
      this.saveSettingForLocal();
    },
    validateKnife4j() {
      // 如果开启增强,请求验证
      var api = this.data.instance.extUrl;
      var idx = api.indexOf("/");
      if (idx == 0) {
        api = api.substr(1);
      }
      // api = "/tx/fff/www/xx";
      // console("验证api地址：" + api);
      var flag = true;
      this.$axios({
        url: api,
        dataType: "json",
        type: "get"
      })
        .then(function (data) {
          if (data != null) {
            if (data.hasOwnProperty("swaggerBootstrapUi")) {
              var sbu = data["swaggerBootstrapUi"];
              if (sbu != null && sbu != undefined) {
                if (sbu.hasOwnProperty("errorMsg")) {
                  // 升级后1.8.9的属性
                  var em = sbu["errorMsg"];
                  if (em != null && em != undefined && em != "") {
                    var errMsg =
                      Constants.i18n.zh.message.settings.plusError + em;
                    instance.$message.info(errMsg);
                    flag = false;
                  }
                }
              }
            }
          }
          instance.settings.enableSwaggerBootstrapUi = flag;
          instance.saveSettingForLocal();
        })
        .catch(function (error) {
          instance.$message.info(Constants.i18n.zh.message.settings.plusFail);
          flag = false;
          instance.settings.enableSwaggerBootstrapUi = flag;
          instance.saveSettingForLocal();
        });
    },
    saveSettings() {
      // 判断是否过滤
      if (!this.settings.enableFilterMultipartApis) {
        // 未开启过滤的情况下还是POST
        this.settings.enableFilterMultipartApiMethodType = "POST";
      }
      // 是否增强
      if (this.settings.enableSwaggerBootstrapUi) {
        // 如果开启增强,请求验证
        var api = this.data.instance.extUrl;
        var idx = api.indexOf("/");
        if (idx == 0) {
          api = api.substr(1);
        }
        api = "/tx/fff/www/xx";
        // console("验证api地址：" + api);
        var flag = true;
        this.$axios({
          url: api,
          dataType: "json",
          type: "get"
        })
          .then(function (data) {
            if (data != null) {
              if (data.hasOwnProperty("swaggerBootstrapUi")) {
                var sbu = data["swaggerBootstrapUi"];
                if (sbu != null && sbu != undefined) {
                  if (sbu.hasOwnProperty("errorMsg")) {
                    // 升级后1.8.9的属性
                    var em = sbu["errorMsg"];
                    if (em != null && em != undefined && em != "") {
                      var errMsg =
                        Constants.i18n.zh.message.settings.plusError + em;
                      instance.$message.info(errMsg);
                      flag = false;
                    }
                  }
                }
              }
            }
            instance.settings.enableSwaggerBootstrapUi = flag;
            instance.saveSettingForLocal(flag);
          })
          .catch(function (error) {
            instance.$message.info(Constants.i18n.zh.message.settings.plusFail);
            flag = false;
            instance.settings.enableSwaggerBootstrapUi = flag;
            instance.saveSettingForLocal(flag);
          });
      } else {
        instance.saveSettingForLocal(true);
      }
    }
  }
};
</script>

<style scoped>
.settingConfig {
  width: 80%;
  margin: 20px auto;
}

.settingConfig .gptips {
  color: #31708f;
  background-color: #d9edf7;
  border-color: #bce8f1;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid transparent;
  border-radius: 4px;
}

.content-line {
  height: 50px;
  line-height: 50px;
}

.divider {
  margin: 4px 0;
}
</style>
