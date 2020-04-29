package com.github.xiaoymin.knife4j.annotations;


import java.lang.annotation.*;

/**
 * Deprecated since 2.0.3,see {@link ApiSupport}
 * @since 1.8.5
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiSort {

    /**
     * tag order value, Deprecated since 2.0.3,see {@link ApiSupport} order field
     * @return order
     */
   int value() default Integer.MAX_VALUE;
}
