# AfterScript

![输入图片说明](/images/knife4j/afterScripts.gif "AfterScript.gif")
`AfterScript`功能是`Knife4j`自`2.0.6`版本开始新增的一项特性功能，在每个接口进行调试Tab中,开发者可以根据`Knife4j`提供的全局变量,在接口调用之前编写一段`JavaScript`脚本,当接口调用成功后,`Knife4j`会执行该脚本。

主要应用场景：

- 针对JWT类型的接口,调用登录接口后,每个接口请求时带上Token参数,此时可以通过该脚本动态赋值全局token参数,省去复制粘贴的麻烦.

## 全局对象

`Knife4j`目前主要提供`ke(Knife4j Environment)`对象来获取或者操作全局对象,主要包含的对象：

- global:全局操作,可以获取或者设置目前的全局参数
  - setHeader(`name`,`value`):设置当前逻辑分组下的全局参数Header请求头
  - setAllHeader(name,value):设置所有逻辑分组下的全局参数Header请求头
  - setParameter(`name`,`value`):设置当前逻辑分组下，主要是针对`query`类型参数进行设置全局设置。
  - setAllParameter(name,value):设置所有逻辑分组下的全局参数，主要是`query`类型
- response:当前请求接口响应内容
  - headers:服务端响应Header对象,注意,此处所有的header的名称全部进行小写转换
  - data:服务端响应数据(json/xml/text等等)

设计结构：

```javascript
ke={
    global:{
        setHeader:function(name,value){
            
        },
        setAllHeader:function(name,value){
            
        },
        setParameter:function(name,value){
            
        },
        setAllParameter:function(name,value){
            
        }
    },
    response:{
        headers:{
            
        },
        data:{
            
        }
    }
}
```

## 代码示例

### 获取服务端响应的Header参数

当我们调用API接口后,开发者如果想拿到服务端写出的响应Header头-`Content-Type`,并且进行控制台输出打印,可以这样编写`AfterScript`

```javascript
var contentType=ke.response.headers["content-type"];
console.log("响应ContentType:"+contentType)
```

### 根据服务端响应的值设置全局Header

假设某一个接口响应的JSON内容如下：

```json
{
  "code": 8200,
  "message": null,
  "data": {
    "token": "1y1tn8tvw93fxixp79dcx0nugixkw4su"
  }
}
```

该接口是登录接口,每个接口请求都需要带上`token`的请求头,因此我们访问登录接口后,设置全局Header参数`token`,代码如下：

```javascript
var code=ke.response.data.code;
if(code==8200){
    //判断,如果服务端响应code是8200才执行操作
    //获取token
    var token=ke.response.data.data.token;
    //1、如何参数是Header，则设置当前逻辑分组下的全局Header
    ke.global.setHeader("token",token);
    //2、如果全局参数是query类型,则设置当前逻辑分组下的全局Parameter,开发者自行选择
    ke.global.setParameter("token",token);
}
```

 
 
 