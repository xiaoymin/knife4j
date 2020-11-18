/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core;

import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:34
 */
public class RouteRequestContext {
    /**
     * 当前请求的接口地址
     */
    private String originalUri;
    /**
     * 请求接口
     */
    private String url;
    /**
     * 请求类型
     */
    private String method;
    /**
     * 请求头
     */
    private Map<String,String> headers=new HashMap<>();
    /**
     * 查询参数
     */
    private Map<String,String> params=new HashMap<>();

    /**
     * 请求内容
     */
    private InputStream requestContent;

    /**
     * 请求长度
     */
    private Long contentLength;
    /**
     * Basic验证
     */
    private BasicAuth basicAuth;

    /**
     * 添加请求头
     * @param key
     * @param value
     */
    public void addHeader(String key,String value){
        this.headers.put(key,value);
    }

    /**
     * 添加params参数
     * @param name
     * @param value
     */
    public void addParam(String name,String value){
        this.params.put(name,value);
    }

    public BasicAuth getBasicAuth() {
        return basicAuth;
    }

    public void setBasicAuth(BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
    }

    public String getOriginalUri() {
        return originalUri;
    }

    public void setOriginalUri(String originalUri) {
        this.originalUri = originalUri;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public InputStream getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(InputStream requestContent) {
        this.requestContent = requestContent;
    }
}
