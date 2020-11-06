# 部署手册

knife4j-admin采用前后端分离的方式进行部署

java环境

```shell
[root@iZbt ~]# java -version
java version "1.8.0_144"
Java(TM) SE Runtime Environment (build 1.8.0_144-b01)
Java HotSpot(TM) 64-Bit Server VM (build 25.144-b01, mixed mode)
```

Linux:

```shell
[root@iZbt ~]# lsb_release -a
LSB Version:    :core-4.1-amd64:core-4.1-noarch
Distributor ID:    CentOS
Description:    CentOS Linux release 7.4.1708 (Core) 
Release:    7.4.1708
Codename:    Core
```

## 安装Nginx

服务端必须安装Nginx，具体的安装教程可以自行搜索,这里不再说明.

## 下载最新发行版

前往[发行版地址](knife4j-admin-download.md)进行下载

发行版一般是以zip压缩包形式进行发布,压缩包中包含jar可运行文件以及一个front的前端文件

## 安装部署

假设部署路径在服务器的`/mnt/application/kadmin`目录下

文件的整体结构如下：

```shell
|-kadmin
|----data    //数据目录,可以为空,在部署时该目录必须先手工创建
|----front   //由zip包解压得到
|----logs    //程序的日志目录,在部署时该目录必须先手工创建
|----startup.sh //启动脚本,必须使用chmod+x startup.sh 命令赋予可执行权限
|----knife4j-admin-1.0.jar //由zip包解压得到
```

### 修改配置文件

在knife4j-admin程序中,如果你的部署目录不是`/mnt/application/kadmin`，那么需要修改jar包中的`application-prod.yml`配置文件

`application-prod.yml`:

```yml
server:
# 端口号
  port: 17808 
knife4j:
  # 监听项目目录 
  monitor: /mnt/application/kadmin/data 
```

必须修改`knife4j.monitor`中的监测目录,该目录是可以由开发者自行更改,更改后在替换jar包中的文件即可

当然,程序的端口号开发者也可自行更改

### 启动程序

knife4j-admin提供的是一个Spring Boot的可执行jar包,因此,`startup.sh`的可执行文件内容如下：

```shell
nohup java -Xms512m -Xmx512m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m -XX:MaxNewSize=256m -jar /mnt/application/kadmin/knife4j-admin-1.0.jar >> /dev/null 2>&1 &
```

通过Linux终端执行命令进行启动

```shell
[root@izbt kadmin]# ./startup.sh
```

### 配置Nginx

当我们的后台服务启动好之后,我们还需要配置一个对外的Nginx端口

`kadmin.conf`：

```nginx
server {
    listen       80;
    server_name  kadmin.xiaominfo.com;
    # 后台服务
    location / {
        proxy_pass http://127.0.0.1:17808;
        client_max_body_size 300m;
    }
    # 前端文件
	location ~*^.+\.(icon|gif|jpg|jpeg|png|html|css|js|txt|xml|swf|wav)$ {
	    root /mnt/application/kadmin/front;
	}

}
```

此Nginx的配置是目前knife4j提供的测试地址,开发者如果没有域名,可以自行使用端口号等进行配置即可

### 启动Nginx

Nginx的环境配置好后,启动Nginx即可

服务启动

```shell
[root@izbt kadmin]# service nginx start
```

如果Nginx未安装成系统服务,则可以命令行启动

```shell
[root@izbt kadmin]# cd /usr/local/nginx
[root@izbt nginx]# sbin/nginx
```


 
 
 