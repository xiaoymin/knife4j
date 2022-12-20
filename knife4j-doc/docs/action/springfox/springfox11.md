# springfox 源码分析(十一) 自定义添加Swagger Models功能实现


在[springfox 源码分析(十) 遍历接口获取Model对象](/docs/action/springfox/springfox10)这一篇中,我们其实已经大致了解了Springfox针对接口中涉及到的Model类进行解析初始化的过程

在默认`OperationModelsProviderPlugin`插件中,`collectGlobalModels`收集全局Models的方法会将我们外部传入的Model添加到Springfox的集合中去,并且最终我们会在Swagger的标准属性定义**definitions**中发现她

那么我们既然知道了springfox的原理,我们知道springfox默认只会把接口中涉及的参数类、返回类、注解中定义的类这三类model添加到框架中

有时,如果我们在程序框架中定义了一些公共的属性Models,但是并没有在接口中使用,此时springfox默认是不会加入的,那么我们应该通过何种方式,才能再swagger的ui界面中看到后端自定义的Model呢

我们通过源码环节知道`OperationModelsProviderPlugin`最终获取全局参数Models是通过`DocumentationContext`对象来获取的,而在[springfox 源码分析(七) 文档初始化-DocumentationContext](/docs/action/springfox/springfox7)这一节时,我们已经介绍了`DocumentationContext`的初始化过程

我们只需要使用springfox为我们提供的Docket对象的方法就可以实现我们的自定义Models

目前Docket对象提供了添加Model的方法，源码如下：

```java
/**
   * Method to add additional models that are not part of any annotation or are perhaps implicit
   *
   * @param first     - at least one is required
   * @param remaining - possible collection of more
   * @return on-going docket
   * @since 2.4.0
   */
public Docket additionalModels(ResolvedType first, ResolvedType... remaining) {
    additionalModels.add(first);
    additionalModels.addAll(newHashSet(remaining));
    return this;
}
```

这是唯一的方法入口,`ResolvedType`是springfox默认使用的jackson提供的类,他是一个静态类

那么,我们如何将Type类型转化为`ResolvedType`类型

jackson也提供了一个类来进行转换,那就是`com.fasterxml.classmate.TypeResolver`

并且该类springfox已经帮助我们注入到了Spring的容器中,具体代码如下：

```java
@Configuration
@ComponentScan(basePackages = {
    "springfox.documentation.schema"
})
@EnablePluginRegistries({
    ModelBuilderPlugin.class,
    ModelPropertyBuilderPlugin.class,
    TypeNameProviderPlugin.class,
    SyntheticModelProviderPlugin.class
})
public class ModelsConfiguration {
  @Bean
  public TypeResolver typeResolver() {
    return new TypeResolver();
  }
}
```

所以,在我们的SwaggerConfiguration配置文件中,只需要将TypeResolver通过注解注入即可使用了

## 第一种方式

在SwaggerConfiguration中引入TypeResolver

```java
@EnableSwagger2
@EnableSwaggerBootstrapUI
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {


    private final TypeResolver typeResolver;

    @Autowired
    public SwaggerConfiguration(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }
    
    
```

在创建Docket对象时,调用additionalModels的方法,代码如下：

```java
@Bean(value = "groupRestApi")
    @Order(value = 1)
    public Docket groupRestApi() {
        List<ResolvedType> list=Lists.newArrayList();

        SpringAddtionalModel springAddtionalModel= springAddtionalModelService.scan("com.swagger.bootstrap.ui.demo.extend");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(groupApiInfo())
                .groupName("分组接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.group"))
                .paths(PathSelectors.any())
                .build()
            //添加自定义Model类型
                .additionalModels(typeResolver.resolve(DeveloperApiInfo.class))
                .ignoredParameterTypes(HttpSession.class).extensions(Lists.newArrayList(new OrderExtensions(2))).securityContexts(Lists.newArrayList(securityContext(),securityContext1())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey(),apiKey1()));
    }
```

这样我们在界面上就可以看见我们默认添加的Model了，效果：

![](/images/springfox/springfox-11-selfmodel.png)

如果我们只是需要添加一个类的情况下,使用这种方式是最简洁的,假如我们有很多类的情况下,我们希望能够提供根据路径包扫描的方式来获取`ResolvedType`,那该如何做呢?

此时,你可以使用第二种方式

## 第二种方式

在[swagger-bootstrap-ui](https://gitee.com/xiaoym/swagger-bootstrap-ui)的1.9.4版本中,为Java开发者提供了公共api方法

在SwaggerConfiguration配置文件中,可以引入swagger-bootstrap-ui提供的工具类

```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    @Autowired
    SpringAddtionalModelService springAddtionalModelService;
    
}
```

**注意**：`@EnableSwaggerBootstrapUI`注解必须在配置类上引入,否则可能引起错误.

然后使用`springAddtionalModelService`提供的scan方法进行包路径扫描，包路径可以是多个,以逗号分隔

```java
@Bean(value = "groupRestApi")
@Order(value = 1)
public Docket groupRestApi() {
    List<ResolvedType> list=Lists.newArrayList();
	//扫描
    SpringAddtionalModel springAddtionalModel= springAddtionalModelService.scan("com.swagger.bootstrap.ui.demo.extend");
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(groupApiInfo())
        .groupName("分组接口")
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.group"))
        .paths(PathSelectors.any())
        .build()
        .additionalModels(springAddtionalModel.getFirst(),springAddtionalModel.getRemaining())
        .ignoredParameterTypes(HttpSession.class).extensions(Lists.newArrayList(new OrderExtensions(2))).securityContexts(Lists.newArrayList(securityContext(),securityContext1())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey(),apiKey1()));
}
```

springAddtionalModelService最终扫描包路径生成`SpringAddtionalModel`对象，该对象源码：

```java
public class SpringAddtionalModel {

    /***
     * 第一个Type
     */
    private ResolvedType first;

    /***
     * 剩余
     */
    private List<ResolvedType> remaining=new ArrayList<>();


    public ResolvedType[] getRemaining() {
        if (!remaining.isEmpty()){
            return remaining.toArray(new ResolvedType[]{});
        }
        return new ResolvedType[]{};
    }

    public ResolvedType getFirst() {
        return first;
    }

    public void setFirst(ResolvedType first) {
        this.first = first;
    }

    public void add(ResolvedType type){
        remaining.add(type);
    }
}
```

注意有两个属性,first和remaining的集合

这也是配合Docket对象提供的additionalModels方法进行的简单封装,开发者扫描包路径后,会得到first以及remaining的集合