# Enhance

SwaggerBootstrapUi has added back-end Java code support since version 1.8.5. The main purpose is to assist Java developers to extend some enhancements while using Springfox-Swagger to help developers have a better document experience.

Currently major enhancements:

- tags grouping tag sorting
- api interface sorting

Use the enhancements provided by swagger-bootstrap-ui, you need to open it in the source Spring config configuration file, add the @EnableSwaggerBootstrapUi annotation on the original EnableSwagger2 annotation, the sample code is as follows：

```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfiguration {
 	//more...   
}
```

For the sorting of tags, the sorting rule of the UI is sequential sorting, the minimum value is 1, and the maximum value is also the default value Integer.Max_VALUE;

If you don't use the enhancements of SwaggerBootstrapUi, you don't need to open the @EnableSwaggerBootstrapUi annotation.

There are two sorting rules for tags:

a, one is to determine whether the position attribute of Swagger's @Api annotation is not equal to 0 (the default value is 0), if the value is not empty, then get this value, sort according to the value

b. If postion=0 (when not writing), determine whether there is a value of the annotation @ApiSort. If there is a value, obtain the value and sort according to the value.

c, so the ordering rule is: position>@ApiSort

The collation of the interface api:

a. Determine whether the postion attribute on the @ApiOperation annotation is not equal to 0 (the default value is 0). If the value is not null, the value is obtained and sorted according to the value.

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

b. If postion=0 (when not writing), determine whether there is a value of the annotation @ApiOperationSort. If there is a value, obtain the value and sort according to the value.

c, so the ordering rules are: position>@ApiOperationSort

note:

The annotations @EnableSwaggerBootstrapUi, @ApiSort, @ApiOperationSort are the Java annotations provided by this UI toolkit. The use of the sorting function requires the @EnableSwaggerBootstrapUi annotation to be added to enable the original EnableSwagger2 annotation.



After all the above background settings are completed, you need to check the Enable Enhancement function in the UI personalization settings, otherwise the enhanced function will not take effect.

Function Directory: **Document Management -> Personalization**

![](/knife4j/images/ehn-fun.png)


 
 <icp/> 
 comment/> 
 
 
 
 