/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.xiaominfo.swagger.cloud.system;

import com.xiaominfo.swagger.cloud.kernel.RouteFileMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/***
 *
 * @since:knife4j-admin 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/11 9:58
 */
@Component
public class DynamicRouteCommander implements CommandLineRunner {

    @Autowired
    RouteFileMonitor routeFileMonitor;

    Logger logger= LoggerFactory.getLogger(DynamicRouteCommander.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("start init Routers");
        routeFileMonitor.routes();
    }
}
