# 版本控制

`swagger-bootstrap-ui` 使用浏览器的localStorage对象,提供了一个细微的版本控制功能,主要体现在如下两个方面：

- 后端新增接口是识别出变化
- 后端接口信息变更是会识别出接口变更

`swagger-bootstrap-ui` 判断新接口的依据:接口地址+接口请求类型(POST|GET|PUT...)

而任何元素的变更,包括参数类型、接口说明、响应参数等等元素的变更,`swagger-bootstrap-ui` 都能识别出接口的变化,并通过icon图表(new)的方式在接口文档中展示出来

对接接口的开发者只需要刷新当前文档页就能看到后端接口是否新增或修改.

效果图:

![](/knife4j/images/1-9-3/version-control.png)

 
 
 