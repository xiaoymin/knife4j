/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.annotations;

import java.lang.annotation.*;

/**
 * Note: 自Knife4j 4.0版本起，放弃维护此属性值，建议开发者建实体类
 */
@Deprecated
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicParameters {

    /***
     * dynamic Model name
     * @return 类名
     */
    String name() default "";

    /***
     * list of properties
     * @return 类属性集合
     */
    DynamicParameter[] properties() default @DynamicParameter;
}
