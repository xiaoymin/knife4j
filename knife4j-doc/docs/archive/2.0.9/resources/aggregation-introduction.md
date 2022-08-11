# 1.1 介绍

::: danger 温馨提示
1、Knife4jAggregation是基于Servlet体系的Filter拦截技术实现的聚合与HTTP请求转发服务,既能预览也能独立调试

2、不能和Spring Cloud Gateway混合使用(因为Gateway底层是基于Netty构建的)

3、Knife4jAggregation是给Spring Boot的web系统赋能,拥有聚合OpenAPI文档的能力
:::

Knife4j一直致力于将目前的Ui提供给更多的平台或者别的语言使用而努力，经过这么长时间的发展，Knife4j提供的轻量级聚合中间件终于诞生了，自`2.0.8`版本开始，Knife4j
提供了`knife4j-aggregation-spring-boot-starter`组件，该组件是一个基于Spring Boot系统的starter，他提供了以下几种能力：

- 最轻量级、最简单、最方便的聚合OpenApi规范的中间件
- 让所有的基于Spring Boot的Web体系拥有了轻松聚合OpenApi的能力
- 提供4种模式供开发者选择
    - 基于本地静态JSON文件的方式聚合OpenAPI
    - 基于云端HTTP接口的方式聚合
    - 基于Eureka注册中心的方式聚合
    - 基于Nacos注册中心的方式聚合
- 基于该starter发布了Docker镜像，跨平台与语言让开发者基于此Docker镜像轻松进行聚合OpenAPI规范 
- 完美兼容所有Spring Boot版本，没有兼容性问题
- 开发者可以彻底放弃基于Zuul、Spring Cloud Gateway等复杂的聚合方式
- 兼容OpenAPI2规范以及OpenAPI3规范

基于Spring Boot引入方式
```xml
 <dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-aggregation-spring-boot-starter</artifactId>
    <!--在引用时请在maven中央仓库搜索aggregation最新版本号-->
    <version>2.0.8</version>
</dependency>
```

目前Knife4jAggregation主要提供了四种方式进行OpenAPI文档的聚合，主要包括：

- 基于OpenAPI的静态JSON文件方式,[Disk模式](aggregation-disk.md)
- 基于HTTP接口的方式获取OpenAPI,[Cloud模式](aggregation-cloud.md)
- 基于Eureka注册中心获取OpenAPI,[Eureka模式](aggregation-eureka.md)
- 基于Nacos注册中心获取OpenAPI,[Nacos模式](aggregation-nacos.md)


::: danger 注意
Disk、Cloud、Eureka、Nacos这四种模式只能使用其中1种，不能混合一起使用(即只能配置这4中模式中的一种属性，然后将其`enable`属性设置为`true`,其他三种的enable则必须设置为false)

如果你有混合使用的需求,你应该考虑[Knife4jAggregationDesktop](desktop-introduction.md)
:::


在Spring Boot框架中，使用Knife4jAggregation组件，需要在yml或者properties配置文件中进行相关的配置，完整的配置如下：


```yml
knife4j:
  enableAggregation: true
  cloud:
    enable: true
    routes:
      - name: 用户体系
        uri: 192.168.0.152:8999
        location: /v2/api-docs?group=2.X版本
        swaggerVersion: 2.0
        servicePath: /abbb/ffe
        routeAuth:
          enable: true
          username: test
          password: 12313
    routeAuth:
      enable: true
      username: test
      password: 12313
  eureka:
    enable: false
    serviceUrl: http://localhost:10000/eureka/
    routeAuth:
      enable: true
      username: test
      password: 12313
    routes:
      - name: 订单服务
        serviceName: service-order
        location: /v2/api-docs?group=default
        swaggerVersion: 2.0
        servicePath: /order
        routeAuth:
          enable: true
          username: test
          password: 12313
  nacos:
    enable: false
    serviceUrl: http://localhost:10000/nacos/
    routeAuth:
      enable: true
      username: test
      password: 12313
    routes:
      - name: 订单服务
        serviceName: service-order
        location: /v2/api-docs?group=default
        swaggerVersion: 2.0
        servicePath: /order
        routeAuth:
          enable: true
          username: test
          password: 12313
  disk:
    enable: false
    routes:
      - name: 用户
        location: classpath:openapi/user.json
```
