# 前言-说明

`Knife4j`从开源至今,都是提供的基于Java+Spring技术栈的jar包方式供Java开发人员方便使用,特别是和`Springfox-Swagger2`组件深度绑定的一个工具

`Springfox-Swagger2`是一个将Spring MVC、Spring Boot等技术框架提供的开发API接口根据Swagger的V2结构定义,最终将我们所写的Rest接口代码解析成Swagger的V2结构json的工具框架

同时他也提供了`springfox-swagger-ui`这个前端Ui包,这就是我们目前大家目前较熟悉的在用的Swagger界面,如下图：

![](/knife4j/images/knife4j/swagger-ui.png)


随着`Knife4j`的发展,越来越多的开发者们也关注到了`Knife4j`,在QQ群交流中,也和同行们进行了深入的交流,对knife4j的结构进行了详细的阐述

但是,一直没有形成统一的文章或博客,因此,将在和同行的沟通情况整理成博客,供有需要的开发者参考,希望能对使用`Knife4j`的开发者们一些启发和帮助


所以,本篇博客主要从目前`Knife4j`在开发的版本中如何改造或者是直接使用非Java技术栈的方式,实现`Knife4j`对Swagger的结构渲染


`Knife4j`自`2.0.0`版本开始,为了适应当前的技术发展(ps:学习新技术中...),同时也是为了在后面的发展中能更好的扩展,基于前端技术栈Vue+Antd Vue版进行了重写

主要是页面的交互方式进行了重写,但是解析Swagger的核心JS并没有做多少改动,源码在[https://gitee.com/xiaoym/knife4j](https://gitee.com/xiaoym/knife4j)的`knife4j-vue`模块中


所以,接下来的篇章将会详解介绍`knife4j`的源码结构、解析过程、如果构造自己的front版本等等


如果你嫌麻烦,在看完了后面的文章介绍后,可以直接使用`knife4j-front`模块,不管是基于IIS或者Nginx,按照文档上的说明即可


 
 
 