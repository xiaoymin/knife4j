# 3.1 简化模式(implicit)

以Spring Security为例，为了达到快速演示效果，授权服务器和资源服务器都在同一个工程中

完整代码示例请参考[knife4j-spring-oauth2-implicit](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/knife4j-spring-oauth2-implicit)

简化模式需要服务端配置Knife4j的回调地址`webjars/oauth/oauth2.html`

部分示例代码：
```java
@Override
public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    //简化模式（implicit）
    clients.inMemory()
        .withClient("app1").secret(noOpPasswordEncoder.encode("123"))
            .authorizedGrantTypes("implicit")
            .redirectUris(
                    "http://localhost:18010/webjars/oauth/oauth2.html")
            .scopes("read","write","reads","writes");
}
```

创建Docket对象时，设置OAuth2的授权类型，示例代码如下：

::: details 查看详细代码
```java
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        //schema
        List<GrantType> grantTypes=new ArrayList<>();
        //简单模式implicit
        ImplicitGrant implicitGrant=new ImplicitGrant(new LoginEndpoint("http://localhost:18010/oauth/authorize"),"access_token");
        grantTypes.add(implicitGrant);
        OAuth oAuth=new OAuthBuilder().name("oauth2")
                .grantTypes(grantTypes).build();
        //context
        //scope方位
        List<AuthorizationScope> scopes=new ArrayList<>();
        scopes.add(new AuthorizationScope("read","read  resources"));
        scopes.add(new AuthorizationScope("write","write resources"));
        scopes.add(new AuthorizationScope("reads","read all resources"));
        scopes.add(new AuthorizationScope("writes","write all resources"));

        SecurityReference securityReference=new SecurityReference("oauth2",scopes.toArray(new AuthorizationScope[]{}));
        SecurityContext securityContext=new SecurityContext(Lists.newArrayList(securityReference),PathSelectors.ant("/api/**"));
        //schemas
        List<SecurityScheme> securitySchemes=Lists.newArrayList(oAuth);
        //securyContext
        List<SecurityContext> securityContexts=Lists.newArrayList(securityContext);
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xiaominfo.knife4j.oauth2.web"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContexts)
                .securitySchemes(securitySchemes)
                .apiInfo(apiInfo());


    }



    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("knife4j-oauth2-demo").description("")
                .termsOfServiceUrl("https://doc.xiaominfo.com")
                .contact(new Contact("Developers", "https://gitee.com/xiaoym/knife4j", ""))
                .license("Open Source")
                .licenseUrl("\"https://www.apache.org/licenses/LICENSE-2.0")
                .version("1.0.0")
                .build();

    }


}
```
:::


最终呈现界面如下：

![](/knife4j/assert/oauth2/implicit.png)

输入clientId,然后点击Authorize按钮，最终跳转授权界面，如下图：


![](/knife4j/assert/oauth2/implicit1.png)


此时，选择进行授权即可

授权完成后，测试我们再我们的接口中，就会看到我们的参数`Authorization`值已经更新了，如下图：

![](/knife4j/assert/oauth2/grantcode2.png)

