/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.executor;

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteExecutor;
import com.github.xiaoymin.knife4j.aggre.core.RouteRequestContext;
import com.github.xiaoymin.knife4j.aggre.core.RouteResponse;
import com.github.xiaoymin.knife4j.aggre.core.ext.PoolingConnectionManager;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/***
 * 基于HttpClient组件的转发策略
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:35
 */
public class ApacheClientExecutor extends PoolingConnectionManager implements RouteExecutor {

    Logger logger= LoggerFactory.getLogger(ApacheClientExecutor.class);

    private HttpUriRequest buildRequest(RouteRequestContext routeContext){
        RequestBuilder builder = RequestBuilder.create(routeContext.getMethod());
        logger.info("ApacheClient Uri:{}",routeContext.getUrl());
        builder.setUri(routeContext.getUrl());
        if (CollectionUtil.isNotEmpty(routeContext.getHeaders())){
            //构建Header
            for (Map.Entry<String,String> entry:routeContext.getHeaders().entrySet()){
                builder.addHeader(entry.getKey(),entry.getValue());
            }
        }
        if (CollectionUtil.isNotEmpty(routeContext.getParams())){
            //构建Params
            for (Map.Entry<String,String> entry:routeContext.getParams().entrySet()){
                builder.addParameter(entry.getKey(),entry.getValue());
            }
        }
        if (routeContext.getRequestContent()!=null){
            //构建请求体
            BasicHttpEntity basicHttpEntity=new BasicHttpEntity();
            basicHttpEntity.setContent(routeContext.getRequestContent());
            // if the entity contentLength isn't set, transfer-encoding will be set
            // to chunked in org.apache.http.protocol.RequestContent. See gh-1042
            builder.setEntity(basicHttpEntity);
        }
        builder.setConfig(getRequestConfig());
        return builder.build();
    }

    @Override
    public RouteResponse executor(RouteRequestContext routeContext) {
        RouteResponse routeResponse=null;
        try {
            //判断当前接口是否需要执行basic
            CloseableHttpResponse closeableHttpResponse=getClient().execute(buildRequest(routeContext));
            routeResponse=new ApacheClientResponse(closeableHttpResponse);
        } catch (Exception e) {
            logger.error("Executor Failed,message:{}",e.getMessage());
            logger.error(e.getMessage(),e);
            routeResponse=new DefaultClientResponse(routeContext.getOriginalUri(),e.getMessage());
        }
        return routeResponse;
    }

}
