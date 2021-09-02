package com.github.xiaoymin.knife4j.spring.plugin.em.plugin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * springMVC 枚举参数转换配置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Configuration
public class EnumWebMvcConfiguration implements WebMvcConfigurer {
    /**
     * <p>enumMvcConverterFactory</p>
     *
     * @return /
     */
    @Bean
    public EnumMvcConverterFactory enumMvcConverterFactory() {
        return new EnumMvcConverterFactory();
    }

    /** {@inheritDoc} */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(enumMvcConverterFactory());
    }
}
