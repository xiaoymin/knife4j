/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.spring.support;

import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.nacos.NacosRoute;

import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:52
 * @since:knife4j 1.0
 */
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
     * 接口访问密钥
     */
    private String secret;

    /**
     * Nacos注册聚合服务路由集合
     */
    private List<NacosRoute> routes;

    /**
     * 配置的Route路由服务的公共Basic验证信息
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
}
