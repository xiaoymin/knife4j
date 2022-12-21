# OpenAPI 规范

## 版本 3.0.3

关键词"MUST"，"MUST NOT"，"REQUIRED"，"SHALL"，"SHALL NOT"，"SHOULD"，"SHOULD NOT"，"RECOMMENDED"，"NOT RECOMMENDED"，"MAY"，,"OPTIONAL"当且仅当它们以全部大写字母出现时，本文档中将按照 [BCP 14](https://tools.ietf.org/html/bcp14)、[RFC2119](https://tools.ietf.org/html/rfc2119)、[RFC8174](https://tools.ietf.org/html/rfc8174)中的描述进行解释。

本文档翻译了，没有那些关键字。o(*￣︶￣*)o

以下所说的“工具”是指实现OAS规范的软件，例如knife4J。



## 修订历史

| 版本  | 翻译时间   | 翻译人            | 备注     |
| ----- | ---------- | ----------------- | -------- |
| 3.0.3 | 2022-09-20 | 1132454419@qq.com | 初次翻译 |



## 介绍

OpenAPI 规范 (OAS) 为 RESTful API 定义了一个与语言无关的标准接口。它允许人们在不访问源代码的情况下通过网络以文档的形式发现和理解服务的功能。如果定义得当，消费者可以用最少的成本实现与远程服务的交互。 可以使用 OpenAPI 定义来显示 API、用各种编程语言生成服务端、客户端、测试端的代码和其他用例。



## 定义

### OpenAPI Document(OpenAPI文档)

定义或描述API的文档（或一组文档）。OpenAPI的定义使用应符合OpenAPI的规范。



### <span id='Path-Templating'>Path Templating(模板路径)</span>

Path Templating是指由花括号 ({})分隔的模板表达式，该表达式使用路径传参将URL路径的一部分标记为可替换的。

路径中的每个模板表达式都必须对应一个<a href='#Path-Item-Object'>Path Item</a> ，该参数包含在 <a href='#Path-Item-Object'>Path Item</a> 本身和/或每个 <a href='#Path-Item-Object'>Path Item</a> 的[Operations](#Operation-Object)中。



### Media Types(媒体类型)

Media type的定义分布在多个资源中，且应符合[RFC6838](https://tools.ietf.org/html/rfc6838)的标准。

部分示例：

```apl
text/plain; charset=utf-8
application/json
application/vnd.github+json
application/vnd.github.v3+json
application/vnd.github.v3.raw+json
application/vnd.github.v3.text+json
application/vnd.github.v3.html+json
application/vnd.github.v3.full+json
application/vnd.github.v3.diff
application/vnd.github.v3.patch
```



### HTTP Status Codes(HTTP状态码)

HTTP Status Codes用于展示操作的状态。可用状态码由 [RFC7231](https://tools.ietf.org/html/rfc7231#section-6)定义，注册状态码列在 [IANA Status Code Registry](https://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml)中。



## Specification(规范)



### Versions(版本)

OpenAPI规范是是使用 [Semantic Versioning 2.0.0](https://semver.org/spec/v2.0.0.html) (semver)进行版本控制的，并遵循其规范。

semver 的major、minor部分（例如 3.0）应指定 OAS 功能集。通常.patch版本解决的是本文档中的错误，而不是新增或修改功能集。支持 OAS 3.0 的工具应该与所有 OAS 3.0.* 版本兼容。工具不应考虑补丁版本，例如不区分 3.0.0 和 3.0.1。

OpenAPI规范的每一个新的小版本都应该在相同的主版本中对任何以前有效的OpenAPI文档小版本以同等的语义更新为新的规范版本。这样的更新必须将openapi属性更改为新的小版本。

例如，一个有效的OpenAPI 3.0.2文档，修改openapi的属性为3.1.0之后，应该是一个有效的OpenAPI 3.1.0文档，在语义上与等同于原始的OpenAPI 3.0.2文档。必须编写新的OpenAPI规范的小版本以确保这种形式的向后兼容性。

一个与OAS `3.*.*`兼容的 OpenAPI 文档包含一个必需的 openapi 字段，该字段指定它使用的 OAS 的语义版本。(OAS 2.0 文档包含一个名为[swagger](https://swagger.io/specification/v2/)，其值为“2.0”的顶级版本字段。)



### Format(格式)

一个符合OpenAPI规范的OpenAPI文档本身就是一个JSON对象，它可以用JSON或YAML格式表示。

例如，如果一个字段有一个数组，则将使用JSON数组表示为：

```json
{
   "field": [ 1, 2, 3 ]
}
```

规范中的所有字段名称都是区分大小写的。包括map中用作Key的所有字段，除非显式地指出Key是不区分大小写的。

Schema公开了两种类型的字段：分别是Fixed fields（固定字段）和Patterned fields（匹配字段），Patterned fields必须在包含对象中具有唯一名称。

为了保留在 YAML 和 JSON 格式之间互相转换的能力，建议使用 [YAML 1.2](https://yaml.org/spec/1.2/spec.html) 版以及一些约束：

- Tag必须限制在 [JSON Schema ruleset](https://yaml.org/spec/1.2/spec.html#id2803231)允许的范围内。

- YAML映射中使用的Key必须为字符串，参考 [YAML Failsafe schema ruleset](https://yaml.org/spec/1.2/spec.html#id2802346)。


注意：虽然 API 可以由 OpenAPI 文档以 YAML 或 JSON 格式定义，但 API 请求、响应正文和其他内容可以不为 JSON 或 YAML。



### Document Structure(文档结构)

OpenAPI Document可以由单个或者多个文档组成。 在多个的情况下，规范中必须使用 $ref 字段来引用[JSON Schema](https://json-schema.org/)定义中的这部分。建议将OpenAPI 文档命名为：`openapi.json `或 `openapi.yaml`。



### Data Types(数据类型)

OAS 中的`primitive `数据类型是基于[JSON Schema Specification Wright Draft 00](https://tools.ietf.org/html/draft-wright-json-schema-00#section-4.2)支持的类型。注意，`integer`也可作为一种类型，它被定义为没有分数或指数部分的 JSON 数字。 不支持`null`作为类型（替代解决方案请参阅[`nullable`](#schema-nullable)）。 模型是使用 <a href='#Schema-Object'>Schema Object</a>定义的，它是[JSON Schema Specification Wright Draft 00](https://tools.ietf.org/html/draft-wright-json-schema-00#section-4.2)的扩展子集。



`primitive `有一个可选的修饰符属性：`format`。 OAS 使用下列几种格式来详细定义所使用的数据类型。 但是为了支持文档需求，`format`属性是一个开放的`string`类型的属性，并且可以具有任何值。 即使本规范未定义，也可以使用比如`email`、`uuid`等格式。 没有定义`format`属性的类型遵循 JSON Schema 中的定义。 不能识别特定`format`的工具可以默认返回单独的`type`，就像没有指定`format`一样。

OAS 定义的格式有：

| type    | format    | **Comments**                                                 |
| ------- | --------- | ------------------------------------------------------------ |
| integer | int32     | 32位有符号数                                                 |
| integer | int64     | 64位有符号数(long类型)                                       |
| number  | float     |                                                              |
| number  | double    |                                                              |
| string  |           |                                                              |
| string  | byte      | base64编码                                                   |
| string  | binary    | 八位字节序列                                                 |
| boolean |           |                                                              |
| string  | date      | 由[RFC3339](https://xml2rfc.ietf.org/public/rfc/html/rfc3339.html#anchor14)定义的`full-date` |
| string  | date-time | 由[RFC3339](https://xml2rfc.ietf.org/public/rfc/html/rfc3339.html#anchor14)定义的`full-time` |
| string  | password  | 提示 UI 以隐藏输入                                           |



### Rich Text Formatting(富文本格式)

整个规范的`description`字段都被标注为支持 CommonMark markdown 格式。 在工具渲染富文本的时候，必须支持[CommonMark 0.27](https://spec.commonmark.org/0.27/)中描述的 markdown 语法。 工具可以选择忽略一些CommonMark功能来解决安全问题。



### Relative References in URLs(URL中的相对引用)

如未有另外说明，所有属于URL的属性都可以是 [RFC3986](https://tools.ietf.org/html/rfc3986#section-4.2)定义的相对引用。 使用<a href='#Server-Object'>Server Object</a>中定义的 URL 作为基本 URI 来解析相对引用。



$ref 中使用的相对引用按照 JSON 引用进行处理，使用当前文档的 URL 作为基本 URI。 另请参见参考对象。



### Schema(架构)

在下面的描述中，如果一个字段没有明确地被要求**必填**(REQUIRED、MUST 、SHALL)描述，它可以被认为是**可选的**(OPTIONAL)。



#### <span id='OpenAPI-Object'>OpenAPI Object(文档对象)</span>

OpenAPI 文档的根对象。

##### Fixed fields：

| 属性名       |                           属性类型                           | 描述                                                         |
| ------------ | :----------------------------------------------------------: | ------------------------------------------------------------ |
| openapi      |                           `string`                           | **必填**，该字符串必须是OpenAPI文档使用的[OpenAPI Specification version](#versions)的语义版本号。 |
| info         |                 <a href='#Info-Object'>Info Object</a>       | **必填**，提供有关 API 的metadata，可以根据需要由工具使用。  |
| servers      |              [<a href='#Server-Object'>Server Object</a>]    | <a href='#Server-Object'>Server Object</a>数组，提供到目标服务器的连接信息。如果未提供`servers`属性，或为空数组，则默认值将是一个<a href='#Server-Object'>Server Object</a>，其url值为`/`。 |
| paths        |                <a href='#paths-object'>Paths Object</a>      | **必填**，API的可用path和操作。                              |
| components   |           <a href='#Components-Object'>Components Object</a> | 保存规范里schemas的元素(element)信息。                       |
| security     | [<a href='#Security-Requirement-Object'>Security Requirement  Object</a>] | API的安全机制配置。值列表可以使用任意的<a href='#Security-Requirement-Object'>Security Requirement  Object</a>。只需满足其中一个就可以对请求进行授权。单个请求可以重写此定义。如不需要，可配置为`{}`。 |
|tags|                 <a href='#Tag-Object'>Tag  Object</a>                 | 带有附加metadata的`tags`列表。`tags`的顺序可用于解析工具展示它们的顺序。 不是每个<a href='#Operation-Object'>Operation  Object</a>的`tags`都必须声明。 未声明的`tags`可以随机展示或基于工具的实现逻辑展示。 列表中的每个`tag`名称必须是唯一的。 |
| externalDocs | <a href='#External-Documentation-Object'>External Documentation Object</a> | 其他外部文档。                   |

该对象可以通过[Specification Extensions](#specification-extensions)进行扩展。



#### <span id='Info-Object'>Info Object(信息对象)</span>

该对象提供有关 API 的metadata 。metadata可以由客户使用也可以在编辑或文档生成工具中使用。

##### Fixed Fields

| 属性名         |             属性类型              | 描述                                                         |
| -------------- | :-------------------------------: | ------------------------------------------------------------ |
| title          |             `string`              | **必填**，API的标题。                                        |
| description    |             `string`              | API的简短描述，使用[CommonMark syntax](https://spec.commonmark.org/)可以用于展示富文本。 |
| termsOfService |             `string`              | 指向API服务条款的URL。必须为URL格式。                        |
| contact        | [Contact Object](#contact-object) | API联系信息。                                                |
| license        | [License Object](#license-object) | API许可信息。                                                |
| version        |             `string`              | **必填**，文档的版本（不同于[OpenAPI 规范版本](#oas-version)或 API 实现版本）。 |

该对象可以通过[Specification Extensions](#specification-extensions)进行扩展。

##### 示例：

```json
{
  "title": "Sample Pet Store App",
  "description": "This is a sample server for a pet store.",
  "termsOfService": "http://example.com/terms/",
  "contact": {
    "name": "API Support",
    "url": "http://www.example.com/support",
    "email": "support@example.com"
  },
  "license": {
    "name": "Apache 2.0",
    "url": "https://www.apache.org/licenses/LICENSE-2.0.html"
  },
  "version": "1.0.1"
}
```

```yaml
title: Sample Pet Store App
description: This is a sample server for a pet store.
termsOfService: http://example.com/terms/
contact:
  name: API Support
  url: http://www.example.com/support
  email: support@example.com
license:
  name: Apache 2.0
  url: https://www.apache.org/licenses/LICENSE-2.0.html
version: 1.0.1
```



#### <span id='Contact-Object'>Contact Object(联系信息对象)</span>

API联系信息

| 属性名 | 属性类型 | 描述                                           |
| ------ | :------: | ---------------------------------------------- |
| name   | `string` | 联系人/组织的名称                              |
| url    | `string` | 联系人信息的URL。必须为URL格式。               |
| email  | `string` | 联系人/组织的电子邮件。 必须采用电子邮件格式。 |

该对象可以通过[Specification Extensions](#specification-extensions)进行扩展。

##### 示例：

```json
{
  "name": "API Support",
  "url": "http://www.example.com/support",
  "email": "support@example.com"
}
```

```yaml
name: API Support
url: http://www.example.com/support
email: support@example.com
```





#### License Object(许可信息对象)

API许可信息

| 属性名 | 属性类型 | 描述                                |
| ------ | :------: | ----------------------------------- |
| name   | `string` | **必填**，API使用的许可证名称。     |
| url    | `string` | API使用的许可的URL。必须为URL格式。 |

##### 示例：

```json
{
  "name": "Apache 2.0",
  "url": "https://www.apache.org/licenses/LICENSE-2.0.html"
}
```

```yaml
name: Apache 2.0
url: https://www.apache.org/licenses/LICENSE-2.0.html
```



#### <span id='Server-Object'>Server Object(服务器对象)</span>

表示服务器的对象。

##### Fixed fields：

| 属性名      | 属性类型                                                     | 描述                                                         |
| ----------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| url         | string                                                       | **必填**，指向目标主机的URL。可以是相对路径，表示服务器相对于OpenAPI文档的位置。当变量命名在`{}`中时，将进行变量替换。例如`{brackets}`则会替换brackets变量。 |
| description | string                                                       | url所指向的服务器的描述信息，使用[CommonMark syntax](https://spec.commonmark.org/)可以用于展示富文本。 |
| variables   | Map[`string`,<a href='#Server-Variable-Object'>Server Variable Object</a>] | 变量名与值之间的映射，用于替换`servers`下`url`配置信息的模板。 |

该对象可以通过[Specification Extensions](#specification-extensions)进行扩展。



##### 示例：

###### 单个服务器：

```json
{
  "url": "https://development.gigantic-server.com/v1",
  "description": "Development server"
}
```

```yaml
url: https://development.gigantic-server.com/v1
description: Development server
```

###### 多个服务器：

```json
{
  "servers": [
    {
      "url": "https://development.gigantic-server.com/v1",
      "description": "Development server"
    },
    {
      "url": "https://staging.gigantic-server.com/v1",
      "description": "Staging server"
    },
    {
      "url": "https://api.gigantic-server.com/v1",
      "description": "Production server"
    }
  ]
}
```

```yaml
servers:
- url: https://development.gigantic-server.com/v1
  description: Development server
- url: https://staging.gigantic-server.com/v1
  description: Staging server
- url: https://api.gigantic-server.com/v1
  description: Production server
```

###### 应用变量：

```json
{
  "servers": [
    {
      "url": "https://{username}.gigantic-server.com:{port}/{basePath}",
      "description": "The production API server",
      "variables": {
        "username": {
          "default": "demo",
          "description": "this value is assigned by the service provider, in this example `gigantic-server.com`"
        },
        "port": {
          "enum": [
            "8443",
            "443"
          ],
          "default": "8443"
        },
        "basePath": {
          "default": "v2"
        }
      }
    }
  ]
}
```

```yaml
servers:
- url: https://{username}.gigantic-server.com:{port}/{basePath}
  description: The production API server
  variables:
    username:
      # note! no enum here means it is an open value
      default: demo
      description: this value is assigned by the service provider, in this example `gigantic-server.com`
    port:
      enum:
        - '8443'
        - '443'
      default: '8443'
    basePath:
      # open meaning there is the opportunity to use special base paths as assigned by the provider, default is `v2`
      default: v2
```



#### <span id='Server-Variable-Object'>Server Variable Object(可变服务器对象)</span>

表示服务器URL模板替换的Server Variable对象。

##### Fixed Fields

| 属性名      |  属性类型  | 描述                                                         |
| ----------- | :--------: | ------------------------------------------------------------ |
| enum        | [`string`] | 如果替换选项来自数组，则要使用的字符串值的枚举。且数组不能为空。 |
| default     |  `string`  | **必填**，用于替换的默认值，如果未提供，则应发送该值。请注意，此行为与<a href='#Schema-Object'>Schema Object</a>对默认值的处理方式不同，因为在这些情况下，参数值是可选的。 如果定义了枚举，则该值应该存在于枚举的值中。 |
| description |  `string`  | 对服务器变量的描述，使用[CommonMark syntax](https://spec.commonmark.org/)可以展示富文本。 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。



#### <span id='Components-Object'>Components Object(组件对象)</span>

为 OAS保存一组可重用的对象。 Components Object中定义的所有对象都不会对API产生影响，除非它们被Components Object外部的属性显式引用。

##### Fixed Fields

| 匹配字段                                         | 属性类型                                                     | 描述                                                         |
| ------------------------------------------------ | :----------------------------------------------------------- | ------------------------------------------------------------ |
| schemas                                          | Map[`string`, <a href='#Schema-Object'>Schema Object</a> \| <a href='#Reference-Object'>Reference Object</a>] | 可重复使用的<a href='#Schema-Object'>Schema Object</a>.      |
| <span id='components-responses'>responses</span> | Map[`string`, <a href='#Response-Object'>Response Object</a> \| <a href='#Reference-Object'>Reference Object</a>] | 可重复使用的<a href='#Response-Object'>Response Object</a>.  |
| parameters                                       | Map[`string`, <a href='#Parameter-Object'>Parameter Object</a> \| <a href='#Reference-Object'>Reference Object</a>] | 可重复使用的<a href='#Parameter-Object'>Parameter Object</a>. |
| examples                                         | Map[`string`, <a href='#Example-Object'>Example Object</a> \| <a href='#Reference-Object'>Reference Object</a>] | 可重复使用的<a href='#Example-Object'>Example Object</a>.    |
| requestBodies                                    | Map[`string`, <a href='#Request-Body-Object'>Request Body Object</a> \| <a href='#Reference-Object'>Reference Object</a>] | 可重复使用的<a href='#Request-Body-Object'>Request Body Object</a>. |
| headers                                          | Map[`string`, <a href='#Header-Object'>Header Object</a>\| <a href='#Reference-Object'>Reference Object</a>] | 可重复使用的<a href='#Header-Object'>Header Object</a>.      |
| securitySchemes                                  | Map[`string`, <a href='#Security-Scheme-Object'>Security Scheme Object</a>\| <a href='#Reference-Object'>Reference Object</a>] | 可重复使用的[Security Scheme Objects](#security-scheme-object). |
| links                                            | Map[`string`, <a href='#Link-Object'>Link Object</a> \| <a href='#Reference-Object'>Reference Object</a>] | 可重复使用的<a href='#Link-Object'>Link Object</a>.          |
| callbacks                                        | Map[`string`, <a href='#Callback-Object'>Callback Object</a> \| <a href='#Reference-Object'>Reference Object</a>] | 可重复使用的<a href='#Callback-Object'>Callback Object</a>.  |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

上面声明的所有字段的key都必须能与正则表达式`^[a-zA-Z0-9\.\-_]+$`匹配。

##### key的示例

```
User
User_1
User_Name
user-name
my.org.User
```

##### 对象示例

```json
"components": {
  "schemas": {
    "GeneralError": {
      "type": "object",
      "properties": {
        "code": {
          "type": "integer",
          "format": "int32"
        },
        "message": {
          "type": "string"
        }
      }
    },
    "Category": {
      "type": "object",
      "properties": {
        "id": {
          "type": "integer",
          "format": "int64"
        },
        "name": {
          "type": "string"
        }
      }
    },
    "Tag": {
      "type": "object",
      "properties": {
        "id": {
          "type": "integer",
          "format": "int64"
        },
        "name": {
          "type": "string"
        }
      }
    }
  },
  "parameters": {
    "skipParam": {
      "name": "skip",
      "in": "query",
      "description": "number of items to skip",
      "required": true,
      "schema": {
        "type": "integer",
        "format": "int32"
      }
    },
    "limitParam": {
      "name": "limit",
      "in": "query",
      "description": "max records to return",
      "required": true,
      "schema" : {
        "type": "integer",
        "format": "int32"
      }
    }
  },
  "responses": {
    "NotFound": {
      "description": "Entity not found."
    },
    "IllegalInput": {
      "description": "Illegal input for operation."
    },
    "GeneralError": {
      "description": "General Error",
      "content": {
        "application/json": {
          "schema": {
            "$ref": "#/components/schemas/GeneralError"
          }
        }
      }
    }
  },
  "securitySchemes": {
    "api_key": {
      "type": "apiKey",
      "name": "api_key",
      "in": "header"
    },
    "petstore_auth": {
      "type": "oauth2",
      "flows": {
        "implicit": {
          "authorizationUrl": "http://example.org/api/oauth/dialog",
          "scopes": {
            "write:pets": "modify pets in your account",
            "read:pets": "read your pets"
          }
        }
      }
    }
  }
}
```

```yaml
components:
  schemas:
    GeneralError:
      type: object
      properties:
        code:
          type: integer
          format: int32
        message:
          type: string
    Category:
      type: object
      properties:
        id:
          type: integer
          format: int64
        name:
          type: string
    Tag:
      type: object
      properties:
        id:
          type: integer
          format: int64
        name:
          type: string
  parameters:
    skipParam:
      name: skip
      in: query
      description: number of items to skip
      required: true
      schema:
        type: integer
        format: int32
    limitParam:
      name: limit
      in: query
      description: max records to return
      required: true
      schema:
        type: integer
        format: int32
  responses:
    NotFound:
      description: Entity not found.
    IllegalInput:
      description: Illegal input for operation.
    GeneralError:
      description: General Error
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/GeneralError'
  securitySchemes:
    api_key:
      type: apiKey
      name: api_key
      in: header
    petstore_auth:
      type: oauth2
      flows: 
        implicit:
          authorizationUrl: http://example.org/api/oauth/dialog
          scopes:
            write:pets: modify pets in your account
            read:pets: read your pets
```



#### <span id='Paths-Object'>Paths Object(路径对象)</span>

保存到各个资源及其操作的相对路径。该路径被附加到来<a href='#Server-Object'>Server Object</a>的URL以构建完整的URL。 由于[ACL约束](#security-filtering)，Path Item可能为空。

##### Patterned fields：

| 匹配字段 |                     属性类型                     | 描述                                                         |
| -------- | :----------------------------------------------: | ------------------------------------------------------------ |
| /{path}  | <a href='#Path-Item-Object'>Path Item Object</a> | 访问资源的相对路径。 字段名称必须以`/`开头。 路径从<a href='#Server-Object'>Server Object</a>的url字段拼接成完整的URL。 可以使用模板路径。 匹配 URL 时，将会优先匹配模板路径。 具有相同层次结构但不同名称的路径不能存在，因为它们是相同的。 如果匹配不明确，则由工具决定使用哪一个。 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

##### 路径匹配示例：

假设定义/pets/mine路径，匹配顺序为：

```bash
/pets/{petId}
/pets/mine
```

相同且无效的匹配方案：

```
/pets/{petId}
/pets/{name}
```

匹配不明确的匹配方案：

```
/{entity}/me
/books/{id}
```

##### 示例：

```json
{
  "/pets": {
    "get": {
      "description": "Returns all pets from the system that the user has access to",
      "responses": {
        "200": {          
          "description": "A list of pets.",
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "items": {
                  "$ref": "#/components/schemas/pet"
                }
              }
            }
          }
        }
      }
    }
  }
}
```

```yaml
/pets:
  get:
    description: Returns all pets from the system that the user has access to
    responses:
      '200':
        description: A list of pets.
        content:
          application/json:
            schema:
              type: array
              items:
                $ref: '#/components/schemas/pet'
```



#### <span id='Path-Item-Object'>Path Item Object(路径详细信息对象)</span>

描述在单个路径上可用的操作。由于[ACL约束](#security-filtering)，Path Item可能为空，虽然仍可以通过文档查看，但无法确定哪些操作和参数可用。

Fixed Fields

| 属性名      |                           属性类型                           | 描述                                                         |
| ----------- | :----------------------------------------------------------: | ------------------------------------------------------------ |
| $ref        |                           `string`                           | 允许对此path item进行外部定义。 引用的结构必须是<a href='#Path-Item-Object'>Path Item Object</a>。 如果<a href='#Path-Item-Object'>Path Item Object</a>字段同时出现在已定义对象和引用对象中，则定义无效。 |
| summary     |                           `string`                           | 一个可选的摘要，应用于此路径中的所有操作。                   |
| description |                           `string`                           | 应用于此路径中的所有操作，如使用[CommonMark syntax](https://spec.commonmark.org/)则可以用于展示富文本。 |
| get         |      <a href='#Operation-Object'>Operation  Object</a>       | get请求操作的定义。                                          |
| put         |      <a href='#Operation-Object'>Operation  Object</a>       | put请求操作的定义。                                          |
| post        |      <a href='#Operation-Object'>Operation  Object</a>       | post请求操作的定义。                                         |
| delete      |      <a href='#Operation-Object'>Operation  Object</a>       | delete请求操作的定义。                                       |
| options     |      <a href='#Operation-Object'>Operation  Object</a>       | options请求操作的定义。                                      |
| head        |      <a href='#Operation-Object'>Operation  Object</a>       | head请求操作的定义。                                         |
| patch       |      <a href='#Operation-Object'>Operation  Object</a>       | patch请求操作的定义。                                        |
| trace       |      <a href='#Operation-Object'>Operation  Object</a>       | trace请求操作的定义。                                        |
| servers     |         [<a href='#Server-Object'>Server Object</a>]         | servers请求操作的定义。                                      |
| parameters  | [<a href='#Parameter-Object'>Parameter Object</a> \| <a href='#Reference-Object'>Reference Object</a>] | 适用于此路径下所有请求操作的参数列表。 这些参数可以在<a href='#Operation-Object'>Operation  Object</a>中配置`parameters`覆盖，但不能删除。 且列表内同一个位置的参数名不能重复。 该列表可以使用<a href='#Reference-Object'>Reference Object</a>链接到[OpenAPI Object](#OpenAPI-Object)中[components/parameters](#Components-Object)定义的参数。 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

##### 示例：

```json
{
  "get": {
    "description": "Returns pets based on ID",
    "summary": "Find pets by ID",
    "operationId": "getPetsById",
    "responses": {
      "200": {
        "description": "pet response",
        "content": {
          "*/*": {
            "schema": {
              "type": "array",
              "items": {
                "$ref": "#/components/schemas/Pet"
              }
            }
          }
        }
      },
      "default": {
        "description": "error payload",
        "content": {
          "text/html": {
            "schema": {
              "$ref": "#/components/schemas/ErrorModel"
            }
          }
        }
      }
    }
  },
  "parameters": [
    {
      "name": "id",
      "in": "path",
      "description": "ID of pet to use",
      "required": true,
      "schema": {
        "type": "array",
        "items": {
          "type": "string"
        }
      },
      "style": "simple"
    }
  ]
}
```

```yaml
get:
  description: Returns pets based on ID
  summary: Find pets by ID
  operationId: getPetsById
  responses:
    '200':
      description: pet response
      content:
        '*/*' :
          schema:
            type: array
            items:
              $ref: '#/components/schemas/Pet'
    default:
      description: error payload
      content:
        'text/html':
          schema:
            $ref: '#/components/schemas/ErrorModel'
parameters:
- name: id
  in: path
  description: ID of pet to use
  required: true
  schema:
    type: array
    items:
      type: string  
  style: simple
```



#### <span id='Operation-Object'>Operation Object(请求对象)</span>

路径上的单个API请求操作的描述。以下简称“请求”。

##### Fixed Fields

| 属性名       |                           属性类型                           | 描述                                                         |
| ------------ | :----------------------------------------------------------: | ------------------------------------------------------------ |
| tags         |                          [`string`]                          | API文档控制的tags列表。 tag可用于对请求资源进行分组。        |
| summary      |                           `string`                           | 请求的简单说明。                                             |
| description  |                           `string`                           | 请求的详细描述。使用[CommonMark syntax](https://spec.commonmark.org/)可以展示富文本。 |
| externalDocs | <a href='#External-Documentation-Object'>External Documentation Object</a> | 该请求附加的外部文档。                                       |
| operationId  |                           `string`                           | 用于标识请求的唯一字符串。 在API中描述的所有请求中，id必须是唯一的。 operationId值区分大小写。工具可以使用operationId来标识唯一请求，建议遵循常见的编程命名约定。 |
| parameters   | [<a href='#Parameter-Object'>Parameter Object</a> \| <a href='#Reference-Object'>Reference Object</a>] | 适用于此请求的参数列表。 如果已经在<a href='#Path-Item-Object'>Path Item</a>中定义了`parameters`，在这里定义将覆盖它，但不会删除。 且列表内的参数在同一个位置参数名不能重复。 该列表可以使用<a href='#Reference-Object'>Reference Object</a>链接到[OpenAPI Object](#OpenAPI-Object)中[components/parameters](#Components-Object)定义的参数。 |
| requestBody  | <a href='#Request-Body-Object'>Request Body Object</a> \| <a href='#Reference-Object'>Reference Object</a> | 适用于此请求的的`requestBody`。`requestBody`仅在HTTP 1.1 specification [RFC7231](https://tools.ietf.org/html/rfc7231#section-4.3.1)明确定义的HTTP方法中受支持。在其他HTTP specification定义不明确的情况下，应该忽略此参数。 |
| responses    |       <a href='#Responses-Object'>Responses Object</a>       | **必填**， 该请求可能的响应列表。                            |
| callbacks    | Map[`string`, <a href='#Callback-Object'>Callback Object</a> \| <a href='#Reference-Object'>Reference Object</a>] | 与请求相关的回调的映射。 key是<a href='#Callback-Object'>Callback Object</a>的唯一标识符。其每个值都是一个<a href='#Callback-Object'>Callback Object</a>，它描述了API提供者发起的请求和预期的响应。 |
| deprecated   |                          `boolean`                           | 声明过期的请求。 默认值为false。                             |
| security     | [<a href='#Security-Requirement-Object'>Security Requirement  Object</a>] | API的安全机制配置。值列表可以使用任意的<a href='#Security-Requirement-Object'>Security Requirement  Object</a>。只需满足其中一个就可以对请求进行授权。在这里定义会覆盖顶级配置的安全机制。如果要忽略顶级的配置，可以使用空数组。 |
| servers      |         [<a href='#Server-Object'>Server Object</a>]         | 提供该请求的服务器数组。如果在<a href='#Path-Item-Object'>Path Item</a>或<a href='#OpenAPI-Object'>根对象</a>指定了`servers`，该值会覆盖以上配置。 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

##### 示例：

```json
{
  "tags": [
    "pet"
  ],
  "summary": "Updates a pet in the store with form data",
  "operationId": "updatePetWithForm",
  "parameters": [
    {
      "name": "petId",
      "in": "path",
      "description": "ID of pet that needs to be updated",
      "required": true,
      "schema": {
        "type": "string"
      }
    }
  ],
  "requestBody": {
    "content": {
      "application/x-www-form-urlencoded": {
        "schema": {
          "type": "object",
          "properties": {
            "name": { 
              "description": "Updated name of the pet",
              "type": "string"
            },
            "status": {
              "description": "Updated status of the pet",
              "type": "string"
            }
          },
          "required": ["status"] 
        }
      }
    }
  },
  "responses": {
    "200": {
      "description": "Pet updated.",
      "content": {
        "application/json": {},
        "application/xml": {}
      }
    },
    "405": {
      "description": "Method Not Allowed",
      "content": {
        "application/json": {},
        "application/xml": {}
      }
    }
  },
  "security": [
    {
      "petstore_auth": [
        "write:pets",
        "read:pets"
      ]
    }
  ]
}
```

```yaml
tags:
- pet
summary: Updates a pet in the store with form data
operationId: updatePetWithForm
parameters:
- name: petId
  in: path
  description: ID of pet that needs to be updated
  required: true
  schema:
    type: string
requestBody:
  content:
    'application/x-www-form-urlencoded':
      schema:
       properties:
          name: 
            description: Updated name of the pet
            type: string
          status:
            description: Updated status of the pet
            type: string
       required:
         - status
responses:
  '200':
    description: Pet updated.
    content: 
      'application/json': {}
      'application/xml': {}
  '405':
    description: Method Not Allowed
    content: 
      'application/json': {}
      'application/xml': {}
security:
- petstore_auth:
  - write:pets
  - read:pets
```



#### <span id='External-Documentation-Object'>External Documentation Object(附加文档信息对象)</span>

引用外部资源的扩展文档。

##### Fixed Fields

| 属性名      | 属性类型 | 描述                                                         |
| ----------- | :------: | ------------------------------------------------------------ |
| description | `string` | 目标文档的简短描述，使用[CommonMark syntax](https://spec.commonmark.org/)可以用于展示富文本。 |
| url         | `string` | **必填**，指向目标文档的URL. 必须为URL格式。                 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。



##### 示例：

```json
{
  "description": "Find more info here",
  "url": "https://example.com"
}
```

```yaml
description: Find more info here
url: https://example.com
```



#### <span id='Parameter-Object'>Parameter Object(参数对象)</span>

描述单个请求的参数，在同一个位置参数名不能重复。

##### 参数位置

 `in`字段指定了参数的四个位置：:

- path - 与<a href='#Path-Templating'>模板路径</a>一起使用，参数实际上是请求URL的一部分。 但不包括API的host和base path。 例如，在/items/{itemId} 中，路径参数为itemId。
- query - 附加到URL后面的参数，例如在`/items?id=###`请求里，query参数为 `id`.
- header - 自定义请求头作为请求的一部分。注意，[RFC7230](https://tools.ietf.org/html/rfc7230#page-22)规定请求头名称不区分大小写。
- cookie - 设置特定的cookie值传递给API。

##### Fixed Fields

| 属性名                            | 属性类型  | 描述                                                         |
| --------------------------------- | :-------: | ------------------------------------------------------------ |
| name                              | `string`  | **必填**， 参数的名称，区分大小写；如果`in`为`"path"`，则名称字段必须在<a href='#Path-Templating'>模板路径</a>的表达式中；如果`in`为`"header"`，且名称字段为“Accept”、“Content-Type”或“Authorization”，则定义无效；其他情况则可以直接使用该字段； |
| <span id='parameter-in'>in</span> | `string`  | **必填**， 参数的位置，有效值为 `"query"`, `"header"`, `"path"` or `"cookie"`。 |
| description                       | `string`  | 参数的简要说明和使用的举例。 使用[CommonMark syntax](https://spec.commonmark.org/)可以用于展示富文本。 |
| required                          | `boolean` | 是否为必填参数。如果参数的`in`为`"path"`，此属性的值必须为true；否则，该属性默认值为false。 |
| deprecated                        | `boolean` | 指定参数已弃用；默认值为false。                              |
| allowEmptyValue                   | `boolean` | 设置参数是否可以为空，仅对`query`参数有效， 默认值为假。 如果使用了 style，并且如果 behavior 为 n/a（不能序列化），则 allowEmptyValue 的值应被忽略；不推荐使用此属性，因为它可能会在以后的版本中被删除。 |

指定参数的序列化规则以两种方式之一。 对于更简单的场景，[`schema`](#parameter-schema)和[`style`](#parameter-style)可以描述参数的结构和语法。

| 属性名        |                           属性类型                           | 描述                                                         |
| ------------- | :----------------------------------------------------------: | ------------------------------------------------------------ |
| style         |                           `string`                           | 描述如何根据参数值的类型对参数值进行序列化。 默认值基于`in`的值；`in`和`style`的对应关系为：`query` - `form`；`path` - `simple`； `header` - `simple`；  `cookie` - `form`。 |
| explode       |                          `boolean`                           | 当属性为true时，会为`array`的每一个值或`object`的key-value生成独立的参数放到map里。 对于其他类型，此属性无效。 当[`style`](#parameter-style)为`form`时，默认值为true；为其他值时，默认值为 false。 |
| allowReserved |                          `boolean`                           | 参数值是否为保留字，如[RFC3986](https://tools.ietf.org/html/rfc3986#section-2.2)所定义`:/?#[]@!$&'()*+,;=`在不包含URL编码(percent-encoding)的情况下。 此属性仅适用于`in`值为`query`的参数。 默认值为false。 |
| schema        | <a href='#Schema-Object'>Schema Object</a> \| <a href='#Reference-Object'>Reference Object</a> | 该schema定义了用于参数的类型。                               |
| example       |                             Any                              | 参数值的示例。 该示例应匹配指定的`schema`和编码。`example`属性和`examples`属性互斥。如果引用的`schema`包含示例，则该示例应覆盖`schema`提供的示例。不能用JSON或YAML表示的media types的示例，可以用字符串表示，并在必要时进行转义。 |
| examples      | Map[ `string`, <a href='#Example-Object'>Example Object</a> \| <a href='#Reference-Object'>Reference Object</a>] | 参数值的示例。每个示例都应该为参数指定编码的正确格式的值。`examples`属性和`example`属性互斥。如果引用的`schema`包含示例，则该示例应覆盖`schema`提供的示例。 |

针对更复杂的场景， [`content`](#parameter-content)属性可以定义参数的media type和schema。一个参数配置必须包含`schema`属性或`content`属性，但不能同时包含两者。当`example`、`examples`和 `schema`一起提供时，该示例必须遵循指定的参数序列化策略。

| 属性名                                      |                           属性类型                           | 描述                                                         |
| ------------------------------------------- | :----------------------------------------------------------: | ------------------------------------------------------------ |
| <span id="parameter-content">content</span> | Map[`string`, <a href='#Media-Type-Object'>Media Type Object</a>] | 表示参数的map。key为media type，值为它的描述。map有且只能有一个值。 |

##### Style 的值：

为了支持简单参数的序列化，定义了一组`style` 值。

| <span id='parameter-style'>style</span> | [`type`](#data-types) | `in`              | Comments                                                     |
| -------------- | ------------------------------------------------------ | ----------------- | ------------------------------------------------------------ |
| matrix         | `primitive`, `array`, `object`                         | `path`            | Path-style参数由[RFC6570](https://tools.ietf.org/html/rfc6570#section-3.2.7)定义。 |
| label          | `primitive`, `array`, `object`                         | `path`            | Label style参数由[RFC6570](https://tools.ietf.org/html/rfc6570#section-3.2.5)定义。 |
| form           | `primitive`, `array`, `object`                         | `query`, `cookie` | 该选项将`collectionFormat`替换为OpenAPI 2.0中的`csv`(`explode`为false时)或multi(`explode`为true时)值。 |
| simple         | `array`                                                | `path`, `header`  | Simple style参数由[RFC6570](https://tools.ietf.org/html/rfc6570#section-3.2.2)定义。该选项将`collectionFormat`替换为OpenAPI 2.0中的`csv`。 |
| spaceDelimited | `array`                                                | `query`           | 空格分隔的数组值。该选项将`collectionFormat`替换为OpenAPI 2.0中的`ssv`。 |
| pipeDelimited  | `array`                                                | `query`           | 管道(\|)分隔数组值。该选项将`collectionFormat`替换为OpenAPI 2.0中的`pipes`。 |
| deepObject     | `object`                                               | `query`           | 提供一种使用表单参数嵌套的对象。 |



##### Style 示例：

假设`color`参数为以下值之一：

```
string -> "blue"
array -> ["blue","black","brown"]
object -> { "R": 100, "G": 200, "B": 150 }
```

差异对照表：

| [`style`](#data-type-format) | `explode` | `empty` | `string`    | `array`                             | `object`                               |
| ------------------------------------------------------------ | --------- | ------- | ----------- | ----------------------------------- | -------------------------------------- |
| matrix                                                       | false     | ;color  | ;color=blue | ;color=blue,black,brown             | ;color=R,100,G,200,B,150               |
| matrix                                                       | true      | ;color  | ;color=blue | ;color=blue;color=black;color=brown | ;R=100;G=200;B=150                     |
| label                                                        | false     | .       | .blue       | .blue.black.brown                   | .R.100.G.200.B.150                     |
| label                                                        | true      | .       | .blue       | .blue.black.brown                   | .R=100.G=200.B=150                     |
| form                                                         | false     | color=  | color=blue  | color=blue,black,brown              | color=R,100,G,200,B,150                |
| form                                                         | true      | color=  | color=blue  | color=blue&color=black&color=brown  | R=100&G=200&B=150                      |
| simple                                                       | false     | n/a     | blue        | blue,black,brown                    | R,100,G,200,B,150                      |
| simple                                                       | true      | n/a     | blue        | blue,black,brown                    | R=100,G=200,B=150                      |
| spaceDelimited                                               | false     | n/a     | n/a         | blue%20black%20brown                | R%20100%20G%20200%20B%20150            |
| pipeDelimited                                                | false     | n/a     | n/a         | blue\|black\|brown                  | R\|100\|G\|200\|B\|150                 |
| deepObject                                                   | true      | n/a     | n/a         | n/a                                 | color[R]=100&color[G]=200&color[B]=150 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

##### 示例：

###### 64位integer数组的header参数示例:

```json
{
  "name": "token",
  "in": "header",
  "description": "token to be passed as a header",
  "required": true,
  "schema": {
    "type": "array",
    "items": {
      "type": "integer",
      "format": "int64"
    }
  },
  "style": "simple"
}
```

```yaml
name: token
in: header
description: token to be passed as a header
required: true
schema:
  type: array
  items:
    type: integer
    format: int64
style: simple
```

###### string类型的path参数示例:

```json
{
  "name": "username",
  "in": "path",
  "description": "username to fetch",
  "required": true,
  "schema": {
    "type": "string"
  }
}
```

```yaml
name: username
in: path
description: username to fetch
required: true
schema:
  type: string
```

字符串类型的query参数，通过重复来实现传递多个值：

```json
{
  "name": "id",
  "in": "query",
  "description": "ID of the object to fetch",
  "required": false,
  "schema": {
    "type": "array",
    "items": {
      "type": "string"
    }
  },
  "style": "form",
  "explode": true
}
```

```yaml
name: id
in: query
description: ID of the object to fetch
required: false
schema:
  type: array
  items:
    type: string
style: form
explode: true
```

free-form的query参数，允许使用特定类型的undefined参数：

```json
{
  "in": "query",
  "name": "freeForm",
  "schema": {
    "type": "object",
    "additionalProperties": {
      "type": "integer"
    },
  },
  "style": "form"
}
```

```yaml
in: query
name: freeForm
schema:
  type: object
  additionalProperties:
    type: integer
style: form
```

使用`content`定义序列化的一个complex参数：

```json
{
  "in": "query",
  "name": "coordinates",
  "content": {
    "application/json": {
      "schema": {
        "type": "object",
        "required": [
          "lat",
          "long"
        ],
        "properties": {
          "lat": {
            "type": "number"
          },
          "long": {
            "type": "number"
          }
        }
      }
    }
  }
}
```

```yaml
in: query
name: coordinates
content:
  application/json:
    schema:
      type: object
      required:
        - lat
        - long
      properties:
        lat:
          type: number
        long:
          type: number
```



#### <span id='Request-Body-Object'>Request Body Object(请求体对象)</span>

描述单个request body.

##### Fixed Fields

| 属性名      |                           属性类型                           | 描述                                                         |
| ----------- | :----------------------------------------------------------: | ------------------------------------------------------------ |
| description |                           `string`                           | request body的简短描述. 使用[CommonMark syntax](https://spec.commonmark.org/)可以用于展示富文本。 |
| content     | Map[`string`, <a href='#Media-Type-Object'>Media Type Object</a>] | **必填**. request body的内容。key是media type或[media type range](https://tools.ietf.org/html/rfc7231#appendix--d)和能描述它的值。 对于匹配多个key的请求，只有最具体的key适用。 例如text/plain覆盖 text/* |
| required    |                          `boolean`                           | 确定请求中是否需要request body。默认值为`false`。            |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

##### 示例：

###### 带有model引用的request body

```json
{
  "description": "user to add to the system",
  "content": {
    "application/json": {
      "schema": {
        "$ref": "#/components/schemas/User"
      },
      "examples": {
          "user" : {
            "summary": "User Example", 
            "externalValue": "http://foo.bar/examples/user-example.json"
          } 
        }
    },
    "application/xml": {
      "schema": {
        "$ref": "#/components/schemas/User"
      },
      "examples": {
          "user" : {
            "summary": "User example in XML",
            "externalValue": "http://foo.bar/examples/user-example.xml"
          }
        }
    },
    "text/plain": {
      "examples": {
        "user" : {
            "summary": "User example in Plain text",
            "externalValue": "http://foo.bar/examples/user-example.txt" 
        }
      } 
    },
    "*/*": {
      "examples": {
        "user" : {
            "summary": "User example in other format",
            "externalValue": "http://foo.bar/examples/user-example.whatever"
        }
      }
    }
  }
}
```

```yaml
description: user to add to the system
content: 
  'application/json':
    schema:
      $ref: '#/components/schemas/User'
    examples:
      user:
        summary: User Example
        externalValue: 'http://foo.bar/examples/user-example.json'
  'application/xml':
    schema:
      $ref: '#/components/schemas/User'
    examples:
      user:
        summary: User Example in XML
        externalValue: 'http://foo.bar/examples/user-example.xml'
  'text/plain':
    examples:
      user:
        summary: User example in text plain format
        externalValue: 'http://foo.bar/examples/user-example.txt'
  '*/*':
    examples:
      user: 
        summary: User example in other format
        externalValue: 'http://foo.bar/examples/user-example.whatever
```

###### 字符串数组的body参数：

```json
{
  "description": "user to add to the system",
  "content": {
    "text/plain": {
      "schema": {
        "type": "array",
        "items": {
          "type": "string"
        }
      }
    }
  }
}
```

```yaml
description: user to add to the system
required: true
content:
  text/plain:
    schema:
      type: array
      items:
        type: string
```



#### <span id='Media-Type-Object'>Media Type Object(媒体类型对象)</span>

Media Type Object提供标识media type的key的schema和examples。

##### Fixed Fields

| 属性名   |                           属性类型                           | 描述                                                         |
| -------- | :----------------------------------------------------------: | ------------------------------------------------------------ |
| schema   | <a href='#Schema-Object'>Schema Object</a> \| <a href='#Reference-Object'>Reference Object</a> | 定义[请求](#Request-Body-Object)、[响应](#Response-Object)或[参数](#parameter-content)content的schema。 |
| example  |                             Any                              | media type示例。 示例应使用media type的正确格式。`example`属性和`examples`属性互斥。如果引用的`schema`包含示例，则该示例应覆盖`schema`提供的示例。 |
| examples | Map[ `string`, <a href='#Example-Object'>Example Object</a> \| <a href='#Reference-Object'>Reference Object</a>] | media type示例。如果存在，每个示例对象应该匹配media type和指定的schema。`example`属性和`examples`属性互斥。如果引用的`schema`包含示例，则该示例应覆盖`schema`提供的示例。 |
| encoding | Map[`string`, <a href='#Encoding-Object'>Encoding Object</a>] | 属性名与编码信息的映射。 作为属性名的key必须作为属性存在于schema中。 当media type为`multipart`或`application/x-www-form-urlencoded`时，编码对象应该仅适用于`requestBody`对象。 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

##### 示例：

```json
{
  "application/json": {
    "schema": {
         "$ref": "#/components/schemas/Pet"
    },
    "examples": {
      "cat" : {
        "summary": "An example of a cat",
        "value": 
          {
            "name": "Fluffy",
            "petType": "Cat",
            "color": "White",
            "gender": "male",
            "breed": "Persian"
          }
      },
      "dog": {
        "summary": "An example of a dog with a cat's name",
        "value" :  { 
          "name": "Puma",
          "petType": "Dog",
          "color": "Black",
          "gender": "Female",
          "breed": "Mixed"
        },
      "frog": {
          "$ref": "#/components/examples/frog-example"
        }
      }
    }
  }
}
```

```yaml
application/json: 
  schema:
    $ref: "#/components/schemas/Pet"
  examples:
    cat:
      summary: An example of a cat
      value:
        name: Fluffy
        petType: Cat
        color: White
        gender: male
        breed: Persian
    dog:
      summary: An example of a dog with a cat's name
      value:
        name: Puma
        petType: Dog
        color: Black
        gender: Female
        breed: Mixed
    frog:
      $ref: "#/components/examples/frog-example"
```

##### 文件上传的注意事项

与2.0规范相比，OpenAPI中的`file` input/output使用与其他schema类型相同的方式进行描述。具体如下：

```yaml
# content transferred with base64 encoding
schema:
  type: string
  format: base64
```

```yaml
# content transferred in binary (octet-stream):
schema:
  type: string
  format: binary
```

这些例子既适用于文件上传的输入载荷，也适用于响应载荷。

在`POST`请求中提交文件的`requestBody`参考如下：

```yaml
requestBody:
  content:
    application/octet-stream:
      schema:
        # a binary file of any type
        type: string
        format: binary
```

指定media types：

```yaml
# multiple, specific media types may be specified:
requestBody:
  content:
      # a binary file of type png or jpeg
    'image/jpeg':
      schema:
        type: string
        format: binary
    'image/png':
      schema:
        type: string
        format: binary        
```

上传多个文件，必须使用`multipart`的media type：

```yaml
requestBody:
  content:
    multipart/form-data:
      schema:
        properties:
          # The property name 'file' will be used for all files.
          file:
            type: array
            items:
              type: string
              format: binary
```

##### 支持x-www-form-urlencoded的请求

要使用[RFC1866](https://tools.ietf.org/html/rfc1866)的form url encoding提交内容，可以使用以下方式：

```yaml
requestBody:
  content:
    application/x-www-form-urlencoded:
      schema:
        type: object
        properties:
          id:
            type: string
            format: uuid
          address:
            # complex types are stringified to support RFC 1866
            type: object
            properties: {}
```

在这个例子中，`requestBody`中的content在传递给服务器时必须遵循[RFC1866](https://tools.ietf.org/html/rfc1866/)进行字符串化。`address`字段的复杂对象也将被字符串化。

在传递`application/x-www-form-urlencoded`类型的complex对象时，这些属性的默认序列化策略在<a href='#Encoding-Object'>Encoding Object</a>的[`style`](#encoding-style)属性中为`form`。



##### `multipart` Content的特殊注意事项

在请求操作时，通常使用`Content-Type`为`multipart/form-data`作为请求体。 与 2.0 相比，当使用`multipart` content时，需要定义一个`schema`作为请求的输入参数。这样做可以支持complex结构以及多个文件的上传。

当传入`multipart`类型时，可以使用分界线(—)来分隔正在传输内容的各个部分，以下是为`multipart`定义的几种默认`Content-Type`：

- 如果属性是primitive或者数组类型的primitive，默认的Content-Typ是`text/plain`
- 如果属性是复杂对象或者是数组类型的复杂对象，默认的Content-Type是`application/json`
- 如果属性是`type: string`和`format: binary`或`format: base64`（又名 file object），则默认Content-Type为 `application/octet-stream`

Examples:

```yaml
requestBody:
  content:
    multipart/form-data:
      schema:
        type: object
        properties:
          id:
            type: string
            format: uuid
          address:
            # default Content-Type for objects is `application/json`
            type: object
            properties: {}
          profileImage:
            # default Content-Type for string/binary is `application/octet-stream`
            type: string
            format: binary
          children:
            # default Content-Type for arrays is based on the `inner` type (text/plain here)
            type: array
            items:
              type: string
          addresses:
            # default Content-Type for arrays is based on the `inner` type (object shown, so `application/json` in this example)
            type: array
            items:
              type: '#/components/schemas/Address'
```

使用`encoding` 属性可以控制`multipart` 请求body的部分序列化。 此属性仅适用于`multipart`和`application/x-www-form-urlencoded` 请求的body。



#### <span id='Encoding-Object'>Encoding Object(编码对象)</span>

定义应用于单个schema属性的单个编码。

##### Fixed Fields

| 属性名        |                           属性类型                           | 描述                                                         |
| ------------- | :----------------------------------------------------------: | ------------------------------------------------------------ |
| contentType   |                           `string`                           | 用于编码特定属性的Content-Type。 默认值取决于属性类型：对`string`和`format`为`binary` – `application/octet-stream`; 对于其他primitive类型为`text/plain`； 对`object` 为`application/json`； 对`array`默认值是根据内部类型定义的。 该值可以是特定媒体类型（例如`application/json`）、通配符媒体类型（例如`image/*`）或用逗号分隔的列表。 |
| headers       | Map[`string`, <a href='#Header-Object'>Header Object</a>\| <a href='#Reference-Object'>Reference Object</a>] | 一个为header提供附加信息的map，例如`Content-Disposition`，`Content-Type`如果单独描述本属性应该忽略。 如果请求body的media type不是`multipart`，本属性应该忽略。 |
| style         |                           `string`                           | 描述如何根据类型序列化属性值。 有关[`style`](#parameter-style)属性的详细信息，参考<a href='#Parameter-Object'>Parameter Object</a>。 该行为遵循与query参数相同的值，包括默认值。 如果请求正文媒体类型不是`application/x-www-form-urlencoded`，则应忽略此属性。 |
| explode       |                          `boolean`                           | 当属性为true时，会为`array`的每一个值或`object`的key-value生成独立的参数放到map里。 对于其他类型，此属性无效。 当[`style`](#parameter-style)为`form`时，默认值为true；为其他值时，默认值为 false。如果请求正文媒体类型不是`application/x-www-form-urlencoded`，则应忽略此属性。 |
| allowReserved |                          `boolean`                           | 参数值是否为保留字，如[RFC3986](https://tools.ietf.org/html/rfc3986#section-2.2)所定义`:/?#[]@!$&'()*+,;=`在不包含URL编码(percent-encoding)的情况下。 默认值为false。如果请求正文媒体类型不是`application/x-www-form-urlencoded`，则应忽略此属性。 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

##### 示例：

```yaml
requestBody:
  content:
    multipart/mixed:
      schema:
        type: object
        properties:
          id:
            # default is text/plain
            type: string
            format: uuid
          address:
            # default is application/json
            type: object
            properties: {}
          historyMetadata:
            # need to declare XML format!
            description: metadata in XML format
            type: object
            properties: {}
          profileImage:
            # default is application/octet-stream, need to declare an image type only!
            type: string
            format: binary
      encoding:
        historyMetadata:
          # require XML Content-Type in utf-8 encoding
          contentType: application/xml; charset=utf-8
        profileImage:
          # only accept png/jpeg
          contentType: image/png, image/jpeg
          headers:
            X-Rate-Limit-Limit:
              description: The number of allowed requests in the current period
              schema:
                type: integer
```







#### <span id='Responses-Object'>Responses Object(响应容器对象)</span>

请求预期响应的容器。 容器内为一个或多个map，将HTTP响应码映射到预期响应。

该文档不一定会涵盖所有的HTTP响应码，因为它们可能事先并没有定义或说明。 但是文档应涵盖所有请求成功和错误的响应。

`default` 属性是如果配置的HTTP响应码没有匹配到，则默认响应的对象。

`Responses Object`至少应该定义一个成功请求的响应代码。

##### Fixed Fields

| 属性名  |                           属性类型                           | 描述                                                         |
| ------- | :----------------------------------------------------------: | ------------------------------------------------------------ |
| default | <a href='#Response-Object'>Response Object</a> \| <a href='#Reference-Object'>Reference Object</a> | 指定响应代码之外的默认响应。 <a href='#Reference-Object'>Reference Object</a>可以链接到[OpenAPI Object's components/responses](#components-responses)部分定义的响应。 |

##### Patterned Fields

| 匹配字段                        |                           属性类型                           | 描述                                                         |
| ------------------------------- | :----------------------------------------------------------: | ------------------------------------------------------------ |
| [HTTP Status Code](#http-codes) | <a href='#Response-Object'>Response Object</a> \| <a href='#Reference-Object'>Reference Object</a> | 任何 HTTP 状态代码都可以用作属性名称，但每个状态码只能定义一次。链接到[OpenAPI Object's components/responses](#components-responses)部分定义的响应。 此字段必须用引号引起来（例如"200"）以实现 JSON 和 YAML 之间的兼容性。 要定义响应代码的范围，此字段可以包含大写通配符`X`。例如`2XX`表示 [200-299] 之间的所有响应代码。 仅允许定义以下范围：`1XX`, `2XX`, `3XX`, `4XX`, 和`5XX`。 如果使用显式代码定义响应，则显式代码定义优先于该代码的范围定义。 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

##### Responses Object 示例：

###### 状态码为200和其他(错误)情况的响应示例:

```json
{
  "200": {
    "description": "a pet to be returned",
    "content": {
      "application/json": {
        "schema": {
          "$ref": "#/components/schemas/Pet"
        }
      }
    }
  },
  "default": {
    "description": "Unexpected error",
    "content": {
      "application/json": {
        "schema": {
          "$ref": "#/components/schemas/ErrorModel"
        }
      }
    }
  }
}
```

```yaml
'200':
  description: a pet to be returned
  content: 
    application/json:
      schema:
        $ref: '#/components/schemas/Pet'
default:
  description: Unexpected error
  content:
    application/json:
      schema:
        $ref: '#/components/schemas/ErrorModel
```



#### <span id='Response-Object'>Response Object(响应对象)</span>

描述来自API请求的单个响应，包括设计时基于响应请求的静态链接。

##### Fixed Fields

| 属性名      |                           属性类型                           | 描述                                                         |
| ----------- | :----------------------------------------------------------: | ------------------------------------------------------------ |
| description |                           `string`                           | **必填**. response的简短描述. 使用[CommonMark syntax](https://spec.commonmark.org/)可以用于展示富文本。 |
| headers     | Map[`string`, <a href='#Header-Object'>Header Object</a>\| <a href='#Reference-Object'>Reference Object</a>] | 以header名定义的一个map，header名以[RFC7230](https://tools.ietf.org/html/rfc7230#page-22)定义的状态为准，不区分大小写。如果定义的响应名为`"Content-Type"`，这个属性会被忽略。 |
| content     | Map[`string`, <a href='#Media-Type-Object'>Media Type Object</a>] | 包含响应载荷的map。key是media type或[media type range](https://tools.ietf.org/html/rfc7231#appendix--d)和能描述它的值。 对于匹配多个key的请求，只有最具体的key适用。 例如text/plain覆盖 text/* |
| links       | Map[`string`, <a href='#Link-Object'>Link Object</a> \| <a href='#Reference-Object'>Reference Object</a>] | 链接请求的map。map的key为link的短名称, 遵循<a href='#Components-Object'>Component Object</a>中的命名约束。 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

##### 示例：

###### 数组类型的复杂对象响应示例：

```json
{
  "description": "A complex object array response",
  "content": {
    "application/json": {
      "schema": {
        "type": "array",
        "items": {
          "$ref": "#/components/schemas/VeryComplexType"
        }
      }
    }
  }
}
```

```yaml
description: A complex object array response
content: 
  application/json:
    schema: 
      type: array
      items:
        $ref: '#/components/schemas/VeryComplexType'
```

###### string类型的响应示例:

```json
{
  "description": "A simple string response",
  "content": {
    "text/plain": {
      "schema": {
        "type": "string"
      }
    }
  }

}
```

```yaml
description: A simple string response
content:
  text/plain:
    schema:
      type: string
```

###### 带标题的纯文本响应示例：

```json
{
  "description": "A simple string response",
  "content": {
    "text/plain": {
      "schema": {
        "type": "string",
        "example": "whoa!"
      }
    }
  },
  "headers": {
    "X-Rate-Limit-Limit": {
      "description": "The number of allowed requests in the current period",
      "schema": {
        "type": "integer"
      }
    },
    "X-Rate-Limit-Remaining": {
      "description": "The number of remaining requests in the current period",
      "schema": {
        "type": "integer"
      }
    },
    "X-Rate-Limit-Reset": {
      "description": "The number of seconds left in the current period",
      "schema": {
        "type": "integer"
      }
    }
  }
}
```

```yaml
description: A simple string response
content:
  text/plain:
    schema:
      type: string
    example: 'whoa!'
headers:
  X-Rate-Limit-Limit:
    description: The number of allowed requests in the current period
    schema:
      type: integer
  X-Rate-Limit-Remaining:
    description: The number of remaining requests in the current period
    schema:
      type: integer
  X-Rate-Limit-Reset:
    description: The number of seconds left in the current period
    schema:
      type: integer
```

###### 没有返回值的响应示例:

```json
{
  "description": "object created"
}
```

```yaml
description: object created
```



#### <span id='Callback-Object'>Callback Object(回调对象)</span>

可能与父操作相关的回调map。map中的每个值都是一个<a href='#Path-Item-Object'>Path Item Object</a>，它描述了一组由API提供者发起的请求和预期的响应。用于标识<a href='#Path-Item-Object'>Path Item Object</a>的key是一个运行时表达式<a href='#Callback-Object-Expression'>(runtime expression)</a>，用它标识回调操作的URL。

##### Patterned Fields

| 匹配字段     |                     属性类型                     | 描述                                                         |
| ------------ | :----------------------------------------------: | ------------------------------------------------------------ |
| {expression} | <a href='#Path-Item-Object'>Path Item Object</a> | 定义回调请求和响应的<a href='#Path-Item-Object'>Path Item Object</a>对象.  A [complete example](https://swagger.io/specification/examples/v3.0/callback-example.yaml) is available. |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

##### <span id='Callback-Object-Expression'>Key Expression</span>

标识<a href='#Path-Item-Object'>Path Item Object</a>的key是一个运行时表达式，可以在运行HTTP 请求/响应时，从上下文中获取值以标识要用于回调请求的URL，使用运行时表达式可以访问完整的HTTP请求。这包括访问[RFC6901](https://tools.ietf.org/html/rfc6901)引用正文的任何部分。

如HTTP请求如下：

```http
POST /subscribe/myevent?queryUrl=http://clientdomain.com/stillrunning HTTP/1.1
Host: example.org
Content-Type: application/json
Content-Length: 187

{
  "failedUrl" : "http://clientdomain.com/failed",
  "successUrls" : [
    "http://clientdomain.com/fast",
    "http://clientdomain.com/medium",
    "http://clientdomain.com/slow"
  ] 
}

201 Created
Location: http://example.org/subscription/1
```

以下展示了使用表达式的方式，假设回调操作有一个名为`eventType` 的path参数和一个名为`queryUrl`的query参数。

| <span id='Callback-Object-Expression'>Expression</span> | Value                                                        |
| ------------------------------------------------------- | :----------------------------------------------------------- |
| $url                                                    | [http://example.org/subscribe/myevent?queryUrl=http://clientdomain.com/stillrunning](http://example.org/subscribe/myevent?query-url=http://clientdomain.com/stillrunning) |
| $method                                                 | POST                                                         |
| $request.path.eventType                                 | myevent                                                      |
| $request.query.queryUrl                                 | http://clientdomain.com/stillrunning                         |
| $request.header.content-Type                            | application/json                                             |
| $request.body#/failedUrl                                | http://clientdomain.com/failed                               |
| $request.body#/successUrls/2                            | http://clientdomain.com/medium                               |
| $response.header.Location                               | http://example.org/subscription/1                            |

##### Callback Object示例：

以下示例使用用户提供的`queryUrl`查询字符串参数来定义回调 URL。

```yaml
myCallback:
  '{$request.query.queryUrl}':
    post:
      requestBody:
        description: Callback payload
        content: 
          'application/json':
            schema:
              $ref: '#/components/schemas/SomePayload'
      responses:
        '200':
          description: callback successfully processed
```

服务器是固定的，但查询字符串参数是取请求正文中的`id` 和`email` 属性填充的回调示例。

```yaml
transactionCallback:
  'http://notificationServer.com?transactionId={$request.body#/id}&email={$request.body#/email}':
    post:
      requestBody:
        description: Callback payload
        content: 
          'application/json':
            schema:
              $ref: '#/components/schemas/SomePayload'
      responses:
        '200':
          description: callback successfully processed
```



#### <span id='Example-Object'>Example Object(示例对象)</span>

##### Fixed Fields

| 属性名        | 属性类型 | 描述                                                         |
| ------------- | :------: | ------------------------------------------------------------ |
| summary       | `string` | 示例的简短描述。                                             |
| description   | `string` | 示例的完整描述，使用[CommonMark syntax](https://spec.commonmark.org/)可以用于展示富文本。 |
| value         |   Any    | 嵌入式示例值(直接写在这里的示例)。`value` 字段和`externalValue` 字段是互斥的。不能在JSON或YAML中自然表示的媒体类型，请使用字符串值来表示，并在必要时转义。 |
| externalValue | `string` | 指向示例的URL(引用示例)。提供了不方便展示示例(如太长了)的引用能力。`value` 字段和`externalValue` 字段是互斥的。 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

所有情况下，示例值都应该与关联的schema类型兼容。工具实现可以选择自动验证兼容性，如果不兼容则拒绝示例值。

##### 示例：

###### 在request body中的示例:

```yaml
requestBody:
  content:
    'application/json':
      schema:
        $ref: '#/components/schemas/Address'
      examples: 
        foo:
          summary: A foo example
          value: {"foo": "bar"}
        bar:
          summary: A bar example
          value: {"bar": "baz"}
    'application/xml':
      examples: 
        xmlExample:
          summary: This is an example in XML
          externalValue: 'http://example.org/examples/address-example.xml'
    'text/plain':
      examples:
        textExample: 
          summary: This is a text example
          externalValue: 'http://foo.bar/examples/address-example.txt'
```

###### 在parameter中的示例:

```yaml
parameters:
  - name: 'zipCode'
    in: 'query'
    schema:
      type: 'string'
      format: 'zip-code'
    examples:
      zip-example: 
        $ref: '#/components/examples/zip-example'
```

###### 在response中的示例:

```yaml
responses:
  '200':
    description: your car appointment has been booked
    content: 
      application/json:
        schema:
          $ref: '#/components/schemas/SuccessResponse'
        examples:
          confirmation-success:
            $ref: '#/components/examples/confirmation-success'
```



#### <span id='Link-Object'>Link Object(链接对象)</span>

`Link object` 表示响应的可能设计时链接。链接存在并不能保证能够成功调用，它只是提供了响应和其他操作之间的关系和遍历机制。

Unlike *dynamic* links (i.e. links provided **in** the response payload), the OAS linking mechanism does not require link information in the runtime response.

与动态链接（响应负载中提供的链接）不同，运行的时候，OAS链接机制不需要响应中的链接信息。

[Runtime表达式](#runtime-expression)用于访问操作中的值，并在调用时将它们作为参数。

##### Fixed Fields

| 属性名       |                         属性类型                          | 描述                                                         |
| ------------ | :-------------------------------------------------------: | ------------------------------------------------------------ |
| operationRef |                         `string`                          | 对 OAS请求的相对或绝对URI引用。该字段与`operationId`字段互斥，并且必须指向一个<a href='#Operation-Object'>Operation  Object</a> 。相对`operationRef` 值可用于定位OpenAPI定义中的现有<a href='#Operation-Object'>Operation  Object</a> 。 |
| operationId  |                         `string`                          | 已定义并且可解析的OAS请求的名称，由唯一的`operationId`定义。该字段与`operationRef`字段互斥。 |
| parameters   | Map[`string`, Any \| [{expression}](#runtime-expression)] | 表示要传递给请求参数的map，该请求由`operationId` 指定或通过`operationRef`标识。key是要使用的参数名称，而值可以是常量或[Runtime表达式](#runtime-expression)。对于在不同位置（例如  path.id）使用相同参数名称的请求，可以使用[参数位置](#parameter-in) `[{in}.]{name}`来限定参数名称。 |
| requestBody  |        Any \| [{expression}](#runtime-expression)         | 调用目标请求时用作请求body的文字值或[Runtime表达式](#runtime-expression)。 |
| description  |                         `string`                          | 链接的描述，使用[CommonMark syntax](https://spec.commonmark.org/)可以用于展示富文本。 |
| server       |        <a href='#Server-Object'>Server Object</a>         | 目标请求要使用的服务器对象。                                 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

必须使用`operationRef`或 `operationId`来标识链接操作。在使用 `operationId`的情况下，它必须是在OAS文档的范围内唯一定义的。由于名称可能会冲突，因此具有外部引用的规范推荐使用`operationRef`。

##### 示例：

在请求中使用表达式传递link的参数，如`$request.path.id`。

```yaml
paths:
  /users/{id}:
    parameters:
    - name: id
      in: path
      required: true
      description: the user identifier, as userId 
      schema:
        type: string
    get:
      responses:
        '200':
          description: the user being returned
          content:
            application/json:
              schema:
                type: object
                properties:
                  uuid: # the unique user id
                    type: string
                    format: uuid
          links:
            address:
              # the target link operationId
              operationId: getUserAddress
              parameters:
                # get the `id` field from the request path parameter named `id`
                userId: $request.path.id
  # the path item of the linked operation
  /users/{userid}/address:
    parameters:
    - name: userid
      in: path
      required: true
      description: the user identifier, as userId 
      schema:
        type: string
    # linked operation
    get:
      operationId: getUserAddress
      responses:
        '200':
          description: the user's address
```

在运行的时候，当表达式无法计算(错误)时，不会将参数值传递给请求。

response body中的值可用于链接。

```yaml
links:
  address:
    operationId: getUserAddressByUUID
    parameters:
      # get the `uuid` field from the `uuid` field in the response body
      userUuid: $response.body#/uuid
```

工具自行决定是否遵循所有链接。权限和成功调用该链接的能力都不能仅由关系来保证。

##### OperationRef Examples

由于使用`operationId` 进行引用时，`operationId` 可能为空（`operationId`是[请求对象](#operation-object)中的一个可选字段），因此也可以通过 `operationRef`进行相对引用：

```yaml
links:
  UserRepositories:
    # returns array of '#/components/schemas/repository'
    operationRef: '#/paths/~12.0~1repositories~1{username}/get'
    parameters:
      username: $response.body#/username
```

或绝对引用：

```yaml
links:
  UserRepositories:
    # returns array of '#/components/schemas/repository'
    operationRef: 'https://na2.gigantic-server.com/#/paths/~12.0~1repositories~1{username}/get'
    parameters:
      username: $response.body#/username
```

请注意，在JSON中使用 `operationRef`时，正斜杠需要转义。

##### <span id='runtime-expression'>Runtime Expressions</span>

使用Runtime表达式可以在API调用过程中，从HTTP消息里提取信息来定义值。<a href='#Link-Object'>Link Object</a>和<a href='#Callback-Object'>Callback Object</a>都使用此机制。

Runtime表达式由下列[ABNF](https://tools.ietf.org/html/rfc5234)语法定义：

```abnf
      expression = ( "$url" / "$method" / "$statusCode" / "$request." source / "$response." source )
      source = ( header-reference / query-reference / path-reference / body-reference )
      header-reference = "header." token
      query-reference = "query." name  
      path-reference = "path." name
      body-reference = "body" ["#" json-pointer ]
      json-pointer    = *( "/" reference-token )
      reference-token = *( unescaped / escaped )
      unescaped       = %x00-2E / %x30-7D / %x7F-10FFFF
         ; %x2F ('/') and %x7E ('~') are excluded from 'unescaped'
      escaped         = "~" ( "0" / "1" )
        ; representing '~' and '/', respectively
      name = *( CHAR )
      token = 1*tchar
      tchar = "!" / "#" / "$" / "%" / "&" / "'" / "*" / "+" / "-" / "." /
        "^" / "_" / "`" / "|" / "~" / DIGIT / ALPHA
```

这里的 `json-pointer`来自[RFC 6901](https://tools.ietf.org/html/rfc6901)，`char`来自[RFC 7159](https://tools.ietf.org/html/rfc7159#section-7) ，`token`来自[RFC 7230](https://tools.ietf.org/html/rfc7230#section-3.2.6).

`name` 标识符区分大小写，而`token` 不区分大小写。

下表提供了Runtime表达式的示例及其在值中的使用示例：

##### 表达式对照表

| 数据位置              | 示例表达式                 | 说明                                                         |
| --------------------- | :------------------------- | :----------------------------------------------------------- |
| HTTP Method           | `$method`                  | 提取请求的method                                             |
| Requested media type  | `$request.header.accept`   | 提取请求的header                                             |
| Request parameter     | `$request.path.id`         | 提取请求的参数，请求参数必须在请求的`parameters`中声明，否则无法提取。包括请求header。 |
| Request body property | `$request.body#/user/uuid` | 提取请求body的值，在请求载荷中，可以对请求body的部分或整个body进行引用。 |
| Request URL           | `$url`                     | 提取请求的URL                                                |
| Response value        | `$response.body#/status`   | 提取返回值，在返回有效载荷中，可以对响应body部分或整个body进行提取。 |
| Response header       | `$response.header.Server`  | 提取返回的header，仅单个header可用                           |

Runtime表达式保留提取值的类型。将表达式放到`{}`花括号内，可以嵌入到字符串中。



#### <span id='Header-Object'>Header Object(头对象)</span>

Header Object遵循<a href='#Parameter-Object'>Parameter Object</a>的结构，但有下列不同：

1. 不能指定`name` ，它在相应的header的map中给出。
2. 不能指定`in`，它隐含在`header`中.
3. 受位置影响的所有特征必须适用于`header` 位置（例如 [`style`](#parameter-style)）。

##### 示例：

###### `integer`类型的header示例：

```json
{
  "description": "The number of allowed requests in the current period",
  "schema": {
    "type": "integer"
  }
}
```

```yaml
description: The number of allowed requests in the current period
schema:
  type: integer
```



#### <span id='Tag-Object'>Tag Object(标签对象)</span>

将metadata添加到<a href='#Operation-Object'>Operation  Object</a>使用的单个标签信息。在<a href='#Operation-Object'>Operation  Object</a>实例中定义的每个标签都不是必填的。

##### Fixed Fields

| 属性名       |                           属性类型                           | 描述                                                         |
| ------------ | :----------------------------------------------------------: | ------------------------------------------------------------ |
| name         |                           `string`                           | **必填**，标签的名字。                                       |
| description  |                           `string`                           | 标签的简短描述， 使用[CommonMark syntax](https://spec.commonmark.org/)可以用于展示富文本。 |
| externalDocs | <a href='#External-Documentation-Object'>External Documentation Object</a> | 额外的外部文档。                                             |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

##### 示例：

```json
{
	"name": "pet",
	"description": "Pets operations"
}
name: pet
description: Pets operations
```

```yaml
name: pet
description: Pets operations
```



#### <span id='Reference-Object'>Reference Object(引用对象)</span>

一个简单对象，允许在内部和外部引用规范中的其他组件。该对象由[JSON Reference](https://tools.ietf.org/html/draft-pbryan-zyp-json-ref-03)定义，遵循相同的结构、行为和规则。关于该规范，引用解析是由[JSON Reference](https://tools.ietf.org/html/draft-pbryan-zyp-json-ref-03)定义的，而不是由[JSON Schema](https://tools.ietf.org/html/draft-wright-json-schema-00#section-4.2)定义的。

固定字段：

| 属性名 | 属性类型 | 描述             |
| ------ | -------- | ---------------- |
| $ref   | `string` | 必填，引用字符串 |

此对象不能使用附加属性进行扩展，并且应忽略添加的其他任何属性。

##### 示例：

```json
{
	"$ref": "#/components/schemas/Pet"
}
```

```yaml
$ref: '#/components/schemas/Pet'
```

##### 相对Schema文档：

```json
{
  "$ref": "Pet.json"
}
```

```yaml
$ref: Pet.yaml
```

##### 相对于Schema文档内的对象

```json
{
  "$ref": "definitions.json#/Pet"
}
```

```yaml
$ref: definitions.yaml#/Pet
```



#### <span id='Schema-Object'>Schema Object(架构对象)</span>

Schema Object 允许定义输入和输出数据类型。 这些类型可以是`objects`，也可以是`primitives`和`arrays`。 此对象是[JSON Schema Specification Wright Draft 00](https://tools.ietf.org/html/draft-wright-json-schema-00#section-4.2)的扩展子集。

更多信息请参考 [JSON Schema Core](https://tools.ietf.org/html/draft-wright-json-schema-00) 和[JSON Schema Validation](https://tools.ietf.org/html/draft-wright-json-schema-validation-00)。 如无例外，属性定义遵循 JSON Schema。

Properties

以下属性直接取自JSON Schema定义，遵循相同的规范:

- title
- multipleOf
- maximum
- exclusiveMaximum
- minimum
- exclusiveMinimum
- maxLength
- minLength
- pattern (正则表达式字符串，使用[Ecma-262 Edition 5.1 regular expression](https://www.ecma-international.org/ecma-262/5.1/#sec-15.10.1)方言)
- maxItems
- minItems
- uniqueItems
- maxProperties
- minProperties
- required
- enum

  

以下属性取自JSON Schema 定义，但是根据OpenAPI规范进行了部分调整，调整如下：

- type - 必须为字符串且不能通过数组传递多个.
- allOf - 内联或引用的schema 必须是 <a href='#Schema-Object'>Schema Object</a>，而不是标准的 JSON Schema。
- oneOf - 内联或引用的schema 必须是 <a href='#Schema-Object'>Schema Object</a>，而不是标准的 JSON Schema。
- anyOf - 内联或引用的schema 必须是 <a href='#Schema-Object'>Schema Object</a>，而不是标准的 JSON Schema。
- not - 内联或引用的schema 必须是 <a href='#Schema-Object'>Schema Object</a>，而不是标准的 JSON Schema。
- items -值必须是一个`object`而不是一个`array`.  内联或引用的schema 必须是 <a href='#Schema-Object'>Schema Object</a>，而不是标准的 JSON Schema。如果`type`为`array`则`items`必须存在
- properties - 属性定义必须是 <a href='#Schema-Object'>Schema Object</a>，而不是标准的 JSON Schema。 (内联或引用).
- additionalProperties - 值可以为一个`boolean`或者一个`object`. 内联或引用的schema 必须是 <a href='#Schema-Object'>Schema Object</a>，而不是标准的 JSON Schema。和JSON Schema一致，additionalProperties默认为true。
- description - [CommonMark syntax](https://spec.commonmark.org/)可用富文本表示。
- format - 参考[Data Type Formats](#data-type-format)。在依赖于JSON Schema的定义格式的同时，OAS还提供了一些额外的预定义格式。
- default - 如果未提供默认值，则使用该值作为默认值。和 JSON Schema 不同的是该值必须符合在同一级别定义的 <a href='#Schema-Object'>Schema Object</a>的定义类型。 例如，如果`type`是 `string`，那么` default`可以是 `"foo"` 但不能是 `1`。

在任何地方使用<a href='#Schema-Object'>Schema Object</a>，都可以在当前位置上使用 <a href='#Reference-Object'>Reference Object</a>。在可以使用引用定义的情况下请勿使用内联定义。

除了以上提及的属性，其他由JSON Schema规范定义的属性是不受支持的，另外，可以使用以下字段进一步的描述Schema：

##### Fixed Fields

| 属性名        |                           属性类型                           | 描述                                                         |
| ------------- | :----------------------------------------------------------: | ------------------------------------------------------------ |
| nullable      |                          `boolean`                           | 为`true`时，`type`允许为`"null"`，只在同一个Schema Object中明确定义`type`时有效。  默认值为`false`。 |
| discriminator |   <a href='#Discriminator-Object'>Discriminator Object</a>   | 增加对polymorphism(多态)的支持。discriminator是一个对象名称，用于区分可满足条件的其他schemas。参考[Composition and Inheritance](#schema-composition)了解更多细节。 |
| readOnly      |                          `boolean`                           | 仅与Schema的`"properties"`定义相关。 将属性声明为只读。如果该属性为`true`并且在`required`列表中，则`required`将仅对响应生效（对请求无效）。 `readOnly`和 `writeOnly`不能同时为`true`。 该属性默认值为`false`。 |
| writeOnly     |                          `boolean`                           | 仅与Schema的`"properties"`定义相关。 将属性声明为只写。 与`readOnly`相反，则`required`将仅对请求生效（对响应无效）。`readOnly`和 `writeOnly`不能同时为`true`。 该属性默认值为`false`。 |
| xml           |             <a href='#XML-Object'>XML Object</a>             | 只能用于properties schemas。 对root schemas没有影响。 添加其他的metadata来描述此属性的XML。 |
| externalDocs  | <a href='#External-Documentation-Object'>External Documentation Object</a> | 此schema的其他外部文档。                                     |
| example       |                             Any                              | 此schema实例示例的free-form属性。 不能用JSON或YAML展示的示例，可以使用字符串表示，并在必要时进行转义。 |
| deprecated    |                          `boolean`                           | 指定schema已被弃用并且应该停止使用。 默认值为`false`。       |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

###### 组合和继承 (多态)

OpenAPI 规范可以使用JSON Schema的allOf属性来组合和扩展模型(model)的定义。 `allOf`是一个object的数组，这些object互相独立，但共同组成一个对象。

虽然组合提供了模型(model)可扩展性，但这并不代表模型(model)之间存在层次结构。 为了支持多态性，OpenAPI 规范添加了`discriminator`字段。 `discriminator`决定要验证schema定义的哪些属性的名称。 所以`discriminator` 字段必须为必填字段。 有两种方法可以为继承实例定义`discriminator` 的值。

- 使用schema的名字。
- 重写schema的名字来覆盖老的值，内联模式定义的情况下，如果没有指定id，将不能用于多态性。

###### XML Modeling

当将JSON转换为[xml](#schema-xml)时，xml属性允许额外的定义。 <a href='#XML-Object'>XML Object</a>包含有关可用属性的其他信息。

##### 示例：

###### 普通值(Primitive)

```json
{
  "type": "string",
  "format": "email"
}
```

```yaml
type: string
format: email
```

###### 简单模型

```json
{
  "type": "object",
  "required": [
    "name"
  ],
  "properties": {
    "name": {
      "type": "string"
    },
    "address": {
      "$ref": "#/components/schemas/Address"
    },
    "age": {
      "type": "integer",
      "format": "int32",
      "minimum": 0
    }
  }
}
```

```yaml
type: object
required:
- name
properties:
  name:
    type: string
  address:
    $ref: '#/components/schemas/Address'
  age:
    type: integer
    format: int32
    minimum: 0
```

###### map模型/数据字典

```json
{
  "type": "object",
  "additionalProperties": {
    "type": "string"
  }
}
```

```yaml
type: object
additionalProperties:
  type: string
```

###### 引用模型

```json
{
  "type": "object",
  "additionalProperties": {
    "$ref": "#/components/schemas/ComplexModel"
  }
}
```

```yaml
type: object
additionalProperties:
  $ref: '#/components/schemas/ComplexModel'
```

###### 带示例的模型

```json
{
  "type": "object",
  "properties": {
    "id": {
      "type": "integer",
      "format": "int64"
    },
    "name": {
      "type": "string"
    }
  },
  "required": [
    "name"
  ],
  "example": {
    "name": "Puma",
    "id": 1
  }
}
```

```yaml
type: object
properties:
  id:
    type: integer
    format: int64
  name:
    type: string
required:
- name
example:
  name: Puma
  id: 1
```

###### 组合的模型

```json
{
  "components": {
    "schemas": {
      "ErrorModel": {
        "type": "object",
        "required": [
          "message",
          "code"
        ],
        "properties": {
          "message": {
            "type": "string"
          },
          "code": {
            "type": "integer",
            "minimum": 100,
            "maximum": 600
          }
        }
      },
      "ExtendedErrorModel": {
        "allOf": [
          {
            "$ref": "#/components/schemas/ErrorModel"
          },
          {
            "type": "object",
            "required": [
              "rootCause"
            ],
            "properties": {
              "rootCause": {
                "type": "string"
              }
            }
          }
        ]
      }
    }
  }
}
```

```yaml
components:
  schemas:
    ErrorModel:
      type: object
      required:
      - message
      - code
      properties:
        message:
          type: string
        code:
          type: integer
          minimum: 100
          maximum: 600
    ExtendedErrorModel:
      allOf:
      - $ref: '#/components/schemas/ErrorModel'
      - type: object
        required:
        - rootCause
        properties:
          rootCause:
            type: string
```

###### 支持多台的模型

```json
{
  "components": {
    "schemas": {
      "Pet": {
        "type": "object",
        "discriminator": {
          "propertyName": "petType"
        },
        "properties": {
          "name": {
            "type": "string"
          },
          "petType": {
            "type": "string"
          }
        },
        "required": [
          "name",
          "petType"
        ]
      },
      "Cat": {
        "description": "A representation of a cat. Note that `Cat` will be used as the discriminator value.",
        "allOf": [
          {
            "$ref": "#/components/schemas/Pet"
          },
          {
            "type": "object",
            "properties": {
              "huntingSkill": {
                "type": "string",
                "description": "The measured skill for hunting",
                "default": "lazy",
                "enum": [
                  "clueless",
                  "lazy",
                  "adventurous",
                  "aggressive"
                ]
              }
            },
            "required": [
              "huntingSkill"
            ]
          }
        ]
      },
      "Dog": {
        "description": "A representation of a dog. Note that `Dog` will be used as the discriminator value.",
        "allOf": [
          {
            "$ref": "#/components/schemas/Pet"
          },
          {
            "type": "object",
            "properties": {
              "packSize": {
                "type": "integer",
                "format": "int32",
                "description": "the size of the pack the dog is from",
                "default": 0,
                "minimum": 0
              }
            },
            "required": [
              "packSize"
            ]
          }
        ]
      }
    }
  }
}
```

```yaml
components:
  schemas:
    Pet:
      type: object
      discriminator:
        propertyName: petType
      properties:
        name:
          type: string
        petType:
          type: string
      required:
      - name
      - petType
    Cat:  ## "Cat" will be used as the discriminator value
      description: A representation of a cat
      allOf:
      - $ref: '#/components/schemas/Pet'
      - type: object
        properties:
          huntingSkill:
            type: string
            description: The measured skill for hunting
            enum:
            - clueless
            - lazy
            - adventurous
            - aggressive
        required:
        - huntingSkill
    Dog:  ## "Dog" will be used as the discriminator value
      description: A representation of a dog
      allOf:
      - $ref: '#/components/schemas/Pet'
      - type: object
        properties:
          packSize:
            type: integer
            format: int32
            description: the size of the pack the dog is from
            default: 0
            minimum: 0
        required:
        - packSize
```



#### <span id='Discriminator-Object'>Discriminator Object</span>

当请求body或响应有效载荷是许多不同schema之一时，鉴别器对象(`discriminator object`)可用于帮助序列化、反序列化和验证。鉴别器是schema的特定对象，用于根据与其关联的值通知消费者替代schema的规范。

当使用`discriminator`时候，内联模式将会失效。

##### Fixed Fields

| 属性名       |        属性类型         | 描述                                                         |
| ------------ | :---------------------: | ------------------------------------------------------------ |
| propertyName |        `string`         | **必填**，鉴别器值的属性的名称。                             |
| mapping      | Map[`string`, `string`] | 用于保存有效负载值(`payload value`)和schema名称或引用之间映射的的对象。 |

只有在使用复合关键字`oneOf`, `anyOf`, `allOf`之一时，鉴别器对象才是可用的。

在OAS 3.0中，响应载荷可以被描述为任意数量的类型之一：

```yaml
MyResponseType:
  oneOf:
  - $ref: '#/components/schemas/Cat'
  - $ref: '#/components/schemas/Dog'
  - $ref: '#/components/schemas/Lizard'
```

在上面的例子中，响应的有效载荷必须与 `Cat`、 `Dog`或`Lizard`描述的schema之一完全匹配才可以通过验证。在这种情况下，使用鉴别器可以快速验证和选择匹配schema，这可能是比较消耗资源的操作，具体取决于schema的复杂性。也可以准确地指定字段使用哪个模式：

```yaml
MyResponseType:
  oneOf:
  - $ref: '#/components/schemas/Cat'
  - $ref: '#/components/schemas/Dog'
  - $ref: '#/components/schemas/Lizard'
  discriminator:
    propertyName: petType
```

在上面例子中，期望是一个名为`petType`的属性必须出现在响应载荷中，并且该值将对应于OAS文档中定义的schema名称。因此响应有效载荷虚构为：

```json
{
  "id": 12345,
  "petType": "Cat"
}
```

这样会指定`Cat`的schema与此有效载荷一起使用。

在鉴别器字段的值与schema的名称不匹配或不能隐式映射的情况下，可以选择直接定义映射关系：

```yaml
MyResponseType:
  oneOf:
  - $ref: '#/components/schemas/Cat'
  - $ref: '#/components/schemas/Dog'
  - $ref: '#/components/schemas/Lizard'
  - $ref: 'https://gigantic-server.com/schemas/Monster/schema.json'
  discriminator:
    propertyName: petType
    mapping:
      dog: '#/components/schemas/Dog'
      monster: 'https://gigantic-server.com/schemas/Monster/schema.json'
```

这里 `dog` 的鉴别器值将映射到模式`#/components/schemas/Dog`，而不是 `Dog`的默认（隐式映射）的值。如果鉴别器值与隐式或显式映射不匹配，则无法确定schema并且应该验证失败。映射key必须是字符串，但工具可以将响应值转换为字符串以进行比较。

当与`anyOf`构造结合使用时，鉴别器的使用可以避免多个schema可能满足单个有效载荷的歧义。

在`oneOf` 和`anyOf` 用例中，必须明确列出所有可能的schema。为了避免重复定义，可以将鉴别器添加到父schema定义中，并且在`allOf` 构造中包含父schema的所有schema都可以用作替代schema。

举个栗子：

```yaml
components:
  schemas:
    Pet:
      type: object
      required:
      - petType
      properties:
        petType:
          type: string
      discriminator:
        propertyName: petType
        mapping:
          dog: Dog
    Cat:
      allOf:
      - $ref: '#/components/schemas/Pet'
      - type: object
        # all other properties specific to a `Cat`
        properties:
          name:
            type: string
    Dog:
      allOf:
      - $ref: '#/components/schemas/Pet'
      - type: object
        # all other properties specific to a `Dog`
        properties:
          bark:
            type: string
    Lizard:
      allOf:
      - $ref: '#/components/schemas/Pet'
      - type: object
        # all other properties specific to a `Lizard`
        properties:
          lovesRocks:
            type: boolean
```

响应载荷虚构如下：

```json
{
  "petType": "Cat",
  "name": "misty"
}
```

以上响应载荷将指定`Cat` schema。另一个响应载荷：

```json
{
  "petType": "dog",
  "bark": "soft"
}
```

由于`Pet`定义了`mapping`映射元素，所以`dog`将映射到`Dog`。



#### <span id='XML-Object'>XML Object(XML对象)</span>

一个元数据对象(`metadata object`)，可以更使用详细的 XML模型定义。

使用数组时，无法从XML的元素名称推断出是单个还是多个，应该使用`name` 属性来添加该信息。请参阅示例以了解预期行为。

##### Fixed Fields

| 属性名    | 属性类型  | 描述                                                         |
| --------- | :-------: | ------------------------------------------------------------ |
| name      | `string`  | 替换用于描述的模式属性的元素/属性(element/attribute)的名称。在`items`中定义时，它将影响列表中各个XML元素的名称。当`wrapped`为`true`时，与`type`为`array`(在`items`外)一起定义，它将影响包装元素。如果`wrapped`为`false`，它将被忽略。 |
| namespace | `string`  | 命名空间(`namespace`)定义的URI。值必须为绝对地址。           |
| prefix    | `string`  | 用于[名称](#xml-name)的前缀(prefix)。                        |
| attribute | `boolean` | 声明属性定义是否转换为属性(property)而不是元素(attribute)。默认值为`false`。 |
| wrapped   | `boolean` | 只能用于数组定义。表示数组是被包装的（例如，`<books><book/><book/></books>`）还是未包装的（`<book/><book/>`）。默认值为`false`。该定义仅在`type`为`array`并在`items`外一起定义时生效。 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

##### 示例：

XML对象定义的示例包含在<a href='#Schema-Object'>Schema Object</a> 的属性定义中，并带有它的XML表示示例。

###### 没有XML元素

基本字符串属性：

```json
{
    "animals": {
        "type": "string"
    }
}
```

```yaml
animals:
  type: string
```

```xml
<animals>...</animals>
```

基本字符串数组属性([`wrapped`](#xml-wrapped) 默认为`false`):

```json
{
    "animals": {
        "type": "array",
        "items": {
            "type": "string"
        }
    }
}
```

```yaml
animals:
  type: array
  items:
    type: string
```

```xml
<animals>...</animals>
<animals>...</animals>
<animals>...</animals>
```

###### XML名称替换

```json
{
  "animals": {
    "type": "string",
    "xml": {
      "name": "animal"
    }
  }
}
```

```yaml
animals:
  type: string
  xml:
    name: animal
```

```xml
<animal>...</animal>
```

###### XML 属性、前缀和命名空间

该示例展示了完整的模型定义。

```json
{
  "Person": {
    "type": "object",
    "properties": {
      "id": {
        "type": "integer",
        "format": "int32",
        "xml": {
          "attribute": true
        }
      },
      "name": {
        "type": "string",
        "xml": {
          "namespace": "http://example.com/schema/sample",
          "prefix": "sample"
        }
      }
    }
  }
}
```

```yaml
Person:
  type: object
  properties:
    id:
      type: integer
      format: int32
      xml:
        attribute: true
    name:
      type: string
      xml:
        namespace: http://example.com/schema/sample
        prefix: sample
```

```xml
<Person id="123">
    <sample:name xmlns:sample="http://example.com/schema/sample">example</sample:name>
</Person>
```

###### XML Arrays

修改元素(element)的名称：

```json
{
  "animals": {
    "type": "array",
    "items": {
      "type": "string",
      "xml": {
        "name": "animal"
      }
    }
  }
}
```

```yaml
animals:
  type: array
  items:
    type: string
    xml:
      name: animal
```

```xml
<animal>value</animal>
<animal>value</animal>
```

如果没有定义`wrapped`为`true`，外部`name`属性对XML没有影响：

```json
{
  "animals": {
    "type": "array",
    "items": {
      "type": "string",
      "xml": {
        "name": "animal"
      }
    },
    "xml": {
      "name": "aliens"
    }
  }
}
```

```yaml
animals:
  type: array
  items:
    type: string
    xml:
      name: animal
  xml:
    name: aliens
```

```xml
<animal>value</animal>
<animal>value</animal>
```

如果数组被包装且没有明确定义名称，内部和外部都将使用相同的名称：

```json
{
  "animals": {
    "type": "array",
    "items": {
      "type": "string"
    },
    "xml": {
      "wrapped": true
    }
  }
}
```

```yaml
animals:
  type: array
  items:
    type: string
  xml:
    wrapped: true
```

```xml
<animals>
  <animals>value</animals>
  <animals>value</animals>
</animals>
```

为了解决上面示例中的命名问题，可以使用以下定义：

```json
{
  "animals": {
    "type": "array",
    "items": {
      "type": "string",
      "xml": {
        "name": "animal"
      }
    },
    "xml": {
      "wrapped": true
    }
  }
}
```

```yaml
animals:
  type: array
  items:
    type: string
    xml:
      name: animal
  xml:
    wrapped: true
```

```yaml
<animals>
  <animal>value</animal>
  <animal>value</animal>
</animals>
```

改变内部和外部名称：

```json
{
  "animals": {
    "type": "array",
    "items": {
      "type": "string",
      "xml": {
        "name": "animal"
      }
    },
    "xml": {
      "name": "aliens",
      "wrapped": true
    }
  }
}
```

```yaml
animals:
  type: array
  items:
    type: string
    xml:
      name: animal
  xml:
    name: aliens
    wrapped: true
```

```xml
<aliens>
  <animal>value</animal>
  <animal>value</animal>
</aliens>
```

只改变外部元素而不改变内部元素：

```json
{
  "animals": {
    "type": "array",
    "items": {
      "type": "string"
    },
    "xml": {
      "name": "aliens",
      "wrapped": true
    }
  }
}
```

```yaml
animals:
  type: array
  items:
    type: string
  xml:
    name: aliens
    wrapped: true
```

```xml
<aliens>
  <aliens>value</aliens>
  <aliens>value</aliens>
</aliens>
```







#### <span id='Security-Scheme-Object'>Security Scheme Object(安全架构对象)</span>

定义操作可以使用的安全方案。 支持的方案有HTTP authentication, API key(header、cookie和query参数）、 [RFC6749](https://tools.ietf.org/html/rfc6749)中定义的OAuth2的常见认证(implicit、password、 client credentials和authorization code)和[OpenID Connect Discovery](https://tools.ietf.org/html/draft-ietf-oauth-discovery-06)。

##### Fixed Fields

| 属性名           |                 属性类型                  | 作用范围            | 描述                                                         |
| ---------------- | :---------------------------------------: | ------------------- | ------------------------------------------------------------ |
| type             |                 `string`                  | Any                 | **必填**，安全方案的类型。 有效值为`"apiKey"`, `"http"`, `"oauth2"`, `"openIdConnect"`。 |
| description      |                 `string`                  | Any                 | 安全方案的简短描述. 使用[CommonMark syntax](https://spec.commonmark.org/)可以用于展示富文本。 |
| name             |                 `string`                  | `apiKey`            | **必填**，要使用的header、query或者cookie参数的名称.         |
| in               |                 `string`                  | `apiKey`            | **必填**，API key的所在位置，有效值为`"query"`, `"header"` or `"cookie"`. |
| scheme           |                 `string`                  | `http`              | **必填**，要在[Authorization header as defined in RFC7235](https://tools.ietf.org/html/rfc7235#section-5.1)中使用的HTTP Authorization scheme名称。 其使用的值应该在[IANA Authentication Scheme registry](https://www.iana.org/assignments/http-authschemes/http-authschemes.xhtml)中注册。 |
| bearerFormat     |                 `string`                  | `http` (`"bearer"`) | 提示客户端识别bearer token的格式。bearer token通常由授权服务器生成，因此此信息主要用于文档。 |
| flows            | [OAuth Flows Object](#oauth-flows-object) | `oauth2`            | **必填**，包含所支持的流类型的配置信息的对象。               |
| openIdConnectUrl |                 `string`                  | `openIdConnect`     | **必填**，OpenId Connect URL以发现OAuth2的配置值。必须为URL格式。 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。



##### 示例：

###### Basic Authentication示例

```json
{
  "type": "http",
  "scheme": "basic"
}
```

```yaml
type: http
scheme: basic
```

###### API Key示例

```json
{
  "type": "apiKey",
  "name": "api_key",
  "in": "header"
}
```

```yaml
type: apiKey
name: api_key
in: header
```

###### JWT Bearer示例

```json
{
  "type": "http",
  "scheme": "bearer",
  "bearerFormat": "JWT",
}
```

```yaml
type: http
scheme: bearer
bearerFormat: JWT
```

###### Implicit OAuth2示例

```json
{
  "type": "oauth2",
  "flows": {
    "implicit": {
      "authorizationUrl": "https://example.com/api/oauth/dialog",
      "scopes": {
        "write:pets": "modify pets in your account",
        "read:pets": "read your pets"
      }
    }
  }
}
```

```yaml
type: oauth2
flows: 
  implicit:
    authorizationUrl: https://example.com/api/oauth/dialog
    scopes:
      write:pets: modify pets in your account
      read:pets: read your pets
```



#### <span id='OAuth-Flows-Object'>OAuth Flows Object(OAuth流对象)</span>

用于配置支持的OAuth流（OAuth Flows）。

##### Fixed Fields

| 属性名            |                属性类型                 | 描述                                                         |
| ----------------- | :-------------------------------------: | ------------------------------------------------------------ |
| implicit          | [OAuth Flow Object](#oauth-flow-object) | OAuth隐式流(OAuth Implicit flow)配置                         |
| password          | [OAuth Flow Object](#oauth-flow-object) | OAuth资源所有者密码流(OAuth Resource Owner Password flow)的配置 |
| clientCredentials | [OAuth Flow Object](#oauth-flow-object) | OAuth客户端凭据流(OAuth Client Credentials flow)的配置。在OpenAPI 2.0中为`application` 。 |
| authorizationCode | [OAuth Flow Object](#oauth-flow-object) | OAuth授权码流(OAuth Authorization Code flow)配置. 在OpenAPI 2.0中为`accessCode`。 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。



#### <span id='OAuth-Flow-Object'>OAuth Flow Object(OAuth流详情对象)</span>

OAuth流的详细配置信息

##### Fixed Fields

| 属性名           |        属性类型         | 适用于                                                       | 描述                                                         |
| ---------------- | :---------------------: | ------------------------------------------------------------ | ------------------------------------------------------------ |
| authorizationUrl |        `string`         | `oauth2` (`"implicit"`, `"authorizationCode"`)               | **必填**，用于此流的授权 URL。必须为 URL 格式。              |
| tokenUrl         |        `string`         | `oauth2` (`"password"`, `"clientCredentials"`, `"authorizationCode"`) | **必填**， 用于此流Token的URL。必须为 URL 格式。             |
| refreshUrl       |        `string`         | `oauth2`                                                     | 获取刷新后Token的URL。必须为 URL 格式。                      |
| scopes           | Map[`string`, `string`] | `oauth2`                                                     | **必填**，OAuth2安全方案的可用范围，是范围名称和它的简短描述之间的映射。map可以为空。 |

这个对象可以使用[Specification Extensions](#specification-extensions)进行扩展。

##### 示例：

```JSON
{
  "type": "oauth2",
  "flows": {
    "implicit": {
      "authorizationUrl": "https://example.com/api/oauth/dialog",
      "scopes": {
        "write:pets": "modify pets in your account",
        "read:pets": "read your pets"
      }
    },
    "authorizationCode": {
      "authorizationUrl": "https://example.com/api/oauth/dialog",
      "tokenUrl": "https://example.com/api/oauth/token",
      "scopes": {
        "write:pets": "modify pets in your account",
        "read:pets": "read your pets"
      }
    }
  }
}
```

```yaml
type: oauth2
flows: 
  implicit:
    authorizationUrl: https://example.com/api/oauth/dialog
    scopes:
      write:pets: modify pets in your account
      read:pets: read your pets
  authorizationCode:
    authorizationUrl: https://example.com/api/oauth/dialog
    tokenUrl: https://example.com/api/oauth/token
    scopes:
      write:pets: modify pets in your account
      read:pets: read your pets 
```



#### <span id='Security-Requirement-Object'>Security Requirement Object(安全要求对象)</span>

执行请求所需的安全方案列表。每个属性的名称必须对应于<a href='#Components-Object'>Components Object</a>下的`securitySchemes`属性声明的安全方案。

Security Requirement Objects包含多个对象时，要求必须满足所有对象才能使请求获得授权。 这需要传递多个query参数或header参数。

当在<a href='#OpenAPI-Object'>OpenAPI Object</a>或<a href='#Operation-Object'>Operation  Object</a>上定义Security Requirement Objects列表时，只需满足列表中的一个Security Requirement Object即可授权请求。



##### Patterned Fields

| 匹配字段 |  属性类型  | 描述                                                         |
| -------- | :--------: | ------------------------------------------------------------ |
| {name}   | [`string`] | 每个名称必须对应于在<a href='#Components-Object'>Components Object</a>下`securitySchemes`中声明的安全方案。 如果安全方案的`type`字段为“oauth2”或“openIdConnect”，则该值是执行范围的名称列表，如果不需要指定范围，则该列表可以为空。 对于其他类型，数组必须为空。 |

##### 示例：

###### 非OAuth2的情况

```json
{
  "api_key": []
}
```

```yaml
api_key: []
```

###### OAuth2的情况

```json
{
  "petstore_auth": [
    "write:pets",
    "read:pets"
  ]
}
```

```yaml
petstore_auth:
- write:pets
- read:pets
```

###### OAuth2的可选情况

和 [OpenAPI Object](#openapi-object) 、 <a href='#Operation-Object'>Operation  Object</a>中定义的一样。

```json
{
  "security": [
    {},
    {
      "petstore_auth": [
        "write:pets",
        "read:pets"
      ]
    }
  ]
}
```

```yaml
security:
  - {}
  - petstore_auth:
    - write:pets
    - read:pets
```

### Specification Extensions(规范扩展)

虽然OpenAPI规范尝试适应大多数的应用场景，但可以添加额外的数据以在某些点扩展规范。

扩展属性的实现始终以`"x-"`为前缀的模式化字段。

| 匹配字段 | 类型 | 描述                                                         |
| -------- | :--: | :----------------------------------------------------------- |
| ^x-      | Any  | 可以对OpenAPI Schema进行扩展。字段名称必须以`x-`开头，例如`x-internal-id`。该值可以是 null、基本数据类型(primitive)、数组(array)或对象(object)。可以有任何有效的 JSON 格式值。 |

可用工具可能支持也可能不支持扩展，但也可以扩展这些扩展以添加请求的支持（如果工具是内部的或开源的）。

### Security Filtering(安全过滤)

OpenAPI规范中的一些对象可以被声明并保持为空，或者被完全删除，即使它们是API文档的核心。

原因是因为可以对文档进行额外的访问控制。虽然这不是规范本身的一部分，但某些库可以选择允许基于某种形式的身份验证/授权访问(authentication/authorization)文档的某些部分。

举例如下：

1. <a href='#paths-object'>Paths Object</a>可能为空。这是为了告诉查看者他们访问到了正确的位置，但无法访问任何文档。但他们仍然可以访问可能包含有关身份验证附加信息的<a href='#Info-Object'>Info Object</a>。
2. <a href='#Path-Item-Object'>Path Item Object</a>可能为空。在这种情况下，查看者将知道路径存在，但将无法看到它的任何请求或参数。这与从<a href='#paths-object'>Paths Object</a>,中隐藏路径本身不同，因为用户会知道它的存在。这允许文档提供者更精细地控制查看者可以看到的内容。