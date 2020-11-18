/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.cloud;

import com.github.xiaoymin.knife4j.aggre.core.pojo.CommonAuthRoute;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/13 13:14
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class CloudRoute extends CommonAuthRoute {

    /**
     * 地址，例如：http://192.179.0.1:8999
     */
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
