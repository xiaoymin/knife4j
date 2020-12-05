/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 21:58
 */
public interface RouteResponse {
    /**
     * 获取响应头
     * @return
     */
    Map<String,String> getHeaders();

    /**
     * 是否请求成功
     * @return
     */
    boolean success();

    /**
     * 获取响应状态码
     * @return
     */
    int getStatusCode();

    /**
     * 获取响应类型
     * @return
     */
    String getContentType();

    /**
     * 响应内容长度
     * @return
     */
    Long getContentLength();

    /**
     * 获取encoding
     * @return
     */
    Charset getCharsetEncoding();

    /**
     * 响应实体
     * @return
     */
    InputStream getBody();

    /**
     * 获取text返回
     * @return
     */
    String text();

}
