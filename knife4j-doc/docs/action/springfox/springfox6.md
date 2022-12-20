# springfox 源码分析(六) web配置类扫描包作用探索


时间：2019-5-23 18:46:50

地点：家中



我们在上一篇中,知道了springfox一系列Plugin接口的实现、作用

而此时,我们联想到springfox为我们提供的Configuration配置类中使用了包路径扫描

先来看`OperationBuilderPlugin`的实现类之一`OperationDeprecatedReader`的代码

`OperationDeprecatedReader.java`

```java
package springfox.documentation.spring.web.readers.operation;

import com.google.common.base.Optional;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OperationDeprecatedReader implements OperationBuilderPlugin {
  @Override
  public void apply(OperationContext context) {
    Optional<Deprecated> annotation = context.findAnnotation(Deprecated.class);
    context.operationBuilder().deprecated(String.valueOf(annotation.isPresent()));
  }

  @Override
  public boolean supports(DocumentationType delimiter) {
    return true;
  }
}

```

这是针对operation中接口是否过时进行处理的实现类

实现类位于`springfox.documentation.spring.web.readers.operation`包下,并且通过`@Component`注解进行bean的实例注入

此时,我们回过头来看`SpringfoxWebMvcConfiguration`的源码，源码中配置了`springfox.documentation.spring.web.readers.operation`扫描路径

```java
@Configuration
@Import({ ModelsConfiguration.class })
@ComponentScan(basePackages = {
    "springfox.documentation.spring.web.scanners",
    "springfox.documentation.spring.web.readers.operation",
    "springfox.documentation.spring.web.readers.parameter",
    "springfox.documentation.spring.web.plugins",
    "springfox.documentation.spring.web.paths"
})
@EnablePluginRegistries({ DocumentationPlugin.class,
    ApiListingBuilderPlugin.class,
    OperationBuilderPlugin.class,
    ParameterBuilderPlugin.class,
    ExpandedParameterBuilderPlugin.class,
    ResourceGroupingStrategy.class,
    OperationModelsProviderPlugin.class,
    DefaultsProviderPlugin.class,
    PathDecorator.class,
    ApiListingScannerPlugin.class
})
public class SpringfoxWebMvcConfiguration {
    //more..
}
```

`@ComponentScan`注解此时配置了5个包路径，分别是：

- springfox.documentation.spring.web.scanners
- springfox.documentation.spring.web.readers.operation
- springfox.documentation.spring.web.readers.parameter
- springfox.documentation.spring.web.plugins
- springfox.documentation.spring.web.paths

源码看到这里,我们应该明白,包括Plugin的接口实现类,都会通过`@ComponentScan`配置的扫描包路径一并全部注入到Spring容器中

而我们只需要在我们的springfox其他代码中通过`@Autowired`依赖注入即可进行相应的实体bean使用