import {Knife4jInstance} from "./type"
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
  }
  
  /**
   * 基础抽象类解析，后期提取Common方法供子类调用。
   */
  export abstract class BaseCommonParser implements ISpecParser {
    public test(): void {
      console.log('Hello.');
    }
    /**
     * 定义 ISpecParser 接口，包含 parse 方法
     * @param data  规范数据源输入，JSON字符串
     * @param options  个性化解析配置项
     */
    abstract parse(data: Record<string, any>, options: ParseOptions): Knife4jInstance;
  }