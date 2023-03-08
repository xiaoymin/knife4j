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

package com.github.xiaoymin.knife4j.spring.gateway.v2;

import com.github.xiaoymin.knife4j.spring.gateway.AbstractKnife4jSwaggerContainer;
import com.github.xiaoymin.knife4j.spring.gateway.utils.PathUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties.Router;


/**
 * @author <a href="milo.xiaomeng@gmail.com">milo.xiaomeng@gmail.com</a>
 *     23/02/26 20:43
 * @since gateway-spring-boot-starter v4.0.0
 */
public class Knife4jSwaggerV2Container extends AbstractKnife4jSwaggerContainer<SwaggerV2Resource> {
    protected final String apiPathPrefix;

    public Knife4jSwaggerV2Container(String apiPathPrefix, String apiDocsPath, Integer defaultDiscoveredOrder) {
        super(apiDocsPath, defaultDiscoveredOrder);
        this.apiPathPrefix = apiPathPrefix;
    }

    @Override
    public SwaggerV2Resource convert(String service) {
        SwaggerV2Resource resource = new SwaggerV2Resource(super.defaultDiscoveredOrder, Boolean.TRUE);
        resource.setName(service);
        resource.setContextPath(this.apiPathPrefix);
        resource.setUrl(PathUtils.append(service, super.apiDocsPath));
        resource.setId(Base64.getEncoder().encodeToString(( resource.getName() + resource.getUrl() + resource.getContextPath() ).getBytes(StandardCharsets.UTF_8)));
        return resource;
    }

    @Override
    public SwaggerV2Resource convert(Router router) {
        SwaggerV2Resource swaggerResource = new SwaggerV2Resource(router.getOrder(), Boolean.FALSE);
        swaggerResource.setName(router.getName());
        if(StringUtils.startsWithIgnoreCase(router.getUrl(), "https://") || StringUtils.startsWithIgnoreCase(router.getUrl(), "http://")) {
            swaggerResource.setUrl(router.getUrl());
        } else {
            swaggerResource.setUrl(PathUtils.append(router.getServiceName(), Optional.ofNullable(router.getUrl()).orElse(super.apiDocsPath)));
        }
        swaggerResource.setContextPath(Optional.ofNullable(router.getContextPath()).orElse(this.apiPathPrefix));
        swaggerResource.setId(Base64.getEncoder().encodeToString(( swaggerResource.getName() + swaggerResource.getUrl() + swaggerResource.getContextPath() ).getBytes(StandardCharsets.UTF_8)));
        return swaggerResource;
    }
}
