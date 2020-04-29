const langOptions = {
  //界面显示
  title: '标题11111',
  searchHolderText: '输入文档关键字搜索',
  docLinkTip: '帮助文档',
  langText: '中',
  settingText: '个性化配置',
  cacheText: '清除缓存',
  //主页显示
  homePage: {
    description: '简介',
    author: '作者',
    version: '版本',
    host: 'host',
    basePath: 'basePath',
    serviceUrl: '服务Url',
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
    unsupportstore: '当前浏览器不支持localStorage对象,无法使用该功能',
    copy: {
      success: '复制成功',
      fail: '复制失败,您当前浏览器版本不兼容,请手动复制.'
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
      copy: '拷贝文档',
      toomany: '当前接口数量超出限制,请使用第三方markdown转换软件进行转换以查看效果.',
      note: 'swagger-bootstrap-ui 提供markdwon格式类型的离线文档,开发者可拷贝该内容通过其他markdown转换工具进行转换为html或pdf.'
    },
    debug: {
      urlNotEmpty: '请求url地址不能为空',
      fieldNotEmpty: '不能为空',
      networkErr: '服务器正在重启或者已经挂了:(~~~~'
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
    tableHeader: {
      name: '参数名称',
      value: '参数值',
      type: '参数类型',
      operator: '操作'
    },
    save: '保存',
    delete: '删除',
    note: 'swagger-bootstrap-ui 提供全局参数Debug功能,目前默认提供header(请求头)、query(form)两种方式的入参.<br /><br />在此添加全局参数后,默认Debug调试tab页会带上该参数,该全局参数只在该分组下有效,不同的分组需要分别设置'

  },
  settings: {
    title: '个性化设置',
    openCache: '开启请求参数缓存',
    dynamicParameter: '开启动态请求参数',
    showApi: '菜单Api地址显示',
    tagDes: '分组tag显示dsecription说明属性',
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
    officeline: '离线文档(MD)',
    selfSettings: '个性化设置'
  },
  offline: {
    des: '简介',
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
    produces: '请求数据类型',
    consumes: '响应数据类型',
    author: '开发者',
    url: '接口地址',
    method: '请求方式',
    des: '接口描述',
    params: '请求参数',
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
    params: '请求参数列表',
    tableHeader: {
      selectAll: '全选',
      type: '参数类型',
      name: '参数名称',
      value: '参数值'
    },
    response: {
      content: '响应内容',
      showDes: '显示说明',
      code: '响应码',
      cost: '耗时',
      size: '大小',
      header: '请求头',
      download: '下载文件'
    }

  },
  tab: {
    closeCurrent: '关闭当前标签页',
    closeOther: '关闭其它标签页',
    closeAll: '关闭全部标签页'
  }

}

export default langOptions;
