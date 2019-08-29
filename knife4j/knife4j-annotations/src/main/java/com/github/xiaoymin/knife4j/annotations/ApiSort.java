package com.github.xiaoymin.knife4j.annotations;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiSort {

    int value() default Integer.MAX_VALUE;
}
