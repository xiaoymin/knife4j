/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.executor;


import com.github.xiaoymin.knife4j.aggre.core.RouteResponse;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/30 10:30
 */
public class DefaultClientResponse implements RouteResponse {

    private final String uri;
    private final String error;

    public DefaultClientResponse(String uri, String error) {
        this.uri = uri;
        this.error = error;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public int getStatusCode() {
        return 500;
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public Long getContentLength() {
        return null;
    }

    @Override
    public Charset getCharsetEncoding() {
        return Charset.forName("UTF-8");
    }

    @Override
    public InputStream getBody() {
        return null;
    }

    @Override
    public String text() {
        String timestamp= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String response="{\n" +
                "    \"timestamp\": \""+timestamp+"\",\n" +
                "    \"status\": "+getStatusCode()+",\n" +
                "    \"message\": \""+error+"\",\n" +
                "    \"path\": \""+uri+"\"\n" +
                "}";
        return response;
    }
}
