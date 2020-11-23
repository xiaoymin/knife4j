# 简介

`knife4j-admin`是一个基于Spring Cloud Gateway网关,通过网关的特性,结合`knife4j`对Swagger的文档进行动态聚合的管理平台

>大家好,我在GitChat中开了一篇关于[《Knife4j 及 Swagger 在企业开发中的实践》](https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03)
>地址：[https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03](https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03)
>
>欢迎对 SpringFox、Swagger、Knife4j 感兴趣以及想了解的人员一起来chat 
>


平台特点：
- 跨语言、跨平台
- 任意聚合Swagger文档,动态发布,调试
- 文档个性化配置、权限等
- 彻底告别聚合网关文档等由于软件版本等造成的技术集成问题
- 独立部署

目前V1.0版本提供的功能：

- 项目管理:查看项目列表,新增项目文档

  ![](/knife4j/images/knife4j/admin/1.png)

- 文档预览：通过项目的JSON结构,解析动态添加至Spring Cloud Gateway网关进行文档聚合,并且可以在线调试

  ![](/knife4j/images/knife4j/admin/2.png)

## 项目新增

1、添加项目必须按照如下格式进行

```json
{
    "name": "大数据测试平台",
    "code":"test1",
    "description":"我是描述信息",
    "groups": [
        {
            "name": "用户模块",
            "uri":"http://knife4j.xiaominfo.com",
            "header":"server1",
            "url": "/v2/api-docs?group=2.X版本",
            "swaggerVersion": "2.0"
        },{
            "name": "订单模块",
            "uri":"http://swagger-bootstrap-ui.xiaominfo.com",
            "header":"server2",
            "url": "/v2/api-docs?group=1.8.X版本接口",
            "swaggerVersion": "2.0"
        }
        //more..
    ]
}
```

2、平台会根据用户上传的JSON文件在服务端保存一个`.json`文件,每一个项目代表内容都是以上一个完整的`json`文件

3、项目`code`必须唯一

4、项目下的服务列表信息中，`header`必须全局唯一,该参数值用户可以随机生成,只需要保证唯一性即可,作为Spring Cloud Gateway网关组件的转发依据

5、`groups`集合中,所提供的Swagger接口必须保证可以访问,完整的访问路径是`uri`+`url`

## 解决痛点

1、多语言使用Swagger时,集成Knife4j较麻烦

> 虽然Knife4j提供了其他语言的前端版本,但是从总体上还需要自己打包构建,而且依赖于其他Web容器进行单独部署,使用上叫复杂
>
> Knife4j-admin目前是根据OpenAPI V2的Swagger规范文档聚合平台,不同的语言在使用Knife4j时也可以很方便的使用

2、以Spring Cloud的微服务体系聚合Swagger困难重重

> 在Knife4j的技术交流群中,目前问的最多的就是Spring Cloud微服务架构如何聚合Swagger文档,要么是文档聚合失败,无法显示,要么是由于网关组件(Gateway|zuul)等组件本身的版本问题导致文档显示异常
>
> Knife4j-admin是独立部署,只需要提供微服务的接口既可以通过admin来很方便的集成

3、个性化配置

> 个性化的配置问题同样也是开发者关心的，例如：
>
> 1).Swagger文档能否登陆?
>
> 2).如何在生产环境屏蔽Swagger文档
>
> 3).文档界面中的XXX功能能否不显示
>
> 4).more...
>
> 对于个性化的需求,由于Knife4j-admin是独立的平台,对于文档的安全性等方面,可以做到很精准的控制,平台有用户,有可视化操作，有授权,并且可以作为唯一的对外文档发布平台,结合自身的服务器网络环境,做到内外网的环境隔离,文档安全输出.

更多有趣的功能等你来发掘~~！！！

## 试用

目前，在服务器上部署了一个版本,开发者如果有兴趣可以去体验

访问地址：[http://kadmin.xiaominfo.com/index.html#/home](http://kadmin.xiaominfo.com/index.html#/home)

## Star & Issue

感谢各位朋友的支持,前往[https://gitee.com/xiaoym/knife4j](https://gitee.com/xiaoym/knife4j)点个Star吧~~ ：）
 
 
 