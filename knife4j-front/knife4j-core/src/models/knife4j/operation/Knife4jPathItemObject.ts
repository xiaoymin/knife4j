import lodash from 'lodash'
//ExternalDocumentationObject
//Knife4jExternalDocumentationObject
import { Knife4jExternalDocumentationObject } from "../externalDoc/Knife4jExternalDocumentationObject";
import { Knife4jRequestBody } from '../schema/Knife4jRequestBody';
import { Knife4jSchema } from '../schema/Knife4jSchema';
//ParameterObject
import { ParameterObject, RequestBodyObject, ExternalDocumentationObject } from "../../openapi3/types";
import OpenAPI3TypeUtils from "../../openapi3/typeCheck";
import { Knife4jInstance } from '../Knife4jInstance';
//RequestBodyObject
/**
* Describes the operations available on a single path.
* A Path Item MAY be empty, due to ACL constraints.
* The path itself is still exposed to the documentation viewer but they will not know which operations and parameters are available.
*/
export class Knife4jPathItemObject {
    /**
     * url
     */
    url: string;

    /**
     * 接口类型
     */
    methodType: string;
    /**
     * Allows for an external definition of this path item.
     * Referencing a `$ref` requires the value of the property to be the URI of a valid OpenAPI Specification file.
     * This file MUST be hosted remotely and accessible via HTTP or HTTPS.
     * The value of the `$ref` field MUST be an RFC 3986 compliant URI.
     */
    $ref?: string;

    /**
     * An optional, string summary, intended to apply to all operations in this path.
     */
    summary?: string;

    /**
     * An optional, string description, intended to apply to all operations in this path.
     * CommonMark syntax MAY be used for rich text representation.
     */
    description?: string;

    /**
   * A list of tags for API documentation control.
   */
    tags?: string[];

    /**
     *  Additional external documentation for this operation.
     */
    extDoc?: Knife4jExternalDocumentationObject;

    /**
     * Unique string used to identify the operation.
     */
    operationId?: string;

    /**
     * 请求类型
     */
    consumer: string = "*/*";

    /**
     * 响应类型
     */
    produces: string = "*/*";

    /**
     * A list of parameters that are applicable for this operation.
     */
    parameters: Knife4jSchema[] = [];

    /**
     * The request body applicable for this operation.
     * include 多种类型
     */
    requestBody: Knife4jRequestBody[] = [];

    /**
     * Expected responses for this operation.
     */
    responses?: {};

    /**
     * A map of possible out-of band callbacks related to the parent operation.
     */
    callbacks?: {};

    /**
     * Declares this operation to be deprecated.
     */
    deprecated?: boolean;

    /**
     * A declaration of which security mechanisms can be used for this operation.
     */
    security?: [];

    /**
     * An alternative server array to service this operation.
     */
    servers?: [];

    constructor(url: string, methodType: string) {
        this.url = url;
        this.methodType = methodType
    }

    /**
     * 新增参数
     * @param param 参数
     */
    addParameter(param: Knife4jSchema) {
        if (lodash.isEmpty(param)) {
            return;
        }
        console.log("addParam:", param)
        this.parameters.push(param)
    }

    /**
     * 解析OpenAPI3规范的外部文档参数
     * @param doc 外部文档
     */
    resolveOpenAPI3ExtDoc(doc: ExternalDocumentationObject | undefined) {
        if (lodash.isEmpty(doc)) {
            return;
        }
        //解析
        const _doc = new Knife4jExternalDocumentationObject(doc.url);
        _doc.description = doc.description;
        this.extDoc = _doc;
    }
    /**
     * 异步解析参数-OpenAPI3
     * @param parameters 参数集合
     * @param operation 当前operation对象
     */
    asyncResolveParameters(parameters: ParameterObject[] | undefined) {
        //解析请求参数parameters
        if (lodash.isEmpty(parameters) || !lodash.isArray(parameters)) {
            return;
        }
        //遍历param
        parameters.forEach(param => {
            //判断类型
            const originalParam = param as ParameterObject;
            //基础表单参数
            const _param = new Knife4jSchema(originalParam.name, originalParam.in)
            //基础赋值
            _param.resolveOpenAPI3Basic(originalParam);
            this.addParameter(_param)
        })
    }
    /**
     * 异步解析请求参数
     * @param body 请求参数
     */
    asyncResolveRequestBody(body: RequestBodyObject | undefined, instance: Knife4jInstance) {
        if (lodash.isEmpty(body) || lodash.isEmpty(body.content)) {
            return;
        }
        const content = body.content;
        const _description = lodash.defaultTo(body.description, "");
        const _required = lodash.defaultTo(body.required, false);
        //遍历
        for (let media in content) {
            const _schema = content[media];
            if (lodash.isEmpty(media) || lodash.isEmpty(_schema)) {
                continue;
            }
            const _requestBody = new Knife4jRequestBody(media);
            _requestBody.resolveBody(_description, _required, _schema, instance);
            this.requestBody.push(_requestBody);
        }

    }
}