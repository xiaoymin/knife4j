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

package com.github.xiaoymin.knife4j.spring.gateway.listener;

import com.github.xiaoymin.knife4j.spring.gateway.AbstractSwaggerResource;
import com.github.xiaoymin.knife4j.spring.gateway.Knife4jSwaggerContainer;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.event.EventListener;

/**
 * @author <a href="milo.xiaomeng@gmail.com">milo.xiaomeng@gmail.com</a>
 *     23/02/26 20:43
 * @since gateway-spring-boot-starter v4.0.0
 */
public class ServiceChangeListener {
    private final DiscoveryClient discoveryClient;
    private final Knife4jSwaggerContainer<? extends AbstractSwaggerResource> abstractKnife4JSwaggerContainer;

    public ServiceChangeListener(DiscoveryClient discoveryClient, Knife4jSwaggerContainer<? extends AbstractSwaggerResource> abstractKnife4JSwaggerContainer) {
        this.discoveryClient = discoveryClient;
        this.abstractKnife4JSwaggerContainer = abstractKnife4JSwaggerContainer;
    }

    @EventListener(classes = { ApplicationReadyEvent.class, HeartbeatEvent.class, RefreshRoutesEvent.class })
    public void discover() {
        this.abstractKnife4JSwaggerContainer.discover(discoveryClient.getServices());
    }
}
