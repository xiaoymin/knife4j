# OAuth2认证

> Knife4j自2.0.6版本开始添加对OAuth2的认证支持
>

目前支持的OAuth2模式包括：简化模式(implicit)、授权码模式(authorization_code)、密码模式(password)、客户端模式(client_credentials)

## 简化模式(implicit)


简化模式(implicit)在Knife4j中界面显示效果如下(点击左侧菜单**Authorize**查看)：

简化模式(implicit)需要配置Knife4j提供的OAuth2的回调地址,回调页面位于`knife4j-spring-ui.jar`包

资源目录：`webjars/oauth/oauth2.html`

因此,需要在服务端配置该回调地址,Spring Security OAuth2示例代码如下：
```java
@Override
public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    //简化模式（implicit）
    clients.inMemory()
        .withClient("app1").secret("123")
            .authorizedGrantTypes("implicit")
            .redirectUris(\"http://192.168.1.10:8080/webjars/oauth/oauth2.html")
            .scopes("read","write","reads","writes");
}
```

界面效果如下：

![](/knife4j/images/knife4j/oauth2/oauth-implicit.png)

使用者直接输入`clientId`，点击授权,此时进行调整授权界面,开发者授权即可

**注意**：当点击按钮Authorize授权后,如果授权成功,接下来点击其余的接口进行调试时，在请求头会存在参数`Authorization`,如何在未授权之前打开了接口,则需要在授权后关闭该接口的Tab，然后重新打开


创建Swagger的授权配置示例代码：
```java
@Bean(value = "defaultApi2")
public Docket defaultApi2() {
    //schema
    List<GrantType> grantTypes=new ArrayList<>();
    //简单模式implicit
    ImplicitGrant implicitGrant=new ImplicitGrant(new LoginEndpoint("http://localhost:8999/oauth/authorize"),"access_token");
    grantTypes.add(implicitGrant);

    OAuth oAuth=new OAuthBuilder().name("oauth2")
            .grantTypes(grantTypes).build();
    //context
    //scope方位
    List<AuthorizationScope> scopes=new ArrayList<>();
    scopes.add(new AuthorizationScope("read","read all resources"));
    SecurityReference securityReference=new SecurityReference("oauth2",scopes.toArray(new AuthorizationScope[]{}));
    SecurityContext securityContext=new SecurityContext(CollectionUtil.newArrayList(securityReference),PathSelectors.ant("/api/**"));
    //schemas
    List<SecurityScheme> securitySchemes=CollectionUtil.newArrayList(oAuth);
    //securyContext
    List<SecurityContext> securityContexts=CollectionUtil.newArrayList(securityContext);

    String groupName="2.X版本";
    Docket docket=new Docket(DocumentationType.SWAGGER_2)
            .host("https://www.baidu.com")
            .apiInfo(apiInfo())
            .groupName(groupName)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.new2"))
            .paths(PathSelectors.any())
            .build()
            .securityContexts(securityContexts).securitySchemes(securitySchemes);
    return docket;
}
```


## 授权码模式(authorization_code)

授权码模式(authorization_code)在Knife4j中界面显示效果如下(点击左侧菜单**Authorize**查看)：

授权码模式(authorization_code)需要配置Knife4j提供的OAuth2的回调地址,回调页面位于`knife4j-spring-ui.jar`包

资源目录：`webjars/oauth/oauth2.html`

因此,需要在服务端配置该回调地址,Spring Security OAuth2示例代码如下：
```java
@Override
public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    //授权码模式（authorization code）
    clients.inMemory()
        .withClient("app1").secret("123")
            .authorizedGrantTypes("authorization_code")
            .redirectUris(\"http://192.168.1.10:8080/webjars/oauth/oauth2.html")
            .scopes("read","write","reads","writes");
}
```
界面效果如下：

![](/knife4j/images/knife4j/oauth2/oauth-authorization.png)

使用者直接输入`clientId`及`clientSecret`后，点击授权,此时进行调整授权界面,开发者授权即可

**注意**：当点击按钮Authorize授权后,如果授权成功,接下来点击其余的接口进行调试时，在请求头会存在参数`Authorization`,如何在未授权之前打开了接口,则需要在授权后关闭该接口的Tab，然后重新打开

创建Swagger的授权配置示例代码：
```java
@Bean(value = "defaultApi2")
public Docket defaultApi2() {
    //schema
    List<GrantType> grantTypes=new ArrayList<>();
    //授权码模式AuthorizationCodeGrant
    TokenRequestEndpoint tokenRequestEndpoint=new TokenRequestEndpoint("http://localhost:8999/oauth/authorize","app1","123");
    TokenEndpoint tokenEndpoint=new TokenEndpoint("http://192.168.1.10:8080/oauth/token","access_token");
    AuthorizationCodeGrant authorizationCodeGrant=new AuthorizationCodeGrant(tokenRequestEndpoint,tokenEndpoint);
    grantTypes.add(authorizationCodeGrant);

    OAuth oAuth=new OAuthBuilder().name("oauth2")
            .grantTypes(grantTypes).build();
    //context
    //scope方位
    List<AuthorizationScope> scopes=new ArrayList<>();
    scopes.add(new AuthorizationScope("read","read all resources"));
    SecurityReference securityReference=new SecurityReference("oauth2",scopes.toArray(new AuthorizationScope[]{}));
    SecurityContext securityContext=new SecurityContext(CollectionUtil.newArrayList(securityReference),PathSelectors.ant("/api/**"));
    //schemas
    List<SecurityScheme> securitySchemes=CollectionUtil.newArrayList(oAuth);
    //securyContext
    List<SecurityContext> securityContexts=CollectionUtil.newArrayList(securityContext);

    String groupName="2.X版本";
    Docket docket=new Docket(DocumentationType.SWAGGER_2)
            .host("https://www.baidu.com")
            .apiInfo(apiInfo())
            .groupName(groupName)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.new2"))
            .paths(PathSelectors.any())
            .build()
            .securityContexts(securityContexts).securitySchemes(securitySchemes);
    return docket;
}
```


## 客户端模式(client_credentials)

客户端模式(client_credentials)在Knife4j中界面显示效果如下(点击左侧菜单**Authorize**查看)：

界面效果如下：

![](/knife4j/images/knife4j/oauth2/oauth-client.png)

使用者直接输入`clientId`及`clientSecret`后，点击授权即可

**注意**：当点击按钮Authorize授权后,如果授权成功,接下来点击其余的接口进行调试时，在请求头会存在参数`Authorization`,如何在未授权之前打开了接口,则需要在授权后关闭该接口的Tab，然后重新打开

创建Swagger的授权配置示例代码：
```java
@Bean(value = "defaultApi2")
public Docket defaultApi2() {
    //schema
    List<GrantType> grantTypes=new ArrayList<>();
    //客户端模式（client credentials）
    String clientTokenUrl="http://192.168.1.10:8080/oauth/token";
    ClientCredentialsGrant clientCredentialsGrant=new ClientCredentialsGrant(clientTokenUrl);
    grantTypes.add(clientCredentialsGrant);


    OAuth oAuth=new OAuthBuilder().name("oauth2")
            .grantTypes(grantTypes).build();
    //context
    //scope方位
    List<AuthorizationScope> scopes=new ArrayList<>();
    scopes.add(new AuthorizationScope("read","read all resources"));
    SecurityReference securityReference=new SecurityReference("oauth2",scopes.toArray(new AuthorizationScope[]{}));
    SecurityContext securityContext=new SecurityContext(CollectionUtil.newArrayList(securityReference),PathSelectors.ant("/api/**"));
    //schemas
    List<SecurityScheme> securitySchemes=CollectionUtil.newArrayList(oAuth);
    //securyContext
    List<SecurityContext> securityContexts=CollectionUtil.newArrayList(securityContext);

    String groupName="2.X版本";
    Docket docket=new Docket(DocumentationType.SWAGGER_2)
            .host("https://www.baidu.com")
            .apiInfo(apiInfo())
            .groupName(groupName)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.new2"))
            .paths(PathSelectors.any())
            .build()
            .securityContexts(securityContexts).securitySchemes(securitySchemes);
    return docket;
}
```

## 密码模式(password)

密码模式(password)在Knife4j中界面显示效果如下(点击左侧菜单**Authorize**查看)：

界面效果如下：

![](/knife4j/images/knife4j/oauth2/oauth-password.png)

使用者直接输入`username`,`password`,`clientId`及`clientSecret`后，点击授权即可

**注意**：当点击按钮Authorize授权后,如果授权成功,接下来点击其余的接口进行调试时，在请求头会存在参数`Authorization`,如何在未授权之前打开了接口,则需要在授权后关闭该接口的Tab，然后重新打开

创建Swagger的授权配置示例代码：
```java
@Bean(value = "defaultApi2")
public Docket defaultApi2() {
    //schema
    List<GrantType> grantTypes=new ArrayList<>();
    //密码模式
    String passwordTokenUrl="http://192.168.1.10:8080/oauth/token";
    ResourceOwnerPasswordCredentialsGrant resourceOwnerPasswordCredentialsGrant=new ResourceOwnerPasswordCredentialsGrant(passwordTokenUrl);
    grantTypes.add(resourceOwnerPasswordCredentialsGrant);

    OAuth oAuth=new OAuthBuilder().name("oauth2")
            .grantTypes(grantTypes).build();
    //context
    //scope方位
    List<AuthorizationScope> scopes=new ArrayList<>();
    scopes.add(new AuthorizationScope("read","read all resources"));
    SecurityReference securityReference=new SecurityReference("oauth2",scopes.toArray(new AuthorizationScope[]{}));
    SecurityContext securityContext=new SecurityContext(CollectionUtil.newArrayList(securityReference),PathSelectors.ant("/api/**"));
    //schemas
    List<SecurityScheme> securitySchemes=CollectionUtil.newArrayList(oAuth);
    //securyContext
    List<SecurityContext> securityContexts=CollectionUtil.newArrayList(securityContext);

    String groupName="2.X版本";
    Docket docket=new Docket(DocumentationType.SWAGGER_2)
            .host("https://www.baidu.com")
            .apiInfo(apiInfo())
            .groupName(groupName)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.new2"))
            .paths(PathSelectors.any())
            .build()
            .securityContexts(securityContexts).securitySchemes(securitySchemes);
    return docket;
}
```


 
 
 