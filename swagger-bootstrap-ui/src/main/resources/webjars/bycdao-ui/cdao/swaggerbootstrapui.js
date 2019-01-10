/***
 * swagger-bootstrap-ui v1.8.9
 * https://gitee.com/xiaoym/swagger-bootstrap-ui
 *
 * Swagger enhanced UI component package
 *
 * Author: xiaoyumin
 * email:xiaoymin@foxmail.com
 * Copyright: 2017 - 2018, xiaoyumin, http://www.xiaominfo.com/
 *
 * Licensed under Apache License 2.0
 * https://github.com/xiaoymin/Swagger-Bootstrap-UI/blob/master/LICENSE
 *
 * create by xiaoymin on 2018-7-4 15:32:07
 * 重构swagger-bootstrap-ui组件,为以后动态扩展更高效,扩展接口打下基础
 */
(function ($) {

    var SwaggerBootstrapUi=function (options) {
        //swagger请求api地址
        this.url="swagger-resources";
        //文档id
        this.docId="content";
        this.title="swagger-bootstrap-ui";
        this.titleOfUrl="https://gitee.com/xiaoym/swagger-bootstrap-ui";
        this.load=1;
        //tabid
        this.tabId="tabUl";
        this.tabContentId="tabContent";
        this.searchEleId="spanSearch";
        this.searchTxtEleId="searchTxt";
        this.menuId="menu";
        this.searchMenuId="searchMenu";
        //实例分组
        this.instances=new Array();
        //当前分组实例
        this.currentInstance=null;
        //动态tab
        this.globalTabId="sbu-dynamic-tab";
        this.globalTabs=new Array();
        this.layui=options.layui;
        this.ace=options.ace;
        this.treetable=options.treetable;
        this.layTabFilter="admin-pagetabs";
        this.version="1.8.9";
        this.requestOrigion="SwaggerBootstrapUi";
        //个性化配置
        this.settings={
            showApiUrl:false,//接口api地址不显示
            showTagStatus:false,//分组tag显示description属性,针对@Api注解没有tags属性值的情况
            enableSwaggerBootstrapUi:false,//是否开启swaggerBootstrapUi增强
            treeExplain:true,
            enableRequestCache:true//是否开启请求参数缓存
        };
        //SwaggerBootstrapUi增强注解地址
        this.extUrl="/v2/api-docs-ext";
        //验证增强有效地址
        this.validateExtUrl="";
        //缓存api对象,以区分是否是新的api,存储SwaggerBootstapUiCacheApi对象
        this.cacheApis=null;
        this.hasLoad=false;
    }
    /***
     * swagger-bootstrap-ui的main方法,初始化文档所有功能,类似于SpringBoot的main方法
     */
    SwaggerBootstrapUi.prototype.main=function () {
        var that=this;
        that.welcome();
        that.initSettings();
        that.initWindowWidthAndHeight();
        that.initApis();
        that.windowResize();
        //加载分组接口
        that.analysisGroup();
        //创建分组元素
        that.createGroupElement();
        //搜索
        that.searchEvents();
        //tab事件
        that.tabCloseEventsInit();
    }

    SwaggerBootstrapUi.prototype.initApis=function () {
        var that=this;
        if(window.localStorage) {
            var store = window.localStorage;
            var cacheApis=store["SwaggerBootstrapUiCacheApis"];
            if(cacheApis!=undefined&&cacheApis!=null&&cacheApis!=""){
                var settings=JSON.parse(cacheApis);
                that.cacheApis=settings;
            }else{
                that.cacheApis=new Array();
            }
        }
    }

    /***
     * 缓存对象
     */
    SwaggerBootstrapUi.prototype.storeCacheApis=function () {
        var that=this;
        if(window.localStorage) {
            var store = window.localStorage;
            var str=JSON.stringify(that.cacheApis);
            store.setItem("SwaggerBootstrapUiCacheApis",str);
        }
    }


    /***
     * 将接口id加入缓存，再页面点击后
     * @param mid
     */
    SwaggerBootstrapUi.prototype.storeCacheApiAddApiInfo=function (mid) {
        var that=this;
        if(window.localStorage){
            var store = window.localStorage;
            var cacheApis=store["SwaggerBootstrapUiCacheApis"];
            var insid=that.currentInstance.groupId;
            if(cacheApis!=undefined&&cacheApis!=null&&cacheApis!="") {
                var settings = JSON.parse(cacheApis);
                $.each(settings,function (i, s) {
                    if(s.id==insid){
                        s.cacheApis.push(mid);
                    }
                })
                var str=JSON.stringify(settings);
                store.setItem("SwaggerBootstrapUiCacheApis",str);
            }
        }


    }


    /***
     * 读取个性化配置信息
     */
    SwaggerBootstrapUi.prototype.initSettings=function () {
        var that=this;
        if(window.localStorage){
            var store = window.localStorage;
            var globalSettings=store["SwaggerBootstrapUiSettings"];
            if(globalSettings!=undefined&&globalSettings!=null&&globalSettings!=""){
                var settings=JSON.parse(globalSettings);
                that.settings=$.extend({},that.settings,settings);
                that.log("settings-----------------")
                that.log(settings)
            }
        }
    }



    SwaggerBootstrapUi.prototype.initScrollEvent=function (id) {
        var that=this;
        that.log("initScrollEvent......................")
        $("#"+id).scroll(function () {
            that.log("滚动----------------")
            that.removeLayerTips();
        })
    }

    /***
     * 移除layui-tips弹出框
     */
    SwaggerBootstrapUi.prototype.removeLayerTips=function () {
        $(".layui-table-tips").remove();
    }

    /***
     * 搜索按钮事件
     */
    SwaggerBootstrapUi.prototype.searchEvents=function () {
       var that=this;
       that.log("searchEvents");
       that.log($("#"+that.searchEleId));
       that.removeLayerTips();
       $("#"+that.searchEleId).on("click",function (e) {
           var val=$("#"+that.searchTxtEleId).val();
           if(val){
               that.log("搜索值："+val);
               var regx=".*?"+val+".*";
               //遍历分组
               var newTagArrs=new Array();
               that.log("开始查询...")
               that.log(new Date());
               $.each(that.instances,function (i, ins) {
                   that.log(ins);
                   var tags=ins.tags;
                   if(tags!=null&&tags!=undefined&&tags.length>0){
                       //只需遍历tags即可
                       $.each(tags,function (j, tag) {
                           var flag=false;
                           var sbtag=new SwaggerBootstrapUiTag(tag.name,tag.description);
                           if($.regexMatchStr(regx,tag.name)){
                               //如果匹配，全部添加
                               sbtag.childrens=tag.childrens;
                           }else{
                               if(tag.childrens!=null&&tag.childrens.length>0){
                                   $.each(tag.childrens,function (a, children) {
                                       //判断url是否匹配,简介是否匹配,类型是否匹配
                                       var urlflag=$.regexMatchStr(regx,children.url);
                                       var sumflag=$.regexMatchStr(regx,children.summary);
                                       var methodflag=$.regexMatchStr(regx,children.methodType);
                                       var desflag=$.regexMatchStr(regx,children.description);
                                       if(urlflag||sumflag||methodflag||desflag){
                                           sbtag.childrens.push(children);
                                       }
                                   })
                               }
                           }
                           if(sbtag.childrens.length>0){
                               newTagArrs.push(sbtag);
                           }
                       })

                   }
               })
               that.log(new Date());
               //隐藏
               that.getMenu().hide();
               that.getSearchMenu().show();
               //创建菜单明细按钮
               that.getSearchMenu().html("");
               if(newTagArrs.length>0){
                   $.each(newTagArrs,function (i, tag) {
                       var len=tag.childrens.length;
                       if(len==0){
                           var li=$('<li class="detailMenu"><a href="javascript:void(0)"><i class="icon-text-width iconfont icon-APIwendang"></i><span class="menu-text"> '+tag.name+' </span></a></li>');
                           that.getSearchMenu().append(li);
                       }else{
                           //存在子标签
                           var li=$('<li  class="detailMenu"></li>');
                           var titleA=$('<a href="#" class="dropdown-toggle"><i class="icon-file-alt icon-text-width iconfont icon-APIwendang"></i><span class="menu-text"> '+tag.name+'<span class="badge badge-primary ">'+len+'</span></span><b class="arrow icon-angle-down"></b></a>');
                           li.append(titleA);
                           //循环树
                           var ul=$('<ul class="submenu"></ul>')
                           $.each(tag.childrens,function (i, children) {
                               var childrenLi=$('<li class="menuLi" ><div class="mhed"><div class="swu-hei"><span class="swu-menu swu-left"><span class="menu-url-'+children.methodType.toLowerCase()+'">'+children.methodType.toUpperCase()+'</span></span><span class="swu-menu swu-left"><span class="menu-url">'+children.url+'</span></span></div><div class="swu-menu-api-des">'+children.summary+'</div></div></li>');
                               childrenLi.data("data",children);
                               ul.append(childrenLi);
                           })
                           li.append(ul);
                           that.getSearchMenu().append(li);
                       }
                   })
                   that.getSearchMenu().find(".menuLi").bind("click",function (e) {
                       e.preventDefault();
                       var menu=$(this);
                       var data=menu.data("data");
                       that.log("Li标签click事件");
                       that.removeLayerTips();
                       that.log(data);
                       //获取parent-Li的class属性值
                       var parentLi=menu.parent().parent();
                       that.log(parentLi);
                       var className=parentLi.prop("class");
                       that.log(className)
                       that.getMenu().find("li").removeClass("active");
                       //parentLi.addClass("active");
                       menu.addClass("active");
                       that.createApiInfoTable(data,menu);
                       //DApiUI.createDebugTab(data);
                   })
               }
           }else{
               that.getMenu().show();
           }
       });
       //keyup事件
       $("#"+that.searchTxtEleId).on("keyup",function () {
           var value=$(this).val();
           if(!value){
               that.getMenu().show();
               that.getSearchMenu().hide();
           }
       });
       //回车事件;
        $(document).keydown(function(event){
            if(event.keyCode == 13){ //绑定回车
                $("#"+that.searchEleId).click();
            }
        });

    }
    /***
     * 调用swagger的分组接口,获取swagger分组信息,包括分组名称,接口url地址,版本号等
     */
    SwaggerBootstrapUi.prototype.analysisGroup=function () {
        var that=this;
        $.ajax({
            url:that.url,
            type:"get",
            dataType:"json",
            async:false,
            success:function (data) {
                that.log("请求成功");
                that.log(data);
                var t=typeof(data);
                var groupData=null;
                if(t=="string"){
                    groupData=JSON.parse(data);
                }else{
                    groupData=data;
                }
                that.log("响应分组json数据");
                that.log(groupData);
                $.each(groupData,function (i, group) {
                    var g=new SwaggerBootstrapUiInstance(group.name,group.location,group.swaggerVersion);
                    g.url=group.url;
                    var newUrl="";
                    //此处需要判断basePath路径的情况
                    if (group.url!=null&&group.url!=undefined&&group.url!=""){
                        newUrl=group.url;
                    }else{
                        newUrl=group.location;
                    }
                    var extBasePath="";
                    var idx=newUrl.indexOf("/v2/api-docs");
                    if(idx>0){
                        //增强地址存在basePath
                        extBasePath=newUrl.substr(0,idx);
                    }
                    that.log("增强basePath地址："+extBasePath);
                    //赋值增强地址
                    g.extUrl=extBasePath+that.extUrl+"?group="+group.name;
                    if(that.validateExtUrl==""){
                        that.validateExtUrl=g.extUrl;
                    }
                    //赋值查找缓存的id
                    if (that.cacheApis.length>0){
                        var cainstance=null;
                        $.each(that.cacheApis,function (x, ca) {
                            if(ca.id==g.groupId){
                                cainstance=ca;
                            }
                        })
                        if (cainstance!=null){
                            g.firstLoad=false;
                            g.groupApis=cainstance.cacheApis;
                        }
                    }
                    that.instances.push(g);
                })
            },
            error:function (xhr, textStatus, errorThrown) {
                that.log("error...")
                that.log(xhr)
                that.log(textStatus);
                that.log(errorThrown)
            }
        })
    }



    /***
     * 基础实例赋值
     * @param menu
     */
    SwaggerBootstrapUi.prototype.setInstanceBasicPorperties=function (menu) {
        var that=this;
        var title="",description="",name="",version="",termsOfService="";
        var host=$.getValue(menu,"host","",true);
        if (menu.hasOwnProperty("info")){
            var info=menu.info;
            title=$.getValue(info,"title","Swagger-Bootstrap-UI-前后端api接口文档",true);
            description=$.getValue(info,"description","",true);
            if(info.hasOwnProperty("contact")){
                var contact=info["contact"];
                name=$.getValue(contact,"name","",true);
            }
            version=$.getValue(info,"version","",true);
            termsOfService=$.getValue(info,"termsOfService","",true);
        }
        that.currentInstance.host=host;
        that.currentInstance.title=title;
        that.currentInstance.description=description;
        that.currentInstance.contact=name;
        that.currentInstance.version=version;
        that.currentInstance.termsOfService=termsOfService;
        that.currentInstance.basePath=menu["basePath"];
        //that.currentInstance.basePath="/";
        //设置doc.html文档的title属性
        if(title!=null&&title!=undefined&&title!=""){
            $("title").html(title);
            if(that.load==1){
                $("#swaggerBootstrapHrefTitle").html(title);
                that.load=2;
                $("#swaggerBootstrapHrefTitle").attr("href","javascript:void(0)")
            }
        }else{
            $("#swaggerBootstrapHrefTitle").html(that.title);
            $("#swaggerBootstrapHrefTitle").attr("href",that.titleOfUrl)
        }
    }
    /***
     * 加载swagger的分组详情接口
     * @param instance 分组接口请求实例
     */
    SwaggerBootstrapUi.prototype.analysisApi=function (instance) {
        var that=this;
        //赋值
        that.currentInstance=instance;
        if(!that.currentInstance.load){
            var api=instance.url;
            if (api==undefined||api==null||api==""){
                api=instance.location;
            }
            //判断是否开启增强功能
            if (that.settings.enableSwaggerBootstrapUi){
                api=instance.extUrl;
            }
            //这里判断url请求是否已加载过
            //防止出现根路径的情况
            var idx=api.indexOf("/");
            if(idx==0){
                api=api.substr(1);
            }
           /* that.log("截取后的url:"+api);
            api="/webjars/bycdao-ui/cdao/d1.json";
            that.log("截取后的url:"+api);*/
            var async=that.hasLoad;
            that.log("是否开启异步加载："+async)

            $.ajax({
                //url:"v2/api-docs",
                url:api,
                dataType:"json",
                type:"get",
                async:async,
                success:function (data) {
                    that.hasLoad=true;
                    //var menu=JSON.parse(data);
                    that.log("success")
                    that.log(data);
                    var t=typeof(data);
                    var menu=null;
                    if(t=="string"){
                        menu=JSON.parse(data);
                    }else{
                        menu=data;
                    }
                    that.setInstanceBasicPorperties(menu);
                    that.analysisDefinition(menu);
                    //DApiUI.definitions(menu);
                    that.log(menu);
                    that.createDescriptionElement();
                    //当前实例已加载
                    that.currentInstance.load=true;
                    //创建swaggerbootstrapui主菜单
                    that.createDetailMenu();
                },
                error:function (xhr, textStatus, errorThrown) {
                    that.log("error...")
                    that.log(xhr);
                    that.log(textStatus);
                    that.log(errorThrown);
                    that.hasLoad=true;
                    var txt=xhr.responseText;
                    //替换带[]
                    that.log("replace...")
                    var replaceData=txt.replace(/'/g,"\"");
                    var menu=JSON.parse(replaceData);
                    that.setInstanceBasicPorperties(menu);
                    that.analysisDefinition(menu);
                    //DApiUI.definitions(menu);
                    that.log(menu);
                    that.createDescriptionElement();
                    //当前实例已加载
                    that.currentInstance.load=true;
                    //创建swaggerbootstrapui主菜单
                    that.createDetailMenu();

                }
            })
        }else{
            //更新当前缓存security
            that.updateCurrentInstanceSecuritys();
            that.createDescriptionElement();
            that.createDetailMenu();
        }
    }
    /***
     * 创建左侧菜单按钮
     * @param menu
     */
    SwaggerBootstrapUi.prototype.createDetailMenu=function () {
        var that=this;
        that.getMenu().find(".detailMenu").remove();
        //简介li
        var dli=$('<li  class="active detailMenu"><a href="javascript:void(0)"><i class="icon-text-width iconfont icon-icon_home"></i><span class="menu-text"> 主页 </span></a></li>')
        dli.on("click",function () {
            that.log("简介click")
            that.createDescriptionElement();
            that.getMenu().find("li").removeClass("active");
            dli.addClass("active");
        })
        that.getMenu().append(dli);
        //是否有全局参数
        if(that.currentInstance.securityArrs!=null&&that.currentInstance.securityArrs.length>0){
            var securityLi=$('<li  class="detailMenu"><a href="javascript:void(0)"><i class="icon-text-width iconfont icon-authenticationsystem"></i><span class="menu-text"> Authorize </span></a></li>');
            securityLi.on("click",function () {
                that.log("securityLi");
                that.createSecurityElement();
                that.getMenu().find("li").removeClass("active");
                securityLi.addClass("active");
            })
            that.getMenu().append(securityLi);
        }
        //Swagger通用Models add by xiaoyumin 2018-11-6 13:26:45
        var modelsLi=$('<li  class="detailMenu"><a href="javascript:void(0)"><i class="icon-text-width iconfont icon-modeling"></i><span class="menu-text">Swagger Models </span></a></li>');
        modelsLi.on("click",function () {
            that.log("Models");
            that.createModelsElement();
            that.getMenu().find("li").removeClass("active");
            modelsLi.addClass("active");
        })
        that.getMenu().append(modelsLi);
        //SwaggerBootstrapUi增强功能全部放置在此
        //存在子标签
        var extLi=$('<li  class="detailMenu"></li>');
        var exttitleA=$('<a href="#" class="dropdown-toggle"><i class="icon-file-alt icon-text-width iconfont icon-zhongduancanshuguanli"></i><span class="menu-text"> 文档管理</span><span class="badge badge-primary ">3</span><b class="arrow icon-angle-down"></b></a>');
        extLi.append(exttitleA);
        //循环树
        var extul=$('<ul class="submenu"></ul>')
        //全局参数菜单功能
        var globalArgsLi=$('<li class="menuLidoc" ><div class="mhed"><div class="swu-hei-none-url"><span class="swu-menu swu-left">全局参数设置</span> </div></div></li>');
        //var globalArgsLi=$("<li  class=\"detailMenu\"><a href=\"javascript:void(0)\"><i class=\"icon-text-width iconfont icon-zhongduancanshuguanli\"></i><span class=\"menu-text\"> 全局参数设置 </span></a></li>");
        globalArgsLi.on("click",function () {
            that.getMenu().find("li").removeClass("active");
            globalArgsLi.addClass("active");
            that.createGlobalParametersElement();
        })
        extul.append(globalArgsLi);

        //离线文档功能
        var mddocli=$('<li class="menuLidoc" ><div class="mhed"><div class="swu-hei-none-url"><span class="swu-menu swu-left">离线文档(MD)</span> </div></div></li>');
        //var mddocli=$("<li  class=\"detailMenu\"><a href=\"javascript:void(0)\"><i class=\"icon-text-width iconfont icon-iconset0118\"></i><span class=\"menu-text\"> 离线文档(MD) </span></a></li>");
        mddocli.on("click",function () {
            that.log("离线文档功能click")
            that.createMarkdownTab();
            that.getMenu().find("li").removeClass("active");
            mddocli.addClass("active");
        })
        extul.append(mddocli);
        //个性化设置
        var settingsli=$('<li class="menuLidoc" ><div class="mhed"><div class="swu-hei-none-url"><span class="swu-menu swu-left">个性化设置</span> </div></div></li>');
        settingsli.on("click",function () {
            that.log("个性化设置功能click")
            that.createSettingsPage();
            that.getMenu().find("li").removeClass("active");
            settingsli.addClass("active");
        })
        extul.append(settingsli);

        extLi.append(extul);
        that.getMenu().append(extLi);



        $.each(that.currentInstance.tags,function (i, tag) {
            var len=tag.childrens.length;
            if(len==0){
                var li=null;
                if (that.settings.showTagStatus){
                    li=$('<li class="detailMenu"><a href="javascript:void(0)"><i class="icon-text-width iconfont icon-APIwendang"></i><span class="menu-text sbu-tag-description"> '+tag.name+"("+tag.description+') </span></a></li>');
                }else{
                    li=$('<li class="detailMenu"><a href="javascript:void(0)"><i class="icon-text-width iconfont icon-APIwendang"></i><span class="menu-text"> '+tag.name+' </span></a></li>');
                }
                that.getMenu().append(li);
            }else{
                //存在子标签
                var li=$('<li  class="detailMenu"></li>');

                var tagNewApiIcon="";
                if(tag.hasNew){
                    tagNewApiIcon='<i class="iconfont icon-xinpin" style="float: right;right: 30px;position: absolute;"></i>';
                }
                var titleA=null;
                if(that.settings.showTagStatus){
                    titleA=$('<a href="#" class="dropdown-toggle"><i class="icon-file-alt icon-text-width iconfont icon-APIwendang"></i><span class="menu-text sbu-tag-description"> '+tag.name+"("+tag.description+')<span class="badge badge-primary ">'+len+'</span></span>'+tagNewApiIcon+'<b class="arrow icon-angle-down"></b></a>');
                }else{
                    titleA=$('<a href="#" class="dropdown-toggle"><i class="icon-file-alt icon-text-width iconfont icon-APIwendang"></i><span class="menu-text"> '+tag.name+'<span class="badge badge-primary ">'+len+'</span></span>'+tagNewApiIcon+'<b class="arrow icon-angle-down"></b></a>');
                }
                //var titleA=$('<a href="#" class="dropdown-toggle"><i class="icon-file-alt icon-text-width iconfont icon-APIwendang"></i><span class="menu-text"> '+tag.name+'<span class="badge badge-primary ">'+len+'</span></span><b class="arrow icon-angle-down"></b></a>');
                li.append(titleA);
                //循环树
                var ul=$('<ul class="submenu"></ul>')


                  $.each(tag.childrens,function (i, children) {
                    var childrenLi=null;
                    var newApiIcon="";
                    if (children.hasNew){
                        //新接口
                        newApiIcon='<i class="iconfont icon-new-api" style="position: absolute;font-size:32px;"></i>';
                    }
                    var depStyle=' ';
                    if(children.deprecated){
                        depStyle=' style="text-decoration:line-through;"';
                    }

                    if(that.settings.showApiUrl){
                        //显示api地址
                        childrenLi=$('<li class="menuLi" >'+newApiIcon+'<div class="mhed"><div class="swu-hei"><span class="swu-menu swu-left"><span class="menu-url-'+children.methodType.toLowerCase()+'">'+children.methodType.toUpperCase()+'</span></span><span class="swu-menu swu-left"><span class="menu-url"  '+depStyle+'>'+children.summary+'</span></span></div><div class="swu-menu-api-des"><span  '+depStyle+'>'+children.showUrl+'</span></div></div></li>');
                    }else{
                        //不显示api地址
                        childrenLi=$('<li class="menuLi" >'+newApiIcon+'<div class="mhed"><div class="swu-hei-none-url"><span class="swu-menu swu-left"><span class="menu-url-'+children.methodType.toLowerCase()+'">'+children.methodType.toUpperCase()+'</span></span><span class="swu-menu swu-left"><span class="menu-url" '+depStyle+'>'+children.summary+'</span></span></div></div></li>');
                    }
                    childrenLi.data("data",children);
                    ul.append(childrenLi);
                })
                li.append(ul);
                that.getMenu().append(li);
            }
        })
        that.log("菜单初始化完成...")
        //DApiUI.initLiClick();
        that.initializationMenuClickEvents();
    }

    /***
     * 创建个性化配置页面
     */
    SwaggerBootstrapUi.prototype.createSettingsPage=function () {
        var that=this;
        var layui=that.layui;
        var element=layui.element;
        var tabId="SwaggerBootstrapUiSettingsScript";
        var tabContetId="layerTab"+tabId;
        //内容覆盖
        setTimeout(function () {
            if (!that.tabExists(tabId)) {
                that.log("settings-----------------")
                that.log(that.settings)
                var html = template(tabId, that.settings);
                var tabObj={
                    id:tabId,
                    title:'个性化设置',
                    content:html
                };
                that.globalTabs.push({id:tabId,title:'个性化设置'});
                element.tabAdd(that.layTabFilter, tabObj);
                element.tabChange(that.layTabFilter,tabId);
                that.tabFinallyRight();
                //保存按钮功能
                $("#btnSaveSettings").on("click",function (e) {
                    e.preventDefault();
                    var showApi=$("#SwaggerBootstrapUiSettings").find("input[name=showApi]");
                    var enableSbu=$("#SwaggerBootstrapUiSettings").find("input[name=enableSwaggerBootstrapUi]");
                    //tag属性说明
                    var showTagStatusElem=$("#SwaggerBootstrapUiSettings").find("input[name=showTagStatus]");

                    var enableRequestCache=$("#SwaggerBootstrapUiSettings").find("input[name=enableRequestCache]");

                    var showApiFlag=showApi.prop("checked");
                    var enableSbuFlag=enableSbu.prop("checked");
                    var showTagStatus=showTagStatusElem.prop("checked");

                    var cacheRequest=enableRequestCache.prop("checked");

                    var flag=true;
                    //如果开启SwawggerBootstrapUi增强,则判断当前后端是否启用注解
                    if(enableSbuFlag){
                        var api=that.validateExtUrl;
                        var idx=api.indexOf("/");
                        if(idx==0){
                            api=api.substr(1);
                        }
                        that.log("验证api地址："+api);
                        $.ajax({
                            url:api,
                            dataType:"json",
                            type:"get",
                            async:false,
                            success:function (data) {
                                that.log("验证成功...")
                            },
                            error:function (xhr, textStatus, errorThrown) {
                                that.log("验证error...")
                                that.log(xhr);
                                //获取响应码
                                var status=xhr.status;
                                if(status!=200){
                                    layer.msg("无法开启SwaggerBootstrapUi增强功能,请确保后端启用注解@EnableSwaggerBootstrapUi");
                                    enableSbu.prop("checked",false);
                                    flag=false;
                                }
                            }
                        })
                    }
                    if (flag){
                        that.log(showApi.prop("checked")+",enable:"+enableSbu.prop("checked"));
                        var setts={
                            showApiUrl:showApiFlag,//接口api地址不显示
                            showTagStatus:showTagStatus,//tag显示description属性.
                            enableSwaggerBootstrapUi:enableSbuFlag,//是否开启swaggerBootstrapUi增强
                            enableRequestCache:cacheRequest
                        }
                        that.saveSettings(setts);
                        if (!cacheRequest){
                            that.disableStoreRequestParams();
                        }
                    }
                })
            }else{
                element.tabChange(that.layTabFilter,tabId);
                that.tabRollPage("auto");
            }
        },100)

    }

    /***
     * 保存SwaggerBootstrapUi 个性化配置信息
     * @param settings
     */
    SwaggerBootstrapUi.prototype.saveSettings=function (settings) {
        if(window.localStorage){
            var store = window.localStorage;
            var gbStr=JSON.stringify(settings);
            store.setItem("SwaggerBootstrapUiSettings",gbStr);
            layer.msg("保存成功,请刷新该文档页");
        }else{
            layer.msg("当前浏览器不支持localStorage对象,无法使用该功能");
        }
    }
    /***
     * 创建全局参数
     */
    SwaggerBootstrapUi.prototype.createGlobalParametersElement=function () {
        var that=this;
        var layui=that.layui;
        var element=layui.element;
        var tabId="GlobalParamScript";
        var tabContetId="layerTab"+tabId;
        //内容覆盖
        setTimeout(function () {
            if(!that.tabExists(tabId)){
                that.currentInstance.globalParameters=that.getGlobalParameters();
                var html = template('GlobalParamScript', that.currentInstance);
                var tabObj={
                    id:tabId,
                    title:'全局参数设置',
                    content:html
                };
                that.globalTabs.push({id:tabId,title:'全局参数设置'});
                element.tabAdd(that.layTabFilter, tabObj);
                element.tabChange(that.layTabFilter,tabId);
                that.tabFinallyRight();
                //初始化添加按钮click事件
                that.getDoc().find("#"+tabContetId).find("#btnAddParam").on("click",function (e) {
                    e.preventDefault();
                    that.log("btnAddParam-click")
                    var tr=$("<tr></tr>");
                    //输入参数名称
                    var nameTd=$("<td><input class=\"form-control p-key\" value=\"\" data-old=''></td>");
                    //参数值
                    var valueTd=$("<td><input class=\"form-control p-key\" value=\"\"></td>");
                    //参数类型
                    var typeTd=$("<td><select class='form-control'><option value='header'>header</option><option value='query'>query</option></select></td>");
                    //操作
                    var operateTd=$("<td>&nbsp;&nbsp;</td>")
                    var btnSave=$("<button class=\"btn btn-circle btn-info btn-small btn-save\" type=\"button\">保存</button>");
                    var btnCancel=$("<button class=\"btn btn-circle  btn-danger btn-small btn-cancel\" type=\"button\">删除</button>");
                    operateTd.append(btnSave).append("&nbsp;&nbsp;").append(btnCancel);
                    tr.append(nameTd).append(valueTd).append(typeTd).append(operateTd);
                    that.getDoc().find("#globalTabBody").append(tr);
                    //保存事件
                    btnSave.on("click",function (e) {
                        e.preventDefault();
                        var save=$(this);
                        var ptr=save.parent().parent();
                        var name=ptr.find("td:eq(0)").find("input:first").val();
                        var oldname=ptr.find("td:eq(0)").find("input:first").data("old");
                        var value=ptr.find("td:eq(1)").find("input:first").val();
                        var type=ptr.find("td:eq(2)").find("select:first").val();
                        that.log("name:"+name+",value:"+value+",type:"+type+",oldname:"+oldname);
                        if(name==null||name==""){
                            layer.msg("请输入全局参数名称");
                            return false;
                        }
                        if(value==null||value==""){
                            layer.msg("请输入全局参数值");
                            return false;
                        }
                        var globalParameterInstance=new SwaggerBootstrapUiParameter();
                        globalParameterInstance.name=name;
                        globalParameterInstance.in=type;
                        globalParameterInstance.value=value;
                        globalParameterInstance.txtValue=value;
                        globalParameterInstance.type="string";
                        globalParameterInstance.require=true;
                        //判断old
                        if(oldname!=null&&oldname!=""&&oldname!=name){
                            //删除旧参数
                            that.deleteGlobalParamsByName(oldname);
                        }
                        if (!that.checkGlobalParamExists(globalParameterInstance)){
                            that.storeGlobalParam(globalParameterInstance,"globalParameters")
                            //that.currentInstance.globalParameters.push(globalParameterInstance);
                        }else{
                            //存在,更新该参数的值
                            that.updateGlobalParams(globalParameterInstance,"globalParameters");
                        }
                        that.log("目前全局参数..")
                        that.log(that.currentInstance.globalParameters);
                        layer.msg("保存成功")
                    })
                    //取消时间
                    btnCancel.on("click",function (e) {
                        e.preventDefault();
                        var cancel=$(this);
                        that.log(cancel)
                        var ptr=cancel.parent().parent();
                        var name=ptr.find("td:eq(0)").find("input:first").val();
                        var oldname=ptr.find("td:eq(0)").find("input:first").data("old");
                        if(oldname!=name){
                            that.deleteGlobalParamsByName(oldname)
                        }
                        if(name!=undefined&& name!=null&&name!=""){
                            that.deleteGlobalParamsByName(name);
                        }
                        cancel.parent().parent().remove();
                        layer.msg("删除成功")
                    })
                })
                //全局保存事件
                that.getDoc().find("#"+tabContetId).find(".btn-save").on("click",function (e) {
                    var save=$(this);
                    var ptr=save.parent().parent();
                    var name=ptr.find("td:eq(0)").find("input:first").val();
                    var oldname=ptr.find("td:eq(0)").find("input:first").data("old");
                    var value=ptr.find("td:eq(1)").find("input:first").val();
                    var type=ptr.find("td:eq(2)").find("select:first").val();
                    that.log("name:"+name+",value:"+value+",type:"+type+",oldname:"+oldname);
                    if(name==null||name==""){
                        layer.msg("请输入全局参数名称");
                        return false;
                    }
                    if(value==null||value==""){
                        layer.msg("请输入全局参数值");
                        return false;
                    }
                    var globalParameterInstance=new SwaggerBootstrapUiParameter();
                    globalParameterInstance.name=name;
                    globalParameterInstance.in=type;
                    globalParameterInstance.value=value;
                    that.log(oldname!=name)
                    //判断old
                    if(oldname!=name){
                        //删除旧参数
                        that.deleteGlobalParamsByName(oldname);
                    }
                    if (!that.checkGlobalParamExists(globalParameterInstance)){
                        that.storeGlobalParam(globalParameterInstance,"globalParameters")
                        //that.currentInstance.globalParameters.push(globalParameterInstance);
                    }else{
                        //存在,更新该参数的值
                        that.updateGlobalParams(globalParameterInstance,"globalParameters");
                    }
                    that.log("目前全局参数..")
                    that.log(that.currentInstance.globalParameters);
                    layer.msg("保存成功")
                })
                //全局取消事件
                that.getDoc().find("#"+tabContetId).find(".btn-cancel").on("click",function (e) {
                    e.preventDefault();
                    var cancel=$(this);
                    that.log(cancel)
                    var ptr=cancel.parent().parent();
                    var name=ptr.find("td:eq(0)").find("input:first").val();
                    var oldname=ptr.find("td:eq(0)").find("input:first").data("old");
                    if(oldname!=null&&oldname!=""){
                        that.deleteGlobalParamsByName(oldname)
                    }
                    if(name!=undefined&& name!=null&&name!=""){
                        that.deleteGlobalParamsByName(name);
                    }
                    cancel.parent().parent().remove();
                    layer.msg("删除成功")
                })
            }else{
                element.tabChange(that.layTabFilter,tabId);
                that.tabRollPage("auto");
            }
        },100)


    }

    /***
     * 获取security
     */
    SwaggerBootstrapUi.prototype.getSecurityInfos=function () {
        var that=this;
        var params=[];
        if(window.localStorage){
            var store = window.localStorage;
            var globalparams=store["securityArrs"];
            if(globalparams!=undefined&&globalparams!=null&&globalparams!=""){
                params=JSON.parse(globalparams);
            }
        }else{
            params=$("#sbu-header").data("cacheSecurity");
        }
        return params;
    }

    /***
     * 获取全局参数
     * @returns {Array}
     */
    SwaggerBootstrapUi.prototype.getGlobalParameters=function () {
        var that=this;
        var params=[];
        if(window.localStorage) {
            var store = window.localStorage;
            var globalparams=store["globalParameters"];
            if(globalparams!=undefined&&globalparams!=null){
                params=JSON.parse(globalparams);
            }
        }else{
            params=that.currentInstance.globalParameters;
        }
        return params;
    }
    /***
     * 判断全局参数是否存在
     * @param param
     */
    SwaggerBootstrapUi.prototype.checkGlobalParamExists=function (param) {
        var that=this;
        var flag=false;
        if(window.localStorage) {
            var store = window.localStorage;
            var globalparams=store["globalParameters"];
            if(globalparams!=undefined&&globalparams!=null&&globalparams!=""){
                globalparams=JSON.parse(globalparams);
                $.each(globalparams,function (i, gp) {
                    if(gp.name==param.name){
                        flag=true;
                    }
                })
            }
        }else{
            $.each(that.currentInstance.globalParameters,function (i, gp) {
                if(gp.name==param.name){
                    flag=true;
                }
            })
        }
        return flag;
    }
    SwaggerBootstrapUi.prototype.updateGlobalParams=function (param,key) {
        var that=this;
        if(window.localStorage) {
            var store = window.localStorage;
            var globalparams=store[key];
            globalparams=JSON.parse(globalparams);
            $.each(globalparams,function (i, gp) {
                if(gp.name==param.name){
                    gp.in=param.in;
                    gp.value=param.value;
                    gp.txtValue=param.value;
                }
            })
            var gbStr=JSON.stringify(globalparams);
            store.setItem(key,gbStr);
        }else{
            $.each(that.currentInstance[key],function (i, gp) {
                if(gp.name==param.name){
                    gp.in=param.in;
                    gp.value=param.value;
                    gp.txtValue=param.value;
                }
            })
        }
    }
    /***
     * 根据名称删除全局参数数组
     * @param name
     */
    SwaggerBootstrapUi.prototype.deleteGlobalParamsByName=function (name) {
        var that=this;
        if(window.localStorage){
            var store=window.localStorage;
            var globalparams=store["globalParameters"];
            globalparams=JSON.parse(globalparams);
            for(var i=0;i<globalparams.length;i++){
                var gp=globalparams[i];
                if (gp.name==name){
                    globalparams.splice(i,1);
                }
            }
            var gbStr=JSON.stringify(globalparams);
            store.setItem("globalParameters",gbStr);
        }else{
            for(var i=0;i<that.currentInstance.globalParameters.length;i++){
                var gp=that.currentInstance.globalParameters[i];
                if (gp.name==name){
                    that.currentInstance.globalParameters.splice(i,1);
                }
            }
        }

    }
    /***
     * 存储全局变量
     * @param obj
     */
    SwaggerBootstrapUi.prototype.storeGlobalParam=function (obj,key) {
        var that=this;
        //判断浏览器是否支持localStorage
        if(window.localStorage){
            var store=window.localStorage;
            var globalparams=store[key];
            if(globalparams!=undefined&&globalparams!=null&&globalparams!=""){
                //exists
                globalparams=JSON.parse(globalparams);
                that.log("获取缓存............")
                that.log(globalparams)
                globalparams.push(obj);
            }else{
                //not exists
                globalparams=new Array();
                globalparams.push(obj);
            }
            var gbStr=JSON.stringify(globalparams);
            store.setItem(key,gbStr);
        }else{
            that.currentInstance[key].push(obj);
        }
    }
    /***
     * 初始化菜单点击事件
     */
    SwaggerBootstrapUi.prototype.initializationMenuClickEvents=function () {
        var that=this;
        that.getMenu().find(".menuLi").bind("click",function (e) {
            e.preventDefault();
            var menu=$(this);
            var data=menu.data("data");
            that.log("Li标签click事件");
            that.removeLayerTips();
            that.log(data);
            //获取parent-Li的class属性值
            var parentLi=menu.parent().parent();
            that.log(parentLi);
            var className=parentLi.prop("class");
            that.log(className)
            that.getMenu().find("li").removeClass("active");
            //parentLi.addClass("active");
            menu.addClass("active");
            that.createApiInfoTable(data,menu);
            //DApiUI.createDebugTab(data);
        })
    }

    SwaggerBootstrapUi.prototype.createApiInfoTable=function (apiInfo,menu) {
        var that=this;
        var element=that.layui.element;
        var treetable=that.treetable;
        if (apiInfo.hasNew){
            //存储id
            that.log("新接口,存储")
            that.storeCacheApiAddApiInfo(apiInfo.id);
        }
        var tabId="tab"+apiInfo.id;
        var layerTabId="layerTab"+tabId;
        //判断tabId是否存在
        if(that.tabExists(tabId)){
            element.tabChange(that.layTabFilter,tabId);
            that.tabRollPage("auto");
        }else{
            //that.createTabElement();
            //html转义
            var dynaTab=template('BootstrapDynaTab',apiInfo);
            //不存在,添加
            var tabObj={id: tabId, title: apiInfo.summary, content: dynaTab};
            element.tabAdd(that.layTabFilter, tabObj);
            element.tabChange(that.layTabFilter,tabId);
            that.tabFinallyRight();

            //that.markdownDocInit(docTextId,contentDocId);
            var requestTableId="requestParameter"+apiInfo.id;
            var data=[];
            if(apiInfo.parameters!=null&&apiInfo.parameters.length>0){
                data=data.concat(apiInfo.parameters);
            }
            if(apiInfo.refTreetableparameters!=null&&apiInfo.refTreetableparameters.length>0){
                $.each(apiInfo.refTreetableparameters,function (i, ref) {
                    data=data.concat(ref.params);
                })
            }
            that.log("treeTable----------------data-------------------------")
            that.log(data);
            that.log("排序")
            if(data!=null){
                data.sort(function (a, b) {
                    return b.require-a.require;
                })
            }
            treetable.render({
              elem:"#"+requestTableId,
                data: data,
                field: 'title',
                treeColIndex: 0,          // treetable新增参数
                treeSpid: -1,             // treetable新增参数
                treeIdName: 'd_id',       // treetable新增参数
                treePidName: 'd_pid',     // treetable新增参数
                treeDefaultClose: true,   // treetable新增参数
                treeLinkage: true,        // treetable新增参数
                cols: [[
                    {
                        field: 'name',
                        title: '参数名称',
                        width: '20%'
                    },
                    {
                        field: 'description',
                        title: '说明',
                        width: '20%'
                    },
                    {
                        field: 'in',
                        title: '请求类型',
                        width: '10%',
                        templet:function (d) {
                            return "<span class='sbu-request-"+d.in+"'>"+d.in+"</span>";
                        }
                    },
                    {
                        field: 'require',
                        title: '必填',
                        width: '10%',
                        templet:function (d) {
                            if(d.require){
                                return "<span style='color:red;'>"+d.require+"</span>";
                            }else{
                                return "<span style='color:black;'>"+d.require+"</span>";
                            }
                        }
                    },
                    {
                        field: 'type',
                        title: '类型',
                        width: '20%',
                        templet:function (d) {
                            if(d.validateStatus){
                                var str="";
                                if (d.validateInstance!=null){
                                    var len=$.getJsonKeyLength(d.validateInstance);
                                    var _size=0;
                                    for(var k in d.validateInstance){
                                        str+=k+":"+d.validateInstance[k];
                                        if (_size<len){
                                            str+="\r\n"
                                        }
                                        _size++;
                                    }
                                }
                                return "<a href='javascript:void(0)' class='sbu-request-validate-jsr' data-tips='"+str+"' title='"+str+"'>"+d.type+"</a>";
                            }else{
                                return d.type;
                            }
                        }
                    },
                    {
                        field: 'schemaValue',
                        title: 'schema',
                        width: '20%'
                    }
                ]]
            })
            //默认全部展开
            treetable.expandAll('#'+requestTableId);
            $("#"+requestTableId).hide();

            //响应参数行
            if(apiInfo.multipartResponseSchema){
                var apiId=apiInfo.id;
                if(apiInfo.requestValue!=null){
                    var sampleRequestId="editorRequestSample"+apiId;
                    var editor = ace.edit(sampleRequestId);
                    /*var JsonMode = ace.require("ace/mode/json").Mode;
                    editor.session.setMode(new JsonMode());*/
                    editor.getSession().setMode("ace/mode/json");
                    editor.setTheme("ace/theme/eclipse");
                    var length_editor = editor.session.getLength();
                    var rows_editor = length_editor * 16;
                    that.log("rows_editor:"+rows_editor);
                    $("#"+sampleRequestId).css('height',rows_editor);
                    editor.resize();
                }
                $.each(apiInfo.responseCodes,function (ixc, rc) {
                    if(rc.schema!=undefined&&rc.schema!=null){
                        //响应参数
                        var responseTableId="responseParameter"+apiId+"-"+rc.code;
                        var respdata=[];
                        if(rc.responseParameters!=null&&rc.responseParameters.length>0){
                            respdata=respdata.concat(rc.responseParameters);
                        }
                        if(rc.responseTreetableRefParameters!=null&&rc.responseTreetableRefParameters.length>0){
                            $.each(rc.responseTreetableRefParameters,function (i, ref) {
                                respdata=respdata.concat(ref.params);
                            })
                        }
                        treetable.render({
                            elem:"#"+responseTableId,
                            data: respdata,
                            field: 'title',
                            treeColIndex: 0,          // treetable新增参数
                            treeSpid: -1,             // treetable新增参数
                            treeIdName: 'd_id',       // treetable新增参数
                            treePidName: 'd_pid',     // treetable新增参数
                            treeDefaultClose: true,   // treetable新增参数
                            treeLinkage: true,        // treetable新增参数
                            cols: [[
                                {
                                    field: 'name',
                                    title: '参数名称',
                                    width: '20%'
                                },
                                {
                                    field: 'description',
                                    title: '说明',
                                    width: '40%'
                                },
                                {
                                    field: 'type',
                                    title: '类型',
                                    width: '20%'
                                },
                                {
                                    field: 'schemaValue',
                                    title: 'schema',
                                    width: '20%'
                                }
                            ]]
                        })
                        $("#"+responseTableId).hide();
                        //默认全部展开
                        treetable.expandAll('#'+responseTableId);
                        //初始化apiInfo响应数据
                        that.log("初始化apiInfo响应数据")
                        that.log(rc)

                        if(rc.responseJson!=null){
                            var sampleId="editorSample"+apiId+"-"+rc.code;
                            var editor = ace.edit(sampleId);
                            /*var JsonMode = ace.require("ace/mode/json").Mode;
                            editor.session.setMode(new JsonMode());*/
                            editor.getSession().setMode("ace/mode/json");
                            editor.setTheme("ace/theme/eclipse");
                            var length_editor = editor.session.getLength();
                            var rows_editor = length_editor * 16;
                            that.log("rows_editor:"+rows_editor);
                            $("#"+sampleId).css('height',rows_editor);
                            editor.resize(true);
                            setTimeout(function(){
                                appendDescriptionVariable($("#"+sampleId),apiInfo.responseCodes[0],that);
                            }, 1000);
                            editor.getSession().on('tokenizerUpdate', function(){
                                setTimeout(function(){
                                    appendDescriptionVariable($("#"+sampleId),apiInfo.responseCodes[0],that);
                                }, 1000);
                            });
                            editor.on('focus', function(){
                                setTimeout(function(){
                                    appendDescriptionVariable($("#"+sampleId),apiInfo.responseCodes[0],that);
                                }, 1000);
                            });
                        }
                    }
                })
            }else{
                //响应参数
                var responseTableId="responseParameter"+apiInfo.id;
                var respdata=[];
                if(apiInfo.responseParameters!=null&&apiInfo.responseParameters.length>0){
                    respdata=respdata.concat(apiInfo.responseParameters);
                }
                if(apiInfo.responseTreetableRefParameters!=null&&apiInfo.responseTreetableRefParameters.length>0){
                    $.each(apiInfo.responseTreetableRefParameters,function (i, ref) {
                        respdata=respdata.concat(ref.params);
                    })
                }
                treetable.render({
                    elem:"#"+responseTableId,
                    data: respdata,
                    field: 'title',
                    treeColIndex: 0,          // treetable新增参数
                    treeSpid: -1,             // treetable新增参数
                    treeIdName: 'd_id',       // treetable新增参数
                    treePidName: 'd_pid',     // treetable新增参数
                    treeDefaultClose: true,   // treetable新增参数
                    treeLinkage: true,        // treetable新增参数
                    cols: [[
                        {
                            field: 'name',
                            title: '参数名称',
                            width: '20%'
                        },
                        {
                            field: 'description',
                            title: '说明',
                            width: '40%'
                        },
                        {
                            field: 'type',
                            title: '类型',
                            width: '20%'
                        },
                        {
                            field: 'schemaValue',
                            title: 'schema',
                            width: '20%'
                        }
                    ]]
                })
                $("#"+responseTableId).hide();
                //默认全部展开
                treetable.expandAll('#'+responseTableId);
                //初始化apiInfo响应数据
                that.log("初始化apiInfo响应数据")
                that.log(apiInfo)
                if(apiInfo.requestValue!=null){
                    var sampleRequestId="editorRequestSample"+apiInfo.id;
                    var editor = ace.edit(sampleRequestId);
                    /*var JsonMode = ace.require("ace/mode/json").Mode;
                    editor.session.setMode(new JsonMode());*/
                    editor.getSession().setMode("ace/mode/json");
                    editor.setTheme("ace/theme/eclipse");
                    var length_editor = editor.session.getLength();
                    var rows_editor = length_editor * 16;
                    that.log("rows_editor:"+rows_editor);
                    $("#"+sampleRequestId).css('height',rows_editor);
                    editor.resize();
                }
                if(apiInfo.responseJson!=null){
                    var sampleId="editorSample"+apiInfo.id;
                    var editor = ace.edit(sampleId);
                    /*var JsonMode = ace.require("ace/mode/json").Mode;
                    editor.session.setMode(new JsonMode());*/
                    editor.getSession().setMode("ace/mode/json");
                    editor.setTheme("ace/theme/eclipse");
                    var length_editor = editor.session.getLength();
                    var rows_editor = length_editor * 16;
                    that.log("rows_editor:"+rows_editor);
                    $("#"+sampleId).css('height',rows_editor);
                    editor.resize(true);
                    setTimeout(function(){
                        appendDescriptionVariable($("#"+sampleId),apiInfo.responseCodes[0],that);
                    }, 1000);
                    editor.getSession().on('tokenizerUpdate', function(){
                        setTimeout(function(){
                            appendDescriptionVariable($("#"+sampleId),apiInfo.responseCodes[0],that);
                        }, 1000);
                    });
                    editor.on('focus', function(){
                        setTimeout(function(){
                            appendDescriptionVariable($("#"+sampleId),apiInfo.responseCodes[0],that);
                        }, 1000);
                    });
                }

            }

            //初始化copy按钮功能
            var clipboard = new ClipboardJS('#copyDocHref'+apiInfo.id,{
                text:function () {
                    return $("#docText"+apiInfo.id).val();
                }
            });
            clipboard.on('success', function(e) {
                layer.msg("复制成功")
            });
            clipboard.on('error', function(e) {
                layer.msg("复制失败,您当前浏览器版本不兼容,请手动复制.")
            });
            that.log(that.currentInstance);
            //创建调试页面
            that.createDebugTab(apiInfo,menu);

            that.initScrollEvent(layerTabId);

        }
    }

    /***
     * 创建调试页面
     * @param apiInfo
     */
    SwaggerBootstrapUi.prototype.createDebugTab=function(apiInfo,menu){
        var that=this;
        //赋值全局参数
        //apiInfo.globalParameters=that.currentInstance.globalParameters;
        //恢复原始show状态
        if(apiInfo.parameters!=null&&apiInfo.parameters.length>0){
            $.each(apiInfo.parameters,function (i, param) {
              param.show=true;
            })
            //判断localStorage对象中是否缓存有参数信息
            var cacheStoreInstance=that.getCacheStoreInstance();
            if (cacheStoreInstance!=null){
                //判断id是否存在
                if($.inArray(apiInfo.id,cacheStoreInstance.ids)>-1){
                    //存在缓存,更新缓存值
                    //遍历获取缓存的parameters
                    var cacheParameters=null;
                    $.each(cacheStoreInstance.stores,function (j, store) {
                        if(store.id==apiInfo.id){
                            cacheParameters=store.data;
                        }
                    })
                    if (cacheParameters!=null){
                        //赋值txtValue
                        $.each(apiInfo.parameters,function (i, param) {
                            //根据参数名称查找cache中的参数值
                            var name=param.name;
                            $.each(cacheParameters,function (j, cache) {
                                if(name==cache.name){
                                    //赋值缓存的值.
                                    param.txtValue=cache.txtValue;
                                }
                            })
                        })
                    }
                }
            }

            //排序
            apiInfo.parameters.sort(function (a, b) {
                return b.require-a.require;
            })
        }


        apiInfo.globalParameters=that.getGlobalParameters();
        var debugContentId="DebugDoc"+apiInfo.id;
        //判断全局参数中和parameter对比，是否存在相同参数，如果存在，判断是否parameters参数有值，如果后端有值,则globalParams中的参数值不显示
        if(apiInfo.globalParameters!=null&&apiInfo.globalParameters.length>0){
            $.each(apiInfo.globalParameters,function (i, global) {
                if(apiInfo.parameters!=null&&apiInfo.parameters.length>0){
                    $.each(apiInfo.parameters,function (i, param) {
                        if(global.name==param.name){
                            //判断txtValue是否有值
                            if(param.txtValue!=undefined&&param.txtValue!=null&&param.txtValue!=""){
                                global.show=false;
                            }else{
                                //反之，param不显示
                                param.show=false;
                            }
                        }
                    })
                }
            })
        }



        var html = template('DebugScript', apiInfo);
        $("#"+debugContentId).html("").html(html)
        //string类型的arr参数动态添加事件
        $(".btn-add-string"+apiInfo.id).on("click",function (e) {
            e.preventDefault();
            var btn=$(this);
            that.log(btn);
            var parentTd=btn.parent();
            var parentDiv=btn.parent().find(".btn-add-div");
            var firstInput=parentTd.find("input:first");
            var divgroup=$('<div class="input-group" style="    margin-top: 5px;"></div>');
            var cloneEle=firstInput.clone(true);
            cloneEle.val("");
            cloneEle.appendTo(divgroup);
            var spanBtn=$('<span class="input-group-btn"></span>')
            var delBtn=$('<button class="btn btn-danger btn-circle btn-small btn-param-delete" type="button">-</button>')
            spanBtn.append(delBtn);
            divgroup.append(spanBtn);
            parentDiv.append(divgroup)
            delBtn.on("click",function (e) {
                e.preventDefault();
                $(this).parent().parent().remove();
            })
        })
        //绑定全选事件
        $("#parameterCheckAll"+apiInfo.id).on("click",function (e) {
            var chk=$(this);
            that.log("是否选中...")
            var chked=chk.find("input:first").prop("checked");
            that.log(chked)
            var paramBodyId="paramBody"+apiInfo.id;
            $("#"+paramBodyId).find("input:checkbox").prop("checked",chked);
        });
        that.requestSend(apiInfo,menu);
        //绑定contentType下拉框选择事件
        that.getDoc().find("#"+debugContentId).find("#contentTypeRequest"+apiInfo.id).find(".option").click(function(){
            var txt=$(this).html();
            var value=$(this).data("value")
            var showId="dropdownMenu"+apiInfo.id;
            $("#"+showId).html(txt+" <span class='caret'></span>");
            $("#"+showId).data("value",value);
            $("#DebugContentType"+apiInfo.id).val(value)
        })
        //check选择事件
        $("input[name=optionsRadiosinline"+apiInfo.id+"]").click(function(){
          var t=$(this);
          that.log("check--click...")
          if(t.val()=="raw"){
              $("#raw"+apiInfo.id).show();
              //判断data是否缓存的值
              var showId="dropdownMenu"+apiInfo.id;
              var cachedata=$("#"+showId).data("value");
              if(cachedata!=null&&cachedata!=undefined&&cachedata!=""){
                  $("#DebugContentType"+apiInfo.id).val(cachedata);
              }else{
                  $("#DebugContentType"+apiInfo.id).val("application/json");
              }
          }else{
              $("#raw"+apiInfo.id).hide();
              $("#DebugContentType"+apiInfo.id).val(t.val());
          }
        })

    }



    /***
     * 发送请求
     * @constructor
     */
    SwaggerBootstrapUi.prototype.requestSend=function (apiInfo,eleObject) {
        var that=this;
        that.log("发送之前...")
        that.log(apiInfo)
        var apiKeyId=apiInfo.id;
        var btnRequest=$("#btnRequest"+apiInfo.id);
        var respcleanDiv=$("#responsebody"+apiInfo.id);
        var laycontentdiv=$("#layuiresponsecontentmain"+apiKeyId);
        var responsestatus=$("#responsestatus"+apiKeyId);

        var resp1=$("#respcontent"+apiKeyId)
        var resp2=$("#respraw"+apiKeyId);
        var resp3=$("#respheaders"+apiKeyId);
        var resp5=$("#respcurl"+apiKeyId);

        var responseHeight=400;

        btnRequest.on("click",function (e) {
            e.preventDefault();
            var tabsContentHeight=$("#tabsContent"+apiKeyId).height();
            that.log($("#tabsContent"+apiKeyId))
            var basicContentHeight=$("#DebugScriptBasic"+apiKeyId).height();
            that.log($("#DebugScriptBasic"+apiKeyId))
            //计算basic和tabs的占比
            var perc=parseInt((basicContentHeight/tabsContentHeight)*100);
            that.log("tabs高度:"+tabsContentHeight+",basic高度："+basicContentHeight+",占比："+perc)
            var laydivHeight=tabsContentHeight-basicContentHeight-5;
            responseHeight=laydivHeight-40;
            if(perc>65){
                responseHeight=500;
                laydivHeight=550;
            }
            that.log("整个tab高度："+tabsContentHeight+",请求Form表单高度："+basicContentHeight+",高度差："+responseHeight);
            //laycontentdiv.css("height",laydivHeight+"px");
            //respcleanDiv.html("")
            var params={};
            var headerparams={};
            var bodyparams="";
            //modify by xiaoyumin 2017-8-9 11:28:16
            //增加表单验证
            var validateflag=false;
            var validateobj={};
            //获取参数
            var paramBody=$("#paramBody"+apiInfo.id);
            that.log("paramsbody..")
            that.log(paramBody)
            //获取url
            var url=$("#txtreqUrl"+apiInfo.id).val();
            if(url==null||url==""){
                layer.msg("请求url地址不能为空");
                return false;
            }
            var bodyRequest=false;
            //构建一个formdata对象,发送表单
            var formData=new FormData();
            var formCurlParams={};
            var fileUploadFlat=false;

            var queryStringParameterFlag=false;
            var queryStringParameterArr=new Array();

            paramBody.find("tr").each(function () {
                var paramtr=$(this);
                var cked=paramtr.find("td:first").find(":checked").prop("checked");
                that.log(cked)
                if (cked){
                    //如果选中
                    var trdata={name:paramtr.find("td:eq(2)").find("input").val(),in:paramtr.data("in"),required:paramtr.data("required"),type:paramtr.data("type"),emflag:paramtr.data("emflag"),schemavalue:paramtr.data("schemavalue")};
                    that.log("trdata....")
                    that.log(trdata);
                    //获取key
                    //var key=paramtr.find("td:eq(1)").find("input").val();
                    var key=trdata["name"];
                    //获取value
                    var value="";
                    var reqflag=false;
                    if(trdata["in"]=="body") {
                        //这里需要判断schema
                        //直接判断那类型
                        if (trdata.schemavalue == "MultipartFile") {
                            value = paramtr.find("td:eq(3)").find("input").val();
                            var fileEle = paramtr.find("td:eq(3)").find("input")[0];
                            fileUploadFlat = true;
                            that.log("files------------------------------")
                            var files = fileEle.files;
                            that.log(files);
                            if(files.length>1){
                                //多个
                                for( var i = 0; i < files.length; i++ ){
                                    var file = files[i];
                                    var formKey=key+"["+i+"]";
                                    that.log("formKey------------------------------")
                                    that.log(formKey);
                                    formData.append(key, file);
                                }
                            }else if(files.length==1){
                                formData.append(key, files[0]);
                            }
                            formCurlParams[key]=value;
                        } else {
                            value = paramtr.find("td:eq(3)").find("textarea").val();
                            formData.append(key, value);
                        }
                        //如果不开启缓存,则不保留
                        if(that.settings.enableRequestCache){
                            that.updateRequestParameter(trdata.name, value, apiInfo);
                        }
                    }
                    else if(trdata["in"]=="formData"){
                        //直接判断那类型
                        if (trdata.schemavalue == "MultipartFile") {
                            value = paramtr.find("td:eq(3)").find("input").val();
                            var fileEle = paramtr.find("td:eq(3)").find("input")[0];
                            fileUploadFlat = true;
                            that.log("files-form-data------------------------------");
                            that.log(fileEle);
                            var files = fileEle.files;
                            that.log(files);
                            if(files.length>1){
                                //多个
                                for( var i = 0; i < files.length; i++ ){
                                    var file = files[i];
                                    var formKey=key+"["+i+"]";
                                    that.log("formKey------------------------------")
                                    that.log(formKey);
                                    formData.append(key, file);
                                }
                            }else if(files.length==1){
                                formData.append(key, files[0]);
                            }
                            formCurlParams[key]=value;
                        } else {
                            var formEle=paramtr.find("td:eq(3)").find("textarea");
                            if (formEle.length>0){
                                value = formEle.val();
                            }else{
                                value=paramtr.find("td:eq(3)").find("input").val();
                            }
                            formData.append(key, value);
                            formCurlParams[key]=value;
                        }
                        if(that.settings.enableRequestCache){
                            that.updateRequestParameter(trdata.name, value, apiInfo);
                        }
                    }else{
                        if(trdata.emflag){
                            value=paramtr.find("td:eq(3)").find("select option:selected").val();
                            if(that.settings.enableRequestCache){
                                that.updateRequestParameter(trdata.name,value,apiInfo);
                            }
                            formData.append(key,value);
                        }else{
                            if(trdata["type"]=="array"){
                                queryStringParameterFlag=true;
                                reqflag=true;
                                //数组类型
                                paramtr.find("td:eq(3)").find("input").each(function (i, x) {
                                    queryStringParameterArr.push(key+"="+$(this).val())
                                    //value=$(this).val();
                                })
                            }else{
                                value=paramtr.find("td:eq(3)").find("input").val();
                                if(that.settings.enableRequestCache){
                                    that.updateRequestParameter(trdata.name,value,apiInfo);
                                }
                                formData.append(key,value);
                                //queryStringParameterArr.push(key+"="+value)
                            }
                        }
                    }

                    if(apiInfo.methodType.toLowerCase()=="delete"){
                        //判断是否是path参数
                        if(trdata["in"]=="path"){
                            url=url.replace("{"+key+"}",value);
                            apiInfo.url=url;
                        }else{
                            //判断是否是header
                            if(trdata["in"]=="header") {
                                headerparams[key] = value;
                            }else if(trdata["in"]=="body"){
                                bodyparams+=value;
                                bodyRequest=true;
                            }else{
                                if (url.indexOf("?")>-1){
                                    url=url+"&"+key+"="+value;
                                }else{
                                    url+="?"+key+"="+value;
                                }
                            }
                        }
                    }else{
                        if(trdata["in"]=="path"){
                            url=url.replace("{"+key+"}",value);
                            apiInfo.url=url;
                        }else{
                            if(trdata["in"]=="body"){
                                bodyparams+=value;
                                bodyRequest=true;
                            }else{
                                if(trdata["in"]=="header"){
                                    headerparams[key]=value;
                                }else{
                                    if (trdata.schemavalue != "MultipartFile") {
                                        //判断数组
                                        if(trdata["type"]!="array"){
                                            params[key]=value;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //判断是否required
                    if (trdata.hasOwnProperty("required")){
                        var required=trdata["required"];
                        if (required){
                            if(!reqflag){
                                //必须,验证value是否为空
                                if(value==null||value==""){
                                    validateflag=true;
                                    var des=trdata["name"]
                                    validateobj={message:des+"不能为空"};
                                    return false;
                                }
                            }
                        }

                    }
                    that.log("key:"+key+",value:"+value);
                }
            })
            that.log("获取参数..")
            that.log(params);
            that.log(apiInfo)
            that.log("请求url："+url);
            var reqdata=null;
            var contType="application/json;charset=UTF-8";
            var paramBodyType="json";
            var formRequest=false;
            if(bodyRequest){
                //非文件请求
                if (!fileUploadFlat){
                    reqdata=bodyparams;
                    //body请求,url追加其他param参数
                    var requestArr=new Array();
                    for(var p in params){
                        requestArr.push(p+"="+params[p]);
                    }
                    if(requestArr.length>0){
                        var reqStrArr=requestArr.join("&");
                        that.log("body请求，尚有其他form表单参数................")
                        that.log(requestArr)
                        that.log(reqStrArr)
                        if (url.indexOf("?")>-1){
                            url=url+"&"+reqStrArr;
                        }else{
                            url=url+"?"+reqStrArr;
                        }
                    }
                    if(apiInfo.consumes!=null&&apiInfo.consumes.length>0) {
                        var ctp = apiInfo.consumes[0];
                        if (ctp == "text/plain") {
                            contType="text/plain";
                        }
                    }
                }else{
                    //body类型的file文件请求
                    reqdata=params;
                }
            }
            else{
                if(fileUploadFlat){
                    contType="multipart/form-data";
                    paramBodyType="form-data";
                    reqdata=params;
                }else{
                    paramBodyType="form";
                    contType="application/x-www-form-urlencoded;charset=UTF-8";
                    reqdata=params;
                    //判断consumes请求类型
                    if(apiInfo.consumes!=null&&apiInfo.consumes.length>0){
                        var ctp=apiInfo.consumes[0];
                        if(ctp=="multipart/form-data"){
                            contType=ctp;
                            paramBodyType="form-data";
                            reqdata=formData;
                            formRequest=true;
                        }
                    }
                }
                //判断query
                if(queryStringParameterFlag){
                    if(queryStringParameterArr.length>0){
                        var reqStrArr=queryStringParameterArr.join("&");
                        if (url.indexOf("?")>-1){
                            url=url+"&"+reqStrArr;
                        }else{
                            url=url+"?"+reqStrArr;
                        }
                    }
                }
            }
            //console.log(reqdata)
            if(validateflag){
                layer.msg(validateobj.message);
                return;
            }
            that.log("发送之后bai...")
            that.log(apiInfo)
            eleObject.data("data",apiInfo);
            //判断是否有表单
            var form=$("#uploadForm"+apiInfo.id);
            var startTime=new Date().getTime();
            var index = layer.load(1);
            that.log("headerParams------------")
            that.log(headerparams)
            //增加header默认发送参数
            headerparams["Request-Origion"]=that.requestOrigion;
            //判断produce
            if(apiInfo.produces!=undefined&&apiInfo.produces!=null&&apiInfo.produces.length>0){
                var first=apiInfo.produces[0];
                headerparams["accept"]=first;
            }
            //判断security参数
            if(that.currentInstance.securityArrs!=null&&that.currentInstance.securityArrs.length>0){
                $.each(that.currentInstance.securityArrs,function (i, sa) {
                    if(sa.in=="header"){
                        headerparams[sa.name]=sa.value;
                    }
                })
            }
            //判断是否全局参数中包含ContentType属性
            if(!headerparams.hasOwnProperty("Content-Type")){
                //如果全局参数中不包含,则获取默认input选择框的
                var _tmp=$("#DebugContentType"+apiKeyId).val();
                headerparams["Content-Type"]=_tmp;
            }
            if(form.length>0||formRequest){
                that.log("form submit------------------------------------------------")
                axios.request({
                    url:url,
                    headers:headerparams,
                    method:$.getStringValue(apiInfo.methodType),
                    data:formData,
                    timeout: 10*60*1000,
                }).then(function (response) {
                    var data=response.data;
                    var xhr=response.request;
                    var allheaders=response.headers;
                    that.createResponseElement(index,apiInfo,headerparams,reqdata,paramBodyType,url,fileUploadFlat,
                        formCurlParams,xhr,data,startTime,allheaders,true);
                }).catch(function (error) {
                    that.log("form request--response error-------------------")
                    respcleanDiv.show();
                    layer.close(index);
                    if(error.response){
                        var response=error.response;
                        var data=response.data;
                        var xhr=response.request;
                        var allheaders=response.headers;
                        that.createResponseElement(index,apiInfo,headerparams,reqdata,paramBodyType,url,fileUploadFlat,
                            formCurlParams,xhr,data,startTime,allheaders,true);
                    }else{
                        if (error!=null){
                            var estr=error.toString();
                            if(estr=="Error: Network Error"){
                                layer.msg("服务器正在重启或者已经挂了:(~~~~")
                            }
                        }
                    }
                })
            }
            else{
                //headerparams["Content-Type"]=contType;
                $.ajax({
                    url:url,
                    headers:headerparams,
                    type:$.getStringValue(apiInfo.methodType),
                    data:reqdata,
                    contentType:contType,
                    success:function (data,status,xhr) {
                        var allheaders=xhr.getAllResponseHeaders();
                        that.createResponseElement(index,apiInfo,headerparams,reqdata,paramBodyType,url,fileUploadFlat,
                            formCurlParams,xhr,data,startTime,allheaders,false);
                    },
                    error:function (xhr, textStatus, errorThrown) {
                        that.log("ajax request--response error-------------------")
                        if(textStatus=="error"&&xhr.status==0){
                            layer.msg("服务器正在重启或者已经挂了:(~~~~")
                            //关闭遮罩层
                            layer.close(index);
                        }else{
                            var allheaders=xhr.getAllResponseHeaders();
                            var data=null;
                            that.createResponseElement(index,apiInfo,headerparams,reqdata,paramBodyType,url,fileUploadFlat,
                                formCurlParams,xhr,data,startTime,allheaders,false);
                        }
                    }
                })
            }
            if(that.settings.enableRequestCache){
                //缓存到localStorage对象中
                that.cacheRequestParameters(apiInfo);
            }
        })

        //path替换url-功能
        that.getDoc().find("#tab2").find(".p-path").on("keyup",function (e) {
            var t=$(this);
            var name=t.data("name");
            var apiUrl=$("#txtreqUrl").data("originalurl");
            var realValue=apiUrl.replace("{"+name+"}",t.val());
            //查找是否还存在其他path参数
            $("#paramBody").find("tr").each(function (i, itr) {
                var itrthat=$(this);
                var itrdata={name:itrthat.data("name"),in:itrthat.data("in"),required:itrthat.data("required"),type:itrthat.data("type")};
                //var itrdata=itrthat.data("data");
                var itrname=itrdata["name"];
                if(itrdata["in"]=="path"&&itrdata["name"]!=name){
                    //查找value值
                    var itrtdvalue=itrthat.find(".p-value").val();
                    if(itrtdvalue!=""){
                        realValue=realValue.replace("{"+itrname+"}",itrtdvalue);
                    }
                }
            })
            that.log(realValue);
            $("#txtreqUrl").val(realValue);
            that.log("keyup。。。。")

        })

    }

    /***
     * 更新apiInfo的请求参数缓存策略
     * @param apiInfo
     */
    SwaggerBootstrapUi.prototype.cacheRequestParameters=function (apiInfo) {
        //判断是否支持localStore对象
        if(window.localStorage){
            var store = window.localStorage;
            var key="SwaggerBootstrapUiStore";
            var storeCacheInstanceStr=store[key];
            if(storeCacheInstanceStr!=undefined&&storeCacheInstanceStr!=null&&storeCacheInstanceStr!=""){
                //store中存在
                var storeCacheInstance=JSON.parse(storeCacheInstanceStr);
                //判断是否存在
                if($.inArray(apiInfo.id,storeCacheInstance.ids)==-1){
                    //不存在,更新
                    storeCacheInstance.ids.push(apiInfo.id);
                    storeCacheInstance.stores.push(new SwaggerBootstrapUiRequestStore(apiInfo.id,apiInfo.parameters));
                    store.setItem(key,JSON.stringify(storeCacheInstance));
                }else{
                    //store中存在,需更新parameter
                    var newArr=new Array();
                    $.each(storeCacheInstance.stores,function (i, sui) {
                        if(sui.id!=apiInfo.id){
                            newArr.push(sui);
                        }
                    })
                    newArr.push(new SwaggerBootstrapUiRequestStore(apiInfo.id,apiInfo.parameters));
                    storeCacheInstance.stores=newArr;
                    store.setItem(key,JSON.stringify(storeCacheInstance));
                }
            }else{
                var storeInstance=new SwaggerBootstrapUiStore();
                //store为空
                storeInstance.ids.push(apiInfo.id);
                storeInstance.stores.push(new SwaggerBootstrapUiRequestStore(apiInfo.id,apiInfo.parameters));
                store.setItem(key,JSON.stringify(storeInstance));
            }
        }

    }

    /***
     * 当设置不启用缓存策略时,移除缓存常量值
     */
    SwaggerBootstrapUi.prototype.disableStoreRequestParams=function () {
        if(window.localStorage) {
            var store = window.localStorage;
            var key = "SwaggerBootstrapUiStore";
            store.setItem(key,"");
        }
    }

    /***
     * 获取缓存在localStorage对象中的请求参数对象
     */
    SwaggerBootstrapUi.prototype.getCacheStoreInstance=function () {
        var storeInstance=null;
        if(window.localStorage){
            var store = window.localStorage;
            var key="SwaggerBootstrapUiStore";
            var storeCacheInstanceStr=store[key];
            if(storeCacheInstanceStr!=undefined&&storeCacheInstanceStr!=null&&storeCacheInstanceStr!="") {
                //store中存在
                storeInstance = JSON.parse(storeCacheInstanceStr);
            }
        }
        return storeInstance;
    }

    /****
     * 发送请求后,创建响应元素
     * @param index
     * @param apiInfo
     * @param headerparams
     * @param reqdata
     * @param paramBodyType
     * @param url
     * @param fileUploadFlat
     * @param formCurlParams
     * @param xhr
     * @param data
     * @param startTime
     * @param allheaders
     * @param formRequest
     */
    SwaggerBootstrapUi.prototype.createResponseElement=function (index,apiInfo,headerparams,reqdata,paramBodyType,url,fileUploadFlat
        ,formCurlParams,xhr,data,startTime,allheaders,formRequest) {
        var that=this;
        var apiKeyId=apiInfo.id;
        var respcleanDiv=$("#responsebody"+apiKeyId);
        var laycontentdiv=$("#layuiresponsecontentmain"+apiKeyId);
        var responsestatus=$("#responsestatus"+apiKeyId);
        var resp1=$("#respcontent"+apiKeyId)
        var resp2=$("#respraw"+apiKeyId);
        var resp3=$("#respheaders"+apiKeyId);
        var resp5=$("#respcurl"+apiKeyId);
        var responseHeight=500;
        //关闭遮罩层
        layer.close(index);
        //清空响应内容div
        respcleanDiv.show();
        //响应码
        var statsCode=xhr.status;
        if(statsCode==200){
            statsCode=statsCode+" OK";
        }
        //计算耗时
        var endTime=new Date().getTime();
        var len=0;
        var diff=endTime-startTime;
        //计算返回数据大小
        var tp=null;
        if (data!=null){
            tp=typeof (data);
        }
        //var tp=typeof (data);
        if(xhr.hasOwnProperty("responseText")){
            len=xhr["responseText"].gblen();
        }
        //清空响应状态栏,赋值响应栏
        responsestatus.html("")
        responsestatus.append($("<span class='debug-span-label'>响应码:</span><span class='debug-span-value'>"+statsCode+"</span>"))
            .append($("<span class='debug-span-label'>耗时:</span><span class='debug-span-value'>"+diff+" ms</span>"))
            .append($("<span class='debug-span-label'>大小:</span><span class='debug-span-value'>"+len+" b</span>"))
            .append($("<span class='debug-span-label' style='margin-left:10px;' id='bigScreen"+apiInfo.id+"'><i class=\"icon-text-width iconfont icon-quanping\" style='cursor: pointer;'></i></span>"));
        //赋值响应headers
        //var mimtype=xhr.overrideMimeType();
        //var allheaders=xhr.getAllResponseHeaders();
        if(allheaders!=null&&typeof (allheaders)!='undefined'&&allheaders!=""){
            var headertable=$('<table class="table table-hover table-bordered table-text-center"><tr><th>请求头</th><th>value</th></tr></table>');
            //如果headers是string，ajax提交
            if(typeof (allheaders)=="string"){
                var headers=allheaders.split("\r\n");
                for(var i=0;i<headers.length;i++){
                    var header=headers[i];
                    if(header!=null&&header!=""){
                        var headerValu=header.split(":");
                        var headertr=$('<tr><th class="active">'+headerValu[0]+'</th><td>'+headerValu[1]+'</td></tr>');
                        headertable.append(headertr);
                    }
                }
            }else{
                for(var hk in allheaders){
                    var headertr=$('<tr><th class="active">'+hk+'</th><td>'+allheaders[hk]+'</td></tr>');
                    headertable.append(headertr);
                }
            }
            //设置Headers内容
            resp3.html("")
            resp3.append(headertable);
        }

        //判断响应内容
        var contentType=xhr.getResponseHeader("Content-Type");
        var rtext=xhr["responseText"];
        that.log(xhr.hasOwnProperty("responseText"));
        that.log(rtext);
        //响应文本内容
        if(rtext!=null&&rtext!=undefined){
            var rawCopyBotton=$("<button class='btn btn-default btn-primary iconfont icon-fuzhi' id='btnCopyRaw"+apiKeyId+"'>复制</button><br /><br />");
            var rawText=$("<span></span>");
            rawText.html(xhr["responseText"]);
            resp2.html("");
            resp2.append(rawCopyBotton).append(rawText);
            var cliprawboard = new ClipboardJS('#btnCopyRaw'+apiKeyId,{
                text:function () {
                    return rawText.html();
                }
            });
            cliprawboard.on('success', function(e) {
                layer.msg("复制成功")
            });
            cliprawboard.on('error', function(e) {
                layer.msg("复制失败,您当前浏览器版本不兼容,请手动复制.")
            });
            if(tp!=null&& tp=="string"){
                //转二进制
                var dv=data.toString(2);
                if(dv!=undefined&&dv!=null){
                    that.log("二进制11..");
                    var div=$("<div></div>");
                    var rowDiv=$("<div style='word-wrap: break-word;'>"+dv+"</div>");
                    var downloadDiv=$("<div style='    position: absolute;\n" +
                        "    right: 0px;\n" +
                        "    width: 100px;\n" +
                        "    bottom: 30px;\n" +
                        "    text-align: center;'></div>")
                    var button=$("<button style='width: 100px;' class=\"btn btn-default btn-primary\"> 下 载 </button>");
                    button.bind("click",function () {
                        window.open(url);
                    })
                    downloadDiv.append(button);
                    div.append(rowDiv).append(downloadDiv);
                    that.log(div)
                    that.log(div[0])
                    resp1.html("")
                    resp1.html(div);
                }
            }
        }
        //响应JSON
        if (xhr.hasOwnProperty("responseJSON")||data!=null||data!=undefined){
            //如果存在该对象,服务端返回为json格式
            resp1.html("")
            that.log(xhr["responseJSON"])
            var jsondiv=$('<div style="width: auto;height: '+responseHeight+'px;" id="responseJsonEditor'+apiKeyId+'"></div>')
            if(xhr.hasOwnProperty("responseJSON")){
                jsondiv.html(JSON.stringify(xhr["responseJSON"],null,2));
            }else{
                //针对表单提交,error的情况,会产生data
                jsondiv.html(JSON.stringify(data,null,2));
            }
            resp1.append(jsondiv);
            var editor = ace.edit("responseJsonEditor"+apiKeyId);
            editor.getSession().setMode("ace/mode/json");
            editor.setTheme("ace/theme/eclipse");
            //重构高度
            var length_editor = editor.session.getLength();
            var rows_editor = length_editor * 16;
            that.log("重构高度："+rows_editor)
            $("#responseJsonEditor"+apiKeyId).css('height',rows_editor+110);
            editor.resize(true);
            that.log($("#responseJsonEditor"+apiKeyId).height())
            //重置响应面板高度
            laycontentdiv.css("height",rows_editor+150);
            setTimeout(function(){
                appendDescriptionVariable($("#responseJsonEditor"+apiKeyId),apiInfo.responseCodes[0],that);
            }, 1000);
            editor.getSession().on('tokenizerUpdate', function(){
                setTimeout(function(){
                    appendDescriptionVariable($("#responseJsonEditor"+apiKeyId),apiInfo.responseCodes[0],that);
                }, 1000);
            });
            editor.on('focus', function(){
                setTimeout(function(){
                    appendDescriptionVariable($("#responseJsonEditor"+apiKeyId),apiInfo.responseCodes[0],that);
                }, 1000);
            });
        }else{
            //判断是否是text
            var regex=new RegExp('.*?text.*','g');
            if(regex.test(contentType)){
                resp1.html("")
                resp1.html(xhr.responseText);
            }
        }
        //构建CURL功能
        //组件curl功能
        var curl=null;
        if (formRequest){
           curl=that.buildCurl(apiInfo,headerparams,reqdata,paramBodyType,url,formCurlParams,fileUploadFlat);
        }else{
            curl=that.buildCurl(apiInfo,headerparams,reqdata,paramBodyType,url,fileUploadFlat);
        }
        var cpcurlBotton=$("<button class='btn btn-default btn-primary iconfont icon-fuzhi' id='btnCopyCurl"+apiKeyId+"'>复制</button><br /><br />");
        var curlcode=$("<code></code>");
        curlcode.html(curl);

        resp5.html("");
        resp5.append(cpcurlBotton).append(curlcode);
        var clipboard = new ClipboardJS('#btnCopyCurl'+apiKeyId,{
            text:function () {
                return curlcode.html();
            }
        });
        clipboard.on('success', function(e) {
            layer.msg("复制成功")
        });
        clipboard.on('error', function(e) {
            layer.msg("复制失败,您当前浏览器版本不兼容,请手动复制.")
        });

        //全屏icon点击事件
        that.getDoc().find("#bigScreen"+apiInfo.id).click(function (e) {
            e.preventDefault();
            //layer.msg(apiInfo.summary+"--------全屏点击事件")
            var showDiv="#respcontent"+apiInfo.id;
            that.log($(showDiv))
            that.log($(showDiv).html())
            var element=$(showDiv)[0];
            that.fullScreen(element);
        })

    }

    /***
     * 构建curl
     */
    SwaggerBootstrapUi.prototype.buildCurl=function (apiInfo,headers,reqdata,paramBodyType,url,formCurlParams,fireRequest) {
        var that=this;
        var curlified=new Array();
        var protocol="http";
        //获取location
        var href=window.location.href;
        that.log("href:"+href);
        //判断是否是https
        var proRegex=new RegExp("^https.*","ig");
        if (proRegex.test(href)){
            protocol="https";
        }
        that.log("protocol:"+protocol)
        var fullurl=protocol+"://"+that.currentInstance.host;
        //判断url是否是以/开头
        if(!apiInfo.url.startWith("/")){
            fullurl+="/";
        }
        fullurl+=url;
        curlified.push( "curl" );
        curlified.push( "-X", apiInfo.methodType.toUpperCase() );
        //此处url需要encoding
        curlified.push( "\""+encodeURI(fullurl)+"\"");
        that.log("curl-------------------header");
        that.log(headers);
        if(paramBodyType=="json"){
            if(apiInfo.consumes!=undefined&&apiInfo.consumes!=null&&apiInfo.consumes.length>0){
                var first=apiInfo.consumes[0];
                headers["Content-Type"]=first;
            }
        }
        if(headers!=undefined&&headers!=null&&headers!=""){
            for(var h in headers){
                curlified.push( "-H " )
                curlified.push("\""+h+":"+headers[h]+"\"");
            }
        }
        that.log("curl-------------------reqdata");
        that.log(paramBodyType)
        that.log(reqdata)
        var tp=typeof (reqdata);
        if(paramBodyType=="json"){
            if(reqdata!=null){
                //如果是filre请求
                if (fireRequest!=undefined&&fireRequest){
                    if(formCurlParams!=null&&formCurlParams!=undefined) {
                        for (var d in formCurlParams) {
                            curlified.push("-F");
                            curlified.push("\"" + d + "=" + formCurlParams[d] + "\"");
                        }
                    }
                    for (var d in reqdata) {
                        curlified.push("-F");
                        curlified.push("\"" + d + "=" + reqdata[d] + "\"");
                    }
                }else{
                    if(tp=="string"){
                        //如果装换JSON失败,以字符串拼接
                        try{
                            var jobj=JSON.parse(reqdata);
                            var objstr=JSON.stringify( jobj ).replace(/\\n/g, "").replace(/"/g,"\\\"");
                            that.log(objstr);
                            curlified.push( "-d" );
                            curlified.push( "\""+objstr +"\"")
                        }catch (error){
                            curlified.push( "-d" );
                            curlified.push( "\""+reqdata +"\"")
                        }

                    }else if(tp=="object"){
                        //req有可能为空
                        //object
                        var objstr=JSON.stringify( reqdata ).replace(/\\n/g, "").replace(/"/g,"\\\"");
                        that.log(objstr);
                        curlified.push( "-d" );
                        curlified.push( "\""+objstr +"\"")
                    }
                }
            }else{
                if(formCurlParams!=null&&formCurlParams!=undefined) {
                    var formArr = new Array();
                    for (var d in formCurlParams) {
                        curlified.push("-F");
                        curlified.push("\"" + d + "=" + formCurlParams[d] + "\"");
                    }
                }
            }

        }else{
            //判断是否是文件上传
            if(formCurlParams!=null&&formCurlParams!=undefined&&formCurlParams){
                for(var d in formCurlParams){
                    curlified.push( "-F" );
                    curlified.push( "\""+d+"="+formCurlParams[d] +"\"");
                }
                if(reqdata!=undefined&&reqdata!=null){
                    for (var d in reqdata) {
                        curlified.push("-F");
                        curlified.push("\"" + d + "=" + reqdata[d] + "\"");
                    }
                }
            }else{
                //form
                for(var d in reqdata){
                    curlified.push( "-d" );
                    curlified.push("\"" + d + "=" + reqdata[d] + "\"");
                    //formArr.push(d+"="+reqdata[d]);
                }
                /*var formStr=formArr.join("&");
                that.log("表单...");
                that.log(formStr);
                that.log(formStr.toString());
                if(formArr.length>0){
                    curlified.push( "-d" );
                    curlified.push( "\""+formStr +"\"");
                }*/
            }
        }
        return curlified.join(" ");
    }


    /***
     * 全屏展示某div元素
     * @param element
     */
    SwaggerBootstrapUi.prototype.fullScreen=function (element) {
        var requestMethod =element.requestFullScreen
            ||element.webkitRequestFullScreen //谷歌
            ||element.mozRequestFullScreen  //火狐
            ||element.msRequestFullScreen; //IE11
        if (requestMethod) {
            requestMethod.call(element);
        } else if (typeof window.ActiveXObject !== "undefined") {
            //window.ActiveXObject判断是否支持ActiveX控件
            //模拟按下键盘F11,使浏览器全屏
            //创建ActiveX
            var wscript = new ActiveXObject("WScript.Shell");
            //创建成功
            if (wscript !== null) {
                //触发f11
                wscript.SendKeys("{F11}");
            }
        }
    }

    /***
     * 更新key值
     * @param key
     * @param value
     * @param apiInfo
     */
    SwaggerBootstrapUi.prototype.updateRequestParameter=function (key, value, apiInfo) {
        $.each(apiInfo.parameters,function (i, p) {
            if(p.name==key){
                p.txtValue=value;
            }
        })
    }

    SwaggerBootstrapUi.prototype.markdownDocInit=function (docTextId,contentId) {
        var that=this;
        //md2Html的配置
        hljs.configure({useBR: false});
        hljs.initHighlightingOnLoad();
        marked.setOptions({
            renderer: new marked.Renderer(),
            gfm: true,
            emoji: true,
            tables: true,
            breaks: false,
            pedantic: false,
            sanitize: true,
            smartLists: true,
            smartypants: false,
            highlight: function (code, lang) {
                try {
                    if (lang)
                        return hljs.highlight(lang, code).value;
                } catch (e) {
                    return hljs.highlightAuto(code).value;
                }
                return hljs.highlightAuto(code).value;
            }
        });
        $("#"+docTextId).each(function(){
            var md = $(this).val();
            if(md){
                $("#"+contentId).html(marked(md));
                $('pre code').each(function(i, block) {
                    hljs.highlightBlock(block);
                });
            }
        });
        $("code").css("background-color", "transparent");
    }

    SwaggerBootstrapUi.prototype.createTabElement=function () {
        var that=this;
        var divcontent=$('<div id="myTab" class="tabs-container" style="width:99%;margin:0px auto;"></div>');
        var ul=$('<ul class="nav nav-tabs"></ul>')
        ul.append($('<li><a data-toggle="tab" href="#tab1" aria-expanded="false"> 接口说明</a></li>'));
        ul.append($('<li class=""><a data-toggle="tab" href="#tab2" aria-expanded="true"> 在线调试</a></li>'));
        divcontent.append(ul);
        var tabcontent=$('<div class="tab-content"></div>');

        tabcontent.append($('<div id="tab1" class="tab-pane"><div class="panel-body"><strong>接口详细说明</strong><p>Bootstrap 使用到的某些 HTML 元素和 CSS 属性需要将页面设置为 HTML5 文档类型。在你项目中的每个页面都要参照下面的格式进行设置。</p></div></div>'));
        tabcontent.append($('<div id="tab2" class="tab-pane"><div class="panel-body"><strong>正在开发中,敬请期待......</strong></div></div>'));
        divcontent.append(tabcontent);
        //内容覆盖
        that.getDoc().html("");
        that.getDoc().append(divcontent);
        that.log("动态激活...")
        //liapi.addClass("active");
        that.log("动态激活12...")
        that.getDoc().find("#myTab a:first").tab('show')

    }
    /***
     * 创建markdown文本框
     * @param apiInfo
     */
    SwaggerBootstrapUi.prototype.createMarkdownTxt=function (apiInfo) {

    }

    SwaggerBootstrapUi.prototype.creatabTab=function () {
        var that=this;
        var divcontent=$('<div id="myTab" class="tabs-container" style="width:99%;margin:0px auto;"></div>');
        var ul=$('<ul class="nav nav-tabs"></ul>')
        ul.append($('<li><a data-toggle="tab" href="#tab1" aria-expanded="false"> 接口说明</a></li>'));
        ul.append($('<li class=""><a data-toggle="tab" href="#tab2" aria-expanded="true"> 在线调试</a></li>'));
        divcontent.append(ul);
        var tabcontent=$('<div class="tab-content"></div>');

        tabcontent.append($('<div id="tab1" class="tab-pane"><div class="panel-body"><strong>接口详细说明</strong><p>Bootstrap 使用到的某些 HTML 元素和 CSS 属性需要将页面设置为 HTML5 文档类型。在你项目中的每个页面都要参照下面的格式进行设置。</p></div></div>'));
        tabcontent.append($('<div id="tab2" class="tab-pane"><div class="panel-body"><strong>正在开发中,敬请期待......</strong></div></div>'));
        divcontent.append(tabcontent);
        //内容覆盖
        that.getDoc().html("");
        that.getDoc().append(divcontent);
        that.log("动态激活...")
        //liapi.addClass("active");
        that.log("动态激活12...")
        that.getDoc().find("#myTab a:first").tab('show')
        //$('#myTab a:first').tab('show')

    }

    /***
     * 判断tabId是否存在
     * @param tabId
     */
    SwaggerBootstrapUi.prototype.tabExists=function (tabId) {
        var that=this;
        var flag=false;
        var tabUl=$('.layui-layout-admin .layui-body .layui-tab .layui-tab-title');
        tabUl.find("li").each(function (i, x) {
            var layId=$(x).attr("lay-id");
            if (layId==tabId){
                flag=true;
            }
        })
        return flag;
    }

    /***
     * tab点击关闭事件
     */
    SwaggerBootstrapUi.prototype.tabCloseEventsInit=function () {
        var that=this;
        var element=that.layui.element;
        //关闭当前tab
        $("#closeThisTabs").on("click",function (e) {
            var tabArr=new Array();
            e.preventDefault();
            var $title = $('.layui-layout-admin .layui-body .layui-tab .layui-tab-title');
            if ($title.find('li').first().hasClass('layui-this')) {
                return;
            }
            var close=$title.find('li.layui-this').find(".icon-sbu-tab-close");
            close.trigger("click");
        })

        //关闭其它tab
        $("#closeOtherTabs").on("click",function (e) {
            e.preventDefault();
            $('.layui-layout-admin .layui-body .layui-tab .layui-tab-title li:gt(0):not(.layui-this)').find(".icon-sbu-tab-close").trigger("click");
        })
        //关闭全部tab
        $("#closeAllTabs").on("click",function (e) {
            e.preventDefault();
            $('.layui-layout-admin .layui-body .layui-tab .layui-tab-title li:gt(0)').find(".icon-sbu-tab-close").trigger("click");
            element.tabChange('admin-pagetabs', "main");
        })

        //tab切换状态
        element.on('tab('+that.layTabFilter+')',function (data) {
            that.log(data)
            that.log("切换")
            that.removeLayerTips();
        })


    }

    /***
     * 自动选中
     * @param d
     */
    SwaggerBootstrapUi.prototype.tabRollPage=function (d) {
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
    }
    /***
     * tab最右显示
     */
    SwaggerBootstrapUi.prototype.tabFinallyRight=function () {
        var $tabTitle = $('.layui-layout-admin .layui-body .layui-tab .layui-tab-title');
        var left = $tabTitle.scrollLeft();
        $tabTitle.scrollLeft(left + $tabTitle.width());
    }
    /**
     * 创建权限页面
     */
    SwaggerBootstrapUi.prototype.createSecurityElement=function () {
        var that=this;
        var layui=that.layui;
        var element=layui.element;
        var tabId="SwaggerBootstrapUiSecurityScript";
        var tabContetId="layerTab"+tabId;
        setTimeout(function () {
            if(!that.tabExists(tabId)){
                that.currentInstance.securityArrs=that.getSecurityInfos();
                var html = template('SwaggerBootstrapUiSecurityScript', that.currentInstance);
                var tabObj={
                    id:tabId,
                    title:'Authorize',
                    content:html
                };
                that.globalTabs.push({id:tabId,title:'Authorize'});
                element.tabAdd(that.layTabFilter, tabObj);
                //保存事件
                that.getDoc().find("#"+tabContetId).find(".btn-save").on("click",function (e) {
                    e.preventDefault();
                    that.log("保存auth事件")
                    var save=$(this);
                    var ptr=save.parent().parent();
                    var data={key:ptr.data("key"),name:ptr.data("name")};
                    var value=ptr.find("input").val();
                    if(!value){
                        layer.msg("值无效");
                        return false;
                    }
                    $.each(that.currentInstance.securityArrs,function (i, sa) {
                        if(sa.key==data.key&&sa.name==data.name){
                            sa.value=value;
                            that.updateGlobalParams(sa,"securityArrs");
                        }
                    })
                    that.log(that.currentInstance);
                    layer.msg("保存成功");
                    that.currentInstance.securityArrs=that.getSecurityInfos();
                })
                that.resetAuthEvent(tabContetId);
                element.tabChange(that.layTabFilter,tabId);
                that.tabFinallyRight();
            }else{
                that.log("Auth Tab选中................")
                that.log(tabId);
                element.tabChange(that.layTabFilter,tabId);
                that.tabRollPage("auto");
            }

        },100)
        //保存事件
        that.getDoc().find("#"+tabContetId).find(".btn-save").on("click",function (e) {
            e.preventDefault();
            that.log("保存auth事件")
            var save=$(this);
            var ptr=save.parent().parent();
            var data={key:ptr.data("key"),name:ptr.data("name")};
            var value=ptr.find("input").val();
            if(!value){
                layer.msg("值无效");
                return false;
            }
            $.each(that.currentInstance.securityArrs,function (i, sa) {
                if(sa.key==data.key&&sa.name==data.name){
                    sa.value=value;
                    that.updateGlobalParams(sa,"securityArrs");
                }
            })
            that.currentInstance.securityArrs=that.getSecurityInfos();
            that.log(that.currentInstance);
        })
        that.resetAuthEvent(tabContetId);

    }

    /***
     * 注销Auth信息
     */
    SwaggerBootstrapUi.prototype.resetAuthEvent=function (tabContentId) {
        var that=this;
        that.getDoc().find("#"+tabContentId).find(".btn-reset-auth").on("click",function (e) {
            e.preventDefault();
            layer.confirm("确定注销吗?",function (index) {
                $.each(that.currentInstance.securityArrs,function (i, sa) {
                    sa.value="";
                    that.updateGlobalParams(sa,"securityArrs");
                })
                that.getDoc().find("#"+tabContentId).find(".btn-save").each(function () {
                    var saveBtn=$(this);
                    //赋值为空
                    saveBtn.parent().parent().find("input").val("");
                })
                layer.close(index);
                layer.msg("注销成功");
            })

        })
    }
    /***
     * 创建简介页面
     */
    SwaggerBootstrapUi.prototype.createDescriptionElement=function () {
        var that=this;
        var layui=that.layui;
        var element=layui.element;
        //内容覆盖
        //that.getDoc().html("");
        setTimeout(function () {
            var html = template('SwaggerBootstrapUiIntroScript', that.currentInstance);
            $("#mainTabContent").html("").html(html);
            element.tabChange('admin-pagetabs',"main");
            that.tabRollPage("auto");
        },100)


    }
    /***
     * 创建Models
     */
    SwaggerBootstrapUi.prototype.createModelsElement=function () {
        var that=this;
        var layui=that.layui;
        var element=layui.element;
        var treetable=layui.treetable;
        var tabId="SwaggerBootstrapUiModelsScript"+that.currentInstance.id;
        setTimeout(function () {
            if(!that.tabExists(tabId)){
                var html = template("SwaggerBootstrapUiModelsScript", that.currentInstance);
                var title="Swagger Models ("+that.currentInstance.name+")";
                var tabObj={
                    id:tabId,
                    title:title,
                    content:html
                };
                that.globalTabs.push({id:tabId,title:title});
                element.tabAdd(that.layTabFilter, tabObj);
                element.tabChange(that.layTabFilter,tabId);
                that.tabFinallyRight();
                /*that.getDoc().find("#accordion"+that.currentInstance.id).collapse({
                    toggle: false
                });*/
                //隐藏可折叠元素
                //that.getDoc().find("#accordion"+that.currentInstance.id).collapse('hide');
                //遍历创建treetable,赋值data值
                if(that.currentInstance.models!=null&&that.currentInstance.models.length>0){
                    var index = layer.load(2, {time: 10*1000});
                    async.forEachOf(that.currentInstance.models,function (model, key, callback) {
                        that.log(model)
                        var smodelAccording="#SwaggerAccordingModel"+model.id;
                        $(smodelAccording).data("data",model.data);
                        $(smodelAccording).collapse('hide');
                        //显示操作,以treetable组件展示models
                        $(smodelAccording).on('shown.bs.collapse', function () {
                            // 执行一些动作...
                            var athat=$(this);
                            var elem="#SwaggerModelTable"+model.id;
                            that.log(athat.data("data"));
                            treetable.render({
                                elem:elem,
                                data: athat.data("data"),
                                field: 'title',
                                treeColIndex: 0,          // treetable新增参数
                                treeSpid: -1,             // treetable新增参数
                                treeIdName: 'd_id',       // treetable新增参数
                                treePidName: 'd_pid',     // treetable新增参数
                                treeDefaultClose: true,   // treetable新增参数
                                treeLinkage: true,        // treetable新增参数
                                cols: [[
                                    {
                                        field: 'name',
                                        title: '名称',
                                        width: '30%'
                                    }, {
                                        field: 'type',
                                        title: '类型',
                                        width: '20%'
                                    },
                                    {
                                        field: 'description',
                                        title: '说明',
                                        width: '30%'
                                    },
                                    {
                                        field: 'schemaValue',
                                        title: 'schema',
                                        width: '20%'
                                    }
                                ]]
                            })
                        })

                    })
                    layer.close(index)
                }
            }else{
                element.tabChange(that.layTabFilter,tabId);
                that.tabRollPage("auto");
            }

        },100)
    }

    SwaggerBootstrapUi.prototype.introMarkdownDocInit=function (txt) {
        var that=this;
        //md2Html的配置
        hljs.configure({useBR: false});
        hljs.initHighlightingOnLoad();
        marked.setOptions({
            renderer: new marked.Renderer(),
            gfm: true,
            emoji: true,
            tables: true,
            breaks: false,
            pedantic: false,
            sanitize: true,
            smartLists: true,
            smartypants: false,
            highlight: function (code, lang) {
                try {
                    if (lang)
                        return hljs.highlight(lang, code).value;
                } catch (e) {
                    return hljs.highlightAuto(code).value;
                }
                return hljs.highlightAuto(code).value;
            }
        });
        $("#txtOffLineDoc").each(function(){
            var md = $(this).val();
            if(md){
                $("#offlineMarkdownShow").html(marked(txt));
                $('pre code').each(function(i, block) {
                    hljs.highlightBlock(block);
                });
            }
        });
        $("code").css("background-color", "transparent");
    }

    /***
     * 创建离线文档页面
     * 点击离线文档菜单时,创建该页面
     */
    SwaggerBootstrapUi.prototype.createMarkdownTab=function () {
        var that=this;
        var layui=that.layui;
        var element=layui.element;
        var tabId="offLinecontentScript";
        var tabContetId="layerTab"+tabId;

        setTimeout(function () {
            if(!that.tabExists(tabId)){
                var html = template('offLinecontentScript', that.currentInstance);
                var tabObj={
                    id:tabId,
                    title:'离线文档(MD)',
                    content:html
                };
                that.globalTabs.push({id:tabId,title:'离线文档(MD)'});
                element.tabAdd(that.layTabFilter, tabObj);
                element.tabChange(that.layTabFilter,tabId);
                that.tabFinallyRight();
                //正则替换离线文档的格式
                //首先替换多行
                var val=$("#txtOffLineDoc").val();
                val=val.replace(/(\s{4}[\n\r]){4,}/gi,"");
                //替换参数、响应码等属性前面多行空格
                val=val.replace(/(\n\s{10,})/gim,"\n");
                //加粗语法换行

                val=val.replace(/(^\*\*.*\*\*\:$)/igm,"\n$1\n");
                val=val.replace(/(^\*\*.*\*\*$)/igm,"\n$1\n");
                $("#txtOffLineDoc").val(val);
                that.log(that.currentInstance.paths.length)
                //如果当前接口梳理超过一定限制,md离线文档不予显示，仅仅展示源文件
                if(that.currentInstance.paths!=null&&that.currentInstance.paths.length>300){
                    $("#txtOffLineDoc").show();
                    $("#txtOffLineDoc").parent().css("width","100%");
                    layer.msg("当前接口数量超出限制,请使用第三方markdown转换软件进行转换以查看效果.")

                }else{
                    var convert=new showdown.Converter({tables:true,tablesHeaderId:true});
                    var html=convert.makeHtml(val);
                    $("#offlineMarkdownShow").html(html);
                    that.markdownToTabContent();
                }



                /*var convert=new showdown.Converter({tables:true,tablesHeaderId:true});
                var html=convert.makeHtml(val);
                $("#offlineMarkdownShow").html(html);
                that.markdownToTabContent();*/
            }else{
                element.tabChange(that.layTabFilter,tabId);
                that.tabRollPage("auto");
            }
        },100)
        var clipboard = new ClipboardJS('#btnCopy',{
            text:function () {
                return $("#txtOffLineDoc").val();
            }
        });
        clipboard.on('success', function(e) {
            layer.msg("复制成功")
        });
        clipboard.on('error', function(e) {
            layer.msg("复制失败,您当前浏览器版本不兼容,请手动复制.")
        });

    }


    SwaggerBootstrapUi.prototype.markdownToTabContent=function () {
        //是否显示导航栏
        var showNavBar = true;
        //是否展开导航栏
        var expandNavBar = true;
        var h1s = $("#offlineMarkdownShow").find("h1");
        var h2s = $("#offlineMarkdownShow").find("h2");
        var h3s = $("#offlineMarkdownShow").find("h3");
        var h4s = $("#offlineMarkdownShow").find("h4");
        var h5s = $("#offlineMarkdownShow").find("h5");
        var h6s = $("#offlineMarkdownShow").find("h6");

        var headCounts = [h1s.length, h2s.length, h3s.length, h4s.length, h5s.length, h6s.length];
        var vH1Tag = null;
        var vH2Tag = null;
        for(var i = 0; i < headCounts.length; i++){
            if(headCounts[i] > 0){
                if(vH1Tag == null){
                    vH1Tag = 'h' + (i + 1);
                }else{
                    vH2Tag = 'h' + (i + 1);
                }
            }
        }
        if(vH1Tag == null){
            return;
        }

        $("#offlineMarkdownShow").append('<div class="BlogAnchor">' +
            '<span style="color:red;position:absolute;top:-6px;left:0px;cursor:pointer;" onclick="$(\'.BlogAnchor\').hide();">×</span>' +
            '<p>' +
            '<b id="AnchorContentToggle" title="收起" style="cursor:pointer;">目录▲</b>' +
            '</p>' +
            '<div class="AnchorContent" id="AnchorContent"> </div>' +
            '</div>' );

        var vH1Index = 0;
        var vH2Index = 0;
        $("#offlineMarkdownShow").find("h1,h2,h3,h4,h5,h6").each(function(i,item){
            var id = '';
            var name = '';
            var tag = $(item).get(0).tagName.toLowerCase();
            var className = '';
            if(tag == vH1Tag){
                id = name = ++vH1Index;
                name = id;
                vH2Index = 0;
                className = 'item_h1';
            }else if(tag == vH2Tag){
                id = vH1Index + '_' + ++vH2Index;
                name = vH1Index + '.' + vH2Index;
                className = 'item_h2';
            }
            $(item).attr("id","wow"+id);
            $(item).addClass("wow_head");
            $("#AnchorContent").css('max-height', ($(window).height() - 400) + 'px');
            $("#AnchorContent").append('<li><a class="nav_item '+className+' anchor-link"  href="#wow'+id+'" link="#wow'+id+'">'+name+" · "+$(this).text()+'</a></li>');
        });

        $("#AnchorContentToggle").click(function(){
            var text = $(this).html();
            if(text=="目录▲"){
                $(this).html("目录▼");
                $(this).attr({"title":"展开"});
            }else{
                $(this).html("目录▲");
                $(this).attr({"title":"收起"});
            }
            $("#AnchorContent").toggle();
        });
        /*$(".anchor-link").click(function(){
            console.log("menu--click")
            console.log(this)
            console.log({scrollTop: $($(this).attr("link")).offset().top})
            $("#layerTaboffLinecontentScript").animate({scrollTop: $($(this).attr("link")).offset().top}, 500);
        });*/
        var headerNavs = $(".BlogAnchor li .nav_item");
        var headerTops = [];
        $(".wow_head").each(function(i, n){
            headerTops.push($(n).offset().top);
        });
        $("#offlineMarkdownShow").scroll(function(){
            var scrollTop = $("#offlineMarkdownShow").scrollTop();
            $.each(headerTops, function(i, n){
                var distance = n - scrollTop;
                if(distance >= 0){
                    $(".BlogAnchor li .nav_item.current").removeClass('current');
                    $(headerNavs[i]).addClass('current');
                    return false;
                }
            });
        });

        if(!showNavBar){
            $('.BlogAnchor').hide();
        }
        if(!expandNavBar){
            $(this).html("目录▼");
            $(this).attr({"title":"展开"});
            $("#AnchorContent").hide();
        }
    }

    /***
     * 解析实例属性
     */
    SwaggerBootstrapUi.prototype.analysisDefinition=function (menu) {
        var that=this;
        //解析definition
        if(menu!=null&&typeof (menu)!="undefined"&&menu!=undefined&&menu.hasOwnProperty("definitions")){
            var definitions=menu["definitions"];
            //改用async的for循环
            for(var name in definitions){
                var swud=new SwaggerBootstrapUiDefinition();
                swud.name=name;
                //that.log("开始解析Definition:"+name);
                //获取value
                var value=definitions[name];
                if ($.checkUndefined(value)){
                    swud.description=$.propValue("description",value,"");
                    swud.type=$.propValue("type",value,"");
                    swud.title=$.propValue("title",value,"");
                    //判断是否有required属性
                    if(value.hasOwnProperty("required")){
                        swud.required=value["required"];
                    }
                    //是否有properties
                    if(value.hasOwnProperty("properties")){
                        var properties=value["properties"];
                        var defiTypeValue={};
                        for(var property in properties){
                            var spropObj=new SwaggerBootstrapUiProperty();
                            spropObj.name=property;
                            var propobj=properties[property];
                            spropObj.originProperty=propobj;
                            spropObj.type=$.propValue("type",propobj,"string");
                            spropObj.description=$.propValue("description",propobj,"");
                            spropObj.example=$.propValue("example",propobj,"");
                            spropObj.format=$.propValue("format",propobj,"");
                            spropObj.required=$.propValue("required",propobj,false);
                            if(swud.required.length>0){
                                //有required属性,需要再判断一次
                                if($.inArray(spropObj.name,swud.required)>-1){
                                    //存在
                                    spropObj.required=true;
                                }
                            }
                            //默认string类型
                            var propValue="";
                            //判断是否有类型
                            if(propobj.hasOwnProperty("type")){
                                var type=propobj["type"];
                                //判断是否有example
                                if(propobj.hasOwnProperty("example")){
                                    propValue=propobj["example"];
                                }else if($.checkIsBasicType(type)){
                                    propValue=$.getBasicTypeValue(type);
                                }else{
                                    //that.log("解析属性："+property);
                                    //that.log(propobj);
                                    if(type=="array"){
                                        propValue=new Array();
                                        var items=propobj["items"];
                                        var ref=items["$ref"];
                                        //此处有可能items是array
                                        if (items.hasOwnProperty("type")){
                                            if(items["type"]=="array"){
                                                ref=items["items"]["$ref"];
                                            }
                                        }
                                        var regex=new RegExp("#/definitions/(.*)$","ig");
                                        if(regex.test(ref)){
                                            var refType=RegExp.$1;
                                            spropObj.refType=refType;
                                            //这里需要递归判断是否是本身,如果是,则退出递归查找
                                            var globalArr=new Array();
                                            //添加类本身
                                            globalArr.push(name);
                                            if(refType!=name){
                                                propValue.push(that.findRefDefinition(refType,definitions,false,globalArr));
                                            }else{
                                                propValue.push(that.findRefDefinition(refType,definitions,true,name,globalArr));
                                            }
                                        }
                                    }
                                }

                            }
                            else{
                                //that.log("解析属性："+property);
                                //that.log(propobj);
                                if(propobj.hasOwnProperty("$ref")){
                                    var ref=propobj["$ref"];
                                    var regex=new RegExp("#/definitions/(.*)$","ig");
                                    if(regex.test(ref)) {
                                        var refType = RegExp.$1;
                                        spropObj.refType=refType;
                                        //这里需要递归判断是否是本身,如果是,则退出递归查找
                                        var globalArr=new Array();
                                        //添加类本身
                                        globalArr.push(name);
                                        if(refType!=name){
                                            propValue=that.findRefDefinition(refType,definitions,false,globalArr);
                                        }else{
                                            propValue=that.findRefDefinition(refType,definitions,true,globalArr);
                                        }

                                    }
                                }else{
                                    propValue={};
                                }
                            }
                            spropObj.value=propValue;
                            //判断是否有format,如果是integer,判断是64位还是32位
                            if(spropObj.format!=null&&spropObj.format!=undefined&&spropObj.format!=""){
                                spropObj.type=spropObj.format;
                            }
                            //判断最终类型
                            if(spropObj.refType!=null&&spropObj.refType!=""){
                                //判断基础类型,非数字类型
                                if(spropObj.type=="string"){
                                    spropObj.type=spropObj.refType;
                                }
                            }
                            //addprop
                            //这里判断去重
                            if(!that.checkPropertiesExists(swud.properties,spropObj)){
                                swud.properties.push(spropObj);
                                defiTypeValue[property]=propValue;
                            }
                        }
                        swud.value=defiTypeValue;
                    }
                }
                that.currentInstance.difArrs.push(swud);
            }
        }

        //解析tags标签
        if(menu!=null&&typeof (menu)!="undefined"&&menu!=undefined&&menu.hasOwnProperty("tags")){
            var tags=menu["tags"];
            //判断是否开启增强配置
            if(that.settings.enableSwaggerBootstrapUi){
                var sbu=menu["swaggerBootstrapUi"]
                tags=sbu["tagSortLists"];
            }
            $.each(tags,function (i, tag) {
                var swuTag=new SwaggerBootstrapUiTag(tag.name,tag.description);
                that.currentInstance.tags.push(swuTag);
            })

        }
        //解析paths属性
        if(menu!=null&&typeof (menu)!="undefined"&&menu!=undefined&&menu.hasOwnProperty("paths")){
            var paths=menu["paths"];
            that.log("开始解析Paths.................")
            that.log(new Date().toTimeString());
            var pathStartTime=new Date().getTime();
            async.forEachOf(paths,function (pathObject,path, callback) {
                //var pathObject=paths[path];
                var apiInfo=null;
                if(pathObject.hasOwnProperty("get")){
                    //get方式
                    apiInfo=pathObject["get"]
                    if(apiInfo!=null){
                        var ins=that.createApiInfoInstance(path,"get",apiInfo);
                        //排序属性赋值
                        //判断是否开启增强配置
                        if(that.settings.enableSwaggerBootstrapUi){
                            var sbu=menu["swaggerBootstrapUi"]
                            var pathSortLists=sbu["pathSortLists"];
                            $.each(pathSortLists,function (i, ps) {
                                if(ps.path==ins.url&&ps.method==ins.methodType){
                                    ins.order=ps.order;
                                }
                            })
                        }
                        that.currentInstance.paths.push(ins);
                        that.methodCountAndDown("GET");

                    }
                }
                if(pathObject.hasOwnProperty("post")){
                    //post 方式
                    apiInfo=pathObject["post"]
                    if(apiInfo!=null){
                        var ins=that.createApiInfoInstance(path,"post",apiInfo);
                        //排序属性赋值
                        //判断是否开启增强配置
                        if(that.settings.enableSwaggerBootstrapUi){
                            var sbu=menu["swaggerBootstrapUi"]
                            var pathSortLists=sbu["pathSortLists"];
                            $.each(pathSortLists,function (i, ps) {
                                if(ps.path==ins.url&&ps.method==ins.methodType){
                                    ins.order=ps.order;
                                }
                            })
                        }
                        that.currentInstance.paths.push(ins);
                        that.methodCountAndDown("POST");
                    }
                }
                if(pathObject.hasOwnProperty("put")){
                    //put
                    apiInfo=pathObject["put"]
                    if(apiInfo!=null){
                        var ins=that.createApiInfoInstance(path,"put",apiInfo);
                        //排序属性赋值
                        //判断是否开启增强配置
                        if(that.settings.enableSwaggerBootstrapUi){
                            var sbu=menu["swaggerBootstrapUi"]
                            var pathSortLists=sbu["pathSortLists"];
                            $.each(pathSortLists,function (i, ps) {
                                if(ps.path==ins.url&&ps.method==ins.methodType){
                                    ins.order=ps.order;
                                }
                            })
                        }
                        that.currentInstance.paths.push(ins);
                        //that.currentInstance.paths.push(that.createApiInfoInstance(path,"put",apiInfo));
                        that.methodCountAndDown("PUT");
                    }
                }
                if(pathObject.hasOwnProperty("delete")){
                    //delete
                    apiInfo=pathObject["delete"]
                    if(apiInfo!=null){
                        var ins=that.createApiInfoInstance(path,"delete",apiInfo);
                        //排序属性赋值
                        //判断是否开启增强配置
                        if(that.settings.enableSwaggerBootstrapUi){
                            var sbu=menu["swaggerBootstrapUi"]
                            var pathSortLists=sbu["pathSortLists"];
                            $.each(pathSortLists,function (i, ps) {
                                if(ps.path==ins.url&&ps.method==ins.methodType){
                                    ins.order=ps.order;
                                }
                            })
                        }
                        that.currentInstance.paths.push(ins);
                        //that.currentInstance.paths.push(that.createApiInfoInstance(path,"delete",apiInfo));
                        that.methodCountAndDown("DELETE");
                    }
                }
                if (pathObject.hasOwnProperty("patch")){
                    //扩展 支持http其余请求方法接口
                    //add by xiaoymin 2018-4-28 07:16:12
                    //patch
                    apiInfo=pathObject["patch"]
                    if(apiInfo!=null){
                        var ins=that.createApiInfoInstance(path,"patch",apiInfo);
                        //排序属性赋值
                        //判断是否开启增强配置
                        if(that.settings.enableSwaggerBootstrapUi){
                            var sbu=menu["swaggerBootstrapUi"]
                            var pathSortLists=sbu["pathSortLists"];
                            $.each(pathSortLists,function (i, ps) {
                                if(ps.path==ins.url&&ps.method==ins.methodType){
                                    ins.order=ps.order;
                                }
                            })
                        }
                        that.currentInstance.paths.push(ins);

                        //that.currentInstance.paths.push(that.createApiInfoInstance(path,"patch",apiInfo));
                        that.methodCountAndDown("PATCH");
                    }
                }
                if (pathObject.hasOwnProperty("options")){
                    //OPTIONS
                    apiInfo=pathObject["options"]
                    if(apiInfo!=null){
                        var ins=that.createApiInfoInstance(path,"options",apiInfo);
                        //排序属性赋值
                        //判断是否开启增强配置
                        if(that.settings.enableSwaggerBootstrapUi){
                            var sbu=menu["swaggerBootstrapUi"]
                            var pathSortLists=sbu["pathSortLists"];
                            $.each(pathSortLists,function (i, ps) {
                                if(ps.path==ins.url&&ps.method==ins.methodType){
                                    ins.order=ps.order;
                                }
                            })
                        }
                        that.currentInstance.paths.push(ins);

                        //that.currentInstance.paths.push(that.createApiInfoInstance(path,"options",apiInfo));
                        that.methodCountAndDown("OPTIONS");
                    }
                }
                if (pathObject.hasOwnProperty("trace")){
                    //TRACE
                    apiInfo=pathObject["trace"]
                    if(apiInfo!=null){
                        var ins=that.createApiInfoInstance(path,"trace",apiInfo);
                        //排序属性赋值
                        //判断是否开启增强配置
                        if(that.settings.enableSwaggerBootstrapUi){
                            var sbu=menu["swaggerBootstrapUi"]
                            var pathSortLists=sbu["pathSortLists"];
                            $.each(pathSortLists,function (i, ps) {
                                if(ps.path==ins.url&&ps.method==ins.methodType){
                                    ins.order=ps.order;
                                }
                            })
                        }
                        that.currentInstance.paths.push(ins);

                        //that.currentInstance.paths.push(that.createApiInfoInstance(path,"trace",apiInfo));
                        that.methodCountAndDown("TRACE");
                    }
                }
                if (pathObject.hasOwnProperty("head")){
                    //HEAD
                    apiInfo=pathObject["head"]
                    if(apiInfo!=null){
                        var ins=that.createApiInfoInstance(path,"head",apiInfo);
                        //排序属性赋值
                        //判断是否开启增强配置
                        if(that.settings.enableSwaggerBootstrapUi){
                            var sbu=menu["swaggerBootstrapUi"]
                            var pathSortLists=sbu["pathSortLists"];
                            $.each(pathSortLists,function (i, ps) {
                                if(ps.path==ins.url&&ps.method==ins.methodType){
                                    ins.order=ps.order;
                                }
                            })
                        }
                        that.currentInstance.paths.push(ins);
                        that.methodCountAndDown("HEAD");
                    }
                }
                if (pathObject.hasOwnProperty("connect")){
                    //CONNECT
                    apiInfo=pathObject["connect"]
                    if(apiInfo!=null){
                        var ins=that.createApiInfoInstance(path,"connect",apiInfo);
                        //排序属性赋值
                        //判断是否开启增强配置
                        if(that.settings.enableSwaggerBootstrapUi){
                            var sbu=menu["swaggerBootstrapUi"]
                            var pathSortLists=sbu["pathSortLists"];
                            $.each(pathSortLists,function (i, ps) {
                                if(ps.path==ins.url&&ps.method==ins.methodType){
                                    ins.order=ps.order;
                                }
                            })
                        }
                        that.currentInstance.paths.push(ins);
                        that.methodCountAndDown("CONNECT");
                    }

                }
            })

           /* var selfPath="/api/new188/self";
            var selfobj={
                "summary": "第一个list功能",
                "tags": [
                    "1.8.8版本-20181208"
                ],
                "deprecated": false,
                "produces": [
                    "*!/!*"
                ],
                "operationId": "list3GET4997028859787988992",
                "responses": {
                    "200": {
                        "schema": {
                            "type": "object",
                            "properties": {
                                "id": {
                                    "name": "id",
                                    "in": "formData",
                                    "description": "标识的desc",
                                    "required": true,
                                    "type": "string"
                                },
                                "createTime": {
                                    "name": "createTime",
                                    "in": "formData",
                                    "description": "时间的desc",
                                    "required": false,
                                    "type": "string"
                                }
                            }
                        },
                        "description": "返回profile列表"
                    }
                },
                "description": "list的description",
                "parameters": [
                    {
                        "name": "id",
                        "in": "formData",
                        "description": "标识的desc",
                        "required": true,
                        "type": "string"
                    },
                    {
                        "name": "createTime",
                        "in": "formData",
                        "description": "时间的desc",
                        "required": false,
                        "type": "string"
                    }
                ],
                "consumes": [
                    "application/xml"
                ]
            }

            var selfins=that.createApiInfoInstance(selfPath,"get",selfobj);
            that.currentInstance.paths.push(selfins);*/
           /* for(var path in paths){


            }*/
            that.log("解析Paths结束,耗时："+(new Date().getTime()-pathStartTime));
            that.log(new Date().toTimeString());

        }
        //解析securityDefinitions属性
        if(menu!=null&&typeof (menu)!="undefined"&&menu!=undefined&&menu.hasOwnProperty("securityDefinitions")){
            var securityDefinitions=menu["securityDefinitions"];
            if(securityDefinitions!=null){
                //判断是否有缓存cache值
                //var cacheSecurityData=$("#sbu-header").data("cacheSecurity");
                var cacheSecurityData=that.getSecurityInfos();
                for(var j in securityDefinitions){
                    var sdf=new SwaggerBootstrapUiSecurityDefinition();
                    var sdobj=securityDefinitions[j];
                    sdf.key=j;
                    sdf.type=sdobj.type;
                    sdf.name=sdobj.name;
                    sdf.in=sdobj.in;
                    var flag=false;
                    if(cacheSecurityData!=null&&cacheSecurityData!=undefined){
                        //存在缓存值,更新当前值,无需再次授权
                        $.each(cacheSecurityData,function (i, sa) {
                            if(sa.key==sdf.key&&sa.name==sdf.name){
                                flag=true;
                                sdf.value=sa.value;
                            }
                        })
                    }
                    if (!flag){
                        //如果cache不存在,存储
                        that.storeGlobalParam(sdf,"securityArrs");
                    }
                    that.currentInstance.securityArrs.push(sdf);
                }
            }
        }

        //tag分组
        $.each(that.currentInstance.tags,function (i, tag) {
            //如果是第一次加载,则所有api都是新接口,无需判断老新
            if(!that.currentInstance.firstLoad){
                //判断是否新
                var tagNewApis=false;
                //查找childrens
                $.each(that.currentInstance.paths, function (k, methodApi) {
                    //判断tags是否相同
                    $.each(methodApi.tags, function (x, tagName) {
                        if (tagName == tag.name) {
                            //是否存在
                            if($.inArray(methodApi.id,that.currentInstance.groupApis)<0){
                                tagNewApis=true;
                                methodApi.hasNew=true;
                            }
                            tag.childrens.push(methodApi);
                        }
                    })
                })
                if(tagNewApis){
                    tag.hasNew=true;
                }
            }else{
                //查找childrens
                $.each(that.currentInstance.paths, function (k, methodApi) {
                    //判断tags是否相同
                    $.each(methodApi.tags, function (x, tagName) {
                        if (tagName == tag.name) {
                            tag.childrens.push(methodApi);
                        }
                    })
                })
            }

            if(that.settings.enableSwaggerBootstrapUi){
                //排序childrens
                tag.childrens.sort(function (a, b) {
                    return a.order-b.order;
                })
            }
        });

        if(that.currentInstance.firstLoad){
            var c=new SwaggerBootstrapUiCacheApis();
            c.id=that.currentInstance.groupId;
            c.cacheApis=that.currentInstance.groupApis;
            that.cacheApis.push(c);
        }else{
            //更新？页面点击后方可更新
        }

        //当前加入的cacheApi加入localStorage对象中
        that.storeCacheApis();
        //解析models
        //遍历paths属性中的请求以及响应Model参数,存在即加入,否则不加入

        that.log("开始解析refTreetableparameters属性.................")
        that.log(new Date().toTimeString());
        var pathStartTime=new Date().getTime();
        //遍历 refTreetableparameters属性
        if(that.currentInstance.paths!=null&&that.currentInstance.paths.length>0){
            $.each(that.currentInstance.paths,function (i, path) {
                //解析请求Model
                var requestParams=path.refTreetableparameters;
                if(requestParams!=null&&requestParams!=undefined&&requestParams.length>0){
                    $.each(requestParams,function (j, param) {
                        var name=param.name;
                        //判断集合中是否存在name
                        if($.inArray(name,that.currentInstance.modelNames)==-1){
                            that.currentInstance.modelNames.push(name);
                            //不存在
                            var model=new SwaggerBootstrapUiModel(param.id,name);
                            //遍历params
                            if(param.params!=null&&param.params.length>0){
                                //model本身需要添加一个父类
                                //model.data.push({id:model.id,name:name,pid:"-1"});
                                //data数据加入本身
                                //model.data=model.data.concat(param.params);
                                //第一层属性设置为pid
                                $.each(param.params,function (a, ps) {
                                    var newparam=$.extend({},ps,{pid:"-1"});
                                    model.data.push(newparam);
                                    if(ps.schema){
                                        //是schema
                                        //查找紫属性中存在的pid
                                        deepSchemaModel(model,requestParams,ps.id);
                                    }
                                })
                            }
                            that.currentInstance.models.push(model);
                        }
                    })
                }
                //解析响应Model
                var responseParams=path.responseTreetableRefParameters;
                //首先解析响应Model类
                if(path.responseParameterRefName!=null&&path.responseParameterRefName!=""){
                    //判断是否存在
                    if($.inArray(path.responseParameterRefName,that.currentInstance.modelNames)==-1){
                        that.currentInstance.modelNames.push(path.responseParameterRefName);
                        var id="param"+Math.round(Math.random()*1000000);
                        var model=new SwaggerBootstrapUiModel(id,path.responseParameterRefName);
                        model.data=[].concat(path.responseParameters);
                        if(responseParams!=null&&responseParams!=undefined&&responseParams.length>0){
                            $.each(responseParams,function (j, param) {
                                //遍历params
                                if(param.params!=null&&param.params.length>0) {
                                    //data数据加入本身
                                    model.data = model.data.concat(param.params);
                                }
                            })
                        }
                        that.currentInstance.models.push(model);
                    }
                }

                if(responseParams!=null&&responseParams!=undefined&&responseParams.length>0){
                    $.each(responseParams,function (j, param) {
                        var name=param.name;
                        //判断集合中是否存在name
                        if($.inArray(name,that.currentInstance.modelNames)==-1){
                            that.currentInstance.modelNames.push(name);
                            //不存在
                            var model=new SwaggerBootstrapUiModel(param.id,name);
                            //遍历params
                            if(param.params!=null&&param.params.length>0){
                                //model本身需要添加一个父类
                                //model.data.push({id:model.id,name:name,pid:"-1"});
                                //data数据加入本身
                                //model.data=model.data.concat(param.params);
                                $.each(param.params,function (a, ps) {
                                    var newparam=$.extend({},ps,{pid:"-1"});
                                    model.data.push(newparam);
                                    if(ps.schema){
                                        //是schema
                                        //查找紫属性中存在的pid
                                        deepSchemaModel(model,responseParams,ps.id);
                                    }
                                })
                            }
                            that.currentInstance.models.push(model);
                        }
                    })
                }
            })
        }
        //排序
        if(that.currentInstance.models!=null&&that.currentInstance.models.length>0){
            that.currentInstance.models.sort(function (a, b) {
                var aname=a.name;
                var bname=b.name;
                if (aname < bname) {
                    return -1;
                } else if (aname > bname) {
                    return 1;
                } else {
                    return 0;
                }
            })
        }
        that.log("解析refTreetableparameters结束,耗时："+(new Date().getTime()-pathStartTime));
        that.log(new Date().toTimeString());

    }
    
    
    function deepSchemaModel(model, arrs,id) {
        $.each(arrs,function (i, arr) {
            if(arr.id==id){
                //找到
                model.data=model.data.concat(arr.params);
                //遍历params
                if(arr.params!=null&&arr.params.length>0){
                    $.each(arr.params,function (j, ps) {
                        if(ps.schema){
                            deepSchemaModel(model,arrs,ps.id);
                        }
                    })
                }
            }
        })
    }
    /***
     * 判断属性是否已经存在
     * @param properties
     * @param prop
     */
    SwaggerBootstrapUi.prototype.checkPropertiesExists=function (properties, prop) {
        var flag=false;
        if(properties!=null&&properties!=undefined&&properties.length>0&&prop!=null&&prop!=undefined){
            $.each(properties,function (i, p) {
                if(p.name==prop.name&&p.in==prop.in&&p.type==prop.type){
                    flag=true;
                }
            })
        }
        return flag;
    }
    /***
     * 更新当前实例的security对象
     */
    SwaggerBootstrapUi.prototype.updateCurrentInstanceSecuritys=function () {
        var that=this;
        if(that.currentInstance.securityArrs!=null&&that.currentInstance.securityArrs.length>0){
            //判断是否有缓存cache值
            var cacheSecurityData=$("#sbu-header").data("cacheSecurity");
            if(cacheSecurityData!=null&&cacheSecurityData!=undefined){
                $.each(cacheSecurityData,function (i, ca) {
                    $.each(that.currentInstance.securityArrs,function (j, sa) {
                        if(ca.key==sa.key&&ca.name==sa.name){
                            sa.value=ca.value;
                        }
                    })
                })

            }
        }
    }

    /***
     * 计数
     * @param method
     */
    SwaggerBootstrapUi.prototype.methodCountAndDown=function (method) {
        var that=this;
        var flag=false;
        $.each(that.currentInstance.pathArrs,function (i, a) {
            if(a.method==method){
                flag=true;
                //计数加1
                a.count=a.count+1;
            }
        })
        if(!flag){
            var me=new SwaggerBootstrapUiPathCountDownLatch();
            me.method=method;
            me.count=1;
            that.currentInstance.pathArrs.push(me);
        }
    }

    /***
     * 根据api接口自定义tags添加
     * @param name
     */
    SwaggerBootstrapUi.prototype.mergeApiInfoSelfTags=function (name) {
        var that=this;
        var flag=false;
        $.each(that.currentInstance.tags,function (i, tag) {
            if(tag.name==name){
                flag=true;
            }
        })
        if(!flag){
            var ntag=new SwaggerBootstrapUiTag(name,name);
            that.currentInstance.tags.push(ntag);
        }
    }

    /***
     * 创建对象实例,返回SwaggerBootstrapUiApiInfo实例
     */
    SwaggerBootstrapUi.prototype.createApiInfoInstance=function(path,mtype,apiInfo){
        var that=this;

        var swpinfo=new SwaggerBootstrapUiApiInfo();
        //添加basePath
        var basePath=that.currentInstance.basePath;
        var newfullPath=that.currentInstance.host;
        var basePathFlag=false;
        //basePath="/addd/";
        if (basePath!=""&&basePath!="/"){
            newfullPath+=basePath;
            //如果非空,非根目录
            basePathFlag=true;
        }
        newfullPath+=path;
        //截取字符串
        var newurl=newfullPath.substring(newfullPath.indexOf("/"));
        //that.log("新的url:"+newurl)
        newurl=newurl.replace("//","/");
        var startApiTime=new Date().getTime();
        swpinfo.showUrl=newurl;
        //swpinfo.id="ApiInfo"+Math.round(Math.random()*1000000);
        swpinfo.url=newurl;
        swpinfo.originalUrl=newurl;
        swpinfo.basePathFlag=basePathFlag;
        swpinfo.methodType=mtype.toUpperCase();
        //接口id使用MD5策略,缓存整个调试参数到localStorage对象中,供二次调用
        var md5Str=newurl+mtype.toUpperCase();
        swpinfo.id=md5(md5Str);
        if(apiInfo!=null){
            if(apiInfo.hasOwnProperty("deprecated")){
                swpinfo.deprecated=apiInfo["deprecated"];
            }
            swpinfo.consumes=apiInfo.consumes;
            swpinfo.description=apiInfo.description;
            swpinfo.operationId=apiInfo.operationId;
            swpinfo.summary=apiInfo.summary;
            swpinfo.tags=apiInfo.tags;
            swpinfo.produces=apiInfo.produces;
            if (apiInfo.hasOwnProperty("parameters")){
                var pameters=apiInfo["parameters"];
                $.each(pameters,function (i, m) {
                    var minfo=new SwaggerBootstrapUiParameter();

                    minfo.name=$.propValue("name",m,"");
                    minfo.type=$.propValue("type",m,"");
                    minfo.in=$.propValue("in",m,"");
                    minfo.require=$.propValue("required",m,false);
                    minfo.description=$.replaceMultipLineStr($.propValue("description",m,""));
                    //判断是否有枚举类型
                    if(m.hasOwnProperty("enum")){
                        //that.log("包括枚举类型...")
                        //that.log(m.enum);
                        minfo.enum=m.enum;
                        //that.log(minfo);
                        //枚举类型,描述显示可用值
                        var avaiableArrStr=m.enum.join(",");
                        if(m.description!=null&&m.description!=undefined&&m.description!=""){
                            minfo.description=m.description+",可用值:"+avaiableArrStr;
                        }else{
                            minfo.description="枚举类型,可用值:"+avaiableArrStr;
                        }

                    }
                    //判断你是否有默认值(后台)
                    if(m.hasOwnProperty("default")){
                        minfo.txtValue=m["default"];
                    }
                    //swagger 2.9.2版本默认值响应X-EXAMPLE的值为2.9.2
                    if(m.hasOwnProperty("x-example")){
                        minfo.txtValue=m["x-example"];
                    }
                    if (m.hasOwnProperty("schema")){
                        //存在schema属性,请求对象是实体类
                        minfo.schema=true;
                        var schemaObject=m["schema"];
                        var schemaType=schemaObject["type"];
                        if (schemaType=="array"){
                            minfo.type=schemaType;
                            var schItem=schemaObject["items"];
                            var ref=schItem["$ref"];
                            var className=$.getClassName(ref);
                            minfo.schemaValue=className;
                            var def=that.getDefinitionByName(className);
                            if(def!=null){
                                minfo.def=def;
                                minfo.value=def.value;
                                if(def.description!=undefined&&def.description!=null&&def.description!=""){
                                    minfo.description=$.replaceMultipLineStr(def.description);
                                }
                            }
                        }else{
                            if (schemaObject.hasOwnProperty("$ref")){
                                var ref=m["schema"]["$ref"];
                                var className=$.getClassName(ref);
                                if(minfo.type!="array"){
                                    minfo.type=className;
                                }
                                minfo.schemaValue=className;
                                var def=that.getDefinitionByName(className);
                                if(def!=null){
                                    minfo.def=def;
                                    minfo.value=def.value;
                                    if(def.description!=undefined&&def.description!=null&&def.description!=""){
                                        minfo.description=$.replaceMultipLineStr(def.description);
                                    }
                                }
                            }else{
                                if (schemaObject.hasOwnProperty("type")){
                                    minfo.type=schemaObject["type"];
                                }
                                minfo.value="";
                            }
                        }
                    }
                    if(m.hasOwnProperty("items")){
                        var items=m["items"];
                        if(items.hasOwnProperty("$ref")){
                            var ref=items["$ref"];
                            var className=$.getClassName(ref);
                            //minfo.type=className;
                            minfo.schemaValue=className;
                            var def=that.getDefinitionByName(className);
                            if(def!=null){
                                minfo.def=def;
                                minfo.value=def.value;
                                if(def.description!=undefined&&def.description!=null&&def.description!=""){
                                    minfo.description=$.replaceMultipLineStr(def.description);
                                }
                            }
                        }else{
                            if (items.hasOwnProperty("type")){
                                //minfo.type=items["type"];
                                minfo.schemaValue=items["type"];
                            }
                            minfo.value="";
                        }
                    }
                    if(minfo.in=="body"){
                        //判断属性是否是array
                        if(minfo.type=="array"){
                            var txtArr=new Array();
                            txtArr.push(minfo.value);
                            //JSON显示
                            minfo.txtValue=JSON.stringify(txtArr,null,"\t")
                        }else{
                            //引用类型
                            if(!$.checkIsBasicType(minfo.type)){
                                minfo.txtValue=JSON.stringify(minfo.value,null,"\t");
                            }
                        }
                    }
                    //JSR-303 注解支持.
                    that.validateJSR303(minfo,m);
                    if(!checkParamArrsExists(swpinfo.parameters,minfo)){
                        swpinfo.parameters.push(minfo);
                        //判断当前属性是否是schema
                        if(minfo.schema){
                            deepRefParameter(minfo,that,minfo.def,swpinfo);
                            minfo.parentTypes.push(minfo.schemaValue);
                            //第一层的对象要一直传递
                            deepTreeTableRefParameter(minfo,that,minfo.def,swpinfo);
                        }
                    }
                })
            }
            var definitionType=null;
            var arr=false;
            //解析responsecode
            if(typeof (apiInfo.responses)!='undefined'&&apiInfo.responses!=null){
                var resp=apiInfo.responses;
                var rpcount=0;
                for(var status in resp) {
                    var swaggerResp=new SwaggerBootstrapUiResponseCode();
                    var rescrobj = resp[status];
                    swaggerResp.code=status;
                    swaggerResp.description=rescrobj["description"];
                    var rptype=null;
                    if (rescrobj.hasOwnProperty("schema")){
                        var schema=rescrobj["schema"];
                        //单引用类型
                        //判断是否是数组类型
                        var regex=new RegExp("#/definitions/(.*)$","ig");
                        if(schema.hasOwnProperty("$ref")){
                            if(regex.test(schema["$ref"])) {
                                var ptype=RegExp.$1;
                                swpinfo.responseParameterRefName=ptype;
                                swaggerResp.responseParameterRefName=ptype;
                                definitionType=ptype;
                                rptype=ptype;
                                swaggerResp.schema=ptype;
                            }
                        }else if(schema.hasOwnProperty("type")){
                            var t=schema["type"];
                            if (t=="array"){
                                arr=true;
                                if(schema.hasOwnProperty("items")){
                                    var items=schema["items"];
                                    var itref=items["$ref"];
                                    //此处需判断items是否数组
                                    if(items.hasOwnProperty("type")){
                                        if(items["type"]=="array"){
                                            itref=items["items"]["$ref"];
                                        }
                                    }
                                    if(regex.test(itref)) {
                                        var ptype=RegExp.$1;
                                        swpinfo.responseParameterRefName=ptype;
                                        swaggerResp.responseParameterRefName=ptype;
                                        definitionType=ptype;
                                        rptype=ptype;
                                        swaggerResp.schema=ptype;
                                    }
                                }
                            }else{
                                //判断是否存在properties属性
                                if(schema.hasOwnProperty("properties")){
                                    swaggerResp.schema=t;
                                    //自定义类型、放入difarrs对象中
                                    var swud=new SwaggerBootstrapUiDefinition();
                                    swud.name=swpinfo.id;
                                    swud.description="自定义Schema";
                                    definitionType=swud.name;
                                    rptype=swud.name;
                                    swaggerResp.responseParameterRefName=swud.name;

                                    var properties=schema["properties"];
                                    var defiTypeValue={};
                                    for(var property in properties) {
                                        var spropObj = new SwaggerBootstrapUiProperty();
                                        spropObj.name = property;
                                        var propobj = properties[property];
                                        spropObj.originProperty = propobj;
                                        spropObj.type = $.propValue("type", propobj, "string");
                                        spropObj.description = $.propValue("description", propobj, "");
                                        spropObj.example = $.propValue("example", propobj, "");
                                        spropObj.format = $.propValue("format", propobj, "");
                                        spropObj.required = $.propValue("required", propobj, false);
                                        if (swud.required.length > 0) {
                                            //有required属性,需要再判断一次
                                            if ($.inArray(spropObj.name, swud.required) > -1) {
                                                //存在
                                                spropObj.required = true;
                                            }
                                        }
                                        //默认string类型
                                        var propValue="";
                                        //判断是否有类型
                                        if(propobj.hasOwnProperty("type")){
                                            var type=propobj["type"];
                                            //判断是否有example
                                            if(propobj.hasOwnProperty("example")){
                                                propValue=propobj["example"];
                                            }else if($.checkIsBasicType(type)){
                                                propValue=$.getBasicTypeValue(type);
                                            }

                                        }
                                        spropObj.value=propValue;
                                        //判断是否有format,如果是integer,判断是64位还是32位
                                        if(spropObj.format!=null&&spropObj.format!=undefined&&spropObj.format!=""){
                                            spropObj.type=spropObj.format;
                                        }
                                        swud.properties.push(spropObj);
                                        defiTypeValue[property]=propValue;
                                    }
                                    swud.value=defiTypeValue;
                                    that.currentInstance.difArrs.push(swud);
                                }else{
                                    //判断是否是基础类型
                                    if($.checkIsBasicType(t)){
                                        //基础类型
                                        swpinfo.responseText=t;
                                        swpinfo.responseBasicType=true;

                                        //响应状态码的响应内容
                                        swaggerResp.responseText=t;
                                        swaggerResp.responseBasicType=true;
                                    }
                                }
                            }
                        }
                    }
                    if(rptype!=null){
                        //查询
                        for(var i=0;i<that.currentInstance.difArrs.length;i++){
                            var ref=that.currentInstance.difArrs[i];
                            if(ref.name==rptype){
                                if (arr){
                                    var na=new Array();
                                    na.push(ref.value);
                                    swaggerResp.responseValue=JSON.stringify(na,null,"\t");
                                    swaggerResp.responseJson=na;
                                }else{
                                    swaggerResp.responseValue=JSON.stringify(ref.value,null,"\t");
                                    swaggerResp.responseJson=ref.value;
                                }
                            }
                        }
                        //响应参数
                        var def=that.getDefinitionByName(rptype);
                        if (def!=null){
                            if(def.hasOwnProperty("properties")){
                                var props=def["properties"];
                                $.each(props,function (i, p) {
                                    var resParam=new SwaggerBootstrapUiParameter();
                                    resParam.name=p.name;
                                    if (!checkParamArrsExists(swaggerResp.responseParameters,resParam)){
                                        swaggerResp.responseParameters.push(resParam);
                                        resParam.description=$.replaceMultipLineStr(p.description);
                                        if(p.type==null||p.type==""){
                                            if(p.refType!=null){
                                                if(!$.checkIsBasicType(p.refType)){
                                                    resParam.schemaValue=p.refType;
                                                    //存在引用类型,修改默认type
                                                    resParam.type=p.refType;
                                                    var deepDef=that.getDefinitionByName(p.refType);
                                                    deepResponseRefParameter(swaggerResp,that,deepDef,resParam);
                                                    resParam.parentTypes.push(p.refType);
                                                    deepTreeTableResponseRefParameter(swaggerResp,that,deepDef,resParam);
                                                }
                                            }
                                        }else{
                                            resParam.type=p.type;
                                            if(!$.checkIsBasicType(p.type)){
                                                if(p.refType!=null){
                                                    if(!$.checkIsBasicType(p.refType)){
                                                        resParam.schemaValue=p.refType;
                                                        //存在引用类型,修改默认type
                                                        if(p.type!="array"){
                                                            resParam.type=p.refType;
                                                        }
                                                        var deepDef=that.getDefinitionByName(p.refType);
                                                        deepResponseRefParameter(swaggerResp,that,deepDef,resParam);
                                                        resParam.parentTypes.push(p.refType);
                                                        deepTreeTableResponseRefParameter(swaggerResp,that,deepDef,resParam);
                                                    }
                                                }else{
                                                    resParam.schemaValue=p.type;
                                                    //存在引用类型,修改默认type
                                                    resParam.type=p.type;
                                                    var deepDef=that.getDefinitionByName(p.type);
                                                    deepResponseRefParameter(swaggerResp,that,deepDef,resParam);
                                                    resParam.parentTypes.push(p.type);
                                                    deepTreeTableResponseRefParameter(swaggerResp,that,deepDef,resParam);
                                                }
                                            }
                                        }
                                    }
                                })

                            }
                        }
                    }

                    if(swaggerResp.schema!=null&&swaggerResp.schema!=undefined){
                        rpcount=rpcount+1;
                    }
                    swpinfo.responseCodes.push(swaggerResp);
                }
                swpinfo.multipartResponseSchemaCount=rpcount;
                if(rpcount>1){
                    swpinfo.multipartResponseSchema=true;
                }
            }

            if (definitionType!=null&&!swpinfo.multipartResponseSchema){
                //查询
                for(var i=0;i<that.currentInstance.difArrs.length;i++){
                    var ref=that.currentInstance.difArrs[i];
                    if(ref.name==definitionType){
                        if (arr){
                            var na=new Array();
                            na.push(ref.value);
                            swpinfo.responseValue=JSON.stringify(na,null,"\t");
                            swpinfo.responseJson=na;
                        }else{
                            swpinfo.responseValue=JSON.stringify(ref.value,null,"\t");
                            swpinfo.responseJson=ref.value;
                        }
                    }
                }
                //响应参数
                var def=that.getDefinitionByName(definitionType);
                if (def!=null){
                    if(def.hasOwnProperty("properties")){
                        var props=def["properties"];
                        $.each(props,function (i, p) {
                            var resParam=new SwaggerBootstrapUiParameter();
                            resParam.name=p.name;
                            if (!checkParamArrsExists(swpinfo.responseParameters,resParam)){
                                swpinfo.responseParameters.push(resParam);
                                resParam.description=$.replaceMultipLineStr(p.description);
                                if(p.type==null||p.type==""){
                                    if(p.refType!=null){
                                        if(!$.checkIsBasicType(p.refType)){
                                            resParam.schemaValue=p.refType;
                                            //存在引用类型,修改默认type
                                            resParam.type=p.refType;
                                            var deepDef=that.getDefinitionByName(p.refType);
                                            deepResponseRefParameter(swpinfo,that,deepDef,resParam);
                                            resParam.parentTypes.push(p.refType);
                                            deepTreeTableResponseRefParameter(swpinfo,that,deepDef,resParam);
                                        }
                                    }
                                }else{
                                    resParam.type=p.type;
                                    if(!$.checkIsBasicType(p.type)){
                                        if(p.refType!=null){
                                            if(!$.checkIsBasicType(p.refType)){
                                                resParam.schemaValue=p.refType;
                                                //存在引用类型,修改默认type
                                                if(p.type!="array"){
                                                    resParam.type=p.refType;
                                                }
                                                var deepDef=that.getDefinitionByName(p.refType);
                                                deepResponseRefParameter(swpinfo,that,deepDef,resParam);
                                                resParam.parentTypes.push(p.refType);
                                                deepTreeTableResponseRefParameter(swpinfo,that,deepDef,resParam);
                                            }
                                        }else{
                                            resParam.schemaValue=p.type;
                                            //存在引用类型,修改默认type
                                            resParam.type=p.type;
                                            var deepDef=that.getDefinitionByName(p.type);
                                            deepResponseRefParameter(swpinfo,that,deepDef,resParam);
                                            resParam.parentTypes.push(p.type);
                                            deepTreeTableResponseRefParameter(swpinfo,that,deepDef,resParam);
                                        }
                                    }
                                }
                            }
                        })

                    }
                }

            }
            //that.currentInstance.paths.push(swpinfo);
            for(var i=0;i<apiInfo.tags.length;i++){
                var tagName=apiInfo.tags[i];
                that.mergeApiInfoSelfTags(tagName);
            }
        }
        //获取请求json
        //统计body次数
        if(swpinfo.parameters!=null){
            var count=0;
            var tmpJsonValue=null;
            $.each(swpinfo.parameters,function (i, p) {
                if(p.in=="body"){
                    count=count+1;
                    if(p.txtValue!=null&&p.txtValue!=""){
                        tmpJsonValue=p.txtValue;
                    }
                }
            })
            if (count==1){
                swpinfo.requestValue=tmpJsonValue;
            }
            //此处判断接口的请求参数类型
            //判断consumes请求类型
            if(apiInfo.consumes!=undefined&&apiInfo.consumes!=null&&apiInfo.consumes.length>0){
                var ctp=apiInfo.consumes[0];
                if(ctp=="multipart/form-data"){
                    swpinfo.contentType=ctp;
                    swpinfo.contentValue="form-data";
                }else if(ctp=="text/plain"){
                    swpinfo.contentType=ctp;
                    swpinfo.contentValue="raw";
                    swpinfo.contentShowValue="Text(text/plain)";
                }else{
                    //根据参数遍历,否则默认是表单x-www-form-urlencoded类型
                    var defaultType="application/x-www-form-urlencoded;charset=UTF-8";
                    var defaultValue="x-www-form-urlencoded";
                    for(var i=0;i<swpinfo.parameters.length;i++){
                        var pt=swpinfo.parameters[i];
                        if(pt.in=="body"){
                            if(pt.schemaValue=="MultipartFile"){
                                defaultType="multipart/form-data";
                                defaultValue="form-data";
                                break;
                            }else{
                                defaultValue="raw";
                                defaultType="application/json";
                                break;
                            }
                        }else{
                            if(pt.schemaValue=="MultipartFile") {
                                defaultType = "multipart/form-data";
                                defaultValue = "form-data";
                                break;
                            }
                        }

                    }
                    swpinfo.contentType=defaultType;
                    swpinfo.contentValue=defaultValue;
                }
            }else{
                //根据参数遍历,否则默认是表单x-www-form-urlencoded类型
                var defaultType="application/x-www-form-urlencoded;charset=UTF-8";
                var defaultValue="x-www-form-urlencoded";
                for(var i=0;i<swpinfo.parameters.length;i++){
                    var pt=swpinfo.parameters[i];
                    if(pt.in=="body"){
                        if(pt.schemaValue=="MultipartFile"){
                            defaultType="multipart/form-data";
                            defaultValue="form-data";
                            break;
                        }else{
                            defaultValue="raw";
                            defaultType="application/json";
                            break;
                        }
                    }else{
                        if(pt.schemaValue=="MultipartFile") {
                            defaultType = "multipart/form-data";
                            defaultValue = "form-data";
                            break;
                        }
                    }
                }
                swpinfo.contentType=defaultType;
                swpinfo.contentValue=defaultValue;
            }
        }
        /*if(swpinfo.parameters.length==1){
            //只有在参数只有一个且是body类型的参数才有请求示例
            var reqp=swpinfo.parameters[0];
            //判断参数是否body类型
            if(reqp.in=="body"){
                if(reqp.txtValue!=null&&reqp.txtValue!=""){
                    swpinfo.requestValue=reqp.txtValue;
                }
            }
        }*/
        //第一次加载
        if(that.currentInstance.firstLoad){
            that.currentInstance.groupApis.push(swpinfo.id);
        }
        return swpinfo;
    }

    /***
     * JSR-303支持
     * @param parameter
     */
    SwaggerBootstrapUi.prototype.validateJSR303=function (parameter,origin) {
        var max=origin["maximum"],min=origin["minimum"],emin=origin["exclusiveMinimum"],emax=origin["exclusiveMaximum"];
        var pattern=origin["pattern"];
        var maxLength=origin["maxLength"],minLength=origin["minLength"];
        if (max||min||emin||emax){
            parameter.validateStatus=true;
            parameter.validateInstance={minimum:min,maximum:max,exclusiveMaximum:emax,exclusiveMinimum:emin};
        }else if(pattern){
            parameter.validateStatus=true;
            parameter.validateInstance={"pattern":origin["pattern"]};
        }else if(maxLength||minLength){
            parameter.validateStatus=true;
            parameter.validateInstance={maxLength:maxLength,minLength:minLength};
        }
    }




    /***
     * 是否已经存在
     * @param arr
     * @param param
     * @returns {boolean}
     */
    function checkParamArrsExists(arr, param) {
        var flag=false;
        if(arr!=null&&arr.length>0){
            $.each(arr,function (i, a) {
                if(a.name==param.name){
                    flag=true;
                }
            })
        }
        return flag;
    }

    function deepResponseRefParameter(swpinfo, that, def,resParam) {
        if (def!=null){
            if(def.hasOwnProperty("properties")){
                var refParam=new SwaggerBootstrapUiRefParameter();
                refParam.name=def.name;
                if(!checkParamArrsExists(swpinfo.responseRefParameters,refParam)){
                    swpinfo.responseRefParameters.push(refParam);
                    if(def.hasOwnProperty("properties")){
                        var props=def["properties"];
                        $.each(props,function (i, p) {
                            var refp=new SwaggerBootstrapUiParameter();
                            refp.pid=resParam.id;
                            refp.name=p.name;
                            refp.type=p.type;
                            refp.description=$.replaceMultipLineStr(p.description);
                            //add之前需要判断是否已添加,递归情况有可能重复
                            refParam.params.push(refp);
                            //判断类型是否基础类型
                            if(!$.checkIsBasicType(p.refType)){
                                refp.schemaValue=p.refType;
                                refp.schema=true;
                                if(resParam.name!=refp.name||resParam.schemaValue!=p.refType){
                                    var deepDef=that.getDefinitionByName(p.refType);
                                    deepResponseRefParameter(swpinfo,that,deepDef,refp);
                                }
                            }
                        })
                    }
                }
            }
        }
    }


    function checkParamTreeTableArrsExists(arr, param) {
        var flag=false;
        if(arr!=null&&arr.length>0){
            $.each(arr,function (i, a) {
                if(a.name==param.name&&a.id==param.id){
                    flag=true;
                }
            })
        }
        return flag;
    }

    function deepTreeTableResponseRefParameter(swpinfo, that, def,resParam) {
        if (def!=null){
            if(def.hasOwnProperty("properties")){
                var refParam=new SwaggerBootstrapUiTreeTableRefParameter();
                refParam.name=def.name;
                refParam.id=resParam.id;
                if(!checkParamTreeTableArrsExists(swpinfo.responseTreetableRefParameters,refParam)){
                    //firstParameter.childrenTypes.push(def.name);
                    swpinfo.responseTreetableRefParameters.push(refParam);
                    if(def.hasOwnProperty("properties")){
                        var props=def["properties"];
                        $.each(props,function (i, p) {
                            var refp=new SwaggerBootstrapUiParameter();
                            $.each(resParam.parentTypes,function (i, pt) {
                                refp.parentTypes.push(pt);
                            })
                            refp.parentTypes.push(def.name);
                            refp.pid=resParam.id;
                            refp.name=p.name;
                            refp.type=p.type;
                            refp.description=$.replaceMultipLineStr(p.description);
                            //add之前需要判断是否已添加,递归情况有可能重复
                            refParam.params.push(refp);
                            //判断类型是否基础类型
                            if(!$.checkIsBasicType(p.refType)){
                                refp.schemaValue=p.refType;
                                refp.schema=true;
                                if(resParam.name!=refp.name||resParam.schemaValue!=p.refType){
                                    var deepDef=that.getDefinitionByName(p.refType);
                                    if(!checkDeepTypeAppear(refp.parentTypes,p.refType)){
                                        deepTreeTableResponseRefParameter(swpinfo,that,deepDef,refp);
                                    }
                                }
                            }
                        })
                    }
                }

            }
        }
    }

    /***
     * 递归查询
     * @param minfo
     * @param that
     * @param def
     */
    function deepRefParameter(minfo,that,def,apiInfo) {
        if (def!=null){
            var refParam=new SwaggerBootstrapUiRefParameter();
            refParam.name=def.name;
            if(!checkParamArrsExists(apiInfo.refparameters,refParam)){
                apiInfo.refparameters.push(refParam);
                if(def.hasOwnProperty("properties")){
                    var props=def["properties"];
                    $.each(props,function (i, p) {
                        var refp=new SwaggerBootstrapUiParameter();
                        refp.pid=minfo.id;
                        refp.name=p.name;
                        refp.type=p.type;
                        //判断非array
                        if(p.type!="array"){
                            if(p.refType!=null&&p.refType!=undefined&&p.refType!=""){
                                //修复针对schema类型的参数,显示类型为schema类型
                                refp.type=p.refType;
                            }
                        }
                        refp.in=minfo.in;
                        refp.require=p.required;
                        refp.description=$.replaceMultipLineStr(p.description);
                        that.validateJSR303(refp,p.originProperty);
                        refParam.params.push(refp);
                        //判断类型是否基础类型
                        if(!$.checkIsBasicType(p.refType)){
                            refp.schemaValue=p.refType;
                            refp.schema=true;
                            //属性名称不同,或者ref类型不同
                            if(minfo.name!=refp.name||minfo.schemaValue!=p.refType){
                                var deepDef=that.getDefinitionByName(p.refType);
                                deepRefParameter(refp,that,deepDef,apiInfo);
                            }
                        }
                    })
                }
            }
        }
    }

    /***
     * 递归父类是否出现
     * @param types
     * @param type
     * @returns {boolean}
     */
    function checkDeepTypeAppear(types, type) {
        var flag=false;
        $.each(types,function (i, t) {
            if(t==type){
                //存在
                flag=true;
            }
        })
        return flag;
    }

    /***
     * treeTable组件
     * @param minfo
     * @param that
     * @param def
     * @param apiInfo
     */
    function deepTreeTableRefParameter(minfo,that,def,apiInfo) {
        if (def!=null){
            var refParam=new SwaggerBootstrapUiTreeTableRefParameter();
            refParam.name=def.name;
            refParam.id=minfo.id;
            //如果当前属性中的schema类出现过1次则不在继续,防止递归死循环
            if(!checkParamTreeTableArrsExists(apiInfo.refTreetableparameters,refParam)){
                //firstParameter.childrenTypes.push(def.name);
                apiInfo.refTreetableparameters.push(refParam);
                if(def.hasOwnProperty("properties")){
                    var props=def["properties"];
                    $.each(props,function (i, p) {
                        var refp=new SwaggerBootstrapUiParameter();
                        refp.pid=minfo.id;
                        $.each(minfo.parentTypes,function (i, pt) {
                            refp.parentTypes.push(pt);
                        })
                        //refp.parentTypes=minfo.parentTypes;
                        refp.parentTypes.push(def.name)
                        //level+1
                        refp.level=minfo.level+1;
                        refp.name=p.name;
                        refp.type=p.type;
                        //判断非array
                        if(p.type!="array"){
                            if(p.refType!=null&&p.refType!=undefined&&p.refType!=""){
                                //修复针对schema类型的参数,显示类型为schema类型
                                refp.type=p.refType;
                            }
                        }
                        refp.in=minfo.in;
                        refp.require=p.required;
                        refp.description=$.replaceMultipLineStr(p.description);
                        that.validateJSR303(refp,p.originProperty);
                        refParam.params.push(refp);
                        //判断类型是否基础类型
                        if(!$.checkIsBasicType(p.refType)){
                            refp.schemaValue=p.refType;
                            refp.schema=true;

                            //属性名称不同,或者ref类型不同
                            if(minfo.name!=refp.name||minfo.schemaValue!=p.refType){
                                var deepDef=that.getDefinitionByName(p.refType);
                                if(!checkDeepTypeAppear(refp.parentTypes,p.refType)){
                                    deepTreeTableRefParameter(refp,that,deepDef,apiInfo);
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    /***
     * 根据类名查找definition
     */
    SwaggerBootstrapUi.prototype.getDefinitionByName=function(name){
        var that=this;
        var def=null;
        $.each(that.currentInstance.difArrs,function (i, d) {
            if (d.name==name){
                def=d;
                return;
            }
        })
        return def;
    }
    /***
     * 递归查询definition
     * @param refType
     * @param definitions
     * @param flag
     */
    SwaggerBootstrapUi.prototype.findRefDefinition=function (definitionName, definitions, flag,globalArr) {
        var that=this;
        var defaultValue="";
        for(var definition in definitions){
            if(definitionName==definition ){
                //不解析本身
                //that.log("解析definitionName:"+definitionName);
                //that.log("是否递归："+flag);
                var value=definitions[definition];
                //是否有properties
                if(value.hasOwnProperty("properties")){
                    var properties=value["properties"];
                    var defiTypeValue={};
                    for(var property in properties){
                        var propobj=properties[property];
                        //默认string类型
                        var propValue="";
                        //判断是否有类型
                        if(propobj.hasOwnProperty("type")){
                            var type=propobj["type"];
                            //判断是否有example
                            if(propobj.hasOwnProperty("example")) {
                                propValue = propobj["example"];
                            }else if($.checkIsBasicType(type)){
                                propValue=$.getBasicTypeValue(type);
                            }else{
                                if(type=="array"){
                                    propValue=new Array();
                                    var items=propobj["items"];
                                    var ref=items["$ref"];
                                    if(items.hasOwnProperty("type")){
                                        if(items["type"]=="array"){
                                            ref=items["items"]["$ref"];
                                        }
                                    }
                                    var regex=new RegExp("#/definitions/(.*)$","ig");
                                    if(regex.test(ref)){
                                        var refType=RegExp.$1;
                                        if (!flag){
                                            //判断是否存在集合中
                                            if($.inArray(refType,globalArr) != -1){
                                                //存在
                                                propValue.push({});
                                            }else{
                                                globalArr.push(definitionName);
                                                propValue.push(that.findRefDefinition(refType,definitions,flag,globalArr));
                                            }
                                        }

                                    }
                                }
                            }

                        }
                        else{
                            //存在ref
                            if(propobj.hasOwnProperty("$ref")){
                                var ref=propobj["$ref"];
                                var regex=new RegExp("#/definitions/(.*)$","ig");
                                if(regex.test(ref)) {
                                    var refType = RegExp.$1;
                                    //这里需要递归判断是否是本身,如果是,则退出递归查找
                                    if(!flag){
                                        if($.inArray(refType,globalArr) != -1){
                                            //存在
                                            propValue={};
                                        }else{
                                            globalArr.push(definitionName);
                                            propValue=that.findRefDefinition(refType,definitions,flag,globalArr);
                                        }
                                    }
                                }
                            }else{
                                propValue={};
                            }

                        }
                        defiTypeValue[property]=propValue;
                    }
                    defaultValue=defiTypeValue;
                }else{
                    defaultValue={};
                }
            }
        }
        return defaultValue;
    }
    /***
     * 创建swagger分组页面元素
     */
    SwaggerBootstrapUi.prototype.createGroupElement=function () {
        var that=this;
        //创建分组flag
        that.getMenu().html("");
        //修改动态创建分组,改为实际赋值
        var groupSele=$("#sbu-group-sel");
        $.each(that.instances,function (i, group) {
            //这里分组url需要判断,先获取url，获取不到再获取location属性
            var url=group.url;
            if(url==undefined||url==null||url==""){
                url=group.location;
            }
            var groupOption=$("<option data-url='"+url+"' data-name='"+group.name+"'>"+group.name+"</option>");
            groupSele.append(groupOption);
        })
        groupSele.on("change",function () {
            var t=$(this);
            var name=t.find("option:selected").attr("data-name");
            that.log("分组：：");
            that.log(name);
            var instance=that.selectInstanceByGroupName(name);
            that.log(instance);
            that.analysisApi(instance);
        })
        //默认加载第一个url
        that.analysisApi(that.instances[0]);
    }
    /***
     * 获取当前分组实例
     * @param name
     * @returns {*}
     */
    SwaggerBootstrapUi.prototype.selectInstanceByGroupName=function (name) {
        var that=this;
        var instance=null;
        $.each(that.instances,function (i, group) {
            if (group.name==name){
                instance=group;
                return;
            }
        })
        return instance;
    }
    /***
     * 添加左侧菜单功能
     */
    SwaggerBootstrapUi.prototype.addMenu=function () {

    }


    SwaggerBootstrapUi.prototype.initWindowWidthAndHeight=function () {
        var that=this;
        $("#leftMenu").css("height",$(window).height()-$("#sbu-header").height()-2);
        $("#content").css("height",$(window).height()-$("#sbu-header").height()-2);
    }

    SwaggerBootstrapUi.prototype.windowResize=function () {
        var that=this;
        var container = $('#container'),left = $('.left'),right = $('.right'),handle = $('#handle');
        //window resize事件
        $(window).resize(function (e) {
            var fullWidth=container.width();
            var leftWidth=left.width();
            var handleWidth=handle.width();
            var rightWidth=fullWidth-leftWidth-handleWidth;
            right.css("width",rightWidth);
            var cheight=$(window).height()-$("#sbu-header").height()-2;
            $("#leftMenu").css("height",$(window).height()-$("#sbu-header").height()-2);
            $("#content").css("height",cheight);
            //that.log("resize------------height")
            //that.log("window--"+$(window).height())
            //that.log("document--"+$(document).height())
            //that.log("left--"+$("#leftMenu").height())
            var headerHeight=$("#"+that.globalTabId).find("div:first");
            var diff=cheight-headerHeight.height()-37;
            $(".HomeDoc").css("height",diff);
        })
    }

    /***
     * 控制台打印
     * @param msg
     */
    SwaggerBootstrapUi.prototype.log=function (msg) {
        if(window.console){
            //正式版不开启console功能
            console.log(msg);
        }
    }
    /***
     * 获取菜单元素
     */
    SwaggerBootstrapUi.prototype.getMenu=function () {
        var menuId=this.menuId;
        return $("#"+menuId);
    }
    SwaggerBootstrapUi.prototype.getSearchMenu=function () {
        var that=this;
        return $("#"+that.searchMenuId);
    }

    SwaggerBootstrapUi.prototype.welcome=function () {
        var that=this;
        var msg="欢迎使用swagger-bootstrap-ui "+that.version+"~!\r\n\r\n欢迎任何形式的反馈issue,star,pr~~~~！祝君生活愉快:)~~~!\r\n\r\nGitHub:https://github.com/xiaoymin/Swagger-Bootstrap-UI\r\n\r\n码云Gitee:https://gitee.com/xiaoym/swagger-bootstrap-ui\r\n\r\nQQ群:608374991";
        if(window.console){
            console.log(msg);
        }
    }
    /***
     * 获取当前swagger页面主页面元素
     * @returns {*|HTMLElement}
     */
    SwaggerBootstrapUi.prototype.getDoc=function () {
        return $("#"+this.docId);
    }
    SwaggerBootstrapUi.prototype.getTab=function () {
        return $("#"+this.tabId);
    }
    SwaggerBootstrapUi.prototype.getTabContent=function () {
        return $("#"+this.tabContentId);
    }

    /***
     * SwaggerBootstrapUi Model树对象
     * @param id
     * @param name
     * @constructor
     */
    var SwaggerBootstrapUiModel=function (id, name) {
        this.id=id;
        this.name=name;
        //存放Model对象的属性结构
        //SwaggerBootstrapUiTreeTableRefParameter集合
        this.data=new Array();
        this.random=parseInt(Math.random()*(6-1+1)+1,10);
        this.modelClass=function () {
            var cname="panel-default";
            switch (this.random){
                case 1:
                    cname="panel-success";
                    break;
                case 2:
                    cname="panel-success";
                    break;
                case 3:
                    cname="panel-info";
                    break;
                case 4:
                    cname="panel-warning";
                    break;
                case 5:
                    cname="panel-danger";
                    break;
                case 6:
                    cname="panel-default";
                    break;
            }
            return cname;
        }

    }

    /***
     * 存储请求参数对象
     * @constructor
     */
    var SwaggerBootstrapUiStore=function () {
        //存储接口的md5码id集合
        this.ids=new Array();
        //存储SwaggerBootstrapUiRequestStore对象集合
        this.stores=new Array();
    }

    /***
     * 缓存请求参数对象
     * @param id
     * @param arrs
     * @constructor
     */
    var SwaggerBootstrapUiRequestStore=function (id, arrs) {
        this.id=id;
        this.data=arrs;
    }

    /***
     * swagger 分组对象
     * @param name 分组对象名称
     * @param location url地址
     * @param version 版本号
     * @constructor
     */
    var SwaggerBootstrapUiInstance=function (name, location, version) {
        this.id="SwaggerBootstrapUiInstance"+Math.round(Math.random()*1000000);
        //默认未加载
        this.load=false;
        //分组名称
        this.name=name;
        //分组url地址
        this.location=location;
        //不分组是url地址
        this.url=null;
        //增强地址
        this.extUrl=null;
        this.groupVersion=version;
        //分组url请求实例
        this.basePath="";
        this.host="";
        this.swagger="";
        this.description="";
        this.title="";
        this.version="";
        this.termsOfService="";
        this.contact="";
        //当前definistion数组
        // SwaggerBootstrapUiDefinition 集合
        this.difArrs=new Array();
        //标签分类信息组
        //SwaggerBootstrapUiTag 集合
        this.tags=new Array();
        //接口url信息
        //存储SwaggerBootstrapUiApiInfo 集合
        this.paths=new Array();
        //全局参数,存放SwaggerBootstrapUiParameter集合
        this.globalParameters=new Array();
        //参数统计信息，存放SwaggerBootstrapUiPathCountDownLatch集合
        this.pathArrs=new Array();
        //权限信息
        this.securityArrs=new Array();
        //Models
        this.models=new Array();
        this.modelNames=new Array();

        //SwaggerBootstrapCacheGroupApis 对象的集合
        //add by xiaoyumin 2018-12-12 18:49:22
        this.groupId=md5(name);
        this.firstLoad=true;
        this.groupApis=new Array();
    }

    /***
     *
     * [{
     *  id:"md5(groupName)",
     *  groupApis:["id1","id2"]
     * }]
     * @constructor
     */
    var SwaggerBootstrapUiCacheApis=function () {
        this.id="";
        //缓存api-id 对象的集合
        this.cacheApis=new Array();
    }




    /***
     * 计数器
     * @constructor
     */
    var SwaggerBootstrapUiPathCountDownLatch=function () {
        this.method="";
        this.count=0;
    }
    
    /***
     * 返回对象解析属性
     * @constructor
     */
    var SwaggerBootstrapUiDefinition=function () {
        //类型名称
        this.name="";
        //介绍
        this.description="";
        //类型
        this.type="";
        //属性 --SwaggerBootstrapUiProperty 集合
        this.properties=new Array();
        this.value=null;
        //add by xiaoymin 2018-8-1 13:35:32
        this.required=new Array();
        this.title="";
    }
    /**
     * 权限验证
     * @constructor
     */
    var SwaggerBootstrapUiSecurityDefinition=function () {
        this.key="";
        this.type="";
        this.in="";
        this.name="";
        this.value="";
    }

    /***
     * definition对象属性
     * @constructor
     */
    var SwaggerBootstrapUiProperty=function () {
        //默认基本类型,非引用
        this.basic=true;
        this.name="";
        this.type="";
        this.refType=null;
        this.description="";
        this.example="";
        this.format="";
        //是否必须
        this.required=false;
        //默认值
        this.value=null;
        //引用类
        this.property=null;
        //原始参数
        this.originProperty=null;
    }
    /***
     * swagger的tag标签
     * @param name
     * @param description
     * @constructor
     */
    var SwaggerBootstrapUiTag=function (name, description) {
        this.name=name;
        this.description=description;
        this.childrens=new Array();
        //是否有新接口
        this.hasNew=false;
    }
    /***
     * Swagger接口基础信息
     * @constructor
     */
    var SwaggerBootstrapUiApiInfo=function () {
        this.url=null;
        this.originalUrl=null;
        this.showUrl="";
        this.basePathFlag=false;
        this.methodType=null;
        this.description=null;
        this.summary=null;
        this.consumes=null;
        this.operationId=null;
        this.produces=null;
        this.tags=null;
        //默认请求contentType
        this.contentType="application/json";
        this.contentShowValue="JSON(application/json)";
        //显示参数
        //存储请求类型，form|row|urlencode
        this.contentValue="raw";
        this.parameters=new Array();
        //请求json示例
        this.requestValue=null;
        //针对parameter属性有引用类型的参数,继续以table 的形式展现
        //存放SwaggerBootstrapUiRefParameter 集合
        this.refparameters=new Array();
        //treetable组件使用对象
        this.refTreetableparameters=new Array();
        this.responseCodes=new Array();
        this.responseValue=null;
        this.responseJson=null;
        this.responseText=null;
        this.responseBasicType=false;
        //响应字段说明
        this.responseParameters=new Array();
        this.responseParameterRefName="";
        this.responseRefParameters=new Array();
        //treetable组件使用对象
        this.responseTreetableRefParameters=new Array();
        //新增菜单id
        this.id="";
        //排序
        this.order=2147483647;
        //add by xiaoymin 2018-12-14 17:04:42
        //是否新接口
        this.hasNew=false;
        //是否过时
        this.deprecated=false;
        //是否存在响应状态码中  存在多个schema的情况
        this.multipartResponseSchema=false;
        this.multipartResponseSchemaCount=0;

    }

    var SwaggerBootstrapUiRefParameter=function () {
        this.name=null;
        //存放SwaggerBootstrapUiParameter集合
        this.params=new Array();
    }

    var SwaggerBootstrapUiTreeTableRefParameter=function () {
        this.id="";
        this.name=null;
        //存放SwaggerBootstrapUiParameter集合
        this.params=new Array();
        this.level=1;
        this.childrenTypes=new Array();


    }

    /***
     * Swagger请求参数
     * @constructor
     */
    var SwaggerBootstrapUiParameter=function () {
        this.name=null;
        this.require=null;
        this.type=null;
        this.in=null;
        this.schema=false;
        this.schemaValue=null;
        this.value=null;
        //JSR-303 annotations supports since 1.8.7
        //默认状态为false
        this.validateStatus=false;
        this.validateInstance=null;
        //引用类
        this.def=null;
        //des
        this.description=null;
        //文本框值
        this.txtValue=null;
        //枚举类型
        this.enum=null;

        this.id="param"+Math.round(Math.random()*1000000);
        this.pid="-1";

        this.level=1;
        //参数是否显示在debug中
        this.show=true;

        this.childrenTypes=new Array();
        this.parentTypes=new Array();
    }

    var SwaggerBootstrapUiParameterLevel=function () {
        this.level=1;

    }

    var isObject = function (item) {
        return (item && typeof item === 'object' && !Array.isArray(item));
    }

    var getKeyDescriptions = function(target, that) {
        var keyList = {};
        if (typeof(target) == 'object') {
            if (Array.isArray(target)) {
                for (var index in target) {
                    var objc = target[index];
                    if (typeof(objc) == 'object') {
                        var key = objc.name;
                        var keyListTemp;
                        keyList[key] = objc.description;
                        if (objc.schemaValue || objc.refType) {
                            var def=that.getDefinitionByName(objc.schemaValue || objc.refType);
                            if (def) {
                                if (def.properties) {
                                    keyListTemp = getKeyDescriptions(def.properties, that);
                                }
                            }
                        } else if (objc.params) {
                            keyListTemp = getKeyDescriptions(objc.params, that);
                        }
                        if (keyListTemp) {
                            for (var j in keyListTemp) {
                                keyList[key + ">" + j ] = keyListTemp[j];
                            }
                        }
                    }
                }
            }
        }
        return keyList;
    }

    var appendDescriptionVariable = function($aceJsonContent, responseCode, that) {
        var paths = [];
        $aceJsonText = $aceJsonContent.find('.ace_text-layer');
        var acePrintMarginLeft = $aceJsonContent.find('.ace_print-margin').css('left');
        $aceJsonText.children('.ace_line').each(function(i,item){
            var $variable = $(item).children('.ace_variable');
            var key;
            if ($variable.length) {
                key = $variable.text().replace(/^"(.*)"$/g,'$1');
                $('<span>'+responseCode.responseDescriptionFind(paths, key, that)+'</span>')
                    .css({'position':'absolute', 'left':acePrintMarginLeft, 'color':'#8c8c8c'})
                    .appendTo($(item));
            }
            switch($(item).children('.ace_paren').text()) {
                case '[':
                case '{':
                    paths.push(key?key:0);
                    break;
                case '}':
                case ']':
                    paths.pop();
                    break;
            }

        });
    }
    /***
     * 响应码
     * @constructor
     */
    var SwaggerBootstrapUiResponseCode=function () {
        this.code=null;
        this.description=null;
        this.schema=null;
        //treetable组件使用对象
        this.refTreetableparameters=new Array();
        this.responseCodes=new Array();
        this.responseValue=null;
        this.responseJson=null;
        this.responseText=null;
        this.responseBasicType=false;
        //响应字段说明
        this.responseParameters=new Array();
        this.responseParameterRefName="";
        this.responseRefParameters=new Array();
        //treetable组件使用对象
        this.responseTreetableRefParameters=new Array();
        this.responseDescriptionFind = function(paths, key, that) {
            if (!this.responseDescriptions) {
                this.responseDescriptions = getKeyDescriptions(this.responseParameters, that);
            }
            var path = paths.join('>') + '>' + key;
            path = path.replace(/0>/g,'');
            if (this.responseDescriptions && this.responseDescriptions[path]) {
                return this.responseDescriptions[path];
            }
            return '';
        }
    }

    /***
     * 公共方法
     */
    $.extend({
        getJsonKeyLength:function (json) {
          var size=0;
          if (json!=null){
              for (key in json) {
                  if (json.hasOwnProperty(key)) size++;
              }
          }
          return size;
        },
        regexMatchStr:function (regex,str) {
            var flag=false;
            if(regex!=null&&regex!=undefined&&str!=null&&str!=undefined){
                var matchResult=str.match(regex);
                if (matchResult!=null){
                    flag=true;
                }
            }
            return flag;
        },
        checkUndefined:function (obj) {
            var flag=false;
            if(obj!=null&&typeof (obj)!="undefined"){
                flag=true;
            }
            return flag;
        },
        propValue:function (key, obj, defaultValue) {
            var t=defaultValue;
            if(obj.hasOwnProperty(key)){
                t=obj[key];
            }
            return t;
        },
        checkIsBasicType:function(type) {
            var basicTypes=["string","integer","number","object","boolean","int32","int64","float","double"];
            var flag=false;
            if(type!=null){
                if($.inArray(type,basicTypes)>-1){
                    flag=true;
                }
            }
            return flag;
        },
        getBasicTypeValue:function(type) {
            var propValue="";
            //是否是基本类型
            if(type=="integer"){
                propValue=0;
            }
            if(type=="boolean"){
                propValue=true;
            }
            if(type=="object"){
                propValue={};
            }
            if(type=="number"){
                propValue=parseFloat(0);
            }
            return propValue;
        },
        getValue:function (obj,key,defaultValue,checkEmpty) {
            var val=defaultValue;
            if(obj!=null&&obj!=undefined){
                if (obj.hasOwnProperty(key)){
                    val=obj[key];
                    if (checkEmpty){
                        if (val==null||val==""){
                            val=defaultValue;
                        }
                    }
                }
            }
            return val;
        },
        getClassName:function (item) {
            var regex=new RegExp("#/definitions/(.*)$","ig");
            if(regex.test(item)) {
                var ptype=RegExp.$1;
                return ptype;
            }
            return null;
        },
        getStringValue:function (obj) {
            var str="";
            if(typeof (obj)!='undefined'&&obj!=null){
                str=obj.toString();
            }
            return str;
        },
        randomNumber:function() {
            return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
        },
        htmlEncode:function (html) {
            if (html !== null) {
                return html.toString().replace(/&/g, "&amp;").replace(/"/g, "&quot;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
            } else {
                return '';
            }
        },
        jsString:function (s) {
            s = JSON.stringify(s).slice(1, -1);
            return $.htmlEncode(s);
        },
        replaceMultipLineStr:function (str) {
            if (str!=null&&str!=undefined&&str!=""){
                var newLinePattern = /(\r\n|\n\r|\r|\n)/g;
                if (newLinePattern.test(str)) {
                    var newDes = str.replace(newLinePattern, '\\n');
                    return newDes;
                }
                return str;
            }
            return "";
        },
        generUUID:function () {
            return ($.randomNumber()+$.randomNumber()+"-"+$.randomNumber()+"-"+$.randomNumber()+"-"+$.randomNumber()+"-"+$.randomNumber()+$.randomNumber()+$.randomNumber());
        },
        base64Encode:function (str) {
            var CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
            var out = "", i = 0, len = str.length, c1, c2, c3;
            while (i < len) {
                c1 = str.charCodeAt(i++) & 0xff;
                if (i == len) {
                    out += CHARS.charAt(c1 >> 2);
                    out += CHARS.charAt((c1 & 0x3) << 4);
                    out += "==";
                    break;
                }
                c2 = str.charCodeAt(i++);
                if (i == len) {
                    out += CHARS.charAt(c1 >> 2);
                    out += CHARS.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
                    out += CHARS.charAt((c2 & 0xF) << 2);
                    out += "=";
                    break;
                }
                c3 = str.charCodeAt(i++);
                out += CHARS.charAt(c1 >> 2);
                out += CHARS.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
                out += CHARS.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >> 6));
                out += CHARS.charAt(c3 & 0x3F);
            }
            return out;
        },
        binToBase64:function (bitString) {
            var code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".split(""); //索引表
            var result = "";
            var tail = bitString.length % 6;
            var bitStringTemp1 = bitString.substr(0, bitString.length - tail);
            var bitStringTemp2 = bitString.substr(bitString.length - tail, tail);
            for (var i = 0; i < bitStringTemp1.length; i += 6) {
                var index = parseInt(bitStringTemp1.substr(i, 6), 2);
                result += code[index];
            }
            bitStringTemp2 += new Array(7 - tail).join("0");
            if (tail) {
                result += code[parseInt(bitStringTemp2, 2)];
                result += new Array((6 - tail) / 2 + 1).join("=");
            }
            return result;
        }
    });

    String.prototype.gblen = function() {
        var len = 0;
        for (var i=0; i<this.length; i++) {
            if (this.charCodeAt(i)>127 || this.charCodeAt(i)==94) {
                len += 2;
            } else {
                len ++;
            }
        }
        return len;
    }

    String.prototype.startWith=function(str){
        var reg=new RegExp("^"+str);
        return reg.test(this);
    }


    window.SwaggerBootstrapUi=SwaggerBootstrapUi;


})(jQuery)