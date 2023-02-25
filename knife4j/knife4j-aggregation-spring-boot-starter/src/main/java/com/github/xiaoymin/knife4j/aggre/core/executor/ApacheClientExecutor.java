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

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteExecutor;
import com.github.xiaoymin.knife4j.aggre.core.RouteRequestContext;
import com.github.xiaoymin.knife4j.aggre.core.RouteResponse;
import com.github.xiaoymin.knife4j.aggre.core.ext.PoolingConnectionManager;
import com.github.xiaoymin.knife4j.aggre.core.pojo.HeaderWrapper;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/***
 * 基于HttpClient组件的转发策略
 * @since  2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:35
 */
public class ApacheClientExecutor extends PoolingConnectionManager implements RouteExecutor {
    
    Logger logger = LoggerFactory.getLogger(ApacheClientExecutor.class);
    
    private HttpUriRequest buildRequest(RouteRequestContext routeContext) {
        RequestBuilder builder = RequestBuilder.create(routeContext.getMethod());
        if (logger.isDebugEnabled()) {
            logger.debug("ApacheClient Uri:{}", routeContext.getUrl());
        }
        builder.setUri(routeContext.getUrl());
        if (CollectionUtil.isNotEmpty(routeContext.getHeaders())) {
            // 构建Header
            for (HeaderWrapper headerWrapper : routeContext.getHeaders()) {
                builder.addHeader(headerWrapper.getName(), headerWrapper.getValue());
            }
        }
        if (CollectionUtil.isNotEmpty(routeContext.getParams())) {
            // 构建Params
            for (Map.Entry<String, String> entry : routeContext.getParams().entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        if (routeContext.getRequestContent() != null) {
            // 文件请求是否为空 since 2.0.9
            if (CollectionUtil.isNotEmpty(routeContext.getParts())) {
                MultipartEntityBuilder partFileBuilder = MultipartEntityBuilder.create();
                partFileBuilder.setCharset(StandardCharsets.UTF_8);
                partFileBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                // 从请求头获取context-type
                Header header = builder.getFirstHeader("content-type");
                if (header != null) {
                    // 赋值
                    partFileBuilder.setContentType(ContentType.parse(header.getValue()));
                }
                for (Part part : routeContext.getParts()) {
                    try {
                        partFileBuilder.addBinaryBody(part.getName(), part.getInputStream(), ContentType.MULTIPART_FORM_DATA, part.getSubmittedFileName());// 文件流
                    } catch (IOException e) {
                        logger.warn("add part file error,message:" + e.getMessage());
                    }
                }
                builder.setEntity(partFileBuilder.build());
            } else {
                // 普通请求，构建请求体
                BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
                basicHttpEntity.setContent(routeContext.getRequestContent());
                // if the entity contentLength isn't set, transfer-encoding will be set
                // to chunked in org.apache.http.protocol.RequestContent. See gh-1042
                builder.setEntity(basicHttpEntity);
            }
        }
        builder.setConfig(getRequestConfig());
        return builder.build();
    }
    
    @Override
    public RouteResponse executor(RouteRequestContext routeContext) {
        RouteResponse routeResponse = null;
        try {
            // 判断当前接口是否需要执行basic
            CloseableHttpResponse closeableHttpResponse = getClient().execute(buildRequest(routeContext));
            routeResponse = new ApacheClientResponse(closeableHttpResponse);
        } catch (Exception e) {
            logger.error("Executor Failed,message:" + e.getMessage(), e);
            // 当前异常有可能是服务下线导致
            if (e instanceof HttpHostConnectException) {
                // 服务下线，连接失败
                routeResponse = new DefaultClientResponse(routeContext.getOriginalUri(), e.getMessage(), 504);
            } else {
                routeResponse = new DefaultClientResponse(routeContext.getOriginalUri(), e.getMessage());
            }
        }
        return routeResponse;
    }
    
}
