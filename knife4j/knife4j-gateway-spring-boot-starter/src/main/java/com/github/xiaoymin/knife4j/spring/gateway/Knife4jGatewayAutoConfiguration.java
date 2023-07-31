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
import com.github.xiaoymin.knife4j.spring.gateway.discover.spi.GatewayServiceExcludeService;
import com.github.xiaoymin.knife4j.spring.gateway.discover.spi.impl.DefaultGatewayServiceExcludeService;
import com.github.xiaoymin.knife4j.spring.gateway.filter.basic.WebFluxSecurityBasicAuthFilter;
import com.github.xiaoymin.knife4j.spring.gateway.utils.EnvironmentUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.ApplicationContext;
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
    // @ConditionalOnProperty(name = "knife4j.gateway.strategy", havingValue = "discover")
    @ConditionalOnExpression(" '${knife4j.gateway.strategy}'.equalsIgnoreCase('discover') || '${knife4j.gateway.strategy}'.equalsIgnoreCase('discover_context')")
    public static class Knife4jDiscoverConfiguration {
        
        @Bean("defaultGatewayServiceExcludeService")
        public GatewayServiceExcludeService defaultGatewayServiceExcludeService() {
            return new DefaultGatewayServiceExcludeService();
        }
        
        @Bean
        public ServiceDiscoverHandler serviceDiscoverHandler(RouteDefinitionRepository routeDefinitionRepository,
                                                             RouteLocator routeLocator,
                                                             GatewayProperties gatewayPropertiesDefault,
                                                             Knife4jGatewayProperties gatewayProperties,
                                                             ApplicationContext applicationContext) {
            return new ServiceDiscoverHandler(routeDefinitionRepository, routeLocator, gatewayPropertiesDefault, gatewayProperties, applicationContext);
            
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
