/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
* Official Web Site: http://www.xiaominfo.com.
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
public abstract class AbstractProxyHttpClient implements ProxyHttpClient{

    protected RouteExecutor routeExecutor;

    protected Set<String> ignoreHeaders=new HashSet<>();
    /**
     * 当前项目的contextPath
     */
    protected String rootPath;


    /**
     * 构造函数
     * @param executorEnum 执行器类型
     * @param rootPath 根路径
     */
    public AbstractProxyHttpClient(ExecutorEnum executorEnum , String rootPath) {
        this.initExecutor(executorEnum);
        this.rootPath = rootPath;
        ignoreHeaders.addAll(Arrays.asList(new String[]{
                "host","content-length",
                GlobalDesktopManager.ROUTE_PROXY_DOCUMENT_CODE,
                GlobalDesktopManager.ROUTE_PROXY_HEADER_NAME,
                GlobalDesktopManager.ROUTE_PROXY_HEADER_BASIC_NAME,
                "Request-Origion"
        }));
    }

    private void initExecutor(ExecutorEnum executorEnum){
        if (executorEnum==null){
            throw new IllegalArgumentException("ExecutorEnum can not be empty");
        }
        switch (executorEnum){
            case APACHE:
                this.routeExecutor=new ApacheClientExecutor();
                break;
            case OKHTTP:
                this.routeExecutor=new OkHttpClientExecutor();
                break;
            default:
                throw new UnsupportedOperationException("UnSupported ExecutorType:"+executorEnum.name());
        }
    }


}
