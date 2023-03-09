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

import com.github.xiaoymin.knife4j.spring.gateway.enums.OpenApiVersion;
import com.github.xiaoymin.knife4j.spring.gateway.listener.ServiceChangeListener;
import com.github.xiaoymin.knife4j.spring.gateway.spec.AbstractKnife4JOpenAPIContainer;
import com.github.xiaoymin.knife4j.spring.gateway.spec.AbstractOpenAPIResource;
import com.github.xiaoymin.knife4j.spring.gateway.spec.Knife4jOpenAPIContainer;
import com.github.xiaoymin.knife4j.spring.gateway.spec.v2.Knife4JOpenAPIV2Container;
import com.github.xiaoymin.knife4j.spring.gateway.spec.v3.Knife4JOpenAPIV3Container;
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
    
    @Bean
    @SuppressWarnings("java:S1452")
    public Knife4jOpenAPIContainer<? extends AbstractOpenAPIResource> knife4jSwaggerContainer(Knife4jGatewayProperties knife4jGateway) {
        AbstractKnife4JOpenAPIContainer<? extends AbstractOpenAPIResource> knife4jSwaggerContainer;
        if (knife4jGateway.getDiscover().getVersion().equals(OpenApiVersion.V2)) {
            knife4jSwaggerContainer = new Knife4JOpenAPIV2Container(knife4jGateway.getApiPathPrefix(), knife4jGateway.getDiscover().getV2().getApiDocsPath(), knife4jGateway.getDiscover().getDefaultOrder());
        } else {
            knife4jSwaggerContainer = new Knife4JOpenAPIV3Container(knife4jGateway.getApiPathPrefix(), knife4jGateway.getDiscover().getV3().getApiDocsPath(), knife4jGateway.getDiscover().getDefaultOrder());
        }
        //knife4jSwaggerContainer.addForRoutes(knife4jGateway.getRoutes());
        knife4jSwaggerContainer.addExcludedDiscoverServices(knife4jGateway.getDiscover().getExcludedServices());
        return knife4jSwaggerContainer;
    }
    
    @Bean
    @ConditionalOnProperty(name = "knife4j.gateway.discover.enabled", matchIfMissing = true, havingValue = "true")
    public ServiceChangeListener serviceChangeListener(DiscoveryClient discoveryClient, Knife4jOpenAPIContainer<? extends AbstractOpenAPIResource> knife4JOpenAPIContainer) {
        return new ServiceChangeListener(discoveryClient, knife4JOpenAPIContainer);
    }
}
