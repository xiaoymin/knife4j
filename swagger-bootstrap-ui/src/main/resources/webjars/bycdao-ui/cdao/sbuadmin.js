layui.define(['layer','element'], function (exports) {
    var $ = layui.$;
    var element=layui.element;
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
            console.log("ddddddddddddddddddddddd")
            var $tabTitle = $('.layui-layout-admin .layui-body .layui-tab .layui-tab-title');

            var left = $tabTitle.scrollLeft();
            console.log(left)
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
        run:function () {
            console.log("run..........")
            admin.rollPage('auto');
            /**
             * 运行
             */
            new SwaggerBootstrapUi({layui:layui}).main();
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
        },
        addTabTest:function () {
            console.log("addTab..")
            var tabContent={
                title: '新选项威风威风问问'+ (Math.random()*1000|0) //用于演示
                ,content: '内容'+ (Math.random()*1000|0)
                ,id: new Date().getTime() //实际使用一般是规定好的id，这里以时间戳模拟下

            };
            element.tabAdd('admin-pagetabs', tabContent);

            element.tabChange('admin-pagetabs', tabContent.id);
            admin.rollRightPage();
        }
    };

    $("#addTab").on("click",function (e) {
        e.preventDefault();
        console.log("add")
        admin.events.addTabTest();
    })
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
    console.log("aaa")

    exports('sbuadmin', admin);
});
