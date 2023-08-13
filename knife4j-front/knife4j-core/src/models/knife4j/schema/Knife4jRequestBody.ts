
import { SchemaObject } from "../../openapi3/types";
import { Knife4jSchema } from "./Knife4jSchema";
import lodash from 'lodash'
import { Knife4jParamType } from "../enums/Knife4jParamType";
import { Knife4jInstance } from "../Knife4jInstance";

export class Knife4jRequestBody {
    /**
     * 请求类型，例如：application/json,application/xml等等
     */
    media: string;
    /**
   * A brief description of the request body. This could contain examples of use.
   */
    description?: string;
    /**
     * 示例值,根据不同的MediaType，依靠parameters生成example
     */
    example: string = "";
    /**
     * A list of parameters that are applicable for this operation.
     */
    parameters: Knife4jSchema[] = [];
    /**
     * Determines if the request body is required in the request. Defaults to false.
     */
    required: boolean = false;


    constructor(media: string) {
        this.media = media;
    }
    /**
     * 解析requestBody
     * @param description 说明
     * @param required 是否必须
     * @param schema schema
     */
    resolveBody(description: string, required: boolean, schema: SchemaObject, instance: Knife4jInstance) {
        this.description = description;
        this.required = required;
        //此处直接解析schema的properties属性
        if (lodash.isEmpty(schema) || lodash.isEmpty(schema.properties)) {
            return;
        }
        //如果是properties，直接解析
        const _properties = schema.properties;
        for (let _propName in _properties) {
            //如果是属性级别，默认query类型
            const _schema = new Knife4jSchema(_propName, Knife4jParamType.query);
            const _openapiSchema = _properties[_propName];
            _schema.asyncResolveProperties(_openapiSchema);
            //作为参数添加进来
            this.parameters.push(_schema);
        }
    }
}