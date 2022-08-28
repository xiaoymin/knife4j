import KUtils from '../utils'

import { Knife4jOAS3ExampleInfo } from './OAS3BaseModel'

/**
 * OAS3.0规范解析响应example对象
 * @param {*} source 请求原始对象
 */
function Knife4jOAS3ResponseExampleReader(source) {
    this.source = source;
    this.hasExample = false;
    this.hasMoreExample = false;
    // 如果既存在example又存在examples，那么examples优先
    this.responseText = null;
    this.responseValue = null;
    this.responseTextArray = [];
    this.init();
}

/**
 * 解析
 * https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.1.0.md#mediaTypeObject
 */
Knife4jOAS3ResponseExampleReader.prototype.init = function () {
    //console.log('init,', this.source);
    // 判断当前是否包含example
    if (KUtils.checkUndefined(this.source)) {
        let example = KUtils.propValue('example', this.source, null);
        let moreExample = KUtils.propValue('examples', this.source, null);
        this.hasExample = example != null;
        this.hasMoreExample = moreExample != null;
        if (this.hasExample) {
            this.oneExample(example);
        }
        if (this.hasMoreExample) {
            this.multipleExample(moreExample);
        }
    }
}

/**
 * 处理单个example
 * @param {*} example 示例值
 */
Knife4jOAS3ResponseExampleReader.prototype.oneExample = function (example) {
    if (KUtils.checkUndefined(example)) {
        this.responseText = example;
        this.responseValue = KUtils.json5stringifyFormat(example, null, '\t');
    }
}

/**
 * 处理多个example
 * @param {*} moreExample 示例值
 */
Knife4jOAS3ResponseExampleReader.prototype.multipleExample = function (moreExample) {
    if (KUtils.checkUndefined(moreExample)) {
        for (let key in moreExample) {
            let singleExample = KUtils.propValue(key, moreExample, null);
            if (KUtils.checkUndefined(singleExample)) {
                let summary = KUtils.propValue('summary', singleExample, null);
                if (KUtils.checkUndefined(summary)) {
                    // summary不能为空，否则下拉框无值
                    let description = KUtils.propValue('description', singleExample, null);
                    let example = KUtils.propValue('example', singleExample, null);
                    let externalValue = KUtils.propValue('externalValue', singleExample, null);
                    this.responseTextArray.push(new Knife4jOAS3ExampleInfo(summary, description, example, externalValue));
                }

            }
        }
    }
}

export default Knife4jOAS3ResponseExampleReader;




