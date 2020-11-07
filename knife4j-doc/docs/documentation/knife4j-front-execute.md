# 5.3 执行步骤解析


`knife4j`的整个执行初始化过程如下图：

![](/knife4j/images/knife4j/execute.png)

上图中的蓝色接口(全部是springfox框架提供)说明如下：

- `/swagger-resources/configuration/ui`:该接口是Swagger的个性化配置接口,包括Swagger的排序,支持接口的调试类型等配置
- `/swagger-resources`:该接口是我们在Swagger的界面中见到的分组下拉框数据初始化接口,在当前微服务盛行的情况下,你可以把他理解为一个服务(例如用户服务、订单服务等)
- `/v2/api-docs?groupName=default`:该接口是单个服务下的Swagger实例,返回的数据是一个完整的Swagger V2版本的JSON结构

::: warning
注意,以上只是Java技术体系所用到的接口说明,在这里列出来单独说明,只是希望非Java技术体系的开发者能够明白目前`knife4j`所做的封装调用工作

后面的篇幅会提到如何基于源码来进行接口地址更改,地址的更改很方便,但是最重要的是每个接口所代表的意义。
:::


接下来,我们将介绍如何基于源码的方式改造得到front版本,请参考[下一篇](knife4j-front-source-modified.md)

 
 
 