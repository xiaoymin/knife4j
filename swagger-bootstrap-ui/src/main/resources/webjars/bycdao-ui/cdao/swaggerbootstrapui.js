/***
 * create by xiaoymin on 2018-7-4 15:32:07
 * 重构swagger-bootstrap-ui组件,为以后动态扩展更高效,扩展接口打下基础
 */
(function ($) {

    var SwaggerBootstrapUi=function () {
        //swagger请求api地址
        this.url="swagger-resources";
        //文档id
        this.docId="content";
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
    }
    /***
     * swagger-bootstrap-ui的main方法,初始化文档所有功能,类似于SpringBoot的main方法
     */
    SwaggerBootstrapUi.prototype.main=function () {
        var that=this;
        that.initWindowWidthAndHeight();

        that.windowResize();
        //加载分组接口
        that.analysisGroup();
        //创建分组元素
        that.createGroupElement();
        //搜索
        that.searchEvents();

    }
    /***
     * 搜索按钮事件
     */
    SwaggerBootstrapUi.prototype.searchEvents=function () {
       var that=this;
       that.log("searchEvents");
       that.log($("#"+that.searchEleId));
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
        //设置doc.html文档的title属性
        if(title!=null&&title!=undefined&&title!=""){
            $("title").html(title);
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
            //这里判断url请求是否已加载过
            //防止出现根路径的情况
            var idx=api.indexOf("/");
            if(idx==0){
                api=api.substr(1);
            }
            that.log("截取后的url:"+api);
            /*api="/webjars/bycdao-ui/cdao/d1.json";
            that.log("截取后的url:"+api);*/
            $.ajax({
                //url:"v2/api-docs",
                url:api,
                dataType:"json",
                type:"get",
                async:false,
                success:function (data) {
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
        //全局参数菜单功能
        var globalArgsLi=$("<li  class=\"detailMenu\"><a href=\"javascript:void(0)\"><i class=\"icon-text-width iconfont icon-zhongduancanshuguanli\"></i><span class=\"menu-text\"> 全局参数设置 </span></a></li>");
        globalArgsLi.on("click",function () {
            that.getMenu().find("li").removeClass("active");
            globalArgsLi.addClass("active");
            that.createGlobalParametersElement();
        })
        that.getMenu().append(globalArgsLi);

        //离线文档功能
        var mddocli=$("<li  class=\"detailMenu\"><a href=\"javascript:void(0)\"><i class=\"icon-text-width iconfont icon-iconset0118\"></i><span class=\"menu-text\"> 离线文档(MD) </span></a></li>");
        mddocli.on("click",function () {
            that.log("离线文档功能click")
            that.createMarkdownTab();
            that.getMenu().find("li").removeClass("active");
            mddocli.addClass("active");
        })
        that.getMenu().append(mddocli);

        $.each(that.currentInstance.tags,function (i, tag) {
            var len=tag.childrens.length;
            if(len==0){
                var li=$('<li class="detailMenu"><a href="javascript:void(0)"><i class="icon-text-width iconfont icon-APIwendang"></i><span class="menu-text"> '+tag.name+' </span></a></li>');
                that.getMenu().append(li);
            }else{
                //存在子标签
                var li=$('<li  class="detailMenu"></li>');
                var titleA=$('<a href="#" class="dropdown-toggle"><i class="icon-file-alt icon-text-width iconfont icon-APIwendang"></i><span class="menu-text"> '+tag.name+'<span class="badge badge-primary ">'+len+'</span></span><b class="arrow icon-angle-down"></b></a>');
                li.append(titleA);
                //循环树
                var ul=$('<ul class="submenu"></ul>')
                $.each(tag.childrens,function (i, children) {
                    var childrenLi=$('<li class="menuLi" ><div class="mhed"><div class="swu-hei"><span class="swu-menu swu-left"><span class="menu-url-'+children.methodType.toLowerCase()+'">'+children.methodType.toUpperCase()+'</span></span><span class="swu-menu swu-left"><span class="menu-url">'+children.showUrl+'</span></span></div><div class="swu-menu-api-des">'+children.summary+'</div></div></li>');
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
     * 创建全局参数
     */
    SwaggerBootstrapUi.prototype.createGlobalParametersElement=function () {
        var that=this;
        that.log(that.currentInstance)
        //内容覆盖
        that.getDoc().html("");
        setTimeout(function () {
            var html = template('GlobalParamScript', that.currentInstance);
            that.getDoc().html(html);

            that.log("注册btnAddParam-click事件")
            that.log(that.getDoc().find("#btnAddParam"))
            //初始化添加按钮click事件
            that.getDoc().find("#btnAddParam").on("click",function (e) {
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
                        that.currentInstance.globalParameters.push(globalParameterInstance);
                    }else{
                        //存在,更新该参数的值
                        that.updateGlobalParams(globalParameterInstance);
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
            that.getDoc().find(".btn-save").on("click",function (e) {
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
                    that.currentInstance.globalParameters.push(globalParameterInstance);
                }else{
                    //存在,更新该参数的值
                    that.updateGlobalParams(globalParameterInstance);
                }
                that.log("目前全局参数..")
                that.log(that.currentInstance.globalParameters);
                layer.msg("保存成功")
            })
            //全局取消事件
            that.getDoc().find(".btn-cancel").on("click",function (e) {
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

        },100)


    }
    /***
     * 判断全局参数是否存在
     * @param param
     */
    SwaggerBootstrapUi.prototype.checkGlobalParamExists=function (param) {
        var that=this;
        var flag=false;
        $.each(that.currentInstance.globalParameters,function (i, gp) {
            if(gp.name==param.name){
                flag=true;
            }
        })
        return flag;
    }
    SwaggerBootstrapUi.prototype.updateGlobalParams=function (param) {
        var that=this;
        $.each(that.currentInstance.globalParameters,function (i, gp) {
            if(gp.name==param.name){
                gp.in=param.in;
                gp.value=param.value;
                gp.txtValue=param.value;
            }
        })
    }
    /***
     * 根据名称删除全局参数数组
     * @param name
     */
    SwaggerBootstrapUi.prototype.deleteGlobalParamsByName=function (name) {
        var that=this;
        for(var i=0;i<that.currentInstance.globalParameters.length;i++){
            var gp=that.currentInstance.globalParameters[i];
            if (gp.name==name){
                that.currentInstance.globalParameters.splice(i,1);
            }
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
        that.createTabElement();
        //查找接口doc
        that.getDoc().find("#tab1").find(".panel-body").html("")
        setTimeout(function () {
            var html = template('contentScript', apiInfo);
            that.getDoc().find("#tab1").find(".panel-body").html(html)
            that.markdownDocInit();
            //初始化apiInfo响应数据
            that.log("初始化apiInfo响应数据")
            that.log(apiInfo)
            if(apiInfo.responseJson!=null){
                $(".language-json:first").JSONView(apiInfo.responseJson);
            }
        },100)
        that.log(that.currentInstance);
        //实现复制文档功能
        //初始化copy按钮功能
        var clipboard = new ClipboardJS('#copyDocHref',{
            text:function () {
                return $("#docText").val();
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
        //赋值全局参数
        apiInfo.globalParameters=that.currentInstance.globalParameters;
        that.getDoc().find("#tab2").find(".panel-body").html("");
        var html = template('DebugScript', apiInfo);
        that.getDoc().find("#tab2").find(".panel-body").html(html);
        //绑定全选事件
        $("#parameterCheckAll").on("click",function (e) {
            var chk=$(this);
            that.log("是否选中...")
            var chked=chk.find("input:first").prop("checked");
            that.log(chked)
            $("#paramBody").find("input:checkbox").prop("checked",chked);
        });
        //如果有文件上传,绑定多文件增加按钮事件

        that.requestSend(apiInfo,menu);

    }

    /***
     * 发送请求
     * @constructor
     */
    SwaggerBootstrapUi.prototype.requestSend=function (apiInfo,eleObject) {
        var that=this;
        that.log("发送之前...")
        that.log(apiInfo)
        var btnRequest=that.getDoc().find("#tab2").find(".panel-body").find("#btnRequest");
        var respcleanDiv=that.getDoc().find("#tab2").find(".panel-body").find("#responsebody");
        btnRequest.on("click",function (e) {
            e.preventDefault();

            respcleanDiv.html("")
            var params={};
            var headerparams={};
            var bodyparams="";
            //modify by xiaoyumin 2017-8-9 11:28:16
            //增加表单验证
            var validateflag=false;
            var validateobj={};

            //获取参数
            var paramBody=that.getDoc().find("#tab2").find("#paramBody")
            that.log("paramsbody..")
            that.log(paramBody)
            //获取url
            var url=$("#txtreqUrl").val();
            if(url==null||url==""){
                layer.msg("请求url地址不能为空");
                return false;
            }
            var bodyRequest=false;
            //构建一个formdata对象,发送表单
            var formData=new FormData();
            var formCurlParams={};
            var fileUploadFlat=false;
            paramBody.find("tr").each(function () {
                var paramtr=$(this);
                var cked=paramtr.find("td:first").find(":checked").prop("checked");
                that.log(cked)
                if (cked){
                    //如果选中
                    var trdata={name:paramtr.data("name"),in:paramtr.data("in"),required:paramtr.data("required"),type:paramtr.data("type"),emflag:paramtr.data("emflag"),schemavalue:paramtr.data("schemavalue")};
                    that.log("trdata....")
                    that.log(trdata);
                    //获取key
                    //var key=paramtr.find("td:eq(1)").find("input").val();
                    var key=trdata["name"];
                    //获取value
                    var value="";
                    if(trdata["in"]=="body") {
                        //这里需要判断schema
                        //直接判断那类型
                        if (trdata.schemavalue == "MultipartFile") {
                            value = paramtr.find("td:eq(2)").find("input").val();
                            var fileEle = paramtr.find("td:eq(2)").find("input")[0];
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
                            value = paramtr.find("td:eq(2)").find("textarea").val();
                            formData.append(key, value);
                        }
                        that.updateRequestParameter(trdata.name, value, apiInfo);
                    }else if(trdata["in"]=="formData"){
                        //直接判断那类型
                        if (trdata.schemavalue == "MultipartFile") {
                            value = paramtr.find("td:eq(2)").find("input").val();
                            var fileEle = paramtr.find("td:eq(2)").find("input")[0];
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
                            value = paramtr.find("td:eq(2)").find("textarea").val();
                            formData.append(key, value);
                        }
                        that.updateRequestParameter(trdata.name, value, apiInfo);
                    }else{
                        if(trdata.emflag){
                            value=paramtr.find("td:eq(2)").find("select option:selected").val();
                            that.updateRequestParameter(trdata.name,value,apiInfo);
                            formData.append(key,value);
                        }else{
                            value=paramtr.find("td:eq(2)").find("input").val();
                            that.updateRequestParameter(trdata.name,value,apiInfo);
                            formData.append(key,value);
                        }
                    }

                    if(apiInfo.methodType=="delete"){
                        //判断是否是path参数
                        if(trdata["in"]=="path"){
                            url=url.replace("{"+key+"}",value);
                            apiInfo.url=url;
                        }else{
                            if (url.indexOf("?")>-1){
                                url=url+"&"+key+"="+value;
                            }else{
                                url+="?"+key+"="+value;
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
                                        params[key]=value;
                                    }
                                }
                            }
                        }
                    }
                    //判断是否required
                    if (trdata.hasOwnProperty("required")){
                        var required=trdata["required"];
                        if (required){
                            //必须,验证value是否为空
                            if(value==null||value==""){
                                validateflag=true;
                                var des=trdata["name"]
                                validateobj={message:des+"不能为空"};
                                return false;
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
            var contType="application/json; charset=UTF-8";
            var paramBodyType="json";
            if(bodyRequest){
                reqdata=bodyparams;
            }else{
                if(fileUploadFlat){
                    contType="multipart/form-data";
                    paramBodyType="form-data";
                    reqdata=params;
                }else{
                    paramBodyType="form";
                    reqdata=params;
                    contType="application/x-www-form-urlencoded; charset=UTF-8";
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
            var form=$("#uploadForm");
            var startTime=new Date().getTime();
            var index = layer.load(1);
            if(form.length>0){
                that.log("form submit------------------------------------------------")
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
                //headerparams["Content-Type"]=contType;
                that.log(headerparams)
                that.log(reqdata);
                that.log("formData-------------------------------------------------------");
                that.log(formData);
                axios.request({
                    url:url,
                    headers:headerparams,
                    method:$.getStringValue(apiInfo.methodType),
                    data:formData,
                    timeout: 10*60*1000,
                }).then(function (response) {
                    that.log("form-------------response------------------")
                    that.log(response);
                    var data=response.data;
                    var xhr=response.request;
                    that.log(data);
                    layer.close(index);
                    var statsCode=xhr.status;
                    if(statsCode==200){
                        statsCode=statsCode+" OK";
                    }
                    var endTime=new Date().getTime();
                    that.log(endTime)
                    var len=0;
                    var diff=endTime-startTime;
                    var tp=typeof (data);
                    if(xhr.hasOwnProperty("responseText")){
                        len=xhr["responseText"].gblen();
                    }
                    that.log(typeof (data))
                    that.log(status)
                    var resptab=$('<div id="resptab" class="tabs-container" ></div>');
                    var ulresp=$('<ul class="nav nav-tabs">' +
                        '<li class=""><a data-toggle="tab" href="#tabresp" aria-expanded="false"> 响应内容 </a></li>' +
                        '<li class=""><a data-toggle="tab" href="#tabraw" aria-expanded="false"> Raw </a></li>' +
                        '<li class=""><a data-toggle="tab" href="#tabcookie" aria-expanded="true"> Cookies</a></li>' +
                        '<li class=""><a data-toggle="tab" href="#tabheader" aria-expanded="true"> Headers </a></li>'+
                        '<li class=""><a data-toggle="tab" href="#tabcurl" aria-expanded="true"> Curl </a></li></ul>')


                    var uldiv=$("<div></div>");
                    uldiv.append(ulresp);

                    //添加响应码div
                    var respcodeDiv=$("<div style='right: 30px;position: absolute;margin-top: -40px;'><span class='debug-span-label'>响应码:</span><span class='debug-span-value'>"+statsCode+"</span>  " +
                        "&nbsp;&nbsp;&nbsp;&nbsp;<span class='debug-span-label'>耗时:</span><span class='debug-span-value'>"+diff+" ms</span>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;<span class='debug-span-label'>大小:</span><span class='debug-span-value'>"+len+" b</span></div>");
                    uldiv.append(respcodeDiv);

                    resptab.append(uldiv);
                    var respcontent=$('<div class="tab-content"></div>');

                    var resp1=$('<div id="tabresp" class="tab-pane active"><div class="panel-body"><pre></pre></div></div>');
                    var resp2=$('<div id="tabcookie" class="tab-pane"><div class="panel-body">暂无</div>');
                    var resp4=$('<div id="tabraw" class="tab-pane"><div class="panel-body" style="word-wrap: break-word;">暂无</div>');
                    var resp3=$('<div id="tabheader" class="tab-pane"><div class="panel-body">暂无</div></div>');
                    var resp5=$('<div id="tabcurl" class="tab-pane"><div class="panel-body" style="word-wrap: break-word;">暂无</div>');
                    respcontent.append(resp1).append(resp2).append(resp3).append(resp4).append(resp5);
                    resptab.append(respcontent)
                    respcleanDiv.append(resptab);
                    that.log(xhr);
                    var allheaders=response.headers;
                    if(allheaders!=null&&typeof (allheaders)!='undefined'&&allheaders!=""){
                        var headertable=$('<table class="table table-hover table-bordered table-text-center"><tr><th>请求头</th><th>value</th></tr></table>');
                        for(var hk in allheaders){
                            var headertr=$('<tr><th class="active">'+hk+'</th><td>'+allheaders[hk]+'</td></tr>');
                            headertable.append(headertr);
                        }
                        //设置Headers内容
                        resp3.find(".panel-body").html("")
                        resp3.find(".panel-body").append(headertable);
                    }
                    that.log("responseTextFlag-----------------")
                    var rtext=xhr["responseText"];
                    that.log(xhr.hasOwnProperty("responseText"));
                    that.log(rtext);
                    if(rtext!=null&&rtext!=undefined){
                        that.log("xhr---------------responseText-----------------------------")
                        that.log(xhr["responseText"])
                        //json
                        resp4.find(".panel-body").html(rtext);
                        if(tp=="string"){
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
                                resp1.find(".panel-body").html("")
                                resp1.find(".panel-body").html(div);
                            }
                        }


                    }
                    if (response.data!=null&&response.data!=undefined){
                        //如果存在该对象,服务端返回为json格式
                        resp1.find(".panel-body").html("")
                        that.log(xhr["responseJSON"])
                        var pre=$('<pre></pre>')
                        var jsondiv=$('<div></div>')
                        jsondiv.JSONView(response.data);
                        pre.html(JSON.stringify(response.data,null,2));
                        resp1.find(".panel-body").append(jsondiv);
                    }
                    that.log("tab show...")
                    //组件curl功能
                    var curl=that.buildCurl(apiInfo,headerparams,reqdata,paramBodyType,url,formCurlParams);
                    var cpcurlBotton=$("<button class='btn btn-default btn-primary' id='btnCopyCurl'>复制</button>");
                    var curlcode=$("<code></code>");
                    curlcode.html(curl);

                    resp5.find(".panel-body").html("");
                    resp5.find(".panel-body").append(curlcode).append(cpcurlBotton);


                    var clipboard = new ClipboardJS('#btnCopyCurl',{
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

                    resptab.find("a:first").tab("show");
                }).catch(function (error) {
                    layer.close(index);
                    that.log("form-------------error-------------------");
                    that.log(error);
                    that.log(error.response);
                    if(error.response){
                        var response=error.response;
                        var data=response.data;
                        var xhr=response.request;
                        that.log(data);
                        layer.close(index);
                        var statsCode=xhr.status;
                        if(statsCode==200){
                            statsCode=statsCode+" OK";
                        }
                        var endTime=new Date().getTime();
                        that.log(endTime)
                        var len=0;
                        var diff=endTime-startTime;
                        var tp=typeof (data);
                        if(xhr.hasOwnProperty("responseText")){
                            len=xhr["responseText"].gblen();
                        }
                        that.log(typeof (data))
                        that.log(status)
                        var resptab=$('<div id="resptab" class="tabs-container" ></div>');
                        var ulresp=$('<ul class="nav nav-tabs">' +
                            '<li class=""><a data-toggle="tab" href="#tabresp" aria-expanded="false"> 响应内容 </a></li>' +
                            '<li class=""><a data-toggle="tab" href="#tabraw" aria-expanded="false"> Raw </a></li>' +
                            '<li class=""><a data-toggle="tab" href="#tabcookie" aria-expanded="true"> Cookies</a></li>' +
                            '<li class=""><a data-toggle="tab" href="#tabheader" aria-expanded="true"> Headers </a></li>'+
                            '<li class=""><a data-toggle="tab" href="#tabcurl" aria-expanded="true"> Curl </a></li></ul>')


                        var uldiv=$("<div></div>");
                        uldiv.append(ulresp);

                        //添加响应码div
                        var respcodeDiv=$("<div style='right: 30px;position: absolute;margin-top: -40px;'><span class='debug-span-label'>响应码:</span><span class='debug-span-value'>"+statsCode+"</span>  " +
                            "&nbsp;&nbsp;&nbsp;&nbsp;<span class='debug-span-label'>耗时:</span><span class='debug-span-value'>"+diff+" ms</span>" +
                            "&nbsp;&nbsp;&nbsp;&nbsp;<span class='debug-span-label'>大小:</span><span class='debug-span-value'>"+len+" b</span></div>");
                        uldiv.append(respcodeDiv);

                        resptab.append(uldiv);
                        var respcontent=$('<div class="tab-content"></div>');

                        var resp1=$('<div id="tabresp" class="tab-pane active"><div class="panel-body"><pre></pre></div></div>');
                        var resp2=$('<div id="tabcookie" class="tab-pane"><div class="panel-body">暂无</div>');
                        var resp4=$('<div id="tabraw" class="tab-pane"><div class="panel-body" style="word-wrap: break-word;">暂无</div>');
                        var resp3=$('<div id="tabheader" class="tab-pane"><div class="panel-body">暂无</div></div>');
                        var resp5=$('<div id="tabcurl" class="tab-pane"><div class="panel-body" style="word-wrap: break-word;">暂无</div>');
                        respcontent.append(resp1).append(resp2).append(resp3).append(resp4).append(resp5);
                        resptab.append(respcontent)
                        respcleanDiv.append(resptab);
                        that.log(xhr);
                        var allheaders=response.headers;
                        if(allheaders!=null&&typeof (allheaders)!='undefined'&&allheaders!=""){
                            var headertable=$('<table class="table table-hover table-bordered table-text-center"><tr><th>请求头</th><th>value</th></tr></table>');
                            for(var hk in allheaders){
                                var headertr=$('<tr><th class="active">'+hk+'</th><td>'+allheaders[hk]+'</td></tr>');
                                headertable.append(headertr);
                            }
                            //设置Headers内容
                            resp3.find(".panel-body").html("")
                            resp3.find(".panel-body").append(headertable);
                        }
                        that.log("responseTextFlag-----------------")
                        var rtext=xhr["responseText"];
                        that.log(xhr.hasOwnProperty("responseText"));
                        that.log(rtext);
                        if(rtext!=null&&rtext!=undefined){
                            that.log("xhr---------------responseText-----------------------------")
                            that.log(xhr["responseText"])
                            //json
                            resp4.find(".panel-body").html(rtext);
                            if(tp=="string"){
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
                                    resp1.find(".panel-body").html("")
                                    resp1.find(".panel-body").html(div);
                                }
                            }
                        }
                        if (response.data!=null&&response.data!=undefined){
                            //如果存在该对象,服务端返回为json格式
                            resp1.find(".panel-body").html("")
                            that.log(xhr["responseJSON"])
                            var pre=$('<pre></pre>')
                            var jsondiv=$('<div></div>')
                            jsondiv.JSONView(response.data);
                            pre.html(JSON.stringify(response.data,null,2));
                            resp1.find(".panel-body").append(jsondiv);
                        }
                        that.log("tab show...")
                        //组件curl功能
                        var curl=that.buildCurl(apiInfo,headerparams,reqdata,paramBodyType,url,formCurlParams);
                        var cpcurlBotton=$("<button class='btn btn-default btn-primary' id='btnCopyCurl'>复制</button>");
                        var curlcode=$("<code></code>");
                        curlcode.html(curl);

                        resp5.find(".panel-body").html("");
                        resp5.find(".panel-body").append(curlcode).append(cpcurlBotton);


                        var clipboard = new ClipboardJS('#btnCopyCurl',{
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
                        resptab.find("a:first").tab("show");
                    }

                })

            }
            else{
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
                headerparams["Content-Type"]=contType;
                that.log("header....")
                that.log(headerparams);
                $.ajax({
                    url:url,
                    headers:headerparams,
                    type:$.getStringValue(apiInfo.methodType),
                    data:reqdata,
                    contentType:contType,
                    success:function (data,status,xhr) {
                        layer.close(index);
                        that.log("success...")
                        var statsCode=xhr.status;
                        if(statsCode==200){
                            statsCode=statsCode+" OK";
                        }
                        var endTime=new Date().getTime();
                        that.log(endTime)
                        var len=0;
                        var diff=endTime-startTime;
                        var tp=typeof (data);
                        if(xhr.hasOwnProperty("responseText")){
                            len=xhr["responseText"].gblen();
                        }
                        that.log(typeof (data))
                        that.log(status)
                        var resptab=$('<div id="resptab" class="tabs-container" ></div>');
                        var ulresp=$('<ul class="nav nav-tabs">' +
                            '<li class=""><a data-toggle="tab" href="#tabresp" aria-expanded="false"> 响应内容 </a></li>' +
                            '<li class=""><a data-toggle="tab" href="#tabraw" aria-expanded="false"> Raw </a></li>' +
                            '<li class=""><a data-toggle="tab" href="#tabcookie" aria-expanded="true"> Cookies</a></li>' +
                            '<li class=""><a data-toggle="tab" href="#tabheader" aria-expanded="true"> Headers </a></li>'+
                            '<li class=""><a data-toggle="tab" href="#tabcurl" aria-expanded="true"> Curl </a></li></ul>')


                        var uldiv=$("<div></div>");
                        uldiv.append(ulresp);

                        //添加响应码div
                        var respcodeDiv=$("<div style='right: 30px;position: absolute;margin-top: -40px;'><span class='debug-span-label'>响应码:</span><span class='debug-span-value'>"+statsCode+"</span>  " +
                            "&nbsp;&nbsp;&nbsp;&nbsp;<span class='debug-span-label'>耗时:</span><span class='debug-span-value'>"+diff+" ms</span>" +
                            "&nbsp;&nbsp;&nbsp;&nbsp;<span class='debug-span-label'>大小:</span><span class='debug-span-value'>"+len+" b</span></div>");
                        uldiv.append(respcodeDiv);

                        resptab.append(uldiv);
                        var respcontent=$('<div class="tab-content"></div>');

                        var resp1=$('<div id="tabresp" class="tab-pane active"><div class="panel-body"><pre></pre></div></div>');
                        var resp2=$('<div id="tabcookie" class="tab-pane active"><div class="panel-body">暂无</div>');
                        var resp4=$('<div id="tabraw" class="tab-pane active"><div class="panel-body" style="word-wrap: break-word;">暂无</div>');
                        var resp3=$('<div id="tabheader" class="tab-pane active"><div class="panel-body">暂无</div></div>');
                        var resp5=$('<div id="tabcurl" class="tab-pane active"><div class="panel-body" style="word-wrap: break-word;">暂无</div>');
                        respcontent.append(resp1).append(resp2).append(resp3).append(resp4).append(resp5);
                        resptab.append(respcontent)
                        respcleanDiv.append(resptab);
                        that.log(xhr);
                        that.log(xhr.getAllResponseHeaders());
                        var mimtype=xhr.overrideMimeType();
                        that.log("MIME-TYPE..")
                        that.log(mimtype)
                        var allheaders=xhr.getAllResponseHeaders();
                        if(allheaders!=null&&typeof (allheaders)!='undefined'&&allheaders!=""){
                            var headers=allheaders.split("\r\n");
                            var headertable=$('<table class="table table-hover table-bordered table-text-center"><tr><th>请求头</th><th>value</th></tr></table>');
                            for(var i=0;i<headers.length;i++){
                                var header=headers[i];
                                if(header!=null&&header!=""){
                                    var headerValu=header.split(":");
                                    var headertr=$('<tr><th class="active">'+headerValu[0]+'</th><td>'+headerValu[1]+'</td></tr>');
                                    headertable.append(headertr);
                                }
                            }
                            //设置Headers内容
                            resp3.find(".panel-body").html("")
                            resp3.find(".panel-body").append(headertable);
                        }
                        var contentType=xhr.getResponseHeader("Content-Type");
                        that.log("Content-Type:"+contentType);
                        that.log(xhr.hasOwnProperty("responseJSON"))
                        if(xhr.hasOwnProperty("responseText")){
                            //json
                            resp4.find(".panel-body").html(xhr["responseText"]);
                            if(tp=="string"){
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
                                    resp1.find(".panel-body").html("")
                                    resp1.find(".panel-body").html(div);
                                }
                            }


                        }
                        if (xhr.hasOwnProperty("responseJSON")){
                            //如果存在该对象,服务端返回为json格式
                            resp1.find(".panel-body").html("")
                            that.log(xhr["responseJSON"])
                            var pre=$('<pre></pre>')
                            var jsondiv=$('<div></div>')
                            jsondiv.JSONView(xhr["responseJSON"]);
                            pre.html(JSON.stringify(xhr["responseJSON"],null,2));
                            resp1.find(".panel-body").append(jsondiv);
                        }
                        that.log("tab show...")
                        //组件curl功能
                        var curl=that.buildCurl(apiInfo,headerparams,reqdata,paramBodyType,url);
                        var cpcurlBotton=$("<button class='btn btn-default btn-primary' id='btnCopyCurl'>复制</button>");
                        var curlcode=$("<code></code>");
                        curlcode.html(curl);

                        resp5.find(".panel-body").html("");
                        resp5.find(".panel-body").append(curlcode).append(cpcurlBotton);


                        var clipboard = new ClipboardJS('#btnCopyCurl',{
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

                        resptab.find("a:first").tab("show");
                    },
                    error:function (xhr, textStatus, errorThrown) {
                        that.log("error.....")
                        layer.close(index);
                        that.log(xhr);
                        var statsCode=xhr.status;
                        if(statsCode==404){
                            statsCode=statsCode+" Not Found";
                        }
                        var endTime=new Date().getTime();
                        that.log(endTime)

                        var diff=endTime-startTime;

                        var resptab=$('<div id="resptab" class="tabs-container" ></div>')
                        var ulresp=$('<ul class="nav nav-tabs">' +
                            '<li class=""><a data-toggle="tab" href="#tabresp" aria-expanded="false"> 响应内容 </a></li>' +
                            '<li class=""><a data-toggle="tab" href="#tabraw" aria-expanded="false"> Raw </a></li>' +
                            '<li class=""><a data-toggle="tab" href="#tabcookie" aria-expanded="true"> Cookies</a></li>' +
                            '<li class=""><a data-toggle="tab" href="#tabheader" aria-expanded="true"> Headers </a></li>'+
                            '<li class=""><a data-toggle="tab" href="#tabcurl" aria-expanded="true"> Curl </a></li></ul>')

                        //resptab.append(ulresp);
                        var uldiv=$("<div></div>");
                        uldiv.append(ulresp);
                        var len=0;
                        var rawTxt="暂无";
                        if (xhr.hasOwnProperty("responseText")){
                            len=xhr["responseText"].toString().gblen();
                            rawTxt=xhr["responseText"];

                        }
                        //添加响应码div
                        var respcodeDiv=$("<div style='right: 30px;position: absolute;margin-top: -40px;'><span class='debug-span-label'>响应码:</span><span class='debug-span-value'>"+statsCode+"</span>  " +
                            "&nbsp;&nbsp;&nbsp;&nbsp;<span class='debug-span-label'>耗时:</span><span class='debug-span-value'>"+diff+" ms</span>" +
                            "&nbsp;&nbsp;&nbsp;&nbsp;<span class='debug-span-label'>大小:</span><span class='debug-span-value'>"+len+" b</span></div>");
                        uldiv.append(respcodeDiv);

                        resptab.append(uldiv);


                        var respcontent=$('<div class="tab-content"></div>');

                        var resp1=$('<div id="tabresp" class="tab-pane active"><div class="panel-body"><pre></pre></div></div>');
                        var resp2=$('<div id="tabcookie" class="tab-pane active"><div class="panel-body">暂无</div>');
                        var resp3=$('<div id="tabheader" class="tab-pane active"><div class="panel-body">暂无</div></div>');
                        var resp4=$('<div id="tabraw" class="tab-pane active"><div class="panel-body" style="word-wrap: break-word;">'+rawTxt+'</div>');
                        var resp5=$('<div id="tabcurl" class="tab-pane active"><div class="panel-body" style="word-wrap: break-word;">暂无</div>');
                        respcontent.append(resp1).append(resp2).append(resp3).append(resp4).append(resp5);

                        resptab.append(respcontent)

                        respcleanDiv.append(resptab);
                        that.log(xhr);
                        var allheaders=xhr.getAllResponseHeaders();
                        if(allheaders!=null&&typeof (allheaders)!='undefined'&&allheaders!=""){
                            that.log("header--------------tab--------------------")
                            that.log(allheaders)
                            var headers=allheaders.split("\r\n");
                            that.log("headers------------------------")
                            that.log(headers)
                            var headertable=$('<table class="table table-hover table-bordered table-text-center"><tr><th>请求头</th><th>value</th></tr></table>');
                            for(var i=0;i<headers.length;i++){
                                var header=headers[i];
                                if(header!=null&&header!=""){
                                    var headerValu=header.split(":");
                                    var headertr=$('<tr><th class="active">'+headerValu[0]+'</th><td>'+headerValu[1]+'</td></tr>');
                                    headertable.append(headertr);
                                }
                            }
                            that.log(headertable)
                            //设置Headers内容
                            resp3.find(".panel-body").html("")
                            resp3.find(".panel-body").append(headertable);
                        }
                        var contentType=xhr.getResponseHeader("Content-Type");
                        that.log("Content-Type:"+contentType);
                        var jsonRegex="";
                        that.log(xhr.hasOwnProperty("responseJSON"))
                        if (xhr.hasOwnProperty("responseJSON")){
                            //如果存在该对象,服务端返回为json格式
                            resp1.find(".panel-body").html("")
                            that.log(xhr["responseJSON"])
                            var jsondiv=$('<div></div>')
                            jsondiv.JSONView(xhr["responseJSON"]);
                            resp1.find(".panel-body").append(jsondiv);
                        }else{
                            //判断是否是text
                            var regex=new RegExp('.*?text.*','g');
                            if(regex.test(contentType)){
                                resp1.find(".panel-body").html("")
                                resp1.find(".panel-body").html(xhr.responseText);
                            }
                        }

                        //组件curl功能
                        var curl=that.buildCurl(apiInfo,headerparams,reqdata,paramBodyType,url);
                        var cpcurlBotton=$("<button class='btn btn-default btn-primary' id='btnCopyCurl'>复制</button>");
                        var curlcode=$("<code></code>");
                        curlcode.html(curl);

                        resp5.find(".panel-body").html("");
                        resp5.find(".panel-body").append(curlcode).append(cpcurlBotton);


                        var clipboard = new ClipboardJS('#btnCopyCurl',{
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

                        that.log("tab show...")
                        resptab.find("a:first").tab("show");

                    }
                })
            }


        })
        //删除按钮功能实现
        that.getDoc().find("#tab2").find(".btn-param-delete").on("click",function (e) {
            e.preventDefault();
            var btndelete=$(this);
            btndelete.parent().parent().remove();
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
     * 构建curl
     */
    SwaggerBootstrapUi.prototype.buildCurl=function (apiInfo,headers,reqdata,paramBodyType,url,formCurlParams) {
        var that=this;
        var curlified=new Array();
        var fullurl="http://"+that.currentInstance.host;
        //判断url是否是以/开头
        if(!apiInfo.url.startWith("/")){
            fullurl+="/";
        }
        fullurl+=url;
        curlified.push( "curl" );
        curlified.push( "-X", apiInfo.methodType.toUpperCase() );
        curlified.push( "\""+fullurl+"\"");
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
            if(tp=="string"){
                var jobj=JSON.parse(reqdata);
                var objstr=JSON.stringify( jobj ).replace(/\\n/g, "").replace(/"/g,"\\\"");
                that.log(objstr);
                curlified.push( "-d" );
                curlified.push( "\""+objstr +"\"")
            }else if(tp=="object"){
                //object
                var objstr=JSON.stringify( reqdata ).replace(/\\n/g, "").replace(/"/g,"\\\"");
                that.log(objstr);
                curlified.push( "-d" );
                curlified.push( "\""+objstr +"\"")
            }
        }else{
            //判断是否是文件上传
            if(formCurlParams!=null&&formCurlParams!=undefined){
                var formArr=new Array();
                for(var d in reqdata){
                    formArr.push(d+"="+reqdata[d]);
                }
                var formStr=formArr.join("&");
                that.log("表单参数...");
                that.log(formStr);
                that.log(formStr.toString());
                if(formArr.length>0){
                    curlified.push( "-F" );
                    curlified.push( "\""+formStr +"\"");
                }
                var fileArr=new Array();
                for(var f in formCurlParams){
                    fileArr.push(f+"="+formCurlParams[f]);
                }
                var fileStr=fileArr.join("&");
                if(fileArr.length>0){
                    curlified.push( "-F" );
                    curlified.push( "\""+fileStr +"\"");
                }
            }else{
                //form
                var formArr=new Array();
                for(var d in reqdata){
                    formArr.push(d+"="+reqdata[d]);
                }
                var formStr=formArr.join("&");
                that.log("表单...");
                that.log(formStr);
                that.log(formStr.toString());
                if(formArr.length>0){
                    curlified.push( "-d" );
                    curlified.push( "\""+formStr +"\"");
                }
            }
        }
        return curlified.join(" ");
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

    SwaggerBootstrapUi.prototype.markdownDocInit=function () {
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
        $("#docText").each(function(){
            var md = $(this).val();
            if(md){
                $("#contentDoc").html(marked(md));
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
    /**
     * 创建权限页面
     */
    SwaggerBootstrapUi.prototype.createSecurityElement=function () {
        var that=this;
        that.getDoc().html("");
        setTimeout(function () {
            var html = template('SwaggerBootstrapUiSecurityScript', that.currentInstance);
            that.getDoc().html(html)
            //保存事件
            that.getDoc().find(".btn-save").on("click",function (e) {
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
                var cacheSecurity={};
                $.each(that.currentInstance.securityArrs,function (i, sa) {
                    if(sa.key==data.key&&sa.name==data.name){
                        sa.value=value;
                        cacheSecurity.key=sa.key;
                        cacheSecurity.name=sa.name;
                        cacheSecurity.value=value;

                    }
                })
                that.log(that.currentInstance);
                layer.msg("保存成功");
                //判断是否有保存instancid
                var cacheSecurityData=$("#sbu-header").data("cacheSecurity");
                if(cacheSecurityData==undefined||cacheSecurityData==null){
                    cacheSecurityData=new Array();
                    cacheSecurityData.push(cacheSecurity);
                }else{
                    //存在
                    var flag=false;
                    //判断当前id是否存在
                    $.each(cacheSecurityData,function (i, sa) {
                        if(sa.key==cacheSecurity.key&&sa.name==cacheSecurity.name){
                            sa.value=cacheSecurity.value;
                            flag=true;
                        }
                    })
                    if(!flag){
                        cacheSecurityData.push(cacheSecurity);
                    }
                }
                //更新
                $("#sbu-header").data("cacheSecurity",cacheSecurityData);
                that.log($("#sbu-header").data("cacheSecurity"));
            })
        },100)
        //保存事件
        that.getDoc().find(".btn-save").on("click",function (e) {
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
                }
            })
            that.log(that.currentInstance);
        })

    }
    /***
     * 创建简介页面
     */
    SwaggerBootstrapUi.prototype.createDescriptionElement=function () {
        var that=this;
        /*var table=$('<table class="table table-hover table-bordered table-text-center"></table>');
        //修改title
        $("title").html("").html(that.currentInstance.title)
        table.append($('<thead><tr><th colspan="2" style="text-align:center">'+that.currentInstance.title+'</th></tr></thead>'));
        var tbody=$('<tbody></tbody>');
        tbody.append($('<tr><th class="active">简介</th><td style="text-align: left">'+that.currentInstance.description+'</td></tr>'));
        tbody.append($('<tr><th class="active">作者</th><td style="text-align: left">'+that.currentInstance.contact+'</td></tr>'));
        tbody.append($('<tr><th class="active">版本</th><td style="text-align: left">'+that.currentInstance.version+'</td></tr>'));
        tbody.append($('<tr><th class="active">host</th><td style="text-align: left">'+that.currentInstance.host+'</td></tr>'))
        tbody.append($('<tr><th class="active">服务url</th><td style="text-align: left">'+that.currentInstance.termsOfService+'</td></tr>'));
        table.append(tbody);
        var div=$('<div  style="width:99%;margin:0px auto;"></div>')
        div.append(table);*/
        //内容覆盖
        that.getDoc().html("");
        //that.getDoc().append(div);

        setTimeout(function () {
            var html = template('SwaggerBootstrapUiIntroScript', that.currentInstance);
            that.getDoc().html(html)
            //that.introMarkdownDocInit();
        },100)


    }

    SwaggerBootstrapUi.prototype.introMarkdownDocInit=function () {
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
        $("#docSbuText").each(function(){
            var md = $(this).val();
            if(md){
                $("#sbuDescriptionDoc").html(marked(md));
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
        /*var description="swagger-bootstrap-ui 提供markdwon格式类型的离线文档,开发者可拷贝该内容通过其他markdown转换工具进行转换为html或pdf.";
        var divdes=$('<div class="alert alert-info" role="alert">'+description+'</div>');
        var div=$('<div  style="width:99%;margin:0px auto;"></div>');
        div.append(divdes);
        //toolbar按钮组
        var toolbarDiv=$('<div class="input-inline" style="margin-bottom:10px;">');
        var copyBtn=$('<button class="btn btn-primary" type="button" id="btnCopy"  data-clipboard-action="copy" data-clipboard-target="#txtDoc">拷贝文档</button></div>');
        toolbarDiv.append(copyBtn);
        div.append(toolbarDiv);
        //添加textarea
        var txtDiv=$("<div class='input-inline'><textarea class='form-control' style='width: 100%;height: 100%;' id='txtDoc'></textarea></div>")
        div.append(txtDiv);*/
        //内容覆盖
        that.getDoc().html("");
        setTimeout(function () {
            var html = template('offLinecontentScript', that.currentInstance);
            that.getDoc().html(html);

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
    /***
     * 解析实例属性
     */
    SwaggerBootstrapUi.prototype.analysisDefinition=function (menu) {
        var that=this;
        //解析definition
        if(menu!=null&&typeof (menu)!="undefined"&&menu!=undefined&&menu.hasOwnProperty("definitions")){
            var definitions=menu["definitions"];
            for(var name in definitions){
                var swud=new SwaggerBootstrapUiDefinition();
                swud.name=name;
                that.log("开始解析Definition:"+name);
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
                                    that.log("解析属性："+property);
                                    that.log(propobj);
                                    if(type=="array"){
                                        propValue=new Array();
                                        var items=propobj["items"];
                                        var ref=items["$ref"];
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
                                that.log("解析属性："+property);
                                that.log(propobj);
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
            $.each(tags,function (i, tag) {
                var swuTag=new SwaggerBootstrapUiTag(tag.name,tag.description);
                that.currentInstance.tags.push(swuTag);
            })

        }
        //解析paths属性
        if(menu!=null&&typeof (menu)!="undefined"&&menu!=undefined&&menu.hasOwnProperty("paths")){
            var paths=menu["paths"];
            for(var path in paths){
                var pathObject=paths[path];
                var apiInfo=null;
                if(pathObject.hasOwnProperty("get")){
                    //get方式
                    apiInfo=pathObject["get"]
                    if(apiInfo!=null){
                        that.currentInstance.paths.push(that.createApiInfoInstance(path,"get",apiInfo));
                        that.methodCountAndDown("GET");

                    }
                }
                if(pathObject.hasOwnProperty("post")){
                    //post 方式
                    apiInfo=pathObject["post"]
                    if(apiInfo!=null){
                        that.currentInstance.paths.push(that.createApiInfoInstance(path,"post",apiInfo));
                        that.methodCountAndDown("POST");
                    }
                }
                if(pathObject.hasOwnProperty("put")){
                    //put
                    apiInfo=pathObject["put"]
                    if(apiInfo!=null){
                        that.currentInstance.paths.push(that.createApiInfoInstance(path,"put",apiInfo));
                        that.methodCountAndDown("PUT");
                    }
                }
                if(pathObject.hasOwnProperty("delete")){
                    //delete
                    apiInfo=pathObject["delete"]
                    if(apiInfo!=null){
                        that.currentInstance.paths.push(that.createApiInfoInstance(path,"delete",apiInfo));
                        that.methodCountAndDown("DELETE");
                    }
                }
                if (pathObject.hasOwnProperty("patch")){
                    //扩展 支持http其余请求方法接口
                    //add by xiaoymin 2018-4-28 07:16:12
                    //patch
                    apiInfo=pathObject["patch"]
                    if(apiInfo!=null){
                        that.currentInstance.paths.push(that.createApiInfoInstance(path,"patch",apiInfo));
                        that.methodCountAndDown("PATCH");
                    }
                }
                if (pathObject.hasOwnProperty("options")){
                    //OPTIONS
                    apiInfo=pathObject["options"]
                    if(apiInfo!=null){
                        that.currentInstance.paths.push(that.createApiInfoInstance(path,"options",apiInfo));
                        that.methodCountAndDown("OPTIONS");
                    }
                }
                if (pathObject.hasOwnProperty("trace")){
                    //TRACE
                    apiInfo=pathObject["trace"]
                    if(apiInfo!=null){
                        that.currentInstance.paths.push(that.createApiInfoInstance(path,"trace",apiInfo));
                        that.methodCountAndDown("TRACE");
                    }
                }
                if (pathObject.hasOwnProperty("head")){
                    //HEAD
                    apiInfo=pathObject["head"]
                    if(apiInfo!=null){
                        that.currentInstance.paths.push(that.createApiInfoInstance(path,"head",apiInfo));
                        that.methodCountAndDown("HEAD");
                    }
                }
                if (pathObject.hasOwnProperty("connect")){
                    //CONNECT
                    apiInfo=pathObject["connect"]
                    if(apiInfo!=null){
                        that.currentInstance.paths.push(that.createApiInfoInstance(path,"connect",apiInfo));
                        that.methodCountAndDown("CONNECT");
                    }

                }

            }

        }
        //解析securityDefinitions属性
        if(menu!=null&&typeof (menu)!="undefined"&&menu!=undefined&&menu.hasOwnProperty("securityDefinitions")){
            var securityDefinitions=menu["securityDefinitions"];
            if(securityDefinitions!=null){
                //判断是否有缓存cache值
                var cacheSecurityData=$("#sbu-header").data("cacheSecurity");
                for(var j in securityDefinitions){
                    var sdf=new SwaggerBootstrapUiSecurityDefinition();
                    var sdobj=securityDefinitions[j];
                    sdf.key=j;
                    sdf.type=sdobj.type;
                    sdf.name=sdobj.name;
                    sdf.in=sdobj.in;
                    if(cacheSecurityData!=null&&cacheSecurityData!=undefined){
                        //存在缓存值,更新当前值,无需再次授权
                        $.each(cacheSecurityData,function (i, sa) {
                            if(sa.key==sdf.key&&sa.name==sdf.name){
                                sdf.value=sa.value;
                            }
                        })
                    }
                    that.currentInstance.securityArrs.push(sdf);
                }
            }
        }
        //tag分组
        $.each(that.currentInstance.tags,function (i, tag) {
            //查找childrens
            $.each(that.currentInstance.paths, function (k, methodApi) {
                //判断tags是否相同
                $.each(methodApi.tags, function (x, tagName) {
                    if (tagName == tag.name) {
                        tag.childrens.push(methodApi);
                    }
                })
            })
        });
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
        var fullpath="";
        var basePathFlag=false;
        if (basePath!=""&&basePath!="/"){
            //如果非空,非根目录
            fullpath+=basePath;
            basePathFlag=true;
            fullpath+=path;
            swpinfo.showUrl=fullpath;
        }else{
            //截取字符串
            fullpath=path.substring(1);
            swpinfo.showUrl=path;
        }

        swpinfo.id="ApiInfo"+Math.round(Math.random()*1000000);
        swpinfo.url=fullpath;
        swpinfo.originalUrl=fullpath;
        swpinfo.basePathFlag=basePathFlag;
        swpinfo.methodType=mtype;
        if(apiInfo!=null){
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
                    minfo.name=m.name;
                    minfo.type=m.type;
                    minfo.in=m.in;
                    minfo.require=m.required;
                    minfo.description=m.description;
                    //判断是否有枚举类型
                    if(m.hasOwnProperty("enum")){
                        that.log("包括枚举类型...")
                        that.log(m.enum);
                        minfo.enum=m.enum;
                        that.log(minfo);
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
                                    minfo.description=def.description;
                                }
                            }
                        }else{
                            if (schemaObject.hasOwnProperty("$ref")){
                                var ref=m["schema"]["$ref"];
                                var className=$.getClassName(ref);
                                minfo.type=className;
                                minfo.schemaValue=className;
                                var def=that.getDefinitionByName(className);
                                if(def!=null){
                                    minfo.def=def;
                                    minfo.value=def.value;
                                    if(def.description!=undefined&&def.description!=null&&def.description!=""){
                                        minfo.description=def.description;
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
                                    minfo.description=def.description;
                                }
                            }
                        }else{
                            if (items.hasOwnProperty("type")){
                                minfo.type=items["type"];
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
                            minfo.txtValue=JSON.stringify(txtArr,null,4)
                        }else{
                            //引用类型
                            if(!$.checkIsBasicType(minfo.type)){
                                minfo.txtValue=JSON.stringify(minfo.value,null,4);
                            }
                        }
                    }

                    if(!checkParamArrsExists(swpinfo.parameters,minfo)){
                        swpinfo.parameters.push(minfo);
                        //判断当前属性是否是schema
                        if(minfo.schema){
                            deepRefParameter(minfo,that,minfo.def,swpinfo);
                        }
                    }
                })
            }

            var definitionType=null;
            var arr=false;
            //解析responsecode
            if(typeof (apiInfo.responses)!='undefined'&&apiInfo.responses!=null){
                var resp=apiInfo.responses;
                for(var status in resp) {
                    var swaggerResp=new SwaggerBootstrapUiResponseCode();
                    var rescrobj = resp[status];
                    swaggerResp.code=status;
                    swaggerResp.description=rescrobj["description"];
                    if (rescrobj.hasOwnProperty("schema")){
                        var schema=rescrobj["schema"];
                        //单引用类型
                        //判断是否是数组类型
                        var regex=new RegExp("#/definitions/(.*)$","ig");
                        if(schema.hasOwnProperty("$ref")){
                            if(regex.test(schema["$ref"])) {
                                var ptype=RegExp.$1;
                                definitionType=ptype;
                                swaggerResp.schema=ptype;
                            }
                        }else if(schema.hasOwnProperty("type")){
                            var t=schema["type"];
                            if (t=="array"){
                                arr=true;
                                if(schema.hasOwnProperty("items")){
                                    var items=schema["items"];
                                    if(regex.test(items["$ref"])) {
                                        var ptype=RegExp.$1;
                                        definitionType=ptype;
                                        swaggerResp.schema=ptype;
                                    }
                                }
                            }else{
                                //判断是否是基础类型
                                if($.checkIsBasicType(t)){
                                    //基础类型
                                    swpinfo.responseText=t;
                                    swpinfo.responseBasicType=true;
                                }
                            }
                        }

                    }
                    swpinfo.responseCodes.push(swaggerResp);
                }
            }

            if (definitionType!=null){
                //查询
                for(var i=0;i<that.currentInstance.difArrs.length;i++){
                    var ref=that.currentInstance.difArrs[i];
                    if(ref.name==definitionType){
                        if (arr){
                            var na=new Array();
                            na.push(ref.value);
                            swpinfo.responseValue=JSON.stringify(na,null,4);
                            swpinfo.responseJson=na;
                        }else{
                            swpinfo.responseValue=JSON.stringify(ref.value,null,4);
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
                                resParam.type=p.type;
                                resParam.description=p.description;
                                if(!$.checkIsBasicType(p.refType)){
                                    resParam.schemaValue=p.refType;
                                    var deepDef=that.getDefinitionByName(p.refType);
                                    deepResponseRefParameter(swpinfo,that,deepDef,resParam);
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
        return swpinfo;
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
                            refp.name=p.name;
                            refp.type=p.type;
                            refp.description=p.description;
                            //add之前需要判断是否已添加,递归情况有可能重复
                            refParam.params.push(refp);
                            //判断类型是否基础类型
                            if(!$.checkIsBasicType(p.refType)){
                                refp.schemaValue=p.refType;
                                if(resParam.name!=refp.name){
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
                        refp.name=p.name;
                        refp.type=p.type;
                        if(p.refType!=null&&p.refType!=undefined&&p.refType!=""){
                            //修复针对schema类型的参数,显示类型为schema类型
                            refp.type=p.refType;
                        }
                        refp.in=minfo.in;
                        refp.require=p.required;
                        refp.description=p.description;
                        refParam.params.push(refp);
                        //判断类型是否基础类型
                        if(!$.checkIsBasicType(p.refType)){
                            refp.schemaValue=p.refType;
                            if(minfo.name!=refp.name){
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
                that.log("解析definitionName:"+definitionName);
                that.log("是否递归："+flag);
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
            $("#leftMenu").css("height",$(window).height()-$("#sbu-header").height()-2);
            $("#content").css("height",$(window).height()-$("#sbu-header").height()-2);
            that.log("resize------------height")
            that.log("window--"+$(window).height())
            that.log("document--"+$(document).height())
            that.log("left--"+$("#leftMenu").height())
        })
    }

    /***
     * 控制台打印
     * @param msg
     */
    SwaggerBootstrapUi.prototype.log=function (msg) {
        /*if(window.console){
            //正式版不开启console功能
            console.log(msg);
        }*/
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
        this.parameters=new Array();
        //针对parameter属性有引用类型的参数,继续以table 的形式展现
        //存放SwaggerBootstrapUiRefParameter 集合
        this.refparameters=new Array();
        this.responseCodes=new Array();
        this.responseValue=null;
        this.responseJson=null;
        this.responseText=null;
        this.responseBasicType=false;
        //响应字段说明
        this.responseParameters=new Array();
        this.responseRefParameters=new Array();
        //新增菜单id
        this.id="";


    }

    var SwaggerBootstrapUiRefParameter=function () {
        this.name=null;
        //存放SwaggerBootstrapUiParameter集合
        this.params=new Array();
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
        //引用类
        this.def=null;
        //des
        this.description=null;
        //文本框值
        this.txtValue=null;
        //枚举类型
        this.enum=null;
    }
    /***
     * 响应码
     * @constructor
     */
    var SwaggerBootstrapUiResponseCode=function () {
        this.code=null;
        this.description=null;
        this.schema=null;
    }

    /***
     * 公共方法
     */
    $.extend({
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
            var basicTypes=["string","integer","number","object","boolean"];
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

    /**
     * 运行
     */
    new SwaggerBootstrapUi().main();
})(jQuery)