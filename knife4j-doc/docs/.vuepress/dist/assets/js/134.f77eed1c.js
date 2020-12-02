(window.webpackJsonp=window.webpackJsonp||[]).push([[134],{583:function(t,a,e){"use strict";e.r(a);var n=e(42),s=Object(n.a)({},(function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("ContentSlotsDistributor",{attrs:{"slot-key":t.$parent.slotKey}},[e("h1",{attrs:{id:"enhance"}},[e("a",{staticClass:"header-anchor",attrs:{href:"#enhance"}},[t._v("#")]),t._v(" Enhance")]),t._v(" "),e("p",[t._v("SwaggerBootstrapUi has added back-end Java code support since version 1.8.5. The main purpose is to assist Java developers to extend some enhancements while using Springfox-Swagger to help developers have a better document experience.")]),t._v(" "),e("p",[t._v("Currently major enhancements:")]),t._v(" "),e("ul",[e("li",[t._v("tags grouping tag sorting")]),t._v(" "),e("li",[t._v("api interface sorting")])]),t._v(" "),e("p",[t._v("Use the enhancements provided by swagger-bootstrap-ui, you need to open it in the source Spring config configuration file, add the @EnableSwaggerBootstrapUi annotation on the original EnableSwagger2 annotation, the sample code is as follows：")]),t._v(" "),e("div",{staticClass:"language-java extra-class"},[e("pre",{pre:!0,attrs:{class:"language-java"}},[e("code",[e("span",{pre:!0,attrs:{class:"token annotation punctuation"}},[t._v("@Configuration")]),t._v("\n"),e("span",{pre:!0,attrs:{class:"token annotation punctuation"}},[t._v("@EnableSwagger2")]),t._v("\n"),e("span",{pre:!0,attrs:{class:"token annotation punctuation"}},[t._v("@EnableSwaggerBootstrapUI")]),t._v("\n"),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("public")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("class")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("SwaggerConfiguration")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n \t"),e("span",{pre:!0,attrs:{class:"token comment"}},[t._v("//more...   ")]),t._v("\n"),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n")])])]),e("p",[t._v("For the sorting of tags, the sorting rule of the UI is sequential sorting, the minimum value is 1, and the maximum value is also the default value Integer.Max_VALUE;")]),t._v(" "),e("p",[t._v("If you don't use the enhancements of SwaggerBootstrapUi, you don't need to open the @EnableSwaggerBootstrapUi annotation.")]),t._v(" "),e("p",[t._v("There are two sorting rules for tags:")]),t._v(" "),e("p",[t._v("a, one is to determine whether the position attribute of Swagger's @Api annotation is not equal to 0 (the default value is 0), if the value is not empty, then get this value, sort according to the value")]),t._v(" "),e("p",[t._v("b. If postion=0 (when not writing), determine whether there is a value of the annotation @ApiSort. If there is a value, obtain the value and sort according to the value.")]),t._v(" "),e("p",[t._v("c, so the ordering rule is: position>@ApiSort")]),t._v(" "),e("p",[t._v("The collation of the interface api:")]),t._v(" "),e("p",[t._v("a. Determine whether the postion attribute on the @ApiOperation annotation is not equal to 0 (the default value is 0). If the value is not null, the value is obtained and sorted according to the value.")]),t._v(" "),e("div",{staticClass:"language-java extra-class"},[e("pre",{pre:!0,attrs:{class:"language-java"}},[e("code",[e("span",{pre:!0,attrs:{class:"token comment"}},[t._v("//postion属性赋值")]),t._v("\n"),e("span",{pre:!0,attrs:{class:"token annotation punctuation"}},[t._v("@ApiOperation")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),t._v("httpMethod "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token string"}},[t._v('"POST"')]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("position "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token number"}},[t._v("2")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("value "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token string"}},[t._v('"Test2Model测试数组参数，多个"')]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("response"),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),e("span",{pre:!0,attrs:{class:"token class-name"}},[t._v("Test2Model")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(".")]),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("class")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n"),e("span",{pre:!0,attrs:{class:"token annotation punctuation"}},[t._v("@ApiResponses")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n    "),e("span",{pre:!0,attrs:{class:"token annotation punctuation"}},[t._v("@ApiResponse")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),t._v("code "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token number"}},[t._v("200")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v(" message "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token string"}},[t._v('"非HTTP状态码，返回值JSON code字段值，描述：成功"')]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n"),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n"),e("span",{pre:!0,attrs:{class:"token annotation punctuation"}},[t._v("@ApiImplicitParams")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n    "),e("span",{pre:!0,attrs:{class:"token annotation punctuation"}},[t._v("@ApiImplicitParam")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),t._v("name "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token string"}},[t._v('"ids"')]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("paramType "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),e("span",{pre:!0,attrs:{class:"token string"}},[t._v('"form"')]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("value "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token string"}},[t._v('"参数"')]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("allowMultiple "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token boolean"}},[t._v("true")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v(" required "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token boolean"}},[t._v("true")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n"),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n")])])]),e("p",[t._v("b. If postion=0 (when not writing), determine whether there is a value of the annotation @ApiOperationSort. If there is a value, obtain the value and sort according to the value.")]),t._v(" "),e("p",[t._v("c, so the ordering rules are: position>@ApiOperationSort")]),t._v(" "),e("p",[t._v("note:")]),t._v(" "),e("p",[t._v("The annotations @EnableSwaggerBootstrapUi, @ApiSort, @ApiOperationSort are the Java annotations provided by this UI toolkit. The use of the sorting function requires the @EnableSwaggerBootstrapUi annotation to be added to enable the original EnableSwagger2 annotation.")]),t._v(" "),e("p",[t._v("After all the above background settings are completed, you need to check the Enable Enhancement function in the UI personalization settings, otherwise the enhanced function will not take effect.")]),t._v(" "),e("p",[t._v("Function Directory: "),e("strong",[t._v("Document Management -> Personalization")])]),t._v(" "),e("p",[e("img",{attrs:{src:"/knife4j/images/ehn-fun.png",alt:""}})]),t._v(" "),e("icp"),t._v(" \n comment/> \n "),e("comment-comment")],1)}),[],!1,null,null,null);a.default=s.exports}}]);