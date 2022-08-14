# [v1.7.7-2018/07/25 修复JS内存溢出]

swagger-bootstrap-ui 1.7.7 发布了。swagger-bootstrap-ui 是 Swagger 的前端 UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿 

该版本基本是fixed版本，修复了很多bug，针对全局参数这种新特性，影响比较大,建议升级

swagger-bootstrap-ui 1.7.7 主要更新如下： 

1、fixed 对象ref应用本身，JS 出现死循环了么，栈内存溢出BUG

2、优化递归查找ref方法,fixed ref自身引用,相互引用的情况下,文档出不来bug

3、响应json属性太多，文档太长,不利于查看,使用jsonview插件格式化,可收缩,便于查看

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.7-issue/json.png)

4、fixed 对象属性值存在required属性时,值显示不对bug

5、兼容firefox,文档菜单换行显示异常问题

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.7-issue/firefox.png)

6、新增枚举请求参数类型支持,调试页面枚举类型为下拉框

![](/knife4j/images/blog/swagger-bootstrap-ui-1.7.7-issue/enum.png)

7、fixed 请求swagger-resources接口响应为string类型，文档无法展示,格式化json展示文档

8、fixed 全局参数重新赋值无效

9、fixed 针对@ApiOperation注解自定义tags接口无法显示bug

**Maven坐标**

```xml
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.7.7</version>
</dependency>
```
**码云**：https://gitee.com/xiaoym/swagger-bootstrap-ui

**GITHUB**:https://github.com/xiaoymin/Swagger-Bootstrap-UI

欢迎提BUG给我~~~~


**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 
 