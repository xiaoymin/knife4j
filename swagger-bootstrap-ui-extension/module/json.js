(function ($) {
    var jsonHtml = function () {
        this.json = null;
        this.editor = null;
    }

    jsonHtml.prototype = {
        init: function () {
            var that = this;
            $.getJSON("extension/json/swagger.json", function (data) {

                var container = document.getElementById("jsoneditor");
                var options = {
                    "mode": "code",
                    "indentation": 2
                };
                var editor = new JSONEditor(container, options);
                editor.set(data);
                that.editor = editor;
                that.json = editor.get();

                //获取屏幕高度
                setTimeout(function () {
                    var height = window.screen.height - 230;
                    $("#jsoneditor").css("height", height + "px");
                }, 10)

            })
            that.initButtons();
        },
        initButtons: function () {
            var that = this;
            //init buttons events
            $("#btnPreview").click(function () {
                //获取名称
                var name = $("#jsonName").val();
                if (!name) {
                    layer.msg("请输入名称");
                    return false;
                }
                //获取json
                var json = that.getSwaggerJson();
                if (json == undefined || json == null || json == "") {
                    layer.msg("JSON文件非法,请重新输入!");
                    return false;
                }
                var dataArr = new Array();
                dataArr.push({
                    name: name,
                    location: "",
                    url: "",
                    swaggerVersion: "2.0",
                    data: json
                });
                Store.set(SwaggerBootstrapUiGlobal.cache.json, dataArr);
                //Store.set(SwaggerBootstrapUiGlobal.cache.name,name);
                Store.set(SwaggerBootstrapUiGlobal.cache.type, 'json');
                window.location = chrome.extension.getURL('doc.html');
            })
        },
        getSwaggerJson: function () {
            var that = this;
            var json = "";
            try {
                json = that.editor.get();
            } catch (e) {

            }
            return json;
        }
    }

    var jn = new jsonHtml();

    jn.init();


})(jQuery)