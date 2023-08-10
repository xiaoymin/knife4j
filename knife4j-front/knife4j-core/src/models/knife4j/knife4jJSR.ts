import lodash from 'lodash'
import { SchemaObject } from '../openapi3/types';

/**
 * 针对校验参数
 */
export class Knife4jValidateObject {

    /**
     * 数字
     */
    numericValidate: Knife4jValidateNumericObject = new Knife4jValidateNumericObject();

    /**
     * 数组
     */
    arraysValidate: Knife4jValidateArrayObject = new Knife4jValidateArrayObject();

    /**
     * 字符
     */
    stringValidate: Knife4jValidateStringObject = new Knife4jValidateStringObject();

    /**
     * 对象属性校验
     */
    propertiesValidate: Knife4jValidatePropertiesObject = new Knife4jValidatePropertiesObject();

    /**
     * 解析schema-validate部分参数
     * @param schema schema对象
     */
    resolveSchema(schema: SchemaObject) {
        if (lodash.isEmpty(schema)) {
            return;
        }
        this.numericValidate.resolveOpenAPI3Schema(schema);
        this.arraysValidate.resolveOpenAPI3Schema(schema);
        this.stringValidate.resolveOpenAPI3Schema(schema);
        this.propertiesValidate.resolveOpenAPI3Schema(schema);
    }
}

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

/**
 * 对象属性的校验
 */
export class Knife4jValidatePropertiesObject {
    /**
       * Maximum number of properties allowed in an object.
       */
    maxProperties?: number;

    /**
     * Minimum number of properties allowed in an object.
     */
    minProperties?: number;

    /**
     * List of required properties in an object.
     */
    required: string[] = new Array();
    /**
   * 解析schema-validate部分参数
   * @param schema schema对象
   */
    resolveOpenAPI3Schema(schema: SchemaObject) {
        if (lodash.isEmpty(schema)) {
            return;
        }
        this.maxProperties = schema.maxProperties;
        this.minProperties = schema.minProperties;
        if (!lodash.isEmpty(schema.required)) {
            schema.required?.forEach(v => {
                this.required.push(v);
            })
        }
    }

    /**
     * 判断当前参数是否必须
     * @param name 属性名称
     */
    isReuqire(name: string) {
        if (!lodash.isEmpty(this.required)) {
            // 包含在required数组元素中
            return this.required.indexOf(name) > -1;
        }
        return false;
    }
}