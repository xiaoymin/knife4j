# 3.23 自定义主页内容

:::caution 温馨提醒
增强功能需要通过配置yml配置文件开启增强,自2.0.8开始
```yml
knife4j:
  enable: true
```
:::

Knife4j自`2.0.8`版本开始,开发者可以提供一个Markdown文件来自定义显示Home主页的显示内容，通过配置yml来进行开启，配置文件如下
```yml
knife4j:
  enable: true
  setting:
    enable-home-custom: true
    # 自4.1.0版本开始，该属性过时，请使用下面home-custom-path属性
    home-custom-location: classpath:markdown/home.md
    # 自4.1.0版本开始，替代home-custom-location属性，开发者请使用该配置
    home-custom-path: classpath:markdown/home.md
```

属性说明：
- `enable-home-custom`:该属性为Boolean值,默认`false`，如果开发者要自定义主页内容,该选项设置为`true`
- `home-custom-location`:提供一个主页的Markdown文件位置
- `home-custom-path`:提供一个主页的Markdown文件位置


:::danger 重要提醒
自Knife4j 4.0版本开始，下面的配置在使用`knife4j-openapi2-spring-boot-starter`组件时才需要，而使用`knife4j-openapi3-spring-boot-starter`或者`knife4j-openapi3-jakarta-spring-boot-starter`组件则**不需要！！！**，开发者需要注意。
:::

开发者配置好后,最核心的一步，也是最后最重要的一步，开发者需要在创建`Docket`逻辑分组对象时，通过`Knife4j`提供的工具对象`OpenApiExtensionResolver`将扩展属性进行赋值

示例代码如下：

:::tip 点击查看代码
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

![](/knife4j/images/enhance/customeHome.png)


:::tip
为什么需要这么做?这样做的目的一方面是充分利用Spring Boot提供的配置方式，方便开发者自定义配置属性，另一方面，通过扩展OpenAPI的规范属性，也更加符合OpenAPI对于扩展属性的要求

OpenAPI规范明确规定,对于扩展属性,开发者应当在响应的某个节点中，增加`x-`开头的属性方式,以扩展自定义的属性配置

自定义文档的扩展属性，开发者可以通过浏览器的Network功能查看OpenAPI的结构，最终Knife4j扩展增加`x-openapi`属性，代表增加的扩展自定义属性，最终在Ui界面解析呈现，结构如下图：

![](/knife4j/images/documentation/setting.png)
:::


