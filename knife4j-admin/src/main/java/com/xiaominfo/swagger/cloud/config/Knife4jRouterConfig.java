/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/***
 *
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/04/24 17:47
 */
@Configuration
public class Knife4jRouterConfig {


    @Bean
    public RouteLocator myRouter(RouteLocatorBuilder routeLocatorBuilder){
        /*return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/service-abc/**")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("http://knife4j.xiaominfo.com/swagger-resources")
                )
                .build();*/
        return routeLocatorBuilder.routes().route("myservice",r->
                r.path("/service-abc/**")
                .filters(f->f.rewritePath("/service-abc/(?<remaining>.*)","/${remaining}"))
                        .uri("http://knife4j.xiaominfo.com/")).build();
    }

    private class MyFilter implements GatewayFilter{

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            return null;
        }
    }
}
