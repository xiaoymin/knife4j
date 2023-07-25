import { Knife4jTagObject } from "./knife4jTag"
import { Knife4jInfoObject } from "./knife4jInfo"
import lodash from "lodash"

/**
 * 该类是所有parse方法最重输出的对象
 */
export class Knife4jInstance {
    id: string;
    name: string;
    url: string;
    version: string;
    // 基础信息
    info: Knife4jInfoObject = new Knife4jInfoObject("Knife4j接口开发文档");
    //分组
    tagNames: Array<string> = [];
    tags: Array<Knife4jTagObject> = [];
    // 原始结构数据
    originalRecord: Record<string, any> = {};
    /**
     * 构造函数
     * @param name 名称
     * @param location OpenAPI接口资源地址
     * @param version 版本，2.0或者3.0
     */
    constructor(name: string, location: string, version: string) {
        this.id = "12";
        this.name = name;
        this.url = location;
        this.version = version;
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
        //避免重复添加
        if (!lodash.includes(this.tagNames, tag.name)) {
            this.tagNames.push(tag.name)
            this.tags.push(tag)
        }
    }
}