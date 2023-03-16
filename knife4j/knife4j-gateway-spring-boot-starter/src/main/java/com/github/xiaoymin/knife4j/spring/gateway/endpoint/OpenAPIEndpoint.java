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

import com.github.xiaoymin.knife4j.spring.gateway.spec.AbstractOpenAPIResource;
import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import com.github.xiaoymin.knife4j.spring.gateway.spec.Knife4jOpenAPIContainer;
import com.github.xiaoymin.knife4j.spring.gateway.spec.v3.SwaggerV3Response;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.SortedSet;

/**
 * @author <a href="milo.xiaomeng@gmail.com">milo.xiaomeng@gmail.com</a>
 *     23/02/26 20:43
 * @since gateway-spring-boot-starter v4.1.0
 */
@AllArgsConstructor
@RestController
@ConditionalOnProperty(name = "knife4j.gateway.enabled", havingValue = "true")
public class OpenAPIEndpoint {
    
    final Knife4jOpenAPIContainer<? extends AbstractOpenAPIResource> knife4JOpenAPIContainer;
    final Knife4jGatewayProperties knife4jGatewayProperties;
    
    @GetMapping("/v3/api-docs/swagger-config")
    public Mono<ResponseEntity<SwaggerV3Response>> swaggerConfig(ServerHttpRequest request) {
        SwaggerV3Response response = new SwaggerV3Response();
        String contextPath = request.getPath().contextPath().value();
        if (!StringUtils.hasLength(contextPath)) {
            contextPath = "/";
        }
        response.setConfigUrl("/v3/api-docs/swagger-config");
        response.setOauth2RedirectUrl(this.knife4jGatewayProperties.getDiscover().getOas3().getOauth2RedirectUrl());
        response.setUrls(knife4JOpenAPIContainer.getSwaggerResource());
        response.setValidatorUrl(this.knife4jGatewayProperties.getDiscover().getOas3().getValidatorUrl());
        return Mono.just(ResponseEntity.ok().body(response));
    }
    
    @GetMapping("/swagger-resources")
    @SuppressWarnings("java:S1452")
    public Mono<ResponseEntity<SortedSet<? extends AbstractOpenAPIResource>>> swaggerResource(ServerHttpRequest request) {
        // 获取分组URL的时候，需要考虑Nginx等软件转发代理的情况
        // 获取x-forward-for请求头
        String contextPath = request.getPath().contextPath().value();
        if (!StringUtils.hasLength(contextPath)) {
            contextPath = "/";
        }
        return Mono.just(ResponseEntity.ok().body(this.knife4JOpenAPIContainer.getSwaggerResource()));
    }
}
