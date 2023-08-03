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
import com.github.xiaoymin.knife4j.spring.gateway.spec.v2.OpenAPI2Resource;
import com.github.xiaoymin.knife4j.spring.gateway.utils.PathUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.*;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/3/17 22:59
 * @since knife4j v4.1.0
 */
@Slf4j
public class ServiceDiscoverHandler implements EnvironmentAware, ApplicationContextAware {
    
    /**
     * Knife4j gateway properties
     */
    final Knife4jGatewayProperties knife4jGatewayProperties;
    /**
     * 聚合内容
     */
    @Getter
    private List<OpenAPI2Resource> gatewayResources;
    
    /**
     * Spring Environment
     */
    private Environment environment;
    private ApplicationContext applicationContext;
    
    public ServiceDiscoverHandler(Knife4jGatewayProperties knife4jGatewayProperties) {
        this.knife4jGatewayProperties = knife4jGatewayProperties;
    }
    
    /**
     * 扩展排除服务列表的钩子函数，开发者自定义实现，since 4.2.0
     * @param service 注册中心服务列表
     * @return 排除服务列表
     * @since v4.2.0
     */
    private Set<String> getExcludeService(List<String> service) {
        // 扩展排除服务列表的钩子函数，开发者自定义实现，since 4.2.0
        // https://gitee.com/xiaoym/knife4j/issues/I6YLMB
        Map<String, GatewayServiceExcludeService> excludeServiceMap = applicationContext.getBeansOfType(GatewayServiceExcludeService.class);
        Set<String> excludeService = new HashSet<>();
        if (!excludeServiceMap.isEmpty()) {
            for (Map.Entry<String, GatewayServiceExcludeService> entry : excludeServiceMap.entrySet()) {
                Set<String> stringSet = entry.getValue().exclude(this.environment, this.knife4jGatewayProperties, service);
                if (stringSet != null && !stringSet.isEmpty()) {
                    excludeService.addAll(stringSet);
                }
            }
        }
        log.debug("exclude-service-size:{},value:{}", excludeService.size(), String.join(",", excludeService));
        return excludeService;
    }
    /**
     * 处理注册中心的服务（通过解析路由配置）
     *
     * @param service 服务列表集合
     */
    public void discover(List<String> service) {
        log.debug("service has change ,do discover doc for default route.");
        Set<String> excludeService = getExcludeService(service);
        ServiceRouterHolder holder=new ServiceRouterHolder(service,excludeService);
        Map<String,ServiceRouterConvert> routerConvertMap = applicationContext.getBeansOfType(ServiceRouterConvert.class);
        List<ServiceRouterConvert> serviceRouterConverts = new ArrayList<>(routerConvertMap.values());
        serviceRouterConverts.sort(Comparator.comparing(ServiceRouterConvert::order));
        for (ServiceRouterConvert routerConvert:serviceRouterConverts){
            routerConvert.process(holder);
        }
        // 赋值
        this.gatewayResources = new ArrayList<>(holder.getResources());
    }
    
    /**
     * 获取所有OpenAPI资源列表
     *
     * @param forwardPath 请求前缀contextPath路径，一般出现在Nginx代理的情况
     * @return 资源列表
     */
    public List<OpenAPI2Resource> getResources(String forwardPath) {
        List<OpenAPI2Resource> resourceList = new ArrayList<>();
        List<OpenAPI2Resource> resources = getGatewayResources();
        if (resources != null && !resources.isEmpty()) {
            // 排序
            resources.sort(Comparator.comparing(OpenAPI2Resource::getOrder));
            for (OpenAPI2Resource resource : resources) {
                // copy one,https://gitee.com/xiaoym/knife4j/issues/I73AOG
                OpenAPI2Resource copy = resource.copy();
                copy.setContextPath(PathUtils.processContextPath(PathUtils.append(forwardPath, copy.getContextPath())));
                copy.setUrl(PathUtils.append(forwardPath, copy.getUrl()));
                log.debug("api-resources:{}", copy);
                // 添加
                resourceList.add(copy);
            }
            return resourceList;
        }
        return resourceList;
    }
    
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
