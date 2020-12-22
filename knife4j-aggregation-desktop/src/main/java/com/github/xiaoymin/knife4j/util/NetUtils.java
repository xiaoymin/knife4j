/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.util;

import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Base64;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/21 14:20
 * @since:knife4j-aggregation-desktop 1.0
 */
public class NetUtils {
    static Logger logger= LoggerFactory.getLogger(NetUtils.class);

    public static InputStream getRequestInput(HttpServerExchange http){
        InputStream inputStream=null;
        try{
            inputStream=http.getInputStream();
        }catch (Exception e){
            //ignore..
        }
        return inputStream;
    }
    public static String decodeBase64(String source){
        String decodeStr=null;
        if (source!=null){
            //BASE64Decoder decoder=new BASE64Decoder();
            try {
                //byte[] bytes=decoder.decodeBuffer(source);
                byte[] bytes= Base64.getDecoder().decode(source);
                decodeStr=new String(bytes);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }
        return decodeStr;
    }
}
