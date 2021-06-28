# 1.3 Cloud模式


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
