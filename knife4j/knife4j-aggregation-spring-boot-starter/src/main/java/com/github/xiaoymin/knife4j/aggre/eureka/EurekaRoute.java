/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.eureka;

import com.github.xiaoymin.knife4j.aggre.core.pojo.CommonAuthRoute;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:38
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class EurekaRoute extends CommonAuthRoute {

    /**
     * 服务名称,不能为空,代表需要聚合的服务
     */
    private String serviceName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
