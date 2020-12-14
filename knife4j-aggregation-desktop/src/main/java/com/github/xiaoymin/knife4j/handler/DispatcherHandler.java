/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.aggre.core.*;
import com.github.xiaoymin.knife4j.aggre.core.common.ExecutorEnum;
import com.github.xiaoymin.knife4j.aggre.core.common.RouteUtils;
import com.github.xiaoymin.knife4j.aggre.core.executor.ApacheClientExecutor;
import com.github.xiaoymin.knife4j.aggre.core.executor.OkHttpClientExecutor;
import com.github.xiaoymin.knife4j.aggre.core.pojo.BasicAuth;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.github.xiaoymin.knife4j.core.GlobalDesktopManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.RedirectHandler;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.*;

/**
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/12/05 9:57
 * @since:knife4j-aggregation-desktop 1.0
 */
public class DispatcherHandler implements HttpHandler {

    Logger logger= LoggerFactory.getLogger(DispatcherHandler.class);

    private RouteExecutor routeExecutor;
    private final Gson gson=new GsonBuilder().create();
    private Set<String> ignoreHeaders=new HashSet<>();
    /**
     * 当前项目的contextPath
     */
    private String rootPath;

    public DispatcherHandler(ExecutorEnum executorEnum,String rootPath){
        this.rootPath=rootPath;
        initExecutor(executorEnum);
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

    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String uri=exchange.getRequestURI();
        if (StrUtil.equalsIgnoreCase(uri,"/")){
            exchange.dispatch(new RedirectHandler("/doc.html"));
            return;
        }
        logger.info("requestURI:{}",uri);
        HeaderMap requestHeaderMap=exchange.getRequestHeaders();
        String code=requestHeaderMap.get(GlobalDesktopManager.ROUTE_PROXY_DOCUMENT_CODE,0);
        if (StrUtil.isNotBlank(code)){
            RouteRepository routeRepository=GlobalDesktopManager.me.repository(code);
            if (routeRepository==null){
                writeDefault(exchange,"Unsupported code:"+code);
                return;
            }
            if (StrUtil.endWith(uri, GlobalDesktopManager.OPENAPI_GROUP_ENDPOINT)) {
                //分组接口
                writeRouteResponse(exchange, gson.toJson(routeRepository.getRoutes(code)));
            }else if(StrUtil.endWith(uri,GlobalDesktopManager.OPENAPI_GROUP_INSTANCE_ENDPOINT)){
                Deque<String> group=exchange.getQueryParameters().get("group");
                String groupStr=group.getFirst();
                SwaggerRoute swaggerRoute=routeRepository.getRoute(code,groupStr);
                writeRouteResponse(exchange,swaggerRoute==null?"":swaggerRoute.getContent());
            }else{
                exeute(exchange);
            }
        }else{
            //不支持的方法
            writeDefault(exchange,"Unsupported Method");
        }
    }
    /**
     * 响应服务端的内容
     * @param response 响应流
     * @param content 内容
     * @throws IOException 异常
     */
    protected void writeRouteResponse(HttpServerExchange response,String content) throws IOException {
        response.getResponseHeaders().add(new HttpString("Content-Type"),"application/json;charset=UTF-8");
        response.getResponseSender().send(content);;
        response.endExchange();
    }

    /**
     * 执行请求
     * @param exchange
     */
    public void exeute(HttpServerExchange exchange){
        try{
            RouteRequestContext routeContext=new RouteRequestContext();
            this.buildContext(routeContext,exchange);
            RouteResponse routeResponse=routeExecutor.executor(routeContext);
            writeResponseHeader(routeResponse,exchange);
            writeBody(routeResponse,exchange);
        }catch (Exception e){
            logger.error("has Error:{}",e.getMessage());
            logger.error(e.getMessage(),e);
            //write Default
            writeDefault(exchange,e.getMessage());
        }
    }
    protected void writeDefault(HttpServerExchange server,String errMsg){
        server.getResponseHeaders().add(new HttpString("Content-Type"),"application/json;charset=UTF-8");
        try {
            Map<String,String> map= new HashMap<>();
            map.put("message",errMsg);
            map.put("code","500");
            map.put("path",server.getRequestURI());
            server.getResponseSender().send(new JSONObject(map).toString());
            server.endExchange();
        } catch (Exception e) {
            //ignore
        }
    }

    /**
     * Write响应头
     * @param routeResponse
     * @param response
     */
    protected void writeResponseHeader(RouteResponse routeResponse,HttpServerExchange response){
        if (routeResponse!=null){
            if (CollectionUtil.isNotEmpty(routeResponse.getHeaders())){
                for (Map.Entry<String,String> entry:routeResponse.getHeaders().entrySet()){
                    //logger.info("{}:{}",entry.getKey(),entry.getValue());
                    if (!StrUtil.equalsIgnoreCase(entry.getKey(),"Transfer-Encoding")){
                        response.getResponseHeaders().add(new HttpString(entry.getKey()),entry.getValue());
                        //response.addHeader(entry.getKey(),entry.getValue());
                    }
                }
            }
            logger.info("响应类型:{},响应编码:{}",routeResponse.getContentType(),routeResponse.getCharsetEncoding());
            StringBuilder contentType=new StringBuilder();
            contentType.append(routeResponse.getContentType());
            if (routeResponse.getCharsetEncoding()!=null){
                contentType.append(";charset=").append(routeResponse.getCharsetEncoding().displayName());
            }
            response.getResponseHeaders().add(new HttpString("Content-Type"),contentType.toString());
            //response.setContentType(routeResponse.getContentType());
            if (routeResponse.getContentLength()>0){
                response.setResponseContentLength(routeResponse.getContentLength());
                //response.setContentLengthLong(routeResponse.getContentLength());
            }
        }
    }

    /**
     * 响应内容
     * @param routeResponse
     * @param response
     */
    protected void writeBody(RouteResponse routeResponse, HttpServerExchange response) throws IOException {
        if (routeResponse!=null){
            if (routeResponse.success()){
                InputStream inputStream=routeResponse.getBody();
                if (inputStream!=null){
                    int read=-1;
                    byte[] bytes=new byte[1024*1024];
                    OutputStream outputStream=response.getOutputStream();
                    while ((read=inputStream.read(bytes))!=-1){
                        outputStream.write(bytes,0,read);
                    }
                    IoUtil.close(inputStream);
                    IoUtil.close(outputStream);
                }
            }else{
                String text=routeResponse.text();
                if (StrUtil.isNotBlank(text)){
                    response.getResponseSender().send(text);
                    response.endExchange();
                }
            }

        }
    }
    /**
     * 构建路由的请求上下文
     * @param routeRequestContext
     * @param http
     */
    protected void buildContext(RouteRequestContext routeRequestContext,HttpServerExchange http) throws IOException {
        //当前请求是否basic请求
        HeaderMap headerValues=http.getRequestHeaders();
        //String basicHeader=headerValues.get(GlobalDesktopManager.ROUTE_PROXY_HEADER_BASIC_NAME);
        String basicHeader=getHeader(headerValues,GlobalDesktopManager.ROUTE_PROXY_HEADER_BASIC_NAME);
        String code=getHeader(headerValues,GlobalDesktopManager.ROUTE_PROXY_DOCUMENT_CODE);
        RouteRepository routeRepository=GlobalDesktopManager.me.repository(code);
        if (StrUtil.isNotBlank(code)&&StrUtil.isNotBlank(basicHeader)){
            BasicAuth basicAuth=routeRepository.getAuth(code,basicHeader);
            if (basicAuth!=null){
                //增加Basic请求头
                routeRequestContext.addHeader("Authorization", RouteUtils.authorize(basicAuth.getUsername(),basicAuth.getPassword()));
            }
        }
        //SwaggerRoute swaggerRoute=getRoute(request.getHeader(ROUTE_PROXY_HEADER_NAME));
        SwaggerRoute swaggerRoute=routeRepository.getRoute(code,getHeader(headerValues,GlobalDesktopManager.ROUTE_PROXY_HEADER_NAME));
        //String uri="http://knife4j.xiaominfo.com";
        String uri=swaggerRoute.getUri();
        if (StrUtil.isBlank(uri)){
            throw new UnsupportedOperationException("Unsupported Debug");
        }
        String host= URI.create(uri).getHost();
        String fromUri=http.getRequestURI();
        StringBuilder requestUrlBuilder=new StringBuilder();
        requestUrlBuilder.append(uri);
        //判断当前聚合项目的contextPath
        /*if (StrUtil.isNotBlank(this.rootPath)&&!StrUtil.equals(this.rootPath,ROUTE_BASE_PATH)){
            fromUri=fromUri.replaceFirst(this.rootPath,"");
        }*/
        //判断servicePath
        if (StrUtil.isNotBlank(swaggerRoute.getServicePath())&&!StrUtil.equals(swaggerRoute.getServicePath(),GlobalDesktopManager.ROUTE_BASE_PATH)){
            if (StrUtil.startWith(fromUri,swaggerRoute.getServicePath())){
                //实际在请求时,剔除servicePath,否则会造成404
                fromUri=fromUri.replaceFirst(swaggerRoute.getServicePath(),"");
            }
        }
        requestUrlBuilder.append(fromUri);
        //String requestUrl=uri+fromUri;
        String requestUrl=requestUrlBuilder.toString();
        logger.info("目标请求Url:{},请求类型:{},Host:{}",requestUrl,http.getRequestMethod().toString(),host);
        routeRequestContext.setOriginalUri(fromUri);
        routeRequestContext.setUrl(requestUrl);
        routeRequestContext.setMethod(http.getRequestMethod().toString());
        if (CollectionUtil.isNotEmpty(headerValues)){
            for (HeaderValues hv:headerValues){
                String key=hv.getHeaderName().toString();
                if (CollectionUtil.isNotEmpty(hv)){
                    String value=hv.getFirst();
                    if (!ignoreHeaders.contains(key.toLowerCase())){
                        routeRequestContext.addHeader(key,value);
                    }
                }
            }
        }
        routeRequestContext.addHeader("Host",host);
        Map<String, Deque<String>> parameters= http.getQueryParameters();
        if (CollectionUtil.isNotEmpty(parameters)){
            for (Map.Entry<String,Deque<String>> dequeEntry:parameters.entrySet()){
                String name=dequeEntry.getKey();
                Deque<String> deque=dequeEntry.getValue();
                if (CollectionUtil.isNotEmpty(deque)){
                    String value=deque.getFirst();
                    logger.info("param-name:{},value:{}",name,value);
                    routeRequestContext.addParam(name,value);
                }
            }
        }
        routeRequestContext.setRequestContent(http.getInputStream());
    }


    private String getHeader(HeaderMap headerMap,String header){
        HeaderValues headerValues=headerMap.get(header);
        if (CollectionUtil.isNotEmpty(headerValues)){
            return headerValues.getFirst();
        }
        return null;
    }
}
