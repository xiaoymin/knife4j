# 4.1 Disk本地模式聚合OpenAPI文档

基于Disk模式聚合是最简单的，开发者只需要在Spring Boot的项目中存在OpenAPI规范的JSON文件即可进行聚合

完整代码请参考[knife4j-aggregation-disk-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/knife4j-aggregation-disk-demo)

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
  disk:
    enable: true
    routes:
      - name: 用户
        location: classpath:openapi/user.json
```
工程目录如下图：

![](/knife4j/assert/aggregation/disk.png)

3、启动项目，访问doc.html进行查看，效果图如下

![](/knife4j/assert/aggregation/disk-ui.png)