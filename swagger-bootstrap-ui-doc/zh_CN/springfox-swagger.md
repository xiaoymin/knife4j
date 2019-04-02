关于SpringfoxSwagger详细使用,这里不过多叙述,可自行通过下面地址查阅

**GitHub**:https://github.com/springfox/springfox

**文档**：[http://springfox.io](http://springfox.io/)

在说swagger-bootstrap-ui的代码之前,先看Springfox-Swagger提供的2个接口，swagger-bootstrap-ui包也是根据这2个接口来动态生成文档的

分组接口：`/swagger-resources`

详情实例接口：`/v2/api-docs`

#### Swagger分组

Swagger的分组接口是用过后端配置不同的扫描包，将后端的接口，按配置的扫描包基础属性响应给前端，看看分组接口响应的json内容：

```json
[
    {
        "name": "分组接口",
        "url": "/v2/api-docs?group=分组接口",
        "swaggerVersion": "2.0",
        "location": "/v2/api-docs?group=分组接口"
    },
    {
        "name": "默认接口",
        "url": "/v2/api-docs?group=默认接口",
        "swaggerVersion": "2.0",
        "location": "/v2/api-docs?group=默认接口"
    }
]
```

在Springfox-Swagger有些较低的版本中，并没有location属性，高版本会有该属性

| 属性           | 说明                    |
| -------------- | ----------------------- |
| name           | 分组名称                |
| url            | 接口url                 |
| swaggerVersion | 版本号                  |
| location       | 接口location，同url属性 |

分组的后端Java配置代码如下：

```java
@Bean(value = "defaultApi")
public Docket defaultApi() {
    ParameterBuilder parameterBuilder=new ParameterBuilder();
    List<Parameter> parameters= Lists.newArrayList();
    parameterBuilder.name("token").description("token令牌").modelRef(new ModelRef("String"))
        .parameterType("header").defaultValue("abc")
        .required(true).build();
    parameters.add(parameterBuilder.build());

    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .groupName("默认接口")
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.controller"))
        .paths(PathSelectors.any())
        .build().globalOperationParameters(parameters)
        .securityContexts(Lists.newArrayList(securityContext(),securityContext1())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey(),apiKey1()));
}
@Bean(value = "groupRestApi")
public Docket groupRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(groupApiInfo())
        .groupName("分组接口")
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.group"))
        .paths(PathSelectors.any())
        .build().securityContexts(Lists.newArrayList(securityContext(),securityContext1())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey(),apiKey1()));
}
```

以上详细配置可参考码云[swagger-bootstrap-ui-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo)在线[SwaggerConfiguration.java](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/blob/master/swagger-bootstrap-ui-demo/src/main/java/com/swagger/bootstrap/ui/demo/config/SwaggerConfiguration.java)

此处groupName即分组名称，basePackage即我们写的接口基础package包路径.

#### 详情实例接口

详情实例接口是根据分组名称,动态获取该组下配置的basePackage所有的接口描述信息

响应json如下：

![](images/apidef.png)

| 属性                | 说明                                                         |
| ------------------- | ------------------------------------------------------------ |
| info                | 定义的该分组一些基础信息,包括标题、简介、联系人等            |
| tags                | 该属性是分组属性，与后端的@Api注解对应                       |
| paths               | 接口示例数组，每个实例包含了接口的入参、出参、响应码等基础信息 |
| securityDefinitions | 权限配置验证，一般JWT等配置的权限配置会在该节点属性出现      |
| definitions         | 该属性定义了所有响应的类属性说明                             |