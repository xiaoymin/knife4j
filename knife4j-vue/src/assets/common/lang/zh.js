const langOptions = {
  //界面显示
  lang: 'zh',
  title: '标题11111',
  searchHolderText: '输入文档关键字搜索',
  docLinkTip: '帮助文档',
  langText: '中',
  settingText: '个性化配置',
  cacheText: '清除缓存',
  //文档中各个表格的属性
  table: {
    //SwaggerModels
    swaggerModelsColumns: [
      {
        title: '名称',
        dataIndex: 'name',
        width: '30%'
      },
      {
        title: '类型',
        dataIndex: 'type',
        width: '15%'
      },
      {
        title: '说明',
        width: '35%',
        dataIndex: 'description',
        scopedSlots: { customRender: 'descriptionValueTemplate' }
      },
      {
        title: 'schema',
        dataIndex: 'schemaValue',
        width: '15%'
      }
    ],
    //文档说明-请求参数
    documentRequestColumns: [
      {
        title: '参数名称',
        dataIndex: 'name',
        width: '30%'
      },
      {
        title: '参数说明',
        dataIndex: 'description',
        width: '25%',
        scopedSlots: { customRender: 'descriptionValueTemplate' }
      },
      {
        title: '请求类型',
        dataIndex: 'in',
        scopedSlots: { customRender: 'typeTemplate' }
      },
      {
        title: '是否必须',
        dataIndex: 'require',
        scopedSlots: { customRender: 'requireTemplate' }
      },
      {
        title: '数据类型',
        dataIndex: 'type',
        scopedSlots: { customRender: 'datatypeTemplate' }
      },
      {
        title: 'schema',
        dataIndex: 'schemaValue',
        width: '15%'
      }
    ],
    //文档说明-响应状态
    documentResponseStatusColumns: [
      {
        title: '状态码',
        dataIndex: 'code',
        width: '20%'
      },
      {
        title: '说明',
        dataIndex: 'description',
        width: '55%',
        scopedSlots: { customRender: 'descriptionTemplate' }
      },
      {
        title: 'schema',
        dataIndex: 'schema',
        scopedSlots: { customRender: 'schemaTemplate' }
      }
    ],
    //文档说明-响应Header
    documentResponseHeaderColumns: [
      {
        title: '参数名称',
        dataIndex: 'name',
        width: '30%'
      },
      {
        title: '参数说明',
        dataIndex: 'description',
        width: '55%'
      },
      {
        title: '数据类型',
        dataIndex: 'type'
      }
    ],
    //文档说明-响应参数
    documentResponseColumns: [
      {
        title: '参数名称',
        dataIndex: 'name',
        width: '35%'
      },
      {
        title: '参数说明',
        dataIndex: 'description',
        scopedSlots: { customRender: 'descriptionTemplate' },
        width: '40%'
      },
      {
        title: '类型',
        dataIndex: 'type'
      },
      {
        title: 'schema',
        dataIndex: 'schemaValue',
        width: '15%'
      }
    ],
    //调试-请求头参数
    debugRequestHeaderColumns: [
      {
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
    //调试-FormData类型请求头
    debugFormDataRequestColumns: [
      {
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
    //调试-url-form类型请求参数头
    debugUrlFormRequestColumns: [
      {
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
    //调试-响应Header
    debugResponseHeaderColumns: [
      {
        title: '响应头',
        dataIndex: 'name',
        width: '20%'
      },
      {
        title: '值',
        dataIndex: 'value'
      }
    ],
    //auth中的header
    authHeaderColumns: [
      {
        title: '参数key',
        dataIndex: 'key',
        customRender(text, row, index) {
          return row.key + '(' + row.type + ')';
        }
      },
      {
        title: '参数名称',
        className: 'column-money',
        dataIndex: 'name'
      },
      {
        title: 'in',
        dataIndex: 'in'
      },
      {
        title: '参数值',
        dataIndex: 'value',
        scopedSlots: {
          customRender: 'paramIpt'
        }
      }
    ]
  },
  //主页显示
  homePage: {
    description: '简介',
    author: '作者',
    version: '版本',
    host: 'host',
    basePath: 'basePath',
    serviceUrl: 'API服务条款',
    groupName: '分组名称',
    groupUrl: '分组Url',
    groupLocation: '分组location',
    apiCountNumber: '接口统计信息'
  },
  markdown: {
    title: '其他文档'
  },
  message: {
    success: '保存成功',
    settingTip: '启用个性化配置后,接口Tab标签需关闭后重新打开或者刷新当前页面',
    settingHost: '启用Host成功,请关闭接口Tab或者刷新当前页面再进行调试',
    unsupportstore: '当前浏览器不支持localStorage对象,无法使用该功能',
    copy: {
      url: {
        success: '复制地址成功',
        fail: '复制地址失败,您当前浏览器版本不兼容,请手动复制.'
      },
      method: {
        success: '复制接口成功',
        fail: '复制接口失败,您当前浏览器版本不兼容,请手动复制.'
      },
      document: {
        success: '复制文档成功',
        fail: '复制文档失败,您当前浏览器版本不兼容,请手动复制.'
      },
      raw: {
        success: '复制raw成功',
        fail: '复制raw失败,您当前浏览器版本不兼容,请手动复制.'
      },
      curl: {
        success: '复制curl成功',
        fail: '复制curl失败,您当前浏览器版本不兼容,请手动复制.'
      },
      open: {
        success: '复制OpenAPI成功',
        fail: '复制OpenAPI失败,您当前浏览器版本不兼容,请手动复制.'
      }
    },
    layer: {
      title: '信息',
      yes: '确定',
      no: '取消'
    },
    auth: {
      invalid: '值无效',
      confirm: '确定注销吗?',
      success: '注销成功'
    },
    global: {
      iptname: '请输入全局参数名称',
      iptvalue: '请输入全局参数值',
      deleteSuccess: '删除成功'
    },
    settings: {
      plusFail: '无法开启Knife4j增强功能,请确保后端启用注解@EnableKnife4j',
      plusError: '无法开启Knife4j增强功能,错误原因:',
      success: '保存成功,请刷新该文档页'
    },
    offline: {
      imple: '该功能尚未实现...',
      markdown: '正在下载Markdown文件中,请稍后...',
      html: '正在下载Html中,请稍后...',
      word: '正在下载Word中,请稍后...',
      copy: '拷贝文档',
      toomany: '当前接口数量超出限制,请使用第三方markdown转换软件进行转换以查看效果.',
      note: 'swagger-bootstrap-ui 提供markdwon格式类型的离线文档,开发者可拷贝该内容通过其他markdown转换工具进行转换为html或pdf.'
    },
    debug: {
      urlNotEmpty: '请求url地址不能为空',
      fieldNotEmpty: '不能为空',
      networkErr: '服务器正在重启或者已经挂了:(~~~~',
      contentToBig: '接口响应数据量超过限制,不在响应内容中显示,请在raw中进行查看',
      contentToBigBlob: '接口响应数据量超过限制,不在响应内容中显示'
    },
    sys: {
      loadErr: '请确保swagger资源接口正确.'
    }
  },
  swaggerModel: {
    nodata: '暂无Swagger Models',
    tableHeader: {
      name: '名称',
      des: '说明',
      type: '类型'
    }
  },
  global: {
    tab: '全局参数设置',
    add: '添加参数',
    model: '新增参数',
    tableHeader: [{
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
    form: {
      name: '参数名称',
      value: '参数值',
      type: '参数类型',
      validate: {
        name: '请输入参数名称',
        value: '请输入参数值'
      }
    },
    ok: '确定',
    cancel: '取消',
    save: '保存',
    delete: '删除',
    note: 'Knife4j 提供全局参数Debug功能,目前默认提供header(请求头)、query(form)两种方式的入参.<br /><br />在此添加全局参数后,默认Debug调试tab页会带上该参数'

  },
  settings: {
    title: '个性化设置',
    openCache: '开启请求参数缓存',
    dynamicParameter: '开启动态请求参数',
    showApi: '菜单Api地址显示',
    tagDes: '分组tag显示description说明属性',
    apiFilter: '开启RequestMapping接口过滤,默认只显示',
    openCacheApi: '开启缓存已打开的api文档',
    plus: '启用Knife4j提供的增强功能',
    save: '保存内容',
    copy: '复制',
    fastTitle: '<h5>通过 <kbd>ctrl + c</kbd> 复制以下地址,打开浏览器快速个性化设置</h5>'
  },
  auth: {
    cancel: '注销',
    save: '保存',
    tableHeader: {
      key: '参数key',
      name: '参数名称',
      in: 'in',
      value: '参数值',
      operator: '操作'
    },
    valueInvalid: '值无效'
  },
  menu: {
    home: '主页',
    manager: '文档管理',
    globalsettings: '全局参数设置',
    officeline: '离线文档',
    selfSettings: '个性化设置',
    other: '其他文档',
    menuItemList: [
      { key: '1', icon: 'caret-left', text: '关闭左侧' },
      { key: '2', icon: 'caret-right', text: '关闭右侧' },
      { key: '3', icon: 'close-circle', text: '关闭其它' }
    ]
  },
  offline: {
    des: 'Knife4j提供导出4种格式的离线文档(Html/Markdown/Word/OpenAPI)',
    download: {
      markdown: '下载Markdown',
      html: '下载Html',
      word: '下载Word',
      pdf: '下载Pdf'
    },
    contact: '联系人',
    url: '接口路径',
    note: '简介',
    schemaDes: 'schema属性说明'
  },
  doc: {
    title: '文档',
    note: '接口说明',
    copy: '复制文档',
    copyHash: '复制地址',
    copyMethod: '复制接口',
    produces: '请求数据类型',
    consumes: '响应数据类型',
    author: '开发者',
    url: '接口地址',
    method: '请求方式',
    des: '接口描述',
    params: '请求参数',
    example: '示例值',
    enumAvalible: '可用值',
    requestExample: '请求示例',
    paramsHeader: {
      name: '参数名称',
      des: '参数说明',
      require: '是否必须',
      type: '数据类型',
      requestType: '请求类型'

    },
    responseHeaderParams: '响应Header',
    response: '响应状态',
    responseHeader: {
      code: '状态码',
      des: '说明'

    },
    responseParams: '响应参数',
    responseParamsHeader: {
      name: '参数名称',
      des: '参数说明',
      type: '类型'
    },
    responseExample: '响应示例',
    nodata: '暂无'

  },
  debug: {
    title: '调试',
    send: ' 发 送 ',
    headers: '请求头部',
    params: '请求参数',
    form: {
      upload: '选择文件',
      itemText: '文本',
      itemFile: '文件'
    },
    tableHeader: {
      holderName: '请求头名称',
      holderValue: '请求头内容',
      holderDel: '删除',
      selectAll: '全选',
      type: '参数类型',
      name: '参数名称',
      value: '参数值'
    },
    response: {
      content: '响应内容',
      showDes: '显示说明',
      code: '响应码:',
      cost: '耗时:',
      size: '大小:',
      header: '请求头',
      download: '下载文件',
      copy: '复制'
    }

  },
  open: {
    copy: ' 复 制 ',
    download: ' 下 载 '
  },
  tab: {
    closeCurrent: '关闭当前标签页',
    closeOther: '关闭其它标签页',
    closeAll: '关闭全部标签页'
  },
  validate: {
    header: '请求头 ',
    notEmpty: ' 不能为空',
    fileNotEmpty: ' 文件不能为空'
  },
  script: {
    JSExample: 'JS模板示例',
    TSExample: 'TS模板示例',
  }
}

export default langOptions;
