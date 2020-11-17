/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.aggre.core.common.ExecutorEnum;
import com.github.xiaoymin.knife4j.aggre.core.executor.ApacheClientExecutor;
import com.github.xiaoymin.knife4j.aggre.core.executor.OkHttpClientExecutor;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.*;

/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:08
 */
public class RouteDispatcher {
    /**
     * 请求头
     */
    public static final String ROUTE_PROXY_HEADER_NAME="knfie4j-gateway-request";
    public static final String ROUTE_BASE_PATH="/";

    Logger logger= LoggerFactory.getLogger(RouteDispatcher.class);
    /**
     * 当前项目的contextPath
     */
    private String rootPath;

    private RouteRepository routeRepository;

    private RouteExecutor routeExecutor;

    private RouteCache<String, SwaggerRoute> routeCache;

    private Set<String> ignoreHeaders=new HashSet<>();

    public RouteDispatcher(RouteRepository routeRepository, RouteCache<String,SwaggerRoute> routeRouteCache, ExecutorEnum executorEnum,String rootPath){
        this.routeRepository=routeRepository;
        this.routeCache=routeRouteCache;
        this.rootPath=rootPath;
        initExecutor(executorEnum);
        ignoreHeaders.addAll(Arrays.asList(new String[]{
                "host","content-length",ROUTE_PROXY_HEADER_NAME,"Request-Origion"
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

    public boolean checkRoute(String header){
        if (StrUtil.isNotBlank(header)){
            SwaggerRoute swaggerRoute=routeCache.get(header);
            if (swaggerRoute!=null){
                return StrUtil.isNotBlank(swaggerRoute.getUri());
            }
            swaggerRoute=routeRepository.getRoute(header);
            if (swaggerRoute!=null){
                routeCache.put(header,swaggerRoute);
                return StrUtil.isNotBlank(swaggerRoute.getUri());
            }
        }
        return false;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response){
        try{
           RouteRequestContext routeContext=new RouteRequestContext();
           this.buildContext(routeContext,request);
           RouteResponse routeResponse=routeExecutor.executor(routeContext);
           writeResponseHeader(routeResponse,response);
           writeBody(routeResponse,response);
        }catch (Exception e){
           logger.error("has Error:{}",e.getMessage());
           logger.error(e.getMessage(),e);
           //write Default
            writeDefault(request,response,e.getMessage());
        }
    }

    protected void writeDefault(HttpServletRequest request,HttpServletResponse response,String errMsg){
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter printWriter=response.getWriter();
            Map<String,String> map= new HashMap<>();
            map.put("message",errMsg);
            map.put("code","500");
            map.put("path",request.getRequestURI());
            new JSONObject(map).write(printWriter);
            printWriter.close();
        } catch (IOException e) {
            //ignore
        }
    }

    /**
     * Write响应头
     * @param routeResponse
     * @param response
     */
    protected void writeResponseHeader(RouteResponse routeResponse,HttpServletResponse response){
        if (routeResponse!=null){
            if (CollectionUtil.isNotEmpty(routeResponse.getHeaders())){
                for (Map.Entry<String,String> entry:routeResponse.getHeaders().entrySet()){
                    //logger.info("{}:{}",entry.getKey(),entry.getValue());
                    if (!StrUtil.equalsIgnoreCase(entry.getKey(),"Transfer-Encoding")){
                        response.addHeader(entry.getKey(),entry.getValue());
                    }
                }
            }
            logger.info("响应类型:{},响应编码:{}",routeResponse.getContentType(),routeResponse.getCharsetEncoding());
            response.setContentType(routeResponse.getContentType());
            if (routeResponse.getContentLength()>0){
                response.setContentLengthLong(routeResponse.getContentLength());
            }
            response.setCharacterEncoding(routeResponse.getCharsetEncoding().displayName());
        }
    }

    /**
     * 响应内容
     * @param routeResponse
     * @param response
     */
    protected void writeBody(RouteResponse routeResponse,HttpServletResponse response) throws IOException {
        if (routeResponse!=null){
            if (routeResponse.success()){
                InputStream inputStream=routeResponse.getBody();
                if (inputStream!=null){
                    int read=-1;
                    byte[] bytes=new byte[1024*1024];
                    ServletOutputStream outputStream=response.getOutputStream();
                    while ((read=inputStream.read(bytes))!=-1){
                        outputStream.write(bytes,0,read);
                    }
                    IoUtil.close(inputStream);
                    IoUtil.close(outputStream);
                }
            }else{
                String text=routeResponse.text();
                if (StrUtil.isNotBlank(text)){
                    PrintWriter printWriter=response.getWriter();
                    printWriter.write(text);
                    printWriter.close();
                }
            }

        }
    }

    /**
     * 构建路由的请求上下文
     * @param routeRequestContext
     * @param request
     */
    protected void buildContext(RouteRequestContext routeRequestContext,HttpServletRequest request) throws IOException {
        SwaggerRoute swaggerRoute=getRoute(request.getHeader(ROUTE_PROXY_HEADER_NAME));
        //String uri="http://knife4j.xiaominfo.com";
        String uri=swaggerRoute.getUri();
        if (StrUtil.isBlank(uri)){
            throw new RuntimeException("Uri is Empty");
        }
        String host=URI.create(uri).getHost();
        String fromUri=request.getRequestURI();
        StringBuilder requestUrlBuilder=new StringBuilder();
        requestUrlBuilder.append(uri);
        //判断当前聚合项目的contextPath
        if (StrUtil.isNotBlank(this.rootPath)&&!StrUtil.equals(this.rootPath,ROUTE_BASE_PATH)){
            fromUri=fromUri.replaceFirst(this.rootPath,"");
        }
        //判断servicePath
        if (StrUtil.isNotBlank(swaggerRoute.getServicePath())&&!StrUtil.equals(swaggerRoute.getServicePath(),ROUTE_BASE_PATH)){
            if (StrUtil.startWith(fromUri,swaggerRoute.getServicePath())){
                //实际在请求时,剔除servicePath,否则会造成404
                fromUri=fromUri.replaceFirst(swaggerRoute.getServicePath(),"");
            }
        }
        requestUrlBuilder.append(fromUri);
        //String requestUrl=uri+fromUri;
        String requestUrl=requestUrlBuilder.toString();
        logger.info("目标请求Url:{},请求类型:{},Host:{}",requestUrl,request.getMethod(),host);
        routeRequestContext.setOriginalUri(fromUri);
        routeRequestContext.setUrl(requestUrl);
        routeRequestContext.setMethod(request.getMethod());
        Enumeration<String> enumeration=request.getHeaderNames();
        while (enumeration.hasMoreElements()){
            String key=enumeration.nextElement();
            String value=request.getHeader(key);
            if (!ignoreHeaders.contains(key.toLowerCase())){
                routeRequestContext.addHeader(key,value);
            }
        }
        routeRequestContext.addHeader("Host",host);
        Enumeration<String> params=request.getParameterNames();
        while (params.hasMoreElements()){
            String name=params.nextElement();
            String value=request.getParameter(name);
            //logger.info("param-name:{},value:{}",name,value);
            routeRequestContext.addParam(name,value);
        }
        routeRequestContext.setRequestContent(request.getInputStream());
    }

    public SwaggerRoute getRoute(String header){
        SwaggerRoute swaggerRoute=routeCache.get(header);
        if (swaggerRoute==null){
            swaggerRoute=routeRepository.getRoute(header);
            if (swaggerRoute!=null){
                routeCache.put(header,swaggerRoute);
            }
        }
        return swaggerRoute;
    }

    public List<SwaggerRoute> getRoutes(){
        return routeRepository.getRoutes();
    }
}
