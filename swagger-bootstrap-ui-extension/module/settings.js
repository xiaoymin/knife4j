(function ($) {
  var SettingsHtml = function () {

  }

  SettingsHtml.prototype = {
    init: function () {
      this.initEvents();
      this.initSettings();
    },
    initEvents: function () {
      var that = this;
      //保存
      $("#btnSaveSettings").click(function (e) {
        e.preventDefault();
        var langEle = $("#SwaggerBootstrapUiSettings").find("input[name=language]:checked");
        var showApi = $("#SwaggerBootstrapUiSettings").find("input[name=showApi]");
        var enableSbu = $("#SwaggerBootstrapUiSettings").find("input[name=enableSwaggerBootstrapUi]");
        //tag属性说明
        var showTagStatusElem = $("#SwaggerBootstrapUiSettings").find("input[name=showTagStatus]");
        var enableRequestCache = $("#SwaggerBootstrapUiSettings").find("input[name=enableRequestCache]");
        var enableReqFilterCache = $("#SwaggerBootstrapUiSettings").find("input[name=enableFilterMultipartApis]");
        var enableCacheOpenApiTable = $("#SwaggerBootstrapUiSettings").find("input[name=enableCacheOpenApiTable]");
        var showApiFlag = showApi.prop("checked");
        var enableSbuFlag = enableSbu.prop("checked");
        var showTagStatus = showTagStatusElem.prop("checked");
        var cacheRequest = enableRequestCache.prop("checked");
        var enableCacheOpenApi = enableCacheOpenApiTable.prop("checked");
        var enableReqFilter = enableReqFilterCache.prop("checked");
        var language = langEle.val();

        //获取值
        var multipartApiMethodType = "POST";
        if (enableReqFilter) {
          //如果选中
          multipartApiMethodType = $("#SwaggerBootstrapUiSettings").find("select[name=enableFilterMultipartApiMethodType] option:selected").val();
        }

        if (!enableCacheOpenApi) {
          that.clearCacheOpenApiTableApis();
        }
        var setts = {
          showApiUrl: showApiFlag, //接口api地址不显示
          showTagStatus: showTagStatus, //tag显示description属性.
          enableSwaggerBootstrapUi: enableSbuFlag, //是否开启swaggerBootstrapUi增强
          enableRequestCache: cacheRequest,
          enableFilterMultipartApis: enableReqFilter,
          enableFilterMultipartApiMethodType: multipartApiMethodType,
          enableCacheOpenApiTable: enableCacheOpenApi,
          language: language
        };
        that.saveSettings(setts);
        that.settings = setts;
        if (!cacheRequest) {
          that.disableStoreRequestParams();
        }
      });
    },
    clearCacheOpenApiTableApis: function () {
      if (window.localStorage) {
        var store = window.localStorage;
        store.removeItem("SwaggerBootstrapUiCacheOpenApiTableApis");
      }
    },
    saveSettings: function (settings) {
      if (window.localStorage) {
        var store = window.localStorage;
        var gbStr = JSON.stringify(settings);
        store.setItem("SwaggerBootstrapUiSettings", gbStr);
        layer.msg("保存成功");
      } else {
        layer.msg("保存失败");
      }
    },
    disableStoreRequestParams: function () {
      if (window.localStorage) {
        var store = window.localStorage;
        var key = "SwaggerBootstrapUiStore";
        store.setItem(key, "");
      }
    },
    initSettings: function () {
      //初始化settings选中状态
      if (window.localStorage) {
        var store = window.localStorage;
        var globalSettings = store["SwaggerBootstrapUiSettings"];
        if (globalSettings != undefined && globalSettings != null && globalSettings != "") {
          var settings = JSON.parse(globalSettings);

          var langEle = $("#SwaggerBootstrapUiSettings").find("input[name=language]");
          var showApi = $("#SwaggerBootstrapUiSettings").find("input[name=showApi]");
          var enableSbu = $("#SwaggerBootstrapUiSettings").find("input[name=enableSwaggerBootstrapUi]");
          //tag属性说明
          var showTagStatusElem = $("#SwaggerBootstrapUiSettings").find("input[name=showTagStatus]");
          var enableRequestCache = $("#SwaggerBootstrapUiSettings").find("input[name=enableRequestCache]");
          var enableReqFilterCache = $("#SwaggerBootstrapUiSettings").find("input[name=enableFilterMultipartApis]");
          var enableCacheOpenApiTable = $("#SwaggerBootstrapUiSettings").find("input[name=enableCacheOpenApiTable]");


          showApi.prop("checked", settings.showApiUrl);
          enableRequestCache.prop("checked", settings.enableRequestCache);
          showTagStatusElem.prop("checked", settings.showTagStatus);

          enableReqFilterCache.prop("checked", settings.enableFilterMultipartApis);
          if (settings.enableFilterMultipartApis) {
            $("#SwaggerBootstrapUiSettings").find("select[name=enableFilterMultipartApiMethodType]").val(settings.enableFilterMultipartApiMethodType);
          }
          enableSbu.prop("checked", settings.enableSwaggerBootstrapUi);

          enableCacheOpenApiTable.prop("checked", settings.enableCacheOpenApiTable);

        }
      }
    }

  };


  var sh = new SettingsHtml();
  sh.init();
})(jQuery)