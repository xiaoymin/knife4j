# 模块说明


## knife4j模块

knife4j目前的项目结构

```properties
|-knife4j
|-----knife4j-annotations // 提供的增强注解包
|-----knife4j-core // 相关业务逻辑核心包
|-----knife4j-dependencies //提供基于Maven Bom方式引用jar包,避免版本冲突,自2.0.2版本开始引入
|-----knife4j-spring  //spring项目使用swagger是可以直接使用该jar包
|-----knife4j-spring-ui  // swagger前端ui文档,访问方式不变,还是doc.html
|-----knife4j-micro-spring-boot-starter //微服务情况下的starter组件,不包含ui组件
|-----knife4j-spring-boot-starter //knife4j为Spring Boot项目提供的starter组件
```

|模块|说明|备注|
|----|---|--|
|knife4j-annotations|提供的增强注解包||
|knife4j-core|相关业务逻辑核心包||
|knife4j-dependencies|提供基于Maven Bom方式引用jar包,避免版本冲突|自2.0.2版本开始引入|
|knife4j-spring|spring项目使用swagger是可以直接使用该jar包||
|knife4j-spring-ui|swagger前端ui文档,访问方式不变,还是doc.html||
|knife4j-micro-spring-boot-starter|微服务情况下的starter组件,不包含ui组件||
|knife4j-spring-boot-starter|knife4j为Spring Boot项目提供的starter组件||




## 开源代码模块

在Gitee代码仓库托管的地址：[https://gitee.com/xiaoym/knife4j](https://gitee.com/xiaoym/knife4j)

结构说明如下：


| 模块名称             | 说明                                                         |
| -------------------- | ------------------------------------------------------------ |
| knife4j              | 为Java MVC框架集成Swagger的增强解决方案,明细请参考上面的介绍说明                      |
| knife4j-admin        | 云端Swagger接口文档注册管理中心,集成gateway网关对任意微服务文档进行组合集成，该模块尚未启动开发 |
| knife4j-extension    | chrome浏览器的增强swagger接口文档ui,快速渲染swagger资源，该模块尚未启动开发      |
| knife4j-service      | 为swagger服务的一系列接口服务程序 ，该模块尚未启动开发                          |
| knife4j-front        | knife4j-spring-ui的纯前端静态版本,用于集成非Java语言使用，该模块尚未启动开发     |
| knife4j-vue | 该模块是knife4j前端的源码模块,基于Vue框架开发,knife4j-spring-ui中的文件是使用该模块进行打包构建的                            |
| swagger-bootstrap-ui | knife4j的前身,最后发布版本是1.9.6                            |
| swagger-bootstrap-ui-front | swagger-bootstrap-ui的纯前端版本,基于1.9.3的分支改造而成,其他开发语言体系可以使用,改造后一直未更新                            |
 
 
 