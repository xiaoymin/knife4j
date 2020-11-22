# 4.3 Eureka注册中心聚合OpenAPI文档



从Eureka注册中心进行聚合的模式和[Cloud模式](aggregation-cloud.md)大同小异，主要的区别是通过`serviceName`来替代了真实的目标服务地址，而是从Eureka注册中心进行动态获取


完整代码请参考[knife4j-aggregation-eureka-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/knife4j-aggregation-eureka-demo)

先来看整个工程的目录：

![](/knife4j/assert/aggregation/eureka.png)

工程目录说明如下：
|工程|说明|
|--|---|
|service-server|Eureka注册中心|
|service-user|一个非常简单的用户服务，包含用户接口|
|service-order|一个非常简单的订单服务，包含订单接口|
|service-doc|聚合文档工程，也是一个Spring Boot工程，不过需要注意的是基于web的，而非webflux|


Eureka注册中心以及service-user、order等都非常简单，按照注册中心、用户服务、订单服务依次进行启动即可

此时，我们访问Eureka的主页，最终能看到我们的注册中心存在两个服务，如下图：


![](/knife4j/assert/aggregation/eureka1.png)


那么，我们的目标是什么呢？从Eureka注册中心直接进行聚合，也就是将用户服务、订单服务的OpenAPI文档聚合在一起进行展示

主要步骤如下：

1、在`service-doc`工程引入`knife4j-aggregation-spring-boot-starter`依赖

::: details 点击查看完整代码
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.xiaominfo.swagger</groupId>
        <artifactId>knife4j-aggregation-eureka-demo</artifactId>
        <version>1.0</version>
        <relativePath>../pom.xml</relativePath> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.xiaominfo.swagger</groupId>
    <artifactId>service-doc</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>service-doc</name>
    <description>Eureka聚合</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-aggregation-spring-boot-starter</artifactId>
            <version>2.0.8</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```
:::

2、配置yml配置文件，如下：

```yml
server:
  port: 10909
knife4j:
  enableAggregation: true
  eureka:
    enable: true
    serviceUrl: http://localhost:10000/eureka/
    routes:
      - name: 订单服务
        serviceName: service-order
        location: /v2/api-docs?group=default
        servicePath: /order
      - name: 用户体系
        serviceName: service-user
        location: /aub/v2/api-docs?group=default
        servicePath: /
```

3、启动项目，访问doc.html进行查看，效果图如下：

聚合效果：
![](/knife4j/assert/aggregation/eureka2.png)

在线调试：
![](/knife4j/assert/aggregation/eureka3.png)


只需要简单的配置，就轻松的将Eureka注册中心的各个服务进行了聚合，是不是比Spring Cloud Gateway、Zuul更加简单和轻量呢？

关于Eureka的更多配置需要开发者参考[文档](../documentation/knife4jAggregation.md)