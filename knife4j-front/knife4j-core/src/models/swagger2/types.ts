/**
 * Swagger2对象
 */
export interface Swagger {
    /**
     * Required. Specifies the Swagger Specification version being used. 
     * It can be used by the Swagger UI and other clients to interpret the API listing. The value MUST be "2.0".
     */
    swagger: string;
    /**
     * Required. Provides metadata about the API. The metadata can be used by the clients if needed.
     */
    info: Info;
    /**
    * Required. The available paths and operations for the API.
    */
    paths: Paths;
    /**
     * The host (name or ip) serving the API. This MUST be the host only and does not include the scheme nor sub-paths. 
     * It MAY include a port. If the host is not included, the host serving the documentation is to be used (including the port). 
     * The host does not support path templating.
     */
    host?: string;
    /**
     * The base path on which the API is served, which is relative to the host. If it is not included, the API is served directly under the host.
     *  The value MUST start with a leading slash (/). The basePath does not support path templating.
     */
    basePath?: string;
    /**
     * The transfer protocol of the API. Values MUST be from the list: "http", "https", "ws", "wss". If the schemes is not included, 
     * the default scheme to be used is the one used to access the Swagger definition itself.
     */
    schemes?: Array<string>;
    /**
     * A list of MIME types the APIs can consume. This is global to all APIs but can be overridden on specific API calls. 
     * Value MUST be as described under Mime Types.
     */
    consumes?: Array<string>;
    /**
     * A list of MIME types the APIs can produce. This is global to all APIs but can be overridden on specific API calls. 
     * Value MUST be as described under Mime Types.
     */
    produces?: Array<string>;
    /**
     * An object to hold data types produced and consumed by operations.
     */
    definitions?: Definitions;
    /**
     * An object to hold parameters that can be used across operations. This property does not define global parameters for all operations.
     */
    parameters?: Parameters;
    /**
     * An object to hold responses that can be used across operations. This property does not define global responses for all operations.
     */
    responses?: Responses;
    /**
     * Security scheme definitions that can be used across the specification.
     */
    securityDefinitions?: SecurityDefinitions;
    /**
     * A declaration of which security schemes are applied for the API as a whole. 
     * The list of values describes alternative security schemes that can be used 
     * (that is, there is a logical OR between the security requirements). Individual operations can override this definition.
     */
    security?: Array<SecurityRequirement>;
    /**
     * A list of tags used by the specification with additional metadata. 
     * The order of the tags can be used to reflect on their order by the parsing tools.
     *  Not all tags that are used by the Operation Object must be declared. 
     * The tags that are not declared may be organized randomly or based on the tools' logic. Each tag name in the list MUST be unique.
     */
    tags?: Array<Tag>;
    /**
     * Additional external documentation.
     */
    externalDocs?: ExternalDocumentation;
}

export interface Info {
    /**
     * Required. The title of the application.
     */
    title: string;
    /**
    * Required Provides the version of the application API (not to be confused with the specification version).
    */
    version: string;
    /**
     * A short description of the application. GFM syntax can be used for rich text representation.
     */
    description?: string;
    /**
     * The Terms of Service for the API.
     */
    termsOfService?: string;
    /**
     * The contact information for the exposed API.
     */
    contact?: Contact;
    /**
     * The license information for the exposed API.
     */
    license?: License;
}


/**
 * Contact information for the exposed API.
 */
export interface Contact {
    /**
     * The identifying name of the contact person/organization.
     */
    name?: string;
    /**
     * The URL pointing to the contact information. MUST be in the format of a URL.
     */
    url?: string;
    /**
     * The email address of the contact person/organization. MUST be in the format of an email address.
     */
    email?: string;
}

/**
 * License information for the exposed API.
 */
export interface License {
    /**
     * Required. The license name used for the API.
     */
    name: string;
    /**
     * A URL to the license used for the API. MUST be in the format of a URL.
     */
    url?: string;
}


/**
 * Holds the relative paths to the individual endpoints. 
 * The path is appended to the basePath in order to construct the full URL. The Paths may be empty, due to ACL constraints.
 */
export interface Paths {
    /**
     * A relative path to an individual endpoint. The field name MUST begin with a slash. 
     * The path is appended to the basePath in order to construct the full URL. Path templating is allowed.
     */
    [pathName: string]: PathItem;
}

/**
 * Describes the operations available on a single path.
 * A Path Item may be empty, due to ACL constraints. 
 * The path itself is still exposed to the documentation viewer but they will not know which operations and parameters are available.
 */
export interface PathItem {
    /**
     * Allows for an external definition of this path item. The referenced structure MUST be in the format of a Path Item Object. 
     * If there are conflicts between the referenced definition and this Path Item's definition, the behavior is undefined.
     */
    $ref?: string;
    /**
     * A definition of a GET operation on this path.
     */
    get?: Operation;
    /**
     * A definition of a PUT operation on this path.
     */
    put?: Operation;
    /**
     * A definition of a POST operation on this path.
     */
    post?: Operation;
    /**
     * A definition of a DELETE operation on this path.
     */
    delete?: Operation;
    /**
     * A definition of a OPTIONS operation on this path.
     */
    options?: Operation;
    /**
     * A definition of a HEAD operation on this path.
     */
    head?: Operation;
    /**
     * A definition of a PATCH operation on this path.
     */
    patch?: Operation;
    /**
     * A list of parameters that are applicable for all the operations described under this path. 
     * These parameters can be overridden at the operation level, but cannot be removed there. 
     * The list MUST NOT include duplicated parameters. A unique parameter is defined by a combination of a name and location. 
     * The list can use the Reference Object to link to parameters that are defined at the Swagger Object's parameters.
     * There can be one "body" parameter at most.
     */
    parameters?: Array<Parameter | Reference>;
}


/**
 * Describes a single API operation on a path.
 */
export interface Operation {
    /**
     * A list of tags for API documentation control. Tags can be used for logical grouping of operations by resources or any other qualifier.
     */
    tags?: Array<string>;
    /**
     * A short summary of what the operation does. For maximum readability in the swagger-ui, this field SHOULD be less than 120 characters.
     */
    summary?: string;
    /**
     * A verbose explanation of the operation behavior. GFM syntax can be used for rich text representation.
     */
    description?: string;
    /**
     * Additional external documentation for this operation.
     */
    externalDocs?: ExternalDocumentation;
    /**
     * Unique string used to identify the operation. The id MUST be unique among all operations described in the API. 
     * Tools and libraries MAY use the operationId to uniquely identify an operation, therefore, 
     * it is recommended to follow common programming naming conventions.
     */
    operationId?: string;
    /**
     * A list of MIME types the operation can consume. This overrides the consumes definition at the Swagger Object. 
     * An empty value MAY be used to clear the global definition. Value MUST be as described under Mime Types.
     */
    consumes?: Array<string>;
    /**
     * A list of MIME types the operation can produce. This overrides the produces definition at the Swagger Object. 
     * An empty value MAY be used to clear the global definition. Value MUST be as described under Mime Types.
     */
    produces?: Array<string>;
    /**
     * A list of parameters that are applicable for this operation. 
     * If a parameter is already defined at the Path Item, the new definition will override it, but can never remove it. 
     * The list MUST NOT include duplicated parameters. A unique parameter is defined by a combination of a name and location. 
     * The list can use the Reference Object to link to parameters that are defined at the Swagger Object's parameters. 
     * There can be one "body" parameter at most.
     */
    parameters?: Array<Parameter | Reference>;
    /**
     * Required. The list of possible responses as they are returned from executing this operation.
     */
    responses: Responses;
    /**
     * The transfer protocol for the operation. Values MUST be from the list: "http", "https", "ws", "wss". 
     * The value overrides the Swagger Object schemes definition.
     */
    schemes?: Array<string>;
    /**
     * Declares this operation to be deprecated. Usage of the declared operation should be refrained. Default value is false.
     */
    deprecated?: boolean;
    /**
     * A declaration of which security schemes are applied for this operation. 
     * The list of values describes alternative security schemes that can be used (that is, there is a logical OR between the security requirements). 
     * This definition overrides any declared top-level security. To remove a top-level security declaration, an empty array can be used.
     */
    security?: Array<SecurityRequirement>;
}

/**
 * An object to hold data types that can be consumed and produced by operations. These data types can be primitives, arrays or models.
 */
export interface Definitions {
    /**
     * A single definition, mapping a "name" to the schema it defines.
     */
    [definitionName: string]: Schema | Reference;
}

/**
 * An object to hold parameters to be reused across operations. Parameter definitions can be referenced to the ones defined here.
 * This does not define global operation parameters.
 */
export interface Parameters {
    /**
     * A single parameter definition, mapping a "name" to the parameter it defines.
     */
    [parameterName: string]: Parameter | Reference;
}

/**
 * A container for the expected responses of an operation. The container maps a HTTP response code to the expected response. It is not expected from the documentation to necessarily cover all possible HTTP response codes, since they may not be known in advance. However, it is expected from the documentation to cover a successful operation response and any known errors.
 * The default can be used as the default response object for all HTTP codes that are not covered individually by the specification.
 * The Responses Object MUST contain at least one response code, and it SHOULD be the response for a successful operation call.
 */
export interface Responses {
    [responseName: string]: Response | Reference;
}

/**
 * A declaration of the security schemes available to be used in the specification. 
 * This does not enforce the security schemes on the operations and only serves to provide the relevant details for each scheme.
 */
export interface SecurityDefinitions {
    /**
     * A single security scheme definition, mapping a "name" to the scheme it defines.
     */
    [securityDefinitionName: string]: SecurityScheme;
}

/**
 * Security scheme definition.
 * Allows the definition of a security scheme that can be used by the operations. 
 * Supported schemes are basic authentication, an API key (either as a header or as a query parameter) 
 * and OAuth2's common flows (implicit, password, application and access code).
 */
export interface SecurityScheme {
    /**
     * The type of the security scheme. Valid values are "basic", "apiKey" or "oauth2".
     */
    type: "basic" | "apiKey" | "oauth2";
    /**
     * A short description for security scheme.
     */
    description?: string;
    /**
     * Required,The name of the header or query parameter to be used when type is "apiKey".
     */
    name?: string;
    /**
     * The location of the API key when type is "apiKey". Valid values are "query" or "header".
     */
    in?: "query" | "header";
    /**
     * The flow used by the OAuth2 security scheme. Valid values are "implicit", "password", "application" or "accessCode".
     */
    flow?: "implicit" | "password" | "application" | "accessCode";
    /**
     * The authorization URL to be used for this flow. This should be the URL to the authorization endpoint, where the client can obtain the authorization code or access token.
     */
    authorizationUrl?: string;
    /**
     * The token URL to be used for this flow. This should be the URL to the token endpoint, where the client can exchange the authorization code or refresh token for an access token.
     */
    tokenUrl?: string;
    /**
     * The available scopes for the OAuth2 security scheme.
     * example:
      {
        "write:pets": "modify pets in your account",
        "read:pets": "read your pets"
      
      }
     */
    scopes?: Record<string, string>;
}


export interface SecurityRequirement {
    [name: string]: Array<string>;
}

/**
 * Allows adding meta data to a single tag that is used by the Operation Object. 
 * It is not mandatory to have a Tag Object per tag used there.
 */
export interface Tag {
    /**
     * Required. The name of the tag.
     */
    name: string;
    /**
     * A short description for the tag. GFM syntax can be used for rich text representation.
     */
    description?: string;
    /**
     * Additional external documentation for this tag.
     */
    externalDocs?: ExternalDocumentation;
}

/**
 * Allows referencing an external resource for extended documentation.
 */
export interface ExternalDocumentation {
    /**
     * Required. The URL for the target documentation. Value MUST be in the format of a URL.
     */
    url: string;
    /**
     * A short description of the target documentation. GFM syntax can be used for rich text representation.
     */
    description?: string;
}

export interface Reference {
    $ref: string;
}

export type ParameterLocation = 'query' | 'header' | 'path' | 'formData' | 'body';

/**
 * Parameter object that represents a single operation parameter.
 */
export interface Parameter {
    /**
     * The name of the parameter.
     */
    name: string;
    /**
     * The location of the parameter.
     * Possible values are "query", "header", "path", "formData" or "body".
     */
    in: "query" | "header" | "path" | "formData" | "body";
    /**
     * A brief description of the parameter.
     * This could contain examples of use, potential errors or anything else to aid consumers.
     */
    description?: string;
    /**
     * Determines whether this parameter is mandatory.
     * If the parameter location is "path", this property is required and its value must be true.
     * Otherwise, the property may be included and its default value is false.
     */
    required?: boolean;
    /**
     * The schema defining the type used for the parameter.
     * It is only used when "in" is set to "body".
     */
    schema?: Schema;
    /**
     * The type of the parameter.
     * It is only used when "in" is set to "query", "header", "formData" or "path".
     */
    type?: string;
    /**
     * The format of the parameter.
     * It is only used when "in" is set to "query", "header", "formData" or "path".
     */
    format?: string;
    /**
     * A map of possible values for the parameter.
     * This is only used when the "type" property is set to "string" and the "enum" property is not specified.
     */
    enum?: Array<string>;
    /**
     * The default value of the parameter.
     */
    default?: any;
    /**
     * Determines whether the parameter value should allow multiple values.
     * If "in" is "query" and "type" is "array", the "collectionFormat" property specifies how multiple values are delimited.
     */
    allowMultiple?: boolean;
    /**
     * Determines how multiple parameter values are delimited.
     * It is only used when "in" is set to "query" and "allowMultiple" is true.
     * Possible values are "csv", "ssv", "tsv", "pipes" or "multi".
     */
    collectionFormat?: "csv" | "ssv" | "tsv" | "pipes" | "multi";
    /**
     * A schema defining the shape of the parameter's value.
     * It is only used when "in" is set to "formData".
     */
    items?: Items;
    /**
     * An example of the parameter's value.
     */
    example?: any;
    /**
     * An object containing information about the expected shape of the parameter.
     * The keys of this object represent the name of the property expected and its value is a schema defining the expected shape of the property value.
     * It is only used when "in" is set to "formData".
     */
    properties?: Record<string, Schema>;
    /**
     * Specifies a reference to a parameter defined in the OpenAPI spec.
     * This is used when a parameter is not directly defined within the operation object.
     */
    $ref?: string;
}


/**
 * A limited subset of JSON-Schema's items object. It is used by parameter definitions that are not located in "body".
 */
export interface Items {
    /**
     * Required. The internal type of the array. The value MUST be one of "string", "number", "integer", "boolean", or "array". 
     * Files and models are not allowed.
     */
    type: string;
    /**
     * The extending format for the previously mentioned type. See Data Type Formats for further details.
     */
    format?: string;
    /**
     * Required if type is "array". Describes the type of items in the array.
     */
    items?: Items | Items[];
    /**
     * Determines the format of the array if type array is used. Possible values are:
        csv - comma separated values foo,bar.
        ssv - space separated values foo bar.
        tsv - tab separated values foo\tbar.
        pipes - pipe separated values foo|bar.
        Default value is csv.
     */
    collectionFormat?: string;
    /**
     * Declares the value of the item that the server will use if none is provided. 
     * (Note: "default" has no meaning for required items.) See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-6.2.
     * Unlike JSON Schema this value MUST conform to the defined type for the data type.
     */
    default?: any;
    /**
     * See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.1.2.
     */
    maximum?: number;
    /**
     * See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.1.2.
     */
    exclusiveMaximum?: boolean;
    /**
     * See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.1.2.
     */
    minimum?: number;
    /**
     * See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.1.2.
     */
    exclusiveMinimum?: boolean;
    /**
     * See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.1.2.
     */
    maxLength?: number;
    /**
     * See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.1.2.
     */
    minLength?: number;
    /**
     * See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.1.2.
     */
    pattern?: string;
    /**
     * See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.1.2.
     */
    maxItems?: number;
    /**
     * See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.1.2.
     */
    minItems?: number;
    /**
     * See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.1.2.
     */
    uniqueItems?: boolean;
    /**
     * See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.5.1.
     */
    enum?: any[];
    /**
     * See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.5.1.
     */
    multipleOf?: number;
}

/**
 * The object provides a description of the response.
 * @see https://swagger.io/specification/v2/#responses-object
 */
export interface Response {
    /**
     * A short description of the response. GFM syntax can be used for rich text representation.
     */
    description: string;
    /**
     * A definition of the response structure. It can be a primitive, an array or an object.
     */
    schema?: Schema | Reference;
    /**
     * A list of headers that are sent with the response.
     */
    headers?: Record<string, HeaderObject | Reference>;
    /**
     * An example of the response message.
     */
    examples?: Record<string, any>;
}


/**
 * Swagger2 Header对象接口定义
 */
export interface HeaderObject {
    /**
     * header的描述
     */
    description?: string;
    /**
     * header的类型
     * Required. The type of the object. The value MUST be one of "string", "number", "integer", "boolean", or "array".
     */
    type: string;
    /**
     * header的格式
     * The extending format for the previously mentioned type. See Data Type Formats for further details.
     */
    format?: string;
    /**
     * 是否允许参数为空
     */
    allowEmptyValue?: boolean;
    /**
     * 重写父级的header属性
     */
    items?: Items | Items[];
    /**
     * 指定值列表
     */
    enum?: string[];
    /**
     * 值最小长度
     */
    minLength?: number;
    /**
     * 值最大长度
     */
    maxLength?: number;
    /**
     * 值最小值
     */
    minimum?: number;
    /**
     * 值最大值
     */
    maximum?: number;
    /**
     * 是否必须传递
     */
    required?: boolean;
}



/**
 * The Schema Object allows the definition of input and output data types. 
 * These types can be objects, but also primitives and arrays. 
 * This object is based on the JSON Schema Specification Draft 4 and uses a predefined subset of it. 
 * On top of this subset, there are extensions provided by this specification to allow for more complete documentation.
 * 
 * Further information about the properties can be found in JSON Schema Core and JSON Schema Validation. 
 * Unless stated otherwise, the property definitions follow the JSON Schema specification as referenced here.
 * 
 * The following properties are taken directly from the JSON Schema definition and follow the same specifications:
 *  $ref - As a JSON Reference
 *  format (See Data Type Formats for further details)
 *  title
 *  description (GFM syntax can be used for rich text representation)
 *  default (Unlike JSON Schema, the value MUST conform to the defined type for the Schema Object)
 *  multipleOf
 *  maximum
 *  exclusiveMaximum
 *  minimum
 *  exclusiveMinimum
 *  maxLength
 *  minLength
 *  pattern
 *  maxItems
 *  minItems
 *  uniqueItems
 *  maxProperties
 *  minProperties
 *  required
 *  enum
 *  type
 * 
 * 
 * The following properties are taken from the JSON Schema definition but their definitions were adjusted to the Swagger Specification. 
 * Their definition is the same as the one from JSON Schema, only where the original definition references the JSON Schema definition, 
 * the Schema Object definition is used instead.
 *  items
 *  allOf
 *  properties
 *  additionalProperties
 * Other than the JSON Schema subset fields, the following fields may be used for further schema documentation.
 */
export interface Schema {
    /**
     * Schema 类型，包括 "integer", "number", "string", "boolean", "array", "file" 或者 "object" 等
     */
    type?: string;
    /**
     * 指定可接受的 MIME 类型
     */
    format?: string;
    /**
     * 可以是任意值，只要不和其它字段定义冲突即可
     */
    title?: string;
    /**
     * 可以是任意值，只要不和其它字段定义冲突即可
     */
    description?: string;
    /**
     * 如果是枚举值类型，则将其包含的值以数组的形式列出来
     */
    enum?: any[];
    /**
     * 声明数组包含的对象类型
     */
    items?: Schema | Reference;
    /**
     * 声明对象类型所包含的属性
     */
    properties?: Record<string, Schema | Reference>;
    /**
     * 声明可扩展的属性
     */
    additionalProperties?: Schema | Reference | boolean;
    /**
     * 对象类型所必须包含的属性列表
     */
    required?: string[];
    /**
     * 用于指定文件上传
     */
    in?: string;
    /**
     * 对象类型的附加限制
     */
    maxProperties?: number;
    minProperties?: number;
    /**
     * 声明是否为只读
     */
    readOnly?: boolean;
    /**
     * 用于指定对象类型的父类型
     */
    allOf?: (Schema | Reference)[];
    /**
     * 用于指定对象类型所实现的接口
     */
    implements?: (Schema | Reference)[];
    /**
     * 用于指定对象类型所继承的类型
     */
    extends?: (Schema | Reference)[];
    /**
     * 定义允许的最大值
     */
    maximum?: number;
    exclusiveMaximum?: boolean;
    /**
     * 定义允许的最小值
     */
    minimum?: number;
    exclusiveMinimum?: boolean;
    /**
     * 定义一个正则表达式，用于匹配字符串类型的值
     */
    pattern?: string;
    /**
     * 数字类型所允许的最大值和最小值
     */
    multipleOf?: number;
    /**
     * 定义字符串类型所允许的最大长度和最小长度
     */
    maxLength?: number;
    minLength?: number;
    /**
     * 数组类型所允许的最大长度和最小长度
     */
    maxItems?: number;
    minItems?: number;
    /**
     * 定义数组类型是否允许包含重复元素
     */
    uniqueItems?: boolean;
}