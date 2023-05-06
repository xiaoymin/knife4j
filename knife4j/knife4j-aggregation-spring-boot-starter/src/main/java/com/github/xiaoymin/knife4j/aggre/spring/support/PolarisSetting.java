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
import com.github.xiaoymin.knife4j.aggre.polaris.PolarisOpenApi;
import com.github.xiaoymin.knife4j.aggre.polaris.PolarisRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author zc
 * @date 2023/3/20 18:55
 */
@ConfigurationProperties(prefix = "knife4j.polaris")
public class PolarisSetting {
    
    Logger logger = LoggerFactory.getLogger(PolarisSetting.class);
    
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
     */
    private BasicAuth serviceAuth;
    
    /**
     * 接口访问密钥
     */
    private String jwtCookie;
    
    /**
     * Polaris注册聚合服务路由集合
     */
    private List<PolarisRoute> routes;
    
    /**
     * 配置的Route路由服务的公共Basic验证信息，仅作用与访问Swagger接口时使用，具体服务的其他接口不使用该配置
     */
    private BasicAuth routeAuth;
    
    public boolean isEnable() {
        return enable;
    }
    
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    
    public String getServiceUrl() {
        return serviceUrl;
    }
    
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
    
    public BasicAuth getServiceAuth() {
        return serviceAuth;
    }
    
    public void setServiceAuth(BasicAuth serviceAuth) {
        this.serviceAuth = serviceAuth;
    }
    
    public String getJwtCookie() {
        return jwtCookie;
    }
    
    public void setJwtCookie(String jwtCookie) {
        if (logger.isDebugEnabled()) {
            logger.debug("Polaris JwtCookie update: {}", jwtCookie);
        }
        this.jwtCookie = jwtCookie;
    }
    
    public List<PolarisRoute> getRoutes() {
        return routes;
    }
    
    public void setRoutes(List<PolarisRoute> routes) {
        this.routes = routes;
    }
    
    public BasicAuth getRouteAuth() {
        return routeAuth;
    }
    
    public void setRouteAuth(BasicAuth routeAuth) {
        this.routeAuth = routeAuth;
    }
    
    public void initJwtCookie() {
        if (this.serviceAuth != null && this.serviceAuth.isEnable()) {
            this.setJwtCookie(PolarisOpenApi.me().getJwtCookie(this.serviceUrl, this.serviceAuth));
        }
    }
}
