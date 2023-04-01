import { BaseCommonParser, ParseOptions } from '../index'
import { Knife4jInstance } from '../knife4j/type'

/**
 * 解析Swagger2的规范
 */
export class SwaggerParser extends BaseCommonParser {

    parse(data: Record<string, any>, options: ParseOptions): Knife4jInstance {

        return new Knife4jInstance();
    }
}

