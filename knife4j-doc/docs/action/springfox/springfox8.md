# springfox 源码分析(八) 遍历接口获取Model对象


我们通过读`DocumentationPluginsBootstrapper`代码中的`start`方法,了解到springfox根据我们外部提供的Docket对象进行初始化时,会通过Docket对象构建DocumentationContext对象来进行初始化操作

```java
private DocumentationContext buildContext(DocumentationPlugin each) {
    return each.configure(defaultContextBuilder(each));
}
```

`BuildContext`方法是通过Docket对象来构建最终的DocumentaionContext对象

所以我们在研究Documentation之前,先来看DocumentationContext对象主要是包含哪些属性和方法

先来看`DocumentationContext`的源码：

```java
public class DocumentationContext {
  //文档类型
  private final DocumentationType documentationType;
  //请求接口
  private final List<RequestHandler> handlerMappings;
  //接口信息，包括title、描述等信息
  private final ApiInfo apiInfo;
  //分组名称
  private final String groupName;
  //接口选择器
  private final ApiSelector apiSelector;

  private final AlternateTypeProvider alternateTypeProvider;
  //忽略的参数类型
  private final Set<Class> ignorableParameterTypes;
  //请求方法对应的响应状态码信息
  private final Map<RequestMethod, List<ResponseMessage>> globalResponseMessages;
  //全局参数
  private final List<Parameter> globalOperationParameters;
  //分组策略
  private final ResourceGroupingStrategy resourceGroupingStrategy;
  //路径Provider
  private final PathProvider pathProvider;
  //安全信息
  private final List<SecurityContext> securityContexts;
  //安全Scheme
  private final List<? extends SecurityScheme> securitySchemes;
  //接口信息
  private final Ordering<ApiListingReference> listingReferenceOrdering;
  //接口描述
  private final Ordering<ApiDescription> apiDescriptionOrdering;
  //接口信息
  private final Ordering<Operation> operationOrdering;
  private final GenericTypeNamingStrategy genericsNamingStrategy;
  private final Optional<String> pathMapping;
  private final Set<ResolvedType> additionalModels;
  //tag分组标签
  private final Set<Tag> tags;
  private Set<String> produces;
  private Set<String> consumes;
  //主机号
  private String host;
  //协议
  private Set<String> protocols;
  private boolean isUriTemplatesEnabled;
  //扩展属性
  private List<VendorExtension> vendorExtensions;
  //getter and setter and constructor
}
```

我们姑且称他为文档上下文环境吧,springfox是通过文档上下文(DocumentationContext)最终构建真正的Documenation对象,然后缓存在内存中,最终通过接口`/v2/api-docs`将Documentation对象转换为标准的Swagger对象输出.

## DocumentationContextBuilder

在springfox的源码中,大量的使用了Builder构造器来进行目标对象的构建,所以文档上下文也一样,最终通过DocumentaionContextBuilder来构造创建

`DocumentationPlugin`中提供了根据`DocumentationContextBuilder`来创建DocumenationContext的方法

```java
public interface DocumentationPlugin extends Plugin<DocumentationType> {
  /**
   * Creates a documentation context based on a given DocumentationContextBuilder
   *
   * @param builder - @see springfox.documentation.spi.service.contexts.DocumentationContextBuilder
   * @return context to use for building the documentation
   */
  DocumentationContext configure(DocumentationContextBuilder builder);
    
}
```



### 源码

根据DocumentaionPlugin对象来构建Builder

```java
public class DocumentationContextBuilder {
  //安全参数
  private final List<SecurityContext> securityContexts = newArrayList();
  //忽律类型
  private final Set<Class> ignorableParameterTypes = newHashSet();
  //接口响应状态码
  private final Map<RequestMethod, List<ResponseMessage>> responseMessageOverrides = newTreeMap();
  //全局参数
  private final List<Parameter> globalOperationParameters = newArrayList();
  //类型规则
  private final List<AlternateTypeRule> rules = newArrayList();
  //默认接口状态响应吗
  private final Map<RequestMethod, List<ResponseMessage>> defaultResponseMessages = newHashMap();
  //protocols
  private final Set<String> protocols = newHashSet();
  private final Set<String> produces = newHashSet();
  private final Set<String> consumes = newHashSet();
  //扩展类型
  private final Set<ResolvedType> additionalModels = newHashSet();
  //分组tag
  private final Set<Tag> tags = newTreeSet(Tags.tagComparator());
  //扩展属性
  private List<VendorExtension> vendorExtensions = new ArrayList<VendorExtension>();
  //类型处理器
  private TypeResolver typeResolver;
  //接口集合
  private List<RequestHandler> handlerMappings;
  //接口信息
  private ApiInfo apiInfo;
  //分组名称
  private String groupName;
  //资源分组策略
  private ResourceGroupingStrategy resourceGroupingStrategy;
  //路径处理
  private PathProvider pathProvider;
  private List<? extends SecurityScheme> securitySchemes;
  private Ordering<ApiListingReference> listingReferenceOrdering;
  private Ordering<ApiDescription> apiDescriptionOrdering;
  //swagger文档类型
  private DocumentationType documentationType;
  private Ordering<Operation> operationOrdering;
  private boolean applyDefaultResponseMessages;
  //接口选择器
  private ApiSelector apiSelector = ApiSelector.DEFAULT;
  //主机
  private String host;
  //默认类型名称策略
  private GenericTypeNamingStrategy genericsNamingStrategy;
  //接口路径映射
  private Optional<String> pathMapping;
  
  private boolean isUrlTemplatesEnabled;
}
```

`DocumentationContextBuilder`基本覆盖了`DocumentationContext`的所有属性,而`DocumentationContextBuilder`没有提供构造函数来实例化参数，只提供了一个构造函数(文档类型),其余参数都是通过Builder构造器模式来赋值属性,最终通过Builder()方法来构建输出DocumentaionContext

**构造函数**

```java
public DocumentationContextBuilder(DocumentationType documentationType) {
    this.documentationType = documentationType;
}
```

**赋值属性**

```java
public DocumentationContextBuilder requestHandlers(List<RequestHandler> handlerMappings) {
    this.handlerMappings = handlerMappings;
    return this;
}
//more...
```

通过返回this对象的方式,提供了很多属性的赋值方法

**build构造**

```java
public DocumentationContext build() {
    Map<RequestMethod, List<ResponseMessage>> responseMessages = aggregateResponseMessages();
    OrderComparator.sort(rules);
    return new DocumentationContext(documentationType,
        handlerMappings,
        apiInfo,
        groupName,
        apiSelector,
        ignorableParameterTypes,
        responseMessages,
        globalOperationParameters,
        resourceGroupingStrategy,
        pathProvider,
        securityContexts,
        securitySchemes,
        rules,
        listingReferenceOrdering,
        apiDescriptionOrdering,
        operationOrdering,
        produces,
        consumes,
        host,
        protocols,
        genericsNamingStrategy,
        pathMapping,
        isUrlTemplatesEnabled,
        additionalModels,
        tags,
        vendorExtensions);
  }
```

最终通过使用build()方法,调用`DocumentationContext`的构造函数,构建`DocumentationContext`对象

### 构造

所以,我们先来看`DocumentationContextBuilder`对象的创建,主要包含了那些参数、方法

```java
/***
   * 构建文档builder
   * @param plugin
   * @return
   */
  private DocumentationContextBuilder defaultContextBuilder(DocumentationPlugin plugin) {
    DocumentationType documentationType = plugin.getDocumentationType();
    //获取RequestHandler
    //疑问：handlerProviders在何时初始化
    List<RequestHandler> requestHandlers = from(handlerProviders)
        .transformAndConcat(handlers())
        .toList();
    List<AlternateTypeRule> rules = from(nullToEmptyList(typeConventions))
          .transformAndConcat(toRules())
          .toList();
    return documentationPluginsManager
        .createContextBuilder(documentationType, defaultConfiguration)
        .rules(rules)
        .requestHandlers(combiner().combine(requestHandlers));
  }
```

通过代码我们可以了解到：

- 首先获取所有的RequestHnadler，而RequestHandlerProvider的默认实现类是`WebMvcRequestHandlerProvider`,该实现类会接收Spring中的所有请求Mapping，最终转化为`WebMvcRequestHandler`,WebMvcRequestHandler是接口`RequestHnadler`的实现，这等于是拿到了所有的接口
- 获取AlternateTypeRule的规则列表
- 初始化创建Builder,赋值请求接口、rules

了解了`DocumentationContextBuilder`的构造方式,在来看他的创建过程

通过代码知道是通过`DocumentationPluginsManager`的`createContextBuilder`方法来构建

```java
public DocumentationContextBuilder createContextBuilder(
    DocumentationType documentationType,
    DefaultConfiguration defaultConfiguration) {
    return defaultsProviders.getPluginFor(documentationType, defaultConfiguration)
        .create(documentationType)
        .withResourceGroupingStrategy(resourceGroupingStrategy(documentationType));
}
```

defaultsProviders是也是一个Plugin接口,但是我们在前面章节也介绍说过,他只有一个实现类`DefaultConfiguration`,但是该实现类并没有通过`@Compoent`注解注入到Spring的容器中,所以此处通过Plugin的实现给了一个默认值defaultConfiguration，其实,此处就是使用的defaultConfiguration

那么该默认配置的create方法做了那些操作呢?,继续跟踪代码：

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
}
```

主要是给`DocumentationContextBuilder`赋值了默认的相关参数，主要包括：

- 默认忽略Class类型
- 默认响应状态码消息
- 类型解析器
- 接口选择器
- 接口排序
- ...

通过上面源码我们知道：

- 首先defaultsProviders是也是一个Plugin接口,但是我们在前面章节也介绍说过,他只有一个实现类`DefaultConfiguration`,但是该实现类并没有通过`@Compoent`注解注入到Spring的容器中,所以此处通过Plugin的实现给了一个默认值defaultConfiguration，其实,此处就是使用的defaultConfiguration

- defaultConfiguration是在`DocumentationPluginsBootstrapper`的构造函数中通过new的方式进行构造的

- ```java
  //通过DefaultConfiguration可以构建DocumentationContextBuilder
  this.defaultConfiguration = new DefaultConfiguration(defaults, typeResolver, servletContext);
  ```

- 在此处的构建过程中，赋值了资源分组策略.

### 赋值

从源码过程中,我们已经了解到Builder赋值的参数主要包括：

- Spring环境中的所有接口,最终是RequestHandler的集合，实际则是`WebMvcRequestHandler`的集合
- 赋值AlternateTypeRule规则集合
- 赋值分组策略属性(resourceGroupingStrategy),默认是`ClassOrApiAnnotationResourceGrouping`实现

## DocumentationContext

获取到了DocumentationContextBuilder对象,此时在通过`DocumentationPlugin`的configure方法,构建`DocumentationContext`

来看Docket的configure方法

```java
/**
   * Builds the Docket by merging/overlaying user specified values.
   * It is not necessary to call this method when defined as a spring bean.
   * NOTE: Calling this method more than once has no effect.
   *
   * @see DocumentationPluginsBootstrapper
   */
  public DocumentationContext configure(DocumentationContextBuilder builder) {
    return builder
        .apiInfo(apiInfo)
        .selector(apiSelector)
        .applyDefaultResponseMessages(applyDefaultResponseMessages)
        .additionalResponseMessages(responseMessages)
        .additionalOperationParameters(globalOperationParameters)
        .additionalIgnorableTypes(ignorableParameterTypes)
        .ruleBuilders(ruleBuilders)
        .groupName(groupName)
        .pathProvider(pathProvider)
        .securityContexts(securityContexts)
        .securitySchemes(securitySchemes)
        .apiListingReferenceOrdering(apiListingReferenceOrdering)
        .apiDescriptionOrdering(apiDescriptionOrdering)
        .operationOrdering(operationOrdering)
        .produces(produces)
        .consumes(consumes)
        .host(host)
        .protocols(protocols)
        .genericsNaming(genericsNamingStrategy)
        .pathMapping(pathMapping)
        .enableUrlTemplating(enableUrlTemplating)
        .additionalModels(additionalModels)
        .tags(tags)
        .vendorExtentions(vendorExtensions)
        .build();
  }
```

针对Builder的一些列二次赋值,最终通过build方法构造

我们的Docket对象是我们开发人员在外部通过Bean来创建的,来看Docket的部分代码：

```java
public class Docket implements DocumentationPlugin {

  public static final String DEFAULT_GROUP_NAME = "default";

  private final DocumentationType documentationType;
  private final List<SecurityContext> securityContexts = newArrayList();
  private final Map<RequestMethod, List<ResponseMessage>> responseMessages = newHashMap();
  private final List<Parameter> globalOperationParameters = newArrayList();
  private final List<Function<TypeResolver, AlternateTypeRule>> ruleBuilders = newArrayList();
  private final Set<Class> ignorableParameterTypes = newHashSet();
  private final Set<String> protocols = newHashSet();
  private final Set<String> produces = newHashSet();
  private final Set<String> consumes = newHashSet();
  private final Set<ResolvedType> additionalModels = newHashSet();
  private final Set<Tag> tags = newHashSet();

  private PathProvider pathProvider;
  private List<? extends SecurityScheme> securitySchemes;
  private Ordering<ApiListingReference> apiListingReferenceOrdering;
  private Ordering<ApiDescription> apiDescriptionOrdering;
  private Ordering<Operation> operationOrdering;

  private ApiInfo apiInfo = ApiInfo.DEFAULT;
  private String groupName = DEFAULT_GROUP_NAME;
  private boolean enabled = true;
  private GenericTypeNamingStrategy genericsNamingStrategy = new DefaultGenericTypeNamingStrategy();
  private boolean applyDefaultResponseMessages = true;
  private String host = "";
  private Optional<String> pathMapping = Optional.absent();
  private ApiSelector apiSelector = ApiSelector.DEFAULT;
  private boolean enableUrlTemplating = false;
  private List<VendorExtension> vendorExtensions = newArrayList();
}
```

我们通过Docket来外部赋值的对象值,最终都会构建到`DocumentationContext`上下文中，我们先来看我们开发中一般创建Docket对象过程

```java
@Bean(value = "defaultApi")
@Order(value = 4)
public Docket defaultApi() {
    ParameterBuilder parameterBuilder=new ParameterBuilder();
    List<Parameter> parameters= Lists.newArrayList();
    parameterBuilder.name("token").description("token令牌").modelRef(new ModelRef("String"))
        .parameterType("header")
        .required(true).build();
    parameters.add(parameterBuilder.build());

    Docket docket=new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .groupName("默认接口")
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.controller"))
        //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
        .paths(PathSelectors.any())
        .build().globalOperationParameters(parameters)
        .securityContexts(Lists.newArrayList(securityContext())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
    return docket;
}
```

一般创建Docket对象,主要的赋值参数：

- 分组名称
- apiInfo信息
- ApiSelector
- 全局参数
- 权限验证

主要包含了以上的一些信息，整个文档上下文环境构造完成,我们也可以以Debug的方式来跟踪代码,如下图：

![](/images/springfox/init.png)

除了默认的参数赋值外,接口、definitions、model等关键信息等都还没有初始化。