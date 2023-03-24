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


package com.github.xiaoymin.knife4j.gateway.context;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.common.lang.DesktopConstants;
import com.github.xiaoymin.knife4j.datasource.DocumentSessionHolder;
import com.github.xiaoymin.knife4j.datasource.model.ServiceDocument;
import com.github.xiaoymin.knife4j.datasource.model.ServiceRoute;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/17 22:21
 * @since:knife4j-desktop
 */
@Slf4j
public class GatewayContextImpl implements GatewayContext {
    
    /**
     * 转发忽略header
     */
    private final Set<String> ignoreHeaders = CollectionUtil.newHashSet(new String[]{
            "host",
            "content-length",
            DesktopConstants.ROUTE_PROXY_HEADER_NAME,
            DesktopConstants.ROUTE_PROXY_HEADER_BASIC_NAME,
            DesktopConstants.ROUTE_PROXY_DOCUMENT_CODE,
            "request-origion",
            "language"
    });
    
    @Override
    public GatewayRequestContext buildContext(DocumentSessionHolder sessionHolder, HttpServletRequest request, HttpServletResponse response) {
        GatewayRequestContext gatewayRequestContext = new GatewayRequestContext();
        Optional<ServiceDocument> documentOptional = sessionHolder.getContext(request.getHeader(DesktopConstants.ROUTE_PROXY_DOCUMENT_CODE));
        Assert.isTrue(documentOptional.isPresent(), "请求非法,项目不存在");
        ServiceDocument serviceDocument = documentOptional.get();
        Optional<ServiceRoute> routeOptional = serviceDocument.getRoute(request.getHeader(DesktopConstants.ROUTE_PROXY_HEADER_NAME));
        Assert.isTrue(routeOptional.isPresent(), "请求非法,分组不存在");
        ServiceRoute serviceRoute = routeOptional.get();
        // String uri="http://knife4j.xiaominfo.com";
        String uri = serviceRoute.getUri();
        String fromUri = request.getRequestURI();
        if (!StrUtil.equalsIgnoreCase(serviceDocument.getContextPath(), DesktopConstants.DESKTOP_ROOT_CONTEXT_DIR)) {
            fromUri = fromUri.replaceFirst(DesktopConstants.ROUTE_BASE_PATH + serviceDocument.getContextPath(), "");
            // 此处需要追加一个请求头basePath，因为父项目设置了context-path
            gatewayRequestContext.addHeader("X-Forwarded-Prefix", "/" + serviceDocument.getContextPath());
        }
        // 判断servicePath
        if (StrUtil.isNotBlank(serviceRoute.getServicePath()) && !StrUtil.equals(serviceRoute.getServicePath(),
                DesktopConstants.ROUTE_BASE_PATH)) {
            if (StrUtil.startWith(fromUri, serviceRoute.getServicePath())) {
                // 实际在请求时,剔除servicePath,否则会造成404
                fromUri = fromUri.replaceFirst(serviceRoute.getServicePath(), "");
            }
        }
        if (StrUtil.isNotBlank(serviceRoute.getLocation())) {
            if (serviceRoute.getLocation().indexOf(fromUri) == -1) {
                log.debug("location:{},fromURI:{}", serviceRoute.getLocation(), fromUri);
                // 当前路径是请求非获取OpenAPI实例路径地址，判断debugURL
                if (StrUtil.isNotBlank(serviceRoute.getDebugUrl())) {
                    // 设置为调试地址
                    uri = serviceRoute.getDebugUrl();
                }
            }
        }
        log.debug("Debug URI:{},fromURI:{}", uri, fromUri);
        Assert.notEmpty(uri, "Uri is Empty");
        StringBuilder requestUrlBuilder = new StringBuilder();
        requestUrlBuilder.append(uri);
        requestUrlBuilder.append(fromUri);
        // String requestUrl=uri+fromUri;
        String requestUrl = requestUrlBuilder.toString();
        String host = URI.create(uri).getHost();
        log.debug("target request Url:{},method:{},Host:{}", requestUrl, request.getMethod(), host);
        gatewayRequestContext.setOriginalUri(fromUri);
        gatewayRequestContext.setUrl(requestUrl);
        gatewayRequestContext.setMethod(request.getMethod());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            if (!ignoreHeaders.contains(key.toLowerCase())) {
                log.debug("header -> {}:{}", key, value);
                gatewayRequestContext.addHeader(key, value);
            }
        }
        gatewayRequestContext.addHeader("Host", host);
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String name = params.nextElement();
            String value = request.getParameter(name);
            log.debug("params -> {}:{}", name, value);
            gatewayRequestContext.addParam(name, value);
        }
        // 增加文件，sinc 2.0.9
        String contentType = request.getContentType();
        if (StrUtil.isNotBlank(contentType) && contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            try {
                Collection<Part> parts = request.getParts();
                if (CollectionUtil.isNotEmpty(parts)) {
                    Map<String, String> paramMap = gatewayRequestContext.getParams();
                    parts.forEach(part -> {
                        String key = part.getName();
                        if (!paramMap.containsKey(key)) {
                            log.debug("Part Name:{}", key);
                            gatewayRequestContext.addPart(part);
                        }
                    });
                }
            } catch (Exception e) {
                // ignore
                log.warn("get part error,message:" + e.getMessage());
            }
        }
        try {
            gatewayRequestContext.setRequestContent(request.getInputStream());
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
        return gatewayRequestContext;
    }
}
