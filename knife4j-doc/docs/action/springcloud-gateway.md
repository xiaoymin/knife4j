# 2.1 Spring Cloud Gateway集成Knife4j


本篇博客主要讲解通过knife4j项目如何集成Spring Cloud Gateway网关,通过网关聚合所有的Swagger微服务文档

源码地址请参考：[knife4j-spring-cloud-gateway](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/knife4j-spring-cloud-gateway)

整体项目结构如下：

```text
|-knife4j-spring-cloud-gateway
|-----service-doc	//文档聚合中心,是所有微服务文档的出口
|-----service-order //订单服务,包含所有与订单业务模块相关的接口
|-----service-server //eureka 注册中心
|-----service-user //用户服务,包含所有的用户接口
```

## 2.1.1 eureka注册中心

注册中心几乎没有代码,只是在pom.xml文件中引入了eureka服务的jar包

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

项目的`application.yml`配置文件如下：

```yml
server:
  port: 10000
eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
spring:
  application:
    name: knife4j-gateway-server
```

定义注册中心访问地址，端口号等属性

最后通过注解`@EnableEurekaServer`来启用注册中心

```java
@EnableEurekaServer
@SpringBootApplication
public class ServiceServerApplication {
    
    
}
```

## 2.1.2 服务接口(订单order & 用户User)

由于服务接口订单和用户两个模块其实属性是差不多,只是接口不一样,因此就随便挑一个服务的配置来说吧

**service-user**:用户服务的接口

每个微服务只需要引入和swagger相关的后端jar包即可,不需要引入swagger的前端Ui包,knife4j为我们提供了微服务项的starter,供开发者使用

当然,作为子服务,还需要引入eureka-client的jar包,所以，pom.xml文件相关配置如下：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-micro-spring-boot-starter</artifactId>
</dependency>
```

项目的jar包引入完成后,接下来是配置swagger的相关配置，`SwaggerConfiguration.java`配置如下：

```java

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUi
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

                .build();
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


}
```

配置扫描目录包

通过`@EnableSwagger2`和`@EnableSwaggerBootstrapUi`来开启swagger和增强特性

配置项目的`application.yml`文件,如下：

```java
server:
  port: 10001
spring:
  application:
    name: service-user
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:10000/eureka/
```

指定注册中心地址即可

最后,启用eureka客户端

```java
@EnableEurekaClient
@SpringBootApplication
public class ServiceUserApplication {
}
```

当然,在服务的模块中还有和自己服务相关的业务接口(Controller代码),在这里就不列举了

订单模块(service-order)的代码配置和用户是类似的

## 2.1.3 文档聚合

有了eureka注册中心,服务模块的接口也已完成,最后一步是把我们所有的微服务都聚合到一个文档,统一输出到前端,供开发者调用了

### 2.1.3.1 pom引入相关jar包

`service-doc`也是一个eureka客户端,首先引入相关的jar包,pom.xml配置文件如下：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
</dependency>
```

- spring-cloud-starter-netflix-eureka-client:eureka客户端
- spring-cloud-starter-gateway：gateway网关
- knife4j-spring-boot-starter：knife4j提供的前端ui和后端代码

另外,其实在文档这里,如果没有后端代码编写的话,仅仅引入一个swagger的前端ui模块也是可以的

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-ui</artifactId>
</dependency>
```

### 2.1.3.2 application文件配置

配置我们的网关属性,路由规则等,`application.yml`配置文件如下：

```yml
server:
  port: 10003
spring:
  application:
    name: service-doc
  cloud:
    gateway:
      discovery:
        locator:
          #          enabled: true
          lowerCaseServiceId: true
      routes:
        - id: service-user
          uri: lb://service-user
          predicates:
            - Path=/user/**
          #            - Header=Cookie,Set-Cookie
          filters:
            - SwaggerHeaderFilter
            - StripPrefix=1
        - id:  service-order
          uri: lb://service-order
          predicates:
            - Path=/order/**
          filters:
            - SwaggerHeaderFilter
            - StripPrefix=1


eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:10000/eureka/

logging:
  level:
    org.springframework:cloud.gateway: debug
```

### 2.1.3.3 文档聚合业务编码

在我们使用Spring Boot等单体架构集成swagger项目时,是通过对包路径进行业务分组,然后在前端进行不同模块的展示,而在微服务架构下,我们的一个服务就类似于原来我们写的一个业务组

springfox-swagger提供的分组接口是`swagger-resource`,返回的是分组接口名称、地址等信息

在Spring Cloud微服务架构下,我们需要重写该接口,主要是通过网关的注册中心动态发现所有的微服务文档,代码如下：

```java
@Slf4j
@Component
@Primary
@AllArgsConstructor
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    private final RouteLocator routeLocator;
    private final GatewayProperties gatewayProperties;


    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId())).forEach(route -> {
            route.getPredicates().stream()
                    .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                    .forEach(predicateDefinition -> resources.add(swaggerResource(route.getId(),
                            predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                    .replace("**", "v2/api-docs"))));
        });

        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        log.info("name:{},location:{}",name,location);
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
```

接口：

```java
@RestController
public class SwaggerHandler {

    @Autowired(required = false)
    private SecurityConfiguration securityConfiguration;

    @Autowired(required = false)
    private UiConfiguration uiConfiguration;

    private final SwaggerResourcesProvider swaggerResources;

    @Autowired
    public SwaggerHandler(SwaggerResourcesProvider swaggerResources) {
        this.swaggerResources = swaggerResources;
    }


    @GetMapping("/swagger-resources/configuration/security")
    public Mono<ResponseEntity<SecurityConfiguration>> securityConfiguration() {
        return Mono.just(new ResponseEntity<>(
                Optional.ofNullable(securityConfiguration).orElse(SecurityConfigurationBuilder.builder().build()), HttpStatus.OK));
    }

    @GetMapping("/swagger-resources/configuration/ui")
    public Mono<ResponseEntity<UiConfiguration>> uiConfiguration() {
        return Mono.just(new ResponseEntity<>(
                Optional.ofNullable(uiConfiguration).orElse(UiConfigurationBuilder.builder().build()), HttpStatus.OK));
    }

    @GetMapping("/swagger-resources")
    public Mono<ResponseEntity> swaggerResources() {
        return Mono.just((new ResponseEntity<>(swaggerResources.get(), HttpStatus.OK)));
    }
}
```

### 2.1.3.4 启动配置

最后,项目启动类添加相关注解，代码如下：

```java
@EnableDiscoveryClient
@EnableEurekaClient
@SpringBootApplication
public class ServiceDocApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDocApplication.class, args);
	}

}
```

## 2.1.4 文档展示

最后分别依次启动项目：

- service-server
- service-user
- service-order
- service-doc

打开文档地址：http://localhost:10003/doc.html

查看文档效果如下：

![](/knife4j/images/k-g-1.png)

![](/knife4j/images/k-g-2.png)

## 2.1.5 注意点

在集成Spring Cloud Gateway网关的时候,会出现没有basePath的情况(即定义的例如/user、/order等微服务的前缀),这个情况在使用zuul网关的时候不会出现此问题,因此,在Gateway网关需要添加一个Filter实体Bean,代码如下：

```java
@Component
public class SwaggerHeaderFilter extends AbstractGatewayFilterFactory {
    private static final String HEADER_NAME = "X-Forwarded-Prefix";

    private static final String URI = "/v2/api-docs";

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            if (!StringUtils.endsWithIgnoreCase(path,URI )) {
                return chain.filter(exchange);
            }
            String basePath = path.substring(0, path.lastIndexOf(URI));
            ServerHttpRequest newRequest = request.mutate().header(HEADER_NAME, basePath).build();
            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newExchange);
        };
    }
}
```

然后在配置文件指定这个filter

```yml
spring:
  application:
    name: service-doc
  cloud:
    gateway:
      discovery:
        locator:
          #          enabled: true
          lowerCaseServiceId: true
      routes:
        - id: service-user
          uri: lb://service-user
          predicates:
            - Path=/user/**
          #            - Header=Cookie,Set-Cookie
          filters:
            - SwaggerHeaderFilter
            - StripPrefix=1
        - id:  service-order
          uri: lb://service-order
          predicates:
            - Path=/order/**
          filters:
            - SwaggerHeaderFilter  //指定filter
            - StripPrefix=1
```

**特别注意：如果是高版本的Spring Cloud Gateway，那么yml配置文件中的`SwaggerHeaderFilter`配置应该去掉**
 