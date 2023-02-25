/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.ext.PoolingConnectionManager;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:36
 * @since  2.0.8
 */
public abstract class AbstractRepository extends PoolingConnectionManager implements RouteRepository {
    
    /**
     * 心跳检测间隔(30s)
     */
    protected static final Long HEART_BEAT_DURATION = 30000L;
    
    protected final Map<String, SwaggerRoute> routeMap = new HashMap<>();
    
    @Override
    public boolean checkRoute(String header) {
        if (StrUtil.isNotBlank(header)) {
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
        // 排序规则,asc
        Collection<SwaggerRoute> swaggerRoutes = routeMap.values();
        if (swaggerRoutes != null) {
            return swaggerRoutes.stream().sorted(Comparator.comparingInt(SwaggerRoute::getOrder))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    
    /**
     * Nacos用户可能存在修改服务配置的情况，需要nacosSetting配置与缓存的routeMap做一次compare，避免出现重复服务的情况出现
     * https://gitee.com/xiaoym/knife4j/issues/I3ZPUS
     * @param settingRouteIds
     */
    protected void heartRepeatClear(List<String> settingRouteIds) {
        if (CollectionUtils.isNotEmpty(this.routeMap) && CollectionUtils.isNotEmpty(settingRouteIds)) {
            List<String> settingCacheRouteIds = this.routeMap.entrySet().stream().filter(s -> !settingRouteIds.contains(s.getKey())).map(s -> s.getKey()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(settingRouteIds)) {
                // 缓存中移出，避免重复
                settingCacheRouteIds.forEach(s -> this.routeMap.remove(s));
            }
        }
    }
    
}
