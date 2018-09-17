(function($){//扩展easyui中tabs的部分方法，实现根据唯一标识id的进行相应操作；技巧：使用一个自执行的函数，激发作用域，避免这里定义的变量与系统全局变量冲突
    var _methods = $.fn.tabs.methods;
    var _exists = _methods.exists;//保存原方法
    var _getTab = _methods.getTab;
        $.extend($.fn.tabs.methods, {
            getTab : function(jq, which) {//重写getTab方法，增加根据id获取tab（注意：这里我们可以定义任意的获取方式，不必一定使用id）
                if(!which) return null;
                console.log("getTab-------------------------")
                console.log(jq)
                var tabs = jq.data('tabs').tabs;
                var searchTab=null;
                for (var i = 0; i < tabs.length; i++) {
                    var tab = tabs[i];
                    if (tab.panel("options").id==which) {
                        searchTab=tab;
                    }
                }
                console.log(searchTab);
                if (searchTab!=null){
                    return searchTab;
                }else{
                    return _getTab(jq, which);//如果根据id无法获取，则通过easyui默认的getTab进行获取
                }
            },
            exists : function(jq, which) {//重写exists方法，增加id判断
                return this.getTab(jq,which)!=null;//调用重写后的getTab方法
            },
            select:function (jq, which) {
                var p=this.getTab(jq,which);
                if (p && !p.is(':visible')){
                    $(jq).children('div.tabs-panels').each(function(){
                        //$(this).stop(true, true);
                    });
                    if (!p.panel('options').disabled){
                        p.panel('open');
                    }
                }

            }
        });
})(jQuery)