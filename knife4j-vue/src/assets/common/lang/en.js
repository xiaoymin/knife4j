const langOptions = {
  lang: 'us',
  title: 'title',
  searchHolderText: 'Enter keyword search',
  docLinkTip: 'Documentation',
  langText: 'EN',
  settingText: 'Settings',
  cacheText: 'Clear Caches',
  //文档中各个表格的属性
  table: {
    //SwaggerModels
    swaggerModelsColumns: [
      {
        title: 'name',
        dataIndex: 'name',
        width: '30%'
      },
      {
        title: 'type',
        dataIndex: 'type',
        width: '15%'
      },
      {
        title: 'description',
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
        title: 'name',
        dataIndex: 'name',
        width: '30%'
      },
      {
        title: 'description',
        dataIndex: 'description',
        width: '25%',
        scopedSlots: { customRender: 'descriptionValueTemplate' }
      },
      {
        title: 'in',
        dataIndex: 'in',
        scopedSlots: { customRender: 'typeTemplate' }
      },
      {
        title: 'require',
        dataIndex: 'require',
        scopedSlots: { customRender: 'requireTemplate' }
      },
      {
        title: 'type',
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
        title: 'code',
        dataIndex: 'code',
        width: '20%'
      },
      {
        title: 'description',
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
        title: 'name',
        dataIndex: 'name',
        width: '30%'
      },
      {
        title: 'description',
        dataIndex: 'description',
        width: '55%'
      },
      {
        title: 'type',
        dataIndex: 'type'
      }
    ],
    //文档说明-响应参数
    documentResponseColumns: [
      {
        title: 'name',
        dataIndex: 'name',
        width: '35%'
      },
      {
        title: 'description',
        dataIndex: 'description',
        scopedSlots: { customRender: 'descriptionTemplate' },
        width: '40%'
      },
      {
        title: 'type',
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
        title: 'name',
        dataIndex: 'name',
        width: '20%',
        scopedSlots: {
          customRender: 'headerName'
        }
      },
      {
        title: 'value',
        dataIndex: 'content',
        scopedSlots: {
          customRender: 'headerValue'
        }
      },
      {
        title: 'operation',
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
        title: 'name',
        dataIndex: 'name',
        width: '20%',
        scopedSlots: {
          customRender: 'formName'
        }
      },
      {
        title: 'type',
        dataIndex: 'type',
        width: '12%',
        scopedSlots: {
          customRender: 'formType'
        }
      },
      {
        title: 'value',
        dataIndex: 'content',
        scopedSlots: {
          customRender: 'formValue'
        }
      },
      {
        title: 'operation',
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
        title: 'name',
        dataIndex: 'name',
        width: '20%',
        scopedSlots: {
          customRender: 'urlFormName'
        }
      },
      {
        title: 'value',
        dataIndex: 'content',
        scopedSlots: {
          customRender: 'urlFormValue'
        }
      },
      {
        title: 'operation',
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
        title: 'name',
        dataIndex: 'name',
        width: '20%'
      },
      {
        title: 'value',
        dataIndex: 'value'
      }
    ],
    //auth中的header
    authHeaderColumns: [
      {
        title: 'key',
        dataIndex: 'key',
        customRender(text, row, index) {
          return row.key + '(' + row.type + ')';
        }
      },
      {
        title: 'name',
        className: 'column-money',
        dataIndex: 'name'
      },
      {
        title: 'in',
        dataIndex: 'in'
      },
      {
        title: 'value',
        dataIndex: 'value',
        scopedSlots: {
          customRender: 'paramIpt'
        }
      }
    ]
  },
  //主页显示
  homePage: {
    description: 'Description',
    author: 'Author',
    version: 'Version',
    host: 'Host',
    basePath: 'BasePath',
    serviceUrl: 'termsOfService',
    groupName: 'Group Name',
    groupUrl: 'Group Url',
    groupLocation: 'Group Location',
    apiCountNumber: 'Interface statistics'
  },
  markdown: {
    title: 'Other Document'
  },
  message: {
    success: 'Save successfully',
    settingTip: 'When personalization is enabled, close the interface tab or refresh the current pageAfter personalization is enabled, the interface tab tab needs to be closed and then reopened or the current page needs to be refreshed',
    settingHost: 'The host is enabled successfully. Please close the interface tab or refresh the current page to enable debuggingThe host is enabled successfully. Please close the interface tab or refresh the current page before debugging',
    unsupportstore: 'Current browsers do not support localStorage objects and cannot use this feature',
    copy: {
      url: {
        success: 'Copy address successfully',
        fail: 'Failed to copy the address, your current browser version is not compatible, please copy manually.'
      },
      method: {
        success: 'Copy Url successfully',
        fail: 'Failed to copy the url, your current browser version is not compatible, please copy manually.'
      },
      document: {
        success: 'Copy document successful',
        fail: 'Failed to copy the document, your current browser version is not compatible, please copy it manually.'
      },
      raw: {
        success: 'Copy raw successfully',
        fail: 'Failed to copy raw, your current browser version is not compatible, please copy manually.'
      },
      curl: {
        success: 'Copy curl successfully',
        fail: 'Copy curl failed, your current browser version is not compatible, please copy manually.'
      },
      open: {
        success: 'Copy OpenAPI successfully',
        fail: 'Copy OpenAPI failed, your current browser version is not compatible, please copy manually.'
      }
    },
    layer: {
      title: 'message',
      yes: 'Yes',
      no: 'No'
    },
    auth: {
      invalid: 'Invalid value',
      confirm: 'Are you sure you want to logout?',
      success: 'Logout Success'
    },
    global: {
      iptname: 'Please enter the global parameter name',
      iptvalue: 'Please enter the global parameter value',
      deleteSuccess: 'Delete Success'
    },
    settings: {
      plusFail: 'Knife4j enhancements cannot be turned on. Make sure that the annotation @EnableKnife4j is enabled on the back end',
      plusError: 'Unable to turn on Swagger BootstrapUi Enhancement, Error Cause:',
      success: 'Save successfully, please refresh the document page'
    },
    offline: {
      imple: 'This feature has not been implemented ...',
      markdown: 'Downloading Markdown file, please wait...',
      html: 'Downloading Html file, please wait...',
      word: 'Downloading Word file, please wait...',
      copy: 'Copy',
      toomany: 'The current number of interfaces exceeds the limit. Please use the third-party markdown conversion software for conversion to see the effect.',
      note: 'swagger-bootstrap-ui provides markdwon-formatted offline documents that developers can copy and convert to HTML or PDF through other markdown conversion tools..'
    },
    debug: {
      urlNotEmpty: 'Request URL address cannot be empty',
      fieldNotEmpty: 'cannot be empty',
      networkErr: 'The server is restarting or hanging up:(~~~~',
      contentToBig: 'The amount of interface response data exceeds the limit and is not displayed in the response content. Please check it in raw',
      contentToBigBlob: 'The amount of interface response data exceeds the limit and is not displayed in the response content.'
    },
    sys: {
      loadErr: 'Make sure the swagger resource interface is correct.'
    }
  },
  home: {
    des: 'Description',
    author: 'Author',
    version: 'Version',
    serviceUrl: 'serviceUrl',
    groupName: 'Group Name',
    groupUrl: 'Group url',
    groupLocation: 'Group Location',
    apiCount: 'Api Counts',
    searchText: 'Search...'
  },
  swaggerModel: {
    nodata: 'No Swagger Models',
    tableHeader: {
      name: 'name',
      des: 'description',
      type: 'type'
    }
  },
  global: {
    tab: 'Global Parameter Settings',
    add: 'Add',
    model: 'Add Parameter',
    tableHeader: [{
      title: 'name',
      dataIndex: 'name',
      width: '15%',
      scopedSlots: {
        customRender: 'name'
      }
    },
    {
      title: 'value',
      className: 'column-money',
      dataIndex: 'value',
      width: '65%',
      scopedSlots: {
        customRender: 'paramContentLabel'
      }
    },
    {
      title: 'type',
      dataIndex: 'in',
      width: '10%',
      scopedSlots: {
        customRender: 'paramTypeLable'
      }
    },
    {
      title: 'operation',
      dataIndex: 'operation',
      scopedSlots: {
        customRender: 'operation'
      }
    }
    ],
    form: {
      name: 'name',
      value: 'value',
      type: 'type',
      validate: {
        name: 'Please enter the parameter name',
        value: 'Please enter the parameter value'
      }
    },
    ok: 'ok',
    cancel: 'cancel',
    save: 'Save',
    delete: 'Delete',
    note: 'Knife4j Provide global parameter Debug function, currently default to provide header (request header), query (form) two ways of entry.<br /><br />After adding the global parameter here, the default Debug debug tab page will take this parameter.'

  },
  settings: {
    title: 'Personalized Settings',
    openCache: 'Enable request parameter cache',
    dynamicParameter: 'Enable dynamic request parameters',
    showApi: 'Enable Menu Api Address Display',
    tagDes: 'Enable Grouping tag displays description properties',
    apiFilter: 'Open RequestMapping Interface Filtering,Default',
    openCacheApi: 'Enable Open cached open API documents',
    plus: 'Enabling enhancements provided by Knife4j',
    save: 'Save',
    copy: 'copy',
    fastTitle: '<h5>Copy the following address through <kbd>ctrl + c</kbd> to open the browser'
  },
  auth: {
    cancel: 'Logout',
    save: 'Save',
    tableHeader: {
      key: 'key',
      name: 'name',
      in: 'in',
      value: 'value',
      operator: 'operate'
    },
    valueInvalid: 'Invalid Value'
  },
  menu: {
    home: 'Home',
    manager: 'DocumentHelper',
    globalsettings: 'GlobalParams',
    officeline: 'OfflineDocument',
    selfSettings: 'Settings',
    other: 'Others',
    menuItemList: [
      { key: '1', icon: 'caret-left', text: 'Close Left' },
      { key: '2', icon: 'caret-right', text: 'Close Right' },
      { key: '3', icon: 'close-circle', text: 'Close Other' }
    ]
  },
  doc: {
    title: 'Doc',
    note: 'Description',
    copy: 'Copy',
    copyHash: 'Copy Address',
    copyMethod: 'Copy Url',
    produces: 'produces',
    consumes: 'consumes',
    author: 'Developer',
    url: 'url',
    method: 'method',
    des: 'Note',
    params: 'Params',
    example: 'example',
    enumAvalible: 'Avalible',
    requestExample: 'Example',
    paramsHeader: {
      name: 'name',
      des: 'description',
      require: 'require',
      type: 'data type',
      requestType: 'request type'

    },
    responseHeaderParams: 'Response Header',
    response: 'Status',
    responseHeader: {
      code: 'code',
      des: 'description'
    },
    responseParams: 'Response Params',
    responseParamsHeader: {
      name: 'name',
      des: 'description',
      type: 'type'
    },
    responseExample: 'Response Example',
    nodata: 'No data'

  },
  offline: {
    des: 'Knife4j provides export of offline documents in 4 formats (Html/Markdown/Word/OpenAPI)',
    download: {
      markdown: 'Markdown',
      html: 'Html',
      word: 'Word',
      pdf: 'Pdf'
    },
    contact: 'Contact',
    url: 'api url',
    note: 'Description',
    schemaDes: 'schema Description'
  },
  debug: {
    title: 'Debug',
    send: 'Send',
    headers: 'Headers',
    params: 'Params',
    form: {
      upload: 'Upload',
      itemText: 'text',
      itemFile: 'file'
    },
    tableHeader: {
      holderName: 'Name',
      holderValue: 'Value',
      holderDel: 'Delete',
      selectAll: 'Select All',
      type: 'type',
      name: 'name',
      value: 'value'
    },
    response: {
      content: 'Response',
      showDes: 'Show Description',
      code: 'code:',
      cost: 'cost:',
      size: 'size:',
      header: 'Request Header',
      download: 'Download File',
      copy: 'copy'
    }

  },
  open: {
    copy: ' Copy ',
    download: ' Download '
  },
  tab: {
    closeCurrent: 'Close Current Tab',
    closeOther: 'Close Other Tab',
    closeAll: 'Close All Tab'
  },
  validate: {
    header: 'Request Header ',
    notEmpty: ' cannot be empty',
    fileNotEmpty: ' file cannot be empty'
  },
  script: {
    JSExample: 'JSExample',
    TSExample: 'TSExample',
  }
};


export default langOptions;
