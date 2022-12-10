import KUtils from '../utils'
import { Knife4jOAS2AdditionalModelClassFinder } from '../oas2/OAS2AdditionalModel'


/**
 * OpenAPI基础工具包
 */
const OpenAPI = {
    /**
     * 获取当前Swagger对象类型名称
     * @param {*} propobj 
     * @param {*} oas2 
     */
    getSwaggerModelRefType(propobj, oas2) {
        var refType = null;
        if (propobj.hasOwnProperty('type')) {
            var type = propobj['type'];
            // 判断是否有example
            if (KUtils.checkIsBasicType(type)) {
                // 此处如果是object情况,需要判断additionalProperties属性的情况
                if (type == 'object') {
                    if (propobj.hasOwnProperty('additionalProperties')) {
                        var addpties = propobj['additionalProperties'];
                        // 判断是否additionalProperties中还包含additionalProperties属性
                        let addtionalClassFinder = new Knife4jOAS2AdditionalModelClassFinder(addpties, oas2);
                        //var addtionalName = this.deepAdditionalProperties(addpties, oas2);
                        let addtionalName = addtionalClassFinder.findClassName();
                        // console.log('递归类型---'+addtionalName)
                        // 判断是否有ref属性,如果有,存在引用类,否则默认是{}object的情况
                        if (KUtils.strNotBlank(addtionalName)) {
                            refType = addtionalName;
                        } else if (addpties.hasOwnProperty('$ref')) {
                            var adref = addpties['$ref'];
                            var regex = new RegExp(KUtils.oasmodel(oas2), 'ig');
                            if (regex.test(adref)) {
                                refType = RegExp.$1;
                            }
                        } else if (addpties.hasOwnProperty('items')) {
                            // 数组
                            var addPropItems = addpties['items'];

                            var adref = addPropItems['$ref'];
                            var regex = new RegExp(KUtils.oasmodel(oas2), 'ig');
                            if (regex.test(adref)) {
                                refType = RegExp.$1;
                            }
                        }
                    } else {
                        refType = type;
                    }
                }
            } else {
                if (type == 'array') {
                    var items = propobj['items'];
                    if (KUtils.checkUndefined(items)) {
                        var ref = items['$ref'];
                        // 此处有可能items是array
                        if (items.hasOwnProperty('type')) {
                            if (items['type'] == 'array') {
                                ref = items['items']['$ref'];
                            }
                        }
                        var regex = new RegExp(KUtils.oasmodel(oas2), 'ig');
                        if (regex.test(ref)) {
                            refType = RegExp.$1;
                        } else {
                            // schema基础类型显示
                            refType = items['type'];
                        }
                    }
                }
            }
        } else {
            if (propobj.hasOwnProperty('$ref')) {
                var ref = propobj['$ref'];
                var regex = new RegExp(KUtils.oasmodel(oas2), 'ig');
                if (regex.test(ref)) {
                    refType = RegExp.$1;
                }
            }
        }
        return refType;
    }
}

export default OpenAPI;