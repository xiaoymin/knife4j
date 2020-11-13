/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.executor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 22:04
 */
public class ApacheClientResponse implements RouteResponse {

    Logger logger= LoggerFactory.getLogger(ApacheClientResponse.class);

    private final HttpResponse httpResponse;

    private HttpEntity httpEntity;

    public ApacheClientResponse(HttpResponse httpResponse) {
        if (httpResponse==null){
            throw new IllegalArgumentException("响应请求体不能为空");
        }
        this.httpResponse = httpResponse;
        if (httpResponse!=null){
            httpEntity=httpResponse.getEntity();
        }
    }

    @Override
    public Map<String, String> getHeaders() {
        Header[] headers= this.httpResponse.getAllHeaders();
        Map<String,String> headerMaps=new HashMap<>();
        if (ArrayUtil.isNotEmpty(headers)){
            for (Header header:headers){
                if (header!=null){
                    headerMaps.put(header.getName(),header.getValue());
                }
            }
        }
        return headerMaps;
    }

    @Override
    public boolean success() {
        return true;
    }

    @Override
    public int getStatusCode() {
        return httpResponse.getStatusLine().getStatusCode();
    }

    @Override
    public String getContentType() {
        if (httpEntity!=null){
            Header header=httpEntity.getContentType();
            if (header!=null){
                return header.getValue();
            }
        }
        return "application/json";
    }

    @Override
    public Long getContentLength() {
        if (httpEntity!=null){
            return httpEntity.getContentLength();
        }
        return Long.valueOf(-1);
    }

    @Override
    public Charset getCharsetEncoding() {
        if (httpEntity!=null){
            Header header= httpEntity.getContentEncoding();
            if (header!=null && StrUtil.isNotBlank(header.getValue())){
                return Charset.forName(header.getValue());
            }
        }
        return Charset.forName("UTF-8");
    }

    @Override
    public InputStream getBody() {
        try {
            return httpEntity!=null?httpEntity.getContent():null;
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    public String text() {
        try {
            return EntityUtils.toString(httpEntity,getCharsetEncoding());
        } catch (IOException e) {
            logger.error("transform text error:"+e.getMessage(),e);
        }
        return null;
    }
}
