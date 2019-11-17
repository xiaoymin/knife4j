<template>
  <a-layout-content class="knife4j-body-content">
    <a-row class="settingConfig">
      <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox @change="checkboxChange('enableRequestCache')" :checked="settings.enableRequestCache">开启请求参数缓存</a-checkbox>
        </a-col>
      </a-row>
      <a-divider class="divider" />
      <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox @change="checkboxChange('enableFilterMultipartApis')" :checked="settings.enableFilterMultipartApis">开启RequestMapping接口过滤,默认只显示</a-checkbox>
          <a-select style="width:140px;" @change="filterOptionsChange" :value="settings.enableFilterMultipartApiMethodType">
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
      <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox @change="checkboxChange('enableCacheOpenApiTable')" :checked="settings.enableCacheOpenApiTable">开启缓存已打开的api文档</a-checkbox>
        </a-col>
      </a-row>
      <a-divider class="divider" />
      <a-row class="content-line">
        <a-col :span="24">
          <a-checkbox @change="checkboxChange('enableSwaggerBootstrapUi')" :checked="settings.enableSwaggerBootstrapUi">启用Knife4j提供的增强功能</a-checkbox>
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
let settings = {
  showApiUrl: false, //接口api地址不显示
  showTagStatus: false, //分组tag显示description属性,针对@Api注解没有tags属性值的情况
  enableSwaggerBootstrapUi: false, //是否开启swaggerBootstrapUi增强
  treeExplain: true,
  enableFilterMultipartApis: false, //针对RequestMapping的接口请求类型,在不指定参数类型的情况下,如果不过滤,默认会显示7个类型的接口地址参数,如果开启此配置,默认展示一个Post类型的接口地址
  enableFilterMultipartApiMethodType: "POST", //默认保存类型
  enableRequestCache: true, //是否开启请求参数缓存
  enableCacheOpenApiTable: false, //是否开启缓存已打开的api文档
  language: "zh" //默认语言版本
};

export default {
  props: {
    data: {
      type: Object
    }
  },
  data() {
    return {
      settings: settings,
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
    localStore.getItem(Constants.globalSettings).then(function(val) {
      if (val != null) {
        instance.settings = val;
      } else {
        localStore.setItem(Constants.globalSettings, instance.settings);
      }
    });
  },
  methods: {
    checkboxChange(field) {
      const ckStatus = this.settings[field];
      this.settings[field] = !ckStatus;
      //判断是否开启增强
      if (field == "enableSwaggerBootstrapUi") {
        if (this.settings.enableSwaggerBootstrapUi) {
          this.validateKnife4j();
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
      localStore.setItem(Constants.globalSettings, instance.settings);
    },
    validateKnife4j() {
      //如果开启增强,请求验证
      var api = this.data.instance.extUrl;
      var idx = api.indexOf("/");
      if (idx == 0) {
        api = api.substr(1);
      }
      //api = "/tx/fff/www/xx";
      console.log("验证api地址：" + api);
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
        console.log("验证api地址：" + api);
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
  width: 60%;
  margin: 60px auto;
}
.content-line {
  height: 50px;
  line-height: 50px;
}
.divider {
  margin: 4px 0;
}
</style>