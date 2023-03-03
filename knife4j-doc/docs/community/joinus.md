# 如何贡献文档

我们欢迎您参与`Knife4j`项目。有很多贡献方式，包括回答有关issues的问题，编写新代码，改进现有代码，协助编写文档，开发示例或教程，报告错误或仅提出建议。有关更多信息，请参见我们的[贡献](https://gitee.com/xiaoym/knife4j/issues/IZUCU)文档。


**如何贡献文档**


目前Knife4j的新版本文档开发者可以直接通过编写Markdown文档的方式执行参与文档贡献。你的文档贡献能帮助到每一个使用Knife4j的人，社区的壮大离不开每个开发者的贡献。


Knife4j的文档通过Docusaurus进行编写，文档源码位于[Knife4j](https://gitee.com/xiaoym/knife4j/tree/dev/)项目dev分支下的`knife4j-doc`目录

主要步骤：

1、开发者对仓库进行fork操作，然后pull到本地

2、本地安装Docusaurus ,具体可以参考[官方文档](https://docusaurus.io/zh-CN/docs)

> 需要安装Node环境,Node版本必须符合Docusaurus要求

3、安装本地依赖`npm install`

4、运行Knife4j-doc文档查看效果`npm run start`


**文档目录说明：**

目前Knife4j-doc文档中主要包含4个顶级目录：

|目录名称|说明|
|----|---|
|middleware|中间件|
|middleware-sources|中间件|
|features|增强属性|
|action|实战指南,欢迎开发者贡献所有关于Knife4j实战部分的文章|
|oas|OAS规范，OAS规范介绍，可以包含不同语言端的注解或说明|
|changelog|Knife4j详细更新日志|
|faq|FAQ问题列表|

每个不同的目录中，开发者只需要在该目录创建markdown文档即可，一篇markdown文档即代表一篇文档说明，最终在`docusaurus.config.js`中添加该markdown文档到相应的目录显示即可


如果有更多的实战文档，开发者可以现在`action`目录创建好与之对应的Markdown文档，然后在这里自行添加目录

**需要注意的是，如果篇幅中有图片，且不是网络图片，贡献者可以将图片放在static/images的目录中.**


文档写完后，就可以在Knife4j的Gitee仓库中向作者发起PR请求了，需要将PR请求提交到**dev分支**！！！


