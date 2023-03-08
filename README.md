>大家好,我在GitChat中写了一篇关于[《Knife4j 及 Swagger 在企业开发中的实践》](https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03)
>地址：[https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03](https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03)
>
>欢迎对 SpringFox、Swagger、Knife4j 感兴趣以及想了解的人员一起来chat 
>

knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案,前身是swagger-bootstrap-ui,取名knife4j是希望她能像一把匕首一样小巧,轻量,并且功能强悍!

knife4j的前身是`swagger-bootstrap-ui`，为了契合微服务的架构发展,由于原来`swagger-bootstrap-ui`采用的是后端Java代码+前端Ui混合打包的方式,在微服务架构下显的很臃肿,因此项目正式更名为`knife4j`

更名后主要专注的方面

- 前后端Java代码以及前端Ui模块进行分离,在微服务架构下使用更加灵活
- 提供专注于Swagger的增强解决方案,不同于只是改善增强前端Ui部分
- 提供更多灵活的中间件方案\工具

## 项目模块

目前仓库主要的模块说明：

| 模块名称             | 说明                                                         |
| -------------------- | ------------------------------------------------------------ |
| knife4j              | 为Java MVC框架集成Swagger的增强解决方案，Java生态下的中间件封装                      |
| knife4j-desktop        |开箱即用的独立解决方案,提供官方[Docker镜像](https://hub.docker.com/repository/docker/xiaoymin/knife4j/general),基于Spring Boot 3.0编写，查看[使用文档](https://doc.xiaominfo.com/docs/middleware-sources/desktop-introduction)|
| knife4j-doc    | knife4j官方文档，基于Docusaurus编写，参与贡献请[参考文档](https://doc.xiaominfo.com/docs/community/joinus)      |
| knife4j-vue      |当前Knife4j的前端源码，基于Vue2.0编写                         |
| knife4j-vue3      |当前Knife4j的前端源码，基于Vue3.0编写，该代码库来自贡献者，目前尚未投入使用                        |
| knife4j-front        | knife4j的前端架构代码,目前是规划阶段，该模块尚未编码,有想法的可以通过交流群与作者沟通    |




## 获取帮助


**官网文档：** [https://doc.xiaominfo.com/](https://doc.xiaominfo.com/)

**预览地址:** [https://doc.xiaominfo.com/demo/doc.html](https://doc.xiaominfo.com/demo/doc.html)

**Demo示例:** [https://gitee.com/xiaoym/swagger-bootstrap-ui-demo](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo)

**Demo说明：** [https://doc.xiaominfo.com/docs/action/action-simple](https://doc.xiaominfo.com/docs/action/action-simple)

**作者交流：** 关注公众号"Knife4j"，点击菜单“交流群”获取加群二维码


![输入图片说明](https://foruda.gitee.com/images/1661053867569480310/扫码_搜索联合传播样式-标准色版.png "扫码_搜索联合传播样式-标准色版.png")
 

 

## 特别声明

不管是knife4j还是swagger-bootstrap-ui

对外提供的地址依然是doc.html

访问：http://ip:port/doc.html

即可查看文档

**这是永远不会改变的**



## 界面效果

![接口说明](static/1.png)

![接口调试](static/8.png)
 

## 特别鸣谢

- 感谢 [JetBrains](https://jb.gg/OpenSourceSupport) 提供的免费开源 License：

<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png"  width="100" height="100" />
  
