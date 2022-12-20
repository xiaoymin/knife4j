import KUtils from '../utils';

/**
 * 所有Knife4j关于对象类的抽象定义文件
 */

/**
 * 
 * @param {*} additionalProperties addtional原始属性对象
 * @param {*} modelName 实体类名称
 * @param {*} modelExampleValue class类型
 */
function Knife4jOAS2AdditionalModel(additionalProperties, modelName, modelExampleValue) {
    this.additionalProperties = additionalProperties;
    this.modelName = modelName;
    this.modelExampleValue = modelExampleValue;
}

/**
 * 找到Additional类型的class名称
 * @param {*} additionalProperties addtional原始属性对象
 * @param {*} oas2 是否oas2
 */
function Knife4jOAS2AdditionalModelClassFinder(additionalProperties, oas2) {
    this.additionalProperties = additionalProperties;
    this.oas = oas2;
}


/**
 * 提供解析方法
 */
Knife4jOAS2AdditionalModel.prototype = {

    /**
     * 递归获取针对AddtionalProperties对象的属性值
     * @param {*} parentAdditional 父类additionalProperties实例对象
     */
    additionalMapValue(parentAdditional) {
        let addionalProp = this.additionalProperties;
        if (KUtils.checkUndefined(parentAdditional)) {
            addionalProp = parentAdditional;
        } else {
            console.log("start---------------")
        }
        console.log(addionalProp)
        if (KUtils.checkUndefined(addionalProp)) {
            //判断type是否是array
            let type = KUtils.propValue("type", addionalProp, '');
            if (addionalProp.hasOwnProperty('additionalProperties')) {
                let childrenAdditionalProp = addionalProp["additionalProperties"];
                if (type == "array") {
                    return {
                        "additionalProperties1": [].concat(this.additionalMapValue(childrenAdditionalProp))
                    }
                } else {
                    return {
                        "additionalProperties1": this.additionalMapValue(childrenAdditionalProp)
                    }
                }
            } else {
                if (type == "array") {
                    return {
                        "additionalProperties1": [].concat(this.modelExampleValue)
                    }
                } else {
                    return {
                        "additionalProperties1": this.modelExampleValue
                    };
                }
            }
        }
        //赋值最后一个model的类型
        return {
            "additionalProperties1": this.modelExampleValue
        };
    }

}

/**
 * 方法集合
 */
Knife4jOAS2AdditionalModelClassFinder.prototype = {
    /**
     * 查询class类名
     * @param {*} parentAdditional 父类
     */
    findClassName(parentAdditional) {
        var definiationName = '';
        let addtionalObject = this.additionalProperties;
        if (KUtils.checkUndefined(parentAdditional)) {
            addtionalObject = parentAdditional;
        }
        // console.log(addtionalObject)
        if (KUtils.checkUndefined(addtionalObject)) {
            if (addtionalObject.hasOwnProperty('additionalProperties')) {
                var dpAddtional = addtionalObject['additionalProperties'];
                return this.findClassName(dpAddtional, this.oas);
            } else {
                // 不存在了，
                definiationName = this.findItemsClassName(addtionalObject);
            }
        }
        return definiationName;
    },
    findItemsClassName(addtionalObject) {
        var definiationName = '';
        if (KUtils.checkUndefined(addtionalObject)) {
            // 不存在了，
            if (addtionalObject.hasOwnProperty('$ref')) {
                var adref = addtionalObject['$ref'];
                var regex = new RegExp(KUtils.oasmodel(this.oas), 'ig');
                if (regex.test(adref)) {
                    definiationName = RegExp.$1;
                }
            } else if (addtionalObject.hasOwnProperty('items')) {
                var addItem = addtionalObject['items'];
                if (addItem.hasOwnProperty('$ref')) {
                    var adrefItem = addItem['$ref'];
                    var regexItem = new RegExp(KUtils.oasmodel(this.oas), 'ig');
                    if (regexItem.test(adrefItem)) {
                        definiationName = RegExp.$1;
                    }
                } else if (addItem.hasOwnProperty("items")) {
                    definiationName = this.findItemsClassName(addItem);
                }
            }
        }
        return definiationName;
    }
}


export { Knife4jOAS2AdditionalModel, Knife4jOAS2AdditionalModelClassFinder };