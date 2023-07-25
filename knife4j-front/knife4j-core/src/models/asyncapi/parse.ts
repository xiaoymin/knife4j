import { BaseCommonParser, ParseOptions } from '../knife4j/baseParse';

import { Knife4jInstance } from '../knife4j/type';

/**
 * 解析AsyncAPI的规范
 */
export class AsyncAPIParser extends BaseCommonParser {
  parse(data: Record<string, any>, options: ParseOptions): Knife4jInstance {
    return new Knife4jInstance('','','');
  }
}
