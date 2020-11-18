/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.eureka;

import java.util.List;

/**
 * Eureka注册中心应用Model
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:20
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class EurekaApplication {

    /**
     * 服务列表
     */
    private String name;

    /**
     * 实例列表
     */
    private List<EurekaInstance> instance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EurekaInstance> getInstance() {
        return instance;
    }

    public void setInstance(List<EurekaInstance> instance) {
        this.instance = instance;
    }
}
