# [v1.8.2-2018/08/26 优化UI样式]


swagger-bootstrap-ui 1.8.2 发布了。swagger-bootstrap-ui 是 Swagger 的前端 UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿

swagger-bootstrap-ui 1.8.2 主要更新如下：

1、fixed 关于@ApiModelProperty的value不支持\n [issue #IM7XC @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM7XC)

2、fixed 关于在线调试界面显示的优化,调试栏新增参数类型列,区分数据参数请求类型 [issue #IM7TV @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM7TV)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.2-issue/param_type.png)

3、fixed 在springcloud下 整合到zuul时 测试路径不正确[issue #IM69X @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM69X)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.2-issue/zuul-basePath.png)

4、属性介绍说明，表格栏统一使用中文

5、fixed 发布到tomcat非root目下时路径被多层嵌套curl路径正确 ui内部测试路径多层[issue #IM69H @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM69H)

6、fixed `List<String>`和String[]类型解析不正确，应该为array，实际为String并且不能增加[issue #IM2ZI @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM2ZI)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.2-issue/strarr.png)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.2-issue/strarr1.png)

7、fixed 类型及引用类在出现array类型时不一致的问题[issue #7 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/17)

8、fixed DELETE请求无法正确处理请求头[issue #16 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/16)

9、fixed 在线调试-参数名称更改不生效 [issue #IMBN3 @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMBN3)

10、fixed 升级到1.8.1后,火狐浏览器无法显示文档[issue #IM37D @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM37D)

11、fixed 关于请求是form表单，但是业务参数是body(json体的)请求异常[issue #IM2YE @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM2YE)

12、fixed 入参中的对象被处理成string[issue #ILU3S @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/ILU3S)

13、fixed UI 样式建议(采纳大部分建议，非常感谢@永夜 提出的建议)[issue #IMCET @GitEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMCET)

14、fixed 当请求，出现param参数时，与body参数时，传到服务器无效params没有传,同issue #IM2YE [issue #IM72N @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM72N)

15、优化，返回raw文本标签页添加复制文本功能,方便开发者调用，复制按钮增加icon

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.2-issue/rawCopy.png)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.2-issue/curlCopy.png)

16、fixed 文件上传的bug[issue #IM4RG @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM4RG)

**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.8.2</version>
</dependency>
```

**码云**：<https://gitee.com/xiaoym/swagger-bootstrap-ui>

**GITHUB**:<https://github.com/xiaoymin/Swagger-Bootstrap-UI>

在线体验：<http://swagger-bootstrap-ui.xiaominfo.com/doc.html>

欢迎提BUG、Pull Request给我，共同来完善这个小工具~~~~

还未给swagger-bootstrap-ui点过赞的朋友，前往<https://gitee.com/xiaoym/swagger-bootstrap-ui>给个Star吧~~ ：）

**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 
 