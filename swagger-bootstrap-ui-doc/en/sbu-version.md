## 版本发布历史

----
| 新闻标题                                                     | 发布时间   |
| ------------------------------------------------------------ | ---------- |
| [Swagger-Bootstrap-Ui 1.8.9 发布，Swagger增强UI 实现](https://www.oschina.net/news/103518/swagger-bootstrap-ui-1-8-9-released) | 2019-01-11 |
| [Swagger-Bootstrap-Ui 1.8.8 发布，Swagger 增强 UI 实现](https://www.oschina.net/news/102771/swagger-bootstrap-ui-1-8-8-released) | 2018-12-17 |
| [Swagger-Bootstrap-Ui 1.8.7 发布，Swagger增强UI 实现](https://www.oschina.net/news/101720/swagger-bootstrap-ui-1-8-7-released) | 2018-11-12 |
| [Swagger-bootstrap-ui 1.8.6 发布，Swagger增强UI 实现](https://www.oschina.net/news/101346/swagger-bootstrap-ui-1-8-6-released) | 2018-10-31 |
| [swagger-bootstrap-ui 1.8.5 发布，Swagger增强UI实现](https://www.oschina.net/news/100888/swagger-bootstrap-ui-1-8-5-released) | 2018-10-16 |
| [swagger-bootstrap-ui 1.8.4 发布，Swagger 前端 UI 实现](https://www.oschina.net/news/100295/swagger-bootstrap-ui-1-8-4-released) | 2018-09-25 |
| [swagger-bootstrap-ui 1.8.3 发布，Swagger前端 UI 实现](https://www.oschina.net/news/100052/swagger-bootstrap-ui-183-released) | 2018-09-17 |
| [swagger-bootstrap-ui 1.8.2 发布，Swagger前端 UI 实现](https://www.oschina.net/news/99355/swagger-bootstrap-ui-1-8-2-released) | 2018-08-27 |
| [swagger-bootstrap-ui 1.8.1 发布，Swagger前端 UI 实现](https://www.oschina.net/news/98955/swagger-bootstrap-ui-1-8-1-released) | 2018-08-14 |
| [swagger-bootstrap-ui 1.8.0 发布，Swagger 前端 UI 实现](https://www.oschina.net/news/98840/swagger-bootstrap-ui-1-8-0-released) | 2018-08-10 |
| [swagger-bootstrap-ui 1.7.9 发布，Swagger前端 UI 实现](https://www.oschina.net/news/98711/swagger-bootstrap-ui-1-7-9-released) | 2018-08-06 |
| [swagger-bootstrap-ui 1.7.8发布，Swagger前端UI实现](https://www.oschina.net/news/98631/swagger-bootstrap-ui-1-7-8-released) | 2018-08-03 |
| [swagger-bootstrap-ui 1.7.7 发布，Swagger 前端 UI 实现](https://www.oschina.net/news/98335/swagger-bootstrap-ui-177-released) | 2018-07-25 |
| [swagger-bootstrap-ui 1.7.6 发布，Swagger 前端 UI 实现](https://www.oschina.net/news/98128/swagger-bootstrap-ui-176-released) | 2018-07-18 |
| [swagger-bootstrap-ui 1.7.5，Swagger前端 UI 实现](https://www.oschina.net/news/98042/swagger-bootstrap-ui-1-7-5-released) | 2018-07-16 |
| [swagger-bootstrap-ui 1.7 发布，前端 UI 实现](https://www.oschina.net/news/91637/swagger-bootstrap-ui-1-7) | 2017-12-18 |
| [swagger-bootstrap-ui 1.6 发布，前端 UI 实现](https://www.oschina.net/news/88444/swagger-bootstrap-ui-1-6) | 2017-09-06 |
| [swagger-bootstrap-ui 1.5 发布，前端 UI 实现](https://www.oschina.net/news/88285/swagger-bootstrap-ui-1-5) | 2017-09-01 |
| [swagger-bootstrap-ui 开源](https://www.oschina.net/p/swagger-bootstrap-ui) | 2017-04-19 |

## 版本发布历史详情

----
- **[2019/01/11] - [swagger-bootstrap-ui 1.8.9 发布，Swagger 增强 UI 实现](https://www.oschina.net/news/103518/swagger-bootstrap-ui-1-8-9-released)**
> 主页面添加页面不缓存元素,防止版本升级缓存造成新功能加载失败.
>
> 响应示例说明、调试响应内容行添加description说明字段,免去切换到文档说明看字段说明的麻烦,非常感谢[@wanyaxing](https://github.com/wanyaxing)提交的[PR](https://github.com/xiaoymin/Swagger-Bootstrap-UI/pull/57)
>
> 新增个性化配置-开启RequestMapping接口类型重复地址过滤,默认只显示POST类型的接口地址(针对RequestMapping的接口请求
>
> 类型,在不指定参数类型的情况下,如果不过滤,默认会显示7个类型的接口地址参数,如果开启此配置,默认展示一个Post类型的接口地址)
>
> 针对application/octet-stream类型的接口提供下载调试.
>
> 启用UI增强时,获取不到`WebApplicationContext`对象造成空指针异常
>
> 修复list套list的返回值会不显示[issue #55 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/55)
>
> 接口请求参数同全局参数配置名称存在冲突的情况下,根据名称匹配导致参数丢失,匹配规则为参数名称、参数类型同时比较[issue #IQV1U @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IQV1U)
>
> 服务端响应HTML标签数据时,响应内容显示异常[issue #IQ9LG @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IQ9LG)
>
> 修复参数格式问题[issue #IPXX7 @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IPXX7)
>
> 针对多响应码返回不同schema类型,离线文档(markdown)未展示完整的bug[issue #IPPHJ @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IPPHJ)
----
- **[2018/12/17] - [swagger-bootstrap-ui 1.8.8 发布，Swagger 增强 UI 实现](https://www.oschina.net/news/102771/swagger-bootstrap-ui-1-8-8-released)**
> **顶部标题可自定义**,去除原默认显示swagger-bootstrap-ui的固定标题,title规则为获取分组对象apiInfo中的第一个title属性.
>
> **个性化配置中新增是否开启请求参数缓存策略**,默认为true，当设置为false时,请求的参数不会再本地产生缓存,下次打开接口调试
>
> 时需要自己重新输入相关接口参数
>
> 分组加载由同步改为异步加载
>
> **新增接口高亮显示**,当后端新增接口后,UI会自动标识该接口为新接口,直到该接口被点击为止.
>
> **当服务器正在重启或者宕机时,接口发生异常,给出友好提示**,告知接口对接人员.
>
> 后端接口方法上**针对@Deprecated标注的接口,UI以中横线标注区分**
>
> 针对不同状态响应码,返回内容均有Schema的情况下，UI以tab方式将所有状态码的schema内容呈现
>
> 优化接口数量过多的情况下,离线文档会导致文档页假死
>
> 修复针对Delete请求,使用@RequestBody注解出现400错误 [issue IPLJT @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IPLJT)
>
> 修复响应状态码HTML标签非转义输出 [issue #47 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/47)
>
> 不能正确解析response内非$ref的schema内容 [issue #43 @Github](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/43)
----
- **[2018/11/12] - [swagger-bootstrap-ui 1.8.7 发布，Swagger 增强 UI 实现](https://www.oschina.net/news/101720/swagger-bootstrap-ui-1-8-7-released)**
> 优化调试框响应内容高度,**根据响应内容自动设置响应高度**,不再设固定高度.
>
> **Authorize**功能提供**注销**功能,清空当前缓存在浏览器的相关Auth信息.
>
> 新增**Swagger Models**菜单项功能,以TreeTable的方式展示当前Swagger分组实例文档中所有相关的Models属性说明.
>
> 个性化配置项**新增是否显示tag分组description属性的选择项**,勾选后,会和swagger官方文档一样显示description属性,默认为false不显示.
>
> 引入**async.js**异步组件库,优化文档解析效率,**解析渲染速度提升5倍以上**.
>
> 优化接口的id生成策略,使用MD5针对接口地址和mehtod方式生成接口id,**调试参数全局缓存localStorage对象中**,方便下次刷新访问调试.
>
> 响应状态栏增加全屏icon,点击全屏icon**可全屏查看响应内容**.
>
> 解决离线文档再开启UI增强功能后不排序的问题
>
> 调试框根据Swagger接口参数显示当前接口的Content-Type类型,在某些特殊情况下可**更改默认定义Content-Type请求头类型**,如果
>
> 使用UI提供的全局参数功能,自定义了Content-Type的请求头,则默认以全局参数中的Content-Type为主.
>
> 增加对**JSR-303 annotations 注解的支持**(部分)
>
> 针对SpringCloud通过网关构建Swagger分组获取不到Documentation对象的情况,根据default再获取一次
>
> 修复UI增强关于使用`@Api`注解tags属性不赋值,使用value，增强排序失败的问题.
>
> 修复针对`@RequestMapping`注解无value属性,UI增强出现数组越界的问题
>
> 修复针对扩展Spring的`RequestMappingHandlerMapping`自定义实现方式,获取不到扩展接口url地址信息,导致UI增强排序失败的问题.
----
- **[2018/10/31] - [swagger-bootstrap-ui 1.8.6 发布，Swagger 增强 UI 实现](https://www.oschina.net/news/101346/swagger-bootstrap-ui-1-8-6-released)**
> 请求参数类型(header|body|query)等以不同颜色着色区分
>
> 调试栏针对必须项(require=true)时,文本框着红色以区分
>
> 调试页输入框可通过tab键自动切换上下级输入框.
>
> 修复Spring使用cglib生成的代理类,导致class无法获取Spring的相关注解,导致接口增强排序失败
>
> 针对basePath属性不是根路径&ldquo;/&rdquo;，导致接口排序比对失败，无法排序的问题
>
> 修复针对SpringCloud通过zuul路由组件加载swagger接口存在basePath属性,增强接口缺失basePath属性的bug,导致增强接口请求失败的问题
>
> 修复Spring的请求地址仅支持value属性，不支持path属性的bug
>
> 针对请求头Content-Type中多余空格问题,部分接口调用失败的问题
>
> 修复针对参数、参数说明太长,导致table换行，样式失效问题.
>
> 修复针对header、path等参数外，传参只包含body类型无请求json示例的问题.
>
> 修复针对请求参数存在多个数组,增加按钮无效的BUG.
>
> 优化离线文档相关的显示格式问题.包括JSON显示格式错乱、添加请求JSON示例、文档开始说明等信息
----
- **[2018/10/16] - [swagger-bootstrap-ui 1.8.5 发布，Swagger 增强 UI 实现](https://www.oschina.net/news/100888/swagger-bootstrap-ui-1-8-5-released)**
> swagger-bootstrap-ui在1.8.5以后,她不在是一个纯webjar的UI工具了,她增强了swagger的一些功能支持,例如tags、接口的排序,一些个性化的支持,目前只增强接口排序
>
> fixed formdata类型参数针对array数组类型无增加按钮
>
> fixed 响应内容高度占比,参数过多的情况无法显示
>
> 多选项卡文档介绍、在线调试position位置引起的不适改动,由竖变横.
>
> 增强排序功能，添加个性化配置管理功能,可开启个性化配置
>
> 关于个性化增强功能,目前已经实现了tags、和接口api方法的排序,使用方式见[增强功能](enh-func.md)
>
> 默认去除接口api地址的线上,默认只显示方法类型、方法说明两个属性,当然,新版本增加的个性化的配置功能，如果你觉得api地
>
> 址显示任然有需要,可在个性化配置中开启该功能，个性化配置属性存储在localStorage对象中.只需要配置一次接口.
>
> fixed 构建curl功能中写死http,根据`window.location.href`动态判断(http|https)的情况
>
> 如果请求参数是json参数body类型，文档说明中添加**请求示例**json展示,方便查看
>
> 请求示例、响应示例json自动适配高度
>
> 选中接口api菜单时,菜单显示激活色,显示背景颜色background-color: #eee;
>
> fixed 离线文档markdown格式错乱问题(table标题换行导致显示异常)
>
> 离线文档已预览html的方式展现,复制文档功能依然是复制markdown语法
>
> 请求参数及响应参数说明改为多行显示,超出长度不以省略号显示,防止出现浮层一直显示的bug
----
- **[2018/09/25] - [swagger-bootstrap-ui 1.8.4 发布，Swagger 前端 UI 实现](https://www.oschina.net/news/100295/swagger-bootstrap-ui-1-8-4-released)**
> fixed key-value表单请求 @RequestParam映射无效,在线调试bug[issue #IMXOV @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMXOV)、[issue #30 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/30)
>
> fixed 树形model默认展开[issue #IMXH5 @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMXH5)
>
> fixed 两个list里放同一个bean，一个显示一个不显示[issue #IMXOY @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMXOY)
>
> fixed 同时传输文本信息和文件时，值重复[issue #IMXDT @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMXDT)
>
> fixed issue [#IN03Q](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IN03Q)
>
> fixed 响应类 3层嵌套解析不出来[issue #IMXOF @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMXOF)
>
> fixed 全局参数设置接口中已有变量,会导致在线调试里面出现2个参数,不方便调试(如果后端swagger配置文件中使用
>
> globalParameter设置全局参数，并且赋予默认值，则以后端全局参数值为准)[issue #IMXVD @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMXVD)
>
> fixed ["text/plain"] controller接收问题[issue #IN0PC @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IN0PC)
>
> 优化调试页响应高度，ace-editor响应高度
>
> 默认在Swagger-bootstrap-ui的请求,UI会增加一个默认的请求头`Request-Origion`:`SwaggerBootstrapUi`
>
> fixed Authorize默认tab不选中的bug
>
> fixed curl响应参数,针对中文urlencode处理
----
- **[2018/09/17] - [swagger-bootstrap-ui 1.8.3 发布，Swagger 前端 UI 实现](https://www.oschina.net/news/100052/swagger-bootstrap-ui-183-released)**
> 新增tab选项卡,各个api接口详情通过新开选项卡来展现
>
> 去除原schema表格形式展示，请求参数、响应参数改由treetable组件(树组件)展示
>
> fixed 请求参数有array类型,显示为schema类型的bug
>
> fixed springcloud zuul 整合ui情况下 地址多个/[ISSUE #IMF0L @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMF0L)
>
> 响应内容去除cookies选项卡，响应示例、响应内容使用ace-editor展示响应内容，方便复制
>
> 优化(全局参数&Authorize)加入浏览器缓存问题,使用localStorage对象全局存储[issue #IMH77 @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMH77)
>
> fixed 泛型数据接口返回list类型时，不能解析[issue #26 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/26)
>
> fixed 模型内部包含模型没有展示[issue #25 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/25)
>
> 优化请求参数是否必填样式,如果该参数必填,则以红色标注显示[issue #22 @Github](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/22)
>
> fixed DELETE请求不能正确处理Query参数 [issue #19 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/19)
>
> fixed 请参数类型为 formData 的参数，填写了参数值还是提示 参数不能为空[issue #24 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/24)、[issue #IMMMJ @Gitee](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMMMJ)
>
> 优化离线文档多行，换行、多空格显示问题
----
- **[2018/08/27] - [swagger-bootstrap-ui 1.8.2 发布，Swagger 前端 UI 实现](https://www.oschina.net/news/99355/swagger-bootstrap-ui-1-8-2-released)**
> fixed 关于@ApiModelProperty的value不支持\n [issue #IM7XC @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM7XC)
>
> fixed 关于在线调试界面显示的优化,调试栏新增参数类型列,区分数据参数请求类型 [issue #IM7TV @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM7TV)
>
> fixed 在springcloud下 整合到zuul时 测试路径不正确[issue #IM69X @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM69X)
>
> 属性介绍说明，表格栏统一使用中文
>
> fixed 发布到tomcat非root目下时路径被多层嵌套curl路径正确 ui内部测试路径多层[issue #IM69H @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM69H)
>
> fixed List<String>和String[]类型解析不正确，应该为array，实际为String并且不能增加[issue #IM2ZI @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM2ZI)
>
> fixed 类型及引用类在出现array类型时不一致的问题[issue #7 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/17)
>
> fixed DELETE请求无法正确处理请求头[issue #16 @GitHub](https://github.com/xiaoymin/Swagger-Bootstrap-UI/issues/16)
>
> fixed 在线调试-参数名称更改不生效 [issue #IMBN3 @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMBN3)
>
> fixed 升级到1.8.1后,火狐浏览器无法显示文档[issue #IM37D @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM37D)
>
> fixed 关于请求是form表单，但是业务参数是body(json体的)请求异常[issue #IM2YE @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM2YE)
>
> fixed 入参中的对象被处理成string[issue #ILU3S @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/ILU3S)
>
> fixed UI 样式建议(采纳大部分建议，非常感谢@永夜 提出的建议)[issue #IMCET @GitEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IMCET)
>
> fixed 当请求，出现param参数时，与body参数时，传到服务器无效params没有传,同issue #IM2YE [issue #IM72N @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM72N)
>
> 优化，返回raw文本标签页添加复制文本功能,方便开发者调用，复制按钮增加icon
>
> fixed 文件上传的bug[issue #IM4RG @GITEE](https://gitee.com/xiaoym/swagger-bootstrap-ui/issues/IM4RG)
----
- **[2018/08/14] - [swagger-bootstrap-ui 1.8.1 发布，Swagger 前端 UI 实现](https://www.oschina.net/news/98955/swagger-bootstrap-ui-1-8-1-released)**
> fixed 针对basePath属性,调试接口重复添加basePath路径,接口报404错误(重大bug,建议升级)
>
> fixed 针对@ApiModelProperty注解,针对example属性值,array类型值带单引号,文档无法显示bug
>
> fixed 针对404 异常,header-curl tab选项卡切换bug
>
> fixed curl -X 参数bug,显示缺少"/"根路径
>
> fixed 左侧接口列表滚条无法完全滚动到底部
>
> fixed 窗口大小改变后，界面混乱
>
> 优化菜单做成接口方法类型和接口类型左对齐
>
> fixed 左侧接口列表滚条无法完全滚动到底部
>
> 优化 针对枚举类型,参数说明显示可用值列表
>
> 表单类型显示header、可提交header信息
>
> fixed 基础类型响应数据为空的情况
----
- **[2018/08/10] - [swagger-bootstrap-ui 1.8.0 发布，Swagger 前端 UI 实现](https://www.oschina.net/news/98840/swagger-bootstrap-ui-1-8-0-released)**
> fixed 请求参数出现重复问题,去重
>
> fixed 无法显示spring cloud 子项目路径,针对basePath不为空,或者不为"/"根路径的情况,相关api地址加上basePath前缀
>
> 调整菜单url各方法配色、接口配色,文档介绍、调试返回响应数据json配色
>
> 响应模块添加http响应码、接口耗时、大小,参数栏添加全选按钮,调试页面针对响应内容tab选项卡去除灰色背景色,为默认白色底色
>
> 调试响应模块增加raw、curl两个子tab选项卡,实现curl功能,方便远程调试
>
> 针对接口二进制返回,提供下载按钮,可点击弹出下载功能
>
> fixed 针对图片返回时报DApiUI is not defined错误
>
> 文档doc.html页面title根据用户自定义title显示
>
> 发送中增加loading效果
>
> 调整菜单顶部分组接口位置,移动到最左侧,添加可隐藏/显示MENU元素
>
> fixed 针对schema类型的参数,显示类型为string类型,按schema类型展示
>
> 文件上传支持文件多选
----
- **[2018/08/06] - [swagger-bootstrap-ui 1.7.9 发布，Swagger 前端 UI 实现](https://www.oschina.net/news/98711/swagger-bootstrap-ui-1-7-9-released)**
> fixed 针对Integer、double、float等类型参数,有format参数则显示format属性,以区分准确类型,如：int64|int32等
>
> fixed 滚动条出现底部部分内容不显示bug
>
> 优化菜单接口根据不同接口类型,颜色调整
>
> 优化文档响应数据jsonview字体,优化间距,更显紧促,优化菜单,接口及接口类型加粗
>
> add 顶部加搜索功能、可根据api地址、api介绍、api类型、分组名称实现模糊搜索,默认搜索当前已加载的分组api,如果其他分组未加载则搜索不到.
>
> add 针对Security-JWT等权限验证,显示Authorize菜单授权
>
> add 左侧菜单栏可自由拖动长度大小
----
- **[2018/08/03] - [swagger-bootstrap-ui 1.7.8 发布，Swagger 前端 UI 实现](https://www.oschina.net/news/98631/swagger-bootstrap-ui-1-7-8-released)**
> fixed 针对@RequestBody注解实体类属性required的值一直显示默认false问题
>
> fixed 针对文件上传,使用allowMultiple = true,上传按钮不显示bug,推荐使用@ApiImplicitParam注解,并且指定dataType = "MultipartFile"
>
> 分组接口移动至顶部,菜单列表添加icon图标,移除简介页的软件介绍信息,丰富简介页信息,新增各类型接口统计信息,菜单简介名称更名为主页
>
> 增加调试参数记忆功能,下次点击该接口时,上次输入的参数会保存继续可使用
>
> 优化 针对@RequestBody注解,参数使用默认description的问题,将使用@ApiModel注解实体类上的description属性
----
- **[2018/07/25] - [swagger-bootstrap-ui 1.7.7 发布，Swagger 前端 UI 实现](https://www.oschina.net/news/98335/swagger-bootstrap-ui-177-released)**
> fixed 对象ref应用本身，JS 出现死循环了么，栈内存溢出BUG
>
> 优化递归查找ref方法,fixed ref自身引用,相互引用的情况下,文档出不来bug
>
> 响应json属性太多，文档太长,不利于查看,使用jsonview插件格式化,可收缩,便于查看
>
> fixed 对象属性值存在required属性时,值显示不对bug
>
> 兼容firefox,文档菜单换行显示异常问题
>
> 新增枚举请求参数类型支持,调试页面枚举类型为下拉框
>
> fixed 请求swagger-resources接口响应为string类型，文档无法展示,格式化json展示文档
>
> fixed 全局参数重新赋值无效
>
> fixed 针对@ApiOperation注解自定义tags接口无法显示bug
----
- **[2018/07/18] - [swagger-bootstrap-ui 1.7.6 发布，Swagger 前端 UI 实现](https://www.oschina.net/news/98128/swagger-bootstrap-ui-176-released)**
> fixed 全局默认参数，设置值无效问题
>
> add 简介页添加basePath属性
>
> fixed 响应类型是Ref引用属性，在响应json中未列出属性
>
> fixed 默认值未显示,swagger 2.9.2版本响应json的默认值为x-example属性
>
> fixed tags存在大写的情况不显示接口 bug,在swagger2.9.2版本测试时，swagger又将后台的tags改为区分大小写了，所以建议升级swagger版本到最新
>
> fixed 相同url地址，不同method类型，接口未展示bug
>
> fixed 请求参数为ref引用类型时，文档列出请求类型和schema类型一致，显示schema类型
>
> tip:推荐使用chrome浏览器，别的浏览器可能有js、css兼容问题，文档效果未到最佳
>
> tip：建议swagger版本升级到2.9.2
----
- **[2018/07/16] - [swagger-bootstrap-ui 1.7.5，Swagger 前端 UI 实现](https://www.oschina.net/news/98042/swagger-bootstrap-ui-1-7-5-released)**
> 重构DApiUI.js功能,新版本使用SwaggerBootstrapUi.js，方便后期扩展,同时删除无效js、css、html文件,新版本jar包由原760kb缩小至295kb
>
> 重构文档页面，剔除原来table展现方式，新版本使用markdown格式展现文档,单个文档页可复制
>
> 新增全局参数配置功能，针对请求参数有全局参数情况下，方便在线调试
>
> 支持离线文档格式,生成markdown格式文档,供开发者对外生成静态文档
>
> 添加clipboard插件,离线文档可复制功能
>
> 正式发布版去除console打印调试信息
>
> fixed 调试页面去除url根路径/,项目名称非ROOT，或分布式情况下路径不对，多一个"/"的问题  
>
> fixed RequestBody 接收实体对象，对象属性中有List属性时,参数显示array，需解析对象属性显示，方便查看
>
> fixed 对象属性展示为string，属性未显示
>
> tip:推荐使用chrome浏览器，别的浏览器可能有js、css兼容问题，文档效果未到最佳
----
- **[2017/12/18] - [swagger-bootstrap-ui 1.7 发布，前端 UI 实现](https://www.oschina.net/news/91637/swagger-bootstrap-ui-1-7)**
> 分组功能实现
>
> 文件上传,form表单action地址取相对路径,截取掉首字符"/"

----
- **[2017/09/06] - [swagger-bootstrap-ui 1.6 发布，前端 UI 实现](https://www.oschina.net/news/88444/swagger-bootstrap-ui-1-6)**
> 支持文件上传，需要指定dataType=MultipartFile
>
> ResponseBody<String>类型的string展示
>
> 布局溢出的问题.bycdao-main样式调整，修改margin-left:270px;
>
> ApiImplicitParam注解defaultValue属性支持
>
> 菜单名称调整，展示接口方法类型、接口地址、接口说明三个参数

----

- **[2017/09/01] - [swagger-bootstrap-ui 1.5 发布，前端 UI 实现](https://www.oschina.net/news/88285/swagger-bootstrap-ui-1-5)**
> 修改groupid为com.github.xiaoymin
>
> 新增layer弹框,增加接口表单验证功能,针对required=true的字段
>
> 修复实体类中包含自引用递归算法错误的bug
>
> 类型dataType有ref类型显示为string的bug
>
> 响应实体字段详细说明table添加
>
> 针对RequestBody字段,如果是ref属性，详细列出每个字段的说明

----

- **[2017/04/19] - [swagger-bootstrap-ui 开源](https://www.oschina.net/news/88285/swagger-bootstrap-ui-1-5)**
> swagger-bootstrap-ui 开源
>
> 主要包含文档说明、在线调试两大核心功能
>

----