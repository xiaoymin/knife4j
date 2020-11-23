# 忽略参数Class类型

> 我们在前面的源码过程中,了解了springfox的基本工作原理,接下来,我们可以通过使用springfox给我们提供的外部接口,来处理一些我们工作中碰到的问题,或者进行自定义扩展

本篇主要介绍如何来忽略某些特定的参数Class类型

先举一个例子,假如我们的接口是这样的：

```java
@PostMapping("/createOr33der")
@ApiOperation(value = "创建订单")
public Rest<Order> createOrdetr(@RequestBody Order order, HttpSession httpSession){
    Rest<Order> r=new Rest<>();
    r.setData(order);
    return r;
}
```

我们在我们的方法上接收了一个`HttpSession`的参数对象,此时,我们来看我们的文档页面效果

![](/knife4j/images/springfox/springfox-ignore-class.png)

在我们的文档介绍页面中,多出了很多我们自认为不必要的参数,因为`HttpSession`对象我们并不需要传参处理,该对象我们是直接拿来使用对session进行操作的

那么,针对这种情况,我们应该如何处理呢（即把HttpSession的参数处理掉,不显示）?

目前有两种方法：

- 在方法的参数前,添加`@ApiIgnore`注解
- 在创建Docket对象时,指定忽略相关的Class

## 第一种方式

先来看第一种的代码处理方式：

```java
@PostMapping("/createOr33der")
@ApiOperation(value = "创建订单")
public Rest<Order> createOrdetr(@RequestBody Order order,@ApiIgnore HttpSession httpSession){
    Rest<Order> r=new Rest<>();
    r.setData(order);
    return r;
}
```

我们在`HttpSession`的参数前,添加`@ApiIgnore`注解，那么此时springfox会帮助我们忽略此参数

`@ApiIgnore`的作用方法不仅仅在参数上,还可以作用于整个接口Controller，或者单个接口

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
public @interface ApiIgnore {
  /**
   * A brief description of why this parameter/operation is ignored
   * @return  the description of why it is ignored
   */
  String value() default "";
}
```

其作用范围是`METHOD`、`TYPE`、`PARAMETER`

## 第二种方式

虽然第一种方式达到了我们的目的,但是细心的朋友可能会发现,如果目前的接口方法中,存在大量的`HttpSession`参数，那么每个方法都需要更改一遍?那岂不是炸了

所以针对这种情况,springfox在我们创建Docket对象时,提供了入口,我们在创建Docket对象时就可以默认传入需要忽略的类Class，这样我们就不需要每个接口都更改一遍了

先来看Docket的部分源码

```java
/**
   * Adds ignored controller method parameter types so that the framework does not generate swagger model or parameter
   * information for these specific types.
   * e.g. HttpServletRequest/HttpServletResponse which are already included in the pre-configured ignored types.
   *
   * @param classes the classes to ignore
   * @return this Docket
   * @see springfox.documentation.spi.service.contexts.Defaults#defaultIgnorableParameterTypes()
   */
public Docket ignoredParameterTypes(Class... classes) {
    this.ignorableParameterTypes.addAll(Arrays.asList(classes));
    return this;
}
```

传入Class的集合

springfox框架默认忽略的类型在`Default`中

```java
private void initIgnorableTypes() {
    ignored = newHashSet();
    ignored.add(ServletRequest.class);
    ignored.add(Class.class);
    ignored.add(Void.class);
    ignored.add(Void.TYPE);
    ignored.add(HttpServletRequest.class);
    ignored.add(HttpServletResponse.class);
    ignored.add(HttpHeaders.class);
    ignored.add(BindingResult.class);
    ignored.add(ServletContext.class);
    ignored.add(UriComponentsBuilder.class);
    ignored.add(ApiIgnore.class); //Used to ignore parameters
}
```

此时,我们在创建Docket对象时,做一下更改,如下：

```java
@Bean(value = "groupRestApi")
@Order(value = 1)
public Docket groupRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(groupApiInfo())
        .groupName("分组接口")
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.group"))
        .paths(PathSelectors.any())
        .build()
        .ignoredParameterTypes(HttpSession.class) //添加忽略类型
        .extensions(Lists.newArrayList(new OrderExtensions(2))).securityContexts(Lists.newArrayList(securityContext(),securityContext1())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey(),apiKey1()));
}
```

通过ignoredParameterTypes方法,传入`HttpSession`的class，告诉springfox框架该class需要忽略

最终的效果如下：

![](/knife4j/images/springfox/springfox-ignore-class1.png)
 
 
 