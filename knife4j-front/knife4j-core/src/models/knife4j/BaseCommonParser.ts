import { Knife4jParseOptions } from "./Knife4jParseOptions";
import { ISpecParser } from "./ISpecParser";
import { Knife4jPathItemObject } from "./operation/Knife4jPathItemObject";
import { Knife4jInstance } from "./Knife4jInstance";

/**
 * 基础抽象类解析，后期提取Common方法供子类调用。
 */
export abstract class BaseCommonParser implements ISpecParser {
    public test(): void {
        console.log('He2llo.');
    }
    /**
     * 定义 ISpecParser 接口，包含 parse 方法
     * @param data  规范数据源输入，JSON字符串
     * @param options  个性化解析配置项
     */
    abstract parse(data: Record<string, any>, options: Knife4jParseOptions): Knife4jInstance;

    /**
     * 异步解析Path节点，只有在打开文档展示页的情况下才解析该配置，避免前端解析渲染性能问题
     * @param instance path节点的数据
     * @param options 个性化解析配置选项
     */
    abstract parsePathAsync(operation: Knife4jPathItemObject, instance: Knife4jInstance, options: Knife4jParseOptions): void;
}