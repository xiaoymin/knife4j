package io.swagger.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiOperationSort {

    int value() default Integer.MAX_VALUE;

}
