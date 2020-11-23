# 自定义文档


::: warning
自定义文档是`knife4j`提供的增强功能,开发者要想使用`knife4j`提供的增强功能,必须在Swagger的配置文件中引入增强注解,各个版本的增强注解区别如下表:<br />
:::

|软件|版本|增强注解|说明|
|--|--|--|--|
|knife4j|<=`2.0.0`|`@EnableSwaggerBootstrapUi`||
|knife4j|>=`2.0.1`|`@EnableKnife4j`|后续版本不会再更改|

- 在使用`knife4j`的<=`2.0.0`版本之前的代码方式,代码示例如下：
```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUi
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    
 //more..

}
```
- 在使用`knife4j`的>=`2.0.1`版本之后的代码方式,代码示例如下：
```java
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    
 //more..

}
```

后端开启增强注解后,还需要再前端个性化配置中勾选启用增强,整个配置才生效，如下图：

![](/knife4j/images/knife4j/enableknife4j.png)


## 效果说明

`knife4j`为了满足文档的个性化配置,添加了自定义文档功能

开发者可自定义`md`文件扩展补充整个系统的文档说明

开发者可以在当前项目中添加一个文件夹，文件夹中存放`.md`格式的markdown文件,每个`.md`文档代表一份自定义文档说明

**注意**：自定义文档说明必须以`.md`结尾的文件,其他格式文件会被忽略

例如项目结构如下：

![](/knife4j/images/1-9-3/construct.png)

每个`.md`文件中，`swagger-bootstrap-ui`允许一级(h1)、二级(h2)、三级(h3)标题作为最终的文档标题

比如`api.md`文档：

```markdown
# 自定义文档说明

## 效果说明

`knife4j`为了满足文档的个性化配置,添加了自定义文档功能

开发者可自定义`md`文件扩展补充整个系统的文档说明

开发者可以在当前项目中添加一个文件夹，文件夹中存放`.md`格式的markdown文件,每个`.md`文档代表一份自定义文档说明

**注意**：自定义文档说明必须以`.md`结尾的文件,其他格式文件会被忽略
```

最终在`knife4j`的界面中,`api.md`的文档标题会是`自定义文档说明`

整个文档效果如下：

![](/knife4j/images/knife4j/self-doc1.png)

如果没有按照一级(h1)、二级(h2)、三级(h3)来设置标题,默认标题会是文件名称，如图上的`api2.md`

## 如何使用

### Spring Boot环境

在SpringBoot环境中,首先需要在`application.yml`或者`application.properties`配置文件中配置自定义文档目录

如下：

```yml
knife4j:
  markdowns: classpath:markdown/*
```

然后在Swagger的配置文件中启用`@EnableKnife4j`注解

如下代码：

```java
@Configuration
@EnableSwagger2
@EnableKnife4j
public class SwaggerConfiguration {
    //more...
    
}
```

## demo示例

以上两种不同环境的demo示例可参考[swagger-bootstrap-ui-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo)
 
 
 