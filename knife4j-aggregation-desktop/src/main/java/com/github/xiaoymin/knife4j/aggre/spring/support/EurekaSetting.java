/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.spring.support;


import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.eureka.EurekaRoute;

import java.util.List;

/**
 * 从Eureka注册中心中动态聚合
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:33
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class EurekaSetting extends BaseSetting{

    /**
     * Eureka注册中心地址,例如(http://localhost:10000/eureka/)
     */
    private String serviceUrl;
    /**
     * 注册中心请求接口是否需要Basic验证
     */
    private BasicAuth serviceAuth;
    /**
     * 配置的Route路由服务的公共Basic验证信息，仅作用与访问Swagger接口时使用，具体服务的其他接口不使用该配置
     */
    private BasicAuth routeAuth;

    /**
     * 路由列表
     */
    private List<EurekaRoute> routes;

    public BasicAuth getRouteAuth() {
        return routeAuth;
    }

    public void setRouteAuth(BasicAuth routeAuth) {
        this.routeAuth = routeAuth;
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

    public BasicAuth getServiceAuth() {
        return serviceAuth;
    }

    public void setServiceAuth(BasicAuth serviceAuth) {
        this.serviceAuth = serviceAuth;
    }
}
