/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.nacos;

import cn.hutool.crypto.digest.MD5;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/16 22:52
 * @since:knife4j 1.0
 */
public class NacosRoute {
    /**
     * 服务名称(最终在Ui界面中分组名称显示，该值可以为空，如果为空，则在Ui分组中显示serviceName)
     */
    private String name;
    /**
     * 服务名称,不能为空,代表需要聚合的服务
     */
    private String serviceName;

    /**
     * openapi路径，例如：/v2/api-docs?group=default
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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
        return "NacosRoute{" +
                "name='" + name + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", location='" + location + '\'' +
                ", swaggerVersion='" + swaggerVersion + '\'' +
                ", servicePath='" + servicePath + '\'' +
                '}';
    }
}
