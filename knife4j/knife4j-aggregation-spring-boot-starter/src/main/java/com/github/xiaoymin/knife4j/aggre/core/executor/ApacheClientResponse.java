/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.aggre.core.executor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteResponse;
import com.github.xiaoymin.knife4j.aggre.core.pojo.HeaderWrapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/***
 *
 * @since  2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 22:04
 */
public class ApacheClientResponse implements RouteResponse {
    
    Logger logger = LoggerFactory.getLogger(ApacheClientResponse.class);
    
    private final HttpResponse httpResponse;
    
    private HttpEntity httpEntity;
    
    public ApacheClientResponse(HttpResponse httpResponse) {
        if (httpResponse == null) {
            throw new IllegalArgumentException("响应请求体不能为空");
        }
        this.httpResponse = httpResponse;
        if (httpResponse != null) {
            httpEntity = httpResponse.getEntity();
        }
    }
    
    @Override
    public List<HeaderWrapper> getHeaders() {
        Header[] headers = this.httpResponse.getAllHeaders();
        List<HeaderWrapper> headerWrappers = new ArrayList<>();
        if (ArrayUtil.isNotEmpty(headers)) {
            for (Header header : headers) {
                if (header != null) {
                    headerWrappers.add(new HeaderWrapper(header.getName(), header.getValue()));
                }
            }
        }
        return headerWrappers;
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
        if (httpEntity != null) {
            Header header = httpEntity.getContentType();
            if (header != null) {
                return header.getValue();
            }
        }
        return "application/json";
    }
    
    @Override
    public Long getContentLength() {
        if (httpEntity != null) {
            return httpEntity.getContentLength();
        }
        return Long.valueOf(-1);
    }
    
    @Override
    public Charset getCharsetEncoding() {
        if (httpEntity != null) {
            Header header = httpEntity.getContentEncoding();
            if (header != null && StrUtil.isNotBlank(header.getValue())) {
                return Charset.forName(header.getValue());
            }
        }
        return Charset.forName("UTF-8");
    }
    
    @Override
    public InputStream getBody() {
        try {
            return httpEntity != null ? httpEntity.getContent() : null;
        } catch (IOException e) {
        }
        return null;
    }
    
    @Override
    public String text() {
        try {
            return EntityUtils.toString(httpEntity, getCharsetEncoding());
        } catch (IOException e) {
            logger.error("transform text error:" + e.getMessage(), e);
        }
        return null;
    }
}
