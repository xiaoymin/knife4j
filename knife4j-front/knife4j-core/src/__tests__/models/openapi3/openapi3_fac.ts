import { SpecParserFactory } from "../../../models/index"
import { SpecType } from "../../../models/type"

import data from "./test.json"

test("testClassTransformer-o3", () => {

    let factory = new SpecParserFactory();
    let parser = factory.getParser(SpecType.OpenAPI);
    console.log('name: Openapi3')
    console.log(data)
    console.log(parser.parse({ "aa": "ccc" }, {}))
})
