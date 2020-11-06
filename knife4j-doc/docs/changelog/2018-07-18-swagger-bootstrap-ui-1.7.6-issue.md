# [v1.7.6-2018/07/18 全局默认参数]

swagger-bootstrap-ui 1.7.6 发布了。swagger-bootstrap-ui 是 Swagger 的前端 UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿 

该版本基本是fixed版本，修复了很多bug，针对全局参数这种新特性，影响比较大,建议升级

swagger-bootstrap-ui 1.7.6 主要更新如下： 

1、fixed 全局默认参数，设置值无效问题

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.6-issue/n3.png)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.6-issue/n4.png)

2、add 简介页添加basePath属性

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.6-issue/n2.png)

3、fixed 响应类型是Ref引用属性，在响应json中未列出属性

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.6-issue/n1.png)

4、fixed 默认值未显示,swagger 2.9.2版本响应json的默认值为x-example属性

5、fixed tags存在大写的情况不显示接口 bug,在swagger2.9.2版本测试时，swagger又将后台的tags改为区分大小写了，所以建议升级swagger版本到最新

6、fixed 相同url地址，不同method类型，接口未展示bug

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.6-issue/n5.png)

7、fixed 请求参数为ref引用类型时，文档列出请求类型和schema类型一致，显示schema类型

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.6-issue/n6.png)

8、tip:推荐使用chrome浏览器，别的浏览器可能有js、css兼容问题，文档效果未到最佳

9、tip：建议swagger版本升级到2.9.2

```xml
<!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger2 -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
```

**Maven坐标**

```xml
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.7.6</version>
</dependency>
```
欢迎提BUG给我~~~~


**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 <icp/> 
 comment/> 
 
 
 
 