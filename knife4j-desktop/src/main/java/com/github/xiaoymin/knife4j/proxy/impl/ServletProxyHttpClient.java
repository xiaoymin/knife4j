/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.proxy.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.RouteRequestContext;
import com.github.xiaoymin.knife4j.aggre.core.RouteResponse;
import com.github.xiaoymin.knife4j.aggre.core.common.ExecutorEnum;
import com.github.xiaoymin.knife4j.aggre.core.common.RouteUtils;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.HeaderWrapper;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import com.github.xiaoymin.knife4j.proxy.AbstractProxyHttpClient;
import com.github.xiaoymin.knife4j.proxy.ProxyHttpClientRequest;
import com.github.xiaoymin.knife4j.proxy.ProxyHttpClientResponse;
import com.github.xiaoymin.knife4j.proxy.request.ServletProxyHttpClientRequest;
import com.github.xiaoymin.knife4j.proxy.response.DefaultProxyHttpclientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/9 9:08
 * @since:knife4j-aggregation-desktop 2.0
 */
public class ServletProxyHttpClient extends AbstractProxyHttpClient {
    
    Logger logger = LoggerFactory.getLogger(ServletProxyHttpClient.class);
    /**
     * 构造函数
     *
     * @param executorEnum 执行器类型
     * @param rootPath     根路径
     */
    public ServletProxyHttpClient(ExecutorEnum executorEnum, String rootPath) {
        super(executorEnum, rootPath);
    }
    
    @Override
    public ProxyHttpClientResponse proxy(ProxyHttpClientRequest request) {
        if (!(request instanceof ServletProxyHttpClientRequest)) {
            throw new IllegalArgumentException("Request not Servlet Instance");
        }
        ServletProxyHttpClientRequest proxyHttpClientRequest = (ServletProxyHttpClientRequest) request;
        boolean success = false;
        String message = "";
        try {
            RouteRequestContext routeContext = new RouteRequestContext();
            this.buildContext(routeContext, proxyHttpClientRequest);
            RouteResponse routeResponse = this.routeExecutor.executor(routeContext);
            writeResponseHeader(routeResponse, proxyHttpClientRequest.getResponse());
            writeBody(routeResponse, proxyHttpClientRequest.getResponse());
            success = true;
            message = "SUCCESS";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            message = e.getMessage();
        }
        return new DefaultProxyHttpclientResponse(success, message);
    }
    
    /**
     * 构建路由的请求上下文
     * @param routeRequestContext
     */
    protected void buildContext(RouteRequestContext routeRequestContext, ServletProxyHttpClientRequest proxyHttpClientRequest) throws IOException {
        HttpServletRequest servletRequest = proxyHttpClientRequest.getRequest();
        // 当前请求是否basic请求
        String basicHeader = servletRequest.getHeader(GlobalDesktopManager.ROUTE_PROXY_HEADER_BASIC_NAME);
        String code = servletRequest.getHeader(GlobalDesktopManager.ROUTE_PROXY_DOCUMENT_CODE);
        RouteRepository routeRepository = GlobalDesktopManager.me.repository(code);
        Assert.notNull(routeRepository, "请求数据非法");
        if (StrUtil.isNotBlank(code) && StrUtil.isNotBlank(basicHeader)) {
            BasicAuth basicAuth = routeRepository.getAuth(code, basicHeader);
            if (basicAuth != null) {
                // 增加Basic请求头
                routeRequestContext.addHeader("Authorization", RouteUtils.authorize(basicAuth.getUsername(), basicAuth.getPassword()));
            }
        }
        SwaggerRoute swaggerRoute = routeRepository.getRoute(code, servletRequest.getHeader(GlobalDesktopManager.ROUTE_PROXY_HEADER_NAME));
        // 有可能是Disk模式
        Assert.notNull(swaggerRoute, "Unsupported Debug");
        // String uri="http://knife4j.xiaominfo.com";
        String uri = swaggerRoute.getUri();
        String fromUri = servletRequest.getRequestURI();
        if (StrUtil.isNotBlank(swaggerRoute.getLocation())) {
            if (swaggerRoute.getLocation().indexOf(fromUri) == -1) {
                logger.debug("location:{},fromURI:{}", swaggerRoute.getLocation(), fromUri);
                // 当前路径是请求非获取OpenAPI实例路径地址，判断debugURL
                if (StrUtil.isNotBlank(swaggerRoute.getDebugUrl())) {
                    // 设置为调试地址
                    uri = swaggerRoute.getDebugUrl();
                }
            }
        }
        logger.debug("Debug URI:{},fromURI:{}", uri, fromUri);
        Assert.notEmpty(uri, "Unsupported Debug");
        String host = URI.create(uri).getHost();
        StringBuilder requestUrlBuilder = new StringBuilder();
        requestUrlBuilder.append(uri);
        // 替换项目的code路径
        String projectContextPath = "/" + code;
        fromUri = fromUri.replaceFirst(projectContextPath, "");
        // 判断servicePath
        if (StrUtil.isNotBlank(swaggerRoute.getServicePath()) && !StrUtil.equals(swaggerRoute.getServicePath(), GlobalDesktopManager.ROUTE_BASE_PATH)) {
            if (StrUtil.startWith(fromUri, swaggerRoute.getServicePath())) {
                // 实际在请求时,剔除servicePath,否则会造成404
                fromUri = fromUri.replaceFirst(swaggerRoute.getServicePath(), "");
            }
        }
        requestUrlBuilder.append(fromUri);
        String requestUrl = requestUrlBuilder.toString();
        logger.debug("Target Request Url:{},Method:{},Host:{}", requestUrl, servletRequest.getMethod(), host);
        routeRequestContext.setOriginalUri(fromUri);
        routeRequestContext.setUrl(requestUrl);
        routeRequestContext.setMethod(servletRequest.getMethod());
        
        List<String> headerValues = CollectionUtil.newArrayList(servletRequest.getHeaderNames());
        if (CollectionUtil.isNotEmpty(headerValues)) {
            logger.debug("Add Request Header Value");
            for (String key : headerValues) {
                if (!this.ignoreHeaders.contains(key.toLowerCase())) {
                    List<String> headerValue = CollectionUtil.newArrayList(servletRequest.getHeaders(key));
                    if (CollectionUtil.isNotEmpty(headerValue)) {
                        String headerStringValue=CollectionUtil.join(headerValue,StrUtil.COMMA);
                        logger.debug("{}:{}",key,headerStringValue);
                        routeRequestContext.addHeader(key, headerStringValue);
                    }
                }
            }
        }
        routeRequestContext.addHeader("Host", host);
        List<String> parameters = CollectionUtil.newArrayList(servletRequest.getParameterNames());
        if (CollectionUtil.isNotEmpty(parameters)) {
            logger.debug("Add Parameter Value");
            for (String name : parameters) {
                String value = servletRequest.getParameter(name);
                logger.debug("{}:{}", name, value);
                routeRequestContext.addParam(name, value);
            }
        }
        routeRequestContext.setRequestContent(servletRequest.getInputStream());
    }
    
    /**
     * Write响应头
     * @param routeResponse
     * @param response
     */
    protected void writeResponseHeader(RouteResponse routeResponse, HttpServletResponse response) {
        if (routeResponse != null) {
            if (CollectionUtil.isNotEmpty(routeResponse.getHeaders())) {
                for (HeaderWrapper headerWrapper : routeResponse.getHeaders()) {
                    response.addHeader(headerWrapper.getName(), headerWrapper.getValue());
                }
            }
            logger.info("ContentType:{},CharsetEncoding:{}", routeResponse.getContentType(), routeResponse.getCharsetEncoding());
            StringBuilder contentType = new StringBuilder();
            contentType.append(routeResponse.getContentType());
            if (routeResponse.getCharsetEncoding() != null) {
                contentType.append(";charset=").append(routeResponse.getCharsetEncoding().displayName());
            }
            response.addHeader("Content-Type", contentType.toString());
            if (routeResponse.getContentLength() > 0) {
                response.setContentLengthLong(routeResponse.getContentLength());
            }
        }
    }
    
    /**
     * 响应内容
     * @param routeResponse
     * @param response
     */
    protected void writeBody(RouteResponse routeResponse, HttpServletResponse response) throws IOException {
        if (routeResponse != null) {
            if (routeResponse.success()) {
                InputStream inputStream = routeResponse.getBody();
                if (inputStream != null) {
                    ServletUtil.write(response, inputStream);
                }
            } else {
                String text = routeResponse.text();
                if (StrUtil.isNotBlank(text)) {
                    ServletUtil.write(response, text, "text/plain");
                }
            }
            
        }
    }
}
