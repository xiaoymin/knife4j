import KUtils from '@/core/utils'
import { marked } from 'marked'

marked.setOptions({
  gfm: true,
  tables: true,
  breaks: false,
  pedantic: false,
  sanitize: false,
  smartLists: true,
  smartypants: false
})

/**
 * 根据当前swagger分组实例得到当前组下word文本
 * @param {*} instance 
 */
export default function wordTextUS(instance) {
  var markdownCollections = [];
  if (instance != null && instance != undefined) {
    createWordHeader(markdownCollections);
    createWordBasicInfo(instance, markdownCollections);
    createWordTagsInfo(instance, markdownCollections);
    // 增强文档
    createWordPlusInfo(instance, markdownCollections);
    createWordFooter(markdownCollections);
  }
  return markdownCollections.join('\n');
}

/**
 * 主动换行
 * @param {*} markdownCollections 
 */
function wordLines(markdownCollections) {
  markdownCollections.push('\n');
}

function createWordHeader(markdownCollections) {
  var wordHeader = `<!DOCTYPE HTML PUBLIC "-// W3C//DTD HTML 4.0 Transitional//EN">
  <html>
  <head>
      <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
      <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
      <title>Export Word API</title>
      <script src="https:// cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
      <style type="text/css">
          .knife4j-word-body{
              width: 90%;
              margin: 20px auto;
              font-family:"宋体";
              font-size: 16px;
          }
          .knife4j-word-row{
              position: relative;
              height: auto;
              margin-right: 0;
              margin-left: 0;
              zoom: 1;
              display: block;
              box-sizing: border-box;
          }
          .knife4j-word-line{
              height: 35px;
              line-height:35px;
          }
          .knife4j-word-divider{
              height: 1px;
              background: #e8e8e8;
              border-bottom: 1px solid #e8e8e8;
          }
          .knife4j-word-title{
              font-weight: 600;
              font-size: 18px;
              margin-top: 15px;
              border-left: 3px solid #00ab6d;
          }
          .knife4j-word-api{
              margin-top: 10px;
          }
          .knife4j-word-content{
              margin-top: 10px;
          }
          .knife4j-word-code-editor{
              border: #ccc 1px solid;
              border-left-width: 4px;
              background-color: #fefefe;
              box-shadow: 0 0 4px #eee;
              word-break: break-all;
              word-wrap: break-word;
              color: #444;
          }
          .knife4j-word-code-editor .string { color: green; }        /*字符串的样式*/
          .knife4j-word-code-editor .number { color: darkorange; }    /*数字的样式*/
          .knife4j-word-code-editor .boolean { color: blue; }        /*布尔型数据的样式*/
          .knife4j-word-code-editor .null { color: magenta; }        /*null值的样式*/
          .knife4j-word-code-editor .key { color: red; }            /*key值的样式*/
          .knife4j-word-method{
              font-size: 14px;
              font-weight: 600;
              margin-right: 10px;
              text-align: center;
              border-radius: 3px;
              text-shadow: 0 1px 0 rgba(0, 0, 0, 0.1);
          }
          .knife4j-word-table{
              width: 120%;
              border: 1px solid #c7c4c4;
              border-collapse: collapse;
          }
          .knife4j-word-table tr{
              border: 1px solid #c7c4c4;
              height: 40px;
          }
          .knife4j-word-table th{
              border: 1px solid #c7c4c4;
              background-color: #dfdada;
          }
          .knife4j-word-table td{
              border: 1px solid #c7c4c4;
          }
      </style>
  </head>
  <body>
  <div class="knife4j-word-body">`
  markdownCollections.push(wordHeader);
}

function createWordFooter(markdownCollections) {
  markdownCollections.push('</div></body></html>');
}

/**
 * 基本信息
 * @param {*} instance 当前分组实例对象
 * @param {*} markdownCollections markdown文本集合对象
 */
function createWordBasicInfo(instance, markdownCollections) {
  markdownCollections.push('<h1>1.Project</h1>');
  markdownCollections.push('<div class="knife4j-word-row">');

  markdownCollections.push('<div class="knife4j-word-line"><strong>title</strong>:' + instance.title + '</div>');
  markdownCollections.push('<div class="knife4j-word-divider"></div>');


  markdownCollections.push('<div class="knife4j-word-line"><strong>Description</strong>:' + instance.description + '</div>');
  markdownCollections.push('<div class="knife4j-word-divider"></div>');

  markdownCollections.push('<div class="knife4j-word-line"><strong>Author</strong>:<code>' + instance.contact + '</code></div>');
  markdownCollections.push('<div class="knife4j-word-divider"></div>');

  markdownCollections.push('<div class="knife4j-word-line"><strong>HOST</strong>:' + instance.host + '</div>');
  markdownCollections.push('<div class="knife4j-word-divider"></div>');

  markdownCollections.push('<div class="knife4j-word-line"><strong>basePath</strong>:' + instance.basePath + '</div>');
  markdownCollections.push('<div class="knife4j-word-divider"></div>');

  markdownCollections.push('<div class="knife4j-word-line"><strong>termsOfService</strong>:' + instance.termsOfService + '</div>');
  markdownCollections.push('<div class="knife4j-word-divider"></div>');

  markdownCollections.push('<div class="knife4j-word-line"><strong>Concat</strong>:' + instance.contact + '</div>')
  markdownCollections.push('<div class="knife4j-word-divider"></div>');

  markdownCollections.push('<div class="knife4j-word-line"><strong>Version</strong>:' + instance.version + '</div>');
  markdownCollections.push('<div class="knife4j-word-divider"></div>');

  markdownCollections.push('<div class="knife4j-word-line"><strong>GroupName</strong>:' + instance.name + '</div>');
  markdownCollections.push('<div class="knife4j-word-divider"></div>');

  markdownCollections.push('<div class="knife4j-word-line"><strong>Group Url</strong>:' + instance.url + '</div>');
  markdownCollections.push('<div class="knife4j-word-divider"></div>');

  markdownCollections.push('<div class="knife4j-word-line"><strong>Location</strong>:' + instance.location + '</div>');
  markdownCollections.push('<div class="knife4j-word-divider"></div>');

  markdownCollections.push('</div>');
}

/**
 * 增加增强文档
 * @param {*} instance 
 * @param {*} markdownCollections 
 */
function createWordPlusInfo(instance, markdownCollections) {
  if (KUtils.checkUndefined(instance.markdownFiles)) {
    if (KUtils.arrNotEmpty(instance.markdownFiles)) {
      // if (instance.markdownFiles.length > 0) {
      wordLines(markdownCollections);
      // markdownCollections.push('# 附录');
      markdownCollections.push('<h1>3.appendix</h1>')
      instance.markdownFiles.forEach(function (md, mdIndex) {
        wordLines(markdownCollections);
        var mindex = mdIndex + 1;
        var mdTitle = '3.' + mindex + md.name;
        markdownCollections.push('<h2>' + mdTitle + '</h2>');
        if (KUtils.arrNotEmpty(md.children)) {
          md.children.forEach(mdfile => {
            markdownCollections.push('<h3>' + mdfile.title + '</h3>');
            markdownCollections.push('<div class="knife4j-word-content">');
            // 判断非空
            if (KUtils.strNotBlank(mdfile.content)) {
              //  markdownCollections.push(marked(mdfile.content));
              markdownCollections.push(marked.parse(mdfile.content));
            }
            markdownCollections.push('</div>');
          })
        }
      })
    }
  }

}

/**
 * 遍历tags分组信息
 * @param {*} instance  当前分组实例对象
 * @param {*} markdownCollections markdown文本集合对象 
 */
function createWordTagsInfo(instance, markdownCollections) {
  if (instance.tags != undefined && instance.tags != null) {
    markdownCollections.push('\n');
    markdownCollections.push('<h1>2.Interface list</h1>')
    instance.tags.forEach(function (tag, index) {
      var docIdex = parseInt(index) + 1;
      var docParent = '2.' + docIdex;
      var tagTitle = docParent + tag.name;
      markdownCollections.push('<h2>' + tagTitle + '</h2>');
      wordLines(markdownCollections);
      if (tag.childrens != undefined && tag.childrens != null && tag.childrens.length > 0) {
        // 遍历
        tag.childrens.forEach(function (apiInfo, aIndex) {
          var apiIndex = aIndex + 1;
          createWrodApiInfo(apiInfo, markdownCollections, docParent, apiIndex);
        })
      } else {
        markdownCollections.push('None')
      }
    })

  }
}

/**
 * 遍历接口详情
 * @param {*} apiInfo 接口实例
 * @param {*} markdownCollections markdown文本集合对象
 */
function createWrodApiInfo(apiInfo, markdownCollections, parentDoc, apiIndex) {
  // 二级标题
  wordLines(markdownCollections);
  var h3Title = parentDoc + '.' + apiIndex + apiInfo.summary;
  markdownCollections.push('<h3>' + h3Title + '</h3>');
  markdownCollections.push('<div class="knife4j-word-api">');

  markdownCollections.push('<div class="knife4j-word-title">method</div>')
  markdownCollections.push('<div class="knife4j-word-content"><span class="knife4j-word-method">' + apiInfo.methodType + '</span>&nbsp;&nbsp;<code>' + apiInfo.showUrl + '</code></div>');

  markdownCollections.push('<div class="knife4j-word-title">Note</div>');
  markdownCollections.push('<div class="knife4j-word-content">' + KUtils.toString(apiInfo.description, '暂无') + '</div>');

  markdownCollections.push('<div class="knife4j-word-title">Produces</div>');
  markdownCollections.push('<div class="knife4j-word-content"><code>' + KUtils.toString(apiInfo.consumes, '*') + '</code></div>')

  markdownCollections.push('<div class="knife4j-word-title">Consumes</div>');
  markdownCollections.push('<div class="knife4j-word-content"><code>' + KUtils.toString(apiInfo.produces, '*') + '</code></div>')

  if (KUtils.strNotBlank(apiInfo.author)) {
    markdownCollections.push('<div class="knife4j-word-title">Developer</div>');
    markdownCollections.push('<div class="knife4j-word-content">' + KUtils.toString(apiInfo.author, '暂无') + '</div>');
  }
  // 判断是否有请求示例
  if (KUtils.checkUndefined(apiInfo.requestValue)) {
    markdownCollections.push('<div class="knife4j-word-title">Example</div>');

    markdownCollections.push('<div class="knife4j-word-content">');
    // 需要判断是否是xml请求
    markdownCollections.push('<pre class="knife4j-word-code-editor">');
    if (apiInfo.xmlRequest) {
      // xml请求,不做处理
      markdownCollections.push(apiInfo.requestValue);
    } else {
      markdownCollections.push(wordJsonFormatter(apiInfo.requestValue));
    }

    markdownCollections.push('</pre>');
    markdownCollections.push('</div>');
  }

  // 请求参数
  createWordApiRequestParameters(apiInfo, markdownCollections);
  // 响应状态
  createWordApiResponseStatus(apiInfo, markdownCollections);
  // 响应Schema-参数
  // 判断响应参数
  createWordApiResponseParameters(apiInfo, markdownCollections);

  markdownCollections.push('</div>');
}
/**
 * 响应状态
 * @param {*} apiInfo 
 * @param {*} markdownCollections 
 */
function createWordApiResponseStatus(apiInfo, markdownCollections) {
  if (KUtils.checkUndefined(apiInfo.responseCodes) && apiInfo.responseCodes.length > 0) {
    wordLines(markdownCollections);
    markdownCollections.push('<div class="knife4j-word-title">Status</div><br/>');
    markdownCollections.push('<div class="knife4j-word-content">');

    markdownCollections.push('<table class="knife4j-word-table">');
    // 表头
    markdownCollections.push('<thead><tr><th>code</th><th>description</th><th>schema</th></tr></thead>')
    // 内容
    markdownCollections.push('<tbody>');
    wordLines(markdownCollections);
    // 拥有参数
    apiInfo.responseCodes.forEach(function (respcode) {
      markdownCollections.push('<tr>');
      markdownCollections.push('<td>' + KUtils.toString(respcode.code, '') + '</td>');
      markdownCollections.push('<td>' + KUtils.toString(respcode.description, '') + '</td>');
      markdownCollections.push('<td>' + KUtils.toString(respcode.schema, '') + '</td>');
      // markdownCollections.push('|' + KUtils.toString(respcode.code, '') + '|' + KUtils.toString(respcode.description, '') + '|' + KUtils.toString(respcode.schema, '') + '|')
      markdownCollections.push('</tr>');
    })

    markdownCollections.push('</tbody>');
    markdownCollections.push('</table><br/>');

    markdownCollections.push('</div>');
  }
}

/**
 * 响应参数拥有响应头
 * @param {*} responseHeaderParameters 
 * @param {*} markdownCollections 
 */
function createWordApiResponseHeaderParams(responseHeaderParameters, markdownCollections) {
  if (KUtils.checkUndefined(responseHeaderParameters)) {
    if (KUtils.arrNotEmpty(responseHeaderParameters)) {
      // if (responseHeaderParameters.length > 0) {
      wordLines(markdownCollections);
      markdownCollections.push('<div class="knife4j-word-title">响应Header</div>')
      wordLines(markdownCollections);
      // 拥有参数
      markdownCollections.push('<div class="knife4j-word-content">');
      markdownCollections.push('<table class="knife4j-word-table">');
      // 表头
      markdownCollections.push('<thead><tr><th>name</th><th>description</th><th>type</th></tr></thead>');
      // 内容
      markdownCollections.push('<tbody>');
      responseHeaderParameters.forEach(function (respHeader) {
        markdownCollections.push('<tr>')
        markdownCollections.push('<td>' + KUtils.toString(respHeader.name, '') + '</td>');
        markdownCollections.push('<td>' + KUtils.toString(respHeader.description, '') + '</td>');
        markdownCollections.push('<td>' + KUtils.toString(respHeader.type, '') + '</td>');
        // markdownCollections.push('|' + KUtils.toString(respHeader.name, '') + '|' + KUtils.toString(respHeader.description, '') + '|' + KUtils.toString(respHeader.type, '') + '|');
        markdownCollections.push('</tr>')
      })
      markdownCollections.push('</tbody>');
      markdownCollections.push('</table>');
      markdownCollections.push('</div>');

    }
  }
}

/**
 * 响应参数
 * @param {*} apiInfo 
 * @param {*} markdownCollections 
 * @param {*} singleFlag 
 */
function createWordApiResponseParameters(apiInfo, markdownCollections) {
  // 判断是否多个schema
  if (apiInfo.multipartResponseSchema) {
    var multipartData = apiInfo.multipCodeDatas;
    if (KUtils.arrNotEmpty(multipartData)) {
      multipartData.forEach(function (resp) {
        wordLines(markdownCollections);
        markdownCollections.push('<div class="knife4j-word-title">code-' + KUtils.toString(resp.code, '') + '</div>');
        // markdownCollections.push('**响应状态码-' + KUtils.toString(resp.code, '') + '**:');
        createWordApiResponseSingleParam(resp, markdownCollections);
      })
    }
  } else {
    // 单个
    createWordApiResponseSingleParam(apiInfo.multipData, markdownCollections);
  }
}
/**
 * 单个响应状态
 * @param {*} resp 
 * @param {*} markdownCollections 
 */
function createWordApiResponseSingleParam(resp, markdownCollections) {
  // 判断是否有响应Header
  createWordApiResponseHeaderParams(resp.responseHeaderParameters, markdownCollections);
  // 数据 
  wordLines(markdownCollections);
  markdownCollections.push('<div class="knife4j-word-title">Response Params</div>');
  // markdownCollections.push('**响应参数**:');
  wordLines(markdownCollections);
  markdownCollections.push('<div class="knife4j-word-content">');
  markdownCollections.push('<table class="knife4j-word-table">');
  // 表头
  markdownCollections.push('<thead><tr><th>name</th><th>description</th><th>type</th><th>schema</th></tr></thead>');
  markdownCollections.push('<tbody>')
  if (KUtils.arrNotEmpty(resp.data)) {
    // 拥有参数
    resp.data.forEach(function (param) {
      param.level = 1;
      markdownCollections.push('<tr>')
      markdownCollections.push('<td>' + getWordTableByLevel(param) + '</td>');
      markdownCollections.push('<td>' + KUtils.toString(param.description, '') + '</td>');
      markdownCollections.push('<td>' + KUtils.toString(param.type, '') + '</td>');
      markdownCollections.push('<td>' + KUtils.toString(param.schemaValue, '') + '</td>');
      markdownCollections.push('</tr>');
      // markdownCollections.push('|' + getWordTableByLevel(param) + '|' + KUtils.toString(param.description, '') + '|' + KUtils.toString(param.type, '') + '|' + KUtils.toString(param.schemaValue, '') + '|')
      deepWordTableByResponseParameter(param.children, markdownCollections, (param.level + 1));
    })
  } else {
    // markdownCollections.push('暂无');
    markdownCollections.push('<tr><td colspan="4">None</td></tr>')
  }
  markdownCollections.push('</tbody>')
  markdownCollections.push('</table>');
  markdownCollections.push('</div>');
  // 判断是否拥有响应示例
  wordLines(markdownCollections);
  markdownCollections.push('<div class="knife4j-word-title">Response Example</div>')
  markdownCollections.push('<div class="knife4j-word-content"><pre class="knife4j-word-code-editor">');
  if (resp.responseBasicType) {
    markdownCollections.push(resp.responseText)
  } else {
    markdownCollections.push(wordJsonFormatter(resp.responseValue))
  }
  markdownCollections.push('</pre></div>');

}


/**
 * 请求参数
 * @param {*} apiInfo 
 * @param {*} markdownCollections 
 */
function createWordApiRequestParameters(apiInfo, markdownCollections) {

  let reqParameters = apiInfo.reqParameters;
  wordLines(markdownCollections);
  markdownCollections.push('<div class="knife4j-word-title">Params</div><br/>')
  markdownCollections.push('<div class="knife4j-word-content">');
  markdownCollections.push('<table class="knife4j-word-table">');
  // 表头
  markdownCollections.push('<thead><tr><th>name</th><th>description</th><th>type</th><th>require</th><th>type</th><th>schema</th></tr></thead>');
  markdownCollections.push('<tbody>');
  // 判断是否拥有请求参数
  if (KUtils.arrNotEmpty(reqParameters)) {
    // if (reqParameters.length > 0) {
    // 级联表格，在表格需要最佳空格缩进符号
    deepWordTableByRequestParameter(reqParameters, markdownCollections, 1);
  } else {
    // 无参数
    markdownCollections.push('<tr><td colspan="6">None</td></tr>');
  }
  markdownCollections.push('</tbody>');
  markdownCollections.push('</table>')
  markdownCollections.push('</div>');
}

/**
 *  递归循环遍历响应参数得到markdown表格
 * @param {*} parameters 
 * @param {*} markdownCollections 
 */
function deepWordTableByResponseParameter(parameters, markdownCollections, level) {
  if (parameters != null && parameters != undefined && parameters.length > 0) {
    parameters.forEach(function (param) {
      param.level = level;
      markdownCollections.push('<tr>')
      markdownCollections.push('<td>' + getWordTableByLevel(param) + '</td>');
      markdownCollections.push('<td>' + KUtils.toString(param.description, '') + '</td>');
      markdownCollections.push('<td>' + KUtils.toString(param.type, '') + '</td>');
      markdownCollections.push('<td>' + KUtils.toString(param.schemaValue, '') + '</td>');
      markdownCollections.push('</tr>');
      // markdownCollections.push('|' + getWordTableByLevel(param) + '|' + KUtils.toString(param.description, '') + '|' + KUtils.toString(param.type, '') + '|' + KUtils.toString(param.schemaValue, '') + '|')
      deepWordTableByResponseParameter(param.children, markdownCollections, (param.level + 1));
    })
  }

}


/**
 * 递归循环遍历参数得到markdown表格
 * @param {*} parameters 
 * @param {*} markdownCollections 
 */
function deepWordTableByRequestParameter(parameters, markdownCollections, level) {
  if (parameters != null && parameters != undefined && parameters.length > 0) {
    parameters.forEach(function (param) {
      // 赋值一个level
      param.level = level;
      markdownCollections.push('<tr>');
      markdownCollections.push('<td>' + getWordTableByLevel(param) + '</td>');
      markdownCollections.push('<td>' + KUtils.toString(param.description, '') + '</td>');
      markdownCollections.push('<td>' + KUtils.toString(param.in, '') + '</td>');
      markdownCollections.push('<td>' + KUtils.toString(param.require, '') + '</td>');
      markdownCollections.push('<td>' + KUtils.toString(param.type, '') + '</td>');
      markdownCollections.push('<td>' + KUtils.toString(param.schemaValue, '') + '</td>');
      markdownCollections.push('</tr>');
      // markdownCollections.push('|' + getWordTableByLevel(param) + '|' + KUtils.toString(param.description, '') + '|' + KUtils.toString(param.in, '') + '|' + KUtils.toString(param.require, '') + '|' + KUtils.toString(param.type, '') + '|' + KUtils.toString(param.schemaValue, '') + '|')
      deepWordTableByRequestParameter(param.children, markdownCollections, (param.level + 1));
    })
  }

}


/**
 * 根据参数级别获取名称
 * @param {*} param 
 */
function getWordTableByLevel(param) {
  var spaceArr = [];
  for (var i = 1; i < param.level; i++) {
    spaceArr.push('&nbsp;')
  }
  var tmpName = spaceArr.join('') + param.name;
  return tmpName;
}

function wordJsonFormatter(json) {
  try {
    if (typeof json != "string") {
      json = JSON.stringify(json, undefined, 2);
    }
    json = json
      .replace(/&/g, "&")
      .replace(/</g, "<")
      .replace(/>/g, ">");
    return json.replace(
      /("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g,
      function (match) {
        var cls = "number";
        if (/^"/.test(match)) {
          if (/:$/.test(match)) {
            cls = "key";
          } else {
            cls = "string";
          }
        } else if (/true|false/.test(match)) {
          cls = "boolean";
        } else if (/null/.test(match)) {
          cls = "null";
        }
        return '<span class="' + cls + '">' + match + "</span>";
      }
    );
  } catch (error) {
    return json;
  }
}
