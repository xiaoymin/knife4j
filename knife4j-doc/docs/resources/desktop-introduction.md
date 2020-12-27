# 2.1 介绍

Knife4jAggregationDesktop是一款基于聚合组件Knife4jAggregation特性的独立部署的聚合OpenAPI文档软件，脱离Spring、Spring Boot技术架构体系，开发者下载后独立部署启动。开发者可以理解为能够渲染OpenAPI规范的独立文档应用

主要功能作用：

- 独立部署(依赖Java JDK8环境)
- 拥有[Knife4jAggregation](aggregation-introduction.md)的全部特性
- 基于动态文件配置方式
- 支持多个项目动态配置


视频介绍：
 
- [第一篇 Knife4jAggregationDesktop介绍](https://www.bilibili.com/video/BV14z4y1r7e9/)
- [第二篇 Knige4jAggregationDesktiop安装和使用说明](https://www.bilibili.com/video/BV1xV411b7Fe/)
- [第三篇 Knife4jAggregationDesktop使用-Disk模式](https://www.bilibili.com/video/BV1XA411s73b/)
- [第四篇 Knife4jAggregationDesktop使用-Cloud模式](https://www.bilibili.com/video/BV14y4y1i7nu/)
- [第五篇 Knife4jAggregationDesktop使用-Eureka模式](https://www.bilibili.com/video/BV1Cy4y1i7B5/)
- [第六篇 Knife4jAggregationDesktop使用-Nacos模式](https://www.bilibili.com/video/BV1zh411f7pz/)

## 2.1.1 软件架构

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
- `bin`:启动命令目录,并且包含启动jar包文件
- `conf`:配置文件目录，`application.properties`包含`Knife4jAggregationDesktop`软件的相关配置，包括端口号，为文档设置basicAuth权限等
- `data`:数据目录，默认根目录存放`ROOT`文件夹,多个项目的OpenAPI聚合，开发者只需要在此目录下建文件夹即可
- `lib`:依赖jar包
- `logs`:日志
- `webapps`:Knife4jUi的静态资源文件


## 2.1.2 配置文件

在`conf`文件夹下有`application.properties`配置文件，是`Knife4jAggregationDesktop`软件的独立配置，主要包含启动端口号,资源配置等信息

> 1、一般情况下,开发者无需更改该配置,如果端口号和系统中的已存在应用存在冲突,那么开发者启动之前进行更改
>
> 2、更改该配置后,应用需要重启才能生效

目前的配置属性如下：
```properties
# Knife4jAggregationDesktop 启动端口号
knife4j.port=18006
# MetaDataMonitor组件刷新频率,单位:毫秒(data文件夹变更时触发Knife4jAggregationDesktop将从硬盘properties配置文件加载文档),默认是5000毫秒
knife4j.duration=5000
# 静态资源访问后缀,主要是配置在webapp目录下的资源
knife4j.statics=gif,png,bmp,jpeg,jpg,html,htm,shtml,mp3,wma,flv,mp4,wmv,ogg,avi,doc,docx,xls,xlsx,ppt,txt,pdf,zip,exe,tat,ico,css,js,swf,apk,ts,m3u8,json

# 给所有文档加权,默认不启用,当个文档的加权访问操作,开发者应该配置在单个项目的配置文件中
knife4j.basic.enable=false
knife4j.basic.username=test
knife4j.basic.password=1234

```
