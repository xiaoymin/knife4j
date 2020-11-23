# 基于Spring Cloud Zuul方式

在基于nginx配置的环节,其实我们已经可以利用nginx的配置,帮助我们聚合文档服务了,而通过代码的方式该如何实现?

在Spring Cloud微服务架构中,各个子服务都是分散的,每个服务集成了Swagger文档,但是接口对接时需要单独分别访问,很麻烦,效率低下,

而Zuul可以帮助我们解决此难题,将多个微服务的Swagger接口聚合到一个文档中,这样整个微服务架构下只会存在一个文档出口,统一文档口径

本文档只涉及如何整合Swagger及Zuul,其他相关知识点请自行搜索解决.

## 项目结构

整个项目结构如下：

```text
swagger-bootstrap-ui-zuul
├── service-server -- eureka服务中心
├── service-order -- 微服务之一订单服务模块
├── service-user -- 微服务之一用户服务模块
├── service-doc -- 文档中心,整合微服务Swagger文档

```

eureka注册服务中心以及微服务模块Swagger的配置集成使用这里不过多骜述,和常规无异.

我们在eureka服务中心可以看到整个微服务模块,如下图：

![](/knife4j/images/front/eureka.png)


## 微服务模块

订单、用户两个微服务模块配置没有什么区别,都是将自己的服务注册到eureka中,并且每个微服务都集成Swagger的配置

```java
@EnableEurekaClient
@SpringBootApplication
public class ServiceUserApplication {

	static Logger logger= LoggerFactory.getLogger(ServiceUserApplication.class);
	//...

}
```

此处需要注意的是Swagger的配置中,不需要设置`groupName`属性

Swagger配置如下：

```java

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    @Bean(value = "userApi")
    @Order(value = 1)
    public Docket groupRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(groupApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xiaominfo.swagger.service.user.controller"))
                .paths(PathSelectors.any())

                .build().securityContexts(Lists.newArrayList(securityContext(),securityContext1())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey(),apiKey1()));
    }

    private ApiInfo groupApiInfo(){
        return new ApiInfoBuilder()
                .title("swagger-bootstrap-ui很棒~~~！！！")
                .description("<div style='font-size:14px;color:red;'>swagger-bootstrap-ui-demo RESTful APIs</div>")
                .termsOfServiceUrl("http://www.group.com/")
                .contact("group@qq.com")
                .version("1.0")
                .build();
    }



    private ApiKey apiKey() {
        return new ApiKey("BearerToken", "Authorization", "header");
    }
    private ApiKey apiKey1() {
        return new ApiKey("BearerToken1", "Authorization-x", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }
    private SecurityContext securityContext1() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth1())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
    }
    List<SecurityReference> defaultAuth1() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("BearerToken1", authorizationScopes));
    }

}
```

## 文档整合

`service-doc`模块是最终整合user、order两个微服务文档的统一文档出口,而本身也注册到eureka服务中心中.

```java
@EnableDiscoveryClient
@EnableEurekaClient
@EnableZuulProxy
@SpringBootApplication
public class ServiceDocApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDocApplication.class, args);
	}

}
```

最后重写SwaggerResource,代码如下：
```java
@Component
@Primary
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    Logger logger= LoggerFactory.getLogger(SwaggerResourceConfig.class);


    @Autowired
    RouteLocator routeLocator;

    @Override
    public List<SwaggerResource> get() {
        //获取所有router
        List<SwaggerResource> resources = new ArrayList<>();
        List<Route> routes = routeLocator.getRoutes();
        logger.info("Route Size:{}",routes.size());
        for (Route route:routes) {
            resources.add(swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs")));
        }
        return resources;
    }
    private SwaggerResource swaggerResource(String name, String location) {
        logger.info("name:{},location:{}",name,location);
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
```

最终效果如下：

![](/knife4j/images/front/sbu.png)


## 示例源码

以上源码可参考[swagger-bootstrap-ui-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo)中的子项目`swagger-bootstrap-ui-zuul`


 
 
 