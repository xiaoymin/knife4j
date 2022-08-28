/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.spring.support;

import com.github.xiaoymin.knife4j.aggre.disk.DiskRoute;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/18 11:17
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
@ConfigurationProperties(prefix = "knife4j.disk")
public class DiskSetting {

    /**
     * 是否启用disk模式
     */
    private boolean enable;

    /**
     * disk模式聚合资源
     */
    private List<DiskRoute> routes;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<DiskRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<DiskRoute> routes) {
        this.routes = routes;
    }
}
