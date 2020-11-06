# 导出离线文档

::: warning
`knife4j` 版本>2.0.1 使用此规则,离线文档导出功能目前暂时未提供导出Word和PDF,推荐大家使用一款MD的渲染软件([Typora](https://typora.io/))进行导出
:::

在以前的开发人员使用Swagger的时候,希望Swagger能够导出一份精细的文档,不管是集成何种方式,使用方法都是比较繁琐的,而且导出的文档效果也不尽如人意

为了解决这个问题,`Knife4j`在前端界面直接集成了两种导出Swagger文档的方式,主要是导出Markdown和离线Html

对于使用者来说,能把过程简单化是最好的,而`Knfie4j`提供的导出功能就是如此简单

在前端Ui界面中的`文档管理 => 离线文档` 菜单栏,我们点击后可以看到如下界面：

![](/knife4j/images/knife4j/plus/exportDocument.png)


直接点击下载Markdown,`Knife4j`就会为我们生成一份精细的MD文档,效果图如下：

![](/knife4j/images/knife4j/3.png)


导出的Html效果,[点击我预览在线效果](https://doc.xiaominfo.com/Knife4j-Offline-Html.html)，如下图：

![](/knife4j/images/knife4j/5.png)
 
 
 