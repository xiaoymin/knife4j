package springfox.documentation.swagger2.annotations;

import java.lang.annotation.*;

/**
 * @Copyright: Zhejiang Drore Technology Co., Ltd  2018 <br/>
 * @Desc: <br/>
 * @ProjectName: swagger-ui-plugin <br/>
 * @Date: 2018/9/25 7:35 <br/>
 * @Author: baoec@drore.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiOperationSort {

    int value() default Integer.MAX_VALUE;

    String modfy_time() default "";
}
