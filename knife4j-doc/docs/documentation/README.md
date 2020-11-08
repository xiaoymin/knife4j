Java开发使用`Knife4j`目前有一些不同的版本变化,详见[版本说明](changelog.md)，主要如下：

1、如果开发者继续使用OpenAPI2的规范结构，底层框架依赖springfox2.10.5版本，那么可以考虑`Knife4j`的2.x版本

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <!--在引用时请在maven中央仓库搜索2.X最新版本号-->
    <version>2.0.7</version>
</dependency>
```

2、如果开发者使用OpenAPI3的结构，底层框架依赖springfox3.0.0,可以考虑`Knife4j`的3.x版本

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <!--在引用时请在maven中央仓库搜索3.X最新版本号-->
    <version>3.0.1</version>
</dependency>
```

3、如果开发者底层框架使用的是`springdoc-openapi`框架,则需要使用`Knife4j`提供的对应版本,需要注意的是该版本没有`Knife4j`提供的增强功能，是一个纯Ui。

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-springdoc-ui</artifactId>
    <!--在引用时请在maven中央仓库搜索3.X最新版本号-->
    <version>3.0.1</version>
</dependency>
```

