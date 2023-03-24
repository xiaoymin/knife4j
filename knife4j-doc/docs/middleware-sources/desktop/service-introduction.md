# 数据源-服务中心

不管是单体架构、微服务架构等，每一个子服务只要集成了OpenAPI框架，都可以通过Insight组件进行聚合，这和语言无关

架构图：

![](/images/website/insight/knife4j-insight.png)


主要是上面架构图中的Service Data部分。

我们知道，目前的服务中心中间件种类非常多，开源的、商业版，或者企业自研版本，从时间和适用性考虑，目前Insight只对接了[Nacos](https://nacos.io/zh-cn/)、Eureka服务中心，如果有朋友有兴趣贡献其他服务中心的代码实现，非常欢迎！！！

> 其核心原理很简单，从服务中心拿到注册服务的真实ip、端口，最终通过HTTP连接代理请求最终的数据源信息，拿到我们所需要的OpenAPI规范数据结构。



主要服务类型：

- **Disk模式:**Disk则代表用户将所有的OpenAPI数据结构文件全部存放到本地
- **Cloud模式：**所有OpenAPI数据结构来源于外部HTTP RESTFul接口
- **Nacos模式:**Nacos服务中心，因为微服务或者容器时代，ip、端口不定，所以可以配置从服务中心进行获取
- **Eureka模式:**Eureka服务中心,因为微服务或者容器时代，ip、端口不定，所以可以配置从服务中心进行获取