import { ParameterObject } from "./types"
import { ReferenceObject } from "./types";

const OpenAPI3TypeUtils = {

    /**
     * 判断是否ParameterObject类型
     * @param obj 对象
     * @returns 
     */
    isParameterObject(obj: any): obj is ParameterObject {
        try {
            let _param = obj as ParameterObject;
            return true;
        } catch (e) {

        }
        return false;
    },
    /**
     * 判断是否ReferenceObject类型
     * @param obj 对象
     * @returns 
     */
    isReferenceObject(obj: any): obj is ReferenceObject {
        try {
            let _param = obj as ReferenceObject;
            return true;
        } catch (e) {

        }
        return false;
    }
}


export default OpenAPI3TypeUtils