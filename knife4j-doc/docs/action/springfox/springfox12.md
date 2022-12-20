# springfox 源码分析(十二) 遍历接口获取ApiDescription集合



ApiDescription是springfox提供的接口描述信息类,在[springfox 源码分析(十) 遍历接口获取Model对象](/docs/action/springfox/springfox10)中我们拿到了接口的类型Model集合信息，但除了Model信息,接口还有更多的信息

## 基础信息 

主要包括：接口路径、consumes、produces、参数、请求类型、描述、说明、响应状态码、是否过时、扩展信息、分组

因为我们的接口可以运行多个请求类型的存在,所以以上信息在springfox是通过`Operation`来声明的

先来看`ApiDescription`的源码

```java
public class ApiDescription {
  //分组名称
  private final String groupName;
  //路径
  private final String path;
  //描述
  private final String description;
  //操作信息集合
  //一个接口有可能存在多个请求方法类型,即：GET、POST、PUT、DELETE等,所以这里也是1：N的映射关系
  private final List<Operation> operations;
  //是否隐藏
  private final Boolean hidden;
    //getter and setters....
}
```

在代码注释中,我也做了说明

因为一个接口有可能存在多个请求方法类型,即：GET、POST、PUT、DELETE等,所以这里也是1：N的映射关系，即存在多个Operation集合

Operation的属性

```java
public class Operation {
  //请求接口
  private final HttpMethod method;
  //接口名称
  private final String summary;
  //接口描述信息
  private final String notes;
  private final ModelReference responseModel;
  //唯一id
  private final String uniqueId;
  private final int position;
  //tags
  private final Set<String> tags;
  private final Set<String> produces;
  private final Set<String> consumes;
  private final Set<String> protocol;
  //是否隐藏
  private final boolean isHidden;
  private final Map<String, List<AuthorizationScope>> securityReferences;
  //参数
  private final List<Parameter> parameters;
  //状态码
  private final Set<ResponseMessage> responseMessages;
  //是否过时
  private final String deprecated;
  //扩展信息
  private final List<VendorExtension> vendorExtensions;

  //setter and getter..   
}
```

在Operation中声明的属性中就是我们上面介绍的接口相关信息.

## 初始化

我们知道了接口的介绍信息,此时,来看springfox如何处理，将接口的上下文信息最终初始化转换为ApiDescription

`ApiDescriptionReader.read`方法

```java
/***
 * 获取ApiDescription接口集合信息
 * @param outerContext
 * @return
 */
public List<ApiDescription> read(RequestMappingContext outerContext) {
  PatternsRequestCondition patternsCondition = outerContext.getPatternsCondition();
  ApiSelector selector = outerContext.getDocumentationContext().getApiSelector();

  List<ApiDescription> apiDescriptionList = newArrayList();
  for (String path : matchingPaths(selector, patternsCondition)) {
    String methodName = outerContext.getName();
    try {
      RequestMappingContext operationContext = outerContext.copyPatternUsing(path);
	//根据接口上下文获取Operation集合
      List<Operation> operations = operationReader.read(operationContext);
      if (operations.size() > 0) {
        operationContext.apiDescriptionBuilder()
            .groupName(outerContext.getGroupName())
            .operations(operations)
            .pathDecorator(pluginsManager.decorator(new PathContext(outerContext, from(operations).first())))
            .path(path)
            .description(methodName)
            .hidden(false);
        ApiDescription apiDescription = operationContext.apiDescriptionBuilder().build();
        lookup.add(outerContext.key(), apiDescription);
        apiDescriptionList.add(apiDescription);
      }
    } catch (Error e) {
      String contentMsg = "Skipping process path[" + path + "], method[" + methodName + "] as it has an error.";
      log.error(contentMsg, e);
    }
  }
  return apiDescriptionList;
}
```

核心操作是通过operationReader.read操作,获取Operation集合

```java
public List<Operation> read(RequestMappingContext outerContext) {

    List<Operation> operations = newArrayList();
		
    Set<RequestMethod> requestMethods = outerContext.getMethodsCondition();
    Set<RequestMethod> supportedMethods = supportedMethods(requestMethods);

    //Setup response message list
    Integer currentCount = 0;
    //遍历获取当前支持的接口类型
    for (RequestMethod httpRequestMethod : supportedMethods) {
      OperationContext operationContext = new OperationContext(new OperationBuilder(nameGenerator),
          httpRequestMethod,
          outerContext,
          currentCount);
		//调用OperationPlugin插件，构造Operation对象
      Operation operation = pluginsManager.operation(operationContext);
        //添加
      if (!operation.isHidden()) {
        operations.add(operation);
        currentCount++;
      }
    }
    Collections.sort(operations, outerContext.operationOrdering());

    return operations;
  }
```

主要的逻辑如下：

- 获取当前支持的接口类型,包括GET|POST、PUT、DELETE等
- 通过调用OperationPlugin的插件对Operation中的每个属性进行赋值操作,包括参数类型、描述、响应状态码等等信息

OperationPlugin插件包含了多个实现类型，这个可以参考前面介绍的[springfox 源码分析(五) web配置类Plugin插件的使用](/docs/action/springfox/springfox5)

既然Operation提供了扩展参数,那么我们后面是可以进行添加自定义扩展的

下一篇会介绍如何添加springfox的接口扩展字段.