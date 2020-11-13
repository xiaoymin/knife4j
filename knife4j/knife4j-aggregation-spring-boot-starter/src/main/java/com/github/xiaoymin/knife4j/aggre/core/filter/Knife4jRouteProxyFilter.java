/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.aggre.core.RouteDispatcher;
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
    private final String OpenAPI_GROUP_URL="/swagger-resources";
    private final RouteDispatcher routeDispatcher;

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
        if (routeDispatcher.checkRoute(request.getHeader(RouteDispatcher.ROUTE_PROXY_HEADER_NAME))){
            logger.info("Current Request:{}",request.getRequestURI());
            logger.info("当前请求是Proxy请求");
            routeDispatcher.execute(request,response);
            logger.info("执行完毕");
        }else{
            //go on
            String uri=request.getRequestURI();
            if (StrUtil.endWith(uri,OpenAPI_GROUP_URL)){
                //响应当前服务聚合结构
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter printWriter=response.getWriter();
                new JSONArray(routeDispatcher.getRoutes()).write(printWriter);
                printWriter.close();
            }else{
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }
    }
    @Override
    public void destroy() {

    }

}
