# springfox 源码分析(十七) Swagger2接口文档示例接口api-docs


前面已经获取得到了swagger的分组接口信息了,接下来就是根据分组名称获取每个分组的Swagger资源详细信息,在springfox中提供了/v2/api-docs接口来进行获取

来看接口的源码

```java
@Controller
@ApiIgnore
public class Swagger2Controller {

  public static final String DEFAULT_URL = "/v2/api-docs";
  private static final Logger LOGGER = LoggerFactory.getLogger(Swagger2Controller.class);
  private static final String HAL_MEDIA_TYPE = "application/hal+json";

  private final String hostNameOverride;
  private final DocumentationCache documentationCache;
  private final ServiceModelToSwagger2Mapper mapper;
  private final JsonSerializer jsonSerializer;

  @Autowired
  public Swagger2Controller(
      Environment environment,
      DocumentationCache documentationCache,
      ServiceModelToSwagger2Mapper mapper,
      JsonSerializer jsonSerializer) {

    this.hostNameOverride =
        environment.getProperty(
            "springfox.documentation.swagger.v2.host",
            "DEFAULT");
    this.documentationCache = documentationCache;
    this.mapper = mapper;
    this.jsonSerializer = jsonSerializer;
  }

  @RequestMapping(
      value = DEFAULT_URL,
      method = RequestMethod.GET,
      produces = { APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE })
  @PropertySourcedMapping(
      value = "${springfox.documentation.swagger.v2.path}",
      propertyKey = "springfox.documentation.swagger.v2.path")
  @ResponseBody
  public ResponseEntity<Json> getDocumentation(
      @RequestParam(value = "group", required = false) String swaggerGroup,
      HttpServletRequest servletRequest) {

    String groupName = Optional.fromNullable(swaggerGroup).or(Docket.DEFAULT_GROUP_NAME);
    Documentation documentation = documentationCache.documentationByGroup(groupName);
    if (documentation == null) {
      LOGGER.warn("Unable to find specification for group {}", groupName);
      return new ResponseEntity<Json>(HttpStatus.NOT_FOUND);
    }
    Swagger swagger = mapper.mapDocumentation(documentation);
    UriComponents uriComponents = componentsFrom(servletRequest, swagger.getBasePath());
    swagger.basePath(Strings.isNullOrEmpty(uriComponents.getPath()) ? "/" : uriComponents.getPath());
    if (isNullOrEmpty(swagger.getHost())) {
      swagger.host(hostName(uriComponents));
    }
    return new ResponseEntity<Json>(jsonSerializer.toJson(swagger), HttpStatus.OK);
  }

  private String hostName(UriComponents uriComponents) {
    if ("DEFAULT".equals(hostNameOverride)) {
      String host = uriComponents.getHost();
      int port = uriComponents.getPort();
      if (port > -1) {
        return String.format("%s:%d", host, port);
      }
      return host;
    }
    return hostNameOverride;
  }
}

```

该接口主要逻辑：

- 传入groupName分组名称参数,从文档缓存对象中获取Documentation文档对象
- 通过mapper提供的方法,将Documentation对象转换为标准的Swagger对象
- JSON响应输出

Swagger的标准对象主要包含信息我们在前面也介绍过

```java
public class Swagger {
    protected String swagger = "2.0";
    protected Info info;
    protected String host;
    protected String basePath;
    protected List<Tag> tags;
    protected List<Scheme> schemes;
    protected List<String> consumes;
    protected List<String> produces;
    protected List<SecurityRequirement> security;
    protected Map<String, Path> paths;
    protected Map<String, SecuritySchemeDefinition> securityDefinitions;
    protected Map<String, Model> definitions;
    protected Map<String, Parameter> parameters;
    protected Map<String, Response> responses;
    protected ExternalDocs externalDocs;
    protected Map<String, Object> vendorExtensions;
}
```

最终在ui端拿到Swagger的属性信息进行接口的信息渲染,开发人员即可进行接口的查看和调试.