# 3.6 接口排序

::: warning
增强功能需要通过配置yml配置文件开启增强,自2.0.6开始
```yml
knife4j:
  enable: true
```
:::


针对Controller下的具体接口,排序规则是使用`Knife4j`提供的增强注解`@ApiOperationSupport`中的order字段,代码示例如下：

```java
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