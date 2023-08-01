# 针对4.1.0版本接口响应Base64编码的情况

Knife4j 在4.1.0版本升级了springdoc的版本号，导致很多开发者反馈会出现初始化OpenAPI接口响应的是Base64编码的情况.导致在Knife4j 界面初始化接口失败。


这种情况主要是开发者在Spring Boot项目中，自定义了`MessageConvert`的情况，示例代码如下：

```javascript
@Configuration
public class CommonWebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 自定义convert
		converters.add(fastJsonHttpMessageConverters());
		//需要追加byte，否则springdoc-openapi接口会响应Base64编码内容，导致接口文档显示失败
		// https://github.com/springdoc/springdoc-openapi/issues/2143
        // 解决方案
		converters.add(new ByteArrayHttpMessageConverter());
    }

}

```

开发者需要添加`ByteArrayHttpMessageConverter`,具体可以参考springdoc-openapi的issues：

[https://github.com/springdoc/springdoc-openapi/issues/2143](https://github.com/springdoc/springdoc-openapi/issues/2143)