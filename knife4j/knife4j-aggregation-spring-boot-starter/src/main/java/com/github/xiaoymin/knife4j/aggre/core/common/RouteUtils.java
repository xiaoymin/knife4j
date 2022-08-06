/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.common;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/11/20 15:22
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 */
public class RouteUtils {


    public static String authorize(String username,String password){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("Basic ");
        String encodeStr=username+":"+password;
        try {
            stringBuilder.append(Base64.getEncoder().encodeToString(encodeStr.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            //ignore
        }
        return stringBuilder.toString();
    }
}
