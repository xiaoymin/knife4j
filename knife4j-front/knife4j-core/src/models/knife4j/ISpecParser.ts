import { Knife4jParseOptions } from "./ParseOptions";
import { Knife4jPathItemObject } from "./operation/Knife4jPathItemObject";

/**
 * 抽象接口，不同子类需要实现该接口，主要实现包括Swagger2、OpenAPI3、AsyncAPI等等规范
 */
export interface ISpecParser {
    /**
     * 定义 ISpecParser 接口，包含 parse 方法
     * @param data 规范数据源输入，JSON字符串
     * @param options 个性化解析配置项
     */
    parse(data: Record<string, any>, options: Knife4jParseOptions): Knife4jInstance;

    /**
     * 异步解析Path节点，只有在打开文档展示页的情况下才解析该配置，避免前端解析渲染性能问题
     * @param operation Knife4j的Operation对象
     * @param data OpenAPI3\Swagger2\AsyncAPI规范数据
     * @param options 个性化解析配置选项
     */
    parsePathAsync(operation: Knife4jPathItemObject, data: Record<string, any>, options: Knife4jParseOptions): void;
}
