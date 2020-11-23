# Get start

## Introducing the Jar package in Maven

Because it is an enhanced UI package for springfox-swagger, the basic functions still depend on Swagger, and the jar package for springfox-swagger must be introduced.

```xml
<dependency>
 <groupId>io.springfox</groupId>
 <artifactId>springfox-swagger2</artifactId>
 <version>2.9.2</version>
</dependency>
```

Then introduce the jar package of SwaggerBootstrapUi

```xml
<dependency>
  <groupId>com.github.xiaoymin</groupId>
  <artifactId>swagger-bootstrap-ui</artifactId>
  <version>${lastVersion}</version>
</dependency>
```

## Write the Swagger2Config configuration file

Swagger2Config configuration file is as follows：

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

## Access address

Swagger-bootstrap-ui default access address is：`http://${host}:${port}/doc.html
 
 <icp/> 
 comment/> 
 
 
 
 