/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.repository;


import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;

/***
 *
 * @since:route-proxy 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/29 20:13
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
}
