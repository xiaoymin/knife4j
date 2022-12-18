/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import com.github.xiaoymin.knife4j.datasource.model.config.route.NacosRoute;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:52
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class NacosSetting extends BaseSetting {
    
    /**
     * Nacos注册中心服务地址,例如：http://192.168.0.223:8888/nacos
     */
    private String serviceUrl;
    /**
     * 接口访问密钥
     */
    private String secret;
    
    /**
     * Nacos注册聚合服务路由集合
     */
    private List<NacosRoute> routes;
    
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
    
    public void initAccessToken() {
        // 判断当前Nacos是否需要鉴权访问
        /**if (this.serviceAuth != null && this.serviceAuth.isEnable()) {
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
        }**/
    }
}
