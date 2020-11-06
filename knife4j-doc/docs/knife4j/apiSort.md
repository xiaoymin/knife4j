# 接口排序

::: warning
`knife4j` 版本>2.0.1 使用此规则,后端必须开启增强注解`@EnableKnife4j`
:::

目前提供的排序规则主要有2种，分别是：

- Controller之间的tags分组排序
- Controller下的接口排序

## tags分组排序

Controller之间的排序主要有两种方式,排序的规则是倒序,但是排序的最小值必须大于0

建议优先级是：`@ApiSupport`>`@ApiSort`>`@Api`

> 对于最高级别的值,可以从999开始
> 
> @ApiSupport注解自2.0.3版本引入

第一种,使用`@ApiSupport`注解中的属性`order`,代码示例如下：
```java
@Api(tags = "2.0.3版本-20200312")
@ApiSupport(order = 284)
@RestController
@RequestMapping("/api/nxew203")
public class Api203Constroller {
    
    
}
```

第二种情况,使用`knife4j`提供的增强注解`@ApiSort`,代码示例如下：
```java
@Api(tags = "2.0.2版本-20200226")
@ApiSort(286)
@RestController
@RequestMapping("/api/nxew202")
public class Api202Controller {
    
    
}
```

第三种,使用注解`@Api`中的属性`position`,代码示例如下：

```java
@Api(tags = "2.0.2版本-20200226",position = 286)
@RestController
@RequestMapping("/api/nxew202")
public class Api202Controller {
    
    
}
```


**注意**：如果排序增强功能后,前端在访问文档时也必须开启增强模式,具体可以参考[前端默认开启增强模式](autoEnableKnife4j.md)


## tag下接口排序

针对Controller下的具体接口,排序规则是使用`Knife4j`提供的增强注解`@ApiOperationSupport`中的order字段,代码示例如下：

```java
@ApiOperationSupport(order = 33)
@ApiOperation(value = "忽略参数值-Form类型")
@PostMapping("/ex")
public Rest<LongUser> findAll(LongUser longUser) {
    Rest<LongUser> r=new Rest<>();
    r.setData(longUser);
    return r;
}
```
 
 
 