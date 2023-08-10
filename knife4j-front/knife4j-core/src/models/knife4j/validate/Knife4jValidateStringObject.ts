import lodash from 'lodash'
import { SchemaObject } from '../../openapi3/types';

/**
 * string类型校验
 */
export class Knife4jValidateStringObject {
    /**
       * Maximum length of a string.
       */
    maxLength?: number;

    /**
     * Minimum length of a string.
     */
    minLength?: number;

    /**
     * Regular expression pattern for a string.
     */
    pattern?: string;

    /**
    * 解析schema-validate部分参数
    * @param schema schema对象
    */
    resolveOpenAPI3Schema(schema: SchemaObject) {
        if (lodash.isEmpty(schema)) {
            return;
        }
        this.maxLength = schema.maxLength;
        this.minLength = schema.minLength;
        this.pattern = schema.pattern;
    }
}