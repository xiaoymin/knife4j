import Document from '../core/document/index'
import Server from '../core/server/index'

import OpenAPI from '../core/index'

const serv: Server = {
    url: "http://www.google.com",
    description: "Google"
}
const myDoc: Document = {
    title: '我是文档'
}

const testOpen: OpenAPI = {
    server: serv,
    document: myDoc
}

test("print", () => {
    console.log(myDoc.title)

    console.log('server url:' + testOpen.server.url)
})


