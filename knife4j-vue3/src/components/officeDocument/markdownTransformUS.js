import KUtils from '@/core/utils'

/**
 * 根据当前swagger分组实例得到当前组下markdown纯文本
 * @param {*} instance 当前对象实例
 */
export default function markdownTextUS(instance) {
  var markdownCollections = [];
  if (instance != null && instance != undefined) {
    createBasicInfo(instance, markdownCollections);
    createTagsInfo(instance, markdownCollections);
    // 增强文档
    createPlusInfo(instance, markdownCollections);
  }
  return markdownCollections.join('\n');
}

/**
 * 主动换行
 * @param {*} markdownCollections  markdown集合
 */
function markdownLines(markdownCollections) {
  markdownCollections.push('\n');
}

/**
 * 基本信息
 * @param {*} instance 当前分组实例对象
 * @param {*} markdownCollections markdown文本集合对象
 */
function createBasicInfo(instance, markdownCollections) {
  markdownCollections.push('# ' + instance.title);
  markdownLines(markdownCollections);
  markdownCollections.push('**Description**:' + instance.description);
  markdownLines(markdownCollections);
  markdownCollections.push('**HOST**:' + instance.host);
  markdownLines(markdownCollections);
  markdownCollections.push('**Contacts**:' + instance.contact);
  markdownLines(markdownCollections);
  markdownCollections.push('**Version**:' + instance.version);
  markdownLines(markdownCollections);
  markdownCollections.push('**URL**:' + instance.url);
  markdownLines(markdownCollections);
  // 第三方md软件Typora目录格式
  markdownCollections.push('[TOC]');
  markdownLines(markdownCollections);
}

/**
 * 增加增强文档
 * @param {*} instance 当前分组实例对象
 * @param {*} markdownCollections markdown文本集合对象
 */
function createPlusInfo(instance, markdownCollections) {
  if (KUtils.checkUndefined(instance.markdownFiles)) {
    if (instance.markdownFiles.length > 0) {
      markdownLines(markdownCollections);
      markdownCollections.push('# appendix');
      instance.markdownFiles.forEach(function (md) {
        markdownLines(markdownCollections);
        // 判断是否包含children
        if (KUtils.arrNotEmpty(md.children)) {
          markdownCollections.push('## ' + md.name);
          markdownLines(markdownCollections);

          md.children.forEach(mdfile => {
            markdownCollections.push('### ' + mdfile.title);
            markdownCollections.push(mdfile.content);
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
function createTagsInfo(instance, markdownCollections) {
  if (instance.tags != undefined && instance.tags != null) {
    markdownCollections.push('\n');
    instance.tags.forEach(function (tag) {
      markdownLines(markdownCollections);
      markdownCollections.push('# ' + tag.name)
      if (tag.childrens != undefined && tag.childrens != null && tag.childrens.length > 0) {
        // 遍历
        tag.childrens.forEach(function (apiInfo) {
          createApiInfo(apiInfo, markdownCollections);
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
function createApiInfo(apiInfo, markdownCollections) {
  // 二级标题
  markdownLines(markdownCollections);
  markdownCollections.push('## ' + apiInfo.summary);
  markdownLines(markdownCollections);
  markdownCollections.push('**url**:`' + apiInfo.showUrl + '`');
  markdownLines(markdownCollections);
  markdownCollections.push('**method**:`' + apiInfo.methodType + '`');
  markdownLines(markdownCollections);
  markdownCollections.push('**produces**:`' + KUtils.toString(apiInfo.consumes, '*') + '`');
  markdownLines(markdownCollections);
  markdownCollections.push('**consumes**:`' + KUtils.toString(apiInfo.produces, '*') + '`');
  markdownLines(markdownCollections);
  if (KUtils.strNotBlank(apiInfo.author)) {
    markdownCollections.push('**author**:' + KUtils.toString(apiInfo.author, '暂无') + '');
    markdownLines(markdownCollections);
  }
  markdownCollections.push('**Note**:' + KUtils.toString(apiInfo.description, '暂无') + '');
  // 判断是否有请求示例
  if (KUtils.checkUndefined(apiInfo.requestValue)) {
    markdownLines(markdownCollections);
    markdownCollections.push('**Example**:');
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
/**
 * 响应状态
 * @param {*} apiInfo 接口对象信息
 * @param {*} markdownCollections markdown文本集合对象
 */
function createApiResponseStatus(apiInfo, markdownCollections) {
  if (KUtils.checkUndefined(apiInfo.responseCodes) && apiInfo.responseCodes.length > 0) {
    markdownLines(markdownCollections);
    markdownCollections.push('**Status**:');
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
 * 响应参数拥有响应头
 * @param {*} responseHeaderParameters  响应头参数
 * @param {*} markdownCollections markdown文本集合对象
 */
function createApiResponseHeaderParams(responseHeaderParameters, markdownCollections) {
  if (KUtils.checkUndefined(responseHeaderParameters)) {
    if (responseHeaderParameters.length > 0) {
      markdownLines(markdownCollections);
      markdownCollections.push('**Response Header**:');
      markdownLines(markdownCollections);
      // 拥有参数
      markdownCollections.push('| name | description | type |');
      markdownCollections.push('| -------- | -------- | ----- | ');
      responseHeaderParameters.forEach(function (respHeader) {
        markdownCollections.push('|' + KUtils.toString(respHeader.name, '') + '|' + KUtils.toString(respHeader.description, '') + '|' + KUtils.toString(respHeader.type, '') + '|');
      })
    }
  }
}

/**
 * 响应参数
 * @param {*} apiInfo 接口对象信息
 * @param {*} markdownCollections markdown文本集合对象
 * @param {*} singleFlag 是否单个schema
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
 * 单个响应状态
 * @param {*} resp 响应对象
 * @param {*} markdownCollections markdown文本集合对象
 */
function createApiResponseSingleParam(resp, markdownCollections) {
  // 判断是否有响应Header
  createApiResponseHeaderParams(resp.responseHeaderParameters, markdownCollections);
  // 数据 
  markdownLines(markdownCollections);
  markdownCollections.push('**Response Params**:');
  markdownLines(markdownCollections);
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
  markdownCollections.push('**Response Example**:');
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
 * 请求参数
 * @param {*} apiInfo 接口信息
 * @param {*} markdownCollections markdown文本集合对象
 */
function createApiRequestParameters(apiInfo, markdownCollections) {

  let reqParameters = apiInfo.reqParameters;
  markdownLines(markdownCollections);
  markdownCollections.push('**Params**:');
  // 判断是否拥有请求参数
  if (KUtils.arrNotEmpty(reqParameters)) {
    markdownLines(markdownCollections);
    // 拥有参数
    markdownCollections.push('| name | description | in    | require | type | schema |');
    markdownCollections.push('| -------- | -------- | ----- | -------- | -------- | ------ |');
    // 级联表格，在表格需要最佳空格缩进符号
    deepMdTableByRequestParameter(reqParameters, markdownCollections, 1);
  } else {
    markdownLines(markdownCollections);
    markdownCollections.push('暂无');
  }
}

/**
 *  递归循环遍历响应参数得到markdown表格
 * @param {*} parameters 参数
 * @param {*} markdownCollections markdown文本集合对象
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
 * 递归循环遍历参数得到markdown表格
 * @param {*} parameters 参数
 * @param {*} markdownCollections markdown文本集合对象
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
 * @param {*} param 参数
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
 * @param {*} md markdown对象
 * @param {*} modelData model数据对象
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

/**
 * 响应参数递归遍历参数
 * @param {*} md markdown对象
 * @param {*} modelData model数据对象
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
