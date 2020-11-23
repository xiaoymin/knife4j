# [v1.8.9-2019/01/11 文件下载、增强优化]

Swagger-Bootstrap-Ui 1.8.9 发布了。Swagger-Bootstrap-Ui是 Swagger 的增强UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿

Swagger-Bootstrap-Ui 1.8.9 主要更新如下：

## 特性&优化

1、主页面添加页面不缓存元素,防止版本升级缓存造成新功能加载失败.

2、响应示例说明、调试响应内容行添加description说明字段,免去切换到文档说明看字段说明的麻烦,非常感谢[@wanyaxing](https://github.com/wanyaxing)提交的[PR](https://github.com/xiaoymin/Swagger-Bootstrap-UI/pull/57)

3、新增个性化配置-开启RequestMapping接口类型重复地址过滤,默认只显示POST类型的接口地址(针对RequestMapping的接口请求类型,在不指定参数类型的情况下,如果不过滤,默认会显示7个类型的接口地址参数,如果开启此配置,默认展示一个Post类型的接口地址)

4、针对application/octet-stream类型的接口提供下载调试.

## Bug修复

1、启用UI增强时,获取不到`WebApplicationContext`对象造成空指针异常

2、修复list套list的返回值会不显示[issue #55 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/55)

3、接口请求参数同全局参数配置名称存在冲突的情况下,根据名称匹配导致参数丢失,匹配规则为参数名称、参数类型同时比较[issue #IQV1U @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IQV1U)

4、服务端响应HTML标签数据时,响应内容显示异常[issue #IQ9LG @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IQ9LG)

5、修复参数格式问题[issue #IPXX7 @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IPXX7)

6、针对多响应码返回不同schema类型,离线文档(markdown)未展示完整的bug[issue #IPPHJ @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IPPHJ)

## UI效果展示

![header-json.png](/images/blog/swagger-bootstrap-ui-1.8.9-issue/1.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.9-issue/2.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.9-issue/3.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.9-issue/4.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.9-issue/5.png)

## 项目地址

**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.8.9</version>
</dependency>
```

**码云**：<https://gitee.com/xiaoym/swagger-bootstrap-ui>

**GitHub**:<https://github.com/xiaoymin/Swagger-Bootstrap-UI>

在线体验：<http://swagger-bootstrap-ui.xiaominfo.com/doc.html>

## 代码集成示例

SpringBoot在线demo地址：https://gitee.com/xiaoym/swagger-bootstrap-ui-demo

Spring Mvc在线demo地址：https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/swagger-bootstrap-ui-demo-mvc

## Star & Issue

前往<https://gitee.com/xiaoym/swagger-bootstrap-ui>点个Star吧~~ ：）



**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 
 