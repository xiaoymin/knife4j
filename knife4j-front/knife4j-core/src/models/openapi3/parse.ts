import { BaseCommonParser, ParseOptions } from '../knife4j/baseParse'
import { Knife4jInstance } from '../knife4j/type'
import { Knife4jTagObject } from "../knife4j/knife4jTag"
import { Knife4jExternalDocumentationObject } from "../knife4j/ExternalObject"
import { Knife4jInfoObject } from "../knife4j/knife4jInfo"
import lodash from 'lodash'
import { TagObject, InfoObject, PathsObject, OperationObject } from "./types"
import { Knife4jPathItemObject } from '../knife4j/knife4jPath'

/**
 * 解析OpenAPI3的规范,参考规范文档：https://spec.openapis.org/oas/v3.1.0
 */
export class OpenAPIParser extends BaseCommonParser {

    /**
     * 解析OpenAPI3规范
     * @param data OpenAPI原始数据
     * @param options 解析Options
     * @returns 
     */
    parse(data: Record<string, any>, options: ParseOptions): Knife4jInstance {
        console.log(options)
        let t1 = lodash.now()
        const instance = new Knife4jInstance('1', '2', '3');
        instance.originalRecord = data;
        // info信息
        const info = data["info"] as InfoObject;
        this.resolveInfo(info, instance);
        const tagArray = data["tags"] as TagObject[];
        //解析tag
        this.resolveTag(tagArray, instance);
        //解析path接口
        const paths = data["paths"] as PathsObject;
        this.resolvePaths(paths, instance);
        return instance;
    }


    /**
     * 解析tag
     * @param tagArray 分组源数据集
     * @param instance 实例对象
     */
    resolveTag(tagArray: TagObject[], instance: Knife4jInstance): void {
        if (lodash.isEmpty(tagArray)) {
            return;
        }
        tagArray.forEach(tag => {
            const _tag = new Knife4jTagObject(tag.name);
            //desc
            _tag.description = lodash.defaultTo(tag.description, '')
            // Knife4j扩展属性
            _tag.order = tag['x-order'] || 0;
            //externalDocs
            if (!lodash.isEmpty(tag.externalDocs)) {
                const extObject = new Knife4jExternalDocumentationObject(lodash.defaultTo(tag.externalDocs.url, ''))
                extObject.description = lodash.defaultTo(tag.externalDocs.description, '');
                _tag.externalDocs = extObject
            }
            instance.addTag(_tag)
        })
    }

    /**
     * 解析info
     * @param info 基础信息
     * @param instance 实例
     */
    resolveInfo(info: InfoObject, instance: Knife4jInstance): void {
        //console.log(info)
        if (lodash.isEmpty(info)) {
            return;
        }
        const _info = new Knife4jInfoObject(info.title)
        _info.version = lodash.defaultTo(info.version, '')
        _info.description = lodash.defaultTo(info.description, '')
        _info.summary = lodash.defaultTo(info.summary, '');
        _info.termsOfService = lodash.defaultTo(info.termsOfService, '')
        // 判断联系人
        if (!lodash.isEmpty(info.contact)) {
            _info.email = lodash.defaultTo(info.contact.email, '')
            _info.name = lodash.defaultTo(info.contact.name, '')
            _info.url = lodash.defaultTo(info.contact.url, '')
        }
        //license
        if (!lodash.isEmpty(info.license)) {
            _info.licenseName = lodash.defaultTo(info.license.name, '')
            _info.licenseUrl = lodash.defaultTo(info.license.url, '')
        }
        //对象属性赋值
        instance.info = _info;
    }


    /**
     * 解析paths对象
     * @param paths 路由paths
     * @param instance 对象实例
     */
    resolvePaths(paths: PathsObject, instance: Knife4jInstance): void {
        //console.log(paths)
        for (let key in paths) {
            let methods = paths[key];
            // 判断非空
            if (lodash.isEmpty(methods)) {
                continue;
            }
            for (let methodType in methods) {
                //console.log("method:", methodType)
                let operation = methods[methodType] as OperationObject;
                // 解析operation
                this.resolveOperation(operation, key, methodType, instance);
            }
        }
    }

    /**
     * path对象
     * @param operation path接口对象
     * @param url 接口url
     * @param methodType 接口类型，包括：POST\GET\PUT\DELETE
     * @param instance  对象实例
     */
    resolveOperation(operation: OperationObject, url: string, methodType: string, instance: Knife4jInstance): void {
        if (lodash.isEmpty(operation)) {
            return;
        }
        //此处只解析基础对象
        let _operation = new Knife4jPathItemObject(url, methodType)
        _operation.originalRecord = operation;
        _operation.summary = lodash.defaultTo(operation.summary, '')
        _operation.description = lodash.defaultTo(operation.description, '')
        _operation.operationId = lodash.defaultTo(operation.operationId, '')
        _operation.deprecated = lodash.defaultTo(operation.deprecated, false)
        // 设置tag
        let _tags = lodash.defaultTo(operation.tags, []);
        _operation.tags = _tags;
        // 遍历tags，计数器+1
        _tags.forEach(_tagName => {
            instance.addTagCount(_tagName)
        })
        //添加paths
        instance.addOperation(_operation);
    }

}

