/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.aggre.core.ext;

import com.github.xiaoymin.knife4j.aggre.spring.configuration.HttpConnectionSetting;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @since:knife4j 4.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/28 19:17
 */
public class ConnectionSettingHolder {
    private HttpConnectionSetting  connectionSetting;
    private volatile RequestConfig defaultRequestConfig;
    private volatile PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    private ConnectionSettingHolder(){}

    public static final ConnectionSettingHolder ME=new ConnectionSettingHolder();

    public synchronized HttpConnectionSetting getConnectionSetting() {
        if (this.connectionSetting==null){
            this.connectionSetting=new HttpConnectionSetting();
        }
        return connectionSetting;
    }

    public void setConnectionSetting(HttpConnectionSetting connectionSetting) {
        this.connectionSetting = connectionSetting;
    }

    public synchronized RequestConfig getDefaultRequestConfig() {
        if (this.defaultRequestConfig==null){
            this.defaultRequestConfig=RequestConfig.custom().setSocketTimeout(this.getConnectionSetting().getSocketTimeout()).setConnectTimeout(this.getConnectionSetting().getConnectTimeout()).build();
        }
        return defaultRequestConfig;
    }

    public synchronized PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
        if (this.poolingHttpClientConnectionManager==null){
            this.poolingHttpClientConnectionManager=new PoolingHttpClientConnectionManager();
            //将最大连接数增加到200
            this.poolingHttpClientConnectionManager.setMaxTotal(this.getConnectionSetting().getMaxConnectionTotal());
            //将每个路由基础的连接数增加到20
            this.poolingHttpClientConnectionManager.setDefaultMaxPerRoute(this.getConnectionSetting().getMaxPreRoute());
        }
        return poolingHttpClientConnectionManager;
    }
}
