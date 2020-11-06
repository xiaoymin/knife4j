# 增强功能

::: warning
开发者要想使用`knife4j`提供的增强功能,必须在Swagger的配置文件中引入增强注解,各个版本的增强注解区别如下表:<br />
:::

|软件|版本|增强注解|说明|
|--|--|--|--|
|swagger-bootstrap-ui |<= `1.9.6`|`@EnableSwaggerBootstrapUI`|| 
|knife4j|<=`2.0.0`|`@EnableSwaggerBootstrapUi`||
|knife4j|>=`2.0.1`|`@EnableKnife4j`|后续版本不会再更改|

- 在使用`swagger-bootstrap-ui`的<=`1.9.6`版本之前的代码方式,代码示例如下：
```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    
 //more..

}
```
- 在使用`knife4j`的<=`2.0.0`版本之前的代码方式,代码示例如下：
```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUi
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    
 //more..

}
```
- 在使用`knife4j`的>=`2.0.1`版本之后的代码方式,代码示例如下：
```java
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    
 //more..

}
```



SwaggerBootstrapUi自[1.8.5](https://www.oschina.net/news/100888/swagger-bootstrap-ui-1-8-5-released)版本以后,增加了后端Java代码的支持功能,主要目的是辅助Java开发者在使用Springfox-Swagger的同时,扩展一些增强功能，帮助开发者拥有更好的文档体验.

目前主要增强功能：

- tags分组标签排序
- api接口排序

使用`swagger-bootstrap-ui`提供的增强功能,需要在源Spring的config配置文件中开启,在原`EnableSwagger2`注解上增加`@EnableSwaggerBootstrapUi`注解，示例代码如下：

```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfiguration {
 	//more...   
}
```

针对tags分组排序，UI的排序规则是顺序排序，最小值1，最大值也是默认值Integer.Max_VALUE;

如果不使用SwaggerBootstrapUi的增强功能,则无需开启@EnableSwaggerBootstrapUi注解

tags的排序规则分两种：

a、一种是判断Swagger的@Api注解的position属性是否不等于0（默认值为0），如果该值不为空,则获取此值,根据该值排序

b、如果postion=0（不写的情况下）,判断是否存在注解@ApiSort的值，如果有值，则获取此值,根据该值排序

c、所以排序的取值规则是：position>@ApiSort

接口api的排序规则：

a、判断@ApiOperation注解上的postion属性是否不等于0（默认值为0），如果该值不为空,则获取此值,根据该值排序

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

b、如果postion=0（不写的情况下）,判断是否存在注解@ApiOperationSort的值，如果有值，则获取此值,根据该值排序

c、所以排序的取值规则是：position>@ApiOperationSort

注意：

注解@EnableSwaggerBootstrapUi、@ApiSort、@ApiOperationSort是本UI工具包提供的Java注解,排序功能的使用需要在启用原EnableSwagger2注解上增加@EnableSwaggerBootstrapUi注解方可生效



以上后台设置全部完成后,在UI的个性化设置中还需勾选开启增强功能,否则增强功能不生效.

功能目录：**文档管理 -> 个性化设置**

![](/knife4j/images/ehn-fun.png)


 
 
 