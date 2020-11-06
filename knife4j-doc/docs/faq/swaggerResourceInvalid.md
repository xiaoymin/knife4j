# 请确保swagger资源接口正确

在`swagger-bootstrap-ui`的`1.9.X`系列版本中,打开文档页面初始化会碰见问题,针对此情况,为了显示更友好一下,在核心的JS代码中进行了异常捕获操作

```javascript
try{
    //some operation
}
catch (err){
    that.error(err);
    layer.msg(i18n.message.sys.loadErr+",Err:"+err.message);
    if (window.console){
        console.error(err);
    }
}
```

错误信息即开发者常见的**请确保swagger资源接口正确**

![](/images/swggerapi404.jpg)

一般出现这种错误的时候有几种情况

## swagger接口请求非200

开发者可以通过chrome浏览器的network控制台进行查看,首先需要排除swagger两个接口状态码非200的情况

有时候开发者未按正确的方式集成`springfox-swagger`组件,导致访问的时候接口地址出现404,springfox-swagger提供的两个接口：

- **分组接口：**接口url一般为`swagger-resources`
- **api资源实例接口:** 该接口为`/v2/api-docs`或者增强接口`/v2/api-docs-ext`

另外,`swagger-bootstrap-ui`增强的接口地址是`/v2/api-docs-ext`,如果出现访问此接口的时候状态码为404,请确保在Swagger的配置文件类上加上启用注解`@EnableSwaggerBootstrapUI`,该注解是和springfox的`@EnableSwagger2`配合一起使用,并非替代.

代码示例：

```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
 	//more ...   
}
```

## swagger接口格式不对

我们在使用集成`springfox-swagger`组件的时候,该组件为我们提供了两个接口,而这两个接口也是`swagger-bootstrap-ui`组件所依赖的接口，主要是分组接口和接口示例接口

**分组接口：**接口url一般为`swagger-resources`

该接口返回的正确格式为：

```json
[
    {
        "name": "1.8.X版本接口",
        "url": "/v2/api-docs?group=1.8.X版本接口",
        "swaggerVersion": "2.0",
        "location": "/v2/api-docs?group=1.8.X版本接口"
    },
    {
        "name": "1.9.X版本接口",
        "url": "/v2/api-docs?group=1.9.X版本接口",
        "swaggerVersion": "2.0",
        "location": "/v2/api-docs?group=1.9.X版本接口"
    },
    {
        "name": "默认接口",
        "url": "/v2/api-docs?group=默认接口",
        "swaggerVersion": "2.0",
        "location": "/v2/api-docs?group=默认接口"
    }
]
```

在低版本中可能会没有url属性,但是在springfox-swagger相对较高的版本中,都会存在,以上格式是一个纯数组的JSON对象，很多时候，后端的开发对响应的接口都会做一层统一的封装,例如封装了响应code、错误信息等,但是针对springfox组件提供的接口需要排除在外,这是开发者需要注意的地方

**api资源实例接口:** 该接口为`/v2/api-docs`或者增强接口`/v2/api-docs-ext`

实例接口正确格式为：

```json
{
  "swagger": "2.0",
  "info": {
    //...
  },
  "host": "127.0.0.1:8999",
  "basePath": "/",
  "tags": [
    {
      "name": "1.8.2版本",
      "description": "Api 182 Controller"
    } 
  ],
  "paths": {
    "/2/api/new187/postRequest": {
     //....
  },
  "securityDefinitions": {
     //,,.,
  },
  "definitions": {
    "AInfoVo": {
     //....
  }
}
```

包含了OpenAPI 2.0规范中定义的相关属性字段，是一个纯JSON对象,开发者不能封装之外的字段进来.

## JSON格式非法

如果以上两种情况都排除的话,最后还有一种情况会造成此问题的出现，那就是响应的JSON并非是一个标准的JSON

![](/images/swggerapi404-1.jpg)

一般出现此情况时,是因为后端在给List集合的属性赋予了`exmpale`属性,例如:

```java
@ApiModel(description = "客户字段分组模型",value = "CrmFieldGroupResponse")
public class CrmFieldGroupResponse {

    @ApiModelProperty(value = "客户字段分组ID")
    private int id;

    @ApiModelProperty(value = "客户字段分组名称")
    private String name;

    @ApiModelProperty(value = "客户字段数据",example = "{'id':'xxx'}")
    private List<CrmFieldResponse> fields;
    
}
```

该情况会导致生成出来的JSON并非是一个标准的JSON，而`swagger-bootstrap-ui`组件在前端是通过`JSON.parse()`方法对后端返回回来的数据进行JSON转换,这会导致转换失败

解决方法是把集合属性中的example属性去掉，交个springfox-swagger框架来自动解析

正确方式：

```java
@ApiModel(description = "客户字段分组模型",value = "CrmFieldGroupResponse")
public class CrmFieldGroupResponse {

    @ApiModelProperty(value = "客户字段分组ID")
    private int id;

    @ApiModelProperty(value = "客户字段分组名称")
    private String name;

    @ApiModelProperty(value = "客户字段数据")
    private List<CrmFieldResponse> fields;
    
}
```






 
 
 