# springfox 源码分析(四) 配置类初始化

时间：2019-5-23 12:46:50

地点：单位、家中


## @EnableSwagger2

有了二三章的理解,此时我们再来看`EnableSwagger2`注解的内容

```java
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import({Swagger2DocumentationConfiguration.class})
public @interface EnableSwagger2 {
}
```

## Swagger2DocumentationConfiguration

该注解没啥好说的,最终是导入`Swagger2DocumentationConfiguration`的配置类

```java
@Configuration
@Import({ SpringfoxWebMvcConfiguration.class, SwaggerCommonConfiguration.class })
@ComponentScan(basePackages = {
    "springfox.documentation.swagger2.mappers"
})
@ConditionalOnWebApplication
public class Swagger2DocumentationConfiguration {
```

此处的`@ComponentScan`注解,扫描了`springfox.documentation.swagger2.mappers`包路径

### Mappers

该包路径下包含了众多运用`MapStruct`组件自动生成的Mapper实体类转换关系,通过扫描注解,自动注入到Spring的容器中

关于`MapStruct`组件的使用,可参考:[springfox 源码分析(二) 初探mapstruct](https://www.xiaominfo.com/2019/05/22/springfox-2/)

主要包括如下：

- LicenseMapper
- ModelMapper
- ParameterMapper
- SecurityMapper
- SerivceModelToSwagger2Mapper
- VendorExtensionsMapper

每个Mapper接口都有一个实现类MapperImpl，实现类通过`@Component`注解注入到Spring的容器中

最重要的是`SerivceModelToSwagger2Mapper`这个Mapper

该类的作用会聚合使用Model、Parameter、License等Mapper,将springfox中的对象转化为Swagger标准的对象，包括Swagger

```java

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2018-06-23T17:02:57-0500",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_151 (Oracle Corporation)"
)
@Component
public class ServiceModelToSwagger2MapperImpl extends ServiceModelToSwagger2Mapper {
    
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ParameterMapper parameterMapper;
    @Autowired
    private SecurityMapper securityMapper;
    @Autowired
    private LicenseMapper licenseMapper;
    @Autowired
    private VendorExtensionsMapper vendorExtensionsMapper;

    @Override
    public Swagger mapDocumentation(Documentation from) {
        if ( from == null ) {
            return null;
        }

        Swagger swagger = new Swagger();

        swagger.setVendorExtensions( vendorExtensionsMapper.mapExtensions( from.getVendorExtensions() ) );
        swagger.setSchemes( mapSchemes( from.getSchemes() ) );
        swagger.setPaths( mapApiListings( from.getApiListings() ) );
        swagger.setHost( from.getHost() );
        swagger.setDefinitions( modelMapper.modelsFromApiListings( from.getApiListings() ) );
        swagger.setSecurityDefinitions( securityMapper.toSecuritySchemeDefinitions( from.getResourceListing() ) );
        ApiInfo info = fromResourceListingInfo( from );
        if ( info != null ) {
            swagger.setInfo( mapApiInfo( info ) );
        }
        swagger.setBasePath( from.getBasePath() );
        swagger.setTags( tagSetToTagList( from.getTags() ) );
        List<String> list2 = from.getConsumes();
        if ( list2 != null ) {
            swagger.setConsumes( new ArrayList<String>( list2 ) );
        }
        else {
            swagger.setConsumes( null );
        }
        List<String> list3 = from.getProduces();
        if ( list3 != null ) {
            swagger.setProduces( new ArrayList<String>( list3 ) );
        }
        else {
            swagger.setProduces( null );
        }

        return swagger;
    }
    //more...
}
```

各个Mapper组件的映射关系如下：

| Mapper        |             目标类                |                             |
| ------------- | --------------------------- | ------------- |
| LicenseMapper | `io.swagger.models.License` | 通过ApiInfo的属性Lincese构建目标类实体对象 |
| ModelMapper | `io.swagger.models.Model` | 将`springfox.documentation.schema.Model`转化成目标类 |
| ParameterMapper | `io.swagger.models.parameters.Parameter` | 将`springfox.documentation.service.Parameter`转化成目标类 |
| SecurityMapper | `io.swagger.models.auth.SecuritySchemeDefinition` |  |
| ServiceModelToSwagger2Mapper | `io.swagger.models.Swagger` | 输出Swagger完整对象 |
|  |  |  |

## SpringfoxWebMvcConfiguration

在`Swagger2DocumentationConfiguration`源码中,我们看到该Configuration类还引入了`SpringfoxWebMvcConfiguration`,该类是注入Spring Rest接口相关的配置核心类

先来看源码:

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

  @Bean
  public Defaults defaults() {
    return new Defaults();
  }

  @Bean
  public DocumentationCache resourceGroupCache() {
    return new DocumentationCache();
  }

  @Bean
  public static ObjectMapperConfigurer objectMapperConfigurer() {
    return new ObjectMapperConfigurer();
  }

  @Bean
  public JsonSerializer jsonSerializer(List<JacksonModuleRegistrar> moduleRegistrars) {
    return new JsonSerializer(moduleRegistrars);
  }

  @Bean
  public DescriptionResolver descriptionResolver(Environment environment) {
    return new DescriptionResolver(environment);
  }

  @Bean
  public HandlerMethodResolver methodResolver(TypeResolver resolver) {
    return new HandlerMethodResolver(resolver);
  }

}
```

从源码中我们可以看到：

- 使用`import`导入`ModelConfiguration`配置类,该类
- 使用`@ComponentScan`注解扫描配置的package包路径,完成Spring的Bean实例注入
- 使用`@EnablePluginRegistries`插件机制来完成插件的动态实例Bean注入到Spring容器中,关于Spring Plugin的使用,不明白的可以参考下上一篇文章对[Spring Plugin的说明](https://www.xiaominfo.com/2019/05/22/springfox-3/)
- 注入相关Bean的实例对象

### ModelsConfiguration

从webmvc配置类导入的Models配置类,我们来看该类的源码

```java
@Configuration
@ComponentScan(basePackages = {
    "springfox.documentation.schema"
})
@EnablePluginRegistries({
    ModelBuilderPlugin.class,
    ModelPropertyBuilderPlugin.class,
    TypeNameProviderPlugin.class,
    SyntheticModelProviderPlugin.class
})
public class ModelsConfiguration {
  @Bean
  public TypeResolver typeResolver() {
    return new TypeResolver();
  }
}
```

该类的配置和`SpringfoxWebMvcConfiguration`配置类相似,作用都是扫描包路径,启用PluginRetry进行Spring的实体Bean动态注入

## SwaggerCommonConfiguration

`Swagger2DocumenationConfiguration`导入的第二个配置类`SwaggerCommonConfiguration`

来看代码:

`SwaggerCommonConfiguration.java`

```java
@Configuration
@ComponentScan(basePackages = {
    "springfox.documentation.swagger.schema",
    "springfox.documentation.swagger.readers",
    "springfox.documentation.swagger.web"
})
public class SwaggerCommonConfiguration {

}
```

作用和以上类似

## 总结

通过`@EnableSwagger2`注解,我们看到了三个4个Configuration配置类的导入

主要作用：

- 实体Bean的注入
- Plugin插件的动态Bean注入
- 扫描springfox配置的各种package路径

看到这里相信我们还是一头雾水,我们并没有发现springfox何时初始化接口类的.

接下来,我们会针对上面Configuration涉及到的Plugin和`@CompnentScan`扫描package路径进行一一探索.