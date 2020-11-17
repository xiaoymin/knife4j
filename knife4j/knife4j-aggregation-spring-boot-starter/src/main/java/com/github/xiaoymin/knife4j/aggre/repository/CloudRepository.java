/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.aggre.spring.support.CloudSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 基于本地配置的方式动态聚合云端(http)任意OpenAPI
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:11
 */
public class CloudRepository implements RouteRepository {

    private final Map<String,SwaggerRoute> routeMap=new HashMap<>();

    public CloudRepository(CloudSetting cloudSetting){
        if (cloudSetting!=null&&CollectionUtil.isNotEmpty(cloudSetting.getRoutes())){
            cloudSetting.getRoutes().stream().forEach(openApiRoute -> routeMap.put(MD5.create().digestHex(openApiRoute.toString()),new SwaggerRoute(openApiRoute)));
        }
    }

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
        return CollectionUtil.newArrayList(routeMap.values());
    }
}
