# 4.2 Cloud模式聚合OpenAPI文档

Cloud(云端)模式和[Disk模式](aggregation-disk.md)大同小异，主要的区别是获取OpenAPI规范的方式换成了基于HTTP接口而已


完整代码请参考[knife4j-aggregation-cloud-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/knife4j-aggregation-cloud-demo)

本次Cloud聚合以Knife4j目前部署的线上demo为例，本地聚合在线的OpenAPI，并且可以本地调试，[Knife4jAggregation](../documentation/knife4jAggregation.md)组件会自动帮助我们转发

任意取目前Knife4j的线上demo两个OpenAPI规范接口地址：

- [http://knife4j.xiaominfo.com/v2/api-docs?group=2.X版本](http://knife4j.xiaominfo.com/v2/api-docs?group=2.X%E7%89%88%E6%9C%AC)
- [http://knife4j.xiaominfo.com/v2/api-docs?group=3.默认接口](http://knife4j.xiaominfo.com/v2/api-docs?group=3.%E9%BB%98%E8%AE%A4%E6%8E%A5%E5%8F%A3)



主要步骤如下：

1、创建Spring Boot项目，引入[Knife4jAggregation](../documentation/knife4jAggregation.md)的依赖包，完整pom文件如下：

::: details 点击查看完整代码
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.4.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-aggregation-disk-demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>knife4j-aggregation-disk-demo</name>
    <description>通过基于Spring Boot的工程聚合任意微服务接口文档</description>

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
  port: 19081
knife4j:
  enableAggregation: true
  cloud:
    enable: true
    routes:
      - name: 测试分组1
        uri: knife4j.xiaominfo.com
        location: /v2/api-docs?group=2.X版本
      - name: 测试分组2
        uri: knife4j.xiaominfo.com
        location: /v2/api-docs?group=3.默认接口
```

3、启动项目，访问doc.html进行查看，效果图如下：

聚合效果：
![](/knife4j/assert/aggregation/cloud1.png)

在线调试：
![](/knife4j/assert/aggregation/cloud.png)
