var _hmt = _hmt || [];
(function () {
    var hm = document.createElement("script");
    var origio = window.location.origin;
    if (origin.indexOf("doc.xiaominfo.com") > -1) {
        hm.src = "https://hm.baidu.com/hm.js?cc5bfb32738435c06e10e57219d99584";
    } else if (origin.indexOf("www.xiaominfo.com" > -1)) {
        hm.src = "https://hm.baidu.com/hm.js?6a7c85e4a256cfadfab6ee2e4cdc00ec";
    } else {
        //gitee.io
        hm.src = "https://hm.baidu.com/hm.js?1819e862ea8110abb753ba48fb1563da";
    }
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(hm, s);
})();