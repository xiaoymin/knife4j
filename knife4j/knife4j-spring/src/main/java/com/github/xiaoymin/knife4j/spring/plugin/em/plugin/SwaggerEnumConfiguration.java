package com.github.xiaoymin.knife4j.spring.plugin.em.plugin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * swagger定义入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Configuration
public class SwaggerEnumConfiguration {

    /**
     * <p>enumModelPropertyBuilderPlugin</p>
     *
     * @return /
     */
    @Bean
    public EnumModelPropertyBuilderPlugin enumModelPropertyBuilderPlugin() {
        return new EnumModelPropertyBuilderPlugin();
    }

    /**
     * <p>enumParameterBuilderPlugin</p>
     *
     * @return /
     */
    @Bean
    public EnumParameterBuilderPlugin enumParameterBuilderPlugin() {
        return new EnumParameterBuilderPlugin();
    }
}
