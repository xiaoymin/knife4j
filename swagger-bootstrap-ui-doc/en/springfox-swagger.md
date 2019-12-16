For the detailed use of SpringfoxSwagger, there is no more description here, you can check it out at the following address.

**GitHub**:https://github.com/springfox/springfox

**Document**：[http://springfox.io](http://springfox.io/)

Before talking about the code of swagger-bootstrap-ui, look at the two interfaces provided by Springfox-Swagger. The swagger-bootstrap-ui package also dynamically generates documents based on these two interfaces.

Grouping interface: /swagger-resources

Detail instance interface: /v2/api-docs

#### Swagger Group

Swagger's packet interface uses different scan packages for the backend configuration. The backend interface responds to the front end according to the configured scan package basic properties, and looks at the json content of the packet interface response.：

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

In some lower versions of Springfox-Swagger, there is no location attribute, which will be available in higher versions.

| attribute      | intro                   |
| -------------- | ----------------------- |
| name           | group name              |
| url            | url                     |
| swaggerVersion | version                 |
| location       | api location，like url. |

The backend Java configuration code for the grouping is as follows：

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

The above detailed configuration can refer to the code cloud[swagger-bootstrap-ui-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo) online [SwaggerConfiguration.java](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/blob/master/swagger-bootstrap-ui-demo/src/main/java/com/swagger/bootstrap/ui/demo/config/SwaggerConfiguration.java)

Here groupName is the group name, basePackage is the interface package package path we wrote.

#### Detail instance interface

The detailed instance interface dynamically obtains all interface descriptions of the basePackage configured in the group according to the group name.

Response json is as follows：

![](images/apidef.png)

| attr                | intro                                                        |
| ------------------- | ------------------------------------------------------------ |
| info                | Define some basic information about the group, including title, profile, contacts, etc. |
| tags                | This attribute is a grouping attribute that corresponds to the @Api annotation on the back end. |
| paths               | An example array of interfaces, each instance contains basic information such as the input parameters, parameters, and response codes of the interface. |
| securityDefinitions | Permission configuration verification, the configuration configuration of the general JWT and other configurations will appear in the node properties. |
| definitions         | This attribute defines the class attribute description for all responses |