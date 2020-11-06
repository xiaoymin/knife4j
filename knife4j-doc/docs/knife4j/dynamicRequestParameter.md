# 动态请求参数添加文档注释

::: warning
`knife4j` 版本>2.0.1 使用此规则，后端必须启用增强注解`@EnableKnife4j`
:::

在开发`Knife4j`的过程中,很多人询问能否提供动态的参数文档注释,因为大多数人在开发接口的时候使用的传递参数都是`Map`或者`JSONObject`这类参数

这类参数对于Swagger这种预先定义再渲染的框架来说是无法满足要求的,即接口文档中是无任何参数注释的,这对于接口对接的人来说很痛苦,因为对接接口的人并不知道需要传递那些参数

`Knife4j`提供了的对于动态参数的注释,使用增强注解`@DynamicParameters`进行说明,代码示例如下：

```java
@PostMapping("/createOrder426")
@ApiOperation(value = "jdk-HashMap-动态创建显示参数-无@RequestBody")
@DynamicParameters(name = "CreateOrderHashMapModel",properties = {
        @DynamicParameter(name = "",value = "注解id",example = "X000111",required = true,dataTypeClass = Integer.class),
        @DynamicParameter(name = "name3",value = "订单编号-gson"),
        @DynamicParameter(name = "name1",value = "订单编号1-gson"),
})
public Rest<HashMap> createOrder1235332(@RequestBody HashMap map){
    Rest<HashMap> r=new Rest<>();
    r.setData(map);
    return r;
}
```

注解`@DynamicParameters`中有一个name属性,该值开发者可以理解为一个类名,如果你赋予name属性值,那么请保证全局唯一,或者干脆不赋值,交给`Knife4j`自动生成一个全局唯一的name值


目前针对动态请求参数只能做到简单的参数说明,对于数组、泛型等复杂的类型暂不提供支持。

::: tip
虽然`Knife4j`提供了这种功能,但作为作者我来说并不推荐使用该功能,主要有以下几个方面：

1、接口层提供Map这类动态参数,传递给Service层,代码可读性大大降低,时间长了后,作为写接口的你,再回头来看这段代码,你可能都想不起来需要传递那些字段属性,对于你的代码维护者来说,那就是更加痛苦了。

2、对于很多鄙视Swagger注解的人来说,这无疑是给人予口实,动态参数注解写了一大堆,从代码简洁程度来说不如一个实体类来的痛快

3、建议大家多多使用Java的一些特性,封装、继承等

以上仅仅是我的个人建议,仅供参考
:::
 
 
 