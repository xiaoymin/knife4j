package com.github.xiaoymin.swaggerbootstrapui.annotations;

import com.github.xiaoymin.swaggerbootstrapui.configuration.SwaggerBootstrapUIConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/***
 * 启用SwaggerBootstrapUi的增强注解,配合<code>@EnableSwagger2</code>注解一起使用 <br />
 *
 * 主要增强功能：
 * <ul>
 *     <li>接口排序功能</li>
 *     <li>接口文档下载功能(word)</li>
 * </ul>
 *
 * @since 1.8.5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({SwaggerBootstrapUIConfiguration.class})
public @interface EnableSwaggerBootstrapUI {

}
