By default, you do not need to add this configuration to access

Many friends can't access the doc.html interface after using SpringBoot integration swagger-bootstrap-ui. At this point, you may need to implement SpringBoot's WebMvcConfigurer interface, add related ResourceHandler, the code is as follows：

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

Similarly, when using a permission framework such as SpringMvc or shiro, if the page is inaccessible, configure the doc.html property.

For a code example of SpringBoot, see [swagger-bootstrap-ui-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/swagger-bootstrap-ui-demo)