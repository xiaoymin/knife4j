/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/05 10:39
 * @since:knife4j-aggregation-desktop 1.0
 */
public class RouteDispatcher {
    /**
     * 请求头
     */
    public static final String ROUTE_PROXY_HEADER_NAME="knfie4j-gateway-request";
    public static final String ROUTE_PROXY_HEADER_BASIC_NAME="knife4j-gateway-basic-request";
    public static final String OPENAPI_GROUP_ENDPOINT="/swagger-resources";
    public static final String OPENAPI_GROUP_INSTANCE_ENDPOINT="/swagger-instance";
    public static final String ROUTE_BASE_PATH="/";
}
