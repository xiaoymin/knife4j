/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.spring.support;


import com.github.xiaoymin.knife4j.aggre.eureka.EurekaRoute;

import java.util.List;

/**
 * 从Eureka注册中心中动态聚合
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:33
 * @since:knife4j 1.0
 */
public class EurekaSetting {

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * Eureka注册中心地址,例如(http://localhost:10000/eureka/)
     */
    private String serviceUrl;

    /**
     * 路由列表
     */
    private List<EurekaRoute> routes;

    /**
     * Eureka服务端是否启用了Basic认证，默认false
     */
    private boolean enableBasicAuth=false;

    /**
     * 启用Basic认证后，需提供用户名
     */
    private String username;

    /**
     * 启用Basic认证后,需提供密码
     */
    private String password;

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

    public List<EurekaRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<EurekaRoute> routes) {
        this.routes = routes;
    }

    public boolean isEnableBasicAuth() {
        return enableBasicAuth;
    }

    public void setEnableBasicAuth(boolean enableBasicAuth) {
        this.enableBasicAuth = enableBasicAuth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
