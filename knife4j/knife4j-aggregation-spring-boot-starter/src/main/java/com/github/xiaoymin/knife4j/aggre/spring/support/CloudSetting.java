/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.spring.support;

import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.CloudRoute;

import java.util.List;

/**
 * 任意聚合OpenAPI,无注册中心
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:32
 * @since:knife4j 1.0
 */
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

    public List<CloudRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<CloudRoute> routes) {
        this.routes = routes;
    }
}
