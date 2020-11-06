# [v1.8.0-2018/08/10 调试栏优化]

swagger-bootstrap-ui 1.8.0 发布了。swagger-bootstrap-ui 是 Swagger 的前端 UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿 

swagger-bootstrap-ui 1.8.0 主要更新如下： 

1、fixed 请求参数出现重复问题,去重

2、fixed 无法显示spring cloud 子项目路径,针对basePath不为空,或者不为"/"根路径的情况,相关api地址加上basePath前缀

3、调整菜单url各方法配色、接口配色,文档介绍、调试返回响应数据json配色

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.0-issue/methodJson.png)

4、响应模块添加http响应码、接口耗时、大小,参数栏添加全选按钮,调试页面针对响应内容tab选项卡去除灰色背景色,为默认白色底色

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.0-issue/rcode.png)

5、调试响应模块增加raw、curl两个子tab选项卡,实现curl功能,方便远程调试

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.0-issue/curl.png)

6、针对接口二进制返回,提供下载按钮,可点击弹出下载功能

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.0-issue/download.png)

7、fixed 针对图片返回时报DApiUI is not defined错误

8、文档doc.html页面title根据用户自定义title显示

9、发送中增加loading效果

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.0-issue/loading.png)

10、调整菜单顶部分组接口位置,移动到最左侧,添加可隐藏/显示MENU元素

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.0-issue/m1.png)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.0-issue/m2.png)

11、fixed 针对schema类型的参数,显示类型为string类型,按schema类型展示

12、文件上传支持文件多选

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.0-issue/upload.png)

**Maven坐标**

```xml
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.8.0</version>
</dependency>
```
**码云**：https://gitee.com/xiaoym/swagger-bootstrap-ui

**GITHUB**:https://github.com/xiaoymin/Swagger-Bootstrap-UI

欢迎提BUG、Pull Request给我，共同来完善这个小工具~~~~


**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 
 