import { BaseCommonParser, ParseOptions } from '../knife4j/baseParse'
import { Knife4jInstance } from '../knife4j/type'

/**
 * 解析Swagger2的规范
 */
export class SwaggerParser extends BaseCommonParser {

    parse(data: Record<string, any>, options: ParseOptions): Knife4jInstance {
        console.log("swagger-parser")
        console.log(data)
        console.log(options)

        return new Knife4jInstance('','','');
    }
}

