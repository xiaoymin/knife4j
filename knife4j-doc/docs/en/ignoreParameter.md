# 忽略参数属性

在`1.9.5`版本,新增了在接口中可以忽略参数的新功能

通常我们在开发接口时,比如一个新增接口和一个修改接口,修改接口需要传递主键id、而新增接口则不需要传递此属性,但大部分情况,我们只写一个Model类,此时在新增接口时显示主键id会显得很多余.

使用自定义增强注解`ApiOperationSupport`中的`ignoreParameters`属性,可以强制忽略要显示的参数.

忽略的规则如下：

- 例如新增接口时,某实体类不需要显示Id,即可使用该属性对参数进行忽略.`ignoreParameters={"id"}`
- 如果存在多个层次的参数过滤,则使用**名称.属性**的方式,例如 `ignoreParameters={"uptModel.id","uptModel.uptPo.id"}`,其中uptModel是实体对象参数名称,id为其属性,uptPo为实体类,作为uptModel类的属性名称
- 如果参数层级只是一级的情况下,并且参数是实体类的情况下,不需要设置参数名称,直接给定属性值名称即可

在接口过滤时,主要有两种情况

## 一级参数

我们在使用实体类直接作为参数时,在我们的ui界面中是不会显示参数名称的,此时可以直接使用实体的属性名称进行参数忽略，例如如下代码：

```java
@ApiOperation(value = "新增Model接口1")
@ApiOperationSupport(ignoreParameters = {"id","orderDate.id"})
@PostMapping("/insertMode1l")
public Rest<UptModel> insertModel1(UptModel uptModel){
    Rest<UptModel> r =new Rest<>();
    r.setData(uptModel);
    return r;
}
```

实体类`UptModel.java`文件代码

```java
public class UptModel {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "订单信息")
    private OrderDate orderDate;
}
```

此时，最终过过滤掉`UptModel`的属性id和属性`orderDate`类中的id属性,不在界面显示.

![](/knife4j/images/ignore1.png)

## JSON参数

如果请求参数是使用JSON的方式

代码如下：

```java
@ApiOperation(value = "新增Model接口")
@ApiOperationSupport(ignoreParameters = {"uptModel.id","uptModel.name","uptModel.orderDate.id"})
@PostMapping("/insertModel")
public Rest<UptModel> insertModel(@RequestBody UptModel uptModel){
    Rest<UptModel> r =new Rest<>();
    r.setData(uptModel);
    return r;
}
```

此时如果要过滤id的话,需要指定带上参数名称`uptModel`

最终忽略的值为`ignoreParameters = {"uptModel.id","uptModel.name","uptModel.orderDate.id"}`

![](/knife4j/images/ignore2.png)


 
 
 