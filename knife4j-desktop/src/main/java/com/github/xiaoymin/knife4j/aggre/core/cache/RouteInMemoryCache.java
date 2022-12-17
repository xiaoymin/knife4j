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


package com.github.xiaoymin.knife4j.aggre.core.cache;

import com.github.xiaoymin.knife4j.aggre.core.RouteCache;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;

import java.util.concurrent.ConcurrentHashMap;
/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/31 11:07
 */
public class RouteInMemoryCache implements RouteCache<String, ServiceRoute> {
    
    private final ConcurrentHashMap<String, ServiceRoute> cache = new ConcurrentHashMap<>();
    
    @Override
    public boolean put(String s, ServiceRoute serviceRoute) {
        cache.put(s, serviceRoute);
        return true;
    }
    
    @Override
    public ServiceRoute get(String s) {
        return cache.get(s);
    }
    
    @Override
    public boolean remove(String s) {
        cache.remove(s);
        return true;
    }
}
