# springfox 源码分析(十四) 归档得到ApiListing接口集合



在前面我们拿到了接口的Model类型集合,然后还获取到了该接口的ApiDescription描述信息

此时针对这些信息,进行接口的资源整合,最终构造ApiListing类

```java
/***
 * 通过已经筛选过滤的接口集合以及context上下文对象来得到接口列表
 * @param context
 * @return
 */
public Multimap<String, ApiListing> scan(ApiListingScanningContext context) {
  final Multimap<String, ApiListing> apiListingMap = LinkedListMultimap.create();
  int position = 0;
  // 从外部拿到已经筛选过滤后的接口信息
  //controler:methods 1:N
  Map<ResourceGroup, List<RequestMappingContext>> requestMappingsByResourceGroup
      = context.getRequestMappingsByResourceGroup();
  //收集接口详细信息
  //由于ApiListingScannerPlugin在springfox中没有实现类,所以此处返回additional集合对象是空的
  //additionalListings在此处是空集合，一个元素都没有
  Collection<ApiDescription> additionalListings = pluginsManager.additionalListings(context);
  //拿到所有的Controller分组信息

  Set<ResourceGroup> allResourceGroups = FluentIterable.from(collectResourceGroups(additionalListings))
          .append(requestMappingsByResourceGroup.keySet())
          .toSet();
  List<SecurityReference> securityReferences = newArrayList();
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
    //根据分组名称进行分组
    List<ApiDescription> additional = from(additionalListings)
        .filter(
            and(
                belongsTo(resourceGroup.getGroupName()),
                onlySelectedApis(documentationContext)))
        .toList();
    apiDescriptions.addAll(additional);

    List<ApiDescription> sortedApis = FluentIterable.from(apiDescriptions)
        .toSortedList(documentationContext.getApiDescriptionOrdering());

    String resourcePath = new ResourcePathProvider(resourceGroup)
        .resourcePath()
        .or(longestCommonPath(sortedApis))
        .orNull();

    PathProvider pathProvider = documentationContext.getPathProvider();
    String basePath = pathProvider.getApplicationBasePath();
    PathAdjuster adjuster = new PathMappingAdjuster(documentationContext);
    ApiListingBuilder apiListingBuilder = new ApiListingBuilder(context.apiDescriptionOrdering())
        .apiVersion(documentationContext.getApiInfo().getVersion())
        .basePath(adjuster.adjustedPath(basePath))
        .resourcePath(resourcePath)
        .produces(produces)
        .consumes(consumes)
        .host(host)
        .protocols(protocols)
        .securityReferences(securityReferences)
        .apis(sortedApis)
        .models(models)
        .position(position++)
        .availableTags(documentationContext.getTags());

    ApiListingContext apiListingContext = new ApiListingContext(
        context.getDocumentationType(),
        resourceGroup,
        apiListingBuilder);
    apiListingMap.put(resourceGroup.getGroupName(), pluginsManager.apiListing(apiListingContext));
  }
  return apiListingMap;
}
```

代码逻辑：

- 首先根据Controller分类获取得到所有接口上下文信息(RequestMappingContext)
- 遍历每个接口上下文信息,获取得到该接口的Models类型集合
- 遍历获取得到ApiDescription接口描述信息,接口描述信息包括：接口名称、路径、是否过时、参数、响应状态码、请求类型等等信息
- 通过以上这些信息构造`ApiListingBuilder`对象
- 调用ApiListingBuilderPlugin插件，构造赋值ApiListingBuilder对象的相关属性信息
- 添加到apiListingMap集合对象中

`ApiListingBuilderPlugin`的 实现类目前有三个：

- **ApiListingReader**:分组信息、描述信息
- **MediaTypeReader**:赋值consumes、produces信息
- **SwaggerApiListingReader**：`@Api`注解，分组tags信息赋值