# [v1.9.1-2019/03/11 优化大数据响应接口]

swagger-bootstrap-ui 1.9.1 发布了。swagger-bootstrap-ui是 Swagger 的增强UI 实现，使文档更友好一点儿

[**GitHub**](https://github.com/xiaoymin/Swagger-Bootstrap-UI)  [**Gitee**](https://gitee.com/xiaoym/swagger-bootstrap-ui)  [**文档**](http://www.xiaominfo.com/swagger-bootstrap-ui/)  [**示例代码**](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo)  [**在线体验**](http://swagger-bootstrap-ui.xiaominfo.com/doc.html)

**主要更新如下：**

## 特性&优化

1、优化大数据响应接口,UI渲染卡顿,导致浏览器崩溃

2、ApiInfo.description支持html[issue #65 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/65)

3、合并[pr#61](https://github.com/xiaoymin/Swagger-Bootstrap-UI/pull/61)，优化array子类型为基础类型时schema显示为空的情况

4、响应数据编辑器增加换行模式,针对响应某个字段特别长时,自动换行.

## Bug修复

1、关闭默认响应状态后，自定义了`@ApiResposes`后，字段属性说明不显示[issue #IRV1I @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IRV1I)

2、example不显示,支持readOnly属性[issue #IS28O @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IS28O)

3、修复`Authorize`缓存bug[issue #ITAST @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/ITAST)
## UI效果展示


![header-json.png](/images/blog/swagger-bootstrap-ui-1.9.1-issue/1.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.1-issue/2.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.1-issue/3.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.1-issue/4.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.1-issue/5.png)


## 项目地址

**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.9.1</version>
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
 
 
 