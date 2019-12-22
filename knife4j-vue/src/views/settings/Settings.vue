<template>
  <a-layout-content class="knife4j-body-content">
    <a-row class="settingConfig">
      <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox
            @change="checkboxChange('enableRequestCache')"
            :checked="settings.enableRequestCache"
            ><span v-html="$t('settings.openCache')"></span
          ></a-checkbox>
        </a-col>
      </a-row>
      <a-divider class="divider" />
      <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox
            @change="checkboxChange('enableDynamicParameter')"
            :checked="settings.enableDynamicParameter"
            ><span v-html="$t('settings.dynamicParameter')"></span
          ></a-checkbox>
        </a-col>
      </a-row>
      <a-divider class="divider" />
      <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox
            @change="checkboxChange('enableFilterMultipartApis')"
            :checked="settings.enableFilterMultipartApis"
            ><span v-html="$t('settings.apiFilter')"></span
          ></a-checkbox>
          <a-select
            style="width:140px;"
            @change="filterOptionsChange"
            :value="settings.enableFilterMultipartApiMethodType"
          >
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
      <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox
            @change="checkboxChange('enableSwaggerBootstrapUi')"
            :checked="settings.enableSwaggerBootstrapUi"
            ><span v-html="$t('settings.plus')"></span
          ></a-checkbox>
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
  data() {
    return {
      settings: Constants.defaultSettings,
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
    //读取本地缓存信息,判断是否存在,如果存在即初始化赋值
    localStore.getItem(Constants.globalSettingsKey).then(function(val) {
      if (val != null) {
        //向下兼容,判断是否包含动态参数的配置
        if (
          val["enableDynamicParameter"] == undefined ||
          val["enableDynamicParameter"] == null
        ) {
          val["enableDynamicParameter"] = false;
        }
        instance.settings = val;
      } else {
        localStore.setItem(Constants.globalSettingsKey, instance.settings);
      }
    });
  },
  methods: {
    checkboxChange(field) {
      const ckStatus = this.settings[field];
      //需要判断是否存在,向下兼容
      if (ckStatus != undefined && ckStatus != null) {
        this.settings[field] = !ckStatus;
      } else {
        this.settings[field] = true;
      }
      //判断是否开启增强
      if (field == "enableSwaggerBootstrapUi") {
        if (this.settings.enableSwaggerBootstrapUi) {
          this.validateKnife4j();
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
    validateKnife4j() {
      //如果开启增强,请求验证
      var api = this.data.instance.extUrl;
      var idx = api.indexOf("/");
      if (idx == 0) {
        api = api.substr(1);
      }
      //api = "/tx/fff/www/xx";
      //console("验证api地址：" + api);
      var flag = true;
      this.$axios({
        url: api,
        dataType: "json",
        type: "get"
      })
        .then(function(data) {
          if (data != null) {
            if (data.hasOwnProperty("swaggerBootstrapUi")) {
              var sbu = data["swaggerBootstrapUi"];
              if (sbu != null && sbu != undefined) {
                if (sbu.hasOwnProperty("errorMsg")) {
                  //升级后1.8.9的属性
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
        .catch(function(error) {
          instance.$message.info(Constants.i18n.zh.message.settings.plusFail);
          flag = false;
          instance.settings.enableSwaggerBootstrapUi = flag;
          instance.saveSettingForLocal();
        });
    },
    saveSettings() {
      //判断是否过滤
      if (!this.settings.enableFilterMultipartApis) {
        //未开启过滤的情况下还是POST
        this.settings.enableFilterMultipartApiMethodType = "POST";
      }
      //是否增强
      if (this.settings.enableSwaggerBootstrapUi) {
        //如果开启增强,请求验证
        var api = this.data.instance.extUrl;
        var idx = api.indexOf("/");
        if (idx == 0) {
          api = api.substr(1);
        }
        api = "/tx/fff/www/xx";
        //console("验证api地址：" + api);
        var flag = true;
        this.$axios({
          url: api,
          dataType: "json",
          type: "get"
        })
          .then(function(data) {
            if (data != null) {
              if (data.hasOwnProperty("swaggerBootstrapUi")) {
                var sbu = data["swaggerBootstrapUi"];
                if (sbu != null && sbu != undefined) {
                  if (sbu.hasOwnProperty("errorMsg")) {
                    //升级后1.8.9的属性
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
          .catch(function(error) {
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
  margin: 40px auto;
}
.content-line {
  height: 50px;
  line-height: 50px;
}
.divider {
  margin: 4px 0;
}
</style>
