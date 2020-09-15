package com.github.xiaoymin.knife4j.annotations;

import java.lang.annotation.*;

/**
 * Deprecated since 1.9.4,see {@link ApiOperationSupport}
 */
@Deprecated
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiOperationSort {

    /**
     * 接口排序值
     * @return 排序值
     */
    int value() default Integer.MAX_VALUE;

}
