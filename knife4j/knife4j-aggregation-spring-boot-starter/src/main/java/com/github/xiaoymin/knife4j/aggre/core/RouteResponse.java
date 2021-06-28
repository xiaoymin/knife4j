/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core;

import com.github.xiaoymin.knife4j.aggre.core.pojo.HeaderWrapper;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 21:58
 */
public interface RouteResponse {
    /**
     * 获取响应头
     * @return 响应头列表
     */
    List<HeaderWrapper> getHeaders();

    /**
     * 是否请求成功
     * @return 请求成功
     */
    boolean success();

    /**
     * 获取响应状态码
     * @return 响应状态码
     */
    int getStatusCode();

    /**
     * 获取响应类型
     * @return 响应类型
     */
    String getContentType();

    /**
     * 响应内容长度
     * @return 内容长度
     */
    Long getContentLength();

    /**
     * 获取encoding
     * @return 字符集编码
     */
    Charset getCharsetEncoding();

    /**
     * 响应实体
     * @return 响应流
     */
    InputStream getBody();

    /**
     * 获取text返回
     * @return 静态文本
     */
    String text();

}
