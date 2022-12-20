# springfox 源码分析(十) 遍历接口获取Model对象



在上一篇中,我们了解到了springfox通过groupName的过滤,拿到了所有的接口,并且通过guava库的`ArrayListMultimap`对接口的Controller进一步进行了分组,接下来就是解析每个接口的操作了

这篇主要介绍springfox解析每个接口中涉及的Model类操作,这其中包含：

- 参数中是Java Bean的参数类型
- 接口返回非void、基础类型的类型
- 在`@ApiResponse`注解中标注返回的class类型

目前主要有以上这三种类型,通过解析拿到接口涉及的Model类型,然后添加到一个Set集合中,该Set集合就对应了Swagger标准属性的definitions属性

先来看遍历接口的代码：

```java
for (final ResourceGroup resourceGroup : sortedByName(allResourceGroups)) {
  DocumentationContext documentationContext = context.getDocumentationContext();
  Set<String> produces = new LinkedHashSet<String>(documentationContext.getProduces());
  Set<String> consumes = new LinkedHashSet<String>(documentationContext.getConsumes());
  String host = documentationContext.getHost();
  Set<String> protocols = new LinkedHashSet<String>(documentationContext.getProtocols());
  Set<ApiDescription> apiDescriptions = newHashSet();

  Map<String, Model> models = new LinkedHashMap<String, Model>();
  //得到该Controller下的所有接口
  List<RequestMappingContext> requestMappings = nullToEmptyList(requestMappingsByResourceGroup.get(resourceGroup));
  for (RequestMappingContext each : sortedByMethods(requestMappings)) {
    //拿到该接口的所有Model
    models.putAll(apiModelReader.read(each.withKnownModels(models)));
    apiDescriptions.addAll(apiDescriptionReader.read(each));
  }
```

遍历`RequestMappingContext`对象,在前面我也已经介绍过,该对象其实就是每个接口实例对象

最主要的是来看`apiModelReader.read`方法

`each.withKnownModels`方法是通过new关键字复制了一个新的`RequestMappingContext`

```java
public RequestMappingContext withKnownModels(Map<String, Model> knownModels) {
    return new RequestMappingContext(documentationContext, handler,
                                     operationModelContextsBuilder, requestMappingPattern, knownModels);
}
```

来看读取接口Models的源码：

```java
/***
   * 读取该接口Model信息
   * @param context
   * @return
   */
public Map<String, Model> read(RequestMappingContext context) {
    //忽略的class集合，如果没有额外设置,则得到的是Defaults类中的默认忽略类Class集合
    Set<Class> ignorableTypes = newHashSet(context.getIgnorableParameterTypes());
    Set<ModelContext> modelContexts = pluginsManager.modelContexts(context);
    Map<String, Model> modelMap = newHashMap(context.getModelMap());
    for (ModelContext each : modelContexts) {
        markIgnorablesAsHasSeen(typeResolver, ignorableTypes, each);
        Optional<Model> pModel = modelProvider.modelFor(each);
        if (pModel.isPresent()) {
            LOG.debug("Generated parameter model id: {}, name: {}, schema: {} models",
                      pModel.get().getId(),
                      pModel.get().getName());
            mergeModelMap(modelMap, pModel.get());
        } else {
            LOG.debug("Did not find any parameter models for {}", each.getType());
        }
        populateDependencies(each, modelMap);
    }
    return modelMap;
}
```

初步通过源码得知,要想先得到Model,则需要先构造得到ModelContext

- 初始化外部涉及到的忽略类型Set集合
- 读取RequestMappingContext，拿到ModelContext的Set集合
- context中的ModelMap在此处其实是空对象，对应的modelMap也是空对象,这个在上面的`withKnownModels`方法中我们可以观察到
- 遍历ModelContext的Set集合,通过modelProvider.modelFor的方法由ModelContext转换构造成目标Model对象

## ModelContext

在初始化ModelContext集合之前,我们先来看看ModelContext的属性

```java

public class ModelContext {
    //类型
  private final Type type;
    //是否返回类型
  private final boolean returnType;
    //分组名称
  private final String groupName;
    //文档类型
  private final DocumentationType documentationType;
  //父级
  private final ModelContext parentContext;
    //ResolvedType
  private final Set<ResolvedType> seenTypes = newHashSet();
    //model构造器
  private final ModelBuilder modelBuilder;
  private final AlternateTypeProvider alternateTypeProvider;
    //名称策略
  private final GenericTypeNamingStrategy genericNamingStrategy;
    //忽略类型
  private final ImmutableSet<Class> ignorableTypes;

  private ModelContext(
      String groupName,
      Type type,
      boolean returnType,
      DocumentationType documentationType,
      AlternateTypeProvider alternateTypeProvider,
      GenericTypeNamingStrategy genericNamingStrategy,
      ImmutableSet<Class> ignorableTypes) {
    this.groupName = groupName;
    this.documentationType = documentationType;
    this.alternateTypeProvider = alternateTypeProvider;
    this.genericNamingStrategy = genericNamingStrategy;
    this.ignorableTypes = ignorableTypes;
    this.parentContext = null;
    this.type = type;
    this.returnType = returnType;
    this.modelBuilder = new ModelBuilder();
  }
 //...   
}
```

参数对象全部是final关键字修饰

## 初始化ModelContext Set集合

先来看`pluginsManager.modelContexts`方法中的初始化操作

```java
public Set<ModelContext> modelContexts(RequestMappingContext context) {
    DocumentationType documentationType = context.getDocumentationContext().getDocumentationType();
    //构建该接口的ModelContext集合
    for (OperationModelsProviderPlugin each : operationModelsProviders.getPluginsFor(documentationType)) {
      each.apply(context);
    }
    return context.operationModelsBuilder().build();
  }
```

通过文档类型,获取Model的`OperationModelsProviderPlugin`插件实现类，然后调用apply方法初始化

`OperationModelsProviderPlugin`主要有两个实现类，分别是：

- **OperationModelsProviderPlugin**：处理返回类型、参数类型等
- **SwaggerOperationModelsProvider**：处理swagger注解提供的值类型,主要包括`@ApiResponse`、`@ApiOperation`

### OperationModelsProviderPlugin

先来看常规类型

```java
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OperationModelsProvider implements OperationModelsProviderPlugin {

  private static final Logger LOG = LoggerFactory.getLogger(OperationModelsProvider.class);
  private final TypeResolver typeResolver;

  @Autowired
  public OperationModelsProvider(TypeResolver typeResolver) {
    this.typeResolver = typeResolver;
  }

  @Override
  public void apply(RequestMappingContext context) {
    collectFromReturnType(context);
    collectParameters(context);
    collectGlobalModels(context);
  }
    
}
```

常规Model插件的apply方法主要有三个方法

#### collectFromReturnType

收集接口返回类型,跟踪源码;

```java
private void collectFromReturnType(RequestMappingContext context) {
    ResolvedType modelType = context.getReturnType();
    modelType = context.alternateFor(modelType);
    LOG.debug("Adding return parameter of type {}", resolvedTypeSignature(modelType).or("<null>"));
    context.operationModelsBuilder().addReturn(modelType);
  }
```

直接拿到接口的返回类型,这个返回类型在前面接口初始化时,已经初始化，getReturnType方法实际调用的是RequestHandler的方法

```java
public ResolvedType getReturnType() {
    return handler.getReturnType();
}
```

我们在前面也介绍过，RequestHander因为是接口，在springfox中的实现类是`WebMvcRequestHandler`

拿到返回类型,最终用addReturn方法添加到context对象中的全局`Set<ModelContext>`对象中

#### collectParameters

收集接口中的参数类型.

```java
private void collectParameters(RequestMappingContext context) {


    LOG.debug("Reading parameters models for handlerMethod |{}|", context.getName());

    List<ResolvedMethodParameter> parameterTypes = context.getParameters();
    for (ResolvedMethodParameter parameterType : parameterTypes) {
        if (parameterType.hasParameterAnnotation(RequestBody.class)
            || parameterType.hasParameterAnnotation(RequestPart.class)) {
          ResolvedType modelType = context.alternateFor(parameterType.getParameterType());
          LOG.debug("Adding input parameter of type {}", resolvedTypeSignature(modelType).or("<null>"));
          context.operationModelsBuilder().addInputParam(modelType);
        }
    }
    LOG.debug("Finished reading parameters models for handlerMethod |{}|", context.getName());
  }
```

接口参数类型中,springfox目前只解析两种

- 一种是实体类通过Spring的`@RequestBody`注解标注的,我们在使用Spring开发的时候如果使用该注解通常是使用的JSON作为接口的交互格式
- 通过Spring的`@ReuqestPart`注解标注的参数类型，`@RequestPart`注解是配合文件上传时附属参数类型使用的注解

#### collectGlobalModels

收集接口的全局Model

```java
private void collectGlobalModels(RequestMappingContext context) {
    for (ResolvedType each : context.getAdditionalModels()) {
        context.operationModelsBuilder().addInputParam(each);
        context.operationModelsBuilder().addReturn(each);
    }
}
```

收集外部全局添加的Model,添加进入集合中

### SwaggerOperationModelsProvider

基于Swagger相关注解赋值的类型Class解析

```java
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class SwaggerOperationModelsProvider implements OperationModelsProviderPlugin {

  private static final Logger LOG = LoggerFactory.getLogger(SwaggerOperationModelsProvider.class);
  private final TypeResolver typeResolver;

  @Autowired
  public SwaggerOperationModelsProvider(TypeResolver typeResolver) {
    this.typeResolver = typeResolver;
  }

  @Override
  public void apply(RequestMappingContext context) {
    collectFromApiOperation(context);
    collectApiResponses(context);
  }
}
```

主要有两种类型：

收集使用`@ApiOperation`注解时,使用主句的属性值

收集`@ApiResponse`响应状态码涉及到的Model

#### collectFromApiOperation

```java
private void collectFromApiOperation(RequestMappingContext context) {
    ResolvedType returnType = context.getReturnType();
    returnType = context.alternateFor(returnType);
    Optional<ResolvedType> returnParameter = context.findAnnotation(ApiOperation.class)
        .transform(resolvedTypeFromOperation(typeResolver, returnType));
    if (returnParameter.isPresent() && returnParameter.get() != returnType) {
      LOG.debug("Adding return parameter of type {}", resolvedTypeSignature(returnParameter.get()).or("<null>"));
      context.operationModelsBuilder().addReturn(returnParameter.get());
    }
  }
```

查找注解,获取ResolvedType的值

核心是拿到ApiOeration注解的response-class属性值

```java
@VisibleForTesting
static ResolvedType getResolvedType(
    ApiOperation annotation,
    TypeResolver resolver,
    ResolvedType defaultType) {

    if (null != annotation) {
        Class<?> response = annotation.response();
        String responseContainer = annotation.responseContainer();
        if (resolvedType(resolver, response, responseContainer).isPresent()) {
            return resolvedType(resolver, response, responseContainer).get();
        }
    }
    return defaultType;
}
```

#### collectApiResponses

收集在接口上标注状态码涉及的Class类，ApiResponses是集合，最终遍历得到ApiResponse

```java
private void collectApiResponses(RequestMappingContext context) {
  List<ApiResponses> allApiResponses = context.findAnnotations(ApiResponses.class);
  LOG.debug("Reading parameters models for handlerMethod |{}|", context.getName());
  Set<ResolvedType> seenTypes = newHashSet();
  for (ApiResponses apiResponses : allApiResponses) {
    List<ResolvedType> modelTypes = toResolvedTypes(context).apply(apiResponses);
    for (ResolvedType modelType : modelTypes) {
      if (!seenTypes.contains(modelType)) {
        seenTypes.add(modelType);
        context.operationModelsBuilder().addReturn(modelType);
      }
    }
  }
}
```

## ModelContext初始化

我们通过TypeResolved方法将基础的Class转换为ResolvedType

此时ModelContext提供了几个方法将ResolvedType转换为ModelContext类型

- returnValue:提供返回的class
- inputParam:参数类的方法

通过`OperationModelContextsBuilder`提供的默认参数,构造函数默认构造出ModelContext对象

```java
/**
   * Convenience method to provide an new context for an input parameter
   *
   * @param group                 - group name of the docket
   * @param type                  - type
   * @param documentationType     - for documentation type
   * @param alternateTypeProvider - alternate type provider
   * @param genericNamingStrategy - how generic types should be named
   * @param ignorableTypes        - types that can be ignored
   * @return new context
   */
public static ModelContext inputParam(
    String group,
    Type type,
    DocumentationType documentationType,
    AlternateTypeProvider alternateTypeProvider,
    GenericTypeNamingStrategy genericNamingStrategy,
    ImmutableSet<Class> ignorableTypes) {

    return new ModelContext(
        group,
        type,
        false,
        documentationType,
        alternateTypeProvider,
        genericNamingStrategy,
        ignorableTypes);
}
```

## ModelContext转化为Model

```java
Set<ModelContext> modelContexts = pluginsManager.modelContexts(context);
Map<String, Model> modelMap = newHashMap(context.getModelMap());
for (ModelContext each : modelContexts) {
    //添加基础忽略类型的ResolvedType类型
    markIgnorablesAsHasSeen(typeResolver, ignorableTypes, each);
    //通过modelProvider获取到Model类型,modelProvider是接口,有两个实现类
    //DefaultModelProvider:默认装换
    //CachingModelProvider:缓存
    Optional<Model> pModel = modelProvider.modelFor(each);
    if (pModel.isPresent()) {
        LOG.debug("Generated parameter model id: {}, name: {}, schema: {} models",
                  pModel.get().getId(),
                  pModel.get().getName());
        mergeModelMap(modelMap, pModel.get());
    } else {
        LOG.debug("Did not find any parameter models for {}", each.getType());
    }
    populateDependencies(each, modelMap);
}
```

通过modelProvider将ModelContext转换为Model类型

modelProvider是接口,有两个实现类：

- DefaultModelProvider:默认装换,每次都会将modelContext转换为model
- CachingModelProvider:声明了一个guava的缓存池,先从缓存池获取,如果没有，则调用默认的处理器,转换为model，然后放入缓存池中

此处modelProvider使用的是caching缓存配置

初次通过modelFor获取是为空的,所以接下来看`populateDependencies`方法

```java
private void populateDependencies(ModelContext modelContext, Map<String, Model> modelMap) {
    Map<String, Model> dependencies = modelProvider.dependencies(modelContext);
    for (Model each : dependencies.values()) {
        mergeModelMap(modelMap, each);
    }
}
```

caching默认是依赖default的，此处dependencies实际是调用的default的

```java
@Override
public Map<String, Model> dependencies(ModelContext modelContext) {
    Map<String, Model> models = newHashMap();
    for (ResolvedType resolvedType : dependencyProvider.dependentModels(modelContext)) {
        ModelContext parentContext = ModelContext.fromParent(modelContext, resolvedType);
        Optional<Model> model = modelFor(parentContext).or(mapModel(parentContext, resolvedType));
        if (model.isPresent()) {
            models.put(model.get().getName(), model.get());
        }
    }
    return models;
}
```

dependencyProvider和modelProvider是同一个策略，默认接口，有两个实现类,一个是cache，一个是default

所以我们默认来看default的即可

```java
@Override
public Set<ResolvedType> dependentModels(ModelContext modelContext) {
    return concat(from(resolvedDependencies(modelContext))
                  .filter(ignorableTypes(modelContext))
                  .filter(not(baseTypes(modelContext))),
                  schemaPluginsManager.dependencies(modelContext))
        .toSet();
}
```

在默认配置中,最终是通过schemaPluginsManager的方法来解决ModelContext的转换

最终是通过`SyntheticModelProviderPlugin`来转换,但是springfox中他没有实现类,所以此处的Plugin是空的，不存在

所以此处的dependentModels返回的Set集合是空的

再来看`resolvedDependencies`的方法

```java
private List<ResolvedType> resolvedDependencies(ModelContext modelContext) {
    ResolvedType resolvedType = modelContext.alternateFor(modelContext.resolvedType(typeResolver));
    if (isBaseType(ModelContext.fromParent(modelContext, resolvedType))) {
      LOG.debug("Marking base type {} as seen", resolvedType.getSignature());
      modelContext.seen(resolvedType);
      return newArrayList();
    }
    List<ResolvedType> dependencies = newArrayList(resolvedTypeParameters(modelContext, resolvedType));
    dependencies.addAll(resolvedArrayElementType(modelContext, resolvedType));
    dependencies.addAll(resolvedMapType(modelContext, resolvedType));
    dependencies.addAll(resolvedPropertiesAndFields(modelContext, resolvedType));
    dependencies.addAll(resolvedSubclasses(resolvedType));
    return dependencies;
  }
```

从代码中我们能看到：

- 首先因为ModelContext中属性是包含type的，所以默认拿到ResolvedType
- 通过拿到参数类型得到ResolvedType集合，如果有父类则递归调用.

最终通过反射的方式拿到Model类型

```java
private Optional<Model> reflectionBasedModel(ModelContext modelContext, ResolvedType propertiesHost) {
    ImmutableMap<String, ModelProperty> propertiesIndex
        = uniqueIndex(properties(modelContext, propertiesHost), byPropertyName());
    LOG.debug("Inferred {} properties. Properties found {}", propertiesIndex.size(),
              Joiner.on(", ").join(propertiesIndex.keySet()));
    Map<String, ModelProperty> properties = newTreeMap();
    properties.putAll(propertiesIndex);
    return Optional.of(modelBuilder(propertiesHost, properties, modelContext));
}

private Model modelBuilder(ResolvedType propertiesHost,
                           Map<String, ModelProperty> properties,
                           ModelContext modelContext) {
    String typeName = typeNameExtractor.typeName(ModelContext.fromParent(modelContext, propertiesHost));
    modelContext.getBuilder()
        .id(typeName)
        .type(propertiesHost)
        .name(typeName)
        .qualifiedType(simpleQualifiedTypeName(propertiesHost))
        .properties(properties)
        .description("")
        .baseModel("")
        .discriminator("")
        .subTypes(new ArrayList<ModelReference>());
    return schemaPluginsManager.model(modelContext);
}
```

构造Model的基础属性，包括类型，名称、描述、属性等信息

```java
private List<ModelProperty> propertiesFor(ResolvedType type, ModelContext givenContext, String namePrefix) {
    List<ModelProperty> properties = newArrayList();
    BeanDescription beanDescription = beanDescription(type, givenContext);
    Map<String, BeanPropertyDefinition> propertyLookup = uniqueIndex(beanDescription.findProperties(),
        BeanPropertyDefinitions.beanPropertyByInternalName());
    for (Map.Entry<String, BeanPropertyDefinition> each : propertyLookup.entrySet()) {
      LOG.debug("Reading property {}", each.getKey());
      BeanPropertyDefinition jacksonProperty = each.getValue();
      Optional<AnnotatedMember> annotatedMember
          = Optional.fromNullable(safeGetPrimaryMember(jacksonProperty));
      if (annotatedMember.isPresent()) {
        properties.addAll(candidateProperties(type, annotatedMember.get(), jacksonProperty, givenContext, namePrefix));
      }
    }
    return FluentIterable.from(properties).toSortedSet(byPropertyName()).asList();
  }
```

获取属性集合配置的代码

## 总结

通过源码的一些研究,springfox主要的处理流程：

- 收集该接口的参数类型Model
- 收集`@ApiOperation`注解的response类型Model
- 收集全局Model
- 收集接口返回类型Model
- 收集接口主键`@ApiResponses`状态码涉及的类型Model
- 在收集过程中使用了缓存策略,这样能提高解析效率