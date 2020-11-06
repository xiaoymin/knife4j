# 3.0 开放API


Knife4jCloud平台对外提供注册Swagger服务的开放API接口,通过该接口,非Java语言的开发者,可以进行独立开发,做到Swagger文档的项目自启动注入平台

## 3.1注册API

接口地址：`/knife4j/cloud/upload`

接口类型：`application/json`

接口方式：`POST`

接口参数：

```json
{
    "accessKey":"JDUkd1YvSi5zZmUkMHYuSGNmN1hMazJPajJuMjNJVW43dWNyL2tyR3N4bzJaa1A2ZC5mSUlwNA",
    "code":"APIFactory",
    "applicationHost":"192.168.0.152",
    "applicationPort":"9200",
    "ssl":false,
    "client":"",
    "cloudRoutes":[{
        "groupName":"订单服务",
        "content":"{....}",
        "path":"/aaa/v2/api-docs?group=订单服务"
    }]
}
```

参数说明：

- `accessKey`:该参数是注册API接口的认证凭证,每一个注册用户拥有自己独立的accessKey,平台注册成功后可以在右上角通过**个人信息**中获取
- `code`:项目编码,如果在平台中不存在,则注册不会成功,因此需要先在平台中添加项目
- `applicationHost`:当前应用服务的IP地址,该参数主要作用于Swagger调试
- `applicationPort`：当前应用服务的端口号
- `ssl`:默认false,如果是true,则代表当前服务是https
- `client`:配置一个应用服务的Client地址,一般是http://host:port,Knife4j会自动识别,如果开发者提供的是域名访问,防火墙屏蔽了端口号(例如：http://doc.xiaominfo.com),则开发者在上传的时候需要设置该属性,否则无法调试,该参数设置后则Host、Port不会生效,会根据该地址自动解析得到host和端口,所以两个属性配置其中一个即可.
- `cloudRoutes`:服务分组
  - `groupName`:服务名称
  - `content`:该内容是OpenAPIv2的JSON结构
  - `path`:提供访问得到OpenAPIv2的接口地址,在实际预览的时候,会通过该接口得到Swagger的JSON内容进行渲染

## 3.2 Spring Boot自动注册

如果你的项目是通过`Spring Boot`进行开发,并且不想通过`Knife4jCloud`提供的界面进行操作,并且已经集成了springfox-swagger组件,那么,你可以引用`Knife4jCloud`提供的自动注册的jar包组件进行自动注册

1.Maven引用

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-discovery-spring-boot-starter</artifactId>
    <!--在引用时请在maven中央仓库(http://search.maven.org)搜索最新版本号-->
    <!-- 该版本必须和Knife4jCloud主版本一致-->
    <version>1.0</version>
</dependency>
```

2、在`application.yml`或者`application.properties`配置文件中配置相关参数,以`yml`为例：

```yml
knife4j:
  cloud:
    ## 参考注册API中的accessKey
    accessKey: JDUkd1YvSi5zZmUkMHYuSGNmN1hMazJPajJuMjNJVW43dWNyL2tyR3N4bzJaa1A2ZC5mSUlwNA
    ## 项目编号
    code: APITest
    ## Knife4jCloud的对外域名地址
    server: http://127.0.0.1:19011
    ## 当前服务是否是HTTPS的,默认可以不配置,并且该参数默认为false
    ssl: false
    ## 参考注册API中的client属性,该参数可以不配置,只有在域名的情况下需要进行配置
    client: http://test.domain.com
    
```

3、在Spring Boot应用中通过注解`@EnableKnife4jCloudDiscovery`进行启用

```java
@EnableKnife4jCloudDiscovery
@SpringBootApplication
public class Knife4jSpringBootDemoApplication implements WebMvcConfigurer{
    //more..
}
```

 
 
 