# [v1.8.3-2018/09/17 treetable展示参数]

swagger-bootstrap-ui 1.8.3 发布了。swagger-bootstrap-ui 是 Swagger 的前端 UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿

swagger-bootstrap-ui 1.8.3 主要更新如下：

1、新增tab选项卡,各个api接口详情通过新开选项卡来展现

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.3-issue/tabs.png)

2、去除原schema表格形式展示，请求参数、响应参数改由treetable组件(树组件)展示

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.3-issue/treetable.png)

3、fixed 请求参数有array类型,显示为schema类型的bug

4、fixed springcloud zuul 整合ui情况下 地址多个/[ISSUE #IMF0L @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMF0L)

5、响应内容去除cookies选项卡，响应示例、响应内容使用ace-editor展示响应内容，方便复制

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.3-issue/samples.png)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.3-issue/response.png)

6、优化(全局参数&Authorize)加入浏览器缓存问题,使用localStorage对象全局存储[issue #IMH77 @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMH77)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.3-issue/store.png)

7、fixed 泛型数据接口返回list类型时，不能解析[issue #26 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/26)

8、fixed 模型内部包含模型没有展示[issue #25 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/25)

9、优化请求参数是否必填样式,如果该参数必填,则以红色标注显示[issue #22 @Github](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/22)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.3-issue/require.png)

10、fixed DELETE请求不能正确处理Query参数 [issue #19 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/19)

11、fixed 请参数类型为 formData 的参数，填写了参数值还是提示 参数不能为空[issue #24 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/24)、[issue #IMMMJ @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMMMJ)

12、优化离线文档多行，换行、多空格显示问题

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.3-issue/md-2.png)

预览效果如下：

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.3-issue/md-1.png)

**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.8.3</version>
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
 
 <icp/> 
 comment/> 
 
 
 
 