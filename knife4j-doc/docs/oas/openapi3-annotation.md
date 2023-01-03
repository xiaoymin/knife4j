# OpenAPI3注解

| Swagger3                                                     | 注解说明                                              |
| :----------------------------------------------------------- | ----------------------------------------------------- |
| @Tag(name = “接口类描述”)                                    | Controller 类                                         |
| @Operation(summary =“接口方法描述”)                          | Controller 方法                                       |
| @Parameters                                                  | Controller 方法                                       |
| @Parameter(description=“参数描述”)                           | Controller 方法上 @Parameters 里Controller 方法的参数 |
| @Parameter(hidden = true)  、@Operation(hidden = true)@Hidden | 排除或隐藏api                                         |
| @Schema                                                      | DTO实体DTO实体属性                                    |

