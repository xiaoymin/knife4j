# NumberFormatException for Springfox-swagger version 2.9.2

Many friends will encounter a NumberFormatException after upgrading Springfox-Swagger to version 2.9.2.

**java.lang.NumberFormatException: For input string: ""**

The exception information is as follows：

```properties
java.lang.NumberFormatException: For input string: ""
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65) ~[na:1.8.0_111]
	at java.lang.Long.parseLong(Long.java:601) ~[na:1.8.0_111]
	at java.lang.Long.valueOf(Long.java:803) ~[na:1.8.0_111]
	at  
	//more....
```

The solution is to exclude Springfox-Swagger's Swagger-Models jar package from pom.xml and reintroduce it as follows:

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


 
 
 