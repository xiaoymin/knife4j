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


package com.github.xiaoymin.knife4j.aggre.spring.support;

import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosRoute;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zc
 * @date 2023/3/20 18:55
 */
public class PolarisSetting {
    
    /**
     * 是否启用
     */
    private boolean enable;
    
    /**
     * Polaris注册中心服务地址,例如：http://192.168.1.200:8080/#/service
     */
    private String serviceUrl;
    /**
     * Polaris注册中心鉴权
     * since 2.0.9
     */
    private BasicAuth serviceAuth;
    
    /**
     * Nacos注册聚合服务路由集合
     */
    private List<NacosRoute> routes;
    
    /**
     * 配置的Route路由服务的公共Basic验证信息，仅作用与访问Swagger接口时使用，具体服务的其他接口不使用该配置
     */
    private BasicAuth routeAuth;
    
    /**
     * Nacos-token失效时间
     */
    private Long tokenExpire = 18000L;
    
    /**
     * secret初始化时间
     */
    private LocalDateTime secretDateTime;
    
}
