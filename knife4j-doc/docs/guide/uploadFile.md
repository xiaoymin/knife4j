# 文件上传

关于文件上传的支持,很多朋友询问为何Ui界面不显示上传选择页面元素,而是输入框,这里做一个统一的说明

在`SwaggerBootstrapUi`以前的版本中(1.8.9之前),如果需要使用文件上传，界面显示,需要做如下代码设置：

```java
@ApiOperation(value = "多文件MultipartFile上传")
@ApiImplicitParams({@ApiImplicitParam(name = "file[]", value = "文件流对象,接收数组格式", required = true,dataType = "MultipartFile",allowMultiple = true),
                    @ApiImplicitParam(name = "title", value = "title", required = true)}
                  )
@RequestMapping(value="/uploadMaterial",method = RequestMethod.POST)
@ResponseBody
public RestMessage uploadMaterial(@RequestParam(value="file[]",required = true) MultipartFile[] files,@RequestParam(value = "title") String title, HttpServletRequest request) throws IOException {
    //int mul=1*1024*1024;
    List<Map> uploadFiles= upload(request,files);
    RestMessage rm=new RestMessage();
    rm.setData(uploadFiles);
    return rm;
}
```

需要指定`dataType="MultipartFile"`,并且`allowMultiple = true`必须设置,该属性从字面意思能知道,允许多文件上传，这里需要说明一下,因为在以前的版本作者并不知道文件的类型,所以特意强加了`MultipartFile`类型,来达到Ui的线上显示效果,所以单文件的上传一直并未支持.

在**1.9.0**版本中，添加了对单文件上传的支持,多文件上传不需要多个input元素,开发者只需要按住`Ctrl`键即可多选文件进行上传，三种情况供大家参考使用：

## 多文件MultipartFile类型上传

这种类型在以前的版本中都支持,需要指定`dataType="MultipartFile"`,并且`allowMultiple = true`,示例代码如下：

```java
@ApiOperation(value = "多文件MultipartFile上传")
@ApiImplicitParams({@ApiImplicitParam(name = "file[]", value = "文件流对象,接收数组格式", required = true,dataType = "MultipartFile",allowMultiple = true),
                    @ApiImplicitParam(name = "title", value = "title", required = true)}
                  )
@RequestMapping(value="/uploadMaterial",method = RequestMethod.POST)
@ResponseBody
public RestMessage uploadMaterial(@RequestParam(value="file[]",required = true) MultipartFile[] files,@RequestParam(value = "title") String title, HttpServletRequest request) throws IOException {
    //more.....

}
```

在Ui的界面中,显示效果如下：

![](/knife4j/images/multipartUpload1.png)

![](/knife4j/images/multipartUpload2.png)

## 多文件File类型上传

除了dataType类型设置为`MultipartFile`外,开发者还可以设置为`__File`类型,代码示例如下：

```java
@ApiOperation(value = "多文件File上传")
@ApiImplicitParams({@ApiImplicitParam(name = "file[]", value = "文件流对象,接收数组格式", required = true,dataType = "__File",allowMultiple = true),
                    @ApiImplicitParam(name = "title", value = "title", required = true)}
                  )
@RequestMapping(value="/uploadMaterial1",method = RequestMethod.POST)
@ResponseBody
public RestMessage uploadMaterial1(@RequestParam(value="file[]",required = true) MultipartFile[] files,@RequestParam(value = "title") String title, HttpServletRequest request) throws IOException {
    //more....
}
```

![](/knife4j/images/multipartFile2.png)

![](/knife4j/images/multipartFile1.png)

## 单文件File类型上传

和多文件File类型上传类似,只需要去掉`allowMultiple = true`属性即可，代码示例如下：

```java
@ApiOperation(value = "单文件File上传")
@ApiImplicitParams({@ApiImplicitParam(name = "file", value = "文件流对象,接收数组格式", required = true,dataType = "__File"),
                    @ApiImplicitParam(name = "title", value = "title", required = true)}
                  )
@RequestMapping(value="/uploadMaterial2",method = RequestMethod.POST)
@ResponseBody
public RestMessage uploadMaterial2(@RequestParam(value="file",required = true) MultipartFile file,@RequestParam(value = "title") String title, HttpServletRequest request) throws IOException {
    //more...
}
```

![](/knife4j/images/upfile1.png)

![](/knife4j/images/upfile2.png)

以上三种情况示例可参考demo代码[**UploadController.java**](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/blob/master/swagger-bootstrap-ui-demo/src/main/java/com/swagger/bootstrap/ui/demo/controller/UploadController.java)
 
 
 