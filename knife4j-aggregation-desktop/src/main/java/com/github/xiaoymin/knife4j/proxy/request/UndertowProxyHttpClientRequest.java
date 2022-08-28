/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
* Official Web Site: http://www.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.proxy.request;


import com.github.xiaoymin.knife4j.proxy.ProxyHttpClientRequest;
import io.undertow.server.HttpServerExchange;


/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/8 19:50
 * @since:knife4j-aggregation-desktop 2.0
 */
public class UndertowProxyHttpClientRequest implements ProxyHttpClientRequest {


    private final HttpServerExchange exchange;

    public UndertowProxyHttpClientRequest(HttpServerExchange exchange) {
        this.exchange = exchange;
    }

    public HttpServerExchange getExchange() {
        return exchange;
    }
}
