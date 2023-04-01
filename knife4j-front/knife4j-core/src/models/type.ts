/**
 * 定义解析器类型,目前支持的规范类型
 */
export enum SpecType {
    /**
     * 主要针对Swagger2规范
     */
    Swagger = 'swagger',
    /**
     * 针对OpenAPI3.x规范
     */
    OpenAPI = 'openapi',
    /**
     * 针对AsyncAPI规范
     */
    AsyncAPI = 'asyncapi',
}
