# 内测预发布版本流程

在相当长一段时间,不管是`swagger-bootstrap-ui`还是`Knife4j`的新版本发布,都是作者自己根据开发者提供的issues,解决的问题进行测试，测试无误后直接发布推送到Maven中央仓库

对于新版本并没有测试完全,这也导致了新版本发布后可能存在不稳定性,在以前关注的人数不多的情况下,影响不是很大,但是随着使用`Knife4j`的关注人数越来越多后,这样的发布机制,全靠作者一人之力是很难保证新版本的稳定性的

在QQ交流群沟通时,有开发者提议每次新版本发布时,可以先提前沟通,要发布那些内容,同时有空的可以帮忙内测,这也帮助提升`Knife4j`的新版本的稳定性

因此,针对上面的一个需求,开发者如何帮忙内测新版本写下了这篇文章,希望开发者能够和我一起共建`Knife4`的发展,每个新版本都能有保证其稳定性(至少不会出现影响使用的重大Bug)

在新版本发布时,我会在QQ群里邀请大家帮忙内测,确认无误后,新版本会推送到Maven中央仓库

**在此也要特别感谢愿意帮忙内测预发布版本的开发者,非常感谢**

## Spring Boot & Spring MVC 单体架构


如果你是一个简单的Spring Boot或者是Spring MVC的单体架构,这种结构内测非常简单

1、首先,在Gitee的仓库[https://gitee.com/xiaoym/knife4j](https://gitee.com/xiaoym/knife4j)下载,主要是`knife4j-vue`这个文件夹,如下图：

![](/knife4j/img/knife4j/test-pre1.png)


`knife4j`在2.x系列的版本后,使用Vue技术栈真正的前后端分离开发,`knife4j-vue`是前端的源码


2、将你们的项目运行起来,假如运行后的端口号是:`8999`


3、修改`knife4j-vue`的接口代理配置,在`knife4j-vue`文件夹下的`vue.config.js`文件中,修改`devServer`节点的配置:部分配置如下：
```json
devServer: {
    proxy: {
      "/": {
        target: 'http://localhost:8999/',
        ws: true,
        changeOrigin: true
      }
    }
}
```

4、如果你的项目端口号是和我一样,同是8999,那么这里都不用修改,否则,修改成你自己项目的端口号

> 如果的电脑中没有安装Node环境,那么需要首先安装Node环境
> 
> 其次,在执行运行命令yarn serve之前,你需要先执行命令yarn install进行依赖的下载

5、运行`knife4j-vue`模块,使用命令`yarn serve` ,接下来就可以进行内测调试了


以上步骤knife4j-vue文件推荐使用Visual Studio Code软件进行打开执行,和后端IDEA彻底分开,效果会不错.



## 微服务架构


微服务架构比较麻烦,目前比较好的方式只能是将仓库源码下载下来后,项目代码下图中：

![](/knife4j/img/knife4j/test-pre2.png)

然后进入该目录,在本地执行`mvn install`命令将knife4j的包在本地安装一份副本

然后在项目`pom.xml`中修改新版本的版本号进行引入测试。

这是对整个微服务的接口进行测试的情况,如果只是对单个微服务进行测试,可以参考上面Spring Boot的测试流程.
 
 
 