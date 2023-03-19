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

import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceDiscoverHandler;
import com.github.xiaoymin.knife4j.spring.gateway.enums.GatewayStrategy;
import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import com.github.xiaoymin.knife4j.spring.gateway.spec.v3.OpenAPI3Response;
import com.github.xiaoymin.knife4j.spring.gateway.utils.PathUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * @author <a href="milo.xiaomeng@gmail.com">milo.xiaomeng@gmail.com</a>
 *     23/02/26 20:43
 * @since gateway-spring-boot-starter v4.1.0
 */
@Slf4j
@AllArgsConstructor
@RestController
@ConditionalOnProperty(name = "knife4j.gateway.enabled", havingValue = "true")
public class OpenAPIEndpoint {
    
    final Knife4jGatewayProperties knife4jGatewayProperties;
    final ApplicationContext applicationContext;
    /**
     * OpenAPI Group Endpoint
     * @param request request
     * @return
     */
    @GetMapping("/v3/api-docs/swagger-config")
    public Mono<ResponseEntity<OpenAPI3Response>> swaggerConfig(ServerHttpRequest request) {
        OpenAPI3Response response = new OpenAPI3Response();
        // 解决nginx网关代理情况
        String contextPath = request.getPath().contextPath().value();
        if (!StringUtils.hasLength(contextPath)) {
            // 从header中获取
            List<String> referer = request.getHeaders().get("Referer");
            if (referer != null && !referer.isEmpty()) {
                String value = referer.get(0);
                log.debug("Referer:{}", value);
                contextPath = PathUtils.getContextPath(value);
            } else {
                contextPath = "/";
            }
        }
        final String basePath = contextPath;
        response.setConfigUrl("/v3/api-docs/swagger-config");
        response.setOauth2RedirectUrl(this.knife4jGatewayProperties.getDiscover().getOas3().getOauth2RedirectUrl());
        response.setValidatorUrl(this.knife4jGatewayProperties.getDiscover().getOas3().getValidatorUrl());
        log.debug("forward-path:{}", basePath);
        // 判断当前模式是手动还是服务发现
        if (knife4jGatewayProperties.getStrategy() == GatewayStrategy.MANUAL) {
            log.debug("manual strategy.");
            List<Object> sortedSet = new LinkedList<>();
            List<Knife4jGatewayProperties.Router> routers = knife4jGatewayProperties.getRoutes();
            if (routers != null && !routers.isEmpty()) {
                Collections.sort(routers, Comparator.comparing(Knife4jGatewayProperties.Router::getOrder));
                for (Knife4jGatewayProperties.Router router : routers) {
                    router.setUrl(PathUtils.append(basePath, router.getUrl()));
                    router.setContextPath(PathUtils.append(basePath, router.getContextPath()));
                    sortedSet.add(router);
                }
                response.setUrls(sortedSet);
            }
        } else {
            log.debug("discover strategy.");
            ServiceDiscoverHandler serviceDiscoverHandler = applicationContext.getBean(ServiceDiscoverHandler.class);
            if (serviceDiscoverHandler != null) {
                response.setUrls(serviceDiscoverHandler.getResources(basePath));
            }
        }
        return Mono.just(ResponseEntity.ok().body(response));
    }
}
