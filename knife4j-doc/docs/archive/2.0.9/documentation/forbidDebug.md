# 3.26 禁用调试

::: warning
增强功能需要通过配置yml配置文件开启增强,自2.0.8开始
```yml
knife4j:
  enable: true
```
:::

在以前的版本中，开发者如果要禁用调试功能，是通过在服务端创建UiConfiguration的实体Bean对象，配置supportMethod来达到禁用部分接口的调试，自`2.0.8`版本后，该属性被废弃

开发者如果想要禁用调试功能，需要通过增强属性进行配置，在yml配置如下：
```yml
knife4j:
  enable: true
  setting:
    enableDebug: false
```

属性说明：

- `enableDebug`:该属性是一个`Boolean`值，代表是否启用调试功能,默认值为`true`(代表开启调试)，如果要禁用调试，该值设为`false`

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
                .extensions(openApiExtensionResolver.buildSettingExtensions());
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

3、插件赋值需要调用`OpenApiExtensionResolver`提供的`buildSettingExtensions`方法，获取`x-settings`的增强属性


**最终界面效果如下：**

![](/knife4j/images/enhance/enableDebug.png)


::: tip
为什么需要这么做?这样做的目的一方面是充分利用Spring Boot提供的配置方式，方便开发者自定义配置属性，另一方面，通过扩展OpenAPI的规范属性，也更加符合OpenAPI对于扩展属性的要求

OpenAPI规范明确规定,对于扩展属性,开发者应当在响应的某个节点中，增加`x-`开头的属性方式,以扩展自定义的属性配置

自定义文档的扩展属性，开发者可以通过浏览器的Network功能查看OpenAPI的结构，最终Knife4j扩展增加`x-openapi`属性，代表增加的扩展自定义属性，最终在Ui界面解析呈现，结构如下图：

![](/knife4j/images/documentation/setting.png)
:::


