# springfox 源码分析(十六) 分组接口swagger-resouces


通过前面的分析,我们最终得到了springfox的Documentation文档对象,将我们的RESTful接口最终转换为了文档对象,文档对象是包含了接口列表、分组信息等属性的

在springfox中,为我们提供了springfox-swagger-ui来呈现最终的接口信息.在ui界面中有两个核心接口：

- swagger-resources:swagger分组接口，创建多少Docket,就会有多少分组信息
- /v2/api-docs:Swagger接口示例信息,通过Documentation对象最终输出为Swagger标准信息

先来看接口源码：

```java

@Controller
@ApiIgnore
@RequestMapping("/swagger-resources")
public class ApiResourceController {


  @Autowired(required = false)
  private SecurityConfiguration securityConfiguration;
  @Autowired(required = false)
  private UiConfiguration uiConfiguration;

  private final SwaggerResourcesProvider swaggerResources;

  @Autowired
  public ApiResourceController(SwaggerResourcesProvider swaggerResources) {
    this.swaggerResources = swaggerResources;
  }

  @RequestMapping(value = "/configuration/security")
  @ResponseBody
  public ResponseEntity<SecurityConfiguration> securityConfiguration() {
    return new ResponseEntity<SecurityConfiguration>(
        Optional.fromNullable(securityConfiguration).or(SecurityConfigurationBuilder.builder().build()), HttpStatus.OK);
  }

  @RequestMapping(value = "/configuration/ui")
  @ResponseBody
  public ResponseEntity<UiConfiguration> uiConfiguration() {
    return new ResponseEntity<UiConfiguration>(
        Optional.fromNullable(uiConfiguration).or(UiConfigurationBuilder.builder().build()), HttpStatus.OK);
  }

  @RequestMapping
  @ResponseBody
  public ResponseEntity<List<SwaggerResource>> swaggerResources() {
    return new ResponseEntity<List<SwaggerResource>>(swaggerResources.get(), HttpStatus.OK);
  }
}

```

通过swaggerResources.get()方法获取最终的信息

`SwaggerResourcesProvider`是接口,在springfox中只有一个实现类`InMemorySwaggerResourcesProvider`

```java

@Component
public class InMemorySwaggerResourcesProvider implements SwaggerResourcesProvider {
  private final String swagger1Url;
  private final String swagger2Url;

  @VisibleForTesting
  boolean swagger1Available;
  @VisibleForTesting
  boolean swagger2Available;

  private final DocumentationCache documentationCache;

  @Autowired
  public InMemorySwaggerResourcesProvider(
      Environment environment,
      DocumentationCache documentationCache) {
    swagger1Url = environment.getProperty("springfox.documentation.swagger.v1.path", "/api-docs");
    swagger2Url = environment.getProperty("springfox.documentation.swagger.v2.path", "/v2/api-docs");
    swagger1Available = classByName("springfox.documentation.swagger1.web.Swagger1Controller").isPresent();
    swagger2Available = classByName("springfox.documentation.swagger2.web.Swagger2Controller").isPresent();
    this.documentationCache = documentationCache;
  }

  @Override
  public List<SwaggerResource> get() {
    List<SwaggerResource> resources = new ArrayList<SwaggerResource>();

    for (Map.Entry<String, Documentation> entry : documentationCache.all().entrySet()) {
      String swaggerGroup = entry.getKey();
      if (swagger1Available) {
        SwaggerResource swaggerResource = resource(swaggerGroup, swagger1Url);
        swaggerResource.setSwaggerVersion("1.2");
        resources.add(swaggerResource);
      }

      if (swagger2Available) {
        SwaggerResource swaggerResource = resource(swaggerGroup, swagger2Url);
        swaggerResource.setSwaggerVersion("2.0");
        resources.add(swaggerResource);
      }
    }
    Collections.sort(resources);
    return resources;
  }

  private SwaggerResource resource(String swaggerGroup, String baseUrl) {
    SwaggerResource swaggerResource = new SwaggerResource();
    swaggerResource.setName(swaggerGroup);
    swaggerResource.setUrl(swaggerLocation(baseUrl, swaggerGroup));
    return swaggerResource;
  }

  private String swaggerLocation(String swaggerUrl, String swaggerGroup) {
    String base = Optional.of(swaggerUrl).get();
    if (Docket.DEFAULT_GROUP_NAME.equals(swaggerGroup)) {
      return base;
    }
    return base + "?group=" + swaggerGroup;
  }
}

```

通过遍历`DocumentationCache`中缓存的Documentation对象,得到接口文档信息的分组信息,响应`SwaggerResource`的集合信息

SwaggerResource信息主要包含的字段信息：名称，url、swagger版本

```java

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwaggerResource implements Comparable<SwaggerResource> {
  private String name;
  private String url;
  private String swaggerVersion;
  @Override
  public int compareTo(SwaggerResource other) {
    return ComparisonChain.start()
        .compare(this.swaggerVersion, other.swaggerVersion)
        .compare(this.name, other.name)
        .result();
  }

}
```

在开发`swagger-bootstrap-ui`的过程中,经常会碰到很多朋友提问,有什么方式能对文档的分组信息进行排序的吗？

我们通过上面的源码可以看到,其实SwaggerResource实现了Comparable接口,但是他的排序规则是先根据swagger的版本进行排序，然后对名称进行排序,asc顺序排序

那么我们如何实现我们自定义的排序方式呢?后面我会详细介绍.