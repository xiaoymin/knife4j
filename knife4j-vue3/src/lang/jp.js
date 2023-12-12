const langOptions = {
  //界面显示
  lang: 'jp',
  title: 'タイトル',
  searchHolderText: 'キーワードで検索',
  docLinkTip: 'ヘルプドキュメント',
  langText: '日本語',
  settingText: '個性化設定',
  cacheText: 'キャッシュをクリア',
  //文档中各个表格的属性
  table: {
    //SwaggerModels
    swaggerModelsColumns: [
      {
        title: '名前',
        dataIndex: 'name',
        width: '30%'
      },
      {
        title: 'タイプ',
        dataIndex: 'type',
        width: '15%'
      },
      {
        title: '説明',
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
        title: 'パラメータ名',
        dataIndex: 'name',
        width: '30%'
      },
      {
        title: 'パラメータの説明',
        dataIndex: 'description',
        width: '25%',
        scopedSlots: { customRender: 'descriptionValueTemplate' }
      },
      {
        title: 'リクエストタイプ',
        dataIndex: 'in',
        scopedSlots: { customRender: 'typeTemplate' }
      },
      {
        title: '必須かどうか',
        dataIndex: 'require',
        scopedSlots: { customRender: 'requireTemplate' }
      },
      {
        title: 'データタイプ',
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
        title: 'ステータスコード',
        dataIndex: 'code',
        width: '20%'
      },
      {
        title: '説明',
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
        title: 'パラメータ名',
        dataIndex: 'name',
        width: '30%'
      },
      {
        title: 'パラメータの説明',
        dataIndex: 'description',
        width: '55%'
      },
      {
        title: 'データタイプ',
        dataIndex: 'type'
      }
    ],
    //文档说明-响应参数
    documentResponseColumns: [
      {
        title: 'パラメータ名',
        dataIndex: 'name',
        width: '35%'
      },
      {
        title: 'パラメータの説明',
        dataIndex: 'description',
        scopedSlots: { customRender: 'descriptionTemplate' },
        width: '40%'
      },
      {
        title: 'タイプ',
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
        title: 'リクエストヘッダ',
        dataIndex: 'name',
        width: '20%',
        scopedSlots: {
          customRender: 'headerName'
        }
      },
      {
        title: 'コンテンツ',
        dataIndex: 'content',
        scopedSlots: {
          customRender: 'headerValue'
        }
      },
      {
        title: 'オプション',
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
        title: 'パラメータ名',
        dataIndex: 'name',
        width: '20%',
        scopedSlots: {
          customRender: 'formName'
        }
      },
      {
        title: 'タイプ',
        dataIndex: 'type',
        width: '12%',
        scopedSlots: {
          customRender: 'formType'
        }
      },
      {
        title: 'パラメータ値',
        dataIndex: 'content',
        scopedSlots: {
          customRender: 'formValue'
        }
      },
      {
        title: 'オプション',
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
        title: 'パラメータ名',
        dataIndex: 'name',
        width: '20%',
        scopedSlots: {
          customRender: 'urlFormName'
        }
      },
      {
        title: 'パラメータ値',
        dataIndex: 'content',
        scopedSlots: {
          customRender: 'urlFormValue'
        }
      },
      {
        title: 'オプション',
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
        title: 'レスポンスヘッダ',
        dataIndex: 'name',
        width: '20%'
      },
      {
        title: '値',
        dataIndex: 'value'
      }
    ],
    //auth中的header
    authHeaderColumns: [
      {
        title: 'パラメータキー',
        dataIndex: 'key',
        customRender(text, row, index) {
          return row.key + '(' + row.type + ')';
        }
      },
      {
        title: 'パラメータ名',
        className: 'column-money',
        dataIndex: 'name'
      },
      {
        title: 'in',
        dataIndex: 'in'
      },
      {
        title: 'パラメータ値',
        dataIndex: 'value',
        scopedSlots: {
          customRender: 'paramIpt'
        }
      }
    ]
  },
  //主页显示
  homePage: {
    description: '紹介',
    author: '作者',
    version: 'バージョン',
    host: 'host',
    basePath: 'basePath',
    serviceUrl: 'サービスURL',
    groupName: 'グループ名',
    groupUrl: 'グループURL',
    groupLocation: 'グループロケーション',
    apiCountNumber: 'API統計情報'
  },
  markdown: {
    title: 'その他のドキュメント'
  },
  message: {
    success: '保存成功',
    settingTip: '個性化設定有効後、インターフェースタブを閉じてから再度開くか、または現在のページをリフレッシュする必要があります。',
    settingHost: 'Hostの有効化が成功しました。インターフェースタブを閉じるか、または現在のページをリフレッシュしてデバッグしてください。',
    unsupportstore: '現在のブラウザはlocalStorageオブジェクトをサポートしていないため、この機能は使用できません。',
    copy: {
      url: {
        success: 'アドレスのコピーが成功しました。',
        fail: 'アドレスのコピーに失敗しました。お使いのブラウザのバージョンが対応していないため、手動でコピーしてください。'
      },
      method: {
        success: 'インターフェースのコピーが成功しました。',
        fail: 'インターフェースのコピーに失敗しました。お使いのブラウザのバージョンが対応していないため、手動でコピーしてください。'
      },
      document: {
        success: 'ドキュメントのコピーが成功しました。',
        fail: 'ドキュメントのコピーに失敗しました。お使いのブラウザのバージョンが対応していないため、手動でコピーしてください。'
      },
      raw: {
        success: 'Rawのコピーが成功しました。',
        fail: 'Rawのコピーに失敗しました。お使いのブラウザのバージョンが対応していないため、手動でコピーしてください。'
      },
      curl: {
        success: 'cURLのコピーが成功しました。',
        fail: 'cURLのコピーに失敗しました。お使いのブラウザのバージョンが対応していないため、手動でコピーしてください。'
      },
      open: {
        success: 'OpenAPIのコピーが成功しました。',
        fail: 'OpenAPIのコピーに失敗しました。お使いのブラウザのバージョンが対応していないため、手動でコピーしてください。'
      }
    },
    layer: {
      title: '情報。',
      yes: '確定。',
      no: 'キャンセル。'
    },
    auth: {
      invalid: '値が無効。',
      confirm: 'ログアウトしますか？',
      success: 'ログアウトが成功しました。'
    },
    global: {
      iptname: '全局パラメータ名を入力してください',
      iptvalue: '全局パラメータ値を入力してください',
      deleteSuccess: '削除が成功しました'
    },
    settings: {
      plusFail: 'Knife4jの拡張機能を有効にできません。バックエンドで@EnableKnife4jアノテーションが有効になっていることを確認してください。',
      plusError: 'Knife4jの拡張機能を有効にできません。エラーの原因：',
      success: '保存が成功しました。このドキュメントページをリフレッシュしてください。'
    },
    offline: {
      imple: 'この機能はまだ実装されていません...',
      markdown: 'Markdownファイルのダウンロード中、お待ちください...',
      html: 'HTMLファイルのダウンロード中、お待ちください...',
      word: 'Wordファイルのダウンロード中、お待ちください...',
      copy: 'ドキュメントのコピー',
      toomany: '現在のインターフェース数が制限を超えています。他のmarkdown変換ソフトウェアを使用して変換してから結果を表示してください。',
      note: 'swagger-bootstrap-uiはmarkdown形式のオフラインドキュメントを提供し、開発者はその内容を他のmarkdown変換ツールを使用してhtmlまたはpdfに変換できます。'
    },
    debug: {
      urlNotEmpty: 'リクエストURLは空にできません。',
      fieldNotEmpty: '空にできません',
      networkErr: 'サーバーは再起動中またはすでに停止しています:(~~~~',
      contentToBig: 'インターフェースの応答データ量が制限を超えており、応答内容に表示されていません。rawで確認してください。',
      contentToBigBlob: 'インターフェースの応答データ量が制限を超えており、応答内容に表示されていません。'
    },
    sys: {
      loadErr: 'swaggerリソースインターフェースが正確であることを確認してください。'
    }
  },
  swaggerModel: {
    nodata: 'Swagger Modelsはありません',
    tableHeader: {
      name: '名前',
      des: '説明',
      type: 'タイプ'
    }
  },
  global: {
    tab: 'グローバルパラメータの設定',
    add: 'パラメータの追加',
    model: '新しいパラメータ',
    tableHeader: [{
      title: 'パラメータ名',
      dataIndex: 'name',
      width: '15%',
      scopedSlots: {
        customRender: 'name'
      }
    },
    {
      title: 'パラメータ値',
      className: 'column-money',
      dataIndex: 'value',
      width: '65%',
      scopedSlots: {
        customRender: 'paramContentLabel'
      }
    },
    {
      title: 'パラメータのタイプ',
      dataIndex: 'in',
      width: '10%',
      scopedSlots: {
        customRender: 'paramTypeLable'
      }
    },
    {
      title: 'オプション',
      dataIndex: 'operation',
      scopedSlots: {
        customRender: 'operation'
      }
    }
    ],
    form: {
      name: 'パラメータ名',
      value: 'パラメータ値',
      type: 'パラメータのタイプ',
      validate: {
        name: 'パラメータ名を入力してください',
        value: 'パラメータ値を入力してください'
      }
    },
    ok: '確定',
    cancel: 'キャンセル',
    save: '保存',
    delete: '削除',
    note: 'Knife4jは全体のパラメータデバッグ機能を提供しており、現在はデフォルトでheader（リクエストヘッダ）およびquery（フォーム）の2つの方法の入力が提供されています。<br /><br />ここに全体のパラメータを追加すると、デフォルトのデバッグタブにそのパラメータが表示されます。'

  },
  settings: {
    title: '個性化設定',
    openCache: 'リクエストパラメータキャッシュを有効にする',
    dynamicParameter: '動的リクエストパラメータを有効にする',
    showApi: 'メニューにAPIアドレスを表示する',
    tagDes: '分組tagにdsecription属性を表示する',
    apiFilter: 'RequestMappingインターフェースのフィルターを有効にし、デフォルトでは表示されるものだけ表示する',
    openCacheApi: '既に開いているAPIドキュメントをキャッシュする',
    plus: 'Knife4jが提供する拡張機能を有効にする',
    save: 'コンテンツを保存する',
    copy: 'コピー',
    fastTitle: '<h5>以下のアドレスを <kbd>ctrl + c</kbd> でコピーして、ブラウザを開いて個性化設定を行います。</h5>'
  },
  auth: {
    cancel: '注销',
    save: '保存',
    tableHeader: {
      key: 'パラメータキー',
      name: 'パラメータ名',
      in: 'in',
      value: 'パラメータ値',
      operator: 'オプション'
    },
    valueInvalid: '値が無効'
  },
  menu: {
    home: 'ホーム',
    manager: 'ドキュメント管理',
    globalsettings: '全体のパラメータ設定',
    officeline: 'オフラインドキュメント',
    selfSettings: '個性化設定',
    other: 'その他のドキュメント',
    menuItemList: [
      { key: '1', icon: 'caret-left', text: '左を閉じる' },
      { key: '2', icon: 'caret-right', text: '右を閉じる' },
      { key: '3', icon: 'close-circle', text: '他を閉じる' }
    ]
  },
  offline: {
    des: 'Knife4jは4つの形式のオフラインドキュメント（Html/Markdown/Word/OpenAPI）を提供しています。',
    download: {
      markdown: 'Markdownのダウンロード',
      html: 'Htmlのダウンロード',
      word: 'Wordのダウンロード',
      pdf: 'Pdfのダウンロード'
    },
    contact: '連絡先',
    url: 'インターフェースのパス',
    note: '概要',
    schemaDes: 'schema属性の説明'
  },
  doc: {
    title: 'ドキュメント',
    note: 'インターフェースの説明',
    copy: 'ドキュメントのコピー',
    copyHash: 'アドレスのコピー',
    copyMethod: 'インターフェースのコピー',
    produces: 'リクエストデータのタイプ',
    consumes: 'レスポンスデータのタイプ',
    author: '開発者',
    url: 'インターフェースのアドレス',
    method: 'リクエストの方法',
    des: 'インターフェースの説明',
    params: 'リクエストパラメータ',
    requestExample: 'リクエストの例',
    paramsHeader: {
      name: 'パラメータ名',
      des: 'パラメータの説明',
      require: '必須',
      type: 'データタイプ',
      requestType: 'リクエストタイプ'

    },
    responseHeaderParams: 'レスポンスヘッダ',
    response: 'レスポンスステータス',
    responseHeader: {
      code: 'ステータスコード',
      des: '説明'

    },
    responseParams: 'レスポンスパラメータ',
    responseParamsHeader: {
      name: 'パラメータ名',
      des: 'パラメータの説明',
      type: 'タイプ'
    },
    responseExample: 'レスポンスの例',
    nodata: '現在はありません'

  },
  debug: {
    title: 'デバッグ',
    send: ' 送信 ',
    headers: 'リクエストヘッダ',
    params: 'リクエストパラメータ',
    form: {
      upload: 'ファイルの選択',
      itemText: 'テキスト',
      itemFile: 'ファイル'
    },
    tableHeader: {
      holderName: 'リクエストヘッダの名前',
      holderValue: 'リクエストヘッダの内容',
      holderDel: '削除',
      selectAll: 'すべて選択',
      type: 'パラメータのタイプ',
      name: 'パラメータ名',
      value: 'パラメータ値'
    },
    response: {
      content: 'レスポンスの内容',
      showDes: '説明を表示',
      code: 'レスポンスコード:',
      cost: '所要時間:',
      size: 'サイズ:',
      header: 'リクエストヘッダ',
      download: 'ファイルのダウンロード',
      copy: 'コピー'
    }

  },
  open: {
    copy: ' 複写 ',
    download: ' ダウンロード '
  },
  tab: {
    closeCurrent: '現在のタブを閉じる',
    closeOther: 'その他のタブを閉じる',
    closeAll: 'すべてのタブを閉じる'
  },
  validate: {
    header: 'リクエストヘッダ ',
    notEmpty: ' 空であってはいけません',
    fileNotEmpty: ' ファイルは空にできません'
  },
  script: {
    JSExample: 'JSテンプレートの例',
    TSExample: 'TSテンプレートの例',
  }
}

export default langOptions;
