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


package com.github.xiaoymin.knife4j.spring.gateway.discover.spi.impl;

import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import com.github.xiaoymin.knife4j.spring.gateway.discover.spi.GatewayServiceExcludeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 默认排除规则
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/7/8 12:58
 * @since knife4j v4.2.0
 */
@Slf4j
public class DefaultGatewayServiceExcludeService implements GatewayServiceExcludeService {
    
    @Override
    public Set<String> exclude(Environment environment, Knife4jGatewayProperties gatewayProperties, List<String> services) {
        Set<String> excludeService = new HashSet<>();
        String gatewayService = environment.getProperty("spring.application.name");
        if (StringUtils.hasLength(gatewayService)) {
            excludeService.add(gatewayService);
        }
        Set<String> configServices = gatewayProperties.getDiscover().getExcludedServices();
        if (configServices != null && !configServices.isEmpty()) {
            excludeService.addAll(configServices);
        }
        return excludeService;
    }
}
