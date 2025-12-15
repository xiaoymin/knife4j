/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.spring.configuration;

import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.extend.filter.basic.AbstractSecurityFilter;
import com.github.xiaoymin.knife4j.extend.filter.basic.JakartaServletSecurityBasicAuthFilter;
import com.github.xiaoymin.knife4j.spring.extension.Knife4jJakartaOperationCustomizer;
import com.github.xiaoymin.knife4j.spring.extension.Knife4jOpenApiCustomizer;
import com.github.xiaoymin.knife4j.spring.filter.JakartaProductionSecurityFilter;
import com.github.xiaoymin.knife4j.spring.util.EnvironmentUtils;
import jakarta.servlet.DispatcherType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/***
 * Knife4j 基础自动配置类
 * {@code @since } 2.0.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2019/08/28 21:08
 */
@Slf4j
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties({Knife4jProperties.class, Knife4jSetting.class, Knife4jHttpBasic.class})
@ConditionalOnProperty(name = "knife4j.enable", havingValue = "true")
public class Knife4jAutoConfiguration {

    private final Knife4jProperties properties;
    private final Environment environment;

    /**
     * 增强自定义配置
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public Knife4jOpenApiCustomizer knife4jOpenApiCustomizer(SpringDocConfigProperties docProperties) {
        log.debug("Register Knife4jOpenApiCustomizer");
        return new Knife4jOpenApiCustomizer(this.properties, docProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public Knife4jJakartaOperationCustomizer knife4jJakartaOperationCustomizer() {
        return new Knife4jJakartaOperationCustomizer();
    }
    /**
     * 配置Cors
     *
     * @since 2.0.4
     */
    @Bean("knife4jCorsFilter")
    @ConditionalOnMissingBean(CorsFilter.class)
    @ConditionalOnProperty(name = "knife4j.cors", havingValue = "true")
    public CorsFilter corsFilter() {
        log.info("init CorsFilter...");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(10000L);
        // 匹配所有API
        source.registerCorsConfiguration("/**", corsConfiguration);
        CorsFilter corsFilter = new CorsFilter(source);
        return corsFilter;
    }

    /**
     * Security with Basic Http
     * @param knife4jProperties Basic Properties
     * @return BasicAuthFilter
     */
    @Bean
    @ConditionalOnMissingBean(JakartaServletSecurityBasicAuthFilter.class)
    @ConditionalOnExpression("${knife4j.production:false} && ${knife4j.basic.enable:true}")
    public FilterRegistrationBean<JakartaServletSecurityBasicAuthFilter> securityBasicAuthFilter(Knife4jProperties knife4jProperties) {
        JakartaServletSecurityBasicAuthFilter authFilter = new JakartaServletSecurityBasicAuthFilter();
        if (knife4jProperties == null) {
            authFilter.setEnableBasicAuth(EnvironmentUtils.resolveBool(environment, "knife4j.basic.enable", Boolean.FALSE));
            authFilter.setUserName(EnvironmentUtils.resolveString(environment, "knife4j.basic.username", GlobalConstants.BASIC_DEFAULT_USERNAME));
            authFilter.setPassword(EnvironmentUtils.resolveString(environment, "knife4j.basic.password", GlobalConstants.BASIC_DEFAULT_PASSWORD));
        } else {
            // 判断非空
            if (knife4jProperties.getBasic() == null) {
                authFilter.setEnableBasicAuth(Boolean.FALSE);
                authFilter.setUserName(GlobalConstants.BASIC_DEFAULT_USERNAME);
                authFilter.setPassword(GlobalConstants.BASIC_DEFAULT_PASSWORD);
            } else {
                authFilter.setEnableBasicAuth(knife4jProperties.getBasic().isEnable());
                authFilter.setUserName(knife4jProperties.getBasic().getUsername());
                authFilter.setPassword(knife4jProperties.getBasic().getPassword());
                authFilter.addRule(knife4jProperties.getBasic().getInclude());
            }
        }
        FilterRegistrationBean<JakartaServletSecurityBasicAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(authFilter);
        registration.setOrder(AbstractSecurityFilter.SPRING_FILTER_ORDER);
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean(JakartaProductionSecurityFilter.class)
    @ConditionalOnProperty(name = "knife4j.production", havingValue = "true")
    public FilterRegistrationBean<JakartaProductionSecurityFilter> productionSecurityFilter(Environment environment) {
        boolean prod = false;
        JakartaProductionSecurityFilter p = null;
        if (properties == null) {
            if (environment != null) {
                String prodStr = environment.getProperty("knife4j.production");
                if (log.isDebugEnabled()) {
                    log.debug("swagger.production:{}", prodStr);
                }
                prod = Boolean.valueOf(prodStr);
            }
            p = new JakartaProductionSecurityFilter(prod);
        } else {
            p = new JakartaProductionSecurityFilter(properties.isProduction());
        }
        FilterRegistrationBean<JakartaProductionSecurityFilter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(p);
        registration.setOrder(AbstractSecurityFilter.SPRING_FILTER_ORDER - 1);
        return registration;
    }

}
