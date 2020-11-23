# 前端默认开启增强模式

::: warning
`knife4j` 版本>2.0.1 使用此规则
:::

**自2.0.6版本后,Knife4j的增强模式可以由后端进行配置,Knife4j提供了更加符合Spring Boot以及OpenAPI规范的方式进行使用,推荐开发者升级使用,文档请移步[增强模式](enhance.md)**

`knife4j`所提供Swagger-Ui默认是访问Springfox-Swagger所提供的`/v2/api-docs`接口,从开源至今,一直默认访问的就是该地址

但随着版本的更新迭代,使用的开发者越来越多,`knife4j`也响应支持了一些特有的增强功能，增强功能主要包括：

- 接口分组排序
- 接口排序
- 参数自动忽略
- 针对`@RequestBody` Map类型的请求提供动态字段注释
- 给接口添加作者信息
- 自定义Markdown文档
- 文档权限过滤
- more...

同时也保留了一些个性化的配置特性,用于`knife4j`的文档调试使用,如下图：

![](/knife4j/images/knife4j/set.png)


在以前的做法,开发者是先访问`doc.html`,然后在个性化配置中勾选 启用Knife4j提供的增强功能

勾选后,重新刷新页面,此时,`knife4j`的ui界面会访问`knife4j`提供的接口地址`/v2/api-docs-ext` ,`knife4j`提供的增强功能扩展属性都会从这个接口中返回,前端ui会解析增强部分并且在页面中体现

**提供这样的方式主要原因是**：

1、原本Knife4j-spring-ui所替代的角色是`springfox-swagger-ui`,一开始的愿景也是希望开发者能够无缝集成,减少软件集成的学习成本,在Java的Maven Pom文件中直接引入jar包就可以完成集成(前提是你已经集成了springfox-swagger)并使用，这种体验对于开发者来说是再简单不过的

2、另外是Knife4j还在发展中,强制使用自己所提供的功能对于各个软件的兼容难免会存在问题,所以把Knife4j所提供的增强功能使用权也是交给开发者,让开发者做选择。


**现在**

上面说了默认规则,那么在增强模式下,能否默认在前端界面就开启呢?

> 有时候我们确实需要`knife4j`所提供的增强功能,但是每次在打开后都需要个性化设置界面勾选启用增强会很繁琐,特别是和别人接口对接的情况下,别人希望看到的例如排序、忽略参数等特性能直接体现

`knife4j`针对这样的需求提供了解决方法,但是有些前提条件是开发者需要首先确保的，主要注意点：

1、Java后端必须在`@EnableSwagger2`的基础上再添加`@EnableKnife4j`增强注解,代码示例如下：
```java
@Configuration
@EnableSwagger2
@EnableKnife4j
public class SwaggerConfiguration {
    
 //more..

}
```

2、如果你使用Spring Security、Shiro这类权限框架时,需要对接口地址`/v2/api-docs-ext`进行放权


**最后**

以上操作完成后,在界面端直接访问地址：`http://host:port/doc.html#/plus` 

以上地址告诉`knife4j`的前端界面后端增强功能万事俱备,前端可以使用了


 
 
 