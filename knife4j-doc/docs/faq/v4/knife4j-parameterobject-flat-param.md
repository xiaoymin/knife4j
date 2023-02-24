# Knife4j v4.0版本针对参数解析ParameterObject的问题说明

:::danger 备注说明

该文档仅限Knife4j v4.0.0版本作为一种解决方案，在未来Knife4j v4.1.0版本中，会将springdoc-openapi的基础版本升级到最新版本，这样开发者就不用在maven中使用`exclusions`进行排除低版本再重新引入了

- 参考[issue#I6HDXO](https://gitee.com/xiaoym/knife4j/issues/I6HDXO#note_16433457)

:::  


先来看示例代码：

```javascript
@Operation(summary = "Get 请求", tags = "对接口分组", description = "对接口的作用进行描述")
@RequestMapping(value = "/api/v1/open-api", method = RequestMethod.GET)
public R<GetDTO> get(GetDTO dto) {
    return R.ok(dto);
}

```

实体类`GetDTO.java`

```javascript
@Data
@Schema(description = "请求 Param 对象")
class GetDTO {

    @Schema(description = "param 必填参数", required = true, example = "param 必填参数")
    private String paramRequired;

    @Schema(description = "param 非必填参数", example = "param 非必填参数")
    private String paramNoRequired;
}

```

这种情况在之前springfox的框架下，会直接解析成form参数，开发者无需过多的配置，但是在升级到Knife4j v4.0版本后，文档的界面效果却和开发者展示的不一样，如下图：

![](https://foruda.gitee.com/images/1677155549782859913/581b6993_11519191.png)


很多朋友会误以为这是Knife4j的前端Ui解析Bug，但其实不是，针对这种情况，目前有两种解决方案：


1、所有的该类型的接口中，增加注解`@ParameterObject`,代码最终如下：

```javascript
@Operation(summary = "Get 请求", tags = "对接口分组", description = "对接口的作用进行描述")
@RequestMapping(value = "/api/v1/open-api", method = RequestMethod.GET)
public R<GetDTO> get(@ParameterObject GetDTO dto) {
    return R.ok(dto);
}
```

> 但是这种情况很多开发者可能会觉得很麻烦，因为如果涉及到接口太多的话，那么确实每个接口都需要加注解的话，工作量会非常的大

:::tip

但是这种情况很多开发者可能会觉得很麻烦，因为如果涉及到接口太多的话，那么确实每个接口都需要加注解的话，工作量会非常的大

::: 

2、第二种情况则是在目前Knife4j的v4.0.0版本中，可以考虑升级springdoc-openapi的版本来解决，通过新特性，一行配置搞定

在springdoc-openapi的1.6.11版本中，增加了`defaultFlatParamObject`的配置项，通过配置该属性，可以不用添加注解，达到上面1中的效果
- [PR#1805](https://github.com/springdoc/springdoc-openapi/pull/1805/files#diff-64a102017f6629fba4d58313db9b30ed97a608a2de83e08cc584e942b28bab81)
- [版本日志1.6.11](https://github.com/springdoc/springdoc-openapi/releases/tag/v1.6.11)


在项目yml配置中，配置如下：

```yml
springdoc:
  # 默认是false，需要设置为true
  default-flat-param-object: true
```

在Knife4j 4.0.0版本中，排除低版本做升级即可，Maven配置如下：

```xml
<dependency>
			<groupId>com.github.xiaoymin</groupId>
			<artifactId>knife4j-openapi3-spring-boot-starter</artifactId>
			<version>4.0.0</version>
			<!--排查低版本-->
			<exclusions>
				<exclusion>
					<groupId>org.springdoc</groupId>
					<artifactId>springdoc-openapi-common</artifactId>
				</exclusion>
				<exclusion>
					<groupId>org.springdoc</groupId>
					<artifactId>springdoc-openapi-webflux-core</artifactId>
				</exclusion>
				<exclusion>
					<groupId>org.springdoc</groupId>
					<artifactId>springdoc-openapi-webmvc-core</artifactId>
				</exclusion>
				<exclusion>
					<groupId>io.swagger.core.v3</groupId>
					<artifactId>swagger-models</artifactId>
				</exclusion>
				<exclusion>
					<groupId>io.swagger.core.v3</groupId>
					<artifactId>swagger-annotations</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>io.swagger.core.v3</groupId>
			<artifactId>swagger-models</artifactId>
			<version>2.2.7</version>
		</dependency>
		<dependency>
			<groupId>io.swagger.core.v3</groupId>
			<artifactId>swagger-annotations</artifactId>
			<version>2.2.7</version>
		</dependency>


		<!--springdoc-openapi的Jar包引用-->
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-common</artifactId>
			<version>1.6.14</version>
		</dependency>
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-webflux-core</artifactId>
			<version>1.6.14</version>
		</dependency>
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-webmvc-core</artifactId>
			<version>1.6.14</version>
		</dependency>
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-ui</artifactId>
			<version>1.6.14</version>
		</dependency>


```