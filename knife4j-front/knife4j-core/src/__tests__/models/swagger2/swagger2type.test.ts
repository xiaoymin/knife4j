import { Contact, Swagger } from '../../../models/swagger2/types'
import SwagggerData from './swaggerdata'


test("testClassTransformer", () => {
    const json = {
        name: 'Knife4j',
        url: 'https://doc.xiaominfo.com'
    };
    const contactInstance: Contact = Object.assign({}, json) as Contact;
    expect(contactInstance.name).toBe(json.name);
    console.log('name:', contactInstance.name)
})

test("swaggerdata", () => {
    const swaggerInfo: Swagger = Object.assign({}, SwagggerData) as Swagger;
    console.log(swaggerInfo.info.title + ":" + swaggerInfo.info.description + ",version:" + swaggerInfo.info.version)
})


