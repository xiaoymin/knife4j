/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.spring.configuration;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/12 20:31
 * @since:knife4j-aggregation-desktop 1.0
 */
public class Knife4jSettingProperties {

    private Knife4jAggregationProperties knife4j;

    public Knife4jAggregationProperties getKnife4j() {
        return knife4j;
    }

    public void setKnife4j(Knife4jAggregationProperties knife4j) {
        this.knife4j = knife4j;
    }
}
