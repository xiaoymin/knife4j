import { SpecParserFactory } from "../../../models/index"
import { SpecType } from "../../../models/type"
import data from "./test.json"

test("testClassTransformer-o3", () => {

    let factory = new SpecParserFactory();
    let spec = factory.getParser(SpecType.OpenAPI);
    //console.log('name: Openapi3')
    let instance = spec.parse(data, {});
    //console.log(instance) sd 
    //console.log("tags:", instance.tags.length)
    //console.log("info:", instance.info)
    console.log(instance.tagNames)
    console.log("extDoc.")
    console.log(instance.extDoc)
    console.log("server")
    console.log(instance.servers)
    instance.resolveSinglePathAsync("/pet/findByStatus", "get")
})
