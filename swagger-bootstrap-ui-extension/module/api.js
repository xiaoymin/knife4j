(function ($) {
    var apiHtml = function () {

    }

    apiHtml.prototype = {
        init: function () {
            this.initButtons();
        },
        cacheOrigin: function (origin) {
            chrome.storage.sync.get({
                "SwagerBootstrapUiExtensionHost": []
            }, function (result) {
                var t = result.SwagerBootstrapUiExtensionHost;
                if ($.inArray(origin, t) == -1) {
                    t.push(origin);
                }
                //set
                chrome.storage.sync.set({
                    "SwagerBootstrapUiExtensionHost": t
                }, function () {});
            });

        },
        initButtons: function () {
            var that = this;
            //init buttons events
            $("#btnPreview").click(function (e) {
                e.preventDefault();
                //获取名称
                var type = $("input:radio[name=type]:checked").val();
                var url = $("#address").val();
                var reg = /((http|https):\/\/.*?)\/.*/g;
                if (!url) {
                    layer.msg("请求url非法");
                    return false;
                }
                if (!reg.test(url)) {
                    layer.msg("请求url非法");
                    return false;
                }
                var origin = RegExp.$1;
                //域名
                var prefix = url.substr(0, url.lastIndexOf("/"));
                Store.set(SwaggerBootstrapUiGlobal.cache.host, origin);
                that.cacheOrigin(origin);
                var dataArr = new Array();
                layer.load(2);
                try {
                    $.ajax({
                        url: url,
                        timeout: 30000,
                        success: function (data) {
                            if (type == "group") {
                                if (!Array.isArray(data)) {
                                    return false;
                                }
                                $.each(data, function (i, d) {
                                    var name = d.name;
                                    var singleUrl = prefix;
                                    if (d.url.indexOf("/") != 0) {
                                        singleUrl = singleUrl + "/";
                                    }
                                    singleUrl = singleUrl + d.url;
                                    $.ajax({
                                        url: singleUrl,
                                        async: false,
                                        timeout: 30000,
                                        success: function (sd) {
                                            dataArr.push({
                                                name: name,
                                                location: "",
                                                url: "",
                                                swaggerVersion: "2.0",
                                                data: sd
                                            });
                                        },
                                        error: function (xhr, status, err) {
                                            layer.closeAll();
                                            layer.msg("请求接口出错:" + err);
                                        }
                                    });
                                });
                            } else {
                                var tp = typeof (data);
                                if (tp != "object") {
                                    return false;
                                }
                                if (Array.isArray(data)) {
                                    return false;
                                }
                                if (!data.hasOwnProperty("swagger") ||
                                    !data.hasOwnProperty("tags") ||
                                    !data.hasOwnProperty("paths") ||
                                    !data.hasOwnProperty("definitions")) {
                                    return false;
                                }
                                //非分组资源
                                var swaggerData = {
                                    name: "default",
                                    location: "",
                                    url: "",
                                    swaggerVersion: "2.0",
                                    data: data
                                };
                                dataArr.push(swaggerData);
                            }

                            if (dataArr.length == 0) {
                                layer.msg("请求接口资源格式非法");
                                return false;
                            }
                            layer.closeAll();
                            Store.set(SwaggerBootstrapUiGlobal.cache.json, dataArr);
                            Store.set(SwaggerBootstrapUiGlobal.cache.type, 'api');
                            window.location = chrome.extension.getURL('doc.html');
                        },
                        error: function (xhr, status, err) {
                            layer.closeAll();
                            layer.msg("请求接口出错:" + err);
                        }
                    });
                } catch (e) {
                    layer.closeAll();
                    layer.msg("请求非法:" + e);
                }
            });

        }
    }


    var ah = new apiHtml();
    ah.init();
})(jQuery)