# knife4j-gateway-spring-boot-starter

## 简介

该组件是在gateway网关下聚合Swagger文档，使用简单，只需要简单配置接口

值得注意的事项：

- 生产环境上线时，配置文件通过配置`knife4j.gateway.enabled: false`进行关闭,避免接口泄漏，造成安全问题
- 支持服务发现`knife4j.gateway.discover.enabled: true`
- 服务发现中注意排除网关服务
## 使用

### 在Spring Cloud Gateway网关中引入starter

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-gateway-spring-boot-starter</artifactId>
    <version>${最新版本}</version>
</dependency>
```


### 选择UI组件

### 配置文件

#### v2最简配置
```yaml
knife4j:
  gateway:
    enabled: true
    version: v2
    discover:
      excluded-services: ${spring.application.name}
```
#### v3最简配置
```yaml
knife4j:
  gateway:
    enabled: true
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

### 访问地址

http://{gateway.host}:{gateway.port}/doc.html
