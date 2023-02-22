# knife4j-core

前端核心模块，也是所有上层应用的基础库，基于TypeScript编写，主要作用：

一、解析适配

- 提供对当前流行的规范解析，例如：OpenAPI3、AsyncApi3、Swagger2等
- 提供对当前流行的工具格式解析，例如：Postman、curl
- 提供对Knife4j框架本身的扩展支持




## 架构设计

枚举类:
```typescript
enum SpecType{

    SWAGGER2,OPENAPI3,ASYNCAPI,POSTMAN
}

```


定义实例接口


```typescript
interface SpecAdapter{

    /***
     * 转换接口
     */
    convert(source:any):Knife4jOpenAPI;
}

```
由于规范众多，解析实现版本各不同，例如：

- Swagger2Adapter
- OpenAPI3Adapter
- PostmanAdapter
- AsyncAPIAdapter
- more...



定义实例创建抽象类

```typescript
interface SpecAdapterFactory{

    /***
     * 转换接口
     */
    createAdapter(type:SpecType):SpecAdapter;
}

```

实现类：

```typescript
class DefaultAdapterFactory implements SpecAdapterFactory{

    /***
     * 转换接口
     */
    createAdapter(type:SpecType):SpecAdapter{
        if(type==Swagger2){
            return new Swagger2Adapter();
        }
        if(type==OpenAPI3){
            return new OpenAPI3Adapter();
        }
        if(type==AsyncAPI){
            return new AsyncAPIAdapter();
        }
        return new DefaultAdapter();
    }
}

```