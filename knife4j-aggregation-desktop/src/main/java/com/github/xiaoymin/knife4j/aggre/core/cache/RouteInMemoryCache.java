/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.aggre.core.cache;

import com.github.xiaoymin.knife4j.aggre.core.RouteCache;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;

import java.util.concurrent.ConcurrentHashMap;
/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/31 11:07
 */
public class RouteInMemoryCache implements RouteCache<String, SwaggerRoute> {

    private final ConcurrentHashMap<String,SwaggerRoute> cache=new ConcurrentHashMap<>();

    @Override
    public boolean put(String s, SwaggerRoute swaggerRoute) {
        cache.put(s,swaggerRoute);
        return true;
    }

    @Override
    public SwaggerRoute get(String s) {
        return cache.get(s);
    }

    @Override
    public boolean remove(String s) {
        cache.remove(s);
        return true;
    }
}
