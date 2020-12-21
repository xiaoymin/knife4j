/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.handler;

import cn.hutool.core.io.IoUtil;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/21 20:38
 * @since:knife4j-aggregation-desktop 1.0
 */
public class BlockingResponseHandler implements HttpHandler {

    Logger logger= LoggerFactory.getLogger(BlockingResponseHandler.class);

    private final InputStream inputStream;

    public BlockingResponseHandler(InputStream inputStream) {
        this.inputStream = inputStream;
    }


    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        int read=-1;
        logger.info("BlockingResponseHandler,response Uri:{}",exchange.getRequestURI());
        exchange.startBlocking();
        byte[] bytes=new byte[1024*1024];
        OutputStream outputStream=exchange.getOutputStream();
        while ((read=inputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,read);
        }
        IoUtil.close(inputStream);
        IoUtil.close(outputStream);
    }
}
