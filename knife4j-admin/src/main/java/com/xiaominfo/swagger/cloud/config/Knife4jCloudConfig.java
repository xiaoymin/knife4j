/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.config;

import com.xiaominfo.swagger.cloud.kernel.Knife4jDynamicRouteService;
import com.xiaominfo.swagger.cloud.kernel.Knife4jMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    /**
     * 初始化Monitor
     * @param knife4jDynamicRouteService
     * @return
     */
    @Bean(initMethod = "start",destroyMethod = "stop")
    public Knife4jMonitor knife4jMonitor(@Autowired Knife4jDynamicRouteService knife4jDynamicRouteService){
        return new Knife4jMonitor(path,knife4jDynamicRouteService);
    }
}
