/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.aggre.spring.configuration;

import com.github.xiaoymin.knife4j.aggre.core.ext.ConnectionSettingHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * https://gitee.com/xiaoym/knife4j/issues/I3UHV0
 * @since:knife4j 4.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/28 19:07
 */
@ConfigurationProperties(prefix = "knife4j.connection-setting")
public class HttpConnectionSetting implements InitializingBean {

    /**
     * 默认值
     */
    private final static int DEFAULT_TIMEOUT=10000;

    /**
     * SocketTimeout
     */
    private int socketTimeout=DEFAULT_TIMEOUT;

    /**
     * ConnectTimeout
     */
    private int connectTimeout=DEFAULT_TIMEOUT;

    /**
     * 最大连接上限数
     */
    private int maxConnectionTotal=200;

    /**
     * 单个路由基础连接数
     */
    private int maxPreRoute=20;


    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getMaxConnectionTotal() {
        return maxConnectionTotal;
    }

    public void setMaxConnectionTotal(int maxConnectionTotal) {
        this.maxConnectionTotal = maxConnectionTotal;
    }

    public int getMaxPreRoute() {
        return maxPreRoute;
    }

    public void setMaxPreRoute(int maxPreRoute) {
        this.maxPreRoute = maxPreRoute;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ConnectionSettingHolder.ME.setConnectionSetting(this);
    }
}
