# Authorize

The Authorize function is set by the backend configuration similar to the JWT and other permission configurations. You can configure parameters such as tokens globally.

![](/knife4j/images/auth.png)

When the background is set by the code, the UI will automatically recognize and generate the Authorize menu function for developers to fill in relevant auth parameters, etc.

Code demo(**[SwaggerConfiguration.java](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/blob/master/swagger-bootstrap-ui-demo/src/main/java/com/swagger/bootstrap/ui/demo/config/SwaggerConfiguration.java)**)：

```java
@Bean(value = "groupRestApi")
@Order(value = 1)
public Docket groupRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(groupApiInfo())
        .groupName("分组接口")
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.group"))
        .paths(PathSelectors.any())
        .build().securityContexts(Lists.newArrayList(securityContext(),securityContext1())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey(),apiKey1()));
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
```


 
 <icp/> 
 comment/> 
 
 
 
 