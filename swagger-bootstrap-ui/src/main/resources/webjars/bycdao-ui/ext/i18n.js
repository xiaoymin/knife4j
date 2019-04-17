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
                },
                global:{
                    iptname:"请输入全局参数名称",
                    iptvalue:"请输入全局参数值",
                    deleteSuccess:"删除成功"
                }
            },
            home:{
                des:"简介",
                author:"作者",
                version:"版本",
                serviceUrl:"服务Url",
                groupName:"分组名称",
                groupUrl:"分组url",
                groupLocation:"分组Location",
                apiCount:"接口统计信息",
                searchText:"请输入搜索内容"
            },
            global:{
                tab:"全局参数设置",
                add:"添加参数",
                tableHeader:{
                    name:"参数名称",
                    value:"参数值",
                    type:"参数类型",
                    operator:"操作"
                },
                save:"保存",
                delete:"删除",
                note:"swagger-bootstrap-ui 提供全局参数Debug功能,目前默认提供header(请求头)、query(form)两种方式的入参.<br /><br />在此添加全局参数后,默认Debug调试tab页会带上该参数,该全局参数只在该分组下有效,不同的分组需要分别设置"

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
                },
                global:{
                    iptname:"Please enter the global parameter name",
                    iptvalue:"Please enter the global parameter value",
                    deleteSuccess:"Delete Success"
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
            global:{
                tab:"Global Parameter Settings",
                add:"Add",
                tableHeader:{
                    name:"name",
                    value:"value",
                    type:"type",
                    operator:"operate"
                },
                save:"Save",
                delete:"Delete",
                note:"swagger-bootstrap-ui Provide global parameter Debug function, currently default to provide header (request header), query (form) two ways of entry.<br /><br />After adding the global parameter here, the default Debug debug tab page will take this parameter, which is valid only under this group, and different groups need to be set separately."

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
                globalsettings:"Global Parameter Settings",
                officeline:"Offline document(MD)",
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