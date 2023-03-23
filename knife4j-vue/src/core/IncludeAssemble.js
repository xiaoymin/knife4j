import KUtils from './utils'

/**
 * 过滤组件
 * @param json
 * @param includeArry
 * @constructor
 */
function IncludeAssemble(json, includeArry) {
    this.json = json;
    // 包含的关系需要把参数的body名称去掉
    var filterArr = new Array();
    var tmpKeys = Object.keys(includeArry || {});
    tmpKeys.forEach(key => {
        filterArr.push(key.substring(key.indexOf('.') + 1))
    })
    this.includeArrays = filterArr;
}

IncludeAssemble.prototype = {
    isObjInArray(o) {
        if (!this.isArray(o)) {
            return false;
        }
        if (o.length === 0) {
            return false;
        }
        return this.isObject(o[0]);
    },
    isObject(o) {
        return Object.prototype.toString.call(o) === '[object Object]';
    },
    isArray(o) {
        return Object.prototype.toString.call(o) === '[object Array]';
    },
    merge(source, target) {
        if (this.isObject(source)) {
            for (let key in target) {
                source[key] = this.isObject(source[key]) || this.isObjInArray(source[key]) ?
                    this.merge(source[key], target[key]) : source[key] = target[key];
            }
        } else {
            if (this.isObjInArray(target)) {
                source.forEach((o1, index) => {
                    this.merge(o1, target[index]);
                })
            } else {
                source.push.apply(source, target);
            }
        }
        return source;
    },
    getByPath(srcObj, path) {
        if (this.isObjInArray(srcObj)) {
            const r = [];
            srcObj.forEach(el => {
                r.push(this.getByPath(el, path));
            });
            return r;
        } else {
            const pathArr = path.split('.');
            // const r=JSON.parse(JSON.stringify(srcObj));
            const r = KUtils.json5parse(KUtils.json5stringify(srcObj));
            let tempObj = r;
            const len = pathArr.length;
            for (let i = 0; i < len; i++) {
                let pathComp = pathArr[i];
                for (let k in tempObj) {
                    if (k !== pathComp) {
                        delete tempObj[k];
                    }
                }
                if (!tempObj[pathComp]) {
                    break;
                }
                if (this.isObjInArray(tempObj[pathComp])) {
                    let t = this.getByPath(tempObj[pathComp], pathArr.slice(i + 1).join('.'));
                    // tempObj[pathComp]=JSON.parse(JSON.stringify(t));
                    tempObj[pathComp] = KUtils.json5parse(KUtils.json5stringify(t));
                    break;
                }
                tempObj = tempObj[pathComp];
            }
            return r
        }
    },
    result() {
        if (this.includeArrays == null || this.includeArrays.length == 0) {
            return this.json;
        } else {
            let arr = [];
            this.includeArrays.forEach(p => {
                arr.push(this.getByPath(this.json, p));
            });
            return arr.reduce((prev, cur) => {
                if (prev) {
                    this.merge(prev, cur);
                    return prev;
                }
                return cur;
            });
        }
    }
}

export default IncludeAssemble;