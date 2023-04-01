import { SpecParserFactory } from './models';
import { SpecType } from './models/type';

export { default as Menu } from './core/menu';

const parserFactory = new SpecParserFactory();
const parser = parserFactory.getParser(SpecType.Swagger);

console.log(parser);
