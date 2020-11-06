# [v1.9.6-2019/08/28 解决长整型精度丢失的问题]

swagger-bootstrap-ui 1.9.6 发布了。swagger-bootstrap-ui是 Swagger 的增强UI 实现，使文档更友好一点儿

**文档**：http://doc.xiaominfo.com

**效果**：http://swagger-bootstrap-ui.xiaominfo.com/doc.html

**Gitee:**<https://gitee.com/xiaoym/swagger-bootstrap-ui>

**GitHub:**<https://github.com/xiaoymin/swagger-bootstrap-ui>

**示例:**https://gitee.com/xiaoym/swagger-bootstrap-ui-demo

## 重要说明

**这是swagger-bootstrap-ui的最后一个版本**

**这是swagger-bootstrap-ui的最后一个版本**

**这是swagger-bootstrap-ui的最后一个版本**

重要的事情说三遍!!!

一开始项目初衷是为了写一个增强版本的Swagger 前端UI,但是随着项目的发展,面对越来越多的个性化需求,不得不编写后端Java代码以满足新的需求,在swagger-bootstrap-ui的1.8.5~1.9.6版本之间,采用的是后端Java代码和Ui都混合在一个Jar包里面的方式提供给开发者使用.这种方式虽说对于集成swagger来说很方便,只需要引入jar包即可,但是在微服务架构下显得有些臃肿。

因此,项目正式更名为**knife4j**,取名knife4j是希望她能像一把匕首一样小巧,轻量,并且功能强悍,更名也是希望把她做成一个为Swagger接口文档服务的通用性解决方案,不仅仅只是专注于前端Ui前端.

swagger-bootstrap-ui的所有特性都会集中在`knife4j-spring-ui`包中,并且后续也会满足开发者更多的个性化需求.

主要的变化是,项目的相关类包路径更换为`com.github.xiaoymin.knife4j`前缀,开发者使用增强注解时需要替换包路径

后端Java代码和ui包分离为多个模块的jar包,以面对在目前微服务架构下,更加方便的使用增强文档注解(使用SpringCloud微服务项目,只需要在网关层集成UI的jar包即可,因此分离前后端)

**knife4j**沿用swagger-bootstrap-ui的版本号,第1个版本从1.9.6开始,关于使用方法,请参考文档

由于更名给大家带来的不便深表歉意~！

## 特性&优化

1、解决Spring路由PathVariable不显示的情况，并优化交互体验

2、解决响应体中的长整型显示错误,精度丢失的问题[#135 @GitHub](https://github.com/xiaoymin/swagger-bootstrap-ui/issues/135)

3、优化请求头Header是中文的情况,如果包含中文则进行encodeURI函数处理,否则不做任何处理[#140 @GitHub](https://github.com/xiaoymin/swagger-bootstrap-ui/issues/140)

4、升级jQuery 1.X系列版本到最新版本`1.12.4`

5、初始化页面请求Swagger接口资源方式改为异步,在jQuery的ajax方法参数项`async:false`时,浏览器会抛出警告的问题(同步ajax请求会造成主线程阻塞,对用户体验不是很好,已被置为过时).

6、支持supportedSubmitMethods,后端配置`UiConfiguration`的Bean[#IVCQ0 @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IVCQ0)

7、优化下载中文乱码问题,后端需要指定filename值,并且对名称进行URLEncoder.encode处理,UI前端会进行decode成中文,保证下载正常

8、修复curl状态栏复制时内容被转义的bug[#136 @GitHub](https://github.com/xiaoymin/swagger-bootstrap-ui/issues/136)

## UI效果展示

![header-json.png](/images/blog/swagger-bootstrap-ui-1.9.5-issue/1.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.5-issue/2.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.5-issue/3.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.5-issue/4.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.5-issue/5.png)

## 项目地址

**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.9.6</version>
</dependency>
```

## Star & Issue

感谢各位朋友的支持,前往<https://gitee.com/xiaoym/swagger-bootstrap-ui>点个Star吧~~ ：）



## 关注

关注我的微信公众号,实时了解`swagger-bootstrap-ui`的最新资讯~~~~

![](/knife4j/images/blog/swagger-bootstrap-ui-1.9.4-issue/us.png)

 
 
 