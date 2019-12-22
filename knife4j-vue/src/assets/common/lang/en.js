const langOptions = {
  title: 'title',
  searchHolderText: 'Enter keyword search',
  docLinkTip: 'Documentation',
  langText: 'EN',
  settingText: 'Settings',
  cacheText: 'Clear Caches',
  //主页显示
  homePage: {
    description: 'Description',
    author: 'Author',
    version: 'Version',
    host: 'Host',
    basePath: 'BasePath',
    serviceUrl: 'Service Url',
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
    unsupportstore: 'Current browsers do not support localStorage objects and cannot use this feature',
    copy: {
      success: 'Copy Success',
      fail: 'Copy failed. Your current browser version is incompatible. Please copy manually.'
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
      copy: 'Copy',
      toomany: 'The current number of interfaces exceeds the limit. Please use the third-party markdown conversion software for conversion to see the effect.',
      note: 'swagger-bootstrap-ui provides markdwon-formatted offline documents that developers can copy and convert to HTML or PDF through other markdown conversion tools..'
    },
    debug: {
      urlNotEmpty: 'Request URL address cannot be empty',
      fieldNotEmpty: 'cannot be empty',
      networkErr: 'The server is restarting or hanging up:(~~~~'
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
    tableHeader: {
      name: 'name',
      value: 'value',
      type: 'type',
      operator: 'operate'
    },
    save: 'Save',
    delete: 'Delete',
    note: 'swagger-bootstrap-ui Provide global parameter Debug function, currently default to provide header (request header), query (form) two ways of entry.<br /><br />After adding the global parameter here, the default Debug debug tab page will take this parameter, which is valid only under this group, and different groups need to be set separately.'

  },
  settings: {
    title: 'Personalized Settings',
    openCache: 'Enable request parameter cache',
    dynamicParameter: 'Enable dynamic request parameters',
    showApi: 'Enable Menu Api Address Display',
    tagDes: 'Enable Grouping tag displays dsecription description properties',
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
    manager: 'Doc Manager',
    globalsettings: 'Global Parameter Settings',
    officeline: 'Offline document(MD)',
    selfSettings: 'Settings'
  },
  doc: {
    title: 'Doc',
    note: 'Description',
    copy: 'Copy',
    copyHash: 'Copy Url',
    produces: 'produces',
    consumes: 'consumes',
    author: 'Developer',
    url: 'url',
    method: 'method',
    des: 'note',
    params: 'Request Params',
    requestExample: 'Request Example',
    paramsHeader: {
      name: 'name',
      des: 'description',
      require: 'require',
      type: 'data type',
      requestType: 'request type'

    },
    responseHeaderParams: 'Response Header',
    response: 'Response Status',
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
    des: 'Description',
    contact: 'Contact',
    url: 'api url',
    note: 'Description',
    schemaDes: 'schema Description'
  },
  debug: {
    title: 'Debug',
    send: 'Send',
    params: 'Request parameter list',
    tableHeader: {
      selectAll: 'Select All',
      type: 'type',
      name: 'name',
      value: 'value'
    },
    response: {
      content: 'Response',
      showDes: 'Show Description',
      code: 'code',
      cost: 'cost',
      size: 'size',
      header: 'Request Header',
      download: 'Download File'
    }

  },
  tab: {
    closeCurrent: 'Close Current Tab',
    closeOther: 'Close Other Tab',
    closeAll: 'Close All Tab'
  }
};


export default langOptions;
