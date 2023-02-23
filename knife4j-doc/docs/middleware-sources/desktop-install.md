import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

#  安装


自2.0版本开始，不在提供发行版文件包的方式进行安装运行，推荐使用`docker-compose`的方式。


Desktop组件自2.0版本开始，数据源开启支持配置中心中间件的模式，因此，相较于原1.0版本中只支持本地磁盘的方式，在使用上会有所不同，这取决于开发者自己选择。

目前的版本中，主要两种模式：

- **Disk本地磁盘**：数据来源于本地磁盘，包括OpenAPI规范文件，或者所支持的集中模式配置文件
- **Nacos配置中心**：数据来源于Nacos中间件上，使用者只需要将配置在Nacos上进行操作即可。

两种模式所分别对应`docker-compose.yml`配置有所不同。


## Disk本地磁盘


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
开发者需注意，此处volumes挂载目录`D:\Temp\data`仅供参考使用

如目录不同，可自行创建，windows/linux均可。

详细说明请参考[使用说明](desktop/config-disk)

## Nacos配置中心

> **注意:Nacos推荐使用版本 >= 2.0**



```yml title="docker-compose.yml"

# 通过docker-compose可以快速部署knife4j服务
version: "2.0"
services:
  knife4j:
    container_name: knife4j-desktop-nacos
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

详细说明请参考[使用说明](desktop/config-nacos)