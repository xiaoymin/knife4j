::: danger 
1、目前已经发行的Knife4j版本，Knife4j本身已经引入了springfox，开发者在使用时不用再单独引入Springfox的具体版本，否额会导致版本冲突。另外在网关层聚合(例如gateway)时，必须禁用Knife4j的增强模式

2、使用Knife4j2.0.6及以上的版本，Spring Boot的版本必须大于等于`2.2.x`

3、微服务聚合组件Knife4jAggregation强势发布，聚合OpenAPI文档太简单了,[详见文档](knife4jAggregation.md)

:::

