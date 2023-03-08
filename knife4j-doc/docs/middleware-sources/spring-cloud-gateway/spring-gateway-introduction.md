# Spring Cloud Gateway网关聚合

## 简介

:::tip

1、Knife4j 在[v4.0](../../upgrading/upgrading-to-v4)版本提供了基于Spring Cloud Gateway网关聚合文档的中间件

2、在使用该组件时，如果开发者在网关层面做了鉴权等操作，需要把Ui资源以及相关的API接口放开，否则会出现无法访问的情况，放开资源清单可以[参考文档](../../features/accessControl)
:::

自4.0版本后,Knife4j提供了一个针对在Spring Cloud Gateway网关进行聚合的组件，开发者可以基于此组件轻松的聚合各个子服务的OpenAPI文档

值得注意的事项：

- 生产环境上线时，配置文件通过配置`knife4j.gateway.enabled: false`进行关闭,避免接口泄漏，造成安全问题
- 支持服务发现`knife4j.gateway.discover.enabled: true`,该属性[4.1.0](https://gitee.com/xiaoym/knife4j/milestones/181381)后新增
- 服务发现中注意排除网关服务

## 使用

### 4.0.0 版本

Maven坐标如下：

```xml   title="Spring Boot < 3.0.0-M1 "
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


| 配置属性名称                                       | 类型            | 描述                                                                            | 默认值                                                                               |
|:---------------------------------------------|---------------|-------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| `knife4j.gateway.enabled`                    | boolean       | 是否开启使用Gateway网关聚合组件                                                           | `false`                                                                           |
| `knife4j.gateway.routes`                     | array{Router} | 通过路由注册文档                                                                      |                                                                                   |
| `knife4j.gateway.routes[0].name`             | string        | 界面显示分组名称                                                                      | `null`                                                                            |
| `knife4j.gateway.routes[0].url`              | string        | 文档地址                                                                          | 子服务的Swagger资源接口地址(Swagger2默认/v2/api-docs,只需要配置group参数即可)，因为是从网关层走，开发者配置时别忘记了网关前缀地址。 |
| `knife4j.gateway.routes[0].service-name`     | string        | 访问服务名称                                                                        | `null`                                                                            |
| `knife4j.gateway.routes[0].order`            | int           | 排序                                                                            | 0                                                                                 |

### 4.1.0-SNAPSHOT

4.1.0版本目前正在迭代开发中，会发布SNAPSHOT快照版本，开发者可关注[里程碑](https://gitee.com/xiaoym/knife4j/milestones/181381)，获取快照版本的使用方法

:::tip

4.1.0版本正式发布后，会兼容4.0.0版本的内容，该文档也会及时更新.

:::

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-gateway-spring-boot-starter</artifactId>
    <version>4.1.0-SNAPSHOT</version>
</dependency>
```

#### 配置文件

##### v2最简配置
```yaml
knife4j:
  gateway:
    enabled: true
    version: v2
    discover:
      excluded-services: ${spring.application.name}
```
##### v3最简配置
```yaml
knife4j:
  gateway:
    enabled: true
    version: v3
    discover:
      excluded-services: ${spring.application.name}
```
#### 详细配置

```yaml
knife4j:
  gateway:
    # 开区网关聚合文档 默认:/
    enabled: true
    # 网关前缀(如nginx配置的代理前缀) 默认:/
    api-path-prefix: /api
    # 使用的UI版本(v2或者v3) 默认: v3
    version: v2
    # 服务发现
    discover:
      # 开启服务发现 默认:true
      enabled: true
      # 默认排序 默认:0
      default-order: 0
      # 排除的服务名 默认:为空(建议排除网关服务)
      excluded-services: order-server,user-server
    v2:
      # 文档访问地址 默认：/v2/api-docs?group=default
      api-docs-path: '/v2/api-docs?group=default'
    v3:
      # 文档访问地址 默认:/v3/api-docs
      api-docs-path: '/v3/api-docs'
      # oauth2 redirect url
      oauth2-redirect-url: ''
      # validator url
      validator-url: ''
    routes:
      # 分组名称
      - name: mall-server
        # 文档地址
        url: '/v2/api-docs'
        # context-path
        context-path: '/'
        # 服务名
        service-name: mall-server
        # 排序
        order: 1
```

#### 配置介绍

| 配置属性名称                                       | 类型            | 描述                                                                            | 默认值                                                                               |
|:---------------------------------------------|---------------|-------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| `knife4j.gateway.enabled`                    | boolean       | 是否开启使用Gateway网关聚合组件                                                           | `false`                                                                           |
| `knife4j.gateway.api-path-prefix`            | string        | 网关前缀(如nginx配置的代理前缀)                                                           | `/`                                                                               |
| `knife4j.gateway.version`                    | string        | 使用的UI版本(v2或者v3)                                                               | `v3`                                                                              |
| `knife4j.gateway.discover`                   | Discover      | 服务发现相关配置                                                                      |                                                                                   |
| `knife4j.gateway.discover.enabled`           | boolean       | 开启服务发现                                                                        | `true`                                                                            |
| `knife4j.gateway.discover.default-order`     | int           | 默认排序                                                                          | 0                                                                                 |
| `knife4j.gateway.discover.excluded-services` | array{string} | 排除的服务名(建议排除网关服务)                                                              | `[]`                                                                              |
| `knife4j.gateway.v2`                         | OpenApiV2     | version为v2时进行配置                                                               |                                                                                   |
| `knife4j.gateway.v2.api-docs-path`           | string        | v2 文档访问地址                                                                     | `/v2/api-docs?group=default`                                                      |
| `knife4j.gateway.v3`                         | OpenApiV3     | version为v3时进行配置                                                               |                                                                                   |
| `knife4j.gateway.v3.api-docs-path`           | string        | v3 文档访问地址                                                                     | /v3/api-docs                                                                      |
| `knife4j.gateway.v3.oauth2-redirect-url`     | string        | v3 oauth2-redirect-url                                                        | `''`                                                                              |
| `knife4j.gateway.v3.validator-url`           | string        | v3 validator-url                                                              | `''`                                                                              |
| `knife4j.gateway.routes`                     | array{Router} | 通过路由注册文档                                                                      |                                                                                   |
| `knife4j.gateway.routes[0].name`             | string        | 界面显示分组名称                                                                      | `null`                                                                            |
| `knife4j.gateway.routes[0].url`              | string        | 文档地址                                                                          | v2时为`${knife4j.gateway.v2.api-docs-path}`,v3时为${knife4j.gateway.v3.api-docs-path} |
| `knife4j.gateway.routes[0].context-path`     | string        | 文档`context-path,context-path`会针对当前Route覆盖`${knife4j.gateway.api-path-prefix}` | `/`                                                                               |
| `knife4j.gateway.routes[0].service-name`     | string        | 访问服务名称                                                                        | `null`                                                                            |
| `knife4j.gateway.routes[0].order`            | int           | 排序                                                                            | 0                                                                                 |

#### 访问地址

http://{gateway.host}:{gateway.port}/doc.html
