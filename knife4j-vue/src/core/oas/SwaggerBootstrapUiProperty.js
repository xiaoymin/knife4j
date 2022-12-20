/***
 * definition对象属性
 * @constructor
 */
var SwaggerBootstrapUiProperty = function () {
    // 默认基本类型,非引用
    this.basic = true;
    this.name = '';
    this.type = '';
    this.refType = null;
    this.description = '';
    this.example = '';
    this.format = '';
    // 是否必须
    this.required = false;
    // 默认值
    this.value = null;
    // 引用类
    this.property = null;
    // 原始参数
    this.originProperty = null;
    // 是否枚举
    this.enum = null;
    // 是否readOnly
    this.readOnly = false;

    // JSR-303 annotations supports since 1.8.7
    // 默认状态为false
    this.validateStatus = false;
    this.validateInstance = null;


    //可能存在子property
    this.properties = new Array();
}

export default SwaggerBootstrapUiProperty;