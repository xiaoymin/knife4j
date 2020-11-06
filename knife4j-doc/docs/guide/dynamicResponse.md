# 动态添加响应类注释字段

::: warning
动态添加响应类注释字段是`knife4j`提供的增强功能,开发者要想使用`knife4j`提供的增强功能,必须在Swagger的配置文件中引入增强注解,各个版本的增强注解区别如下表:<br />
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



在`1.9.5`版本中,新增了动态响应类的字段注释说明，很多情况下,开发者开发的接口都是返回的动态`JSONObject`或者`Map`类，返回该类我们都知道,在使用Swagger的过程中,在文档中是没有注释说明的,但是我们又不想创建写太多的实体类Model

因此,为了解决以上问题,在`1.9.5`版本中,扩展了该特性的支持

自定义增强注解`ApiOperationSupport`中新增了`responses`属性，来看代码示例

```java
@ApiOperationSupport(
    responses = @DynamicResponseParameters(properties = {
        @DynamicParameter(value = "编号",name = "id"),
        @DynamicParameter(value = "名称",name = "name"),
        @DynamicParameter(value = "订单",name = "orderDate",dataTypeClass = OrderDate.class)
    })
)
@ApiOperation(value = "响应JSONObject类型")
@GetMapping("/jsonObject")
public JSONObject jsonObjectxxxx(){
    JSONObject jsonObject=new JSONObject();
    jsonObject.put("name","xx");
    return jsonObject;
}
```

其实,只要使用了`ApiOperationSupport`注解中的`responses`属性,不管接口返回是什么类,在增强中都会覆盖该接口的返回类型,动态重新生成新的Class以替代接口中返回的Type

所以,我们可以这样写

```java
public Object api(){
    
}
```

最终效果

![](/knife4j/images/dynamicResponse.png)
 
 
 