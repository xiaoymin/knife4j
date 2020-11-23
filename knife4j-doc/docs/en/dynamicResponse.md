# 动态添加响应类注释字段

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
 
 
 