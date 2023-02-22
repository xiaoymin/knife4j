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

    Swagger2,OpenAPI3,AsyncAPI,PostMan
}

```


定义实例接口


```typescript
interface Processer{

    /***
     * 转换接口
     */
    convert(source:any):Knife4jInstance;
}

```
由于规范众多，解析实现版本各不同，例如：

- Swagger2Processer
- OpenAPI3Processer
- PostmanProcesser
- AsyncProcesser
- more...



定义实例创建抽象类

```typescript
interface ProcesserFactory{

    /***
     * 转换接口
     */
    convert(type:SpecType):InstanceProcesser;
}

```

实现类：

```typescript
class MyProcesserFactory implements ProcesserFactory{

    /***
     * 转换接口
     */
    convert(type:SpecType):InstanceProcesser{
        if(type==Swagger2){
            return new Swagger2Processer();
        }
        if(type==OpenAPI3){
            return new OpenAPI3Processer();
        }
        if(type==AsyncAPI){
            return new AsyncProcesser();
        }
        return new DefaultProcess();
    }
}

```