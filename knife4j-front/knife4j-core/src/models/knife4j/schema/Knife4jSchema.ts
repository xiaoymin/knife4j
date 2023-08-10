import lodash from 'lodash'
import { Knife4jValidateObject } from '../validate/Knife4jValidateObject';
import { SchemaObject, ParameterObject } from '../../openapi3/types';
import OpenAPI3TypeUtils from '../../openapi3/typeCheck';
import KUtils from '../utils/KUtils';
/**
 * Knife4j封装参数类，按树组件的格式进行封装
 */
export class Knife4jSchema {
    /**
     * 当前参数唯一id
     */
    id: string;
    /**
     * title属性
     */
    title: string = "";
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
    children: Knife4jSchema[] = [];

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
     * 数据格式化
     */
    format?: string;

    /**
     * Example of the media type. The example SHOULD match the specified schema and encoding properties if present.
     */
    example?: any;

    /**
     * 默认值？
     */
    default?: any;

    /**
     * Possible values for an object or array.
     */
    enum?: any[];

    /**
     * 校验参数部分
     */
    validate: Knife4jValidateObject;

    constructor(name: string, type: string) {
        this.id = lodash.uniqueId("param-")
        this.name = name;
        this.type = type;
        this.validate = new Knife4jValidateObject();
    }

    /**
     * 添加子参数
     * @param child 子元素
     */
    addChildren(child: Knife4jSchema) {
        if (lodash.isEmpty(child)) {
            return;
        }
        //指定父id
        child.parentId = this.id;
        this.children.push(child);
    }

    /**
     * 解析基础参数,对于Parameter参数，不管是OpenAPI2还是OpenAPI3，都不用解析的太深
     * 如果是复杂的参数，在3的规范中，应该通过content节点来描述,最终解析Schema属性
     * @param originalParam 原始参数数据集合
     */
    resolveOpenAPI3Basic(originalParam: ParameterObject) {
        //基础赋值
        this.deprecated = lodash.defaultTo(originalParam.deprecated, false);
        this.description = KUtils.wrapLine(lodash.defaultTo(originalParam.description, ''));
        this.allowEmptyValue = lodash.defaultTo(originalParam.allowEmptyValue, false);
        this.explode = lodash.defaultTo(originalParam.explode, false);
        this.style = originalParam.style;
        this.allowReserved = originalParam.allowReserved;
        this.required = lodash.defaultTo(originalParam.required, false);
        const _schema = originalParam.schema;
        if (OpenAPI3TypeUtils.isSchemaObject(_schema)) {
            this.resolveOpenAPI3SchemaBasic(_schema)
        }
    }

    /**
     * 解析schema,只解析类型和example
     * @param schema schema对象
     */
    resolveOpenAPI3SchemaBasic(schema: SchemaObject) {
        if (lodash.isEmpty(schema)) {
            return;
        }
        //类型
        if (!lodash.isEmpty(schema.type)) {
            let schemaType = schema.type;
            if (lodash.isArray(schemaType)) {
                //数组类型
                this.schema = schemaType.join()
            } else {
                //String values MUST be one of the six primitive types ("null","boolean", "object", "array", "number", or "string"), or "integer" which matches any number with a zero fractional part.
                //但类型
                this.schema = lodash.defaultTo(schemaType, '');
            }
        }
        this.format = lodash.defaultTo(schema.format, '');
        //处理枚举
        if (!lodash.isEmpty(schema.enum)) {
            this.enum = schema.enum
        }
        //处理jsr303等部分属性
        this.validate.resolveSchema(schema);
        //判断当前参数是否必须
        //this.required = this.validate.propertiesValidate.isReuqire(this.name);
        this.title = lodash.defaultTo(schema.title, "");
        this.default = schema.default
        //赋值example值
        this.example = schema.example;
    }

    /**
     * 解析属性
     * @param schema schema对象
     */
    asyncResolveProperties(schema: SchemaObject) {

    }

}