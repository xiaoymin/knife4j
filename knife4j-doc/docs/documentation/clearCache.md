# 3.14 清除缓存


::: warning
`knife4j` 版本>2.0.1 使用此规则
:::


在文档升级、或者产生一些莫名其妙的问题时,大家可以点击文档右上角的清除缓存操作,如下图

![](/knife4j/images/knife4j/plus/clearCache.png)

`Knife4j`的缓存全部存储在浏览器中的IndexedDB中,所以,通过浏览器的强制刷新等操作是无法起到清理缓存的作用的

缓存位置如下图：

![](/knife4j/images/knife4j/plus/cacheLocation.png)

你也可以手工删除该缓存值,然后再刷新`Knife4j`的文档查看文档效果
 
 

