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


package com.github.xiaoymin.knife4j.aggre.core;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.aggre.core.common.ExecutorEnum;
import com.github.xiaoymin.knife4j.aggre.core.common.RouteUtils;
import com.github.xiaoymin.knife4j.aggre.core.executor.ApacheClientExecutor;
import com.github.xiaoymin.knife4j.aggre.core.executor.OkHttpClientExecutor;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.HeaderWrapper;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.*;

/***
 *
 * @since  2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:08
 */
public class RouteDispatcher {
    
    /**
     * header
     */
    public static final String ROUTE_PROXY_HEADER_NAME = "knfie4j-gateway-request";
    public static final String ROUTE_PROXY_HEADER_BASIC_NAME = "knife4j-gateway-basic-request";
    public static final String OPENAPI_GROUP_ENDPOINT = "/swagger-resources";
    public static final String OPENAPI_GROUP_INSTANCE_ENDPOINT = "/swagger-instance";
    public static final String ROUTE_BASE_PATH = "/";
    
    Logger logger = LoggerFactory.getLogger(RouteDispatcher.class);
    /**
     * current project contextPath
     */
    private String rootPath;
    
    private RouteRepository routeRepository;
    
    private RouteExecutor routeExecutor;
    
    private RouteCache<String, SwaggerRoute> routeCache;
    
    private Set<String> ignoreHeaders = new HashSet<>();
    
    public RouteDispatcher(RouteRepository routeRepository, RouteCache<String, SwaggerRoute> routeRouteCache,
                           ExecutorEnum executorEnum, String rootPath) {
        this.routeRepository = routeRepository;
        this.routeCache = routeRouteCache;
        this.rootPath = rootPath;
        initExecutor(executorEnum);
        ignoreHeaders.addAll(Arrays.asList(new String[]{
                "host", "content-length", ROUTE_PROXY_HEADER_NAME, ROUTE_PROXY_HEADER_BASIC_NAME, "Request-Origion", "language", "knife4j-gateway-code"
        }));
    }
    
    private void initExecutor(ExecutorEnum executorEnum) {
        if (executorEnum == null) {
            throw new IllegalArgumentException("ExecutorEnum can not be empty");
        }
        switch (executorEnum) {
            case APACHE:
                this.routeExecutor = new ApacheClientExecutor();
                break;
            case OKHTTP:
                this.routeExecutor = new OkHttpClientExecutor();
                break;
            default:
                throw new UnsupportedOperationException("UnSupported ExecutorType:" + executorEnum.name());
        }
    }
    
    public boolean checkRoute(String header) {
        if (StrUtil.isNotBlank(header)) {
            SwaggerRoute swaggerRoute = routeRepository.getRoute(header);
            if (swaggerRoute != null) {
                return StrUtil.isNotBlank(swaggerRoute.getUri());
            }
        }
        return false;
    }
    
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            RouteRequestContext routeContext = new RouteRequestContext();
            this.buildContext(routeContext, request);
            RouteResponse routeResponse = routeExecutor.executor(routeContext);
            writeResponseStatus(routeResponse, response);
            writeResponseHeader(routeResponse, response);
            writeBody(routeResponse, response);
        } catch (Exception e) {
            logger.error("has Error:{}", e.getMessage());
            logger.error(e.getMessage(), e);
            // write Default
            writeDefault(request, response, e.getMessage());
        }
    }
    
    protected void writeDefault(HttpServletRequest request, HttpServletResponse response, String errMsg) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter printWriter = response.getWriter();
            Map<String, String> map = new HashMap<>();
            map.put("message", errMsg);
            map.put("code", "500");
            map.put("path", request.getRequestURI());
            new JSONObject(map).write(printWriter);
            printWriter.close();
        } catch (IOException e) {
            // ignore
        }
    }
    
    /**
     * Write Http Status Code
     *
     * @param routeResponse routeResponse
     * @param response      response
     */
    protected void writeResponseStatus(RouteResponse routeResponse, HttpServletResponse response) {
        if (routeResponse != null) {
            response.setStatus(routeResponse.getStatusCode());
        }
    }
    
    /**
     * Write Response Header
     *
     * @param routeResponse route instance
     * @param response Servlet Response
     */
    protected void writeResponseHeader(RouteResponse routeResponse, HttpServletResponse response) {
        if (routeResponse != null) {
            if (CollectionUtil.isNotEmpty(routeResponse.getHeaders())) {
                for (HeaderWrapper header : routeResponse.getHeaders()) {
                    if (!StrUtil.equalsIgnoreCase(header.getName(), "Transfer-Encoding")) {
                        response.addHeader(header.getName(), header.getValue());
                    }
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Content-Type:{},Charset-Encoding:{}", routeResponse.getContentType(), routeResponse.getCharsetEncoding());
            }
            response.setContentType(routeResponse.getContentType());
            if (routeResponse.getContentLength() > 0) {
                response.setContentLengthLong(routeResponse.getContentLength());
            }
            response.setCharacterEncoding(routeResponse.getCharsetEncoding().displayName());
        }
    }
    
    /**
     * Write Body
     *
     * @param routeResponse route
     * @param response Servlet Response
     */
    protected void writeBody(RouteResponse routeResponse, HttpServletResponse response) throws IOException {
        if (routeResponse != null) {
            if (routeResponse.success()) {
                InputStream inputStream = routeResponse.getBody();
                if (inputStream != null) {
                    int read = -1;
                    byte[] bytes = new byte[1024 * 1024];
                    ServletOutputStream outputStream = response.getOutputStream();
                    while ((read = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }
                    IoUtil.close(inputStream);
                    IoUtil.close(outputStream);
                }
            } else {
                String text = routeResponse.text();
                if (StrUtil.isNotBlank(text)) {
                    PrintWriter printWriter = response.getWriter();
                    printWriter.write(text);
                    printWriter.close();
                }
            }
            
        }
    }
    
    /**
     * Build Context of Route
     * @param routeRequestContext Route Context
     * @param request Servlet Request
     */
    protected void buildContext(RouteRequestContext routeRequestContext, HttpServletRequest request) throws IOException {
        // Whether Basic
        String basicHeader = request.getHeader(ROUTE_PROXY_HEADER_BASIC_NAME);
        if (StrUtil.isNotBlank(basicHeader)) {
            BasicAuth basicAuth = routeRepository.getAuth(basicHeader);
            if (basicAuth != null) {
                // add Basic header
                routeRequestContext.addHeader("Authorization", RouteUtils.authorize(basicAuth.getUsername(),
                        basicAuth.getPassword()));
            }
        }
        SwaggerRoute swaggerRoute = getRoute(request.getHeader(ROUTE_PROXY_HEADER_NAME));
        // String uri="http://knife4j.xiaominfo.com";
        String uri = swaggerRoute.getUri();
        String fromUri = request.getRequestURI();
        // get project servlet.contextPath
        if (StrUtil.isNotBlank(this.rootPath) && !StrUtil.equals(this.rootPath, ROUTE_BASE_PATH)) {
            fromUri = fromUri.replaceFirst(this.rootPath, "");
            // 此处需要追加一个请求头basePath，因为父项目设置了context-path
            routeRequestContext.addHeader("X-Forwarded-Prefix", this.rootPath);
        }
        // 判断servicePath
        if (StrUtil.isNotBlank(swaggerRoute.getServicePath()) && !StrUtil.equals(swaggerRoute.getServicePath(),
                ROUTE_BASE_PATH)) {
            if (StrUtil.startWith(fromUri, swaggerRoute.getServicePath())) {
                // 实际在请求时,剔除servicePath,否则会造成404
                fromUri = fromUri.replaceFirst(swaggerRoute.getServicePath(), "");
            }
        }
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
        Assert.notEmpty(uri, "Uri is Empty");
        StringBuilder requestUrlBuilder = new StringBuilder();
        requestUrlBuilder.append(uri);
        requestUrlBuilder.append(fromUri);
        // String requestUrl=uri+fromUri;
        String requestUrl = requestUrlBuilder.toString();
        String host = URI.create(uri).getHost();
        if (logger.isDebugEnabled()) {
            logger.debug("目标请求Url:{},请求类型:{},Host:{}", requestUrl, request.getMethod(), host);
        }
        routeRequestContext.setOriginalUri(fromUri);
        routeRequestContext.setUrl(requestUrl);
        routeRequestContext.setMethod(request.getMethod());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            if (!ignoreHeaders.contains(key.toLowerCase())) {
                routeRequestContext.addHeader(key, value);
            }
        }
        routeRequestContext.addHeader("Host", host);
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String name = params.nextElement();
            String value = request.getParameter(name);
            // logger.info("param-name:{},value:{}",name,value);
            routeRequestContext.addParam(name, value);
        }
        // 增加文件，sinc 2.0.9
        String contentType = request.getContentType();
        if ((!StringUtils.isEmpty(contentType)) &&
                contentType.contains("multipart/form-data")) {
            try {
                Collection<Part> parts = request.getParts();
                if (CollectionUtil.isNotEmpty(parts)) {
                    Map<String, String> paramMap = routeRequestContext.getParams();
                    parts.forEach(part -> {
                        String key = part.getName();
                        if (!paramMap.containsKey(key)) {
                            routeRequestContext.addPart(part);
                        }
                    });
                }
            } catch (ServletException e) {
                // ignore
                logger.warn("get part error,message:" + e.getMessage());
            }
        }
        routeRequestContext.setRequestContent(request.getInputStream());
    }
    
    public SwaggerRoute getRoute(String header) {
        // 去除缓存机制，由于Eureka以及Nacos设立了心跳检测机制，服务在多节点部署时，节点ip可能存在变化,导致调试最终转发给已经下线的服务
        // since 2.0.9
        SwaggerRoute swaggerRoute = routeRepository.getRoute(header);
        return swaggerRoute;
    }
    
    public List<SwaggerRoute> getRoutes() {
        return routeRepository.getRoutes();
    }
    
    public String getRootPath() {
        return rootPath;
    }
}
