/*
 * Copyright 2017-2023 八一菜刀(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.config;

import com.github.xiaoymin.knife4j.gateway.GatewayClientDispatcher;
import com.github.xiaoymin.knife4j.gateway.executor.ExecutorType;
import com.github.xiaoymin.knife4j.datasource.ConfigDataProviderHolder;
import com.github.xiaoymin.knife4j.datasource.DocumentSessionHolder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @since:knife4j-desktop
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/11 14:52
 */
@Configuration
public class DesktopWebConfig implements WebMvcConfigurer {
    
    public static final String[] RESOURCES = {
            "classpath:/META-INF/resources/",
            "classpath:/resources/",
            "classpath:/static/",
            "classpath:/public/"
    };
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // registry.addResourceHandler("/webjars/**").addResourceLocations("classpath*:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**")
                .addResourceLocations(RESOURCES);
    }
    
    @Bean
    public DocumentSessionHolder documentSessionHolder() {
        return new DocumentSessionHolder();
    }
    
    @Bean
    public ConfigDataProviderHolder configDataServiceLoader(DocumentSessionHolder documentSessionHolder) {
        return new ConfigDataProviderHolder(documentSessionHolder);
    }
    
    @Bean
    public FilterRegistrationBean routeProxyFilter(DocumentSessionHolder documentSessionHolder) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new GatewayClientDispatcher(documentSessionHolder, ExecutorType.APACHE));
        filterRegistrationBean.setOrder(99);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
