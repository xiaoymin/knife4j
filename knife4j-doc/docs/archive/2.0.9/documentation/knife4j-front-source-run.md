# 5.2 源码运行方式介绍
::: warning
如果你是第一次接触Vue.js框架,或者是Node等前端相关的技术,对他并不了解,建议先花2个小时的时间了解一下他们.

作为后端开发工程师来说,并不需要掌握太多,只需要有一个大概即可.

如果你问我和这些前端相关的技术问题,我是不会告诉你的,因为我也是在学习中的状态
:::


本篇博客主要是讲解`knife4j-vue`模块的调试开发过程,目前工程结构如下图：

![](/knife4j/images/knife4j/construct.png)

如果你想基于该源码方式进行调试,在下载下来该文件夹后,首先需要更改`vue.config.js`文件中的的接口代理地址,因为源码中使用的是localhost,为了方便,你可以更改为目前`knife4j`线上部署的接口进行调试，如下图：

![](/knife4j/images/knife4j/proxy-url.png)


将以上`target`地址更改为`http://knife4j.xiaominfo.com/`

接下来,就可以本地运行了,运行步骤：

一、下载依赖文件(该步骤和Java开发者配置好Maven配置后下载jar包的操作类似)
> 运行该步骤的前提是你本机需要安装Node环境,并且配置好Node相关的环境变量

在该目录下使用命令进行下载安装：

```shell
yarn install
```

二、开发模式运行

```shell
yarn serve
```

三、打包

完成对Vue源码的改动后,需要进行编译打包,命令如下：

```shell
yarn build
```

 
 
 