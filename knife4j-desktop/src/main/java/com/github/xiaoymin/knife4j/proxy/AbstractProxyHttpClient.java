/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.proxy;

import com.github.xiaoymin.knife4j.aggre.core.RouteExecutor;
import com.github.xiaoymin.knife4j.aggre.core.common.ExecutorEnum;
import com.github.xiaoymin.knife4j.aggre.core.executor.ApacheClientExecutor;
import com.github.xiaoymin.knife4j.aggre.core.executor.OkHttpClientExecutor;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/9 8:55
 * @since:knife4j-aggregation-desktop 2.0
 */
public abstract class AbstractProxyHttpClient implements ProxyHttpClient {
    
    protected RouteExecutor routeExecutor;
    
    protected Set<String> ignoreHeaders = new HashSet<>();
    /**
     * 当前项目的contextPath
     */
    protected String rootPath;
    
    /**
     * 构造函数
     * @param executorEnum 执行器类型
     * @param rootPath 根路径
     */
    public AbstractProxyHttpClient(ExecutorEnum executorEnum, String rootPath) {
        this.initExecutor(executorEnum);
        this.rootPath = rootPath;
        ignoreHeaders.addAll(Arrays.asList(new String[]{
                "host", "content-length",
                GlobalDesktopManager.ROUTE_PROXY_DOCUMENT_CODE,
                GlobalDesktopManager.ROUTE_PROXY_HEADER_NAME,
                GlobalDesktopManager.ROUTE_PROXY_HEADER_BASIC_NAME,
                "Request-Origion"
        }));
    }
    
    private void initExecutor(ExecutorEnum executorEnum) {
        if (executorEnum == null) {
            throw new IllegalArgumentException("ExecutorEnum can not be empty");
        }
        switch (executorEnum) {
            case APACHE:
                this.routeExecutor = new ApacheClientExecutor();
                break;
            case OKHTTP:
                this.routeExecutor = new OkHttpClientExecutor();
                break;
            default:
                throw new UnsupportedOperationException("UnSupported ExecutorType:" + executorEnum.name());
        }
    }
    
}
