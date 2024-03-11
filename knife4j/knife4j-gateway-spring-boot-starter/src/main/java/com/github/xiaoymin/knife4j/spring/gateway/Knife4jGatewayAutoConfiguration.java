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


package com.github.xiaoymin.knife4j.spring.gateway;

import com.github.xiaoymin.knife4j.spring.gateway.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceChangeListener;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceDiscoverHandler;
import com.github.xiaoymin.knife4j.spring.gateway.discover.router.ConfigRouteServiceConvert;
import com.github.xiaoymin.knife4j.spring.gateway.discover.router.CustomerServiceConvert;
import com.github.xiaoymin.knife4j.spring.gateway.discover.router.DiscoverClientRouteServiceConvert;
import com.github.xiaoymin.knife4j.spring.gateway.discover.router.DynamicRouteServiceConvert;
import com.github.xiaoymin.knife4j.spring.gateway.discover.spi.GatewayServiceExcludeService;
import com.github.xiaoymin.knife4j.spring.gateway.discover.spi.impl.DefaultGatewayServiceExcludeService;
import com.github.xiaoymin.knife4j.spring.gateway.filter.basic.WebFluxSecurityBasicAuthFilter;
import com.github.xiaoymin.knife4j.spring.gateway.utils.EnvironmentUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/03 15:41
 * @since gateway-spring-boot-starter v4.0.0
 */
@Configuration
@EnableConfigurationProperties({Knife4jGatewayProperties.class, Knife4jGatewayHttpBasic.class})
@ComponentScan(basePackageClasses = Knife4jGatewayAutoConfiguration.class)
@ConditionalOnProperty(name = "knife4j.gateway.enabled", havingValue = "true")
public class Knife4jGatewayAutoConfiguration {
    
    private final Environment environment;
    
    public Knife4jGatewayAutoConfiguration(Environment environment) {
        this.environment = environment;
    }
    
    @Configuration
    @EnableConfigurationProperties(Knife4jGatewayProperties.class)
    @ConditionalOnProperty(name = "knife4j.gateway.strategy", havingValue = "discover")
    public static class Knife4jDiscoverConfiguration {
        
        /**
         * 基于Spring Cloud Gateway配置的路由策略
         * @param gatewayProperties 网关配置
         * @param knife4jGatewayProperties knife4j聚合配置
         * @return ConfigRouteServiceConvert
         * @since v4.3.0
         */
        @Bean
        public ConfigRouteServiceConvert configRouteServiceConvert(GatewayProperties gatewayProperties,
                                                                   Knife4jGatewayProperties knife4jGatewayProperties) {
            return new ConfigRouteServiceConvert(gatewayProperties, knife4jGatewayProperties);
        }
        
        /**
         * 动态网关路由配置
         * @param routeDefinitionRepository 动态路由
         * @param knife4jGatewayProperties knife4j聚合配置
         * @return DynamicRouteServiceConvert
         * @since v4.3.0
         */
        @Bean
        public DynamicRouteServiceConvert dynamicRouteServiceConvert(RouteDefinitionRepository routeDefinitionRepository,
                                                                     Knife4jGatewayProperties knife4jGatewayProperties) {
            return new DynamicRouteServiceConvert(routeDefinitionRepository, knife4jGatewayProperties);
        }
        
        /**
         * 基于Knife4j在discover模式下默认配置的routes规则，添加
         * @param knife4jGatewayProperties knife4j配置
         * @return CustomerServiceConvert
         * @since v4.3.0
         */
        @Bean
        public CustomerServiceConvert customerServiceConvert(Knife4jGatewayProperties knife4jGatewayProperties) {
            return new CustomerServiceConvert(knife4jGatewayProperties);
        }
        
        @Bean("defaultGatewayServiceExcludeService")
        public GatewayServiceExcludeService defaultGatewayServiceExcludeService() {
            return new DefaultGatewayServiceExcludeService();
        }
        
        @Bean
        @ConditionalOnMissingBean
        public ServiceDiscoverHandler serviceDiscoverHandler(Knife4jGatewayProperties knife4jGatewayProperties) {
            return new ServiceDiscoverHandler(knife4jGatewayProperties);
            
        }
        
        /**
         * Service Listener
         *
         * @param discoveryClient        Registry Service Discovery Client
         * @param serviceDiscoverHandler Service Discover Handler
         * @return ServiceListener
         */
        @Bean
        public ServiceChangeListener serviceChangeListener(DiscoveryClient discoveryClient, ServiceDiscoverHandler serviceDiscoverHandler, Knife4jGatewayProperties knife4jGatewayProperties) {
            return new ServiceChangeListener(discoveryClient, serviceDiscoverHandler, knife4jGatewayProperties);
        }
        
        /**
         * 在依赖DiscoverClient模式自动聚合路由的场景下，才注入{@link DiscoverClientRouteServiceConvert}处理方法
         * @since v4.3.0
         */
        @Configuration(proxyBeanMethods = false)
        @ConditionalOnProperty(value = "spring.cloud.discovery.reactive.enabled", matchIfMissing = true)
        public static class ReactiveDiscoveryClientRouteDefinitionLocatorConfiguration {
            
            @Bean
            @ConditionalOnMissingBean(DiscoverClientRouteServiceConvert.class)
            @ConditionalOnProperty(name = "spring.cloud.gateway.discovery.locator.enabled")
            public DiscoverClientRouteServiceConvert discoverClientRouteServiceConvert(
                                                                                       DiscoveryClientRouteDefinitionLocator discoveryClient, Knife4jGatewayProperties knife4jGatewayProperties) {
                return new DiscoverClientRouteServiceConvert(discoveryClient, knife4jGatewayProperties);
            }
            
        }
    }
    
    /**
     * Security with Basic Http
     *
     * @param knife4jGatewayProperties Basic Properties
     * @return BasicAuthFilter
     */
    @Bean
    @ConditionalOnMissingBean(WebFluxSecurityBasicAuthFilter.class)
    @ConditionalOnProperty(name = "knife4j.gateway.basic.enable", havingValue = "true")
    public WebFluxSecurityBasicAuthFilter securityBasicAuthFilter(Knife4jGatewayProperties knife4jGatewayProperties) {
        WebFluxSecurityBasicAuthFilter authFilter = new WebFluxSecurityBasicAuthFilter();
        if (knife4jGatewayProperties == null) {
            authFilter.setEnableBasicAuth(EnvironmentUtils.resolveBool(environment, "knife4j.gateway.basic.enable", Boolean.FALSE));
            authFilter.setUserName(EnvironmentUtils.resolveString(environment, "knife4j.gateway.basic.username", GlobalConstants.BASIC_DEFAULT_USERNAME));
            authFilter.setPassword(EnvironmentUtils.resolveString(environment, "knife4j.gateway.basic.password", GlobalConstants.BASIC_DEFAULT_PASSWORD));
        } else {
            // 判断非空
            if (knife4jGatewayProperties.getBasic() == null) {
                authFilter.setEnableBasicAuth(Boolean.FALSE);
                authFilter.setUserName(GlobalConstants.BASIC_DEFAULT_USERNAME);
                authFilter.setPassword(GlobalConstants.BASIC_DEFAULT_PASSWORD);
            } else {
                authFilter.setEnableBasicAuth(knife4jGatewayProperties.getBasic().isEnable());
                authFilter.setUserName(knife4jGatewayProperties.getBasic().getUsername());
                authFilter.setPassword(knife4jGatewayProperties.getBasic().getPassword());
                authFilter.addRule(knife4jGatewayProperties.getBasic().getInclude());
            }
        }
        return authFilter;
    }
}
