# nginx反向代理路径无法识别

* 项目配置文件中增加
```
server.use-forward-headers=true
```
自 springboot 3.2 之后设置
```
server.forward-headers-strategy=framework
```

* 在反向代理增加指定header

```
proxy_set_header X-Forwarded-Prefix ${your_foward_path};
```

## 官方教程指引地址
``` http
https://springdoc.org/#how-can-i-deploy-springdoc-openapi-starter-webmvc-ui-behind-a-reverse-proxy
```

### 解决问题来自

* @https://github.com/SiriusTseng
* @https://github.com/lgxisbb