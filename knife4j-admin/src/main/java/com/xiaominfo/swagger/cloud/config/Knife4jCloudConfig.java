/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.config;

import com.xiaominfo.swagger.cloud.kernel.DynamicRouteService;
import com.xiaominfo.swagger.cloud.kernel.RouteFileMonitor;
import com.xiaominfo.swagger.cloud.repository.RouteRepository;
import com.xiaominfo.swagger.cloud.repository.impl.RouteInMemoryReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

/***
 *
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/11 9:40
 */
@Configuration
public class Knife4jCloudConfig {

    @Value("${knife4j.monitor}")
    private String path;

    /***
     * 静态static文件
     * @return
     */
    @Bean
    RouterFunction<ServerResponse> staticRouter(){
        return RouterFunctions.resources("/**",new ClassPathResource("static/"));
    }

    @Bean
    public RouteRepository routeRepository(){
        return new RouteInMemoryReposity();
    }

    /**
     * 初始化Monitor
     * @param dynamicRouteService
     * @return
     */
    @Bean(initMethod = "start",destroyMethod = "stop")
    public RouteFileMonitor knife4jMonitor(@Autowired DynamicRouteService dynamicRouteService,@Autowired
                                           RouteRepository routeRepository){
        return new RouteFileMonitor(path,dynamicRouteService, routeRepository);
    }
}
