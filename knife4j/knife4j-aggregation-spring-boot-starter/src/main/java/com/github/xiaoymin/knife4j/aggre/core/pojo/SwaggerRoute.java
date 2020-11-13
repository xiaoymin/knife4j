/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.pojo;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;

/***
 * 最终返回前端Swagger的数据结构
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/31 9:34
 */
public class SwaggerRoute {

    private String name;
    private String uri;
    private String header;
    private String location;
    private String swaggerVersion;
    private boolean debug=true;
    /**
     * 是否本地请求,本地请求在前端无需添加Header,否则会走代理
     */
    private boolean local=false;

    public SwaggerRoute() {
    }
    public SwaggerRoute(OpenApiRoute openApiRoute){
        if (openApiRoute!=null){
            this.header= MD5.create().digestHex(openApiRoute.toString());
            this.name=openApiRoute.getName();
            if (StrUtil.isNotBlank(openApiRoute.getUri())){
                //判断
                if (!ReUtil.isMatch("(http|https)://.*?$",openApiRoute.getUri())){
                    this.uri="http://"+openApiRoute.getUri();
                }else{
                    this.uri=openApiRoute.getUri();
                }
            }
            this.location=openApiRoute.getLocation();
            this.swaggerVersion=openApiRoute.getSwaggerVersion();
        }
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
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

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
