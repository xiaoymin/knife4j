import lodash from 'lodash'
import { SchemaObject } from '../../openapi3/types';

/**
 * 数字校验
 */
export class Knife4jValidateNumericObject {
    /**
       * Number that the value must be a multiple of.
       */
    multipleOf?: number;

    /**
     * Maximum value allowed.
     */
    maximum?: number;

    /**
     * Whether the maximum value is exclusive.
     */
    exclusiveMaximum?: boolean;

    /**
     * Minimum value allowed.
     */
    minimum?: number;

    /**
     * Whether the minimum value is exclusive.
     */
    exclusiveMinimum?: boolean;
    /**
   * 解析schema-validate部分参数
   * @param schema schema对象
   */
    resolveOpenAPI3Schema(schema: SchemaObject) {
        if (lodash.isEmpty(schema)) {
            return;
        }
        this.multipleOf = schema.multipleOf;
        this.maximum = schema.maximum;
        this.exclusiveMaximum = schema.exclusiveMaximum;
        this.minimum = schema.minimum;
        this.exclusiveMaximum = schema.exclusiveMaximum;
    }
}