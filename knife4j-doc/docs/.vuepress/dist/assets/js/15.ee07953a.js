(window.webpackJsonp=window.webpackJsonp||[]).push([[15],{405:function(e,t,a){"use strict";a.r(t);var r=a(42),n=Object(r.a)({},(function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("ContentSlotsDistributor",{attrs:{"slot-key":e.$parent.slotKey}},[a("h1",{attrs:{id:"_5-2-net-core直接集成knife4j"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#_5-2-net-core直接集成knife4j"}},[e._v("#")]),e._v(" 5.2 .NET Core直接集成Knife4j")]),e._v(" "),a("h2",{attrs:{id:"igeekfan-aspnetcore-knife4jui"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#igeekfan-aspnetcore-knife4jui"}},[e._v("#")]),e._v(" "),a("a",{attrs:{href:"https://github.com/luoyunchong/IGeekFan.AspNetCore.Knife4jUI",target:"_blank",rel:"noopener noreferrer"}},[e._v("IGeekFan.AspNetCore.Knife4jUI"),a("OutboundLink")],1)]),e._v(" "),a("p",[e._v("一个swagger ui 库："),a("strong",[a("a",{attrs:{href:"https://gitee.com/xiaoym/knife4j",target:"_blank",rel:"noopener noreferrer"}},[e._v("knife4j UI"),a("OutboundLink")],1)]),e._v("，支持 .NET Core3.0+或.NET Standard2.0。")]),e._v(" "),a("p",[a("a",{attrs:{href:"https://www.nuget.org/packages/IGeekFan.AspNetCore.Knife4jUI",target:"_blank",rel:"noopener noreferrer"}},[a("img",{attrs:{src:"https://img.shields.io/nuget/v/IGeekFan.AspNetCore.Knife4jUI.svg?style=flat-square",alt:"nuget"}}),a("OutboundLink")],1),e._v(" "),a("a",{attrs:{href:"https://www.nuget.org/stats/packages/IGeekFan.AspNetCore.Knife4jUI?groupby=Version",target:"_blank",rel:"noopener noreferrer"}},[a("img",{attrs:{src:"https://img.shields.io/nuget/dt/IGeekFan.AspNetCore.Knife4jUI.svg?style=flat-square",alt:"stats"}}),a("OutboundLink")],1),e._v(" "),a("a",{attrs:{href:"https://raw.githubusercontent.com/luoyunchong/IGeekFan.AspNetCore.Knife4jUI/master/LICENSE.txt",target:"_blank",rel:"noopener noreferrer"}},[a("img",{attrs:{src:"https://img.shields.io/badge/license-Apache-blue.svg",alt:"GitHub license"}}),a("OutboundLink")],1)]),e._v(" "),a("h2",{attrs:{id:"相关依赖项"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#相关依赖项"}},[e._v("#")]),e._v(" 相关依赖项")]),e._v(" "),a("h3",{attrs:{id:"knife4j"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#knife4j"}},[e._v("#")]),e._v(" "),a("a",{attrs:{href:"https://gitee.com/xiaoym/knife4j",target:"_blank",rel:"noopener noreferrer"}},[e._v("knife4j"),a("OutboundLink")],1)]),e._v(" "),a("ul",[a("li",[e._v("knife4j-vue-v3(不是vue3,而是swagger-ui-v3版本）")])]),e._v(" "),a("h3",{attrs:{id:"swashbuckle-aspnetcore"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#swashbuckle-aspnetcore"}},[e._v("#")]),e._v(" "),a("a",{attrs:{href:"https://github.com/domaindrivendev/Swashbuckle.AspNetCore",target:"_blank",rel:"noopener noreferrer"}},[e._v("Swashbuckle.AspNetCore"),a("OutboundLink")],1)]),e._v(" "),a("ul",[a("li",[e._v("Swashbuckle.AspNetCore.Swagger")]),e._v(" "),a("li",[e._v("Swashbuckle.AspNetCore.SwaggerGen")])]),e._v(" "),a("h2",{attrs:{id:"demo"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#demo"}},[e._v("#")]),e._v(" Demo")]),e._v(" "),a("ul",[a("li",[a("a",{attrs:{href:"https://github.com/luoyunchong/IGeekFan.AspNetCore.Knife4jUI/blob/master/test/Basic",target:"_blank",rel:"noopener noreferrer"}},[e._v("Basic"),a("OutboundLink")],1)]),e._v(" "),a("li",[a("a",{attrs:{href:"https://github.com/luoyunchong/IGeekFan.AspNetCore.Knife4jUI/blob/master/test/Knife4jUIDemo",target:"_blank",rel:"noopener noreferrer"}},[e._v("Knife4jUIDemo"),a("OutboundLink")],1)])]),e._v(" "),a("h2",{attrs:{id:"📚-快速开始"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#📚-快速开始"}},[e._v("#")]),e._v(" 📚 快速开始")]),e._v(" "),a("h3",{attrs:{id:"🚀安装包"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#🚀安装包"}},[e._v("#")]),e._v(" 🚀安装包")]),e._v(" "),a("p",[e._v("以下为使用Swashbuckle.AspNetCore.Swagger底层组件")]),e._v(" "),a("p",[e._v("1.Install the standard Nuget package into your ASP.NET Core application.")]),e._v(" "),a("div",{staticClass:"language- extra-class"},[a("pre",{pre:!0,attrs:{class:"language-text"}},[a("code",[e._v("Package Manager : \n\nInstall-Package Swashbuckle.AspNetCore.Swagger\nInstall-Package Swashbuckle.AspNetCore.SwaggerGen\nInstall-Package IGeekFan.AspNetCore.Knife4jUI\n\nOR\n\nCLI :\n\ndotnet add package Swashbuckle.AspNetCore.Swagger\ndotnet add package Swashbuckle.AspNetCore.SwaggerGen\ndotnet add package IGeekFan.AspNetCore.Knife4jUI\n")])])]),a("p",[e._v("2.In the ConfigureServices method of Startup.cs, register the Swagger generator, defining one or more Swagger documents.")]),e._v(" "),a("div",{staticClass:"language- extra-class"},[a("pre",{pre:!0,attrs:{class:"language-text"}},[a("code",[e._v("using Microsoft.AspNetCore.Mvc.Controllers\nusing Microsoft.OpenApi.Models;\nusing Swashbuckle.AspNetCore.SwaggerGen;\nusing IGeekFan.AspNetCore.Knife4jUI;\n")])])]),a("h3",{attrs:{id:"🚁-configureservices"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#🚁-configureservices"}},[e._v("#")]),e._v(" 🚁 ConfigureServices")]),e._v(" "),a("p",[e._v("3.服务配置，CustomOperationIds和AddServer是必须的。")]),e._v(" "),a("div",{staticClass:"language- extra-class"},[a("pre",{pre:!0,attrs:{class:"language-text"}},[a("code",[e._v('   services.AddSwaggerGen(c =>\n    {\n        c.SwaggerDoc("v1",new OpenApiInfo{Title = "API V1",Version = "v1"});\n        c.AddServer(new OpenApiServer()\n        {\n            Url = "",\n            Description = "vvv"\n        });\n        c.CustomOperationIds(apiDesc =>\n        {\n            var controllerAction = apiDesc.ActionDescriptor as ControllerActionDescriptor;\n            return  controllerAction.ControllerName+"-"+controllerAction.ActionName;\n        });\n    });\n')])])]),a("h3",{attrs:{id:"💪-configure"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#💪-configure"}},[e._v("#")]),e._v(" 💪 Configure")]),e._v(" "),a("ol",{attrs:{start:"4"}},[a("li",[e._v("中间件配置")])]),e._v(" "),a("div",{staticClass:"language- extra-class"},[a("pre",{pre:!0,attrs:{class:"language-text"}},[a("code",[e._v('app.UseSwagger();\n\napp.UseKnife4UI(c =>\n{\n    c.RoutePrefix = ""; // serve the UI at root\n    c.SwaggerEndpoint("/v1/api-docs", "V1 Docs");\n});\n\napp.UseEndpoints(endpoints =>\n{\n    endpoints.MapControllers();\n    endpoints.MapSwagger("{documentName}/api-docs");\n});\n')])])]),a("p",[e._v("5.更多功能")]),e._v(" "),a("p",[e._v("为文档添加注释 在项目上右键--属性--生成")]),e._v(" "),a("p",[a("img",{attrs:{src:"https://pic.downk.cc/item/5f34161d14195aa59413f0fc.jpg",alt:""}})]),e._v(" "),a("p",[e._v("在AddSwaggerGen方法中添加如下代码")]),e._v(" "),a("div",{staticClass:"language- extra-class"},[a("pre",{pre:!0,attrs:{class:"language-text"}},[a("code",[e._v('c.IncludeXmlComments(Path.Combine(AppContext.BaseDirectory, "SwaggerDemo.xml"),true);\n')])])]),a("p",[e._v("最后一个参数设置为true，代表启用控制器上的注释")]),e._v(" "),a("p",[e._v("运行后如看不到控制器上注释显示，请点开文档管理->个性化设置，开启分组tag显示description说明属性")]),e._v(" "),a("p",[a("img",{attrs:{src:"https://pic.downk.cc/item/5f34171114195aa594142d2e.jpg",alt:""}})]),e._v(" "),a("h3",{attrs:{id:"nswag-aspnetcore"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#nswag-aspnetcore"}},[e._v("#")]),e._v(" NSwag.AspNetCore")]),e._v(" "),a("p",[e._v("（请参考目录test/WebSites/NSwag.Swagger.Knife4jUI）")]),e._v(" "),a("div",{staticClass:"language- extra-class"},[a("pre",{pre:!0,attrs:{class:"language-text"}},[a("code",[e._v("public void ConfigureServices(IServiceCollection services)\n {\n    // 其它Service\n     services.AddOpenApiDocument();\n }\n")])])]),a("div",{staticClass:"language- extra-class"},[a("pre",{pre:!0,attrs:{class:"language-text"}},[a("code",[e._v('public void Configure(IApplicationBuilder app, IWebHostEnvironment env)\n{\n            // 其它 Use\n          app.UseOpenApi();\n          app.UseKnife4UI(c =>\n         {\n               c.RoutePrefix = "";\n               c.SwaggerEndpoint("/swagger/v1/swagger.json");\n          });\n}\n')])])]),a("p",[e._v("即可使用 Knife4jUI")]),e._v(" "),a("h3",{attrs:{id:"🔎-效果图"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#🔎-效果图"}},[e._v("#")]),e._v(" 🔎 效果图")]),e._v(" "),a("p",[e._v("运行项目，打开 https://localhost:5001/index.html#/home")]),e._v(" "),a("p",[a("img",{attrs:{src:"https://pic.downk.cc/item/5f2fa77b14195aa594ccbedc.jpg",alt:"https://pic.downk.cc/item/5f2fa77b14195aa594ccbedc.jpg"}})]),e._v(" "),a("h3",{attrs:{id:"更多配置请参考"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#更多配置请参考"}},[e._v("#")]),e._v(" 更多配置请参考")]),e._v(" "),a("ul",[a("li",[a("a",{attrs:{href:"https://github.com/domaindrivendev/Swashbuckle.AspNetCore",target:"_blank",rel:"noopener noreferrer"}},[e._v("https://github.com/domaindrivendev/Swashbuckle.AspNetCore"),a("OutboundLink")],1)])]),e._v(" "),a("h3",{attrs:{id:"更多项目"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#更多项目"}},[e._v("#")]),e._v(" 更多项目")]),e._v(" "),a("ul",[a("li",[a("a",{attrs:{href:"https://api.igeekfan.cn/swagger/index.html",target:"_blank",rel:"noopener noreferrer"}},[e._v("https://api.igeekfan.cn/swagger/index.html"),a("OutboundLink")],1)]),e._v(" "),a("li",[a("a",{attrs:{href:"https://github.com/luoyunchong/lin-cms-dotnetcore",target:"_blank",rel:"noopener noreferrer"}},[e._v("https://github.com/luoyunchong/lin-cms-dotnetcore"),a("OutboundLink")],1),e._v(" "),a("img",{attrs:{src:"https://pic.downk.cc/item/5f2fa97814195aa594cd5cfc.jpg",alt:"image"}})])]),e._v(" "),a("comment-comment")],1)}),[],!1,null,null,null);t.default=n.exports}}]);