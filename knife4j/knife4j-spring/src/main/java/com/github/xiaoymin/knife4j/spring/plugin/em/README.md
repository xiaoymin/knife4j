# 枚举定义
由于swagger对枚举支持不太友好，使用的枚举定义的名称，这里优化了swagger对枚举类型的取值方式
after和before里的图片是未使用枚举扩展、已使用枚举扩展的效果图

> 使用限制
1. 入参定义在类里，只支持@RequestBody
2. 入参定义在控制器里，只支持@RequestParam
3.枚举类的value只支持int与String

> ENUM(枚举类)，这样定义的好处是swagger与springMVC的出入参都是value
```JAVA
    @Getter
    @AllArgsConstructor
    public enum CourseEnum {
    
        /**
         * 图文
         */
        PICTURE(102, "图文"),
        /**
         * 音频
         */
        AUDIO(103, "音频"),
        /**
         * 视频
         */
        VIDEO(104, "视频"),
        /**
         * 外链
         */
        URL(105, "外链"),
        ;
    
        @JsonValue
        private final int value;
        private final String desc;
    
        private static final Map<Integer, CourseEnum> MAPPINGS;
    
        static {
            Map<Integer, CourseEnum> temp = new HashMap<>();
            for (CourseEnum courseEnum : values()) {
                temp.put(courseEnum.value, courseEnum);
            }
            MAPPINGS = Collections.unmodifiableMap(temp);
        }
    
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public static CourseEnum resolve(Integer index) {
            return MAPPINGS.get(index);
        }
    }
```
> 字段排序枚举
``` JAVA
@Getter
@AllArgsConstructor
public enum UserSortEnum{

    /**
    * 主键
    */
    ID_DESC("id DESC", "id倒序"),
    ID_ASC("id ASC", "id正序"),
    ;

    /**
    * Constant <code>MAPPINGS</code>
    */
    private static final Map<String, UserSortEnum> MAPPINGS;

    static {
        Map<String, UserSortEnum> temp = new HashMap<String, UserSortEnum>();
        for (UserSortEnum courseEnum : values()) {
        temp.put(courseEnum.value, courseEnum);
        }
        MAPPINGS = Collections.unmodifiableMap(temp);
    }

    @JsonValue
    private final String value;
    private final String desc;

    /**
     * <p>
     * 根据index获取枚举
     * </p>
     *
     * @param index a String.
     * @return 枚举
     * @author miaoyj
     * @since 2020-10-19
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UserSortEnum resolve(String index) {
    return MAPPINGS.get(index);
    }
}
```

> 使用
```
/**
     * <p>
     * 测试enum
     * </p>
     *
     * @param courseEnum 课程类型
     * @return 登录信息
     * @author miaoyj
     * @since 2020-07-09
     */
    @GetMapping("enum")
    @ApiOperation(value = "测试enum")
    public LoginVO wd(@ApiParam(value = "排序", required = true)
                      @NotNull(message = "排序字段不正确")
                      @RequestParam UserSortEnum sort,
                      @ApiParam(value = "课程")
                      @RequestParam(required = false) CourseEnum course) {
            LoginVO loginVO = new LoginVO();
            loginVO.setCourseEnum(course);
            return loginVO;
        }
```