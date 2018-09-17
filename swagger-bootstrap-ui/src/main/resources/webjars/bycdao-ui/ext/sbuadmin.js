layui.define(['layer','element','treetable','form'], function (exports) {
    var $ = layui.$;
    var element=layui.element;
    var treetable=layui.treetable;
    var admin = {
        // 窗口大小改变监听
        onResize: function () {
            if (config.autoRender) {
                if ($('.layui-table-view').length > 0) {
                    setTimeout(function () {
                        admin.events.refresh();
                    }, 800);
                }
            }
        },
        // 滑动选项卡
        rollPage: function (d) {
            var $tabTitle = $('.layui-layout-admin .layui-body .layui-tab .layui-tab-title');
            var left = $tabTitle.scrollLeft();
            if ('left' === d) {
                $tabTitle.scrollLeft(left - 150);
            } else if ('auto' === d) {
                var autoLeft = 0;
                $tabTitle.children("li").each(function () {
                    if ($(this).hasClass('layui-this')) {
                        return false;
                    } else {
                        autoLeft += $(this).outerWidth();
                    }
                });
                // console.log(autoLeft);
                $tabTitle.scrollLeft(autoLeft - 47);
            } else {
                $tabTitle.scrollLeft(left + 150);
            }
        },
        run:function (ace) {
            admin.rollPage('auto');
            /**
             * 运行
             */
            new SwaggerBootstrapUi({layui:layui,treetable:treetable,ace:ace}).main();
        }
    };

    // ewAdmin提供的事件
    admin.events = {
        // 左滑动tab
        leftPage: function () {
            admin.rollPage("left");
        },
        // 右滑动tab
        rightPage: function () {
            admin.rollPage();
        }
    };


    // 所有ew-event
    $('body').on('click', '*[ew-event]', function () {
        var event = $(this).attr('ew-event');
        var te = admin.events[event];
        te && te.call(this, $(this));
    });
    $(".admin-tabs-select").on("mouseenter",function () {
        $(this).find("dl:first").addClass("layui-show")
    }).on("mouseleave",function () {
        $(this).find("dl:first").removeClass("layui-show")
    })
    exports('sbuadmin', admin);
});
