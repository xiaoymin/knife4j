# SwaggerBootstrapUi

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.xiaoymin/swagger-bootstrap-ui/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.xiaoymin/swagger-bootstrap-ui)

## 项目说明

`swagger-bootstrap-ui`是`springfox-swagger`的增强UI实现，为Java开发者在使用Swagger的时候，能拥有一份简洁、强大的接口文档体验

**在 线 效 果：**[http://swagger-bootstrap-ui.xiaominfo.com/doc.html](http://swagger-bootstrap-ui.xiaominfo.com/doc.html)

**项 目 Demo:**[https://gitee.com/xiaoym/swagger-bootstrap-ui-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo)

**QQ 交流群：**<a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=16b81902c23fbca82780fa107da1b6612e2ee44a05c4103c9176ad9d61c2f6bf"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="XM开源技术交流群" title="XM开源技术交流群"></a> 608374991


**文档说明**:[http://www.xiaominfo.com/swagger-bootstrap-ui/](http://www.xiaominfo.com/swagger-bootstrap-ui/)

### 核心功能

该UI增强包主要包括两大核心功能：**文档说明** 和 **在线调试**

- **文档说明**：根据Swagger的规范说明，详细列出接口文档的说明，包括接口地址、类型、请求示例、请求参数、响应示例、响应参数、响应码等信息，使用swagger-bootstrap-ui能根据该文档说明，对该接口的使用情况一目了然。

- **在线调试**：提供在线接口联调的强大功能，自动解析当前接口参数,同时包含表单验证，调用参数可返回接口响应内容、headers、Curl请求命令实例、响应时间、响应状态码等信息，帮助开发者在线调试，而不必通过其他测试工具测试接口是否正确,简介、强大。

### UI增强

同时，swagger-bootstrap-ui在满足以上功能的同时，还提供了文档的增强功能，这些功能是官方swagger-ui所没有的，每一个增强的功能都是贴合实际,考虑到开发者的实际开发需要,是比不可少的功能，主要包括：

- **个性化配置**：通过个性化ui配置项，可自定义UI的相关显示信息

- **离线文档**：根据标准规范，生成的在线markdown离线文档，开发者可以进行拷贝生成markdown接口文档，通过其他第三方markdown转换工具转换成html或pdf，这样也可以放弃swagger2markdown组件

- **接口排序**：自1.8.5后，ui支持了接口排序功能，例如一个注册功能主要包含了多个步骤,可以根据swagger-bootstrap-ui提供的接口排序规则实现接口的排序，step化接口操作，方便其他开发者进行接口对接

## UI特点

- 以markdown形式展示文档,将文档的请求地址、类型、请求参数、示例、响应参数分层次依次展示,接口文档一目了然,方便开发者对接
- 在线调试栏除了自动解析参数外,针对必填项着颜色区分,同时支持tab键快速输入上下切换.调试时可自定义Content-Type请求头类型
- 个性化配置项,支持接口地址、接口description属性、UI增强等个性化配置功能
- 接口排序,支持分组及接口的排序功能
- 支持markdown文档离线文档导出,也可在线查看离线文档
- 调试信息全局缓存,页面刷新后依然存在,方便开发者调试
- 以更人性化的treetable组件展示Swagger Models功能
- 响应内容可全屏查看,针对响应内容很多的情况下，全屏查看，方便调试、复制
- 文档以多tab方式可显示多个接口文档
- 请求参数栏请求类型、是否必填着颜色区分
- 主页中粗略统计接口不同类型数量
- 支持接口在线搜索功能
- 左右菜单和内容页可自由拖动宽度
- 支持自定义全局参数功能，主页包括header及query两种类型
- i18n国际化支持,目前支持：中文简体、中文繁体、英文
- JSR-303 annotations 注解的支持

## 使用说明

### Maven中引入Jar包

由于是`springfox-swagger`的增强UI包,所以基础功能依然依赖Swagger,`springfox-swagge`r的jar包必须引入

```xml
<dependency>
 <groupId>io.springfox</groupId>
 <artifactId>springfox-swagger2</artifactId>
 <version>2.9.2</version>
</dependency>
```

然后引入SwaggerBootstrapUi的jar包

```xml
<dependency>
  <groupId>com.github.xiaoymin</groupId>
  <artifactId>swagger-bootstrap-ui</artifactId>
  <version>${lastVersion}</version>
</dependency>
```

### 编写Swagger2Config配置文件

Swagger2Config配置文件如下：

```java
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

 @Bean
 public Docket createRestApi() {
     return new Docket(DocumentationType.SWAGGER_2)
     .apiInfo(apiInfo())
     .select()
     .apis(RequestHandlerSelectors.basePackage("com.bycdao.cloud"))
     .paths(PathSelectors.any())
     .build();
 }

 private ApiInfo apiInfo() {
     return new ApiInfoBuilder()
     .title("swagger-bootstrap-ui RESTful APIs")
     .description("swagger-bootstrap-ui")
     .termsOfServiceUrl("http://localhost:8999/")
     .contact("developer@mail.com")
     .version("1.0")
     .build();
 }
}
```

### 访问地址

`swagger-bootstrap-ui`默认访问地址是：`http://${host}:${port}/doc.html`

### 注意事项

Springfox-swagger默认提供了两个Swagger接口,需要开发者放开权限(如果使用shiro权限控制框架等)，如果使用SwaggerBootstrapUi的增强功能,还需放开增强接口地址,所以，放开的权限接口包括3个，分别是：

- **/swagger-resources**:Swagger的分组接口
- **/v2/api-docs?group=groupName**:Swagger的具体分组实例接口,返回该分组下所有接口相关的Swagger信息
- **/v2/api-docs-ext?group=groupName**:该接口是SwaggerBootstrapUi提供的增强接口地址,如不使用UI增强,则可以忽略该接口

`Shiro`的相关配置实例如下：

```xml
<!---other settings-->
<property name="filterChainDefinitions">    
    <value>     
        /swagger-resources = anon
        /v2/api-docs = anon
        /v2/api-docs-ext = anon
        /doc.html = anon
        /webjars/** = anon
        
        //others....
    </value>    
</property>
```

SpringBoot中访问`doc.html`报404的解决办法

实现SpringBoot的`WebMvcConfigurer`接口，添加相关的`ResourceHandler`,代码如下：

```java
@SpringBootApplication
@ConditionalOnClass(SpringfoxWebMvcConfiguration.class)
public class SwaggerBootstrapUiDemoApplication  implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
```

使用SpringMvc的朋友.在web.xml中配置了`DispatcherServlet`,则需要追加一个url匹配规则,如下

```xml
<servlet>
   <servlet-name>cmsMvc</servlet-name>
   <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
   <init-param>
   <param-name>contextConfigLocation</param-name>
   <param-value>classpath:config/spring.xml</param-value>
   </init-param>
   <load-on-startup>1</load-on-startup>
</servlet>
<!--默认配置,.htm|.do|.json等等配置-->
<servlet-mapping>
	<servlet-name>cmsMvc</servlet-name>
 	<url-pattern>*.htm</url-pattern>
</servlet-mapping>
<!-- 配置swagger-bootstrap-ui的url请求路径-->
<servlet-mapping>
   <servlet-name>cmsMvc</servlet-name>
   <url-pattern>/v2/api-docs</url-pattern>
</servlet-mapping>
<servlet-mapping>
   <servlet-name>cmsMvc</servlet-name>
   <url-pattern>/swagger-resources</url-pattern>
</servlet-mapping>
<servlet-mapping>
   <servlet-name>cmsMvc</servlet-name>
   <url-pattern>/v2/api-docs-ext</url-pattern>
</servlet-mapping>

```

## UI效果图

![接口说明](../static/des.png)

![接口调试](../static/debug.png)

![个性化设置](../static/settings.png)

![接口离线文档](../static/markdown.png)

![SwaggerModels](../static/models.png)




