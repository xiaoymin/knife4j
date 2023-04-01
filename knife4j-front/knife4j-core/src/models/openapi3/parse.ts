import { BaseCommonParser, ParseOptions } from '../index'

import { Knife4jInstance } from '../knife4j/type'

/**
 * 解析OpenAPI3的规范
 */
export class OpenAPIParser extends BaseCommonParser {

    parse(data: Record<string, any>, options: ParseOptions): Knife4jInstance {

        return new Knife4jInstance();
    }
}

