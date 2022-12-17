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


package com.github.xiaoymin.knife4j.aggre.spring.support;

import com.github.xiaoymin.knife4j.datasource.model.config.route.CloudRoute;

import java.util.List;

/**
 * 任意聚合OpenAPI,无注册中心
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:32
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class CloudSetting extends BaseSetting {
    
    /**
     * 微服务集合
     */
    private List<CloudRoute> routes;
    public List<CloudRoute> getRoutes() {
        return routes;
    }
    
    public void setRoutes(List<CloudRoute> routes) {
        this.routes = routes;
    }
}
