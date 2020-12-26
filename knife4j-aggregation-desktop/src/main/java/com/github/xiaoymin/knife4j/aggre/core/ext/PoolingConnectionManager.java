/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.ext;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/***
 *
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/10/14 14:42
 */
public  class PoolingConnectionManager {

    Logger logger= LoggerFactory.getLogger(PoolingConnectionManager.class);

    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

    /***
     * 默认连接配置参数
     */
    private final RequestConfig defaultRequestConfig=RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();


    //Request retry handler
    private HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            logger.info("retryRequest-->");
            if (executionCount > 2) {
                return false;
            }
            if (exception instanceof InterruptedIOException) {
                // Timeout
                return false;
            }
            if (exception instanceof UnknownHostException) {
                // Unknown host
                return false;
            }
            if (exception instanceof ConnectTimeoutException) {
                // Connection refused
                return false;
            }
            if (exception instanceof SSLException) {
                // SSL handshake exception
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                // Retry if the request is considered idempotent
                return true;
            }

            return false;
        }
    };

    static{
        poolingHttpClientConnectionManager=new PoolingHttpClientConnectionManager();
        //将最大连接数增加到200
        poolingHttpClientConnectionManager.setMaxTotal(200);
        //将每个路由基础的连接数增加到20
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(20);
    }

    protected RequestConfig getRequestConfig(){
        return defaultRequestConfig;
    }

    /***
     * 获取client连接
     * @return
     */
    public CloseableHttpClient getClient(){
        return HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(defaultRequestConfig)
                .setRetryHandler(retryHandler)
                .setConnectionManagerShared(true)
                .build();
    }

    public CloseableHttpClient getClient(CredentialsProvider credentialsProvider){
        return HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(defaultRequestConfig)
                .setRetryHandler(retryHandler)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setConnectionManagerShared(true)
                .build();
    }

}
