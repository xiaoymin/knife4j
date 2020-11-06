# [v1.8.5-2018/10/16 文档增强,接口排序]

swagger-bootstrap-ui 1.8.5 发布了。swagger-bootstrap-ui 是 Swagger 的增强UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿

swagger-bootstrap-ui在1.8.5以后,她不在是一个纯webjar的UI工具了,她增强了swagger的一些功能支持,例如tags、接口的排序,一些个性化的支持,目前只增强接口排序

后续更多关于swagger的增强功能需求非常欢迎大家提[issue](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/new?issue%5Bassignee_id%5D=0&issue%5Bmilestone_id%5D=0)反馈,让这款UI更加丰富强大.

swagger-bootstrap-ui 1.8.5 主要更新如下：

1、fixed formdata类型参数针对array数组类型无增加按钮

2、fixed 响应内容高度占比,参数过多的情况无法显示

3、多选项卡文档介绍、在线调试position位置引起的不适改动,由竖变横.

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.5-issue/tab-position.png)

4、增强排序功能，添加个性化配置管理功能,可开启个性化配置

![](/images/blog/swagger-bootstrap-ui-1.8.5-issue/exced1.gif)

5、关于个性化增强功能,目前已经实现了tags、和接口api方法的排序,使用方式：

在原`EnableSwagger2`注解上增加`@EnableSwaggerBootstrapUi`注解

```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfiguration {
 	//more...   
}
```

针对tags分组排序，UI的排序规则是顺序排序，最小值1，最大值也是默认值Integer.Max_VALUE;

如果不使用SwaggerBootstrapUi的增强功能,则无需开启`@EnableSwaggerBootstrapUi`注解

**tags的排序规则分两种**：

a、一种是判断Swagger的`@Api`注解的position属性是否不等于0（默认值为0），如果该值不为空,则获取此值,根据该值排序

b、如果postion=0（不写的情况下）,判断是否存在注解`@ApiSort`的值，如果有值，则获取此值,根据该值排序

c、所以排序的取值规则是：position>@ApiSort

**接口api的排序规则**：

a、判断`@ApiOperation`注解上的postion属性是否不等于0（默认值为0），如果该值不为空,则获取此值,根据该值排序

```java
//postion属性赋值
@ApiOperation(httpMethod = "POST",position = 2,value = "Test2Model测试数组参数，多个",response=Test2Model.class)
@ApiResponses({
    @ApiResponse(code = 200, message = "非HTTP状态码，返回值JSON code字段值，描述：成功")
})
@ApiImplicitParams({
    @ApiImplicitParam(name = "ids",paramType ="form",value = "参数",allowMultiple = true, required = true)
})
```

b、如果postion=0（不写的情况下）,判断是否存在注解`@ApiOperationSort`的值，如果有值，则获取此值,根据该值排序

c、所以排序的取值规则是：position>@ApiOperationSort

**注意**：

注解`@EnableSwaggerBootstrapUi`、`@ApiSort`、`@ApiOperationSort`是本UI工具包提供的Java注解,排序功能的使用需要在启用原`EnableSwagger2`注解上增加`@EnableSwaggerBootstrapUi`注解方可生效

6、默认去除接口api地址的线上,默认只显示方法类型、方法说明两个属性,当然,新版本增加的个性化的配置功能，如果你觉得api地址显示任然有需要,可在个性化配置中开启该功能，个性化配置属性存储在localStorage对象中.只需要配置一次接口.

7、fixed 构建curl功能中写死http,根据`window.location.href`动态判断(http|https)的情况

8、如果请求参数是json参数body类型，文档说明中添加**请求示例**json展示,方便查看

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.5-issue/request-json.png)

9、请求示例、响应示例json自动适配高度

10、选中接口api菜单时,菜单显示激活色,显示背景颜色background-color: #eee;

11、fixed 离线文档markdown格式错乱问题(table标题换行导致显示异常)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.5-issue/md.png)

12、离线文档已预览html的方式展现,复制文档功能依然是复制markdown语法

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.5-issue/md-copy.png)

13、请求参数及响应参数说明改为多行显示,超出长度不以省略号显示,防止出现浮层一直显示的bug

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.5-issue/br.png)

**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.8.5</version>
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
 
 
 