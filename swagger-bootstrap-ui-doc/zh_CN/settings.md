**个性化设置**功能是SwaggerBootstrapUi针对本身Ui特点提供的个性化设置功能,主要包括：

- 开启请求参数缓存
- 菜单Api地址显示
- 分组tag显示description说明属性
- 开启RequestMapping接口类型重复地址过滤
- 开启SwaggerBootstrapUi增强功能.

功能目录：**文档管理 -> 个性化设置**

![](images/settings.png)

**开启请求参数缓存**

此功能在在线调试时可见效果,当针对每个接口点击发送调试查看后,后面打开该接口再调试时,默认为保留上一次发送的接口参数信息

如果不想开启此缓存,不勾选此项即可.默认为true,即开启状态

**菜单Api地址显示**

菜单Api地址显示是在左侧菜单不显示api地址信息,默认为false,即不显示,默认效果如下图
![](images/url-no.png)

如果需要左侧菜单栏显示接口地址,则勾选此项接口,显示效果图如下：

![](images/url-display.png)

**分组tag显示description说明属性**

tag是否显示代码中的description属性,默认为false,及不显示，如果勾选显示description属性,效果图如下：

![](images/tag-desc.png)

**开启RequestMapping接口类型重复地址过滤**

针对后端`RequestMapping`注解类型的接口,如果开发者没有指定接口类型,默认使用Swagger会生成七个不同类型的接口地址,效果图如下：

![](images/rp-multipar.png)

再某些情况下,开发者可能需要过滤,简化重复的接口文档,此时,开发者通过勾选此选项,并在后面选择显示接口类型的选项,SwaggerBootstrapUi会根据此选项自动过滤

例如勾选，然后默认显示Post类型，则效果如下：

![](images/rp-multipar-filter.png)

此项默认为false,即不开启此项(不过滤).

**开启SwaggerBootstrapUi增强功能**

开启此项后,可使用SwaggerBootstrapUi的增强功能,关于增强功能,可参考[增强功能](enh-func.md)章节介绍说明