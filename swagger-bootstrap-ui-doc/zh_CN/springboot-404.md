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

同理，在使用SpringMvc或者shiro等权限框架时，如果页面无法访问，配置doc.html属性即可

关于SpringBoot的代码示例可参考[swagger-bootstrap-ui-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/swagger-bootstrap-ui-demo)