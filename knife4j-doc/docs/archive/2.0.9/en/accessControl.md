# Access control

n developing ` SwaggerBootstrapUi ` function, with many developers often discuss the problem is that in a production environment, block or remove Swagger document very troublesome

,Usually we have the following problems：

- When a system deploys a production environment, we want to shield Swagger's documentation capabilities, whether they are interface or HTML documents
- Usually we sometimes need production environment deployment, but Swagger document debugging function to assist developers debugging, but there are security concerns, there is no filtering of the Swagger resource interface
- etc.

In view of the above two situations, SwaggerBootstrapUi in version 1.9.0 joined the Filter function of the Filter, if the developer use SpringBoot development framework for development, only in ` application. The properties ` or ` application. Yml ` configuration file configuration properties can be convenient to solve the above problems, don't delete Springfox - swagger jar packages or delete the related code complex operations, development experience of ascension.

## Enable the production environment to shield Swagger all resource interfaces

Currently ` Springfox - Swagger ` and ` SwaggerBootstrapUi ` resources interface includes the following：

| Resource                                      | Description                                    |
| ----------------------------------------- | --------------------------------------- |
| /doc.html                                 | SwaggerBootstrapUi provides the document access address    |
| /api-docs-ext                             | SwaggerBootstrapUi provides addresses for enhanced interfaces    |
| /swagger-resources                        | Springfox-swagger provides grouped interfaces         |
| /api-docs                                 | Springfox-swagger provides the packet instance detail interface |
| /swagger-ui.html                          | Springfox-swagger - document access addresses provided by the Swagger     |
| /swagger-resources/configuration/ui       | Springfox-swagger provides                  |
| /swagger-resources/configuration/security | Springfox-swagger provides                   |


When we deploy a system to a production system, we will need to shield all Swagger associated resources for interface security

If you use SpringBoot,Only Configurations in `application.properties` or `application.yml`


```properties
swagger.production=true
```

When this property is configured, all resources are disabled.

like：

![](/knife4j/images/ac.png)

## Access page weighting control

Whether the official `swagger-ui.html` or ` doc.html`, interface access is currently access interface without permission of the document, many friends asked me before can provide a login interface functions, developers enter the user name and password to access control interface, only people who know the user name and password to access this document


Doing login page control requires a user concept, so it was not provided for quite some time

But in ` 1.9.0 ` version, in view of the resource interface Swagger, ` SwaggerBootstrapUi ` provides simple **Basic authentication function** 



like ：

![](/knife4j/images/ac-pwd.png)

Allows developers in the configuration file to configure a static user name and password, when docking access Swagger page, enter the configuration of a user name and password to access Swagger document page, if you use SpringBoot development, simply in the corresponding ` application.properties ` or ` application.yml ` configuration is as follows:

```properties
## Enable swagger basic funs,default false
swagger.basic.enable=true
## Basic username
swagger.basic.username=zhangsan
## Basic password
swagger.basic.password=123
```

If the user opens the basic authentication function, but is not configured user name and password, ` SwaggerBootstrapUi ` provides the default user name and password：

```text
admin/123321
```

If you use `SpringMVC`,need configuration Filter in `web.xml`,example：

```xml
  <!--Production Filter-->
  <filter>
    <filter-name>swaggerProductionFilter</filter-name>
    <filter-class>com.github.xiaoymin.swaggerbootstrapui.filter.ProductionSecurityFilter</filter-class>
    <init-param>
      <param-name>production</param-name>
      <param-value>false</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>swaggerProductionFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  <!--Swagger resource Basic Access-->
  <filter>
    <filter-name>swaggerSecurityBasic</filter-name>
    <filter-class>com.github.xiaoymin.swaggerbootstrapui.filter.SecurityBasicAuthFilter</filter-class>
    <!--enable Basic-->
    <init-param>
      <param-name>enableBasicAuth</param-name>
      <param-value>true</param-value>
    </init-param>
    <!--use&pwd-->
    <init-param>
      <param-name>userName</param-name>
      <param-value>lisi</param-value>
    </init-param>
    <init-param>
      <param-name>password</param-name>
      <param-value>123</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>swaggerSecurityBasic</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
```

[ProductionSecurityFilter](https://gitee.com/xiaoym/swagger-bootstrap-ui/blob/master/swagger-bootstrap-ui/src/main/java/com/github/xiaoymin/swaggerbootstrapui/filter/ProductionSecurityFilter.java)和[SecurityBasicAuthFilter](https://gitee.com/xiaoym/swagger-bootstrap-ui/blob/master/swagger-bootstrap-ui/src/main/java/com/github/xiaoymin/swaggerbootstrapui/filter/SecurityBasicAuthFilter.java)只需配置一个即可

[ProductionSecurityFilter](https://gitee.com/xiaoym/swagger-bootstrap-ui/blob/master/swagger-bootstrap-ui/src/main/java/com/github/xiaoymin/swaggerbootstrapui/filter/ProductionSecurityFilter.java):开启生产环境,屏蔽所有Swagger资源,不可访问,production配置为true时,basic认证功能不可用

[SecurityBasicAuthFilter](https://gitee.com/xiaoym/swagger-bootstrap-ui/blob/master/swagger-bootstrap-ui/src/main/java/com/github/xiaoymin/swaggerbootstrapui/filter/SecurityBasicAuthFilter.java)：开启HTTP Basic认证,访问Swagger资源需要提供服务端配置的用户名以及密码
 
 
 