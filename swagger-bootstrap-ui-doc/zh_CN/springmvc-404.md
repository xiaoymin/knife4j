SpringMvc中文档出现异常一般分2种情况：

- doc.html打开提示404
- doc.html已经可以打开,但是页面无任何接口文档

针对以上两种情况的解决办法：

第一种：**doc.html打开提示404?**

一般无需添加此配置,如果出现这种情况,在Spring的xml配置文件中,添加Spring的静态资源映射路径即可,如下：

```xml
<mvc:resources location="classpath:/META-INF/resources/" mapping="doc.html"/>
<mvc:resources location="classpath:/META-INF/resources/webjars/" mapping="/webjars/**"/>
```

第二种：**doc.html已经可以打开,但是页面无任何接口文档？**

在[web.xml](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/blob/master/swagger-bootstrap-ui-demo-mvc/src/main/webapp/WEB-INF/web.xml)中配置了`DispatcherServlet`,则需要追加一个url匹配规则,如下

```xml
<!-- 配置swagger-bootstrap-ui的url请求路径-->
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
<!--此接口地址为SwaggerBootstrapUi提供的增强地址,如果不使用增强功能,可排除此配置-->
<servlet-mapping>
    <servlet-name>swaggerDemoMvc</servlet-name>
    <url-pattern>/v2/api-docs-ext</url-pattern>
</servlet-mapping>
```

关于SpringMvc的代码示例可参考[swagger-bootstrap-ui-demo-mvc](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/swagger-bootstrap-ui-demo-mvc)