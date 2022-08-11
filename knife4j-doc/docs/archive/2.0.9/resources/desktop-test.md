# 2.4 测试场景

本文主要包含作者对`Knife4jAggregationDesktop`的测试场景,主要包含支持的四种模式进行分别测试,仅供使用者进行参考

> **注意**:以下测试场景无需重启服务

## 2.3.1 Disk模式

> [视频介绍-Knife4jAggregationDesktop使用-Disk模式](https://www.bilibili.com/video/BV1XA411s73b/)

1、在`data`目录下新增文件夹并且修改符合Desktop要求的名称格式，直接放入OpenAPI规范的静态JSON文件，不重启的情况下渲染正常(<font style="color:red">正常</font>)

2、修改某个文件夹下的OpenAPI规范静态JSON文件,不重启的情况下等待5s后刷新文档是否变更(<font style="color:red">正常</font>)

3、为disk模式创建`disk.properties`,为disk模式配置服务的重命名是否生效(<font style="color:red">正常</font>)

4、修改`disk.properties`配置文件，修改服务的名称显示是否生效(<font style="color:red">正常</font>)

5、修改`disk.properties`配置文件,为服务配置`host`开启调试功能，调试是否生效(<font style="color:red">正常</font>)

```properties
knife4j.disk.routes[0].name=测试平台
knife4j.disk.routes[0].host=http://knife4j.xiaominfo.com
knife4j.disk.routes[0].location=test.json
```

6、修改`disk.properties`配置文件,取消调试配置`host`的属性，调试是否失效(<font style="color:red">正常</font>)

7、删除`disk.properties`配置文件，刷新是否正常(<font style="color:red">正常</font>)

8、修改`disk.properties`配置文件，配置`basicAuth`以开启文档保护策略，是否生效(<font style="color:red">正常</font>)

```properties
knife4j.basicAuth.enable=true
knife4j.basicAuth.username=zhangsan
knife4j.basicAuth.password=123456
```

9、修改`disk.properties`配置文件，配置`basicAuth`以开启文档保护策略后，访问其他项目的文档是否无影响能正常访问(<font style="color:red">正常</font>)

10、修改`disk.properties`配置文件，删除`basicAuth`去除文档的保护策略，是否生效(<font style="color:red">正常</font>)

11、修改`disk.properties`配置文件，为`routes`节点配置`servicePath`属性，是否能正常渲染及调试(<font style="color:red">正常</font>)

```properties
knife4j.disk.routes[0].name=测试平台
knife4j.disk.routes[0].host=http://knife4j.xiaominfo.com
knife4j.disk.routes[0].location=test.json
knife4j.disk.routes[0].servicePath=/niubia
```

12、在`data`目录下新增文件夹并且修改符合Desktop要求的名称格式，直接放入OpenAPI规范的静态`yml`文件，是否渲染正常(<font style="color:red">正常</font>)

13、按规定放入`yml`及`json`两种OpenAPI规范的静态文件，是否渲染正常(<font style="color:red">正常</font>)

14、创建`disk.properties`配置文件,对于yml和json两种格式的文件进行重命名配置,是否渲染生效(<font style="color:red">正常</font>)

```properties
knife4j.disk.routes[0].name=yml格式
knife4j.disk.routes[0].location=33.yml
knife4j.disk.routes[0].order=1
knife4j.disk.routes[1].name=json格式
knife4j.disk.routes[1].location=DolphinScheduler.json
knife4j.disk.routes[1].order=2
```

供参考示例配置`disk.properties`:

```properties
knife4j.disk.routes[0].name=测试平台
knife4j.disk.routes[0].host=http://knife4j.xiaominfo.com
knife4j.disk.routes[0].location=test.json
knife4j.disk.routes[0].servicePath=/niubia
knife4j.disk.routes[1].name=用户体系
knife4j.disk.routes[1].host=http://knife4j.xiaominfo.com
knife4j.disk.routes[1].location=user.json
#knife4j.basicAuth.enable=true
#knife4j.basicAuth.username=zhangsan
#knife4j.basicAuth.password=123456
```

## 2.3.2 Cloud模式

> [视频介绍-Knife4jAggregationDesktop使用-Cloud模式](https://www.bilibili.com/video/BV14y4y1i7nu/)

1、在`data`目录下新增文件夹并且修改符合Desktop要求的名称格式，新建`cloud.properties`配置文件及配置，是否生效(<font style="color:red">正常</font>)

```properties
knife4j.cloud.routes[0].name=大数据平台1
knife4j.cloud.routes[0].location=/v2/api-docs?group=3.默认接口
knife4j.cloud.routes[0].uri=http://knife4j.xiaominfo.com
knife4j.cloud.routes[1].name=甄选平台
knife4j.cloud.routes[1].location=/v2/api-docs?group=2.X版本
knife4j.cloud.routes[1].uri=http://knife4j.xiaominfo.com
```

2、修改`cloud.properties`配置文件，增加排序字段，排序(正序)是否生效(<font style="color:red">正常</font>)

```properties
knife4j.cloud.routes[0].name=大数据平台1
knife4j.cloud.routes[0].order=1
knife4j.cloud.routes[0].location=/v2/api-docs?group=3.默认接口
knife4j.cloud.routes[0].uri=http://knife4j.xiaominfo.com
knife4j.cloud.routes[1].name=甄选平台
knife4j.cloud.routes[1].order=2
knife4j.cloud.routes[1].location=/v2/api-docs?group=2.X版本
knife4j.cloud.routes[1].uri=http://knife4j.xiaominfo.com
```

3、调试是否正常(<font style="color:red">正常</font>)

4、修改`cloud.properties`配置文件，配置`basicAuth`以开启文档保护策略，是否生效(<font style="color:red">正常</font>)

```properties
knife4j.basicAuth.enable=true
knife4j.basicAuth.username=cloud
knife4j.basicAuth.password=123456
```

5、修改`cloud.properties`配置文件，配置`basicAuth`以开启文档保护策略后，访问其他项目的文档是否无影响能正常访问(<font style="color:red">正常</font>)

6、修改`cloud.properties`配置文件，删除`basicAuth`去除文档的保护策略，是否生效(<font style="color:red">正常</font>)

7、修改`cloud.properties`配置文件，为`routes`节点配置`servicePath`属性，是否能正常渲染及调试(<font style="color:red">正常</font>)

参考示例配置`cloud.properties`:

```properties
knife4j.cloud.routes[0].name=大数据平台1
knife4j.cloud.routes[0].order=12
knife4j.cloud.routes[0].location=/v2/api-docs?group=3.默认接口
knife4j.cloud.routes[0].uri=http://knife4j.xiaominfo.com
#knife4j.cloud.routes[0].servicePath=/niub
knife4j.cloud.routes[1].name=甄选平台
knife4j.cloud.routes[1].order=2
knife4j.cloud.routes[1].location=/v2/api-docs?group=2.X版本
knife4j.cloud.routes[1].uri=http://knife4j.xiaominfo.com
#knife4j.cloud.routes[1].servicePath=/cloud
#knife4j.basicAuth.enable=true
#knife4j.basicAuth.username=cloud
#knife4j.basicAuth.password=123456
```

## 2.3.3 Eureka模式

> [视频介绍-Knife4jAggregationDesktop使用-Eureka模式](https://www.bilibili.com/video/BV1Cy4y1i7B5/)

1、在`data`目录下新增文件夹并且修改符合Desktop要求的名称格式，新建`eureka.properties`配置文件及配置，是否生效(<font style="color:red">正常</font>)

```properties
knife4j.eureka.serviceUrl=http://localhost:10000/eureka/
knife4j.eureka.routes[0].name=用户服务asdf
knife4j.eureka.routes[0].serviceName=service-user
knife4j.eureka.routes[0].location=/aub/v2/api-docs?group=default
knife4j.eureka.routes[1].name=订单服务2ff
knife4j.eureka.routes[1].serviceName=service-order
knife4j.eureka.routes[1].location=/v2/api-docs?group=default
```

2、修改`eureka.properties`配置文件，增加order属性修改分组显示顺序，是否正常(<font style="color:red">正常</font>)

```properties
knife4j.eureka.serviceUrl=http://localhost:10000/eureka/
knife4j.eureka.routes[0].name=用户服务asdf
knife4j.eureka.routes[0].serviceName=service-user
knife4j.eureka.routes[0].location=/aub/v2/api-docs?group=default
knife4j.eureka.routes[0].order=1
knife4j.eureka.routes[1].name=订单服务2ff
knife4j.eureka.routes[1].serviceName=service-order
knife4j.eureka.routes[1].location=/v2/api-docs?group=default
knife4j.eureka.routes[1].order=2
```

3、修改`eureka.properties`配置文件，修改服务的显示名称`name`,文档界面是否正常显示(<font style="color:red">正常</font>)

```properties
knife4j.eureka.serviceUrl=http://localhost:10000/eureka/
knife4j.eureka.routes[0].name=自定义服务1
knife4j.eureka.routes[0].serviceName=service-user
knife4j.eureka.routes[0].location=/aub/v2/api-docs?group=default
knife4j.eureka.routes[0].order=1
knife4j.eureka.routes[1].name=自定义服务2
knife4j.eureka.routes[1].serviceName=service-order
knife4j.eureka.routes[1].location=/v2/api-docs?group=default
knife4j.eureka.routes[1].order=2
```

4、调试是否正常(<font style="color:red">正常</font>)

5、修改`eureka.properties`配置文件,增加Basic权限控制，访问该文档需要输入用户名及密码，是否正常显示(<font style="color:red">正常</font>)

```properties
knife4j.eureka.serviceUrl=http://localhost:10000/eureka/
knife4j.eureka.routes[0].name=自定义服务1
knife4j.eureka.routes[0].serviceName=service-user
knife4j.eureka.routes[0].location=/aub/v2/api-docs?group=default
knife4j.eureka.routes[0].order=1
knife4j.eureka.routes[1].name=自定义服务2
knife4j.eureka.routes[1].serviceName=service-order
knife4j.eureka.routes[1].location=/v2/api-docs?group=default
knife4j.eureka.routes[1].order=2
knife4j.basicAuth.enable=true
knife4j.basicAuth.username=abc
knife4j.basicAuth.password=123456
```

6、修改`eureka.properties`配置文件，去除basic权限控制，访问文档是否正常(<font style="color:red">正常</font>)

```properties
knife4j.eureka.serviceUrl=http://localhost:10000/eureka/
knife4j.eureka.routes[0].name=自定义服务1
knife4j.eureka.routes[0].serviceName=service-user
knife4j.eureka.routes[0].location=/aub/v2/api-docs?group=default
knife4j.eureka.routes[0].order=1
knife4j.eureka.routes[1].name=自定义服务2
knife4j.eureka.routes[1].serviceName=service-order
knife4j.eureka.routes[1].location=/v2/api-docs?group=default
knife4j.eureka.routes[1].order=2
# 可以改为false，或者把下面的配置全部注释，两种方式都行
knife4j.basicAuth.enable=false
knife4j.basicAuth.username=abc
knife4j.basicAuth.password=123456
```

7、删除`eureka.properties`配置文件，文档是否还能访问(<font style="color:red">正常</font>)

## 2.3.4 Nacos模式

> [视频介绍-Knife4jAggregationDesktop使用-Nacos模式](https://www.bilibili.com/video/BV1zh411f7pz/)

1、在`data`目录下新增文件夹并且修改符合Desktop要求的名称格式，新建`nacos.properties`配置文件及配置，是否生效(<font style="color:red">正常</font>)

```properties
knife4j.nacos.serviceUrl=http://192.168.0.223:8848/nacos
knife4j.nacos.routes[0].name=自定义服务1
knife4j.nacos.routes[0].serviceName=service-user
knife4j.nacos.routes[0].location=/v2/api-docs?group=default
knife4j.nacos.routes[1].name=自定义服务2
knife4j.nacos.routes[1].serviceName=service-order
knife4j.nacos.routes[1].location=/v2/api-docs?group=default
```

2、修改`nacos.properties`配置文件，增加order属性修改分组显示顺序，是否正常(<font style="color:red">正常</font>)

```properties
knife4j.nacos.serviceUrl=http://192.168.0.223:8848/nacos
knife4j.nacos.routes[0].name=自定义服务1
knife4j.nacos.routes[0].serviceName=service-user
knife4j.nacos.routes[0].location=/v2/api-docs?group=default
knife4j.nacos.routes[0].order=3
knife4j.nacos.routes[1].name=自定义服务2
knife4j.nacos.routes[1].serviceName=service-order
knife4j.nacos.routes[1].location=/v2/api-docs?group=default
knife4j.nacos.routes[1].order=2
```

3、修改`nacos.properties`配置文件，修改服务的显示名称`name`,文档界面是否正常显示(<font style="color:red">正常</font>)

```properties
knife4j.nacos.serviceUrl=http://192.168.0.223:8848/nacos
knife4j.nacos.routes[0].name=自定义X服务1
knife4j.nacos.routes[0].serviceName=service-user
knife4j.nacos.routes[0].location=/v2/api-docs?group=default
knife4j.nacos.routes[0].order=3
knife4j.nacos.routes[1].name=自定义X服务2
knife4j.nacos.routes[1].serviceName=service-order
knife4j.nacos.routes[1].location=/v2/api-docs?group=default
knife4j.nacos.routes[1].order=2
```

4、调试是否正常(<font style="color:red">正常</font>)

5、修改`nacos.properties`配置文件,增加Basic权限控制，访问该文档需要输入用户名及密码，是否正常显示(<font style="color:red">正常</font>)

```properties
knife4j.nacos.serviceUrl=http://192.168.0.223:8848/nacos
knife4j.nacos.routes[0].name=自定义X服务1
knife4j.nacos.routes[0].serviceName=service-user
knife4j.nacos.routes[0].location=/v2/api-docs?group=default
knife4j.nacos.routes[0].order=3
knife4j.nacos.routes[1].name=自定义X服务2
knife4j.nacos.routes[1].serviceName=service-order
knife4j.nacos.routes[1].location=/v2/api-docs?group=default
knife4j.nacos.routes[1].order=2
knife4j.basicAuth.enable=true
knife4j.basicAuth.username=nacos
knife4j.basicAuth.password=1234
```

6、修改`nacos.properties`配置文件，去除basic权限控制，访问文档是否正常(<font style="color:red">正常</font>)

```properties
knife4j.nacos.serviceUrl=http://192.168.0.223:8848/nacos
knife4j.nacos.routes[0].name=自定义X服务1
knife4j.nacos.routes[0].serviceName=service-user
knife4j.nacos.routes[0].location=/v2/api-docs?group=default
knife4j.nacos.routes[0].order=3
knife4j.nacos.routes[1].name=自定义X服务2
knife4j.nacos.routes[1].serviceName=service-order
knife4j.nacos.routes[1].location=/v2/api-docs?group=default
knife4j.nacos.routes[1].order=2
# 可以改为false，或者把下面的配置全部注释，两种方式都行
knife4j.basicAuth.enable=false
knife4j.basicAuth.username=nacos
knife4j.basicAuth.password=1234
```

7、Nacos注册中心开启了权限验证,在`nacos.properties`配置文件中进行配置，测试文档聚合是否正常(<font style="color:red">正常</font>)

```properties
knife4j.nacos.serviceUrl=http://192.168.0.223:8848/nacos
# Nacos OpenAPI权限
knife4j.nacos.serviceAuth.enable=true
knife4j.nacos.serviceAuth.username=nacos
knife4j.nacos.serviceAuth.password=nacos

knife4j.nacos.routes[0].name=自定义X服务1
knife4j.nacos.routes[0].serviceName=service-user
knife4j.nacos.routes[0].location=/v2/api-docs?group=default
knife4j.nacos.routes[0].order=3
knife4j.nacos.routes[1].name=自定义X服务2
knife4j.nacos.routes[1].serviceName=service-order
knife4j.nacos.routes[1].location=/v2/api-docs?group=default
knife4j.nacos.routes[1].order=2
# 可以改为false，或者把下面的配置全部注释，两种方式都行
knife4j.basicAuth.enable=false
knife4j.basicAuth.username=nacos
knife4j.basicAuth.password=1234
```

8、删除`nacos.properties`配置文件，文档是否还能访问(<font style="color:red">正常</font>)