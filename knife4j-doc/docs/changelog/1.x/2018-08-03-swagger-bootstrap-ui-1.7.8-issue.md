# [v1.7.8-2018/08/03 文件上传支持]

swagger-bootstrap-ui 1.7.8 发布了。swagger-bootstrap-ui 是 Swagger 的前端 UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿 

swagger-bootstrap-ui 1.7.8 主要更新如下： 

1、fixed 针对@RequestBody注解实体类属性required的值一直显示默认false问题

2、fixed 针对文件上传,使用allowMultiple = true,上传按钮不显示bug,推荐使用@ApiImplicitParam注解,并且指定dataType = "MultipartFile"

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.8-issue/upload.png)

3、分组接口移动至顶部,菜单列表添加icon图标,移除简介页的软件介绍信息,丰富简介页信息,新增各类型接口统计信息,菜单简介名称更名为主页

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.8-issue/main.png)

4、增加调试参数记忆功能,下次点击该接口时,上次输入的参数会保存继续可使用

5、优化 针对@RequestBody注解,参数使用默认description的问题,将使用@ApiModel注解实体类上的description属性

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.8-issue/des.png)

**Maven坐标**

```xml
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.7.8</version>
</dependency>
```
**码云**：https://gitee.com/xiaoym/swagger-bootstrap-ui

**GITHUB**:https://github.com/xiaoymin/Swagger-Bootstrap-UI

欢迎提BUG、Pull Request给我，共同来完善这个小工具~~~~


**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 
 