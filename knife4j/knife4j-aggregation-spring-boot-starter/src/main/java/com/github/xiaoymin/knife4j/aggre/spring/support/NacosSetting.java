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
import com.github.xiaoymin.knife4j.aggre.nacos.NacosOpenApi;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosRoute;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:52
 * @since  2.0.8
 */
@ConfigurationProperties(prefix = "knife4j.nacos")
public class NacosSetting {
    
    /**
     * 是否启用
     */
    private boolean enable;
    
    /**
     * Nacos注册中心服务地址,例如：http://192.168.0.223:8888/nacos
     */
    private String serviceUrl;
    /**
     * Nacos注册中心鉴权,参考issue：https://gitee.com/xiaoym/knife4j/issues/I28IF9
     * since 2.0.9
     */
    private BasicAuth serviceAuth;
    /**
     * 接口访问密钥
     */
    private String secret;
    
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
    
    public LocalDateTime getSecretDateTime() {
        return secretDateTime;
    }
    
    public void setSecretDateTime(LocalDateTime secretDateTime) {
        this.secretDateTime = secretDateTime;
    }
    
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
    
    public String getServiceUrl() {
        return serviceUrl;
    }
    
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
    
    public String getSecret() {
        return secret;
    }
    
    public void setSecret(String secret) {
        this.secret = secret;
    }
    
    public List<NacosRoute> getRoutes() {
        return routes;
    }
    
    public void setRoutes(List<NacosRoute> routes) {
        this.routes = routes;
    }
    
    public BasicAuth getServiceAuth() {
        return serviceAuth;
    }
    
    public void setServiceAuth(BasicAuth serviceAuth) {
        this.serviceAuth = serviceAuth;
    }
    
    public void initAccessToken() {
        // 判断当前Nacos是否需要鉴权访问
        if (this.serviceAuth != null && this.serviceAuth.isEnable()) {
            if (this.secretDateTime == null) {
                setSecret(NacosOpenApi.me().getAccessToken(this.serviceUrl, this.serviceAuth));
                setSecretDateTime(LocalDateTime.now().plusSeconds(this.tokenExpire));
            } else {
                LocalDateTime nowTime = LocalDateTime.now();
                long seconds = Duration.between(nowTime, this.secretDateTime).getSeconds();
                // token expired,初始化token
                if (seconds < 100) {
                    setSecret(NacosOpenApi.me().getAccessToken(this.serviceUrl, this.serviceAuth));
                    setSecretDateTime(LocalDateTime.now().plusSeconds(this.tokenExpire));
                }
            }
        }
    }
}
