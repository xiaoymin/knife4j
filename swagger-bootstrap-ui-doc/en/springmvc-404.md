There are two cases in the SpringMvc document exception:

- doc.html Open prompt 404
- doc.html is already open, but the page does not have any interface documentation

Solution for the above two situations：

one：**doc.html Open prompt 404?**

Generally, there is no need to add this configuration. If this happens, add Spring's static resource mapping path in Spring's xml configuration file, as follows:

```xml
<mvc:resources location="classpath:/META-INF/resources/" mapping="doc.html"/>
<mvc:resources location="classpath:/META-INF/resources/webjars/" mapping="/webjars/**"/>
```

two：**Doc.html is already open, but the page does not have any interface documentation？**

To configure the DispatcherServlet in [web.xml](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/blob/master/swagger-bootstrap-ui-demo-mvc/src/main/webapp/WEB-INF/web.xml), you need to append a url matching rule, as follows

```xml
<!-- Configure the url request path for swagger-bootstrap-ui-->
<servlet-mapping>
    <servlet-name>swaggerDemoMvc</servlet-name>
    <url-pattern>/v2/api-docs</url-pattern>
</servlet-mapping>
<servlet-mapping>
    <servlet-name>swaggerDemoMvc</servlet-name>
    <url-pattern>/swagger-resources</url-pattern>
</servlet-mapping>
<servlet-mapping>
    <servlet-name>swaggerDemoMvc</servlet-name>
    <url-pattern>/swagger-resources/configuration/ui</url-pattern>
</servlet-mapping>
<servlet-mapping>
    <servlet-name>swaggerDemoMvc</servlet-name>
    <url-pattern>/swagger-resources/configuration/security</url-pattern>
</servlet-mapping>
<!--This interface address is an enhanced address provided by SwaggerBootstrapUi, which can be excluded if no enhancements are used.-->
<servlet-mapping>
    <servlet-name>swaggerDemoMvc</servlet-name>
    <url-pattern>/v2/api-docs-ext</url-pattern>
</servlet-mapping>
```

For a code example of SpringMvc, see [swagger-bootstrap-ui-demo-mvc](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/swagger-bootstrap-ui-demo-mvc)