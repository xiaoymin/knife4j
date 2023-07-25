import { SpecParserFactory } from "../../../models/index"
import { SpecType } from "../../../models/type"

import data from "./test.json"

test("testClassTransformer-o3", () => {

    let factory = new SpecParserFactory();
    let spec = factory.getParser(SpecType.OpenAPI);
    console.log('name: Openapi3')
    let instance = spec.parse(data, {});
    //console.log(instance)
    console.log("tags:", instance.tags.length)
    instance.tags.forEach(tag => {
        console.log(tag)
    })
})
