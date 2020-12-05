/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.spring.configuration;

import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.spring.support.DiskSetting;
import com.github.xiaoymin.knife4j.aggre.spring.support.EurekaSetting;
import com.github.xiaoymin.knife4j.aggre.spring.support.NacosSetting;
import com.github.xiaoymin.knife4j.aggre.spring.support.CloudSetting;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/13 13:12
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class Knife4jAggregationProperties {

    /**
     * 是否开启Knife4j聚合模式
     */
    private boolean enableAggregation=false;

    /**
     * 文档Basic保护
     */
    private BasicAuth basicAuth;
    /**
     * 本地json
     */
    private DiskSetting disk;

    /**
     * 任意聚合
     */
    private CloudSetting cloud;

    /**
     * 从Eureka注册中心中获取
     */
    private EurekaSetting eureka;

    /**
     * 从Nacos注册中心中获取
     */
    private NacosSetting nacos;

    public boolean isEnableAggregation() {
        return enableAggregation;
    }

    public void setEnableAggregation(boolean enableAggregation) {
        this.enableAggregation = enableAggregation;
    }

    public BasicAuth getBasicAuth() {
        return basicAuth;
    }

    public void setBasicAuth(BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
    }

    public DiskSetting getDisk() {
        return disk;
    }

    public void setDisk(DiskSetting disk) {
        this.disk = disk;
    }

    public CloudSetting getCloud() {
        return cloud;
    }

    public void setCloud(CloudSetting cloud) {
        this.cloud = cloud;
    }

    public EurekaSetting getEureka() {
        return eureka;
    }

    public void setEureka(EurekaSetting eureka) {
        this.eureka = eureka;
    }

    public NacosSetting getNacos() {
        return nacos;
    }

    public void setNacos(NacosSetting nacos) {
        this.nacos = nacos;
    }
}
