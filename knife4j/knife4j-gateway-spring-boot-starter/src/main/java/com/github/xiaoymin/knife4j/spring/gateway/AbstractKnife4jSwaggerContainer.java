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


package com.github.xiaoymin.knife4j.spring.gateway;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties.Router;

/**
 * @author <a href="milo.xiaomeng@gmail.com">milo.xiaomeng@gmail.com</a>
 *     23/02/26 20:43
 * @since gateway-spring-boot-starter v4.0.0
 */
public abstract class AbstractKnife4jSwaggerContainer<T extends AbstractSwaggerResource> implements Knife4jSwaggerContainer<T>, EnvironmentAware {
    
    private final SortedSet<T> swaggerContainer = Collections.synchronizedSortedSet(new TreeSet<>());
    private final Set<String> excludedDiscoverServices = new HashSet<>();
    private Environment environment;
    protected final String apiDocsPath;
    protected final Integer defaultDiscoveredOrder;
    
    protected AbstractKnife4jSwaggerContainer(String apiDocsPath, Integer defaultDiscoveredOrder) {
        this.apiDocsPath = apiDocsPath;
        this.defaultDiscoveredOrder = defaultDiscoveredOrder;
    }
    
    @Override
    public void discover(List<String> services) {
        Set<T> discoverSwaggerResource = services
                .stream()
                .filter(service -> this.excludedDiscoverServices.stream().noneMatch(service::equals))
                .map(service -> service.equals(this.getApplicationName()) ? "/" : service)
                .map(this::convert)
                .collect(Collectors.toSet());
        this.swaggerContainer.removeIf(AbstractSwaggerResource::getDiscovered);
        this.swaggerContainer.addAll(discoverSwaggerResource);
    }
    
    public abstract T convert(String service);
    
    public abstract T convert(Router router);
    
    public void add(Collection<T> resources) {
        this.swaggerContainer.addAll(resources);
    }
    
    public void addForRoutes(Collection<Router> routers) {
        List<T> resource = routers.stream().map(this::convert).collect(Collectors.toList());
        this.add(resource);
    }
    
    public void addExcludedDiscoverServices(Collection<String> excludedDiscoverServices) {
        this.excludedDiscoverServices.addAll(excludedDiscoverServices);
    }
    
    @Override
    public SortedSet<T> getSwaggerResource() {
        return this.swaggerContainer;
    }
    
    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }
    
    public String getApplicationName() {
        return this.environment.getProperty("spring.application.name");
    }
}
