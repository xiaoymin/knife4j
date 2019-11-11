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
 * create by xiaoymin on 2018-7-4 15:32:07
 *
 * 重构swagger-bootstrap-ui组件,为以后动态扩展更高效,扩展接口打下基础
 *
 * modified by xiaoymin on 2019-11-11 16:42:43
 *
 * 基于Vue + Ant Design Vue重构Ui组件
 *
 */
import {
  message
} from 'ant-design-vue'
import md5 from 'js-md5'

function SwaggerBootstrapUi(options) {
  console.log('init------------------sbui--------')
  console.log(options)
  //swagger请求api地址
  this.url = options.url || 'swagger-resources'
  this.configUrl = options.configUrl || 'swagger-resources/configuration/ui'
  this.$Vue = options.Vue
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
  this.extUrl = '/v2/api-docs-ext'
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
  /* that.initSettings();
  that.initUnTemplatePageI18n();
  that.initWindowWidthAndHeight();
  that.initApis();
  that.windowResize(); */
  //2019/08/28 13:16:50 支持configuration接口,主要是相关配置,目前支持属性supportedSubmitMethods(请求调试)
  //接口地址:/swagger-resources/configuration/ui
  that.configInit()
  //加载分组接口
  that.analysisGroup()
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
          that.requestParameter[$.trim(pmArr[0])] = $.trim(pmArr[1])
        }
      }
    }
  }
  that.log('请求参数========================================')
  that.log(that.requestParameter)
}

/***
 * 读取个性化配置信息
 */
SwaggerBootstrapUi.prototype.initSettings = function () {
  var that = this
  if (window.localStorage) {
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
  }
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
        dataType: 'json'
      })
      .then(function (data) {
        that.analysisGroupSuccess(data)
        //创建分组元素
        that.createGroupElement()
      })
      .catch(function (err) {
        message.error(err)
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
  var groupData = null
  if (t == 'string') {
    groupData = JSON.parse(data)
  } else {
    groupData = data
  }
  that.log('响应分组json数据')
  that.log(groupData)
  var serviceOptions = [];
  groupData.forEach(function (group) {
    var g = new SwaggerBootstrapUiInstance(
      group.name,
      group.location,
      group.swaggerVersion
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
    var idx = newUrl.indexOf('/v2/api-docs')
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
    }
    //赋值查找缓存的id
    if (that.cacheApis.length > 0) {
      var cainstance = null
      $.each(that.cacheApis, function (x, ca) {
        if (ca.id == g.groupId) {
          cainstance = ca
        }
      })
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
    console.log("绑定-0------------------")
    that.$Vue.defaultServiceOption = serviceOptions[0].value;
    console.log(that.$Vue.defaultServiceOption)
  }

}

/***
 * 创建swagger分组页面元素
 */
SwaggerBootstrapUi.prototype.createGroupElement = function () {
  var that = this;
  //创建分组flag 
  that.log("分组---")
  that.log(that.instances)
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
 * swagger 分组对象
 * @param name 分组对象名称
 * @param location url地址
 * @param version 版本号
 * @constructor
 */
function SwaggerBootstrapUiInstance(name, location, version) {
  this.id = 'SwaggerBootstrapUiInstance' + Math.round(Math.random() * 1000000)
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
  //针对Swagger Models功能,再存一份SwaggerBootstrapUiDefinition集合
  this.swaggerModelsDifinitions = []
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

  //SwaggerBootstrapCacheGroupApis 对象的集合
  //add by xiaoyumin 2018-12-12 18:49:22
  this.groupId = md5(name)
  this.firstLoad = true
  this.groupApis = []
  //缓存对象
  //this.cacheInstance=new SwaggerBootstrapUiCacheApis({id:this.groupId,name:this.name});
  this.cacheInstance = null
  //自定义文档
  this.markdownFiles = null

  this.i18n = null
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
  if (window.console) {
    //正式版不开启console功能
    window.console.log(msg)
  }
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
