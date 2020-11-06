# 3.7 分组排序

::: warning
增强功能需要通过配置yml配置文件开启增强,自2.0.7开始
```yml
knife4j:
  enable: true
```
:::

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

第三种,使用注解`@Api`中的属性`position`(需要注意的是该属性以及过时,建议开发者使用第一种),代码示例如下：

```java
@Api(tags = "2.0.2版本-20200226",position = 286)
@RestController
@RequestMapping("/api/nxew202")
public class Api202Controller {
    
    
}
```

很[接口排序](apiSort.md)规则一样,Knife4j也是通过Spring Plugin插件化的方式，扫描接口注解，最终通过扩展OpenAPI的扩展属性`x-order`进行赋值，最终在Ui界面中解析，然后再进行排序后渲染组件

最终在OpenAPI的结构如下：

![](/knife4j/images/documentation/tagorder.png)

开发者如果遇到排序不生效的问题，可以通过检查接口返回的OpenAPI规范中，分组`tag`节点下是否包含`x-order`的扩展属性