# 1.2 SpringMVC框架集成Knife4j


如果你是Spring MVC项目,想使用knife4j提供的增强ui包,使用方法很简单

demo参考示例地址：[knife4j-spring-mvc-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/knife4j-spring-mvc-demo)

## 1.2.1 依赖引用

### 1.2.1.1 2.0.4(包含)以前的版本

如果开发者使用的是2.0.4(包含)以前的版本,那么需要引入以下2个jar包

第一步是需要引入Knife4j提供的依赖包，如下：

```xml
<!--引入Knife4j-->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring</artifactId>
    <!--在引用时请在maven中央仓库搜索最新版本号-->
    <version>2.0.4</version>
</dependency>
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-ui</artifactId>
    <!--在引用时请在maven中央仓库搜索最新版本号-->
    <version>2.0.4</version>
</dependency>
```

### 1.2.1.2 2.0.4(不包含)以后的版本

在2.0.4(不包含)以后的版本中,引入Knife4j进行了改善,开发者只需要引入1个jar包，如下：

```xml
<!--引入Knife4j-->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-mvc</artifactId>
    <!--在引用时请在maven中央仓库搜索最新版本号，如果不存在则代表尚未发布,作者正在努力开发中-->
    <version>2.0.5</version>
</dependency>
```

## 1.2.2 创建配置文件

第二步是创建配置文件`SwaggerConfiguration.java`,示例代码如下：

```java
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {


    @Bean
    public Docket defaultApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(groupApiInfo())
                .groupName("默认接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xiaominfo.knife4j.controller"))
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo groupApiInfo(){
        return new ApiInfoBuilder()
                .title("swagger-bootstrap-ui很棒~~~！！！")
                .description("swagger-bootstrap-ui-demo RESTful APIs")
                .termsOfServiceUrl("http://www.group.com/")
                .contact("group@qq.com")
                .version("1.0")
                .build();
    }
}
```

## 1.2.3 配置静态文件

由于knife4j是通过webjar的方式提供服务,因此对外访问的`doc.html`需要在我们的mvc环境中配置静态目录,否则会出现404，在`spring.xml`主容器的配置文件中配置,代码如下：

```xml
<mvc:resources location="classpath:/META-INF/resources/" mapping="doc.html"/>
<mvc:resources location="classpath:/META-INF/resources/webjars/" mapping="/webjars/**"/>
```

## 1.2.4 配置增强Filter(可选)

如果开发者需要使用Knife4j提供的两个增强Filter：`Basic验证`和`Production验证`

那么需要在`web.xml`进行配置，代码如下：

```xml
<!--Knife4j提供的Swagger增强功能,Filter过滤保护Swagger资源-->
<!--生产环境Filter-->
<filter>
    <filter-name>knife4jProductionFilter</filter-name>
    <filter-class>com.github.xiaoymin.knife4j.spring.filter.ProductionSecurityFilter</filter-class>
    <init-param>
        <param-name>production</param-name>
        <!--如果该值配置为true则代表开启-->
        <param-value>false</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>knife4jProductionFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
<!--Swagger资源的Basic认证保护策略-->
<filter>
    <filter-name>knife4jSecurityBasic</filter-name>
    <filter-class>com.github.xiaoymin.knife4j.spring.filter.SecurityBasicAuthFilter</filter-class>
    <!--开启basic认证-->
    <init-param>
        <param-name>enableBasicAuth</param-name>
         <!--如果该值配置为true则代表开启-->
        <param-value>false</param-value>
    </init-param>
    <!--用户名&密码-->
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
    <filter-name>knife4jSecurityBasic</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>

```

这2个增强Filter二者只能有一个存在。

如果开发者不需要配置,可忽略该步骤。

## 1.2.5 配置web.xml

最后一步,我们需要配置`web.xml`，添加部分`servlet-mapping`节点，添加这些节点是防止swagger的接口出现404的出现，配置如下：

```xml
<!-- 配置knife4j的url请求路径-->

<!--1.该接口是springfox提供的Swagger实例接口-->
<servlet-mapping>
    <servlet-name>knife4jDemoMvc</servlet-name>
    <url-pattern>/v2/api-docs</url-pattern>
</servlet-mapping>
<!--2.该接口是springfox提供的Swagger分组接口-->
<servlet-mapping>
    <servlet-name>knife4jDemoMvc</servlet-name>
    <url-pattern>/swagger-resources</url-pattern>
</servlet-mapping>
<!--3.该接口是springfox提供的Swagger配置接口-->
<servlet-mapping>
    <servlet-name>knife4jDemoMvc</servlet-name>
    <url-pattern>/swagger-resources/configuration/ui</url-pattern>
</servlet-mapping>
<!--4.该接口是springfox提供的Swagger权限接口(在knife4j中未使用)-->
<servlet-mapping>
    <servlet-name>knife4jDemoMvc</servlet-name>
    <url-pattern>/swagger-resources/configuration/security</url-pattern>
</servlet-mapping>

<!--5.该接口是knife4j提供的Swagger增强接口-->
<servlet-mapping>
    <servlet-name>knife4jDemoMvc</servlet-name>
    <url-pattern>/v2/api-docs-ext</url-pattern>
</servlet-mapping>
```

在上面的配置中,1、2、3是必须添加的

如果开发者需要使用knife4j提供的增强功能,那么第5点也需要添加上

## 1.2.6 配置增强模式(可选)

如果开发者需要使用`Knife4j`提供的增强模式中的功能,那么需要进行配置，主要分两个版本

### 1.2.6.1 2.0.4(包含)以前的版本

如果开发者使用的是2.0.4(包含)以前的版本，启用增强功能则在`SwaggerConfiguration`配置文件中进行配置，代码如下：

```java
@Configuration
@EnableSwagger2
//增强扫描
@ComponentScan(
        basePackages = {
                "com.github.xiaoymin.knife4j.spring.plugin",
                "com.github.xiaoymin.knife4j.spring.web"
        }
)
public class SwaggerConfiguration {
    //more..
}
```

### 1.2.6.2 2.0.4(不包含)以后的版本

在2.0.4(不包含)以后的版本,依赖引用会有所不同,开发者引入`knife4j-spring-mvc`的jar包,该包中存在`@EnableKnife4j`注解,则启用增强代码如下：

```java
@Configuration
@EnableSwagger2
//增强扫描
@EnableKnife4j
public class SwaggerConfiguration {
    //more..
}
```

原理很简单,只是提供一个方便大家记忆的注解,其核心只是把上面`@ComponentScan`注解的功能进行了集成。

## 1.2.7 访问

最后,开发者可以在浏览器通过地址进行访问,访问格式：`http://host:port/doc.html`