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


package com.github.xiaoymin.knife4j.spring.gateway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cloud.gateway.support.NameUtils;

/**
 * Spring Cloud Gateway路由策略
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/8/3 21:27
 * @since knife4j v4.3.0
 */
@Getter
@AllArgsConstructor
public enum GatewayRouterStrategy {
    
    /**
     * 基于gateway配置routes，spring.gateway.routes的方式
     */
    CONFIG(NameUtils.GENERATED_NAME_PREFIX + "0", 1),
    /**
     * 动态注入路由的方式
     */
    DYNAMIC(NameUtils.GENERATED_NAME_PREFIX + "0", 2),
    /**
     * 基于discoverClient客户端默认发现，配置的默认规则，路由keys为：pattern正则
     */
    REACTIVE("pattern", 3),
    /**
     * knife4j自定义配置的routes规则，排序最后，rule规则为空，因为是从knife4j.gateway.routes数据中进行读取
     */
    CUSTOM("", 4);
    /**
     * 路由规则
     */
    final String rule;
    final int order;
    
}
