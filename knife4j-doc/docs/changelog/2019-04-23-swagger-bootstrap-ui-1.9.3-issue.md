# [v1.9.3-2019/04/23 i18n及自定义文档支持]
swagger-bootstrap-ui 1.9.3 发布了。swagger-bootstrap-ui是 Swagger 的增强UI 实现，使文档更友好一点儿

**文档**：http://doc.xiaominfo.com

**效果**：http://swagger-bootstrap-ui.xiaominfo.com/doc.html

**Gitee:**<https://gitee.com/xiaoym/swagger-bootstrap-ui>

**GitHub:**<https://github.com/xiaoymin/Swagger-Bootstrap-UI>

**示例:**https://gitee.com/xiaoym/swagger-bootstrap-ui-demo

## 特性&优化

1、增加i18n国际化支持(中文、English)，可[参考文档](http://doc.xiaominfo.com/guide/i18n.html)

2、优化调试框请求参数类型,添加数据类型[issue #IVF2L @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IVF2L)

3、接口描述支持Html渲染[issue #IVBWM @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IVBWM)

4、允许添加自定义文档(以markdown的形式)[issue #IUWN9 @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IUWN9),可[参考文档](http://doc.xiaominfo.com/guide/self-doc.html)

5、优化非200状态码调试栏显示高度过低的情况.

6、分组tag名称很长时超出bug,增加菜单title鼠标悬浮显示分组tag名称[issue #IVE0S @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IVE0S)

7、初始化请求异常处理,弹出友好提示信息.

8、接口任何信息变更和新增接口一样,添加new的icon图表样式,代表当前接口信息已产生变化.

9、Swagger Models中的属性类显示readOnly|example属性[issue #77 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/77)

## Bug修复

1、解决多个api文档切换时,Authorize的参数没有变更的bug[issue #IV3OZ @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IV3OZ)

2、解决Basic认证出现的空指针异常以及账户密码为空的时候，页面崩溃的情况[issue #78 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/78)

## UI效果展示

![header-json.png](/images/blog/swagger-bootstrap-ui-1.9.3-issue/1.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.3-issue/2.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.3-issue/3.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.3-issue/4.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.3-issue/5.png)

## 项目地址

**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.9.3</version>
</dependency>
```

## Star & Issue

感谢各位朋友的支持,前往<https://gitee.com/xiaoym/swagger-bootstrap-ui>点个Star吧~~ ：）

## 关注

关注我的微信公众号,实时了解`swagger-bootstrap-ui`的最新资讯~~~~

![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.3-issue/us.png)

 
 
 