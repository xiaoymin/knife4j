$(function () {
    /***
     * 拖动事件
     * @type {boolean}
     */
    var isResizing = false,lastDownX = 0;
    var container = $('#container'),left = $('.left'),right = $('.right'),handle = $('#handle'),tabc=$("#sbu-dynamic-tab");
    //设置rightwidth
    right.css("width",container.width()-left.width()-8);
    tabc.css("width",container.width()-left.width()-8);
    handle.on('mousedown',function(e) {
        isResizing = true;
        lastDownX = e.clientX;
    });
    $(document).on('mousemove',function(e) {
        // we don't want to do anything if we aren't resizing.
        if (!isResizing) return;
        var offsetRight = container.width() - (e.clientX - container.offset().left-8);
        left.css("width",e.clientX)
        right.css('width', offsetRight-20);
        right.css("right","1px")
        handle.css("left",e.clientX)
    }).on('mouseup', function (e) {
        // stop resizing
        isResizing = false;
    });

    //菜单点击事件
    $("#leftCheck").on("click",function (e) {
        e.preventDefault();
        var that=$(this);
        var display=that.data("display");
        if(display=="0"){
            that.removeClass("icon-zuosuojin1");
            that.addClass("icon-yousuojin1");
            //隐藏
            that.data("display","1");
            //分组
            $("#leftGroup").hide("slow")
            //菜单
            $("#leftMenu").hide();
            //handle
            $("#handle").hide();

            var allwidth=$('#container').width();
            right.css("width",allwidth)
        }else{
            that.addClass("icon-zuosuojin1");
            that.removeClass("icon-yousuojin1");
            var width=container.width();
            var leftWidth=left.width();
            var handleWidth=handle.width();
            var wid=width-leftWidth-handleWidth;
            //显示
            that.data("display","0");
            //分组
            $("#leftGroup").show("slow")
            //菜单
            $("#leftMenu").show("slow");
            //handle
            $("#handle").show("slow");
            right.css("width",wid);
            right.css("right","0");
        }
    })

    layui.config({
        base: 'extension/ext/'
    }).use([ 'sbuadmin', 'element'], function () {
        var $ = layui.$;
        var admin=layui.sbuadmin;
        admin.run({
            ace:ace
        });
    });
})