/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.common;

import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;
import springfox.documentation.swagger.common.XForwardPrefixPathAdjuster;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.util.StringUtils.hasText;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromContextPath;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/01/14 16:29
 */
public class SwaggerBootstrapUiHostNameProvider {

    public SwaggerBootstrapUiHostNameProvider() {
        throw new UnsupportedOperationException();
    }

    public static UriComponents componentsFrom(
            HttpServletRequest request,
            String basePath) {

        ServletUriComponentsBuilder builder = fromServletMapping(request, basePath);

        UriComponents components = UriComponentsBuilder.fromHttpRequest(
                new ServletServerHttpRequest(request))
                .build();

        String host = components.getHost();
        if (!hasText(host)) {
            return builder.build();
        }

        builder.host(host);
        builder.port(components.getPort());

        return builder.build();
    }

    private static ServletUriComponentsBuilder fromServletMapping(
            HttpServletRequest request,
            String basePath) {

        ServletUriComponentsBuilder builder = fromContextPath(request);

        SwaggerBootstrapUiXForwardPrefixPathAdjuster adjuster = new SwaggerBootstrapUiXForwardPrefixPathAdjuster(request);
        builder.replacePath(adjuster.adjustedPath(basePath));
        if (hasText(new UrlPathHelper().getPathWithinServletMapping(request))) {
            builder.path(request.getServletPath());
        }

        return builder;
    }
}
