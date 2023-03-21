# Nacos配置中心模式


Nacos配置中心模式在代表运行Insight组件时，所有数据全部来源于Nacos配置中心，由于Nacos提供了可操作的Web界面，因此使用上也是较方便的。
2

## 启动配置

来看Nacos模式下的`docker-compose.yml`文件，



```yml title="docker-compose.yml"

# 通过docker-compose可以快速部署knife4j服务
version: "2.0"
services:
  knife4j:
    container_name: knife4j-insight-nacos
    restart: always
    image: "xiaoymin/knife4j:v2.0"
    network_mode: "bridge"
    # 端口映射
    ports:
      - "10000:10000"
    # 指定配置属性模式为Nacos配置中心
    environment:
      - knife4j.source=nacos
      - knife4j.nacos.server=127.0.0.1:8848
      - knife4j.nacos.username=nacos
      - knife4j.nacos.password=nacos
      # 使用者自行在所指定的nacos上创建 namespace 、dataid、group
      - knife4j.nacos.namespace=knife4j
      - knife4j.nacos.dataId=knife4j_data_id
      - knife4j.nacos.group=DEFAULT_GROUP

```


属性说明：

|属性|说明|
|----|------|
|knife4j.source|代表当前容器以什么模式启动，nacos则代表当前组件选择的是基于Nacos配置中心|
|knife4j.nacos.server|Nacos服务地址，规则：`ip:port`,推荐Nacos版本>=2.0|
|knife4j.nacos.username|Nacos鉴权用户名|
|knife4j.nacos.password|Nacos鉴权密码|
|knife4j.nacos.namespace|配置所属nacos上namespace名称|
|knife4j.nacos.dataId|配置dataId|
|knife4j.nacos.group|配置group名称|

Nacos模式需要注意的是，使用者需要事先在Nacos配置中心中创建一个配置文件(仅支持properties和yml类型)以作为Insight组件数据源启动运行，内容可以为空

创建的该配置所对应的namespace、dataId、group就是上面配置所对应的值。

而该配置，则也是Insight用来聚合各方OpenAPI数据的配置来源,下面介绍的四种模式的配置内容都存放在该配置中

## 文档配置

下面则介绍，如果在Nacos的配置中心，配置聚合不同类型的OpenAPI规范文档。

首先声明，不同于[Disk本地配置中心](config-disk)模式，以文件夹作为每一个文档的内容进行聚合

Nacos配置中心模式则是**将所有文档的配置都配置在同一个配置中**


### Disk模式

由于在[Disk本地配置中心](config-disk)模式中，我们聚合本地OpenAPI文件即可，而Nacos配置中心模式则有轻微不同，因为已经没有了磁盘文件，因此，用户可以将OpenAPI规范文件存放在Nacos配置中心中，这样就能利用Insight进行聚合了

示例配置如下：


```properties  title="Nacos{dataId=knife4j_data_id,group=DEFAULT_GROUP}"
# 第一个文档，访问：http://ip:port/disk-t1/doc.html
knife4j.disk[0].contextPath=disk-t1
knife4j.disk[0].routes[0].name=分组1
knife4j.disk[0].routes[0].dataId=openapi1
knife4j.disk[0].routes[0].group=DEFAULT_GROUP
knife4j.disk[0].routes[0].order=1

# 第二个文档，访问：http://ip:port/disk-t2/doc.html
knife4j.disk[1].contextPath=disk-t2
knife4j.disk[1].routes[0].name=分组2-1
knife4j.disk[1].routes[0].dataId=openapi2
knife4j.disk[1].routes[0].group=DEFAULT_GROUP
knife4j.disk[1].routes[0].order=1
knife4j.disk[1].routes[1].name=分组2-2
knife4j.disk[1].routes[1].dataId=openapi3
knife4j.disk[1].routes[1].group=DEFAULT_GROUP
knife4j.disk[1].routes[1].order=2

# more....

```

在上面的配置中，我们分别配置了三个离线的OpenAPI规范文件，dataId分别是`openapi1`、`openapi2`、`openapi3`

如下图所示：

![](/images/website/desktop/config/knife4j-nacos-disk.png)

通过该模式的支持，以后OpenAPI定义内容则可以破历史的放在Nacos配置中心而快速进行渲染了。

Disk模式详细配置属性如下：

|属性|类型|说明|
|----|---|----|
|knife4j|`object`|顶级目录|
|knife4j.disk[0]|`array`|disk模式文档，多个文档下标累加即可|
|knife4j.disk[0].contextPath|`string`|当前文档路径，规则：`英文、数字、英文+数字等`|
|knife4j.disk[0].routes|`array`|disk模式聚合的文档数量，多个则下标增加即可|
|knife4j.disk[0].routes[0].name|`string`|当前文档分组显示名称|
|knife4j.disk[0].routes[0].dataId|`string`|存放在Nacos配置中的OpenAPI内容的dataId名称|
|knife4j.disk[0].routes[0].group|`string`|该OpenAPI内容在Nacos配置中心中的group分组名称|
|knife4j.disk[0].routes[0].debugUrl|`string`|当前文档在调试时的HTTP地址,例如：`http://192.168.1.1:9090`|
|knife4j.disk[0].routes[0].order|`int`|当前分组显示顺序值，排序规则为`asc`|



### Cloud模式

Cloud模式则配置比较简单，示例配置如下：


```properties   title="Nacos{dataId=knife4j_data_id,group=DEFAULT_GROUP}"

# cloud模式第一个文档，http://ip:port/cloud1/doc.html
knife4j.cloud[0].contextPath=cloud1
knife4j.cloud[0].routes[0].name=用户
knife4j.cloud[0].routes[0].uri=http://192.168.0.152:8999
knife4j.cloud[0].routes[0].location=/v2/api-docs?group=2.X版本

# cloud模式第二个文档，http://ip:port/cloud2/doc.html
knife4j.cloud[1].contextPath=cloud2
knife4j.cloud[1].routes[0].name=订单
knife4j.cloud[1].routes[0].uri=http://192.168.0.153:8999
knife4j.cloud[1].routes[0].location=/v2/api-docs?group=2.X版本



```

配置属性说明：

|属性|类型|说明|
|----|---|----|
|knife4j|`object`|顶级目录|
|knife4j.cloud[0]|`array`|cloud模式文档，多个文档下标累加即可|
|knife4j.cloud[0].contextPath|`string`|当前文档路径，规则：`英文、数字、英文+数字等`|
|knife4j.cloud[0].routes|`array`|cloud模式聚合的文档数量，多个则下标增加即可|
|knife4j.cloud[0].routes[0].name|`string`|当前文档分组显示名称|
|knife4j.cloud[0].routes[0].uri|`string`|cloud模式下获取OpenAPI信息的服务地址|
|knife4j.cloud[0].routes[0].location|`string`|当前文档的实际OpenAPI接口地址|
|knife4j.cloud[0].routes[0].debugUrl|`string`|当前文档在调试时的HTTP地址(如果不配置，默认走uri的地址),例如：`http://192.168.1.1:9090`|
|knife4j.cloud[0].routes[0].order|`int`|当前分组显示顺序值，排序规则为`asc`|


### Nacos模式

此Nacos模式则代表的是Nacos服务中心，通过配置聚合各个Nacos服务中心的各个服务的OpenAPI文档，用户需要避免混淆。
> 1.只要是网络互通，各个项目组、中心的Nacos实例都可以配置。
> 2.Nacos版本必须大于等于2.0版本

示例配置如下：
```properties  title="Nacos{dataId=knife4j_data_id,group=DEFAULT_GROUP}"
# Nacos模式下的第一个文档，访问：http://ip:port/nacos_doc1/doc.html
knife4j.nacos[0].contextPath=nacos_doc1
# 聚合192.168.0.110服务器上的Nacos实例
knife4j.nacos[0].server=192.168.0.110:8848
knife4j.nacos[0].username=nacos
knife4j.nacos[0].password=nacos
knife4j.nacos[0].namespace=dev
knife4j.nacos[0].routes[0].name=用户
knife4j.nacos[0].routes[0].serviceName=userService
knife4j.nacos[0].routes[0].groupName=DEFAULT_GROUP
knife4j.nacos[0].routes[0].location=/v2/api-docs?group=2.X版本

# Nacos模式下的第二个文档，访问：http://ip:port/nacos_doc2/doc.html
knife4j.nacos[1].contextPath=nacos_doc2
# 聚合192.168.0.112服务器上的Nacos实例
knife4j.nacos[1].server=192.168.0.112:8848
knife4j.nacos[1].username=nacos
knife4j.nacos[1].password=nacos
knife4j.nacos[1].namespace=dev
knife4j.nacos[1].routes[0].name=用户
knife4j.nacos[1].routes[0].serviceName=orderService
knife4j.nacos[1].routes[0].groupName=DEFAULT_GROUP
knife4j.nacos[1].routes[0].location=/v2/api-docs?group=default


```

配置属性说明：

|属性|类型|说明|
|----|---|----|
|knife4j|`object`|顶级目录|
|knife4j.nacos[0]|`array`|nacos模式文档，多个文档下标累加即可|
|knife4j.nacos[0].contextPath|`string`|当前文档路径，规则：`英文、数字、英文+数字等`|
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


### Eureka模式

Eureka模式则同Nacos模式差不多，聚合Eureka注册中心实例上的各个子服务OpenAPI文档

示例配置如下：

```properties   title="Nacos{dataId=knife4j_data_id,group=DEFAULT_GROUP}"
# Eureka模式下的第一个文档，访问：http://ip:port/eureka1/doc.html
knife4j.eureka[0].contextPath=eureka1
knife4j.eureka[0].serviceUrl=http://localhost:10000/eureka/
knife4j.eureka[0].username=eureka鉴权账号
knife4j.eureka[0].password=eureka鉴权密码
knife4j.eureka[0].routes[0].name=用户
knife4j.eureka[0].routes[0].serviceName=userService
knife4j.eureka[0].routes[0].location=/v2/api-docs?group=2.X版本

# Eureka模式下的第二个文档，访问：http://ip:port/eureka2/doc.html
knife4j.eureka[1].contextPath=eureka2
knife4j.eureka[1].serviceUrl=http://192.168.0.220:10000/eureka/
knife4j.eureka[1].username=eureka鉴权账号
knife4j.eureka[1].password=eureka鉴权密码
knife4j.eureka[1].routes[0].name=用户
knife4j.eureka[1].routes[0].serviceName=userService
knife4j.eureka[1].routes[0].location=/v2/api-docs?group=2.X版本
```


配置属性说明：

|属性|类型|说明|
|----|---|----|
|knife4j|`object`|顶级目录|
|knife4j.eureka[0]|`array`|eureka模式文档，多个文档下标累加即可|
|knife4j.eureka[0].contextPath|`string`|当前文档路径，规则：`英文、数字、英文+数字等`|
|knife4j.eureka[0].serviceUrl|`string`|eureka服务注册中心地址|
|knife4j.eureka[0].username|`string`|eureka服务注册中心用户名|
|knife4j.eureka[0].password|`string`|eureka服务注册中心密码|
|knife4j.eureka[0].routes|`array`|eureka模式聚合的文档数量，多个则下标增加即可|
|knife4j.eureka[0].routes[0].name|`string`|当前文档分组显示名称|
|knife4j.eureka[0].routes[0].serviceName|`string`|eureka服务注册中心中的真实服务名称|
|knife4j.eureka[0].routes[0].location|`string`|当前文档的实际OpenAPI接口地址|
|knife4j.eureka[0].routes[0].debugUrl|`string`|当前文档在调试时的HTTP地址(如果不配置，默认走从eureka注册中心解析拿到的服务真实地址)|
|knife4j.eureka[0].routes[0].order|`int`|当前分组显示顺序值，排序规则为`asc`|