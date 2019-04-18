package com.github.xiaoymin.swaggerbootstrapui.annotations;

import com.github.xiaoymin.swaggerbootstrapui.configuration.MarkdownFileConfiguration;
import com.github.xiaoymin.swaggerbootstrapui.configuration.SecurityConfiguration;
import com.github.xiaoymin.swaggerbootstrapui.configuration.SwaggerBootstrapUIConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/***
 * Enable SwaggerBootstrapUi enhanced annotation and use @EnableSwagger2 annotation together.
 *
 * inlude:
 * <ul>
 *     <li>Interface sorting </li>
 *     <li>Interface document download  (word)</li>
 * </ul>
 *
 * @since 1.8.5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({SwaggerBootstrapUIConfiguration.class, SecurityConfiguration.class, MarkdownFileConfiguration.class})
public @interface EnableSwaggerBootstrapUI {

}
