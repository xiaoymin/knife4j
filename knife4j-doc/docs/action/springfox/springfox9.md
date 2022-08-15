# springfox 源码分析(九) 文档初始化-分组


在前面我们了解了`DocumennationContext`的初始化过程,包括一系列的默认属性的赋值,接下来,开始真正的文档解析操作

我们的源码分析方式是按照springfox的文档初始化来进行归纳的,所以也是看到哪儿,就写到哪儿,当我们整个过程都研究完后,我会总结一篇文章来统一说明springfox的整个流程说明，或许以图文的方式来配合说明更能加深我们的印象.

我们在前面的初始化过程中,springfox将Spring环境中所有的接口都转换成了WebMvcRequestHandler,但是我们在外部创建Docket对象是对整个系统的接口文档来分组的,所以接下来需要对所有的接口进行分组(根据Docket对象传入的接口Selector来分).

先来看scan方法

```java
 /***
   * 最终生成Documentation文档对象
   * @param context
   * @return
   */
public Documentation scan(DocumentationContext context) {
    //得到分组接口
    ApiListingReferenceScanResult result = apiListingReferenceScanner.scan(context);
	//more...
}
```

通过`DocumentationContext`对象创建`ApiListingReferenceScanResult`对象

而`ApiListingReferenceScanResult`类只有一个属性,那就是根据controller分组后的接口方法

```java
public class ApiListingReferenceScanResult {
  //分组
  private final Map<ResourceGroup, List<RequestMappingContext>> resourceGroupRequestMappings;

  public ApiListingReferenceScanResult(Map<ResourceGroup, List<RequestMappingContext>> resourceGroupRequestMappings) {
    this.resourceGroupRequestMappings = resourceGroupRequestMappings;
  }
  public Map<ResourceGroup, List<RequestMappingContext>> getResourceGroupRequestMappings() {			        return resourceGroupRequestMappings;
  }
}
```

继续来看`apiListingReferenceScanner`对象的scan方法

```java
public ApiListingReferenceScanResult scan(DocumentationContext context) {
    LOG.info("Scanning for api listing references");

    ArrayListMultimap<ResourceGroup, RequestMappingContext> resourceGroupRequestMappings
        = ArrayListMultimap.create();
    //拿到外部的接口选择器
    //通常我们在创建Docket对象时,会赋予接口选择器,一般是以包路径来区分
    ApiSelector selector = context.getApiSelector();
    //过滤筛选
    //如果是以package路径来区分的,则会根据接口的Handler的包路径是否已packagePath开始来进行匹配
    //如果是以注解的方式,则会判断handler是否包含annotation注解
    Iterable<RequestHandler> matchingHandlers = from(context.getRequestHandlers())
        .filter(selector.getRequestHandlerSelector());
    //
    for (RequestHandler handler : matchingHandlers) {
      //接口分组
      //我们在一个Controller中会存在1个或多个接口方法
      //所以resourceGroup和RequestMapping的关系是1:N
      ResourceGroup resourceGroup = new ResourceGroup(
          handler.groupName(),
          handler.declaringClass(),
          0);
      //构建RequestMappingContext对象
      RequestMappingContext requestMappingContext
          = new RequestMappingContext(context, handler);

      resourceGroupRequestMappings.put(resourceGroup, requestMappingContext);
    }
    return new ApiListingReferenceScanResult(asMap(resourceGroupRequestMappings));
  }
```

从代码流程中,我们得知：

- 首先获取外部Docket对象的ApiSelector选择器，该选择器我们一般选择的是包路径
- 根据选择的规则进行接口过滤,此处会排除掉部分不符合规则的RequestHandler接口,通常是以package路径或者注解的方式，如果默认没有提供规则,那么springfox会根据在controller类上和方法上都没有标注`@ApiIgnore`注解的默认ApiSelctor来进行筛选
- 最后通过`ArrayListMultimap`来进行接口的归类操作
- 关于`ArrayListMultimap`的操作可参考[springfox 源码分析(十九) guava库学习](springfox19)来了解