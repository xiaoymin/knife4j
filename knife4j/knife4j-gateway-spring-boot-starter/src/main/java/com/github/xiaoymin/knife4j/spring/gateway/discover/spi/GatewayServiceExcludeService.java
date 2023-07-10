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


package com.github.xiaoymin.knife4j.spring.gateway.discover.spi;

import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Set;

/**
 * 网关聚合服务中，排除聚合规则
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/7/8 12:56
 * @since knife4j v4.2.0
 */
public interface GatewayServiceExcludeService {
    
    /**
     * 获取要排除的服务列表
     * @param environment 当前环境变量
     * @param properties Knife4j网关聚合配置信息
     * @param services 微服务注册中心中所有子服务
     * @return 需要排除的子服务列表，不参加网关Swagger文档聚合
     */
    Set<String> exclude(Environment environment, Knife4jGatewayProperties properties, List<String> services);
}
