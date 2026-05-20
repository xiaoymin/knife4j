# 3.19 自定义脚本

`customJavaScriptUrls` 用于在 `doc.html` 页面加载开发者自己托管的 JavaScript 脚本。脚本加载后可以通过 `window.Knife4j.registerRequestInterceptor` 注册请求前拦截器，在调试请求发出前修改请求配置。

## 开启配置

```yaml
knife4j:
  setting:
    customJavaScriptUrls:
      - /knife4j/custom/request-id.js
```

`customJavaScriptUrls` 支持配置多个脚本地址，Knife4j 会按配置顺序加载。空地址会被忽略，相同地址只加载一次。脚本加载失败时只在浏览器控制台输出警告，不会阻断 `doc.html` 页面和调试功能。

## 注入调试请求头

例如在应用中提供 `/knife4j/custom/request-id.js`：

```js
window.Knife4j.registerRequestInterceptor(function(config, context) {
  config.headers = config.headers || {};
  config.headers["x-request-id"] = Date.now().toString();
  return config;
});
```

注册后，开发者在 `doc.html` 页面发起 Raw、Form、x-www-form-urlencoded 三种调试请求时，请求头都会自动带上 `x-request-id`，页面展示的 curl 内容也会包含最终注入的请求头。

## 拦截器参数

`registerRequestInterceptor` 接收一个同步函数：

```js
window.Knife4j.registerRequestInterceptor(function(config, context) {
  return config;
});
```

- `config`：本次调试请求的 axios request config。
- `context.api`：当前接口信息。
- `context.swaggerInstance`：当前文档分组实例。
- `context.requestType`：当前请求类型，取值为 `raw`、`form`、`urlForm`。
- `context.settings`：当前 Knife4j 个性化配置。
- `context.headers`：本次请求初始请求头。

如果某个拦截器抛出异常，Knife4j 会捕获异常并继续执行调试请求，避免自定义脚本影响主流程。

## 与 AfterScript 的区别

`AfterScript` 是单个接口调试响应成功后执行的脚本，适合处理响应结果。

自定义脚本是页面级扩展能力，脚本会在 `doc.html` 初始化配置后加载，适合注册请求前拦截器，例如统一追加请求头、按分组补充调试参数等。

## 安全说明

自定义脚本会在 `doc.html` 页面内执行，权限等同页面脚本。请只配置可信来源，并由应用自行托管和审查脚本内容。
