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

import java.util.List;
import java.util.Map;

import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.util.CollectionUtils;

import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import com.github.xiaoymin.knife4j.spring.gateway.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceRouterConvert;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceRouterHolder;
import com.github.xiaoymin.knife4j.spring.gateway.enums.OpenApiVersion;
import com.github.xiaoymin.knife4j.spring.gateway.spec.v2.OpenAPI2Resource;
import com.github.xiaoymin.knife4j.spring.gateway.utils.PathUtils;
import com.github.xiaoymin.knife4j.spring.gateway.utils.ServiceUtils;
import com.github.xiaoymin.knife4j.spring.gateway.utils.StrUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务路由转换接口抽象处理类
 * 
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a><br>
 *         2023/8/3 15:03
 * @since knife4j v4.3.0
 */
@AllArgsConstructor
@Slf4j
public abstract class AbstactServiceRouterConvert implements ServiceRouterConvert {
    
    /**
     * 获取路由前缀
     * 
     * @param predicateArgs 参数
     * @return 路由前缀
     */
    abstract String convertPathPrefix(Map<String, String> predicateArgs);
    
    /**
     * 获取服务发现策略配置信息
     * 
     * @return 服务发现策略配置信息
     */
    abstract Knife4jGatewayProperties.Discover getDiscover();
    
    /**
     * 解析gateway的路由定义
     * 
     * @param routerHolder 当前路由处理
     * @param predicateDefinitions 断言definitions
     * @param id id
     * @param serviceName 服务名称
     */
    protected void parseRouteDefinition(ServiceRouterHolder routerHolder,
                                        List<PredicateDefinition> predicateDefinitions, String id, String serviceName) {
        predicateDefinitions.stream().filter(
                predicateDefinition -> GlobalConstants.ROUTER_PATH_NAME.equalsIgnoreCase(predicateDefinition.getName()))
                .findFirst().ifPresent(predicateDefinition -> this.processRouteDefinition(routerHolder, id, serviceName,
                        predicateDefinition));
    }
    
    /**
     * 处理gateway的路由定义
     * 
     * @param routerHolder 当前路由处理
     * @param id id
     * @param serviceName 服务名称
     * @param predicateDefinition 断言definitions
     */
    private void processRouteDefinition(ServiceRouterHolder routerHolder, String id, String serviceName,
                                        PredicateDefinition predicateDefinition) {
        log.debug("serviceId:{},serviceName:{}", id, serviceName);
        String pathPrefix = this.convertPathPrefix(predicateDefinition.getArgs());
        log.debug("pathPrefix:{}", pathPrefix);
        String contextPath = GlobalConstants.EMPTY_STR;
        String groupName = id;
        int order = 0;
        Knife4jGatewayProperties.Discover discover = this.getDiscover();
        String targetUrl = ServiceUtils.getOpenAPIURL(discover, pathPrefix, null);
        // 如果自定义了setting内容 拼接
        Knife4jGatewayProperties.ServiceConfigInfo configInfo = this.getServiceConfigInfo(serviceName);
        if (configInfo != null) {
            order = configInfo.getOrder();
            if (discover.getVersion() == OpenApiVersion.OpenAPI3) {
                // 如果是springfox-swagger2，springfox框架会自动根据targetUrl访问追加一个basePath路径到Swagger2规范中
                // 避免重复追加ContextPath路径，此规则只作用与openapi3
                contextPath = PathUtils.append(pathPrefix, configInfo.getContextPath());
            } else if (discover.getVersion() == OpenApiVersion.Swagger2) {
                // 如果是swagger2场景，判断当前contextPath配置的是否为空
                if (StrUtil.isNotBlank(configInfo.getContextPath())
                        && !GlobalConstants.DEFAULT_API_PATH_PREFIX.equals(configInfo.getContextPath())) {
                    // 用户自行设定，追加
                    contextPath = configInfo.getContextPath();
                }
            }
            // 复用contextPath的路径
            targetUrl = PathUtils.append(contextPath,
                    ServiceUtils.getOpenAPIURL(discover, GlobalConstants.DEFAULT_API_PATH_PREFIX, null));
            List<String> groupNames = configInfo.getGroupNames();
            if (CollectionUtils.isEmpty(groupNames)) {
                groupName = configInfo.getGroupName();
            } else {
                // 服务内接口分组
                int sort = order;
                String ctx = contextPath;
                String url = targetUrl;
                groupNames.forEach(name -> routerHolder
                        .add(this.buildOpenApi2Resource(serviceName, ctx, name, sort, PathUtils.append(url, name))));
                return;
            }
        } else {
            // 如果没有配置service-config，追加一个子服务的前缀contextPath
            // 仅在openapi3框架下追加，springfox-swagger2会默认根据url的请求，自动在Swagger2规范中设置一个basePath属性
            if (discover.getVersion() == OpenApiVersion.OpenAPI3) {
                contextPath = PathUtils.processContextPath(pathPrefix);
            }
        }
        routerHolder.add(this.buildOpenApi2Resource(serviceName, contextPath, groupName, order, targetUrl));
    }
    
    /**
     * 从配置里面获取服务配置信息
     * 
     * @param serviceName 服务名称
     * @return 服务配置
     */
    private Knife4jGatewayProperties.ServiceConfigInfo getServiceConfigInfo(String serviceName) {
        Map<String, Knife4jGatewayProperties.ServiceConfigInfo> configInfoMap = this.getDiscover().getServiceConfig();
        return configInfoMap.get(serviceName);
    }
    
    /**
     * 构建资源
     * 
     * @param serviceName 服务名称
     * @param contextPath contextPath
     * @param groupName 分组名称
     * @param order 排序
     * @param targetUrl 目标地址
     * @return 资源
     */
    private OpenAPI2Resource buildOpenApi2Resource(String serviceName, String contextPath, String groupName, int order,
                                                   String targetUrl) {
        return new OpenAPI2Resource(targetUrl, order, true, groupName, contextPath, serviceName);
    }
    
}
