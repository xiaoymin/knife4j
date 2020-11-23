# 2.6 版本说明

在更名为`Knife4j`之前,原来的名称是叫`swagger-bootstrap-ui`，这是两种不一样风格的Ui,对比情况如下：

| 软件                 | 开发语言&框架      | 状态       | 最后版本 |风格|
| -------------------- | ------------------ | ---------- | -------- |------ |
| Knife4j              | Java、JavaScript、Vue | 持续更新中 | 无       |黑色|
| swagger-bootstrap-ui | Java、JavaScript、jQuery | 停更       | 1.9.6    |蓝色|


Knife4j从开源至今,目前主要经历版本的变化，分别如下：
|版本|说明|
|--|--|
|1.9.6|蓝色皮肤风格,开始更名，增加更多后端模块|
|2.0~2.0.5|Ui重写，底层依赖的springfox框架版本是`2.9.2`|
|2.0.6~|底层springfox框架版本升级知`2.10.5`,OpenAPI规范是v2|
|3.0~|底层依赖springfox框架版本升级至`3.0.3`,OpenAPI规范是v3|

需要注意的是，目前Knife4j的主版本依然是沿用2.x的版本号，也就是从2.0.6版本开始逐步升级，迭代发布时版本会随之升级，但同时3.x版本也会同步更新发布，主要是满足开发者对于springfox3以及OpenAPI3规范的使用
::: danger 特别注意
1、目前已经发行的Knife4j版本，Knife4j本身已经引入了springfox，开发者在使用时不用再单独引入Springfox的具体版本，否额会导致版本冲突。另外在网关层聚合(例如gateway)时，必须禁用Knife4j的增强模式

2、使用Knife4j2.0.6及以上的版本，Spring Boot的版本必须大于等于`2.2.x`
:::

自2.0.6版本开始，2.x与3.x的版本主要变化是底层springfox所引用的版本不同，但Knife4j提供的Ui其实是同一个，同时兼容OpenAPI2以及OpenAPI3规范，源码请参考[knife4j-vue](https://gitee.com/xiaoym/knife4j/tree/master/knife4j-vue)，如果开发者依然想沿用以前Knife4j一直以来发布的2.x版本，请继续更随Knife4j的更新步伐使用2.x的版本即可，如果开发者想尝鲜，则可以考虑3.x的版本
::: tip
3.x的版本依赖springfox3.0.0，springfox3.0目前也只更新发布了一个版本，从功能稳定性来说，可能不如2.x系列，所以开发者慎重使用，当然,如果是Ui上的一些功能性问题或者Bug，也欢迎开发者向Knife4j[发起ISSUE](https://gitee.com/xiaoym/knife4j/issues/new)，等springfox3版本趋于稳定后，knife4j的2.x版本就不会在更新，会并向3.x
:::

具体的对应关系如下：
|2.x版本|3.x版本|
|--|--|
|2.0.6|3.0|
|2.0.7|3.0.1|
|2.0.8|3.0.2|
|以此类推...|以此类推...|

如果开发者底层框架使用的是springdoc-openapi框架而非springfox,则需要使用Knife4j提供的对应3.x版本,需要注意的是该版本没有Knife4j提供的部分增强功能，是一个纯Ui。
```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-springdoc-ui</artifactId>
    <!--在引用时请在maven中央仓库搜索3.X最新版本号-->
    <version>3.0.2</version>
</dependency>
```

每一个版本的更详细的更新日志，开发者可参考[更新日志](../changelog)
