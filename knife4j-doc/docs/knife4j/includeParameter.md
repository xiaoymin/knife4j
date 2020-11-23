# 包含请求参数

`Knife4j`在2.0.3版本中提供了`includeParameters`的特性支持,与[过滤请求参数](ignoreParameter.md)特性是相对应的新特性

::: warning
`knife4j` 版本>2.0.1 使用此规则,后端必须开启增强注解`@EnableKnife4j`
:::

在使用`knife4j`的>=`2.0.1`版本之后的代码方式,代码示例如下：
```java
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    
 //more..

}
```

在实际开发中,Knife4j提供了忽略参数的特性,这帮助开发者在某些场景中大大提高文档的可变性操作

但有时候需要忽略的参数太多时,我们需要写很多的忽略参数属性,此时,一个与忽略参数对立取反的特性就显得很有帮助了

使用自定义增强注解`ApiOperationSupport`中的`includeParameters`属性,可以强制包含要显示的参数.去除多余的参数显示

include的规则如下：

- 例如新增接口时,某实体类不需要显示Id,即可使用该属性对参数进行忽略.`includeParameters={"id"}`
- 如果存在多个层次的参数包含关系,则使用**名称.属性**的方式,例如 `includeParameters={"uptModel.id","uptModel.uptPo.id"}`,其中uptModel是实体对象参数名称,id为其属性,uptPo为实体类,作为uptModel类的属性名称
- 一般是form表单类的请求,不需要设置参数名称,直接给定属性值名称即可


## 简单请求

简单请求比较简单,一般以formdata或者x-www-form-urlencoded类型的请求居多,针对这种请求的参数包含关系,我们在开发中只需要直接写上相应的属性名称即可

代码示例如下：

```java
@ApiOperationSupport(order = 40,includeParameters = {"ignoreLabels","longUser.ids"})
@ApiOperation(value = "包含参数值-Form类型1")
@PostMapping("/ex1c")
public Rest<IgnoreP1> findAllc12(IgnoreP1 ignoreP1) {
    Rest<IgnoreP1> r=new Rest<>();
    r.setData(ignoreP1);
    return r;
}
```

`IgnoreP1.java`如下：
```java
public class IgnoreP1 {
    @ApiModelProperty(value = "姓名a啊",example = "你好")
    private String name;

    @ApiModelProperty(value = "用户列表")
    private LongUser longUser;

    @ApiModelProperty(value = "标签集合")
    private List<IgnoreLabel> ignoreLabels;
    //getter and setter...

}
```


## JSON请求

JSON请求相比较简单请求有一个区别,需要把一级参数名称带上

代码示例如下：
```java
@ApiOperationSupport(order = 42,includeParameters = {"ignoreP1.ignoreLabels.code","ignoreP1.longUser.ids"})
@ApiOperation(value = "包含参数值-JSON类型1")
@PostMapping("/exc3")
public Rest<IgnoreP1> findAllc3(@RequestBody IgnoreP1 ignoreP1) {
    Rest<IgnoreP1> r=new Rest<>();
    r.setData(ignoreP1);
    return r;
}
```

在上面的代码示例中,是一个标准的JSON请求应用,`ignoreP1`就是参数名称,在包含该JSON下的某些属性时,必须在`includeParameters`属性中指明

例如：`ignoreP1.longUser.ids`


 
 
 