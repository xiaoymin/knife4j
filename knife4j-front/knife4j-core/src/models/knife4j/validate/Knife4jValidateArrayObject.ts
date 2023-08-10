import lodash from 'lodash'
import { SchemaObject } from '../../openapi3/types';


/**
 * 数组的校验
 */
export class Knife4jValidateArrayObject {

    /**
     * Maximum number of items in an array.
     */
    maxItems?: number;

    /**
     * Minimum number of items in an array.
     */
    minItems?: number;

    /**
     * Whether all items in the array must be unique.
     */
    uniqueItems?: boolean;

    maxContains?: number;

    minContains?: number;
    /**
   * 解析schema-validate部分参数
   * @param schema schema对象
   */
    resolveOpenAPI3Schema(schema: SchemaObject) {
        if (lodash.isEmpty(schema)) {
            return;
        }
        this.maxItems = schema.maxItems;
        this.minItems = schema.minItems;
        this.uniqueItems = schema.uniqueItems;
        this.maxContains = schema.maxContains;
        this.minContains = schema.minContains;
    }

}
