(function ($) {
  var ProxyHtml = function () {

  }
  ProxyHtml.prototype = {
    init: function () {
      this.initWebData();
    },
    initEvents: function () {
      var that = this;
      //表格
      $("#settingsforProxys").find("tbody").find("tr").find("td:first").click(function () {
        var t = $(this);
        var ck = t.find("input[name=inlineCheckbox3]");
        var val = ck.val();
        ck.prop("checked", true);
        $("#settings_proxy_current_api").val(val);
      });

      //确认
      $("#settings_proxy_save").click(function () {
        console.log("dd")
        var val = $("#settings_proxy_current_api").val();
        var reg = /((http|https):\/\/.*)/g;
        if (!val) {
          layer.msg("字符非法");
          return false;
        }
        if (!reg.test(val)) {
          layer.msg("proxy地址字符非法");
          return false;
        }
        Store.set(SwaggerBootstrapUiGlobal.cache.host, val);
        var t = null;
        chrome.storage.sync.get({
          "SwagerBootstrapUiExtensionHost": []
        }, function (result) {
          t = result.SwagerBootstrapUiExtensionHost;
          console.log(t);
          //判断是否存在
          if ($.inArray(val, t) == -1) {
            t.push(val);
          }
          console.log("set t")
          console.log(t);
          chrome.storage.sync.set({
            "SwagerBootstrapUiExtensionHost": t
          }, function () {
            that.initWebData();
          });
        });
        layer.msg("操作成功");

      });
      //删除事件
      $("#settingsforProxys").find("tbody").find("tr").find("button").click(function () {
        var del = $(this);
        var purl = del.data("url");
        chrome.storage.sync.get({
          "SwagerBootstrapUiExtensionHost": []
        }, function (result) {
          var t = result.SwagerBootstrapUiExtensionHost;
          var narr = new Array();
          $.each(t, function (i, j) {
            if (j != purl) {
              narr.push(j);
            }
          });
          //set
          chrome.storage.sync.set({
            "SwagerBootstrapUiExtensionHost": narr
          }, function () {
            del.parent().parent().remove();
          });
        });

      })
    },
    initWebData: function () {
      var that = this;
      //表单
      $("#settings_proxy_current_api").val(Store.get(SwaggerBootstrapUiGlobal.cache.host));
      //表格
      chrome.storage.sync.get({
        "SwagerBootstrapUiExtensionHost": []
      }, function (result) {
        var t = result.SwagerBootstrapUiExtensionHost;
        var body = $("#settings_proxy_table_body");
        body.empty();
        if (t.length > 0) {
          $.each(t, function (i, proxy) {
            var tr = $("<tr></tr>");
            var radio = $('<td scope="row"><label class="checkbox-inline"><input type="radio" name="inlineCheckbox3" value="' + proxy + '"></label></td>');
            var content = $('<td>' + proxy + '</td>');
            var operate = $('<td><button type="button" class="btn btn-danger" data-url="' + proxy + '">删除</button></td>');
            tr.append(radio).append(content).append(operate);
            body.append(tr);
          });
        }
        that.initEvents();
      })
    }
  }

  var p = new ProxyHtml();
  p.init();
})(jQuery)