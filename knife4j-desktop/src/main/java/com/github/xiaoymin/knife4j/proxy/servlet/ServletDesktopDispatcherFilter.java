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


package com.github.xiaoymin.knife4j.proxy.servlet;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteDispatcher;
import com.github.xiaoymin.knife4j.aggre.core.RouteRepository;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import com.github.xiaoymin.knife4j.proxy.ProxyHttpClient;
import com.github.xiaoymin.knife4j.proxy.pojo.WebJarFile;
import com.github.xiaoymin.knife4j.proxy.request.ServletProxyHttpClientRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @since:knife4j-desktop
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/11 16:28
 */
@Slf4j
@AllArgsConstructor
public class ServletDesktopDispatcherFilter implements Filter {
    
    /**
     * 底层实现
     */
    final ProxyHttpClient proxyHttpClient;
    
    private final Gson gson = new GsonBuilder().create();
    
    /**
     * webjar缓存对象
     */
    private final Map<String, WebJarFile> webJarFileMap = new ConcurrentHashMap<>();
    /**
     * 正则表达式
     */
    private final String WEBJAR_RESOURCE_PATTERN = "/(.*?)/(webjars.*)";
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String code = request.getHeader(GlobalDesktopManager.ROUTE_PROXY_DOCUMENT_CODE);
        // 排除SwaggerResource以及SwaggerInstance路由访问
        String uri = request.getRequestURI();
        if (StrUtil.isNotBlank(code)) {
            log.info("project code:{}", code);
            if (!StrUtil.endWith(uri, GlobalDesktopManager.OPENAPI_GROUP_ENDPOINT) && !StrUtil.endWith(uri, GlobalDesktopManager.OPENAPI_GROUP_INSTANCE_ENDPOINT)) {
                // 路由请求
                log.info("Proxy URI:{}", uri);
                ServletProxyHttpClientRequest httpClientRequest = new ServletProxyHttpClientRequest(request, response);
                proxyHttpClient.proxy(httpClientRequest);
            } else {
                RouteRepository routeRepository = GlobalDesktopManager.me.repository(code);
                if (routeRepository != null) {
                    if (StrUtil.endWith(uri, GlobalDesktopManager.OPENAPI_GROUP_ENDPOINT)) {
                        List<SwaggerRoute> swaggerRoutes = routeRepository.getRoutes(code);
                        writeRouteResponse(response, gson.toJson(swaggerRoutes));
                    } else if (StrUtil.endWith(uri, RouteDispatcher.OPENAPI_GROUP_INSTANCE_ENDPOINT)) {
                        // 响应当前服务disk-实例
                        String group = request.getParameter("group");
                        SwaggerRoute swaggerRoute = routeRepository.getRoute(code, group);
                        writeRouteResponse(response, swaggerRoute == null ? "" : swaggerRoute.getContent());
                    } else {
                        // 路由请求
                        log.info("process URI:{}", uri);
                        filterChain.doFilter(servletRequest, servletResponse);
                    }
                } else {
                    log.info("project {} not exists", code);
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }
        } else {
            if (ReUtil.isMatch(WEBJAR_RESOURCE_PATTERN, uri)) {
                String webjarURL = ReUtil.get(WEBJAR_RESOURCE_PATTERN, uri, 2);
                if (webJarFileMap.containsKey(webjarURL)) {
                    WebJarFile webJarFile = webJarFileMap.get(webjarURL);
                    ServletUtil.write(response, webJarFile.getContent(), webJarFile.getMediaType().toString());
                } else {
                    log.info("webjars.{},real:{}", uri, webjarURL);
                    String resourcePath = "/META-INF/resources/" + webjarURL;
                    log.info("resourcePath:{}", resourcePath);
                    ClassPathResource classPathResource = new ClassPathResource(resourcePath);
                    if (classPathResource.exists()) {
                        log.info("exists:{}", classPathResource.exists());
                        Optional<MediaType> mediaTypeOptional = MediaTypeFactory.getMediaType(webjarURL);
                        MediaType mediaType = mediaTypeOptional.get();
                        log.info("mediaType:{}", mediaType);
                        String content = IoUtil.read(classPathResource.getInputStream(), StandardCharsets.UTF_8);
                        WebJarFile webJarFile = new WebJarFile();
                        webJarFile.setContent(content);
                        webJarFile.setMediaType(mediaType);
                        webJarFile.setWebjar(webjarURL);
                        webJarFileMap.put(webjarURL, webJarFile);
                        ServletUtil.write(response, content, mediaType.toString());
                    } else {
                        filterChain.doFilter(servletRequest, servletResponse);
                    }
                }
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
        
    }
    
    protected void writeRouteResponse(HttpServletResponse response, String content) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(content);
        printWriter.close();
    }
}
