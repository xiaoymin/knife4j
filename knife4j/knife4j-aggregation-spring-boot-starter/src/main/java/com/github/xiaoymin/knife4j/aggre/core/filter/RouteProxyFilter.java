/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.aggre.core.filter;

import com.github.xiaoymin.knife4j.aggre.core.RouteDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 *
 * @since:route-proxy 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2020/05/29 20:06
 */
public class RouteProxyFilter implements Filter {

    private final RouteDispatcher routeDispatcher;

    Logger logger= LoggerFactory.getLogger(RouteProxyFilter.class);

    public RouteProxyFilter(RouteDispatcher routeDispatcher) {
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
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }
    @Override
    public void destroy() {

    }

}
