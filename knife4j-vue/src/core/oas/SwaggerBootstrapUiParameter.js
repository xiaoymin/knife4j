/***
 * Swagger请求参数
 * @constructor
 */
var SwaggerBootstrapUiParameter = function () {
    this.name = null;
    // 该属性用于过滤参数使用
    this.ignoreFilterName = null;
    // 参数是否过时
    this.deprecated = false;
    this.allowEmptyValue = false;
    // 默认false
    this.require = false;
    this.type = null;
    this.in = null;
    this.schema = false;
    this.schemaValue = null;
    this.value = null;
    // JSR-303 annotations supports since 1.8.7
    // 默认状态为false
    this.validateStatus = false;
    this.validateInstance = null;
    // 引用类
    this.def = null;
    // des
    this.description = null;
    // 文本框值
    this.txtValue = null;
    // 枚举类型
    this.enum = null;

    // this.id = 'param' + Math.round(Math.random() * 1000000);
    this.id = uniqueId('param'); //  'param' + KUtils.randomMd5(); 使用 uniqueId 方法替代
    this.pid = '-1';
    this.level = 1;
    // 参数是否显示在debug中
    this.show = true;
    // 是否readOnly
    this.readOnly = false;
    this.example = null;


    this.childrenTypes = new Array();
    this.children = null;
    this.parentTypes = new Array();
}

export default SwaggerBootstrapUiParameter;
