import { default as Document } from './document/index'
import { default as Server } from './server/index'



export default interface Knife4jOpenAPI {
    document: Document,
    server: Server
}