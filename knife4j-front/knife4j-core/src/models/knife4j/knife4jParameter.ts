import lodash from 'lodash'
//ParameterObject
import { ParameterObject } from '../openapi3/types';
/**
 * Knife4j封装参数类，按树组件的格式进行封装
 */
export class Knife4jParameter {
    /**
     * 当前参数唯一id
     */
    id: string;
    /**
       * The name of the parameter.
       */
    name: string;

    /**
     * The location of the parameter. Possible values are "query", "header", "path" or "cookie".
     */
    type: string;

    /**
     * 父id，默认顶级情况下为0
     */
    parentId: string = "0";

    /**
     * 子参数
     */
    children: Knife4jParameter[] = [];

    /**
     * A brief description of the parameter. This could contain examples of use. CommonMark syntax MAY be used for rich text representation.
     */
    description?: string;

    /**
     * Determines whether this parameter is mandatory.
     */
    required: boolean = false;

    /**
     * Specifies that a parameter is deprecated and SHOULD be transitioned out of usage.
     */
    deprecated: boolean = false;

    /**
     * Sets the ability to pass empty-valued parameters.
     */
    allowEmptyValue?: boolean;

    /**
     * Describes how the parameter value will be serialized depending on the type of the parameter value.
     */
    style?: string;

    /**
     * When this is true, parameter values of type array or object generate separate parameters for each value of the array or key-value pair of the map.
     */
    explode?: boolean;

    /**
     * Determines whether the parameter value SHOULD allow reserved characters, as defined by RFC3986.
     */
    allowReserved?: boolean;

    /**
     * 参数数据类型
     */
    schema: string = "string";

    /**
     * Example of the media type. The example SHOULD match the specified schema and encoding properties if present.
     */
    example?: any;

    constructor(name: string, type: string) {
        this.id = lodash.uniqueId("param-")
        this.name = name;
        this.type = type;
    }

    /**
     * 添加子参数
     * @param child 子元素
     */
    addChildren(child: Knife4jParameter) {
        this.children.push(child);
    }

    /**
     * 解析基础参数
     * @param originalParam 原始参数数据集合
     */
    resolveOpenAPI3Basic(originalParam: ParameterObject) {
        //基础赋值
        this.deprecated = lodash.defaultTo(originalParam.deprecated, false);
        this.description = lodash.defaultTo(originalParam.description, '');
        this.allowEmptyValue = lodash.defaultTo(originalParam.allowEmptyValue, false)
    }

}