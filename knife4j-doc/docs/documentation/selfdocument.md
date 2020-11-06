# 3.4 自定义文档

::: warning
增强功能需要通过配置yml配置文件开启增强,自2.0.7开始
```yml
knife4j:
  enable: true
```
:::

`Knife4j`为了满足文档的个性化配置,添加了自定义文档功能,自定义文档功能能丰富接口文档,在OpenAPI不足以满足接口说明的情况下,开发者通过`Knife4j`提供的自定义文档功能能够把接口文档更清晰的描述。开发者自定义`.md`文件扩展补充整个系统的文档说明

开发者可以在当前项目中添加多个个文件夹，文件夹中存放`.md`格式的markdown文件,每个`.md`文档代表一份自定义文档说明

**注意**：自定义文档说明必须以`.md`结尾的文件,其他格式文件会被忽略

例如项目结构如下：

![](/knife4j/images/1-9-3/construct.png)

每个`.md`文件中，`Knife4j`允许一级(h1)、二级(h2)、三级(h3)标题作为最终的文档标题

比如`api.md`文档：

```markdown
# 自定义文档说明

## 效果说明

`knife4j`为了满足文档的个性化配置,添加了自定义文档功能

开发者可自定义`md`文件扩展补充整个系统的文档说明

开发者可以在当前项目中添加一个文件夹，文件夹中存放`.md`格式的markdown文件,每个`.md`文档代表一份自定义文档说明

**注意**：自定义文档说明必须以`.md`结尾的文件,其他格式文件会被忽略
```

最终在`Knife4j`的界面中,`api.md`的文档标题会是`自定义文档说明`

整个文档效果如下：

![](/knife4j/images/knife4j/self-doc1.png)

如果没有按照一级(h1)、二级(h2)、三级(h3)来设置标题,默认标题会是文件名称，如图上的`api2.md`

**如何使用**

在Spring Boot环境中,首先需要在`application.yml`或者`application.properties`配置文件中配置自定义文档目录,支持多级目录

如下：

```yml
knife4j:
  enable: true
  documents:
    -
      group: 1.2.x
      name: 测试自定义标题分组
      # 某一个文件夹下所有的.md文件
      locations: classpath:markdown/*
    -
      group: 1.2.x
      name: 接口签名
      # 某一个文件夹下单个.md文件
      locations: classpath:markdown/sign.md
```

在配置`knife4j.documents`中，该属性是一个集合数组,代表开发者可以添加多个自定义文档分组,因为我们在最终呈现接口文档时，会存在逻辑分组的情况，有时候我们希望不同的逻辑分组下显示不同的逻辑分组文档，所以需要通过该节点下的group(分组名称)进行区分

相关属性说明如下：
|属性名称|是否必须|说明|
|--|---|---|
|group|true|逻辑分组名称,最终在逻辑分组时该属性需要传入|
|name|true|自定义文档的分组名称，可以理解为开发者存在多个自定义文档，最终在Ui界面呈现时的一个分组名称|
|location|true|提供自定义`.md`文件的路径或者文件|

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

![](/knife4j/images/documentation/markdownfiles.png)
:::
