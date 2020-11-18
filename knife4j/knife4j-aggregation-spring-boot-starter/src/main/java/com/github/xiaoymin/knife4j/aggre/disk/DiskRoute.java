/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.disk;

import cn.hutool.crypto.digest.MD5;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/17 22:15
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class DiskRoute {
    /**
     * 服务名称(最终在Ui分组进行展示)
     */
    private String name;
    /**
     * openapi本地文件路径或者OpenAPI访问接口路径
     */
    private String location;

    /**
     * OpenAPI版本，2.0 or 3.0
     */
    private String swaggerVersion="2.0";

    /**
     * 微服务路径,主要是针对在网关使用时，追加的basePath，主要是为了和在网关转发时路径在文档上展示一致的问题
     */
    private String servicePath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSwaggerVersion() {
        return swaggerVersion;
    }

    public void setSwaggerVersion(String swaggerVersion) {
        this.swaggerVersion = swaggerVersion;
    }

    public String getServicePath() {
        return servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public String pkId(){
        return MD5.create().digestHex(this.toString());
    }

    @Override
    public String toString() {
        return "DiskRoute{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", swaggerVersion='" + swaggerVersion + '\'' +
                ", servicePath='" + servicePath + '\'' +
                '}';
    }
}
