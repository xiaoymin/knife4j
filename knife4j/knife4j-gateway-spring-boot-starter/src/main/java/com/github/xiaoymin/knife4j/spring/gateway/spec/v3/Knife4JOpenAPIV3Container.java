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


package com.github.xiaoymin.knife4j.spring.gateway.spec.v3;

import com.github.xiaoymin.knife4j.spring.gateway.spec.AbstractKnife4JOpenAPIContainer;
import com.github.xiaoymin.knife4j.spring.gateway.utils.PathUtils;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties.Router;

/**
 * @author <a href="milo.xiaomeng@gmail.com">milo.xiaomeng@gmail.com</a>
 *     23/02/26 20:43
 * @since gateway-spring-boot-starter v4.0.0
 */
public class Knife4JOpenAPIV3Container extends AbstractKnife4JOpenAPIContainer<OpenAPIV3Resource> {
    
    protected final String apiPathPrefix;
    
    public Knife4JOpenAPIV3Container(String apiPathPrefix, String apiDocsPath, Integer defaultDiscoveredOrder) {
        super(apiDocsPath, defaultDiscoveredOrder);
        this.apiPathPrefix = apiPathPrefix;
    }
    
    @Override
    public OpenAPIV3Resource convert(String service) {
        OpenAPIV3Resource swaggerResource = new OpenAPIV3Resource(super.defaultDiscoveredOrder, Boolean.TRUE);
        swaggerResource.setName(service);
        swaggerResource.setUrl(PathUtils.append(this.apiPathPrefix, super.apiDocsPath));
        return swaggerResource;
    }
    
    @Override
    public OpenAPIV3Resource convert(Router router) {
        OpenAPIV3Resource swaggerResource = new OpenAPIV3Resource(router.getOrder(), Boolean.FALSE);
        swaggerResource.setName(router.getName());
        if (StringUtils.startsWithIgnoreCase(router.getUrl(), "https://") || StringUtils.startsWithIgnoreCase(router.getUrl(), "http://")) {
            swaggerResource.setUrl(router.getUrl());
        } else {
            swaggerResource.setUrl(PathUtils.append(Optional.ofNullable(router.getContextPath()).orElse(this.apiPathPrefix), router.getServiceName(), router.getUrl()));
        }
        return swaggerResource;
    }
}
