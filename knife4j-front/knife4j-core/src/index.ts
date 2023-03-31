
export { default as Menu } from './core/menu';


import { OpenAPIParserFactory, OpenAPIParserConfig } from './openapi/parser';
import { AsyncAPIParserFactory, AsyncAPIParserConfig } from './asyncapi/parser';
import { SwaggerParserFactory, SwaggerParserConfig } from './swagger/parser';
import { ParserStrategyFactory, ParserStrategyType } from './parser-strategy-factory';
import { Command } from './command';
import { SomeComponent } from './some-component';

// 选择要解析的规范类型
enum SpecificationType {
    OpenAPI = 'OpenAPI',
    AsyncAPI = 'AsyncAPI',
    Swagger = 'Swagger'
}

// 解析器的总入口函数
async function parseSpecification(specificationType: SpecificationType, specificationFilePath: string) {
    // 初始化解析器配置
    let parserConfig: OpenAPIParserConfig | AsyncAPIParserConfig | SwaggerParserConfig;
    switch (specificationType) {
        case SpecificationType.OpenAPI:
            parserConfig = { ... };
            break;
        case SpecificationType.AsyncAPI:
            parserConfig = { ... };
            break;
        case SpecificationType.Swagger:
            parserConfig = { ... };
            break;
        default:
            throw new Error('Unsupported specification type');
    }

    // 创建解析器实例
    let parserFactory;
    switch (specificationType) {
        case SpecificationType.OpenAPI:
            parserFactory = new OpenAPIParserFactory();
            break;
        case SpecificationType.AsyncAPI:
            parserFactory = new AsyncAPIParserFactory();
            break;
        case SpecificationType.Swagger:
            parserFactory = new SwaggerParserFactory();
            break;
    }
    const parser = parserFactory.create(parserConfig);

    // 创建解析策略实例
    const strategyFactory = new ParserStrategyFactory();
    const strategyType = strategyFactory.getStrategyType(specificationType);
    const strategy = strategyFactory.create(strategyType, parser);

    // 解析规范文件
    const parsedResult = await parser.parse(specificationFilePath, strategy);

    // 执行命令
    const command = new Command();
    command.execute(parsedResult);

    // 处理解析结果
    const component = new SomeComponent();
    component.handleResult(parsedResult);
}

// 使用方式示例
parseSpecification(SpecificationType.OpenAPI, 'path/to/openapi.yaml');


