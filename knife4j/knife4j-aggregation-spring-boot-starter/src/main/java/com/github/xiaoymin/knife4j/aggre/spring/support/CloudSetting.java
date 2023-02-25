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
import com.github.xiaoymin.knife4j.aggre.cloud.CloudRoute;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 任意聚合OpenAPI,无注册中心
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:32
 * @since  2.0.8
 */
@ConfigurationProperties(prefix = "knife4j.cloud")
public class CloudSetting {
    
    /**
     * 是否启用
     */
    private boolean enable;
    /**
     * 微服务集合
     */
    private List<CloudRoute> routes;
    
    /**
     * 配置的Route路由服务的公共Basic验证信息，仅作用与访问Swagger接口时使用，具体服务的其他接口不使用该配置
     */
    private BasicAuth routeAuth;
    
    public BasicAuth getRouteAuth() {
        return routeAuth;
    }
    
    public void setRouteAuth(BasicAuth routeAuth) {
        this.routeAuth = routeAuth;
    }
    
    public boolean isEnable() {
        return enable;
    }
    
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    
    public List<CloudRoute> getRoutes() {
        return routes;
    }
    
    public void setRoutes(List<CloudRoute> routes) {
        this.routes = routes;
    }
}
