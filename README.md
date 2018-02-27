swagger-bootstrap-ui
=========================
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.xiaoymin/swagger-bootstrap-ui/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.xiaoymin/swagger-bootstrap-ui)

## English introduction | [中文说明](README_zh.md)

## Introduction

`Swagger-bootstrap-ui` is the Swagger front-end UI implementation, the purpose is to replace the Swagger default UI implementation Swagger-UI, make the document more friendly....

`Swagger-bootstrap-ui` is just the UI implementation of Swagger, instead of replacing Swagger function. So the back-end module still depends on Swagger, and it needs to cooperate with Swagger's annotation to achieve the effect, [annotation explanation](swagger-annotation.md).

## Functions


* The interface document is illustrated with the effect diagram as follows：



![](https://static.oschina.net/uploads/space/2017/1218/160550_m8iQ_254762.jpg)

* Online debugging function, the effect diagram is as follows:

![](https://static.oschina.net/uploads/space/2017/1218/160754_zeO9_254762.jpg)

## 

## Demo

see [swagger-bootstarp-ui-demo](http://git.oschina.net/xiaoym/swagger-bootstrap-ui-demo)

## Download

download address: [http://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases](http://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)

## Use

* First, you need to introduce the configuration package information of swagger, as follows：



```java

<dependency>

 <groupId>io.springfox</groupId>

 <artifactId>springfox-swagger2</artifactId>

 <version>2.2.2</version>

</dependency>



<!-- Here swagger-ui is the default implementation of swagger, and this jar can be replaced with the following swagger-bootstrap-ui substitution--->

<dependency>

 <groupId>io.springfox</groupId>

 <artifactId>springfox-swagger-ui</artifactId>

 <version>2.2.2</version>

</dependency>



```






* The jar package dependency of swagger-bootstrap-ui is referenced in the Maven project, as follows：



```java
<dependency>
  <groupId>com.github.xiaoymin</groupId>
  <artifactId>swagger-bootstrap-ui</artifactId>
  <version>1.6</version>
</dependency>
```



* Swagger is enabled in the Spring project, and the code is as follows：


1.Annotation

```java

@Configuration

@EnableSwagger2

public class SwaggerConfiguration {



 @Bean

 public Docket createRestApi() {

 return new Docket(DocumentationType.SWAGGER_2)

 .apiInfo(apiInfo())

 .select()

 .apis(RequestHandlerSelectors.basePackage("com.bycdao.cloud"))

 .paths(PathSelectors.any())

 .build();

 }



 private ApiInfo apiInfo() {

 return new ApiInfoBuilder()

 .title("swagger-bootstrap-ui RESTful APIs")

 .description("swagger-bootstrap-ui")

 .termsOfServiceUrl("http://localhost:8999/")

 .contact("developer@mail.com")

 .version("1.0")

 .build();

 }



}



```



* swagger-bootstrap-ui default access address：`http://${host}:${port}/doc.html`



## Attention



* Request address swagger package given by default is `/v2/api-docs`, so the swagger-bootstrap-ui call back is `/v2/api-docs`, not with the suffix, and the need to return to the JSON data frame if spring boot can be used directly without modification, if MVC is Spring, the configuration of the DispatcherServlet in web.xml, you need an additional URL matching rules are as follows：



```java

<servlet>

 <servlet-name>cmsMvc</servlet-name>

 <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>

 <init-param>

 <param-name>contextConfigLocation</param-name>

 <param-value>classpath:config/spring.xml</param-value>

 </init-param>

 <load-on-startup>1</load-on-startup>

</servlet>



<!--Default configuration,.Htm|.do|.json and so on configuration-->

<servlet-mapping>

 <servlet-name>cmsMvc</servlet-name>

 <url-pattern>*.htm</url-pattern>

</servlet-mapping>

<!-- Configuring the URL request path for swagger-bootstrap-ui-->

<servlet-mapping>

 <servlet-name>cmsMvc</servlet-name>

 <url-pattern>/v2/api-docs</url-pattern>

</servlet-mapping>

```
## Thanks

Especially thanks to the js/css and HTML front-end frame developed by the following Daniel, beautiful and easy to use

| frame       | website                                  |
| ----------- | ---------------------------------------- |
| *jquery*    | [http://jquery.com/](http://jquery.com/ "http://jquery.com/") |
| *bootstrap* | [http://getbootstrap.com](http://getbootstrap.com "http://getbootstrap.com") |
| *layer*     | [http://layer.layui.com/](http://layer.layui.com/ "http://layer.layui.com/") |
| *jsonview*  | [https://github.com/yesmeck/jquery-jsonview](https://github.com/yesmeck/jquery-jsonview "https://github.com/yesmeck/jquery-jsonview") |
