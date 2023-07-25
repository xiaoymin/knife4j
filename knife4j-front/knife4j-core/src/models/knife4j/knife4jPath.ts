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
     * Additional external documentation for this operation.
     */
    externalDocs?: {};

    /**
     * Unique string used to identify the operation.
     */
    operationId?: string;

    /**
     * A list of parameters that are applicable for this operation.
     */
    parameters?: [];

    /**
     * The request body applicable for this operation.
     */
    requestBody?: {};

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

    /**
     * 原始数据
     */
    originalRecord?: Record<string, any>

    constructor(url: string, methodType: string) {
        this.url = url;
        this.methodType = methodType
    }
}