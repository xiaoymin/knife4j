# 基于源码来改造得到Front版本

## 源码结构说明

在[上一篇](knife4j-front-execute.md)中,我们介绍了`knife4j`的本地运行方式以及`knife4j`的整个运行解析过程

本篇主要将怎么通过改造`knife4j`的源码来得到Front版本

从上一篇的执行解析图中,我们可以知道,`/src/layouts/BasicLayout.vue`文件是整个软件的主入口

在该文件中的`create()`方法中主要存在两个方法：
- `this.initKnife4jSpringUi()`:该方法主要是结合Springfox的组件打包构建来使用的,Java技术体系者需要使用该方法调用
- `this.initKnife4jFront()`:非Java体系使用,仅仅传入不同的参数来构造属于自己特定的版本

java技术栈的本文不再阐述,本文主要讲解如何使用非java技术体系的结构来运行调通`knife4j`

现在来看`initKnife4jFront()`方法：

```js
initKnife4jFront(){
  var that = this;
  //初始化swagger文档
  var url = this.$route.path;
  var plusFlag = false;
  if (url == "/plus") {
    //开启增强
    plusFlag = true;
  }
  this.swagger = new SwaggerBootstrapUi({
    Vue: that,
    plus: plusFlag,
    //禁用config的url调用
    configSupport: false,
    //覆盖url地址,多个服务的组合
    url: "services.json"
  });
  try {
    this.swagger.main();
  } catch (e) {
    console.error(e);
  }
}

```

该方法很简单,初始化一个`SwaggerBootstrapUi`的对象,然后调用其`main()`方法

`SwaggerBootstrapUi`是`Knife4j`封装的核心JS对象,位于文件`/src/core/Knife4j.js`中,定义如下图：

![](/knife4j/images/knife4j/knife4j-const.png)

option核心参数的意义说明如下：

|参数|说明|
|--|---|
|Vue|当前Vue实例化对象,传入该对象主要是在请求得到Swagger的操作后,初始化整个服务分组下拉框以及菜单|
|plus|是否启用增强的操作,关于前端增强启用的方法请[移步](autoEnableKnife4j.md)|
|configSupport|该参数传入布尔值,代表是否需要调用Swagger的配置接口,获取后端的配置信息,我们在Front版本该参数应该传入false,即不需要个性化的配置|
|configUrl|个性化配置的方法地址,如果启用个性化配置,则需要传入该地址|
|url|该地址是服务分组的接口地址,即我们在Swagger界面见到的下拉框数据|

针对`configUrl`以及`url`所返回的JSON数据结构实例如下：

个性化配置接口`configUrl` 返回的JSON:
```json

{
    "deepLinking": true,
    "displayOperationId": false,
    "defaultModelsExpandDepth": 1,
    "defaultModelExpandDepth": 1,
    "defaultModelRendering": "example",
    "displayRequestDuration": false,
    "docExpansion": "none",
    "filter": false,
    "operationsSorter": "alpha",
    "showExtensions": false,
    "tagsSorter": "alpha",
    "validatorUrl": "",
    "apisSorter": "alpha",
    "jsonEditor": false,
    "showRequestHeaders": false,
    "supportedSubmitMethods": [ "get","put","post","delete","options","head","patch","trace"]
}
```
`knife4j`目前只对属性`supportedSubmitMethods`做了支持,即对不支持的HTTP类型不显示调试栏进行调试,仅显示文档页,效果如下图：

![](/knife4j/images/knife4j/debug-1.png)

所以如果你在构造front版本的时候,仅仅只是希望别人查看文档,而不希望调试文档,则则可以构建一个config的静态JSON,将`supportedSubmitMethods`属性返回一个空数组即可


接下来是服务分组接口url返回的JSON结构,如下：
```json
[
    {
        "name": "2.X版本",
        "url": "/v2/api-docs?group=2.X版本",
        "swaggerVersion": "2.0",
        "location": "/v2/api-docs?group=2.X版本"
    }
]
```

|属性|说明|
|--|--|
|name|服务名称,下拉框的label可见部分|
|url|同location|
|swaggerVersion|OPen API 版本,默认V2|
|location|服务的Swagger实例地址|

## 构造front版本

既然我们已经知道了上面option的地址,接下来我们即可改造`initKnife4jFront()`方法来进行

方法如下：
```js
this.swagger = new SwaggerBootstrapUi({
    Vue: that,
    plus: plusFlag,
    //禁用config的url调用
    configSupport: false,
    //覆盖url地址,多个服务的组合
    url: "/static/services.json"
});

```

我们将configSupport属性改为false,即不需要个性化配置,然后传入一个静态的JSON文件名称,该url在访问的时候是访问根目录下的static目录下的json文件

改造完成后,在该Vue的`create()`方法中进行调用,如下：
```js
created() {
    //this.initKnife4jSpringUi();
    this.initKnife4jFront();
}
```

代码在这里就已经改造完成,接下来执行打包命令,构建我们的front版本
```shell
yarn build
```

以上命令会在dist目录生成编译好的文件,文件结构如下：

![](/knife4j/images/knife4j/front-dist.png)

接下来如何运行呢?，你需要参考：

- [基于IIS使用knife4j](knife4j-front-iis.md)
- [基于Nginx方式使用](knife4j-front-nginx.md)


 
 
 