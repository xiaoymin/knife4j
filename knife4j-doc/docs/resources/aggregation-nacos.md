# 1.5 Nacos模式

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

