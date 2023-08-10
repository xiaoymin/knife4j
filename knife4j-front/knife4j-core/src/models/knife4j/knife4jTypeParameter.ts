/**
 * 定义解析器类型,目前支持的规范类型
 */
export enum Knife4jParamType {
    /**
     * header
     */
    header = 'header',
    /**
     * path
     */
    path = 'path',
    /**
     * query
     */
    query = 'query',

    /**
     * cookie
     */
    cookie = "cookie"
}
