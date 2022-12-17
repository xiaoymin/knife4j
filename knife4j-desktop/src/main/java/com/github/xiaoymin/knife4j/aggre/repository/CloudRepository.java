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
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;
import com.github.xiaoymin.knife4j.aggre.spring.support.CloudSetting;

import java.util.HashMap;
import java.util.Map;

/***
 * 基于本地配置的方式动态聚合云端(http)任意OpenAPI
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:11
 */
public class CloudRepository extends AbsctractRepository {
    
    private final Map<String, CloudSetting> cloudSettingMap = new HashMap<>();
    
    @Override
    public void remove(String code) {
        this.multipartRouteMap.remove(code);
        this.cloudSettingMap.remove(code);
    }
    
    /**
     * 根据Cloud模式配置新增
     * @param code
     * @param cloudSetting
     */
    public void add(String code, CloudSetting cloudSetting) {
        if (cloudSetting != null && CollectionUtil.isNotEmpty(cloudSetting.getRoutes())) {
            Map<String, ServiceRoute> cloudRouteMap = new HashMap<>();
            cloudSetting.getRoutes().stream().forEach(cloudRoute -> {
                cloudRouteMap.put(cloudRoute.pkId(), new ServiceRoute(cloudRoute));
            });
            // 存一个副本
            this.cloudSettingMap.put(code, cloudSetting);
            multipartRouteMap.put(code, cloudRouteMap);
        }
    }
}
