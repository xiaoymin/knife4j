/*
 * Copyright 2017-2023 八一菜刀(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.datasource.model;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.common.lang.UserProfileType;
import lombok.Data;

import java.util.*;

/**
 * Knife4j单个项目文档对象信息
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 16:51
 * @since:knife4j-desktop
 */
@Data
public class ServiceDocument {
    
    /**
     * 访问路径
     */
    private String contextPath;
    
    /**
     * 文档鉴权对象
     */
    private UserProfileType userProfile = UserProfileType.DEFAULT;
    
    /**
     * 分组OpenAPI资源
     */
    private List<ServiceRoute> routes = new ArrayList<>();
    
    /**
     * 添加服务分组
     * @param route
     */
    public void addRoute(ServiceRoute route) {
        this.routes.add(route);
    }
    
    /**
     * 根据route唯一id获取Route对象
     * @param routeId
     * @return
     */
    public Optional<ServiceRoute> getRoute(String routeId) {
        if (CollectionUtil.isNotEmpty(routes)) {
            return routes.stream().filter(serviceRoute -> StrUtil.equalsIgnoreCase(serviceRoute.getPkId(), routeId)).findFirst();
        }
        return Optional.empty();
    }
    
    /**
     * 当前文档的上下文唯一id
     * @return
     */
    public String contextId() {
        StringBuilder contextBuilder = new StringBuilder();
        contextBuilder.append(this.contextPath);
        contextBuilder.append(userProfile);
        if (CollectionUtil.isNotEmpty(routes)) {
            Collections.sort(routes, Comparator.comparing(ServiceRoute::getOrder));
            routes.forEach(r -> contextBuilder.append(r.getPkId()));
        }
        return contextBuilder.toString();
    }
    
}
