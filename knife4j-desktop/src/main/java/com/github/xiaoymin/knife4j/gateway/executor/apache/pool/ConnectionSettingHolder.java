/*
 * Copyright 2017-2022 ε«δΈθε(xiaoymin@foxmail.com)
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

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @since:knife4j 4.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/28 19:17
 */
public class ConnectionSettingHolder {
    
    private HttpConnectionSetting connectionSetting;
    private volatile RequestConfig defaultRequestConfig;
    private volatile PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    private ConnectionSettingHolder() {
    }
    
    public static final ConnectionSettingHolder ME = new ConnectionSettingHolder();
    
    public synchronized HttpConnectionSetting getConnectionSetting() {
        if (this.connectionSetting == null) {
            this.connectionSetting = new HttpConnectionSetting();
        }
        return connectionSetting;
    }
    
    public void setConnectionSetting(HttpConnectionSetting connectionSetting) {
        this.connectionSetting = connectionSetting;
    }
    
    public synchronized RequestConfig getDefaultRequestConfig() {
        if (this.defaultRequestConfig == null) {
            this.defaultRequestConfig =
                    RequestConfig.custom().setSocketTimeout(this.getConnectionSetting().getSocketTimeout()).setConnectTimeout(this.getConnectionSetting().getConnectTimeout()).build();
        }
        return defaultRequestConfig;
    }
    
    public synchronized PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
        if (this.poolingHttpClientConnectionManager == null) {
            this.poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
            // ε°ζε€§θΏζ₯ζ°ε’ε ε°200
            this.poolingHttpClientConnectionManager.setMaxTotal(this.getConnectionSetting().getMaxConnectionTotal());
            // ε°ζ―δΈͺθ·―η±εΊη‘ηθΏζ₯ζ°ε’ε ε°20
            this.poolingHttpClientConnectionManager.setDefaultMaxPerRoute(this.getConnectionSetting().getMaxPreRoute());
        }
        return poolingHttpClientConnectionManager;
    }
}
