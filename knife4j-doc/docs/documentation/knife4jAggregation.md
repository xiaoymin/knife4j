# 4.2 Knife4jAggregation微服务聚合中间件

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

接下来将详细介绍该组件的说明以及不同的聚合方式介绍
[[toc]]

## 4.2.1 组件属性说明

既然基于Spring Boot的starter组件发布，那么必然提供了很多属性，完整的属性如下：

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

属性分为5个方面：

- 开启Knife4jAggregation组件
- 开启Disk模式
- 开启Cloud模式
- 开启Eureka模式
- 开启Nacos模式

::: danger 注意
Disk、Cloud、Eureka、Nacos这四种模式只能使用1种，不能混合一起使用(即只能配置这4中模式中的一种属性，然后将其`enable`属性设置为`true`,其他三种的enable则必须设置为false)
:::

开启聚合组件

- `knife4j.enableAggregation`:该属性是前提条件，如果要启用聚合，那么该属性必须设置为true

## 4.2.2 Disk模式

更加详细的实战demo请参考[基于Disk模式聚合OpenAPI](../action/aggregation-disk.md)

> Disk模式代表的是本地模式，开发者本地存在一个或多个OpenAPI规范的JSON文件，此时可以通过Knife4j的聚合组件将该OpenAPI规范解析并渲染

```yml
knife4j:
  enableAggregation: true
  disk:
    enable: true
    routes:
      - name: 用户
        location: classpath:openapi/user.json
```

- `knife4j.disk.enable`:将该属性设置为true，则代表启用Disk模式
- `knife4j.disk.routes`:需要聚合的服务集合，可以配置多个
- `knife4j.disk.routes.name`:服务名称(显示名称，最终在Ui的左上角下拉框进行显示)
- `knife4j.disk.routes.location`:本地Disk模式聚合的OpenAPI规范JSON文件,可以是V2也可以是V3版本

## 4.2.3 Cloud模式

更加详细的实战demo请参考[基于Cloud模式聚合OpenAPI](../action/aggregation-cloud.md)

> 取名Cloud模式代表的是开发者的OpenAPI规范是以HTTP接口的形式存在，开发者可以配置通过调用HTTP接口来获取OpenAPI规范
>
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
          username: test3
          password: 66666
    routeAuth:
      enable: true
      username: test
      password: 12313
```
- `knife4j.cloud.enable`:将该属性设置为true，则代表启用Cloud模式
- `knife4j.cloud.routeAuth`:该属性是一个公共Basic验证属性(可选)，如果开发者提供的OpenAPI规范的HTTP接口需要以Basic验证进行鉴权访问，那么可以配置该属性，如果配置该属性，则该模式下所有配置的Routes节点接口都会以Basic验证信息访问接口
- `knife4j.cloud.routeAuth.enable`:是否启用Basic验证
- `knife4j.cloud.routeAuth.usernae`:Basic用户名
- `knife4j.cloud.routeAuth.password`:Basic密码
- `knife4j.cloud.routes`:需要聚合的服务集合(必选)，可以配置多个
- `knife4j.cloud.routes.name`:服务名称(显示名称，最终在Ui的左上角下拉框进行显示)
- `knife4j.cloud.routes.uri`:该服务的接口URI资源，如果是HTTPS，则需要完整配置
- `knife4j.cloud.routes.location:`:具体资源接口地址，最终Knife4j是通过uri+location的组合路径进行访问
- `knife4j.cloud.routes.swaggerVersion`:版本号，默认是`2.0`，可选配置
- `knife4j.cloud.routes.servicePath`:该属性是最终在Ui中展示的接口前缀属性，提供该属性的目的也是因为通常开发者在以Gateway等方式聚合时，需要一个前缀路径来进行转发，而最终这个前缀路径会在每个接口中进行追加
- `knife4j.cloud.routes.routeAuth`:如果该Route节点的接口开启了Basic，并且和公共配置的Basic不一样，需要单独配置
- `knife4j.cloud.routes.routeAuth.enable`:是否启用Basic验证
- `knife4j.cloud.routes.routeAuth.usernae`:Basic用户名
- `knife4j.cloud.routes.routeAuth.password`:Basic密码


## 4.2.4 Eureka模式

更加详细的实战demo请参考[基于Eureka注册中心聚合OpenAPI](../action/aggregation-eureka.md)

> 开发者可以从Eureka注册中心中聚合已经注册的服务，需要注意的是以及注册的服务必须集成OpenAPI并且能提供接口
> 该模式类似于Cloud模式，只是隐藏了服务的地址而已


```yml
knife4j:
  enableAggregation: true
  eureka:
    enable: false
    serviceUrl: http://localhost:10000/eureka/
    serviceAuth:
      enable: false
      username: test
      password: 12313
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
```

- `knife4j.eureka.enable`:将该属性设置为true，则代表启用Eureka模式
- `knife4j.eureka.serviceUrl`:Eureka注册中心的地址
- `knife4j.eureka.serviceAuth`:该属性是一个公共Basic验证属性(可选)，如果Eureka的注册和获取服务需要进行Basic认证，开发者需要配置该属性
- `knife4j.eureka.serviceAuth.enable`:是否启用Eureka注册中心Basic验证
- `knife4j.eureka.serviceAuth.usernae`:Eureka注册中心Basic用户名
- `knife4j.eureka.serviceAuth.password`:Eureka注册中心Basic密码
- `knife4j.eureka.routeAuth`:该属性是一个公共Basic验证属性(可选)，如果开发者提供的OpenAPI规范的服务需要以Basic验证进行鉴权访问，那么可以配置该属性，如果配置该属性，则该模式下所有配置的Routes节点接口都会以Basic验证信息访问接口
- `knife4j.eureka.routeAuth.enable`:是否启用Basic验证
- `knife4j.eureka.routeAuth.usernae`:Basic用户名
- `knife4j.eureka.routeAuth.password`:Basic密码
- `knife4j.eureka.routes`:需要聚合的服务集合(必选)，可以配置多个
- `knife4j.eureka.routes.name`:服务名称(显示名称，最终在Ui的左上角下拉框进行显示)，如果该属性不配置，最终Ui会显示`serviceName`
- `knife4j.eureka.routes.serviceName`:Eureka注册中心的服务名称
- `knife4j.eureka.routes.uri`:该服务的接口URI资源，如果是HTTPS，则需要完整配置
- `knife4j.eureka.routes.location:`:具体资源接口地址，最终Knife4j是通过注册服务uri+location的组合路径进行访问
- `knife4j.eureka.routes.swaggerVersion`:版本号，默认是`2.0`，可选配置
- `knife4j.eureka.routes.servicePath`:该属性是最终在Ui中展示的接口前缀属性，提供该属性的目的也是因为通常开发者在以Gateway等方式聚合时，需要一个前缀路径来进行转发，而最终这个前缀路径会在每个接口中进行追加
- `knife4j.eureka.routes.routeAuth`:如果该Route节点的接口开启了Basic，并且和公共配置的Basic不一样，需要单独配置
- `knife4j.eureka.routes.routeAuth.enable`:是否启用Basic验证
- `knife4j.eureka.routes.routeAuth.usernae`:Basic用户名
- `knife4j.eureka.routes.routeAuth.password`:Basic密码


## 4.2.4 Nacos模式

更加详细的实战demo请参考[基于Nacos注册中心聚合OpenAPI](../action/aggregation-nacos.md)

> 开发者可以从Nacos注册中心中聚合已经注册的服务，需要注意的是以及注册的服务必须集成OpenAPI并且能提供接口
> 该模式类似于Cloud模式，只是隐藏了服务的地址而已

```yml
knife4j:
  enableAggregation: true
  nacos:
    enable: true
    serviceUrl: http://192.168.0.112:8804/nacos/
    routeAuth:
      enable: true
      username: test
      password: 12313
    routes:
      - name: 订单服务
        serviceName: service-order
        groupName: test
        namespaceId: test
        clusters: test
        location: /v2/api-docs?group=default
        swaggerVersion: 2.0
        servicePath: /order
        routeAuth:
          enable: true
          username: test
          password: 12313
```

- `knife4j.nacos.enable`:将该属性设置为true，则代表启用nacos模式
- `knife4j.nacos.serviceUrl`:nacos注册中心的地址
- `knife4j.nacos.routeAuth`:该属性是一个公共Basic验证属性(可选)，如果开发者提供的OpenAPI规范的服务需要以Basic验证进行鉴权访问，那么可以配置该属性，如果配置该属性，则该模式下所有配置的Routes节点接口都会以Basic验证信息访问接口
- `knife4j.nacos.routeAuth.enable`:是否启用Basic验证
- `knife4j.nacos.routeAuth.usernae`:Basic用户名
- `knife4j.nacos.routeAuth.password`:Basic密码
- `knife4j.nacos.routes`:需要聚合的服务集合(必选)，可以配置多个
- `knife4j.nacos.routes.name`:服务名称(显示名称，最终在Ui的左上角下拉框进行显示)，如果该属性不配置，最终Ui会显示`serviceName`
- `knife4j.nacos.routes.serviceName`:nacos注册中心的服务名称
- `knife4j.nacos.routes.groupName`:Nacos分组名称,非必须,开发者根据自己的实际情况进行配置
- `knife4j.nacos.routes.namespaceId`:命名空间id,非必须,开发者根据自己的实际情况进行配置
- `knife4j.nacos.routes.clusters`:集群名称,多个集群用逗号分隔,非必须,开发者根据自己的实际情况进行配置
- `knife4j.nacos.routes.uri`:该服务的接口URI资源，如果是HTTPS，则需要完整配置
- `knife4j.nacos.routes.location:`:具体资源接口地址，最终Knife4j是通过注册服务uri+location的组合路径进行访问
- `knife4j.nacos.routes.swaggerVersion`:版本号，默认是`2.0`，可选配置
- `knife4j.nacos.routes.servicePath`:该属性是最终在Ui中展示的接口前缀属性，提供该属性的目的也是因为通常开发者在以Gateway等方式聚合时，需要一个前缀路径来进行转发，而最终这个前缀路径会在每个接口中进行追加
- `knife4j.nacos.routes.routeAuth`:如果该Route节点的接口开启了Basic，并且和公共配置的Basic不一样，需要单独配置
- `knife4j.nacos.routes.routeAuth.enable`:是否启用Basic验证
- `knife4j.nacos.routes.routeAuth.usernae`:Basic用户名
- `knife4j.nacos.routes.routeAuth.password`:Basic密码
