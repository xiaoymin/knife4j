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


package com.github.xiaoymin.knife4j.spring.gateway.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @since gateway-spring-boot-starter v4.0.0
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/03 15:41
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(Knife4jGatewayProperties.class)
@ConditionalOnProperty(name = "knife4j.gateway.enable", havingValue = "true")
public class Knife4jGatewayAutoConfiguration {
    
    /**
     * 分组url
     */
    public static final String GATEWAY_SWAGGER_GROUP_URL = "/swagger-resources";
    
    @Bean
    public RouterFunction<ServerResponse> gatewaySwaggerRoute(Knife4jGatewayProperties knife4jGatewayProperties) {
        log.info("init gateway swagger resources.");
        return RouterFunctions.route().GET(GATEWAY_SWAGGER_GROUP_URL, request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(knife4jGatewayProperties.build())).build();
    }
}
