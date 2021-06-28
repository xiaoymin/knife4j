/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.disk;

import com.github.xiaoymin.knife4j.aggre.core.pojo.CommonRoute;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/17 22:15
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class DiskRoute extends CommonRoute {

    /**
     * disk模式调试目标服务器的Host地址，如果不为空，则配置可以调试走代理转发
     */
    private String host;

    @Override
    public String toString() {
        return "DiskRoute{" +
                "host='" + host + '\'' +
                "} " + super.toString();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
