/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.ext.PoolingConnectionManager;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:36
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public abstract class AbsctractRepository extends PoolingConnectionManager implements RouteRepository {

    /**
     * 心跳检测间隔(30s)
     */
    protected static final Long HEART_BEAT_DURATION=30000L;

    protected final Map<String, SwaggerRoute> routeMap=new HashMap<>();

    @Override
    public boolean checkRoute(String header) {
        if (StrUtil.isNotBlank(header)){
            return routeMap.containsKey(header);
        }
        return false;
    }
    @Override
    public SwaggerRoute getRoute(String header) {
        return routeMap.get(header);
    }

    @Override
    public List<SwaggerRoute> getRoutes() {
        //排序规则,asc
        Collection<SwaggerRoute> swaggerRoutes=routeMap.values();
        if (swaggerRoutes!=null){
            return swaggerRoutes.stream().sorted(Comparator.comparingInt(SwaggerRoute::getOrder))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}
