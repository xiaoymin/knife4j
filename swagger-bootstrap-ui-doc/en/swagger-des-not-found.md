Whether in the previous version of SwaggerBootstrapUi, or in the 1.8.9 version of SwaggerBootstrapUi, the new feature field comment is displayed line by line, many friends will ask if the comment is not displayed on the UI document.

The function of 1.8.9 is shown below.：

![](images/nf2.png)

Under normal circumstances, the above field description will be displayed whether it is debugging or documentation (unless you have not written an explanation)

There are two main problems that many of my friends have encountered here:

- Return to Map|Object why not display
- Use generic T or not to display

Do not display the effect may be as shown below：

![](images/nf1.jpg)

Return Object does not display field properties

![](images/nf3.png)



**Why does the return map not display**

Why does the return map not display, everyone knows that Map is the collection interface in Java, whether it is Map itself or sub-implementation such as HashMap, this kind of data is undefined structure data for Swagger.

Swagger only knows the defined class-attributes, so the interface returns a Map. For Swagger, there is no field display. This also applies to returning the top parent class of Object. This is why the generic T is applied.

**Applicable to generic T or not displayed**

Many friends will say that I have used generic T, but the document is still not displayed. The main reasons are as follows:

The attribute definition must be a generic T, as follows：

```java
private T data;//return T
```

Returning the T type get method must return T, sometimes automatically generate get, setter method plugin, etc. will generate our code back to Object, for example：

```java
public Object getData(){
    return data;
}
```

The above is the wrong form, although the attribute has been defined as T, the correct way：

```java
public T getData(){
    return data;
}
```

The most important step, the above steps are completely correct, the code is no problem, but ui still does not display attributes, you must strongly specify the generic type in the interface layer (may be Swagger requires us to write code to be standardized ~~~), as follows：

![](images/nf4.png)

If the above situation is ok, or do not show instructions, congratulations you found a bug in SwaggerBootstrapUi, welcome to mention the [issue](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues) feedback, I will get it ~~!

Finally paste a simple return package class for your reference([Rest.java](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/blob/master/swagger-bootstrap-ui-demo/src/main/java/com/swagger/bootstrap/ui/demo/common/Rest.java?tdsourcetag=s_pctim_aiomsg))

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

