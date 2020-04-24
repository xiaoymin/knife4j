/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.spring.common;

import com.github.xiaoymin.knife4j.spring.model.Version;
import org.springframework.core.SpringVersion;

import javax.servlet.http.HttpServletRequest;

/***
 *
 * @since:swagger-bootstrap-ui 1.9.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/01/14 16:44
 */
public class SwaggerBootstrapUiXForwardPrefixPathAdjuster {

    static final String X_FORWARDED_PREFIX = "X-Forwarded-Prefix";
    private static final Version FIVE_ZERO_ZERO = Version.parse("5.0.0.RELEASE");
    private static final Version FIVE_ZERO_FIVE = Version.parse("5.0.5.RELEASE");
    private static final Version FOUR_THREE_FIFTEEN = Version.parse("4.3.15.RELEASE");

    private final HttpServletRequest request;

    public SwaggerBootstrapUiXForwardPrefixPathAdjuster(HttpServletRequest request) {
        this.request = request;
    }

    public static boolean supportsXForwardPrefixHeader(String version) {
        Version parsed = Version.parse(version);
        return parsed.isGreaterThanOrEqualTo(FOUR_THREE_FIFTEEN) && parsed.isLessThan(FIVE_ZERO_ZERO) || parsed.isGreaterThanOrEqualTo(FIVE_ZERO_FIVE);
    }

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
