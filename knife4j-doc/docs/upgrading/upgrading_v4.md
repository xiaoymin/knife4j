---
id: upgrading-to-v4
title: 升级到v4.0.0版本
---

import ApiLink from '@site/src/components/ApiLink';


`Knife4j` [4.0.0版本](https://www.oschina.net/news/222157/knife4j-4-0-released)目前已经发布!!

## 项目结构(New)

目前`knife4j`的项目结构：

| 模块名称                                         | 说明                                                                                                |
|----------------------------------------------|---------------------------------------------------------------------------------------------------|
| knife4j-aggregation-spring-boot-starter      | 基于 Servlet 体系下的聚合中间件                                                                              |
| knife4j-core                                 | 核心类,包含一些工具包、增强注解等                                                                                 |
| knife4j-dependencies                         | Knife4j 提供的 dependencies 工程，引入该工程后，knife4j\springfox\swagger\springdoc-openapi 等版本号不用在独自声明        |
| knife4j-openapi2-ui                          | 增强 UI 文档,该包是一个 webjar,只包含前端代码，支持 OpenAPI2                                                         |
| knife4j-openapi3-ui                          | 增强 UI 文档,该包是一个 webjar,只包含前端代码，支持 OpenAPI3                                                         |
| knife4j-gateway-spring-boot-starter          | 基于Spring Cloud Gateway网关的项目可以引用该组件实现简单的文档聚合,参考[文档](https://gitee.com/xiaoym/knife4j/tree/dev/knife4j/knife4j-gateway-spring-boot-starter)           |
| knife4j-openapi2-spring-boot-starter         | 基于 OpenAPI2 规范，在 Spring Boot < 3.0.0-M1 的单体架构下可以直接引用此 starter,该模块包含了 Ui 部分，底层依赖 springfox-swagger 2.10.5 项目 |
| knife4j-openapi3-spring-boot-starter         | 基于 OpenAPI3 规范，在 Spring Boot < 3.0.0-M1 的单体架构下可以直接引用此 starter,该模块包含了 Ui 部分，底层基于 springdoc-openapi 项目 |
| knife4j-openapi3-jakarta-spring-boot-starter | 基于 OpenAPI3 规范，在 Spring Boot >= 3.0.0-M1 的单体架构下可以直接引用此 starter,该模块包含了 Ui 部分，底层基于 springdoc-openapi 项目 |


## Spring Boot 2

:::tip

- Spring Boot 版本建议 2.4.0~3.0.0之间
- Spring Boot 版本 < 2.4 版本则建议选择Knife4j 4.0之前的版本
- Spring Boot 2 + OpenAPI2 demo:[knife4j-spring-boot27-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/knife4j-spring-boot27-demo)
- Spring Boot 2 + OpenAPI3 demo:[knife4j-springdoc-openapi-demo ](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/knife4j-springdoc-openapi-demo)

:::  

如果开发者目前仍然使用的是Spring Boot 2.x的主版本号，那么不管是使用OpenAPI2(Swagger)的规范，还是使用OpenAPI3的规范,需要按下面不同的规范类型按需进行引用

**开发者在使用的时候必须2选1，不能说使用2的注解，但是想用OpenAPI3的规范，这是不允许的。**

### OpenAPI2(Swagger)


OpenAPI2的规范在开发者中，使用应该是最广的，开发者从一开始使用springfox项目开始一直使用，Knife4j在之前的版本中也是依赖springfox项目.

此次Knife4j 4.0版本针对OpenAPI2的规范依然提供了Ui及后端增强功能的增强，但是有所不同的是，Knife4j底层依赖的springfox版本停留在**2.10.5版本**，并没有使用springfox最新的3.0.0版本

[springfox 3.0.0版本](https://github.com/springfox/springfox/releases/tag/3.0.0)同时提供了OpenAPI2及OpenAPI3规范的兼容处理，但是开发者依然可以混用不同规范的注解，springfox在底层做了兼容处理，这种方式对于OpenAPI3规范的发展以及适配其工作量是巨大的，另外，springfox 3.0.0 版本也停更了很久了，在目前3.0.0版本中也存在大量的异常或者兼容性问题，所以开发者在以后的版本中，应该尽快迁移到OpenAPI3规范上来.

> Knife4j在后面的发展中,会全力投入到OpenAPI3的规范兼容处理上面.

继续用OpenAPI2规范的情况下使用Knife4j 4.0版本没有太大的变化,只需要在引入的时候，更改**artifactId**即可

Maven坐标：

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi2-spring-boot-starter</artifactId>
    <version>4.0.0</version>
</dependency>

```

如果开发者使用的是springfox3.0.0版本，那么则不能使用Knife4j提供的增强功能，可以单独引用knife4j提供的ui包，

Maven坐标：

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi2-ui</artifactId>
    <version>4.0.0</version>
</dependency>

```

### OpenAPI3(New)


OpenAPI3规范Knife4j底层依赖了[springdoc-openapi](https://github.com/springdoc/springdoc-openapi)项目，该项目针对Spring 项目提供了OpenAPI3规范的解析适配，Knife4j提供了Ui界面以及之前在OpenAPI2的同样的增强功能(包括自定义文档、个性化配置等等)


如果开发者当前的Spring Boot项目已经引用了[springdoc-openapi](https://github.com/springdoc/springdoc-openapi)项目，那么直接引入当前Knife4j提供的starter即可

> 注意，knife4j-openapi3-spring-boot-starter中已经有依赖springdoc-openapi的jar，开发者要避免jar包版本冲突

Maven坐标如下：

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-spring-boot-starter</artifactId>
    <version>4.0.0</version>
</dependency>
```

## Spring Boot 3(New)

Knife4j在后面的版本中会全面切入OpenAPI3的规范中，因此在以后的版本迭代中，只会提供给OpenAPI3规范的适配，开发者应该尽快迁移到3的规范上来。

Spring Boot 3的版本,knife4j依赖[springdoc-openapi](https://github.com/springdoc/springdoc-openapi)项目2.0版本

需要注意的是JDK版本必须大于17，这是由Spring Boot 3.0版本所决定的。

Maven坐标如下：

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
    <version>4.0.0</version>
</dependency>
```


## Gateway网关聚合(New)

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
|knife4j.gateway.routes[0].service-name | String | 服务名称 |
|knife4j.gateway.routes[0].order | Int | 界面分组显示顺序，按正序(asc)排序 |