# 1.2 Disk模式

> 更加详细的实战demo请参考[基于Disk模式聚合OpenAPI](../action/aggregation-disk.md)

Disk模式即代表你拥有OpenAPI规范的静态JSON文件,那么可以考虑使用Knife4jAggregation组件进行聚合展示,目前只支持JSON文件的格式

Disk模式的配置如下：

```yml
knife4j:
  enableAggregation: true
  disk:
    enable: true
    routes:
      - name: 用户
        location: classpath:openapi/user.json
```

- `knife4j.disk.enable`:将该属性设置为true，则代表启用Disk模式
- `knife4j.disk.routes`:需要聚合的服务集合，可以配置多个
- `knife4j.disk.routes.name`:服务名称(显示名称，最终在Ui的左上角下拉框进行显示)
- `knife4j.disk.routes.location`:本地Disk模式聚合的OpenAPI规范JSON文件,可以是V2也可以是V3版本
