# Spring Cloud Gateway网关聚合

## 简介

:::tip

1、Knife4j 在[v4.0](../../upgrading/upgrading-to-v4)版本提供了基于Spring Cloud Gateway网关聚合文档的中间件

2、在使用该组件时，如果开发者在网关层面做了鉴权等操作，需要把Ui资源以及相关的API接口放开，否则会出现无法访问的情况，放开资源清单可以[参考文档](../../features/accessControl)

3、实战文章请参考：[Spring Cloud Gateway网关下的文档聚合?就用它了](/docs/blog/gateway/knife4j-gateway-introduce)
:::

自4.0版本后,Knife4j提供了一个针对在Spring Cloud Gateway网关进行聚合的组件，开发者可以基于此组件轻松的聚合各个子服务的OpenAPI文档

值得注意的事项：

- 生产环境上线时，配置文件通过配置`knife4j.gateway.enabled: false`进行关闭,避免接口泄漏，造成安全问题
- 支持服务发现`knife4j.gateway.discover.enabled: true`,该属性[4.1.0](https://gitee.com/xiaoym/knife4j/milestones/181381)后新增
- 服务发现中注意排除网关服务

## 使用

Maven坐标如下：

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-gateway-spring-boot-starter</artifactId>
    <version>4.4.0</version>
</dependency>
```


### 手动配置聚合(manual)

手动配置聚合，顾名思义，开发者只需要在Spring Cloud Gateway网关的yml配置文件中进行配置即可。

在`application.yml`配置文件中配置路由规则(该配置可以配置在Nacos配置中心中，实时生效)，配置规则如下：

```yml

knife4j:
  # 聚合swagger文档
  gateway:
    enabled: true
    # 排序规则(tag/operation排序自4.2.0版本新增)
    # 取值：alpha-默认排序规则，官方swagger-ui默认实现,order-Knife4j提供的增强排序规则，开发者可扩展x-order，根据数值来自定义排序
    tags-sorter: order
    operations-sorter: order
     # 指定手动配置的模式(默认为该模式)
    strategy: manual
    routes:
      - name: 用户服务
        # 真实子服务访问url地址-提供OpenAPI的文档
        url: /user-service/v2/api-docs?group=default
        service-name: user-service
        # 路由前缀
        # 兼容OpenAPI3规范在聚合时丢失contextPath属性的异常情况，由开发者自己配置contextPath,Knife4j的前端Ui做兼容处理,与url属性独立不冲突，仅OpenAPI3规范聚合需要，OpenAPI2规范不需要设置此属性,默认为(apiPathPrefix)
        context-path: /
        order: 2
      - name: 订单服务
        url: /order-service/v2/api-docs?group=default
        service-name: order-service
        # 路由前缀
        context-path: /
        order: 3

```

配置属性说明：


| 配置属性名称                             | 类型          | 描述                                                                     | 默认值                                                                                                                              |
| :--------------------------------------- | ------------- | ------------------------------------------------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------- |
| `knife4j.gateway.enabled`                | boolean       | 是否开启使用Gateway网关聚合组件                                          | `false`                                                                                                                             |
| `knife4j.gateway.strategy`               | enum          | 聚合的策略，主要支持两种，分别是手动配置(`manual`)、服务发现(`discover`) | `manual`                                                                                                                            |
| `knife4j.gateway.routes`                 | array{Router} | 通过路由注册文档                                                         |                                                                                                                                     |
| `knife4j.gateway.routes[0].name`         | string        | 界面显示分组名称                                                         | `null`                                                                                                                              |
| `knife4j.gateway.routes[0].url`          | string        | 文档地址                                                                 | 子服务的Swagger资源接口地址(Swagger2默认/v2/api-docs,只需要配置group参数即可)，因为是从网关层走，开发者配置时别忘记了网关前缀地址。 |
| `knife4j.gateway.routes[0].service-name` | string        | 访问服务名称                                                             | `null`                                                                                                                              |
| `knife4j.gateway.routes[0].order`        | int           | 排序                                                                     | 0                                                                                                                                   |
| `knife4j.gateway.routes[0].context-path` | string        | 路由前缀,根据实际情况自行配置                                            | /                                                                                                                                   |


### 服务发现模式(discover)

如果子服务非常多的情况下，那么手动配置就会显得很累，那么可以通过服务发现的模式，自动从注册中心聚合文档，不过服务发现的模式，有一些注意事项，开发者需要注意：

:::danger 注意事项

- 子服务必须统一规范实现，统一使用OpenAPI2或者OpenAPI3的版本
- 子服务需要提供默认`default`分组，该分组是服务发现模式下自动聚合的url

:::


#### 配置属性

在服务发现模式下的配置属性如下：

```yml
knife4j:
  gateway:
    # 是否开启
    enabled: true
    # 排序规则(tag/operation排序自4.2.0版本新增)
    # 取值：alpha-默认排序规则，官方swagger-ui默认实现,order-Knife4j提供的增强排序规则，开发者可扩展x-order，根据数值来自定义排序
    tags-sorter: order
    operations-sorter: order
    # 指定服务发现的模式聚合微服务文档，并且是默认`default`分组
    strategy: discover
    # 子服务存在其他分组情况，聚合其他分组，只能手动配置
    routes:
      - name: 用户服务-1
        # 子服务存在其他分组情况，聚合其他分组
        url: /user-service/v2/api-docs?group=用户服务
        # 服务名称(Optional)
        service-name: user-service
        # 路由前缀
        context-path: /
        # 排序
        order: 2
      - name: 订单服务-2
        url: /order-service/v2/api-docs?group=订单服务
        service-name: order-service
        # 路由前缀
        context-path: /
        order: 3
    # 服务发现模式的配置
    discover:
        # 开启
      enabled: true
        # 指定版本号(swagger2|openapi3)
        version : openapi3
        # 需要排除的微服务(eg:网关服务) 
        excluded-services:
            - gateway-service
        # 如果子服务是OpenAPI3，并且有个性化配置
        oas3:
          url: /v3/api-docs?group=default
          oauth2-redirect-url: ''
          validator-url: ''
        # 如何子服务是Swagger2，并且个性化配置
        swagger2:
          url: /v2/api-docs?group=default
        # 单个服务的个性化配置，key-服务名称，value-配置信息
        service-config:
          # 假设order服务(具体真实服务开发者根据自己的情况配置)
          order-service:
            # 该服务的排序
            order: 0
            # 分组显示名称
            group-name: 订单服务名称
            # 兼容OpenAPI3规范在聚合时丢失contextPath属性的异常情况，由开发者自己配置contextPath,Knife4j的前端Ui做兼容处理,与url属性独立不冲突，仅OpenAPI3规范聚合需要，OpenAPI2规范不需要设置此属性,默认为(apiPathPrefix)
            context-path: /
            # 该属性自4.2.0添加，支持子服务非`default`分组的其他分组聚合
            # 参考 https://gitee.com/xiaoym/knife4j/pulls/87
            group-names:
              - 分组1
              - 分组2


```

#### 排除服务

自4.2.0版本，在Spring Cloud Gateway网关聚合时，开发者可自定义排除服务的规则，实现`Knife4j`开放的接口即可
> 主要解决在Dubbo等服务的场景中聚合了不必要的服务。参考[Gitee#I6YLMB](https://gitee.com/xiaoym/knife4j/issues/I6YLMB)


```javascript
@Slf4j
@Component
public class MyExcludeService implements GatewayServiceExcludeService {
    @Override
    public Set<String> exclude(Environment environment, Knife4jGatewayProperties properties, List<String> services) {
        log.info("自定义过滤器.");
        if (!CollectionUtils.isEmpty(services)){
						// 排除注册中心包含order字眼的服务
            return services.stream().filter(s -> s.contains("order")).collect(Collectors.toSet());
        }
        return new TreeSet<>();
    }
}
```

#### 场景case


2.1 所有子服务全部是OpenAPI3规范
```yml
knife4j:
  gateway:
    enabled: true
    # 指定服务发现的模式聚合微服务文档，并且是默认`default`分组
    strategy: discover
    discover:
      enabled: true
      # 指定版本号(Swagger2|OpenAPI3)
      version : openapi3
      # 需要排除的微服务(eg:网关服务)
      excluded-services:
        - gateway-service

 ```
2.2 所有子服务全部是Swagger2规范，并且是默认`default`分组
```yml
knife4j:
  gateway:
    enabled: true
    # 指定服务发现的模式聚合微服务文档
    strategy: discover
    discover:
      enabled: true
      # 指定版本号(Swagger2|OpenAPI3)
      version : swagger2
      # 需要排除的微服务(eg:网关服务)
      excluded-services:
        - gateway-service
```         

2.3     子服务中除了`default`分组，还有别的分组，测试，我们需要单独聚合(因为discover模式只聚合默认)，此时，则复用`routes`自定义属性
```yml
knife4j:
  gateway:
    enabled: true
    # 指定服务发现的模式聚合微服务文档
    strategy: discover
    discover:
      enabled: true
      # 指定版本号(Swagger2|OpenAPI3)
      version : swagger2
      # 需要排除的微服务(eg:网关服务)
      excluded-services:
        - gateway-service
      # 个性化定制的部分子服务分组情况        
      routes:
        - name: 用户服务
          service-name: user-service
          url: /user/v2/api-docs?group=组织管理
          order: 1
        - name: 订单服务
          service-name: order-service
          url: /order/v2/api-docs?group=订单管理
          order: 1
 ```
2.4 在discover服务发现模式下，如果我们希望对聚合起来的微服务提供一些个性化配置，例如：排序、分组重命名、context-path配置等等，那么可以通过服务配置对每个服务进行配置，如下：
```yml
knife4j:
  gateway:
    enabled: true
    # 指定服务发现的模式聚合微服务文档
    strategy: discover
    discover:
      enabled: true
      # 指定版本号(Swagger2|OpenAPI3)
      version : openapi3
      # 需要排除的微服务(eg:网关服务)
      excluded-services:
        - gateway-service
      # 各个聚合服务的个性化配置，key:注册中心中的服务名称，value：个性化配置
      service-config:
        user-service:
          # 排序
          order: 1
          # 前端显示名称
          group-name : 用户服务
          # 重新指定basePath，一般在OpenAPI3规范中需要
          context-path: /user
        order-service:
          # 排序
          order: 2
          # 前端显示名称
          group-name : 订单服务
          # 重新指定basePath，一般在OpenAPI3规范中需要
          context-path: /order
 ```


#### 访问地址

http://{gateway.host}:{gateway.port}/doc.html
