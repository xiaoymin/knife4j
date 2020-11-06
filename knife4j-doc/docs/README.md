---
home: true
heroText: knife4j
tagline: knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案
actionText: 快速开始 →
actionLink: /documentation/
features:
- title: 简洁
  details: 基于左右菜单式的布局方式,是更符合国人的操作习惯吧.文档更清晰...
- title: 个性化配置
  details: 个性化配置项,支持接口地址、接口description属性、UI增强等个性化配置功能...
- title: 增强
  details: 接口排序、Swagger资源保护、导出Markdown、参数缓存众多强大功能...
footer: Apache License 2.0 | Copyright © 2019-八一菜刀 
---

>大家好,我在GitChat中开了一篇关于[《Knife4j 及 Swagger 在企业开发中的实践》](https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03)
>地址：[https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03](https://gitbook.cn/gitchat/activity/5f86b4cb1772090f20e13b03)
>
>欢迎对 SpringFox、Swagger、Knife4j 感兴趣以及想了解的人员一起来chat 
>

## 社群交流
- [![](https://img.shields.io/badge/加入QQ1群-453925079(满)-red.svg)](//shang.qq.com/wpa/qunwpa?idkey=9a160903786b88bf3ca112842e501d3623510db4f1c307eec81849165485bf5f)
- [![](https://img.shields.io/badge/加入QQ2群-621154782(满)-red.svg)](//shang.qq.com/wpa/qunwpa?idkey=11e0a1453a6a3695bd8ed709fbc8359c9c48dd8538aaafbece7b84ecd325b91c)
- [![](https://img.shields.io/badge/加入QQ3群-608374991(满)-red.svg)](//shang.qq.com/wpa/qunwpa?idkey=16b81902c23fbca82780fa107da1b6612e2ee44a05c4103c9176ad9d61c2f6bf)  
- [![](https://img.shields.io/badge/加入QQ4群-593495275-red.svg)](https://qm.qq.com/cgi-bin/qm/qr?k=8mdCNeiPZsMuUtB8rC8jIopW5vfmYAXQ&jump_from=webapi)

## 源码

[![star](https://gitee.com/xiaoym/knife4j/badge/star.svg?theme=gvp)](https://gitee.com/xiaoym/knife4j/stargazers)
[![fork](https://gitee.com/xiaoym/knife4j/badge/fork.svg?theme=gvp)](https://gitee.com/xiaoym/knife4j/members)


[![萧明/knife4j](https://gitee.com/xiaoym/knife4j/widgets/widget_card.svg?colors=393222,ebdfc1,fffae5,d8ca9f,393222,a28b40)](https://gitee.com/xiaoym/knife4j)

 

### 重大说明

`swagger-bootstrap-ui`的最后一个版本是`1.9.6`,已更名为`knife4j`

knife4j是为Java MVC框架集成Swagger生成Api文档的增强解决方案,前身是`swagger-bootstrap-ui`,取名knife4j是希望她能像一把匕首一样小巧,轻量,并且功能强悍!


后续版本请使用knife4j,关于knife4j的使用方法请[参考文档](/knife4j/)


Java开发使用`Knife4j`目前有一些不同的版本变化，主要如下：

1、如果开发者继续使用OpenAPI2的规范结构，底层框架依赖springfox2.10.5版本，那么可以考虑`Knife4j`的2.x版本

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <!--在引用时请在maven中央仓库搜索2.X最新版本号-->
    <version>2.0.7</version>
</dependency>
```

2、如果开发者使用OpenAPI3的结构，底层框架依赖springfox3.0.0,可以考虑`Knife4j`的3.x版本

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <!--在引用时请在maven中央仓库搜索3.X最新版本号-->
    <version>3.0.1</version>
</dependency>
```

3、如果开发者底层框架使用的是`springdoc-openapi`框架,则需要使用`Knife4j`提供的对应版本,需要注意的是该版本没有`Knife4j`提供的增强功能，是一个纯Ui。

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-springdoc-ui</artifactId>
    <!--在引用时请在maven中央仓库搜索3.X最新版本号-->
    <version>3.0.1</version>
</dependency>
```


###  swagger-bootstrap-ui的最后一个版本

```xml
<dependency>
  <groupId>com.github.xiaoymin</groupId>
  <artifactId>swagger-bootstrap-ui</artifactId>
  <version>1.9.6</version>
</dependency>
```

### 效果预览

![](/knife4j/img/k2/1.png)
![](/knife4j/img/k2/5.png)
![](/knife4j/img/k2/6.png)
![](/knife4j/img/k2/8.png)

### 关注 & 交流

关注我的微信公众号,实时了解`swagger-bootstrap-ui`的最新资讯~~~~

![](/knife4j/img/wechat.png)



 
 
 