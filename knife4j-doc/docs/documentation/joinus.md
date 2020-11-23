# 2.2 参与其中

我们欢迎您参与`Knife4j`项目。有很多贡献方式，包括回答有关issues的问题，编写新代码，改进现有代码，协助编写文档，开发示例或教程，报告错误或仅提出建议。有关更多信息，请参见我们的[贡献](https://gitee.com/xiaoym/knife4j/issues/IZUCU)文档。


**如何贡献文档**


目前Knife4j的新版本文档启用Gitee提供的Pages服务,开发者可以直接通过编写Markdown文档的方式执行参与文档贡献。你的文档贡献能帮助到每一个使用Knife4j的人，社区的壮大离不开每个开发者的贡献。


Knife4j的文档通过VuePress进行编写，文档源码位于[Knife4j](https://gitee.com/xiaoym/knife4j/tree/dev/)项目dev分支下的`knife4j-doc`目录

主要步骤：

1、开发者对仓库进行fork操作，然后pull到本地

2、本地安装VuePress,具体可以参考[官方文档](https://vuepress.vuejs.org/zh/)
> 需要安装Node环境

3、安装本地依赖`yarn install`

4、运行Knife4j-doc文档查看效果`npm run docs:dev`,Windows用户可以直接调用`start.bat`命令


**文档目录说明：**

目前Knife4j-doc文档中主要包含4个顶级目录：
|目录名称|说明|
|----|---|
|documentation|用户指南,Knife4j的软件特性介绍位于此文件夹下|
|action|实战指南,欢迎开发者贡献所有关于Knife4j实战部分的文章|
|oas|OAS规范，OAS规范介绍，可以包含不同语言端的注解或说明|
|changelog|Knife4j详细更新日志|
|faq|FAQ问题列表|

每个不同的目录中，开发者只需要在该目录创建markdown文档即可，一篇markdown文档即代表一篇文档说明，最终在`.vuepress`目录下的`config.js`中添加该markdown文档到相应的目录显示即可

例如：实战指南的目录规则：
```javascript
function genActionSideBar() {
    return [
        {
            title: "1.Spring单体架构",
            collapsable: false,
            children: ["mavenbom.md", "springmvc", "springboot"]
        },
        {
            title: "2.Spring微服务架构",
            collapsable: false,
            children: ["springcloud-gateway", "springcloud-zuul.md"]
        },
        {
            title: "3.OAuth2.0",
            collapsable: false,
            children: ["oauth2-implicit.md", "oauth2-authorization_code.md", "oauth2-client_credentials.md", "oauth2-password.md"]
        },
        {
            title: "4. ASP.NET Core",
            collapsable: false,
            children: ["dotnetcore-knife4j-how.md", "dotnetcore-knife4j-guid.md"]
        }
    ]
}
```

如果有更多的实战文档，开发者可以现在`action`目录创建好与之对应的Markdown文档，然后在这里自行添加目录

**需要注意的是，如果篇幅中有图片，且不是网络图片，贡献者可以将图片放在public的目录中，但是通过MD规范进行引用的时候，需要添加`/knife4j`的前缀，否则会出现图片404的情况**


文档写完后，就可以在Knife4j的Gitee仓库中向作者发起PR请求了，需要将PR请求提交到**dev分支**！！！


