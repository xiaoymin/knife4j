# 版本升级

`swagger-bootstrap-ui`经历两年的稳定发展,目前整体架构已经趋于稳定

但是伴随新版本的发布,每个版本都会加入很多好玩的新特性,作者会尽力保证做到向下兼容,但是如果开发者在使用`swagger-bootstrap-ui`的途中,因为升级版本导致的问题

一般都是由于前端的问题导致,作者在开发的时候未考虑周全,请大家见谅.

`swagger-bootstrap-ui`大量使用了浏览器的`localStorage`对象来进行相关缓存的处理

所以如果开发者升级之后,后端没问题的情况下,前端JS报错时,可以考虑清理浏览器的缓存和`localStorage`对象的值

如下图：

![](/knife4j/images/1-9-3/cache.png)
 
 
 