/***
 * swagger-bootstrap-ui v1.9.7 / 2019-11-11 16:10:52
 *
 * Gitee:https://gitee.com/xiaoym/knife4j
 * GitHub:https://github.com/xiaoymin/swagger-bootstrap-ui
 * QQ:621154782
 *
 * Swagger enhanced UI component package
 *
 * Author: xiaoyumin
 * email:xiaoymin@foxmail.com
 * Copyright: 2017 - 2019, xiaoyumin, https://doc.xiaominfo.com/
 *
 * Licensed under Apache License 2.0
 * https://github.com/xiaoymin/swagger-bootstrap-ui/blob/master/LICENSE
 *
 * v1.7.5
 * create by xiaoymin on 2018-7-4 15:32:07
 *
 * 重构swagger-bootstrap-ui组件,为以后动态扩展更高效,扩展接口打下基础
 *
 * v2.0.0
 * modified by xiaoymin on 2019-11-11 16:42:43
 *
 * 基于Vue + Ant Design Vue重构Ui组件
 *
 */
import {
  message
} from 'ant-design-vue'
import md5 from 'js-md5'
import {
  urlToList
} from '@/components/utils/pathTools'
import KUtils from './utils'
import marked from 'marked'
import async from 'async'
import {
  findComponentsByPath,
  findMenuByKey
} from '@/components/utils/Knife4jUtils'
import Constants from '@/store/constants'
import uniqueId from 'lodash/uniqueId'
import isObject from 'lodash/isObject'
import has from 'lodash/has'
import unset from 'lodash/unset'
import isNull from 'lodash/isNull'
import isUndefined from 'lodash/isUndefined'
import xml2js from 'xml2js'

marked.setOptions({
  gfm: true,
  tables: true,
  breaks: false,
  pedantic: false,
  sanitize: false,
  smartLists: true,
  smartypants: false
})

function SwaggerBootstrapUi(options) {
  //swagger请求api地址
  this.url = options.url || '/v3/api-docs/swagger-config'
  this.configUrl = options.configUrl || '/v3/api-docs/swagger-config'
  //用于控制是否请求configUrl的配置
  this.configSupport = options.configSupport || false;
  //用于控制是否请求configSecurityUrl的配置
  this.securitySupport = options.securitySupport || false;
  this.$Vue = options.Vue
  this.plus = options.plus
  //文档id
  this.docId = 'content'
  this.title = 'knife4j'
  this.titleOfUrl = 'https://gitee.com/xiaoym/knife4j'
  this.load = 1
  //tabid
  this.tabId = 'tabUl'
  this.tabContentId = 'tabContent'
  this.searchEleId = 'spanSearch'
  this.searchTxtEleId = 'searchTxt'
  this.menuId = 'menu'
  this.searchMenuId = 'searchMenu'
  //实例分组
  this.instances = []
  //当前分组实例
  this.currentInstance = null
  //全局菜单 add by xiaoymin at 2019-11-30 13:51:39
  //菜单初始化时添加,后期tab切换时用来查找
  this.globalMenuDatas = []
  //动态tab
  this.globalTabId = 'sbu-dynamic-tab'
  this.globalTabs = []
  this.layui = options.layui
  this.ace = options.ace
  this.treetable = options.treetable
  this.layTabFilter = 'admin-pagetabs'
  this.version = '1.9.6'
  this.requestOrigion = 'SwaggerBootstrapUi'
  this.requestParameter = {} //浏览器请求参数
  //个性化配置
  this.settings = {
    showApiUrl: false, //接口api地址不显示
    showTagStatus: false, //分组tag显示description属性,针对@Api注解没有tags属性值的情况
    enableSwaggerBootstrapUi: false, //是否开启swaggerBootstrapUi增强
    treeExplain: true,
    enableFilterMultipartApis: false, //针对RequestMapping的接口请求类型,在不指定参数类型的情况下,如果不过滤,默认会显示7个类型的接口地址参数,如果开启此配置,默认展示一个Post类型的接口地址
    enableFilterMultipartApiMethodType: 'POST', //默认保存类型
    enableRequestCache: true, //是否开启请求参数缓存
    enableCacheOpenApiTable: false, //是否开启缓存已打开的api文档
    language: 'zh' //默认语言版本
  }
  //SwaggerBootstrapUi增强注解地址
  this.extUrl = '/v3/api-docs-ext'
  //验证增强有效地址
  this.validateExtUrl = ''
  //缓存api对象,以区分是否是新的api,存储SwaggerBootstapUiCacheApi对象
  this.cacheApis = []
  this.hasLoad = false
  //add i18n supports by xiaoymin at 2019-4-17 20:27:34
  //this.i18n = new I18n();
  //配置属性 2019-8-28 13:19:35,目前仅支持属性supportedSubmitMethods
  this.configuration = {
    supportedSubmitMethods: [
      'get',
      'put',
      'post',
      'delete',
      'options',
      'head',
      'patch',
      'trace'
    ]
  }
}
/***
 * swagger-bootstrap-ui的main方法,初始化文档所有功能,类似于SpringBoot的main方法
 */
SwaggerBootstrapUi.prototype.main = function () {
  var that = this
  //that.welcome();
  that.initRequestParameters()
  that.initSettings();
  /* that.initUnTemplatePageI18n();
  that.initWindowWidthAndHeight();
  that.initApis();
  that.windowResize(); */
  //2019/08/28 13:16:50 支持configuration接口,主要是相关配置,目前支持属性supportedSubmitMethods(请求调试)
  //接口地址:/swagger-resources/configuration/ui
}

/***
 * 初始化请求参数
 * 开启请求参数缓存：cache=1
 * 菜单Api地址显示: showMenuApi=1
 * 分组tag显示dsecription说明属性: showDes=1
 * 开启RequestMapping接口过滤,默认只显示: filterApi=1  filterApiType=post
 * 开启缓存已打开的api文档:cacheApi=1
 * 启用SwaggerBootstrapUi提供的增强功能:plus=1
 * i18n支持：lang=zh|en
 */
SwaggerBootstrapUi.prototype.initRequestParameters = function () {
  var that = this
  var params = window.location.search
  if (params != undefined && params != '') {
    var notQus = params.substr(1)
    if (notQus != undefined && notQus != null && notQus != '') {
      var pms = notQus.split('&')
      for (var i = 0; i < pms.length; i++) {
        var pm = pms[i]
        if (pm != undefined && pm != null && pm != '') {
          var pmArr = pm.split('=')
          that.requestParameter[KUtils.trim(pmArr[0])] = KUtils.trim(pmArr[1])
        }
      }
    }
  }
  that.log('请求参数========================================')
  that.log(that.requestParameter)
}

/***
 * 读取个性化配置信息
 * modified by xiaoymin at 2019-11-30 20:49:59
 * 个性化配置功能在v1.9.7版本中更新,去掉原来一些复杂无用的配置,通过单页面Settings.vue单组件来对个性化配置进行操作
 * 此处仅作为一个读取初始化的作用
 */
SwaggerBootstrapUi.prototype.initSettings = function () {
  var that = this
  that.log("本地Settings初始化")
  var defaultSettings = Constants.defaultSettings;
  var defaultPlusSettings = Constants.defaultPlusSettings;
  //读取本地变量
  that.$Vue.$localStore.getItem(Constants.globalSettingsKey).then(function (val) {
    //判断是否开启增强
    if (val != undefined && val != null && val != '') {
      if (that.plus) {
        val.enableSwaggerBootstrapUi = defaultPlusSettings.enableSwaggerBootstrapUi
        val.enableRequestCache = defaultPlusSettings.enableRequestCache;
      } //如果本地存在,则使用本地的
      that.settings = val;
    } else {
      if (that.plus) {
        that.settings = defaultPlusSettings;
      } else {
        //判断是否开启增强
        that.settings = defaultSettings;
      }
    }
    that.log("Setings------------------------------")
    that.log(that.settings)
    //本地缓存
    that.$Vue.$localStore.setItem(Constants.globalSettingsKey, that.settings);
    //初始化读取缓存api接口
    that.$Vue.$localStore.getItem(Constants.globalGitApiVersionCaches).then(gitVal => {
      if (KUtils.strNotBlank(gitVal)) {
        //存在值
        that.cacheApis = gitVal;
      } else {
        that.cacheApis = []
      }
      //添加对knife4j-front版本的支持,静态版本不提供配置
      if (that.configSupport) {
        that.configInit()
      }
      //加载分组接口
      that.analysisGroup()
    })

  });



  /* if (window.localStorage) {
    var store = window.localStorage
    var globalSettings = store['SwaggerBootstrapUiSettings']
    if (
      globalSettings != undefined &&
      globalSettings != null &&
      globalSettings != ''
    ) {
      var settings = JSON.parse(globalSettings)
      that.settings = $.extend({}, that.settings, settings)
      that.log('settings-----------------')
      that.log(settings)
    }
  }
  //此处判断浏览器参数
  if (that.requestParameter != null) {
    //开启请求参数缓存：cache=1
    if (checkFiledExistsAndEqStr(that.requestParameter, 'cache', '1')) {
      that.settings.enableRequestCache = true
    }

    //菜单Api地址显示
    if (checkFiledExistsAndEqStr(that.requestParameter, 'showMenuApi', '1')) {
      that.settings.showApiUrl = true
    }

    //分组tag显示dsecription说明属性
    if (checkFiledExistsAndEqStr(that.requestParameter, 'showDes', '1')) {
      that.settings.showTagStatus = true
    }

    //开启RequestMapping接口过滤,默认只显示
    if (checkFiledExistsAndEqStr(that.requestParameter, 'filterApi', '1')) {
      that.settings.enableFilterMultipartApis = true
      //判断是否传了默认类型
      if (that.requestParameter.hasOwnProperty('filterApiType')) {
        var type = that.requestParameter['filterApiType']
        //判断是否在默认类型中
        if (type != undefined && type != null && type != '') {
          var methodArr = [
            'POST',
            'GET',
            'PUT',
            'DELETE',
            'PATCH',
            'OPTIONS',
            'HEAD'
          ]
          if ($.inArray(type.toUpperCase(), methodArr) != -1) {
            that.settings.enableFilterMultipartApiMethodType = type.toUpperCase()
          }
        }
      }
    }

    //开启缓存已打开的api文档
    if (checkFiledExistsAndEqStr(that.requestParameter, 'cacheApi', '1')) {
      that.settings.enableCacheOpenApiTable = true
    }

    //启用SwaggerBootstrapUi提供的增强功能
    if (checkFiledExistsAndEqStr(that.requestParameter, 'plus', '1')) {
      that.settings.enableSwaggerBootstrapUi = true
    }

    //判断语言版本
    if (that.requestParameter.hasOwnProperty('lang')) {
      var currentLanguage = that.i18n.language
      var reqLanguage = that.requestParameter['lang']
      $.each(that.i18n.supports, function (i, sp) {
        if (reqLanguage == sp.lang) {
          currentLanguage = sp.lang
        }
      })
      that.settings.language = currentLanguage
      that.log('当前语言版本')
      that.log(that.settings)
    }
    that.log('参数初始化Settings结束')
    that.log(that.settings)

    if (window.localStorage) {
      var store = window.localStorage
      var gbStr = JSON.stringify(that.settings)
      store.setItem('SwaggerBootstrapUiSettings', gbStr)
    }
  }

  //判断语言
  if (
    that.settings.language != null &&
    that.settings.language != undefined &&
    that.settings.language != ''
  ) {
    //初始化切换
    that.i18n.instance = that.i18n.getSupportLanguage(that.settings.language)
  } */
}

SwaggerBootstrapUi.prototype.initApis = function () {
  var that = this
  if (window.localStorage) {
    var store = window.localStorage
    var cacheApis = store['SwaggerBootstrapUiCacheApis']
    if (cacheApis != undefined && cacheApis != null && cacheApis != '') {
      var settings = JSON.parse(cacheApis)
      that.cacheApis = settings
    } else {
      that.cacheApis = []
    }
  }
}

/**
 * Swagger配置信息加载
 */
SwaggerBootstrapUi.prototype.configInit = function () {
  var that = this
  that.$Vue
    .$axios({
      url: that.configUrl,
      type: 'get',
      timeout: 20000,
      dataType: 'json'
    })
    .then(function (data) {
      if (
        data != null &&
        data != undefined &&
        data.hasOwnProperty('supportedSubmitMethods')
      ) {
        var originalSupportSubmitMethods = data['supportedSubmitMethods']
        if (originalSupportSubmitMethods.length > 0) {
          var newSupports = []
          originalSupportSubmitMethods.forEach(function (method) {
            newSupports.push(method.toLowerCase())
          })
          that.configuration.supportedSubmitMethods = newSupports
        } else {
          that.configuration.supportedSubmitMethods = []
        }
      }
    }).catch(function (err) {
      //message.error('Knife4j文档请求异常')
      //隐藏config的请求接口错误显示
      that.error(err);
    })
}

/***
 * 调用swagger的分组接口,获取swagger分组信息,包括分组名称,接口url地址,版本号等
 */
SwaggerBootstrapUi.prototype.analysisGroup = function () {
  var that = this
  try {
    that.$Vue
      .$axios({
        url: that.url,
        type: 'get',
        timeout: 20000,
        dataType: 'json'
      })
      .then(function (data) {
        that.analysisGroupSuccess(data)
        //创建分组元素
        that.createGroupElement()
      })
      .catch(function (err) {
        message.error('Knife4j文档请求异常')
        that.error(err)
      })
  } catch (err) {
    that.error(err)
  }
}

/***
 * 请求分组成功处理逻辑
 * @param data
 */
SwaggerBootstrapUi.prototype.analysisGroupSuccess = function (data) {
  var that = this
  that.log('done---------------------------')
  that.log(data)
  that.log('请求成功')
  that.log(data)
  var t = typeof data
  var v3Config = null;
  var groupData = null
  if (t == 'string') {
    v3Config = JSON.parse(data)
  } else {
    v3Config = data
  }
  groupData = v3Config.urls;
  that.log('响应分组json数据')
  that.log(groupData)
  var serviceOptions = [];
  groupData.forEach(function (group) {
    var g = new SwaggerBootstrapUiInstance(
      group.name,
      group.url
    )
    g.url = group.url
    var newUrl = ''
    //此处需要判断basePath路径的情况
    if (group.url != null && group.url != undefined && group.url != '') {
      newUrl = group.url
    } else {
      newUrl = group.location
    }
    var extBasePath = ''
    var idx = newUrl.indexOf('/v3/api-docs')
    if (idx > 0) {
      //增强地址存在basePath
      extBasePath = newUrl.substr(0, idx)
    }
    that.log('增强basePath地址：' + extBasePath)
    //赋值增强地址
    g.extUrl = extBasePath + that.extUrl + '?group=' + group.name
    if (that.validateExtUrl == '') {
      that.validateExtUrl = g.extUrl
    }
    //判断当前分组url是否存在basePath
    if (
      group.basePath != null &&
      group.basePath != undefined &&
      group.basePath != ''
    ) {
      g.baseUrl = group.basePath
    } else {
      g.baseUrl = "/";
    }
    //赋值查找缓存的id
    if (that.cacheApis.length > 0) {
      var cainstance = null
      that.cacheApis.forEach(ca => {
        if (ca.id == g.groupId) {
          cainstance = ca
        }
      })
      /*  $.each(that.cacheApis, function (x, ca) {
         if (ca.id == g.groupId) {
           cainstance = ca
         }
       }) */
      if (cainstance != null) {
        g.firstLoad = false
        //判断旧版本是否包含updatesApi属性
        if (!cainstance.hasOwnProperty('updateApis')) {
          cainstance['updateApis'] = {}
        }
        g.cacheInstance = cainstance
        that.log(g)
        //g.groupApis=cainstance.cacheApis;
      } else {
        g.cacheInstance = new SwaggerBootstrapUiCacheApis({
          id: g.groupId,
          name: g.name
        })
      }
    } else {
      g.cacheInstance = new SwaggerBootstrapUiCacheApis({
        id: g.groupId,
        name: g.name
      })
    }
    //双向绑定
    serviceOptions.push({
      label: g.name,
      value: g.id
    })
    that.instances.push(g)
  })
  that.$Vue.serviceOptions = serviceOptions;
  if (serviceOptions.length > 0) {
    that.$Vue.defaultServiceOption = serviceOptions[0].value;
  }

}

/***
 * 创建swagger分组页面元素
 */
SwaggerBootstrapUi.prototype.createGroupElement = function () {
  var that = this;
  //创建分组flag
  that.log("分组-------------------------------")
  //that.log(that.instances)
  that.log(that.$Vue.$route.params)
  //此处需要根据当前访问hash地址动态设置访问的下拉组
  //待写,是否包含分组名称
  var urlParams = that.$Vue.$route.params;
  if (KUtils.checkUndefined(urlParams)) {
    if (urlParams.hasOwnProperty('groupName')) {
      //是否不为空
      var gpName = urlParams.groupName;
      if (KUtils.checkUndefined(gpName) && gpName != '') {
        let selectInstance = that.selectInstanceByGroupName(gpName);
        that.log("包含分组名称")
        that.log(selectInstance)
        //双向绑定下拉框的服务选项
        that.$Vue.defaultServiceOption = selectInstance.id;
        that.analysisApi(selectInstance);
      } else {
        //默认加载第一个url
        that.analysisApi(that.instances[0]);
      }
    } else {
      //默认加载第一个url
      that.analysisApi(that.instances[0]);
    }
  } else {
    //默认加载第一个url
    that.analysisApi(that.instances[0]);
  }

}

/***
 * 获取当前分组实例
 * @param name
 * @returns {*}
 */
SwaggerBootstrapUi.prototype.selectInstanceByGroupName = function (name) {
  var that = this;
  var instance = null;
  that.instances.forEach(function (group) {
    //})
    //$.each(that.instances,function (i, group) {
    if (group.name == name) {
      instance = group;
      return;
    }
  })
  return instance;
}

/***
 * 加载swagger的分组详情接口
 * @param instance 分组接口请求实例
 */
SwaggerBootstrapUi.prototype.analysisApi = function (instance) {
  var that = this;
  try {
    //赋值
    that.currentInstance = instance;
    if (!that.currentInstance.load) {
      var api = instance.url;
      if (api == undefined || api == null || api == "") {
        api = instance.location;
      }
      //判断是否开启增强功能
      if (that.settings.enableSwaggerBootstrapUi) {
        api = instance.extUrl;
      }
      //这里判断url请求是否已加载过
      //防止出现根路径的情况
      // 每个 group 的 文档接口, 都是由服务器返回的, 这里无需手动处理, 也与nginx的配置无关, 直接使用服务端返回的 url 即可
      // var idx = api.indexOf('/');
      // if (idx == 0) {
      //   api = api.substr(1);
      // }
      //测试
      //api = "jj2.json";
      that.$Vue.$axios({
        url: api,
        dataType: 'json',
        timeout: 20000,
        type: 'get'
      }).then(function (data) {
        that.analysisApiSuccess(data);
      }).catch(function (err) {
        message.error('Knife4j文档请求异常')
        that.error(err);
      })
    } else {
      //that.setInstanceBasicPorperties(null);
      //更新当前缓存security
      //that.updateCurrentInstanceSecuritys();
      that.createDescriptionElement();
      that.createDetailMenu(false);
      that.afterApiInitSuccess();
    }
  } catch (err) {
    that.error(err);
    if (window.console) {
      console.error(err);
    }
  }
}

/**
 * 当swagger-api请求初始化完成后,初始化页面的相关操作
 * 包括搜索、打开地址hash地址、tab事件等等
 */
SwaggerBootstrapUi.prototype.afterApiInitSuccess = function () {
  var that = this;
  //搜索
  //that.searchEvents();
  //tab事件,新版本无此属性
  //that.tabCloseEventsInit();
  //opentab
  that.initOpenTable();
  //hash
  //that.hashInitEvent();
  //init hashMethod
  //地址栏打开api地址
  //新版本默认已实现
  //that.initCurrentHashApi();
}

/***
 * 已经打开的api缓存,下一次刷新时打开
 * 新版本需要通过tabs实现
 */
SwaggerBootstrapUi.prototype.initOpenTable = function () {
  var that = this;
  if (!that.settings.enableCacheOpenApiTable) {
    return
  }
  if (window.localStorage) {
    var store = window.localStorage;
    var cacheApis = store["SwaggerBootstrapUiCacheOpenApiTableApis"] || "{}";
    var settings = JSON.parse(cacheApis);
    var insid = that.currentInstance.groupId;
    var cacheApis = settings[insid] || [];

    if (cacheApis.length > 0) {
      for (var i = 0; i < cacheApis.length; i++) {
        var cacheApi = cacheApis[i];
        that.log(cacheApi)
        //var xx=that.getMenu().find(".menuLi[lay-id='"+cacheApi.tabId+"']");
        //xx.trigger("click");
      }
    }
  }
}

/**
 * 接口请求api成功时的操作
 */
SwaggerBootstrapUi.prototype.analysisApiSuccess = function (data) {
  var that = this;
  that.hasLoad = true;
  that.log(data);
  var t = typeof (data);
  var menu = null;
  if (t == 'string') {
    menu = JSON.parse(data);
  } else {
    menu = data;
  }
  that.setInstanceBasicPorperties(menu);
  that.analysisDefinition(menu);
  //DApiUI.definitions(menu);
  that.log(menu);
  that.createDescriptionElement();
  //当前实例已加载
  that.currentInstance.load = true;
  //创建swaggerbootstrapui主菜单
  that.createDetailMenu(true);
  //opentab
  //that.initOpenTable();

  //that.afterApiInitSuccess();

}

/***
 * 更新当前实例的security对象
 */
SwaggerBootstrapUi.prototype.updateCurrentInstanceSecuritys = function () {
  var that = this;
  if (that.currentInstance.securityArrs != null && that.currentInstance.securityArrs.length > 0) {
    //判断是否有缓存cache值
    //var cacheSecurityData=$("#sbu-header").data("cacheSecurity");
    var cacheSecurityData = that.getSecurityStores();
    if (cacheSecurityData != null && cacheSecurityData != undefined) {
      cacheSecurityData.forEach(function (ca) {
        //})
        //$.each(cacheSecurityData,function (i, ca) {
        that.currentInstance.securityArrs.forEach(function (sa) {
          //})
          //$.each(that.currentInstance.securityArrs,function (j, sa) {
          if (ca.key == sa.key && ca.name == sa.name) {
            sa.value = ca.value;
          }
        })
      })

    }
  }
}

/***
 * 从localStorage对象中获取
 */
SwaggerBootstrapUi.prototype.getSecurityStores = function () {
  var csys = null;
  if (window.localStorage) {
    var store = window.localStorage;
    var cacheSecuritys = store["SwaggerBootstrapUiCacheSecuritys"];
    if (cacheSecuritys != undefined && cacheSecuritys != null && cacheSecuritys != "") {
      var settings = JSON.parse(cacheApis);
      csys = settings;
    }
  }
  return csys;
}

/***
 * 基础实例赋值
 * @param menu
 */
SwaggerBootstrapUi.prototype.setInstanceBasicPorperties = function (menu) {
  var that = this;
  var title = '',
    description = '',
    name = '',
    version = '',
    termsOfService = '';
  var servers = KUtils.getValue(menu, "servers", [], true);
  var host = (servers.length ? "" : servers[0].url);
  if (menu != null && menu != undefined) {
    if (menu.hasOwnProperty("info")) {
      var info = menu.info;
      title = KUtils.getValue(info, "title", '', true);
      description = KUtils.getValue(info, "description", "", true);
      if (info.hasOwnProperty("contact")) {
        var contact = info["contact"];
        name = KUtils.getValue(contact, "name", "", true);
      }
      version = KUtils.getValue(info, "version", "", true);
      termsOfService = KUtils.getValue(info, "termsOfService", "", true);
    }
    that.currentInstance.host = host;
    that.currentInstance.title = title;
    //impl markdown syntax
    that.currentInstance.description = marked(description);
    that.currentInstance.contact = name;
    that.currentInstance.version = version;
    that.currentInstance.termsOfService = termsOfService;
    that.currentInstance.basePath = "/";
  } else {
    title = that.currentInstance.title;
  }
}

/***
 * 解析实例属性
 */
SwaggerBootstrapUi.prototype.analysisDefinition = function (menu) {
  var that = this;
  //解析definition
  if (menu != null && typeof (menu) != "undefined" && menu != undefined && menu.hasOwnProperty("components")) {
    var schemas = menu["components"].schemas;
    schemas = (schemas ? schemas : {});
    //改用async的for循环
    for (var name in schemas) {
      var swud = new SwaggerBootstrapUiDefinition();
      swud.name = name;
      swud.ignoreFilterName = name;
      //that.log("开始解析Definition:"+name);
      //获取value
      var value = schemas[name];
      if (KUtils.checkUndefined(value)) {
        swud.description = KUtils.propValue("description", value, "");
        swud.type = KUtils.propValue("type", value, "");
        swud.title = KUtils.propValue("title", value, "");
        //判断是否有required属性
        if (value.hasOwnProperty("required")) {
          swud.required = value["required"];
        }
        //是否有properties
        if (value.hasOwnProperty("properties")) {
          var properties = value["properties"];
          var defiTypeValue = {};
          for (var property in properties) {
            var propobj = properties[property];
            //判断是否包含readOnly属性
            if (!propobj.hasOwnProperty("readOnly") || !propobj["readOnly"]) {}
            var spropObj = new SwaggerBootstrapUiProperty();
            //赋值readOnly属性
            if (propobj.hasOwnProperty("readOnly")) {
              spropObj.readOnly = propobj["readOnly"];
            }
            spropObj.name = property;
            spropObj.originProperty = propobj;
            spropObj.type = KUtils.propValue("type", propobj, "string");
            spropObj.description = KUtils.propValue("description", propobj, "");
            //判断是否包含枚举
            if (propobj.hasOwnProperty("enum")) {
              spropObj.enum = propobj["enum"];
              if (spropObj.description != "") {
                spropObj.description += ",";
              }
              spropObj.description = spropObj.description + "可用值:" + spropObj.enum.join(",");
            }
            if (spropObj.type == "string") {
              spropObj.example = String(KUtils.propValue("example", propobj, ""));
            } else {
              spropObj.example = KUtils.propValue("example", propobj, "");
            }

            spropObj.format = KUtils.propValue("format", propobj, "");
            spropObj.required = KUtils.propValue("required", propobj, false);
            if (swud.required.length > 0) {
              //有required属性,需要再判断一次
              if (swud.required.indexOf(spropObj.name) > -1) {
                //if($.inArray(spropObj.name,swud.required)>-1){
                //存在
                spropObj.required = true;
              }
            }
            //默认string类型
            var propValue = "";
            //判断是否有类型
            if (propobj.hasOwnProperty("type")) {
              var type = propobj["type"];
              //判断是否有example
              if (propobj.hasOwnProperty("example")) {
                if (type == "string") {
                  propValue = String(KUtils.propValue("example", propobj, ""));
                } else {
                  propValue = propobj["example"];
                }
              } else if (KUtils.checkIsBasicType(type)) {
                propValue = KUtils.getBasicTypeValue(type);
                //此处如果是object情况,需要判断additionalProperties属性的情况
                if (type == "object") {
                  if (propobj.hasOwnProperty("additionalProperties")) {
                    var addpties = propobj["additionalProperties"];
                    that.log("------解析map-=-----------additionalProperties,defName:" + name);
                    //判断是否有ref属性,如果有,存在引用类,否则默认是{}object的情况
                    if (addpties.hasOwnProperty("$ref")) {
                      var adref = addpties["$ref"];
                      var regex = new RegExp("#/components/schemas/(.*)$", "ig");
                      if (regex.test(adref)) {
                        var addrefType = RegExp.$1;
                        var addTempValue = null;
                        //这里需要递归判断是否是本身,如果是,则退出递归查找
                        var globalArr = new Array();
                        //添加类本身
                        globalArr.push(name);

                        if (addrefType != name) {
                          addTempValue = that.findRefDefinition(addrefType, schemas, false, globalArr);
                        } else {
                          addTempValue = that.findRefDefinition(addrefType, schemas, true, name, globalArr);
                        }
                        propValue = {
                          "additionalProperties1": addTempValue
                        }
                        that.log("解析map-=完毕：")
                        that.log(propValue);
                        spropObj.type = addrefType;
                        spropObj.refType = addrefType;
                      }
                    } else if (addpties.hasOwnProperty("items")) {
                      //数组
                      var addPropItems = addpties["items"];

                      var adref = addPropItems["$ref"];
                      var regex = new RegExp("#/components/schemas/(.*)$", "ig");
                      if (regex.test(adref)) {
                        var addrefType = RegExp.$1;
                        var addTempValue = null;
                        //这里需要递归判断是否是本身,如果是,则退出递归查找
                        var globalArr = new Array();
                        //添加类本身
                        globalArr.push(name);

                        if (addrefType != name) {
                          addTempValue = that.findRefDefinition(addrefType, schemas, false, globalArr);
                        } else {
                          addTempValue = that.findRefDefinition(addrefType, schemas, true, name, globalArr);
                        }
                        var tempAddValue = new Array();
                        tempAddValue.push(addTempValue);
                        propValue = {
                          "additionalProperties1": tempAddValue
                        }
                        that.log("解析map-=完毕：")
                        that.log(propValue);
                        spropObj.type = "array";
                        spropObj.refType = addrefType;
                      }
                    }
                  }
                }
              } else {
                if (type == "array") {
                  propValue = new Array();
                  var items = propobj["items"];
                  var ref = items["$ref"];
                  //此处有可能items是array
                  if (items.hasOwnProperty("type")) {
                    if (items["type"] == "array") {
                      ref = items["items"]["$ref"];
                    }
                  }
                  //判断是否存在枚举
                  if (items.hasOwnProperty("enum")) {
                    if (spropObj.description != "") {
                      spropObj.description += ",";
                    }
                    spropObj.description = spropObj.description + "可用值:" + items["enum"].join(",");
                  }
                  var regex = new RegExp("#/components/schemas/(.*)$", "ig");
                  if (regex.test(ref)) {
                    var refType = RegExp.$1;
                    spropObj.refType = refType;
                    //这里需要递归判断是否是本身,如果是,则退出递归查找
                    var globalArr = new Array();
                    //添加类本身
                    globalArr.push(name);
                    if (refType != name) {
                      propValue.push(that.findRefDefinition(refType, schemas, false, globalArr));
                    } else {
                      propValue.push(that.findRefDefinition(refType, schemas, true, name, globalArr));
                    }
                  } else {
                    //schema基础类型显示
                    spropObj.refType = items["type"];
                  }
                }
              }

            } else {
              //that.log("解析属性："+property);
              //that.log(propobj);
              if (propobj.hasOwnProperty("$ref")) {
                var ref = propobj["$ref"];
                var regex = new RegExp("#/components/schemas/(.*)$", "ig");
                if (regex.test(ref)) {
                  var refType = RegExp.$1;
                  spropObj.refType = refType;
                  //这里需要递归判断是否是本身,如果是,则退出递归查找
                  var globalArr = new Array();
                  //添加类本身
                  globalArr.push(name);
                  if (refType != name) {
                    propValue = that.findRefDefinition(refType, schemas, false, globalArr);
                  } else {
                    propValue = that.findRefDefinition(refType, schemas, true, globalArr);
                  }

                }
              } else {
                propValue = {};
              }
            }
            spropObj.value = propValue;
            //判断是否有format,如果是integer,判断是64位还是32位
            if (spropObj.format != null && spropObj.format != undefined && spropObj.format != "") {
              //spropObj.type=spropObj.format;
              spropObj.type += "(" + spropObj.format + ")";
            }
            //判断最终类型
            if (spropObj.refType != null && spropObj.refType != "") {
              //判断基础类型,非数字类型
              if (spropObj.type == "string") {
                spropObj.type = spropObj.refType;
              }
            }
            //addprop
            //这里判断去重
            if (!that.checkPropertiesExists(swud.properties, spropObj)) {
              swud.properties.push(spropObj);
              //如果当前属性readOnly=true，则实体类value排除此属性的值
              if (!spropObj.readOnly) {
                defiTypeValue[property] = propValue;
              }
            }
          }
          swud.value = defiTypeValue;
        }
      }
      ////console("开始递归---------------deepTreeTableRefParameter")
      deepTreeTableRefParameter(swud, that, swud, swud);
      ////console(swud)
      that.currentInstance.difArrs.push(swud);
    }
  }
  //解析tags标签
  if (menu != null && typeof (menu) != "undefined" && menu != undefined && menu.hasOwnProperty("tags")) {
    var tags = menu["tags"];
    //判断是否开启增强配置
    if (that.settings.enableSwaggerBootstrapUi) {
      var sbu = menu["swaggerBootstrapUi"]
      if (KUtils.checkUndefined(sbu)) {
        if (KUtils.checkUndefined(sbu.tagSortLists)) {
          tags = sbu["tagSortLists"];
        }
      }
    }
    tags.forEach(function (tag) {
      var swuTag = new SwaggerBootstrapUiTag(tag.name, tag.description);
      if (KUtils.strNotBlank(tag.author)) {
        swuTag.author = tag.author;
      }
      that.currentInstance.tags.push(swuTag);
    })
  }
  //解析paths属性
  if (menu != null && typeof (menu) != "undefined" && menu != undefined && menu.hasOwnProperty("paths")) {
    var paths = menu["paths"];
    that.log("开始解析Paths.................")
    that.log(new Date().toTimeString());
    var pathStartTime = new Date().getTime();
    var _supportMethods = ["get", "post", "put", "delete", "patch", "options", "trace", "head", "connect"];
    async.forEachOf(paths, function (pathObject, path, callback) {
      //var pathObject=paths[path];
      var apiInfo = null;
      _supportMethods.forEach(function (method) {
        if (pathObject.hasOwnProperty(method)) {
          apiInfo = pathObject[method]
          if (apiInfo != null) {
            var ins = that.createApiInfoInstance(path, method, apiInfo);
            that.currentInstance.paths.push(ins);
            ins.hashCollections.forEach(function (hashurl) {
              that.currentInstance.pathsDictionary[hashurl] = ins;
            })
            that.methodCountAndDown(method.toUpperCase());
          }
        }
      })

    })
    that.log("解析Paths结束,耗时：" + (new Date().getTime() - pathStartTime));
    that.log(new Date().toTimeString());
    //判断是否开启过滤
    if (that.settings.enableFilterMultipartApis) {
      //开启过滤
      that.currentInstance.paths.forEach(function (methodApi) {
        //判断是否包含
        var p = that.currentInstance.pathFilters[methodApi.url];
        if (p == null || p == undefined) {
          var d = new SwaggerBootstrapUiApiFilter();
          d.methods.push(methodApi);
          that.currentInstance.pathFilters[methodApi.url] = d;
        } else {
          p.methods.push(methodApi);
          that.currentInstance.pathFilters[methodApi.url] = p;
        }
      })
      var newPathArr = new Array();
      that.log(that.currentInstance.pathFilters)
      for (var url in that.currentInstance.pathFilters) {
        var saf = that.currentInstance.pathFilters[url];
        //that.log(url)
        //that.log(saf)
        //that.log(saf.api(that.settings.enableFilterMultipartApiMethodType))
        //that.log("")
        newPathArr = newPathArr.concat(saf.api(that.settings.enableFilterMultipartApiMethodType));
      }
      that.log("重新赋值。。。。。")
      //that.log(that.currentInstance.paths)
      ///that.log(newPathArr)
      //重新赋值
      that.currentInstance.paths = newPathArr;
      //that.log(that.currentInstance.paths)
    }
  }
  //解析securityDefinitions属性
  if (menu != null && typeof (menu) != "undefined" && menu != undefined && menu.hasOwnProperty("securityDefinitions")) {
    var securityDefinitions = menu["securityDefinitions"];
    if (securityDefinitions != null) {
      //判断是否有缓存cache值
      //var cacheSecurityData=$("#sbu-header").data("cacheSecurity");
      //var cacheSecurityData=that.getSecurityInfos();
      var cacheSecurityData = that.getGlobalSecurityInfos();
      var securityArr = new Array();
      for (var j in securityDefinitions) {
        var sdf = new SwaggerBootstrapUiSecurityDefinition();
        var sdobj = securityDefinitions[j];
        sdf.key = j;
        sdf.type = sdobj.type;
        sdf.name = sdobj.name;
        sdf.in = sdobj.in;
        var flag = false;
        if (cacheSecurityData != null && cacheSecurityData != undefined) {
          //存在缓存值,更新当前值,无需再次授权
          cacheSecurityData.forEach(function (sa) {
            //})
            //$.each(cacheSecurityData, function (i, sa) {
            if (sa.key == sdf.key && sa.name == sdf.name) {
              flag = true;
              sdf.value = sa.value;
            }
          })
        }
        /* if (!flag){
            //如果cache不存在,存储
            that.storeGlobalParam(sdf,"securityArrs");
        }*/
        //at 2019-12-7 18:22:01
        //得到主键id端
        var md5StrBefore = sdf.key + sdf.type + sdf.in + sdf.name;
        sdf.id = md5(md5StrBefore);
        securityArr.push(sdf);
        //that.currentInstance.securityArrs.push(sdf);
      }
      if (securityArr.length > 0) {
        that.currentInstance.securityArrs = securityArr;
        that.log("解析securityDefinitions属性--------------------------------------------------------------->")
        /* if (window.localStorage) {
          var store = window.localStorage;
          var storeKey = "SwaggerBootstrapUiSecuritys";
          var _securityValue = store[storeKey];
          that.log(that.currentInstance.name)
          //初始化
          var _secArr = new Array();
          var _key = md5(that.currentInstance.name);
          that.log(_securityValue)
          if (_securityValue != undefined && _securityValue != null && _securityValue != "") {
            that.log("判断：" + _key)
            //有值
            var _secTempArr = JSON.parse(_securityValue);
            var flag = false;
            //判断值是否存在
            _secTempArr.forEach(function (sta) {
              //})
              //$.each(_secTempArr, function (i, sta) {
              if (sta.key == _key) {
                that.log("exists")
                flag = true;
                _secArr.push({
                  key: _key,
                  value: securityArr
                })
              } else {
                _secArr.push(sta)
              }
            })
            if (!flag) {
              _secArr.push({
                key: _key,
                value: securityArr
              })
            }
          } else {
            var _secObject = {
              key: _key,
              value: securityArr
            };
            _secArr.push(_secObject);

          }
          that.log(_secArr)
          //store.setItem("securityArrs",JSON.stringify(securityArr))
          store.setItem(storeKey, JSON.stringify(_secArr))
        } */
      } else {
        //清空缓存
        that.clearSecuritys();
      }
    } else {
      //清空缓存security
      that.clearSecuritys();
    }
  }
  //console.log("分组------------")
  //console.log(that.currentInstance.cacheInstance)
  //tag分组
  that.currentInstance.tags.forEach(function (tag) {
    //})
    //$.each(that.currentInstance.tags, function (i, tag) {
    //如果是第一次加载,则所有api都是新接口,无需判断老新
    if (!that.currentInstance.firstLoad) {
      //判断是否新
      var tagNewApis = false;
      //是否改变
      var tagChangeApis = false;
      //查找childrens
      that.currentInstance.paths.forEach(methodApi => {
        //判断tags是否相同
        methodApi.tags.forEach(tagName => {
          if (tagName == tag.name) {
            //是否存在
            if (!that.currentInstance.cacheInstance.cacheApis.includes(methodApi.id)) {
              //}
              //if ($.inArray(methodApi.id, that.currentInstance.cacheInstance.cacheApis) < 0) {
              tagNewApis = true;
              methodApi.hasNew = true;
            }
            //console.log(methodApi)
            //判断作者
            if (!KUtils.strNotBlank(methodApi.author)) {
              if (KUtils.strNotBlank(tag.author)) {
                methodApi.author = tag.author;
              }
            }
            tag.childrens.push(methodApi);
          }
        })
      })
      if (tagNewApis) {
        tag.hasNew = true;
      } else {
        //不是新接口,判断接口是否变更
        that.currentInstance.paths.forEach(function (methodApi) {
          //})
          //$.each(that.currentInstance.paths, function (k, methodApi) {
          //判断tags是否相同
          methodApi.tags.forEach(function (tagName) {
            // $.each(methodApi.tags, function (x, tagName) {
            if (tagName == tag.name) {
              if (methodApi.hasChanged) {
                //已经存在变更
                tagChangeApis = true;
              }
            }
          })
        })
        tag.hasChanged = tagChangeApis;
      }
    } else {
      //查找childrens
      that.currentInstance.paths.forEach(function (methodApi) {
        //$.each(that.currentInstance.paths, function (k, methodApi) {
        //判断tags是否相同
        methodApi.tags.forEach(function (tagName) {
          //$.each(methodApi.tags, function (x, tagName) {
          if (tagName == tag.name) {
            //判断作者
            if (!KUtils.strNotBlank(methodApi.author)) {
              if (KUtils.strNotBlank(tag.author)) {
                methodApi.author = tag.author;
              }
            }
            tag.childrens.push(methodApi);
          }
        })
      })
    }

    if (that.settings.enableSwaggerBootstrapUi) {
      //排序childrens
      tag.childrens.sort(function (a, b) {
        return a.order - b.order;
      })
    }
  });

  if (that.currentInstance.firstLoad) {
    /*var c=new SwaggerBootstrapUiCacheApis();
    c.id=that.currentInstance.groupId;
    c.name=that.currentInstance.name;
    c.cacheApis=that.currentInstance.groupApis;*/
    //that.cacheApis.push(c);
    //that.currentInstance.cacheInstance.versionFlag=false;
    that.cacheApis.push(that.currentInstance.cacheInstance);
  } else {
    //更新？页面点击后方可更新
    //that.currentInstance.cacheInstance.versionFlag=false;
    //更新当前cacheApi
    if (that.cacheApis.length > 0) {
      that.cacheApis.forEach(function (ca) {
        //})
        //$.each(that.cacheApis, function (j, ca) {
        if (ca.id == that.currentInstance.cacheInstance.id) {
          ca.updateApis = that.currentInstance.cacheInstance.updateApis;
        }
      })
    }
  }

  //当前加入的cacheApi加入localStorage对象中
  that.storeCacheApis();
  //解析models
  //遍历paths属性中的请求以及响应Model参数,存在即加入,否则不加入

  that.log("开始解析refTreetableparameters属性.................")
  that.log(new Date().toTimeString());
  var pathStartTime = new Date().getTime();
  //models的逻辑从这里移除,放到单组件中进行异步加载,解决效率问题

  //自定义文档
  if (that.settings.enableSwaggerBootstrapUi) {
    var sbu = menu["swaggerBootstrapUi"]
    if (KUtils.checkUndefined(sbu)) {
      if (KUtils.checkUndefined(sbu.markdownFiles)) {
        sbu.markdownFiles.forEach(function (md) {
          let key = md5(md.title)
          that.currentInstance.markdownFiles.push({
            ...md,
            id: key
          })
        })
      }
    }
    //that.currentInstance.markdownFiles = sbu.markdownFiles;
  }
  that.log("解析refTreetableparameters结束,耗时：" + (new Date().getTime() - pathStartTime));
  that.log(new Date().toTimeString());

}
/***
 * 清空security
 */
SwaggerBootstrapUi.prototype.clearSecuritys = function () {
  this.$Vue.$localStore.setItem(Constants.globalSecurityParamPrefix, []);
}
/**
 * 处理Models
 * add at 2019-12-11 21:01:46
 */
SwaggerBootstrapUi.prototype.processModels = function () {
  var that = this;
  if (KUtils.checkUndefined(this.currentInstance.refTreeTableModels)) {
    for (var name in that.currentInstance.refTreeTableModels) {
      that.currentInstance.modelNames.push(name);
      var param = that.currentInstance.refTreeTableModels[name];
      var model = new SwaggerBootstrapUiModel(param.id, name);
      if (KUtils.arrNotEmpty(param.params)) {
        param.params.forEach(function (ps) {
          var newparam = {
            ...ps,
            pid: "-1"
          }
          model.data.push(newparam);
          if (ps.schema) {
            //查找当前ps的属性值
            deepTreeTableSchemaModel(model, that.currentInstance.refTreeTableModels, ps, newparam);
          }

        })
      }
      that.currentInstance.models.push(model);
    }
  }
}

/**
 * 递归查找
 * @param {*} model
 * @param {*} treeTableModel
 * @param {*} id
 * @param {*} rootParam
 */
function deepTreeTableSchemaModel(model, treeTableModel, param, rootParam) {
  ////console(model.name)
  if (KUtils.checkUndefined(param.schemaValue)) {
    var schema = treeTableModel[param.schemaValue]
    if (KUtils.checkUndefined(schema)) {
      rootParam.parentTypes.push(param.schemaValue);
      if (KUtils.arrNotEmpty(schema.params)) {
        schema.params.forEach(function (nmd) {
          //childrenparam需要深拷贝一个对象
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
          }
          childrenParam.pid = param.id;
          childrenParam.parentParam = param;
          model.data.push(childrenParam);
          if (childrenParam.schema) {
            //存在schema,判断是否出现过
            if (rootParam.parentTypes.indexOf(childrenParam.schemaValue) == -1) {
              deepTreeTableSchemaModel(model, treeTableModel, childrenParam, rootParam);
            }
          }
        })
      }
    }
  }
}


function findModelChildren(md, modelData) {
  if (modelData != null && modelData != undefined && modelData.length > 0) {
    modelData.forEach(function (nmd) {
      var newnmd = {
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
      }
      if (newnmd.pid == md.id) {
        newnmd.children = [];
        newnmd.childrenIds = [];
        findModelChildren(newnmd, modelData);
        //查找后如果没有,则将children置空
        if (newnmd.children.length == 0) {
          newnmd.children = null;
        }
        //判断是否存在
        if (md.childrenIds.indexOf(newnmd.id) == -1) {
          //不存在
          md.childrenIds.push(newnmd.id);
          md.children.push(newnmd);
        }
      }
    })
  }
}

/***
 * 创建简介页面
 */
SwaggerBootstrapUi.prototype.createDescriptionElement = function () {
  /*var that = this;
   var layui=that.layui;
  var element=layui.element;
  //内容覆盖
  //that.getDoc().html("");
  setTimeout(function () {
      var html = template('SwaggerBootstrapUiIntroScript', that.currentInstance);
      $("#mainTabContent").html("").html(html);
      element.tabChange('admin-pagetabs',"main");
      that.tabRollPage("auto");
  },10) */
}

/***
 * 根据分组id查找实例
 */
SwaggerBootstrapUi.prototype.selectInstanceByGroupId = function (id) {
  var that = this;
  var instance = null;
  //console(that.instances)
  that.instances.forEach(function (group) {
    //})
    //$.each(that.instances, function (i, id) {
    if (group.id == id) {
      instance = group;
      return;
    }
  })
  return instance;
}
/***
 * 创建左侧菜单按钮
 * @param menu
 */
SwaggerBootstrapUi.prototype.createDetailMenu = function (addFlag) {
  var that = this;
  //创建菜单数据
  var menuArr = [];
  that.log(that.currentInstance)
  var groupName = that.currentInstance.name;
  var groupId = that.currentInstance.id;
  //主页
  menuArr.push({
    groupName: groupName,
    groupId: groupId,
    key: 'kmain',
    name: '主页',
    component: 'Main',
    icon: 'icon-home',
    path: 'home',
  })
  //是否有全局参数
  if (that.currentInstance.securityArrs != null && that.currentInstance.securityArrs.length > 0) {
    menuArr.push({
      groupName: groupName,
      groupId: groupId,
      key: 'Authorize' + md5(groupName),
      name: 'Authorize',
      tabName: 'Authorize(' + groupName + ')',
      component: 'Authorize',
      icon: 'icon-authenticationsystem',
      path: 'Authorize/' + groupName,
    })
  }
  //Swagger通用Models add by xiaoyumin 2018-11-6 13:26:45
  menuArr.push({
    groupName: groupName,
    groupId: groupId,
    key: 'swaggerModel' + md5(groupName),
    name: 'Swagger Models',
    component: 'SwaggerModels',
    tabName: 'Swagger Models(' + groupName + ')',
    icon: 'icon-modeling',
    path: 'SwaggerModels/' + groupName,
  })
  //文档管理
  menuArr.push({
    groupName: groupName,
    groupId: groupId,
    key: 'documentManager' + md5(groupName),
    name: '文档管理',
    icon: 'icon-zdlxb',
    path: 'documentManager',
    children: [{
        groupName: groupName,
        groupId: groupId,
        key: 'globalParameters' + md5(groupName),
        name: '全局参数设置',
        tabName: '全局参数设置(' + groupName + ')',
        component: 'GlobalParameters',
        path: 'GlobalParameters-' + groupName
      },
      {
        groupName: groupName,
        groupId: groupId,
        key: 'OfficelineDocument' + md5(groupName),
        name: '离线文档',
        tabName: '离线文档(' + groupName + ')',
        component: 'OfficelineDocument',
        path: 'OfficelineDocument-' + groupName
      },
      {
        groupName: groupName,
        groupId: groupId,
        key: 'Settings' + md5(groupName),
        name: '个性化设置',
        component: 'Settings',
        path: 'Settings'
        // hideInBreadcrumb: true,
        // hideInMenu: true,
      }
    ]
  })
  //自定义文档
  if (that.settings.enableSwaggerBootstrapUi) {
    //如果是启用
    //判断自定义文档是否不为空
    if (that.currentInstance.markdownFiles != null && that.currentInstance.markdownFiles.length > 0) {
      var mdlength = that.currentInstance.markdownFiles.length;
      //存在自定义文档
      var otherMarkdowns = {
        groupName: groupName,
        groupId: groupId,
        key: 'otherMarkdowns',
        name: '其他文档',
        icon: 'icon-APIwendang',
        path: 'otherMarkdowns',
        children: []
      }
      that.currentInstance.markdownFiles.forEach(function (md) {
        var unmdkey = md5(md.title);
        otherMarkdowns.children.push({
          groupName: groupName,
          groupId: groupId,
          key: unmdkey,
          component: 'OtherMarkdown',
          name: md.title,
          path: unmdkey
        })
      })
      menuArr.push(otherMarkdowns);
    }
  }
  //接口文档
  that.currentInstance.tags.forEach(function (tag) {
    //})
    //$.each(that.currentInstance.tags, function (i, tag) {
    var len = tag.childrens.length;
    var _lititle = "";
    if (len == 0) {
      if (that.settings.showTagStatus) {
        _lititle = tag.name + "(" + tag.description + ")";
      } else {
        _lititle = tag.name;
      }
      menuArr.push({
        groupName: groupName,
        groupId: groupId,
        key: md5(_lititle),
        name: _lititle,
        icon: 'icon-APIwendang',
        path: groupName + "/" + tag.name
      })
    } else {
      if (that.settings.showTagStatus) {
        _lititle = tag.name + "(" + tag.description + ")";
      } else {
        _lititle = tag.name;
      }
      var tagMenu = {
        groupName: groupName,
        groupId: groupId,
        key: md5(_lititle),
        name: _lititle,
        icon: 'icon-APIwendang',
        path: groupName + "/" + tag.name,
        hasNew: tag.hasNew || tag.hasChanged,
        children: []
      }
      tag.childrens.forEach(function (children) {
        //})
        //$.each(tag.childrens, function (i, children) {
        var tabSubMenu = {
          groupName: groupName,
          groupId: groupId,
          key: md5(groupName + children.summary + children.operationId),
          name: children.summary,
          description: children.description,
          path: children.operationId,
          component: 'ApiInfo',
          hasNew: children.hasNew || children.hasChanged,
          deprecated: children.deprecated,
          //用于搜索
          url: children.url
        }
        tagMenu.children.push(tabSubMenu);

      })
      menuArr.push(tagMenu);

    }
  })
  ////console(menuArr)
  var mdata = KUtils.formatter(menuArr);
  //添加全局参数
  if (addFlag) {
    that.globalMenuDatas = that.globalMenuDatas.concat(mdata);
  }
  ////console(JSON.stringify(mdata))
  //双向绑定
  that.$Vue.MenuData = mdata;
  that.$Vue.swaggerCurrentInstance = that.currentInstance;
  that.$Vue.$store.dispatch("globals/setMenuData", mdata);
  //设置菜单选中
  that.selectDefaultMenu(mdata);
  that.log("菜单初始化完成...")
}


/***
 * 初始化菜单后,如果当前选择有根据地址打开api地址，则默认选中当前菜单
 */
SwaggerBootstrapUi.prototype.selectDefaultMenu = function (mdata) {
  var that = this;
  var url = that.$Vue.$route.path;
  const pathArr = urlToList(url);
  var m = findComponentsByPath(url, mdata);
  if (pathArr.length == 2) {
    //二级子菜单
    var parentM = findComponentsByPath(pathArr[0], mdata);
    if (parentM != null) {
      that.$Vue.openKeys = [parentM.key];
    }
  } else if (pathArr.length == 3) {
    //三级子菜单
    var parentM = findComponentsByPath(pathArr[1], mdata);
    if (parentM != null) {
      that.$Vue.openKeys = [parentM.key];
    }
  } else {
    if (m != null) {
      that.$Vue.openKeys = [m.key];
    }
  }
  //this.selectedKeys = [this.location.path];
  if (m != null) {
    that.$Vue.selectedKeys = [m.key];
  }

}


/***
 * 判断属性是否已经存在
 * @param properties
 * @param prop
 */
SwaggerBootstrapUi.prototype.checkPropertiesExists = function (properties, prop) {
  var flag = false;
  if (properties != null && properties != undefined && properties.length > 0 && prop != null && prop != undefined) {
    properties.forEach(function (p) {
      if (p.name == prop.name && p.in == prop.in && p.type == prop.type) {
        flag = true;
      }
    })
  }
  return flag;
}
/***
 * 缓存对象
 */
SwaggerBootstrapUi.prototype.storeCacheApis = function () {
  var that = this;
  that.log("缓存对象...storeCacheApis-->")
  /* if (window.localStorage) {
    var store = window.localStorage;
    that.log(that.cacheApis);
    var str = JSON.stringify(that.cacheApis);
    store.setItem("SwaggerBootstrapUiCacheApis", str);
  } */
  that.$Vue.$localStore.setItem(Constants.globalGitApiVersionCaches, that.cacheApis);
}
/***
 * 创建对象实例,返回SwaggerBootstrapUiApiInfo实例
 */
SwaggerBootstrapUi.prototype.createApiInfoInstance = function (path, mtype, apiInfo) {
  var that = this;

  var swpinfo = new SwaggerBootstrapUiApiInfo();
  //添加basePath
  var basePath = that.currentInstance.basePath;
  var newfullPath = that.currentInstance.host;
  var basePathFlag = false;
  //basePath="/addd/";
  if (basePath != "" && basePath != "/") {
    newfullPath += basePath;
    //如果非空,非根目录
    basePathFlag = true;
  }
  newfullPath += path;
  //截取字符串
  var newurl = newfullPath.substring(newfullPath.indexOf("/"));
  //that.log("新的url:"+newurl)
  newurl = newurl.replace("//", "/");
  //判断应用实例的baseurl
  console.log(that.currentInstance.baseUrl)
  if (that.currentInstance.baseUrl != "" && that.currentInstance.baseUrl != "/") {
    newurl = that.currentInstance.baseUrl + newurl;
  }
  var startApiTime = new Date().getTime();
  swpinfo.showUrl = newurl;
  //swpinfo.id="ApiInfo"+Math.round(Math.random()*1000000);
  swpinfo.instanceId = that.currentInstance.id;
  swpinfo.host = that.currentInstance.host;

  swpinfo.url = newurl;
  swpinfo.originalUrl = newurl;

  //new --> https://github.com/xiaoymin/swagger-bootstrap-ui/pull/108
  /*var urlForRealUsage=newurl.replace(/^([^{]+).*$/g, '$1');
  swpinfo.url=urlForRealUsage;
  swpinfo.originalUrl=urlForRealUsage;*/
  swpinfo.basePathFlag = basePathFlag;
  swpinfo.methodType = mtype.toUpperCase();
  //add by xiaoymin 2020-3-11 20:34:39
  // 判断当前接口是否支持调试
  if (KUtils.arrNotEmpty(that.configuration.supportedSubmitMethods)) {
    if (!that.configuration.supportedSubmitMethods.includes(mtype.toLowerCase())) {
      swpinfo.configurationDebugSupport = false;
    }
  }
  //接口id使用MD5策略,缓存整个调试参数到localStorage对象中,供二次调用
  var md5Str = newurl + mtype.toUpperCase();
  swpinfo.id = md5(md5Str);
  swpinfo.versionId = KUtils.md5Id(apiInfo);
  if (apiInfo != null) {
    if (apiInfo.hasOwnProperty("deprecated")) {
      swpinfo.deprecated = apiInfo["deprecated"];
    }
    if (!apiInfo.tags) {
      apiInfo.tags = ['default'];
    }
    // swpinfo.consumes = apiInfo.consumes; // openAPI v3 版本的 consumes 是根据 requestBody 的 key 来决定的, 支持 不同的 consumes 接收不同的body参数
    swpinfo.description = KUtils.getValue(apiInfo, "description", "", true);
    swpinfo.operationId = apiInfo.operationId;
    swpinfo.summary = (apiInfo.summary ? apiInfo.summary : path);
    swpinfo.tags = apiInfo.tags;
    //读取扩展属性x-ignoreParameters
    if (apiInfo.hasOwnProperty("x-ignoreParameters")) {
      var ignoArr = apiInfo["x-ignoreParameters"];
      //忽略参数对象
      swpinfo.ignoreParameters = ignoArr[0];
    }
    //读取扩展属性x-order值
    if (apiInfo.hasOwnProperty("x-order")) {
      swpinfo.order = parseInt(apiInfo["x-order"]);
    }
    //读取扩展属性x-author
    if (apiInfo.hasOwnProperty("x-author")) {
      var xauthor = apiInfo["x-author"];
      if (KUtils.strNotBlank(xauthor)) {
        swpinfo.author = xauthor;
      }
    }
    //operationId
    swpinfo.operationId = KUtils.getValue(apiInfo, "operationId", "", true);
    var _groupName = that.currentInstance.name;
    //设置hashurl
    swpinfo.tags.forEach(function (tag) {
      var _hashUrl = "#/" + _groupName + "/" + tag + "/" + swpinfo.operationId;
      swpinfo.hashCollections.push(_hashUrl);
    })
    // swpinfo.produces = apiInfo.produces; // openAPI v3 版本的 produces 是根据 response content 的 key 来决定的, 支持 不同的 produces 返回不同的 response 内容
    // 解析parameters
    if (apiInfo.hasOwnProperty("parameters")) {
      var pameters = apiInfo["parameters"];
      pameters.forEach(function (m) {
        //})
        //$.each(pameters, function (i, m) {
        var originalName = KUtils.propValue("name", m, "");
        var inType = KUtils.propValue("in", m, "");
        //忽略参数
        //if (swpinfo.ignoreParameters == null || (swpinfo.ignoreParameters != null && !swpinfo.ignoreParameters.hasOwnProperty(originalName))) {
        if (KUtils.filterIgnoreParameters(inType, originalName, swpinfo.ignoreParameters)) {
          var minfo = new SwaggerBootstrapUiParameter();
          minfo.name = originalName;
          minfo.ignoreFilterName = originalName;
          minfo.type = KUtils.propValue("type", m, "");
          minfo.in = inType;
          minfo.require = KUtils.propValue("required", m, false);
          minfo.description = KUtils.replaceMultipLineStr(KUtils.propValue("description", m, ""));
          //add at 2019-12-10 09:20:08  判断请求参数类型是否包含format
          //https://github.com/xiaoymin/swagger-bootstrap-ui/issues/161
          var _format = KUtils.propValue("format", m, "");
          if (KUtils.strNotBlank(_format)) {
            //存在format
            var _rtype = minfo.type + "(" + _format + ")";
            minfo.type = _rtype;
          }
          //判断是否有枚举类型
          if (m.hasOwnProperty("enum")) {
            //that.log("包括枚举类型...")
            //that.log(m.enum);
            minfo.enum = m.enum;
            //that.log(minfo);
            //枚举类型,描述显示可用值
            var avaiableArrStr = m.enum.join(",");
            if (m.description != null && m.description != undefined && m.description != "") {
              minfo.description = m.description + ",可用值:" + avaiableArrStr;
            } else {
              minfo.description = "枚举类型,可用值:" + avaiableArrStr;
            }

          }
          //判断你是否有默认值(后台)
          if (m.hasOwnProperty("default")) {
            minfo.txtValue = m["default"];
          }
          //swagger 2.9.2版本默认值响应X-EXAMPLE的值为2.9.2
          if (m.hasOwnProperty("x-example")) {
            minfo.txtValue = m["x-example"];
          }
          if (m.hasOwnProperty("schema")) {
            //存在schema属性,请求对象是实体类
            minfo.schema = true;
            var schemaObject = m["schema"];
            var schemaType = schemaObject["type"];
            if (schemaType == "array") {
              minfo.type = schemaType;
              var schItem = schemaObject["items"];
              var ref = schItem["$ref"];
              var className = KUtils.getClassName(ref);
              minfo.schemaValue = className;
              var def = that.getDefinitionByName(className);
              if (def != null) {
                minfo.def = def;
                minfo.value = def.value;
                if (def.description != undefined && def.description != null && def.description != "") {
                  minfo.description = KUtils.replaceMultipLineStr(def.description);
                }
              } else {
                var sty = schItem["type"];
                minfo.schemaValue = schItem["type"]
                //此处判断Array的类型,如果
                if (sty == "string") {
                  minfo.value = "";
                }
                if (sty == "integer") {
                  //判断format
                  if (schItem["format"] != undefined && schItem["format"] != null && schItem["format"] == "int32") {
                    minfo.value = 0;
                  } else {
                    minfo.value = 1054661322597744642;
                  }
                }
                if (sty == "number") {
                  if (schItem["format"] != undefined && schItem["format"] != null && schItem["format"] == "double") {
                    minfo.value = 0.5;
                  } else {
                    minfo.value = 0;
                  }
                }
              }
            } else {
              if (schemaObject.hasOwnProperty("$ref")) {
                var ref = m["schema"]["$ref"];
                var className = KUtils.getClassName(ref);
                if (minfo.type != "array") {
                  minfo.type = className;
                }
                minfo.schemaValue = className;
                var def = that.getDefinitionByName(className);
                if (def != null) {
                  minfo.def = def;
                  minfo.value = def.value;
                  if (def.description != undefined && def.description != null && def.description != "") {
                    minfo.description = KUtils.replaceMultipLineStr(def.description);
                  }
                }
              } else {
                //判断是否包含addtionalProperties属性
                if (schemaObject.hasOwnProperty("additionalProperties")) {
                  //判断是否是数组
                  var addProp = schemaObject["additionalProperties"];
                  if (addProp.hasOwnProperty("$ref")) {
                    //object
                    var className = KUtils.getClassName(addProp["$ref"]);
                    if (className != null) {
                      var def = that.getDefinitionByName(className);
                      if (def != null) {
                        minfo.def = def;
                        minfo.value = {
                          "additionalProperties1": def.value
                        };
                        if (def.description != undefined && def.description != null && def.description != "") {
                          minfo.description = KUtils.replaceMultipLineStr(def.description);
                        }
                      }
                    }
                  } else if (addProp.hasOwnProperty("items")) {
                    //数组
                    var addItems = addProp["items"];
                    var className = KUtils.getClassName(addItems["$ref"]);
                    if (className != null) {
                      var def = that.getDefinitionByName(className);
                      if (def != null) {
                        var addArrValue = new Array();
                        addArrValue.push(def.value)
                        minfo.def = def;
                        minfo.value = {
                          "additionalProperties1": addArrValue
                        };
                        if (def.description != undefined && def.description != null && def.description != "") {
                          minfo.description = KUtils.replaceMultipLineStr(def.description);
                        }
                      }
                    }

                  }


                } else {
                  if (schemaObject.hasOwnProperty("type")) {
                    minfo.type = schemaObject["type"];
                  }
                  minfo.value = "";
                }
              }
            }
          }
          if (m.hasOwnProperty("items")) {
            var items = m["items"];
            if (items.hasOwnProperty("$ref")) {
              var ref = items["$ref"];
              var className = KUtils.getClassName(ref);
              //minfo.type=className;
              minfo.schemaValue = className;
              var def = that.getDefinitionByName(className);
              if (def != null) {
                minfo.def = def;
                minfo.value = def.value;
                if (def.description != undefined && def.description != null && def.description != "") {
                  minfo.description = KUtils.replaceMultipLineStr(def.description);
                }
              }
            } else {
              if (items.hasOwnProperty("type")) {
                //minfo.type=items["type"];
                minfo.schemaValue = items["type"];
              }
              minfo.value = "";
            }
          }

          if (minfo.in == 'body') {
            if (isUndefined(minfo.txtValue) || isNull(minfo.txtValue)) {
              // ********************************************************************
              // 改造参数过滤规则，新的规则支持数组嵌套过滤，参考文档：https://www.lodashjs.com/docs/latest#_unsetobject-path
              // 入参方式   参数类型  忽略规则写法                               参数example                                       过滤后的example
              // form      object   ignoreParameters={"key"}                 {key:'', value:''}                               {key:'', value:''}
              // form      object   ignoreParameters={"nodes[0].key"}        {key:'', value:'',nodes:[{key:'', value:''}]}    {key:'', value:'',nodes:[{value:''}]}
              // form      array    ignoreParameters={"[0].key"}             [{key:'', value:''}]                             [{value:''}]
              // body      object   ignoreParameters={"item.key"}            {key:'', value:''}                               {key:'', value:''}
              // body      object   ignoreParameters={"item.nodes[0].key"}   {key:'', value:'',nodes:[{key:'', value:''}]}    {key:'', value:'',nodes:[{value:''}]}
              // body      array    ignoreParameters={"item.[0].key"}        [{key:'', value:''}]                             [{value:''}]
              // ********************************************************************
              const newValue = (() => {
                if (swpinfo.ignoreParameters && isObject(minfo.value)) {
                  const cloneValue = JSON.parse(JSON.stringify(minfo.value)); // 深拷贝对象或数组
                  Object.keys(swpinfo.ignoreParameters || {}).forEach(key => {
                    const ignorePath = key.startsWith(`${originalName}.`) ?
                      key.replace(`${originalName}.`, '') // 处理 body 带参，需要加前缀问题
                      :
                      key;
                    if (has(cloneValue, ignorePath)) {
                      // 使用 lodash.unset 方法移除 newValue 对象中的属性
                      unset(cloneValue, ignorePath);
                    }
                  });
                  return cloneValue;
                }
                return minfo.value;
              })();
              if (isUndefined(newValue) || isNull(newValue)) {
                if (minfo.type === 'array') {
                  minfo.txtValue = JSON.stringify([]);
                }
              } else {
                //如果type是发array类型,判断撒地方是否是integer
                minfo.txtValue = JSON.stringify(minfo.type === 'array' ? [newValue] : newValue, null, "\t");
              }
            }
          }
          //JSR-303 注解支持.
          that.validateJSR303(minfo, m);
          if (!KUtils.checkParamArrsExists(swpinfo.parameters, minfo)) {
            const ignoreParameterKeys = Object.keys(swpinfo.ignoreParameters || {});
            // 处理请求参数表格依然展示忽略参数
            if (!ignoreParameterKeys.includes(originalName)) {
              swpinfo.parameters.push(minfo);
            }
            //判断当前属性是否是schema
            if (minfo.schema) {
              ////console("存在schema------------开始递归")
              ////console(minfo)

              //deepRefParameter(minfo, that, minfo.def, swpinfo);
              minfo.parentTypes.push(minfo.schemaValue);
              //第一层的对象要一直传递
              //deepTreeTableRefParameter(minfo, that, minfo.def, swpinfo);
            }
          }
        }
      })
    }
    //解析requestBody
    if (apiInfo.hasOwnProperty("requestBody")) {
      var requestBodyObj = apiInfo["requestBody"];
      var m = null;
      var consumes = [];
      if (requestBodyObj.hasOwnProperty("content")) {
        for (var consumeType in requestBodyObj["content"]) {
          consumes.push(consumeType);
          if (m == null) {
            m = requestBodyObj["content"][consumeType];
          }
        }
      }
      swpinfo.consumes = consumes;
      var originalName = KUtils.propValue("name", m, "");
      var inType = "body";
      //忽略参数
      //if (swpinfo.ignoreParameters == null || (swpinfo.ignoreParameters != null && !swpinfo.ignoreParameters.hasOwnProperty(originalName))) {
      if (KUtils.filterIgnoreParameters(inType, originalName, swpinfo.ignoreParameters)) {
        var minfo = new SwaggerBootstrapUiParameter();
        minfo.name = originalName;
        minfo.ignoreFilterName = originalName;
        minfo.type = KUtils.propValue("type", m, "");
        minfo.in = inType;
        minfo.require = KUtils.propValue("required", m, false);
        minfo.description = KUtils.replaceMultipLineStr(KUtils.propValue("description", m, ""));
        //add at 2019-12-10 09:20:08  判断请求参数类型是否包含format
        //https://github.com/xiaoymin/swagger-bootstrap-ui/issues/161
        var _format = KUtils.propValue("format", m, "");
        if (KUtils.strNotBlank(_format)) {
          //存在format
          var _rtype = minfo.type + "(" + _format + ")";
          minfo.type = _rtype;
        }
        //判断是否有枚举类型
        if (m.hasOwnProperty("enum")) {
          //that.log("包括枚举类型...")
          //that.log(m.enum);
          minfo.enum = m.enum;
          //that.log(minfo);
          //枚举类型,描述显示可用值
          var avaiableArrStr = m.enum.join(",");
          if (m.description != null && m.description != undefined && m.description != "") {
            minfo.description = m.description + ",可用值:" + avaiableArrStr;
          } else {
            minfo.description = "枚举类型,可用值:" + avaiableArrStr;
          }

        }
        //判断你是否有默认值(后台)
        if (m.hasOwnProperty("default")) {
          minfo.txtValue = m["default"];
        }
        //swagger 2.9.2版本默认值响应X-EXAMPLE的值为2.9.2
        if (m.hasOwnProperty("x-example")) {
          minfo.txtValue = m["x-example"];
        }
        if (m.hasOwnProperty("schema")) {
          //存在schema属性,请求对象是实体类
          minfo.schema = true;
          var schemaObject = m["schema"];
          var schemaType = schemaObject["type"];
          if (schemaType == "array") {
            minfo.type = schemaType;
            var schItem = schemaObject["items"];
            var ref = schItem["$ref"];
            var className = KUtils.getClassName(ref);
            minfo.schemaValue = className;
            var def = that.getDefinitionByName(className);
            if (def != null) {
              minfo.def = def;
              minfo.value = def.value;
              if (def.description != undefined && def.description != null && def.description != "") {
                minfo.description = KUtils.replaceMultipLineStr(def.description);
              }
            } else {
              var sty = schItem["type"];
              minfo.schemaValue = schItem["type"]
              //此处判断Array的类型,如果
              if (sty == "string") {
                minfo.value = "";
              }
              if (sty == "integer") {
                //判断format
                if (schItem["format"] != undefined && schItem["format"] != null && schItem["format"] == "int32") {
                  minfo.value = 0;
                } else {
                  minfo.value = 1054661322597744642;
                }
              }
              if (sty == "number") {
                if (schItem["format"] != undefined && schItem["format"] != null && schItem["format"] == "double") {
                  minfo.value = 0.5;
                } else {
                  minfo.value = 0;
                }
              }
            }
          } else {
            if (schemaObject.hasOwnProperty("$ref")) {
              var ref = m["schema"]["$ref"];
              var className = KUtils.getClassName(ref);
              if (minfo.type != "array") {
                minfo.type = className;
              }
              minfo.schemaValue = className;
              var def = that.getDefinitionByName(className);
              if (def != null) {
                minfo.def = def;
                minfo.value = def.value;
                if (def.description != undefined && def.description != null && def.description != "") {
                  minfo.description = KUtils.replaceMultipLineStr(def.description);
                }
              }
            } else {
              //判断是否包含addtionalProperties属性
              if (schemaObject.hasOwnProperty("additionalProperties")) {
                //判断是否是数组
                var addProp = schemaObject["additionalProperties"];
                if (addProp.hasOwnProperty("$ref")) {
                  //object
                  var className = KUtils.getClassName(addProp["$ref"]);
                  if (className != null) {
                    var def = that.getDefinitionByName(className);
                    if (def != null) {
                      minfo.def = def;
                      minfo.value = {
                        "additionalProperties1": def.value
                      };
                      if (def.description != undefined && def.description != null && def.description != "") {
                        minfo.description = KUtils.replaceMultipLineStr(def.description);
                      }
                    }
                  }
                } else if (addProp.hasOwnProperty("items")) {
                  //数组
                  var addItems = addProp["items"];
                  var className = KUtils.getClassName(addItems["$ref"]);
                  if (className != null) {
                    var def = that.getDefinitionByName(className);
                    if (def != null) {
                      var addArrValue = new Array();
                      addArrValue.push(def.value)
                      minfo.def = def;
                      minfo.value = {
                        "additionalProperties1": addArrValue
                      };
                      if (def.description != undefined && def.description != null && def.description != "") {
                        minfo.description = KUtils.replaceMultipLineStr(def.description);
                      }
                    }
                  }

                }


              } else {
                if (schemaObject.hasOwnProperty("type")) {
                  minfo.type = schemaObject["type"];
                }
                minfo.value = "";
              }
            }
          }
        }
        if (m.hasOwnProperty("items")) {
          var items = m["items"];
          if (items.hasOwnProperty("$ref")) {
            var ref = items["$ref"];
            var className = KUtils.getClassName(ref);
            //minfo.type=className;
            minfo.schemaValue = className;
            var def = that.getDefinitionByName(className);
            if (def != null) {
              minfo.def = def;
              minfo.value = def.value;
              if (def.description != undefined && def.description != null && def.description != "") {
                minfo.description = KUtils.replaceMultipLineStr(def.description);
              }
            }
          } else {
            if (items.hasOwnProperty("type")) {
              //minfo.type=items["type"];
              minfo.schemaValue = items["type"];
            }
            minfo.value = "";
          }
        }

        if (minfo.in == 'body') {
          if (isUndefined(minfo.txtValue) || isNull(minfo.txtValue)) {
            // ********************************************************************
            // 改造参数过滤规则，新的规则支持数组嵌套过滤，参考文档：https://www.lodashjs.com/docs/latest#_unsetobject-path
            // 入参方式   参数类型  忽略规则写法                               参数example                                       过滤后的example
            // form      object   ignoreParameters={"key"}                 {key:'', value:''}                               {key:'', value:''}
            // form      object   ignoreParameters={"nodes[0].key"}        {key:'', value:'',nodes:[{key:'', value:''}]}    {key:'', value:'',nodes:[{value:''}]}
            // form      array    ignoreParameters={"[0].key"}             [{key:'', value:''}]                             [{value:''}]
            // body      object   ignoreParameters={"item.key"}            {key:'', value:''}                               {key:'', value:''}
            // body      object   ignoreParameters={"item.nodes[0].key"}   {key:'', value:'',nodes:[{key:'', value:''}]}    {key:'', value:'',nodes:[{value:''}]}
            // body      array    ignoreParameters={"item.[0].key"}        [{key:'', value:''}]                             [{value:''}]
            // ********************************************************************
            const newValue = (() => {
              if (swpinfo.ignoreParameters && isObject(minfo.value)) {
                const cloneValue = JSON.parse(JSON.stringify(minfo.value)); // 深拷贝对象或数组
                Object.keys(swpinfo.ignoreParameters || {}).forEach(key => {
                  const ignorePath = key.startsWith(`${originalName}.`) ?
                    key.replace(`${originalName}.`, '') // 处理 body 带参，需要加前缀问题
                    :
                    key;
                  if (has(cloneValue, ignorePath)) {
                    // 使用 lodash.unset 方法移除 newValue 对象中的属性
                    unset(cloneValue, ignorePath);
                  }
                });
                return cloneValue;
              }
              return minfo.value;
            })();
            if (isUndefined(newValue) || isNull(newValue)) {
              if (minfo.type === 'array') {
                minfo.txtValue = JSON.stringify([]);
              }
            } else {
              //如果type是发array类型,判断撒地方是否是integer
              minfo.txtValue = JSON.stringify(minfo.type === 'array' ? [newValue] : newValue, null, "\t");
            }
          }
        }
        //JSR-303 注解支持.
        that.validateJSR303(minfo, m);
        if (!KUtils.checkParamArrsExists(swpinfo.parameters, minfo)) {
          const ignoreParameterKeys = Object.keys(swpinfo.ignoreParameters || {});
          // 处理请求参数表格依然展示忽略参数
          if (!ignoreParameterKeys.includes(originalName)) {
            swpinfo.parameters.push(minfo);
          }
          //判断当前属性是否是schema
          if (minfo.schema) {
            ////console("存在schema------------开始递归")
            ////console(minfo)

            //deepRefParameter(minfo, that, minfo.def, swpinfo);
            minfo.parentTypes.push(minfo.schemaValue);
            //第一层的对象要一直传递
            //deepTreeTableRefParameter(minfo, that, minfo.def, swpinfo);
          }
        }
      }

    }
    var definitionType = null;
    var arr = false;
    //解析responsecode
    if (typeof (apiInfo.responses) != 'undefined' && apiInfo.responses != null) {
      var resp = apiInfo.responses;
      var rpcount = 0;
      for (var status in resp) {
        var swaggerResp = new SwaggerBootstrapUiResponseCode();
        var rescrobj = resp[status];
        swaggerResp.code = status;
        swaggerResp.description = rescrobj["description"];
        var rptype = null;
        if (rescrobj.hasOwnProperty("content")) {
          var schema = null;
          var produceTypesObj = rescrobj["content"];
          var produces = [];
          for (var produceType in produceTypesObj) {
            produces.push(produceType);
            var produceTypeSubObj = produceTypesObj[produceType];
            if (typeof produceTypeSubObj !== 'undefined' && produceTypeSubObj.hasOwnProperty("schema")) {
              if (schema == null) {
                schema = produceTypeSubObj.schema;
              }
            }
          }
          swpinfo.produces = produces;
          //单引用类型
          //判断是否是数组类型
          var regex = new RegExp("#/components/schemas/(.*)$", "ig");
          if (schema.hasOwnProperty("$ref")) {
            if (regex.test(schema["$ref"])) {
              var ptype = RegExp.$1;
              swpinfo.responseParameterRefName = ptype;
              swaggerResp.responseParameterRefName = ptype;
              definitionType = ptype;
              rptype = ptype;
              swaggerResp.schema = ptype;
            }
          } else if (schema.hasOwnProperty("type")) {
            var t = schema["type"];
            if (t == "array") {
              arr = true;
              if (schema.hasOwnProperty("items")) {
                var items = schema["items"];
                var itref = items["$ref"];
                //此处需判断items是否数组
                if (items.hasOwnProperty("type")) {
                  if (items["type"] == "array") {
                    itref = items["items"]["$ref"];
                  }
                }
                if (regex.test(itref)) {
                  var ptype = RegExp.$1;
                  swpinfo.responseParameterRefName = ptype;
                  swaggerResp.responseParameterRefName = ptype;
                  definitionType = ptype;
                  rptype = ptype;
                  swaggerResp.schema = ptype;
                }
              }
            } else {
              //判断是否存在properties属性
              if (schema.hasOwnProperty("properties")) {
                swaggerResp.schema = t;
                //自定义类型、放入difarrs对象中
                var swud = new SwaggerBootstrapUiDefinition();
                swud.name = swpinfo.id;
                swud.description = "自定义Schema";
                definitionType = swud.name;
                rptype = swud.name;
                swaggerResp.responseParameterRefName = swud.name;

                var properties = schema["properties"];
                var defiTypeValue = {};
                for (var property in properties) {
                  var spropObj = new SwaggerBootstrapUiProperty();
                  spropObj.name = property;
                  var propobj = properties[property];
                  spropObj.originProperty = propobj;
                  spropObj.type = KUtils.propValue("type", propobj, "string");
                  spropObj.description = KUtils.propValue("description", propobj, "");
                  spropObj.example = KUtils.propValue("example", propobj, "");
                  spropObj.format = KUtils.propValue("format", propobj, "");
                  spropObj.required = KUtils.propValue("required", propobj, false);
                  if (swud.required.length > 0) {
                    //有required属性,需要再判断一次
                    //if ($.inArray(spropObj.name, swud.required) > -1) {
                    if (swud.required.includes(spropObj.name)) {
                      //存在
                      spropObj.required = true;
                    }
                  }
                  //默认string类型
                  var propValue = "";
                  //判断是否有类型
                  if (propobj.hasOwnProperty("type")) {
                    var type = propobj["type"];
                    //判断是否有example
                    if (propobj.hasOwnProperty("example")) {
                      if (type == "string") {
                        propValue = String(KUtils.propValue("example", propobj, ""));
                      } else {
                        propValue = propobj["example"];
                      }
                    } else if (KUtils.checkIsBasicType(type)) {
                      propValue = KUtils.getBasicTypeValue(type);
                    }

                  }
                  spropObj.value = propValue;
                  //判断是否有format,如果是integer,判断是64位还是32位
                  if (spropObj.format != null && spropObj.format != undefined && spropObj.format != "") {
                    //spropObj.type=spropObj.format;
                    spropObj.type += "(" + spropObj.format + ")";
                  }
                  swud.properties.push(spropObj);
                  defiTypeValue[property] = propValue;
                }
                swud.value = defiTypeValue;
                that.currentInstance.difArrs.push(swud);
              } else {
                //判断是否是基础类型
                if (KUtils.checkIsBasicType(t)) {
                  //基础类型
                  swpinfo.responseText = t;
                  swpinfo.responseBasicType = true;

                  //响应状态码的响应内容
                  swaggerResp.responseText = t;
                  swaggerResp.responseBasicType = true;
                }
              }
            }
          }
        }
        if (rptype != null) {
          //查询
          for (var i = 0; i < that.currentInstance.difArrs.length; i++) {
            var ref = that.currentInstance.difArrs[i];
            if (ref.name == rptype) {
              if (arr) {
                var na = new Array();
                na.push(ref.value);
                swaggerResp.responseValue = JSON.stringify(na, null, "\t");
                swaggerResp.responseJson = na;
              } else {
                swaggerResp.responseValue = JSON.stringify(ref.value, null, "\t");
                swaggerResp.responseJson = ref.value;
              }
            }
          }
          //响应参数
          var def = that.getDefinitionByName(rptype);
          if (def != null) {
            if (def.hasOwnProperty("properties")) {
              var props = def["properties"];
              props.forEach(function (p) {
                //})
                //$.each(props, function (i, p) {
                var resParam = new SwaggerBootstrapUiParameter();
                resParam.name = p.name;
                if (!KUtils.checkParamArrsExists(swaggerResp.responseParameters, resParam)) {
                  swaggerResp.responseParameters.push(resParam);
                  resParam.description = KUtils.replaceMultipLineStr(p.description);
                  if (p.type == null || p.type == "") {
                    if (p.refType != null) {
                      if (!KUtils.checkIsBasicType(p.refType)) {
                        resParam.schemaValue = p.refType;
                        resParam.schema = true;
                        //存在引用类型,修改默认type
                        resParam.type = p.refType;
                        var deepDef = that.getDefinitionByName(p.refType);
                        deepResponseRefParameter(swaggerResp, that, deepDef, resParam);
                        resParam.parentTypes.push(p.refType);
                        deepTreeTableResponseRefParameter(swaggerResp, that, deepDef, resParam);
                      }
                    }
                  } else {
                    resParam.type = p.type;
                    if (!KUtils.checkIsBasicType(p.type)) {
                      if (p.refType != null) {
                        if (!KUtils.checkIsBasicType(p.refType)) {
                          resParam.schemaValue = p.refType;
                          resParam.schema = true;
                          //存在引用类型,修改默认type
                          if (p.type != "array") {
                            resParam.type = p.refType;
                          }
                          var deepDef = that.getDefinitionByName(p.refType);
                          deepResponseRefParameter(swaggerResp, that, deepDef, resParam);
                          resParam.parentTypes.push(p.refType);
                          deepTreeTableResponseRefParameter(swaggerResp, that, deepDef, resParam);
                        }
                      } else {
                        resParam.schemaValue = p.type;
                        resParam.schema = true;
                        //存在引用类型,修改默认type
                        resParam.type = p.type;
                        var deepDef = that.getDefinitionByName(p.type);
                        deepResponseRefParameter(swaggerResp, that, deepDef, resParam);
                        resParam.parentTypes.push(p.type);
                        deepTreeTableResponseRefParameter(swaggerResp, that, deepDef, resParam);
                      }
                    }
                  }
                }
              })

            }
          }
        }

        if (swaggerResp.schema != null && swaggerResp.schema != undefined) {
          rpcount = rpcount + 1;
        }
        //判断是否有响应headers
        if (rescrobj.hasOwnProperty("headers")) {
          var _headers = rescrobj["headers"];
          swaggerResp.responseHeaderParameters = new Array();
          for (var _headerN in _headers) {
            var _hv = {
              ..._headers[_headerN],
              name: _headerN,
              id: md5(_headerN),
              pid: "-1"
            }
            /*
            var _hv = $.extend({}, _headers[_headerN], {
              name: _headerN,
              id: md5(_headerN),
              pid: "-1"
            }); */
            swaggerResp.responseHeaderParameters.push(_hv);
          }
          if (status == "200") {
            swpinfo.responseHeaderParameters = swaggerResp.responseHeaderParameters;
          }
        }
        swpinfo.responseCodes.push(swaggerResp);
      }
      swpinfo.multipartResponseSchemaCount = rpcount;
      if (rpcount > 1) {
        swpinfo.multipartResponseSchema = true;
      }
    }

    if (definitionType != null && !swpinfo.multipartResponseSchema) {
      //查询
      for (var i = 0; i < that.currentInstance.difArrs.length; i++) {
        var ref = that.currentInstance.difArrs[i];
        if (ref.name == definitionType) {
          if (arr) {
            var na = new Array();
            na.push(ref.value);
            swpinfo.responseValue = JSON.stringify(na, null, "\t");
            swpinfo.responseJson = na;
          } else {
            swpinfo.responseValue = JSON.stringify(ref.value, null, "\t");
            swpinfo.responseJson = ref.value;
          }
        }
      }
      //响应参数
      var def = that.getDefinitionByName(definitionType);
      if (def != null) {
        if (def.hasOwnProperty("properties")) {
          var props = def["properties"];
          props.forEach(function (p) {
            //})
            //$.each(props, function (i, p) {
            var resParam = new SwaggerBootstrapUiParameter();
            resParam.name = p.name;
            if (!KUtils.checkParamArrsExists(swpinfo.responseParameters, resParam)) {
              swpinfo.responseParameters.push(resParam);
              resParam.description = KUtils.replaceMultipLineStr(p.description);
              if (p.type == null || p.type == "") {
                if (p.refType != null) {
                  if (!KUtils.checkIsBasicType(p.refType)) {
                    resParam.schemaValue = p.refType;
                    resParam.schema = true;
                    //存在引用类型,修改默认type
                    resParam.type = p.refType;
                    var deepDef = that.getDefinitionByName(p.refType);
                    deepResponseRefParameter(swpinfo, that, deepDef, resParam);
                    resParam.parentTypes.push(p.refType);
                    deepTreeTableResponseRefParameter(swpinfo, that, deepDef, resParam);
                  }
                }
              } else {
                resParam.type = p.type;
                if (!KUtils.checkIsBasicType(p.type)) {
                  if (p.refType != null) {
                    if (!KUtils.checkIsBasicType(p.refType)) {
                      resParam.schemaValue = p.refType;
                      //存在引用类型,修改默认type
                      if (p.type != "array") {
                        resParam.type = p.refType;
                      }
                      var deepDef = that.getDefinitionByName(p.refType);
                      deepResponseRefParameter(swpinfo, that, deepDef, resParam);
                      resParam.parentTypes.push(p.refType);
                      deepTreeTableResponseRefParameter(swpinfo, that, deepDef, resParam);
                    }
                  } else {
                    resParam.schemaValue = p.type;
                    //存在引用类型,修改默认type
                    resParam.type = p.type;
                    var deepDef = that.getDefinitionByName(p.type);
                    deepResponseRefParameter(swpinfo, that, deepDef, resParam);
                    resParam.parentTypes.push(p.type);
                    deepTreeTableResponseRefParameter(swpinfo, that, deepDef, resParam);
                  }
                }
              }
            }
          })

        }
      }

    }
    //that.currentInstance.paths.push(swpinfo);
    for (var i = 0; i < apiInfo.tags.length; i++) {
      var tagName = apiInfo.tags[i];
      that.mergeApiInfoSelfTags(tagName);
    }
  }
  //获取请求json
  //统计body次数
  if (swpinfo.parameters != null) {
    var count = 0;
    var tmpJsonValue = null;
    var tmpRootXmlName = "";
    swpinfo.parameters.forEach(function (p) {
      //})
      //$.each(swpinfo.parameters, function (i, p) {
      if (p.in == "body") {
        count = count + 1;
        if (p.txtValue != null && p.txtValue != "") {
          tmpJsonValue = p.txtValue;
          tmpRootXmlName = p.schemaValue;
        }
      }
    })
    if (count == 1) {
      swpinfo.requestValue = tmpJsonValue;
      //判断consume是否是XML
      //https://gitee.com/xiaoym/knife4j/issues/I1BCKB
      if (KUtils.arrNotEmpty(swpinfo.consumes)) {
        var notEmptyConsumes = swpinfo.consumes.filter(consume => KUtils.strNotBlank(consume));
        if (KUtils.arrNotEmpty(notEmptyConsumes)) {
          var xmlRequest = notEmptyConsumes.some(consume => consume.toLowerCase().indexOf("xml") > -1);
          if (xmlRequest) {
            //是Xml请求
            if (KUtils.strNotBlank(tmpJsonValue)) {
              var tmpJsonObject = KUtils.json5parse(tmpJsonValue);
              var builder = new xml2js.Builder({
                rootName: tmpRootXmlName
              });
              var obj = builder.buildObject(tmpJsonObject);
              swpinfo.requestValue = builder.buildObject(tmpJsonObject);
              swpinfo.xmlRequest = true;
            }
          }

        }
      }
    }
    //此处判断接口的请求参数类型
    //判断consumes请求类型
    if (apiInfo.consumes != undefined && apiInfo.consumes != null && apiInfo.consumes.length > 0) {
      var ctp = apiInfo.consumes[0];
      if (ctp == "multipart/form-data") {
        swpinfo.contentType = ctp;
        swpinfo.contentValue = "form-data";
      } else if (ctp == "text/plain") {
        swpinfo.contentType = ctp;
        swpinfo.contentValue = "raw";
        swpinfo.contentShowValue = "Text(text/plain)";
        swpinfo.contentMode = "text";
      } else if (ctp == "application/xml") {
        swpinfo.contentType = ctp;
        swpinfo.contentValue = "raw";
        swpinfo.contentShowValue = "XML(application/xml)";
        swpinfo.contentMode = "xml";
      } else {
        //根据参数遍历,否则默认是表单x-www-form-urlencoded类型
        var defaultType = "application/x-www-form-urlencoded;charset=UTF-8";
        var defaultValue = "x-www-form-urlencoded";
        for (var i = 0; i < swpinfo.parameters.length; i++) {
          var pt = swpinfo.parameters[i];
          if (pt.in == "body") {
            if (pt.schemaValue == "MultipartFile") {
              defaultType = "multipart/form-data";
              defaultValue = "form-data";
              break;
            } else {
              defaultValue = "raw";
              defaultType = "application/json";
              swpinfo.contentMode = "json";
              break;
            }
          } else {
            if (pt.schemaValue == "MultipartFile") {
              defaultType = "multipart/form-data";
              defaultValue = "form-data";
              break;
            }
          }

        }
        swpinfo.contentType = defaultType;
        swpinfo.contentValue = defaultValue;
      }
    } else {
      //根据参数遍历,否则默认是表单x-www-form-urlencoded类型
      var defaultType = "application/x-www-form-urlencoded;charset=UTF-8";
      var defaultValue = "x-www-form-urlencoded";
      for (var i = 0; i < swpinfo.parameters.length; i++) {
        var pt = swpinfo.parameters[i];
        if (pt.in == "body") {
          if (pt.schemaValue == "MultipartFile") {
            defaultType = "multipart/form-data";
            defaultValue = "form-data";
            break;
          } else {
            defaultValue = "raw";
            defaultType = "application/json";
            swpinfo.contentMode = "json";
            break;
          }
        } else {
          if (pt.schemaValue == "MultipartFile") {
            defaultType = "multipart/form-data";
            defaultValue = "form-data";
            break;
          }
        }
      }
      swpinfo.contentType = defaultType;
      swpinfo.contentValue = defaultValue;
    }
  }
  /*if(swpinfo.parameters.length==1){
      //只有在参数只有一个且是body类型的参数才有请求示例
      var reqp=swpinfo.parameters[0];
      //判断参数是否body类型
      if(reqp.in=="body"){
          if(reqp.txtValue!=null&&reqp.txtValue!=""){
              swpinfo.requestValue=reqp.txtValue;
          }
      }
  }*/
  //第一次加载
  if (that.currentInstance.firstLoad) {
    that.currentInstance.cacheInstance.cacheApis.push(swpinfo.id);
    //that.currentInstance.groupApis.push(swpinfo.id);
    //构建当前版本对象
    var _uptObject = new SwaggerBootstrapUiCacheUptApi(swpinfo.versionId);
    _uptObject.url = swpinfo.url;
    that.currentInstance.cacheInstance.updateApis[swpinfo.id] = _uptObject;
    //that.log(that.currentInstance)
  } else {
    //判断当前是否接口信息有变更,兼容赏上个版本的缓存
    var _cacheUa = that.currentInstance.cacheInstance.updateApis;
    if (_cacheUa.hasOwnProperty(swpinfo.id)) {
      var _uptInfo = _cacheUa[swpinfo.id];
      if (_uptInfo != null && _uptInfo != undefined) {
        if (_uptInfo.versionId != swpinfo.versionId) {
          //已经存在变更
          swpinfo.hasChanged = true;
        }
      }
    } else {
      //构建当前版本对象
      var _uptObject = new SwaggerBootstrapUiCacheUptApi(swpinfo.versionId);
      _uptObject.url = swpinfo.url;
      that.currentInstance.cacheInstance.updateApis[swpinfo.id] = _uptObject;
      that.log(that.currentInstance.cacheInstance)
    }
  }
  return swpinfo;
}
/***
 * 根据api接口自定义tags添加
 * @param name
 */
SwaggerBootstrapUi.prototype.mergeApiInfoSelfTags = function (name) {
  var that = this;
  var flag = false;
  that.currentInstance.tags.forEach(function (tag) {
    //})
    //$.each(that.currentInstance.tags,function (i, tag) {
    if (tag.name == name) {
      flag = true;
    }
  })
  if (!flag) {
    var ntag = new SwaggerBootstrapUiTag(name, name);
    that.currentInstance.tags.push(ntag);
  }
}
/***
 * JSR-303支持
 * @param parameter
 */
SwaggerBootstrapUi.prototype.validateJSR303 = function (parameter, origin) {
  var max = origin["maximum"],
    min = origin["minimum"],
    emin = origin["exclusiveMinimum"],
    emax = origin["exclusiveMaximum"];
  var pattern = origin["pattern"];
  var maxLength = origin["maxLength"],
    minLength = origin["minLength"];
  if (max || min || emin || emax) {
    parameter.validateStatus = true;
    parameter.validateInstance = {
      minimum: min,
      maximum: max,
      exclusiveMaximum: emax,
      exclusiveMinimum: emin
    };
  } else if (pattern) {
    parameter.validateStatus = true;
    parameter.validateInstance = {
      "pattern": origin["pattern"]
    };
  } else if (maxLength || minLength) {
    parameter.validateStatus = true;
    parameter.validateInstance = {
      maxLength: maxLength,
      minLength: minLength
    };
  }
}

/***
 * 根据类名查找definition
 */
SwaggerBootstrapUi.prototype.getDefinitionByName = function (name) {
  var that = this;
  var def = null;
  that.currentInstance.difArrs.forEach(function (d) {
    if (d.name == name) {
      def = d;
      return;
    }
  })
  return def;
}

/***
 * 递归查询definition
 * @param refType
 * @param definitions
 * @param flag
 */
SwaggerBootstrapUi.prototype.findRefDefinition = function (definitionName, definitions, flag, globalArr) {
  var that = this;
  var defaultValue = "";
  if (KUtils.checkUndefined(that.currentInstance.definitionValues[definitionName])) {
    defaultValue = that.currentInstance.definitionValues[definitionName];
  } else {
    for (var definition in definitions) {
      if (definitionName == definition) {
        //不解析本身
        //that.log("解析definitionName:"+definitionName);
        //that.log("是否递归："+flag);
        var value = definitions[definition];
        //是否有properties
        if (value.hasOwnProperty("properties")) {
          var properties = value["properties"];
          var defiTypeValue = {};
          for (var property in properties) {
            var propobj = properties[property];
            if (!propobj.hasOwnProperty("readOnly") || !propobj["readOnly"]) {
              //默认string类型
              var propValue = "";
              //判断是否有类型
              if (propobj.hasOwnProperty("type")) {
                var type = propobj["type"];
                //判断是否有example
                if (propobj.hasOwnProperty("example")) {
                  propValue = propobj["example"];
                } else if (KUtils.checkIsBasicType(type)) {
                  propValue = KUtils.getBasicTypeValue(type);
                  //此处如果是object情况,需要判断additionalProperties属性的情况
                  if (type == "object") {
                    if (propobj.hasOwnProperty("additionalProperties")) {
                      var addpties = propobj["additionalProperties"];
                      //判断是否有ref属性,如果有,存在引用类,否则默认是{}object的情况
                      if (addpties.hasOwnProperty("$ref")) {
                        var adref = addpties["$ref"];
                        var regex = new RegExp("#/components/schemas/(.*)$", "ig");
                        if (regex.test(adref)) {
                          var addrefType = RegExp.$1;
                          var addTempValue = null;
                          if (!flag) {
                            if (globalArr.indexOf(addrefType) == -1) {
                              addTempValue = that.findRefDefinition(addrefType, definitions, flag, globalArr);
                              propValue = {
                                "additionalProperties1": addTempValue
                              }
                            }

                          }
                        }
                      }
                    }
                  }
                } else {
                  if (type == "array") {
                    propValue = new Array();
                    var items = propobj["items"];
                    var ref = items["$ref"];
                    if (items.hasOwnProperty("type")) {
                      if (items["type"] == "array") {
                        ref = items["items"]["$ref"];
                      }
                    }
                    var regex = new RegExp("#/components/schemas/(.*)$", "ig");
                    if (regex.test(ref)) {
                      var refType = RegExp.$1;
                      if (!flag) {
                        //判断是否存在集合中
                        if (globalArr.indexOf(refType) != -1) {
                          //存在
                          propValue.push({});
                        } else {
                          globalArr.push(definitionName);
                          propValue.push(that.findRefDefinition(refType, definitions, flag, globalArr));
                        }
                      }

                    }
                  }
                }

              } else {
                //存在ref
                if (propobj.hasOwnProperty("$ref")) {
                  var ref = propobj["$ref"];
                  var regex = new RegExp("#/components/schemas/(.*)$", "ig");
                  if (regex.test(ref)) {
                    var refType = RegExp.$1;
                    //这里需要递归判断是否是本身,如果是,则退出递归查找
                    if (!flag) {
                      //if($.inArray(refType,globalArr) != -1){
                      if (globalArr.indexOf(refType) != -1) {
                        //存在
                        propValue = {};
                      } else {
                        globalArr.push(definitionName);
                        propValue = that.findRefDefinition(refType, definitions, flag, globalArr);
                      }
                    }
                  }
                } else {
                  propValue = {};
                }

              }
              defiTypeValue[property] = propValue;
            }
          }
          defaultValue = defiTypeValue;
        } else {
          defaultValue = {};
        }
      }
    }
    //赋值
    that.currentInstance.definitionValues[definitionName] = defaultValue;
  }
  return defaultValue;
}

/***
 * 计数
 * @param method
 */
SwaggerBootstrapUi.prototype.methodCountAndDown = function (method) {
  var that = this;
  var flag = false;
  that.currentInstance.pathArrs.forEach(function (a) {
    //})
    //$.each(that.currentInstance.pathArrs,function (i, a) {
    if (a.method == method) {
      flag = true;
      //计数加1
      a.count = a.count + 1;
    }
  })
  if (!flag) {
    var me = new SwaggerBootstrapUiPathCountDownLatch();
    me.method = method;
    me.count = 1;
    that.currentInstance.pathArrs.push(me);
  }
}
/***
 * 获取全局缓存auth信息
 */
SwaggerBootstrapUi.prototype.getGlobalSecurityInfos = function () {
  var that = this;
  var params = [];
  if (window.localStorage) {
    var store = window.localStorage;
    var globalparams = store["SwaggerBootstrapUiSecuritys"];
    if (globalparams != undefined && globalparams != null && globalparams != "") {
      var gpJson = JSON.parse(globalparams);
      gpJson.forEach(function (j) {
        //})
        //$.each(gpJson, function (i, j) {
        params = params.concat(j.value);
      })
    }
  } else {
    //params=$("#sbu-header").data("cacheSecurity");
  }
  return params;
}
/***
 * 计数器
 * @constructor
 */
var SwaggerBootstrapUiPathCountDownLatch = function () {
  this.method = "";
  this.count = 0;
}

function deepResponseRefParameter(swpinfo, that, def, resParam) {
  if (def != null) {
    if (def.hasOwnProperty("properties")) {
      var refParam = new SwaggerBootstrapUiRefParameter();
      refParam.name = def.name;
      if (!KUtils.checkParamArrsExists(swpinfo.responseRefParameters, refParam)) {
        swpinfo.responseRefParameters.push(refParam);
        if (def.hasOwnProperty("properties")) {
          var props = def["properties"];
          props.forEach(function (p) {
            //})
            //$.each(props,function (i, p) {
            var refp = new SwaggerBootstrapUiParameter();
            refp.pid = resParam.id;
            refp.name = p.name;
            refp.type = p.type;
            refp.description = KUtils.replaceMultipLineStr(p.description);
            //add之前需要判断是否已添加,递归情况有可能重复
            refParam.params.push(refp);
            //判断类型是否基础类型
            if (!KUtils.checkIsBasicType(p.refType)) {
              refp.schemaValue = p.refType;
              refp.schema = true;
              if (resParam.name != refp.name || resParam.schemaValue != p.refType) {
                var deepDef = that.getDefinitionByName(p.refType);
                deepResponseRefParameter(swpinfo, that, deepDef, refp);
              }
            }
          })
        }
      }
    }
  }
}

function deepTreeTableResponseRefParameter(swpinfo, that, def, resParam) {
  if (def != null) {
    if (def.hasOwnProperty("properties")) {
      var refParam = new SwaggerBootstrapUiTreeTableRefParameter();
      refParam.name = def.name;
      refParam.id = resParam.id;
      if (!checkParamTreeTableArrsExists(swpinfo.responseTreetableRefParameters, refParam)) {
        //firstParameter.childrenTypes.push(def.name);
        swpinfo.responseTreetableRefParameters.push(refParam);
        if (def.hasOwnProperty("properties")) {
          var props = def["properties"];
          props.forEach(function (p) {
            //})
            //$.each(props,function (i, p) {
            var refp = new SwaggerBootstrapUiParameter();
            resParam.parentTypes.forEach(function (pt) {
              refp.parentTypes.push(pt);
            })
            /*  $.each(resParam.parentTypes,function (i, pt) {
                 refp.parentTypes.push(pt);
             }) */
            if (p.hasOwnProperty("readOnly")) {
              refp.readOnly = p.readOnly;
            }
            refp.parentTypes.push(def.name);
            refp.pid = resParam.id;
            refp.name = p.name;
            refp.type = p.type;
            refp.description = KUtils.replaceMultipLineStr(p.description);
            refp.example = p.example;
            //add之前需要判断是否已添加,递归情况有可能重复
            refParam.params.push(refp);
            //判断类型是否基础类型
            if (!KUtils.checkIsBasicType(p.refType)) {
              refp.schemaValue = p.refType;
              refp.schema = true;
              if (resParam.name != refp.name || resParam.schemaValue != p.refType) {
                var deepDef = that.getDefinitionByName(p.refType);
                if (!checkDeepTypeAppear(refp.parentTypes, p.refType)) {
                  deepTreeTableResponseRefParameter(swpinfo, that, deepDef, refp);
                }
              }
            } else {
              if (p.type == "array") {
                if (p.refType != null && p.refType != undefined && p.refType != "") {
                  refp.schemaValue = p.refType;
                }
              }
            }
          })
        }
      }

    }
  }
}

/***
 * treeTable组件
 * @param minfo
 * @param that
 * @param def
 * @param apiInfo
 */
function deepTreeTableRefParameter(minfo, that, def, apiInfo) {
  if (def != null) {
    //查询
    if (KUtils.checkUndefined(that.currentInstance.refTreeTableModels[def.name])) {
      //存在
      ////console("refTreeTableModels-----------递归存在,modelName:" + def.name)
      var refParam = that.currentInstance.refTreeTableModels[def.name];
      ////console(refParam)
      apiInfo.refTreetableparameters.push(refParam);
      apiInfo.refTreetableModelsparameters.push(refParam);
    } else {
      var refParam = new SwaggerBootstrapUiTreeTableRefParameter();
      refParam.name = def.name;
      refParam.id = minfo.id;
      //SwaggerModels
      var refModelParam = new SwaggerBootstrapUiTreeTableRefParameter();
      refModelParam.name = def.name;
      refModelParam.id = minfo.id;
      //如果当前属性中的schema类出现过1次则不在继续,防止递归死循环
      if (!checkParamTreeTableArrsExists(apiInfo.refTreetableparameters, refParam)) {
        //firstParameter.childrenTypes.push(def.name);
        apiInfo.refTreetableparameters.push(refParam);
        apiInfo.refTreetableModelsparameters.push(refModelParam);
        if (def.hasOwnProperty("properties")) {
          var props = def["properties"];
          props.forEach(function (p) {
            var _ignoreFilterName = minfo.ignoreFilterName + "." + p.name;
            if (apiInfo.ignoreParameters == null || (apiInfo.ignoreParameters != null && !apiInfo.ignoreParameters.hasOwnProperty(_ignoreFilterName))) {
              var refp = new SwaggerBootstrapUiParameter();
              refp.pid = minfo.id;
              minfo.parentTypes.forEach(function (pt) {
                refp.parentTypes.push(pt);
              })
              refp.readOnly = p.readOnly;
              //refp.parentTypes=minfo.parentTypes;
              refp.parentTypes.push(def.name)
              //level+1
              refp.level = minfo.level + 1;
              refp.name = p.name;
              refp.ignoreFilterName = _ignoreFilterName;
              refp.type = p.type;
              //判断非array
              if (p.type != "array") {
                if (p.refType != null && p.refType != undefined && p.refType != "") {
                  //修复针对schema类型的参数,显示类型为schema类型
                  refp.type = p.refType;
                }
              }
              refp.in = minfo.in;
              refp.require = p.required;
              refp.example = p.example;
              refp.description = KUtils.replaceMultipLineStr(p.description);
              that.validateJSR303(refp, p.originProperty);
              //models添加所有属性
              refModelParam.params.push(refp);
              if (!p.readOnly) {
                refParam.params.push(refp);
              }
              //判断类型是否基础类型
              if (KUtils.checkUndefined(p.refType) && !KUtils.checkIsBasicType(p.refType)) {
                ////console("schema类型--------------" + p.refType)
                refp.schemaValue = p.refType;
                refp.schema = true;
                //属性名称不同,或者ref类型不同
                if (minfo.name != refp.name || minfo.schemaValue != p.refType) {
                  var deepDef = that.getDefinitionByName(p.refType);
                  if (!checkDeepTypeAppear(refp.parentTypes, p.refType)) {
                    deepTreeTableRefParameter(refp, that, deepDef, apiInfo);
                  }
                }
              } else {
                if (p.type == "array") {
                  if (p.refType != null && p.refType != undefined && p.refType != "") {
                    //修复针对schema类型的参数,显示类型为schema类型
                    refp.schemaValue = p.refType;
                  }
                }
              }
            }
          })

        }
      }
      //存放值
      that.currentInstance.refTreeTableModels[def.name] = refParam;
    }
  }
}

/***
 * 递归查询
 * @param minfo
 * @param that
 * @param def
 */
function deepRefParameter(minfo, that, def, apiInfo) {
  if (def != null) {
    var refParam = new SwaggerBootstrapUiRefParameter();
    refParam.name = def.name;
    if (!KUtils.checkParamArrsExists(apiInfo.refparameters, refParam)) {
      apiInfo.refparameters.push(refParam);
      if (def.hasOwnProperty("properties")) {
        var props = def["properties"];
        props.forEach(function (p) {

          //})
          //$.each(props,function (i, p) {
          //如果当前属性为readOnly，则不加入
          if (!p.readOnly) {
            var _filterName = minfo.ignoreFilterName + "." + p.name;
            //判断是否忽略
            if (apiInfo.ignoreParameters == null || (apiInfo.ignoreParameters != null && !apiInfo.ignoreParameters.hasOwnProperty(_filterName))) {
              var refp = new SwaggerBootstrapUiParameter();
              refp.pid = minfo.id;
              refp.name = p.name;
              refp.ignoreFilterName = _filterName;

              refp.type = p.type;
              //判断非array
              if (p.type != "array") {
                if (p.refType != null && p.refType != undefined && p.refType != "") {
                  //修复针对schema类型的参数,显示类型为schema类型
                  refp.type = p.refType;
                }
              }
              refp.in = minfo.in;
              refp.require = p.required;
              refp.description = KUtils.replaceMultipLineStr(p.description);
              that.validateJSR303(refp, p.originProperty);
              refParam.params.push(refp);
              //判断类型是否基础类型
              if (!KUtils.checkIsBasicType(p.refType)) {
                refp.schemaValue = p.refType;
                refp.schema = true;
                //属性名称不同,或者ref类型不同
                if (minfo.name != refp.name || minfo.schemaValue != p.refType) {
                  var deepDef = that.getDefinitionByName(p.refType);
                  deepRefParameter(refp, that, deepDef, apiInfo);
                }
              }
            }
          }

        })
      }
    }
  }
}
/***
 * 递归父类是否出现
 * @param types
 * @param type
 * @returns {boolean}
 */
function checkDeepTypeAppear(types, type) {
  var flag = false;
  types.forEach(function (t) {
    if (t == type) {
      flag = true;
    }
  })
  return flag;
}

function checkParamTreeTableArrsExists(arr, param) {
  var flag = false;
  if (arr != null && arr.length > 0) {
    arr.forEach(function (a) {
      if (a.name == param.name && a.id == param.id) {
        flag = true;
      }
    })
  }
  return flag;
}

function deepSchemaModel(model, arrs, id) {
  ////console(model.name)
  arrs.forEach(function (arr) {
    //})
    //$.each(arrs,function (i, arr) {
    if (arr.id == id) {
      //找到
      model.data = model.data.concat(arr.params);
      //遍历params
      if (arr.params != null && arr.params.length > 0) {
        arr.params.forEach(function (ps) {
          //})
          //$.each(arr.params, function (j, ps) {
          if (ps.schema) {
            deepSchemaModel(model, arrs, ps.id);
          }
        })
      }
    }
  })
}

/***
 * SwaggerBootstrapUi Model树对象
 * @param id
 * @param name
 * @constructor
 */
var SwaggerBootstrapUiModel = function (id, name) {
  this.id = id;
  this.name = name;
  //存放Model对象的属性结构
  //SwaggerBootstrapUiTreeTableRefParameter集合
  this.data = new Array();
  this.random = parseInt(Math.random() * (6 - 1 + 1) + 1, 10);
  this.modelClass = function () {
    var cname = "panel-default";
    switch (this.random) {
      case 1:
        cname = "panel-success";
        break;
      case 2:
        cname = "panel-success";
        break;
      case 3:
        cname = "panel-info";
        break;
      case 4:
        cname = "panel-warning";
        break;
      case 5:
        cname = "panel-danger";
        break;
      case 6:
        cname = "panel-default";
        break;
    }
    return cname;
  }

}

/***
 * 响应码
 * @constructor
 */
var SwaggerBootstrapUiResponseCode = function () {
  this.code = null;
  this.description = null;
  this.schema = null;
  //treetable组件使用对象
  this.refTreetableparameters = new Array();
  this.responseCodes = new Array();
  this.responseValue = null;
  this.responseJson = null;
  this.responseText = null;
  this.responseBasicType = false;
  //响应Header字段说明
  this.responseHeaderParameters = null;
  //响应字段说明
  this.responseParameters = new Array();
  this.responseParameterRefName = "";
  this.responseRefParameters = new Array();
  //treetable组件使用对象
  this.responseTreetableRefParameters = new Array();
  this.responseDescriptionFind = function (paths, key, that) {
    if (!this.responseDescriptions) {
      this.responseDescriptions = getKeyDescriptions(this.responseParameters, that);
    }
    var path = paths.join('>') + '>' + key;
    path = path.replace(/0>/g, '');
    //console.log(this.responseDescriptions)
    if (this.responseDescriptions && this.responseDescriptions[path]) {
      return this.responseDescriptions[path];
    }
    return '';
  }
}

var getKeyDescriptions = function (target, that, parentTypes) {
  var keyList = {};
  if (typeof (target) == 'object') {
    if (Array.isArray(target)) {
      for (var index in target) {
        var objc = target[index];
        //遍历属性
        if (parentTypes == null || parentTypes == undefined) {
          //first init
          parentTypes = new Array();
        }
        if (typeof (objc) == 'object') {
          var key = objc.name;
          var keyListTemp;
          keyList[key] = objc.description;
          if (objc.schemaValue || objc.refType) {
            //此处判断父级schema不能是自己
            //parentTypes次数>1此,出现递归
            if (parentTypes.indexOf(objc.schemaValue || objc.refType) == -1) {
              //if ($.inArray(objc.schemaValue || objc.refType, parentTypes) == -1) {
              parentTypes.push(objc.schemaValue || objc.refType);
              var def = that.getDefinitionByName(objc.schemaValue || objc.refType);
              if (def) {
                if (def.properties) {
                  //递归存在相互引用的情况,导致无限递归
                  keyListTemp = getKeyDescriptions(def.properties, that, parentTypes);
                }
              }
            }
          } else if (objc.params) {
            keyListTemp = getKeyDescriptions(objc.params, that);
          }
          if (keyListTemp) {
            for (var j in keyListTemp) {
              keyList[key + ">" + j] = keyListTemp[j];
            }
          }
        }
      }
    }
  }
  return keyList;
}
/**
 * 过滤多余POST功能
 */
var SwaggerBootstrapUiApiFilter = function () {
  this.api = function (methodType) {
    var apis = new Array();
    //判断当前methods类型,如果methods只有1条则返回
    if (this.methods.length == 7) {
      //如果是7个则 开启过滤
      var mpt = null;
      //如果接口梳理是7个
      for (var c = 0; c < this.methods.length; c++) {
        if (this.methods[c].methodType == methodType) {
          mpt = this.methods[c];
        }
      }
      if (mpt == null) {
        mpt = this.methods[0];
      }
      apis.push(mpt);
    } else {
      apis = apis.concat(this.methods);
    }
    return apis;

  };
  this.methods = new Array();
}

/***
 * 缓存更新对象
 * @constructor
 */
var SwaggerBootstrapUiCacheUptApi = function (id) {
  //当前版本id
  this.url = "";
  this.versionId = id;
  this.lastTime = new Date();
}
/***
 *
 * [{
 *  id:"md5(groupName)",
 *  groupApis:["id1","id2"]
 * }]
 * @constructor
 */
function SwaggerBootstrapUiCacheApis(options) {
  //分组id
  this.id = options.id || '';
  //分组名称
  this.name = options.name || '';
  //缓存api-id 对象的集合
  this.cacheApis = [];
  //缓存整个对象的id?
  //存储 id:{"uptversion":"102010221299393993","lastTime":"2019/11/12 12:30:33"}
  this.updateApis = {};
}

/***
 * 返回对象解析属性
 * @constructor
 */
var SwaggerBootstrapUiDefinition = function () {
  //类型名称
  this.name = "";
  this.ignoreFilterName = null;
  this.schemaValue = null;
  //this.id = "definition" + Math.round(Math.random() * 1000000);
  this.id = "definition" + KUtils.randomMd5();
  this.pid = "-1";
  this.level = 1;
  this.childrenTypes = new Array();
  this.parentTypes = new Array();
  //介绍
  this.description = "";
  //类型
  this.type = "";
  //属性 --SwaggerBootstrapUiProperty 集合
  this.properties = new Array();
  this.value = null;
  //add by xiaoymin 2018-8-1 13:35:32
  this.required = new Array();
  this.title = "";
  //treetable组件使用对象
  this.refTreetableparameters = new Array();
  //swaggerModels功能
  this.refTreetableModelsparameters = new Array();
}
/**
 * 权限验证
 * @constructor
 */
var SwaggerBootstrapUiSecurityDefinition = function () {
  this.key = "";
  this.type = "";
  this.in = "";
  this.name = "";
  this.value = "";
  //add at 2019-12-7 18:20:35
  this.id = "";

}

/***
 * definition对象属性
 * @constructor
 */
var SwaggerBootstrapUiProperty = function () {
  //默认基本类型,非引用
  this.basic = true;
  this.name = "";
  this.type = "";
  this.refType = null;
  this.description = "";
  this.example = "";
  this.format = "";
  //是否必须
  this.required = false;
  //默认值
  this.value = null;
  //引用类
  this.property = null;
  //原始参数
  this.originProperty = null;
  //是否枚举
  this.enum = null;
  //是否readOnly
  this.readOnly = false;
}
/***
 * swagger的tag标签
 * @param name
 * @param description
 * @constructor
 */
var SwaggerBootstrapUiTag = function (name, description) {
  this.name = name;
  this.description = description;
  //add by xiaoymin 2020-4-5 11:03:07 分组作者
  this.author = null;
  this.childrens = new Array();
  //是否有新接口
  this.hasNew = false;
  //是否有接口变更
  this.hasChanged = false;
}
/***
 * Swagger接口基础信息
 * @constructor
 */
var SwaggerBootstrapUiApiInfo = function () {
  this.url = null;
  this.originalUrl = null;
  this.configurationDebugSupport = true;
  this.showUrl = "";
  this.basePathFlag = false;
  //接口作者
  this.author = null;
  this.methodType = null;
  this.description = null;
  this.summary = null;
  this.consumes = null;
  this.operationId = null;
  this.produces = null;
  this.tags = null;
  //默认请求contentType
  this.contentType = "application/json";
  this.contentShowValue = "JSON(application/json)";
  //请求如果是raw类型,给定mode类型
  this.contentMode = "Text";
  //显示参数
  //存储请求类型，form|row|urlencode
  this.contentValue = "raw";
  this.parameters = new Array();
  //参数数量
  this.parameterSize = 0;
  //请求json示例
  this.requestValue = null;
  //是否xml请求
  this.xmlRequest = false;
  //针对parameter属性有引用类型的参数,继续以table 的形式展现
  //存放SwaggerBootstrapUiRefParameter 集合
  this.refparameters = new Array();
  //treetable组件使用对象
  this.refTreetableparameters = new Array();
  //swaggerModels功能
  this.refTreetableModelsparameters = new Array();
  //请求参数treetalbe
  // add at 2019-12-15 13:29:19
  this.reqParameters = new Array();

  this.responseCodes = new Array();
  this.responseHttpObject = null;
  /***
   * 返回状态码为200的
   */
  this.getHttpSuccessCodeObject = function () {
    if (this.responseHttpObject == null) {
      if (this.responseCodes != null && this.responseCodes.length > 0) {
        var _tmp = null;
        for (var i = 0; i < this.responseCodes.length; i++) {
          if (this.responseCodes[i].code == "200") {
            _tmp = this.responseCodes[i];
            break;
          }
        }
        this.responseHttpObject = _tmp;
      }
    }
    return this.responseHttpObject;
  }

  this.responseValue = null;
  this.responseJson = null;
  this.responseText = null;
  this.responseBasicType = false;
  //响应Header字段说明
  this.responseHeaderParameters = null;
  //响应字段说明
  this.responseParameters = new Array();
  this.responseParameterRefName = "";
  this.responseRefParameters = new Array();
  //treetable组件使用对象
  this.responseTreetableRefParameters = new Array();
  //新增菜单id
  this.id = "";
  //版本id
  this.versionId = "";
  //排序
  this.order = 2147483647;
  //add by xiaoymin 2018-12-14 17:04:42
  //是否新接口
  this.hasNew = false;
  //是否有接口变更
  this.hasChanged = false;
  //是否过时
  this.deprecated = false;
  //是否存在响应状态码中  存在多个schema的情况
  this.multipartResponseSchema = false;
  this.multipartResponseSchemaCount = 0;
  //hashUrl
  this.hashCollections = [];
  //ignoreParameters add 2019-7-30 16:10:08
  this.ignoreParameters = null;
  //当前接口用户实例id add 2019-12-5 10:49:40
  this.instanceId = null;
  // 用于请求后构建curl
  this.host = null;
}

var SwaggerBootstrapUiRefParameter = function () {
  this.name = null;
  //存放SwaggerBootstrapUiParameter集合
  this.params = new Array();
}

var SwaggerBootstrapUiTreeTableRefParameter = function () {
  this.id = "";
  this.name = null;
  //存放SwaggerBootstrapUiParameter集合
  this.params = new Array();
  this.level = 1;
  this.childrenTypes = new Array();


}

/***
 * Swagger请求参数
 * @constructor
 */
var SwaggerBootstrapUiParameter = function () {
  this.name = null;
  //该属性用于过滤参数使用
  this.ignoreFilterName = null;
  this.require = null;
  this.type = null;
  this.in = null;
  this.schema = false;
  this.schemaValue = null;
  this.value = null;
  //JSR-303 annotations supports since 1.8.7
  //默认状态为false
  this.validateStatus = false;
  this.validateInstance = null;
  //引用类
  this.def = null;
  //des
  this.description = null;
  //文本框值
  this.txtValue = null;
  //枚举类型
  this.enum = null;

  //this.id = "param" + Math.round(Math.random() * 1000000);
  this.id = uniqueId('param'); // "param" + KUtils.randomMd5(); 使用 uniqueId 方法替代
  this.pid = "-1";

  this.level = 1;
  //参数是否显示在debug中
  this.show = true;
  //是否readOnly
  this.readOnly = false;
  this.example = null;


  this.childrenTypes = new Array();
  this.parentTypes = new Array();
}

function SwaggerBootstrapUiParameterLevel() {
  this.level = 1;

}

/***
 * swagger 分组对象
 * @param name 分组对象名称
 * @param location url地址
 * @param version 版本号
 * @constructor
 */
function SwaggerBootstrapUiInstance(name, location, version) {
  //this.id = 'SwaggerBootstrapUiInstance' + Math.round(Math.random() * 1000000)
  this.id = 'SwaggerBootstrapUiInstance' + md5(name + location + version)
  //默认未加载
  this.load = false
  //分组名称
  this.name = name
  //分组url地址
  this.location = location
  //不分组是url地址
  this.url = null
  //增强地址
  this.extUrl = null
  this.groupVersion = version
  //分组url请求实例
  this.basePath = ''
  //使用nginx,反向代理服务名称
  this.baseUrl = ''
  this.host = ''
  this.swagger = ''
  this.description = ''
  this.title = ''
  this.version = ''
  this.termsOfService = ''
  this.contact = ''
  //当前definistion数组
  // SwaggerBootstrapUiDefinition 集合
  this.difArrs = []
  //difinition的请求value缓存值
  //key-definiationName  -value :requestValue
  // add 2019-12-11 15:06:30
  this.definitionValues = {};
  //针对Swagger Models功能,再存一份SwaggerBootstrapUiDefinition集合
  this.swaggerModelsDifinitions = []
  //add 2019-12-11 20:08:51
  //存放treeTable已经递归查询过的schema值
  this.refTreeTableModels = {};
  //标签分类信息组
  //SwaggerBootstrapUiTag 集合
  this.tags = []
  //接口url信息
  //存储SwaggerBootstrapUiApiInfo 集合
  this.paths = []
  //字典
  this.pathsDictionary = {}
  //全局参数,存放SwaggerBootstrapUiParameter集合
  this.globalParameters = []
  //参数统计信息，存放SwaggerBootstrapUiPathCountDownLatch集合
  this.pathArrs = []
  //key-value方式存放
  //key-存放接口地址
  //value:存放实际值
  this.pathFilters = {}
  //权限信息
  this.securityArrs = []
  //Models
  this.models = []
  this.modelNames = []
  //新版本的models 适配antd的属性表格
  this.modelArrs = []

  //SwaggerBootstrapCacheGroupApis 对象的集合
  //add by xiaoyumin 2018-12-12 18:49:22
  this.groupId = md5(name)
  this.firstLoad = true
  this.groupApis = []
  //缓存对象
  //this.cacheInstance=new SwaggerBootstrapUiCacheApis({id:this.groupId,name:this.name});
  this.cacheInstance = null
  //自定义文档
  this.markdownFiles = []

  this.i18n = null
}


/***
 * 根据类名查找definition
 */
SwaggerBootstrapUiInstance.prototype.getDefinitionByName = function (name) {
  var that = this;
  var def = null;
  that.difArrs.forEach(function (d) {
    if (d.name == name) {
      def = d;
      return;
    }
  })
  return def;
}

function checkFiledExistsAndEqStr(object, filed, eq) {
  var flag = false
  if (object.hasOwnProperty(filed)) {
    if (object[filed] == eq) {
      flag = true
    }
  }
  return flag
}
/***
 * 控制台打印
 * @param msg
 */
SwaggerBootstrapUi.prototype.log = function (msg) {
  /* if (window.console) {
    //正式版不开启console功能
    window.console.log(msg)
  } */
}

/***
 * 错误异常输出
 * @param msg
 */
SwaggerBootstrapUi.prototype.error = function (msg) {
  if (window.console) {
    window.console.error(msg)
  }
}

export default SwaggerBootstrapUi
