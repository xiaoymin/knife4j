package springfox.documentation.swagger2.annotations;


import java.lang.annotation.*;

/**
 * @Copyright: Zhejiang Drore Technology Co., Ltd  2018 <br/>
 * @Desc: <br/>
 * @ProjectName: swagger-bootstrap-ui <br/>
 * @Date: 2018/9/23 14:22 <br/>
 * @Author: baoec@drore.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiSort {

    int value() default Integer.MAX_VALUE;
}
