import {Knife4jExternalDocumentationObject} from "./ExternalObject"

/**
 * tag内容
 */
export class Knife4jTagObject{
    /**
     * REQUIRED. The name of the tag.
     */
    name: string;

    /**
     * A short description for the tag. CommonMark syntax MAY be used for rich text representation.
     */
    description?: string;

    /**
     * tag顺序
     */
    order=0;

    /**
     * 支持多级别的Tag
     */
    children:Array<Knife4jTagObject>=[];

    /**
     * Additional external documentation for this tag.
     */
    externalDocs?: Knife4jExternalDocumentationObject;

    /**
     * 构造函数
     * @param name tag名称
     */
    constructor(name:string){
        this.name=name
    }
}


