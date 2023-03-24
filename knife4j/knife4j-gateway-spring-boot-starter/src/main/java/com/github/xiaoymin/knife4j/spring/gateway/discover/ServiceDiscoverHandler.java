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
import com.github.xiaoymin.knife4j.spring.gateway.enums.OpenApiVersion;
import com.github.xiaoymin.knife4j.spring.gateway.spec.v2.OpenAPI2Resource;
import com.github.xiaoymin.knife4j.spring.gateway.utils.PathUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/3/17 22:59
 * @since:knife4j v4.1.0
 */
@Slf4j
public class ServiceDiscoverHandler implements EnvironmentAware {
    
    /**
     * Knife4j gateway properties
     */
    final Knife4jGatewayProperties gatewayProperties;
    
    /**
     * 聚合内容
     */
    @Getter
    private List<OpenAPI2Resource> gatewayResources;
    
    /**
     * Spring Environment
     */
    private Environment environment;
    
    public ServiceDiscoverHandler(Knife4jGatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }
    
    /**
     * 处理注册中心的服务
     * @param service 服务列表集合
     */
    public void discover(List<String> service) {
        log.debug("service has change.");
        Set<String> excludeService = getExcludeService();
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
                if (serviceConfigInfo != null) {
                    order = serviceConfigInfo.getOrder();
                    groupName = serviceConfigInfo.getGroupName();
                    contextPath = PathUtils.append(contextPath, serviceConfigInfo.getContextPath());
                }
                OpenAPI2Resource resource = new OpenAPI2Resource(order, true);
                resource.setName(groupName);
                resource.setContextPath(contextPath);
                // 判断版本
                if (apiVersion == OpenApiVersion.OpenAPI3) {
                    if (contextPath.equalsIgnoreCase("/")) {
                        // 自动追加一个serviceName
                        resource.setContextPath("/" + serviceName);
                    }
                }
                resource.setUrl(PathUtils.append(serviceName, url));
                resource.setId(Base64.getEncoder().encodeToString((resource.getName() + resource.getUrl() + resource.getContextPath()).getBytes(StandardCharsets.UTF_8)));
                resources.add(resource);
            }
        }
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
        // 赋值
        this.gatewayResources = resources;
    }
    
    /**
     * 获取所有OpenAPI资源列表
     * @param forwardPath 请求前缀contextPath路径，一般出现在Nginx代理的情况
     * @return
     */
    public List<OpenAPI2Resource> getResources(String forwardPath) {
        List<OpenAPI2Resource> resources = getGatewayResources();
        if (resources != null && !resources.isEmpty()) {
            // 排序
            Collections.sort(resources, Comparator.comparing(OpenAPI2Resource::getOrder));
            for (OpenAPI2Resource resource : resources) {
                resource.setContextPath(PathUtils.append(forwardPath, resource.getContextPath()));
                resource.setUrl(PathUtils.append(forwardPath, resource.getUrl()));
            }
            return resources;
        }
        return Collections.EMPTY_LIST;
    }
    
    /**
     * 获取排除的服务列表
     * @return
     */
    public Set<String> getExcludeService() {
        Set<String> excludeService = new HashSet<>();
        String gatewayService = this.environment.getProperty("spring.application.name");
        if (StringUtils.hasLength(gatewayService)) {
            excludeService.add(gatewayService);
        }
        Set<String> configServices = this.gatewayProperties.getDiscover().getExcludedServices();
        if (configServices != null && !configServices.isEmpty()) {
            excludeService.addAll(configServices);
        }
        return excludeService;
    }
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
