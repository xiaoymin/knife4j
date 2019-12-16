不管是在SwaggerBootstrapUi以前的版本中,还是在SwaggerBootstrapUi的1.8.9版本发布新功能字段注释逐行显示时,很多朋友都会问为啥自己的UI文档上不显示注释.

1.8.9的功能展示如下图：

![](images/nf2.png)

正常情况下,不管是调试还是文档说明都会显示以上字段说明(除非你没写注解说明)

这里很多朋友碰见的最多的问题主要有2个：

- 返回Map|Object为何不显示
- 使用泛型T还是不显示

不显示效果可能如下图：

![](images/nf1.jpg)

返回Object不显示字段属性

![](images/nf3.png)



**返回Map为何不显示**

为何返回Map不显示,大家都知道Map是Java里面的集合接口,不管是Map本身还是诸如HashMap等子实现,这类数据对于Swagger来说都是未定义结构的数据

Swagger只认识定义好的类-属性，所以接口返回Map,对于Swagger来说是没有字段展示的,这种情况同样适用与返回Object这个顶级父类.这也是为何要适用泛型T的原因

**适用泛型T还是不显示**

很多朋友会说我已经使用泛型T了,可是文档上还是不显示,这里主要的原因有以下几点

属性定义必须是泛型T，如下：

```java
private T data;//返回属性T
```

返回T类型的get方法必须是返回T，有时候自动生成get、setter方法插件等会将我们的代码生成返回Object，例如：

```java
public Object getData(){
    return data;
}
```

以上是错误的形式,尽管属性中已经定义为T了，正确的方式：

```java
public T getData(){
    return data;
}
```

最重要的一步,以上步骤完全正确,代码也没有问题,可是ui还是不显示属性，必须在接口层强指定泛型类型(可能是Swagger要求我们写代码要规范吧~~~),如下：

![](images/nf4.png)

如果以上情况都ok，还是不显示说明,恭喜你发现了SwaggerBootstrapUi的一个bug，欢迎提[issue](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues)反馈给我,我会搞定它的~~！

**另外**

一般在完成以上情况后,字段说明都会显示,这里再提醒一下大家,如果已经在泛型中强制约束了返回类型后,就无需在注解`@ApiOperation`中设置`response`属性值，比如如下代码

```java
@ApiOperation(value = "查询所有",response=AlarmReponse.class)
@GetMapping("/queryAll")
public Rest<List<AlarmResponse>> queryAll(){
    //more..
}
```

以上代码返回了泛型`Rest`类型的List-AlarmResponse集合,但是却`ApiOperation`注解中加了response属性为`AlarmResponse.class`，这种情况会造成Ui只显示`AlarmReponse`类的属性说明，这显然是不对的，因为它把`Rest`的属性给忽略了,所以:

**一般情况下,是不写注解`@ApiOperation`中的`response`属性值,能少写就少写,将剩下的交给springfox-swagger这个框架,由它自动解析生成接口返回类型**

最后贴一个简单的返回封装类供大家参考([Rest.java](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/blob/master/swagger-bootstrap-ui-demo/src/main/java/com/swagger/bootstrap/ui/demo/common/Rest.java?tdsourcetag=s_pctim_aiomsg))

```java
public class Rest<T> {

    @ApiModelProperty(value = "是否成功")
    private boolean success=true;
    @ApiModelProperty(value = "返回对象")
    private T data;
    @ApiModelProperty(value = "错误编号")
    private Integer errCode;
    @ApiModelProperty(value = "错误信息")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
```

