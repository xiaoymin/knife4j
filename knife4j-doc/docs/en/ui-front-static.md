# Static Swagger JSON

Preview based on static Swagger JSON file is simple. First, you need an HTTP web service.

Two options:

- Nginx
- IIS

Whether it's based on nginx or IIS, you first need to generate a static swagger JSON file structure locally.

`swagger-bootstrap-ui` A simple example is provided here.

Modify `group.json'in the project JSON directory`

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

We predefined two static swagger JSON files in the static `group.json` file and stored them in the JSON folder at the same time.

 
The content of ` swagger.json` is the content of the response in the swagger interface `v2/api-docs`.

```json
{
  "swagger": "2.0",
  "info": {
    "description": "<div style='font-size:14px;color:red;'>swagger-bootstrap-ui-demo RESTful APIs</div>",
    "version": "1.0",
    "title": "swagger-bootstrap-ui is great~~~！！！",
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

When we have completed the above steps, we can browse our interface documents through nginx or IIS deployment static files.

## nginx

In the nginx-based way, you only need to copy all the static files of `swagger-bootstrap-ui-front` to the response directory, and then configure the server node in the conf configuration file of nginx.

like ：

```shell
server {
        listen       18001;
        server_name  192.168.0.112;
        #charset koi8-r;

        location / {
            # ui location;
            root /mnt/application/swagger-bootstrap-ui-front;
        }

    }

```

## IIS

In Windows, we can deploy our static site using IIS to quickly preview Swagger documents.

Specific path：

Control Panel - > Management Tool - > Internet Information Services (IIS) Manager - > Add Website - > Select Static Directory - > Browse

If there is no IIS manager, you need to install the service by yourself first. As for how to install IIS and search for solutions by yourself, I will not elaborate here.
 
 
 