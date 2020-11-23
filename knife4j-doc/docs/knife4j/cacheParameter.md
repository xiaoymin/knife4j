# 请求参数缓存

在默认情况下,在接口调试的情况下,`Knifetj`对于接口的请求参数都会缓存起来，该配置可以在前端界面中的个性化设置中看到,如下图：

> 如果你不喜欢在调试的时候对参数进行缓存,可以在个性化设置中将该选项取消

![](/knife4j/images/knife4j/plus/cacheparameter.png)


缓存的情况只会在后端没有给属性`example`的情况下产生,如果后端在写Swagger的注解的时候,给每个字段赋予了example的值,那么,`Knife4j`不会使用调试时缓存的值,而是会一直使用后端的example值

例如后端Java实体类如下情况：

```java
public class SwaggerRequestBody{
    
    @ApiModelProperty(value="姓名",example="张飞")
    private String name;
    
    //more...
}
```


对于上面的代码示例,`Knife4j`在每一次打开该接口的请求参数值,其默认值都是`张飞`



以下情况会在第二次请求的情况下启用上一次调试时填的缓存值

```java
public class SwaggerRequestBody{
    
    @ApiModelProperty(value="姓名")
    private String name;
    
    //more...
}
```

这种情况在ui界面不会出现默认值,所以当开发者在调试的时候,填了name属性的值后,`Knife4j`就会将该值缓存起来,方便下次调试调用.
 
 
 