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


package com.github.xiaoymin.knife4j.aggre.core;

import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;

import java.util.List;
import java.util.Map;

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
    boolean checkRoute(String code, String header);
    
    /**
     * 根据请求header获取
     * @param header
     * @return
     */
    ServiceRoute getRoute(String code, String header);
    
    /**
     * 获取所有
     * @return
     */
    List<ServiceRoute> getRoutes(String code);

    /**
     * 移除项目文档
     * @param code
     */
    void remove(String code);
    
    /**
     * 添加文档
     * @param code 项目编号
     * @param routeMap 文档集合
     */
    void add(String code, Map<String, ServiceRoute> routeMap);
}
