/**
 * Created by xiaoym on 2017/4/17.
 */

(function ($) {
    //初始化类
    var DApiUI={};

    DApiUI.init=function () {
        DApiUI.createGroupTab();
    }

    /**
     * 创建分组
     */
    DApiUI.createGroupTab=function () {
        $.ajax({
            url:"swagger-resources",
            type:"get",
            async:false,
            success:function (data) {
                //获取分组名称
                var groupData=data;
                DApiUI.initGroupSele(groupData);
            }

        })

    }



    /***
     * 创建面板
     */
    DApiUI.creatabTab=function () {
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
        DApiUI.getDoc().html("");
        DApiUI.getDoc().append(divcontent);
        DApiUI.log("动态激活...")
        //liapi.addClass("active");
        DApiUI.log("动态激活12...")
        DApiUI.getDoc().find("#myTab a:first").tab('show')
        //$('#myTab a:first').tab('show')

    }

    /***
     * 检查对象属性,in并赋予默认值
     * @param obj
     * @param key
     * @param defaultValue
     * @param checkEmpty
     * @returns {*}
     */
    DApiUI.getValue=function (obj,key,defaultValue,checkEmpty) {
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
    }


    /***
     * 创建简介table页面
     * @param menu
     */
    DApiUI.createDescription=function (menu) {
        var table=$('<table class="table table-hover table-bordered table-text-center"></table>');
        var title="",description="",name="",version="",termsOfService="";
        var host=DApiUI.getValue(menu,"host","",true);
        if (menu.hasOwnProperty("info")){
            var info=menu.info;
            title=DApiUI.getValue(info,"title","Swagger-Bootstrap-UI-前后端api接口文档",true);
            description=DApiUI.getValue(info,"description","",true);
            if(info.hasOwnProperty("contact")){
                var contact=info["contact"];
                name=DApiUI.getValue(contact,"name","",true);
            }
            version=DApiUI.getValue(info,"version","",true);
            termsOfService=DApiUI.getValue(info,"termsOfService","",true);
        }
        //修改title
        $("title").html("").html(title)
        table.append($('<thead><tr><th colspan="2" style="text-align:center">'+title+'</th></tr></thead>'));
        var tbody=$('<tbody></tbody>');
        tbody.append($('<tr><th class="active">简介</th><td style="text-align: left">'+description+'</td></tr>'));
        tbody.append($('<tr><th class="active">作者</th><td style="text-align: left">'+name+'</td></tr>'));
        tbody.append($('<tr><th class="active">版本</th><td style="text-align: left">'+version+'</td></tr>'));
        tbody.append($('<tr><th class="active">host</th><td style="text-align: left">'+host+'</td></tr>'))
        tbody.append($('<tr><th class="active">服务url</th><td style="text-align: left">'+termsOfService+'</td></tr>'));
        table.append(tbody);
        var div=$('<div  style="width:99%;margin:0px auto;"></div>')
        div.append(table);
        //内容覆盖
        DApiUI.getDoc().html("");
        DApiUI.getDoc().append(div);
        DApiUI.getDoc().data("data",menu);
    }

    /***
     * 获取菜单结构
     */
    DApiUI.getMenuConstructs=function () {
        return DApiUI.getDoc().data("data");
    }

    DApiUI.toString=function (obj, defaultStr) {
        if (obj!=null && typeof (obj)!="undefined"){
            return obj.toString();
        }
        if (obj==undefined){
            return defaultStr;
        }
        return defaultStr;
    }


    /***
     * 初始化分组菜单
     * @param menu
     */
    DApiUI.initGroupSele=function (groupData) {
        //遍历tags
        var tags=new Array();
        //创建分组flag
        var groupli=$('<li  class="active"></li>');
        var groupSele=$("<select id='groupSel' style='width:100%;'></select>");
        $.each(groupData,function (i, group) {
            var groupOption=$("<option data-url='"+group.location+"' data-name='"+group.name+"'>"+group.name+"</option>");
            groupSele.append(groupOption);
        })
        groupli.append(groupSele);
        groupSele.on("change",function () {
            var that=$(this);
            DApiUI.log(that)
            var apiurl=that.find("option:selected").attr("data-url");
            DApiUI.log("分组：：");
            DApiUI.log(apiurl);
            DApiUI.initApiTree(apiurl);
        })
        DApiUI.getMenu().html("");
        DApiUI.getMenu().append(groupli);
        var url=groupData[0].location;
        //默认加载第一个url
        DApiUI.initApiTree(url);
    }

    DApiUI.initApiTree=function (url) {
        var idx=url.indexOf("/");
        if(idx==0){
            url=url.substr(1);
        }
        DApiUI.log("截取后的url:"+url);
        $.ajax({
            //url:"v2/api-docs",
            url:url,
            dataType:"json",
            type:"get",
            async:false,
            success:function (data) {
                //var menu=JSON.parse(data)
                var menu=data;
                DApiUI.definitions(menu);
                DApiUI.log(menu);
                DApiUI.createDescription(menu);
                DApiUI.createDetailMenu(menu);
                DApiUI.eachPath(menu);
            }
        })
    }

    /***
     * 创建分组详情菜单
     * @param menu
     */
    DApiUI.createDetailMenu=function (menu) {
        DApiUI.getMenu().find(".detailMenu").remove();

        //简介li
        var dli=$('<li  class="active detailMenu"><a href="javascript:void(0)"><i class="icon-text-width"></i><span class="menu-text"> 简介 </span></a></li>')
        dli.on("click",function () {
            DApiUI.log("简介click")
            DApiUI.createDescription(menu);
            dli.addClass("active");
        })
        DApiUI.getMenu().append(dli);
        var methodApis=DApiUI.eachPath(menu);

        $.each(menu.tags,function (i, tag) {
            var tagInfo=new TagInfo(tag.name,tag.description);
            //查找childrens
            $.each(methodApis,function (i, methodApi) {
                //判断tags是否相同
                if(methodApi.tag==tagInfo.name){
                    tagInfo.childrens.push(methodApi);
                }
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


    DApiUI.eachPath=function (menu) {
        var paths=menu.paths;
        DApiUI.log(paths);
        //paths是object对象,key是api接口地址,
        var methodApis=[];
        for(var key in paths){
            var obj=paths[key];
            //遍历obj,获取api接口访问方式
            //八中方式类型,直接判断
            if(obj.hasOwnProperty("get")){
                //get方式
                var apiInfo=new ApiInfo(obj["get"]);
                apiInfo.methodType="get";
                apiInfo.url=key;
                methodApis.push(apiInfo);
            }

            if(obj.hasOwnProperty("post")){
                //post 方式
                var apiInfo=new ApiInfo(obj["post"]);
                apiInfo.methodType="post";
                apiInfo.url=key;
                methodApis.push(apiInfo);
            }
            if(obj.hasOwnProperty("put")){
                //put
                var apiInfo=new ApiInfo(obj["put"]);
                apiInfo.methodType="put";
                apiInfo.url=key;
                methodApis.push(apiInfo);
            }
            if(obj.hasOwnProperty("delete")){
                //delete
                var apiInfo=new ApiInfo(obj["delete"]);
                apiInfo.methodType="delete";
                apiInfo.url=key;
                methodApis.push(apiInfo);
            }
        }
        //console.log(methodApis);
        return methodApis;

    }

    /***
     * li标签click事件
     */
    DApiUI.initLiClick=function () {
        DApiUI.getMenu().find(".menuLi").bind("click",function (e) {
            e.preventDefault();
            var that=$(this);
            var data=that.data("data");
            DApiUI.log("Li标签click事件");
            DApiUI.log(data);
            //获取parent-Li的class属性值
            var parentLi=that.parent().parent();
            DApiUI.log(parentLi);
            var className=parentLi.prop("class");
            DApiUI.log(className)
            DApiUI.getMenu().find("li").removeClass("active");
            //parentLi.addClass("active");
            that.addClass("active");
            DApiUI.createApiInfoTable(data);
            DApiUI.createDebugTab(data);
        })
    }

    DApiUI.getStringValue=function (obj) {
        var str="";
        if(typeof (obj)!='undefined'&&obj!=null){
            str=obj.toString();
        }
        return str;
    }


    /***
     * 格式化json
     * @param text_value
     */
    function formatterJson(text_value) {
        var res="";
        for(var i=0,j=0,k=0,ii,ele;i<text_value.length;i++)
        {//k:缩进，j:""个数
            ele=text_value.charAt(i);
            if(j%2==0&&ele=="}")
            {
                k--;
                for(ii=0;ii<k;ii++) ele="    "+ele;
                ele="\n"+ele;
            }
            else if(j%2==0&&ele=="{")
            {
                ele+="\n";
                k++;
                //debugger;
                for(ii=0;ii<k;ii++) ele+="    ";
            }
            else if(j%2==0&&ele==",")
            {
                ele+="\n";
                for(ii=0;ii<k;ii++) ele+="    ";
            }
            else if(ele=="\"") j++;
            res+=ele;
        }
        return res;

    }
    /**
     * 创建调试面板
     */
    DApiUI.createDebugTab=function (apiInfo) {
        DApiUI.log("创建调试tab")
        //方法、请求类型、发送按钮
        var div=$('<div style="width: 100%;margin: 0px auto;margin-top: 20px;"></div>');
        var headdiv1=$('<div class="input-group m-bot15"><span class="input-group-btn"><button class="btn btn-default btn-info" type="button">'+DApiUI.getStringValue(apiInfo.methodType)+'</button></span><input type="text" id="txtreqUrl" class="form-control" value="'+DApiUI.getStringValue(apiInfo.url)+'"/><span class="input-group-btn"><button id="btnRequest" class="btn btn-default btn-primary" type="button"> 发 送 </button></span></div>');
        div.append(headdiv1);


        //请求参数
        var divp=$('<div class="panel panel-primary"><div class="panel-heading">请求参数</div></div>')

        var divpbody=$('<div class="panel-body"></div>');
        //是否是文件上传
        var fileform=false;
        //判断是否有请求参数
        if(typeof (apiInfo.parameters)!='undefined'&&apiInfo.parameters!=null){
            var table=$('<table class="table table-hover table-bordered table-text-center"></table>')
            var thead=$('<thead><tr><th></th><th>参数名称</th><th>参数值</th><th>操作</th></tr></thead>');
            table.append(thead);
            var tbody=$('<tbody id="paramBody"></tbody>');
            $.each(apiInfo.parameters,function (i, param) {
                var tr=$('<tr></tr>');
                tr.data("data",param);
                //判断parame的in类型
                //query--text
                //body--textarea
                var checkbox=$('<td width="5%"><div class="checkbox"><label><input type="checkbox" value="" checked></label></div></td>');
                var key=$('<td width="20%"><input class="form-control p-key"   value="'+param.name+'"/></td>')
                //var value=$('<td><input class="form-control p-value" data-apiUrl="'+apiInfo.url+'" data-name="'+param.name+'" placeholder="'+DApiUI.getStringValue(param['description'])+'"/></td>');
                var value=$('<td></td>');

                var val=null;
                if(param["in"]=="body"){
                    tbody.attr("reqtype","body");
                    val=$('<textarea class="form-control p-value" style="font-size: 16px;" rows="10" data-apiUrl="'+apiInfo.url+'" name="'+param.name+'" data-name="'+param.name+'" placeholder="'+DApiUI.getStringValue(param['description'])+'"></textarea>')
                    //判断是否有schma
                    if(param.hasOwnProperty("schema")){
                        var schema=param["schema"];
                        var ref=schema["$ref"];
                        var regex=new RegExp("#/definitions/(.*)$","ig");
                        if(regex.test(ref)) {
                            var refType = RegExp.$1;
                            //这里判断refType是否是MultipartFile类型,如果是该类型,上传组件
                            if(refType=="MultipartFile"){
                                fileform=true;
                                val=$('<input name="'+param.name+'" type="file" class="form-control p-value" data-apiUrl="'+apiInfo.url+'" data-name="'+param.name+'" placeholder="'+DApiUI.getStringValue(param['description'])+'"/>');
                            }else{
                                //find in definitionsArray
                                var definitionsArray=DApiUI.getDoc().data("definitionsArray");
                                var deftion=null;
                                for(var i=0;i<definitionsArray.length;i++){
                                    var definition=definitionsArray[i];
                                    if(definition.key==refType){
                                        deftion=definition.value;
                                        break;
                                    }
                                }
                                //遍历proprietary
                                for(var k in deftion){
                                    deftion[k]="";
                                }
                                if(deftion!=null){
                                    //赋值
                                    val.val(formatterJson(JSON.stringify(deftion)));
                                }
                            }
                        }
                    }

                }else{
                    val=$('<input class="form-control p-value" name="'+param.name+'" data-apiUrl="'+apiInfo.url+'" data-name="'+param.name+'" placeholder="'+DApiUI.getStringValue(param['description'])+'"/>');
                    //判断是否有defaultvalue
                    if(param.hasOwnProperty("default")){
                        var defaultValue=param["default"];
                        val.val(defaultValue);
                    }
                    //这里判断param类型,如果是int类型,只能输入数字
                }
                value.append(val);
                var oper=$('<td width="5%"><button class="btn btn-danger btn-circle btn-lg" type="button"><strong>×</strong></button></td>');
                //删除事件
                oper.find("button").on("click",function (e) {
                    e.preventDefault();
                    var that=$(this);
                    that.parent().parent().remove();
                })
                //判断参数类型,针对path参数
                if(param["in"]=="path"){
                    //赋予change事件
                    value.find("input").on("keyup",function () {
                        var t=$(this);
                        var name=t.data("name");
                        var apiUrl=t.attr("data-apiUrl");
                        var realValue=apiUrl.replace("{"+name+"}",t.val());
                        //查找是否还存在其他path参数
                        $("#paramBody").find("tr").each(function (i, itr) {
                            var itrthat=$(this);
                            var itrdata=itrthat.data("data");
                            var itrname=itrdata["name"];
                            if(itrdata["in"]=="path"&&itrdata["name"]!=name){
                                //查找value值
                                var itrtdvalue=itrthat.find(".p-value").val();
                                if(itrtdvalue!=""){
                                    realValue=realValue.replace("{"+itrname+"}",itrtdvalue);
                                }
                            }
                        })
                        DApiUI.log(realValue);
                        $("#txtreqUrl").val(realValue);
                        DApiUI.log("keyup。。。。")
                    })

                }
                tr.append(checkbox).append(key).append(value).append(oper);
                tbody.append(tr);
            })
            table.append(tbody);
            //如果有文件上传,追加form表单
            if(fileform){
                //文件上传使用相对路径
                var uploadurltemp=apiInfo.url;
                if (uploadurltemp.indexOf("/")==0){
                    uploadurltemp=uploadurltemp.substr(1);
                }
                DApiUI.log("upload上传接口截取后:"+uploadurltemp)
                var form=$('<form id="uploadForm"  target="uploadIframe" action="'+uploadurltemp+'" type="" enctype="multipart/form-data" method="'+apiInfo.methodType+'"></form>');
                form.append(table);
                divpbody.append(form);
            }else{
                divpbody.append(table);
            }
        }else{
            divpbody.append($('<strong>暂无参数</strong>'))
        }
        divp.append(divpbody);
        //
        if(fileform){
            //追加iframe
            var resptabframe=$('<div id="resptab" class="tabs-container" ><iframe name="uploadIframe" id="uploadIframe" style="border: none;height: 1%;display: none;"></iframe></div>')
            divp.append(resptabframe);
        }
        div.append(divp);
        //创建reesponsebody
        var respcleanDiv=$('<div id="responsebody"></div>');

        div.append(respcleanDiv);


        DApiUI.getDoc().find("#tab2").find(".panel-body").html("")
        DApiUI.getDoc().find("#tab2").find(".panel-body").append(div);




        //发送事件
        headdiv1.find("#btnRequest").bind("click",function (e) {
            e.preventDefault();
            respcleanDiv.html("")
            DApiUI.log("发送请求");
            //
            var params={};
            var headerparams={};
            var bodyparams="";
            //modify by xiaoyumin 2017-8-9 11:28:16
            //增加表单验证
            var validateflag=false;
            var validateobj={};

            //获取参数
            var paramBody=DApiUI.getDoc().find("#tab2").find("#paramBody")
            DApiUI.log("paramsbody..")
            DApiUI.log(paramBody)
            //组装请求url
            var url=DApiUI.getStringValue(apiInfo.url);
            var cacheData=DApiUI.getDoc().data("data");
            if(typeof (cacheData.basePath)!="undefined"&&cacheData.basePath!=""){
                if(cacheData.basePath!="/"){
                    DApiUI.log("NOT ROOT PATH:");
                    url=cacheData.basePath+DApiUI.getStringValue(apiInfo.url);
                }
            }


            paramBody.find("tr").each(function () {
                var paramtr=$(this);
                var cked=paramtr.find("td:first").find(":checked").prop("checked");
                DApiUI.log(cked)
                if (cked){
                    //如果选中
                    var trdata=paramtr.data("data");
                    DApiUI.log("trdata....")
                    DApiUI.log(trdata);
                    //获取key
                    //var key=paramtr.find("td:eq(1)").find("input").val();
                    var key=trdata["name"];
                    //获取value
                    var value="";
                    if(trdata["in"]=="body"){
                        value=paramtr.find("td:eq(2)").find("textarea").val();
                        //这里需要判断schema
                        if(trdata.hasOwnProperty("schema")){
                            var schema=trdata["schema"];
                            if(schema.hasOwnProperty("$ref")){
                                var ref=schema["$ref"];
                                var regex=new RegExp("#/definitions/(.*)$","ig");
                                if(regex.test(ref)) {
                                    var refType = RegExp.$1;
                                    //这里判断refType是否是MultipartFile类型,如果是该类型,上传组件
                                    if (refType == "MultipartFile") {
                                        value=paramtr.find("td:eq(2)").find("input").val();
                                    }
                                }
                            }
                        }
                    }else{
                        value=paramtr.find("td:eq(2)").find("input").val();
                    }
                    //var value=paramtr.find("td:eq(2)").find("input").val();
                    //delete方式参数url传递
                    if(apiInfo.methodType=="delete"){
                        //判断是否是path参数
                        if(trdata["in"]=="path"){
                            url=url.replace("{"+key+"}",value);
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
                        }else{
                            if(trdata["in"]=="body"){
                                bodyparams+=value;
                            }else{
                                if(trdata["in"]=="header"){
                                    headerparams[key]=value;
                                }else{
                                    params[key]=value;
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
                    DApiUI.log("key:"+key+",value:"+value);
                }
            })
            DApiUI.log("获取参数..")
            DApiUI.log(params);
            DApiUI.log(apiInfo)

            DApiUI.log("请求url："+url);
            var reqdata=null;
            //console.log(paramBody.attr("reqtype"))
            var contType="application/json; charset=UTF-8";
            if(paramBody.attr("reqtype")!=null&&paramBody.attr("reqtype")!=undefined&&paramBody.attr("reqtype")=="body"){
                reqdata=bodyparams;
            }else{
                reqdata=params;
                contType="application/x-www-form-urlencoded; charset=UTF-8";
            }
            //console.log(reqdata)
            if(validateflag){
                layer.msg(validateobj.message);
                return;
            }
            //判断是否有表单
            var form=$("#uploadForm");
            if(form.length>0){
                form[0].submit();
                //console.log("表单提交")
                //iframe监听change事件
                $("#uploadIframe").on("load",function () {
                    //console.log("uploadIframe changed....")
                    $(this).unbind('load');
                    var framebody=$(this).contents().find("body");
                    var ret=framebody.html();
                    //是否存在pre标签
                    if(framebody.find("pre").length>0){
                        ret=framebody.find("pre").html();
                    }
                    var res;
                    try{
                        res=JSON.parse(ret);
                        //console.log(res)
                        var resptab=$('<div id="resptab" class="tabs-container" ></div>')
                        var ulresp=$('<ul class="nav nav-tabs">' +
                            '<li class=""><a data-toggle="tab" href="#tabresp" aria-expanded="false"> 响应内容 </a></li></ul>')
                        resptab.append(ulresp);
                        var respcontent=$('<div class="tab-content"></div>');
                        var resp1=$('<div id="tabresp" class="tab-pane active"><div class="panel-body"></div></div>');
                        respcontent.append(resp1);
                        resptab.append(respcontent)
                        respcleanDiv.append(resptab);

                        var jsondiv=$('<div></div>');
                        jsondiv.JSONView(res);
                        resp1.find(".panel-body").append(jsondiv);
                        resptab.find("a:first").tab("show");
                    }catch (err){
                        //nothing to do,default to show
                        respcleanDiv.html(ret);
                    }
                })
            }else{
                $.ajax({
                    url:url,
                    headers:headerparams,
                    type:DApiUI.getStringValue(apiInfo.methodType),
                    data:reqdata,
                    contentType:contType,
                    success:function (data,status,xhr) {
                        var resptab=$('<div id="resptab" class="tabs-container" ></div>')
                        var ulresp=$('<ul class="nav nav-tabs">' +
                            '<li class=""><a data-toggle="tab" href="#tabresp" aria-expanded="false"> 响应内容 </a></li>' +
                            '<li class=""><a data-toggle="tab" href="#tabcookie" aria-expanded="true"> Cookies</a></li>' +
                            '<li class=""><a data-toggle="tab" href="#tabheader" aria-expanded="true"> Headers </a></li></ul>')

                        resptab.append(ulresp);
                        var respcontent=$('<div class="tab-content"></div>');

                        var resp1=$('<div id="tabresp" class="tab-pane active"><div class="panel-body"><pre></pre></div></div>');
                        var resp2=$('<div id="tabcookie" class="tab-pane active"><div class="panel-body">暂无</div>');
                        var resp3=$('<div id="tabheader" class="tab-pane active"><div class="panel-body">暂无</div></div>');

                        respcontent.append(resp1).append(resp2).append(resp3);

                        resptab.append(respcontent)

                        respcleanDiv.append(resptab);
                        DApiUI.log(xhr);
                        DApiUI.log(xhr.getAllResponseHeaders());
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
                        DApiUI.log("Content-Type:"+contentType);
                        DApiUI.log(xhr.hasOwnProperty("responseJSON"))
                        if (xhr.hasOwnProperty("responseJSON")){
                            //如果存在该对象,服务端返回为json格式
                            resp1.find(".panel-body").html("")
                            DApiUI.log(xhr["responseJSON"])
                            var pre=$('<pre></pre>')
                            var jsondiv=$('<div></div>')
                            jsondiv.JSONView(xhr["responseJSON"]);
                            pre.html(JSON.stringify(xhr["responseJSON"],null,2));
                            resp1.find(".panel-body").append(jsondiv);
                        }else{
                            //判断content-type
                            //如果是image资源
                            var regex=new RegExp('image/(jpeg|jpg|png|gif)','g');
                            if(regex.test(contentType)){
                                var d=DApiUI.getDoc().data("data");
                                var imgUrl="http://"+d.host+apiInfo.url;
                                var img = document.createElement("img");
                                img.onload = function(e) {
                                    window.URL.revokeObjectURL(img.src); // 清除释放
                                };
                                img.src = imgUrl;
                                resp1.find(".panel-body").html("")
                                resp1.find(".panel-body")[0].appendChild(img);
                            }else{
                                //判断是否是text
                                var regex=new RegExp('.*?text.*','g');
                                if(regex.test(contentType)){
                                    resp1.find(".panel-body").html("")
                                    resp1.find(".panel-body").html(xhr.responseText);
                                }
                            }

                        }

                        DApiUI.log("tab show...")
                        resptab.find("a:first").tab("show");
                    },
                    error:function (xhr, textStatus, errorThrown) {
                        DApiUI.log("error.....")
                        DApiUI.log(xhr);
                        DApiUI.log(textStatus);
                        DApiUI.log(errorThrown);
                        var resptab=$('<div id="resptab" class="tabs-container" ></div>')
                        var ulresp=$('<ul class="nav nav-tabs">' +
                            '<li class=""><a data-toggle="tab" href="#tabresp" aria-expanded="false"> 响应内容 </a></li>' +
                            '<li class=""><a data-toggle="tab" href="#tabcookie" aria-expanded="true"> Cookies</a></li>' +
                            '<li class=""><a data-toggle="tab" href="#tabheader" aria-expanded="true"> Headers </a></li></ul>')

                        resptab.append(ulresp);
                        var respcontent=$('<div class="tab-content"></div>');

                        var resp1=$('<div id="tabresp" class="tab-pane active"><div class="panel-body"><pre></pre></div></div>');
                        var resp2=$('<div id="tabcookie" class="tab-pane active"><div class="panel-body">暂无</div>');
                        var resp3=$('<div id="tabheader" class="tab-pane active"><div class="panel-body">暂无</div></div>');

                        respcontent.append(resp1).append(resp2).append(resp3);

                        resptab.append(respcontent)

                        respcleanDiv.append(resptab);
                        DApiUI.log(xhr);
                        DApiUI.log(xhr.getAllResponseHeaders());
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
                        DApiUI.log("Content-Type:"+contentType);
                        var jsonRegex="";
                        DApiUI.log(xhr.hasOwnProperty("responseJSON"))
                        if (xhr.hasOwnProperty("responseJSON")){
                            //如果存在该对象,服务端返回为json格式
                            resp1.find(".panel-body").html("")
                            DApiUI.log(xhr["responseJSON"])
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
                        DApiUI.log("tab show...")
                        resptab.find("a:first").tab("show");

                    }
                })
            }
        })

    }

    DApiUI.createDebugResponseTab=function (parent, data) {

    }
    
    
    DApiUI.writeUTF8=function (str, isGetBytes) {
        var back = [],
            byteSize = 0;
        for (var i = 0; i < str.length; i++) {
            var code = str.charCodeAt(i);
            if (code >= 0 && code <= 127) {
                byteSize += 1;
                back.push(code);
            } else if (code >= 128 && code <= 2047) {
                byteSize += 2;
                back.push((192 | (31 & (code >> 6))));
                back.push((128 | (63 & code)))
            } else if (code >= 2048 && code <= 65535) {
                byteSize += 3;
                back.push((224 | (15 & (code >> 12))));
                back.push((128 | (63 & (code >> 6))));
                back.push((128 | (63 & code)))
            }
        }
        for (i = 0; i < back.length; i++) {
            if (back[i] > 255) {
                back[i] &= 255
            }
        }
        if (isGetBytes) {
            return back
        }
        if (byteSize <= 255) {
            return [0, byteSize].concat(back);
        } else {
            return [byteSize >> 8, byteSize & 255].concat(back);
        }
    }

    DApiUI.createApiInfoTable=function (apiInfo) {
        var table=$('<table class="table table-hover table-bordered table-text-center"></table>');
        var thead=$('<thead><tr><th colspan="2" style="text-align:center">API接口文档</th></tr></thead>');
        table.append(thead);
        var tbody=$('<tbody></tbody>');

        var url=$('<tr><th class="active" style="text-align: right;">接口url</th><td style="text-align: left"><code>'+DApiUI.getStringValue(apiInfo.url)+'</code></td></tr>');
        tbody.append(url);

        var summary=$('<tr><th class="active" style="text-align: right;">接口名称</th><td style="text-align: left">'+DApiUI.getStringValue(apiInfo.summary)+'</td></tr>');
        tbody.append(summary);


        var description=$('<tr><th class="active" style="text-align: right;">说明</th><td style="text-align: left">'+DApiUI.getStringValue(apiInfo.description)+'</td></tr>');
        tbody.append(description);

        var methodType=$('<tr><th class="active" style="text-align: right;">请求方式</th><td style="text-align: left"><code>'+DApiUI.getStringValue(apiInfo.methodType)+'</code></td></tr>');
        tbody.append(methodType);

        var consumesArr=DApiUI.getValue(apiInfo,"consumes",new Array(),true);


        var consumes=$('<tr><th class="active" style="text-align: right;">consumes</th><td style="text-align: left"><code>'+consumesArr+'</code></td></tr>');
        tbody.append(consumes);

        var producesArr=DApiUI.getValue(apiInfo,"produces",new Array(),true);

        var produces=$('<tr><th class="active" style="text-align: right;">produces</th><td style="text-align: left"><code>'+producesArr+'</code></td></tr>');
        tbody.append(produces);

        //请求参数
        var args=$('<tr><th class="active" style="text-align: right;">请求参数</th></tr>');
        //判断是否有请求参数
        if(typeof (apiInfo.parameters)!='undefined'&&apiInfo.parameters!=null){
            var ptd=$("<td></td>");
            var ptable=$('<table class="table table-bordered"></table>')
            var phead=$('<thead><th>参数名称</th><th>说明</th><th>类型</th><th>in</th><th>是否必须</th></thead>');
            ptable.append(phead);
            var pbody=$('<tbody></tbody>');
            $.each(apiInfo.parameters,function (i, param) {
                //判断是否是ref,如果是，列出他的属性说明
                var refflag=false;
                //判断是否有type属性,如果有,则后端为实体类形参
                var ptype="string";
                if(param.hasOwnProperty("type")){
                    ptype=param["type"];
                }else{
                    ///判断是有schma
                    if(param.hasOwnProperty("schema")){
                        var schema=param["schema"];
                        //是否有type
                        if(schema.hasOwnProperty("type")){
                            ptype=schema["type"];
                        }else if(schema.hasOwnProperty("$ref")){
                            //是否是ref
                            var regex=new RegExp("#/definitions/(.*)$","ig");
                            if(regex.test(schema["$ref"])) {
                                refflag=true;
                                ptype=RegExp.$1;
                            }
                        }
                    }
                }
                var ptr=null;
                //列出属性
                if (refflag){
                    ptr=$('<tr><td>'+param.name+'</td><td style="text-align: center;">'+DApiUI.getStringValue(param['description'])+'</td><td>'+ptype+'</td><td>'+DApiUI.getStringValue(param['in'])+'</td><td>'+param['required']+'</td></tr>');
                    pbody.append(ptr);
                    var definitionsArray=DApiUI.getDoc().data("definitionsArray");
                    var mcs=DApiUI.getMenuConstructs();
                    for(var k in mcs.definitions){
                        if(ptype==k){
                            var tp=mcs.definitions[ptype];
                            var props=tp["properties"];
                            for(var prop in props){
                                var pvalue=props[prop];
                                var tr=$("<tr></tr>")
                                tr.append($("<td style='text-align: right;'>"+prop+"</td>"))
                                tr.append($("<td>"+DApiUI.toString(pvalue.description,"")+"</td>"));
                                var type=DApiUI.toString(pvalue.type,"string");
                                tr.append($("<td>"+type+"</td>"));
                                tr.append($("<td>"+DApiUI.getStringValue(param['in'])+"</td>"));
                                tr.append($("<td>"+param['required']+"</td>"));
                                pbody.append(tr);
                            }
                        }
                    }
                }else{
                    ptr=$('<tr><td>'+param.name+'</td><td style="text-align: center;">'+DApiUI.getStringValue(param['description'])+'</td><td>'+ptype+'</td><td>'+DApiUI.getStringValue(param['in'])+'</td><td>'+param['required']+'</td></tr>');
                    pbody.append(ptr);
                }
            })
            ptable.append(pbody);
            ptd.append(ptable);
            args.append(ptd);
        }else{
            args.append($('<td  style="text-align: left">暂无</td>'));
        }
        tbody.append(args);
        //响应数据结构
        var responseConstruct=$('<tr><th class="active" style="text-align: right;">响应Model</th></tr>');
        var responseConstructtd=$('<td  style="text-align: left"></td>')
        responseConstructtd.append(DApiUI.createResponseDefinition(apiInfo));
        responseConstruct.append(responseConstructtd);
        tbody.append(responseConstruct)

        //响应参数 add by xiaoymin 2017-8-20 16:17:18

        var respParams=$('<tr><th class="active" style="text-align: right;">响应参数说明</th></tr>');
        var respPart=$('<td  style="text-align: left"></td>');
        respPart.append(DApiUI.createResponseDefinitionDetail(apiInfo));
        respParams.append(respPart);

        tbody.append(respParams);

        //响应状态码
        var response=$('<tr><th class="active" style="text-align: right;">响应</th></tr>');
        if(typeof (apiInfo.responses)!='undefined'&&apiInfo.responses!=null){
            var resp=apiInfo.responses;
            var ptd=$("<td></td>");
            var ptable=$('<table class="table table-bordered"></table>')
            var phead=$('<thead><th>状态码</th><th>说明</th><th>schema</th></thead>');
            ptable.append(phead);
            var pbody=$('<tbody></tbody>');
            if(resp.hasOwnProperty("200")){
                var ptr=$('<tr><td>200</td><td>http响应成功</td><td></td></tr>');
                pbody.append(ptr);
            }
            //400
            pbody.append($('<tr><td>400</td><td>Bad Request 请求出现语法错误,一般是请求参数不对</td><td></td></tr>'));
            //404
            pbody.append($('<tr><td>404</td><td>Not Found 无法找到指定位置的资源</td><td></td></tr>'));
            //401
            pbody.append($('<tr><td>401</td><td>Unauthorized 访问被拒绝</td><td></td></tr>'));
            //403
            pbody.append($('<tr><td>403</td><td>Forbidden 资源不可用</td><td></td></tr>'));
            //500
            pbody.append($('<tr><td>500</td><td>服务器内部错误,请联系Java后台开发人员!!!</td><td></td></tr>'));
            ptable.append(pbody);
            ptd.append(ptable);
            response.append(ptd);
        }else{
            response.append($("<td>暂无</td>"));
        }
        tbody.append(response);
        table.append(tbody);

        DApiUI.creatabTab();
        //内容覆盖
        //DApiUI.getDoc().html("");
        //查找接口doc
        DApiUI.getDoc().find("#tab1").find(".panel-body").html("")
        DApiUI.getDoc().find("#tab1").find(".panel-body").append(table);
        //DApiUI.getDoc().append(table);

    }

    /***
     * 响应参数详情
     * @param apiInfo
     */
    DApiUI.createResponseDefinitionDetail=function(apiInfo){
        var resp=apiInfo.responses;
        var div=$("<div class='panel'></div>");
        if(resp.hasOwnProperty("200")) {
            var ok = resp["200"];
            if (ok.hasOwnProperty("schema")) {
                var schema = ok["schema"];
                var ref = schema["$ref"];
                var regex = new RegExp("#/definitions/(.*)$", "ig");
                if (regex.test(ref)) {
                    var refType = RegExp.$1;
                    var definitionsArray=DApiUI.getDoc().data("definitionsArray");
                    var mcs=DApiUI.getMenuConstructs();
                    for(var k in mcs.definitions){
                        if(refType==k){
                            var table=$("<table class=\"table table-bordered\">");
                            table.append('<thead><tr><th>参数名称</th><th>类型</th><th>说明</th></tr></thead>');
                            var tp=mcs.definitions[refType];
                            var props=tp["properties"];

                            var tbody=$("<tbody></tbody>")
                            for(var prop in props){
                                var pvalue=props[prop];
                                var tr=$("<tr></tr>")
                                //只遍历一级属性
                                //判断是否是ref
                                if(pvalue.hasOwnProperty("$ref")){
                                    var param_ref = pvalue["$ref"];
                                    var regex1 = new RegExp("#/definitions/(.*)$", "ig");
                                    if(regex1.test((param_ref))){
                                        var ptype=RegExp.$1;
                                        tr.append($("<td>"+prop+"</td>"))
                                        tr.append($("<td>"+ptype+"</td>"))
                                        tr.append($("<td></td>"))
                                        tbody.append(tr);
                                        for(var j in mcs.definitions) {
                                            if (ptype == j) {
                                                var tpp=mcs.definitions[ptype];
                                                var pp_props=tpp["properties"];
                                                for(var prop1 in pp_props) {
                                                    var tr1=$("<tr></tr>")
                                                    var pvalue1 = pp_props[prop1];
                                                    tr1.append($("<td style='text-align: right;'>" + prop1 + "</td>"));
                                                    tr1.append($("<td>"+DApiUI.getValue(pvalue1,"type","string",true)+"</td>"));
                                                    tr1.append($("<td>"+DApiUI.getValue(pvalue1,"description","",true)+"</td>"));
                                                    tbody.append(tr1);
                                                }
                                            }
                                        }
                                    }
                                }else{
                                    tr.append($("<td>"+prop+"</td>"))
                                    var type=DApiUI.toString(pvalue.type,"string");
                                    tr.append($("<td>"+type+"</td>"));
                                    tr.append($("<td>"+DApiUI.toString(pvalue.description,"")+"</td>"));
                                    tbody.append(tr);
                                }
                            }
                            table.append(tbody);
                            div.append(table)
                        }
                    }
                }
            }
        }
        return div;
    }


    DApiUI.createResponseDefinition=function (apiInfo) {
        var resp=apiInfo.responses;
        var div=$("<div class='panel'>暂无</div>");
        if(resp.hasOwnProperty("200")){
            var ok=resp["200"];
            if(ok.hasOwnProperty("schema")){
                var schema=ok["schema"];
                var ref=schema["$ref"];
                var regex=new RegExp("#/definitions/(.*)$","ig");
                if(regex.test(ref)) {
                    var refType = RegExp.$1;
                    //这里去definitionsArrar查找,如果未找到,直接展示refType
                    var flag=false;
                    var htmlValue=refType;
                    var definitionsArray=DApiUI.getDoc().data("definitionsArray");
                    for(var i=0;i<definitionsArray.length;i++){
                        var definition=definitionsArray[i];
                        if(definition.key==refType){
                            flag=true;
                            htmlValue=definition.value;
                            break;

                        }
                    }
                    div.html("")
                    if (flag){
                        div.JSONView(htmlValue);
                    }else{
                        div.html(refType);
                    }
                }else{
                    //未发现ref属性
                    if(schema.hasOwnProperty("type")){
                        div.html("")
                        div.html(schema["type"]);
                    }
                }
            }
        }
        return div;
    }


    DApiUI.definitions=function (menu) {
        var definitionsArray=new Array();
        DApiUI.log("definitionsArray....")
        if(menu!=null&&typeof (menu)!="undefined"&&menu.hasOwnProperty("definitions")){
            var definitions=menu["definitions"];
            for(var definition in definitions){
                var defiType=new definitionType();
                defiType.key=definition;
                //获取value
                var value=definitions[definition];
                if (checkUndefined(value)){
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
                                if(propobj.hasOwnProperty("example")){
                                    propValue=propobj["example"];
                                }else if(checkIsBasicType(type)){
                                    propValue=getBasicTypeValue(type);
                                }else{
                                    if(type=="array"){
                                        propValue=new Array();
                                        var items=propobj["items"];
                                        var ref=items["$ref"];
                                        var regex=new RegExp("#/definitions/(.*)$","ig");
                                        if(regex.test(ref)){
                                            var refType=RegExp.$1;
                                            //这里需要递归判断是否是本身,如果是,则退出递归查找
                                            if(refType!=definition){
                                                propValue.push(findRefDefinition(refType,definitions));
                                            }
                                        }
                                    }
                                }

                            }else{
                                if(propobj.hasOwnProperty("$ref")){
                                    var ref=propobj["$ref"];
                                    var regex=new RegExp("#/definitions/(.*)$","ig");
                                    if(regex.test(ref)) {
                                        var refType = RegExp.$1;
                                        //这里需要递归判断是否是本身,如果是,则退出递归查找
                                        if(refType!=definition){
                                            propValue=findRefDefinition(refType,definitions);
                                        }else{
                                            propValue={};
                                        }

                                    }
                                }else{
                                    propValue={};
                                }
                            }
                            defiTypeValue[property]=propValue;
                        }
                        defiType.value=defiTypeValue;
                    }else{
                        defiType.value={};
                    }
                }
                definitionsArray.push(defiType);
            }
        }
        DApiUI.getDoc().data("definitionsArray",definitionsArray);
    }

    DApiUI.getDefinitions=function () {
        return DApiUI.getDoc().data("definitionsArray");
    }

    function checkIsBasicType(type) {
        var basicTypes=["string","integer","number","object","boolean"];
        var flag=false;
        if($.inArray(type,basicTypes)>-1){
            flag=true;
        }
        return flag;
    }

    function getBasicTypeValue(type) {
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
    }

    function findRefDefinition(definitionName, definitions) {
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
                            }else if(checkIsBasicType(type)){
                                propValue=getBasicTypeValue(type);
                            }else{
                                if(type=="array"){
                                    propValue=new Array();
                                    var items=propobj["items"];
                                    var ref=items["$ref"];
                                    var regex=new RegExp("#/definitions/(.*)$","ig");
                                    if(regex.test(ref)){
                                        var refType=RegExp.$1;
                                        if(refType!=definitionName){
                                            propValue.push(findRefDefinition(refType,definitions));
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
    function checkUndefined(obj) {
        var flag=false;
        if(obj!=null&&typeof (obj)!="undefined"){
            flag=true;
        }
        return flag;
    }


    function definitionType() {
        this.key="";
        this.value={};
    }


    /***
     * 获取默认请求参数类型
     * @param obj
     * @returns {string}
     */
    DApiUI.getDefaultRequiredType=function (obj) {
        var t="string";
        if(typeof (obj)!='undefined'&&obj!=null){
            t=obj.toString();
        }
        return  t;
    }

    /***
     * 查找子类
     * @param tagInfo
     * @param menu
     */
    DApiUI.initChildrens=function (tagInfo, menu) {

    }

    DApiUI.getDoc=function () {
        return $("#content");
    }
    DApiUI.getMenu=function () {
        return $("#menu");
    }

    DApiUI.log=function (msg) {
        if (window.console){
            console.log(msg);
        }
    }
    DApiUI.init();


    /***
     * 标签组信息
     * @constructor
     */
    function TagInfo(name,description) {
        this.name=name;
        this.description=description;
        this.childrens=new Array();
    }


    /***
     * api实体信息
     * @param options
     * @constructor
     */
    function ApiInfo(options) {
        //判断options
        this.tag="";
        this.url="";
        this.description="";
        this.operationId="";
        this.parameters=new Array();
        this.produces=new Array();
        this.responses={};
        this.methodType="post";
        this.consumes=new Array();
        this.summary="";
        if(options!=null&& typeof (options)!='undefined' ){
            this.tag=options.tags[0];
            this.description=options.description;
            this.operationId=options.operationId;
            this.summary=options.summary;
            this.parameters=options.parameters;
            this.produces=options.produces;
            this.responses=options.responses;
            this.consumes=options.consumes;
        }

    }




})(jQuery)