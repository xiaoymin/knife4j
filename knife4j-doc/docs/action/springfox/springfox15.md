# springfox 源码分析(十五) 归档得到Documentation文档对象

通过上篇的分析,我们已经得到了ApiListing的map集合，接下来最终做文档归档,得到Documentation对象

```java
/***
   * 最终生成Documentation文档对象
   * @param context
   * @return
   */
  public Documentation scan(DocumentationContext context) {
    //得到分组接口
    ApiListingReferenceScanResult result = apiListingReferenceScanner.scan(context);
    //拿到所有接口请求
    //controller:methods-- 1:N的关系
    ApiListingScanningContext listingContext = new ApiListingScanningContext(context,
        result.getResourceGroupRequestMappings());
    //核心操作,springfox的关键操作都在下面这个scan方法中,构造接口函数
    Multimap<String, ApiListing> apiListings = apiListingScanner.scan(listingContext);
    Set<Tag> tags = toTags(apiListings);
    tags.addAll(context.getTags());
    DocumentationBuilder group = new DocumentationBuilder()
        .name(context.getGroupName())
        .apiListingsByResourceGroupName(apiListings)
        .produces(context.getProduces())
        .consumes(context.getConsumes())
        .host(context.getHost())
        .schemes(context.getProtocols())
        .basePath(context.getPathProvider().getApplicationBasePath())
        .extensions(context.getVendorExtentions())
        .tags(tags);

    Set<ApiListingReference> apiReferenceSet = newTreeSet(listingReferencePathComparator());
    apiReferenceSet.addAll(apiListingReferences(apiListings, context));

    ResourceListing resourceListing = new ResourceListingBuilder()
        .apiVersion(context.getApiInfo().getVersion())
        .apis(from(apiReferenceSet).toSortedList(context.getListingReferenceOrdering()))
        .securitySchemes(context.getSecuritySchemes())
        .info(context.getApiInfo())
        .build();
    group.resourceListing(resourceListing);
    return group.build();
  }
```

Documentation主要包含的信息

- 分组名称
- 接口列表
- produces
- consumes
- 基础路径
- host
- 分组tags
- 扩展
- schema-Models