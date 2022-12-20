### 集成思路

knife4j的openapi能力是来自springfox，也可以说来自swagger

dubbox要集成knife4j，那么首先就要集成swagger

dubbox是jaxrs提供的restful能力，jackson提供的json序列化能力（可以替换为fastjson）

swagger正好有对jaxrs提供支持，所以我们引入桥接包`swagger-jaxrs`就可以最终做到dubbox集成knife4j

### 1、pom引入

```xml
    <dependency>
        <groupId>com.github.xiaoymin</groupId>
        <artifactId>knife4j-spring</artifactId>
        <version>2.0.9</version>
    </dependency>
    <dependency>
        <groupId>com.github.xiaoymin</groupId>
        <artifactId>knife4j-spring-ui</artifactId>
        <version>2.0.9</version>
    </dependency>
    <dependency>
        <groupId>io.swagger</groupId>
        <artifactId>swagger-jaxrs</artifactId>
        <version>1.6.2</version>
    </dependency>
    <dependency>
        <groupId>io.swagger</groupId>
        <artifactId>swagger-core</artifactId>
        <version>1.6.2</version>
    </dependency>
    <dependency>
        <groupId>io.swagger</groupId>
        <artifactId>swagger-jersey-jaxrs</artifactId>
        <version>1.6.2</version>
    </dependency>
    <dependency>
        <groupId>io.swagger</groupId>
        <artifactId>swagger-annotations</artifactId>
        <version>1.6.2</version>
    </dependency>
    <dependency>
        <groupId>io.swagger</groupId>
        <artifactId>swagger-jersey2-jaxrs</artifactId>
        <version>1.6.2</version>
    </dependency>
```

### 2、编写代码

DubboxSwaggerService

```java
import javax.servlet.ServletConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * 类描述：dubbox+swagger
 *
 * @author xksgs
 * @date 2021-09-02
 */
@Path("dubboxswagger")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({MediaType.APPLICATION_JSON + "; " + "charset=UTF-8", MediaType.TEXT_XML + "; " + "charset=UTF-8"})
public interface DubboxSwaggerService {

    @GET
    @Path("swagger")
    Response getListingJson(@Context Application app,
                            @Context ServletConfig sc, @Context HttpHeaders headers,
                            @Context UriInfo uriInfo);
}
```
DubboxAcceptHeaderApiListingResource

```java
import io.swagger.jaxrs.listing.BaseApiListingResource;
import org.springframework.stereotype.Component;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * 类描述：dubbox+swagger
 *
 * @author xksgs
 * @date 2021-09-02
 */
@Component
public class DubboxAcceptHeaderApiListingResource extends BaseApiListingResource implements DubboxSwaggerService {

    @Context
    ServletContext context;

    @Override
    public Response getListingJson(Application app, ServletConfig sc,
                                   HttpHeaders headers, UriInfo uriInfo) {
        Response rsp = getListingJsonResponse(app, context, sc, headers, uriInfo);
        // 解决跨域问题
        /*rsp.getHeaders().add("Access-Control-Allow-Origin", "*");
        rsp.getHeaders().add("Access-Control-Allow-Headers", "x-requested-with, ssi-token");
        rsp.getHeaders().add("Access-Control-Max-Age","3600");
        rsp.getHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");*/
        return rsp;
    }
}
```
SwaggerConfig

```java
import io.swagger.jaxrs.config.BeanConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述: swagger全局配置
 *
 * @author xksgs
 * @date 2021-09-02
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public BeanConfig beanConfig() {
        BeanConfig config = new BeanConfig();
        config.setConfigId("xxx-collect");
        config.setTitle("xxx-collect rest api接口文档");
        config.setDescription("xxx-collect rest api接口文档");
        config.setVersion("v1");
        config.setContact("xxx@gmail.com");
        config.setHost("localhost:9026");
        config.setSchemes(new String[]{"http"});
        config.setBasePath("/collect");
        config.setResourcePackage("com.xx.xxx.collect");
        config.setPrettyPrint(true);
        config.setScan(true);
        return config;
    }
}
```

### 3、声明dubbo接口

spring-restful-service.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
	http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
	
	<dubbo:protocol name="rest" port="9026" threads="1000"
		contextpath="collect" server="tomcat" accepts="1000"
		extension="com.alibaba.dubbo.rpc.protocol.rest.support.LoggingFilter" />

	<dubbo:service document="dubboxswagger"
				   interface="com.xx.xxx.collect.framework.swagger.DubboxSwaggerService"
				   ref="dubboxAcceptHeaderApiListingResource" protocol="rest" validation="true"/>

```

### 4、完成以上操作，算是完成了swagger的集成

检查`localhost:9026/collect/dubboxswagger/swagger`是否能拿到openapi接口

项目同时也是一个springboot项目，dubbox开放的9026端口，springboot开放的8080端口，
基础版本的knife4j只支持固定一个端口，所以我们换knife4j的高级功能
[Cloud模式](https://doc.xiaominfo.com/knife4j/resources/aggregation-cloud.html)

### 5、引入knife4jCloud模式

```xml
    <dependency>
    	<groupId>com.github.xiaoymin</groupId>
    	<artifactId>knife4j-aggregation-spring-boot-starter</artifactId>
    	<version>2.0.9</version>
    </dependency>
```

### 6、配置自定义openapi路径

application.properties
```properties
knife4j.enableAggregation=true
knife4j.cloud.enable=true
knife4j.cloud.routes[0].name=xxx-collect
knife4j.cloud.routes[0].uri=localhost:9026
knife4j.cloud.routes[0].location=/collect/dubboxswagger/swagger
knife4j.cloud.routes[0].swaggerVersion=2.0
knife4j.cloud.routes[0].servicePath=/
knife4j.cloud.routes[0].routeAuth.enable=false
knife4j.cloud.routes[0].routeAuth.username=test3
knife4j.cloud.routes[0].routeAuth.password=66666
knife4j.cloud.routeAuth.enable=false
knife4j.cloud.routeAuth.username=test
knife4j.cloud.routeAuth.password=12313
```

### 7、最后访问`localhost:8080/doc.html`

### 8、踩坑指南

`localhost:9026/collect/dubboxswagger/swagger`出来的openapi内容不一定满足knife4j，如果不满足就需要调整json序列化

- 1、请求参数、相应参数都是空，前端识别的是`schema`下的`$ref`属性，检查json`schema`下有没有`$ref`属性，fastjson使用`SerializerFeature.DisableCircularReferenceDetect` 关闭循环引用

正确示例json片段
```json
"paths": {
    "/xxxService/pageList": {
      "post": {
        "vendorExtensions": {},
        "tags": [
          "相关的接口"
        ],
        "summary": "分页查询列表",
        "description": "分页查询列表",
        "operationId": "listPage_3",
        "schemes": null,
        "consumes": null,
        "produces": null,
        "parameters": [
          {
            "vendorExtensions": {},
            "in": "body",
            "name": "body",
            "description": null,
            "required": false,
            "access": null,
            "pattern": null,
            "readOnly": null,
            "schema": {
              "description": null,
              "externalDocs": null,
              "properties": null,
              "example": null,
              "title": null,
              "reference": "#/definitions/查询请求DTO",
              "vendorExtensions": null,
              "$ref": "#/definitions/查询请求DTO",
              "simpleRef": "查询请求DTO",
              "originalRef": "查询请求DTO",
              "refFormat": "INTERNAL"
            },
            "examples": null
          }
        ],
        "responses": {
          "200": {
            "description": "successful operation",
            "examples": null,
            "headers": null,
            "vendorExtensions": {},
            "schema": {
              "type": "ref",
              "required": false,
              "$ref": "#/definitions/基础返回对象公共的分页返回封装实例DTO",
              "simpleRef": "基础返回对象公共的分页返回封装实例DTO",
              "originalRef": "基础返回对象公共的分页返回封装实例DTO",
              "refFormat": "INTERNAL"
            },
            "responseSchema": {
              "description": null,
              "externalDocs": null,
              "properties": null,
              "example": null,
              "title": null,
              "reference": "#/definitions/基础返回对象公共的分页返回封装实例DTO",
              "vendorExtensions": null,
              "$ref": "#/definitions/基础返回对象公共的分页返回封装实例DTO",
              "simpleRef": "基础返回对象公共的分页返回封装实例DTO",
              "originalRef": "基础返回对象公共的分页返回封装实例DTO",
              "refFormat": "INTERNAL"
            }
          }
        },
        "security": null,
        "externalDocs": null,
        "deprecated": null
      }
    }
  }
```

- 2、请求参数、相应参数都是空，还可能是`definitions`和`properties`属性下的null值属性引起的，要统统去掉

错误示例json片段
```json
"definitions": {
      "基础返回对象": {
      "type": "object",
      "required": null,
      "discriminator": null,
	  "externalDocs": null,
      "reference": null,
      "title": null,
      "vendorExtensions": {},
      "xml": null,
      "minimum": null,
      "maximum": null,
      "multipleOf": null,
      "exclusiveMinimum": null,
      "exclusiveMaximum": null,
      "minLength": null,
      "maxLength": null,
      "pattern": null,
      "format": null,
      "name": "基础返回对象",
      "allowEmptyValue": null,
      "uniqueItems": null,
      "description": null,
      "example": null,
      "additionalProperties": null,
      "defaultValue": null,
      "simple": false,
      "enum": null,
      "properties": {
        "requestId": {
          "name": "requestId",
          "type": "string",
          "format": null,
          "example": null,
          "xml": null,
          "required": false,
          "position": null,
          "description": null,
          "title": null,
          "readOnly": null,
          "allowEmptyValue": null,
          "access": null,
          "vendorExtensions": {},
          "minLength": null,
          "maxLength": null,
          "pattern": null,
          "default": null,
          "enum": null
        },
```

正确示例json片段
```json
"definitions": {
    "基础返回对象": {
      "type": "object",
      "properties": {
        "requestId": {
          "name": "requestId",
          "type": "string",
          "required": false
        }
	  }
	}
```

- 3、接口不显示，检查`paths`-`url`-`post`-`responses`-`200`-`schema`-`$ref`路径是否完整

正确示例json片段
```json
"paths": {
    "/xxxService/pageList": {
      "post": {
        "vendorExtensions": {},
        "tags": [
          "相关的接口"
        ],
        "summary": "分页查询列表",
        "description": "分页查询列表",
        "operationId": "listPage_3",
        "schemes": null,
        "consumes": null,
        "produces": null,
        "parameters": [
          {
            "vendorExtensions": {},
            "in": "body",
            "name": "body",
            "description": null,
            "required": false,
            "access": null,
            "pattern": null,
            "readOnly": null,
            "schema": {
              "description": null,
              "externalDocs": null,
              "properties": null,
              "example": null,
              "title": null,
              "reference": "#/definitions/查询请求DTO",
              "vendorExtensions": null,
              "$ref": "#/definitions/查询请求DTO",
              "simpleRef": "查询请求DTO",
              "originalRef": "查询请求DTO",
              "refFormat": "INTERNAL"
            },
            "examples": null
          }
        ],
        "responses": {
          "200": {
            "description": "successful operation",
            "examples": null,
            "headers": null,
            "vendorExtensions": {},
            "schema": {
              "type": "ref",
              "required": false,
              "$ref": "#/definitions/基础返回对象公共的分页返回封装实例DTO",
              "simpleRef": "基础返回对象公共的分页返回封装实例DTO",
              "originalRef": "基础返回对象公共的分页返回封装实例DTO",
              "refFormat": "INTERNAL"
            },
            "responseSchema": {
              "description": null,
              "externalDocs": null,
              "properties": null,
              "example": null,
              "title": null,
              "reference": "#/definitions/基础返回对象公共的分页返回封装实例DTO",
              "vendorExtensions": null,
              "$ref": "#/definitions/基础返回对象公共的分页返回封装实例DTO",
              "simpleRef": "基础返回对象公共的分页返回封装实例DTO",
              "originalRef": "基础返回对象公共的分页返回封装实例DTO",
              "refFormat": "INTERNAL"
            }
          }
        },
        "security": null,
        "externalDocs": null,
        "deprecated": null
      }
    }
  }
```
