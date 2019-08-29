(function ($) {
    //JSON渲染
    $("#from-json").click(function (e) {
        e.preventDefault();
        window.open(chrome.extension.getURL('json.html'));
    })
    //RESTFul API
    $("#from-api").click(function (e) {
        e.preventDefault();
        window.open(chrome.extension.getURL('api.html'));
    })

    //清理缓存
    $("#cleanCache").click(function (e) {
        e.preventDefault();
        var local = window.localStorage;
        if (local) {
            local.clear();
        }
        layer.msg("清除缓存成功!")
    })
    //选项
    $("#options").click(function (e) {
        e.preventDefault();
        window.open(chrome.extension.getURL('options.html#!/settings'));
    })
})(jQuery)