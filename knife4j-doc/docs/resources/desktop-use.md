# 2.3 使用说明

本篇文档主要介绍如何使用`Knife4jAggregationDesktop`


## 2.3.1 增加文档

当`Knife4jAggregationDesktop`软件启动后,开发者想要增加OpenAPI文档应该怎么办？

从技术架构图可以看到,`Knife4jAggregationDesktop`提供了`MetaDataMonitor`组件,该组件是一个监听器,主要监听data目录。当开发者在data目录新建文件夹(文档)后，添加相应的配置文件后自动加载元数据配置，无需重启即可在线访问文档

在data目录下，开发者可以建N个一级文件夹(文档),文件夹名称必须是英文或英文+数组，示例如下：

```shell script
|-data
|------ROOT  
|------project1
|------project2
|------project3
|-----—project4
|-----—more...
```

ROOT目录代表的是根目录，最终访问文档的地址是：`http://ip:port/doc.html`

而开发者自建的文件夹目录，例如project1、project2、project3等等，所代表的是一个项目名称，最终访问的地址如下：

|项目code|文档地址|
|----|----|
|ROOT|根目录，访问地址：`http://ip:port/doc.html`|
|project1|`http://ip:port/project1/doc.html`|
|project2|`http://ip:port/project2/doc.html`|
|project3|`http://ip:port/project3/doc.html`|
|project4|`http://ip:port/project4/doc.html`|
|以此类推|`http://ip:port/${code}/doc.html`|

## 2.3.2 配置文档

在上面的介绍种，我们知道了如何增加多个文档，其实很简单，只需要在data目录建文件夹即可，那么建立好了文件夹后，如何配置呢？

这里需要涉及到Knife4jAggregation提供支持的4种模式了：Disk、Cloud、Eureka、Nacos

**一个项目文件夹只支持一种模式**

拿ROOT根目录来做示例说明，开发者如何配置。

### 2.3.2.1 Disk模式

Disk模式在Knife4jAggregationDesktop中是最简单的，如果开发者拥有OpenAPI文档的静态`JSON`文件或者`yml`文件，那么就可以直接放在建好的文件夹中，不用任何配置，即可渲染。

目录结构如下：

```shell script
|-data
|------ROOT  
|--------userOpenApi.json
|--------orderOpenApi.json
|--------goodsOpenApi.yml

```

在ROOT目录下，我们放置了三个OpenAPI文档的静态文件：`userOpenAPI`以及`orderOpenApi`、`goodsOpenApi.yml`,此时访问地址：`http://ip:port/doc.html`

开发者就能在文档界面中看到会存在三个分组下的OpenAPI文档了。

那么随之问题也来了，在文档中，下拉框的选项名称是以文件的名称来命名显示的，如果要自定义显示应该怎么办？，此时就需要继续在ROOT目录添加一个名为`disk.properties`的配置文件来进行重命名配置

`disk.properties`配置文件(该配置和[Knife4jAggregation聚合组件](aggregation-introduction.md)中声明的[disk模式](aggregation-disk.md)的route节点配置完全一样)：
```properties
knife4j.disk.routes[0].name=用户服务
# 此处location需要注意，只需要配置同级的文件名称即可
knife4j.disk.routes[0].location=userOpenApi.json

knife4j.disk.routes[1].name=订单服务
# 此处location需要注意，只需要配置同级的文件名称即可
knife4j.disk.routes[1].location=orderOpenApi.json

knife4j.disk.routes[2].name=商品服务
# 此处location需要注意，只需要配置同级的文件名称即可
knife4j.disk.routes[2].location=goodsOpenApi.yml

```
配置好后，无需重启，应用会自动加载,也可以参考[测试场景](desktop-test.md)中的配置

### 2.3.2.1 Cloud模式

Cloud模式则是需要在创建好的文件夹目录下新建`cloud.properties`配置文件，然后配置Cloud模式的节点属性

目录结构如下：

```shell script
|-data
|------ROOT  
|--------cloud.properties
```
`cloud.properties`配置文件(该配置和[Knife4jAggregation聚合组件](aggregation-introduction.md)中声明的[Cloud模式](aggregation-cloud.md)的route节点配置完全一样)：

```properties
knife4j.cloud.routes[0].name=用户
knife4j.cloud.routes[0].uri=192.168.0.152:8999
knife4j.cloud.routes[0].location=/v2/api-docs?group=2.X版本
# more...具体参考Knife4jAggregation聚合组件配置Cloud模式

```
配置好后，无需重启，应用会自动加载,也可以参考[测试场景](desktop-test.md)中的配置

### 2.3.2.2 Eureka模式

Eureka模式则是需要在创建好的文件夹目录下新建`eureka.properties`配置文件，然后配置eureka模式的节点属性

目录结构如下：

```shell script
|-data
|------ROOT  
|--------eureka.properties
```
`eureka.properties`配置文件(该配置和[Knife4jAggregation聚合组件](aggregation-introduction.md)中声明的[Eloud模式](aggregation-eureka.md)的route节点配置完全一样)：

```properties
knife4j.eureka.serviceUrl=http://localhost:10000/eureka/
knife4j.eureka.routes[0].name=用户
knife4j.eureka.routes[0].serviceName=userService
knife4j.eureka.routes[0].location=/v2/api-docs?group=2.X版本
# more...具体参考Knife4jAggregation聚合组件配置Eureka模式
```
配置好后，无需重启，应用会自动加载,也可以参考[测试场景](desktop-test.md)中的配置

### 2.3.2.3 Nacos模式

Nacos模式则是需要在创建好的文件夹目录下新建`nacos.properties`配置文件，然后配置nacos模式的节点属性

目录结构如下：

```shell script
|-data
|------ROOT  
|--------nacos.properties
```
`nacos.properties`配置文件(该配置和[Knife4jAggregation聚合组件](aggregation-introduction.md)中声明的[Nacos模式](aggregation-nacos.md)的route节点配置完全一样)：

```properties
knife4j.nacos.serviceUrl=http://localhost:10000/nacos/
knife4j.nacos.routes[0].name=用户
knife4j.nacos.routes[0].serviceName=userService
knife4j.nacos.routes[0].location=/v2/api-docs?group=2.X版本
# more...具体参考Knife4jAggregation聚合组件配置Nacos模式
```

配置好后，无需重启，应用会自动加载,也可以参考[测试场景](desktop-test.md)中的配置