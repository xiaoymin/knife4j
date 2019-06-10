package io.swagger.annotations;

import java.lang.annotation.*;

/**
 * Deprecated since 1.9.4,see {@link ApiOperationSupport}
 */
@Deprecated
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiOperationSort {

    int value() default Integer.MAX_VALUE;

}
