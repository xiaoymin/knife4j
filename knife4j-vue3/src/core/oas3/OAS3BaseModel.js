/**
 * 所有Knife4j关于对象类的抽象定义文件
 */


/**
 * 示例值对象信息
 * https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.1.0.md#exampleObject
 * @param {*} summary 标题
 * @param {*} description 概述
 * @param {*} example 示例值
 * @param {*} externalValue A URI that points to the literal example. This provides the capability to reference examples that cannot easily be included in JSON or YAML documents. The value field and externalValue field are mutually exclusive. See the rules for resolving Relative References.
 */
function Knife4jOAS3ExampleInfo(summary, description, example, externalValue) {
    this.summary = summary;
    this.description = description;
    this.example = example;
    this.externalValue = externalValue;
}

/**
 * 测试类
 * @param {*} title 标题
 */
function Knife4jOAS3TestInfo(title) {
    this.title = title;
}


export { Knife4jOAS3ExampleInfo, Knife4jOAS3TestInfo };