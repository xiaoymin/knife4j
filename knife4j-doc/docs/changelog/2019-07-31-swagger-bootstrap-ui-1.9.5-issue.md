# [v1.9.5-2019/07/31 支持过滤请求参数]

swagger-bootstrap-ui 1.9.5 发布了。swagger-bootstrap-ui是 Swagger 的增强UI 实现，使文档更友好一点儿

**文档**：http://doc.xiaominfo.com

**效果**：http://swagger-bootstrap-ui.xiaominfo.com/doc.html

**Gitee:**<https://gitee.com/xiaoym/swagger-bootstrap-ui>

**GitHub:**<https://github.com/xiaoymin/swagger-bootstrap-ui>

**示例:**https://gitee.com/xiaoym/swagger-bootstrap-ui-demo

## 特性&优化

1、针对文件上传响应JSON内容时,内容不高亮的问题[#IYXZB @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IYXZB)

2、文件上传响应内容显示异常Bug[#IYO96 @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IYO96)

3、针对中文请求头使用`encodeURIComponent()`函数进行编码处理[#IYMUF @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IYMUF)

4、修复开启增强时空指针异常Bug[#IYADU @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IYADU)

5、针对`@ResponseHeader`注解未显示Bug[#IY86A @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IY86A)

6、DELETE请求针对Array类型的请求参数错误Bug[#IY37Z @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IY37Z)

7、修复GET请求时CURL响应栏参数拼装错误[#131 @GitHub](https://github.com/xiaoymin/swagger-bootstrap-ui/issues/131)

8、修复非200状态码响应内容不格式化高亮的问题[#130 @GitHub](https://github.com/xiaoymin/swagger-bootstrap-ui/issues/130)

9、解决地址显示的BUG, 确保请求能够正确发送出去[#PR108 @GitHub](https://github.com/xiaoymin/swagger-bootstrap-ui/pull/108)

10、在使用动态扩展字段说明时,服务器上部署会造成空指针异常,该错误是由未对field名称进行非空判断导致[#IYLVC @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IYLVC)、[#119 @GitHub](https://github.com/xiaoymin/swagger-bootstrap-ui/issues/119)

11、可以自定义动态过滤请求参数,这在很多时候可以让我少写实体类，比如新增的时候不需要id，修改时又需要id，只需要在接口层使用增强注解`@ApiOperationSupport`的`ignoreParameters`属性即可,具体使用规则请[参考文档](https://doc.xiaominfo.com/guide/ignoreParameter.html)

12、优化增强排序接口注解`@ApiSort`无效果的问题

13、响应类Model动态添加解释字段.请[参考文档](https://doc.xiaominfo.com/guide/dynamicResponse.html)

## UI效果展示

![header-json.png](/images/blog/swagger-bootstrap-ui-1.9.5-issue/1.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.5-issue/2.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.5-issue/3.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.5-issue/4.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.5-issue/5.png)

## 项目地址

**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.9.5</version>
</dependency>
```

## Star & Issue

感谢各位朋友的支持,前往<https://gitee.com/xiaoym/swagger-bootstrap-ui>点个Star吧~~ ：）



## 关注

关注我的微信公众号,实时了解`swagger-bootstrap-ui`的最新资讯~~~~

![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.4-issue/us.png)

 
 
 