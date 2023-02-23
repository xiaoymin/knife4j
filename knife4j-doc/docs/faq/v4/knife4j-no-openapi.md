# No OpenAPI resource found for group: swagger-config


很多朋友在升级到Knife4j 4.0的OpenAPI3版本中，会出现该错误信息,导致页面加载失败

```shell
No OpenAPI resource found for group: swagger-config
```

解决办法是再添加一个`springdoc-openapi-ui`的依赖即可，Maven坐标如下：

```xml
<dependency>
  <groupId>com.github.xiaoymin</groupId>
  <artifactId>knife4j-openapi3-spring-boot-starter</artifactId>
  <version>4.0.0</version>
</dependency>

<!--再添加一个jar-->
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-ui</artifactId>
  <!--保持版本与Knife4j v4.0的版本一致，避免jar包冲突，因为Knife4j-v4.0.0版本依赖的springdoc版本是1.6.9 -->
  <version>1.6.9</version>
</dependency>
```


该问题会在之后的4.1版本中把依赖添加进去，这样开发者就不必再单独引入了。在4.1版本没发版本之前，先临时这么解决吧~