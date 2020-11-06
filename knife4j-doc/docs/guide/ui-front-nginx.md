# 基于Nginx反向代理

在静态部署预览Swagger JSON章节中我们已经讲过如何通过nginx来部署静态文件预览文档，但此时我们会发现存在一个问题，即无法进行接口的调试。

我们借助于nginx的反向代理功能,帮助我们实现接口的调试功能

假设还是提供静态JSON的方式,我们只需要在nginx的配置节点中添加一层location即可

如下：

```shell
server {
        listen       18001;
        server_name  192.168.0.112;
        #charset koi8-r;

        location / {
            root /mnt/application/swagger-static;
        }
        location /api/ {
        	// Swagger JSON文件中所有以api开头的接口全部走8999的代理
            proxy_pass http://127.0.0.1:8999/api/;
            client_max_body_size   200m;
        }

    }
```

通过以上配置,我们即可预览及调试我们的接口文档

**但是**

我们又会发现问题,很多时候,我们所写的接口可能并不规范,并非所有的接口都是以/api开头的,或者以固定的其他格式开头的接口,那此时如果我们以上面的配置方式来配置,当我们的接口以`/admin/test`这种形式出现时,我们就无法调试该接口

那或许我们可以换一种方式,我们将该服务下的所有接口理解为一个服务,我们给一个服务取一个特点的名称,然后通过聚合服务的方式,将文档聚合显示出来,这样既可进行调试

例如：

将`127.0.0.1:8999`理解为`service1`

我们在访问该服务的接口时加上服务前缀：`/service1/api/xxx`,此时,不管我们的接口又多么不规范,只要是service1下的接口,nginx都会将它转发到`127.0.0.1:8999`这台服务上,这样我们也完成了接口的调试

nginx配置：

```json
server {
        listen       18001;
        server_name  192.168.0.112;
        #charset koi8-r;

        location / {
            root /mnt/application/swagger-static;
        }
        location /service1 {
            proxy_pass http://127.0.0.1:8999/;

        }
		location /service2 {
            proxy_pass http://127.0.0.1:8998/;

        }

    }
```

很显然,通过以上配置,最终能达到我们的预期,但是在我们的文档界面中,没有`service1`这样的basePath属性供我们配置,此时,我们应该如何处理

针对这种情况，`swagger-bootstrap-ui`在分组属性中,扩展了一个`basePath`属性值

此时，我们的`group.json`文件如下：

```json
[
  {
    "name": "微服务-用户模块",
    "url": "/service1/v2/api-docs?group=分组接口",
    "swaggerVersion": "2.0",
    "location": "/service1/v2/api-docs?group=分组接口",
    "basePath":"/service1"
  },
  {
    "name": "微服务-订单模块",
    "url": "/service2/v2/api-docs?group=默认接口",
    "swaggerVersion": "2.0",
    "location": "/service2/v2/api-docs?group=默认接口",
    "basePath":"/service2"
  }
]
```

此时,我们的Swagger的JSON路径地址,我们也可以使用我们服务提供给我们的接口地址，只需要加上为服务名称,分组名称即可得到该服务的Swagger JSON文件

通过这种方式,我们可以在`group.json`文件中聚合所有后端的Swagger服务接口,最终一致输出显示

效果如下：

![](/knife4j/images/front-1.png)


 
 
 