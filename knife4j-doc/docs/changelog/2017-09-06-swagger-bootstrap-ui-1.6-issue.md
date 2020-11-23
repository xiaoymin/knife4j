# [v1.6-2017/09/06 支持文件上传]

swagger-bootstrap-ui 1.6 发布了。swagger-bootstrap-ui 是 Swagger 的前端 UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿

swagger-bootstrap-ui 1.6更新如下：

1、支持文件上传，需要指定dataType=MultipartFile,如下：
```java
 @ApiOperation(value = "文件素材上传接口")
 @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "文件流对象,接收数组格式", required = true,dataType = "MultipartFile"),
            @ApiImplicitParam(name = "title", value = "title", required = true)}
    )
 @RequestMapping(value="/uploadMaterial",method = RequestMethod.POST)
 @ResponseBody
 public RestMessage uploadMaterial(@RequestParam(value="file") MultipartFile[] files,@RequestParam(value = "title") String title, HttpServletRequest request) throws IOException {
        //int mul=1*1024*1024;
        String realPath=request.getSession().getServletContext().getRealPath("/upload");
  //......
}
```
![](/knife4j/images/blog/swagger-bootstrap-ui-1.6-issue/upload.png)
2、`ResponseBody<String>`类型的string展示

3、布局溢出的问题.bycdao-main样式调整，修改margin-left:270px;

4、ApiImplicitParam注解defaultValue属性支持

5、菜单名称调整，展示接口方法类型、接口地址、接口说明三个参数

![](/knife4j/images/blog/swagger-bootstrap-ui-1.6-issue/menu.png)

**Maven坐标**
```xml
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.6</version>
</dependency>
```

**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 
 