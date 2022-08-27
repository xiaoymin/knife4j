/***
 * knife4j常量
 */
const constants = {
  // 全局离线参数
  globalParameter: 'Knife4jOfficeParameter',
  // 全局离线参数表头
  globalParameterTableColumns: [{
    title: '参数名称',
    dataIndex: 'name',
    width: '15%',
    scopedSlots: {
      customRender: 'name'
    }
  },
  {
    title: '参数值',
    className: 'column-money',
    dataIndex: 'value',
    width: '65%',
    scopedSlots: {
      customRender: 'paramContentLabel'
    }
  },
  {
    title: '参数类型',
    dataIndex: 'in',
    width: '10%',
    scopedSlots: {
      customRender: 'paramTypeLable'
    }
  },
  {
    title: '操作',
    dataIndex: 'operation',
    scopedSlots: {
      customRender: 'operation'
    }
  }
  ],
  globalSecurityParameters: 'Knife4jGlobalSecurityParameters',
  globalSecurityParameterObject: 'Knife4jGlobalSecurityParameterObject',
  globalSecurityParamPrefix: 'Knife4jSecurityParams',
  globalI18nCache: 'Knife4jI18n',
  // 缓存api接口,用于判断后端接口是否存在变化
  globalGitApiVersionCaches: 'Knife4jGitApiVersionCaches',
  // 个性化设置名称
  globalSettingsKey: 'Knife4jGlobalSettings',
  globalTreeTableModelParams: 'Knife4jGlobalTreeTableModelParams',
  // Ui界面个性化配置默认初始值
  defaultWebSettings: {
    enableDebug: true,// 是否开启Debug调试栏
    enableFooter: true,// 是否默认显示底部Footer
    enableFooterCustom: false,// 是否自定义Footer
    footerCustomContent: '',// 自定义footer内容
    enableSearch: true,// 是否显示搜索框
    enableOpenApi: true,// 是否显示OpenApi原始规范结构
    enableHomeCustom: false,//  是否开启主页自定义配置，默认false
    homeCustomLocation: '',// 自定义主页的Markdown文档内容
    enableGroup: true,// 是否显示分组下拉框，默认true(即显示)，一般情况下，如果是单个分组的情况下，可以设置该属性为false，即不显示分组，那么也就不用选择了
    enableResponseCode: true, // since 4.0.0 是否显示响应状态码栏
    enableSwaggerModels: true,// 是否显示界面中SwaggerModel功能
    swaggerModelName: 'Swagger Models',// 重命名界面Swagger Model的显示名称
    enableReloadCacheParameter: false,//  是否在每个Debug调试栏后显示刷新变量按钮,默认不显示
    enableAfterScript: true,// 调试Tab是否显示AfterScript功能,默认开启
    enableDocumentManage: true,// 是否显示界面中'文档管理'功能
    enableVersion: false,// 是否开启界面中对某接口的版本控制,如果开启，后端变化后Ui界面会存在小蓝点
    showApiUrl: false, // 接口api地址不显示
    showTagStatus: false, // 分组tag显示description属性,针对@Api注解没有tags属性值的情况
    enableSwaggerBootstrapUi: false, // 是否开启swaggerBootstrapUi增强
    treeExplain: true,

    language: 'zh-CN' // 默认语言版本
  },
  defaultSettings: {
    enableDebug: true,// 是否开启Debug调试栏
    enableFooter: true,// 是否默认显示底部Footer
    enableFooterCustom: false,// 是否自定义Footer
    footerCustomContent: '',// 自定义footer内容
    enableSearch: true,// 是否显示搜索框
    enableOpenApi: true,// 是否显示OpenApi原始规范结构
    enableHomeCustom: false,//  是否开启主页自定义配置，默认false
    homeCustomLocation: '',// 自定义主页的Markdown文档内容
    enableGroup: true,// 是否显示分组下拉框，默认true(即显示)，一般情况下，如果是单个分组的情况下，可以设置该属性为false，即不显示分组，那么也就不用选择了

    enableResponseCode: true, // since 4.0.0 是否显示响应状态码栏

    enableSwaggerModels: true,// 是否显示界面中SwaggerModel功能
    swaggerModelName: 'Swagger Models',// 重命名界面Swagger Model的显示名称
    enableReloadCacheParameter: false,//  是否在每个Debug调试栏后显示刷新变量按钮,默认不显示
    enableAfterScript: true,// 调试Tab是否显示AfterScript功能,默认开启
    enableDocumentManage: true,// 是否显示界面中'文档管理'功能
    enableVersion: false,// 是否开启界面中对某接口的版本控制,如果开启，后端变化后Ui界面会存在小蓝点
    showApiUrl: false, // 接口api地址不显示
    showTagStatus: false, // 分组tag显示description属性,针对@Api注解没有tags属性值的情况
    enableSwaggerBootstrapUi: false, // 是否开启swaggerBootstrapUi增强
    treeExplain: true,
    enableDynamicParameter: false, // 开启动态参数
    enableFilterMultipartApis: false, // 针对RequestMapping的接口请求类型,在不指定参数类型的情况下,如果不过滤,默认会显示7个类型的接口地址参数,如果开启此配置,默认展示一个Post类型的接口地址
    enableFilterMultipartApiMethodType: 'POST', // 默认保存类型
    enableRequestCache: true, // 是否开启请求参数缓存
    enableCacheOpenApiTable: false, // 是否开启缓存已打开的api文档
    enableHost: false,// 是否启用Host
    enableHostText: '',// 启用Host后文本
    language: 'zh-CN' // 默认语言版本
  },
  // 增强配置
  defaultPlusSettings: {
    enableDebug: true,// 是否开启Debug调试栏
    enableFooter: true,// 是否默认显示底部Footer
    enableFooterCustom: false,// 是否自定义Footer
    footerCustomContent: '',// 自定义footer内容
    enableSearch: true,// 是否显示搜索框
    enableOpenApi: true,// 是否显示OpenApi原始规范结构
    enableHomeCustom: false,//  是否开启主页自定义配置，默认false
    homeCustomLocation: '',// 自定义主页的Markdown文档内容
    enableGroup: true,// 是否显示分组下拉框，默认true(即显示)，一般情况下，如果是单个分组的情况下，可以设置该属性为false，即不显示分组，那么也就不用选择了

    enableSwaggerModels: true,// 是否显示界面中SwaggerModel功能
    swaggerModelName: 'Swagger Models',// 重命名界面Swagger Model的显示名称
    enableReloadCacheParameter: false,//  是否在每个Debug调试栏后显示刷新变量按钮,默认不显示
    enableAfterScript: true,// 调试Tab是否显示AfterScript功能,默认开启
    enableDocumentManage: true,// 是否显示界面中'文档管理'功能
    enableVersion: false,// 是否开启界面中对某接口的版本控制,如果开启，后端变化后Ui界面会存在小蓝点
    showApiUrl: false, // 接口api地址不显示
    showTagStatus: false, // 分组tag显示description属性,针对@Api注解没有tags属性值的情况
    enableSwaggerBootstrapUi: true, // 是否开启swaggerBootstrapUi增强
    treeExplain: true,
    enableDynamicParameter: false, // 开启动态参数
    enableFilterMultipartApis: false, // 针对RequestMapping的接口请求类型,在不指定参数类型的情况下,如果不过滤,默认会显示7个类型的接口地址参数,如果开启此配置,默认展示一个Post类型的接口地址
    enableFilterMultipartApiMethodType: 'POST', // 默认保存类型
    enableRequestCache: true, // 是否开启请求参数缓存
    enableCacheOpenApiTable: false, // 是否开启缓存已打开的api文档
    enableHost: false,// 是否启用Host
    enableHostText: '',// 启用Host后文本
    language: 'zh-CN' // 默认语言版本
  },
  debugRequestHeaders: ['Accept',
    'Accept-Charset',
    'Accept-Encoding',
    'Accept-Language',
    'Accept-Ranges',
    'Authorization',
    'Cache-Control',
    'Connection',
    'Cookie',
    'Content-Length',
    'Content-Type',
    'Content-MD5',
    'Date',
    'Expect',
    'From',
    'Host',
    'If-Match',
    'If-Modified-Since',
    'If-None-Match',
    'If-Range',
    'If-Unmodified-Since',
    'Max-Forwards',
    'Origin',
    'Pragma',
    'Proxy-Authorization',
    'Range',
    'Referer',
    'TE',
    'Upgrade',
    'User-Agent',
    'Via',
    'Warning'
  ],
  debugRequestHeaderOptions: [{
    value: 'Accept',
    label: 'Accept'
  },
  {
    value: 'Accept-Charset',
    label: 'Accept-Charset'
  },
  {
    value: 'Accept-Encoding',
    label: 'Accept-Encoding'
  },
  {
    value: 'Accept-Language',
    label: 'Accept-Language'
  },
  {
    value: 'Accept-Ranges',
    label: 'Accept-Ranges'
  },
  {
    value: 'Authorization',
    label: 'Authorization'
  },
  {
    value: 'Cache-Control',
    label: 'Cache-Control'
  },
  {
    value: 'Connection',
    label: 'Connection'
  },
  {
    value: 'Cookie',
    label: 'Cookie'
  },
  {
    value: 'Content-Length',
    label: 'Content-Length'
  },
  {
    value: 'Content-Type',
    label: 'Content-Type'
  },
  {
    value: 'Content-MD5',
    label: 'Content-MD5'
  },
  {
    value: 'Date',
    label: 'Date'
  },
  {
    value: 'Expect',
    label: 'Expect'
  },
  {
    value: 'From',
    label: 'From'
  },
  {
    value: 'Host',
    label: 'Host'
  },
  {
    value: 'If-Match',
    label: 'If-Match'
  },
  {
    value: 'If-Modified-Since',
    label: 'If-Modified-Since'
  },
  {
    value: 'If-None-Match',
    label: 'If-None-Match'
  },
  {
    value: 'If-Range',
    label: 'If-Range'
  },
  {
    value: 'If-Unmodified-Since',
    label: 'If-Unmodified-Since'
  },
  {
    value: 'Max-Forwards',
    label: 'Max-Forwards'
  },
  {
    value: 'Origin',
    label: 'Origin'
  },
  {
    value: 'Pragma',
    label: 'Pragma'
  },
  {
    value: 'Proxy-Authorization',
    label: 'Proxy-Authorization'
  },
  {
    value: 'Range',
    label: 'Range'
  },
  {
    value: 'Referer',
    label: 'Referer'
  },
  {
    value: 'TE',
    label: 'TE'
  },
  {
    value: 'Upgrade',
    label: 'Upgrade'
  },
  {
    value: 'User-Agent',
    label: 'User-Agent'
  },
  {
    value: 'Via',
    label: 'Via'
  },
  {
    value: 'Warning',
    label: 'Warning'
  }
  ],
  debugRequestHeaderColumn: [{
    title: '请求头',
    dataIndex: 'name',
    width: '20%',
    scopedSlots: {
      customRender: 'headerName'
    }
  },
  {
    title: '内容',
    dataIndex: 'content',
    scopedSlots: {
      customRender: 'headerValue'
    }
  },
  {
    title: '操作',
    dataIndex: 'operation',
    width: '10%',
    scopedSlots: {
      customRender: 'operation'
    }
  }
  ],
  debugFormRequestHeader: [{
    title: '参数名称',
    dataIndex: 'name',
    width: '20%',
    scopedSlots: {
      customRender: 'formName'
    }
  },
  {
    title: '类型',
    dataIndex: 'type',
    width: '12%',
    scopedSlots: {
      customRender: 'formType'
    }
  },
  {
    title: '参数值',
    dataIndex: 'content',
    scopedSlots: {
      customRender: 'formValue'
    }
  },
  {
    title: '操作',
    dataIndex: 'operation',
    width: '10%',
    scopedSlots: {
      customRender: 'operation'
    }
  }
  ],
  debugUrlFormRequestHeader: [{
    title: '参数名称',
    dataIndex: 'name',
    width: '20%',
    scopedSlots: {
      customRender: 'urlFormName'
    }
  },
  {
    title: '参数值',
    dataIndex: 'content',
    scopedSlots: {
      customRender: 'urlFormValue'
    }
  },
  {
    title: '操作',
    dataIndex: 'operation',
    width: '10%',
    scopedSlots: {
      customRender: 'operation'
    }
  }
  ],
  debugCacheApiId: 'Knife4jCacheApi'

}

export default constants
