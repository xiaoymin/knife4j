/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.aggre.core;


import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;

import java.util.List;

/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:09
 */
public interface RouteRepository {

    /**
     * 校验请求Header是否正确
     * @param header
     * @return
     */
    boolean checkRoute(String header);

    /**
     * 根据请求header获取
     * @param header
     * @return
     */
    SwaggerRoute getRoute(String header);

    /**
     * 获取所有
     * @return
     */
    List<SwaggerRoute> getRoutes();
}
