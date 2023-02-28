/*
 * Copyright 2017-2023 八一菜刀(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.gateway.executor.apache.pool;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
public class PoolingConnectionManager {
    
    Logger logger = LoggerFactory.getLogger(PoolingConnectionManager.class);
    
    // Request retry handler
    private HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
        
        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (logger.isDebugEnabled()) {
                logger.debug("retryRequest-->");
            }
            if (executionCount > 5) {
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
    
    protected RequestConfig getRequestConfig() {
        return ConnectionSettingHolder.ME.getDefaultRequestConfig();
    }
    
    /***
     * 获取client连接
     * @return client链接对象
     */
    public CloseableHttpClient getClient() {
        return HttpClients.custom()
                .setConnectionManager(ConnectionSettingHolder.ME.getPoolingHttpClientConnectionManager())
                .setDefaultRequestConfig(ConnectionSettingHolder.ME.getDefaultRequestConfig())
                .setRetryHandler(retryHandler)
                .setConnectionManagerShared(true)
                .build();
    }
    
    public CloseableHttpClient getClient(CredentialsProvider credentialsProvider) {
        return HttpClients.custom()
                .setConnectionManager(ConnectionSettingHolder.ME.getPoolingHttpClientConnectionManager())
                .setDefaultRequestConfig(ConnectionSettingHolder.ME.getDefaultRequestConfig())
                .setRetryHandler(retryHandler)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setConnectionManagerShared(true)
                .build();
    }
    
}
