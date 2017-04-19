# Swagger 注解说明


`Swagger`的注解位于`swagger-annotations`基础jar包中,如图:

![](https://xiaoymin.github.io/cloud-sdk-doc/assets/swagger-an1.jpg)



# 常用注解说明

* `@Api`:一般用于Controller中,用于接口分组,使用如下图：

 ![](https://xiaoymin.github.io/cloud-sdk-doc/assets/api.jpg)

* `@ApiOperation`:接口说明,用于api方法上

 ![](https://xiaoymin.github.io/cloud-sdk-doc/assets/apioper.jpg)

* `@ApiImplicitParam`:参数说明,适用于只有一个请求参数,主要参数
    | 模块名称 | 说明 |
| :--- | :--- |
| name | 参数名称  |
| value | 参数中文说明  |
| required | 是否请求必须,boolean值  |
| dataType | 数据类型  |

 ![](https://xiaoymin.github.io/cloud-sdk-doc/assets/apiparam.jpg)

* `@ApiImplicitParams`:多个参数说明,主要参数参考上面`@ApiImplicitParam`
 ![](https://xiaoymin.github.io/cloud-sdk-doc/assets/apiparams.jpg)

* `@ApiModelProperty`:实体参数说明

 实体类：

 ![](https://xiaoymin.github.io/cloud-sdk-doc/assets/apiprop.jpg)

 Controller类:

 ![](https://xiaoymin.github.io/cloud-sdk-doc/assets/apipropmodel.jpg)