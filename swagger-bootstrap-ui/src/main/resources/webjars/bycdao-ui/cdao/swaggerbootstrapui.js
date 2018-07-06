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

        this.menuId="menu";
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
        //加载分组接口
        that.analysisGroup();
        //创建分组元素
        that.createGroupElement();
    }
    /***
     * 调用swagger的分组接口,获取swagger分组信息,包括分组名称,接口url地址,版本号等
     */
    SwaggerBootstrapUi.prototype.analysisGroup=function () {
        var that=this;
        $.ajax({
            url:that.url,
            type:"get",
            async:false,
            success:function (data) {
                //获取分组名称
                var groupData=data;
                $.each(groupData,function (i, group) {
                    var g=new SwaggerBootstrapUiInstance(group.name,group.location,group.swaggerVersion);
                    that.instances.push(g);
                })
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
            var api=instance.location;
            //这里判断url请求是否已加载过
            //防止出现根路径的情况
            var idx=api.indexOf("/");
            if(idx==0){
                api=api.substr(1);
            }
            that.log("截取后的url:"+api);
            $.ajax({
                //url:"v2/api-docs",
                url:api,
                dataType:"json",
                type:"get",
                async:false,
                success:function (data) {
                    //var menu=JSON.parse(data)
                    var menu=data;
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
        var dli=$('<li  class="active detailMenu"><a href="javascript:void(0)"><i class="icon-text-width"></i><span class="menu-text"> 简介 </span></a></li>')
        dli.on("click",function () {
            that.log("简介click")
            that.createDescriptionElement();
            dli.addClass("active");
        })
        that.getMenu().append(dli);
        //离线文档功能
        var mddocli=$("<li  class=\"detailMenu\"><a href=\"javascript:void(0)\"><i class=\"icon-text-width\"></i><span class=\"menu-text\"> 离线文档(MD) </span></a></li>");
        mddocli.on("click",function () {
            that.log("离线文档功能click")
            that.createMarkdownTab();
            that.getMenu().find("li").removeClass("active");
            mddocli.addClass("active");
        })
        that.getMenu().append(mddocli);
        var methodApis=DApiUI.eachPath(menu);

        $.each(that.currentInstance.tags,function (i, tag) {
            var tagInfo=new TagInfo(tag.name,tag.description);
            //查找childrens
            $.each(methodApis,function (i, methodApi) {
                //判断tags是否相同
                $.each(methodApi.tag,function(i,tagName){
                    if(tagName==tagInfo.name){
                        tagInfo.childrens.push(methodApi);
                    }
                })
            })
            var len=tagInfo.childrens.length;
            if(len==0){
                var li=$('<li class="detailMenu"><a href="javascript:void(0)"><i class="icon-text-width"></i><span class="menu-text"> '+tagInfo.name+' </span></a></li>');
                DApiUI.getMenu().append(li);
            }else{
                //存在子标签
                var li=$('<li  class="detailMenu"></li>');
                var titleA=$('<a href="#" class="dropdown-toggle"><i class="icon-file-alt"></i><span class="menu-text">'+tagInfo.name+'<span class="badge badge-primary ">'+len+'</span></span><b class="arrow icon-angle-down"></b></a>');
                li.append(titleA);
                //循环树
                var ul=$('<ul class="submenu"></ul>')
                $.each(tagInfo.childrens,function (i, children) {
                    var childrenLi=$('<li class="menuLi" ><div class="mhed"><div>'+children.methodType.toUpperCase()+'-<code>'+children.url+'</code></div><div>'+children.summary+'</div></div></li>');
                    //console.log(children)
                    //var childrenA=$('<a href="javascript:void(0)"><i class="icon-double-angle-right"></i><div  ><h5><span class="method">['+children.methodType+']</span></h5></div>'+children.summary+'('+children.url+')</a>');
                    //childrenLi.append(childrenA);
                    childrenLi.data("data",children);
                    ul.append(childrenLi);
                })
                li.append(ul);
                DApiUI.getMenu().append(li);
            }
        })
        DApiUI.log("菜单初始化完成...")
        DApiUI.initLiClick();
    }
    /***
     * 创建简介页面
     */
    SwaggerBootstrapUi.prototype.createDescriptionElement=function () {
        var that=this;
        var table=$('<table class="table table-hover table-bordered table-text-center"></table>');
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
        div.append(table);
        //内容覆盖
        that.getDoc().html("");
        that.getDoc().append(div);
    }
    /***
     * 创建离线文档页面
     * 点击离线文档菜单时,创建该页面
     */
    SwaggerBootstrapUi.prototype.createMarkdownTab=function () {
        var that=this;
        var description="swagger-bootstrap-ui 提供markdwon格式类型的离线文档,开发者可拷贝该内容通过其他markdown转换工具进行转换为html或pdf.";
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
        div.append(txtDiv);
        //内容覆盖
        that.getDoc().html("");
        that.getDoc().append(div);
        //初始化copy按钮功能
        var clipboard = new ClipboardJS('#btnCopy');
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
        if(menu!=null&&typeof (menu)!="undefined"&&menu.hasOwnProperty("definitions")){
            var definitions=menu["definitions"];
            for(var name in definitions){
                var swud=new SwaggerBootstrapUiDefinition();
                swud.name=name;
                //获取value
                var value=definitions[name];
                if ($.checkUndefined(value)){
                    swud.description=$.propValue("description",value,"");
                    swud.type=$.propValue("type",value,"");
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
                                    if(type=="array"){
                                        propValue=new Array();
                                        var items=propobj["items"];
                                        var ref=items["$ref"];
                                        var regex=new RegExp("#/definitions/(.*)$","ig");
                                        if(regex.test(ref)){
                                            var refType=RegExp.$1;
                                            //这里需要递归判断是否是本身,如果是,则退出递归查找
                                            if(refType!=name){
                                                propValue.push(that.findRefDefinition(refType,definitions,false));
                                            }else{
                                                propValue.push(that.findRefDefinition(refType,definitions,true));
                                            }
                                        }
                                    }
                                }

                            }
                            else{
                                if(propobj.hasOwnProperty("$ref")){
                                    var ref=propobj["$ref"];
                                    var regex=new RegExp("#/definitions/(.*)$","ig");
                                    if(regex.test(ref)) {
                                        var refType = RegExp.$1;
                                        //这里需要递归判断是否是本身,如果是,则退出递归查找
                                        if(refType!=name){
                                            propValue=that.findRefDefinition(refType,definitions,false);
                                        }else{
                                            propValue=that.findRefDefinition(refType,definitions,true);
                                        }

                                    }
                                }else{
                                    propValue={};
                                }
                            }

                            spropObj.value=propValue;
                            //addprop
                            swud.properties.push(spropObj);
                            defiTypeValue[property]=propValue;
                        }
                        swud.value=defiTypeValue;
                    }
                }
                that.currentInstance.difArrs.push(swud);
            }
        }
        //解析tags标签
        if(menu!=null&&typeof (menu)!="undefined"&&menu.hasOwnProperty("tags")){
            var tags=menu["tags"];
            $.each(tags,function (i, tag) {
                var swuTag=new SwaggerBootstrapUiTag(tag.name,tag.description);
                that.currentInstance.tags.push(swuTag);
            })

        }
        //解析paths属性
        if(menu!=null&&typeof (menu)!="undefined"&&menu.hasOwnProperty("paths")){
            var paths=menu["paths"];
            for(var path in paths){
                var pathObject=paths[path];
                var swpinfo=new SwaggerBootstrapUiApiInfo();
                swpinfo.url=path;
                var apiInfo=null;
                if(pathObject.hasOwnProperty("get")){
                    //get方式
                    apiInfo=pathObject["get"]
                    swpinfo.methodType="get";
                }
                if(pathObject.hasOwnProperty("post")){
                    //post 方式
                    apiInfo=pathObject["post"]
                    swpinfo.methodType="post";
                }
                if(pathObject.hasOwnProperty("put")){
                    //put
                    apiInfo=pathObject["put"]
                    swpinfo.methodType="put";
                }
                if(pathObject.hasOwnProperty("delete")){
                    //delete
                    apiInfo=pathObject["delete"]
                    swpinfo.methodType="delete";
                }
                //扩展 支持http其余请求方法接口
                //add by xiaoymin 2018-4-28 07:16:12
                if (pathObject.hasOwnProperty("patch")){
                    //patch
                    apiInfo=pathObject["patch"]
                    swpinfo.methodType="patch";
                }
                if (pathObject.hasOwnProperty("options")){
                    //OPTIONS
                    apiInfo=pathObject["options"]
                    swpinfo.methodType="options";
                }
                if (pathObject.hasOwnProperty("trace")){
                    //TRACE
                    apiInfo=pathObject["trace"]
                    swpinfo.methodType="trace";
                }
                if (pathObject.hasOwnProperty("head")){
                    //HEAD
                    apiInfo=pathObject["head"]
                    swpinfo.methodType="head";
                }
                if (pathObject.hasOwnProperty("connect")){
                    //CONNECT
                    apiInfo=pathObject["connect"]
                    swpinfo.methodType="connect";
                }
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
                            minfo.require=m.require;
                            if (m.hasOwnProperty("schema")){
                                //存在schema属性,请求对象是实体类
                                minfo.schema=true;
                                var ref=m["schema"]["$ref"];
                                var className=$.getClassName(ref);
                                var def=that.getDefinitionByName(className);
                                minfo.value=def.value;
                            }
                            swpinfo.parameters.push(minfo);
                        })
                    }
                    that.currentInstance.paths.push(swpinfo);
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
    SwaggerBootstrapUi.prototype.findRefDefinition=function (definitionName, definitions, flag) {
        var that=this;
        var defaultValue="";
        for(var definition in definitions){
            if(definitionName==definition){
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
                                            //非递归查找
                                            if(refType!=definitionName){
                                                propValue.push(that.findRefDefinition(refType,definitions,flag));
                                            }else{
                                                propValue.push(that.findRefDefinition(refType,definitions,true));
                                            }
                                        }

                                    }
                                }
                            }

                        }else{

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
        var groupli=$('<li  class="active"></li>');
        var groupSele=$("<select id='groupSel' style='width:100%;'></select>");
        $.each(that.instances,function (i, group) {
            var groupOption=$("<option data-url='"+group.location+"' data-name='"+group.name+"'>"+group.name+"</option>");
            groupSele.append(groupOption);
        })
        groupli.append(groupSele);
        groupSele.on("change",function () {
            var that=$(this);
            that.log(that)
            var name=that.find("option:selected").attr("data-name");
            that.log("分组：：");
            that.log(name);
            var instance=that.selectInstanceByGroupName(name);
            $.log(instance);
            that.analysisApi(instance);
        })
        that.getMenu().html("");
        that.getMenu().append(groupli);
        //默认加载第一个url
        that.analysisApi(that.group[0]);
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
    /***
     * 控制台打印
     * @param msg
     */
    SwaggerBootstrapUi.prototype.log=function (msg) {
        if(window.console){
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
    /***
     * 获取当前swagger页面主页面元素
     * @returns {*|HTMLElement}
     */
    SwaggerBootstrapUi.prototype.getDoc=function () {
        return $("#"+this.docId);
    }
    /***
     * swagger 分组对象
     * @param name 分组对象名称
     * @param location url地址
     * @param version 版本号
     * @constructor
     */
    var SwaggerBootstrapUiInstance=function (name, location, version) {
        //默认未加载
        this.load=false;
        //分组名称
        this.name=name;
        //分组url地址
        this.location=location;
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
        this.description="";
        this.example="";
        this.format="";
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
    }

    /***
     * Swagger接口基础信息
     * @constructor
     */
    var SwaggerBootstrapUiApiInfo=function () {
        this.url=null;
        this.methodType=null;
        this.description=null;
        this.summary=null;
        this.consumes=null;
        this.operationId=null;
        this.produces=null;
        this.tags=null;
        this.parameters=new Array();
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
        this.value=null;

    }
    /***
     * 公共方法
     */
    $.extend({
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
            if($.inArray(type,basicTypes)>-1){
                flag=true;
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
        }
    })


    /**
     * 运行
     */
    new SwaggerBootstrapUi().main();
})(jQuery)