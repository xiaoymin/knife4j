/**
 * 该类是所有parse方法最重输出的对象
 */
export class Knife4jInstance {
    id:string;
    name:string;
    url:string;
    version:string;
    /**
     * 构造函数
     * @param name 名称
     * @param location OpenAPI接口资源地址
     * @param version 版本，2.0或者3.0
     */
    constructor(name:string, location:string, version:string){
        this.id="12";
        this.name=name; 
        this.url=location;
        this.version=version;
    }

}