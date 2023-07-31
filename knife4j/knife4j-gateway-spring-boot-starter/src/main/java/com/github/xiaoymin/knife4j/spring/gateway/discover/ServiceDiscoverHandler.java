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


package com.github.xiaoymin.knife4j.spring.gateway.discover;

import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import com.github.xiaoymin.knife4j.spring.gateway.discover.spi.GatewayServiceExcludeService;
import com.github.xiaoymin.knife4j.spring.gateway.enums.OpenApiVersion;
import com.github.xiaoymin.knife4j.spring.gateway.spec.v2.OpenAPI2Resource;
import com.github.xiaoymin.knife4j.spring.gateway.utils.PathUtils;
import io.netty.util.internal.StringUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties.DEFAULT_OPEN_API_V3_PATH;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/3/17 22:59
 * @since knife4j v4.1.0
 */
@Slf4j
public class ServiceDiscoverHandler implements EnvironmentAware {
    
    final RouteDefinitionRepository routeDefinitionRepository;
    final RouteLocator routeLocator;
    final GatewayProperties gatewayPropertiesDefault;
    
    /**
     * Knife4j gateway properties
     */
    final Knife4jGatewayProperties gatewayProperties;
    final ApplicationContext applicationContext;
    private final String LB = "lb://";
    private final String PATH = "Path";
    
    /**
     * 聚合内容
     */
    @Getter
    private List<OpenAPI2Resource> gatewayResources;
    
    /**
     * Spring Environment
     */
    private Environment environment;
    
    public ServiceDiscoverHandler(RouteDefinitionRepository routeDefinitionRepository,
                                  RouteLocator routeLocator,
                                  GatewayProperties gatewayPropertiesDefault,
                                  Knife4jGatewayProperties gatewayProperties, ApplicationContext applicationContext) {
        this.routeDefinitionRepository = routeDefinitionRepository;
        this.routeLocator = routeLocator;
        this.gatewayPropertiesDefault = gatewayPropertiesDefault;
        this.gatewayProperties = gatewayProperties;
        this.applicationContext = applicationContext;
    }
    
    /**
     * 扩展排除服务列表的钩子函数，开发者自定义实现，since 4.2.0
     * @param service 注册中心服务列表
     * @return 排除服务列表
     */
    private Set<String> getExcludeService(List<String> service) {
        // 扩展排除服务列表的钩子函数，开发者自定义实现，since 4.2.0
        // https://gitee.com/xiaoym/knife4j/issues/I6YLMB
        Map<String, GatewayServiceExcludeService> excludeServiceMap = applicationContext.getBeansOfType(GatewayServiceExcludeService.class);
        Set<String> excludeService = new HashSet<>();
        if (!excludeServiceMap.isEmpty()) {
            for (Map.Entry<String, GatewayServiceExcludeService> entry : excludeServiceMap.entrySet()) {
                Set<String> stringSet = entry.getValue().exclude(this.environment, this.gatewayProperties, service);
                if (stringSet != null && !stringSet.isEmpty()) {
                    excludeService.addAll(stringSet);
                }
            }
        }
        return excludeService;
    }
    
    /**
     * 处理注册中心的服务
     *
     * @param service 服务列表集合
     */
    public void discover(List<String> service) {
        log.debug("service has change.");
        Set<String> excludeService = getExcludeService(service);
        // 版本
        OpenApiVersion apiVersion = this.gatewayProperties.getDiscover().getVersion();
        // 判断当前类型
        String url = this.gatewayProperties.getDiscover().getUrl();
        // 个性化服务的配置信息
        Map<String, Knife4jGatewayProperties.ServiceConfigInfo> configInfoMap = this.gatewayProperties.getDiscover().getServiceConfig();
        List<OpenAPI2Resource> resources = new ArrayList<>();
        if (service != null && !service.isEmpty()) {
            for (String serviceName : service) {
                if (excludeService.contains(serviceName)) {
                    continue;
                }
                int order = 0;
                String groupName = serviceName;
                String contextPath = "/";
                Knife4jGatewayProperties.ServiceConfigInfo serviceConfigInfo = configInfoMap.get(serviceName);
                String targetUrl = PathUtils.append(serviceName, url);
                if (serviceConfigInfo != null) {
                    order = serviceConfigInfo.getOrder();
                    // 判断版本
                    if (apiVersion == OpenApiVersion.OpenAPI3) {
                        if (contextPath.equalsIgnoreCase("/")) {
                            // 自动追加一个serviceName
                            contextPath = "/" + serviceName;
                        }
                    }
                    contextPath = PathUtils.append(contextPath, serviceConfigInfo.getContextPath());
                    List<String> groupNames = serviceConfigInfo.getGroupNames();
                    if (CollectionUtils.isEmpty(groupNames)) {
                        groupName = serviceConfigInfo.getGroupName();
                    } else {
                        // 服务内接口分组
                        int sort = order;
                        String ctx = contextPath;
                        groupNames.forEach(_groupName -> resources.add(buildResource(PathUtils.append(targetUrl,
                                _groupName), sort, _groupName, ctx)));
                        continue;
                    }
                }
                resources.add(buildResource(targetUrl, order, groupName, contextPath));
            }
        }
        
        addCustomerResources(resources);
        
        // 赋值
        this.gatewayResources = resources;
    }
    
    /**
     * 处理注册中心的服务（通过解析路由配置）
     *
     * @param service 服务列表集合
     */
    public void discoverDefault(List<String> service) {
        log.debug("service has change ,do discover doc for default route.");
        Set<String> excludeService = getExcludeService(service);
        // 个性化服务的配置信息
        Map<String, Knife4jGatewayProperties.ServiceConfigInfo> configInfoMap = this.gatewayProperties.getDiscover()
                .getServiceConfig();
        Set<OpenAPI2Resource> resources = new TreeSet<>(Comparator.comparing(OpenAPI2Resource::getId));
        // 获取路由定义，并解析
        gatewayPropertiesDefault.getRoutes()
                .stream()
                .filter(routeDefinition -> routeDefinition.getUri().toString().startsWith(LB))
                .filter(routeDefinition -> isInclude(service, excludeService, routeDefinition))
                .forEach(routeDefinition -> parseRouteDefinition(resources, configInfoMap, routeDefinition));
        
        // 动态路由
        routeDefinitionRepository.getRouteDefinitions()
                .filter(routeDefinition -> routeDefinition.getUri().toString().startsWith(LB))
                .filter(routeDefinition -> isInclude(service, excludeService, routeDefinition))
                .subscribe(routeDefinition -> parseRouteDefinition(resources, configInfoMap, routeDefinition));
        
        addCustomerResources(resources);
        // 赋值
        this.gatewayResources = new ArrayList<>(resources);
    }
    
    /**
     * 获取所有OpenAPI资源列表
     *
     * @param forwardPath 请求前缀contextPath路径，一般出现在Nginx代理的情况
     * @return 资源列表
     */
    public List<OpenAPI2Resource> getResources(String forwardPath) {
        List<OpenAPI2Resource> resources = getGatewayResources();
        if (resources != null && !resources.isEmpty()) {
            // 排序
            resources.sort(Comparator.comparing(OpenAPI2Resource::getOrder));
            for (OpenAPI2Resource resource : resources) {
                resource.setContextPath(PathUtils.processContextPath(PathUtils.append(forwardPath, resource.getContextPath())));
                resource.setUrl(PathUtils.append(forwardPath, resource.getUrl()));
            }
            return resources;
        }
        return new ArrayList<>();
    }
    
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    
    private void addCustomerResources(Collection<OpenAPI2Resource> resources) {
        // 在添加自己的配置的个性化的服务
        if (this.gatewayProperties.getRoutes() != null) {
            for (Knife4jGatewayProperties.Router router : this.gatewayProperties.getRoutes()) {
                OpenAPI2Resource resource = new OpenAPI2Resource(router.getOrder(), false);
                resource.setName(router.getName());
                // 开发者配什么就返回什么
                resource.setUrl(router.getUrl());
                resource.setContextPath(router.getContextPath());
                resource.setId(Base64.getEncoder().encodeToString((resource.getName() + resource.getUrl() + resource.getContextPath()).getBytes(StandardCharsets.UTF_8)));
                resources.add(resource);
            }
        }
    }
    
    /**
     * 构建资源
     *
     * @param url         url
     * @param order       排序
     * @param groupName   组名称
     * @param contextPath 上下文路径
     * @return {@link OpenAPI2Resource}
     */
    private OpenAPI2Resource buildResource(String url,
                                           int order,
                                           String groupName,
                                           String contextPath) {
        OpenAPI2Resource resource = new OpenAPI2Resource(order, true);
        resource.setName(groupName);
        resource.setContextPath(contextPath);
        resource.setUrl(url);
        resource.setId(Base64.getEncoder().encodeToString((resource.getName() + resource.getUrl() +
                resource.getContextPath()).getBytes(StandardCharsets.UTF_8)));
        return resource;
    }
    
    private void parseRouteDefinition(Set<OpenAPI2Resource> resources,
                                      Map<String, Knife4jGatewayProperties.ServiceConfigInfo> configInfoMap,
                                      RouteDefinition routeDefinition) {
        routeDefinition.getPredicates()
                .stream()
                .filter(predicateDefinition -> PATH.equalsIgnoreCase(predicateDefinition.getName()))
                .findFirst()
                .ifPresent(predicateDefinition -> {
                    String id = routeDefinition.getId();
                    String serviceName = routeDefinition.getUri().getHost();
                    String pathPrefix = predicateDefinition.getArgs()
                            .get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("**",
                                    StringUtil.EMPTY_STRING);
                    String contextPath = StringUtil.EMPTY_STRING;
                    String groupName = id;
                    int order = 0;
                    String targetUrl = PathUtils.append(pathPrefix, DEFAULT_OPEN_API_V3_PATH);
                    // 如果自定义了setting内容 拼接
                    Knife4jGatewayProperties.ServiceConfigInfo configInfo = configInfoMap.get(serviceName);
                    if (configInfo != null) {
                        order = configInfo.getOrder();
                        contextPath = PathUtils.append(pathPrefix, configInfo.getContextPath());
                        targetUrl = PathUtils.append(contextPath, DEFAULT_OPEN_API_V3_PATH);
                        List<String> groupNames = configInfo.getGroupNames();
                        if (CollectionUtils.isEmpty(groupNames)) {
                            groupName = configInfo.getGroupName();
                        } else {
                            // 服务内接口分组
                            int sort = order;
                            String ctx = contextPath;
                            String url = targetUrl;
                            groupNames.forEach(_groupName -> resources.add(buildResource(PathUtils.append(url,
                                    _groupName), sort, _groupName, ctx)));
                            return;
                        }
                    }
                    resources.add(buildResource(targetUrl, order, groupName, contextPath));
                });
    }
    
    private boolean isInclude(List<String> service, Set<String> excludeService, RouteDefinition routeDefinition) {
        String serviceName = routeDefinition.getUri().getHost();
        return service.contains(serviceName) && !excludeService.contains(serviceName);
    }
}
