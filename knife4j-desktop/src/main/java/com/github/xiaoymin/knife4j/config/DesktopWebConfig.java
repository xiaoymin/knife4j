/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteDispatcher;
import com.github.xiaoymin.knife4j.aggre.core.common.ExecutorEnum;
import com.github.xiaoymin.knife4j.data.compoents.DesktopDataMonitor;
import com.github.xiaoymin.knife4j.proxy.ProxyHttpClient;
import com.github.xiaoymin.knife4j.proxy.impl.ServletProxyHttpClient;
import com.github.xiaoymin.knife4j.proxy.servlet.ServletDesktopDispatcherFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

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
    
    @AllArgsConstructor
    @Configuration
    @EnableConfigurationProperties(value = Knife4jDesktopProperties.class)
    public class Knife4jDesktopAutoConfiguration {
        
        final Environment environment;
        
        @Bean
        public DesktopDataMonitor desktopDataMonitor(Knife4jDesktopProperties knife4jDesktopProperties) {
            return new DesktopDataMonitor(knife4jDesktopProperties);
        }
        
        @Bean
        public FilterRegistrationBean routeProxyFilter() {
            // 获取当前项目的contextPath
            String contextPath = Objects.toString(environment.getProperty("server.servlet.context-path"), "");
            if (StrUtil.isBlank(contextPath)) {
                contextPath = "/";
            }
            if (StrUtil.isNotBlank(contextPath) && !StrUtil.equals(contextPath, RouteDispatcher.ROUTE_BASE_PATH)) {
                // 判断是否/开头
                if (!StrUtil.startWith(contextPath, RouteDispatcher.ROUTE_BASE_PATH)) {
                    contextPath = RouteDispatcher.ROUTE_BASE_PATH + contextPath;
                }
            }
            ProxyHttpClient proxyHttpClient = new ServletProxyHttpClient(ExecutorEnum.APACHE, contextPath);
            ServletDesktopDispatcherFilter servletDesktopDispatcherFilter = new ServletDesktopDispatcherFilter(proxyHttpClient);
            
            FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
            filterRegistrationBean.setFilter(servletDesktopDispatcherFilter);
            filterRegistrationBean.setOrder(99);
            filterRegistrationBean.setEnabled(true);
            filterRegistrationBean.addUrlPatterns("/*");
            return filterRegistrationBean;
        }
        
    }
}
