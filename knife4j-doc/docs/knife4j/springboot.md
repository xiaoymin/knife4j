# Spring Boot 项目使用

如果你是Spring Boot项目,想使用knife4j提供的增强ui包,使用方法很简单

demo参考示例地址：[knife4j-spring-boot-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/knife4j-spring-boot-demo)


## maven引用

第一步,是在项目的`pom.xml`文件中引入`knife4j`的依赖,如下：

```xml
<dependencies>
    <dependency>
        <groupId>com.github.xiaoymin</groupId>
        <artifactId>knife4j-spring-boot-starter</artifactId>
        <!--在引用时请在maven中央仓库搜索最新版本号-->
        <version>2.0.2</version>
    </dependency>
</dependencies>
```

如果你想使用bom的方式引入,请参考[Maven Bom方式引用](mavenbom.md)

## 创建Swagger配置文件

新建Swagger的配置文件`SwaggerConfiguration.java`文件,创建springfox提供的Docket分组对象,代码如下：

```java
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
 

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //分组名称
                .groupName("2.X版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.new2"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

}
```

以上有两个注解需要特别说明，如下表：

|注解|说明|
|----|----|
|`@EnableSwagger2`|该注解是Springfox-swagger框架提供的使用Swagger注解，该注解必须加|
|`@EnableKnife4j`|该注解是`knife4j`提供的增强注解,Ui提供了例如动态参数、参数过滤、接口排序等增强功能,如果你想使用这些增强功能就必须加该注解，否则可以不用加|

## 访问

在浏览器输入地址：`http://host:port/doc.html`


 
 
 