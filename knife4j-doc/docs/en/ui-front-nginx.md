# Nginx Proxy

In the section of static deployment preview Swagger JSON, we have talked about how to deploy static file preview documents through nginx, but at this point we will find a problem, that is, the interface can not be debugged.

With the help of the reverse proxy function of nginx, we can realize the debugging function of the interface.

Assuming that static JSON is still available, we only need to add a layer of location to the configuration node of nginx.

like：

```shell
server {
        listen       18001;
        server_name  192.168.0.112;
        #charset koi8-r;

        location / {
            root /mnt/application/swagger-bootstrap-ui-front;
        }
        location /api/ {
        	// proxy api server
            proxy_pass http://127.0.0.1:8999/api/;
            client_max_body_size   200m;
        }

    }
```

With the above configuration, we can preview and debug our interface documents.

**But**

We will also find the problem. Many times, the interfaces we write may not be standardized. Not all interfaces start with / api, or with fixed other formats. If we configure the interfaces above, we will not be able to adjust them when our interfaces appear in the form of `admin / test'. Try this interface

Maybe we can change the way we think of all interfaces under this service as a service. We give a service a characteristic name, and then aggregate the documents by aggregating the services so that it can be debugged.

for example：

Understand `127.0.0.1:8999'` as `service 1`

When we access the interface of the service, we add the service prefix: `service 1/api/xxx`. At this time, no matter how irregular our interface is, as long as it is the interface under service 1, nginx will forward it to `127.0.0.1:8999', so that we have completed the debugging of the interface.

nginx conf：

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

Obviously, through the above configuration, we can finally achieve our expectations, but in our document interface, there is no `service1'basePath attribute for us to configure, at this time, how should we deal with it?

In this case, `swagger-bootstrap-ui` extends a `basePath attribute value in the grouping attribute.

our `group.json` file like this：

```json
[
  {
    "name": "user-service",
    "url": "/service1/v2/api-docs?group=分组接口",
    "swaggerVersion": "2.0",
    "location": "/service1/v2/api-docs?group=分组接口",
    "basePath":"/service1"
  },
  {
    "name": "order-service",
    "url": "/service2/v2/api-docs?group=默认接口",
    "swaggerVersion": "2.0",
    "location": "/service2/v2/api-docs?group=默认接口",
    "basePath":"/service2"
  }
]
```

At this point, our Swagger JSON path address, we can also use our service to provide us with the interface address, just add the name of the service, the group name can get the Swagger JSON file of the service.

In this way, we can aggregate all back-end Swagger service interfaces in the `group.json` file and eventually output consistent display.

effect：

![](/knife4j/images/front-1.png)


 
 
 