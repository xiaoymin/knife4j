/*
 * Copyright © 2017-2022 Knife4j(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.spring.gateway.configuration;

import com.github.xiaoymin.knife4j.spring.gateway.pojo.Knife4jGatewayRoute;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @since gateway-spring-boot-starter v4.0.0
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/03 15:41
 */
@Data
@ConfigurationProperties(prefix = "knife4j.gateway")
public class Knife4jGatewayProperties {
    
    /**
     * 是否启用聚合Swagger组件
     */
    private boolean enable = false;
    
    /**
     * 聚合分组名称
     */
    private List<Knife4jGatewayRoute> routes;
    
    /**
     * 构建分组数据
     * @return
     */
    public List<Map<String, String>> build() {
        List<Map<String, String>> dataMaps = new ArrayList<>();
        if (routes != null && routes.size() > 0) {
            long count = this.routes.stream().filter(r -> r.getOrder() > 0).count();
            if (count > 0) {
                // 排序
                this.routes.sort(Comparator.comparing(Knife4jGatewayRoute::getOrder));
            }
            for (Knife4jGatewayRoute route : this.routes) {
                Map<String, String> routeMap = new LinkedHashMap<>();
                routeMap.put("name", route.getName());
                routeMap.put("url", route.getUrl());
                routeMap.put("contextPath", route.getContextPath());
                String source = route.getName() + route.getUrl() + route.getServiceName();
                String id = Base64.getEncoder().encodeToString(source.getBytes(StandardCharsets.UTF_8));
                routeMap.put("id", id);
                dataMaps.add(routeMap);
            }
        }
        return dataMaps;
    }
}
