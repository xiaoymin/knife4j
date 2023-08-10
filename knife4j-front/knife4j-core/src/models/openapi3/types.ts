/**
 * OpenAPI结构
 */
export type OpenAPI = {
  /**
   * REQUIRED. This string MUST be the semantic version number of the OpenAPI Specification version that the OpenAPI document uses.
   * The openapi field SHOULD be used by tooling specifications and clients to interpret the OpenAPI document.
   * This is not related to the API info.version string.
   */
  openapi: string;

  /**
   * Provides metadata about the API.
   */
  info: InfoObject;

  /**
   * An array of Server Objects, which provide connectivity information to a target server.
   */
  servers?: ServerObject[];

  /**
   * The available paths and operations for the API.
   */
  paths: PathsObject;

  /**
   * The incoming webhooks that MAY be received as part of this API and that the API consumer MAY choose to implement.
   */
  webhooks?: { [expression: string]: WebhookObject | ReferenceObject };

  /**
   * Holds the relative paths to the individual endpoints and their operations. The path is appended to the URL from the Server Object in order to construct the full URL.
   * The Paths MAY be empty, due to ACL constraints.
   */
  components?: ComponentsObject;

  /**
   * A declaration of which security mechanisms can be used across the API.
   */
  security?: SecurityRequirementObject[];

  /**
   * A list of tags used by the specification with additional metadata.
   * The order of the tags can be used to reflect on their order by the parsing tools.
   * Not all tags that are used by the Operation Object must be declared.
   * The tags that are not declared MAY be organized randomly or based on the tools' logic.
   * Each tag name in the list MUST be unique.
   */
  tags?: TagObject[];

  /**
   * Additional external documentation.
   */
  externalDocs?: ExternalDocumentationObject;
};

/**
 * The object provides metadata about the API. The metadata can be used by the clients if needed.
 */
export type InfoObject = {
  /**
   * The title of the API.
   */
  title: string;

  /**
   * A short summary of the API.
   */
  summary?: string;

  /**
   * The version of the OpenAPI document (which is distinct from the OpenAPI Specification version or the API implementation version).
   */
  version: string;

  /**
   * A verbose explanation of the API. CommonMark syntax MAY be used for rich text representation.
   */
  description?: string;

  /**
   * A URL to the Terms of Service for the API. MUST be in the format of a URL.
   */
  termsOfService?: string;

  /**
   * The contact information for the exposed API.
   */
  contact?: ContactObject;

  /**
   * The license information for the exposed API.
   */
  license?: LicenseObject;
};

/**
 * Contact information for the exposed API.
 */
export type ContactObject = {
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
};

/**
 * License information for the exposed API.
 */
export type LicenseObject = {
  /**
   * The license name used for the API.
   */
  name: string;

  /**
   * A URL to the license used for the API. MUST be in the format of a URL.
   */
  url?: string;
};

/**
 * Holds the relative paths to the individual endpoints and their operations.
 * The path is appended to the URL from the Server Object in order to construct the full URL. The Paths MAY be empty, due to ACL constraints.
 */
export type PathsObject = {
  /**
   * A relative path to an individual endpoint. The field name MUST begin with a slash. The path is appended to the URL from the Server Object in order to construct the full URL. Path templating is allowed.
   */
  [path: string]: PathItemObject;
};

/**
 * An object representing a Server.
 */
export type ServerObject = {
  /**
   * A URL to the target host. This URL supports Server Variables and MAY be relative, to indicate that the host location is relative to the location where the OpenAPI document is being served.
   * Variable substitutions will be made when a variable is named in {brackets}.
   */
  url: string;

  /**
   * An optional string describing the host designated by the URL.
   * CommonMark syntax MAY be used for rich text representation.
   */
  description?: string;

  /**
   * A map between a variable name and its value.
   * The value is used for substitution in the server's URL template.
   */
  variables?: { [variable: string]: ServerVariableObject };
};

/**
 * Defines a Webhook for an operation.
 */
export type WebhookObject = {
  /**
   * A URL to register for the webhook.
   * Will be called when the defined event occurs.
   */
  url: string;

  /**
   * A map of string keys to replace the template parameters in the Webhook URL (if present).
   * The name of a parameter MUST match the name of a path or query parameter defined in the path field in which the webhook is defined,
   * or a Property Field if it is defined at the top level.
   * This property MAY be a map, an array of maps, or a string that identifies a map in the Components Object.
   * For more details, see the [Webhooks tutorial](https://swagger.io/docs/specification/using-the-specification/webhooks/).
   */
  binding?: Record<string, string | Record<string, string>> | string;
};

/**
 * Holds a set of reusable objects for different aspects of the OAS.
 * All objects defined within the components object will have no effect on the API unless they are explicitly referenced from properties outside the components object.
 */
export type ComponentsObject = {
  /**
   * An object to hold reusable Schema Objects.
   */
  schemas?: Record<string, SchemaObject>;

  /**
   * An object to hold reusable Response Objects.
   */
  responses?: Record<string, ResponseObject>;

  /**
   * An object to hold reusable Parameter Objects.
   */
  parameters?: Record<string, ParameterObject>;

  /**
   * An object to hold reusable Example Objects.
   */
  examples?: Record<string, ExampleObject>;

  /**
   * An object to hold reusable Request Body Objects.
   */
  requestBodies?: Record<string, RequestBodyObject>;

  /**
   * An object to hold reusable Header Objects.
   */
  headers?: Record<string, HeaderObject>;

  /**
   * An object to hold reusable Security Scheme Objects.
   */
  securitySchemes?: Record<string, SecuritySchemeObject>;

  /**
   * An object to hold reusable Link Objects.
   */
  links?: Record<string, LinkObject>;

  /**
   * An object to hold reusable Callback Objects.
   */
  callbacks?: Record<string, CallbackObject>;
};

/**
 * Adds metadata to a single tag that is used by the Operation Object.
 * It is not mandatory to have a Tag Object per tag defined in the Operation Object instances.
 */
export type TagObject = {
  /**
   * REQUIRED. The name of the tag.
   */
  name: string;

  /**
   * A short description for the tag. CommonMark syntax MAY be used for rich text representation.
   */
  description?: string;

  /**
   * Knife4j扩展属性，排序规则
   */
  "x-order"?: number;

  /**
   * Additional external documentation for this tag.
   */
  externalDocs?: ExternalDocumentationObject;
};

/**
 * An object representing a Server Variable for server URL template substitution.
 */
export type ServerVariableObject = {
  /**
   * An enumeration of string values to be used if the substitution options are from a limited set.
   */
  enum?: string[];

  /**
   * REQUIRED. The default value to use for substitution, which SHALL be sent if an alternate value is not supplied.
   * Note this behavior is different than the Schema Object's treatment of default values, because in those cases parameter values are optional.
   */
  default: string;

  /**
   * An optional description for the server variable. CommonMark syntax MAY be used for rich text representation.
   */
  description?: string;
};

/**
 * When request bodies or response payloads may be one of a number of different schemas, a `discriminator` object can be used to aid in serialization, deserialization, and validation.
 * The discriminator is a specific object in a schema which is used to inform the consumer of the specification of an alternative schema based on the value associated with it.
 * When using the discriminator, inline schemas will not be considered.
 *
 * A discriminator MUST be a required property.
 *
 * When used, the value of the discriminator property MUST be the name of the schema or any schema that inherits from it.
 */
export type DiscriminatorObject = {
  /**
   * REQUIRED. The name of the property in the payload that will hold the discriminator value.
   */
  propertyName: string;

  /**
   * An object to hold mappings between payload values and schema names or references.
   *
   * The keys MUST match the value of the discriminator property of the parent schema, and the values MUST be either a valid JSON Schema or a reference to a valid JSON Schema.
   */
  mapping: Record<string, string>;
};

/**
 * A metadata object that allows for more fine-tuned XML model definitions.
 */
export type XMLObject = {
  /**
   * Replaces the name of the element/attribute used for the described schema property.
   * When defined within `items`, it will affect the name of the individual XML elements within the list.
   * When defined alongside `type` being array (outside the `items`), it controls the name of the wrapper element.
   * If just one `XMLObject` is defined, `XMLObject` can be given as a shorthand for the full form.
   */
  name?: string;

  /**
   * The URL of the namespace definition. Value should be in the form of an absolute URI.
   */
  namespace?: string;

  /**
   * The prefix to be used for the name.
   */
  prefix?: string;

  /**
   * Declares whether the property definition translates to an attribute instead of an element.
   * Default value is `false`.
   */
  attribute?: boolean;

  /**
   * May be used only for an array definition.
   * Signifies whether the array is wrapped (for example, `<books><book/><book/></books>`) or unwrapped (`<book/><book/>`).
   * Default value is `false`.
   * The definition takes effect only when defined alongside `type` being `array` (outside the `items`).
   */
  wrapped?: boolean;
};

/**
 * Schema Object
 * https://datatracker.ietf.org/doc/html/draft-bhutton-json-schema-validation-00#page-6
 */
export type SchemaObject = {
  /**
   * Title of the schema.
   */
  title?: string;

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
  required?: string[];

  /**
   * Possible values for an object or array.
   */
  enum?: any[];

  /**
   * Data type of the schema.
   */
  type?: string | string[];

  /**
   * List of schemas that this schema must conform to all of.
   */
  allOf?: SchemaObject[];
  /**
     * Reference to the object.
     */
  $ref?: string;
  /**
   * List of schemas that this schema must conform to one of.
   */
  oneOf?: SchemaObject[];

  /**
   * List of schemas that this schema may conform to.
   */
  anyOf?: SchemaObject[];

  /**
   * Schema that this schema must not conform to.
   */
  not?: SchemaObject;

  /**
   * Schema(s) for the items in an array.
   */
  items?: SchemaObject;

  /**
   * List of properties in an object.
   */
  properties?: Record<string, SchemaObject>;

  /**
   * Whether additional properties are allowed in an object.
   */
  additionalProperties?: boolean | SchemaObject;

  /**
   * Description of the schema.
   */
  description?: string;

  /**
   * Format of the data.
   */
  format?: string;

  /**
   * Default value for the data.
   */
  default?: any;

  /**
   * Whether null is allowed.
   */
  nullable?: boolean;

  /**
   * Object for handling polymorphism.
   */
  discriminator?: DiscriminatorObject;

  /**
   * Whether the data is read-only.
   */
  readOnly?: boolean;

  /**
   * Whether the data is write-only.
   */
  writeOnly?: boolean;

  /**
   * Object for handling XML.
   */
  xml?: XMLObject;

  /**
   * External documentation for the schema.
   */
  externalDocs?: ExternalDocumentationObject;

  /**
   * Example of the data.
   */
  example?: any;

  /**
   * Whether the schema is deprecated.
   */
  deprecated?: boolean;
};

/**
 * Reference Object
 */
export type ReferenceObject = {
  /**
   * Reference to the object.
   */
  $ref: string;
};

/**
 * Security Requirement Object
 */
export type SecurityRequirementObject = {
  /**
   * Map of security schemes to their required scopes.
   */
  [name: string]: string[];
};

/**
 * Security Scheme Object
 */
export type SecuritySchemeObject = {
  /**
   * Type of security scheme.
   */
  type: string;

  /**
   * Description of the security scheme.
   */
  description?: string;

  /**
   * Name of the security scheme.
   */
  name?: string;

  /**
   * Location of the API key.
   */
  in?: string;

  /**
   * URL of the authorization server.
   */
  scheme?: string;

  /**
   * OpenID Connect URL to discover OAuth2 configuration values.
   */
  openIdConnectUrl?: string;

  /**
   * Map of OAuth2 flows to their configuration.
   */
  flows?: OAuthFlowsObject;
};

/**
 * Allows configuration of the supported OAuth Flows.
 */
export type OAuthFlowsObject = {
  /**
   * Configuration for the OAuth Implicit flow.
   */
  implicit?: OAuthFlowObject;

  /**
   * Configuration for the OAuth Resource Owner Password flow.
   */
  password?: OAuthFlowObject;

  /**
   * Configuration for the OAuth Client Credentials flow.
   * Previously called `application` in OpenAPI 2.0.
   */
  clientCredentials?: OAuthFlowObject;

  /**
   * Configuration for the OAuth Authorization Code flow.
   */
  authorizationCode?: OAuthFlowObject;
};

/**
 * OAuth Flow Object
 */
export type OAuthFlowObject = {
  /**
   * Authorization URL for the flow.
   */
  authorizationUrl: string;

  /**
   * Token URL for the flow.
   */
  tokenUrl: string;

  /**
   * URL for the OAuth2 client to redirect to after authorization.
   */
  redirectUrl?: string;

  /**
   * Scopes available for the flow.
   */
  scopes?: Record<string, string>;
};

/**
 * External Documentation Object
 */
export type ExternalDocumentationObject = {
  /**
   * URL for the external documentation.
   */
  url: string;

  /**
   * Brief description of the external documentation.
   */
  description?: string;
};

/**
 * Examples Object
 */
export type ExamplesObject = {
  /**
   * Map of media types to their examples.
   */
  [mimeType: string]: ExampleObject | ReferenceObject;
};

/**
 * Example Object
 */
export type ExampleObject = {
  /**
   * Brief description of the example.
   */
  summary?: string;

  /**
   * Detailed description of the example.
   */
  description?: string;

  /**
   * Value of the example.
   */
  value?: any;

  /**
   * URL to the external example.
   */
  externalValue?: string;
  /**
   * Reference to the object.
   */
  $ref?: string;
};

/**
 * Link Parameters Object
 */
export type LinkParametersObject = {
  /**
   * Parameters to be passed to the target link.
   */
  [name: string]: any;
};

/**
 * Encoding Object
 */
export type EncodingObject = {
  /**
   * Media type the encoding applies to.
   */
  contentType?: string;

  /**
   * Map of headers to their encoding properties.
   */
  headers?: Record<string, HeaderObject | ReferenceObject>;

  /**
   * Style of encoding.
   */
  style?: string;

  /**
   * Describes how arrays and objects should be serialized.
   */
  explode?: boolean;

  /**
   * Whether the encoding is mandatory.
   */
  allowReserved?: boolean;
};

/**
 * Link Object
 */
export type LinkObject = {
  /**
   * A relative or absolute URL that resolves to an OAS operation.
   */
  operationRef?: string;

  /**
   * An OAS operation to use for this link.
   */
  operationId?: string;

  /**
   * Map of parameters to be passed to the linked operation.
   */
  parameters?: LinkParametersObject;

  /**
   * Description of the link.
   */
  description?: string;

  /**
   * Server object to be used for this link.
   */
  server?: ServerObject;
};

/**
 * Callback Object
 */
export type CallbackObject = {
  /**
   * A map of possible out-of band callbacks related to the parent operation.
   */
  [expression: string]: PathItemObject | ReferenceObject;
};

/**
 * Describes the operations available on a single path.
 * A Path Item MAY be empty, due to ACL constraints.
 * The path itself is still exposed to the documentation viewer but they will not know which operations and parameters are available.
 */
export type PathItemObject = {
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
   * A definition of a GET operation on this path.
   */
  get?: OperationObject;

  /**
   * A definition of a PUT operation on this path.
   */
  put?: OperationObject;

  /**
   * A definition of a POST operation on this path.
   */
  post?: OperationObject;

  /**
   * A definition of a DELETE operation on this path.
   */
  delete?: OperationObject;

  /**
   * A definition of a OPTIONS operation on this path.
   */
  options?: OperationObject;

  /**
   * A definition of a HEAD operation on this path.
   */
  head?: OperationObject;

  /**
   * A definition of a PATCH operation on this path.
   */
  patch?: OperationObject;

  /**
   * A definition of a TRACE operation on this path.
   */
  trace?: OperationObject;

  /**
   * An alternative server array to service all operations in this path.
   */
  servers?: ServerObject[];

  /**
   * A list of parameters that are applicable for all the operations described under this path.
   * These parameters can be overridden at the operation level, but cannot be removed there.
   * The list MUST NOT include duplicated parameters.
   * A unique parameter is defined by a combination of a name and location.
   * The list can use the Reference Object to link to parameters that are defined at the OpenAPI Object's components/parameters.
   */
  parameters?: ParameterObject[];
  /**
   * Allows extensions to the OpenAPI Schema. The field name MUST begin with x-, for example, x-internal-id. The value can be null, a primitive, an array or an object. Can have any valid JSON format value.
   */
  [key: string]: any;
};

/**
 * Operation Object
 */
export type OperationObject = {
  /**
   * A list of tags for API documentation control.
   */
  tags?: string[];

  /**
   * A short summary of what the operation does.
   */
  summary?: string;

  /**
   * A verbose explanation of the operation behavior.
   */
  description?: string;

  /**
   * Additional external documentation for this operation.
   */
  externalDocs?: ExternalDocumentationObject;

  /**
   * Unique string used to identify the operation.
   */
  operationId?: string;

  /**
   * A list of parameters that are applicable for this operation.
   */
  parameters?: ParameterObject[];

  /**
   * The request body applicable for this operation.
   */
  requestBody?: RequestBodyObject;

  /**
   * Expected responses for this operation.
   */
  responses: ResponsesObject;

  /**
   * A map of possible out-of band callbacks related to the parent operation.
   */
  callbacks?: CallbackObject;

  /**
   * Declares this operation to be deprecated.
   */
  deprecated?: boolean;

  /**
   * A declaration of which security mechanisms can be used for this operation.
   */
  security?: SecurityRequirementObject[];

  /**
   * An alternative server array to service this operation.
   */
  servers?: ServerObject[];
};

/**
 * Parameter Object
 */
export type ParameterObject = {
  /**
   * The name of the parameter.
   */
  name: string;

  /**
   * The location of the parameter.
   */
  in: string;
  /**
   * Reference to the object.
   */
  $ref?: string;
  /**
   * A brief description of the parameter. This could contain examples of use.
   */
  description?: string;

  /**
   * Determines whether this parameter is mandatory.
   */
  required?: boolean;

  /**
   * Specifies that a parameter is deprecated and SHOULD be transitioned out of usage.
   */
  deprecated?: boolean;

  /**
   * Sets the ability to pass empty-valued parameters.
   */
  allowEmptyValue?: boolean;

  /**
   * Describes how the parameter value will be serialized depending on the type of the parameter value.
   */
  style?: string;

  /**
   * When this is true, parameter values of type array or object generate separate parameters for each value of the array or key-value pair of the map.
   */
  explode?: boolean;

  /**
   * Determines whether the parameter value SHOULD allow reserved characters, as defined by RFC3986.
   */
  allowReserved?: boolean;

  /**
   * The schema defining the type used for the parameter.
   */
  schema?: SchemaObject;

  /**
   * Example of the media type. The example SHOULD match the specified schema and encoding properties if present.
   */
  example?: any;

  /**
   * Examples of the media type. Each example SHOULD contain a value in the correct format as specified in the parameter encoding. The examples object is mutually exclusive of the example object.
   */
  examples?: { [media: string]: ExampleObject | ReferenceObject };

  /**
   * A map containing the representations for the parameter. The key is the media type and the value describes it. The map MUST only contain one entry.
   */
  content?: { [media: string]: MediaTypeObject };
};

/**
 * Request Body Object
 */
export type RequestBodyObject = {
  /**
   * A brief description of the request body. This could contain examples of use.
   */
  description?: string;

  /**
   * The content of the request body. The key is a media type or media type range and the value describes it.
   */
  content: { [media: string]: MediaTypeObject };

  /**
   * Determines if the request body is required in the request. Defaults to false.
   */
  required?: boolean;

  /**
   * Reference to the object.
   */
  $ref?: string;
};

/**
 * A container for the expected responses of an operation.
 * The container maps a HTTP response status code to the expected response.
 * It is not expected from the documentation to necessarily cover all possible HTTP response codes, since they may not be known in advance.
 * However, it is expected from the documentation to cover a successful operation response and any known errors.
 * The default MAY be used as a default response object for all HTTP codes that are not covered individually by the specification.
 * The Responses Object MUST contain at least one response code, and it SHOULD be the response for a successful operation call.
 */
export type ResponsesObject = {
  /**
   * The documentation of responses other than the ones declared for specific HTTP response codes.
   * It can be used to cover undeclared responses.
   */
  [httpStatusCode: string]: ResponseObject;

  /**
   * The documentation of a successful response.
   */
  200: ResponseObject;

  /**
   * The documentation of responses to other error situations.
   */
  default: ResponseObject;
};

/**
 * Each Media Type Object provides schema and examples for the media type identified by its key.
 */
export type MediaTypeObject = {
  /**
   * The schema defining the content of the request, response, or parameter.
   */
  schema?: SchemaObject;

  /**
   * Example of the media type.
   * The example object SHOULD be in the correct format as specified by the media type.
   * The `example` field is mutually exclusive of the `examples` field.
   * Furthermore, if referencing a `schema` which contains an example, the `example` value SHALL override the example provided by the schema.
   */
  example?: unknown;

  /**
   * Examples of the media type.
   * Each example object SHOULD match the media type and specified schema if present.
   * The `examples` field is mutually exclusive of the `example` field.
   * Furthermore, if referencing a `schema` which contains an example, the `examples` value SHALL override the example provided by the schema.
   */
  examples?: { [media: string]: ExampleObject | ReferenceObject };

  /**
   * A map between a property name and its encoding information.
   * The key, being the property name, MUST exist in the schema as a property.
   * The encoding object SHALL only apply to `requestBody` objects when the media type is `multipart` or `application/x-www-form-urlencoded`.
   */
  encoding?: { [property: string]: EncodingObject };
};

/**
 * Response Object
 */
export type ResponseObject = {
  /**
   * A short description of the response.
   */
  description: string;

  /**
   * Maps a header name to its definition.
   */
  headers?: { [headerName: string]: HeaderObject | ReferenceObject };

  /**
   * A map containing descriptions of potential response payloads.
   */
  content?: { [media: string]: MediaTypeObject };

  /**
   * A map of operations links that can be followed from the response.
   */
  links?: { [link: string]: LinkObject | ReferenceObject };

  /**
  * Reference to the object.
  */
  $ref?: string;
};

/**
 * Header Object
 */
export type HeaderObject = {
  /**
   * A brief description of the header parameter. This could contain examples of use. CommonMark syntax MAY be used for rich text representation.
   */
  description?: string;

  /**
   * The schema defining the type used for the header parameter.
   */
  schema?: SchemaObject | ReferenceObject;

  /**
   * A string example.
   */
  example?: string;

  /**
   * Examples of the header parameter. Each example SHOULD contain a value in the correct format as specified in the parameter encoding. The examples object is mutually exclusive of the example object.
   */
  examples?: { [header: string]: ExampleObject | ReferenceObject };

  /**
   * Determines whether this parameter is mandatory. The default value is false.
   */
  required?: boolean;

  /**
   * Specifies that a parameter is deprecated and SHOULD be transitioned out of usage. Default value is false.
   */
  deprecated?: boolean;

  /**
   * Determines whether the header parameter value should allow reserved characters, as defined by RFC3986.
   * This defaults to false.
   */
  allowReserved?: boolean;

  /**
   * The style of the parameter value. Since there is no query parameter value, the value MUST be omitted.
   */
  style?: never;

  /**
   * When this is true, header parameters SHALL allow empty values. The default value is false.
   */
  allowEmptyValue?: boolean;
};
