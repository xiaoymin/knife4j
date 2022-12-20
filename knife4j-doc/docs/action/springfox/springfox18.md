# springfox 源码分析(十八) 自定义扩展实现分组的排序


既然我们对springfox提供的接口已经有了一个初步的了解,那么针对我们在分组接口文章中提的需求,如果自定义扩展实现分组的排序如何做呢？

在swagger-bootstrap-ui以前的版本中,已经存在了增强功能,增强功能主要的方式是重写了springfox的接口,然后在我们自定义的ui中渲染即可.

因为`SwaggerResource.java`中没有提供排序的字段属性,所以我们可以扩展该类,提供一个排序字段

```java
/***
 *
 * @since:swagger-bootstrap-ui 1.9.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/06/02 16:24
 */
public class SwaggerResourceExt extends SwaggerResource {

    private Integer order;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
```

很简单,提供一个order属性,该类继承自springfox的`SwaggerResource`

扩展了基础属性类,那么我们提供的方式也需要进行扩展

```java
@Component
@Qualifier("swaggerResourcesExtProvider")
public class SwaggerResourcesExtProvider  {
    private final String swagger1Url;
    private final String swagger2Url;

    @VisibleForTesting
    boolean swagger1Available;
    @VisibleForTesting
    boolean swagger2Available;

    private final DocumentationCache documentationCache;

    @Autowired
    public SwaggerResourcesExtProvider(Environment environment, DocumentationCache documentationCache) {
        swagger1Url = environment.getProperty("springfox.documentation.swagger.v1.path", "/api-docs-ext");
        swagger2Url = environment.getProperty("springfox.documentation.swagger.v2.path", "/v2/api-docs-ext");
        swagger1Available = classByName("springfox.documentation.swagger1.web.Swagger1Controller").isPresent();
        swagger2Available = classByName("springfox.documentation.swagger2.web.Swagger2Controller").isPresent();
        this.documentationCache = documentationCache;
    }

    public List<SwaggerResourceExt> get() {
        List<SwaggerResourceExt> resources = new ArrayList<SwaggerResourceExt>();

        for (Map.Entry<String, Documentation> entry : documentationCache.all().entrySet()) {
            String swaggerGroup = entry.getKey();
            Documentation documentation=entry.getValue();
            List<VendorExtension> vendorExtensions=documentation.getVendorExtensions();
            if (swagger1Available) {
                SwaggerResourceExt swaggerResource = resource(swaggerGroup, swagger1Url,vendorExtensions);
                swaggerResource.setSwaggerVersion("1.2");
            }

            if (swagger2Available) {
                SwaggerResourceExt swaggerResource = resource(swaggerGroup, swagger2Url,vendorExtensions);
                swaggerResource.setSwaggerVersion("2.0");
                resources.add(swaggerResource);
            }
        }
        //根据自定义扩展属性order进行排序
        Collections.sort(resources, new Comparator<SwaggerResourceExt>() {
            @Override
            public int compare(SwaggerResourceExt o1, SwaggerResourceExt o2) {
                return o1.getOrder().compareTo(o2.getOrder());
            }
        });
        return resources;
    }

    private SwaggerResourceExt resource(String swaggerGroup, String baseUrl,List<VendorExtension> vendorExtensions) {
        SwaggerResourceExt swaggerResource = new SwaggerResourceExt();
        swaggerResource.setName(swaggerGroup);
        swaggerResource.setUrl(swaggerLocation(baseUrl, swaggerGroup));
        swaggerResource.setOrder(0);
        //判断是否不为空
        if (vendorExtensions!=null&&!vendorExtensions.isEmpty()){
            Optional<VendorExtension> ov= FluentIterable.from(vendorExtensions).filter(new Predicate<VendorExtension>() {
                @Override
                public boolean apply(VendorExtension input) {
                    return input.getClass().isAssignableFrom(OrderExtensions.class);
                }
            }).first();
            if (ov.isPresent()){
                OrderExtensions orderExtensions=(OrderExtensions) ov.get();
                swaggerResource.setOrder(orderExtensions.getValue());
            }
        }
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

针对原springfox的方式进行扩展,主要逻辑

- 首先获取Documentation的Map集合对象,进行遍历
- 我们的接口参数是需要从外部由开发者自定义的传入的,那么此时我们可以利用Docket对象提供的扩展属性集合来操作,`swagger-bootstrap-ui`提供了`OrderExtensions`扩展,开发者创建Docket对象时进行参数传入即可
- 筛选Documentation的扩展属性集合,找到符合规范的扩展,如果未找到则默认排序值为0

重写了获取`SwaggerResource`集合的工具类,接下来重写接口层

```java
@ApiIgnore
@Controller
@RequestMapping("/swagger-resources-ext")
public class SwaggerBootstrapUiResourceExtController {
    private final SwaggerResourcesExtProvider swaggerResourcesExtProvider;

    @Autowired
    public SwaggerBootstrapUiResourceExtController(@Qualifier("swaggerResourcesExtProvider") SwaggerResourcesExtProvider swaggerResources) {
        this.swaggerResourcesExtProvider = swaggerResources;
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<List<SwaggerResourceExt>> swaggerResources() {
        return new ResponseEntity<List<SwaggerResourceExt>>(swaggerResourcesExtProvider.get(), HttpStatus.OK);
    }
}
```

此时,提供一个类似springfox的分组接口,通过工具类提供,获取拿到分组信息

有了以上的扩展实现,我们SwaggerConfiguration配置文件创建Docket对象时需要稍微做一个改动

```java
@Bean(value = "defaultApi")
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
                .build()
            //添加扩展
            .extensions(Lists.newArrayList(new OrderExtensions(1)))
            .globalOperationParameters(parameters)
                .securityContexts(Lists.newArrayList(securityContext())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
        return docket;
    }
```

此时,我们使用extensions方法添加扩展,赋值OrderExtensions的排序order值

此时我们访问接口`/swagger-resoueces-ext`返回：

```java
[
    {
        "order": 1,
        "name": "默认接口",
        "url": "/v2/api-docs-ext?group=默认接口",
        "swaggerVersion": "2.0",
        "location": "/v2/api-docs-ext?group=默认接口"
    },
    {
        "order": 2,
        "name": "分组接口",
        "url": "/v2/api-docs-ext?group=分组接口",
        "swaggerVersion": "2.0",
        "location": "/v2/api-docs-ext?group=分组接口"
    }
]
```

此时,我们在Ui端就可以自定义接口分组的排序了

以上功能在`swagger-bootstrap-ui`1.9.4版本已经实现,开发者如果有排序的需求,可以使用此方法.

**注意：**在使用此功能时,需要在Swagger的配置文件类上加上`@EnableSwaggerBootstrapUI`注解