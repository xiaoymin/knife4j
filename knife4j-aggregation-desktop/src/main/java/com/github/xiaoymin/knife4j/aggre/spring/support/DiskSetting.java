/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.spring.support;

import com.github.xiaoymin.knife4j.aggre.disk.DiskRoute;

import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/18 11:17
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class DiskSetting extends BaseSetting{

    private List<DiskRoute> routes;

    public List<DiskRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<DiskRoute> routes) {
        this.routes = routes;
    }
}
