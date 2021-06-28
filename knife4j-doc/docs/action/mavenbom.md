# 1.1 基于Maven Bom方式使用

::: warning
该方式自`2.0.2`版本才提供,在`knife4j`以前的版本中没有`knife4j-dependencies`模块,开发者在使用的时候需要注意
:::

`knife4j`自`2.0.2`版本中提供了`knife4j-dependencies`模块,该模块是对`knife4j`所使用的jar包进行管理,开发者如果想以maven bom的方式使用`knife4j`,可以引用该jar包，避免版本冲突

在Maven项目的`pom.xml`文件中引入该依赖,如下：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-dependencies</artifactId>
            <!--在引用时请在maven中央仓库搜索2.X最新版本号-->
            <version>2.0.9</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```


导入以上配置后,在maven的`dependencies`节点中就可以直接引用`knife4j`提供的模块,无需指定版本号,如下：


在Spring Boot项目中引入`knife4j-spring-boot-starter`模块

```xml
<dependencies>
    <dependency>
        <groupId>com.github.xiaoymin</groupId>
        <artifactId>knife4j-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

 
 
 