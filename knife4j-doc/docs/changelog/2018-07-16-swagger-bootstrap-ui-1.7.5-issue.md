# [v1.7.5-2018/07/16 Ui全面改版]

swagger-bootstrap-ui 1.7.5 发布了。swagger-bootstrap-ui 是 Swagger 的前端 UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿 

本版本,swagger-bootstrap-ui核心JS组件进行重构,新版本中，文档呈现将剔除原table的展现方式,以markdown格式展现

swagger-bootstrap-ui 1.7.5 主要更新如下： 

- 重构DApiUI.js功能,新版本使用SwaggerBootstrapUi.js，方便后期扩展,同时删除无效js、css、html文件,新版本jar包由原760kb缩小至295kb

- 重构文档页面，剔除原来table展现方式，新版本使用markdown格式展现文档,单个文档页可复制
  ![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.5-issue/n1.png)
  ![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.5-issue/n2.png)
  ![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.5-issue/n3.png)

- 新增全局参数配置功能，针对请求参数有全局参数情况下，方便在线调试

  ![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.5-issue/n4.png)

- 支持离线文档格式,生成markdown格式文档,供开发者对外生成静态文档

  ![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.5-issue/n5.png)

  通过markdown转换工具Typora预览效果

  ![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.5-issue/n6.png)

  Typora导出pdf预览效果

  ![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.5-issue/n7.png)

- 添加clipboard插件,离线文档可复制功能

- 正式发布版去除console打印调试信息

- fixed 调试页面去除url根路径/,项目名称非ROOT，或分布式情况下路径不对，多一个"/"的问题  

- fixed RequestBody 接收实体对象，对象属性中有List属性时,参数显示array，需解析对象属性显示，方便查看

- fixed 对象属性展示为string，属性未显示

- tip:推荐使用chrome浏览器，别的浏览器可能有js、css兼容问题，文档效果未到最佳

**Maven坐标**

```xml
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.7.5</version>
</dependency>
```


**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 <icp/> 
 comment/> 
 
 
 
 