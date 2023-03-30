interface ParseOptions {
    allowCustomTags?: boolean;
    validateSchema?: boolean;
}

interface Specification {
    // 定义 Specification 接口，包含 parse 方法
    parse(data: Record<string, any>, options: ParseOptions): any;
}

class SwaggerSpec implements Specification {
    // 实现 Specification 接口的 parse 方法
    public parse(data: Record<string, any>, options: ParseOptions): any {
        // ...
    }
}

class OpenAPISpec implements Specification {
    // 实现 Specification 接口的 parse 方法
    public parse(data: Record<string, any>, options: ParseOptions): any {
        // ...
    }
}

class AsyncAPISpec implements Specification {
    // 实现 Specification 接口的 parse 方法
    public parse(data: Record<string, any>, options: ParseOptions): any {
        // ...
    }
}

enum SpecType {
    Swagger = "swagger",
    OpenAPI = "openapi",
    AsyncAPI = "asyncapi",
}

class SpecParseFactory {
    // 工厂类，实例化不同的规范解析实现
    static createSpec(type: SpecType): Specification {
        switch (type) {
            case SpecType.Swagger:
                return new SwaggerSpec();
            case SpecType.OpenAPI:
                return new OpenAPISpec();
            case SpecType.AsyncAPI:
                return new AsyncAPISpec();
            default:
                throw new Error(`Unsupported spec type: ${type}`);
        }
    }
}