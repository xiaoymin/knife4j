/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.pojo;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/18 21:34
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public abstract class CommonAuthRoute extends CommonRoute{
    /**
     * swagger访问接口请求是否需要Basic验证
     */
    private BasicAuth routeAuth;

    public BasicAuth getRouteAuth() {
        return routeAuth;
    }

    public void setRouteAuth(BasicAuth routeAuth) {
        this.routeAuth = routeAuth;
    }
}
