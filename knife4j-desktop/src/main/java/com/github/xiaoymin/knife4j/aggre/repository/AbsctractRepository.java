/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.gateway.executor.apache.PoolingConnectionManager;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;

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
    protected final Map<String, Map<String, ServiceRoute>> multipartRouteMap = new HashMap<>();
    
    @Override
    public boolean checkRoute(String code, String header) {
        if (StrUtil.isNotBlank(code) && StrUtil.isNotBlank(header)) {
            Map<String, ServiceRoute> routeMap = this.multipartRouteMap.get(code);
            if (CollectionUtil.isNotEmpty(routeMap)) {
                return routeMap.containsKey(header);
            }
        }
        return false;
    }
    @Override
    public ServiceRoute getRoute(String code, String header) {
        if (StrUtil.isNotBlank(code) && StrUtil.isNotBlank(header)) {
            Map<String, ServiceRoute> routeMap = this.multipartRouteMap.get(code);
            if (CollectionUtil.isNotEmpty(routeMap)) {
                return routeMap.get(header);
            }
        }
        return null;
    }
    
    @Override
    public List<ServiceRoute> getRoutes(String code) {
        if (StrUtil.isNotBlank(code)) {
            Map<String, ServiceRoute> routeMap = this.multipartRouteMap.get(code);
            if (CollectionUtil.isNotEmpty(routeMap)) {
                List<ServiceRoute> serviceRoutes = CollectionUtil.newArrayList(routeMap.values());
                Collections.sort(serviceRoutes, Comparator.comparingInt(ServiceRoute::getOrder));
                return serviceRoutes;
            }
        }
        return new ArrayList<>();
    }
    
    @Override
    public void add(String code, Map<String, ServiceRoute> routeMap) {
        this.multipartRouteMap.put(code, routeMap);
    }
}
