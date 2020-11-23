# [v1.9.4-2019/06/10 扩展支持动态字段注释]

swagger-bootstrap-ui 1.9.4 发布了。swagger-bootstrap-ui是 Swagger 的增强UI 实现，使文档更友好一点儿

**文档**：http://doc.xiaominfo.com

**效果**：http://swagger-bootstrap-ui.xiaominfo.com/doc.html

**Gitee:**<https://gitee.com/xiaoym/swagger-bootstrap-ui>

**GitHub:**<https://github.com/xiaoymin/Swagger-Bootstrap-UI>

**示例:**https://gitee.com/xiaoym/swagger-bootstrap-ui-demo

## 特性&优化

1、**最低需要JDK 1.8支持**

2、单独接口通过hash地址访问,方便开发人员之间快速复制传递接口信息,能准确定位到接口

3、优化下载参数名称问题,忽略filename大小写敏感[#IXA5C @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IXA5C)

4、优化BasicFilter过滤器正则匹配频率问题,decode函数调用替换为JDK 1.8版本中的`java.util.Base64`

5、tab操作项修改为点击事件显示,避免同调试按钮冲突导致误关选项卡[#IXA5I @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IXA5I)

6、增加调试接口响应类型为Xml、Html、Text的支持[#IWP49 @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IWP49)

7、优化调试后header、raw、curl等选项卡高度太低的问题[#IWLSU @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IWLSU)

8、主页简介description字段支持markdown格式[#IVVRX @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IVVRX)

9、针对枚举类型的集合类型(List),在字段描述中显示枚举可用列表值[#100 @GitHub](https://github.com/xiaoymin/swagger-bootstrap-ui/issues/100)

10、重构原接口排序、tag排序规则,新增接口作者属性,可写每个接口的作者,方便开发者调试.[参考文档](https://doc.xiaominfo.com/guide/dynamic-parameter.html)

11、针对Authorize授权的相关属性,不同分组相同的请求参数只需授权一次即可则全局通用[#IXHBL @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/issues/IXHBL)

12、针对Map、JSONObject等动态类型可通过自定义注解`@ApiOperationSupport`或者`@DynamicParameters`来增加参数的字段说明,解决不想写实体类的烦恼,但是又无文档的困扰.[参考文档](https://doc.xiaominfo.com/guide/dynamic-parameter.html)

13、优化自定义文档(markdown)界面效果,增加相关markdown语法样式(引用editormd.css)

## UI效果展示

![header-json.png](/images/blog/swagger-bootstrap-ui-1.9.4-issue/1.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.4-issue/2.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.4-issue/3.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.4-issue/4.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.4-issue/5.png)

## 项目地址

**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.9.4</version>
</dependency>
```

## Star & Issue

感谢各位朋友的支持,前往<https://gitee.com/xiaoym/swagger-bootstrap-ui>点个Star吧~~ ：）


## 源码分析

距离上一个版本也有挺长时间了,这段时间主要是对springfox的源码进行了一些研究和学习，并且记录了一些博客,该版本(`1.9.4`)的一些功能也在看源码的过程中对我有一些启发,对于Swagger的规范也多了一些了解

对springfox源码有兴趣的朋友可以去我的博客查看,[点击前往](https://www.xiaominfo.com/2019/05/20/springfox-0/)



## 关注

关注我的微信公众号,实时了解`swagger-bootstrap-ui`的最新资讯~~~~

![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.4-issue/us.png)

 
 
 