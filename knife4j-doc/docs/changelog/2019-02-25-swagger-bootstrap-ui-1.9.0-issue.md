# [v1.9.0-2019/02/25 提供Swagger资源保护]

SwaggerBootstrapUi 1.9.0 发布了。SwaggerBootstrapUi是 Swagger 的增强UI 实现，使文档更友好一点儿

[**GitHub**](https://github.com/xiaoymin/Swagger-Bootstrap-UI)  [**Gitee**](https://gitee.com/xiaoym/swagger-bootstrap-ui)  [**文档**](http://www.xiaominfo.com/swagger-bootstrap-ui/)  [**示例代码**](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo)  [**在线体验**](http://swagger-bootstrap-ui.xiaominfo.com/doc.html)

**主要更新如下：**
## 特性&优化

1、优化未给与tags分组时,Ui默认赋值`default`.

2、针对使用SwaggerBootstrapUi的增强排序功能时导致升级Springfox-Swagger必须升级到2.9.2引起的jar包冲突版本问题，Ui做向下兼容处理,Springfox-Swagger版本最低兼容2.7.0(相对稳定版本,亲测可用)

3、个性化新增配置，是否开启缓存已打开的api文档,感谢[@web-xiaxia](https://gitee.com/web-xiaxia)提交的pr

4、优化application/octet-stream下载出现的参数(header|query)问题

5、优化图片验证码显示问题,可参考文档[文件下载及图片预览](http://www.xiaominfo.com/swagger-bootstrap-ui/accessControl.html)

6、新增权限特性属性`swagger.production`，开启此属性后会屏蔽swagger所有访问资源,可用于生产环境中部署屏蔽文档输出.保护文档安全,可参考文档[访问权限控制](http://www.xiaominfo.com/swagger-bootstrap-ui/accessControl.html)

7、针对Swagger资源请求,提供Basic认证功能,可用于保护Swagger文档页面.可参考[Basic详情](http://www.xiaominfo.com/swagger-bootstrap-ui/accessControl.html)

8、优化文件上传参数类型File的支持.可参考文档[文件上传](http://www.xiaominfo.com/swagger-bootstrap-ui/uploadFile.html)

9、优化响应数据右侧存在字段说明Span元素重叠,并增加Toggle开关显示关闭右侧字段说明

10、优化离线文档预览,超出UI默认接口数量(100个)时,自动显示markdown源文件代码,供开发者自动复制到第三方转换软件查看,不再提供预览效果

## Bug修复

1、启用UI增强时,获取不到`WebApplicationContext`对象造成空指针异常

2、修复SpringMvc启用增强失败的Bug

3、修改对象属性设置example导致解析Model失败的bug[issue #IROVN @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IROVN)

4、修复搜索后,相关个性化状态设置不显示的bug[issue #IRE8W @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IRE8W)

5、修复 请求响应实体类内有Map类型参数无法正常显示 [issue #IR61U @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IR61U)

## UI效果展示


![header-json.png](/images/blog/swagger-bootstrap-ui-1.9.0-issue/1.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.0-issue/2.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.0-issue/3.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.0-issue/4.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.0-issue/5.png)


## 项目地址

**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.9.0</version>
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
 
 
 