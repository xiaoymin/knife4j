# [v1.8.4-2018/09/25 Model解析异常修复]

swagger-bootstrap-ui 1.8.4 发布了。swagger-bootstrap-ui 是 Swagger 的前端 UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿

swagger-bootstrap-ui 1.8.4 主要更新如下：

1、fixed key-value表单请求 @RequestParam映射无效,在线调试bug[issue #IMXOV @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMXOV)、[issue #30 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/30)

2、fixed 树形model默认展开[issue #IMXH5 @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMXH5)

3、fixed 两个list里放同一个bean，一个显示一个不显示[issue #IMXOY @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMXOY)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.4-issue/list-bean.png)

4、fixed 同时传输文本信息和文件时，值重复[issue #IMXDT @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMXDT)

5、fixed issue [#IN03Q](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IN03Q)

6、fixed 响应类 3层嵌套解析不出来[issue #IMXOF @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMXOF)

7、fixed 全局参数设置接口中已有变量,会导致在线调试里面出现2个参数,不方便调试(如果后端swagger配置文件中使用globalParameter设置全局参数，并且赋予默认值，则以后端全局参数值为准)[issue #IMXVD @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMXVD)

8、fixed ["text/plain"] controller接收问题[issue #IN0PC @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IN0PC)

9、优化调试页响应高度，ace-editor响应高度

10、默认在Swagger-bootstrap-ui的请求,UI会增加一个默认的请求头`Request-Origion`:`SwaggerBootstrapUi`

11、fixed Authorize默认tab不选中的bug

12、fixed curl响应参数,针对中文urlencode处理

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.4-issue/urlencode.png)



**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.8.4</version>
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
 
 
 