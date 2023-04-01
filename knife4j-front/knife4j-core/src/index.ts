import { SpecParserFactory } from './models';
import { SpecType } from './models/type';

export { default as Menu } from './core/menu';



let parserFactory = new SpecParserFactory();
let parser = parserFactory.getParser(SpecType.Swagger);

console.log(parser)