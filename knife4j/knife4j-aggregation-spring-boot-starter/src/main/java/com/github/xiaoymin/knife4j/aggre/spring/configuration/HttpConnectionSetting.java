/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
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


package com.github.xiaoymin.knife4j.aggre.spring.configuration;

import com.github.xiaoymin.knife4j.aggre.core.ext.ConnectionSettingHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * https://gitee.com/xiaoym/knife4j/issues/I3UHV0
 * @since  4.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/28 19:07
 */
@ConfigurationProperties(prefix = "knife4j.connection-setting")
public class HttpConnectionSetting implements InitializingBean {
    
    /**
     * 默认值
     */
    private final static int DEFAULT_TIMEOUT = 10000;
    
    /**
     * SocketTimeout
     */
    private int socketTimeout = DEFAULT_TIMEOUT;
    
    /**
     * ConnectTimeout
     */
    private int connectTimeout = DEFAULT_TIMEOUT;
    
    /**
     * 最大连接上限数
     */
    private int maxConnectionTotal = 200;
    
    /**
     * 单个路由基础连接数
     */
    private int maxPreRoute = 20;
    
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
