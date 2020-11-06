# Download And Preview

目前`SwaggerBootstrapUi`支持的响应类型如下：

| 类型                     | 说明     |
| ------------------------ | -------- |
| application/octet-stream | 二进制流 |
| image/png                | 图片     |
| image/jpg                | 图片     |
| image/jpeg               | 图片     |
| image/gif                | 图片     |

**特别需要注意的是**：不管是文件下载或者是需要图片预览,都需要在接口中指定接口的`produces`,否则不能达到预期效果,接口的produces可参考上面表格中列出项.

# 关于文件下载的支持

`SwaggerBootstrapUi`在`1.8.9`版本中添加了`application/octet-stream`下载类型的支持,并在`1.9.0`版本中完善,只需要配置相应接口的produces,即可在`doc.html`页面中查看效果，如下图：

![](/knife4j/images/filedownload1.png)

![](/knife4j/images/filedownload2.png)

点击下载文件即可下载当前接口响应的二进制流.示例代码可参考[**Api190Controller.java**](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/blob/master/swagger-bootstrap-ui-demo/src/main/java/com/swagger/bootstrap/ui/demo/group/Api190Controller.java)

特别说明,需要指定相应的produces

```java
@ApiOperation(value = "下载测试-有参数+请求头版",position = 3)
@GetMapping(value = "/downloadFileAndParam2",produces = "application/octet-stream")
public void postRequest3AndParam(@RequestHeader(value = "uud") String uud,@RequestParam(value = "name") String name, HttpServletRequest request, HttpServletResponse response){
    logger.info("header:{}",uud);
    download(name,response);
}
```



# 关于图片预览的支持

图片预览一般用在验证码等场景中,很多时候,需要直接展示出验证码的情况，如下图：

![](/knife4j/images/preview1.png)

![](/knife4j/images/preview2.png)

验证码预览的后端代码可参考[**ImageController.java**](https://gitee.com/xiaoym/swagger-bootstrap-ui-demo/blob/master/swagger-bootstrap-ui-demo/src/main/java/com/swagger/bootstrap/ui/demo/group/ImageController.java)

特别说明,需要指定相应的produces

```java
@Api(value = "图片预览",tags = "图片预览")
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @GetMapping(value = "/preview",produces = "image/jpeg")
    public void preview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //more....
    }
}
```




 
 <icp/> 
 comment/> 
 
 
 
 