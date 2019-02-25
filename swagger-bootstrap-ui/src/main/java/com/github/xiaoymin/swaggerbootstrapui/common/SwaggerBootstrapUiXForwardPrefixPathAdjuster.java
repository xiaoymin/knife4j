/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.common;

import org.springframework.core.SpringVersion;
import springfox.documentation.service.PathAdjuster;

import javax.servlet.http.HttpServletRequest;

import static springfox.documentation.swagger.common.SpringVersionCapability.supportsXForwardPrefixHeader;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/01/14 16:44
 */
public class SwaggerBootstrapUiXForwardPrefixPathAdjuster implements PathAdjuster {

    static final String X_FORWARDED_PREFIX = "X-Forwarded-Prefix";

    private final HttpServletRequest request;

    public SwaggerBootstrapUiXForwardPrefixPathAdjuster(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String adjustedPath(String path) {
        String prefix = request.getHeader(X_FORWARDED_PREFIX);
        if (prefix != null) {
            if (!supportsXForwardPrefixHeader(SpringVersion.getVersion())) {
                return prefix + path;
            } else {
                return prefix;
            }
        } else {
            return path;
        }
    }
}
