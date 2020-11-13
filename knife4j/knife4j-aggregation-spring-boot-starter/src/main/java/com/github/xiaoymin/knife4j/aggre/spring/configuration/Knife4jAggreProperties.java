/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.spring.configuration;

import com.github.xiaoymin.knife4j.aggre.core.pojo.OpenApiRoute;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/13 13:12
 * @since:knife4j 1.0
 */
@Component
@ConfigurationProperties(prefix = "knife4j")
public class Knife4jAggreProperties {

    /**
     * 是否开启Knife4j聚合模式
     */
    private boolean enableAggre=false;

    /**
     * 微服务集合
     */
    private List<OpenApiRoute> routes;

    public boolean isEnableAggre() {
        return enableAggre;
    }

    public void setEnableAggre(boolean enableAggre) {
        this.enableAggre = enableAggre;
    }

    public List<OpenApiRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<OpenApiRoute> routes) {
        this.routes = routes;
    }
}
