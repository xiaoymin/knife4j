/*
 * Copyright 2017-2023 八一菜刀(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.gateway;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.github.xiaoymin.knife4j.common.model.WebJarFile;
import com.github.xiaoymin.knife4j.datasource.DocumentSessionHolder;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;
import com.github.xiaoymin.knife4j.gateway.context.GatewayContext;
import com.github.xiaoymin.knife4j.gateway.context.GatewayContextImpl;
import com.github.xiaoymin.knife4j.gateway.executor.ExecutorType;
import com.github.xiaoymin.knife4j.gateway.executor.GatewayClientExecutor;
import com.github.xiaoymin.knife4j.common.holder.WebJarHolder;
import com.github.xiaoymin.knife4j.common.utils.GatewayUtils;
import com.github.xiaoymin.knife4j.gateway.executor.response.GatewayClientResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 21:30
 * @since:knife4j-desktop
 */
@Slf4j
public class GatewayClientDispatcher implements Filter {
    
    final DocumentSessionHolder sessionHolder;
    final ExecutorType executorType;
    
    final WebJarHolder webJarHolder = new WebJarHolder();
    final GatewayContext gatewayContext = new GatewayContextImpl();
    final GatewayClientExecutor gatewayClientExecutor;
    
    public GatewayClientDispatcher(DocumentSessionHolder sessionHolder, ExecutorType executorType) {
        this.sessionHolder = sessionHolder;
        this.executorType = executorType;
        this.gatewayClientExecutor = ReflectUtil.newInstance(executorType.getExecutorClazz());
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String code = request.getHeader(DesktopConstants.ROUTE_PROXY_DOCUMENT_CODE);
        // 排除SwaggerResource以及SwaggerInstance路由访问
        String uri = request.getRequestURI();
        if (StrUtil.isNotBlank(code)) {
            log.info("------------------------------------------------------------------------------");
            log.info("project code:{}", code);
            if (!StrUtil.endWith(uri, DesktopConstants.OPENAPI_GROUP_ENDPOINT) && !StrUtil.endWith(uri, DesktopConstants.OPENAPI_GROUP_INSTANCE_ENDPOINT)) {
                // 路由请求
                log.info("Proxy URI:{}", uri);
                try {
                    GatewayClientResponse gatewayClientResponse = gatewayClientExecutor.executor(gatewayContext.buildContext(sessionHolder, request, response));
                    GatewayUtils.writeResponseStatus(gatewayClientResponse, response);
                    GatewayUtils.writeResponseHeader(gatewayClientResponse, response);
                    GatewayUtils.writeBody(gatewayClientResponse, response);
                } catch (Exception e) {
                    log.error("has Error:{}", e.getMessage());
                    log.error(e.getMessage(), e);
                    // write Default
                    GatewayUtils.writeDefault(request, response, e.getMessage());
                }
            } else {
                Optional<ServiceDocument> documentOptional = sessionHolder.getContext(code);
                if (documentOptional.isPresent()) {
                    ServiceDocument serviceDocument = documentOptional.get();
                    if (StrUtil.endWith(uri, DesktopConstants.OPENAPI_GROUP_ENDPOINT)) {
                        List<ServiceRoute> serviceRoutes = serviceDocument.getRoutes();
                        GatewayUtils.writeContentResponse(response, DesktopConstants.GSON.toJson(serviceRoutes));
                    } else if (StrUtil.endWith(uri, DesktopConstants.OPENAPI_GROUP_INSTANCE_ENDPOINT)) {
                        // 响应当前服务disk-实例
                        String group = request.getParameter("group");
                        Optional<ServiceRoute> routeOptional = serviceDocument.getRoute(group);
                        String content = routeOptional.isPresent() ? routeOptional.get().getContent() : "";
                        GatewayUtils.writeContentResponse(response, content);
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
            if (ReUtil.isMatch(DesktopConstants.WEBJAR_RESOURCE_PATTERN, uri)) {
                String webjarURL = ReUtil.get(DesktopConstants.WEBJAR_RESOURCE_PATTERN, uri, 2);
                Optional<WebJarFile> webJarFileOptional = this.webJarHolder.getWebJar(webjarURL);
                if (webJarFileOptional.isPresent()) {
                    WebJarFile webJarFile = webJarFileOptional.get();
                    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    JakartaServletUtil.write(response, webJarFile.getContent(), webJarFile.getMediaType().toString());
                } else {
                    log.info("webjars.{},real:{}", uri, webjarURL);
                    String resourcePath = "/META-INF/resources/" + webjarURL;
                    log.info("resourcePath:{}", resourcePath);
                    ClassPathResource classPathResource = new ClassPathResource(resourcePath);
                    if (classPathResource.exists()) {
                        log.info("exists:{}", classPathResource.exists());
                        Optional<MediaType> mediaTypeOptional = MediaTypeFactory.getMediaType(webjarURL);
                        MediaType mediaType = mediaTypeOptional.isPresent() ? mediaTypeOptional.get() : MediaType.TEXT_PLAIN;
                        log.info("mediaType:{}", mediaType);
                        String content = IoUtil.read(classPathResource.getInputStream(), StandardCharsets.UTF_8);
                        WebJarFile webJarFile = new WebJarFile();
                        webJarFile.setContent(content);
                        webJarFile.setMediaType(mediaType);
                        webJarFile.setWebjar(webjarURL);
                        this.webJarHolder.addFile(webjarURL, webJarFile);
                        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                        JakartaServletUtil.write(response, content, mediaType.toString());
                    } else {
                        filterChain.doFilter(servletRequest, servletResponse);
                    }
                }
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }
}
