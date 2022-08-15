# springfox 源码分析(二) 初探mapstruct


时间：2019-5-22 12:40:21

地点：单位

## 前言

在继续阅读学习springfox源码之前,我们需要先来学习一下mapstruct这个组件,只有在理解了mapstruct组件后,后面再看springfox的源码才不会有疑惑

因为之前并没有接触过mapstruct这个组件,所以记录一下学习的过程.

## mapstruct

官网地址：[http://mapstruct.org/](http://mapstruct.org/)

GitHub:[https://github.com/mapstruct/mapstruct](https://github.com/mapstruct/mapstruct)

文档：[http://mapstruct.org/documentation/stable/reference/html/](http://mapstruct.org/documentation/stable/reference/html/)

一言一概之:**Java bean mappings, the easy way!**

### 简介

通过上面的最简单的一句话,很清晰的描述了mapstruct的作用,主要用于Java Bean的映射,这有点类似mybatis中的对象关系映射,但此处的mappings并非mybatis那样

MapStruct同时也是一个代码生成器，它基于约定优于配置方法极大地简化了Java bean类型之间映射的实现。

通过MapStruct生成的映射代码使用普通方法调用，因此快速，类型安全且易于理解。

### 作用

我们为什么需要`MapStruct`组件？

我们的应用程序通常会使用分层结构,分层时每层的对象会有不同的POJO对象(例如实体DO和业务DTO),实体DO定义了程序内部的逻辑属性,而DTO定义了外部业务逻辑关系

通常我们在应用程序通过DTO接受进来外部参数时,需要将之转化为内部DO对象,供内部调用,此时`MapStruct`组件正为此而生.

与其他映射框架相比，MapStruct在编译时生成bean映射，可确保高性能，允许快速的开发人员反馈和彻底的错误检查。

### 示例

先来看一组关于`MapStruct`的示例

在`maven`中引入`MapStruct`框架的jar包

```xml
<properties>
    <mapstruct.version>1.2.0.Final</mapstruct.version>
</properties>
<dependencies>
    <!-- https://mvnrepository.com/artifact/junit/junit -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.8.2</version>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-jdk8</artifactId>
        <version>${mapstruct.version}</version>
    </dependency>
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>${mapstruct.version}</version>
    </dependency>
</dependencies>
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.5.1</version> <!-- or newer version -->
            <configuration>
                <source>1.8</source> <!-- depending on your project -->
                <target>1.8</target> <!-- depending on your project -->
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${mapstruct.version}</version>
                    </path>
                    <!-- other annotation processors -->
                </annotationProcessorPaths>
                <compilerArgs>
                    <compilerArg>
                        -Amapstruct.suppressGeneratorTimestamp=true
                    </compilerArg>
                    <compilerArg>
                        -Amapstruct.suppressGeneratorVersionInfoComment=true
                    </compilerArg>
                </compilerArgs>
            </configuration>
        </plugin>
    </plugins>
</build>
```

假设我们拥有DO和业务DTO对象`Car.java`、`CarDTO.java`

`Car.java:`

```java
public class Car {

    private String name;
    private String make;
    private int numberOfSeats;
 	//getter and setter ,constructs   
}
```

`CarDTO.java`

```java
public class CarDTO {
    private int seatCount;
    //getter and setter ,constructs
}
```

按照传统的转换关系,如果我们不使用`MapStruct`框架的话,使用方式如下：

```java
Car car=new Car("c1","m1",12);
CarDTO cd=new CarDTO();
//赋值
cd.setSeatCount(car.getNumberOfSeats())；
```

按照以上的方式会有以下局限:

- 如果属性很多的话,需要写大量的赋值属性代码
- 代码极其繁琐

如果使用`MapStruct`框架,我们可以先声明一个`CarMapping`接口

```java
/***
 *
 * @since:spring-plugin-demo 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/05/21 21:40
 */
@Mapper
public interface CarMapper {

    CarMapper INSTANCE= Mappers.getMapper(CarMapper.class);

    @Mapping(source = "numberOfSeats", target = "seatCount")
    CarDTO carToCarDto(Car car);


}
```

我们将所有DO对象及DTO对象的映射关系都定义在Mapper接口中,然后通过`@Mapper`注解标注

而此时我们使用的话也会有所变化：

```java
 //given
Car car = new Car( "Morris", "4make", 13 );
//when
CarDTO carDto = CarMapper.INSTANCE.carToCarDto( car );
//then
System.out.println(carDto.getSeatCount());
```

此时，我们程序在compile编译时,`MapStruct`框架会为我们自动生成`CarMapper`的实现类：

![](/images/springfox/compile.png)

此时在`generated-source`也会生成相应的java文件

![](/images/springfox/compile1.png)

`CarMapperImpl.java`

```java

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-05-21T21:46:10+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_111 (Oracle Corporation)"
)
public class CarMapperImpl implements CarMapper {

    @Override
    public CarDTO carToCarDto(Car car) {
        if ( car == null ) {
            return null;
        }

        CarDTO carDTO = new CarDTO();

        carDTO.setSeatCount( car.getNumberOfSeats() );

        return carDTO;
    }
}
```

映射接口自动帮助我们实现了Java实体的转换

## 总结

了解了mapstruct的功能,我们在接下来学习springfox的源码时会轻松很多.