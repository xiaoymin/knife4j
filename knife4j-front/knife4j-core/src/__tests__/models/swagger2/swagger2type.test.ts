import { Contact } from '../../../models/swagger2/types'

test("testClassTransformer", () => {
    const json = {
        name: 'Knife4j',
        url: 'https://doc.xiaominfo.com'
    };
    const contactInstance: Contact = Object.assign({}, json) as Contact;
    expect(contactInstance.name).toBe(json.name);
    console.log('name:', contactInstance.name)
})




