# 基于静态Swagger JSON文件

基于静态Swagger JSON文件的方式预览很简单,首先需要一个HTTP的web服务即可

两种选择：

- Nginx
- IIS

不管是基于nginx还是IIS，首先都需要先本地生成一个静态的Swagger JSON文件结构

`swagger-bootstrap-ui`此处提供了一个简单的示例

修改项目json目录下的`group.json`

```json
[
  {
    "name": "swagger",
    "url": "/json/swagger.json",
    "swaggerVersion": "2.0",
    "location": "/json/swagger.json"
  },
  {
    "name": "swagger1",
    "url": "/json/swagger1.json",
    "swaggerVersion": "2.0",
    "location": "/json/swagger1.json"
  }
]
```

我们在静态的`group.json`文件中预定义了两个静态的swagger JSON文件,也同时存放在json文件夹中

`swagger.json`的内容为swagger接口`/v2/api-docs`中响应的内容

```json
{
  "swagger": "2.0",
  "info": {
    "description": "<div style='font-size:14px;color:red;'>swagger-bootstrap-ui-demo RESTful APIs</div>",
    "version": "1.0",
    "title": "swagger-bootstrap-ui很棒~~~！！！",
    "termsOfService": "http://www.group.com/",
    "contact": {
      "name": "group@qq.com"
    }
  },
  "host": "127.0.0.1:8999",
  "basePath": "/"
   //more.....
}
```

当我们完成以上步骤后,即可通过nginx或者IIS部署静态文件浏览我们的接口文档了

## 基于nginx

基于nginx的方式,只需要将`swagger-bootstrap-ui`的所有静态文件拷贝到响应目录,然后再nginx的conf配置文件中配置server节点即可

参考信息如下：

```shell
server {
        listen       18001;
        server_name  192.168.0.112;
        #charset koi8-r;

        location / {
            #此处为swagger-bootstrap-ui项目中resources目录中的静态资源;
            root /mnt/application/swagger-static;
        }

    }

```

## 基于IIS

在Windows系统中,可以使用IIS部署我们的静态站点,快速预览Swagger文档

具体路径：

控制面板 -> 管理工具 -> Internet Information Services (IIS)管理器 -> 添加网站 -> 选择静态目录 -> 浏览

如果没有IIS管理器,你首先需要自行安装该服务,至于如何安装IIS,自行搜索解决,此处不再骜述.