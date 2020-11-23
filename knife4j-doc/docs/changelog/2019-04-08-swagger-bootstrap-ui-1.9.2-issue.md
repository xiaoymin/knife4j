# [v1.9.2-2019/04/08 提供前后端分离解决方案]

swagger-bootstrap-ui 1.9.2 发布了。swagger-bootstrap-ui是 Swagger 的增强UI 实现，使文档更友好一点儿

[**GitHub**](https://github.com/xiaoymin/Swagger-Bootstrap-UI)  [**Gitee**](https://gitee.com/xiaoym/swagger-bootstrap-ui)  [**文档**](http://www.xiaominfo.com/swagger-bootstrap-ui/)  [**示例代码**](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo)  [**在线体验**](http://swagger-bootstrap-ui.xiaominfo.com/doc.html)

**主要更新如下：**

## 特性&优化

1、增加地址栏参数访问,快速个性化设置功能，可[参考文档](http://www.xiaominfo.com/swagger-bootstrap-ui/settingsFastAccess.html)

2、修改`SecurityConfiguration`中关于`Environment`的注入方式,改为属性注解注入,提供默认无参构造,避免某些情况下使用SpringAop导致异常[issue #ITI1C @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/ITI1C)

3、针对存在format属性字段类型,显示format属性，使参数更加清晰明了(例如：Integer-int32,Integer-int64,string-date)[issue #ITIPQ @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/ITIPQ)

4、针对body类型的Array类型请求,给与默认参数值[issue #ITVZ2 @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/ITVZ2)

5、优化新接口图标太大的问题,解决下拉框选择分组后,title标题属性不切换的问题.[issue #IUGWF @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IUGWF)

6、当请求参数太多(>5)时,调试栏显示折叠栏,点击发送后可自动折叠参数

7、图片预览显示高度自适应[issue #72 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/72)

8、针对`@RequestBody`类型的参数类型枚举的支持[issue #73 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/73)

9、**提供前后端分离的文档预览解决方案**,具体[参考文档](http://www.xiaominfo.com/swagger-bootstrap-ui/ui-front.html)

## Bug修复

1、修复请求示例中支持readOnly属性[issue #IS28O @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IS28O)

2、修复响应返回数据的Map类型数据无法展开显示[issue #IUAXW @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IUAXW)

3、修复点击复制文档，复制的md文件中，没有接口名称[issue #71 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/71)


## UI效果展示


![header-json.png](/images/blog/swagger-bootstrap-ui-1.9.2-issue/1.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.2-issue/2.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.2-issue/3.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.2-issue/4.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.2-issue/5.png)


## 项目地址

**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.9.2</version>
</dependency>
```

**码云**：<https://gitee.com/xiaoym/swagger-bootstrap-ui>

**GitHub**:<https://github.com/xiaoymin/Swagger-Bootstrap-UI>

在线体验：<http://swagger-bootstrap-ui.xiaominfo.com/doc.html>

项目文档：http://www.xiaominfo.com/swagger-bootstrap-ui/

## 代码集成示例

SpringBoot在线demo地址：https://gitee.com/xiaoym/swagger-bootstrap-ui-demo

Spring Mvc在线demo地址：https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/swagger-bootstrap-ui-demo-mvc

## Star & Issue

前往<https://gitee.com/xiaoym/swagger-bootstrap-ui>点个Star吧~~ ：）

**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 
 