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


package com.github.xiaoymin.knife4j.spring.gateway.discover.router;

import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import com.github.xiaoymin.knife4j.spring.gateway.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceRouterConvert;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceRouterHolder;
import com.github.xiaoymin.knife4j.spring.gateway.spec.v2.OpenAPI2Resource;
import com.github.xiaoymin.knife4j.spring.gateway.utils.PathUtils;
import com.github.xiaoymin.knife4j.spring.gateway.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/8/3 15:03
 * @since knife4j v4.3.0
 */
@AllArgsConstructor
@Slf4j
public abstract class AbstactServiceRouterConvert implements ServiceRouterConvert {
    
    /**
     * 获取路由前缀
     * @param predicateArgs 参数
     * @return 路由前缀
     */
    abstract String convertPathPrefix(Map<String, String> predicateArgs);
    
    protected void parseRouteDefinition(ServiceRouterHolder routerHolder, Knife4jGatewayProperties.Discover discover,
                                        List<PredicateDefinition> predicateDefinitions, String id, String serviceName) {
        predicateDefinitions
                .stream()
                .filter(predicateDefinition -> GlobalConstants.ROUTER_PATH_NAME.equalsIgnoreCase(predicateDefinition.getName()))
                .findFirst()
                .ifPresent(predicateDefinition -> {
                    log.debug("serviceId:{},serviceName:{}", id, serviceName);
                    Map<String, Knife4jGatewayProperties.ServiceConfigInfo> configInfoMap = discover.getServiceConfig();
                    // String pathPrefix = predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("**",StringUtil.EMPTY_STRING);
                    String pathPrefix = convertPathPrefix(predicateDefinition.getArgs());
                    log.debug("pathPrefix:{}", pathPrefix);
                    String contextPath;
                    String groupName = id;
                    int order = 0;
                    String targetUrl = ServiceUtils.getOpenAPIURL(discover, pathPrefix, null);
                    // String targetUrl = PathUtils.append(pathPrefix, GlobalConstants.DEFAULT_OPEN_API_V3_PATH);
                    // 如果自定义了setting内容 拼接
                    Knife4jGatewayProperties.ServiceConfigInfo configInfo = configInfoMap.get(serviceName);
                    if (configInfo != null) {
                        order = configInfo.getOrder();
                        contextPath = PathUtils.append(pathPrefix, configInfo.getContextPath());
                        // 复用contextPath的路径
                        targetUrl = PathUtils.append(contextPath, ServiceUtils.getOpenAPIURL(discover, GlobalConstants.DEFAULT_API_PATH_PREFIX, null));
                        List<String> groupNames = configInfo.getGroupNames();
                        if (CollectionUtils.isEmpty(groupNames)) {
                            groupName = configInfo.getGroupName();
                        } else {
                            // 服务内接口分组
                            int sort = order;
                            String ctx = contextPath;
                            String url = targetUrl;
                            groupNames.forEach(_groupName -> routerHolder.add(new OpenAPI2Resource(PathUtils.append(url, _groupName), sort, true, _groupName, ctx, serviceName)));
                            return;
                        }
                    } else {
                        // 如果没有配置service-config，追加一个子服务的前缀contextPath
                        contextPath = PathUtils.processContextPath(pathPrefix);
                    }
                    routerHolder.add(new OpenAPI2Resource(targetUrl, order, true, groupName, contextPath, serviceName));
                });
    }
}
