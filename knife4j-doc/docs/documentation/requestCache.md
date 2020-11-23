# 3.8 请求参数缓存


在默认情况下,在接口调试的情况下,`Knifetj`对于接口的请求参数都会缓存起来，该配置可以在前端界面中的个性化设置中看到,如下图：

> 如果你不喜欢在调试的时候对参数进行缓存,可以在个性化设置中将该选项取消

![](/knife4j/images/knife4j/plus/cacheparameter.png)


缓存的情况只会在后端没有给属性`example`的情况下产生,如果后端在写Swagger的注解的时候,给每个字段赋予了example的值,那么,`Knife4j`不会使用调试时缓存的值,而是会一直使用后端的example值

例如后端Java实体类如下情况：

```java
public class SwaggerRequestBody{
    
    @ApiModelProperty(value="姓名",example="张飞")
    private String name;
    
    //more...
}
```


对于上面的代码示例,`Knife4j`在每一次打开该接口的请求参数值,其默认值都是`张飞`



以下情况会在第二次请求的情况下启用上一次调试时填的缓存值

```java
public class SwaggerRequestBody{
    
    @ApiModelProperty(value="姓名")
    private String name;
    
    //more...
}
```

这种情况在ui界面不会出现默认值,所以当开发者在调试的时候,填了name属性的值后,`Knife4j`就会将该值缓存起来,方便下次调试调用.
 
 
 当然，开发者也可以在后端控制文档的接口调试功能，针对请求后的参数是否需要缓存(自2.0.6版本开始)
 
 yml配置如下：
```yml
knife4j:
  enable: true
  setting:
    # 对于调试中的请求参数是否缓存进行开启配置，该参数默认为true
    enableRequestCache: true
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

![](/knife4j/images/documentation/settings.png)
:::

 
 