import { Knife4jInstance } from "./type"
import { Knife4jPathItemObject } from "./knife4jPath";
/**
 * 解析个性化自定义参数配置选项
 */
export interface ParseOptions {
  allowCustomTags?: boolean;
  validateSchema?: boolean;
}

/**
 * 抽象接口，不同子类需要实现该接口，主要实现包括Swagger2、OpenAPI3、AsyncAPI等等规范
 */
export interface ISpecParser {
  /**
   * 定义 ISpecParser 接口，包含 parse 方法
   * @param data 规范数据源输入，JSON字符串
   * @param options 个性化解析配置项
   */
  parse(data: Record<string, any>, options: ParseOptions): Knife4jInstance;

  /**
   * 异步解析Path节点，只有在打开文档展示页的情况下才解析该配置，避免前端解析渲染性能问题
   * @param operation Knife4j的Operation对象
   * @param data OpenAPI3\Swagger2\AsyncAPI规范数据
   * @param options 个性化解析配置选项
   */
  parsePathAsync(operation: Knife4jPathItemObject, data: Record<string, any>, options: ParseOptions): void;
}

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
  abstract parse(data: Record<string, any>, options: ParseOptions): Knife4jInstance;

  /**
   * 异步解析Path节点，只有在打开文档展示页的情况下才解析该配置，避免前端解析渲染性能问题
   * @param data path节点的数据
   * @param options 个性化解析配置选项
   */
  abstract parsePathAsync(operation: Knife4jPathItemObject, data: Record<string, any>, options: ParseOptions): void;
}