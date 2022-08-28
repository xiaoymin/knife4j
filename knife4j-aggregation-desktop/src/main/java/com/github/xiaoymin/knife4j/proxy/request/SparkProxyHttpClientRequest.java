/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
* Official Web Site: http://www.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.proxy.request;

import com.github.xiaoymin.knife4j.proxy.ProxyHttpClientRequest;
import spark.Request;
import spark.Response;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/9 9:09
 * @since:knife4j-aggregation-desktop 1.0
 */
public class SparkProxyHttpClientRequest implements ProxyHttpClientRequest {

    private final Request request;
    private final Response response;

    public SparkProxyHttpClientRequest(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}
