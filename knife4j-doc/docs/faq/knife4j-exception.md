# Knife4j文档请求异常

很多朋友在集成Knife4j文档的时候会弹出该异常,效果图如下:
![](/knife4j/images/faq/knife4j-error.png)

首先,先申明一点,如果你在你的项目中已经成功集成了`springfox-swagger2`组件,并且可以访问`swagger-ui.html`页面,那么可以100%确定不会出现以上问题


第二种情况就是,开发者第一次集成`Knife4j`的情况,这种情况请看以下几种情况：


首先,可以使用Chrome浏览器的F12功能,查看该接口文档的network选项,选择XHR选项栏进行访问接口的查看,如下图：
![](/knife4j/images/faq/network.png)

首先确保文档访问的接口能够正确响应200成功状态码


其次,查看Swagger的分组接口`/swagger-resources`接口的响应格式必须正确,正确格式参考如下：
```json
[
    {
        "name": "2.X版本",
        "url": "/v2/api-docs?group=2.X版本",
        "swaggerVersion": "2.0",
        "location": "/v2/api-docs?group=2.X版本"
    },
    {
        "name": "分组接口",
        "url": "/v2/api-docs?group=分组接口",
        "swaggerVersion": "2.0",
        "location": "/v2/api-docs?group=分组接口"
    },
    {
        "name": "默认接口",
        "url": "/v2/api-docs?group=默认接口",
        "swaggerVersion": "2.0",
        "location": "/v2/api-docs?group=默认接口"
    }
]
```

是一个没有经过包装处理的纯数组对象,很多时候开发者会对响应的接口做统一的封装,这也会导致对swagger的接口进行封装,如果你有这种行为,请在自己的封装代码中去除对swagger分组接口的封装


第三步是查看swagger的响应实例接口`/v2/api-docs`响应的结构是否正确，如下图：
![](/knife4j/images/faq/v2api.png)

和分组接口一样,不能有封装信息,必须保证和以上的格式一致.

最后如果你都保证了以上的情况都没有发生,那么有可能还有一种情况,就是响应的swagger的接口JSON是一个非法JSON,不是正常的JSON


一般出现此情况时,是因为后端在给List集合的属性赋予了`exmpale`属性,例如:

```java
@ApiModel(description = "客户字段分组模型",value = "CrmFieldGroupResponse")
public class CrmFieldGroupResponse {

    @ApiModelProperty(value = "客户字段分组ID")
    private int id;

    @ApiModelProperty(value = "客户字段分组名称")
    private String name;

    @ApiModelProperty(value = "客户字段数据",example = "{'id':'xxx'}")
    private List<CrmFieldResponse> fields;
    
}
```

该情况会导致生成出来的JSON并非是一个标准的JSON，而`Knife4j`组件在前端是通过`JSON.parse()`方法对后端返回回来的数据进行JSON转换,这会导致转换失败

解决方法是把集合属性中的example属性去掉，交给`springfox-swagger2`框架来自动解析

正确方式：

```java
@ApiModel(description = "客户字段分组模型",value = "CrmFieldGroupResponse")
public class CrmFieldGroupResponse {

    @ApiModelProperty(value = "客户字段分组ID")
    private int id;

    @ApiModelProperty(value = "客户字段分组名称")
    private String name;

    @ApiModelProperty(value = "客户字段数据")
    private List<CrmFieldResponse> fields;
    
}
```







 
 
 