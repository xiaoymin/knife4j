# [v1.8.8-2018/12/17 多个响应code支持]

Swagger-Bootstrap-Ui 1.8.8 发布了。Swagger-Bootstrap-Ui是 Swagger 的增强UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿

Swagger-Bootstrap-Ui 1.8.8 主要更新如下：

## 特性&优化

1、**顶部标题可自定义**,去除原默认显示swagger-bootstrap-ui的固定标题,title规则为获取分组对象apiInfo中的第一个title属性

2、**个性化配置中新增是否开启请求参数缓存策略**,默认为true，当设置为false时,请求的参数不会再本地产生缓存,下次打开接口调试时需要自己重新输入相关接口参数

3、分组加载由同步改为异步加载

4、**新增接口高亮显示**,当后端新增接口后,UI会自动标识该接口为新接口,直到该接口被点击为止.

5、**当服务器正在重启或者宕机时,接口发生异常,给出友好提示**,告知接口对接人员.

6、**请求参数必填排序,require=true排最前**

7、后端接口方法上**针对`@Deprecated`标注的接口,UI以中横线标注区分**

8、针对不同状态响应码,返回内容均有Schema的情况下，UI以tab方式将所有状态码的schema内容呈现

9、优化接口数量过多的情况下,离线文档会导致文档页假死

## Bug修复

1、修复针对Delete请求,使用@RequestBody注解出现400错误 [issue IPLJT @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IPLJT)

2、修复响应状态码HTML标签非转义输出 [issue #47 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/47)

3、不能正确解析response内非$ref的schema内容 [issue #43 @Github](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/43)

## UI效果展示

![header-json.png](/images/blog/swagger-bootstrap-ui-1.8.8-issue/1.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.8-issue/2.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.8-issue/3.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.8-issue/4.png)
![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.8-issue/5.png)

## 项目地址

**Maven坐标**

```
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.8.8</version>
</dependency>
```

**码云**：<https://gitee.com/xiaoym/swagger-bootstrap-ui>

**GitHub**:<https://github.com/xiaoymin/Swagger-Bootstrap-UI>

在线体验：<http://swagger-bootstrap-ui.xiaominfo.com/doc.html>

## Star & Issue

前往<https://gitee.com/xiaoym/swagger-bootstrap-ui>点个Star吧~~ ：）




**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 
 