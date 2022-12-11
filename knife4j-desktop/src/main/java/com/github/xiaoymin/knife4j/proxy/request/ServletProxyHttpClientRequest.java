/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
* Official Web Site: http://www.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.proxy.request;

import com.github.xiaoymin.knife4j.proxy.ProxyHttpClientRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/9 9:09
 * @since:knife4j-aggregation-desktop 1.0
 */
public class ServletProxyHttpClientRequest implements ProxyHttpClientRequest {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public ServletProxyHttpClientRequest(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
