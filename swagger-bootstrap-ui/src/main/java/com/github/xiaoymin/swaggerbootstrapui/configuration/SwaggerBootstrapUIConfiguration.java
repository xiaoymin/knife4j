package com.github.xiaoymin.swaggerbootstrapui.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        basePackages = {
                "com.github.xiaoymin.swaggerbootstrapui.plugin",
                "com.github.xiaoymin.swaggerbootstrapui.web",
                "com.github.xiaoymin.swaggerbootstrapui.service"
        }
)
public class SwaggerBootstrapUIConfiguration {






}
