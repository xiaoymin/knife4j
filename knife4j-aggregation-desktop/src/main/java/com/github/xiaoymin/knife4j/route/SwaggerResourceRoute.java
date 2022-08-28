/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
* Official Web Site: http://www.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.route;

import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.core.AggregationDesktopConf;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/8 18:52
 * @since:knife4j-aggregation-desktop 2.0
 */
public class SwaggerResourceRoute extends AbstractRoute{

    Logger logger= LoggerFactory.getLogger(SwaggerResourceRoute.class);

    public SwaggerResourceRoute(AggregationDesktopConf desktopConf) {
        super(desktopConf);
    }

    @Override
    protected Object resolveResponseObject(RouteRepository routeRepository, Request request) {
        String code=request.headers(GlobalDesktopManager.ROUTE_PROXY_DOCUMENT_CODE);
        return routeRepository.getRoutes(code);
    }


}
