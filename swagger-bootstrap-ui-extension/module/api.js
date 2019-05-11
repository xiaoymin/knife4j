(function($){
    var apiHtml=function(){

    }

    apiHtml.prototype={
        init:function(){
            this.initButtons();
        },
        initButtons:function(){
            var that=this;
            //init buttons events
            $("#btnPreview").click(function(e){
                e.preventDefault();
                //获取名称
                var type=$("input:radio[name=type]:checked").val();
                var url=$("#address").val();
                var reg=/((http|https):\/\/.*?)\/.*/g;
                if(!url){
                    layer.msg("请求url非法");
                    return false;
                }
                if(!reg.test(url)){
                    layer.msg("请求url非法");
                    return false;
                }
                var origin=RegExp.$1;
                console.log("origin:"+origin);
                var dataArr=new Array();
                try{
                    layer.load(2);
                    $.ajax({
                        url:url,
                        async:false,
                        success:function(data){
                            if(type=="group"){
                                if(!Array.isArray(data)){
                                    return false;
                                }
                                $.each(data,function(i,d){
                                    var name=d.name;
                                    var singleUrl=origin+d.url;
                                    console.log(singleUrl);
                                    $.ajax({
                                        url:singleUrl,
                                        async:false,
                                        success:function(sd){
                                            dataArr.push({name:name,location:"",url:"", swaggerVersion: "2.0",data:sd});
                                        }
                                    })
                                })
                            }else{
                                var tp=typeof(data);
                                console.log(tp);
                                if(tp!="object"){
                                    return false;
                                }
                                if(Array.isArray(data)){
                                    return false;
                                }
                                if(!data.hasOwnProperty("swagger")
                                ||!data.hasOwnProperty("tags")
                                ||!data.hasOwnProperty("paths")
                                ||!data.hasOwnProperty("definitions")){
                                    return false;
                                }
                                //非分组资源
                                var swaggerData={name:"default",location:"",url:"", swaggerVersion: "2.0",data:data};
                                dataArr.push(swaggerData);
                            }
                        },
                        error:function(xhr,status,err){
                            layer.msg("请求接口出错:"+err);
                        }
                    })

                    layer.closeAll();
                    if(dataArr.length==0){
                        layer.msg("请求接口资源格式非法");
                        return false;
                    }
                    console.log(dataArr)
                    Store.set(SwaggerBootstrapUiGlobal.cache.json,dataArr);
                    Store.set(SwaggerBootstrapUiGlobal.cache.type,'api');
                    window.location=chrome.extension.getURL('doc.html');
                }catch(e){
                    layer.msg("请求非法:"+e);
                }
            })

        }
    }


    var ah=new apiHtml();
    ah.init();
})(jQuery)