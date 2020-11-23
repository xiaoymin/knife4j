# 项目说明

>大家好,我在GitChat中开了一篇关于[《Knife4j 及 Swagger 在企业开发中的实践》](https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03)
>地址：[https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03](https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03)
>
>欢迎对 SpringFox、Swagger、Knife4j 感兴趣以及想了解的人员一起来chat 
>


一开始项目初衷是为了写一个增强版本的Swagger 前端UI,但是随着项目的发展,面对越来越多的个性化需求,不得不编写后端Java代码以满足新的需求,在swagger-bootstrap-ui的1.8.5~1.9.6版本之间,采用的是后端Java代码和Ui都混合在一个Jar包里面的方式提供给开发者使用.这种方式虽说对于集成swagger来说很方便,只需要引入jar包即可,但是在微服务架构下显得有些臃肿。

因此,项目正式更名为**knife4j**,取名knife4j是希望她能像一把匕首一样小巧,轻量,并且功能强悍,更名也是希望把她做成一个为Swagger接口文档服务的通用性解决方案,不仅仅只是专注于前端Ui前端.

swagger-bootstrap-ui的所有特性都会集中在`knife4j-spring-ui`包中,并且后续也会满足开发者更多的个性化需求.

主要的变化是,项目的相关类包路径更换为`com.github.xiaoymin.knife4j`前缀,开发者使用增强注解时需要替换包路径

后端Java代码和ui包分离为多个模块的jar包,以面对在目前微服务架构下,更加方便的使用增强文档注解(使用SpringCloud微服务项目,只需要在网关层集成UI的jar包即可,因此分离前后端)

**knife4j**沿用swagger-bootstrap-ui的版本号,第1个版本从1.9.6开始,关于使用方法,请参考文档

由于更名给大家带来的不便深表歉意~！


本文将详细介绍在不同项目场景下如何从`swagger-bootsstrap-ui`切换使用knife4j

## Spring Boot单服务架构

如果你是Spring Boot的单体架构,所有的服务Controller接口都是写在一起的,那么使用knife4j的方式就很简单了,你只需要引入starter即可

maven中的`pom.xml`文件引入starter即可

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <!--在引用时请在maven中央仓库搜索最新版本号-->
    <version>2.0.4</version>
</dependency>
```

`knife4j-spring-boot-starter`主要为我们引用的相关jar包：

- knife4j-spring:Swagger增强处理类
- knife4j-spring-ui:swagger的增强ui文档
- springfox-swagger:springfox最新2.9.2版本
- springfox-bean-validators：springfox验证支持组件

此时,位于包路径`com.github.xiaoymin.knife4j.spring.configuration.Knife4jAutoConfiguration.java`

类会为我们开启Swagger的增强注解,您只需要在项目中创建Swagger的Docket对象即可

```java
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    
}
```

项目可以参考示例[knife4j-spring-boot-single-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/tree/master/knife4j-spring-boot-single-demo)

## Spring Cloud微服务架构

在微服务架构下,引入微服务的starter

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-micro-spring-boot-starter</artifactId>
    <!--在引用时请在maven中央仓库搜索最新版本号-->
    <version>2.0.4</version>
</dependency>
```

`knife4j-micro-spring-boot-starter`的区别在于去掉了Swagger的前端UI包(`springfox-swagger-ui`以及`knife4j-spring-ui`),只引入了后端的Java代码模块

主要包含的核心模块jar：

- knife4j-spring:Swagger增强处理类
- springfox-swagger:springfox最新2.9.2版本
- springfox-bean-validators：springfox验证支持组件
 
 
 