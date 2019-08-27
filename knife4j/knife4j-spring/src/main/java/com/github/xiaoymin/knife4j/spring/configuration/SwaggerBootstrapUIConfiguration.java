package com.github.xiaoymin.knife4j.spring.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        basePackages = {
                "com.github.xiaoymin.knife4j.spring.plugin",
                "com.github.xiaoymin.knife4j.spring.web"
        }
)
public class SwaggerBootstrapUIConfiguration {






}
