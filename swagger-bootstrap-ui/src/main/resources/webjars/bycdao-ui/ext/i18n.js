(function () {
    var i18n=function () {
        //默认中文
        this.language="zh";
        //目前支持语言版本
        this.supports=[{
            lang:"zh",title:"中文"
        },{
            lang:"en",title:"English"
        }];
        this.instance=null;

        //获取当前浏览器语言
        if(window.navigator){
            var lang =(navigator.language || navigator.browserLanguage);
            if(lang!=null&&lang!=undefined&&lang!=""){
                lang=lang.toLowerCase();
                if (lang.indexOf("en")>0){
                    this.language="en";
                }
            }
        }

        if (this.language=="zh"){
            this.instance=i18n_instance.zh;
        }else{
            this.instance=i18n_instance.en;
        }
        //this.init();
    }

    i18n.prototype={
        getSupportLanguage:function (lang) {
            if(lang=="en"){
                return i18n_instance.en;
            }
            return i18n_instance.zh;
        }
    }


    var i18n_instance={
        zh:{
            title:"swagger-bootstrap-ui-前后端api接口文档",
            message:{
                success:"保存成功",
                layer:{
                    title:"信息",
                    yes:"确定",
                    no:"取消"
                },
                auth:{
                    invalid:"值无效",
                    confirm:"确定注销吗?",
                    success:"注销成功"
                }
            },
            home:{
                des:"简介1212",
                author:"作者",
                version:"版本",
                serviceUrl:"服务Url",
                groupName:"分组名称",
                groupUrl:"分组url",
                groupLocation:"分组Location",
                apiCount:"接口统计信息",
                searchText:"请输入搜索内容"
            },
            auth:{
                cancel:"注销",
                save:"保存",
                tableHeader:{
                    key:"参数key",
                    name:"参数名称",
                    in:"in",
                    value:"参数值",
                    operator:"操作"
                },
                valueInvalid:"值无效"
            },
            menu:{
                home:"主页",
                manager:"文档管理",
                globalsettings:"全局参数设置",
                officeline:"离线文档(MD)",
                selfSettings:"个性化设置"
            },
            doc:{
                title:"文档",
                copy:"复制文档",
                url:"接口地址",
                method:"请求方式",
                des:"接口描述",
                params:"请求参数",
                paramsHeader:{

                },
                response:"响应状态",
                responseHeader:{

                },
                responseParams:"响应参数",
                responseParamsHeader:{

                },
                responseExample:"响应示例"

            },
            debug:{
                title:"调试",
                send:"发送"

            }

        },
        en:{
            title:"swagger-bootstrap-ui RESTful Api Docs",
            message:{
                success:"Save successfully",
                layer:{
                    title:"message",
                    yes:"Yes",
                    no:"No"
                },
                auth:{
                    invalid:"Invalid value",
                    confirm:"Are you sure you want to logout?",
                    success:"Logout Success"
                }
            },
            home:{
                des:"Description",
                author:"Author",
                version:"Version",
                serviceUrl:"serviceUrl",
                groupName:"Group Name",
                groupUrl:"Group url",
                groupLocation:"Group Location",
                apiCount:"Api Counts",
                searchText:"Search..."
            },
            auth:{
                cancel:"Logout",
                save:"Save",
                tableHeader:{
                    key:"key",
                    name:"name",
                    in:"in",
                    value:"value",
                    operator:"operate"
                },
                valueInvalid:"Invalid Value"
            },
            menu:{
                home:"Home",
                manager:"Doc Manager",
                globalsettings:"Global Params Settings",
                officeline:"Offince-Line(MD)",
                selfSettings:"Settings"
            },
            doc:{
                title:"Doc",
                copy:"Copy",
                url:"url",
                method:"method",
                des:"Description",
                params:"Request Params",
                paramsHeader:{

                },
                response:"Response Status",
                responseHeader:{

                },
                responseParams:"Response Params",
                responseParamsHeader:{

                },
                responseExample:"Response Example"

            },
            debug:{
                title:"Debug",
                send:"Send"

            }
        }
    }

    window.I18n=i18n;
})()