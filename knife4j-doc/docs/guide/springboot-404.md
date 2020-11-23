# SpringBoot访问doc.html页面404

默认情况下并不需要添加此配置即可访问

很多朋友在使用SpringBoot集成swagger-bootstrap-ui后，都无法访问doc.html界面，此时，你可能需要实现SpringBoot的`WebMvcConfigurer`接口，添加相关的ResourceHandler,代码如下：

```java
@SpringBootApplication
public class SwaggerBootstrapUiDemoApplication  implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("doc.html").addResourceLocations("classpath*:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath*:/META-INF/resources/webjars/");
	}
}
```

或者

```java

@SpringBootApplication
public class SwaggerBootstrapUiDemoApplication  implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
```

如果你是使用的老的版本SpringBoot,通过继承`WebMvcConfigurationSupport`来扩展SpringBoot相关的配置,则把以上配置加在相应的`addResourceHandlers`方法中即可

推荐使用实现`WebMvcConfigurer`接口的方式来进行扩展


如果以上方式还是不行,建议开启Spring的Debug日志来进行跟踪,一般访问`doc.html`页面会出现如下日志(成功情况下)：
```java
2019-04-19 13:39:36,896 DEBUG (AbstractHandlerMethodMapping.java:312)- Looking up handler method for path /doc.html
2019-04-19 13:39:36,902 DEBUG (AbstractHandlerMethodMapping.java:322)- Did not find handler method for [/doc.html]
2019-04-19 13:39:36,921 DEBUG (AbstractUrlHandlerMapping.java:199)- Matching patterns for request [/doc.html] are [/**]
2019-04-19 13:39:36,922 DEBUG (AbstractUrlHandlerMapping.java:233)- URI Template variables for request [/doc.html] are {}
2019-04-19 13:39:36,923 DEBUG (AbstractUrlHandlerMapping.java:146)- Mapping [/doc.html] to HandlerExecutionChain with handler [ResourceHttpRequestHandler [locations=[class path resource [META-INF/resources/], class path resource [resources/], class path resource [static/], class path resource [public/], ServletContext resource [/]], resolvers=[org.springframework.web.servlet.resource.PathResourceResolver@da32f3]]] and 1 interceptor
2019-04-19 13:39:36,957 DEBUG (RequestContextFilter.java:104)- Cleared thread-bound request context: org.apache.catalina.connector.RequestFacade@28759ea7
2019-04-19 13:39:41,649 DEBUG (RequestContextFilter.java:114)- Bound request context to thread: org.apache.catalina.connector.RequestFacade@28759ea7
```



同理，在使用SpringMvc或者shiro等权限框架时，如果页面无法访问，配置doc.html属性即可

关于SpringBoot的代码示例可参考[swagger-bootstrap-ui-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/swagger-bootstrap-ui-demo)
 
 
 