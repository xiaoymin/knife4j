# java.lang.NumberFormatException: For input string: ""

很多朋友在升级Springfox-Swagger到2.9.2版本后会碰见NumberFormatException异常**java.lang.NumberFormatException: For input string: ""**

异常信息如下：

```properties
java.lang.NumberFormatException: For input string: ""
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65) ~[na:1.8.0_111]
	at java.lang.Long.parseLong(Long.java:601) ~[na:1.8.0_111]
	at java.lang.Long.valueOf(Long.java:803) ~[na:1.8.0_111]
	at  
	//more....
```

解决办法是在pom.xml中排除Springfox-Swagger的Swagger-Models的jar包,重新引入，如下：

```xml
<!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger2 -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
    <exclusions>
        <exclusion>
            <groupId>io.swagger</groupId>
            <artifactId>swagger-models</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<!-- https://mvnrepository.com/artifact/io.swagger/swagger-models -->
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-models</artifactId>
    <version>1.5.21</version>
</dependency>
```


 
 
 