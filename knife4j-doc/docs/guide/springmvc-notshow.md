# Spring MVC不显示接口文档

很多朋友在使用Spring MVC时,会碰到接口文档没有的情况,在此处做一个说明

1、首先保证SwaggerConfig的配置文件中配置正确的Docket对象(即默认扫描的包路径或者基于`@ApiOperation`注解)

2、其次,区别于SpringBoot的注入,不使用`@Configuration`注解注入到Spring的IOC容器中,采用`<bean>`XML注入的方式注入到Spring的容器中,如下：

```xml
<bean id="SwaggerConfig" class="com.xiaominfo.swagger.config.SwaggerConfiguration"></bean>
```

3、需保证注入的SwaggerConfig的bean在Spring的MVC的容器中,因为Spring MVC存在父子容器的关系,如果不将该Bean注入到Spring MVC容器中的话,Swagger就会扫描不到Controller层的接口，自然也就不会显示文档

例如：

```xml
<servlet>
    <servlet-name>swaggerDemoMvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextConfigLocation</param-name>
        <!--在第二步中注入的SwaggerConfig的bean需写在spring.xml文件中-->
      <param-value>classpath:config/spring.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>
```


 
 
 