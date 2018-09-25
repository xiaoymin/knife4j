package springfox.documentation.swagger2.annotations;

import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.configuration.Swagger2UIConfiguration;

import java.lang.annotation.*;

/**
 * @Copyright: Zhejiang Drore Technology Co., Ltd  2018 <br/>
 * @Desc: <br/>
 * @ProjectName: swagger-bootstrap-ui <br/>
 * @Date: 2018/9/23 20:45 <br/>
 * @Author: baoec@drore.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({Swagger2UIConfiguration.class})
public @interface EnableSwaggerBootstrapUI {
}
