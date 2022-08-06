/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.filter;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.RouteDispatcher;
import com.github.xiaoymin.knife4j.aggre.core.pojo.SwaggerRoute;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/***
 *
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/10/29 20:06
 */
public class Knife4jRouteProxyFilter implements Filter {
    private final RouteDispatcher routeDispatcher;
    private final Gson gson=new GsonBuilder().create();

    Logger logger= LoggerFactory.getLogger(Knife4jRouteProxyFilter.class);

    public Knife4jRouteProxyFilter(RouteDispatcher routeDispatcher) {
        this.routeDispatcher = routeDispatcher;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        String uri=request.getRequestURI();
        if (routeDispatcher.checkRoute(request.getHeader(RouteDispatcher.ROUTE_PROXY_HEADER_NAME))){
            if(StrUtil.endWith(uri,RouteDispatcher.OPENAPI_GROUP_INSTANCE_ENDPOINT)){
                String group=request.getParameter("group");
                SwaggerRoute swaggerRoute=routeDispatcher.getRoute(group);
                writeRouteResponse(response,swaggerRoute==null?"":swaggerRoute.getContent());
                //响应当前服务disk-实例
            }else{
                if (logger.isDebugEnabled()){
                    logger.debug("Current Request URI:{},Proxy Request",uri);
                }
                routeDispatcher.execute(request,response);
            }
        }else{
            //go on
            if (StrUtil.endWith(uri,RouteDispatcher.OPENAPI_GROUP_ENDPOINT)){
                //响应当前服务聚合结构
                writeRouteResponse(response,gson.toJson(routeDispatcher.getRoutes()));
            }else if(StrUtil.endWith(uri,RouteDispatcher.OPENAPI_GROUP_INSTANCE_ENDPOINT)){
                String group=request.getParameter("group");
                SwaggerRoute swaggerRoute=routeDispatcher.getRoute(group);
                writeRouteResponse(response,swaggerRoute==null?"":swaggerRoute.getContent());
                //响应当前服务disk-实例
            }else{
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }
    }

    /**
     * 响应服务端的内容
     * @param response 响应流
     * @param content 内容
     * @throws IOException 异常
     */
    protected void writeRouteResponse(HttpServletResponse response,String content) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter printWriter=response.getWriter();
        printWriter.write(content);
        printWriter.close();
    }
    @Override
    public void destroy() {

    }

}
