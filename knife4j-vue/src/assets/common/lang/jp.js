const langOptions = {
  //界面显示
  lang: 'jp',
  title: 'タイトル',
  searchHolderText: 'キーワードで検索',
  docLinkTip: 'ドキュメンテーション',
  langText: '日本語',
  settingText: '設定',
  cacheText: 'キャッシュをクリアする',
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
        title: '種類',
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
        title: 'パラメータ説明',
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
        title: '必須項目',
        dataIndex: 'require',
        scopedSlots: { customRender: 'requireTemplate' }
      },
      {
        title: 'データ型',
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
        title: 'ステータス',
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
        title: 'パラメータ説明',
        dataIndex: 'description',
        width: '55%'
      },
      {
        title: 'データ型',
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
        title: 'パラメータ説明',
        dataIndex: 'description',
        scopedSlots: { customRender: 'descriptionTemplate' },
        width: '40%'
      },
      {
        title: 'データ型',
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
        title: 'データ型',
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
        title: 'リクエストヘッダ',
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
        title: 'キー',
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
    author: '著者',
    version: 'バージョン',
    host: 'host',
    basePath: 'basePath',
    serviceUrl: 'APIサービス契約',
    groupName: 'グループ名',
    groupUrl: 'グループ URL',
    groupLocation: 'グループlocation',
    apiCountNumber: 'インターフェース統計情報'
  },
  markdown: {
    title: 'その他'
  },
  message: {
    success: '保存が成功しました',
    settingTip: '個性的な設定を有効にした後、インターフェースタブは閉じて再度開くか、または現在のページを更新する必要があります',
    settingHost: 'ホストの有効化が成功しました。インターフェースタブを閉じるか、または現在のページを更新してからデバッグを続行してください',
    unsupportstore: '現在のブラウザは localStorage オブジェクトをサポートしていないため、この機能は使用できません',
    copy: {
      url: {
        success: 'アドレスのコピーが成功しました',
        fail: 'アドレスのコピーに失敗しました。お使いのブラウザのバージョンが対応していません。手動でコピーしてください'
      },
      method: {
        success: 'インターフェースのコピーが成功しました',
        fail: 'インターフェースのコピーに失敗しました。お使いのブラウザのバージョンが対応していません。手動でコピーしてください'
      },
      document: {
        success: 'ドキュメントのコピーが成功しました',
        fail: 'ドキュメントのコピーに失敗しました。お使いのブラウザのバージョンが対応していません。手動でコピーしてください。'
      },
      raw: {
        success: 'Raw のコピーが成功しました',
        fail: 'Raw のコピーに失敗しました。お使いのブラウザのバージョンが対応していません。手動でコピーしてください'
      },
      curl: {
        success: 'CURL のコピーが成功しました',
        fail: '"CURL のコピーに失敗しました。お使いのブラウザのバージョンが対応していません。手動でコピーしてください。'
      },
      open: {
        success: 'OpenAPI のコピーが完了しました',
        fail: 'OpenAPI のコピーに失敗しました。お使いのブラウザのバージョンが対応していません。手動でコピーしてください'
      }
    },
    layer: {
      title: '情報',
      yes: '確定',
      no: '取消'
    },
    auth: {
      invalid: '無効な値',
      confirm: 'ログアウトしますか？',
      success: 'ログアウトが成功しました'
    },
    global: {
      iptname: 'グローバルパラメータ名を入力してください',
      iptvalue: 'グローバルパラメータの値を入力してください',
      deleteSuccess: '削除成功'
    },
    settings: {
      plusFail: 'Knife4jの拡張機能を有効にできません。バックエンドで@EnableKnife4j注釈が有効になっていることを確認してください',
      plusError: 'Knife4jの拡張機能を有効にできません。エラーの原因：',
      success: '保存が成功しました。このドキュメントページをリフレッシュしてください。'
    },
    offline: {
      imple: 'この機能はまだ実装されていません...',
      markdown: 'Markdownファイルをダウンロードしています、お待ちください...',
      html: 'HTMLファイルをダウンロードしています、お待ちください...',
      word: 'Wordファイルをダウンロードしています、お待ちください...',
      copy: 'ドキュメントをコピー',
      toomany: '現在のAPI数が制限を超えていますので、他のMarkdown変換ツールを使用して変換して結果を確認してください。',
      note: 'Swagger-Bootstrap-UI はMarkdown形式のオフラインドキュメントを提供していますので、開発者はこのコンテンツをコピーして他のMarkdown変換ツールを使用してHTMLまたはPDFに変換できます。'
    },
    debug: {
      urlNotEmpty: 'リクエストURLが空です',
      fieldNotEmpty: '空であってはいけません',
      networkErr: 'サーバーは再起動中または既に停止しています:(~~~~',
      contentToBig: 'APIの応答データ量が制限を超えています。応答内容には表示されませんので、raw で確認してください',
      contentToBigBlob: 'APIの応答データ量が制限を超えています。応答内容には表示されません'
    },
    sys: {
      loadErr: '请确保 Swagger 资源接口正确'
    }
  },
  swaggerModel: {
    nodata: 'Swagger Models はありません',
    tableHeader: {
      name: '名称',
      des: '説明',
      type: 'タイプ'
    }
  },
  global: {
    tab: 'グローバルパラメータの設定',
    add: 'パラメータの追加',
    model: '新しいパラメータの追加',
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
      title: 'パラメータ型',
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
      type: 'パラメータ型',
      validate: {
        name: 'パラメータ名称を入力してください',
        value: 'パラメータ値を入力してください'
      }
    },
    ok: '確定',
    cancel: '取消',
    save: '保存',
    delete: '削除',
    note: 'Knife4jは、グローバルパラメータのDebug機能を提供しており、現在はデフォルトでheader（リクエストヘッダ）およびquery（form）の2つの入力方法が提供されています。<br /><br />ここにグローバルパラメータを追加すると、デフォルトのDebugデバッグタブページにそのパラメータが表示されます。'

  },
  settings: {
    title: '個性的な設定',
    openCache: 'リクエストパラメータキャッシュを有効にする',
    dynamicParameter: 'ダイナミックリクエストパラメータを有効にする',
    showApi: 'メニューAPIアドレスを表示',
    tagDes: 'グループタグにdescriptionプロパティを表示',
    apiFilter: 'RequestMappingインターフェースのフィルタリングを有効にする',
    openCacheApi: '既存のAPIドキュメントのキャッシュを有効にする',
    plus: 'Knife4jが提供する拡張機能を有効にする',
    save: '内容を保存',
    copy: 'コピー',
    fastTitle: '<h5>以下のアドレスを <kbd>ctrl + c</kbd> でコピーし、ブラウザを開いて素早く個性化設定を行うことができます</h5>'
  },
  auth: {
    cancel: 'ログアウト',
    save: '保存',
    tableHeader: {
      key: 'パラメータキー',
      name: 'パラメータ名',
      in: 'in',
      value: 'パラメータ値',
      operator: '操作'
    },
    valueInvalid: '値が無効'
  },
  menu: {
    home: 'ホーム',
    manager: 'ドキュメント管理',
    globalsettings: 'グローバルパラメータの設定',
    officeline: 'オフラインドキュメント',
    selfSettings: '個性化の設定',
    other: 'その他のドキュメント',
    menuItemList: [
      { key: '1', icon: 'caret-left', text: '左側を閉じる' },
      { key: '2', icon: 'caret-right', text: '右側を閉じる' },
      { key: '3', icon: 'close-circle', text: '他を閉じる' }
    ]
  },
  offline: {
    des: 'Knife4jは4つの形式（Html/Markdown/Word/OpenAPI）でのオフラインドキュメントのエクスポートを提供しています',
    download: {
      markdown: 'Markdownをダウンロード',
      html: 'Htmlをダウンロード',
      word: 'Wordをダウンロード',
      pdf: 'Pdfをダウンロード'
    },
    contact: '連絡先',
    url: 'インターフェースパス',
    note: '紹介',
    schemaDes: 'schemaプロパティの説明'
  },
  doc: {
    title: 'インターフェースの説明',
    note: 'インターフェースの説明',
    copy: 'ドキュメントのコピー',
    copyHash: 'アドレスのコピー',
    copyMethod: 'インターフェースのコピー',
    produces: 'リクエストデータタイプ',
    consumes: 'レスポンスデータタイプ',
    author: '開発者',
    url: 'インターフェースアドレス',
    method: 'リクエスト方法',
    des: 'インターフェースの説明',
    params: 'リクエストパラメータ',
    example: '例',
    enumAvalible: '利用可能な値',
    requestExample: 'リクエストの例',
    paramsHeader: {
      name: 'パラメータ名',
      des: 'パラメータの説明',
      require: '必須項目',
      type: 'データタイプ',
      requestType: 'リクエストの種類'

    },
    responseHeaderParams: 'レスポンスヘッダー',
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
    nodata: 'なし'

  },
  debug: {
    title: 'デバッグ',
    send: ' 送信 ',
    headers: 'リクエストヘッダ',
    params: 'リクエストパラメータ',
    form: {
      upload: 'ファイルを選択',
      itemText: 'テキスト',
      itemFile: 'ファイル'
    },
    tableHeader: {
      holderName: 'リクエストヘッダ名',
      holderValue: 'リクエストヘッダの内容',
      holderDel: '削除',
      selectAll: 'すべて選択',
      type: 'パラメータタイプ',
      name: 'パラメータ名',
      value: 'パラメータ値'
    },
    response: {
      content: 'レスポンス内容',
      showDes: '説明を表示',
      code: 'レスポンスコード:',
      cost: '処理時間:',
      size: 'サイズ:',
      header: 'リクエストヘッダ',
      download: 'ファイルのダウンロード',
      copy: 'コピー'
    }

  },
  open: {
    copy: ' コピー ',
    download: ' ダウンロード '
  },
  tab: {
    closeCurrent: '現在のタブを閉じる',
    closeOther: '他のすべてのタブを閉じる',
    closeAll: 'すべてのタブを閉じる'
  },
  validate: {
    header: 'リクエストヘッダ ',
    notEmpty: ' 必須です',
    fileNotEmpty: ' ファイルは空にできません'
  },
  script: {
    JSExample: 'JSテンプレートの例',
    TSExample: 'TSテンプレートの例',
  }
}

export default langOptions;
