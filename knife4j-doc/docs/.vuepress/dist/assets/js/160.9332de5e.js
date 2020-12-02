(window.webpackJsonp=window.webpackJsonp||[]).push([[160],{620:function(t,e,a){"use strict";a.r(e);var s=a(42),n=Object(s.a)({},(function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("ContentSlotsDistributor",{attrs:{"slot-key":t.$parent.slotKey}},[a("h1",{attrs:{id:"nginx-proxy"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#nginx-proxy"}},[t._v("#")]),t._v(" Nginx Proxy")]),t._v(" "),a("p",[t._v("In the section of static deployment preview Swagger JSON, we have talked about how to deploy static file preview documents through nginx, but at this point we will find a problem, that is, the interface can not be debugged.")]),t._v(" "),a("p",[t._v("With the help of the reverse proxy function of nginx, we can realize the debugging function of the interface.")]),t._v(" "),a("p",[t._v("Assuming that static JSON is still available, we only need to add a layer of location to the configuration node of nginx.")]),t._v(" "),a("p",[t._v("like：")]),t._v(" "),a("div",{staticClass:"language-shell extra-class"},[a("pre",{pre:!0,attrs:{class:"language-shell"}},[a("code",[t._v("server "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n        listen       "),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("18001")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n        server_name  "),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("192.168")]),t._v(".0.112"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n        "),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("#charset koi8-r;")]),t._v("\n\n        location / "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n            root /mnt/application/swagger-bootstrap-ui-front"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n        "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n        location /api/ "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n        \t// proxy api server\n            proxy_pass http://127.0.0.1:8999/api/"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n            client_max_body_size   200m"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(";")]),t._v("\n        "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n\n    "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n")])])]),a("p",[t._v("With the above configuration, we can preview and debug our interface documents.")]),t._v(" "),a("p",[a("strong",[t._v("But")])]),t._v(" "),a("p",[t._v("We will also find the problem. Many times, the interfaces we write may not be standardized. Not all interfaces start with / api, or with fixed other formats. If we configure the interfaces above, we will not be able to adjust them when our interfaces appear in the form of `admin / test'. Try this interface")]),t._v(" "),a("p",[t._v("Maybe we can change the way we think of all interfaces under this service as a service. We give a service a characteristic name, and then aggregate the documents by aggregating the services so that it can be debugged.")]),t._v(" "),a("p",[t._v("for example：")]),t._v(" "),a("p",[t._v("Understand "),a("code",[t._v("127.0.0.1:8999'")]),t._v(" as "),a("code",[t._v("service 1")])]),t._v(" "),a("p",[t._v("When we access the interface of the service, we add the service prefix: "),a("code",[t._v("service 1/api/xxx")]),t._v(". At this time, no matter how irregular our interface is, as long as it is the interface under service 1, nginx will forward it to `127.0.0.1:8999', so that we have completed the debugging of the interface.")]),t._v(" "),a("p",[t._v("nginx conf：")]),t._v(" "),a("div",{staticClass:"language-json extra-class"},[a("pre",{pre:!0,attrs:{class:"language-json"}},[a("code",[t._v("server "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n        listen       "),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("18001")]),t._v(";\n        server_name  "),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("192.168")]),t._v("."),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("0.112")]),t._v(";\n        #charset koi8-r;\n\n        location / "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n            root /mnt/application/swagger-static;\n        "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n        location /service1 "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n            proxy_pass http"),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v(":")]),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("//127.0.0.1:8999/;")]),t._v("\n\n        "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n\t\tlocation /service2 "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n            proxy_pass http"),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v(":")]),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("//127.0.0.1:8998/;")]),t._v("\n\n        "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n\n    "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n")])])]),a("p",[t._v("Obviously, through the above configuration, we can finally achieve our expectations, but in our document interface, there is no `service1'basePath attribute for us to configure, at this time, how should we deal with it?")]),t._v(" "),a("p",[t._v("In this case, "),a("code",[t._v("swagger-bootstrap-ui")]),t._v(" extends a `basePath attribute value in the grouping attribute.")]),t._v(" "),a("p",[t._v("our "),a("code",[t._v("group.json")]),t._v(" file like this：")]),t._v(" "),a("div",{staticClass:"language-json extra-class"},[a("pre",{pre:!0,attrs:{class:"language-json"}},[a("code",[a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("[")]),t._v("\n  "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token property"}},[t._v('"name"')]),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v(":")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"user-service"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token property"}},[t._v('"url"')]),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v(":")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"/service1/v2/api-docs?group=分组接口"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token property"}},[t._v('"swaggerVersion"')]),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v(":")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"2.0"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token property"}},[t._v('"location"')]),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v(":")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"/service1/v2/api-docs?group=分组接口"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token property"}},[t._v('"basePath"')]),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v(":")]),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"/service1"')]),t._v("\n  "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("\n  "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token property"}},[t._v('"name"')]),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v(":")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"order-service"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token property"}},[t._v('"url"')]),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v(":")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"/service2/v2/api-docs?group=默认接口"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token property"}},[t._v('"swaggerVersion"')]),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v(":")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"2.0"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token property"}},[t._v('"location"')]),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v(":")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"/service2/v2/api-docs?group=默认接口"')]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token property"}},[t._v('"basePath"')]),a("span",{pre:!0,attrs:{class:"token operator"}},[t._v(":")]),a("span",{pre:!0,attrs:{class:"token string"}},[t._v('"/service2"')]),t._v("\n  "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("]")]),t._v("\n")])])]),a("p",[t._v("At this point, our Swagger JSON path address, we can also use our service to provide us with the interface address, just add the name of the service, the group name can get the Swagger JSON file of the service.")]),t._v(" "),a("p",[t._v("In this way, we can aggregate all back-end Swagger service interfaces in the "),a("code",[t._v("group.json")]),t._v(" file and eventually output consistent display.")]),t._v(" "),a("p",[t._v("effect：")]),t._v(" "),a("p",[a("img",{attrs:{src:"/knife4j/images/front-1.png",alt:""}})]),t._v(" "),a("comment-comment")],1)}),[],!1,null,null,null);e.default=n.exports}}]);