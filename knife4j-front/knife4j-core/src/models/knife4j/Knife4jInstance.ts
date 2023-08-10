import lodash from "lodash"

import { Knife4jTagObject } from "./tag/Knife4jTagObject";
import { Knife4jInfoObject } from "./info/Knife4jInfoObject";
import { Knife4jExternalDocumentationObject } from "./externalDoc/Knife4jExternalDocumentationObject";
import { Knife4jServer } from "./servers/Knife4jServer";
import { Knife4jParseOptions } from "./Knife4jParseOptions";
import { Knife4jPathItemObject } from "./operation/Knife4jPathItemObject";
import { ISpecParser } from "./ISpecParser";

/**
 * 该类是所有parse方法最重输出的对象
 */
export class Knife4jInstance {
    // 原始结构数据
    originalRecord: Record<string, any> = {};
    /**当前规范数据的解析器，方便异步解析操作 */
    parseFactory: ISpecParser;
    parseOptions: Knife4jParseOptions;
    id: string;
    name: string;
    url: string;
    version: string;
    // 基础信息
    info: Knife4jInfoObject = new Knife4jInfoObject("Knife4j接口开发文档");
    //分组tag
    tagNames: Record<string, number> = {};
    tags: Array<Knife4jTagObject> = [];
    //分组接口
    paths: Array<Knife4jPathItemObject> = [];
    servers: Array<Knife4jServer> = [];
    //外部扩展配置
    extDoc?: Knife4jExternalDocumentationObject;
    /**
     * 构造函数
     * @param name 名称
     * @param location OpenAPI接口资源地址
     * @param version 版本，2.0或者3.0
     */
    constructor(name: string, location: string, version: string, factory: ISpecParser, options: Knife4jParseOptions) {
        this.id = "12";
        this.name = name;
        this.url = location;
        this.version = version;
        this.parseFactory = factory;
        this.parseOptions = options;
    }

    /**
     * 标签分组
     * @param tag 接口分组
     * @returns 
     */
    addTag(tag: Knife4jTagObject): void {
        if (tag === null) {
            return;
        }
        let keys = lodash.keys(this.tagNames)
        //避免重复添加
        if (!lodash.includes(keys, tag.name)) {
            // 初始化count为0
            this.tagNames[tag.name] = 0
            this.tags.push(tag)
        }
    }

    /**
     * tag分组下计数器+1
     * @param tagName tag名称
     */
    addTagCount(tagName: string): void {
        let keys = lodash.keys(this.tagNames)
        let count = 0
        if (lodash.includes(keys, tagName)) {
            //已经包含
            count = this.tagNames[tagName]
            count = count + 1
        }
        //重新赋值count统计
        this.tagNames[tagName] = count;
    }

    /**
     * 添加path
     * @param operation path对象
     */
    addOperation(operation: Knife4jPathItemObject): void {
        this.paths.push(operation);
    }

    /**
     * 设置外部文档对象
     * @param ext 设置外部文档对象
     */
    setExtDoc(ext: Knife4jExternalDocumentationObject) {
        this.extDoc = ext;
    }

    /**
     * 设置servers信息
     * @param server server信息
     */
    addServer(server: Knife4jServer) {
        this.servers.push(server)
    }

    /**
     * 根据接口路径path解析规则
     * @param path 接口路径path
     * @param methodType 接口类型，例如：post、get、delete等等
     */
    resolveSinglePathAsync(path: string, methodType: string) {
        if (lodash.isEmpty(path)) {
            return;
        }
        console.log("async path:", path)
        console.log(this.parseFactory)
        //从集合中找出当前对象
        const pathFilters = this.paths.filter(operation => operation.url == path && operation.methodType == methodType);
        if (lodash.isEmpty(pathFilters)) {
            return;
        }
        const pathObject = pathFilters[0];
        this.parseFactory.parsePathAsync(pathObject, this.originalRecord, this.parseOptions)
    }
}