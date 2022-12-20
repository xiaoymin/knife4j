# [v1.7.9-2018/08/06 Authorize授权支持]

swagger-bootstrap-ui 1.7.9 发布了。swagger-bootstrap-ui 是 Swagger 的前端 UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿 

swagger-bootstrap-ui 1.7.9 主要更新如下： 

1、fixed 针对Integer、double、float等类型参数,有format参数则显示format属性,以区分准确类型,如：int64|int32等

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.9-issue/type.png)

2、fixed 滚动条出现底部部分内容不显示bug

3、优化菜单接口根据不同接口类型,颜色调整

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.9-issue/method.png)

4、优化文档响应数据jsonview字体,优化间距,更显紧促,优化菜单,接口及接口类型加粗

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.9-issue/r1.png)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.9-issue/r2.png)

5、add 顶部加搜索功能、可根据api地址、api介绍、api类型、分组名称实现模糊搜索,默认搜索当前已加载的分组api,如果其他分组未加载则搜索不到.

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.9-issue/search.png)

6、add 针对Security-JWT等权限验证,显示Authorize菜单授权

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.9-issue/auth.png)

7、add 左侧菜单栏可自由拖动长度大小

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.9-issue/drag.png)

**Maven坐标**

```xml
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.7.9</version>
</dependency>
```
**码云**：https://gitee.com/xiaoym/swagger-bootstrap-ui

**GITHUB**:https://github.com/xiaoymin/Swagger-Bootstrap-UI

欢迎提BUG、Pull Request给我，共同来完善这个小工具~~~~


**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 
 