# 动态字段注释

在`1.9.4`版本中,`swagger-bootstrap-ui`增加了动态类型的字段注释功能

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

所有使用`swagger-bootstrap-ui`提供的增强功能都需要在SwaggerConfiguration配置文件中开启增强注解

如下：

```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfiguration {
    
    
}

```
`@EnableSwaggerBootstrapUI`注解必须加上


 
 
 