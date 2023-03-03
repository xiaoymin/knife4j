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


package com.github.xiaoymin.knife4j.spring.gateway.endpoint;

import com.github.xiaoymin.knife4j.spring.gateway.AbstractSwaggerResource;
import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import com.github.xiaoymin.knife4j.spring.gateway.Knife4jSwaggerContainer;
import com.github.xiaoymin.knife4j.spring.gateway.v3.SwaggerV3Response;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.SortedSet;

/**
 * @author <a href="milo.xiaomeng@gmail.com">milo.xiaomeng@gmail.com</a>
 *     23/02/26 20:43
 * @since gateway-spring-boot-starter v4.0.0
 */
@RestController
@ConditionalOnProperty(name = "knife4j.gateway.enabled", havingValue = "true")
public class SwaggerEndpoint {
    
    private final Knife4jSwaggerContainer<? extends AbstractSwaggerResource> knife4jSwaggerContainer;
    private final Knife4jGatewayProperties knife4jGatewayProperties;
    
    public SwaggerEndpoint(Knife4jSwaggerContainer<? extends AbstractSwaggerResource> knife4jSwaggerContainer, Knife4jGatewayProperties knife4jGatewayProperties) {
        this.knife4jSwaggerContainer = knife4jSwaggerContainer;
        this.knife4jGatewayProperties = knife4jGatewayProperties;
    }
    
    @GetMapping("/v3/api-docs/swagger-config")
    public Mono<ResponseEntity<SwaggerV3Response>> swaggerConfig() {
        SwaggerV3Response response = new SwaggerV3Response();
        response.setConfigUrl("/v3/api-docs/swagger-config");
        response.setOauth2RedirectUrl(this.knife4jGatewayProperties.getV3().getOauth2RedirectUrl());
        response.setUrls(knife4jSwaggerContainer.getSwaggerResource());
        response.setValidatorUrl(this.knife4jGatewayProperties.getV3().getValidatorUrl());
        return Mono.just(ResponseEntity.ok().body(response));
    }
    
    @GetMapping("/swagger-resources")
    @SuppressWarnings("java:S1452")
    public Mono<ResponseEntity<SortedSet<? extends AbstractSwaggerResource>>> swaggerResource() {
        return Mono.just(ResponseEntity.ok().body(this.knife4jSwaggerContainer.getSwaggerResource()));
    }
}
