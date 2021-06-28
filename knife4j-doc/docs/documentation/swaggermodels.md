# 3.22 自定义Swagger Models名称

::: warning
增强功能需要通过配置yml配置文件开启增强,自2.0.7开始
```yml
knife4j:
  enable: true
```
:::

Knife4j在Ui菜单层提供的OpenAPI规范中的模型结构菜单名称Swagger Models,对于接口文档展示的效果来说，可能很多人并不知道其具体所代表的含义，因此自2.0.7版本开始,开发者可以自定义该名称的显示


**如何使用**

在Spring Boot环境中,首先需要在`application.yml`或者`application.properties`配置文件中配置自定义名称

如下：

```yml
knife4j:
  enable: true
  setting:
    enableSwaggerModels: true
    swaggerModelName: 我是自定义的Model名称
```

开发者配置好后,最核心的一步，也是最后最重要的一步，开发者需要在创建`Docket`逻辑分组对象时，通过`Knife4j`提供的工具对象`OpenApiExtensionResolver`将扩展属性进行赋值

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

自定义文档的扩展属性，开发者可以通过浏览器的Network功能查看OpenAPI的结构，最终Knife4j扩展增加`x-markdownFiles`属性，代表增加的扩展自定义文档属性，最终在Ui界面解析呈现，结构如下图：

![](/knife4j/images/documentation/setting.png)
:::


