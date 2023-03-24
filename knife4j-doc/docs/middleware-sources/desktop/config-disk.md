# Disk本地配置模式

Disk本地磁盘的配置中心模式是最简单的，用户只需要在本地磁盘建立一个数据目录即可。


## 启动配置

先来看Disk模式的docker-compose.yml信息：


```yml title="docker-compose.yml"
# 通过docker-compose可以快速部署knife4j服务
version: "2.0"
services:
  knife4j:
    container_name: knife4j-desktop
    restart: always
    image: "xiaoymin/knife4j:v2.0"
    network_mode: "bridge"
    # 本地磁盘目录映射
    volumes:
      - D:\Temp\data:/knife4j/data
    ports:
      - "10000:10000"
    # 指定配置属性模式为disk本地磁盘
    environment:
      - knife4j.source=disk
      - knife4j.disk.dir=/knife4j/data

```

属性说明：

|属性|说明|
|----|------|
|volumes|docker磁盘挂载映射目录，规则：`宿主机本地目录(自定义):/knife4j/data`|
|knife4j.source|代表当前容器以什么模式启动，disk模式则代表是当前本地磁盘模式|
|knife4j.disk.dir|代表disk模式下的配置信息读取目录，该目录可以存放OpenAPI离线文件(json/yml),也可以存放支持的服务中心以从远端拉取数据进行聚合|


## 文档配置

那么，当我们通过docker-compose将容器启动后，如何增加文档呢？





### 增加文档

针对disk配置中心模式，在上面我们指定的`volumes`目录下

用户可以建N个一级文件夹(文档),文件夹名称必须是英文或英文+数字，示例如下：

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

### 配置文档

在上面的介绍种，我们知道了如何增加多个文档，其实很简单，只需要在data目录建文件夹即可，那么建立好了文件夹后，如何配置呢？

这里需要涉及到Knife4jAggregation提供支持的4种模式了：Disk、Cloud、Eureka、Nacos

**一个项目文件夹只支持一种模式**

拿ROOT根目录来做示例说明，开发者如何配置。

####  Disk模式

> 此disk模式代表的是服务中心的disk，虽然有点绕，但希望别混淆。

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

```properties
knife4j.disk[0].routes[0].name=用户服务
# 此处location需要注意，只需要配置同级的文件名称即可
knife4j.disk[0].routes[0].location=userOpenApi.json

knife4j.disk[0].routes[1].name=订单服务
# 此处location需要注意，只需要配置同级的文件名称即可
knife4j.disk[0].routes[1].location=orderOpenApi.json

knife4j.disk[0].routes[2].name=商品服务
# 此处location需要注意，只需要配置同级的文件名称即可
knife4j.disk[0].routes[2].location=goodsOpenApi.yml

```
配置好后，无需重启，应用会自动加载


配置属性说明：

|属性|类型|说明|
|----|---|----|
|knife4j|`object`|顶级目录|
|knife4j.disk[0]|`array`|disk模式文档，因为disk模式以文件夹名称进行了切割，所有这里所有的配置都配置下标为`0`|
|knife4j.disk[0].routes|`array`|disk模式聚合的文档数量，多个则下标增加即可|
|knife4j.disk[0].routes[0].name|`string`|当前文档分组显示名称|
|knife4j.disk[0].routes[0].location|`string`|当前文档在本目录下的文件名称，目前只支持以`.json`和`.yml`结尾的OpenAPI规范文件|
|knife4j.disk[0].routes[0].debugUrl|`string`|当前文档在调试时的HTTP地址,例如：`http://192.168.1.1:9090`|
|knife4j.disk[0].routes[0].order|`int`|当前分组显示顺序值，排序规则为`asc`|



#### Cloud模式

Cloud模式则是需要在创建好的文件夹目录下新建`cloud.properties`配置文件，然后配置Cloud模式的节点属性

目录结构如下：

```shell script
|-data
|------ROOT  
|--------cloud.properties
```
示例配置：

```properties
knife4j.cloud[0].routes[0].name=用户
knife4j.cloud[0].routes[0].uri=http://192.168.0.152:8999
knife4j.cloud[0].routes[0].location=/v2/api-docs?group=2.X版本
# more...具体参考Knife4jAggregation聚合组件配置Cloud模式

```
配置好后，无需重启，应用会自动加载


配置属性说明：

|属性|类型|说明|
|----|---|----|
|knife4j|`object`|顶级目录|
|knife4j.cloud[0]|`array`|cloud模式文档，因为disk配置中心模式下以文件夹名称进行了切割，所有这里所有的配置都配置下标为`0`|
|knife4j.cloud[0].routes|`array`|cloud模式聚合的文档数量，多个则下标增加即可|
|knife4j.cloud[0].routes[0].name|`string`|当前文档分组显示名称|
|knife4j.cloud[0].routes[0].uri|`string`|cloud模式下获取OpenAPI信息的服务地址|
|knife4j.cloud[0].routes[0].location|`string`|当前文档的实际OpenAPI接口地址|
|knife4j.cloud[0].routes[0].debugUrl|`string`|当前文档在调试时的HTTP地址(如果不配置，默认走uri的地址),例如：`http://192.168.1.1:9090`|
|knife4j.cloud[0].routes[0].order|`int`|当前分组显示顺序值，排序规则为`asc`|


#### Eureka模式

Eureka模式则是需要在创建好的文件夹目录下新建`eureka.properties`配置文件，然后配置eureka模式的节点属性

目录结构如下：

```shell script
|-data
|------ROOT  
|--------eureka.properties
```

```properties
knife4j.eureka[0].serviceUrl=http://localhost:10000/eureka/
knife4j.eureka[0].username=eureka鉴权账号
knife4j.eureka[0].password=eureka鉴权密码
knife4j.eureka[0].routes[0].name=用户
knife4j.eureka[0].routes[0].serviceName=userService
knife4j.eureka[0].routes[0].location=/v2/api-docs?group=2.X版本

```
配置好后，无需重启，应用会自动加载



配置属性说明：

|属性|类型|说明|
|----|---|----|
|knife4j|`object`|顶级目录|
|knife4j.eureka[0]|`array`|eureka模式文档，因为disk配置中心模式下以文件夹名称进行了切割，所有这里所有的配置都配置下标为`0`|
|knife4j.eureka[0].serviceUrl|`string`|eureka服务注册中心地址|
|knife4j.eureka[0].username|`string`|eureka服务注册中心用户名|
|knife4j.eureka[0].password|`string`|eureka服务注册中心密码|
|knife4j.eureka[0].routes|`array`|eureka模式聚合的文档数量，多个则下标增加即可|
|knife4j.eureka[0].routes[0].name|`string`|当前文档分组显示名称|
|knife4j.eureka[0].routes[0].serviceName|`string`|eureka服务注册中心中的真实服务名称|
|knife4j.eureka[0].routes[0].location|`string`|当前文档的实际OpenAPI接口地址|
|knife4j.eureka[0].routes[0].debugUrl|`string`|当前文档在调试时的HTTP地址(如果不配置，默认走从eureka注册中心解析拿到的服务真实地址)|
|knife4j.eureka[0].routes[0].order|`int`|当前分组显示顺序值，排序规则为`asc`|


#### Nacos模式

> 推荐目标Nacos版本需2.0版本以上

Nacos模式则是需要在创建好的文件夹目录下新建`nacos.properties`配置文件，然后配置nacos模式的节点属性

目录结构如下：

```shell script
|-data
|------ROOT  
|--------nacos.properties
```
示例配置如下：
```properties
knife4j.nacos[0].server=127.0.0.1:8848
knife4j.nacos[0].username=nacos
knife4j.nacos[0].password=nacos
knife4j.nacos[0].namespace=dev
knife4j.nacos[0].routes[0].name=用户
knife4j.nacos[0].routes[0].serviceName=userService
knife4j.nacos[0].routes[0].groupName=DEFAULT_GROUP
knife4j.nacos[0].routes[0].location=/v2/api-docs?group=2.X版本

```

配置好后，无需重启，应用会自动加载


配置属性说明：

|属性|类型|说明|
|----|---|----|
|knife4j|`object`|顶级目录|
|knife4j.nacos[0]|`array`|nacos模式文档，因为disk配置中心模式下以文件夹名称进行了切割，所有这里所有的配置都配置下标为`0`|
|knife4j.nacos[0].server|`string`|nacos服务注册中心地址,规则：`ip:port`,不需要protocol|
|knife4j.nacos[0].username|`string`|nacos服务注册中心用户名|
|knife4j.nacos[0].password|`string`|nacos服务注册中心密码|
|knife4j.nacos[0].password|`string`|nacos服务注册中心密码|
|knife4j.nacos[0].namespace|`string`|nacos服务注册namespaceId|
|knife4j.nacos[0].clusters|`string`|集群，多个以逗号分割|
|knife4j.nacos[0].routes|`array`|nacos模式聚合的文档数量，多个则下标增加即可|
|knife4j.nacos[0].routes[0].name|`string`|当前文档分组显示名称|
|knife4j.nacos[0].routes[0].serviceName|`string`|nacos服务注册中心中的真实服务名称|
|knife4j.nacos[0].routes[0].groupName|`string`|nacos服务注册中心中的真实服务所处分组名称，例如：`DEFAULT_GROUP`|
|knife4j.nacos[0].routes[0].namespace|`string`|nacos服务注册中心中的真实服务所处namespace|
|knife4j.nacos[0].routes[0].location|`string`|当前文档的实际OpenAPI接口地址|
|knife4j.nacos[0].routes[0].debugUrl|`string`|当前文档在调试时的HTTP地址(如果不配置，默认走从nacos注册中心解析拿到的服务真实地址)|
|knife4j.nacos[0].routes[0].order|`int`|当前分组显示顺序值，排序规则为`asc`|
