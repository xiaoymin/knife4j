import KUtils from '@/core/utils'

/**
 * 在单页面文档中点击复制,得到当前文档页的markdown文档
 * @param {*} apiInfo 
 */
export default function markdownSingleTextUs(apiInfo) {
  var markdownCollections = [];
  if (apiInfo != null && apiInfo != undefined) {
    // 二级标题
    markdownLines(markdownCollections);
    markdownCollections.push('## ' + apiInfo.summary);
    markdownLines(markdownCollections);
    markdownCollections.push('**Url**:`' + apiInfo.showUrl + '`');
    markdownLines(markdownCollections);
    markdownCollections.push('**Method**:`' + apiInfo.methodType + '`');
    markdownLines(markdownCollections);
    markdownCollections.push('**produces**:`' + KUtils.toString(apiInfo.consumes, '*') + '`');
    markdownLines(markdownCollections);
    markdownCollections.push('**consumes**:`' + KUtils.toString(apiInfo.produces, '*') + '`');
    markdownLines(markdownCollections);
    markdownCollections.push('**description**:' + KUtils.toString(apiInfo.description, 'None') + '');
    // 判断是否有请求示例
    if (KUtils.checkUndefined(apiInfo.requestValue)) {
      markdownLines(markdownCollections);
      markdownCollections.push('**Sample**:');
      markdownLines(markdownCollections);
      markdownCollections.push('```javascript');
      markdownCollections.push(apiInfo.requestValue);
      markdownCollections.push('```');
    }
    // 请求参数
    createApiRequestParameters(apiInfo, markdownCollections);
    // 响应状态
    createApiResponseStatus(apiInfo, markdownCollections);
    // 响应Schema-参数
    // 判断响应参数
    createApiResponseParameters(apiInfo, markdownCollections);
  }
  return markdownCollections.join('\n');
}
/**
 * 主动换行
 * @param {*} markdownCollections 
 */
function markdownLines(markdownCollections) {
  markdownCollections.push('\n');
}

/**
 * 请求参数
 * @param {*} apiInfo 
 * @param {*} markdownCollections 
 */
function createApiRequestParameters(apiInfo, markdownCollections) {
  let reqParameters = apiInfo.reqParameters;
  markdownLines(markdownCollections);
  markdownCollections.push('**Params**:');
  markdownLines(markdownCollections);
  markdownCollections.push('**Params**:');
  // 判断是否拥有请求参数
  if (reqParameters.length > 0) {
    markdownLines(markdownCollections);
    // 拥有参数
    markdownCollections.push('| name | description | in    | require | type | schema |');
    markdownCollections.push('| -------- | -------- | ----- | -------- | -------- | ------ |');
    // 级联表格，在表格需要最佳空格缩进符号
    deepMdTableByRequestParameter(reqParameters, markdownCollections, 1);
  } else {
    markdownLines(markdownCollections);
    markdownCollections.push('None');
  }
}

/**
 * 响应状态
 * @param {*} apiInfo 
 * @param {*} markdownCollections 
 */
function createApiResponseStatus(apiInfo, markdownCollections) {
  if (KUtils.checkUndefined(apiInfo.responseCodes) && apiInfo.responseCodes.length > 0) {
    markdownLines(markdownCollections);
    markdownCollections.push('**status**:');
    markdownLines(markdownCollections);
    // 拥有参数
    markdownCollections.push('| code | description | schema |');
    markdownCollections.push('| -------- | -------- | ----- | ');
    apiInfo.responseCodes.forEach(function (respcode) {
      markdownCollections.push('|' + KUtils.toString(respcode.code, '') + '|' + KUtils.toString(respcode.description, '') + '|' + KUtils.toString(respcode.schema, '') + '|')
    })
  }
}

/**
 * 响应参数
 * @param {*} apiInfo 
 * @param {*} markdownCollections 
 * @param {*} singleFlag 
 */
function createApiResponseParameters(apiInfo, markdownCollections) {
  // 判断是否多个schema
  if (apiInfo.multipartResponseSchema) {
    var multipartData = apiInfo.multipCodeDatas;
    if (KUtils.arrNotEmpty(multipartData)) {
      multipartData.forEach(function (resp) {
        markdownLines(markdownCollections);
        markdownCollections.push('**code-' + KUtils.toString(resp.code, '') + '**:');
        createApiResponseSingleParam(resp, markdownCollections);
      })
    }
  } else {
    // 单个
    createApiResponseSingleParam(apiInfo.multipData, markdownCollections);
  }

}


/**
 * 响应参数递归遍历参数
 * @param {*} md 
 * @param {*} modelData 
 */
function findRespModelChildren(md, modelData) {
  if (modelData != null && modelData != undefined && modelData.length > 0) {
    modelData.forEach(function (nmd) {
      if (nmd.pid == md.id) {
        nmd.children = [];
        // 本级level+1
        nmd.level = md.level + 1;
        findRespModelChildren(nmd, modelData);
        // 查找后如果没有,则将children置空
        if (nmd.children.length == 0) {
          nmd.children = null;
        }
        md.children.push(nmd);
      }
    });
  }
}

/**
 * 单个响应状态
 * @param {*} resp 
 * @param {*} markdownCollections 
 */
function createApiResponseSingleParam(resp, markdownCollections) {
  // 判断是否有响应Header
  createApiResponseHeaderParams(resp.responseHeaderParameters, markdownCollections);
  // 数据
  markdownLines(markdownCollections);
  markdownCollections.push('**Responses**:');
  markdownLines(markdownCollections);
  // 拥有参数
  if (KUtils.arrNotEmpty(resp.data)) {
    // 拥有参数
    markdownCollections.push('| name | description | type | schema |');
    markdownCollections.push('| -------- | -------- | ----- |----- | ');
    resp.data.forEach(function (param) {
      param.level = 1;
      markdownCollections.push('|' + getMdTableByLevel(param) + '|' + KUtils.toString(param.description, '') + '|' + KUtils.toString(param.type, '') + '|' + KUtils.toString(param.schemaValue, '') + '|')
      deepMdTableByResponseParameter(param.children, markdownCollections, (param.level + 1));
    })
  } else {
    markdownCollections.push('None');
  }
  // 判断是否拥有响应示例
  markdownLines(markdownCollections);
  markdownCollections.push('**Response Sample**:');
  if (resp.responseBasicType) {
    markdownCollections.push('```text');
    markdownCollections.push(resp.responseText);
    markdownCollections.push('```');
  } else {
    markdownCollections.push('```javascript');
    markdownCollections.push(resp.responseValue);
    markdownCollections.push('```');
  }

}

/**
 *  递归循环遍历响应参数得到markdown表格
 * @param {*} parameters 
 * @param {*} markdownCollections 
 */
function deepMdTableByResponseParameter(parameters, markdownCollections, level) {
  if (parameters != null && parameters != undefined && parameters.length > 0) {
    parameters.forEach(function (param) {
      param.level = level;
      markdownCollections.push('|' + getMdTableByLevel(param) + '|' + KUtils.toString(param.description, '') + '|' + KUtils.toString(param.type, '') + '|' + KUtils.toString(param.schemaValue, '') + '|')
      deepMdTableByResponseParameter(param.children, markdownCollections, (param.level + 1));
    })
  }

}

/**
 * 响应参数拥有响应头
 * @param {*} responseHeaderParameters 
 * @param {*} markdownCollections 
 */
function createApiResponseHeaderParams(responseHeaderParameters, markdownCollections) {
  if (KUtils.checkUndefined(responseHeaderParameters)) {
    if (responseHeaderParameters.length > 0) {
      markdownLines(markdownCollections);
      markdownCollections.push('**Response Header**:');
      markdownLines(markdownCollections);
      // 拥有参数
      markdownCollections.push('|name | description | type |');
      markdownCollections.push('| -------- | -------- | ----- | ');
      responseHeaderParameters.forEach(function (respHeader) {
        markdownCollections.push('|' + KUtils.toString(respHeader.name, '') + '|' + KUtils.toString(respHeader.description, '') + '|' + KUtils.toString(respHeader.type, '') + '|');
      })
    }
  }
}


/**
 * 递归循环遍历参数得到markdown表格
 * @param {*} parameters 
 * @param {*} markdownCollections 
 */
function deepMdTableByRequestParameter(parameters, markdownCollections, level) {
  if (parameters != null && parameters != undefined && parameters.length > 0) {
    parameters.forEach(function (param) {
      // 赋值一个level
      param.level = level;
      markdownCollections.push('|' + getMdTableByLevel(param) + '|' + KUtils.toString(param.description, '') + '|' + KUtils.toString(param.in, '') + '|' + KUtils.toString(param.require, '') + '|' + KUtils.toString(param.type, '') + '|' + KUtils.toString(param.schemaValue, '') + '|')
      deepMdTableByRequestParameter(param.children, markdownCollections, (param.level + 1));
    })
  }

}


/**
 * 根据参数级别获取名称
 * @param {*} param 
 */
function getMdTableByLevel(param) {
  var spaceArr = [];
  for (var i = 1; i < param.level; i++) {
    spaceArr.push('&emsp;&emsp;')
  }
  var tmpName = spaceArr.join('') + param.name;
  return tmpName;
}

/**
 * 递归遍历子元素
 * @param {*} md 
 * @param {*} modelData 
 */
function findModelChildren(md, modelData) {
  if (modelData != null && modelData != undefined && modelData.length > 0) {
    modelData.forEach(function (nmd) {
      if (nmd.pid == md.id) {
        nmd.children = [];
        findModelChildren(nmd, modelData);
        // 查找后如果没有,则将children置空
        if (nmd.children.length == 0) {
          nmd.children = null;
        }
        md.children.push(nmd);
      }
    });
  }
}
