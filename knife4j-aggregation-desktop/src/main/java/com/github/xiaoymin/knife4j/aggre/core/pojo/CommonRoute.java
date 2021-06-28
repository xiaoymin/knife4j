/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.pojo;

import cn.hutool.crypto.digest.MD5;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/18 21:34
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public abstract class CommonRoute {
    /**
     * 服务名称
     */
    private String name;
    /**
     * 排序顺序
     */
    private int order=0;
    /**
     * openapi本地文件路径
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

    /**
     * 当前Route主键唯一id
     * @return 唯一id
     */
    public String pkId(){
        return MD5.create().digestHex(this.toString());
    }


    @Override
    public String toString() {
        return "CommonRoute{" +
                "name='" + name + '\'' +
                ", order=" + order +
                ", location='" + location + '\'' +
                ", swaggerVersion='" + swaggerVersion + '\'' +
                ", servicePath='" + servicePath + '\'' +
                '}';
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

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
}
