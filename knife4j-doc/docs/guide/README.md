## 简介

>大家好,我在GitChat中开了一篇关于[《Knife4j 及 Swagger 在企业开发中的实践》](https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03)
>地址：[https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03](https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03)
>
>欢迎对 SpringFox、Swagger、Knife4j 感兴趣以及想了解的人员一起来chat 
>

>
> 本系列文档开发者使用swagger-bootstrap-ui的用户可以参考,如果使用2.x的Knife4j,请移步参考[Knife4j](../knife4j/README.md)

[![star](https://gitee.com/xiaoym/knife4j/badge/star.svg?theme=gvp)](https://gitee.com/xiaoym/knife4j/stargazers)
[![fork](https://gitee.com/xiaoym/knife4j/badge/fork.svg?theme=gvp)](https://gitee.com/xiaoym/knife4j/members)

[swagger-bootstrap-ui](https://gitee.com/xiaoym/knife4j)是springfox-swagger的增强UI实现，为Java开发者在使用Swagger的时候，能拥有一份简洁、强大的接口文档体验

**效果：**[http://swagger-bootstrap-ui.xiaominfo.com/doc.html](http://swagger-bootstrap-ui.xiaominfo.com/doc.html)

**示例:**[https://gitee.com/xiaoym/swagger-bootstrap-ui-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo)

**交流：**

- [![](https://img.shields.io/badge/加入QQ1群-453925079(满)-red.svg)](//shang.qq.com/wpa/qunwpa?idkey=9a160903786b88bf3ca112842e501d3623510db4f1c307eec81849165485bf5f)
- [![](https://img.shields.io/badge/加入QQ2群-621154782(满)-red.svg)](//shang.qq.com/wpa/qunwpa?idkey=11e0a1453a6a3695bd8ed709fbc8359c9c48dd8538aaafbece7b84ecd325b91c)
- [![](https://img.shields.io/badge/加入QQ3群-608374991(满)-red.svg)](//shang.qq.com/wpa/qunwpa?idkey=16b81902c23fbca82780fa107da1b6612e2ee44a05c4103c9176ad9d61c2f6bf)  
- [![](https://img.shields.io/badge/加入QQ4群-593495275-red.svg)](https://qm.qq.com/cgi-bin/qm/qr?k=8mdCNeiPZsMuUtB8rC8jIopW5vfmYAXQ&jump_from=webapi)


## 核心功能

该UI增强包主要包括两大核心功能：**文档说明** 和 **在线调试**

- **文档说明**：根据Swagger的规范说明，详细列出接口文档的说明，包括接口地址、类型、请求示例、请求参数、响应示例、响应参数、响应码等信息，使用swagger-bootstrap-ui能根据该文档说明，对该接口的使用情况一目了然。

- **在线调试**：提供在线接口联调的强大功能，自动解析当前接口参数,同时包含表单验证，调用参数可返回接口响应内容、headers、Curl请求命令实例、响应时间、响应状态码等信息，帮助开发者在线调试，而不必通过其他测试工具测试接口是否正确,简洁、强大。

## UI增强

同时，swagger-bootstrap-ui在满足以上功能的同时，还提供了文档的增强功能，这些功能是官方swagger-ui所没有的，每一个增强的功能都是贴合实际,考虑到开发者的实际开发需要,是必不可少的功能，主要包括：

- **个性化配置**：通过个性化ui配置项，可自定义UI的相关显示信息

- **离线文档**：根据标准规范，生成的在线markdown离线文档，开发者可以进行拷贝生成markdown接口文档，通过其他第三方markdown转换工具转换成html或pdf，这样也可以放弃swagger2markdown组件

- **接口排序**：自1.8.5后，ui支持了接口排序功能，例如一个注册功能主要包含了多个步骤,可以根据swagger-bootstrap-ui提供的接口排序规则实现接口的排序，step化接口操作，方便其他开发者进行接口对接

## UI特点

- 以markdown形式展示文档,将文档的请求地址、类型、请求参数、示例、响应参数分层次依次展示,接口文档一目了然,方便开发者对接
- 在线调试栏除了自动解析参数外,针对必填项着颜色区分,同时支持tab键快速输入上下切换.调试时可自定义Content-Type请求头类型
- 个性化配置项,支持接口地址、接口description属性、UI增强等个性化配置功能
- 接口排序,支持分组及接口的排序功能
- 支持markdown文档离线文档导出,也可在线查看离线文档
- 调试信息全局缓存,页面刷新后依然存在,方便开发者调试
- 以更人性化的treetable组件展示Swagger Models功能
- 响应内容可全屏查看,针对响应内容很多的情况下，全屏查看，方便调试、复制
- 文档以多tab方式可显示多个接口文档
- 请求参数栏请求类型、是否必填着颜色区分
- 主页中粗略统计接口不同类型数量
- 支持接口在线搜索功能
- 左右菜单和内容页可自由拖动宽度
- 支持自定义全局参数功能，主页包括header及query两种类型
- i18n国际化支持,目前支持：中文简体、中文繁体、英文
- JSR-303 annotations 注解的支持


## UI效果图

![接口说明](/knife4j/static/des.png)

![接口调试](/knife4j/static/debug.png)

![个性化设置](/knife4j/static/settings.png)

![接口离线文档](/knife4j/static/markdown.png)

![SwaggerModels](/knife4j/static/models.png)
 
 
 