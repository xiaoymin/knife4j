# 接口排序

::: warning
**接口排序**功能是`knife4j`提供的增强功能,开发者要想使用`knife4j`提供的增强功能,必须在Swagger的配置文件中引入`@EnableSwaggerBootstrapUi`注解 <br />
注意：在使用swagger-bootstrap-ui时,该注解是`@EnableSwaggerBootstrapUI`,而在knife4j中,最后字母变为小写,为`@EnableSwaggerBootstrapUi`
:::

代码示例如下：
```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUi
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    
 //more..

}
```


在开启增强注解后,我们在代码中就可以对接口进行排序了

## 接口分组排序

在swagger中,我们没一个Controller类一般代表的是一个业务功能接口组合,此时,我们要对接口的tags进行排序主要有两种方式：

第一种方式：使用注解`@Api`中的`position`属性,如下：
```java
@Api(tags = "1.9.5版本-20190728",position = 290)
@RestController
@RequestMapping("/api/new195")
public class Api195Controller {
//more..    

}
```

第二种方式,使用`knife4j`提供的增强注解`@ApiSort`,代码示例如下：

```java
@Api(tags = "1.9.5版本-20190728")
@ApiSort(290)
@RestController
@RequestMapping("/api/new195")
public class Api195Controller {
//more..    

}
```

## 接口排序

我们在对接口的分组排序后,有时候我们希望对我们分组下的接口进行排序,例如我们一个业务注册接口,分为几个步骤：step1、step2、step3等等,通过排序后,有助于我们开发效率的提升,减少前后端的沟通效率

使用`knife4j`提供的增强注解`@ApiOperationSupport`的`order`属性进行排序,代码示例如下：

```java
@ApiOperation(value = "注册接口第1步")
@ApiOperationSupport(order=1)
@GetMapping("/step1")
public Rest<Map<String,String>> getBody(@RequestBody Map<String,String> map){
    Rest<Map<String,String>> r=new Rest<>();
    r.setData(map);
    return r;
}

@ApiOperation(value = "注册接口第2步")
@ApiOperationSupport(order=2)
@GetMapping("/step2")
public Rest<Map<String,String>> getBody(@RequestBody Map<String,String> map){
    Rest<Map<String,String>> r=new Rest<>();
    r.setData(map);
    return r;
}

```

 
 <icp/> 
 comment/> 
 
 
 
 