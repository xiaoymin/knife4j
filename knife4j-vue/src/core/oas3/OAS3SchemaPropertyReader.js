import KUtils from '../utils'
import OpenAPI from '../oas/OpenAPI'

import { Knife4jOAS2AdditionalModelClassFinder } from '../oas2/OAS2AdditionalModel'
import { SwaggerBootstrapUiParameter } from '../oas/SwaggerBootstrapUiParameter'
import SwaggerBootstrapUiProperty from '../oas/SwaggerBootstrapUiProperty'
/**
 * OAS3规范SchemaReader对象
 * 参考规范：https://spec.openapis.org/oas/latest.html#schemaObject
 * {
 * "properties": {
          "code": {
            "format": "int32",
            "description": "状态码",
            "type": "integer"
          },
          "msg": {
            "format": "string",
            "description": "消息",
            "type": "string"
          }
    }
 * }
 * @param {*} schema 当前Schema对象实例信息
 * @param {*} instance 当前SwaggerBootstrapUi对象实例
 */
function OAS3SchemaPropertyReader(schema) {
  /**
   * 原始实例对象
   */
  this.schema = schema;
}


/**
 * 读取基础properties列表
 * @param {*} swud  SwaggerBootstrapUiDefinition对象实例
 * @param {*} instance  当前SwaggerBootstrapUi对象实例
 */
OAS3SchemaPropertyReader.prototype.readBasicProperty = function (swud, instance, definitions, oas2) {
  var defiTypeValue = {};
  let _tempSchemaObject = this.schema;
  // 是否有properties
  if (_tempSchemaObject.hasOwnProperty('properties')) {
    var properties = _tempSchemaObject['properties'];
    for (var property in properties) {
      var propobj = properties[property];
      // 判断是否包含readOnly属性
      if (!propobj.hasOwnProperty('readOnly') || !propobj['readOnly']) { }
      var spropObj = new SwaggerBootstrapUiProperty();
      // jsr303
      instance.validateJSR303(spropObj, propobj);
      // 赋值readOnly属性
      if (propobj.hasOwnProperty('readOnly')) {
        spropObj.readOnly = propobj['readOnly'];
      }
      spropObj.name = property;
      spropObj.originProperty = propobj;
      spropObj.type = KUtils.propValue('type', propobj, 'string');
      spropObj.description = KUtils.propValue('description', propobj, '');
      // 判断是否包含枚举
      if (propobj.hasOwnProperty('enum')) {
        spropObj.enum = propobj['enum'];
        if (spropObj.description != '') {
          spropObj.description += ',';
        }
        //spropObj.description = spropObj.description + '可用值:' + spropObj.enum.join(',');
        spropObj.description = spropObj.description + KUtils.enumAvalibleLabel(instance.i18nInstance, spropObj.enum);
      }
      if (spropObj.type == 'string') {
        // spropObj.example = String(KUtils.propValue('example', propobj, ''));
        spropObj.example = KUtils.getExample('example', propobj, '');
      } else {
        spropObj.example = KUtils.propValue('example', propobj, '');
      }

      spropObj.format = KUtils.propValue('format', propobj, '');
      spropObj.required = KUtils.propValue('required', propobj, false);
      if (swud.required.length > 0) {
        // 有required属性,需要再判断一次
        if (swud.required.indexOf(spropObj.name) > -1) {
          // if($.inArray(spropObj.name,swud.required)>-1){
          // 存在
          spropObj.required = true;
        }
      }
      // 默认string类型
      var propValue = '';
      // 判断是否有类型
      if (propobj.hasOwnProperty('type')) {
        var type = propobj['type'];
        // 判断是否有example
        // console.log(propobj)
        if (propobj.hasOwnProperty('example')) {
          if (type == 'string') {
            // propValue = String(KUtils.propValue('example', propobj, ''));
            propValue = KUtils.getExample('example', propobj, '');
          } else {
            propValue = propobj['example'];
          }
        } else if (propobj.hasOwnProperty('default')) {
          propValue = KUtils.propValue('default', propobj, '')
        } else if (KUtils.checkIsBasicType(type)) {
          propValue = KUtils.getBasicTypeValue(type);
          // 此处如果是object情况,需要判断additionalProperties属性的情况
          if (type == 'object') {
            if (propobj.hasOwnProperty('additionalProperties')) {
              var addpties = propobj['additionalProperties'];
              instance.log('------解析map-=-----------additionalProperties,defName:' + name);
              // 判断是否additionalProperties中还包含additionalProperties属性
              let addtionalClassFinder = new Knife4jOAS2AdditionalModelClassFinder(addpties, oas2);
              //var addtionalName = this.deepAdditionalProperties(addpties, oas2);
              let addtionalName = addtionalClassFinder.findClassName();
              // console.log('递归类型---'+addtionalName)
              // 判断是否有ref属性,如果有,存在引用类,否则默认是{}object的情况
              if (KUtils.strNotBlank(addtionalName)) {
                // console.log('-------------------------addtionalName--------'+addtionalName)
                // 这里需要递归判断是否是本身,如果是,则退出递归查找
                var globalArr = new Array();
                // 添加类本身
                globalArr.push(name);
                var addTempValue = null;
                if (addtionalName != name) {
                  addTempValue = instance.findRefDefinition(addtionalName, definitions, false, globalArr, null, oas2);
                } else {
                  addTempValue = instance.findRefDefinition(addtionalName, definitions, true, globalArr, name, oas2);
                }
                propValue = {
                  'additionalProperties1': addTempValue
                };
                // console.log(propValue)
                spropObj.type = addtionalName;
                spropObj.refType = addtionalName;
              } else if (addpties.hasOwnProperty('$ref')) {
                var adref = addpties['$ref'];
                var regex = new RegExp(KUtils.oasmodel(oas2), 'ig');
                if (regex.test(adref)) {
                  var addrefType = RegExp.$1;
                  var addTempValue = null;
                  // 这里需要递归判断是否是本身,如果是,则退出递归查找
                  var globalArr = new Array();
                  // 添加类本身
                  globalArr.push(name);

                  if (addrefType != name) {
                    addTempValue = instance.findRefDefinition(addrefType, definitions, false, globalArr, null, oas2);
                  } else {
                    addTempValue = instance.findRefDefinition(addrefType, definitions, true, globalArr, name, oas2);
                  }
                  propValue = {
                    'additionalProperties1': addTempValue
                  };
                  instance.log('解析map-=完毕：');
                  instance.log(propValue);
                  spropObj.type = addrefType;
                  spropObj.refType = addrefType;
                }
              } else if (addpties.hasOwnProperty('items')) {
                // 数组
                var addPropItems = addpties['items'];

                var adref = addPropItems['$ref'];
                var regex = new RegExp(KUtils.oasmodel(oas2), 'ig');
                if (regex.test(adref)) {
                  var addrefType = RegExp.$1;
                  var addTempValue = null;
                  // 这里需要递归判断是否是本身,如果是,则退出递归查找
                  var globalArr = new Array();
                  // 添加类本身
                  globalArr.push(name);

                  if (addrefType != name) {
                    addTempValue = instance.findRefDefinition(addrefType, definitions, false, globalArr, null, oas2);
                  } else {
                    addTempValue = instance.findRefDefinition(addrefType, definitions, true, globalArr, name, oas2);
                  }
                  var tempAddValue = new Array();
                  tempAddValue.push(addTempValue);
                  propValue = {
                    'additionalProperties1': tempAddValue
                  };
                  instance.log('解析map-=完毕：');
                  instance.log(propValue);
                  spropObj.type = 'array';
                  spropObj.refType = addrefType;
                }
              }
            }
          }
        } else {
          if (type == 'array') {
            propValue = new Array();
            var items = propobj['items'];
            var ref = items['$ref'];
            // 此处有可能items是array
            if (items.hasOwnProperty('type')) {
              if (items['type'] == 'array') {
                ref = items['items']['$ref'];
              }
            }
            // 判断是否存在枚举
            if (items.hasOwnProperty('enum')) {
              if (spropObj.description != '') {
                spropObj.description += ',';
              }
              //spropObj.description = spropObj.description + '可用值:' + items['enum'].join(',');
              spropObj.description = spropObj.description + KUtils.enumAvalibleLabel(instance.i18nInstance, items['enum']);
            }
            var regex = new RegExp(KUtils.oasmodel(oas2), 'ig');
            if (regex.test(ref)) {
              var refType = RegExp.$1;
              spropObj.refType = refType;
              // 这里需要递归判断是否是本身,如果是,则退出递归查找
              var globalArr = new Array();
              // 添加类本身
              globalArr.push(name);
              if (refType != name) {
                propValue.push(instance.findRefDefinition(refType, definitions, false, globalArr, null, oas2));
              } else {
                propValue.push(instance.findRefDefinition(refType, definitions, true, globalArr, name, oas2));
              }
            } else {
              // schema基础类型显示
              spropObj.refType = items['type'];
            }
          }
        }

      } else {
        // instance.log('解析属性：'+property);
        // instance.log(propobj);
        if (propobj.hasOwnProperty('$ref')) {
          var ref = propobj['$ref'];
          var regex = new RegExp(KUtils.oasmodel(oas2), 'ig');
          if (regex.test(ref)) {
            var refType = RegExp.$1;
            spropObj.refType = refType;
            // 这里需要递归判断是否是本身,如果是,则退出递归查找
            var globalArr = new Array();
            // 添加类本身
            globalArr.push(name);
            if (refType != name) {
              propValue = instance.findRefDefinition(refType, definitions, false, globalArr, null, oas2);
            } else {
              propValue = instance.findRefDefinition(refType, definitions, true, globalArr, null, oas2);
            }

          }
        } else {
          propValue = {};
        }
      }
      spropObj.value = propValue;
      // 判断是否有format,如果是integer,判断是64位还是32位
      if (spropObj.format != null && spropObj.format != undefined && spropObj.format != '') {
        // spropObj.type=spropObj.format;
        spropObj.type += '(' + spropObj.format + ')';
      }
      // 判断最终类型
      if (spropObj.refType != null && spropObj.refType != '') {
        // 判断基础类型,非数字类型
        if (spropObj.type == 'string') {
          spropObj.type = spropObj.refType;
        }
      }
      // addprop
      // 这里判断去重
      if (!instance.checkPropertiesExists(swud.properties, spropObj)) {
        swud.properties.push(spropObj);
        // 如果当前属性readOnly=true，则实体类value排除此属性的值
        if (!spropObj.readOnly) {
          defiTypeValue[property] = propValue;
        }
      }
    }
    // console.log('proValue:', defiTypeValue)
    swud.value = defiTypeValue;
  }
}



/**
 * 读取当前对象属性properties列表
 * @param {*} swud SwaggerBootstrapUiDefinition对象实例
 * @param {*} instance 当前SwaggerBootstrapUi对象实例
 */
OAS3SchemaPropertyReader.prototype.readProperty = function (originalSchema, swud, instance) {
  var defiTypeValue = {};
  let _tempSchemaObject = this.schema;
  if (KUtils.checkUndefined(originalSchema)) {
    _tempSchemaObject = originalSchema;
  }
  // 是否有properties
  if (_tempSchemaObject.hasOwnProperty('properties')) {
    var properties = value['properties'];
    for (var property in properties) {
      var propobj = properties[property];
      // 判断是否包含readOnly属性
      if (!propobj.hasOwnProperty('readOnly') || !propobj['readOnly']) { }
      var spropObj = new SwaggerBootstrapUiProperty();
      // jsr303
      instance.validateJSR303(spropObj, propobj);
      // 赋值readOnly属性
      if (propobj.hasOwnProperty('readOnly')) {
        spropObj.readOnly = propobj['readOnly'];
      }
      spropObj.name = property;
      spropObj.originProperty = propobj;
      spropObj.type = KUtils.propValue('type', propobj, 'string');
      spropObj.description = KUtils.propValue('description', propobj, '');
      // 判断是否包含枚举
      if (propobj.hasOwnProperty('enum')) {
        spropObj.enum = propobj['enum'];
        if (spropObj.description != '') {
          spropObj.description += ',';
        }
        //spropObj.description = spropObj.description + '可用值:' + spropObj.enum.join(',');
        spropObj.description = spropObj.description + KUtils.enumAvalibleLabel(instance.i18nInstance, spropObj.enum);
      }
      if (spropObj.type == 'string') {
        // spropObj.example = String(KUtils.propValue('example', propobj, ''));
        spropObj.example = KUtils.getExample('example', propobj, '');
      } else {
        spropObj.example = KUtils.propValue('example', propobj, '');
      }

      spropObj.format = KUtils.propValue('format', propobj, '');
      spropObj.required = KUtils.propValue('required', propobj, false);
      if (swud.required.length > 0) {
        // 有required属性,需要再判断一次
        if (swud.required.indexOf(spropObj.name) > -1) {
          // if($.inArray(spropObj.name,swud.required)>-1){
          // 存在
          spropObj.required = true;
        }
      }
      // 默认string类型
      var propValue = '';
      // 判断是否有类型
      if (propobj.hasOwnProperty('type')) {
        var type = propobj['type'];
        // 判断是否有example
        // console.log(propobj)
        if (propobj.hasOwnProperty('example')) {
          if (type == 'string') {
            // propValue = String(KUtils.propValue('example', propobj, ''));
            propValue = KUtils.getExample('example', propobj, '');
          } else {
            propValue = propobj['example'];
          }
        } else if (propobj.hasOwnProperty('default')) {
          propValue = KUtils.propValue('default', propobj, '')
        } else if (KUtils.checkIsBasicType(type)) {
          propValue = KUtils.getBasicTypeValue(type);
          // 此处如果是object情况,需要判断additionalProperties属性的情况
          if (type == 'object') {
            if (propobj.hasOwnProperty('additionalProperties')) {
              var addpties = propobj['additionalProperties'];
              instance.log('------解析map-=-----------additionalProperties,defName:' + name);
              // 判断是否additionalProperties中还包含additionalProperties属性
              let addtionalClassFinder = new Knife4jOAS2AdditionalModelClassFinder(addpties, oas2);
              //var addtionalName = this.deepAdditionalProperties(addpties, oas2);
              let addtionalName = addtionalClassFinder.findClassName();
              // console.log('递归类型---'+addtionalName)
              // 判断是否有ref属性,如果有,存在引用类,否则默认是{}object的情况
              if (KUtils.strNotBlank(addtionalName)) {
                // console.log('-------------------------addtionalName--------'+addtionalName)
                // 这里需要递归判断是否是本身,如果是,则退出递归查找
                var globalArr = new Array();
                // 添加类本身
                globalArr.push(name);
                var addTempValue = null;
                if (addtionalName != name) {
                  addTempValue = instance.findRefDefinition(addtionalName, definitions, false, globalArr, null, oas2);
                } else {
                  addTempValue = instance.findRefDefinition(addtionalName, definitions, true, globalArr, name, oas2);
                }
                propValue = {
                  'additionalProperties1': addTempValue
                };
                // console.log(propValue)
                spropObj.type = addtionalName;
                spropObj.refType = addtionalName;
              } else if (addpties.hasOwnProperty('$ref')) {
                var adref = addpties['$ref'];
                var regex = new RegExp(KUtils.oasmodel(oas2), 'ig');
                if (regex.test(adref)) {
                  var addrefType = RegExp.$1;
                  var addTempValue = null;
                  // 这里需要递归判断是否是本身,如果是,则退出递归查找
                  var globalArr = new Array();
                  // 添加类本身
                  globalArr.push(name);

                  if (addrefType != name) {
                    addTempValue = instance.findRefDefinition(addrefType, definitions, false, globalArr, null, oas2);
                  } else {
                    addTempValue = instance.findRefDefinition(addrefType, definitions, true, globalArr, name, oas2);
                  }
                  propValue = {
                    'additionalProperties1': addTempValue
                  };
                  instance.log('解析map-=完毕：');
                  instance.log(propValue);
                  spropObj.type = addrefType;
                  spropObj.refType = addrefType;
                }
              } else if (addpties.hasOwnProperty('items')) {
                // 数组
                var addPropItems = addpties['items'];

                var adref = addPropItems['$ref'];
                var regex = new RegExp(KUtils.oasmodel(oas2), 'ig');
                if (regex.test(adref)) {
                  var addrefType = RegExp.$1;
                  var addTempValue = null;
                  // 这里需要递归判断是否是本身,如果是,则退出递归查找
                  var globalArr = new Array();
                  // 添加类本身
                  globalArr.push(name);

                  if (addrefType != name) {
                    addTempValue = instance.findRefDefinition(addrefType, definitions, false, globalArr, null, oas2);
                  } else {
                    addTempValue = instance.findRefDefinition(addrefType, definitions, true, globalArr, name, oas2);
                  }
                  var tempAddValue = new Array();
                  tempAddValue.push(addTempValue);
                  propValue = {
                    'additionalProperties1': tempAddValue
                  };
                  instance.log('解析map-=完毕：');
                  instance.log(propValue);
                  spropObj.type = 'array';
                  spropObj.refType = addrefType;
                }
              }
            }
          }
        } else {
          if (type == 'array') {
            propValue = new Array();
            var items = propobj['items'];
            var ref = items['$ref'];
            // 此处有可能items是array
            if (items.hasOwnProperty('type')) {
              if (items['type'] == 'array') {
                ref = items['items']['$ref'];
              }
            }
            // 判断是否存在枚举
            if (items.hasOwnProperty('enum')) {
              if (spropObj.description != '') {
                spropObj.description += ',';
              }
              //spropObj.description = spropObj.description + '可用值:' + items['enum'].join(',');
              spropObj.description = spropObj.description + KUtils.enumAvalibleLabel(instance.i18nInstance, items['enum']);
            }
            var regex = new RegExp(KUtils.oasmodel(oas2), 'ig');
            if (regex.test(ref)) {
              var refType = RegExp.$1;
              spropObj.refType = refType;
              // 这里需要递归判断是否是本身,如果是,则退出递归查找
              var globalArr = new Array();
              // 添加类本身
              globalArr.push(name);
              if (refType != name) {
                propValue.push(instance.findRefDefinition(refType, definitions, false, globalArr, null, oas2));
              } else {
                propValue.push(instance.findRefDefinition(refType, definitions, true, globalArr, name, oas2));
              }
            } else {
              // schema基础类型显示
              spropObj.refType = items['type'];
            }
            //有可能存在properties属性
            if (items.hasOwnProperty("properties")) {

            }
          }
        }

      } else {
        // instance.log('解析属性：'+property);
        // instance.log(propobj);
        if (propobj.hasOwnProperty('$ref')) {
          var ref = propobj['$ref'];
          var regex = new RegExp(KUtils.oasmodel(oas2), 'ig');
          if (regex.test(ref)) {
            var refType = RegExp.$1;
            spropObj.refType = refType;
            // 这里需要递归判断是否是本身,如果是,则退出递归查找
            var globalArr = new Array();
            // 添加类本身
            globalArr.push(name);
            if (refType != name) {
              propValue = instance.findRefDefinition(refType, definitions, false, globalArr, null, oas2);
            } else {
              propValue = instance.findRefDefinition(refType, definitions, true, globalArr, null, oas2);
            }

          }
        } else {
          propValue = {};
        }
      }
      spropObj.value = propValue;
      // 判断是否有format,如果是integer,判断是64位还是32位
      if (spropObj.format != null && spropObj.format != undefined && spropObj.format != '') {
        // spropObj.type=spropObj.format;
        spropObj.type += '(' + spropObj.format + ')';
      }
      // 判断最终类型
      if (spropObj.refType != null && spropObj.refType != '') {
        // 判断基础类型,非数字类型
        if (spropObj.type == 'string') {
          spropObj.type = spropObj.refType;
        }
      }
      // addprop
      // 这里判断去重
      if (!instance.checkPropertiesExists(swud.properties, spropObj)) {
        swud.properties.push(spropObj);
        // 如果当前属性readOnly=true，则实体类value排除此属性的值
        if (!spropObj.readOnly) {
          defiTypeValue[property] = propValue;
        }
      }
    }
    // console.log('proValue:', defiTypeValue)
    swud.value = defiTypeValue;
  }
}


export default OAS3SchemaPropertyReader;