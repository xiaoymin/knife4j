# [v1.8.1-2018/08/14 修复basePath导致404]

swagger-bootstrap-ui 1.8.1 发布了。swagger-bootstrap-ui 是 Swagger 的前端 UI 实现，目的是替换 Swagger 默认的 UI 实现 Swagger-UI，使文档更友好一点儿 

swagger-bootstrap-ui 1.8.1 主要更新如下：

1、fixed 针对basePath属性,调试接口重复添加basePath路径,接口报404错误(重大bug,建议升级)

2、fixed 针对@ApiModelProperty注解,针对example属性值,array类型值带单引号,文档无法显示bug

3、fixed 针对404 异常,header-curl tab选项卡切换bug

4、fixed curl -X 参数bug,显示缺少"/"根路径

5、fixed 左侧接口列表滚条无法完全滚动到底部

6、fixed 窗口大小改变后，界面混乱

7、优化菜单做成接口方法类型和接口类型左对齐

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.1-issue/left.png)

8、fixed 左侧接口列表滚条无法完全滚动到底部 

9、优化 针对枚举类型,参数说明显示可用值列表

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.1-issue/enum.png)

10、表单类型显示header、可提交header信息

11、fixed 基础类型响应数据为空的情况

![](/knife4j/images/blog/swagger-bootstrap-ui-1.8.1-issue/basic.png)



**Maven坐标**

```xml
<dependency>
   <groupId>com.github.xiaoymin</groupId>
   <artifactId>swagger-bootstrap-ui</artifactId>
   <version>1.8.1</version>
</dependency>
```
**码云**：https://gitee.com/xiaoym/swagger-bootstrap-ui

**GITHUB**:https://github.com/xiaoymin/Swagger-Bootstrap-UI

在线体验：<http://swagger-bootstrap-ui.xiaominfo.com/doc.html> 

欢迎提BUG、Pull Request给我，共同来完善这个小工具~~~~

**相关链接**

- swagger-bootstrap-ui 的详细介绍：[点击查看](https://www.oschina.net/p/swagger-bootstrap-ui)
- swagger-bootstrap-ui 的下载地址：[点击下载](https://git.oschina.net/xiaoym/swagger-bootstrap-ui/releases)
 
 
 