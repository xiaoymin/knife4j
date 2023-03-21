import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

#  安装

:::danger 注意事项

由于目前Knife4jInsight是以Docker的方式提供的服务，因此使用者在使用的时候有些事项还是需要注意，否则会导致一些问题发生

- 不管是Disk模式还是Nacos模式，如果是配置使用通过HTTP接口获取OpenAPI数据的服务类型(Cloud/Nacos/Eureka模式)，需要**保证Docker容器内的网络能够互通进行访问**
- 如果是Nacos模式启动，注意启动参数`knife4j.nacos.namespace`的设定，要保持一致，否则会获取Nacos配置失败，导致聚合访问文档不成功
- `docker-compose.yml`中的`network_mode`模式使用的是`bridge`,开发者根据自身情况自己选择即可
- 如果聚合访问文档失败，可以查看程序logs日志，确定自己是否配置错误所导致,确认软件问题可以通过[issues](https://gitee.com/xiaoym/knife4j/issues)向作者反馈

:::


自2.0版本开始，不在提供发行版文件包的方式进行安装运行，推荐使用`docker-compose`的方式。


Knife4jInsight组件自2.0版本开始，数据源开启支持配置中心中间件的模式，因此，相较于原1.0版本中只支持本地磁盘的方式，在使用上会有所不同，这取决于开发者自己选择。

目前的版本中，主要两种模式：

- **Disk本地磁盘**：数据来源于本地磁盘，包括OpenAPI规范文件，或者所支持的集中模式配置文件
- **Nacos配置中心**：数据来源于Nacos中间件上，使用者只需要将配置在Nacos上进行操作即可。

两种模式所分别对应`docker-compose.yml`配置有所不同。

## 模式类别

### Disk本地磁盘


```yml title="docker-compose.yml"
# 通过docker-compose可以快速部署knife4j服务
version: "2.0"
services:
  knife4j:
    container_name: knife4j-insight
    restart: always
    image: "xiaoymin/knife4j:v2.0.1"
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
开发者需注意，此处volumes挂载目录`D:\Temp\data`仅供参考使用

如目录不同，可自行创建，windows/linux均可。

详细说明请参考[使用说明](desktop/config-disk)

### Nacos配置中心

> **注意:Nacos推荐使用版本 >= 2.0**



```yml title="docker-compose.yml"

# 通过docker-compose可以快速部署knife4j服务
version: "2.0"
services:
  knife4j:
    container_name: knife4j-insight-nacos
    restart: always
    # 访问
    image: "xiaoymin/knife4j:v2.0.1"
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

详细说明请参考[使用说明](desktop/config-nacos)



