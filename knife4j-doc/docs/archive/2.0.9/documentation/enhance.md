# 3.1 增强模式

Knife4j自2.0.6版本开始,将目前在Ui界面中一些个性化配置剥离,开发者可以在后端进行配置，并且提供的knife4j-spring-boot-strater组件自动装载

spring.factories配置如下：
```properties
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.github.xiaoymin.knife4j.spring.configuration.Knife4jAutoConfiguration

```


在Spring Boot配置文件中,完整的配置如下:
```yml
knife4j:
  enable: true
  documents:
    -
      group: 2.X版本
      name: 接口签名
      locations: classpath:sign/*
  setting:
    language: zh-CN
    enableSwaggerModels: true
    enableDocumentManage: true
    swaggerModelName: 实体类列表
    enableVersion: false
    enableReloadCacheParameter: false
    enableAfterScript: true
    enableFilterMultipartApiMethodType: POST
    enableFilterMultipartApis: false
    enableRequestCache: true
    enableHost: false
    enableHostText: 192.168.0.193:8000
    enableHomeCustom: true
    homeCustomLocation: classpath:markdown/home.md
    enableSearch: false
    enableFooter: false
    enableFooterCustom: true
    footerCustomContent: Apache License 2.0 | Copyright  2019-[浙江八一菜刀股份有限公司](https://gitee.com/xiaoym/knife4j)
    enableDynamicParameter: false
    enableDebug: true
    enableOpenApi: false
    enableGroup: true
  cors: false
  production: false
  basic:
    enable: false
    username: test
    password: 12313
```

在以前的版本中,开发者需要在配置文件中手动使用`@EnableKnife4j`来使用增强，自2.0.6版本后,只需要在配置文件中配置`knife4j.enable=true`即可不在使用注解

**注意：要使用Knife4j提供的增强，`knife4j.enable=true`必须开启**

各个配置属性说明如下：
|属性|默认值|说明值|
|--|---|--|
|knife4j.enable|false|是否开启Knife4j增强模式|
|knife4j.cors|false|是否开启一个默认的跨域配置,该功能配合自定义Host使用|
|knife4j.production|false|是否开启生产环境保护策略,详情参考[文档](accessControl.md)|
|knife4j.basic||对Knife4j提供的资源提供BasicHttp校验,保护文档|
|knife4j.basic.enable|false|关闭BasicHttp功能|
|knife4j.basic.username||basic用户名|
|knife4j.basic.password||basic密码|
|knife4j.documents||自定义文档集合，该属性是数组|
|knife4j.documents.group||所属分组|
|knife4j.documents.name||类似于接口中的tag,对于自定义文档的分组|
|knife4j.documents.locations||markdown文件路径,可以是一个文件夹(`classpath:markdowns/*`)，也可以是单个文件(`classpath:md/sign.md`)|
|knife4j.setting||前端Ui的个性化配置属性|
|knife4j.setting.enableAfterScript|true| 调试Tab是否显示AfterScript功能,默认开启|
|knife4j.setting.language|zh-CN|Ui默认显示语言,目前主要有两种:中文(zh-CN)、英文(en-US)|
|knife4j.setting.enableSwaggerModels|true|是否显示界面中SwaggerModel功能|
|knife4j.setting.swaggerModelName|`Swagger Models`| 重命名SwaggerModel名称,默认|
|knife4j.setting.enableDocumentManage|true|是否显示界面中"文档管理"功能|
|knife4j.setting.enableReloadCacheParameter|false|是否在每个Debug调试栏后显示刷新变量按钮,默认不显示|
|knife4j.setting.enableVersion|false|是否开启界面中对某接口的版本控制,如果开启，后端变化后Ui界面会存在小蓝点|
|knife4j.setting.enableRequestCache|true|是否开启请求参数缓存|
|knife4j.setting.enableFilterMultipartApis|false| 针对RequestMapping的接口请求类型,在不指定参数类型的情况下,如果不过滤,默认会显示7个类型的接口地址参数,如果开启此配置,默认展示一个Post类型的接口地址|
|knife4j.setting.enableFilterMultipartApiMethodType|POST|具体接口的过滤类型|
|knife4j.setting.enableHost|false|是否启用Host|
|knife4j.setting.enableHomeCustom|false|是否开启自定义主页内容|
|knife4j.setting.homeCustomLocation||主页内容Markdown文件路径|
|knife4j.setting.enableSearch|false|是否禁用Ui界面中的搜索框|
|knife4j.setting.enableFooter|true|是否显示Footer|
|knife4j.setting.enableFooterCustom|false|是否开启自定义Footer|
|knife4j.setting.footerCustomContent|false|自定义Footer内容|
|knife4j.setting.enableDynamicParameter|false|是否开启动态参数调试功能|
|knife4j.setting.enableDebug|true|启用调试|
|knife4j.setting.enableOpenApi|true|显示OpenAPI规范|
|knife4j.setting.enableGroup|true|显示服务分组|

关于个性化文档(`knife4j.documents`)以及个性化设置(`knife4j.setting`),有一些细微的区别,开发者在配置文件中进行配合好后,还需要在创建Docket对象时调用`Knife4j`提供的扩展Extesions进行赋值

示例代码如下：

```java
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfiguration {

    /*引入Knife4j提供的扩展类*/
   private final OpenApiExtensionResolver openApiExtensionResolver;

    @Autowired
    public SwaggerConfiguration(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

@Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        String groupName="2.X版本";
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .host("https://www.baidu.com")
                .apiInfo(apiInfo())
                .groupName(groupName)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.swagger.bootstrap.ui.demo.new2"))
                .paths(PathSelectors.any())
                .build()
                //赋予插件体系
                .extensions(openApiExtensionResolver.buildExtensions(groupName));
        return docket;
    }
```

`buildExtensions`方法需要传入分组名称,该分组名称主要是为了区分开发者在构建自定义文档时，在不同的Docket逻辑分组下进行区别显示。

`OpenApiExtensionResolver`辅助类需要配置`knife4j.enable=true`才能自动`@Autowired`

增强效果开启后,在最终调用接口时，Knife4j会添加扩展属性`x-openapi`,如下图：

![](/knife4j/images/knife4j/enc.png)
 
 
 