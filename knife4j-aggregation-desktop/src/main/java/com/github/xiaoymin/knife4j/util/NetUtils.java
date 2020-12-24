/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.util;

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.undertow.io.Sender;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/21 14:20
 * @since:knife4j-aggregation-desktop 1.0
 */
public class NetUtils {

    static Logger logger= LoggerFactory.getLogger(NetUtils.class);

    static final Gson gson=new GsonBuilder().create();

    /**
     * 获取Header请求头
     * @param headerMap
     * @param header
     * @return
     */
    public static String getHeader(HeaderMap headerMap, String header){
        if (headerMap!=null){
            HeaderValues headerValues=headerMap.get(header);
            if (CollectionUtil.isNotEmpty(headerValues)){
                return headerValues.getFirst();
            }
        }
        return null;
    }
    /**
     * 401错误
     * @param exchange
     */
    public static void writeForbiddenCode(HttpServerExchange exchange){
        exchange.setStatusCode(401);
        exchange.getResponseHeaders().put(new HttpString("WWW-Authenticate"),"Basic realm=\"input Document Basic userName & password \"");
        exchange.getResponseSender().send("You do not have permission to access this resource");
    }

    /**
     * 响应Knife4jDesktop的错误信息
     * @param exchange
     * @param message 错误信息
     */
    public static void renderCommonJson(HttpServerExchange exchange,String message){
        Map<String,String> map= new HashMap<>();
        map.put("message",message);
        map.put("code","500");
        map.put("path",exchange.getRequestURI());
        renderJson(exchange,gson.toJson(map));
    }
    /**
     * 响应JSON信息
     * @param exchange
     * @param json
     */
    public static void renderJson(HttpServerExchange exchange,String json){
        exchange.getResponseHeaders().add(new HttpString("Content-Type"),"application/json;charset=UTF-8");
        Sender sender=exchange.getResponseSender();
        sender.send(json, GlobalDesktopManager.UTF_8);
    }

    /**
     * 获取请求Inputstream，捕获异常
     * @param http
     * @return
     */
    public static InputStream getRequestInput(HttpServerExchange http){
        InputStream inputStream=null;
        try{
            final ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            http.getRequestReceiver().receiveFullBytes((exchange, message) -> {
                try {
                    byteArrayOutputStream.write(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            //inputStream=http.getInputStream();
            inputStream=new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        }catch (Exception e){
            //ignore..
        }
        return inputStream;
    }

    public static String decodeBase64(String source){
        String decodeStr=null;
        if (source!=null){
            try {
                byte[] bytes= Base64.getDecoder().decode(source);
                decodeStr=new String(bytes);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }
        return decodeStr;
    }
}
