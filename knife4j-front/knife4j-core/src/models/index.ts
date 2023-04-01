import { SpecType } from './type'
import { Knife4jInstance } from './knife4j/type';
import { OpenAPIParser } from './openapi3/parse';
import { SwaggerParser } from './swagger2/parse';
import { AsyncAPIParser } from './asyncapi/parse';


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
        console.log('Hello.')
    }
    /**
     * 定义 ISpecParser 接口，包含 parse 方法
     * @param data  规范数据源输入，JSON字符串
     * @param options  个性化解析配置项
     */
    abstract parse(data: Record<string, any>, options: ParseOptions): Knife4jInstance;
}


/**
 * 工厂类
 */
export class SpecParserFactory {
    /**
     * 工厂Map
     */
    private parsers: Map<SpecType, ISpecParser> = new Map();

    constructor() {
        this.registerParser(SpecType.Swagger, new SwaggerParser());
        this.registerParser(SpecType.OpenAPI, new OpenAPIParser());
        this.registerParser(SpecType.AsyncAPI, new AsyncAPIParser());
    }

    /**
     * 注册实例
     * @param specType 规范类型
     * @param parser 转换器
     */
    public registerParser(specType: SpecType, parser: ISpecParser): void {
        this.parsers.set(specType, parser);
    }

    /**
     * 获取parse实例对象
     * @param parserType 规范类型
     * @returns 
     */
    public getParser(parserType: SpecType): ISpecParser {
        const parser = this.parsers.get(parserType);

        if (!parser) {
            throw new Error(`Parser not found`);
        }
        return parser;
    }

}


