# 3.30 Spring Security注解

:::caution 温馨提醒
增强功能需要通过配置yml配置文件开启增强,自4.0.0开始
```yml
knife4j:
  enable: true
```
:::

`Knife4j`为了满足`权限验证`将Spring Security的`PostAuthorize`、`PostFilter`、`PreAuthorize`、`PreFilter`的注解信息追加到接口描述中


代码展示
```java
@RestController
@RequestMapping("/hello")
@PostAuthorize("hasAuthority('class')")
@PostFilter("hasAuthority('class')")
@PreAuthorize("hasAuthority('class')")
@PreFilter("hasAuthority('class')")
public class HelloController {

    @GetMapping("/security")
    @PostAuthorize("hasAuthority('method')")
    @PostFilter("hasAuthority('method')")
    @PreAuthorize("hasAuthority('method')")
    @PreFilter("hasAuthority('method')")
    @ApiOperation(value = "", notes = "Spring Security注解追加到接口描述")
    public String security() {
        return "hello security";
    }
}
```

