# springfox 源码分析(五) web配置类Plugin插件的使用


时间：2019-5-23 14:46:50

地点：家中


## 前言

我们在上一篇文章,通过`@EnableSwagger2`注解,知道springfox使用开启Plugin注解的配置,注入了很多Plugin的配置类，结合我们第二篇针对[Spring Plugin的使用文章](https://www.xiaominfo.com/2019/05/22/springfox-3/)介绍,该篇主要探索springfox中的各种不同Plugin的具体作用,以及声明了那些方法

## 插件

在`SpringfoxWebMvcConfiguration`配置中,主要涉及了以下Plugin

- DocumentationPlugin
- ApiListingBuilderPlugin
- OperationBuilderPlugin
- ParameterBuilderPlugin
- ExpandedParameterBuilderPlugin
- OperationModelsProviderPlugin
- DefaultsProviderPlugin
- PathDecorator
- ApiListingScannerPlugin

代码结构一览：

![](/images/springfox/plugins.png)

plugin中声明的接口,都是为处理contexts上下文中的属性

### DocumentationPlugin

先来看`DocumentationPlugin`的源码

```java
public interface DocumentationPlugin extends Plugin<DocumentationType> {
  /**
   * @return indicator to determine if the plugin is enabled
   */
  boolean isEnabled();

  DocumentationType getDocumentationType();

  /**
   * Creates a documentation context based on a given DocumentationContextBuilder
   *
   * @param builder - @see springfox.documentation.spi.service.contexts.DocumentationContextBuilder
   * @return context to use for building the documentation
   */
  DocumentationContext configure(DocumentationContextBuilder builder);

  /**
   * Gets the group name for the plugin. This is expected to be unique for each instance of the plugin
   * @return group the plugin belongs to
   */
  String getGroupName();
}
```

此Plugin使用的分隔符类是`DocumentationType`,文档类型,在Springfox中声明了三个版本的文档类型，主要是：

- SWAGGER_2:swagger的2.0版本
- SWAGGER_12:Swagger的1.2版本
- SPRING_WEB:springfox项目是由原spring-mvc-swagger项目演变而来,所以这是最早的一个版本

`DocumentationPlugin`定义了三个方法：

- 是否启用
- 获取文档类型
- 通过文档上下文Builder构建文档上下文对象
- 获取分组名称

那么,他的实现类是谁，我们通过IDEA的编辑器功能能轻松定位到,是Docket类

来看类图：

![](/images/springfox/DocumentationPlugin.png)

由于Docket对象是最终实现类,而我们开发者一般在使用时,都是通过创建Docket的实体对象来注入到Spring的容器中

所以,我们创建几个Docket对象的Bean实例，那么通过`PluginRetry<DocumentationPlugin,DocumentationType>`的getPlugins()方法,最终就会获取到外部注入的Docket对象实例,然后再程序中就可以使用了

### ApiListingBuilderPlugin

来看`ApiListingBuilderPlugin`的源码

```java
public interface ApiListingBuilderPlugin extends Plugin<DocumentationType> {
  /**
   * Implement this method to override the ApiListing using the ApiListingBuilder
   *
   * @param apiListingContext - context that can be used to override the model property attributes
   * @see springfox.documentation.service.ApiListing
   * @see springfox.documentation.builders.ApiListingBuilder
   */
  void apply(ApiListingContext apiListingContext);
}
```

实现`ApiListingBuilderPlugin`插件类需要实现apply方法,主要是处理ApiListingContext上下文的属性信息

来看类图：

![](/images/springfox/ApiListingBuilderPlugin.png)

从`ApiListingBuilderPlugin`类图中,我们可以看到,他有三个子类,分别是：

- MediaTypeReader：获取接口的`RequestMapping`注解,赋值Produces和Consumes属性
- ApiListingReader：针对controller名称的处理操作,最后赋值给ApiListingBuilder对象description属性
- SwaggerApiListingReader:获取@Api注解,赋值tag及description属性

这三个实现类都通过`@Component`注解注入到了Spring的容器中

### OperationBuilderPlugin

springfox中的一些列插件Plugin最终的作用都是分别为定义传参的Context上下文进行一系列的赋值处理

每个上下文中几乎都会存在该对象的Builder,最终通过各种不同的Plugin来分别进行赋值,这样整个程序架构会清晰很多

`Operation`也不例外,先来看Operation的上下文类

`OperationContext.java`

```java
public class OperationContext {
    //builder函数
  private final OperationBuilder operationBuilder;
    //Spring中接口的请求方法类型枚举
  private final RequestMethod requestMethod;
    //请求接口上下文
  private final RequestMappingContext requestContext;
  private final int operationIndex;
    //getter setter and constructor
    
}
```

此时,我们来看`OperationBuilderPlugin`插件的源码：

```java
public interface OperationBuilderPlugin extends Plugin<DocumentationType> {
  /**
   * Implement this method to override the Operation using the OperationBuilder available in the context
   *
   * @param context - context that can be used to override the parameter attributes
   * @see springfox.documentation.service.Operation
   * @see springfox.documentation.builders.OperationBuilder
   */
  void apply(OperationContext context);
}
```

顶级Plugin声明接口,分层逐步给OperationContext对象赋值

先来看部分类图：

![](/images/springfox/OperationBuilderPlugin.png)

由于`OperationBuilderPlugin`实现类比较多,此处类图进列出其中四个实现类,我们通过文字来一一说明

- `DefaultOperationReader`:请求方法、接口说明、唯一id值
- `MediaTypeReader`:consumes、produces
- `OperationAuthReader`:权限赋值
- `OperationDeprecatedReader`；接口是否过时
- `OperationHiddenReader`：是否隐藏
- `OperationHttpMethodReader`:接口请求方法
- `OperationImplicitParameterReader`:针对`@ApiImplicitParam`注解的接口进行读取赋值
- `OperationImplicitParametersReader`:针对`@ApiImplicitParams`注解的接口进行读取赋值
- `OperationNicknameIntoUniqueIdReader`:昵称属性,通过读取`@ApiOperation`注解中的`nickname`属性进行赋值
- `OperationNotesReader`:接口说明
- `OperationParameterHeadersConditionReader`:请求头
- `OperationParameterReader`:后端配置的全局parameter以及接口的parameter参数进行读取赋值
- `OperationParameterRequestConditionReader`:参数条件
- `OperationPositionReader`:position属性
- `OperationResponseClassReader`:响应类处理
- `OperationSummaryReader`:接口名称
- `OperationTagsReader`:tags
- `ResponseMessagesReader`:响应状态码信息，先读取后端配置的全局,然后读取接口
- `SwaggerMediaTypeReader`:consumes、produces
- `SwaggerOperationResponseClassReader`:响应class类
- `SwaggerOperationTagsReader`：接口的tags处理
- `SwaggerResponseMessageReader`:状态码信息,针对`@ApiResponse`注解标注的接口
- `VendorExtensionsReader`:扩展

整个代码结构一览：

![](/images/springfox/scan.png)

### ParameterBuilderPlugin

针对参数处理的Plugin，先来看相关类图：

![](/images/springfox/ParameterBuilderPlugin.png)

总共有七个实现类，我们一一说明：

- `ApiParamParameterBuilder`:针对接口使用`@ApiParam`注解的参数进行处理
- `ParameterDataTypeReader`:参数的数据类型
- `ParameterDefaultReader`:参数默认值
- `ParameterMultiplesReader`:
- `ParameterNameReader`:参数名称
- `ParameterRequiredReader`:参数是否必须
- `ParameterTypeReader`：参数类型，包括（form、header、query、formdata、body），默认是body

### ExpandedParameterBuilderPlugin

先来看类图关系

![](/images/springfox/ExpandedParameterBuilderPlugin.png)

从类图关系中,实现类主要有两个：

- `ExpandedParameterBuilder`:实体类参数的默认属性赋值
- `SwaggerExpandedParameterBuilder`:针对我们的类使用`@ApiModelProperty`注解的操作

### OperationModelsProviderPlugin

来看类图关系

![](/images/springfox/OperationModelsProviderPlugin.png)

- `OperationModelsProvider`:收集所有的参数Models,返回类型，全局Models
- `SwaggerOperationModelsProvider`:接口返回类型已经使用`@ApiResponse`标注的返回类型

### DefaultsProviderPlugin

来看`DefaultsProviderPlugin`的源码

```java
public interface DefaultsProviderPlugin extends Plugin<DocumentationType> {
  /**
   * Implement this method to override the @see  springfox.documentation.spi.service.contexts
   * .DocumentationContextBuilder
   *
   * @param documentationType - creates a default DocumentationContextBuilder based on documentation type
   * @return - returns the documentation context builder
   */
  DocumentationContextBuilder create(DocumentationType documentationType);
}

```

根据文档类型创建`DocumentationContextBuilder`对象

而`DefaultsProviderPlugin`只有一个实现子类,那就是`springfox.documentation.spring.web.plugins.DefaultConfiguration`

```java
public class DefaultConfiguration implements DefaultsProviderPlugin {

  private final Defaults defaults;
  private final TypeResolver typeResolver;
  private final ServletContext servletContext;

  public DefaultConfiguration(Defaults defaults,
                       TypeResolver typeResolver,
                       ServletContext servletContext) {

    this.servletContext = servletContext;
    this.defaults = defaults;
    this.typeResolver = typeResolver;
  }

  @Override
  public DocumentationContextBuilder create(DocumentationType documentationType) {
    return new DocumentationContextBuilder(documentationType)
            .operationOrdering(defaults.operationOrdering())
            .apiDescriptionOrdering(defaults.apiDescriptionOrdering())
            .apiListingReferenceOrdering(defaults.apiListingReferenceOrdering())
            .additionalIgnorableTypes(defaults.defaultIgnorableParameterTypes())
            .rules(defaults.defaultRules(typeResolver))
            .defaultResponseMessages(defaults.defaultResponseMessages())
            .pathProvider(new RelativePathProvider(servletContext))
            .typeResolver(typeResolver)
            .enableUrlTemplating(false)
            .selector(ApiSelector.DEFAULT);
  }

  @Override
  public boolean supports(DocumentationType delimiter) {
    return true;
  }
}

```

给DocumentationContextBuilder创建一系列空对象默认值,方便后期其他Plugin进行赋值初始化

`Defaults`和`TypeResolver`在前面的Configuration配置类中已经通过Bean注解进行了注入

### PathDecorator

先来看类图：

![](/images/springfox/PathDecorator.png)

`PathDecorator`声明了一个Guava函数库中的Function函数接口

`Function`

```java

@GwtCompatible
public interface Function<F, T> {
  @Nullable
  @CanIgnoreReturnValue // TODO(kevinb): remove this
  T apply(@Nullable F input);

  /**
   * <i>May</i> return {@code true} if {@object} is a {@code Function} that behaves identically to
   * this function.
   *
   * <p><b>Warning: do not depend</b> on the behavior of this method.
   *
   * <p>Historically, {@code Function} instances in this library have implemented this method to
   * recognize certain cases where distinct {@code Function} instances would in fact behave
   * identically. However, as code migrates to {@code java.util.function}, that behavior will
   * disappear. It is best not to depend on it.
   */
  @Override
  boolean equals(@Nullable Object object);
}
```

传入输入参数,返回输出参数

主要实现类：

- `OperationPathDecorator`:basePath处理类
- `PathMappingDecorator`:接口path处理
- `PathSanitizer`:接口path-origin处理
- `QueryStringUriTemplateDecorator`:接口参数处理

### ApiListingScannerPlugin

来看源码

```java
public interface ApiListingScannerPlugin extends Plugin<DocumentationType> {
  /**
   * Implement this method to manually add ApiDescriptions
   *
   * @param context - Documentation context that can be used infer documentation context
   * @see springfox.documentation.service.ApiDescription
   * @return List of {@link ApiDescription}
   */
  List<ApiDescription> apply(DocumentationContext context);
}

```

该Plugin没有任何实现子类

## 总结

通过上面的Plugin,我们大致了解了Springfox中定义的Plugin接口,以及实现类,完事具备，此时只需要查看springfox的初始化代码部分了

在下一篇文章中我们继续.