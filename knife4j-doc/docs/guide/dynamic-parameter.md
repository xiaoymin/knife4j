# 动态字段注释

::: warning
动态类型字段注释是`knife4j`提供的增强功能,开发者要想使用`knife4j`提供的增强功能,必须在Swagger的配置文件中引入增强注解,各个版本的增强注解区别如下表:<br />
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



在`1.9.4`版本中,`swagger-bootstrap-ui`增加了动态类型的字段注释功能,更名后的`knife4j`同样保留

针对Map、JSONObject等动态类型可通过自定义注解@ApiOperationSupport或者@DynamicParameters来增加参数的字段说明,解决不想写实体类的烦恼,但是又无文档的困扰.

如下代码：
```java
@PostMapping("/createOrder422")
@ApiOperation(value = "jdk-Map-动态创建显示参数")
public Rest<Map> createOrder12232(@RequestBody Map map){
    Rest<Map> r=new Rest<>();
    r.setData(map);
    return r;
}



@PostMapping("/createOrder423")
@ApiOperation(value = "jdk-JSONObject-动态创建显示参数")
public Rest<JSONObject> createOrder12232(@RequestBody JSONObject json){
    Rest<JSONObject> r=new Rest<>();
    r.setData(json);
    return r;
}

```

很多开发者都会喜欢传递动态的Map或者JSONObject作为入参接收对象,这样能以JSON的方式对入参进行扩展,方便业务功能的扩展开发

但是,如果使用以上的方式,我们用Swagger框架来生成文档时,Swagger并不知道我们需要传递那些字段属性,因为它是声明式的

只有在定义了相关类或者参数注释说明后,Swagger才可以帮助我们生成文档

为了解决以上这种动态的类型生成注释,在`1.9.4`版本中新增了两个注解以帮助我们完成字段的注释说明

- ApiOperationSupport:该注解是扩展增强注解,目前主要扩展的属性有order(接口排序)、author(接口开发者)、params(动态字段集合)
- DynamicParameters：动态扩展注解,主要包括name(Model名称),properties(属性列表)

此时,我们修改一下上面的两个方法
```java
@PostMapping("/createOrder421")
@ApiOperation(value = "fastjson-JSONObject-动态创建显示参数")
@ApiOperationSupport(params = @DynamicParameters(name = "CreateOrderModel",properties = {
        @DynamicParameter(name = "id",value = "注解id",example = "X000111",required = true,dataTypeClass = Integer.class),
        @DynamicParameter(name = "name",value = "订单编号",required = false)
}))
public Rest<JSONObject> createOrder12222(@RequestBody JSONObject jsonObject){
    Rest<JSONObject> r=new Rest<>();
    r.setData(jsonObject);
    return r;
}

@PostMapping("/createOrder422")
@ApiOperation(value = "jdk-Map-动态创建显示参数")
@DynamicParameters(name = "CreateOrderMapModel",properties = {
            @DynamicParameter(name = "id",value = "注解id",example = "X000111",required = true,dataTypeClass = Integer.class),
            @DynamicParameter(name = "name",value = "订单编号"),
            @DynamicParameter(name = "name1",value = "订单编号1"),
            @DynamicParameter(name = "orderInfo",value = "订单信息",dataTypeClass = Order.class),
    })
public Rest<Map> createOrder12232(@RequestBody Map map){
    Rest<Map> r=new Rest<>();
    r.setData(map);
    return r;
}
```

最终,页面效果呈现如下：

![](/knife4j/images/1-9-4/dynamic-map.png)
![](/knife4j/images/1-9-4/dynamic-json.png)


**注意**

所有使用`swagger-bootstrap-ui`或者`knife4j`提供的增强功能都需要在SwaggerConfiguration配置文件中开启增强注解

 
 
 