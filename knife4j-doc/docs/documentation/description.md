# 1.1 项目介绍

`Knife4j`的前身是`swagger-bootstrap-ui`,前身`swagger-bootstrap-ui`是一个纯`swagger-ui`的`ui`皮肤项目

一开始项目初衷是为了写一个增强版本的swagger的前端ui,但是随着项目的发展,面对越来越多的个性化需求,不得不编写后端Java代码以满足新的需求,在`swagger-bootstrap-ui`的1.8.5~1.9.6版本之间,采用的是后端Java代码和Ui都混合在一个Jar包里面的方式提供给开发者使用.这种方式虽说对于集成swagger来说很方便,只需要引入jar包即可,但是在微服务架构下显得有些臃肿。

因此,项目正式更名为**knife4j**,取名knife4j是希望她能像一把匕首一样小巧,轻量,并且功能强悍,更名也是希望把她做成一个为Swagger接口文档服务的通用性解决方案,不仅仅只是专注于前端Ui前端.

`swagger-bootstrap-ui`的所有特性都会集中在`knife4j-spring-ui`包中,并且后续也会满足开发者更多的个性化需求.

主要的变化是,项目的相关类包路径更换为`com.github.xiaoymin.knife4j`前缀,开发者使用增强注解时需要替换包路径

后端Java代码和ui包分离为多个模块的jar包,以面对在目前微服务架构下,更加方便的使用增强文档注解(使用SpringCloud微服务项目,只需要在网关层集成UI的jar包即可,因此分离前后端)

**knife4j**沿用`swagger-bootstrap-ui`的版本号,第1个版本从1.9.6开始,关于使用方法,请参考文档