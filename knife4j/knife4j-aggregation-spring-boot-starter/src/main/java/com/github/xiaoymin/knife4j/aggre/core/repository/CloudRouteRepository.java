/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.repository;


import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;

import java.util.List;

/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:13
 */
public class CloudRouteRepository implements RouteRepository {

    @Override
    public boolean checkRoute(String header) {
        return false;
    }

    @Override
    public SwaggerRoute getRoute(String header) {
        return null;
    }

    @Override
    public List<SwaggerRoute> getRoutes() {
        return null;
    }
}
