# Spring Cloud Gateway网关聚合

:::tip
1、Knife4j 在[v4.0](../../upgrading/upgrading-to-v4)版本提供了基于Spring Cloud Gateway网关聚合文档的中间件

2、在使用该组件时，如果开发者在网关层面做了鉴权等操作，需要把Ui资源以及相关的API接口放开，否则会出现无法访问的情况，放开资源清单可以[参考文档](../../features/accessControl)
:::

在4.0版本中,Knife4j提供了一个针对在Spring Cloud Gateway网关进行聚合的简单小组件，开发者可以基于此组件轻松的聚合各个子服务的OpenAPI文档

Maven坐标如下：

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-gateway-spring-boot-starter</artifactId>
    <version>4.0.0</version>
</dependency>


```


在application.yml配置文件中配置路由规则(该配置可以配置在Nacos配置中心中，实时生效)，配置规则如下：

```yml

knife4j:
  # 聚合swagger文档
  gateway:
    enable: true
    routes:
      - name: 用户服务
        url: /user-service/v2/api-docs?group=default
        service-name: user-service
        order: 2
      - name: 订单服务
        url: /order-service/v2/api-docs?group=default
        service-name: order-service
        order: 3
```


配置属性说明：

|属性|类型|说明|
|---|-----|----|
|knife4j.gateway.enable | Boolean | 是否开启使用Gateway网关聚合组件，默认false |
|knife4j.gateway.routes | List | 聚合Swagger分组集合，可以添加多个，无上限 |
|knife4j.gateway.routes[0].name | String | 界面显示分组名称 |
|knife4j.gateway.routes[0].url | String | 子服务的Swagger资源接口地址(Swagger2默认/v2/api-docs,只需要配置group参数即可)，因为是从网关层走，开发者配置时别忘记了网关前缀地址。 |
|knife4j.gateway.routes[0].service-name | String | 注册中心中的服务名称 |
|knife4j.gateway.routes[0].order | Int | 界面分组显示顺序，按正序(asc)排序 |