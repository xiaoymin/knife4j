# 4.3 Knife4jAggregationDesktop

> 有了新的想法，然后就开始折腾吧~~！
>
> 目前该想法作者正在开发中......


Knife4jAggregationDesktop是一款基于聚合组件Knife4jAggregation特性的独立部署的聚合OpenAPI文档软件，脱离Spring、Spring Boot技术架构体系，开发者下载后独立部署启动。

主要功能作用：

- 独立部署(依赖Java JDK8环境)
- 拥有Knife4jAggregation的全部特性
- 基于动态文件配置方式
- 支持多个项目动态配置


## 4.3.1 软件架构

**技术架构图**如下：

![](/knife4j/assert/aggregation/Knife4jAggregationDesktop.png)

**软件目录**如下：

```shell script
|-Knife4jAggregationDesktop
|------bin 
|------conf
|------data
|------lib
|-----—logs
|------webapps
|------LICENSE
|------readme.txt
```

目录说明：
- `bin`:启动命令目录
- `conf`:配置文件目录，`application.properties`包含`Knife4jAggregationDesktop`软件的相关配置，包括端口号，为文档设置basicAuth权限等
- `data`:数据目录，默认根目录存放`ROOT`文件夹,多个项目的OpenAPI聚合，开发者只需要在此目录下建文件夹即可
- `lib`:依赖jar包
- `logs`:日志
- `webapps`:Knife4jUi的静态资源文件


## 4.3.2 配置文件

在`conf`文件夹下有`application.properties`配置文件，是`Knife4jAggregationDesktop`软件的独立配置

目前的配置属性如下：
```properties
# Knife4jAggregationDesktop 启动端口号
knife4j.port=18006
# 为所有Knife4jAggregationDesktop开放出去的OpenAPI文档加权，设置BasicAuth访问密码
# enable=true 代表启用
knife4j.basic.enable=true
knife4j.basic.username=zhangsan
knife4j.basic.password=123456

```

## 4.3.3 增加文档

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

## 4.3.4 配置文档

在上面的介绍种，我们知道了如何增加多个文档，其实很简单，只需要在data目录建文件夹即可，那么建立好了文件夹后，如何配置呢？

这里需要涉及到Knife4jAggregation提供支持的4种模式了：Disk、Cloud、Eureka、Nacos

**一个项目文件夹只支持一种模式**

拿ROOT根目录来做示例说明，开发者如何配置。

### 4.3.4.1 Disk模式

Disk模式在Knife4jAggregationDesktop中是最简单的，如果开发者拥有OpenAPI文档的静态JSON文件，那么就可以直接放在建好的文件夹中，不用任何配置，即可渲染。

目录结构如下：

```shell script
|-data
|------ROOT  
|--------userOpenApi.json
|--------orderOpenApi.json

```

在ROOT目录下，我们放置了两个OpenAPI文档的静态JSON文件：`userOpenAPI`以及`orderOpenApi`,此时访问地址：`http://ip:port/doc.html`

开发者就能在文档界面中看到会存在两个分组下的OpenAPI文档了。

那么随之问题也来了，在文档中，下拉框的选项名称是以文件的名称来命名显示的，如果要自定义显示应该怎么办？，此时就需要继续在ROOT目录添加一个名为`disk.properties`的配置文件来进行重命名配置

`disk.properties`配置文件(该配置和[Knife4jAggregation聚合组件](knife4jAggregation.md)中声明的disk模式的route节点配置完全一样)：
```properties
knife4j.disk.routes[0].name=用户服务
# 此处location需要注意，只需要配置同级的文件名称即可
knife4j.disk.routes[0].location=userOpenApi.json

knife4j.disk.routes[1].name=订单服务
# 此处location需要注意，只需要配置同级的文件名称即可
knife4j.disk.routes[1].location=orderOpenApi.json

```
配置好后，无需重启，应用会自动加载

### 4.3.4.1 Cloud模式

Cloud模式则是需要在创建好的文件夹目录下新建`cloud.properties`配置文件，然后配置Cloud模式的节点属性

目录结构如下：

```shell script
|-data
|------ROOT  
|--------cloud.properties
```
`cloud.properties`配置文件(该配置和[Knife4jAggregation聚合组件](knife4jAggregation.md)中声明的cloud模式的route节点配置完全一样)：

```properties
knife4j.cloud.routes[0].name=用户
knife4j.cloud.routes[0].uri=192.168.0.152:8999
knife4j.cloud.routes[0].location=/v2/api-docs?group=2.X版本
# more...具体参考Knife4jAggregation聚合组件配置Cloud模式

```

### 4.3.4.2 Eureka模式

Eureka模式则是需要在创建好的文件夹目录下新建`eureka.properties`配置文件，然后配置eureka模式的节点属性

目录结构如下：

```shell script
|-data
|------ROOT  
|--------eureka.properties
```
`eureka.properties`配置文件(该配置和[Knife4jAggregation聚合组件](knife4jAggregation.md)中声明的cloud模式的route节点配置完全一样)：

```properties
knife4j.eureka.serviceUrl=http://localhost:10000/eureka/
knife4j.eureka.routes[0].name=用户
knife4j.eureka.routes[0].serviceName=userService
knife4j.eureka.routes[0].location=/v2/api-docs?group=2.X版本
# more...具体参考Knife4jAggregation聚合组件配置Eureka模式
```

### 4.3.4.3 Nacos模式

Nacos模式则是需要在创建好的文件夹目录下新建`nacos.properties`配置文件，然后配置nacos模式的节点属性

目录结构如下：

```shell script
|-data
|------ROOT  
|--------nacos.properties
```
`nacos.properties`配置文件(该配置和[Knife4jAggregation聚合组件](knife4jAggregation.md)中声明的nacos模式的route节点配置完全一样)：

```properties
knife4j.nacos.serviceUrl=http://localhost:10000/nacos/
knife4j.nacos.routes[0].name=用户
knife4j.nacos.routes[0].serviceName=userService
knife4j.nacos.routes[0].location=/v2/api-docs?group=2.X版本
# more...具体参考Knife4jAggregation聚合组件配置Nacos模式
```