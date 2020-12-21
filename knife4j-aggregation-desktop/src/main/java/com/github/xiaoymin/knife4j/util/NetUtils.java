/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.util;

import io.undertow.server.HttpServerExchange;

import java.io.InputStream;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/21 14:20
 * @since:knife4j-aggregation-desktop 1.0
 */
public class NetUtils {

    public static InputStream getRequestInput(HttpServerExchange http){
        InputStream inputStream=null;
        try{
            inputStream=http.getInputStream();
        }catch (Exception e){
            //ignore..
        }
        return inputStream;
    }
}
