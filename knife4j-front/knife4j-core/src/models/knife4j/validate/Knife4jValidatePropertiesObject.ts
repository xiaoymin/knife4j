import lodash from 'lodash'
import { SchemaObject } from '../../openapi3/types';

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