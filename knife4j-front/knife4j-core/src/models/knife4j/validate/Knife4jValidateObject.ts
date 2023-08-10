import lodash from 'lodash'
import { SchemaObject } from "../../openapi3/types";
import { Knife4jValidateArrayObject } from "./Knife4jValidateArrayObject";
import { Knife4jValidateNumericObject } from "./Knife4jValidateNumericObject";
import { Knife4jValidatePropertiesObject } from "./Knife4jValidatePropertiesObject";
import { Knife4jValidateStringObject } from "./Knife4jValidateStringObject";

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
