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

import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceDiscoverHandler;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceChangeListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 *     2022/12/03 15:41
 * @since gateway-spring-boot-starter v4.0.0
 */
@Configuration
@EnableConfigurationProperties(Knife4jGatewayProperties.class)
@ComponentScan(basePackageClasses = Knife4jGatewayAutoConfiguration.class)
@ConditionalOnProperty(name = "knife4j.gateway.enabled", havingValue = "true")
public class Knife4jGatewayAutoConfiguration {
    
    @Configuration
    @EnableConfigurationProperties(Knife4jGatewayProperties.class)
    @ConditionalOnProperty(name = "knife4j.gateway.strategy", havingValue = "discover")
    public static class Knife4jDiscoverConfiguration {
        
        @Bean
        public ServiceDiscoverHandler serviceDiscoverHandler(Knife4jGatewayProperties knife4jGatewayProperties) {
            return new ServiceDiscoverHandler(knife4jGatewayProperties);
            
        }
        /**
         * Service Listener
         * @param discoveryClient Registry Service Discovery Client
         * @param serviceDiscoverHandler Service Discover Handler
         * @return
         */
        @Bean
        public ServiceChangeListener serviceChangeListener(DiscoveryClient discoveryClient, ServiceDiscoverHandler serviceDiscoverHandler) {
            return new ServiceChangeListener(discoveryClient, serviceDiscoverHandler);
        }
    }
    
}
