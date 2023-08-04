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
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceRouterConvert;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceRouterHolder;
import com.github.xiaoymin.knife4j.spring.gateway.enums.GatewayRouterStrategy;
import com.github.xiaoymin.knife4j.spring.gateway.spec.v2.OpenAPI2Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 在discover服务发现场景下，针对自定义添加的routes，默认再次追加
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/8/3 16:16
 * @since knife4j v4.3.0
 */
@Slf4j
@AllArgsConstructor
public class CustomerServiceConvert implements ServiceRouterConvert {
    
    final Knife4jGatewayProperties knife4jGatewayProperties;
    
    @Override
    public void process(ServiceRouterHolder routerHolder) {
        log.debug("Knife4j Gateway Routes Config process...");
        // 在添加自己的配置的个性化的服务
        if (knife4jGatewayProperties.getRoutes() != null) {
            for (Knife4jGatewayProperties.Router router : knife4jGatewayProperties.getRoutes()) {
                OpenAPI2Resource resource = new OpenAPI2Resource(router.getOrder(), false);
                resource.setName(router.getName());
                // 开发者配什么就返回什么
                resource.setUrl(router.getUrl());
                resource.setContextPath(router.getContextPath());
                resource.setId(Base64.getEncoder().encodeToString((resource.getName() + resource.getUrl() + resource.getContextPath()).getBytes(StandardCharsets.UTF_8)));
                routerHolder.add(resource);
            }
        }
    }
    
    @Override
    public int order() {
        return GatewayRouterStrategy.CUSTOM.getOrder();
    }
}
