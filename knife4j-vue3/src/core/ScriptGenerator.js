import {
  builders as t,
} from "ast-types";
import generate from "@babel/generator";

/**
 * 首字母大写
 * @param {string} str
 * @returns
 */
 export function upperFirstCase(str) {
  const temp = str.split('');
  temp[0] = temp[0].toUpperCase();
  return temp.join('');
}

/**
 *  当url含有路径参数时候获取url数组
 * @param {string} url
 */
 export function getUrls(url) {
  const arr = url.split('{');
  const res = arr.map(u => {
    if(u.includes('}')) {
      return u.split('}')[1]
    }
    return u
  })
  return res;
}

/**
 * 获取字符串模板路径
 * @param {string} path
 */
function getTemplateElement(path) {
  return t.templateElement({ raw: path, cooked: path }, true)
}

/**
 * 构建request请求的路径参数
 * @param {array} urls
 * @param {array} pathParams
 * @param {array} queryParams
 */
export function getTemplateUrl(urls, pathParams, queryParams) {
  let templateUrl = null;
  const quasis = [];
  const expressions = [];
  // 既不存在path也不存在query参数时
  if(pathParams.length === 0 && queryParams.length === 0) {
    urls.map(u => quasis.push(getTemplateElement(u)));
  }
  // 存在path但不存在query参数时
  else if(pathParams.length > 0 && queryParams.length === 0) {
    urls.map(u => quasis.push(getTemplateElement(u)));
    pathParams.map(p => expressions.push(t.identifier(p.name)));
  }
  // 存在query但不存在path参数时
  else if(pathParams.length === 0 && queryParams.length > 0) {
    // 针对第一项的query特殊处理 /url/path?name=name
    quasis.push(getTemplateElement(`${urls[0]}?${queryParams[0].name}=`));
    expressions.push(t.identifier(queryParams[0].name));
    // 第一项以后的处理方式
    queryParams.map((query, index) => {
      if(index > 0) {
        quasis.push(getTemplateElement(`&${query.name}=`));
        expressions.push(t.identifier(query.name));
      }
    })
    quasis.push(getTemplateElement('')); // 结尾 必须
  }
  // 既存在path也存在query时
  else {
    urls.map((u, i) => {
      // 针对最后一个url特殊处理
      if(i + 1 === urls.length) {
        quasis.push(getTemplateElement(`${u}?${queryParams[0].name}=`));
      } else {
        quasis.push(getTemplateElement(u));
      }
    });
    pathParams.map(p => expressions.push(t.identifier(p.name)));
    expressions.push(t.identifier(queryParams[0].name));
    queryParams.map((q, i) =>{
      if(i > 0) {
        quasis.push(getTemplateElement(`&${q.name}=`));
        expressions.push(t.identifier(q.name));
      }
    })
    quasis.push(getTemplateElement('')); // 结尾 必须
  }
  templateUrl = t.templateLiteral(quasis, expressions);
  return templateUrl;
}

/**
 * 判断对象是否闭环，并获取闭环的属性名
 * @param {object} obj
 * @returns
 */
export function isLoopObject(obj) {
  let res = '';
  try {
    JSON.stringify(obj);
  }
  catch(err){
    res = err;
  }
  const resStr = res.toString();
  return {
    isLoop: resStr.indexOf('circular') === -1 ? false : true,
    loopPro: resStr && resStr.match(/property '(\S*)'/mg)[0].replace('property \'', '').replace('\'', '')
  }
}

/**
 * 获取响应参数
 * @param {object} api
 */
function getResParams(resParams, resRefParams) {
  const params = [];
  resParams.map(res => {
    const schema = resRefParams.filter(ref => ref.name === res.schemaValue)[0];
    if(schema) {
      res.children = schema.params
      getResParams(res.children, resRefParams)
    }
    params.push(res);
  })
  return params;
}

/**
 * 获取函数配置
 * @param {obj} api
 * @returns
 */
export function formatApi(api) {
  // console.log(api);
  const config = {
    name: api.operationId.split('Using')[0], // 函数名称
    method: api.methodType.toLowerCase(), // http方法;
    url: api.url,
    pathParams: [], // path请求参数
    queryParams: [], // query请求参数
    bodyParams: [], // body请求参数
    resParam: {}, // res响应参数
  };
  if(Array.isArray(api.parameters)) {
    api.parameters.map(p => {
      switch(p.in) {
        case 'path':
          config.pathParams.push({
            name: p.name,
            type: getBaseType(p.type)
          });
          break;
        case 'query':
          config.queryParams.push({
            name: p.name,
            type:  getBaseType(p.type)
          });
          break;
        default:
          config.bodyParams.push({
            name: 'params',
            type:  p.type === 'array' ? 'array' : 'object',
            children: p.children
          });
          break;
      }
    })
  }
  if(Array.isArray(api.responseCodes)) {
    // let loopObj = {}; // 被循环引用的对象
    // const resRefParamsTemp = api.responseRefParameters.map(ref =>
    //   ref.params && ref.params.map(param => {
    //     const { isLoop, loopPro } = isLoopObject(param);
    //     if(isLoop) {
    //       // loopObj = param[loopPro];
    //       param[loopPro] = null; // 解除循环应用
    //     }
    //     return param;
    //   })
    // )
    let type = '';
    if(api.responseJson instanceof Array) {
      type = 'array';
    } else if(api.responseJson instanceof Object) {
      type = 'object';
    }
    // else {
    //   throw new Error('类型不正确')
    // }
    config.resParam = {
      name: 'res',
      type,
      children: getResParams(api.responseParameters, api.responseRefParameters)
    };
  }
  return config
}

/**
 * 获取基本类型
 * @param {string} type
 */
export function getBaseType(type) {
  let res = null;
  switch(type) {
    case 'string':
      res = 'string'
      break;
    case 'boolean':
      res = 'boolean';
      break;
    case 'number':
      res = 'number';
      break;
    case 'array':
      res = 'array';
      break;
    case 'object':
      res = 'object';
      break;
    case 'integer(int32)':
      res = 'number';
      break;
    case 'integer(int64)':
      res = 'number';
      break;
    default:
      res = 'object';
      break;
  }
  return res;
}

/**
 * 获取引用类型
 * @param {string} name
 * @returns
 */
function getReferenceTSType (name) {
  return t.tsTypeReference(t.identifier(name))
}

/**
 * 获取Record<string, unknown>泛型
 * @returns
 */
function getRecordAny() {
  return t.tsTypeReference(
    t.identifier('Record'),
    t.tsTypeParameterInstantiation([
      t.tsStringKeyword(),
      t.tsUnknownKeyword()
    ])
  )
}

/**
 * 获取泛型body参数
 * @param {array} props
 * @param {boolean} openOptional
 */
export function getInterfaceBody(props, openOptional) {
 return props.map(p => {
    return t.tsPropertySignature(
      t.identifier(p.name),
      t.tsTypeAnnotation(getTsType(p, getBaseType(p.type), openOptional)),
      openOptional ? !p.require : false,
    )
  })
}

/**
 * 获取ts类型
 * @param {object} prop
 * @param {string} type
 * @param {boolean} openOptional
 */
export function getTsType(prop, type, openOptional) {
  const baseType = ['boolean', 'string', 'number'];
  if (baseType.includes(type)) {
    return getReferenceTSType(type);
  }
  if (type === 'object') {
    if(prop.children) {
      const InterfaceBody = getInterfaceBody(prop.children, openOptional);
      return t.tsTypeLiteral(InterfaceBody);
    } else {
      return getRecordAny();
    }
  }
  if (type === 'array') {
    if(prop.children) {
      const InterfaceBody = getInterfaceBody(prop.children, openOptional);
      return t.tsArrayType(t.tsTypeLiteral(InterfaceBody));
    } else {
      return t.tsArrayType(getRecordAny());
    }
  }
  return getReferenceTSType(type);
}

/**
 * 获取子属性注释
 * @param {object} common
 * @param {object} data
 * @param {string} parent
 * @returns
 */
function getChildCommon(common, data, parent) {
  if(Array.isArray(data)) {
    data.map(child=>{
      common += ` * @param {${getBaseType(child.type)}} ${parent}.${child.name} ${child.description}\n`
      getChildCommon(common, child.children, `${parent}.${child.name}`)
    })
  }
  return common
}

/**
 * 获取注释
 * @param {object} api
 * @returns
 */
export function getComment(api) {
  const { summary, parameters } = api;
  let common = ``;
  // 构造函数注释
  common += `/** \n`
  common += ` * ${summary}\n`
  Array.isArray(parameters) && parameters.map(parameter => {
    if(parameter.in === 'path') {
      common += ` * @param {string} ${parameter.name} ${parameter.description}\n `
    }
    if(parameter.in === 'query') {
      common += ` * @param {string} ${parameter.name} ${parameter.description}\n `
    }
    if(parameter.in === 'body') {
      if(parameter.def && parameter.def.type === 'object' || parameter.type === "object") {
        common += ` * @param {object} params ${parameter.description}\n`
        common = getChildCommon(common, parameter.children, 'params')
      } else if(parameter.def && parameter.def.type === 'array'|| parameter.type === "array") {
        common += ` * @param {array} params ${parameter.description}\n`
      }
    }
  })
  common += ` * @returns\n`
  common += ` */\n`
  return common;
}

/**
 * 获取类型注释
 * @param {object} param
 * @returns
 */
export function getTypeAnnotation (param) {
  // url参数取name，body参数取data,query参数取params
  let arg = t.identifier(param.name);
  // 缺少ref类型转化
  arg.typeAnnotation = t.typeAnnotation(
    t.genericTypeAnnotation(
      t.identifier(param.type),
      null
    ),
  );
  return arg;
}

/**
 * js函数构造器
 * @param {object} api
 * @returns
 */
export function getJsFunctionDeclaration(api) {
  const {
    name,
    method,
    url,
    pathParams,
    queryParams,
    bodyParams,
  } = api;

  const funParams = [];
  const requestParams = [];
  // 构建request请求的路径参数
  requestParams.push(getTemplateUrl(getUrls(url), pathParams, queryParams));
  [].concat(pathParams, queryParams).map(param => {
    funParams.push(t.identifier(param.name));
  })
  bodyParams.map(param => {
    const temp = t.identifier(param.name);
    funParams.push(temp);
    requestParams.push(temp);
  })
  const declaration = t.functionDeclaration(
    t.identifier(name),
    funParams,
    t.blockStatement([
      t.returnStatement(
        t.callExpression( // 方法调用
          t.memberExpression(
            t.identifier('request'),
            t.identifier(method),
          ),
          requestParams,
        )
      )
    ]))
  return generate(t.exportNamedDeclaration(declaration)).code;
}

/**
 * 生成ts接口
 * @param {object} prop
 * @param {string} interfaceName
 * @param {boolean} openOptional 是否开启可选
 * @returns
 */
export function getTsInterfaceDeclaration(prop, interfaceName, openOptional) {
  let declaration = null;
  if(prop.type === 'object') {
    if(!prop.children) return '\n';
    const InterfaceBody = getInterfaceBody(prop.children, openOptional);
    declaration = t.tsInterfaceDeclaration(
      t.identifier(interfaceName),
      t.tsInterfaceBody(InterfaceBody),
    )
  } else if (prop.type === 'array') {
    if(!prop.children) return '\n';
    const InterfaceBody = getInterfaceBody(prop.children, openOptional);
    declaration = t.tsInterfaceDeclaration(
      t.identifier(interfaceName),
      t.tsInterfaceBody(InterfaceBody),
    )
  } else {
    declaration = t.tsInterfaceDeclaration(
      t.identifier(interfaceName),
      t.tsInterfaceBody([]),
    )
  }
  return generate(t.exportNamedDeclaration(declaration)).code + '\n\n';
}

/**
 * ts函数构造器
 * @param {object} api
 * @param {string} interfaceName
 * @returns
 */
 export function getTsFunctionDeclaration(api, interfaceName) {
  const {
    name,
    method,
    url,
    pathParams,
    queryParams,
    bodyParams,
    resParam,
  } = api;

  const funParams = [];
  const requestParams = [];
  // 构建request请求的路径参数
  requestParams.push(getTemplateUrl(getUrls(url), pathParams, queryParams));
  [].concat(pathParams, queryParams).map(param => {
    funParams.push(getTypeAnnotation(param));
  })
  bodyParams.map(param => {
    // 指定接口类型名称
    if(param.type === 'array') {
      funParams.push(getTypeAnnotation({name: param.name, type: `any[]`}));
    } else if(param.type === 'object') {
      funParams.push(getTypeAnnotation({name: param.name, type: `${interfaceName}Params`}));
    }
    requestParams.push(t.identifier(param.name));
  })
  const declaration = t.functionDeclaration(
    t.identifier(name),
    funParams,
    t.blockStatement([
      t.returnStatement(
        t.callExpression( // 方法调用
          t.memberExpression(
            t.identifier('request'),
            t.identifier(method),
          ),
          requestParams,
        )
      )
    ])
  )
  const resType = resParam.type === 'array' ? `${interfaceName}Res[]` : `${interfaceName}Res`;
  declaration.returnType = t.tsTypeAnnotation(
    t.tsTypeReference(
      t.identifier('Promise'),
      t.tsTypeParameterInstantiation( //泛型
        [t.tsTypeReference(
          t.identifier(resType),
        )]
      )
    )
  )
  return generate(t.exportNamedDeclaration(declaration)).code;
}
