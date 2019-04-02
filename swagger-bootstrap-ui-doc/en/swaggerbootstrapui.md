With the above two interfaces of Springfox-Swagger, you can generate pages based on these two interfaces. There is a premise here, why can it be generated based on this, because the two interface addresses given by Springfox-Swagger are fixed. So writing this UI can also get generic.

The front-end technology stack used by swagger-bootstrap-ui mainly includes：

| attr         | intro                                               |
| ------------ | --------------------------------------------------- |
| jquery       | <http://jquery.com/>                                |
| bootstrap    | [http://getbootstrap.com](http://getbootstrap.com/) |
| layer        | <http://layer.layui.com/>                           |
| jsonviews    | <https://github.com/yesmeck/jquery-jsonview>        |
| clipboard    | <https://github.com/zenorocha/clipboard.js>         |
| axios.min.js | <https://github.com/axios/axios>                    |
| marked       | <https://github.com/markedjs/marked>                |
| art-template | <https://github.com/aui/art-template>               |

Here mainly talk about some ideas of swagger-bootstrap-ui, source code, then you can go to the [Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui) or [GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI) to see

1、Build the SwaggerBootstrapUi main object, similar to the Java backend object-oriented way to write, define some basic properties, which is also convenient for later extension

```javascript
var SwaggerBootstrapUi=function () {
    //swagger请求api地址
    this.url="swagger-resources";
    //文档id
    this.docId="content";
    //tabid
    this.tabId="tabUl";
    this.tabContentId="tabContent";
    this.searchEleId="spanSearch";
    this.searchTxtEleId="searchTxt";
    this.menuId="menu";
    this.searchMenuId="searchMenu";
    //实例分组
    this.instances=new Array();
    //当前分组实例
    this.currentInstance=null;
    //动态tab
    this.globalTabId="sbu-dynamic-tab";
    this.globalTabs=new Array();
    this.tabsLiContent=null;
    this.tabsPostProcessors=null;
}
```

Include the properties of the swagger's response, and also re-define the function in js, using an object-oriented approach

![](images/sbudef.png)

2、Initialization work, the main method of the sbu is the main method, similar to the main method of SpringBoot, the source code reader can enter from this method

```javascript
/***
     * swagger-bootstrap-ui的main方法,初始化文档所有功能,类似于SpringBoot的main方法
     */
SwaggerBootstrapUi.prototype.main=function () {
    var that=this;
    that.initWindowWidthAndHeight();

    that.windowResize();
    //加载分组接口
    that.analysisGroup();
    //创建分组元素
    that.createGroupElement();
    //搜索
    that.searchEvents();

}
```

3、Data and page separation, rendered using art-template templates, thus maintaining js independence