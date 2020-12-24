/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.ext.PoolingConnectionManager;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;

import java.util.*;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:36
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public abstract class AbsctractRepository extends PoolingConnectionManager implements RouteRepository {

    /**
     * 多项目版本
     */
    protected final Map<String,Map<String,SwaggerRoute>> multipartRouteMap=new HashMap<>();

    @Override
    public boolean checkRoute(String code,String header) {
        if (StrUtil.isNotBlank(code)&&StrUtil.isNotBlank(header)){
            Map<String,SwaggerRoute> routeMap=this.multipartRouteMap.get(code);
            if (CollectionUtil.isNotEmpty(routeMap)){
                return routeMap.containsKey(header);
            }
        }
        return false;
    }
    @Override
    public SwaggerRoute getRoute(String code,String header) {
        if (StrUtil.isNotBlank(code)&&StrUtil.isNotBlank(header)){
            Map<String,SwaggerRoute> routeMap=this.multipartRouteMap.get(code);
            if (CollectionUtil.isNotEmpty(routeMap)){
                return routeMap.get(header);
            }
        }
        return null;
    }

    @Override
    public List<SwaggerRoute> getRoutes(String code) {
        if (StrUtil.isNotBlank(code)){
            Map<String,SwaggerRoute> routeMap=this.multipartRouteMap.get(code);
            if (CollectionUtil.isNotEmpty(routeMap)){
                List<SwaggerRoute> swaggerRoutes=CollectionUtil.newArrayList(routeMap.values());
                Collections.sort(swaggerRoutes,Comparator.comparingInt(SwaggerRoute::getOrder));
                return swaggerRoutes;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void add(String code, Map<String, SwaggerRoute> routeMap) {
        this.multipartRouteMap.put(code,routeMap);
    }
}
