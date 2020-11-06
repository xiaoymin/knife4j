# [v1.8.6-2018/10/31 Spring MVC接口增强异常]

swagger-bootstrap-ui 1.8.6 发布了。swagger-bootstrap-ui 是 Swagger 的增强UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿

swagger-bootstrap-ui 1.8.6 主要更新如下：

## 特性增加

1、请求参数类型(header|body|query)等以不同颜色着色区分

2、调试栏针对必须项(require=true)时,文本框着红色以区分

3、调试页输入框可通过tab键自动切换上下级输入框.

## Bug修复

1、修复Spring使用cglib生成的代理类,导致class无法获取Spring的相关注解,导致接口增强排序失败

2、针对basePath属性不是根路径“/”，导致接口排序比对失败，无法排序的问题

3、修复针对SpringCloud通过zuul路由组件加载swagger接口存在basePath属性,增强接口缺失basePath属性的bug,导致增强接口请求失败的问题

4、修复Spring的请求地址仅支持value属性，不支持path属性的bug

5、针对请求头Content-Type中多余空格问题,部分接口调用失败的问题

6、修复针对参数、参数说明太长,导致table换行，样式失效问题.

7、修复针对header、path等参数外，传参只包含body类型无请求json示例的问题.

8、修复针对请求参数存在多个数组,增加按钮无效的BUG.

9、优化离线文档相关的显示格式问题.包括JSON显示格式错乱、添加请求JSON示例、文档开始说明等信息

## UI效果展示

![header-json.png](/images/blog/swagger-bootstrap-ui-1.8.6-issue/header-json.png)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.6-issue/debug-require.png)

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.6-issue/more-params.png)

## Maven坐标

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.8.6</version>
</dependency>
```

**码云**：<https://gitee.com/xiaoym/swagger-bootstrap-ui>

**GITHUB**:<https://github.com/xiaoymin/Swagger-Bootstrap-UI>

在线体验：<http://swagger-bootstrap-ui.xiaominfo.com/doc.html>

欢迎提BUG、Pull Request给我，共同来完善这个小工具~~~~

还未给swagger-bootstrap-ui点过赞的朋友，前往<https://gitee.com/xiaoym/swagger-bootstrap-ui>给个Star吧~~ ：）



**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 
 