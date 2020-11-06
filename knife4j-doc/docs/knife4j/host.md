# 自定义Host

在Knife4j的2.0.4版本新增该功能,新增Host的个性化配置也是方便开发或调试人员在Swagger文档部署后，针对不同的网络环境,可以通过配置该属性,方便的进行调试.

## 前置条件

要在Knife4j使用此属性,服务端必须开启跨域配置，如果你的工程是Spring Boot,示例代码如下：

```java
@Bean
public CorsFilter corsFilter(){
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration corsConfiguration=new CorsConfiguration();
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.addAllowedOrigin("*");
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.setMaxAge(10000L);
    source.registerCorsConfiguration("/**",corsConfiguration);
    CorsFilter corsFilter=new CorsFilter(source);
    return corsFilter;
}
```

以上代码在Knife4j中已经内置,如果开发者嫌弃每个项目都需要配置很麻烦,可以通过在`application.yml`配置文件通过配置进行开启使用,配置如下：

```yml
knife4j:
	cors: true # 开启服务端Cors配置
```

配置后,在Swagger的配置代码中必须使用`@EnableKnife4j`注解

一般Spring Boot的单体架构可以使用此方式进行开启Cors的跨域配置,但是如果你的服务是走网关的形式,那么必须在网关层进行开启，网关层的形式会有所不同

以`Spring Cloud Gateway`为例,该网关底层是基于Netty,并非是Servlet架构,所以需要使用网关自带的`WebFilter`进行开启，否则在前端到请求服务时,数据可以流入,但是在网关层时会无法响应数据,出现`Access-Control-Allow-Origin`等错误

## 配置形式

Host的配置可以有多种形式，举例如下：

1、ip+port的形式，例如`192.169.0.111:8080`

2、HTTP/HTTPS+ip+port的形式，例如：`http://192.168.0.111:8080`或者`https://192.168.0.111:8080`

3、域名的方式，例如：`knife4j.xiaominfo.com`

4、HTTP/HTTPS+域名,例如：`http://knife4j.xiaominfo.com`或者`https://knife4j.xiaominfo.com`

5、除了配置域名或者ip地址外,在Host后还可以跟随`basePath`,例如：`http://192.168.0.111:8080/v1`等

如果当前配置的Host不包含`Protocol`,默认为`HTTP`

## 原理

使用该`Host`后,Knife4j会在发送接口调试时校验是否启用,其原理是在通过调用`axios`组件时,配置其`baseURL`属性

伪代码如下：

```javascript
var baseUrl='';//默认是空
//是否启用Host
if(this.enableHost){
    baseUrl=this.enableHostText;
}
var requestConfig={
    baseURL:baseUrl,//调用目标Host服务的接口
    url: url,
    method: methodType,
    headers: headers,
    params: formParams,
    data: data,
    //Cookie标志
    withCredentials:this.debugSendHasCookie(headers),
    timeout: 0
}
```


 
 
 