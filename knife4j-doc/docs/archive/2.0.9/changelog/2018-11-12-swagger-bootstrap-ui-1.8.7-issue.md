# [v1.8.7-2018/11/12 Swagger Models支持]

Swagger-Bootstrap-Ui 1.8.7 发布了。Swagger-Bootstrap-Ui是 Swagger 的增强UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿

从1.0到更新至今,Swagger-Bootstrap-Ui也新增了很多小特性,为使更多人了解她,我重写了一份关于Swagger-Bootstrap-Ui的文档说明.希望越来越多使用她的用户都能体验到她带来的便利.详情可关注[README.MD](https://gitee.com/xiaoym/swagger-bootstrap-ui/blob/master/README.md)

Swagger-Bootstrap-Ui 1.8.7 主要更新如下：

## 特性&优化

1、优化调试框响应内容高度,**根据响应内容自动设置响应高度**,不再设固定高度.

2、**Authorize**功能提供**注销**功能,清空当前缓存在浏览器的相关Auth信息.

3、新增**Swagger Models**菜单项功能,以TreeTable的方式展示当前Swagger分组实例文档中所有相关的Models属性说明.

4、个性化配置项**新增是否显示tag分组description属性的选择项**,勾选后,会和swagger官方文档一样显示description属性,默认为false不显示.

5、引入**[async.js](https://github.com/caolan/async)**异步组件库,优化文档解析效率,**解析渲染速度提升5倍以上**.

6、优化接口的id生成策略,使用MD5针对接口地址和mehtod方式生成接口id,**调试参数全局缓存localStorage对象中**,方便下次刷新访问调试.

7、响应状态栏增加全屏icon,点击全屏icon**可全屏查看响应内容**.

8、解决离线文档再开启UI增强功能后不排序的问题

9、调试框根据Swagger接口参数显示当前接口的Content-Type类型,在某些特殊情况下可**更改默认定义Content-Type请求头类型**,如果使用UI提供的全局参数功能,自定义了Content-Type的请求头,则默认以全局参数中的Content-Type为主.

10、增加对**JSR-303 annotations 注解的支持**(部分)

## Bug修复

1、针对SpringCloud通过网关构建Swagger分组获取不到Documentation对象的情况,根据default再获取一次

2、修复UI增强关于使用`@Api`注解tags属性不赋值,使用value，增强排序失败的问题.

3、修复针对`@RequestMapping`注解无value属性,UI增强出现数组越界的问题

4、修复针对扩展Spring的`RequestMappingHandlerMapping`自定义实现方式,获取不到扩展接口url地址信息,导致UI增强排序失败的问题.

## UI效果展示

![header-json.png](/images/blog/swagger-bootstrap-ui-1.8.6-issue/header-json.png)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.6-issue/debug-require.png)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.6-issue/more-params.png)

## 项目地址

**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.8.7</version>
</dependency>
```

**码云**：<https://gitee.com/xiaoym/swagger-bootstrap-ui>

**GitHub**:<https://github.com/xiaoymin/Swagger-Bootstrap-UI>

在线体验：<http://swagger-bootstrap-ui.xiaominfo.com/doc.html>

## Star & Issue

**或许她不是最漂亮的SwaggerUi，但绝对是目前最实用的SwaggerUi**

前往<https://gitee.com/xiaoym/swagger-bootstrap-ui>点个Star吧~~ ：）



**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 <icp/> 
 comment/> 
 
 
 
 