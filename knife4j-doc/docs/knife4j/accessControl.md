# 访问权限控制


::: warning
访问权限控制功能是`knife4j`提供的增强功能,开发者要想使用`knife4j`提供的增强功能,必须在Swagger的配置文件中引入增强注解,各个版本的增强注解区别如下表:<br />
:::

|软件|版本|增强注解|说明|
|--|--|--|--|
|knife4j|<=`2.0.0`|`@EnableSwaggerBootstrapUi`||
|knife4j|>=`2.0.1`|`@EnableKnife4j`|后续版本不会再更改|
 
- 在使用`knife4j`的<=`2.0.0`版本之前的代码方式,代码示例如下：
```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUi
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    
 //more..

}
```
- 在使用`knife4j`的>=`2.0.1`版本之后的代码方式,代码示例如下：
```java
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    
 //more..

}
```



在开发`Knife4j`功能时,同很多开发者经常讨论的问题就是在生产环境时,屏蔽或者去除Swagger的文档很麻烦

,通常有时候我们碰到的问题如下：

- 系统部署生产环境时,我们想屏蔽Swagger的文档功能,不管是接口或者html文档
- 通常我们有时候需要生产环境部署后,又需要Swagger的文档调试功能,辅助开发者调试,但是存在安全隐患,没有对Swagger的资源接口过滤
- 等等

针对以上两种情况,SwaggerBootstrapUi在1.9.0的版本中加入了过滤Filter功能,如果开发者使用SpringBoot开发框架进行开发的话,只需在`application.properties`或者`application.yml`配置文件中配置相关属性即可方便的解决上面的问题,不用删除Springfox-swagger的jar包或者删除相关代码等复杂的操作,提升开发体验.

## 开启生产环境,屏蔽Swagger所有资源接口

目前`Springfox-Swagger`以及`SwaggerBootstrapUi`提供的资源接口包括如下：

| 资源                                      | 说明                                    |
| ----------------------------------------- | --------------------------------------- |
| /doc.html                                 | SwaggerBootstrapUi提供的文档访问地址    |
| /api-docs-ext                             | SwaggerBootstrapUi提供的增强接口地址    |
| /swagger-resources                        | Springfox-Swagger提供的分组接口         |
| /api-docs                                 | Springfox-Swagger提供的分组实例详情接口 |
| /swagger-ui.html                          | Springfox-Swagger提供的文档访问地址     |
| /swagger-resources/configuration/ui       | Springfox-Swagger提供                   |
| /swagger-resources/configuration/security | Springfox-Swagger提供                   |

当我们部署系统到生产系统,为了接口安全,需要屏蔽所有Swagger的相关资源

如果使用SpringBoot框架,只需在`application.properties`或者`application.yml`配置文件中配置

```properties
knife4j.production=true
```

配置此属性后,所有资源都会屏蔽输出.

效果图如下：

![](/knife4j/images/ac.png)

## 访问页面加权控制

不管是官方的`swagger-ui.html`或者`doc.html`,目前接口访问都是无需权限即可访问接口文档的,很多朋友以前问我能不能提供一个登陆界面的功能,开发者输入用户名和密码来控制界面的访问,只有知道用户名和密码的人才能访问此文档

做登录页控制需要有用户的概念,所以相当长一段时间都没有提供此功能

不过在`1.9.0`版本时,针对Swagger的资源接口,`SwaggerBootstrapUi`提供了简单的**Basic认证功能**

效果图如下：

![](/knife4j/images/ac-pwd.png)

允许开发者在配置文件中配置一个静态的用户名和密码,当对接者访问Swagger页面时,输入此配置的用户名和密码后才能访问Swagger文档页面,如果您使用SpringBoot开发,则只需在相应的`application.properties`或者`application.yml`中配置如下：

```properties
## 开启Swagger的Basic认证功能,默认是false
knife4j.basic.enable=true
## Basic认证用户名
knife4j.basic.username=zhangsan
## Basic认证密码
knife4j.basic.password=123
```

如果用户开启了basic认证功能,但是并未配置用户名及密码,`SwaggerBootstrapUi`提供了默认的用户名和密码：

```text
admin/123321
```

如果您使用的是`SpringMVC`,则需要在`web.xml`中添加相应的Filter,参考如下：

```xml
<!--SwaggerBootstrapUi提供的Swagger增强功能,Filter过滤保护Swagger资源-->
  <!--生产环境Filter-->
  <filter>
    <filter-name>swaggerProductionFilter</filter-name>
    <filter-class>com.github.xiaoymin.knife4j.spring.filter.ProductionSecurityFilter</filter-class>
    <init-param>
      <param-name>production</param-name>
      <param-value>false</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>swaggerProductionFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  <!--Swagger资源的Basic认证保护策略-->
  <filter>
    <filter-name>swaggerSecurityBasic</filter-name>
    <filter-class>com.github.xiaoymin.knife4j.spring.filter.SecurityBasicAuthFilter</filter-class>
    <!--开启basic认证-->
    <init-param>
      <param-name>enableBasicAuth</param-name>
      <param-value>true</param-value>
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
    <filter-name>swaggerSecurityBasic</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
```

[ProductionSecurityFilter](https://gitee.com/xiaoym/knife4j/blob/master/knife4j/knife4j-spring/src/main/java/com/github/xiaoymin/knife4j/spring/filter/ProductionSecurityFilter.java)和[SecurityBasicAuthFilter](https://gitee.com/xiaoym/knife4j/blob/master/knife4j/knife4j-spring/src/main/java/com/github/xiaoymin/knife4j/spring/filter/SecurityBasicAuthFilter.java)只需配置一个即可

[ProductionSecurityFilter](https://gitee.com/xiaoym/knife4j/blob/master/knife4j/knife4j-spring/src/main/java/com/github/xiaoymin/knife4j/spring/filter/ProductionSecurityFilter.java):开启生产环境,屏蔽所有Swagger资源,不可访问,production配置为true时,basic认证功能不可用

[SecurityBasicAuthFilter](https://gitee.com/xiaoym/knife4j/blob/master/knife4j/knife4j-spring/src/main/java/com/github/xiaoymin/knife4j/spring/filter/SecurityBasicAuthFilter.java)：开启HTTP Basic认证,访问Swagger资源需要提供服务端配置的用户名以及密码
 
 
 