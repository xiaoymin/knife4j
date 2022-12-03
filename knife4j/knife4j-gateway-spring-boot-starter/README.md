# knife4j-gateway-spring-boot-starter

## 简介

该组件是在gateway网关下聚合Swagger文档，使用简单，只需要简单配置接口

值得注意的事项：

- 子服务中必须使用Swagger2的版本，可以使用Knife4j提供的4.x版本
- 生产环境上线时，配置文件通过配置`knife4j.gateway.enable: false`进行关闭,避免接口泄漏，造成安全问题


## 使用

1、在Spring Cloud Gateway网关项目中引入该starter组件，pom.xml引用如下：
```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-gateway-spring-boot-starter</artifactId>
    <version>4.0.0-SNAPSHOT</version>
</dependency>

```
 

3、配置路由规则(该配置可以配置在Nacos配置中心中，实时生效)，配置规则如下：
```yaml

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


- `knife4j.gateway.enable`          | `Boolean` | 是否开启使用Gateway网关聚合组件，默认`false`                 |
- `knife4j.gateway.routes`          | `List`    | 聚合Swagger分组集合，可以添加多个，无上限                    |
- `knife4j.gateway.routes[0].name`  | `String`  | 界面显示分组名称                                             |
- `knife4j.gateway.routes[0].url`   | `String`  | 子服务的Swagger资源接口地址(Swagger2默认`/v2/api-docs`,只需要配置`group`参数即可)，因为是从网关层走，开发者配置时别忘记了网关前缀地址。 |
- `knife4j.gateway.routes[0].service-name`   | `String`  | 服务名称 |
- `knife4j.gateway.routes[0].order` | `Int`     | 界面分组显示顺序，按正序(`asc`)排序                          |

3、访问地址：http://ip:port/doc.html
