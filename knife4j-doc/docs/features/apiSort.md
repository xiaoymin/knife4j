# 3.6 接口排序

:::caution 温馨提醒
1、增强功能需要通过配置yml配置文件开启增强,自2.0.6开始
```yml
knife4j:
  enable: true
```
2、自Knife4j 4.0版本，开发者必须使用`knife4j-openapi2-spring-boot-starter`组件才生效

3、如果使用openapi3版本，那么Knife4j 版本>=4.1.0
:::

## 基础使用

针对Controller下的具体接口,排序规则是使用`Knife4j`提供的增强注解`@ApiOperationSupport`中的order字段,代码示例如下：

```javascript
@ApiOperationSupport(order = 33)
@ApiOperation(value = "忽略参数值-Form类型")
@PostMapping("/ex")
public Rest<LongUser> findAll(LongUser longUser) {
    Rest<LongUser> r=new Rest<>();
    r.setData(longUser);
    return r;
}
```
 
Knife4j通过Spring Plugin插件体系,对每个接口进行扫描,最终将扫描的`@ApiOperationSupport`注解获取的`order`值通过OpenAPI的扩展属性规范进行赋值

最终在OpenAPI的规范中，接口的path节点下,通过`x-order`属性得到接口的排序，最终前端根据排序值进行排序(顺序)，如下图：

![](/knife4j/images/documentation/apiorder.png)

开发者如果遇到排序不生效的问题，可以通过检查接口返回的OpenAPI规范中，接口`path`节点下是否包含`x-order`的扩展属性


## springdoc-openapi版本适配


开发者使用Knife4j针对springdoc-openapi版本的情况下，必须使用Knife4j 版本>=4.1.0版本，这里会有几个注意事项：

1、开发者如果自己通过springdoc-openapi的情况下设置了排序，代码如下：

```yml
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    # 该参数是swagger默认的排序规则，如果设置为alpha，那么Knife4j提供的按照order排序的增强规则不生效
    operations-sorter: alpha
```

2、修改配置

```yml
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    # 使用增强order属性进行排序，或者不设置该参数
    operations-sorter: order
```


## knife4j-gateway版本聚合的order不生效

在目前knife4j提供的[gateway](../middleware-sources/spring-cloud-gateway/spring-gateway-introduction)网关聚合组件中，也会出现order排序不生效的问题

主要原因是在Knife4j网关服务端代码中，将分组的接口排序规则硬编码了，代码如下：


```javascript title="knife4j-gateway-spring-boot-starter/com/github/xiaoymin/knife4j/spring/gateway/spec/v3/OpenAPI3Response.java"

public class OpenAPI3Response implements Serializable {
    
    /**
     * ConfigUrl，eg: /v3/api-docs/swagger-config
     */
    private String configUrl;
    /**
     * oauth2RedirectUrl,eg : http://192.168.10.103:17812/swagger-ui/oauth2-redirect.html
     */
    private String oauth2RedirectUrl;
    
    /**
     * operation接口排序规则
     */
    private String operationsSorter = "alpha";
    
    /**
     * tag排序规则,此处硬编码导致排序order不生效
     */
    private String tagsSorter = "alpha";

    // other....

}

```

这个在未来4.2.0版本中会修复该问题

临时的解决办法(**投机取巧**)，按当前knife4j的**一样的包路径**，在项目代码中**重写该实体类**，修改为如下：

```javascript title="knife4j-gateway-spring-boot-starter/com/github/xiaoymin/knife4j/spring/gateway/spec/v3/OpenAPI3Response.java"

public class OpenAPI3Response implements Serializable {
    
    /**
     * ConfigUrl，eg: /v3/api-docs/swagger-config
     */
    private String configUrl;
    /**
     * oauth2RedirectUrl,eg : http://192.168.10.103:17812/swagger-ui/oauth2-redirect.html
     */
    private String oauth2RedirectUrl;
    
    /**
     * operation接口排序规则
     */
    private String operationsSorter = "alpha";
    
    /**
     * 修改为order
     */
    private String tagsSorter = "order";

    // other....

}

```

完整实体类代码参考[OpenAPI3Response.java](https://gitee.com/xiaoym/knife4j/blob/dev/knife4j/knife4j-gateway-spring-boot-starter/src/main/java/com/github/xiaoymin/knife4j/spring/gateway/spec/v3/OpenAPI3Response.java)
