# 5.5 基于IIS使用knife4j

在[上一篇](knife4j-front-source-modified.md)中,我们通过改造`knife4j`的源码,构造了自己的front版本,接下来

本篇主要讲解如何使用IIS服务器运行`knife4j`

::: warning
关于Windows环境的IIS环境安装不在本篇文章的范围内,不知道IIS如何安装的请自行搜索解决
:::

我们在上一篇中在分组的url中,我们构建了一个虚拟的地址`/static/services.json`

接下来,我们在同目录中创建该文件,如下图：

![](/knife4j/images/knife4j/knife4j-front-group.png)

`services.json`文件的内容参考java技术体系接口返回的结构即可,如下：
```json
[
    {
        "name": "knife4j-front服务模块",
        "url": "/static/server1.json",
        "swaggerVersion": "2.0",
        "location": "/static/server1.json"
    },
    {
        "name": "第二个模块",
        "url": "/static/server2.json",
        "swaggerVersion": "2.0",
        "location": "/static/server2.json"
    }
]
```

在分组的JSON结构中,我们同样返回一个Swagger 实例的静态JSON结构地址`/static/server1.json`

该JSON结构返回的是真正的Swagger结构,结构如下图：

![](/knife4j/images/knife4j/knife4j-front-instance.png)

构造虚拟的JSON结构完成后,接下来启动IIS服务,在浏览器访问地址(`http://localhost:port/doc.html`),效果如下：

![](/knife4j/images/knife4j/knife4j-front-efffect.png)





 
 
 