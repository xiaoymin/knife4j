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
import com.github.xiaoymin.knife4j.spring.gateway.enums.GatewayStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="milo.xiaomeng@gmail.com">milo.xiaomeng@gmail.com</a>
 * 23/02/26 20:43
 * @since gateway-spring-boot-starter v4.1.0
 */
@Slf4j
@AllArgsConstructor
public class ServiceChangeListener {
    
    final DiscoveryClient discoveryClient;
    final ServiceDiscoverHandler serviceDiscoverHandler;
    final Knife4jGatewayProperties knife4jGatewayProperties;
    
    @EventListener(classes = {ApplicationReadyEvent.class, HeartbeatEvent.class, RefreshRoutesEvent.class})
    public void discover() {
        log.debug("discover service.");
        List<String> services = discoveryClient.getServices();
        if (Objects.equals(knife4jGatewayProperties.getStrategy(), GatewayStrategy.DISCOVER)) {
            this.serviceDiscoverHandler.discover(services);
        }
    }
}
