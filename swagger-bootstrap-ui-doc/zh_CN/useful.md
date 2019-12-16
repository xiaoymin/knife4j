## Maven中引入Jar包

由于是`springfox-swagger`的增强UI包,所以基础功能依然依赖Swagger,`springfox-swagger`的jar包必须引入

```xml
<dependency>
 <groupId>io.springfox</groupId>
 <artifactId>springfox-swagger2</artifactId>
 <version>2.9.2</version>
</dependency>
```

然后引入SwaggerBootstrapUi的jar包

```xml
<dependency>
  <groupId>com.github.xiaoymin</groupId>
  <artifactId>swagger-bootstrap-ui</artifactId>
  <version>${lastVersion}</version>
</dependency>
```

## 编写Swagger2Config配置文件

Swagger2Config配置文件如下：

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

## 访问地址

swagger-bootstrap-ui`默认访问地址是：`http://${host}:${port}/doc.html