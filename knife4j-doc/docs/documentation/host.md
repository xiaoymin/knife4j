# 3.17 自定义Host


在Knife4j的2.0.4版本新增该功能,新增Host的个性化配置也是方便开发或调试人员在Swagger文档部署后，针对不同的网络环境,可以通过配置该属性,方便的进行调试.

## 3.17.1 前置条件

要在Knife4j使用此属性,服务端必须开启跨域配置，如果你的工程是Spring Boot,示例代码如下：

```java
@Bean
public CorsFilter corsFilter(){
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration corsConfiguration=new CorsConfiguration();
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.addAllowedOrigin("*");
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.setMaxAge(10000L);
    source.registerCorsConfiguration("/**",corsConfiguration);
    CorsFilter corsFilter=new CorsFilter(source);
    return corsFilter;
}
```

以上代码在Knife4j中已经内置,如果开发者嫌弃每个项目都需要配置很麻烦,可以通过在`application.yml`配置文件通过配置进行开启使用,配置如下：

 当然，开发者也可以在后端控制文档语言呈现方式(自**2.0.6版本**开始)
 
 yml配置如下：
```yml
knife4j:
  enable: true
  setting:
    # 是否启用Host
    enableHost:false
    # 启用Host后地址，例如：http://192.168.0.111:8080
    enableHostText:""
```

在配置文件中进行配置后,还需要在创建Docket对象时，通过调用Knife4j提供的工具对象`OpenApiExtensionResolver`提供一个OpenAPI的扩展属性结构


示例代码如下：

::: details 点击查看代码
```java
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfiguration {

   private final OpenApiExtensionResolver openApiExtensionResolver;

    @Autowired
    public SwaggerConfiguration(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        String groupName="2.X版本";
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .host("https://www.baidu.com")
                .apiInfo(apiInfo())
                .groupName(groupName)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.new2"))
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildExtensions(groupName));
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //.title("swagger-bootstrap-ui-demo RESTful APIs")
                .description("# swagger-bootstrap-ui-demo RESTful APIs")
                .termsOfServiceUrl("http://www.xx.com/")
                .contact("xx@qq.com")
                .version("1.0")
                .build();
    }
}
```
:::
通过上面示例代码，主要步骤如下：

1、通过`@Autowired`注解引入`Knife4j`向Spring容器注入的Bean对象`OpenApiExtensionResolver`

2、最终在`Dcoket`对象构建后，通过调用`Docket`对象的`extensions`方法进行插件赋值

3、插件赋值需要调用`OpenApiExtensionResolver`提供的`buildExtensions`方法，该方法需要一个逻辑分组名称，就是开发者在`yml`配置文件中配置的`group`名称

::: tip
为什么需要这么做?这样做的目的一方面是充分利用Spring Boot提供的配置方式，方便开发者自定义配置属性，另一方面，通过扩展OpenAPI的规范属性，也更加符合OpenAPI对于扩展属性的要求

OpenAPI规范明确规定,对于扩展属性,开发者应当在响应的某个节点中，增加`x-`开头的属性方式,以扩展自定义的属性配置

自定义文档的扩展属性，开发者可以通过浏览器的Network功能查看OpenAPI的结构，最终Knife4j扩展增加`x-setting`属性，代表增加的扩展自定义文档属性，最终在Ui界面解析呈现，结构如下图：

![](/knife4j/images/documentation/setting.png)
:::

一般Spring Boot的单体架构可以使用此方式进行开启Cors的跨域配置,但是如果你的服务是走网关的形式,那么必须在网关层进行开启，网关层的形式会有所不同

以`Spring Cloud Gateway`为例,该网关底层是基于Netty,并非是Servlet架构,所以需要使用网关自带的`WebFilter`进行开启，否则在前端到请求服务时,数据可以流入,但是在网关层时会无法响应数据,出现`Access-Control-Allow-Origin`等错误

## 3.17.2 配置形式

Host的配置可以有多种形式，举例如下：

1、ip+port的形式，例如`192.169.0.111:8080`

2、HTTP/HTTPS+ip+port的形式，例如：`http://192.168.0.111:8080`或者`https://192.168.0.111:8080`

3、域名的方式，例如：`knife4j.xiaominfo.com`

4、HTTP/HTTPS+域名,例如：`http://knife4j.xiaominfo.com`或者`https://knife4j.xiaominfo.com`

5、除了配置域名或者ip地址外,在Host后还可以跟随`basePath`,例如：`http://192.168.0.111:8080/v1`等

如果当前配置的Host不包含`Protocol`,默认为`HTTP`

## 3.17.3 原理

使用该`Host`后,Knife4j会在发送接口调试时校验是否启用,其原理是在通过调用`axios`组件时,配置其`baseURL`属性

伪代码如下：

```javascript
var baseUrl='';//默认是空
//是否启用Host
if(this.enableHost){
    baseUrl=this.enableHostText;
}
var requestConfig={
    baseURL:baseUrl,//调用目标Host服务的接口
    url: url,
    method: methodType,
    headers: headers,
    params: formParams,
    data: data,
    //Cookie标志
    withCredentials:this.debugSendHasCookie(headers),
    timeout: 0
}
```


 
 
 