import lodash from 'lodash'

const KUtils = {
    /**
     * 处理换行
     * @param str 换行字符
     */
    wrapLine(str: string): string {
        if (!lodash.isEmpty(str)) {
            var newLinePattern = /(\r\n|\n\r|\r|\n)/g;
            if (newLinePattern.test(str)) {
                var newDes = str.replace(newLinePattern, '\\n');
                return newDes;
            }
            return str;
        }
        return "";
    },
    /**
     * 校验是否基础类型
     *  ("null","boolean", "object", "array", "number", or "string"), or "integer"
     * @param type 类型
     */
    basicType(type: string): boolean {
        const basicTypes = ["string", "integer", "number", "object", "boolean", "int32", "int64", "float", "double"];
        let flag = false;
        if (!lodash.isEmpty(type)) {
            if (basicTypes.indexOf(type) > -1) {
                flag = true;
            }
        }
        return flag;
    },
    /**
     * 获取基础类型的初始value值
     * @param type 类型
     * @returns 
     */
    basicTypeValue(type: string): any {
        var propValue;
        // 是否是基本类型
        if (type == "integer") {
            propValue = 0;
        }
        if (type == "boolean") {
            propValue = true;
        }
        if (type == "object") {
            propValue = {};
        }
        if (type == "number") {
            propValue = parseFloat("0");
        }
        return propValue;
    }

}

export default KUtils;