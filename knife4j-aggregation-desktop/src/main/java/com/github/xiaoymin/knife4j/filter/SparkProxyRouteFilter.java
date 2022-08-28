/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
* Official Web Site: http://www.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.filter;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.common.ExecutorEnum;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import com.github.xiaoymin.knife4j.proxy.ProxyHttpClient;
import com.github.xiaoymin.knife4j.proxy.request.SparkProxyHttpClientRequest;
import com.github.xiaoymin.knife4j.proxy.spark.SparkProxyHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/5/9 9:13
 * @since:knife4j-aggregation-desktop 2.0
 */
public class SparkProxyRouteFilter implements Filter {

    Logger logger= LoggerFactory.getLogger(SparkProxyRouteFilter.class);

    private final String rootPath;

    private final ProxyHttpClient proxyHttpClient;

    public SparkProxyRouteFilter(String rootPath) {
        this.rootPath = rootPath;
        this.proxyHttpClient=new SparkProxyHttpClient(ExecutorEnum.APACHE,this.rootPath);
    }

    @Override
    public void handle(Request request, Response response) throws Exception {
        //排除SwaggerResource以及SwaggerInstance路由访问
        String uri=request.uri();
        if (!StrUtil.endWith(uri, GlobalDesktopManager.OPENAPI_GROUP_ENDPOINT)&&!StrUtil.endWith(uri,GlobalDesktopManager.OPENAPI_GROUP_INSTANCE_ENDPOINT)) {
            //路由请求
            logger.info("Proxy URI:{}",uri);
            this.proxyHttpClient.proxy(new SparkProxyHttpClientRequest(request,response));
        }
    }
}
